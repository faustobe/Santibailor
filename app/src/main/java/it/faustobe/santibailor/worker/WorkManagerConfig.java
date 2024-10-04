package it.faustobe.santibailor.worker;

import androidx.work.Configuration;
import androidx.hilt.work.HiltWorkerFactory;
import javax.inject.Inject;

public class WorkManagerConfig {
    private final HiltWorkerFactory workerFactory;

    @Inject
    public WorkManagerConfig(HiltWorkerFactory workerFactory) {
        this.workerFactory = workerFactory;
    }

    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
    }
}