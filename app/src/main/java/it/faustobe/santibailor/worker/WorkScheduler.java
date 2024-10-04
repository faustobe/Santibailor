package it.faustobe.santibailor.worker;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class WorkScheduler {
    public static void scheduleImageCleanup(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(true)
                .build();

        PeriodicWorkRequest cleanupWork = new PeriodicWorkRequest.Builder(
                ImageCleanupWorker.class,
                1,
                TimeUnit.DAYS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueue(cleanupWork);
    }
}
