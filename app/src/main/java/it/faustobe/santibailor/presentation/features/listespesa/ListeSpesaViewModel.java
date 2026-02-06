package it.faustobe.santibailor.presentation.features.listespesa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import it.faustobe.santibailor.data.local.entities.ProdottoFrequenteEntity;
import it.faustobe.santibailor.data.repository.ListaSpesaRepository;
import it.faustobe.santibailor.data.repository.ProdottoFrequenteRepository;
import it.faustobe.santibailor.domain.model.ItemSpesa;
import it.faustobe.santibailor.domain.model.ListaSpesa;

@HiltViewModel
public class ListeSpesaViewModel extends ViewModel {
    private final ListaSpesaRepository repository;
    private final ProdottoFrequenteRepository prodottoFrequenteRepository;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> listaCreatedId = new MutableLiveData<>();
    private final MutableLiveData<List<String>> suggestionsList = new MutableLiveData<>();

    @Inject
    public ListeSpesaViewModel(ListaSpesaRepository repository, ProdottoFrequenteRepository prodottoFrequenteRepository) {
        this.repository = repository;
        this.prodottoFrequenteRepository = prodottoFrequenteRepository;
    }

    // ========== LiveData Getters ==========

    public LiveData<List<ListaSpesa>> getAllListe() {
        return repository.getAllListe();
    }

    public LiveData<List<ListaSpesa>> getListeAttive() {
        return repository.getListeAttive();
    }

    public LiveData<List<ListaSpesa>> getListeCompletate() {
        return repository.getListeCompletate();
    }

    public LiveData<ListaSpesa> getListaById(int id) {
        return repository.getListaById(id);
    }

    public LiveData<List<ItemSpesa>> getItemsByListaId(int idLista) {
        return repository.getItemsByListaId(idLista);
    }

    public LiveData<List<ItemSpesa>> getItemNonCompletati(int idLista) {
        return repository.getItemNonCompletati(idLista);
    }

