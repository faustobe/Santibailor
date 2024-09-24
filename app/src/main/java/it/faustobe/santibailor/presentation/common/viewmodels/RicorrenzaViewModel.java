package it.faustobe.santibailor.presentation.common.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import android.content.SharedPreferences;
import android.content.Context;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import dagger.hilt.android.lifecycle.HiltViewModel;
import it.faustobe.santibailor.MyApplication;
import it.faustobe.santibailor.data.AppDatabase;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.repository.GenericRepository;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.domain.model.Ricorrenza;
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
import it.faustobe.santibailor.util.ImageManager;
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
    private final ImageManager imageManager;
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

    private long lastLoadTime = 0;
    private SearchParams lastSearchParams;

    @Inject
    public RicorrenzaViewModel(Application application,
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
        this.ricorrenzaRepository = new RicorrenzaRepository(application);
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
        this.imageManager = initializeImageManager(application);
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

    private ImageManager initializeImageManager(Application application) {
        if (application instanceof MyApplication) {
            return ((MyApplication) application).getImageManager();
        } else {
            return new ImageManager(application);
        }
    }

    public void loadTipiRicorrenza() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastLoadTime > CACHE_DURATION || listaTipoLiveData.getValue() == null) {
            executorService.execute(() -> {
                List<TipoRicorrenza> tipos = getTipiRicorrenzaUseCase.execute(null);//faccio solo questa modifica
                if (tipos.isEmpty() || !tipos.get(0).getTipo().equals("Tutte")) {
                    tipos.add(0, new TipoRicorrenza(0, "Tutte"));
                }
                mainHandler.post(() -> {
                    listaTipoLiveData.setValue(tipos);
                    lastLoadTime = currentTime;
                });
            });
        }
    }

    public void loadImage(String url, ImageView imageView, int placeholderResId) {
        imageManager.loadImage(url, imageView, placeholderResId);
    }

    public void setDate(Date date) {
        selectedDate.setValue(date);
    }

    public void loadRicorrenzeDelGiorno(int giorno, int mese) {
        executorService.execute(() -> {
            GetRicorrenzeDelGiornoUseCase.Params params = new GetRicorrenzeDelGiornoUseCase.Params(giorno, mese);
            List<Ricorrenza> ricorrenze = getRicorrenzeDelGiornoUseCase.executeSync(params);
            mainHandler.post(() -> {
                List<Ricorrenza> paginatedRicorrenze = ricorrenze.subList(0, Math.min(ricorrenze.size(), PAGE_SIZE));
                ricorrenzeDelGiornoPaginate.setValue(paginatedRicorrenze);
            });
        });
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

    public void delete(Ricorrenza ricorrenza) {
        executorService.execute(() -> {
            try {
                deleteRicorrenzaUseCase.execute(ricorrenza);
                mainHandler.post(() -> deleteResult.setValue(true));
            } catch (Exception e) {
                mainHandler.post(() -> deleteResult.setValue(false));
            }
        });
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
        if (tipo != null && "Tutte".equals(tipo.getTipo())) {
            selectedTipo.setValue(null);
        } else {
            selectedTipo.setValue(tipo);
        }
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

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Integer> getTotalSearchResults() {
        return totalSearchResults;
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
