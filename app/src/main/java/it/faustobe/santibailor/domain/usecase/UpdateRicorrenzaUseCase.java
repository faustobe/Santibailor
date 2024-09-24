package it.faustobe.santibailor.domain.usecase;

import javax.inject.Inject;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.domain.model.Ricorrenza;

public class UpdateRicorrenzaUseCase implements BaseUseCase<Ricorrenza, Void> {

    private final RicorrenzaRepository repository;

    @Inject
    public UpdateRicorrenzaUseCase(RicorrenzaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Void execute(Ricorrenza ricorrenza) {
        repository.update(ricorrenza);
        return null;
    }
}
