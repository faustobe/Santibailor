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
    private final MutableLiveData<Boolean> notificationsEnabled = new MutableLiveData<>();
    private final MutableLiveData<String> fontSize = new MutableLiveData<>();
    private final MutableLiveData<Boolean> syncEnabled = new MutableLiveData<>();
    private final MutableLiveData<String> backupFrequency = new MutableLiveData<>();
    private final MutableLiveData<String> recurrenceRepetition = new MutableLiveData<>();
    private final MutableLiveData<Integer> recurrenceReminderDays = new MutableLiveData<>();
    private final MutableLiveData<String> commitmentViewType = new MutableLiveData<>();
    private final MutableLiveData<String> shoppingListOrder = new MutableLiveData<>();
    private final MutableLiveData<Boolean> calendarIntegrationEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> voiceAssistantIntegrationEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> appLockEnabled = new MutableLiveData<>();
    private final MutableLiveData<Boolean> dataEncryptionEnabled = new MutableLiveData<>();

    private final SharedPreferences sharedPreferences;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("settings", Context.MODE_PRIVATE);
        loadSettings();
    }

    private void loadSettings() {
        theme.setValue(sharedPreferences.getString("theme", "system"));
        language.setValue(sharedPreferences.getString("language", "system"));
        notificationsEnabled.setValue(sharedPreferences.getBoolean("notifications_enabled", true));
        fontSize.setValue(sharedPreferences.getString("font_size", "medium"));
        syncEnabled.setValue(sharedPreferences.getBoolean("sync_enabled", false));
        backupFrequency.setValue(sharedPreferences.getString("backup_frequency", "weekly"));
        recurrenceRepetition.setValue(sharedPreferences.getString("recurrence_repetition", "yearly"));
        recurrenceReminderDays.setValue(sharedPreferences.getInt("recurrence_reminder_days", 7));
        commitmentViewType.setValue(sharedPreferences.getString("commitment_view_type", "daily"));
        shoppingListOrder.setValue(sharedPreferences.getString("shopping_list_order", "category"));
        calendarIntegrationEnabled.setValue(sharedPreferences.getBoolean("calendar_integration_enabled", false));
        voiceAssistantIntegrationEnabled.setValue(sharedPreferences.getBoolean("voice_assistant_integration_enabled", false));
        appLockEnabled.setValue(sharedPreferences.getBoolean("app_lock_enabled", false));
        dataEncryptionEnabled.setValue(sharedPreferences.getBoolean("data_encryption_enabled", false));
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

    // Metodi per showReligiose
    public LiveData<Boolean> getShowReligiose() {
        return showReligiose;
    }

    public void setShowReligiose(boolean show) {
        showReligiose.setValue(show);
    }

    // Metodi per showLaiche
    public LiveData<Boolean> getShowLaiche() {
        return showLaiche;
    }

    public void setShowLaiche(boolean show) {
        showLaiche.setValue(show);
    }

    // Metodi per ricorrenzeViewType
    public LiveData<String> getRicorrenzeViewType() {
        return ricorrenzeViewType;
    }

    public void setRicorrenzeViewType(String viewType) {
        ricorrenzeViewType.setValue(viewType);
    }

    // Implementa getter e setter per tutte le impostazioni
    public LiveData<String> getTheme() { return theme; }
    public void setTheme(String newTheme) {
        theme.setValue(newTheme);
        saveSettings("theme", newTheme);
    }

    public LiveData<String> getLanguage() { return theme; }
    public void setLanguage(String newTheme) {
        theme.setValue(newTheme);
        saveSettings("theme", newTheme);
    }

    public LiveData<Boolean> getNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean enabled) {
        notificationsEnabled.setValue(enabled);
        saveSettings("notifications_enabled", enabled);
    }

    public LiveData<String> getFontSize() { return fontSize; }
    public void setFontSize(String size) {
        fontSize.setValue(size);
    }

    public LiveData<Boolean> getSyncEnabled() { return syncEnabled; }
    public void setSyncEnabled(boolean enabled) {
        syncEnabled.setValue(enabled);
    }

    public LiveData<String> getBackupFrequency() { return backupFrequency; }
    public void setBackupFrequency(String frequency) {
        backupFrequency.setValue(frequency);
    }

    public LiveData<String> getRecurrenceRepetition() { return recurrenceRepetition; }
    public void setRecurrenceRepetition(String repetition) {
        recurrenceRepetition.setValue(repetition);
    }

    public LiveData<Integer> getRecurrenceReminderDays() { return recurrenceReminderDays; }
    public void setRecurrenceReminderDays(int days) {
        recurrenceReminderDays.setValue(days);
    }

    public LiveData<String> getCommitmentViewType() { return commitmentViewType; }
    public void setCommitmentViewType(String viewType) {
        commitmentViewType.setValue(viewType);
    }

    public LiveData<String> getShoppingListOrder() { return shoppingListOrder; }
    public void setShoppingListOrder(String order) {
        shoppingListOrder.setValue(order);
    }

    public LiveData<Boolean> getCalendarIntegrationEnabled() { return calendarIntegrationEnabled; }
    public void setCalendarIntegrationEnabled(boolean enabled) {
        calendarIntegrationEnabled.setValue(enabled);
    }

    public LiveData<Boolean> getVoiceAssistantIntegrationEnabled() { return voiceAssistantIntegrationEnabled; }
    public void setVoiceAssistantIntegrationEnabled(boolean enabled) {
        voiceAssistantIntegrationEnabled.setValue(enabled);
    }

    public LiveData<Boolean> getAppLockEnabled() { return appLockEnabled; }
    public void setAppLockEnabled(boolean enabled) {
        appLockEnabled.setValue(enabled);
    }

    public LiveData<Boolean> getDataEncryptionEnabled() { return dataEncryptionEnabled; }
    public void setDataEncryptionEnabled(boolean enabled) {
        dataEncryptionEnabled.setValue(enabled);
    }

 }
