package it.faustobe.santibailor.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import it.faustobe.santibailor.data.AppDatabase;
import it.faustobe.santibailor.data.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.entities.Ricorrenza;
import it.faustobe.santibailor.data.entities.RicorrenzaConTipo;
import it.faustobe.santibailor.data.entities.TipoRicorrenza;

public class RicorrenzaRepository {
    private RicorrenzaDao ricorrenzaDao;
    private ExecutorService executorService;

    public RicorrenzaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        ricorrenzaDao = db.ricorrenzaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    /*public LiveData<List<RicorrenzaConTipo>> getRicorrenzeDelGiorno(int giorno, int mese) {
        return ricorrenzaDao.getRicorrenzeDelGiorno(giorno, mese);
    }*/

    public LiveData<List<RicorrenzaConTipo>> getRicorrenzeReligiose(int giorno, int mese) {
        return ricorrenzaDao.getRicorrenzeDelGiornoPerTipo(giorno, mese, TipoRicorrenza.RELIGIOSA);
    }

    public LiveData<List<RicorrenzaConTipo>> getRicorrenzeLaiche(int giorno, int mese) {
        return ricorrenzaDao.getRicorrenzeDelGiornoPerTipo(giorno, mese, TipoRicorrenza.LAICA);
    }

    public void insert(Ricorrenza ricorrenza) {
        executorService.execute(() -> ricorrenzaDao.insert(ricorrenza));
    }

    public void update(Ricorrenza ricorrenza) {
        executorService.execute(() -> ricorrenzaDao.update(ricorrenza));
    }

    public void delete(Ricorrenza ricorrenza) {
        executorService.execute(() -> ricorrenzaDao.delete(ricorrenza));
    }

    public LiveData<List<RicorrenzaConTipo>> getRicorrenzeDelGiorno(int giorno, int mese) {
        return ricorrenzaDao.getRicorrenzeDelGiorno(giorno, mese);
    }

    public LiveData<List<Ricorrenza>> getRicorrenzePerTipo(String tipo) {
        return ricorrenzaDao.getRicorrenzePerTipo(tipo);
    }

    public LiveData<Ricorrenza> getRicorrenzaById(int id) {
        return ricorrenzaDao.getRicorrenzaById(id);
    }

    public LiveData<List<Ricorrenza>> cercaRicorrenzePerNome(String nome) {
        return ricorrenzaDao.cercaRicorrenzePerNome("%" + nome + "%");
    }

    public LiveData<List<Ricorrenza>> getAllRicorrenze() {
        return ricorrenzaDao.getAllRicorrenze();
    }
}
