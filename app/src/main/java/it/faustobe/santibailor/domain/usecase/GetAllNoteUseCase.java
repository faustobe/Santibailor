package it.faustobe.santibailor.domain.usecase;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import it.faustobe.santibailor.data.repository.NoteRepository;
import it.faustobe.santibailor.domain.model.Nota;

/**
 * UseCase per ottenere tutte le note
 */
public class GetAllNoteUseCase implements BaseUseCase<Void, LiveData<List<Nota>>> {

    private final NoteRepository repository;

    @Inject
    public GetAllNoteUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public LiveData<List<Nota>> execute(Void input) {
        return repository.getAllNote();
    }
}
