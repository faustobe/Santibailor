package it.faustobe.santibailor.domain.usecase;

import javax.inject.Inject;

import it.faustobe.santibailor.data.repository.NoteRepository;
import it.faustobe.santibailor.domain.model.Nota;

/**
 * UseCase per inserire una nuova nota
 */
public class InsertNotaUseCase {

    private final NoteRepository repository;

    @Inject
    public InsertNotaUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    public void execute(Nota nota, NoteRepository.OnOperationCompleteListener listener) {
        repository.insertNota(nota, listener);
    }
}
