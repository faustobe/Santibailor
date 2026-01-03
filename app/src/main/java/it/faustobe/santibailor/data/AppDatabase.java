package it.faustobe.santibailor.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import it.faustobe.santibailor.data.local.dao.ImpegnoDao;
import it.faustobe.santibailor.data.local.dao.ItemSpesaDao;
import it.faustobe.santibailor.data.local.dao.ListaSpesaDao;
import it.faustobe.santibailor.data.local.dao.NoteDao;
import it.faustobe.santibailor.data.local.dao.ProdottoFrequenteDao;
import it.faustobe.santibailor.data.local.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.TipoRicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.SearchDao;
import it.faustobe.santibailor.data.local.entities.ImpegnoEntity;
import it.faustobe.santibailor.data.local.entities.ItemSpesaEntity;
import it.faustobe.santibailor.data.local.entities.ListaSpesaEntity;
import it.faustobe.santibailor.data.local.entities.NoteEntity;
import it.faustobe.santibailor.data.local.entities.ProdottoFrequenteEntity;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.local.entities.TipoRicorrenzaEntity;

@Database(entities = {RicorrenzaEntity.class, TipoRicorrenzaEntity.class, ImpegnoEntity.class, ListaSpesaEntity.class, ItemSpesaEntity.class, ProdottoFrequenteEntity.class, NoteEntity.class}, version = 11, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "santocal.db";
    private static volatile AppDatabase INSTANCE;
    public abstract RicorrenzaDao ricorrenzaDao();
    public abstract TipoRicorrenzaDao tipoRicorrenzaDao();
    public abstract SearchDao searchDao();
    public abstract ImpegnoDao impegnoDao();
    public abstract ListaSpesaDao listaSpesaDao();
    public abstract ItemSpesaDao itemSpesaDao();
    public abstract ProdottoFrequenteDao prodottoFrequenteDao();
    public abstract NoteDao noteDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        AppDatabase.class, DATABASE_NAME)
                                .createFromAsset(DATABASE_NAME)
                                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11)
                                .fallbackToDestructiveMigration()
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

    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Crea la tabella impegni
            database.execSQL("CREATE TABLE IF NOT EXISTS impegni (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "titolo TEXT NOT NULL, " +
                    "descrizione TEXT, " +
                    "data_ora INTEGER NOT NULL, " +
                    "categoria TEXT, " +
                    "reminder_enabled INTEGER NOT NULL DEFAULT 0, " +
                    "reminder_minutes_before INTEGER NOT NULL DEFAULT 30, " +
                    "completato INTEGER NOT NULL DEFAULT 0, " +
                    "note TEXT, " +
                    "created_at INTEGER NOT NULL, " +
                    "updated_at INTEGER NOT NULL)");

            Log.d("AppDatabase", "Migration 5->6: Tabella impegni creata");
        }
    };

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Aggiungi i campi priorita e image_url alla tabella impegni
            database.execSQL("ALTER TABLE impegni ADD COLUMN priorita TEXT DEFAULT 'MEDIA'");
            database.execSQL("ALTER TABLE impegni ADD COLUMN image_url TEXT");
            Log.d("AppDatabase", "Migration 6->7: Aggiunti campi priorita e image_url alla tabella impegni");
        }
    };

    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Crea indici per velocizzare le query più comuni
            // Indice composito su giorno e mese - usato in TUTTE le query principali
            database.execSQL("CREATE INDEX IF NOT EXISTS idx_santi_giorno_mese ON santi(giorno_del_mese, id_mese)");

            // Indice su tipo - usato per filtrare per tipo ricorrenza
            database.execSQL("CREATE INDEX IF NOT EXISTS idx_santi_tipo ON santi(id_tipo)");

            // Indice composito per query filtrate per giorno, mese E tipo
            database.execSQL("CREATE INDEX IF NOT EXISTS idx_santi_giorno_mese_tipo ON santi(giorno_del_mese, id_mese, id_tipo)");

            Log.d("AppDatabase", "Migration 7->8: Indici creati per ottimizzare le query");
        }
    };

    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Crea tabella liste_spesa
            database.execSQL("CREATE TABLE IF NOT EXISTS liste_spesa (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "nome TEXT NOT NULL, " +
                    "data_creazione INTEGER NOT NULL, " +
                    "completata INTEGER NOT NULL DEFAULT 0, " +
                    "colore TEXT)");

            // Crea tabella item_spesa con foreign key verso liste_spesa
            database.execSQL("CREATE TABLE IF NOT EXISTS item_spesa (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "id_lista INTEGER NOT NULL, " +
                    "nome TEXT NOT NULL, " +
                    "quantita TEXT, " +
                    "completato INTEGER NOT NULL DEFAULT 0, " +
                    "categoria TEXT, " +
                    "note TEXT, " +
                    "ordine INTEGER NOT NULL DEFAULT 0, " +
                    "FOREIGN KEY(id_lista) REFERENCES liste_spesa(id) ON DELETE CASCADE)");

            // Crea indice sulla foreign key per ottimizzare le query
            database.execSQL("CREATE INDEX IF NOT EXISTS idx_item_spesa_id_lista ON item_spesa(id_lista)");

            Log.d("AppDatabase", "Migration 8->9: Tabelle liste_spesa e item_spesa create");
        }
    };

    private static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Crea tabella prodotti_frequenti
            database.execSQL("CREATE TABLE IF NOT EXISTS prodotti_frequenti (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "nome TEXT NOT NULL, " +
                    "categoria TEXT, " +
                    "frequenza_utilizzo INTEGER NOT NULL, " +
                    "ultima_data_utilizzo INTEGER NOT NULL)");

            Log.d("AppDatabase", "Migration 9->10: Tabella prodotti_frequenti creata");
        }
    };

    private static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Crea tabella note per gestire note multiple
            database.execSQL("CREATE TABLE IF NOT EXISTS note (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "titolo TEXT NOT NULL, " +
                    "contenuto TEXT, " +
                    "data_creazione INTEGER NOT NULL, " +
                    "data_modifica INTEGER NOT NULL)");

            // Crea indice su data_modifica per ordinamento veloce
            database.execSQL("CREATE INDEX IF NOT EXISTS idx_note_data_modifica " +
                    "ON note(data_modifica)");

            Log.d("AppDatabase", "Migration 10->11: Tabella note creata con indice su data_modifica");
        }
    };
}

