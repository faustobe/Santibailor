package it.faustobe.santibailor.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.NumberPicker;
import android.view.LayoutInflater;

import java.util.Calendar;

import it.faustobe.santibailor.R;

public class MonthDayPickerHelper {

    public interface OnDateSetListener {
        void onDateSet(int month, int day);
    }

    public static void showPicker(Context context, OnDateSetListener listener) {
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_month_day_picker, null);
        final NumberPicker monthPicker = dialogView.findViewById(R.id.month_picker);
        final NumberPicker dayPicker = dialogView.findViewById(R.id.day_picker);

        // Configurazione del picker per i mesi
        String[] monthNames = new String[]{"Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Set", "Ott", "Nov", "Dic"};
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setDisplayedValues(monthNames);

        // Configurazione del picker per i giorni
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        // Listener per aggiustare i giorni in base al mese selezionato
        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            adjustDayPickerForMonth(dayPicker, newVal);
        });

        // Imposta i valori iniziali
        Calendar cal = Calendar.getInstance();
        monthPicker.setValue(cal.get(Calendar.MONTH));
        dayPicker.setValue(cal.get(Calendar.DAY_OF_MONTH));
        adjustDayPickerForMonth(dayPicker, cal.get(Calendar.MONTH));

        new AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    if (listener != null) {
                        listener.onDateSet(monthPicker.getValue(), dayPicker.getValue());
                    }
                })
                .setNegativeButton("Annulla", null)
                .show();
    }

    private static void adjustDayPickerForMonth(NumberPicker dayPicker, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), month, 1);
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayPicker.setMaxValue(maxDays);
    }
}
