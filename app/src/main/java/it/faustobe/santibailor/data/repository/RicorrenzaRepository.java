package it.faustobe.santibailor.data.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import it.faustobe.santibailor.data.AppDatabase;
import it.faustobe.santibailor.data.local.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.TipoRicorrenzaDao;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.local.entities.TipoRicorrenzaEntity;
import it.faustobe.santibailor.data.mapper.RicorrenzaMapper;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;

public class RicorrenzaRepository {
    private final RicorrenzaDao ricorrenzaDao;
    private final TipoRicorrenzaDao tipoRicorrenzaDao;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    private static final String TAG = "RicorrenzaRepository";

    public RicorrenzaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.ricorrenzaDao = db.ricorrenzaDao();
        this.tipoRicorrenzaDao = db.tipoRicorrenzaDao();
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
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
                queryBuilder.append("(r.id_mesi > ? OR (r.id_mesi = ? AND r.giorno >= ?))");
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
                queryBuilder.append("(r.id_mesi < ? OR (r.id_mesi = ? AND r.giorno <= ?))");
                params.add(mese);
                params.add(mese);
                params.add(giorno);
            }
        }
    }

    public void updateImageUrl(int ricorrenzaId, String imageUrl) {
        executorService.execute(() -> ricorrenzaDao.updateImageUrl(ricorrenzaId, imageUrl));
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
        return ricorrenzaDao.getRicorrenzeDelGiornoPerTipoPaginate(giorno, mese, TipoRicorrenzaEntity.LAICA, offset, limit)
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
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM santi WHERE 1=1");
        List<Object> params = new ArrayList<>();

        addNomeCondition(queryBuilder, params, nome);
        addTipoCondition(queryBuilder, params, tipo);
        addDataCondition(queryBuilder, params, dataInizio, dataFine);

        queryBuilder.append(" ORDER BY id_mesi, giorno LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        String query = queryBuilder.toString();
        Log.d(TAG, "query in RicercaAvanzataPaginata -> " + query);
        List<RicorrenzaEntity> entities = ricorrenzaDao.eseguiRicercaAvanzata(new SimpleSQLiteQuery(query, params.toArray()));
        return entities.stream().map(RicorrenzaMapper::toDomain).collect(Collectors.toList());
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