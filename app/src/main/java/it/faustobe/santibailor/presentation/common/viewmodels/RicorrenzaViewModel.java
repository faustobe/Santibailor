package it.faustobe.santibailor.presentation.common.viewmodels;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import android.content.SharedPreferences;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import dagger.hilt.android.lifecycle.HiltViewModel;
import it.faustobe.santibailor.MyApplication;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.data.AppDatabase;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.repository.GenericRepository;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.data.repository.SearchRepository;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.SearchResult;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.domain.usecase.DeleteRicorrenzaUseCase;
import it.faustobe.santibailor.domain.usecase.GetRicorrenzaByIdUseCase;
import it.faustobe.santibailor.domain.usecase.GetRicorrenzeDelGiornoUseCase;
import it.faustobe.santibailor.domain.usecase.GetTipiRicorrenzaUseCase;
import it.faustobe.santibailor.domain.usecase.GetTotalItemCountUseCase;
import it.faustobe.santibailor.domain.usecase.InsertRicorrenzaUseCase;
import it.faustobe.santibailor.domain.usecase.RicercaAvanzataUseCase;
import it.faustobe.santibailor.domain.usecase.UpdateRicorrenzaImageUseCase;
import it.faustobe.santibailor.domain.usecase.UpdateRicorrenzaUseCase;
//import it.faustobe.santibailor.util.ImageManager;
import it.faustobe.santibailor.util.ImageHandler;
import it.faustobe.santibailor.util.PaginationHelper;

@HiltViewModel
public class RicorrenzaViewModel extends AndroidViewModel {
    private static final String TAG = "RicorrenzaViewModel";
    private static final String PREFS_NAME = "RicorrenzaPrefs";
    private static final String KEY_TIPO_RICORRENZA = "tipoRicorrenza";
    private static final String KEY_SEARCH_CRITERIA = "search_criteria";
    private static final String KEY_SEARCH_RESULTS = "search_results";
    private static final int PAGE_SIZE = 20;
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minuti in millisecondi
    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private final SavedStateHandle savedStateHandle;
    private final MutableLiveData<Boolean> updateResult = new MutableLiveData<>();
    private final RicorrenzaRepository ricorrenzaRepository;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private final ImageHandler imageHandler;
    private final GenericRepository<Ricorrenza, RicorrenzaEntity, Ricorrenza> genericRepository;
    private final GetRicorrenzeDelGiornoUseCase getRicorrenzeDelGiornoUseCase;
    private final RicercaAvanzataUseCase ricercaAvanzataUseCase;
    private final GetTipiRicorrenzaUseCase getTipiRicorrenzaUseCase;
    private final UpdateRicorrenzaUseCase updateRicorrenzaUseCase;
    private final DeleteRicorrenzaUseCase deleteRicorrenzaUseCase;
    private final GetRicorrenzaByIdUseCase getRicorrenzaByIdUseCase;
    private final InsertRicorrenzaUseCase insertRicorrenzaUseCase;
    private final GetTotalItemCountUseCase getTotalItemCountUseCase;
    private final UpdateRicorrenzaImageUseCase updateRicorrenzaImageUseCase;
    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private final MutableLiveData<SearchCriteria> searchCriteria = new MutableLiveData<>();
    private final MutableLiveData<List<Ricorrenza>> risultatiRicercaAvanzata = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalPages = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalDatabaseItemCount = new MutableLiveData<>();
    private final MutableLiveData<String> imageLoadingStatus = new MutableLiveData<>();
    private final MutableLiveData<SearchParams> searchParams = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isLastPage = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> totalSearchResults = new MutableLiveData<>(0);
    private final MutableLiveData<TipoRicorrenza> selectedTipo = new MutableLiveData<>();
    private final MutableLiveData<List<TipoRicorrenza>> listaTipoLiveData = new MutableLiveData<>();
    private final MutableLiveData<Ricorrenza> currentSaint = new MutableLiveData<>();
    private List<Ricorrenza> saintsOfDay = new ArrayList<>();
    private Random random = new Random();
    private int lastLoadedDay = -1;
    private int lastLoadedMonth = -1;
    private final LiveData<List<Ricorrenza>> ricorrenzeDelGiorno;
    private final LiveData<List<Ricorrenza>> ricorrenzeReligiose;
    private final LiveData<List<Ricorrenza>> ricorrenzeLaiche;
    private final LiveData<List<Ricorrenza>> risultatiRicerca;
    private final MutableLiveData<List<Ricorrenza>> ricorrenzeDelGiornoPaginate = new MutableLiveData<>();
    private final MutableLiveData<List<Ricorrenza>> ricorrenzeReligiosePaginate = new MutableLiveData<>();
    private final MutableLiveData<List<Ricorrenza>> ricorrenzeLaichePaginate = new MutableLiveData<>();
    private final PaginationHelper<Ricorrenza> paginationHelper;
    private final PaginationHelper<Ricorrenza> paginationHelperDelGiorno;
    private final PaginationHelper<Ricorrenza> paginationHelperReligiose;
    private final PaginationHelper<Ricorrenza> paginationHelperLaiche;
    private final SearchRepository searchRepository;
    private final MutableLiveData<List<SearchResult>> searchResults = new MutableLiveData<>();
    private long lastLoadTime = 0;
    private SearchParams lastSearchParams;
    private final FirebaseStorage storage;
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Ricorrenza>> cachedRicorrenzeDelGiorno = new MutableLiveData<>();

