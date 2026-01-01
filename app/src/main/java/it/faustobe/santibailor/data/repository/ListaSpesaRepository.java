package it.faustobe.santibailor.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.faustobe.santibailor.data.local.dao.ItemSpesaDao;
import it.faustobe.santibailor.data.local.dao.ListaSpesaDao;
import it.faustobe.santibailor.data.mapper.ItemSpesaMapper;
import it.faustobe.santibailor.data.mapper.ListaSpesaMapper;
import it.faustobe.santibailor.domain.model.ItemSpesa;
import it.faustobe.santibailor.domain.model.ListaSpesa;

@Singleton
public class ListaSpesaRepository {
    private final ListaSpesaDao listaSpesaDao;
    private final ItemSpesaDao itemSpesaDao;
    private final ExecutorService executorService;

    @Inject
    public ListaSpesaRepository(ListaSpesaDao listaSpesaDao, ItemSpesaDao itemSpesaDao) {
        this.listaSpesaDao = listaSpesaDao;
        this.itemSpesaDao = itemSpesaDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    // ========== Liste Spesa ==========

    public LiveData<List<ListaSpesa>> getAllListe() {
        return Transformations.map(listaSpesaDao.getAllListe(), ListaSpesaMapper::toDomainList);
    }

    public LiveData<List<ListaSpesa>> getListeAttive() {
        return Transformations.map(listaSpesaDao.getListeAttive(), ListaSpesaMapper::toDomainList);
    }

    public LiveData<List<ListaSpesa>> getListeCompletate() {
        return Transformations.map(listaSpesaDao.getListeCompletate(), ListaSpesaMapper::toDomainList);
    }

    public LiveData<ListaSpesa> getListaById(int id) {
        return Transformations.map(listaSpesaDao.getListaById(id), ListaSpesaMapper::toDomain);
    }

    public void insertLista(ListaSpesa lista, OnInsertListener listener) {
        executorService.execute(() -> {
            try {
                long id = listaSpesaDao.insert(ListaSpesaMapper.toEntity(lista));
                if (listener != null) {
                    listener.onSuccess((int) id);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    public void updateLista(ListaSpesa lista, OnUpdateListener listener) {
        executorService.execute(() -> {
            try {
                listaSpesaDao.update(ListaSpesaMapper.toEntity(lista));
                if (listener != null) {
                    listener.onSuccess();
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    public void deleteLista(ListaSpesa lista, OnDeleteListener listener) {
        executorService.execute(() -> {
            try {
                listaSpesaDao.delete(ListaSpesaMapper.toEntity(lista));
                if (listener != null) {
                    listener.onSuccess();
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    public void deleteAllListeCompletate(OnDeleteListener listener) {
        executorService.execute(() -> {
            try {
                listaSpesaDao.deleteAllCompletate();
                if (listener != null) {
                    listener.onSuccess();
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    // ========== Item Spesa ==========

    public LiveData<List<ItemSpesa>> getItemsByListaId(int idLista) {
        return Transformations.map(itemSpesaDao.getItemsByListaId(idLista), ItemSpesaMapper::toDomainList);
    }

    public LiveData<List<ItemSpesa>> getItemNonCompletati(int idLista) {
        return Transformations.map(itemSpesaDao.getItemNonCompletati(idLista), ItemSpesaMapper::toDomainList);
    }

    public LiveData<List<ItemSpesa>> getItemCompletati(int idLista) {
        return Transformations.map(itemSpesaDao.getItemCompletati(idLista), ItemSpesaMapper::toDomainList);
    }

    public void insertItem(ItemSpesa item, OnInsertListener listener) {
        executorService.execute(() -> {
            try {
                long id = itemSpesaDao.insert(ItemSpesaMapper.toEntity(item));
                if (listener != null) {
                    listener.onSuccess((int) id);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    public void updateItem(ItemSpesa item, OnUpdateListener listener) {
        executorService.execute(() -> {
            try {
                itemSpesaDao.update(ItemSpesaMapper.toEntity(item));
                if (listener != null) {
                    listener.onSuccess();
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    public void deleteItem(ItemSpesa item, OnDeleteListener listener) {
        executorService.execute(() -> {
            try {
                itemSpesaDao.delete(ItemSpesaMapper.toEntity(item));
                if (listener != null) {
                    listener.onSuccess();
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    public void toggleItemCompletato(int itemId, boolean completato, OnUpdateListener listener) {
        executorService.execute(() -> {
            try {
                itemSpesaDao.updateCompletato(itemId, completato);
                if (listener != null) {
                    listener.onSuccess();
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    public void deleteCompletatiByListaId(int idLista, OnDeleteListener listener) {
        executorService.execute(() -> {
            try {
                itemSpesaDao.deleteCompletatiByListaId(idLista);
                if (listener != null) {
                    listener.onSuccess();
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    // ========== Callbacks ==========

    public interface OnInsertListener {
        void onSuccess(int id);
        void onError(Exception e);
    }

    public interface OnUpdateListener {
        void onSuccess();
        void onError(Exception e);
    }

    public interface OnDeleteListener {
        void onSuccess();
        void onError(Exception e);
    }
}
