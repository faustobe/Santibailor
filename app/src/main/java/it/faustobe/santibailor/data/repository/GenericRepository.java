package it.faustobe.santibailor.data.repository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import it.faustobe.santibailor.data.local.dao.BaseDao;

public class GenericRepository<D, E, C> {
    private final BaseDao<E> dao;
    private final ExecutorService executorService;
    private final Function<C, E> toEntityMapper;
    private final Function<E, C> toCombinedMapper;

    public GenericRepository(BaseDao<E> dao, Function<C, E> toEntityMapper, Function<E, C> toCombinedMapper) {
        this.dao = dao;
        this.executorService = Executors.newSingleThreadExecutor();
        this.toEntityMapper = toEntityMapper;
        this.toCombinedMapper = toCombinedMapper;
    }

    public void insert(C combinedObject, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            E entity = toEntityMapper.apply(combinedObject);
            long newId = dao.insert(entity);
            if (newId != -1) {
                listener.onSuccess((int) newId);
            } else {
                listener.onError("Inserimento fallito");
            }
        });
    }

    public void update(C combinedObject, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            E entity = toEntityMapper.apply(combinedObject);
            int rowsAffected = dao.update(entity);
            if (rowsAffected > 0) {
                listener.onSuccess(0);
            } else {
                listener.onError("Aggiornamento fallito");
            }
        });
    }

    public void delete(C combinedObject, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            E entity = toEntityMapper.apply(combinedObject);
            int rowsAffected = dao.delete(entity);
            if (rowsAffected > 0) {
                listener.onSuccess(0);
            } else {
                listener.onError("Cancellazione fallita");
            }
        });
    }

    public interface OnOperationCompleteListener {
        void onSuccess(int id);
        void onError(String errorMessage);
    }
}