    @Inject
    public RicorrenzaViewModel(@NonNull Application application,
                               RicorrenzaRepository repository,
                               SearchRepository searchRepository,
                               FirebaseStorage storage,
                               ImageHandler imageHandler,
                               SavedStateHandle savedStateHandle,
                               GetRicorrenzeDelGiornoUseCase getRicorrenzeDelGiornoUseCase,
                               RicercaAvanzataUseCase ricercaAvanzataUseCase,
                               GetTipiRicorrenzaUseCase getTipiRicorrenzaUseCase,
                               UpdateRicorrenzaUseCase updateRicorrenzaUseCase,
                               DeleteRicorrenzaUseCase deleteRicorrenzaUseCase,
                               GetRicorrenzaByIdUseCase getRicorrenzaByIdUseCase,
                               InsertRicorrenzaUseCase insertRicorrenzaUseCase,
                               GetTotalItemCountUseCase getTotalItemCountUseCase,
                               UpdateRicorrenzaImageUseCase updateRicorrenzaImageUseCase) {
        super(application);
        this.savedStateHandle = savedStateHandle;
        this.ricorrenzaRepository = repository;
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
        this.imageHandler = imageHandler;
        this.genericRepository = initializeGenericRepository(application);
        this.paginationHelper = new PaginationHelper<>(PAGE_SIZE);
        this.paginationHelperDelGiorno = new PaginationHelper<>(PAGE_SIZE);
        this.paginationHelperReligiose = new PaginationHelper<>(PAGE_SIZE);
        this.paginationHelperLaiche = new PaginationHelper<>(PAGE_SIZE);
        this.getRicorrenzeDelGiornoUseCase = getRicorrenzeDelGiornoUseCase;
        this.ricercaAvanzataUseCase = ricercaAvanzataUseCase;
        this.getTipiRicorrenzaUseCase = getTipiRicorrenzaUseCase;
        this.updateRicorrenzaUseCase = updateRicorrenzaUseCase;
        this.deleteRicorrenzaUseCase = deleteRicorrenzaUseCase;
        this.getRicorrenzaByIdUseCase = getRicorrenzaByIdUseCase;
        this.insertRicorrenzaUseCase = insertRicorrenzaUseCase;
        this.getTotalItemCountUseCase = getTotalItemCountUseCase;
        this.updateRicorrenzaImageUseCase = updateRicorrenzaImageUseCase;
        this.searchRepository = searchRepository;
        this.storage = storage;

        selectedDate.setValue(new Date());

        ricorrenzeDelGiorno = Transformations.switchMap(selectedDate, date -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int giorno = cal.get(Calendar.DAY_OF_MONTH);
            int mese = cal.get(Calendar.MONTH);
            loadRicorrenzeDelGiorno(giorno, mese);
            return ricorrenzeDelGiornoPaginate;
        });