    public LiveData<List<ItemSpesa>> getItemCompletati(int idLista) {
        return repository.getItemCompletati(idLista);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<Integer> getListaCreatedId() {
        return listaCreatedId;
    }

    public LiveData<List<String>> getSuggestionsList() {
        return suggestionsList;
    }

    // ========== Liste Operations ==========

    public void createLista(String nome, String colore) {
        if (nome == null || nome.trim().isEmpty()) {
            errorMessage.setValue("Il nome della lista non può essere vuoto");
            return;
        }

        isLoading.setValue(true);
        ListaSpesa lista = new ListaSpesa();
        lista.setNome(nome);
        lista.setDataCreazione(new Date());
        lista.setCompletata(false);
        lista.setColore(colore != null ? colore : "#4CAF50");

        repository.insertLista(lista, new ListaSpesaRepository.OnInsertListener() {
            @Override
            public void onSuccess(int id) {
                isLoading.postValue(false);
                successMessage.postValue("Lista creata con successo");
                listaCreatedId.postValue(id);
            }

            @Override
            public void onError(Exception e) {
                isLoading.postValue(false);
                errorMessage.postValue("Errore durante la creazione: " + e.getMessage());
            }
        });
    }

    public void updateLista(ListaSpesa lista) {
        if (lista == null) {
            errorMessage.setValue("Lista non valida");
            return;
        }

        isLoading.setValue(true);
        repository.updateLista(lista, new ListaSpesaRepository.OnUpdateListener() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                successMessage.postValue("Lista aggiornata");
            }

            @Override
            public void onError(Exception e) {
                isLoading.postValue(false);
                errorMessage.postValue("Errore durante l'aggiornamento: " + e.getMessage());
            }
        });
    }

    public void deleteLista(ListaSpesa lista) {
        if (lista == null) {
            errorMessage.setValue("Lista non valida");
            return;
        }

        isLoading.setValue(true);
        repository.deleteLista(lista, new ListaSpesaRepository.OnDeleteListener() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                successMessage.postValue("Lista eliminata");
            }

            @Override
            public void onError(Exception e) {
                isLoading.postValue(false);
                errorMessage.postValue("Errore durante l'eliminazione: " + e.getMessage());
            }
        });
    }

    public void toggleListaCompletata(ListaSpesa lista) {
        if (lista == null) return;
        lista.setCompletata(!lista.isCompletata());
        updateLista(lista);
    }

    public void deleteAllListeCompletate() {
        isLoading.setValue(true);
        repository.deleteAllListeCompletate(new ListaSpesaRepository.OnDeleteListener() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                successMessage.postValue("Liste completate eliminate");
            }

            @Override
            public void onError(Exception e) {
                isLoading.postValue(false);
                errorMessage.postValue("Errore: " + e.getMessage());
            }
        });
    }

    // ========== Item Operations ==========

    public void addItem(int idLista, String nome, String quantita, String categoria) {
        if (nome == null || nome.trim().isEmpty()) {
            errorMessage.setValue("Il nome dell'item non può essere vuoto");
            return;
        }

        ItemSpesa item = new ItemSpesa();
        item.setIdLista(idLista);
        item.setNome(nome);
        item.setQuantita(quantita);
        item.setCategoria(categoria);
        item.setCompletato(false);
        item.setOrdine(0);

        repository.insertItem(item, new ListaSpesaRepository.OnInsertListener() {
            @Override
            public void onSuccess(int id) {
                successMessage.postValue(null);
                // Salva o aggiorna la frequenza del prodotto
                prodottoFrequenteRepository.salvaOAggiornaFrequenza(nome, categoria);
            }

            @Override
            public void onError(Exception e) {
                if (e instanceof ListaSpesaRepository.ItemAlreadyExistsException) {
                    errorMessage.postValue("\"" + nome + "\" è già nella lista");
                } else {
                    errorMessage.postValue("Errore: " + e.getMessage());
                }
            }
        });
    }

    public void updateItem(ItemSpesa item) {
        if (item == null) {
            errorMessage.setValue("Item non valido");
            return;
        }

        repository.updateItem(item, new ListaSpesaRepository.OnUpdateListener() {
            @Override
            public void onSuccess() {
                // Silenzioso
            }

            @Override
            public void onError(Exception e) {
                errorMessage.postValue("Errore: " + e.getMessage());
            }
        });
    }

    public void deleteItem(ItemSpesa item) {
        if (item == null) {
            errorMessage.setValue("Item non valido");
            return;
        }

        repository.deleteItem(item, new ListaSpesaRepository.OnDeleteListener() {
            @Override
            public void onSuccess() {
                successMessage.postValue("Item eliminato");
            }

            @Override
            public void onError(Exception e) {
                errorMessage.postValue("Errore: " + e.getMessage());
            }
        });
    }

    public void toggleItemCompletato(ItemSpesa item) {
        if (item == null) return;

        repository.toggleItemCompletato(item.getId(), !item.isCompletato(),
            new ListaSpesaRepository.OnUpdateListener() {
                @Override
                public void onSuccess() {
                    // Silenzioso - l'UI si aggiorna automaticamente tramite LiveData
                }

                @Override
                public void onError(Exception e) {
                    errorMessage.postValue("Errore: " + e.getMessage());
                }
            });
    }

    public void deleteItemCompletati(int idLista) {
        repository.deleteCompletatiByListaId(idLista, new ListaSpesaRepository.OnDeleteListener() {
            @Override
            public void onSuccess() {
                successMessage.postValue("Item completati eliminati");
            }

            @Override
            public void onError(Exception e) {
                errorMessage.postValue("Errore: " + e.getMessage());
            }
        });
    }

    // ========== Prodotti Frequenti ==========

    public void searchProdotti(String query) {
        if (query == null || query.trim().isEmpty()) {
            suggestionsList.setValue(new java.util.ArrayList<>());
            return;
        }

        prodottoFrequenteRepository.searchProdotti(query, results -> {
            List<String> nomiProdotti = new java.util.ArrayList<>();
            for (ProdottoFrequenteEntity prodotto : results) {
                nomiProdotti.add(prodotto.getNome());
            }
            suggestionsList.postValue(nomiProdotti);
        });
    }

    // ========== Utility ==========

    public void clearMessages() {
        errorMessage.setValue(null);
        successMessage.setValue(null);
    }
}
