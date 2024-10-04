package it.faustobe.santibailor.util;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import it.faustobe.santibailor.R;

/**
 * Classe per la gestione delle immagini nell'applicazione SantiBailor.
 * Fornisce metodi per salvare, caricare, aggiornare ed eliminare immagini.
 * Utilizza il pattern Singleton per garantire un'unica istanza in tutta l'applicazione.
 *
 * Utilizzo:
 * - Ottenere l'istanza: ImageHandler imageHandler = ImageHandler.getInstance(context);
 * - Salvare un'immagine: String imageUrl = imageHandler.saveOrUpdateImageSafely(newImageUri, oldImageUrl);
 * - Caricare un'immagine: imageHandler.loadImage(imageUrl, imageView, R.drawable.placeholder_image);
 *
 * Nota: Questa classe gestisce la compressione delle immagini e utilizza Glide per il caching efficiente.
 */

public class ImageHandler {
    private static final String IMAGE_DIRECTORY = "images";
    private static volatile ImageHandler instance;
    private final Context context;

    /**
     * Costruttore pubblico che inizializza l'ImageHandler.
     * Utilizzare getInstance() per ottenere l'istanza singleton.
     *
     * @param context Il contesto dell'applicazione.
     * @throws RuntimeException se si tenta di creare più di un'istanza.
     */
    public ImageHandler(Context context) {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.context = context.getApplicationContext();
    }

    /**
     * Ottiene l'istanza singleton di ImageHandler.
     * @param context Il contesto dell'applicazione.
     * @return L'istanza di ImageHandler.
     */
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

    /**
     * Salva o aggiorna un'immagine in modo sicuro, gestendo internamente le eccezioni.
     * Questo metodo dovrebbe essere utilizzato in tutte le classi che necessitano di salvare o aggiornare immagini.
     *
     * @param newImageUri URI della nuova immagine da salvare
     * @param existingImageUrl URL dell'immagine esistente da sostituire (può essere null)
     * @return L'URL della nuova immagine salvata, o null in caso di errore
     */
    public String saveOrUpdateImageSafely(Uri newImageUri, String existingImageUrl) {
        try {
            return saveOrUpdateImage(newImageUri, existingImageUrl);
        } catch (IOException e) {
            Log.e(TAG, "Error saving image", e);
            // Potresti voler lanciare un'eccezione personalizzata qui
            return null;
        }
    }

    /**
     * Salva o aggiorna un'immagine, comprimendola prima del salvataggio.
     * Questo metodo è interno e non dovrebbe essere chiamato direttamente. Utilizzare saveOrUpdateImageSafely invece.
     *
     * @param imageUri URI dell'immagine da salvare
     * @param existingImageUrl URL dell'immagine esistente da sostituire (può essere null)
     * @return L'URL della nuova immagine salvata
     * @throws IOException se si verifica un errore durante il salvataggio
     */
    public String saveOrUpdateImage(Uri imageUri, String existingImageUrl) throws IOException {
        // Comprimiamo l'immagine prima di salvarla
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        byte[] compressedImage = outputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "compressed_image_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri insertUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (insertUri == null) {
            throw new IOException("Failed to create new MediaStore record.");
        }

        try (OutputStream os = context.getContentResolver().openOutputStream(insertUri)) {
            if (os == null) {
                throw new IOException("Failed to open output stream");
            }
            os.write(compressedImage);
        }

        // Se esiste un'immagine precedente, la eliminiamo
        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
            deleteImage(existingImageUrl);
        }

        Log.d("ImageHandler", "Compressed image saved using MediaStore at: " + insertUri);
        return insertUri.toString();
    }

    private void copyImageToFile(Uri sourceUri, File destFile) throws IOException {
        try (InputStream in = context.getContentResolver().openInputStream(sourceUri);
             OutputStream out = new FileOutputStream(destFile)) {
            if (in == null) {
                throw new IOException("Failed to open input stream");
            }
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    public void loadImage(String url, ImageView imageView, int placeholderResId) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false);

        GlideApp.with(context)
                .load(getImageSource(url))
                .apply(options)
                .into(imageView);
    }

    private Object getImageSource(String url) {
        if (url == null || url.isEmpty()) {
            return R.drawable.default_ricorrenza_image;
        }
        if (url.startsWith("file://")) {
            return new File(url.substring(7));
        } else if (url.startsWith("asset://")) {
            return "file:///android_asset/" + url.substring(8);
        } else {
            return url;
        }
    }

    public String saveImageToInternalStorage(Uri imageUri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
        if (inputStream == null) {
            throw new IOException("Cannot open input stream for image URI");
        }

        File directory = new File(context.getFilesDir(), "images");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, fileName);

        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            inputStream.close();
        }

        return "file://" + file.getAbsolutePath();
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl != null && imageUrl.startsWith("file://")) {
            File file = new File(Uri.parse(imageUrl).getPath());
            if (file.exists()) {
                boolean deleted = file.delete();
                Log.d("ImageHandler", "Image deletion " + (deleted ? "successful" : "failed") + ": " + imageUrl);
            }
        }
    }

    public void listSavedImages() {
        File directory = new File(context.getFilesDir(), IMAGE_DIRECTORY);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                Log.d("ImageHandler", "Saved image: " + file.getAbsolutePath());
            }
        } else {
            Log.d("ImageHandler", "No saved images found or directory doesn't exist");
        }
    }

    public void loadImage(int resourceId, ImageView imageView, int placeholderResId) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(resourceId)
                .apply(options)
                .into(imageView);
    }
}