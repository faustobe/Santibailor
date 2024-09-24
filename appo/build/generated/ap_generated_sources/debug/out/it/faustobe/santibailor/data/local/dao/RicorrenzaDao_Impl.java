package it.faustobe.santibailor.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import it.faustobe.santibailor.data.local.entities.RicorrenzaConTipoEntity;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.local.entities.TipoRicorrenzaEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RicorrenzaDao_Impl implements RicorrenzaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RicorrenzaEntity> __insertionAdapterOfRicorrenzaEntity;

  private final EntityDeletionOrUpdateAdapter<RicorrenzaEntity> __deletionAdapterOfRicorrenzaEntity;

  private final EntityDeletionOrUpdateAdapter<RicorrenzaEntity> __updateAdapterOfRicorrenzaEntity;

  public RicorrenzaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRicorrenzaEntity = new EntityInsertionAdapter<RicorrenzaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `santi` (`id`,`id_mesi`,`giorno`,`santo`,`bio`,`img`,`prefix`,`suffix`,`tipo_ricorrenza_id`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RicorrenzaEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getIdMese());
        statement.bindLong(3, entity.getGiorno());
        if (entity.getNome() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNome());
        }
        if (entity.getBio() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBio());
        }
        if (entity.getImg() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getImg());
        }
        if (entity.getPrefix() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPrefix());
        }
        if (entity.getSuffix() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSuffix());
        }
        statement.bindLong(9, entity.getTipoRicorrenzaId());
      }
    };
    this.__deletionAdapterOfRicorrenzaEntity = new EntityDeletionOrUpdateAdapter<RicorrenzaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `santi` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RicorrenzaEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRicorrenzaEntity = new EntityDeletionOrUpdateAdapter<RicorrenzaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `santi` SET `id` = ?,`id_mesi` = ?,`giorno` = ?,`santo` = ?,`bio` = ?,`img` = ?,`prefix` = ?,`suffix` = ?,`tipo_ricorrenza_id` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RicorrenzaEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getIdMese());
        statement.bindLong(3, entity.getGiorno());
        if (entity.getNome() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNome());
        }
        if (entity.getBio() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBio());
        }
        if (entity.getImg() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getImg());
        }
        if (entity.getPrefix() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPrefix());
        }
        if (entity.getSuffix() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSuffix());
        }
        statement.bindLong(9, entity.getTipoRicorrenzaId());
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public long insert(final RicorrenzaEntity entity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfRicorrenzaEntity.insertAndReturnId(entity);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final RicorrenzaEntity entity) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __deletionAdapterOfRicorrenzaEntity.handle(entity);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final RicorrenzaEntity entity) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __updateAdapterOfRicorrenzaEntity.handle(entity);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<RicorrenzaConTipoEntity> getRicorrenzeConTipo() {
    final String _sql = "SELECT * FROM santi";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
        final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
        final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
        final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
        final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
        final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
        final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
        final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
        final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
        final LongSparseArray<TipoRicorrenzaEntity> _collectionTipoRicorrenza = new LongSparseArray<TipoRicorrenzaEntity>();
        while (_cursor.moveToNext()) {
          final Long _tmpKey;
          if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
            _tmpKey = null;
          } else {
            _tmpKey = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
          }
          if (_tmpKey != null) {
            _collectionTipoRicorrenza.put(_tmpKey, null);
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(_collectionTipoRicorrenza);
        final List<RicorrenzaConTipoEntity> _result = new ArrayList<RicorrenzaConTipoEntity>(_cursor.getCount());
        while (_cursor.moveToNext()) {
          final RicorrenzaConTipoEntity _item;
          final RicorrenzaEntity _tmpRicorrenza;
          if (!(_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfIdMese) && _cursor.isNull(_cursorIndexOfGiorno) && _cursor.isNull(_cursorIndexOfNome) && _cursor.isNull(_cursorIndexOfBio) && _cursor.isNull(_cursorIndexOfImg) && _cursor.isNull(_cursorIndexOfPrefix) && _cursor.isNull(_cursorIndexOfSuffix) && _cursor.isNull(_cursorIndexOfTipoRicorrenzaId))) {
            _tmpRicorrenza = new RicorrenzaEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _tmpRicorrenza.setId(_tmpId);
            final int _tmpIdMese;
            _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
            _tmpRicorrenza.setIdMese(_tmpIdMese);
            final int _tmpGiorno;
            _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
            _tmpRicorrenza.setGiorno(_tmpGiorno);
            final String _tmpNome;
            if (_cursor.isNull(_cursorIndexOfNome)) {
              _tmpNome = null;
            } else {
              _tmpNome = _cursor.getString(_cursorIndexOfNome);
            }
            _tmpRicorrenza.setNome(_tmpNome);
            final String _tmpBio;
            if (_cursor.isNull(_cursorIndexOfBio)) {
              _tmpBio = null;
            } else {
              _tmpBio = _cursor.getString(_cursorIndexOfBio);
            }
            _tmpRicorrenza.setBio(_tmpBio);
            final String _tmpImg;
            if (_cursor.isNull(_cursorIndexOfImg)) {
              _tmpImg = null;
            } else {
              _tmpImg = _cursor.getString(_cursorIndexOfImg);
            }
            _tmpRicorrenza.setImg(_tmpImg);
            final String _tmpPrefix;
            if (_cursor.isNull(_cursorIndexOfPrefix)) {
              _tmpPrefix = null;
            } else {
              _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
            }
            _tmpRicorrenza.setPrefix(_tmpPrefix);
            final String _tmpSuffix;
            if (_cursor.isNull(_cursorIndexOfSuffix)) {
              _tmpSuffix = null;
            } else {
              _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
            }
            _tmpRicorrenza.setSuffix(_tmpSuffix);
            final int _tmpTipoRicorrenzaId;
            _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
            _tmpRicorrenza.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
          } else {
            _tmpRicorrenza = null;
          }
          final TipoRicorrenzaEntity _tmpTipoRicorrenza;
          final Long _tmpKey_1;
          if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
          }
          if (_tmpKey_1 != null) {
            _tmpTipoRicorrenza = _collectionTipoRicorrenza.get(_tmpKey_1);
          } else {
            _tmpTipoRicorrenza = null;
          }
          _item = new RicorrenzaConTipoEntity();
          _item.ricorrenza = _tmpRicorrenza;
          _item.tipoRicorrenza = _tmpTipoRicorrenza;
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<RicorrenzaConTipoEntity>> getRicorrenzeDelGiorno(final int giorno,
      final int mese) {
    final String _sql = "SELECT * FROM santi WHERE giorno = ? AND id_mesi = ? ORDER BY RANDOM()";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, giorno);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mese);
    return __db.getInvalidationTracker().createLiveData(new String[] {"tipo_ricorrenza",
        "santi"}, true, new Callable<List<RicorrenzaConTipoEntity>>() {
      @Override
      @Nullable
      public List<RicorrenzaConTipoEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
            final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
            final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
            final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
            final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
            final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
            final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
            final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
            final LongSparseArray<TipoRicorrenzaEntity> _collectionTipoRicorrenza = new LongSparseArray<TipoRicorrenzaEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
              }
              if (_tmpKey != null) {
                _collectionTipoRicorrenza.put(_tmpKey, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(_collectionTipoRicorrenza);
            final List<RicorrenzaConTipoEntity> _result = new ArrayList<RicorrenzaConTipoEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final RicorrenzaConTipoEntity _item;
              final RicorrenzaEntity _tmpRicorrenza;
              if (!(_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfIdMese) && _cursor.isNull(_cursorIndexOfGiorno) && _cursor.isNull(_cursorIndexOfNome) && _cursor.isNull(_cursorIndexOfBio) && _cursor.isNull(_cursorIndexOfImg) && _cursor.isNull(_cursorIndexOfPrefix) && _cursor.isNull(_cursorIndexOfSuffix) && _cursor.isNull(_cursorIndexOfTipoRicorrenzaId))) {
                _tmpRicorrenza = new RicorrenzaEntity();
                final int _tmpId;
                _tmpId = _cursor.getInt(_cursorIndexOfId);
                _tmpRicorrenza.setId(_tmpId);
                final int _tmpIdMese;
                _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
                _tmpRicorrenza.setIdMese(_tmpIdMese);
                final int _tmpGiorno;
                _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
                _tmpRicorrenza.setGiorno(_tmpGiorno);
                final String _tmpNome;
                if (_cursor.isNull(_cursorIndexOfNome)) {
                  _tmpNome = null;
                } else {
                  _tmpNome = _cursor.getString(_cursorIndexOfNome);
                }
                _tmpRicorrenza.setNome(_tmpNome);
                final String _tmpBio;
                if (_cursor.isNull(_cursorIndexOfBio)) {
                  _tmpBio = null;
                } else {
                  _tmpBio = _cursor.getString(_cursorIndexOfBio);
                }
                _tmpRicorrenza.setBio(_tmpBio);
                final String _tmpImg;
                if (_cursor.isNull(_cursorIndexOfImg)) {
                  _tmpImg = null;
                } else {
                  _tmpImg = _cursor.getString(_cursorIndexOfImg);
                }
                _tmpRicorrenza.setImg(_tmpImg);
                final String _tmpPrefix;
                if (_cursor.isNull(_cursorIndexOfPrefix)) {
                  _tmpPrefix = null;
                } else {
                  _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
                }
                _tmpRicorrenza.setPrefix(_tmpPrefix);
                final String _tmpSuffix;
                if (_cursor.isNull(_cursorIndexOfSuffix)) {
                  _tmpSuffix = null;
                } else {
                  _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
                }
                _tmpRicorrenza.setSuffix(_tmpSuffix);
                final int _tmpTipoRicorrenzaId;
                _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
                _tmpRicorrenza.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
              } else {
                _tmpRicorrenza = null;
              }
              final TipoRicorrenzaEntity _tmpTipoRicorrenza;
              final Long _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
              }
              if (_tmpKey_1 != null) {
                _tmpTipoRicorrenza = _collectionTipoRicorrenza.get(_tmpKey_1);
              } else {
                _tmpTipoRicorrenza = null;
              }
              _item = new RicorrenzaConTipoEntity();
              _item.ricorrenza = _tmpRicorrenza;
              _item.tipoRicorrenza = _tmpTipoRicorrenza;
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<RicorrenzaConTipoEntity>> getRicorrenzeDelGiornoPerTipo(final int giorno,
      final int mese, final int tipoId) {
    final String _sql = "SELECT * FROM santi WHERE giorno = ? AND id_mesi = ? AND tipo_ricorrenza_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, giorno);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mese);
    _argIndex = 3;
    _statement.bindLong(_argIndex, tipoId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"tipo_ricorrenza",
        "santi"}, true, new Callable<List<RicorrenzaConTipoEntity>>() {
      @Override
      @Nullable
      public List<RicorrenzaConTipoEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
            final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
            final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
            final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
            final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
            final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
            final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
            final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
            final LongSparseArray<TipoRicorrenzaEntity> _collectionTipoRicorrenza = new LongSparseArray<TipoRicorrenzaEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
              }
              if (_tmpKey != null) {
                _collectionTipoRicorrenza.put(_tmpKey, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(_collectionTipoRicorrenza);
            final List<RicorrenzaConTipoEntity> _result = new ArrayList<RicorrenzaConTipoEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final RicorrenzaConTipoEntity _item;
              final RicorrenzaEntity _tmpRicorrenza;
              if (!(_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfIdMese) && _cursor.isNull(_cursorIndexOfGiorno) && _cursor.isNull(_cursorIndexOfNome) && _cursor.isNull(_cursorIndexOfBio) && _cursor.isNull(_cursorIndexOfImg) && _cursor.isNull(_cursorIndexOfPrefix) && _cursor.isNull(_cursorIndexOfSuffix) && _cursor.isNull(_cursorIndexOfTipoRicorrenzaId))) {
                _tmpRicorrenza = new RicorrenzaEntity();
                final int _tmpId;
                _tmpId = _cursor.getInt(_cursorIndexOfId);
                _tmpRicorrenza.setId(_tmpId);
                final int _tmpIdMese;
                _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
                _tmpRicorrenza.setIdMese(_tmpIdMese);
                final int _tmpGiorno;
                _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
                _tmpRicorrenza.setGiorno(_tmpGiorno);
                final String _tmpNome;
                if (_cursor.isNull(_cursorIndexOfNome)) {
                  _tmpNome = null;
                } else {
                  _tmpNome = _cursor.getString(_cursorIndexOfNome);
                }
                _tmpRicorrenza.setNome(_tmpNome);
                final String _tmpBio;
                if (_cursor.isNull(_cursorIndexOfBio)) {
                  _tmpBio = null;
                } else {
                  _tmpBio = _cursor.getString(_cursorIndexOfBio);
                }
                _tmpRicorrenza.setBio(_tmpBio);
                final String _tmpImg;
                if (_cursor.isNull(_cursorIndexOfImg)) {
                  _tmpImg = null;
                } else {
                  _tmpImg = _cursor.getString(_cursorIndexOfImg);
                }
                _tmpRicorrenza.setImg(_tmpImg);
                final String _tmpPrefix;
                if (_cursor.isNull(_cursorIndexOfPrefix)) {
                  _tmpPrefix = null;
                } else {
                  _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
                }
                _tmpRicorrenza.setPrefix(_tmpPrefix);
                final String _tmpSuffix;
                if (_cursor.isNull(_cursorIndexOfSuffix)) {
                  _tmpSuffix = null;
                } else {
                  _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
                }
                _tmpRicorrenza.setSuffix(_tmpSuffix);
                final int _tmpTipoRicorrenzaId;
                _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
                _tmpRicorrenza.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
              } else {
                _tmpRicorrenza = null;
              }
              final TipoRicorrenzaEntity _tmpTipoRicorrenza;
              final Long _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
              }
              if (_tmpKey_1 != null) {
                _tmpTipoRicorrenza = _collectionTipoRicorrenza.get(_tmpKey_1);
              } else {
                _tmpTipoRicorrenza = null;
              }
              _item = new RicorrenzaConTipoEntity();
              _item.ricorrenza = _tmpRicorrenza;
              _item.tipoRicorrenza = _tmpTipoRicorrenza;
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<RicorrenzaEntity> getRicorrenzaById(final int id) {
    final String _sql = "SELECT * FROM santi WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[] {"santi"}, true, new Callable<RicorrenzaEntity>() {
      @Override
      @Nullable
      public RicorrenzaEntity call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
            final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
            final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
            final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
            final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
            final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
            final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
            final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
            final RicorrenzaEntity _result;
            if (_cursor.moveToFirst()) {
              _result = new RicorrenzaEntity();
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              _result.setId(_tmpId);
              final int _tmpIdMese;
              _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
              _result.setIdMese(_tmpIdMese);
              final int _tmpGiorno;
              _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
              _result.setGiorno(_tmpGiorno);
              final String _tmpNome;
              if (_cursor.isNull(_cursorIndexOfNome)) {
                _tmpNome = null;
              } else {
                _tmpNome = _cursor.getString(_cursorIndexOfNome);
              }
              _result.setNome(_tmpNome);
              final String _tmpBio;
              if (_cursor.isNull(_cursorIndexOfBio)) {
                _tmpBio = null;
              } else {
                _tmpBio = _cursor.getString(_cursorIndexOfBio);
              }
              _result.setBio(_tmpBio);
              final String _tmpImg;
              if (_cursor.isNull(_cursorIndexOfImg)) {
                _tmpImg = null;
              } else {
                _tmpImg = _cursor.getString(_cursorIndexOfImg);
              }
              _result.setImg(_tmpImg);
              final String _tmpPrefix;
              if (_cursor.isNull(_cursorIndexOfPrefix)) {
                _tmpPrefix = null;
              } else {
                _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
              }
              _result.setPrefix(_tmpPrefix);
              final String _tmpSuffix;
              if (_cursor.isNull(_cursorIndexOfSuffix)) {
                _tmpSuffix = null;
              } else {
                _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
              }
              _result.setSuffix(_tmpSuffix);
              final int _tmpTipoRicorrenzaId;
              _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
              _result.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<RicorrenzaEntity>> cercaRicorrenzePerNome(final String nome) {
    final String _sql = "SELECT * FROM santi WHERE santo LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nome == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nome);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"santi"}, true, new Callable<List<RicorrenzaEntity>>() {
      @Override
      @Nullable
      public List<RicorrenzaEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
            final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
            final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
            final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
            final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
            final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
            final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
            final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
            final List<RicorrenzaEntity> _result = new ArrayList<RicorrenzaEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final RicorrenzaEntity _item;
              _item = new RicorrenzaEntity();
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              _item.setId(_tmpId);
              final int _tmpIdMese;
              _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
              _item.setIdMese(_tmpIdMese);
              final int _tmpGiorno;
              _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
              _item.setGiorno(_tmpGiorno);
              final String _tmpNome;
              if (_cursor.isNull(_cursorIndexOfNome)) {
                _tmpNome = null;
              } else {
                _tmpNome = _cursor.getString(_cursorIndexOfNome);
              }
              _item.setNome(_tmpNome);
              final String _tmpBio;
              if (_cursor.isNull(_cursorIndexOfBio)) {
                _tmpBio = null;
              } else {
                _tmpBio = _cursor.getString(_cursorIndexOfBio);
              }
              _item.setBio(_tmpBio);
              final String _tmpImg;
              if (_cursor.isNull(_cursorIndexOfImg)) {
                _tmpImg = null;
              } else {
                _tmpImg = _cursor.getString(_cursorIndexOfImg);
              }
              _item.setImg(_tmpImg);
              final String _tmpPrefix;
              if (_cursor.isNull(_cursorIndexOfPrefix)) {
                _tmpPrefix = null;
              } else {
                _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
              }
              _item.setPrefix(_tmpPrefix);
              final String _tmpSuffix;
              if (_cursor.isNull(_cursorIndexOfSuffix)) {
                _tmpSuffix = null;
              } else {
                _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
              }
              _item.setSuffix(_tmpSuffix);
              final int _tmpTipoRicorrenzaId;
              _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
              _item.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int getTotalItemCount() {
    final String _sql = "SELECT COUNT(*) FROM santi";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<RicorrenzaConTipoEntity>> ricercaAvanzata(final String nome,
      final Integer tipo, final Integer meseInizio, final Integer meseFine,
      final Integer giornoInizio, final Integer giornoFine) {
    final String _sql = "SELECT * FROM santi WHERE (? IS NULL OR santo LIKE '%' || ? || '%') AND (? IS NULL OR tipo_ricorrenza_id = ?) AND (? IS NULL OR id_mesi >= ?) AND (? IS NULL OR id_mesi <= ?) AND (? IS NULL OR giorno >= ?) AND (? IS NULL OR giorno <= ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 12);
    int _argIndex = 1;
    if (nome == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nome);
    }
    _argIndex = 2;
    if (nome == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nome);
    }
    _argIndex = 3;
    if (tipo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, tipo);
    }
    _argIndex = 4;
    if (tipo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, tipo);
    }
    _argIndex = 5;
    if (meseInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseInizio);
    }
    _argIndex = 6;
    if (meseInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseInizio);
    }
    _argIndex = 7;
    if (meseFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseFine);
    }
    _argIndex = 8;
    if (meseFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseFine);
    }
    _argIndex = 9;
    if (giornoInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, giornoInizio);
    }
    _argIndex = 10;
    if (giornoInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, giornoInizio);
    }
    _argIndex = 11;
    if (giornoFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, giornoFine);
    }
    _argIndex = 12;
    if (giornoFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, giornoFine);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"tipo_ricorrenza",
        "santi"}, true, new Callable<List<RicorrenzaConTipoEntity>>() {
      @Override
      @Nullable
      public List<RicorrenzaConTipoEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
            final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
            final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
            final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
            final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
            final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
            final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
            final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
            final LongSparseArray<TipoRicorrenzaEntity> _collectionTipoRicorrenza = new LongSparseArray<TipoRicorrenzaEntity>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
              }
              if (_tmpKey != null) {
                _collectionTipoRicorrenza.put(_tmpKey, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(_collectionTipoRicorrenza);
            final List<RicorrenzaConTipoEntity> _result = new ArrayList<RicorrenzaConTipoEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final RicorrenzaConTipoEntity _item;
              final RicorrenzaEntity _tmpRicorrenza;
              if (!(_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfIdMese) && _cursor.isNull(_cursorIndexOfGiorno) && _cursor.isNull(_cursorIndexOfNome) && _cursor.isNull(_cursorIndexOfBio) && _cursor.isNull(_cursorIndexOfImg) && _cursor.isNull(_cursorIndexOfPrefix) && _cursor.isNull(_cursorIndexOfSuffix) && _cursor.isNull(_cursorIndexOfTipoRicorrenzaId))) {
                _tmpRicorrenza = new RicorrenzaEntity();
                final int _tmpId;
                _tmpId = _cursor.getInt(_cursorIndexOfId);
                _tmpRicorrenza.setId(_tmpId);
                final int _tmpIdMese;
                _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
                _tmpRicorrenza.setIdMese(_tmpIdMese);
                final int _tmpGiorno;
                _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
                _tmpRicorrenza.setGiorno(_tmpGiorno);
                final String _tmpNome;
                if (_cursor.isNull(_cursorIndexOfNome)) {
                  _tmpNome = null;
                } else {
                  _tmpNome = _cursor.getString(_cursorIndexOfNome);
                }
                _tmpRicorrenza.setNome(_tmpNome);
                final String _tmpBio;
                if (_cursor.isNull(_cursorIndexOfBio)) {
                  _tmpBio = null;
                } else {
                  _tmpBio = _cursor.getString(_cursorIndexOfBio);
                }
                _tmpRicorrenza.setBio(_tmpBio);
                final String _tmpImg;
                if (_cursor.isNull(_cursorIndexOfImg)) {
                  _tmpImg = null;
                } else {
                  _tmpImg = _cursor.getString(_cursorIndexOfImg);
                }
                _tmpRicorrenza.setImg(_tmpImg);
                final String _tmpPrefix;
                if (_cursor.isNull(_cursorIndexOfPrefix)) {
                  _tmpPrefix = null;
                } else {
                  _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
                }
                _tmpRicorrenza.setPrefix(_tmpPrefix);
                final String _tmpSuffix;
                if (_cursor.isNull(_cursorIndexOfSuffix)) {
                  _tmpSuffix = null;
                } else {
                  _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
                }
                _tmpRicorrenza.setSuffix(_tmpSuffix);
                final int _tmpTipoRicorrenzaId;
                _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
                _tmpRicorrenza.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
              } else {
                _tmpRicorrenza = null;
              }
              final TipoRicorrenzaEntity _tmpTipoRicorrenza;
              final Long _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
              }
              if (_tmpKey_1 != null) {
                _tmpTipoRicorrenza = _collectionTipoRicorrenza.get(_tmpKey_1);
              } else {
                _tmpTipoRicorrenza = null;
              }
              _item = new RicorrenzaConTipoEntity();
              _item.ricorrenza = _tmpRicorrenza;
              _item.tipoRicorrenza = _tmpTipoRicorrenza;
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<RicorrenzaConTipoEntity> ricercaAvanzataPaginata(final String nome,
      final Integer tipo, final Integer meseInizio, final Integer meseFine,
      final Integer giornoInizio, final Integer giornoFine, final int limit, final int offset) {
    final String _sql = "SELECT * FROM santi WHERE (? IS NULL OR santo LIKE '%' || ? || '%') AND (? IS NULL OR tipo_ricorrenza_id = ?) AND (? IS NULL OR id_mesi > ? OR (id_mesi = ? AND giorno >= ?)) AND (? IS NULL OR id_mesi < ? OR (id_mesi = ? AND giorno <= ?)) ORDER BY id_mesi, giorno LIMIT ? OFFSET ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 14);
    int _argIndex = 1;
    if (nome == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nome);
    }
    _argIndex = 2;
    if (nome == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nome);
    }
    _argIndex = 3;
    if (tipo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, tipo);
    }
    _argIndex = 4;
    if (tipo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, tipo);
    }
    _argIndex = 5;
    if (meseInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseInizio);
    }
    _argIndex = 6;
    if (meseInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseInizio);
    }
    _argIndex = 7;
    if (meseInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseInizio);
    }
    _argIndex = 8;
    if (giornoInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, giornoInizio);
    }
    _argIndex = 9;
    if (meseFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseFine);
    }
    _argIndex = 10;
    if (meseFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseFine);
    }
    _argIndex = 11;
    if (meseFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseFine);
    }
    _argIndex = 12;
    if (giornoFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, giornoFine);
    }
    _argIndex = 13;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 14;
    _statement.bindLong(_argIndex, offset);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
        final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
        final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
        final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
        final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
        final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
        final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
        final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
        final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
        final LongSparseArray<TipoRicorrenzaEntity> _collectionTipoRicorrenza = new LongSparseArray<TipoRicorrenzaEntity>();
        while (_cursor.moveToNext()) {
          final Long _tmpKey;
          if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
            _tmpKey = null;
          } else {
            _tmpKey = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
          }
          if (_tmpKey != null) {
            _collectionTipoRicorrenza.put(_tmpKey, null);
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(_collectionTipoRicorrenza);
        final List<RicorrenzaConTipoEntity> _result = new ArrayList<RicorrenzaConTipoEntity>(_cursor.getCount());
        while (_cursor.moveToNext()) {
          final RicorrenzaConTipoEntity _item;
          final RicorrenzaEntity _tmpRicorrenza;
          if (!(_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfIdMese) && _cursor.isNull(_cursorIndexOfGiorno) && _cursor.isNull(_cursorIndexOfNome) && _cursor.isNull(_cursorIndexOfBio) && _cursor.isNull(_cursorIndexOfImg) && _cursor.isNull(_cursorIndexOfPrefix) && _cursor.isNull(_cursorIndexOfSuffix) && _cursor.isNull(_cursorIndexOfTipoRicorrenzaId))) {
            _tmpRicorrenza = new RicorrenzaEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _tmpRicorrenza.setId(_tmpId);
            final int _tmpIdMese;
            _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
            _tmpRicorrenza.setIdMese(_tmpIdMese);
            final int _tmpGiorno;
            _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
            _tmpRicorrenza.setGiorno(_tmpGiorno);
            final String _tmpNome;
            if (_cursor.isNull(_cursorIndexOfNome)) {
              _tmpNome = null;
            } else {
              _tmpNome = _cursor.getString(_cursorIndexOfNome);
            }
            _tmpRicorrenza.setNome(_tmpNome);
            final String _tmpBio;
            if (_cursor.isNull(_cursorIndexOfBio)) {
              _tmpBio = null;
            } else {
              _tmpBio = _cursor.getString(_cursorIndexOfBio);
            }
            _tmpRicorrenza.setBio(_tmpBio);
            final String _tmpImg;
            if (_cursor.isNull(_cursorIndexOfImg)) {
              _tmpImg = null;
            } else {
              _tmpImg = _cursor.getString(_cursorIndexOfImg);
            }
            _tmpRicorrenza.setImg(_tmpImg);
            final String _tmpPrefix;
            if (_cursor.isNull(_cursorIndexOfPrefix)) {
              _tmpPrefix = null;
            } else {
              _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
            }
            _tmpRicorrenza.setPrefix(_tmpPrefix);
            final String _tmpSuffix;
            if (_cursor.isNull(_cursorIndexOfSuffix)) {
              _tmpSuffix = null;
            } else {
              _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
            }
            _tmpRicorrenza.setSuffix(_tmpSuffix);
            final int _tmpTipoRicorrenzaId;
            _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
            _tmpRicorrenza.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
          } else {
            _tmpRicorrenza = null;
          }
          final TipoRicorrenzaEntity _tmpTipoRicorrenza;
          final Long _tmpKey_1;
          if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
          }
          if (_tmpKey_1 != null) {
            _tmpTipoRicorrenza = _collectionTipoRicorrenza.get(_tmpKey_1);
          } else {
            _tmpTipoRicorrenza = null;
          }
          _item = new RicorrenzaConTipoEntity();
          _item.ricorrenza = _tmpRicorrenza;
          _item.tipoRicorrenza = _tmpTipoRicorrenza;
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int contaRisultatiRicercaAvanzata(final String nome, final Integer tipo,
      final Integer meseInizio, final Integer meseFine, final Integer giornoInizio,
      final Integer giornoFine) {
    final String _sql = "SELECT COUNT(*) FROM santi WHERE (? IS NULL OR santo LIKE '%' || ? || '%') AND (? IS NULL OR tipo_ricorrenza_id = ?) AND (? IS NULL OR id_mesi > ? OR (id_mesi = ? AND giorno >= ?)) AND (? IS NULL OR id_mesi < ? OR (id_mesi = ? AND giorno <= ?))";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 12);
    int _argIndex = 1;
    if (nome == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nome);
    }
    _argIndex = 2;
    if (nome == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nome);
    }
    _argIndex = 3;
    if (tipo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, tipo);
    }
    _argIndex = 4;
    if (tipo == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, tipo);
    }
    _argIndex = 5;
    if (meseInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseInizio);
    }
    _argIndex = 6;
    if (meseInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseInizio);
    }
    _argIndex = 7;
    if (meseInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseInizio);
    }
    _argIndex = 8;
    if (giornoInizio == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, giornoInizio);
    }
    _argIndex = 9;
    if (meseFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseFine);
    }
    _argIndex = 10;
    if (meseFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseFine);
    }
    _argIndex = 11;
    if (meseFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, meseFine);
    }
    _argIndex = 12;
    if (giornoFine == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, giornoFine);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<RicorrenzaEntity>> getAllRicorrenze() {
    final String _sql = "SELECT * FROM santi ORDER BY id_mesi, giorno";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"santi"}, true, new Callable<List<RicorrenzaEntity>>() {
      @Override
      @Nullable
      public List<RicorrenzaEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
            final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
            final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
            final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
            final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
            final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
            final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
            final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
            final List<RicorrenzaEntity> _result = new ArrayList<RicorrenzaEntity>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final RicorrenzaEntity _item;
              _item = new RicorrenzaEntity();
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              _item.setId(_tmpId);
              final int _tmpIdMese;
              _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
              _item.setIdMese(_tmpIdMese);
              final int _tmpGiorno;
              _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
              _item.setGiorno(_tmpGiorno);
              final String _tmpNome;
              if (_cursor.isNull(_cursorIndexOfNome)) {
                _tmpNome = null;
              } else {
                _tmpNome = _cursor.getString(_cursorIndexOfNome);
              }
              _item.setNome(_tmpNome);
              final String _tmpBio;
              if (_cursor.isNull(_cursorIndexOfBio)) {
                _tmpBio = null;
              } else {
                _tmpBio = _cursor.getString(_cursorIndexOfBio);
              }
              _item.setBio(_tmpBio);
              final String _tmpImg;
              if (_cursor.isNull(_cursorIndexOfImg)) {
                _tmpImg = null;
              } else {
                _tmpImg = _cursor.getString(_cursorIndexOfImg);
              }
              _item.setImg(_tmpImg);
              final String _tmpPrefix;
              if (_cursor.isNull(_cursorIndexOfPrefix)) {
                _tmpPrefix = null;
              } else {
                _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
              }
              _item.setPrefix(_tmpPrefix);
              final String _tmpSuffix;
              if (_cursor.isNull(_cursorIndexOfSuffix)) {
                _tmpSuffix = null;
              } else {
                _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
              }
              _item.setSuffix(_tmpSuffix);
              final int _tmpTipoRicorrenzaId;
              _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
              _item.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<RicorrenzaConTipoEntity> getRicorrenzeDelGiornoPaginate(final int giorno,
      final int mese, final int offset, final int limit) {
    final String _sql = "SELECT * FROM santi WHERE giorno = ? AND id_mesi = ? ORDER BY RANDOM() LIMIT ? OFFSET ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, giorno);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mese);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 4;
    _statement.bindLong(_argIndex, offset);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
        final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
        final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
        final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
        final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
        final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
        final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
        final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
        final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
        final LongSparseArray<TipoRicorrenzaEntity> _collectionTipoRicorrenza = new LongSparseArray<TipoRicorrenzaEntity>();
        while (_cursor.moveToNext()) {
          final Long _tmpKey;
          if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
            _tmpKey = null;
          } else {
            _tmpKey = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
          }
          if (_tmpKey != null) {
            _collectionTipoRicorrenza.put(_tmpKey, null);
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(_collectionTipoRicorrenza);
        final List<RicorrenzaConTipoEntity> _result = new ArrayList<RicorrenzaConTipoEntity>(_cursor.getCount());
        while (_cursor.moveToNext()) {
          final RicorrenzaConTipoEntity _item;
          final RicorrenzaEntity _tmpRicorrenza;
          if (!(_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfIdMese) && _cursor.isNull(_cursorIndexOfGiorno) && _cursor.isNull(_cursorIndexOfNome) && _cursor.isNull(_cursorIndexOfBio) && _cursor.isNull(_cursorIndexOfImg) && _cursor.isNull(_cursorIndexOfPrefix) && _cursor.isNull(_cursorIndexOfSuffix) && _cursor.isNull(_cursorIndexOfTipoRicorrenzaId))) {
            _tmpRicorrenza = new RicorrenzaEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _tmpRicorrenza.setId(_tmpId);
            final int _tmpIdMese;
            _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
            _tmpRicorrenza.setIdMese(_tmpIdMese);
            final int _tmpGiorno;
            _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
            _tmpRicorrenza.setGiorno(_tmpGiorno);
            final String _tmpNome;
            if (_cursor.isNull(_cursorIndexOfNome)) {
              _tmpNome = null;
            } else {
              _tmpNome = _cursor.getString(_cursorIndexOfNome);
            }
            _tmpRicorrenza.setNome(_tmpNome);
            final String _tmpBio;
            if (_cursor.isNull(_cursorIndexOfBio)) {
              _tmpBio = null;
            } else {
              _tmpBio = _cursor.getString(_cursorIndexOfBio);
            }
            _tmpRicorrenza.setBio(_tmpBio);
            final String _tmpImg;
            if (_cursor.isNull(_cursorIndexOfImg)) {
              _tmpImg = null;
            } else {
              _tmpImg = _cursor.getString(_cursorIndexOfImg);
            }
            _tmpRicorrenza.setImg(_tmpImg);
            final String _tmpPrefix;
            if (_cursor.isNull(_cursorIndexOfPrefix)) {
              _tmpPrefix = null;
            } else {
              _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
            }
            _tmpRicorrenza.setPrefix(_tmpPrefix);
            final String _tmpSuffix;
            if (_cursor.isNull(_cursorIndexOfSuffix)) {
              _tmpSuffix = null;
            } else {
              _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
            }
            _tmpRicorrenza.setSuffix(_tmpSuffix);
            final int _tmpTipoRicorrenzaId;
            _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
            _tmpRicorrenza.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
          } else {
            _tmpRicorrenza = null;
          }
          final TipoRicorrenzaEntity _tmpTipoRicorrenza;
          final Long _tmpKey_1;
          if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
          }
          if (_tmpKey_1 != null) {
            _tmpTipoRicorrenza = _collectionTipoRicorrenza.get(_tmpKey_1);
          } else {
            _tmpTipoRicorrenza = null;
          }
          _item = new RicorrenzaConTipoEntity();
          _item.ricorrenza = _tmpRicorrenza;
          _item.tipoRicorrenza = _tmpTipoRicorrenza;
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int getCountRicorrenzeDelGiorno(final int giorno, final int mese) {
    final String _sql = "SELECT COUNT(*) FROM santi WHERE giorno = ? AND id_mesi = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, giorno);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mese);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RicorrenzaConTipoEntity> getRicorrenzeDelGiornoPerTipoPaginate(final int giorno,
      final int mese, final int tipoId, final int offset, final int limit) {
    final String _sql = "SELECT * FROM santi WHERE giorno = ? AND id_mesi = ? AND tipo_ricorrenza_id = ? LIMIT ? OFFSET ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, giorno);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mese);
    _argIndex = 3;
    _statement.bindLong(_argIndex, tipoId);
    _argIndex = 4;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 5;
    _statement.bindLong(_argIndex, offset);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
        final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
        final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
        final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
        final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
        final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
        final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
        final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
        final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
        final LongSparseArray<TipoRicorrenzaEntity> _collectionTipoRicorrenza = new LongSparseArray<TipoRicorrenzaEntity>();
        while (_cursor.moveToNext()) {
          final Long _tmpKey;
          if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
            _tmpKey = null;
          } else {
            _tmpKey = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
          }
          if (_tmpKey != null) {
            _collectionTipoRicorrenza.put(_tmpKey, null);
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(_collectionTipoRicorrenza);
        final List<RicorrenzaConTipoEntity> _result = new ArrayList<RicorrenzaConTipoEntity>(_cursor.getCount());
        while (_cursor.moveToNext()) {
          final RicorrenzaConTipoEntity _item;
          final RicorrenzaEntity _tmpRicorrenza;
          if (!(_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfIdMese) && _cursor.isNull(_cursorIndexOfGiorno) && _cursor.isNull(_cursorIndexOfNome) && _cursor.isNull(_cursorIndexOfBio) && _cursor.isNull(_cursorIndexOfImg) && _cursor.isNull(_cursorIndexOfPrefix) && _cursor.isNull(_cursorIndexOfSuffix) && _cursor.isNull(_cursorIndexOfTipoRicorrenzaId))) {
            _tmpRicorrenza = new RicorrenzaEntity();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _tmpRicorrenza.setId(_tmpId);
            final int _tmpIdMese;
            _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
            _tmpRicorrenza.setIdMese(_tmpIdMese);
            final int _tmpGiorno;
            _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
            _tmpRicorrenza.setGiorno(_tmpGiorno);
            final String _tmpNome;
            if (_cursor.isNull(_cursorIndexOfNome)) {
              _tmpNome = null;
            } else {
              _tmpNome = _cursor.getString(_cursorIndexOfNome);
            }
            _tmpRicorrenza.setNome(_tmpNome);
            final String _tmpBio;
            if (_cursor.isNull(_cursorIndexOfBio)) {
              _tmpBio = null;
            } else {
              _tmpBio = _cursor.getString(_cursorIndexOfBio);
            }
            _tmpRicorrenza.setBio(_tmpBio);
            final String _tmpImg;
            if (_cursor.isNull(_cursorIndexOfImg)) {
              _tmpImg = null;
            } else {
              _tmpImg = _cursor.getString(_cursorIndexOfImg);
            }
            _tmpRicorrenza.setImg(_tmpImg);
            final String _tmpPrefix;
            if (_cursor.isNull(_cursorIndexOfPrefix)) {
              _tmpPrefix = null;
            } else {
              _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
            }
            _tmpRicorrenza.setPrefix(_tmpPrefix);
            final String _tmpSuffix;
            if (_cursor.isNull(_cursorIndexOfSuffix)) {
              _tmpSuffix = null;
            } else {
              _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
            }
            _tmpRicorrenza.setSuffix(_tmpSuffix);
            final int _tmpTipoRicorrenzaId;
            _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
            _tmpRicorrenza.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
          } else {
            _tmpRicorrenza = null;
          }
          final TipoRicorrenzaEntity _tmpTipoRicorrenza;
          final Long _tmpKey_1;
          if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
          }
          if (_tmpKey_1 != null) {
            _tmpTipoRicorrenza = _collectionTipoRicorrenza.get(_tmpKey_1);
          } else {
            _tmpTipoRicorrenza = null;
          }
          _item = new RicorrenzaConTipoEntity();
          _item.ricorrenza = _tmpRicorrenza;
          _item.tipoRicorrenza = _tmpTipoRicorrenza;
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int getCountRicorrenzeDelGiornoPerTipo(final int giorno, final int mese,
      final int tipoId) {
    final String _sql = "SELECT COUNT(*) FROM santi WHERE giorno = ? AND id_mesi = ? AND tipo_ricorrenza_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, giorno);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mese);
    _argIndex = 3;
    _statement.bindLong(_argIndex, tipoId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int contaRicorrenzePerGiornoMese(final int giorno, final int mese) {
    final String _sql = "SELECT COUNT(*) FROM santi WHERE giorno = ? AND id_mesi = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, giorno);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mese);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RicorrenzaEntity> getRicorrenzePerGiornoMese(final int giorno, final int mese) {
    final String _sql = "SELECT * FROM santi WHERE giorno = ? AND id_mesi = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, giorno);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mese);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
      final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
      final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
      final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
      final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
      final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
      final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
      final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
      final List<RicorrenzaEntity> _result = new ArrayList<RicorrenzaEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RicorrenzaEntity _item;
        _item = new RicorrenzaEntity();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpIdMese;
        _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
        _item.setIdMese(_tmpIdMese);
        final int _tmpGiorno;
        _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
        _item.setGiorno(_tmpGiorno);
        final String _tmpNome;
        if (_cursor.isNull(_cursorIndexOfNome)) {
          _tmpNome = null;
        } else {
          _tmpNome = _cursor.getString(_cursorIndexOfNome);
        }
        _item.setNome(_tmpNome);
        final String _tmpBio;
        if (_cursor.isNull(_cursorIndexOfBio)) {
          _tmpBio = null;
        } else {
          _tmpBio = _cursor.getString(_cursorIndexOfBio);
        }
        _item.setBio(_tmpBio);
        final String _tmpImg;
        if (_cursor.isNull(_cursorIndexOfImg)) {
          _tmpImg = null;
        } else {
          _tmpImg = _cursor.getString(_cursorIndexOfImg);
        }
        _item.setImg(_tmpImg);
        final String _tmpPrefix;
        if (_cursor.isNull(_cursorIndexOfPrefix)) {
          _tmpPrefix = null;
        } else {
          _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
        }
        _item.setPrefix(_tmpPrefix);
        final String _tmpSuffix;
        if (_cursor.isNull(_cursorIndexOfSuffix)) {
          _tmpSuffix = null;
        } else {
          _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
        }
        _item.setSuffix(_tmpSuffix);
        final int _tmpTipoRicorrenzaId;
        _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
        _item.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RicorrenzaEntity> debugRicorrenzeDelGiorno(final int giorno, final int mese) {
    final String _sql = "SELECT * FROM santi WHERE giorno = ? AND id_mesi = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, giorno);
    _argIndex = 2;
    _statement.bindLong(_argIndex, mese);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIdMese = CursorUtil.getColumnIndexOrThrow(_cursor, "id_mesi");
      final int _cursorIndexOfGiorno = CursorUtil.getColumnIndexOrThrow(_cursor, "giorno");
      final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "santo");
      final int _cursorIndexOfBio = CursorUtil.getColumnIndexOrThrow(_cursor, "bio");
      final int _cursorIndexOfImg = CursorUtil.getColumnIndexOrThrow(_cursor, "img");
      final int _cursorIndexOfPrefix = CursorUtil.getColumnIndexOrThrow(_cursor, "prefix");
      final int _cursorIndexOfSuffix = CursorUtil.getColumnIndexOrThrow(_cursor, "suffix");
      final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo_ricorrenza_id");
      final List<RicorrenzaEntity> _result = new ArrayList<RicorrenzaEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RicorrenzaEntity _item;
        _item = new RicorrenzaEntity();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpIdMese;
        _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
        _item.setIdMese(_tmpIdMese);
        final int _tmpGiorno;
        _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
        _item.setGiorno(_tmpGiorno);
        final String _tmpNome;
        if (_cursor.isNull(_cursorIndexOfNome)) {
          _tmpNome = null;
        } else {
          _tmpNome = _cursor.getString(_cursorIndexOfNome);
        }
        _item.setNome(_tmpNome);
        final String _tmpBio;
        if (_cursor.isNull(_cursorIndexOfBio)) {
          _tmpBio = null;
        } else {
          _tmpBio = _cursor.getString(_cursorIndexOfBio);
        }
        _item.setBio(_tmpBio);
        final String _tmpImg;
        if (_cursor.isNull(_cursorIndexOfImg)) {
          _tmpImg = null;
        } else {
          _tmpImg = _cursor.getString(_cursorIndexOfImg);
        }
        _item.setImg(_tmpImg);
        final String _tmpPrefix;
        if (_cursor.isNull(_cursorIndexOfPrefix)) {
          _tmpPrefix = null;
        } else {
          _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
        }
        _item.setPrefix(_tmpPrefix);
        final String _tmpSuffix;
        if (_cursor.isNull(_cursorIndexOfSuffix)) {
          _tmpSuffix = null;
        } else {
          _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
        }
        _item.setSuffix(_tmpSuffix);
        final int _tmpTipoRicorrenzaId;
        _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
        _item.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<RicorrenzaConTipoEntity> eseguiRicercaAvanzata(final SupportSQLiteQuery query) {
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, query, true, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndex(_cursor, "id");
      final int _cursorIndexOfIdMese = CursorUtil.getColumnIndex(_cursor, "id_mesi");
      final int _cursorIndexOfGiorno = CursorUtil.getColumnIndex(_cursor, "giorno");
      final int _cursorIndexOfNome = CursorUtil.getColumnIndex(_cursor, "santo");
      final int _cursorIndexOfBio = CursorUtil.getColumnIndex(_cursor, "bio");
      final int _cursorIndexOfImg = CursorUtil.getColumnIndex(_cursor, "img");
      final int _cursorIndexOfPrefix = CursorUtil.getColumnIndex(_cursor, "prefix");
      final int _cursorIndexOfSuffix = CursorUtil.getColumnIndex(_cursor, "suffix");
      final int _cursorIndexOfTipoRicorrenzaId = CursorUtil.getColumnIndex(_cursor, "tipo_ricorrenza_id");
      final LongSparseArray<TipoRicorrenzaEntity> _collectionTipoRicorrenza = new LongSparseArray<TipoRicorrenzaEntity>();
      while (_cursor.moveToNext()) {
        final Long _tmpKey;
        if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
          _tmpKey = null;
        } else {
          _tmpKey = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
        }
        if (_tmpKey != null) {
          _collectionTipoRicorrenza.put(_tmpKey, null);
        }
      }
      _cursor.moveToPosition(-1);
      __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(_collectionTipoRicorrenza);
      final List<RicorrenzaConTipoEntity> _result = new ArrayList<RicorrenzaConTipoEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RicorrenzaConTipoEntity _item;
        final RicorrenzaEntity _tmpRicorrenza;
        if (!((_cursorIndexOfId == -1 || _cursor.isNull(_cursorIndexOfId)) && (_cursorIndexOfIdMese == -1 || _cursor.isNull(_cursorIndexOfIdMese)) && (_cursorIndexOfGiorno == -1 || _cursor.isNull(_cursorIndexOfGiorno)) && (_cursorIndexOfNome == -1 || _cursor.isNull(_cursorIndexOfNome)) && (_cursorIndexOfBio == -1 || _cursor.isNull(_cursorIndexOfBio)) && (_cursorIndexOfImg == -1 || _cursor.isNull(_cursorIndexOfImg)) && (_cursorIndexOfPrefix == -1 || _cursor.isNull(_cursorIndexOfPrefix)) && (_cursorIndexOfSuffix == -1 || _cursor.isNull(_cursorIndexOfSuffix)) && (_cursorIndexOfTipoRicorrenzaId == -1 || _cursor.isNull(_cursorIndexOfTipoRicorrenzaId)))) {
          _tmpRicorrenza = new RicorrenzaEntity();
          if (_cursorIndexOfId != -1) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _tmpRicorrenza.setId(_tmpId);
          }
          if (_cursorIndexOfIdMese != -1) {
            final int _tmpIdMese;
            _tmpIdMese = _cursor.getInt(_cursorIndexOfIdMese);
            _tmpRicorrenza.setIdMese(_tmpIdMese);
          }
          if (_cursorIndexOfGiorno != -1) {
            final int _tmpGiorno;
            _tmpGiorno = _cursor.getInt(_cursorIndexOfGiorno);
            _tmpRicorrenza.setGiorno(_tmpGiorno);
          }
          if (_cursorIndexOfNome != -1) {
            final String _tmpNome;
            if (_cursor.isNull(_cursorIndexOfNome)) {
              _tmpNome = null;
            } else {
              _tmpNome = _cursor.getString(_cursorIndexOfNome);
            }
            _tmpRicorrenza.setNome(_tmpNome);
          }
          if (_cursorIndexOfBio != -1) {
            final String _tmpBio;
            if (_cursor.isNull(_cursorIndexOfBio)) {
              _tmpBio = null;
            } else {
              _tmpBio = _cursor.getString(_cursorIndexOfBio);
            }
            _tmpRicorrenza.setBio(_tmpBio);
          }
          if (_cursorIndexOfImg != -1) {
            final String _tmpImg;
            if (_cursor.isNull(_cursorIndexOfImg)) {
              _tmpImg = null;
            } else {
              _tmpImg = _cursor.getString(_cursorIndexOfImg);
            }
            _tmpRicorrenza.setImg(_tmpImg);
          }
          if (_cursorIndexOfPrefix != -1) {
            final String _tmpPrefix;
            if (_cursor.isNull(_cursorIndexOfPrefix)) {
              _tmpPrefix = null;
            } else {
              _tmpPrefix = _cursor.getString(_cursorIndexOfPrefix);
            }
            _tmpRicorrenza.setPrefix(_tmpPrefix);
          }
          if (_cursorIndexOfSuffix != -1) {
            final String _tmpSuffix;
            if (_cursor.isNull(_cursorIndexOfSuffix)) {
              _tmpSuffix = null;
            } else {
              _tmpSuffix = _cursor.getString(_cursorIndexOfSuffix);
            }
            _tmpRicorrenza.setSuffix(_tmpSuffix);
          }
          if (_cursorIndexOfTipoRicorrenzaId != -1) {
            final int _tmpTipoRicorrenzaId;
            _tmpTipoRicorrenzaId = _cursor.getInt(_cursorIndexOfTipoRicorrenzaId);
            _tmpRicorrenza.setTipoRicorrenzaId(_tmpTipoRicorrenzaId);
          }
        } else {
          _tmpRicorrenza = null;
        }
        final TipoRicorrenzaEntity _tmpTipoRicorrenza;
        final Long _tmpKey_1;
        if (_cursor.isNull(_cursorIndexOfTipoRicorrenzaId)) {
          _tmpKey_1 = null;
        } else {
          _tmpKey_1 = _cursor.getLong(_cursorIndexOfTipoRicorrenzaId);
        }
        if (_tmpKey_1 != null) {
          _tmpTipoRicorrenza = _collectionTipoRicorrenza.get(_tmpKey_1);
        } else {
          _tmpTipoRicorrenza = null;
        }
        _item = new RicorrenzaConTipoEntity();
        _item.ricorrenza = _tmpRicorrenza;
        _item.tipoRicorrenza = _tmpTipoRicorrenza;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(
      @NonNull final LongSparseArray<TipoRicorrenzaEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshiptipoRicorrenzaAsitFaustobeSantibailorDataLocalEntitiesTipoRicorrenzaEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`santo` FROM `tipo_ricorrenza` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfNome = 1;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final TipoRicorrenzaEntity _item_1;
          final int _tmpId;
          _tmpId = _cursor.getInt(_cursorIndexOfId);
          final String _tmpNome;
          if (_cursor.isNull(_cursorIndexOfNome)) {
            _tmpNome = null;
          } else {
            _tmpNome = _cursor.getString(_cursorIndexOfNome);
          }
          _item_1 = new TipoRicorrenzaEntity(_tmpId,_tmpNome);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
