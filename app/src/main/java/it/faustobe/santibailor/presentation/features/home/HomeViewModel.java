package it.faustobe.santibailor.presentation.features.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final MutableLiveData<Boolean> allComponentsCollapsed = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> isCalendarExpanded = new MutableLiveData<>(true);

    public LiveData<Boolean> isCalendarExpanded() {
        return isCalendarExpanded;
    }

    public void setCalendarExpanded(boolean expanded) {
        isCalendarExpanded.setValue(expanded);
    }

    @Inject
    public HomeViewModel() {
        // Costruttore vuoto
        // Se in futuro avrai bisogno di iniettare dipendenze, potrai aggiungerle qui
    }

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
