package it.faustobe.santibailor.domain.usecase;

import java.util.List;
import javax.inject.Inject;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;

public class GetTipiRicorrenzaUseCase implements BaseUseCase<Void, List<TipoRicorrenza>> {

    private final RicorrenzaRepository repository;

    @Inject
    public GetTipiRicorrenzaUseCase(RicorrenzaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TipoRicorrenza> execute(Void params) {
        return repository.getAllTipiRicorrenza();
    }
}
