package it.faustobe.santibailor.presentation.features.settings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SettingsViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> showReligiose = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showLaiche = new MutableLiveData<>();
    private final MutableLiveData<String> ricorrenzeViewType = new MutableLiveData<>();
    private final MutableLiveData<String> theme = new MutableLiveData<>();
    private final MutableLiveData<String> language = new MutableLiveData<>();
    private final MutableLiveData<String> background = new MutableLiveData<>();
    private final MutableLiveData<Boolean> notificationsEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saintNotificationsEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> impegniNotificationsEnabled = new MutableLiveData<>();

    private final SharedPreferences sharedPreferences;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("settings", Context.MODE_PRIVATE);
        loadSettings();
    }

    private void loadSettings() {
        theme.setValue(sharedPreferences.getString("theme", "system"));
        language.setValue(sharedPreferences.getString("language", "system"));
        background.setValue(sharedPreferences.getString("background", "santo"));
        notificationsEnabled.setValue(sharedPreferences.getBoolean("notifications_enabled", true));
        saintNotificationsEnabled.setValue(sharedPreferences.getBoolean("saint_notifications_enabled", true));
        impegniNotificationsEnabled.setValue(sharedPreferences.getBoolean("impegni_notifications_enabled", true));
    }

    private void saveSettings(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }
        editor.apply();
    }

    public LiveData<Boolean> getShowReligiose() {
        return showReligiose;
    }

    public void setShowReligiose(boolean show) {
        showReligiose.setValue(show);
    }

    public LiveData<Boolean> getShowLaiche() {
        return showLaiche;
    }

    public void setShowLaiche(boolean show) {
        showLaiche.setValue(show);
    }

    public LiveData<String> getRicorrenzeViewType() {
        return ricorrenzeViewType;
    }

    public void setRicorrenzeViewType(String viewType) {
        ricorrenzeViewType.setValue(viewType);
    }

    public LiveData<String> getTheme() { return theme; }
    public void setTheme(String newTheme) {
        theme.setValue(newTheme);
        saveSettings("theme", newTheme);
    }

    public LiveData<String> getLanguage() { return language; }
    public void setLanguage(String newLanguage) {
        language.setValue(newLanguage);
        saveSettings("language", newLanguage);
    }

    public LiveData<String> getBackground() { return background; }
    public void setBackground(String newBackground) {
        background.setValue(newBackground);
        saveSettings("background", newBackground);
    }

    public LiveData<Boolean> getNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean enabled) {
        notificationsEnabled.setValue(enabled);
        saveSettings("notifications_enabled", enabled);
    }

    public LiveData<Boolean> getSaintNotificationsEnabled() { return saintNotificationsEnabled; }
    public void setSaintNotificationsEnabled(boolean enabled) {
        saintNotificationsEnabled.setValue(enabled);
        saveSettings("saint_notifications_enabled", enabled);
    }

    public LiveData<Boolean> getImpegniNotificationsEnabled() { return impegniNotificationsEnabled; }
    public void setImpegniNotificationsEnabled(boolean enabled) {
        impegniNotificationsEnabled.setValue(enabled);
        saveSettings("impegni_notifications_enabled", enabled);
    }

}
