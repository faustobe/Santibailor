package it.faustobe.santibailor.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import androidx.annotation.Nullable;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.domain.model.Ricorrenza;

public class ImageHandler {
    private static final String TAG = "ImageHandler";
    private static final String IMAGE_DIRECTORY = "images";
    private static volatile ImageHandler instance;
    private final Context context;
    private final FirebaseStorage firebaseStorage;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private LruCache<String, Bitmap> memoryCache;


    private void initializeCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    private ImageHandler(Context context) {
        this.context = context.getApplicationContext();
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public static ImageHandler getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageHandler.class) {
                if (instance == null) {
                    instance = new ImageHandler(context);
                }
            }
        }
        return instance;
    }

    public interface OnImageSavedListener {
        void onImageSaved(String imageUrl);
        void onError(Exception e);
    }

    public void preloadImage(String url) {
        if (url == null || url.isEmpty()) return;

        Glide.with(context.getApplicationContext())
                .load(getImageSource(url))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .preload();
    }

    private Uri saveLocalImage(Uri imageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        byte[] compressedImage = outputStream.toByteArray();

        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File directory = new File(context.getFilesDir(), "images");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(compressedImage);
        }

        return Uri.fromFile(file);
    }

    public void saveOrUpdateImageSafely(Uri newImageUri, String existingImageUrl, OnImageSavedListener listener) {
        executorService.execute(() -> {
            try {
                String updatedImageUrl = saveOrUpdateImage(newImageUri, existingImageUrl);
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onImageSaved(updatedImageUrl);
                    }
                });
            } catch (IOException e) {
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onError(e);
                    }
                });
            }
        });
    }

    private String saveOrUpdateImage(Uri imageUri, String existingImageUrl) throws IOException {
        if (existingImageUrl != null && existingImageUrl.startsWith("https://firebasestorage.googleapis.com")) {
            return updateFirebaseImage(imageUri, existingImageUrl);
        } else {
            Uri localUri = saveLocalImage(imageUri);
            return uploadToFirebase(localUri, "image_" + System.currentTimeMillis() + ".jpg");
        }
    }

    private String updateFirebaseImage(Uri newImageUri, String existingFirebaseUrl) throws IOException {
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        return uploadToFirebase(newImageUri, fileName);
    }

    public void loadImage(String url, ImageView imageView, int placeholderResId) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(placeholderResId);
            return;
        }

        Glide.with(context)
                .load(getImageSource(url))
                .apply(new RequestOptions()
                        .placeholder(placeholderResId)
                        .error(placeholderResId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Error loading image: " + url, e);
                        // Qui puoi implementare una logica per gestire il fallimento, ad esempio tentare un re-upload
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    private Object getImageSource(String url) {
        if (url == null || url.isEmpty()) {
            return R.drawable.default_ricorrenza_image;
        }
        if (url.startsWith("file://")) {
            return new File(url.substring(7));
        } else if (url.startsWith("content://")) {
            return url;
        } else if (url.startsWith("https://firebasestorage.googleapis.com")) {
            return url;
        } else {
            return R.drawable.default_ricorrenza_image;
        }
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl == null) return;

        if (imageUrl.startsWith("https://firebasestorage.googleapis.com")) {
            StorageReference ref = firebaseStorage.getReferenceFromUrl(imageUrl);
            ref.delete().addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Firebase image deleted successfully");
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error deleting Firebase image", e);
            });
        } else if (imageUrl.startsWith("file://") || imageUrl.startsWith("content://")) {
            Uri uri = Uri.parse(imageUrl);
            File file = new File(uri.getPath());
            if (file.exists() && file.delete()) {
                Log.d(TAG, "Local image deleted successfully");
            } else {
                Log.e(TAG, "Error deleting local image");
            }
        }
    }

    private String getFileNameAndPath(Uri uri) {
        String fileName = getFileName(uri);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return String.format("ricorrenze/%s_%s_%s", timestamp, uniqueId, fileName);
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public Task<Uri> getFirebaseImageUrl(String id, boolean isThumb) {
        String fileName = id + (isThumb ? "_thumb" : "") + ".webp";
        return firebaseStorage.getReference().child("images/" + fileName).getDownloadUrl();
    }

    public void migrateImages(RicorrenzaRepository repository) {
        Log.d(TAG, "Starting image migration");
        List<Ricorrenza> ricorrenze = repository.getAllRicorrenzeWithImages();
        Log.d(TAG, "Found " + ricorrenze.size() + " ricorrenze with images");

        for (Ricorrenza ricorrenza : ricorrenze) {
            try {
                String imageUrl = ricorrenza.getImageUrl();
                Log.d(TAG, "Processing ricorrenza ID: " + ricorrenza.getId() + " with imageUrl: " + imageUrl);

                if (imageUrl != null && !imageUrl.startsWith("https://firebasestorage.googleapis.com")) {
                    if (imageUrl.startsWith("content://")) {
                        Log.d(TAG, "Migrating content URI for ricorrenza ID: " + ricorrenza.getId());
                        migrateContentUri(repository, ricorrenza, imageUrl);
                    } else if (imageUrl.startsWith("file://") || imageUrl.startsWith("/")) {
                        Log.d(TAG, "Migrating local file for ricorrenza ID: " + ricorrenza.getId());
                        migrateLocalFile(repository, ricorrenza, imageUrl);
                    } else {
                        Log.w(TAG, "Unsupported URL format: " + imageUrl + " for ricorrenza ID: " + ricorrenza.getId());
                        setDefaultImage(repository, ricorrenza);
                    }
                } else {
                    Log.d(TAG, "Skipping ricorrenza ID: " + ricorrenza.getId() + " (already on Firebase or null URL)");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error migrating image for ricorrenza ID: " + ricorrenza.getId(), e);
                setDefaultImage(repository, ricorrenza);
            }
        }
        Log.d(TAG, "Image migration completed");
    }

    private void migrateContentUri(RicorrenzaRepository repository, Ricorrenza ricorrenza, String imageUrl) {
        try {
            Uri contentUri = Uri.parse(imageUrl);
            InputStream inputStream = context.getContentResolver().openInputStream(contentUri);
            if (inputStream != null) {
                String fileName = "migrated_" + ricorrenza.getId() + "_" + System.currentTimeMillis() + ".jpg";
                String newUrl = uploadToFirebase(inputStream, fileName);
                repository.updateImageUrl(ricorrenza.getId(), newUrl);
                inputStream.close();
            } else {
                Log.w(TAG, "Unable to open input stream for content URI: " + imageUrl);
                setDefaultImage(repository, ricorrenza);
            }
        } catch (Exception e) {
            Log.e(TAG, "Errore nelle migrazione del content URI: " + imageUrl, e);
            setDefaultImage(repository, ricorrenza);
        }
    }

    private void migrateLocalFile(RicorrenzaRepository repository, Ricorrenza ricorrenza, String imageUrl) {
        File localFile = new File(imageUrl.startsWith("file://") ? imageUrl.substring(7) : imageUrl);
        if (localFile.exists()) {
            try {
                String fileName = "migrated_" + ricorrenza.getId() + "_" + System.currentTimeMillis() + ".jpg";
                String newUrl = uploadToFirebase(Uri.fromFile(localFile), fileName);
                repository.updateImageUrl(ricorrenza.getId(), newUrl);
                localFile.delete();
            } catch (Exception e) {
                Log.e(TAG, "Error uploading local file: " + imageUrl, e);
                setDefaultImage(repository, ricorrenza);
            }
        } else {
            Log.w(TAG, "image file locale not found: " + imageUrl);
            setDefaultImage(repository, ricorrenza);
        }
    }

    private void setDefaultImage(RicorrenzaRepository repository, Ricorrenza ricorrenza) {
        repository.updateImageUrl(ricorrenza.getId(), "default_image_url");
    }

    private String uploadToFirebase(Uri fileUri, String fileName) throws IOException {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images/" + fileName);

        // Converti il file URI in un file path
        String filePath = fileUri.getPath();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        UploadTask uploadTask = imageRef.putFile(Uri.fromFile(file));

        try {
            Tasks.await(uploadTask);
            return Tasks.await(imageRef.getDownloadUrl()).toString();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "Error uploading image to Firebase", e);
            throw new IOException("Failed to upload image to Firebase", e);
        }
    }

    private String uploadToFirebase(InputStream inputStream, String fileName) throws IOException {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images/" + fileName);

        UploadTask uploadTask = imageRef.putStream(inputStream);

        try {
            Tasks.await(uploadTask);
            return Tasks.await(imageRef.getDownloadUrl()).toString();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "Error uploading image to Firebase", e);
            throw new IOException("Failed to upload image to Firebase", e);
        }
    }
}