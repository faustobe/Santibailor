package it.faustobe.santibailor.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import it.faustobe.santibailor.data.local.entities.NoteEntity;

/**
 * DAO per la gestione delle operazioni su note nel database
 */
@Dao
public interface NoteDao extends BaseDao<NoteEntity> {

    /**
     * Ottiene tutte le note ordinate per data modifica (più recente prima)
     * @return LiveData con la lista di note
     */
    @Query("SELECT * FROM note ORDER BY data_modifica DESC")
    LiveData<List<NoteEntity>> getAllNote();

    /**
     * Ottiene tutte le note (sincrono) ordinate per data modifica
     * Usato nei UseCase per operazioni sincrone
     * @return Lista di note
     */
    @Query("SELECT * FROM note ORDER BY data_modifica DESC")
    List<NoteEntity> getAllNoteSync();

    /**
     * Ottiene una nota per ID (LiveData per osservazione reattiva)
     * @param id ID della nota
     * @return LiveData con la nota
     */
    @Query("SELECT * FROM note WHERE id = :id")
    LiveData<NoteEntity> getNotaById(int id);

    /**
     * Ottiene una nota per ID (sincrono)
     * @param id ID della nota
     * @return La nota o null se non trovata
     */
    @Query("SELECT * FROM note WHERE id = :id")
    NoteEntity getNotaByIdSync(int id);

    /**
     * Conta il numero totale di note
     * @return Numero di note nel database
     */
    @Query("SELECT COUNT(*) FROM note")
    int getCountNote();

    /**
     * Elimina tutte le note
     * Utile per testing o reset
     */
    @Query("DELETE FROM note")
    void deleteAllNote();
}
