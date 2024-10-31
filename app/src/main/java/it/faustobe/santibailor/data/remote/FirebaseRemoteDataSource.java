package it.faustobe.santibailor.data.remote;

import android.net.Uri;
import android.util.Log;
import android.util.LruCache;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.LocalCacheSettings;
import com.google.android.gms.tasks.Tasks;

import java.util.HashMap;

import it.faustobe.santibailor.util.FirebaseErrorHandler;


public class FirebaseRemoteDataSource {
    private static final String TAG = "FirebaseRemoteDataSource";
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private final StorageReference storageRef;
    private final LruCache<String, byte[]> imageCache;
    private final LruCache<String, String> bioCache;

    public FirebaseRemoteDataSource(FirebaseFirestore db, FirebaseStorage storage) {
        this.db = db;
        this.storage = storage;
        this.storageRef = storage.getReference();

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;

        imageCache = new LruCache<String, byte[]>(cacheSize) {
            @Override
            protected int sizeOf(String key, byte[] value) {
                return value.length / 1024;
            }
        };

        bioCache = new LruCache<>(100);

        // Enable Firestore offline persistence
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                .build();
        db.setFirestoreSettings(settings);
    }

    public Task<Uri> uploadImage(String id, byte[] imageData) {
        Log.d(TAG, "Uploading image for ID: " + id);
        StorageReference imageRef = storageRef.child("images/" + id + ".webp");
        return imageRef.putBytes(imageData)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        Log.e(TAG, "Error uploading image: " + e.getMessage());
                        FirebaseErrorHandler.handleException(e);
                        throw e;
                    }
                    Log.d(TAG, "Image uploaded successfully, getting download URL");
                    return imageRef.getDownloadUrl();
                })
                .addOnSuccessListener(uri -> {
                    Log.d(TAG, "Image upload completed. URL: " + uri);
                    imageCache.put(id, imageData);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Image upload failed", e));
    }

    public Task<byte[]> downloadImage(String id) {
        byte[] cachedImage = imageCache.get(id);
        if (cachedImage != null) {
            Log.d(TAG, "Image found in cache for ID: " + id);
            return Tasks.forResult(cachedImage);
        }

        Log.d(TAG, "Downloading image for ID: " + id);
        StorageReference imageRef = storageRef.child("images/" + id + ".webp");
        return imageRef.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(bytes -> {
                    Log.d(TAG, "Image downloaded successfully. Size: " + bytes.length);
                    imageCache.put(id, bytes);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Image download failed", e);
                    FirebaseErrorHandler.handleException(e);
                });
    }

    public Task<String> getBio(String id) {
        String cachedBio = bioCache.get(id);
        if (cachedBio != null) {
            Log.d(TAG, "Bio found in cache for ID: " + id);
            return Tasks.forResult(cachedBio);
        }

        Log.d(TAG, "Getting bio for ID: " + id);
        return db.collection("ricorrenze").document(id).get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String bio = document.getString("bio");
                            if (bio != null) {
                                bioCache.put(id, bio);
                            }
                            return bio;
                        }
                    } else {
                        Log.e(TAG, "Failed to get bio", task.getException());
                        FirebaseErrorHandler.handleException(task.getException());
                    }
                    return null;
                });
    }
    public Task<Void> updateBio(String id, String bio) {
        Log.d(TAG, "Updating bio for ID: " + id);
        return db.collection("ricorrenze").document(id)
                .update("bio", bio)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Bio updated successfully");
                    bioCache.put(id, bio);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to update bio", e);
                    FirebaseErrorHandler.handleException(e);
                });
    }

    // Other methods remain the same
}
