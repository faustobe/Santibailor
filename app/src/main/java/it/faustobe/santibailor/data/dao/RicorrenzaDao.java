package it.faustobe.santibailor.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import it.faustobe.santibailor.data.entities.Ricorrenza;
import it.faustobe.santibailor.data.entities.RicorrenzaConTipo;

@Dao
public interface RicorrenzaDao {
    /*@Query("SELECT * FROM santi WHERE giorno = :giorno AND id_mesi = :mese")
    LiveData<List<Ricorrenza>> getRicorrenzeDelGiorno(int giorno, int mese);*/

    @Transaction
    @Query("SELECT santi.* FROM santi WHERE giorno = :giorno AND id_mesi = :mese")
    LiveData<List<RicorrenzaConTipo>> getRicorrenzeDelGiorno(int giorno, int mese);

    @Transaction
    @Query("SELECT santi.* FROM santi WHERE giorno = :giorno AND id_mesi = :mese AND tipo_ricorrenza_id = :tipoId")
    LiveData<List<RicorrenzaConTipo>> getRicorrenzeDelGiornoPerTipo(int giorno, int mese, int tipoId);

    @Transaction
    @Query("SELECT * FROM santi WHERE tipo_ricorrenza_id = :tipo")
    LiveData<List<Ricorrenza>> getRicorrenzePerTipo(String tipo);

    @Transaction
    @Query("SELECT * FROM santi WHERE id = :id")
    LiveData<Ricorrenza> getRicorrenzaById(int id);

    @Transaction
    @Query("SELECT * FROM santi WHERE santo LIKE :nome")
    LiveData<List<Ricorrenza>> cercaRicorrenzePerNome(String nome);

    @Transaction
    @Query("SELECT * FROM santi ORDER BY id_mesi, giorno")
    LiveData<List<Ricorrenza>> getAllRicorrenze();

    @Insert
    void insert(Ricorrenza ricorrenza);

    @Update
    void update(Ricorrenza ricorrenza);

    @Delete
    void delete(Ricorrenza ricorrenza);

    @Insert
    void insertMultiple(List<Ricorrenza> ricorrenze);

    @Delete
    void deleteMultiple(List<Ricorrenza> ricorrenze);
}