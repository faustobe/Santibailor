package it.faustobe.santibailor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration;
import androidx.work.WorkManager;
import dagger.hilt.android.HiltAndroidApp;
import it.faustobe.santibailor.util.ImageHandler;
import it.faustobe.santibailor.worker.WorkManagerConfig;
import it.faustobe.santibailor.worker.WorkScheduler;

import javax.inject.Inject;

@HiltAndroidApp
public class MyApplication extends Application implements Configuration.Provider {
    @Inject
    WorkManagerConfig workManagerConfig;

    @Inject
    ImageHandler imageHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        if (WorkManager.getInstance(this) == null) {
            WorkManager.initialize(this, workManagerConfig.getWorkManagerConfiguration());
        }
        WorkScheduler.scheduleImageCleanup(this);
    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return workManagerConfig.getWorkManagerConfiguration();
    }

    public ImageHandler getImageHandler() {
        return imageHandler;
    }
}