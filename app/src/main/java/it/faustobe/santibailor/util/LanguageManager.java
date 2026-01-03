package it.faustobe.santibailor.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 * Gestisce la lingua dell'applicazione
 */
public class LanguageManager {
    private static final String PREFS_NAME = "settings";
    private static final String KEY_LANGUAGE = "language";

    // Lingue supportate
    public static final String LANGUAGE_ITALIAN = "it";
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_SYSTEM = "system";

    /**
     * Applica la lingua salvata nelle preferenze
     */
    public static void applyLanguage(Context context) {
        String language = getSavedLanguage(context);
        applyLanguage(context, language);
    }

    /**
     * Applica la lingua specificata
     */
    public static void applyLanguage(Context context, String language) {
        Locale locale;

        if (LANGUAGE_SYSTEM.equals(language)) {
            // Usa la lingua di sistema
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Resources.getSystem().getConfiguration().getLocales().get(0);
            } else {
                locale = Resources.getSystem().getConfiguration().locale;
            }
        } else {
            // Usa la lingua specificata
            locale = new Locale(language);
        }

        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    /**
     * Salva la lingua selezionata
     */
    public static void saveLanguage(Context context, String language) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, language).apply();
    }

    /**
     * Ottiene la lingua salvata (default: system)
     */
    public static String getSavedLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_SYSTEM);
    }

    /**
     * Ottiene il nome leggibile della lingua
     */
    public static String getLanguageDisplayName(String language) {
        switch (language) {
            case LANGUAGE_ITALIAN:
                return "Italiano";
            case LANGUAGE_ENGLISH:
                return "English";
            case LANGUAGE_SYSTEM:
            default:
                return "Sistema";
        }
    }

    /**
     * Ricrea l'activity per applicare la nuova lingua
     */
    public static void recreateActivity(Activity activity) {
        if (activity != null) {
            activity.recreate();
        }
    }
}
