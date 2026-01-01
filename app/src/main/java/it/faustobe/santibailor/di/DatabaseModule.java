package it.faustobe.santibailor.di;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import it.faustobe.santibailor.data.AppDatabase;
import it.faustobe.santibailor.data.local.dao.ImpegnoDao;
import it.faustobe.santibailor.data.local.dao.ItemSpesaDao;
import it.faustobe.santibailor.data.local.dao.ListaSpesaDao;
import it.faustobe.santibailor.data.local.dao.ProdottoFrequenteDao;
import it.faustobe.santibailor.data.local.dao.SearchDao;
import it.faustobe.santibailor.data.local.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.TipoRicorrenzaDao;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public SearchDao provideSearchDao(AppDatabase database) {
        return database.searchDao();
    }

    @Provides
    @Singleton
    public RicorrenzaDao provideRicorrenzaDao(AppDatabase database) {
        return database.ricorrenzaDao();
    }

    @Provides
    @Singleton
    public TipoRicorrenzaDao provideTipoRicorrenzaDao(AppDatabase database){
        return database.tipoRicorrenzaDao();
    }

    @Provides
    @Singleton
    public ImpegnoDao provideImpegnoDao(AppDatabase database) {
        return database.impegnoDao();
    }

    @Provides
    @Singleton
    public ListaSpesaDao provideListaSpesaDao(AppDatabase database) {
        return database.listaSpesaDao();
    }

    @Provides
    @Singleton
    public ItemSpesaDao provideItemSpesaDao(AppDatabase database) {
        return database.itemSpesaDao();
    }

    @Provides
    @Singleton
    public ProdottoFrequenteDao provideProdottoFrequenteDao(AppDatabase database) {
        return database.prodottoFrequenteDao();
    }
}
