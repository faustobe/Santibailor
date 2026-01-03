package it.faustobe.santibailor.domain.usecase;

import javax.inject.Inject;

import it.faustobe.santibailor.data.repository.NoteRepository;
import it.faustobe.santibailor.domain.model.Nota;

/**
 * UseCase per aggiornare una nota esistente
 */
public class UpdateNotaUseCase {

    private final NoteRepository repository;

    @Inject
    public UpdateNotaUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    public void execute(Nota nota, NoteRepository.OnOperationCompleteListener listener) {
        repository.updateNota(nota, listener);
    }
}
