package it.faustobe.santibailor.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.faustobe.santibailor.data.entities.TipoRicorrenza;

@Dao
public interface TipoRicorrenzaDao {
    @Query("SELECT * FROM tipo_ricorrenza")
    LiveData<List<TipoRicorrenza>> getAllTipiRicorrenza();

    @Insert
    void insert(TipoRicorrenza tipoRicorrenza);
}
