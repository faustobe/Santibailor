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
import it.faustobe.santibailor.data.local.dao.SearchDao;
import it.faustobe.santibailor.data.local.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.TipoRicorrenzaDao;
// Importa altri DAO se necessario

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

    // Aggiungi altri metodi @Provides per altri DAO se necessario
}
