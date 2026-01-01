package it.faustobe.santibailor.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.faustobe.santibailor.data.local.dao.ProdottoFrequenteDao;
import it.faustobe.santibailor.data.local.entities.ProdottoFrequenteEntity;

@Singleton
public class ProdottoFrequenteRepository {
    private final ProdottoFrequenteDao prodottoFrequenteDao;
    private final Executor executor;

    @Inject
    public ProdottoFrequenteRepository(ProdottoFrequenteDao prodottoFrequenteDao) {
        this.prodottoFrequenteDao = prodottoFrequenteDao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ProdottoFrequenteEntity>> getProdottiFrequenti() {
        return prodottoFrequenteDao.getProdottiFrequenti();
    }

    public void searchProdotti(String query, SearchCallback callback) {
        executor.execute(() -> {
            List<ProdottoFrequenteEntity> results = prodottoFrequenteDao.searchProdotti(query);
            callback.onSearchComplete(results);
        });
    }

    public void salvaOAggiornaFrequenza(String nomeProdotto, String categoria) {
        executor.execute(() -> {
            ProdottoFrequenteEntity esistente = prodottoFrequenteDao.getProdottoByNome(nomeProdotto);
            long timestamp = System.currentTimeMillis();

            if (esistente != null) {
                // Incrementa la frequenza
                prodottoFrequenteDao.incrementaFrequenza(nomeProdotto, timestamp);
            } else {
                // Crea nuovo prodotto
                ProdottoFrequenteEntity nuovo = new ProdottoFrequenteEntity();
                nuovo.setNome(nomeProdotto);
                nuovo.setCategoria(categoria);
                nuovo.setFrequenzaUtilizzo(1);
                nuovo.setUltimaDataUtilizzo(timestamp);
                prodottoFrequenteDao.insert(nuovo);
            }
        });
    }

    public interface SearchCallback {
        void onSearchComplete(List<ProdottoFrequenteEntity> results);
    }
}
