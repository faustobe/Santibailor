package it.faustobe.santibailor.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class RicorrenzaConTipo {
    @Embedded
    public Ricorrenza ricorrenza;

    @Relation(
            parentColumn = "tipo_ricorrenza_id",
            entityColumn = "id"
    )
    public TipoRicorrenza tipoRicorrenza;
}