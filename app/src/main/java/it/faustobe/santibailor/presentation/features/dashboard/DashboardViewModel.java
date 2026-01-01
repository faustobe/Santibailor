package it.faustobe.santibailor.presentation.features.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import it.faustobe.santibailor.data.local.dao.ImpegnoDao;
import it.faustobe.santibailor.data.local.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;

@HiltViewModel
public class DashboardViewModel extends ViewModel {

    private final RicorrenzaDao ricorrenzaDao;
    private final ImpegnoDao impegnoDao;
    private final ExecutorService executorService;

    private final MutableLiveData<Integer> totalRicorrenze = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> todayRicorrenze = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> religioseCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> laicheCount = new MutableLiveData<>(0);
    private final MutableLiveData<String> databaseInfo = new MutableLiveData<>("Caricamento...");

    // Stats Impegni
    private final MutableLiveData<Integer> totalImpegni = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> impegniOggi = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> impegniFuturi = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> impegniCompletati = new MutableLiveData<>(0);

    @Inject
    public DashboardViewModel(RicorrenzaDao ricorrenzaDao, ImpegnoDao impegnoDao) {
        this.ricorrenzaDao = ricorrenzaDao;
        this.impegnoDao = impegnoDao;
        this.executorService = Executors.newSingleThreadExecutor();
        loadStatistics();
    }

    private void loadStatistics() {
        executorService.execute(() -> {
            try {
                // Totale ricorrenze
                int total = ricorrenzaDao.getTotalItemCount();
                totalRicorrenze.postValue(total);

                // Ricorrenze di oggi
                Calendar calendar = Calendar.getInstance();
                int giorno = calendar.get(Calendar.DAY_OF_MONTH);
                int mese = calendar.get(Calendar.MONTH) + 1;
                int today = ricorrenzaDao.getCountRicorrenzeDelGiorno(giorno, mese);
                todayRicorrenze.postValue(today);

                // Ricorrenze per tipo
                List<RicorrenzaEntity> allRicorrenze = ricorrenzaDao.getAllRicorrenze();
                int religiose = 0;
                int laiche = 0;

                for (RicorrenzaEntity r : allRicorrenze) {
                    if (r.getIdTipo() == 1) {
                        religiose++;
                    } else if (r.getIdTipo() == 2) {
                        laiche++;
                    }
                }

                religioseCount.postValue(religiose);
                laicheCount.postValue(laiche);

                // Info database
                String dbInfo = "Database: " + total + " ricorrenze caricate";
                databaseInfo.postValue(dbInfo);

                // === STATISTICHE IMPEGNI ===
                // Totale impegni
                int totalImp = impegnoDao.getTotalCount();
                totalImpegni.postValue(totalImp);

                // Impegni di oggi
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long startOfDay = cal.getTimeInMillis();
                long endOfDay = startOfDay + (24 * 60 * 60 * 1000);
                int impOggi = impegnoDao.getCountImpegniOggi(startOfDay, endOfDay);
                impegniOggi.postValue(impOggi);

                // Impegni futuri (non completati)
                long now = System.currentTimeMillis();
                int impFuturi = impegnoDao.getCountImpegniFuturi(now);
                impegniFuturi.postValue(impFuturi);

                // Impegni completati
                int impCompletati = impegnoDao.getCountImpegniCompletati();
                impegniCompletati.postValue(impCompletati);

                android.util.Log.d("DashboardViewModel", "Stats Impegni - Totali: " + totalImp +
                    ", Oggi: " + impOggi + ", Futuri: " + impFuturi + ", Completati: " + impCompletati);

            } catch (Exception e) {
                databaseInfo.postValue("Errore caricamento dati");
                android.util.Log.e("DashboardViewModel", "Error loading statistics", e);
            }
        });
    }

    // Getters per LiveData
    public LiveData<Integer> getTotalRicorrenze() {
        return totalRicorrenze;
    }

    public LiveData<Integer> getTodayRicorrenze() {
        return todayRicorrenze;
    }

    public LiveData<Integer> getReligioseCount() {
        return religioseCount;
    }

    public LiveData<Integer> getLaicheCount() {
        return laicheCount;
    }

    public LiveData<String> getDatabaseInfo() {
        return databaseInfo;
    }

    // Getters per statistiche Impegni
    public LiveData<Integer> getTotalImpegni() {
        return totalImpegni;
    }

    public LiveData<Integer> getImpegniOggi() {
        return impegniOggi;
    }

    public LiveData<Integer> getImpegniFuturi() {
        return impegniFuturi;
    }

    public LiveData<Integer> getImpegniCompletati() {
        return impegniCompletati;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
