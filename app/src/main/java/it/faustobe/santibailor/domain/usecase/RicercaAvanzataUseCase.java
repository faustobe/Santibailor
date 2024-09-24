package it.faustobe.santibailor.domain.usecase;

import java.util.List;
import javax.inject.Inject;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.domain.model.Ricorrenza;

public class RicercaAvanzataUseCase implements BaseUseCase<RicercaAvanzataUseCase.Params, List<Ricorrenza>> {

    private final RicorrenzaRepository repository;

    @Inject
    public RicercaAvanzataUseCase(RicorrenzaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Ricorrenza> execute(Params params) {
        return repository.ricercaAvanzataPaginata(
                params.nome, params.tipo, params.dataInizio, params.dataFine, params.limit, params.offset
        );
    }

    public int getTotalCount(Params params) {
        return repository.contaTotaleRisultati(params.nome, params.tipo, params.dataInizio, params.dataFine);
    }

    public static class Params {
        public final String nome;
        public final Integer tipo;
        public final String dataInizio;
        public final String dataFine;
        public final int limit;
        public final int offset;

        public Params(String nome, Integer tipo, String dataInizio, String dataFine, int limit, int offset) {
            this.nome = nome;
            this.tipo = tipo;
            this.dataInizio = dataInizio;
            this.dataFine = dataFine;
            this.limit = limit;
            this.offset = offset;
        }
    }
}
