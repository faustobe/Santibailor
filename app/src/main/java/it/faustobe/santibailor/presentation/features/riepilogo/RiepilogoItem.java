package it.faustobe.santibailor.presentation.features.riepilogo;

import java.util.Date;

import it.faustobe.santibailor.domain.model.Impegno;
import it.faustobe.santibailor.domain.model.Ricorrenza;

public class RiepilogoItem {
    public enum Type { IMPEGNO, RICORRENZA }

    private Type type;
    private Impegno impegno;
    private Ricorrenza ricorrenza;

    public RiepilogoItem(Impegno impegno) {
        this.type = Type.IMPEGNO;
        this.impegno = impegno;
    }

    public RiepilogoItem(Ricorrenza ricorrenza) {
        this.type = Type.RICORRENZA;
        this.ricorrenza = ricorrenza;
    }

    public Type getType() {
        return type;
    }

    public Impegno getImpegno() {
        return impegno;
    }

    public Ricorrenza getRicorrenza() {
        return ricorrenza;
    }

    public String getTitle() {
        if (type == Type.IMPEGNO) {
            return impegno.getTitolo();
        } else {
            return ricorrenza.getNomeCompleto();
        }
    }

    public long getDateTimestamp() {
        if (type == Type.IMPEGNO) {
            return impegno.getDataOra();
        } else {
            // Ricorrenza non ha timestamp, usa mese/giorno
            return 0;
        }
    }

    public boolean isCompleted() {
        return type == Type.IMPEGNO && impegno.isCompletato();
    }
}
