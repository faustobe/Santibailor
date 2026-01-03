package it.faustobe.santibailor.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Gestisce il tema dell'applicazione (Chiaro, Scuro, Sistema)
 */
public class ThemeManager {
    private static final String PREFS_NAME = "settings";
    private static final String KEY_THEME = "theme";

    // Valori possibili per il tema
    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";
    public static final String THEME_SYSTEM = "system";

    /**
     * Applica il tema salvato nelle preferenze
     */
    public static void applyTheme(Context context) {
        String theme = getSavedTheme(context);
        applyTheme(theme);
    }

    /**
     * Applica il tema specificato
     */
    public static void applyTheme(String theme) {
        switch (theme) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                break;
        }
    }

    /**
     * Salva il tema selezionato
     */
    public static void saveTheme(Context context, String theme) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_THEME, theme).apply();
    }

    /**
     * Ottiene il tema salvato (default: system)
     */
    public static String getSavedTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_THEME, THEME_SYSTEM);
    }

    /**
     * Ottiene il nome leggibile del tema
     */
    public static String getThemeDisplayName(String theme) {
        switch (theme) {
            case THEME_LIGHT:
                return "Chiaro";
            case THEME_DARK:
                return "Scuro";
            case THEME_SYSTEM:
            default:
                return "Sistema";
        }
    }
}
