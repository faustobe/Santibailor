package it.faustobe.santibailor.domain.model;

import it.faustobe.santibailor.R;

/**
 * Costanti per la priorità degli impegni
 */
public class Priorita {
    public static final String BASSA = "BASSA";
    public static final String MEDIA = "MEDIA";
    public static final String ALTA = "ALTA";

    public static String[] getAll() {
        return new String[]{BASSA, MEDIA, ALTA};
    }

    public static int getColorResource(String priorita) {
        if (priorita == null) {
            return R.color.priority_medium;
        }
        switch (priorita) {
            case ALTA:
                return R.color.priority_high;
            case MEDIA:
                return R.color.priority_medium;
            case BASSA:
                return R.color.priority_low;
            default:
                return R.color.priority_medium;
        }
    }

    public static String getDisplayName(String priorita) {
        if (priorita == null) {
            return "Media";
        }
        switch (priorita) {
            case ALTA:
                return "Alta";
            case MEDIA:
                return "Media";
            case BASSA:
                return "Bassa";
            default:
                return "Media";
        }
    }
}
