package it.faustobe.santibailor.domain.usecase;

import javax.inject.Inject;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.domain.model.Ricorrenza;

public class InsertRicorrenzaUseCase implements BaseUseCase<Ricorrenza, Long> {

    private final RicorrenzaRepository repository;

    @Inject
    public InsertRicorrenzaUseCase(RicorrenzaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long execute(Ricorrenza ricorrenza) {
        return repository.insert(ricorrenza);
    }
}