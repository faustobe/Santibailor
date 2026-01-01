package it.faustobe.santibailor.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import it.faustobe.santibailor.data.local.dao.RicorrenzaDao;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.mapper.RicorrenzaMapper;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.util.NotificationHelper;

/**
 * Worker che invia una notifica giornaliera con un santo random del giorno corrente
 */
@HiltWorker
public class DailySaintNotificationWorker extends Worker {

    private static final String TAG = "DailySaintWorker";

    private final RicorrenzaDao ricorrenzaDao;

    @AssistedInject
    public DailySaintNotificationWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters workerParams,
            RicorrenzaDao ricorrenzaDao
    ) {
        super(context, workerParams);
        this.ricorrenzaDao = ricorrenzaDao;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.d(TAG, "DailySaintNotificationWorker started");

            // Ottieni data corrente
            Calendar calendar = Calendar.getInstance();
            int mese = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH è 0-based
            int giorno = calendar.get(Calendar.DAY_OF_MONTH);

            Log.d(TAG, "Fetching saints for date: " + giorno + "/" + mese);

            // Recupera i santi del giorno (operazione sincrona, siamo già in background thread)
            List<RicorrenzaEntity> saintsEntities = ricorrenzaDao.getRicorrenzeDelGiornoSync(giorno, mese);

            if (saintsEntities == null || saintsEntities.isEmpty()) {
                Log.w(TAG, "No saints found for today");
                return Result.success();
            }

            // Seleziona un santo random
            Random random = new Random();
            RicorrenzaEntity randomSaintEntity = saintsEntities.get(random.nextInt(saintsEntities.size()));
            Ricorrenza randomSaint = RicorrenzaMapper.toDomain(randomSaintEntity);

            Log.d(TAG, "Selected saint: " + randomSaint.getNomeCompleto());

            // Crea e mostra la notifica
            NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());

            String saintName = randomSaint.getNomeCompleto();
            String saintDescription = randomSaint.getBio();

            // Limita la descrizione a 200 caratteri per la notifica
            if (saintDescription != null && saintDescription.length() > 200) {
                saintDescription = saintDescription.substring(0, 197) + "...";
            }

            notificationHelper.showDailySaintNotification(saintName, saintDescription);

            Log.d(TAG, "Notification sent successfully");

            return Result.success();

        } catch (Exception e) {
            Log.e(TAG, "Error in DailySaintNotificationWorker", e);
            // Riprova in caso di fallimento
            return Result.retry();
        }
    }
}
