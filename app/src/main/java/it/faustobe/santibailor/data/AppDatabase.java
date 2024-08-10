package it.faustobe.santibailor.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import it.faustobe.santibailor.data.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.dao.TipoRicorrenzaDao;
import it.faustobe.santibailor.data.entities.Ricorrenza;
import it.faustobe.santibailor.data.entities.TipoRicorrenza;

@Database(entities = {Ricorrenza.class, TipoRicorrenza.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "santocal.db";
    private static volatile AppDatabase INSTANCE;

    public abstract RicorrenzaDao ricorrenzaDao();
    public abstract TipoRicorrenzaDao tipoRicorrenzaDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        AppDatabase.class, DATABASE_NAME)
                                .createFromAsset(DATABASE_NAME)
                                .build();
                        Log.d("AppDatabase", "Database created successfully");
                    } catch (Exception e) {
                        Log.e("AppDatabase", "Error creating database", e);
                    }
                }
            }
        }
        return INSTANCE;
    }
}
