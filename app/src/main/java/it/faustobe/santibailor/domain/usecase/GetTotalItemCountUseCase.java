package it.faustobe.santibailor.domain.usecase;

import javax.inject.Inject;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;

public class GetTotalItemCountUseCase implements BaseUseCase<Void, Integer> {

    private final RicorrenzaRepository repository;

    @Inject
    public GetTotalItemCountUseCase(RicorrenzaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer execute(Void params) {
        return repository.getTotalItemCount();
    }
}
