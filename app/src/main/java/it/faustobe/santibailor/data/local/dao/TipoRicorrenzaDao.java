package it.faustobe.santibailor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.faustobe.santibailor.data.local.entities.TipoRicorrenzaEntity;

@Dao
public interface TipoRicorrenzaDao {

    @Query("SELECT * FROM tipo_ricorrenza ORDER BY id")
    List<TipoRicorrenzaEntity> getAllTipiRicorrenza();

    @Insert
    void insert(TipoRicorrenzaEntity tipoRicorrenzaEntity);

    @Query("SELECT * FROM tipo_ricorrenza WHERE id = :id")
    LiveData<TipoRicorrenzaEntity> getTipoRicorrenzaById(int id);
}
