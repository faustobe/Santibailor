package it.faustobe.santibailor.presentation.features.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import it.faustobe.santibailor.data.repository.NoteRepository;
import it.faustobe.santibailor.domain.model.Nota;
import it.faustobe.santibailor.domain.usecase.DeleteNotaUseCase;
import it.faustobe.santibailor.domain.usecase.GetAllNoteUseCase;
import it.faustobe.santibailor.domain.usecase.GetNotaByIdUseCase;
import it.faustobe.santibailor.domain.usecase.InsertNotaUseCase;
import it.faustobe.santibailor.domain.usecase.UpdateNotaUseCase;

/**
 * ViewModel per la gestione delle note
 * Gestisce lo stato UI e coordina le operazioni tramite UseCase
 */
@HiltViewModel
public class NoteViewModel extends ViewModel {

    private final GetAllNoteUseCase getAllNoteUseCase;
    private final GetNotaByIdUseCase getNotaByIdUseCase;
    private final InsertNotaUseCase insertNotaUseCase;
    private final UpdateNotaUseCase updateNotaUseCase;
    private final DeleteNotaUseCase deleteNotaUseCase;

    // LiveData per stato UI
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> notaCreatedId = new MutableLiveData<>();

    @Inject
    public NoteViewModel(
            GetAllNoteUseCase getAllNoteUseCase,
            GetNotaByIdUseCase getNotaByIdUseCase,
            InsertNotaUseCase insertNotaUseCase,
            UpdateNotaUseCase updateNotaUseCase,
            DeleteNotaUseCase deleteNotaUseCase) {
        this.getAllNoteUseCase = getAllNoteUseCase;
        this.getNotaByIdUseCase = getNotaByIdUseCase;
        this.insertNotaUseCase = insertNotaUseCase;
        this.updateNotaUseCase = updateNotaUseCase;
        this.deleteNotaUseCase = deleteNotaUseCase;
    }

    // ========== LiveData Getters ==========

    /**
     * Ottiene tutte le note ordinate per data modifica
     */
    public LiveData<List<Nota>> getAllNote() {
        return getAllNoteUseCase.execute(null);
    }

    /**
     * Ottiene una nota per ID
     */
    public LiveData<Nota> getNotaById(int id) {
        return getNotaByIdUseCase.execute(id);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<Integer> getNotaCreatedId() {
        return notaCreatedId;
    }

    // ========== Note Operations ==========

    /**
     * Crea una nuova nota
     */
    public void createNota(String titolo, String contenuto) {
        if (titolo == null || titolo.trim().isEmpty()) {
            errorMessage.setValue("Il titolo non può essere vuoto");
            return;
        }

        isLoading.setValue(true);
        long now = System.currentTimeMillis();
        Nota nota = new Nota(0, titolo, contenuto != null ? contenuto : "", now, now);

        insertNotaUseCase.execute(nota, new NoteRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int id) {
                isLoading.postValue(false);
                successMessage.postValue("Nota creata");
                notaCreatedId.postValue(id);
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Errore: " + error);
            }
        });
    }

    /**
     * Aggiorna una nota esistente
     */
    public void updateNota(Nota nota) {
        if (nota == null) {
            errorMessage.setValue("Nota non valida");
            return;
        }

        if (nota.getTitolo() == null || nota.getTitolo().trim().isEmpty()) {
            errorMessage.setValue("Il titolo non può essere vuoto");
            return;
        }

        isLoading.setValue(true);
        // Aggiorna data modifica
        nota.setDataModifica(System.currentTimeMillis());

        updateNotaUseCase.execute(nota, new NoteRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int id) {
                isLoading.postValue(false);
                successMessage.postValue("Nota salvata");
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Errore: " + error);
            }
        });
    }

    /**
     * Elimina una nota
     */
    public void deleteNota(Nota nota) {
        if (nota == null) {
            errorMessage.setValue("Nota non valida");
            return;
        }

        isLoading.setValue(true);
        deleteNotaUseCase.execute(nota, new NoteRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int id) {
                isLoading.postValue(false);
                successMessage.postValue("Nota eliminata");
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                errorMessage.postValue("Errore: " + error);
            }
        });
    }

    // ========== Utility ==========

    /**
     * Pulisce i messaggi di errore e successo
     */
    public void clearMessages() {
        errorMessage.setValue(null);
        successMessage.setValue(null);
    }
}
