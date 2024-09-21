package it.faustobe.santibailor.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import it.faustobe.santibailor.data.local.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.RicorrenzaDao_Impl;
import it.faustobe.santibailor.data.local.dao.TipoRicorrenzaDao;
import it.faustobe.santibailor.data.local.dao.TipoRicorrenzaDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile RicorrenzaDao _ricorrenzaDao;

  private volatile TipoRicorrenzaDao _tipoRicorrenzaDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `santi` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id_mesi` INTEGER NOT NULL, `giorno` INTEGER NOT NULL, `santo` TEXT NOT NULL, `bio` TEXT, `img` TEXT, `prefix` TEXT, `suffix` TEXT, `tipo_ricorrenza_id` INTEGER NOT NULL DEFAULT 1)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tipo_ricorrenza` (`id` INTEGER NOT NULL, `santo` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '59bf579302c1c6dd3e0a5ab273f0e213')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `santi`");
        db.execSQL("DROP TABLE IF EXISTS `tipo_ricorrenza`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSanti = new HashMap<String, TableInfo.Column>(9);
        _columnsSanti.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSanti.put("id_mesi", new TableInfo.Column("id_mesi", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSanti.put("giorno", new TableInfo.Column("giorno", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSanti.put("santo", new TableInfo.Column("santo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSanti.put("bio", new TableInfo.Column("bio", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSanti.put("img", new TableInfo.Column("img", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSanti.put("prefix", new TableInfo.Column("prefix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSanti.put("suffix", new TableInfo.Column("suffix", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSanti.put("tipo_ricorrenza_id", new TableInfo.Column("tipo_ricorrenza_id", "INTEGER", true, 0, "1", TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSanti = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSanti = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSanti = new TableInfo("santi", _columnsSanti, _foreignKeysSanti, _indicesSanti);
        final TableInfo _existingSanti = TableInfo.read(db, "santi");
        if (!_infoSanti.equals(_existingSanti)) {
          return new RoomOpenHelper.ValidationResult(false, "santi(it.faustobe.santibailor.data.local.entities.RicorrenzaEntity).\n"
                  + " Expected:\n" + _infoSanti + "\n"
                  + " Found:\n" + _existingSanti);
        }
        final HashMap<String, TableInfo.Column> _columnsTipoRicorrenza = new HashMap<String, TableInfo.Column>(2);
        _columnsTipoRicorrenza.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTipoRicorrenza.put("santo", new TableInfo.Column("santo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTipoRicorrenza = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTipoRicorrenza = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTipoRicorrenza = new TableInfo("tipo_ricorrenza", _columnsTipoRicorrenza, _foreignKeysTipoRicorrenza, _indicesTipoRicorrenza);
        final TableInfo _existingTipoRicorrenza = TableInfo.read(db, "tipo_ricorrenza");
        if (!_infoTipoRicorrenza.equals(_existingTipoRicorrenza)) {
          return new RoomOpenHelper.ValidationResult(false, "tipo_ricorrenza(it.faustobe.santibailor.data.local.entities.TipoRicorrenzaEntity).\n"
                  + " Expected:\n" + _infoTipoRicorrenza + "\n"
                  + " Found:\n" + _existingTipoRicorrenza);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "59bf579302c1c6dd3e0a5ab273f0e213", "9b375eae3b2312d5459ab52f364c881a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "santi","tipo_ricorrenza");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `santi`");
      _db.execSQL("DELETE FROM `tipo_ricorrenza`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RicorrenzaDao.class, RicorrenzaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TipoRicorrenzaDao.class, TipoRicorrenzaDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RicorrenzaDao ricorrenzaDao() {
    if (_ricorrenzaDao != null) {
      return _ricorrenzaDao;
    } else {
      synchronized(this) {
        if(_ricorrenzaDao == null) {
          _ricorrenzaDao = new RicorrenzaDao_Impl(this);
        }
        return _ricorrenzaDao;
      }
    }
  }

  @Override
  public TipoRicorrenzaDao tipoRicorrenzaDao() {
    if (_tipoRicorrenzaDao != null) {
      return _tipoRicorrenzaDao;
    } else {
      synchronized(this) {
        if(_tipoRicorrenzaDao == null) {
          _tipoRicorrenzaDao = new TipoRicorrenzaDao_Impl(this);
        }
        return _tipoRicorrenzaDao;
      }
    }
  }
}
