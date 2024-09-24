package it.faustobe.santibailor.domain.usecase;

import java.util.List;
import javax.inject.Inject;
import androidx.lifecycle.LiveData;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.domain.model.Ricorrenza;

public class GetRicorrenzeDelGiornoUseCase implements BaseUseCase<GetRicorrenzeDelGiornoUseCase.Params, LiveData<List<Ricorrenza>>> {

    private final RicorrenzaRepository repository;

    @Inject
    public GetRicorrenzeDelGiornoUseCase(RicorrenzaRepository repository) {
        this.repository = repository;
    }

    @Override
    public LiveData<List<Ricorrenza>> execute(Params params) {
        return repository.getRicorrenzeDelGiorno(params.giorno, params.mese);
    }

    public List<Ricorrenza> executeSync(Params params) {
        // Assuming repository has a method to get ricorrenze synchronously
        return repository.getRicorrenzeDelGiornoSync(params.giorno, params.mese);
    }

    public static class Params {
        public final int giorno;
        public final int mese;

        public Params(int giorno, int mese) {
            this.giorno = giorno;
            this.mese = mese;
        }
    }
}