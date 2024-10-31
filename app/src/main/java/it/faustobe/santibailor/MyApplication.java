package it.faustobe.santibailor;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import dagger.hilt.android.HiltAndroidApp;
import it.faustobe.santibailor.util.FirebaseErrorHandler;
import it.faustobe.santibailor.util.ImageHandler;
import it.faustobe.santibailor.worker.WorkManagerConfig;
import it.faustobe.santibailor.worker.WorkScheduler;

import javax.inject.Inject;

@HiltAndroidApp
public class MyApplication extends Application implements Configuration.Provider {
    private static final String TAG = "MyApplication";

    @Inject
    WorkManagerConfig workManagerConfig;

    @Inject
    ImageHandler imageHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

        // Inizializza Firebase App Check con Play Integrity
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

        // Autenticazione anonima
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Autenticazione anonima completata con successo");
                    } else {
                        handleAuthenticationError(task.getException());
                    }
                });

        // Inizializzazione di WorkManager
        if (WorkManager.getInstance(this) == null) {
            WorkManager.initialize(this, workManagerConfig.getWorkManagerConfiguration());
        }
        WorkScheduler.scheduleImageCleanup(this);
    }

    private void handleAuthenticationError(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            FirebaseAuthException authException = (FirebaseAuthException) exception;
            Log.e(TAG, "Autenticazione fallita: " + authException.getErrorCode()
                    + " - " + authException.getMessage(), authException);
        } else {
            Log.e(TAG, "Autenticazione fallita con eccezione non specifica", exception);
        }
        // Qui potresti aggiungere ulteriori azioni in caso di fallimento dell'autenticazione
    }

    private void initializeFirebase() {
        try {
            FirebaseApp.initializeApp(this);
            FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
            firebaseAppCheck.installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance());

            FirebaseAuth.getInstance().signInAnonymously()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Autenticazione anonima completata con successo");
                        } else {
                            FirebaseErrorHandler.handleException(task.getException());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Errore durante l'inizializzazione di Firebase", e);
        }
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