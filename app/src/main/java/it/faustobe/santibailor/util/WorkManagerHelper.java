package it.faustobe.santibailor.util;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import it.faustobe.santibailor.worker.DailySaintNotificationWorker;

/**
 * Helper class per la gestione di WorkManager e task periodici
 */
public class WorkManagerHelper {

    private static final String DAILY_SAINT_WORK_NAME = "daily_saint_notification_work";
    private static final int NOTIFICATION_HOUR = 7; // Ora della notifica (7:00 AM)
    private static final int NOTIFICATION_MINUTE = 0;

    /**
     * Programma la notifica giornaliera del santo del giorno
     *
     * @param context Context dell'applicazione
     */
    public static void scheduleDailySaintNotification(Context context) {
        // Constraints: esegui solo se il dispositivo è carico o in carica (opzionale)
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED) // Non serve connessione
                .setRequiresBatteryNotLow(false) // Non aspettare batteria alta
                .build();

        // Calcola il delay iniziale per eseguire il worker alle 7:00 AM
        long initialDelay = calculateInitialDelay();

        // Crea un PeriodicWorkRequest che si ripete ogni 24 ore
        PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(
                DailySaintNotificationWorker.class,
                1, // Intervallo di ripetizione
                TimeUnit.DAYS // Unità di tempo
        )
                .setConstraints(constraints)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag(DAILY_SAINT_WORK_NAME)
                .build();

        // Schedula il work con policy KEEP (mantieni l'esistente se già schedulato)
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                DAILY_SAINT_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, // Non sovrascrivere se esiste già
                dailyWorkRequest
        );

        android.util.Log.d("WorkManagerHelper", "Daily saint notification scheduled with initial delay: " + initialDelay + "ms");
    }

    /**
     * Calcola il delay iniziale per eseguire il worker all'ora specificata
     *
     * @return delay in millisecondi
     */
    private static long calculateInitialDelay() {
        Calendar currentTime = Calendar.getInstance();
        Calendar scheduledTime = Calendar.getInstance();

        // Imposta l'ora programmata (es. 7:00 AM)
        scheduledTime.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        scheduledTime.set(Calendar.MINUTE, NOTIFICATION_MINUTE);
        scheduledTime.set(Calendar.SECOND, 0);
        scheduledTime.set(Calendar.MILLISECOND, 0);

        // Se l'ora programmata è già passata oggi, schedula per domani
        if (scheduledTime.before(currentTime)) {
            scheduledTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Calcola la differenza in millisecondi
        return scheduledTime.getTimeInMillis() - currentTime.getTimeInMillis();
    }

    /**
     * Cancella la schedulazione della notifica giornaliera
     *
     * @param context Context dell'applicazione
     */
    public static void cancelDailySaintNotification(Context context) {
        WorkManager.getInstance(context).cancelUniqueWork(DAILY_SAINT_WORK_NAME);
        android.util.Log.d("WorkManagerHelper", "Daily saint notification cancelled");
    }

    /**
     * Verifica se la notifica giornaliera è schedulata
     * (Metodo asincrono, usare ListenableFuture per ottenere il risultato)
     *
     * @param context Context dell'applicazione
     */
    public static void isDailySaintNotificationScheduled(Context context, OnWorkStatusCheckedListener listener) {
        WorkManager.getInstance(context)
                .getWorkInfosForUniqueWork(DAILY_SAINT_WORK_NAME)
                .addListener(() -> {
                    // Callback eseguito quando lo stato è disponibile
                    if (listener != null) {
                        listener.onStatusChecked(true);
                    }
                }, command -> command.run());
    }

    public interface OnWorkStatusCheckedListener {
        void onStatusChecked(boolean isScheduled);
    }
}