        ricorrenzeReligiose = Transformations.switchMap(selectedDate, date -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int giorno = cal.get(Calendar.DAY_OF_MONTH);
            int mese = cal.get(Calendar.MONTH);
            loadRicorrenzeReligiose(giorno, mese);
            return ricorrenzeReligiosePaginate;
        });

        ricorrenzeLaiche = Transformations.switchMap(selectedDate, date -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int giorno = cal.get(Calendar.DAY_OF_MONTH);
            int mese = cal.get(Calendar.MONTH);
            loadRicorrenzeLaiche(giorno, mese);
            return ricorrenzeLaichePaginate;
        });

        risultatiRicerca = Transformations.switchMap(searchParams, params ->
                ricorrenzaRepository.ricercaAvanzata(params.nome, params.tipo, params.dataInizio, params.dataFine)
        );

        loadTipiRicorrenza();
    }

    private GenericRepository<Ricorrenza, RicorrenzaEntity, Ricorrenza> initializeGenericRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        return new GenericRepository<>(
                db.ricorrenzaDao(),
                this::convertToEntity,
                this::convertToDomain
        );
    }

    private RicorrenzaEntity convertToEntity(Ricorrenza ricorrenza) {
        RicorrenzaEntity entity = new RicorrenzaEntity();
        entity.setId(ricorrenza.getId());
        entity.setIdMese(ricorrenza.getIdMese());
        entity.setGiornoDelMese(ricorrenza.getGiorno());
        entity.setNome(ricorrenza.getNome());
        entity.setBio(ricorrenza.getBio());
        entity.setImageUrl(ricorrenza.getImageUrl());
        entity.setPrefix(ricorrenza.getPrefix());
        entity.setSuffix(ricorrenza.getSuffix());
        entity.setIdTipo(ricorrenza.getTipoRicorrenzaId());
        return entity;
    }

    private Ricorrenza convertToDomain(RicorrenzaEntity entity) {
        return new Ricorrenza(
                entity.getId(),
                entity.getIdMese(),
                entity.getGiornoDelMese(),
                entity.getNome(),
                entity.getBio(),
                entity.getImageUrl(),
                entity.getPrefix(),
                entity.getSuffix(),
                entity.getIdTipo()
        );
    }
