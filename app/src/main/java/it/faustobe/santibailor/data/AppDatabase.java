package it.faustobe.santibailor.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import it.faustobe.santibailor.data.local.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.TipoRicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.SearchDao;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.local.entities.TipoRicorrenzaEntity;

@Database(entities = {RicorrenzaEntity.class, TipoRicorrenzaEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "santocal.db";
    private static volatile AppDatabase INSTANCE;
    public abstract RicorrenzaDao ricorrenzaDao();
    public abstract TipoRicorrenzaDao tipoRicorrenzaDao();
    public abstract SearchDao searchDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        AppDatabase.class, DATABASE_NAME)
                                .createFromAsset(DATABASE_NAME)
                                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
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

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Creiamo una nuova tabella con la struttura desiderata
            database.execSQL("CREATE TABLE santi_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "santo TEXT NOT NULL, " +
                    "giorno INTEGER NOT NULL, " +
                    "id_mese INTEGER NOT NULL, " +
                    "tipo_ricorrenza_id INTEGER NOT NULL, " +
                    "prefix TEXT, " +
                    "suffix TEXT, " +
                    "bio TEXT, " +
                    "image_url TEXT)");

            // Copiamo i dati dalla vecchia tabella alla nuova
            database.execSQL("INSERT INTO santi_new (id, santo, giorno, id_mese, tipo_ricorrenza_id, prefix, suffix, bio, image_url) " +
                    "SELECT id, santo, giorno, id_mese, tipo_ricorrenza_id, prefix, suffix, bio, image_url FROM santi");

            // Rimuoviamo la vecchia tabella
            database.execSQL("DROP TABLE santi");

            // Rinominiamo la nuova tabella
            database.execSQL("ALTER TABLE santi_new RENAME TO santi");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Non facciamo modifiche effettive, ma incrementiamo solo la versione
            database.execSQL("PRAGMA user_version = 3");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Verifica se la colonna 'image_url' esiste
            Cursor cursor = database.query("PRAGMA table_info(santi)");
            boolean hasImageUrlColumn = false;
            int columnNameIndex = cursor.getColumnIndex("name");

            if (columnNameIndex != -1) {
                while (cursor.moveToNext()) {
                    String columnName = cursor.getString(columnNameIndex);
                    if ("image_url".equals(columnName)) {
                        hasImageUrlColumn = true;
                        break;
                    }
                }
            }
            cursor.close();

            if (!hasImageUrlColumn) {
                // Se 'image_url' non esiste (cosa improbabile dato l'update recente), la aggiungiamo
                database.execSQL("ALTER TABLE santi ADD COLUMN image_url TEXT");
            }

            // Non c'è bisogno di rinominare la colonna, dato che è già 'image_url'
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("SELECT id, id_mese, giorno_del_mese, santo, bio, image_url, prefix, suffix, id_tipo FROM santi LIMIT 0");
        }
    };
}

