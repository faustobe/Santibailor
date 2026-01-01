package it.faustobe.santibailor.presentation.features.impegni;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import it.faustobe.santibailor.data.repository.ImpegnoRepository;
import it.faustobe.santibailor.domain.model.Impegno;

@HiltViewModel
public class ImpegniViewModel extends ViewModel {

    private final ImpegnoRepository repository;

    private final MutableLiveData<String> operationResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public ImpegniViewModel(ImpegnoRepository repository) {
        this.repository = repository;
    }

    // Getters per LiveData
    public LiveData<List<Impegno>> getAllImpegni() {
        return repository.getAllImpegni();
    }

    public LiveData<List<Impegno>> getImpegniOggi() {
        return repository.getImpegniOggi();
    }

    public LiveData<List<Impegno>> getImpegniFuturi(int limit) {
        return repository.getImpegniFuturi(limit);
    }

    public LiveData<Impegno> getImpegnoById(int id) {
        return repository.getImpegnoById(id);
    }

    public LiveData<String> getOperationResult() {
        return operationResult;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // Operazioni CRUD
    public void insertImpegno(Impegno impegno) {
        android.util.Log.d("ImpegniViewModel", "insertImpegno: " + impegno.getTitolo() + ", imageUrl: " + impegno.getImageUrl());
        isLoading.setValue(true);
        long now = System.currentTimeMillis();
        impegno.setUpdatedAt(now);

        repository.insertImpegno(impegno, new ImpegnoRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int id) {
                android.util.Log.d("ImpegniViewModel", "Impegno inserted successfully with ID: " + id);
                isLoading.postValue(false);
                operationResult.postValue("Impegno aggiunto con successo");
            }

            @Override
            public void onError(String error) {
                android.util.Log.e("ImpegniViewModel", "Error inserting impegno: " + error);
                isLoading.postValue(false);
                operationResult.postValue("Errore: " + error);
            }
        });
    }

    public void updateImpegno(Impegno impegno) {
        android.util.Log.d("ImpegniViewModel", "updateImpegno: " + impegno.getTitolo() + ", imageUrl: " + impegno.getImageUrl());
        isLoading.setValue(true);
        impegno.setUpdatedAt(System.currentTimeMillis());

        repository.updateImpegno(impegno, new ImpegnoRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int id) {
                android.util.Log.d("ImpegniViewModel", "Impegno updated successfully");
                isLoading.postValue(false);
                operationResult.postValue("Impegno aggiornato");
            }

            @Override
            public void onError(String error) {
                android.util.Log.e("ImpegniViewModel", "Error updating impegno: " + error);
                isLoading.postValue(false);
                operationResult.postValue("Errore: " + error);
            }
        });
    }

    public void deleteImpegno(Impegno impegno) {
        isLoading.setValue(true);
        repository.deleteImpegno(impegno, new ImpegnoRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int id) {
                isLoading.postValue(false);
                operationResult.postValue("Impegno eliminato");
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                operationResult.postValue("Errore: " + error);
            }
        });
    }

    public void markAsCompletato(int id) {
        repository.markAsCompletato(id, new ImpegnoRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int resultId) {
                operationResult.postValue("Impegno completato!");
            }

            @Override
            public void onError(String error) {
                operationResult.postValue("Errore: " + error);
            }
        });
    }

    public void markAsNonCompletato(int id) {
        repository.markAsNonCompletato(id, new ImpegnoRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int resultId) {
                operationResult.postValue("Impegno riaperto");
            }

            @Override
            public void onError(String error) {
                operationResult.postValue("Errore: " + error);
            }
        });
    }
}