/*
    private ImageHandler initializeImageHandler(Application application) {
        if (application instanceof MyApplication) {
            return ((MyApplication) application).getImageHandler();
        } else {
            return new ImageHandler(application);
        }
    }
 */
    public void performSearch(String query, int limit, int offset) {
        executorService.execute(() -> {
            List<SearchResult> results = searchRepository.searchEntities(query, limit, offset);
            int count = searchRepository.getTotalSearchResultsCount(query);
            mainHandler.post(() -> {
                searchResults.setValue(results);
                totalSearchResults.setValue(count);
            });
        });
    }

    public LiveData<List<SearchResult>> getSearchResults() {
        return searchResults;
    }

    public LiveData<Integer> getTotalSearchResults() {
        return totalSearchResults;
    }

    public void reloadSaintOfDay() {
        Log.d("RicorrenzaViewModel", "reloadSaintOfDay chiamata");
        Calendar calendar = Calendar.getInstance();
        int giorno = calendar.get(Calendar.DAY_OF_MONTH);
        int mese = calendar.get(Calendar.MONTH); // +1 perché Calendar.MONTH parte da 0
        ricorrenzeDelGiornoPaginate.setValue(null);
        loadRicorrenzeDelGiorno(giorno, mese);
    }

    public void setSelectedDate(Date date) {
        if (selectedDate.getValue() == null || !selectedDate.getValue().equals(date)) {
            selectedDate.setValue(date);
        } else {
            // Se la data è la stessa, forziamo comunque un refresh
            loadRicorrenzeDelGiorno(date);
        }
    }

    private void loadRicorrenzeDelGiorno(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int giorno = cal.get(Calendar.DAY_OF_MONTH);
        int mese = cal.get(Calendar.MONTH);
        loadRicorrenzeDelGiorno(giorno, mese);
    }

    public void loadRicorrenzeForCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int giorno = calendar.get(Calendar.DAY_OF_MONTH);
        int mese = calendar.get(Calendar.MONTH);
        loadRicorrenzeDelGiorno(giorno, mese);
    }

    public void forceReloadRicorrenze() {
        lastLoadedDay = -1;
        lastLoadedMonth = -1;
        loadRicorrenzeForCurrentDate();
    }

    private void loadRicorrenzeDelGiorno(int giorno, int mese) {
        if (isLoading.getValue() != null && isLoading.getValue()) return;
        Log.d(TAG, "Tentativo di caricamento ricorrenze per " + giorno + "/" + mese);

        if (ricorrenzeDelGiornoPaginate.getValue() != null &&
                lastLoadedDay == giorno && lastLoadedMonth == mese) {
            Log.d(TAG, "Dati già caricati per questo giorno, usando cache");
            refreshRandomSaint();
            return;
        }

        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                GetRicorrenzeDelGiornoUseCase.Params params = new GetRicorrenzeDelGiornoUseCase.Params(giorno, mese);
                List<Ricorrenza> ricorrenze = getRicorrenzeDelGiornoUseCase.executeSync(params);

                mainHandler.post(() -> {
                    saintsOfDay = new ArrayList<>(ricorrenze);
                    List<Ricorrenza> paginatedRicorrenze = ricorrenze.subList(0, Math.min(ricorrenze.size(), PAGE_SIZE));
                    ricorrenzeDelGiornoPaginate.setValue(paginatedRicorrenze);
                    lastLoadedDay = giorno;
                    lastLoadedMonth = mese;
                    Log.d(TAG, "Caricamento completato per " + giorno + "/" + mese + ": " + ricorrenze.size() + " ricorrenze");

                    refreshRandomSaint();
                    loadImagesForVisibleItems(paginatedRicorrenze);

                    isLoading.setValue(false);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    Log.e(TAG, "Errore nel caricamento delle ricorrenze", e);
                    isLoading.setValue(false);
                    errorLiveData.setValue("Errore nel caricamento delle ricorrenze: " + e.getMessage());
                });
            }
        });
    }

    private void loadImagesForVisibleItems(List<Ricorrenza> ricorrenze) {
        for (Ricorrenza ricorrenza : ricorrenze) {
            if (ricorrenza.getImageUrl() != null && !ricorrenza.getImageUrl().isEmpty()) {
                imageHandler.preloadImage(ricorrenza.getImageUrl());
            }
        }
    }

    public LiveData<Ricorrenza> getRicorrenza(String id) {
        return ricorrenzaRepository.getRicorrenza(id);
    }

    public LiveData<Uri> uploadImage(String id, byte[] imageData) {
        MutableLiveData<Uri> result = new MutableLiveData<>();
        ricorrenzaRepository.uploadImage(id, imageData)
                .addOnSuccessListener(result::setValue)
                .addOnFailureListener(e -> {
                    // Gestisci l'errore
                });
        return result;
    }

    public LiveData<byte[]> downloadImage(String id) {
        MutableLiveData<byte[]> result = new MutableLiveData<>();
        ricorrenzaRepository.downloadImage(id)
                .addOnSuccessListener(result::setValue)
                .addOnFailureListener(e -> {
                    // Gestisci l'errore
                });
        return result;
    }

    public LiveData<Boolean> updateBio(String id, String bio) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        ricorrenzaRepository.updateBio(id, bio)
                .addOnSuccessListener(aVoid -> result.setValue(true))
                .addOnFailureListener(e -> result.setValue(false));
        return result;
    }

    public LiveData<String> getBio(String id) {
        MutableLiveData<String> result = new MutableLiveData<>();
        ricorrenzaRepository.getBio(id)
                .addOnSuccessListener(result::setValue)
                .addOnFailureListener(e -> {
                    // Gestisci l'errore
                });
        return result;
    }

    public LiveData<String> getImageUrl(String id, boolean isThumb) {
        MutableLiveData<String> result = new MutableLiveData<>();
        ricorrenzaRepository.getImageUrl(id, isThumb)
                .addOnSuccessListener(uri -> result.setValue(uri.toString()))
                .addOnFailureListener(e -> result.setValue(null));
        return result;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void refreshRandomSaint() {
        List<Ricorrenza> ricorrenze = ricorrenzeDelGiorno.getValue();
        Log.d(TAG, "Refreshing random saint. Total saints: " + (ricorrenze != null ? ricorrenze.size() : 0));
        if (ricorrenze != null && !ricorrenze.isEmpty()) {
            int randomIndex = new Random().nextInt(ricorrenze.size());
            Ricorrenza newSaint = ricorrenze.get(randomIndex);
            if (!newSaint.equals(currentSaint.getValue())) {
                currentSaint.setValue(newSaint);
                Log.d(TAG, "New saint selected: " + newSaint.getNome());
            } else {
                Log.d(TAG, "Same saint selected, skipping update");
            }
        } else {
            Log.d(TAG, "No saints available to select from");
        }
    }

    public LiveData<Ricorrenza> getCurrentSaint() {
        return currentSaint;
    }

    public void loadTipiRicorrenza() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastLoadTime > CACHE_DURATION || listaTipoLiveData.getValue() == null) {
            executorService.execute(() -> {
                List<TipoRicorrenza> tipos = getTipiRicorrenzaUseCase.execute(null);
                mainHandler.post(() -> {
                    listaTipoLiveData.setValue(tipos);
                    lastLoadTime = currentTime;
                });
            });
        }
    }

    public void loadImage(String url, ImageView imageView, int placeholderResId) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(placeholderResId);
            return;
        }

        if (url.startsWith("https://firebasestorage.googleapis.com")) {
            Glide.with(getApplication())
                    .load(url)
                    .placeholder(placeholderResId)
                    .error(placeholderResId)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("RicorrenzaViewModel", "Error loading image from Firebase: " + url, e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        } else {
            ImageHandler.getInstance(getApplication()).loadImage(url, imageView, placeholderResId);
        }
    }

    public void loadRicorrenzeReligiose(int giorno, int mese) {
        executorService.execute(() -> {
            List<Ricorrenza> ricorrenze = ricorrenzaRepository.getRicorrenzeReligiosePaginate(giorno, mese, 0, PAGE_SIZE);
            mainHandler.post(() -> ricorrenzeReligiosePaginate.setValue(ricorrenze));
        });
    }

    public void loadRicorrenzeLaiche(int giorno, int mese) {
        executorService.execute(() -> {
            List<Ricorrenza> ricorrenze = ricorrenzaRepository.getRicorrenzeLaichePaginate(giorno, mese, 0, PAGE_SIZE);
            mainHandler.post(() -> ricorrenzeLaichePaginate.setValue(ricorrenze));
        });
    }

    public RicorrenzaRepository getRepository() {
        return ricorrenzaRepository;
    }

    public void update(Ricorrenza ricorrenza) {
        executorService.execute(() -> {
            try {
                updateRicorrenzaUseCase.execute(ricorrenza);
                mainHandler.post(() -> updateResult.setValue(true));
            } catch (Exception e) {
                mainHandler.post(() -> updateResult.setValue(false));
            }
        });
    }

    public LiveData<Boolean> getUpdateResult() {
        return updateResult;
    }

    public LiveData<Ricorrenza> getRicorrenzaById(int id) {
        return getRicorrenzaByIdUseCase.execute(id);
    }

    public void insert(Ricorrenza ricorrenza, OnInsertCompleteListener listener) {
        executorService.execute(() -> {
            long newId = insertRicorrenzaUseCase.execute(ricorrenza);
            mainHandler.post(() -> {
                if (newId > 0) {
                    listener.onInsertSuccess((int) newId);
                    loadRicorrenzeForCurrentDate();

                } else {
                    listener.onInsertFailure("Inserimento fallito");
                }
            });
        });
    }

    public void deleteRicorrenza(Ricorrenza ricorrenza) {
        genericRepository.delete(ricorrenza, new GenericRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int id) {
                deleteResult.postValue(true);
            }

            @Override
            public void onError(String errorMessage) {
                deleteResult.postValue(false);
            }
        });
    }

    public void loadTotalItemCount() {
        executorService.execute(() -> {
            int count = getTotalItemCountUseCase.execute(null);
            totalDatabaseItemCount.postValue(count);
        });
    }

    public void updateRicorrenzaImage(int ricorrenzaId, String imageUrl) {
        executorService.execute(() -> {
            updateRicorrenzaImageUseCase.execute(new UpdateRicorrenzaImageUseCase.Params(ricorrenzaId, imageUrl));
            imageLoadingStatus.postValue("Immagine aggiornata con successo");
        });
    }

    public void setSearchParams(String nome, Integer tipo, String dataInizio, String dataFine) {
        SearchCriteria criteria = new SearchCriteria(nome, tipo, dataInizio, dataFine);
        searchCriteria.setValue(criteria);
        eseguiRicercaAvanzata(nome, tipo, dataInizio, dataFine);
    }

    private void saveSearchCriteria(SearchCriteria criteria) {
        savedStateHandle.set(KEY_SEARCH_CRITERIA + "_nome", criteria.nome);
        savedStateHandle.set(KEY_SEARCH_CRITERIA + "_tipo", criteria.tipo);
        savedStateHandle.set(KEY_SEARCH_CRITERIA + "_dataInizio", criteria.dataInizio);
        savedStateHandle.set(KEY_SEARCH_CRITERIA + "_dataFine", criteria.dataFine);
    }

    // Definizione della classe SearchCriteria
    public static class SearchCriteria implements Parcelable {
        public final String nome;
        public final Integer tipo;
        public final String dataInizio;
        public final String dataFine;

        public SearchCriteria(String nome, Integer tipo, String dataInizio, String dataFine) {
            this.nome = nome;
            this.tipo = tipo;
            this.dataInizio = dataInizio;
            this.dataFine = dataFine;
        }

        protected SearchCriteria(Parcel in) {
            nome = in.readString();
            tipo = in.readInt();
            dataInizio = in.readString();
            dataFine = in.readString();
        }

        public static final Creator<SearchCriteria> CREATOR = new Creator<SearchCriteria>() {
            @Override
            public SearchCriteria createFromParcel(Parcel in) {
                return new SearchCriteria(in);
            }

            @Override
            public SearchCriteria[] newArray(int size) {
                return new SearchCriteria[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(nome);
            dest.writeInt(tipo);
            dest.writeString(dataInizio);
            dest.writeString(dataFine);
        }
    }

    public void eseguiRicercaAvanzata(String nome, Integer tipo, String dataInizio, String dataFine) {
        isLoading.setValue(true);
        lastSearchParams = new SearchParams(nome, tipo, dataInizio, dataFine);
        paginationHelper.reset();
        currentPage.setValue(0);
        executorService.execute(() -> {
            try {
                paginationHelper.loadNextPage(new PaginationHelper.DataLoader<Ricorrenza>() {
                    @Override
                    public List<Ricorrenza> loadData(int offset, int limit) {
                        RicercaAvanzataUseCase.Params params = new RicercaAvanzataUseCase.Params(
                                nome, tipo, dataInizio, dataFine, limit, offset);
                        return ricercaAvanzataUseCase.execute(params);
                    }

                    @Override
                    public int getTotalCount() {
                        // Creiamo un oggetto Params per getTotalCount
                        // Usiamo 0 per limit e offset poiché non sono rilevanti per il conteggio totale
                        RicercaAvanzataUseCase.Params params = new RicercaAvanzataUseCase.Params(
                                nome, tipo, dataInizio, dataFine, 0, 0);
                        return ricercaAvanzataUseCase.getTotalCount(params);
                    }
                });

                mainHandler.post(() -> {
                    List<Ricorrenza> results = paginationHelper.getCurrentPageItems();
                    int totalResults = paginationHelper.getTotalCount();

                    totalSearchResults.setValue(totalResults);
                    risultatiRicercaAvanzata.setValue(results);

                    isLastPage.setValue(!paginationHelper.hasNextPage());
                    isLoading.setValue(false);
                    currentPage.setValue(paginationHelper.getCurrentPage());
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    isLoading.setValue(false);
                });
            }
        });
    }

    public void loadNextPage() {
        if (isLoading.getValue() != null && isLoading.getValue()) return;
        if (isLastPage.getValue() != null && isLastPage.getValue()) return;

        isLoading.setValue(true);

        executorService.execute(() -> {
            try {
                paginationHelper.loadNextPage(new PaginationHelper.DataLoader<Ricorrenza>() {
                    @Override
                    public List<Ricorrenza> loadData(int offset, int limit) {
                        return ricorrenzaRepository.ricercaAvanzataPaginata(
                                lastSearchParams.nome, lastSearchParams.tipo,
                                lastSearchParams.dataInizio, lastSearchParams.dataFine,
                                limit, offset);
                    }

                    @Override
                    public int getTotalCount() {
                        return ricorrenzaRepository.contaTotaleRisultati(
                                lastSearchParams.nome, lastSearchParams.tipo,
                                lastSearchParams.dataInizio, lastSearchParams.dataFine);
                    }
                });

                mainHandler.post(() -> {
                    List<Ricorrenza> newResults = paginationHelper.getCurrentPageItems();
                    List<Ricorrenza> currentList = risultatiRicercaAvanzata.getValue();
                    if (currentList != null) {
                        currentList.addAll(newResults);
                        risultatiRicercaAvanzata.setValue(currentList);
                    }

                    isLastPage.setValue(!paginationHelper.hasNextPage());
                    isLoading.setValue(false);
                    currentPage.setValue(paginationHelper.getCurrentPage());
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    isLoading.setValue(false);
                });
            }
        });
    }

    public void clearSearchState() {
        risultatiRicercaAvanzata.setValue(new ArrayList<>());
        currentPage.setValue(0);
        totalPages.setValue(0);
        totalDatabaseItemCount.setValue(0);
        lastSearchParams = null;
    }

    public void setSelectedTipo(TipoRicorrenza tipo) {
        selectedTipo.setValue(tipo);
    }

    public void moveToNextDay() {
        Date currentDate = selectedDate.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDate.setValue(calendar.getTime());
    }

    public void moveToPreviousDay() {
        Date currentDate = selectedDate.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        selectedDate.setValue(calendar.getTime());
    }

    public void debugRicorrenzeOggi() {
        Date oggi = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(oggi);
        int giorno = cal.get(Calendar.DAY_OF_MONTH);
        int mese = cal.get(Calendar.MONTH) + 1;

        executorService.execute(() -> {
            int count = ricorrenzaRepository.contaRicorrenzePerGiornoMese(giorno, mese);
            ricorrenzaRepository.getRicorrenzePerGiornoMese(giorno, mese).observeForever(ricorrenze -> {
                Log.d(TAG, "Ricorrenze per oggi (" + giorno + "/" + mese + "): " + count);
                for (Ricorrenza r : ricorrenze) {
                    Log.d(TAG, "Ricorrenza: " + r.toString());
                }
                ricorrenzaRepository.getRicorrenzePerGiornoMese(giorno, mese).removeObserver(ricorrenze1 -> {});
            });
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }

    // Getters for LiveData
    public LiveData<List<Ricorrenza>> getRicorrenzeDelGiorno() {
        return ricorrenzeDelGiorno;
    }

    public LiveData<List<Ricorrenza>> getRicorrenzeReligiose() {
        return ricorrenzeReligiose;
    }

    public LiveData<List<Ricorrenza>> getRicorrenzeLaiche() {
        return ricorrenzeLaiche;
    }

    public LiveData<List<Ricorrenza>> getRisultatiRicerca() {
        return risultatiRicerca;
    }

    public LiveData<List<Ricorrenza>> getRisultatiRicercaAvanzata() {
        return risultatiRicercaAvanzata;
    }

    public LiveData<Boolean> getDeleteResult() {
        return deleteResult;
    }

    public LiveData<Integer> getTotalItemCount() {
        return totalDatabaseItemCount;
    }

    public LiveData<String> getImageLoadingStatus() {
        return imageLoadingStatus;
    }

    public LiveData<Boolean> getIsLastPage() {
        return isLastPage;
    }

    public LiveData<TipoRicorrenza> getSelectedTipo() {
        return selectedTipo;
    }

    public LiveData<List<TipoRicorrenza>> getListaTipo() {
        return listaTipoLiveData;
    }

    public LiveData<Date> getSelectedDate() {
        return selectedDate;
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }

    public LiveData<List<TipoRicorrenza>> getAllTipiRicorrenza() {
        return listaTipoLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void clearSearch() {
        // Implementa la logica per cancellare la ricerca
    }

    public void clearRisultatiRicerca() {
        risultatiRicercaAvanzata.setValue(new ArrayList<>());
    }

    // Inner classes
    private static class SearchParams {
        final String nome;
        final Integer tipo;
        final String dataInizio;
        final String dataFine;

        SearchParams(String nome, Integer tipo, String dataInizio, String dataFine) {
            this.nome = nome;
            this.tipo = tipo;
            this.dataInizio = dataInizio;
            this.dataFine = dataFine;
        }
    }

    public interface OnInsertCompleteListener {
        void onInsertSuccess(int newId);
        void onInsertFailure(String error);
    }
/*
    public static class Factory implements ViewModelProvider.Factory {
        private final Application application;
        private final SavedStateHandle savedStateHandle;

        public Factory(Application application, SavedStateHandle savedStateHandle) {
            this.application = application;
            this.savedStateHandle = savedStateHandle;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RicorrenzaViewModel.class)) {
                return (T) new RicorrenzaViewModel(application, savedStateHandle);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

 */
}
