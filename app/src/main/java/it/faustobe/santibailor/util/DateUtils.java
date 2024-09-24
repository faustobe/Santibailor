package it.faustobe.santibailor.util;

import java.util.Calendar;

public class DateUtils {

    private static final String[] MONTH_NAMES_SHORT = {"Gen", "Feb", "Mar", "Apr", "Mag", "Giu",
            "Lug", "Ago", "Set", "Ott", "Nov", "Dic"};

    private static final String[] MONTH_NAMES_FULL = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
            "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};

    public static String[] getMonthNamesShort() {
        return MONTH_NAMES_SHORT;
    }

    public static String getMonthNameFull(int month) {
        // month è 0-based
        return MONTH_NAMES_FULL[month];
    }

    public static int getMaxDaysInMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String formatDate(int day, int month) {
        // month è 0-based nel database, ma vogliamo visualizzarlo come 1-based
        return String.format("%02d/%02d", day, month + 1);
    }

    public static String getCurrentMonthNameFull() {
        Calendar cal = Calendar.getInstance();
        return getMonthNameFull(cal.get(Calendar.MONTH));
    }
}
