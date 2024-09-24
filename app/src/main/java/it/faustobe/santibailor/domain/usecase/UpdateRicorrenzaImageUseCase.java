package it.faustobe.santibailor.domain.usecase;

import javax.inject.Inject;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;

public class UpdateRicorrenzaImageUseCase implements BaseUseCase<UpdateRicorrenzaImageUseCase.Params, Void> {

    private final RicorrenzaRepository repository;

    @Inject
    public UpdateRicorrenzaImageUseCase(RicorrenzaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Void execute(Params params) {
        repository.updateImageUrl(params.ricorrenzaId, params.imageUrl);
        return null;
    }

    public static class Params {
        public final int ricorrenzaId;
        public final String imageUrl;

        public Params(int ricorrenzaId, String imageUrl) {
            this.ricorrenzaId = ricorrenzaId;
            this.imageUrl = imageUrl;
        }
    }
}