package it.faustobe.santibailor.domain.usecase;

import javax.inject.Inject;
import androidx.lifecycle.LiveData;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.domain.model.Ricorrenza;

public class GetRicorrenzaByIdUseCase implements BaseUseCase<Integer, LiveData<Ricorrenza>> {

    private final RicorrenzaRepository repository;

    @Inject
    public GetRicorrenzaByIdUseCase(RicorrenzaRepository repository) {
        this.repository = repository;
    }

    @Override
    public LiveData<Ricorrenza> execute(Integer id) {
        return repository.getRicorrenzaById(id);
    }
}
