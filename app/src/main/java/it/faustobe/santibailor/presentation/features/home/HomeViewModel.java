package it.faustobe.santibailor.presentation.features.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<Boolean> allComponentsCollapsed = new MutableLiveData<>(true);

    public void setAllComponentsCollapsed(boolean collapsed) {
        allComponentsCollapsed.setValue(collapsed);
    }

    public LiveData<Boolean> getAllComponentsCollapsed() {
        return allComponentsCollapsed;
    }

    public void refreshComponentsState() {
        // Questo metodo forza un aggiornamento del LiveData
        boolean currentState = allComponentsCollapsed.getValue() != null ? allComponentsCollapsed.getValue() : true;
        allComponentsCollapsed.setValue(currentState);
    }
}
