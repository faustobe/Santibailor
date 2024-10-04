package it.faustobe.santibailor.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.util.ImageHandler;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@HiltWorker
public class ImageCleanupWorker extends Worker {

    private final RicorrenzaRepository ricorrenzaRepository;
    private final ImageHandler imageHandler;

    @AssistedInject
    public ImageCleanupWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters workerParams,
            RicorrenzaRepository ricorrenzaRepository,
            ImageHandler imageHandler) {
        super(context, workerParams);
        this.ricorrenzaRepository = ricorrenzaRepository;
        this.imageHandler = imageHandler;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            // Get all image URLs from the database
            List<String> databaseImageUrls = ricorrenzaRepository.getAllImageUrls();

            // Get all image files from the device
            File imageDir = new File(getApplicationContext().getFilesDir(), "images");
            File[] imageFiles = imageDir.listFiles();

            if (imageFiles != null) {
                Set<String> databaseImageNames = databaseImageUrls.stream()
                        .map(url -> new File(url).getName())
                        .collect(Collectors.toSet());

                for (File file : imageFiles) {
                    if (!databaseImageNames.contains(file.getName())) {
                        // This image is not in the database, delete it
                        boolean deleted = file.delete();
                        if (!deleted) {
                            // Log the failure to delete the file
                        }
                    }
                }
            }

            return Result.success();
        } catch (Exception e) {
            // Log the exception
            return Result.failure();
        }
    }
}
