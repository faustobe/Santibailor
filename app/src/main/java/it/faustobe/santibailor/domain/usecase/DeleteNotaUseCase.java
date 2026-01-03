package it.faustobe.santibailor.domain.usecase;

import javax.inject.Inject;

import it.faustobe.santibailor.data.repository.NoteRepository;
import it.faustobe.santibailor.domain.model.Nota;

/**
 * UseCase per eliminare una nota
 */
public class DeleteNotaUseCase {

    private final NoteRepository repository;

    @Inject
    public DeleteNotaUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    public void execute(Nota nota, NoteRepository.OnOperationCompleteListener listener) {
        repository.deleteNota(nota, listener);
    }
}
