package it.faustobe.santibailor.domain.usecase;

import androidx.lifecycle.LiveData;

import javax.inject.Inject;

import it.faustobe.santibailor.data.repository.NoteRepository;
import it.faustobe.santibailor.domain.model.Nota;

/**
 * UseCase per ottenere una nota per ID
 */
public class GetNotaByIdUseCase implements BaseUseCase<Integer, LiveData<Nota>> {

    private final NoteRepository repository;

    @Inject
    public GetNotaByIdUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public LiveData<Nota> execute(Integer id) {
        return repository.getNotaById(id);
    }
}
