package it.faustobe.santibailor.data.repository;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import it.faustobe.santibailor.data.AppDatabase;
import it.faustobe.santibailor.data.local.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.TipoRicorrenzaDao;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.local.entities.TipoRicorrenzaEntity;
import it.faustobe.santibailor.data.mapper.RicorrenzaMapper;
import it.faustobe.santibailor.data.remote.FirebaseRemoteDataSource;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.util.FirebaseErrorHandler;

public class RicorrenzaRepository {
    private final RicorrenzaDao ricorrenzaDao;
    private final FirebaseRemoteDataSource remoteDataSource;
    private final FirebaseFirestore dbf;
    private final FirebaseStorage storage;
    private final TipoRicorrenzaDao tipoRicorrenzaDao;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    private static final String TAG = "RicorrenzaRepository";

    @Inject
    public RicorrenzaRepository(Application application,
                                FirebaseRemoteDataSource remoteDataSource,
                                FirebaseFirestore dbf,
                                FirebaseStorage storage) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.ricorrenzaDao = db.ricorrenzaDao();
        this.tipoRicorrenzaDao = db.tipoRicorrenzaDao();
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.remoteDataSource = remoteDataSource;
        this.dbf = dbf;
        this.storage = storage;
    }

    // Metodi ausiliari per la costruzione delle query
    private void addNomeCondition(StringBuilder queryBuilder, List<Object> params, String nome) {
        if (!TextUtils.isEmpty(nome)) {
            queryBuilder.append(" AND r.santo LIKE ?");
            params.add("%" + nome + "%");
        }
    }

    private void addTipoCondition(StringBuilder queryBuilder, List<Object> params, Integer tipo) {
        if (tipo != null && tipo != 0) {
            queryBuilder.append(" AND r.tipo_ricorrenza_id = ?");
            params.add(tipo);
        }
    }

    private void addDataCondition(StringBuilder queryBuilder, List<Object> params, String dataInizio, String dataFine) {
        if (!TextUtils.isEmpty(dataInizio) || !TextUtils.isEmpty(dataFine)) {
            queryBuilder.append(" AND (");
            addDataInizioCondition(queryBuilder, params, dataInizio);
            if (!TextUtils.isEmpty(dataInizio) && !TextUtils.isEmpty(dataFine)) {
                queryBuilder.append(" AND ");
            }
            addDataFineCondition(queryBuilder, params, dataFine);
            queryBuilder.append(")");
        }
    }

    private void addDataInizioCondition(StringBuilder queryBuilder, List<Object> params, String dataInizio) {
        if (!TextUtils.isEmpty(dataInizio)) {
            String[] parts = dataInizio.split("/");
            if (parts.length == 2) {
                int giorno = Integer.parseInt(parts[0]);
                int mese = Integer.parseInt(parts[1]);
                queryBuilder.append("(r.id_mese > ? OR (r.id_mese = ? AND r.giorno_del_mese >= ?))");
                params.add(mese);
                params.add(mese);
                params.add(giorno);
            }
        }
    }

    private void addDataFineCondition(StringBuilder queryBuilder, List<Object> params, String dataFine) {
        if (!TextUtils.isEmpty(dataFine)) {
            String[] parts = dataFine.split("/");
            if (parts.length == 2) {
                int giorno = Integer.parseInt(parts[0]);
                int mese = Integer.parseInt(parts[1]);
                queryBuilder.append("(r.id_mese < ? OR (r.id_mese = ? AND r.giorno_del_mese <= ?))");
                params.add(mese);
                params.add(mese);
                params.add(giorno);
            }
        }
    }

    public LiveData<Ricorrenza> getRicorrenza(String id) {
        Log.d(TAG, "Getting ricorrenza for ID: " + id);
        MediatorLiveData<Ricorrenza> result = new MediatorLiveData<>();

        // Aggiungi la fonte dati locale
        result.addSource(ricorrenzaDao.getRicorrenzaById(Integer.parseInt(id)), localData -> {
            if (localData != null) {
                result.setValue(RicorrenzaMapper.toDomain(localData));
            }
        });

        // Aggiungi la fonte dati remota
        remoteDataSource.getBio(id).addOnSuccessListener(remoteBio -> {
            if (remoteBio != null) {
                Ricorrenza currentValue = result.getValue();
                if (currentValue != null) {
                    currentValue.setBio(remoteBio);
                    result.setValue(currentValue);
                }
                // Aggiorna il dato locale
                executorService.execute(() -> ricorrenzaDao.updateBio(Integer.parseInt(id), remoteBio));
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error getting remote bio for ID: " + id, e);
            FirebaseErrorHandler.handleException(e);
        });

        return result;
    }

    public Task<DocumentSnapshot> getRicorrenzaBio(String id) {
        return dbf.collection("ricorrenze").document(id).get();
    }

    public Task<Void> updateRicorrenzaBio(String id, String bio) {
        return dbf.collection("ricorrenze").document(id).update("bio", bio);
    }

    public Task<Uri> getImageUrl(String id, boolean isThumb) {
        String fileName = id + (isThumb ? "_thumb" : "") + ".webp";
        return storage.getReference().child("images/" + fileName).getDownloadUrl();
    }

    public Task<Uri> uploadImage(String id, byte[] imageData) {
        Log.d(TAG, "Uploading image for ID: " + id);
        return remoteDataSource.uploadImage(id, imageData)
                .addOnSuccessListener(uri -> {
                    Log.d(TAG, "Image uploaded successfully. URL: " + uri);
                    executorService.execute(() -> ricorrenzaDao.updateImageUrlById(Integer.parseInt(id), uri.toString()));
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error uploading image for ID: " + id, e);
                    FirebaseErrorHandler.handleException(e);
                });
    }

    public Task<byte[]> downloadImage(String id) {
        return remoteDataSource.downloadImage(id);
    }

    public Task<Void> updateBio(String id, String bio) {
        return remoteDataSource.updateBio(id, bio);
    }

    public Task<String> getBio(String id) {
        return remoteDataSource.getBio(id);
    }

    public void syncWithFirebase(String id) {
        Log.d(TAG, "Syncing with Firebase for ID: " + id);
        dbf.collection("ricorrenze").document(id).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String remoteBio = document.getString("bio");
                        executorService.execute(() -> {
                            ricorrenzaDao.updateBio(Integer.parseInt(id), remoteBio);
                            Log.d(TAG, "Sync completed for ID: " + id);
                        });
                    } else {
                        Log.w(TAG, "No remote data found for ID: " + id);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error syncing with Firebase for ID: " + id, e);
                    FirebaseErrorHandler.handleException(e);
                });
    }

    public void updateImageUrl(int ricorrenzaId, String newUrl) {
        executorService.execute(() -> ricorrenzaDao.updateImageUrlById(ricorrenzaId, newUrl));
    }

    public void updateImageUrl(String oldUrl, String newUrl) {
        executorService.execute(() -> ricorrenzaDao.updateImageUrlByOldUrl(oldUrl, newUrl));
    }

    public List<Ricorrenza> getAllRicorrenzeWithImages() {
        // Utilizziamo una CountDownLatch per gestire l'operazione asincrona
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Ricorrenza> result = new ArrayList<>();

        executorService.execute(() -> {
            List<RicorrenzaEntity> entities = ricorrenzaDao.getAllRicorrenzeWithImages();
            result.addAll(entities.stream()
                    .map(RicorrenzaMapper::toDomain)
                    .collect(Collectors.toList()));
            latch.countDown();
        });

        try {
            // Attendiamo il completamento dell'operazione per un massimo di 5 secondi
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e("RicorrenzaRepository", "Interrupted while waiting for getAllRicorrenzeWithImages", e);
            Thread.currentThread().interrupt();
        }

        return result;
    }

    public List<String> getAllImageUrls() {
        Log.d("RicorrenzaRepository", "Fetching all image URLs");
        List<String> urls = ricorrenzaDao.getAllImageUrls();
        Log.d("RicorrenzaRepository", "Found " + urls.size() + " image URLs");
        return urls;
    }

    public List<String> getImageUrlsInUse() {
        return ricorrenzaDao.getImageUrlsInUse();
    }

    public LiveData<List<Ricorrenza>> getRicorrenzeReligiose(int giorno, int mese) {
        return Transformations.map(
                ricorrenzaDao.getRicorrenzeDelGiornoPerTipo(giorno, mese, TipoRicorrenzaEntity.RELIGIOSA),
                entities -> entities.stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList())
        );
    }

    public LiveData<List<Ricorrenza>> getRicorrenzeLaiche(int giorno, int mese) {
        return Transformations.map(
                ricorrenzaDao.getRicorrenzeDelGiornoPerTipo(giorno, mese, TipoRicorrenzaEntity.LAICA),
                entities -> entities.stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList())
        );
    }

    public List<Ricorrenza> getRicorrenzeDelGiornoPaginate(int giorno, int mese, int offset, int limit) {
        return ricorrenzaDao.getRicorrenzeDelGiornoPaginate(giorno, mese, offset, limit)
                .stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList());
    }

    public List<Ricorrenza> getRicorrenzeReligiosePaginate(int giorno, int mese, int offset, int limit) {
        return ricorrenzaDao.getRicorrenzeDelGiornoPerTipoPaginate(giorno, mese, TipoRicorrenzaEntity.RELIGIOSA, offset, limit)
                .stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList());
    }

    public List<Ricorrenza> getRicorrenzeLaichePaginate(int giorno, int mese, int offset, int limit) {
        // Usa la nuova query che include ENTRAMBE Laiche (2) E Personali (3)
        return ricorrenzaDao.getRicorrenzeLaicheEPersonaliPaginate(giorno, mese, offset, limit)
                .stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList());
    }

    public int getCountRicorrenzeDelGiorno(int giorno, int mese) {
        return ricorrenzaDao.getCountRicorrenzeDelGiorno(giorno, mese);
    }

    public int getCountRicorrenzeReligiose(int giorno, int mese) {
        return ricorrenzaDao.getCountRicorrenzeDelGiornoPerTipo(giorno, mese, TipoRicorrenzaEntity.RELIGIOSA);
    }

    public int getCountRicorrenzeLaiche(int giorno, int mese) {
        return ricorrenzaDao.getCountRicorrenzeDelGiornoPerTipo(giorno, mese, TipoRicorrenzaEntity.LAICA);
    }

    public void update(Ricorrenza ricorrenza) {
        executorService.execute(() -> ricorrenzaDao.update(RicorrenzaMapper.toEntity(ricorrenza)));
    }

    public void delete(Ricorrenza ricorrenza) {
        executorService.execute(() -> ricorrenzaDao.delete(RicorrenzaMapper.toEntity(ricorrenza)));
    }

    public LiveData<List<Ricorrenza>> getRicorrenzeDelGiorno(int giorno, int mese) {
        return Transformations.map(
                ricorrenzaDao.getRicorrenzeDelGiorno(giorno, mese),
                entities -> entities.stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList())
        );
    }

    public List<Ricorrenza> getRicorrenzeDelGiornoSync(int giorno, int mese) {
        return ricorrenzaDao.getRicorrenzeDelGiornoSync(giorno, mese)
                .stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList());
    }

    public int contaRicorrenzePerGiornoMese(int giorno, int mese) {
        return ricorrenzaDao.contaRicorrenzePerGiornoMese(giorno, mese);
    }

    public LiveData<List<Ricorrenza>> getRicorrenzePerGiornoMese(int giorno, int mese) {
        return Transformations.map(
                ricorrenzaDao.getRicorrenzePerGiornoMeseLiveData(giorno, mese),
                entities -> entities.stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList())
        );
    }

    public long insert(Ricorrenza ricorrenza) {
        RicorrenzaEntity entity = RicorrenzaMapper.toEntity(ricorrenza);
        entity.setId(0);  // Assicuriamoci che l'ID non sia impostato prima dell'inserimento
        return ricorrenzaDao.insert(entity);
    }

    public LiveData<Ricorrenza> getRicorrenzaById(int id) {
        return Transformations.map(
                ricorrenzaDao.getRicorrenzaById(id),
                RicorrenzaMapper::toDomain
        );
    }

    public LiveData<List<Ricorrenza>> ricercaAvanzata(String nome, Integer tipo, String dataInizio, String dataFine) {
        Log.d(TAG, "ricercaAvanzata: nome=" + nome + ", tipo=" + tipo +
                ", dataInizio=" + dataInizio + ", dataFine=" + dataFine);

        Integer meseInizio = getMonthFromDate(dataInizio);
        Integer meseFine = getMonthFromDate(dataFine);
        Integer giornoInizio = getDayFromDate(dataInizio);
        Integer giornoFine = getDayFromDate(dataFine);

        Log.d(TAG, "Parametri query: meseInizio=" + meseInizio + ", meseFine=" + meseFine +
                ", giornoInizio=" + giornoInizio + ", giornoFine=" + giornoFine);

        return Transformations.map(
                ricorrenzaDao.ricercaAvanzata(nome, tipo, meseInizio, meseFine, giornoInizio, giornoFine),
                result -> {
                    Log.d(TAG, "Risultati ricerca avanzata: " + (result != null ? result.size() : 0));
                    return result != null ? result.stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList()) : new ArrayList<>();
                }
        );
    }

    public List<Ricorrenza> ricercaAvanzataPaginata(String nome, Integer tipo, String dataInizio, String dataFine, int limit, int offset) {
        StringBuilder queryBuilder = new StringBuilder("SELECT r.*, t.tipo AS tipo_nome FROM santi r LEFT JOIN tipo_ricorrenza t ON r.id_tipo = t.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        addNomeCondition(queryBuilder, params, nome);
        addTipoCondition(queryBuilder, params, tipo);
        addDataCondition(queryBuilder, params, dataInizio, dataFine);

        queryBuilder.append(" ORDER BY r.id_mese, r.giorno_del_mese LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        String query = queryBuilder.toString();
        Log.d(TAG, "query in RicercaAvanzataPaginata -> " + query);
        List<RicorrenzaEntity> entities = ricorrenzaDao.eseguiRicercaAvanzata(new SimpleSQLiteQuery(query, params.toArray()));
        return entities.stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList());
    }

    public void updateImageUrlByOldUrl(String oldUrl, String newUrl) {
        executorService.execute(() -> {
            RicorrenzaEntity ricorrenza = ricorrenzaDao.getRicorrenzaByImageUrl(oldUrl);
            if (ricorrenza != null) {
                ricorrenza.setImageUrl(newUrl);
                ricorrenzaDao.update(ricorrenza);
            }
        });
    }

    public int contaTotaleRisultati(String nome, Integer tipo, String dataInizio, String dataFine) {
        int meseInizio = 1, meseFine = 12, giornoInizio = 1, giornoFine = 31;

        if (!TextUtils.isEmpty(dataInizio)) {
            String[] parts = dataInizio.split("/");
            if (parts.length == 2) {
                giornoInizio = Integer.parseInt(parts[0]);
                meseInizio = Integer.parseInt(parts[1]);
            }
        }

        if (!TextUtils.isEmpty(dataFine)) {
            String[] parts = dataFine.split("/");
            if (parts.length == 2) {
                giornoFine = Integer.parseInt(parts[0]);
                meseFine = Integer.parseInt(parts[1]);
            }
        }

        return ricorrenzaDao.contaRisultatiRicercaAvanzata(nome, tipo, meseInizio, meseFine, giornoInizio, giornoFine);
    }

    public int getTotalDatabaseItemCount() {
        return ricorrenzaDao.getTotalItemCount();
    }

    public List<TipoRicorrenza> getAllTipiRicorrenza() {
        return tipoRicorrenzaDao.getAllTipiRicorrenza()
                .stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList());
    }

    private Integer getMonthFromDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        String[] parts = date.split("/");
        if (parts.length == 2) {
            return Integer.parseInt(parts[1]);
        }
        return null;
    }

    private Integer getDayFromDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        String[] parts = date.split("/");
        if (parts.length == 2) {
            return Integer.parseInt(parts[0]);
        }
        return null;
    }

    public int getTotalItemCount() {
        return ricorrenzaDao.getTotalItemCount();
    }

    public LiveData<List<Ricorrenza>> getAllRicorrenze() {
        return Transformations.map(
                ricorrenzaDao.getAllRicorrenzeOrdered(),
                entities -> entities.stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList())
        );
    }

    public void shutdown() {
        executorService.shutdown();
    }
}