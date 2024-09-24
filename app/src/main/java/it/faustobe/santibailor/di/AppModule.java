package it.faustobe.santibailor.di;

import android.app.Application;
import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import it.faustobe.santibailor.data.AppDatabase;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.domain.usecase.DeleteRicorrenzaUseCase;
import it.faustobe.santibailor.domain.usecase.GetRicorrenzaByIdUseCase;
import it.faustobe.santibailor.domain.usecase.GetRicorrenzeDelGiornoUseCase;
import it.faustobe.santibailor.domain.usecase.GetTotalItemCountUseCase;
import it.faustobe.santibailor.domain.usecase.GetTipiRicorrenzaUseCase;
import it.faustobe.santibailor.domain.usecase.InsertRicorrenzaUseCase;
import it.faustobe.santibailor.domain.usecase.RicercaAvanzataUseCase;
import it.faustobe.santibailor.domain.usecase.UpdateRicorrenzaImageUseCase;
import it.faustobe.santibailor.domain.usecase.UpdateRicorrenzaUseCase;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return AppDatabase.getDatabase(context);
    }

    @Provides
    @Singleton
    public RicorrenzaRepository provideRicorrenzaRepository(Application application) {
        return new RicorrenzaRepository(application);
    }

    @Provides
    public GetRicorrenzeDelGiornoUseCase provideGetRicorrenzeDelGiornoUseCase(RicorrenzaRepository repository) {
        return new GetRicorrenzeDelGiornoUseCase(repository);
    }

    @Provides
    public RicercaAvanzataUseCase provideRicercaAvanzataUseCase(RicorrenzaRepository repository) {
        return new RicercaAvanzataUseCase(repository);
    }

    @Provides
    public GetTipiRicorrenzaUseCase provideGetTipiRicorrenzaUseCase(RicorrenzaRepository repository) {
        return new GetTipiRicorrenzaUseCase(repository);
    }

    @Provides
    public UpdateRicorrenzaUseCase provideUpdateRicorrenzaUseCase(RicorrenzaRepository repository) {
        return new UpdateRicorrenzaUseCase(repository);
    }

    @Provides
    public DeleteRicorrenzaUseCase provideDeleteRicorrenzaUseCase(RicorrenzaRepository repository) {
        return new DeleteRicorrenzaUseCase(repository);
    }

    @Provides
    public GetRicorrenzaByIdUseCase provideGetRicorrenzaByIdUseCase(RicorrenzaRepository repository) {
        return new GetRicorrenzaByIdUseCase(repository);
    }

    @Provides
    public InsertRicorrenzaUseCase provideInsertRicorrenzaUseCase(RicorrenzaRepository repository) {
        return new InsertRicorrenzaUseCase(repository);
    }

    @Provides
    public GetTotalItemCountUseCase provideGetTotalItemCountUseCase(RicorrenzaRepository repository) {
        return new GetTotalItemCountUseCase(repository);
    }

    @Provides
    public UpdateRicorrenzaImageUseCase provideUpdateRicorrenzaImageUseCase(RicorrenzaRepository repository) {
        return new UpdateRicorrenzaImageUseCase(repository);
    }
}
