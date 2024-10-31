package it.faustobe.santibailor.util;

import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageException;

public class FirebaseErrorHandler {
    private static final String TAG = "FirebaseErrorHandler";

    public static void handleException(Exception e) {
        if (e instanceof FirebaseAuthException) {
            handleAuthException((FirebaseAuthException) e);
        } else if (e instanceof FirebaseFirestoreException) {
            handleFirestoreException((FirebaseFirestoreException) e);
        } else if (e instanceof StorageException) {
            handleStorageException((StorageException) e);
        } else {
            Log.e(TAG, "Unspecified Firebase error", e);
        }
    }

    private static void handleAuthException(FirebaseAuthException e) {
        String errorCode = e.getErrorCode();
        switch (errorCode) {
            case "ERROR_INVALID_CREDENTIAL":
                Log.e(TAG, "The authentication credential is malformed or expired.", e);
                break;
            case "ERROR_USER_DISABLED":
                Log.e(TAG, "The user account has been disabled.", e);
                break;
            // Aggiungere altri casi specifici per l'autenticazione
            default:
                Log.e(TAG, "Authentication error: " + errorCode, e);
        }
    }

    private static void handleFirestoreException(FirebaseFirestoreException e) {
        FirebaseFirestoreException.Code code = e.getCode();
        switch (code) {
            case UNAVAILABLE:
                Log.e(TAG, "Firestore service is currently unavailable.", e);
                break;
            case PERMISSION_DENIED:
                Log.e(TAG, "Client doesn't have permission to access the resource.", e);
                break;
            // Aggiungere altri casi specifici per Firestore
            default:
                Log.e(TAG, "Firestore error: " + code, e);
        }
    }

    private static void handleStorageException(StorageException e) {
        int errorCode = e.getErrorCode();
        switch (errorCode) {
            case StorageException.ERROR_OBJECT_NOT_FOUND:
                Log.e(TAG, "File not found in Firebase Storage.", e);
                break;
            case StorageException.ERROR_QUOTA_EXCEEDED:
                Log.e(TAG, "Firebase Storage quota exceeded.", e);
                break;
            // Aggiungere altri casi specifici per Storage
            default:
                Log.e(TAG, "Storage error: " + errorCode, e);
        }
    }

    // Nuovo metodo per gestire errori di Firestore con retry
    public static void handleFirestoreExceptionWithRetry(FirebaseFirestoreException e, Runnable retryAction, int maxRetries) {
        FirebaseFirestoreException.Code code = e.getCode();
        switch (code) {
            case UNAVAILABLE:
            case DEADLINE_EXCEEDED:
                if (maxRetries > 0) {
                    Log.w(TAG, "Temporary Firestore error, retrying. Retries left: " + maxRetries, e);
                    new Handler().postDelayed(retryAction, 1000); // Retry after 1 second
                } else {
                    Log.e(TAG, "Firestore error after all retries: " + code, e);
                }
                break;
            default:
                Log.e(TAG, "Firestore error: " + code, e);
        }
    }

    // Nuovo metodo per gestire errori di Storage con retry
    public static void handleStorageExceptionWithRetry(StorageException e, Runnable retryAction, int maxRetries) {
        int errorCode = e.getErrorCode();
        switch (errorCode) {
            case StorageException.ERROR_RETRY_LIMIT_EXCEEDED:
            case StorageException.ERROR_UNKNOWN:
                if (maxRetries > 0) {
                    Log.w(TAG, "Temporary Storage error, retrying. Retries left: " + maxRetries, e);
                    new Handler().postDelayed(retryAction, 1000); // Retry after 1 second
                } else {
                    Log.e(TAG, "Storage error after all retries: " + errorCode, e);
                }
                break;
            default:
                Log.e(TAG, "Storage error: " + errorCode, e);
        }
    }
}
