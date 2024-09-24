package it.faustobe.santibailor.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SearchPreferences {
    private static final String PREFS_NAME = "SearchPreferences";
    private static final String PREF_NOME = "nome";
    private static final String PREF_TIPO = "tipo";
    private static final String PREF_DATA_INIZIO = "dataInizio";
    private static final String PREF_DATA_FINE = "dataFine";

    public static void saveSearchParams(Context context, String nome, String tipo, String dataInizio, String dataFine) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_NOME, nome);
        editor.putString(PREF_TIPO, tipo);
        editor.putString(PREF_DATA_INIZIO, dataInizio);
        editor.putString(PREF_DATA_FINE, dataFine);
        editor.apply();
    }

    public static void saveTipo(Context context, String tipo) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_TIPO, tipo);
        editor.apply();
    }

    public static SearchParams getSearchParams(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return new SearchParams(
                prefs.getString(PREF_NOME, ""),
                prefs.getString(PREF_TIPO, "Tutte"),
                prefs.getString(PREF_DATA_INIZIO, ""),
                prefs.getString(PREF_DATA_FINE, "")
        );
    }

    public static String getSavedTipo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PREF_TIPO, "Tutte");
    }

    public static void clearSearchParams(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public static class SearchParams {
        public final String nome;
        public final String tipo;
        public final String dataInizio;
        public final String dataFine;

        SearchParams(String nome, String tipo, String dataInizio, String dataFine) {
            this.nome = nome;
            this.tipo = tipo;
            this.dataInizio = dataInizio;
            this.dataFine = dataFine;
        }
    }
}
