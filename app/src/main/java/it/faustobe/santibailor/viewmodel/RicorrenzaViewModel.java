package it.faustobe.santibailor.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.faustobe.santibailor.data.entities.RicorrenzaConTipo;
import it.faustobe.santibailor.data.repository.RicorrenzaRepository;

public class RicorrenzaViewModel extends AndroidViewModel {
    private RicorrenzaRepository repository;
    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>();
    private final LiveData<List<RicorrenzaConTipo>> ricorrenzeDelGiorno;
    private final LiveData<List<RicorrenzaConTipo>> ricorrenzeReligiose;
    private final LiveData<List<RicorrenzaConTipo>> ricorrenzeLaiche;

    public RicorrenzaViewModel(Application application) {
        super(application);
        repository = new RicorrenzaRepository(application);
        selectedDate.setValue(new Date()); // Imposta la data corrente di default

        ricorrenzeDelGiorno = Transformations.switchMap(selectedDate, date -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int giorno = cal.get(Calendar.DAY_OF_MONTH);
            int mese = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH è zero-based
            return repository.getRicorrenzeDelGiorno(giorno, mese);
        });

        ricorrenzeReligiose = Transformations.switchMap(selectedDate, date -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int giorno = cal.get(Calendar.DAY_OF_MONTH);
            int mese = cal.get(Calendar.MONTH) + 1;
            return repository.getRicorrenzeReligiose(giorno, mese);
        });

        ricorrenzeLaiche = Transformations.switchMap(selectedDate, date -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int giorno = cal.get(Calendar.DAY_OF_MONTH);
            int mese = cal.get(Calendar.MONTH) + 1;
            return repository.getRicorrenzeLaiche(giorno, mese);
        });
    }

    public void setDate(Date date) {
        selectedDate.setValue(date);
    }

    public LiveData<Date> getSelectedDate() {
        return selectedDate;
    }

    public LiveData<List<RicorrenzaConTipo>> getRicorrenzeDelGiorno() {
        return ricorrenzeDelGiorno;
    }

    public LiveData<List<RicorrenzaConTipo>> getRicorrenzeReligiose() {
        return ricorrenzeReligiose;
    }

    public LiveData<List<RicorrenzaConTipo>> getRicorrenzeLaiche() {
        return ricorrenzeLaiche;
    }

    public void moveToNextDay() {
        Date currentDate = selectedDate.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDate.setValue(calendar.getTime());
    }

    public void moveToPreviousDay() {
        Date currentDate = selectedDate.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        selectedDate.setValue(calendar.getTime());
    }
}