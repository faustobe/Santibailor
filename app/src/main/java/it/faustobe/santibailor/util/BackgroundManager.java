package it.faustobe.santibailor.util;

import android.content.Context;
import android.content.SharedPreferences;

import it.faustobe.santibailor.R;

/**
 * Gestisce lo sfondo della home (Santo, Nessuno, Gradienti, Personalizzato)
 */
public class BackgroundManager {
    private static final String PREFS_NAME = "settings";
    private static final String KEY_BACKGROUND = "background";
    private static final String KEY_CUSTOM_BACKGROUND_PATH = "custom_background_path";

    public static final String BG_SANTO = "santo";
    public static final String BG_NONE = "none";
    public static final String BG_GRADIENT_WARM = "gradient_warm";
    public static final String BG_GRADIENT_COOL = "gradient_cool";
    public static final String BG_GRADIENT_SUNSET = "gradient_sunset";
    public static final String BG_CUSTOM = "custom";

    public static void saveBackground(Context context, String background) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_BACKGROUND, background).apply();
    }

    public static String getSavedBackground(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_BACKGROUND, BG_SANTO);
    }

    public static void saveCustomBackgroundPath(Context context, String path) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_CUSTOM_BACKGROUND_PATH, path).apply();
    }

    public static String getCustomBackgroundPath(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_CUSTOM_BACKGROUND_PATH, null);
    }

    /**
     * Restituisce il resource ID del drawable per i gradienti, 0 per altri tipi
     */
    public static int getBackgroundDrawableResId(String background) {
        switch (background) {
            case BG_GRADIENT_WARM:
                return R.drawable.bg_gradient_warm;
            case BG_GRADIENT_COOL:
                return R.drawable.bg_gradient_cool;
            case BG_GRADIENT_SUNSET:
                return R.drawable.bg_gradient_sunset;
            default:
                return 0;
        }
    }
}
