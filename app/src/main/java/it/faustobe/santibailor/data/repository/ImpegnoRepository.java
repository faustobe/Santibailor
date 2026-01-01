package it.faustobe.santibailor.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.faustobe.santibailor.data.local.dao.ImpegnoDao;
import it.faustobe.santibailor.data.local.entities.ImpegnoEntity;
import it.faustobe.santibailor.data.mapper.ImpegnoMapper;
import it.faustobe.santibailor.domain.model.Impegno;

/**
 * Repository per gestire gli impegni
 * Gestisce l'accesso ai dati (Room database)
 */
@Singleton
public class ImpegnoRepository {

    private final ImpegnoDao impegnoDao;
    private final ExecutorService executorService;

    @Inject
    public ImpegnoRepository(ImpegnoDao impegnoDao) {
        this.impegnoDao = impegnoDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Ottiene tutti gli impegni
     */
    public LiveData<List<Impegno>> getAllImpegni() {
        return Transformations.map(
                impegnoDao.getAllImpegni(),
                ImpegnoMapper::toDomainList
        );
    }

    /**
     * Ottiene un impegno per ID
     */
    public LiveData<Impegno> getImpegnoById(int id) {
        return Transformations.map(
                impegnoDao.getImpegnoById(id),
                ImpegnoMapper::toDomain
        );
    }

    /**
     * Ottiene impegni di oggi
     */
    public LiveData<List<Impegno>> getImpegniOggi() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDay = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        long endOfDay = calendar.getTimeInMillis();

        return Transformations.map(
                impegnoDao.getImpegniOggi(startOfDay, endOfDay),
                ImpegnoMapper::toDomainList
        );
    }

    /**
     * Ottiene impegni futuri (prossimi N impegni non completati)
     */
    public LiveData<List<Impegno>> getImpegniFuturi(int limit) {
        long now = System.currentTimeMillis();
        return Transformations.map(
                impegnoDao.getImpegniFuturi(now, limit),
                ImpegnoMapper::toDomainList
        );
    }

    /**
     * Ottiene impegni per categoria
     */
    public LiveData<List<Impegno>> getImpegniPerCategoria(String categoria) {
        return Transformations.map(
                impegnoDao.getImpegniPerCategoria(categoria),
                ImpegnoMapper::toDomainList
        );
    }

    /**
     * Ottiene impegni in un range di date
     */
    public LiveData<List<Impegno>> getImpegniInRange(long startDate, long endDate) {
        return Transformations.map(
                impegnoDao.getImpegniInRange(startDate, endDate),
                ImpegnoMapper::toDomainList
        );
    }

    /**
     * Inserisce un nuovo impegno
     */
    public void insertImpegno(Impegno impegno, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                ImpegnoEntity entity = ImpegnoMapper.toEntity(impegno);
                long id = impegnoDao.insert(entity);
                if (listener != null) {
                    listener.onSuccess((int) id);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Aggiorna un impegno esistente
     */
    public void updateImpegno(Impegno impegno, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                ImpegnoEntity entity = ImpegnoMapper.toEntity(impegno);
                impegnoDao.update(entity);
                if (listener != null) {
                    listener.onSuccess(impegno.getId());
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Elimina un impegno
     */
    public void deleteImpegno(Impegno impegno, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                ImpegnoEntity entity = ImpegnoMapper.toEntity(impegno);
                impegnoDao.delete(entity);
                if (listener != null) {
                    listener.onSuccess(impegno.getId());
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Segna un impegno come completato
     */
    public void markAsCompletato(int id, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                long now = System.currentTimeMillis();
                impegnoDao.markAsCompletato(id, now);
                if (listener != null) {
                    listener.onSuccess(id);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Segna un impegno come non completato
     */
    public void markAsNonCompletato(int id, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                long now = System.currentTimeMillis();
                impegnoDao.markAsNonCompletato(id, now);
                if (listener != null) {
                    listener.onSuccess(id);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    /**
     * Callback per operazioni asincrone
     */
    public interface OnOperationCompleteListener {
        void onSuccess(int id);
        void onError(String error);
    }
}
