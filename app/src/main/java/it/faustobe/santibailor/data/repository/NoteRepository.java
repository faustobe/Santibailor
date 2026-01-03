package it.faustobe.santibailor.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.faustobe.santibailor.data.local.dao.NoteDao;
import it.faustobe.santibailor.data.local.entities.NoteEntity;
import it.faustobe.santibailor.data.mapper.NotaMapper;
import it.faustobe.santibailor.domain.model.Nota;

/**
 * Repository per gestire le note
 * Gestisce l'accesso ai dati (Room database) e fornisce un'interfaccia pulita
 * al domain layer
 */
@Singleton
public class NoteRepository {

    private final NoteDao noteDao;
    private final ExecutorService executorService;

    @Inject
    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Ottiene tutte le note ordinate per data modifica (più recente prima)
     * @return LiveData con lista di note
     */
    public LiveData<List<Nota>> getAllNote() {
        return Transformations.map(
                noteDao.getAllNote(),
                NotaMapper::toDomainList
        );
    }

    /**
     * Ottiene una nota per ID
     * @param id ID della nota
     * @return LiveData con la nota
     */
    public LiveData<Nota> getNotaById(int id) {
        return Transformations.map(
                noteDao.getNotaById(id),
                NotaMapper::toDomain
        );
    }

    /**
     * Inserisce una nuova nota
     * @param nota Nota da inserire
     * @param listener Callback per risultato operazione
     */
    public void insertNota(Nota nota, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                Log.d("NoteRepository", "Inserting nota: " + nota.getTitolo());
                NoteEntity entity = NotaMapper.toEntity(nota);
                long id = noteDao.insert(entity);
                Log.d("NoteRepository", "Nota inserted with ID: " + id);
                if (listener != null) {
                    listener.onSuccess((int) id);
                }
            } catch (Exception e) {
                Log.e("NoteRepository", "Error inserting nota", e);
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Aggiorna una nota esistente
     * @param nota Nota da aggiornare
     * @param listener Callback per risultato operazione
     */
    public void updateNota(Nota nota, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                Log.d("NoteRepository", "Updating nota ID: " + nota.getId() + " - " + nota.getTitolo());
                NoteEntity entity = NotaMapper.toEntity(nota);
                noteDao.update(entity);
                Log.d("NoteRepository", "Nota updated successfully");
                if (listener != null) {
                    listener.onSuccess(nota.getId());
                }
            } catch (Exception e) {
                Log.e("NoteRepository", "Error updating nota", e);
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Elimina una nota
     * @param nota Nota da eliminare
     * @param listener Callback per risultato operazione
     */
    public void deleteNota(Nota nota, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                NoteEntity entity = NotaMapper.toEntity(nota);
                noteDao.delete(entity);
                if (listener != null) {
                    listener.onSuccess(nota.getId());
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Callback interface per operazioni asincrone
     */
    public interface OnOperationCompleteListener {
        void onSuccess(int id);
        void onError(String error);
    }

    /**
     * Shutdown dell'executor service
     * Da chiamare quando il repository non è più necessario
     */
    public void shutdown() {
        executorService.shutdown();
    }
}
