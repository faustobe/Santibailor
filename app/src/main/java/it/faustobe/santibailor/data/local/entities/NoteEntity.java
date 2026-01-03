package it.faustobe.santibailor.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity Room per la tabella note
 * Rappresenta una singola nota/appunto nel database
 */
@Entity(
    tableName = "note",
    indices = {
        @Index(value = {"data_modifica"}, name = "idx_note_data_modifica")
    }
)
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "titolo")
    private String titolo;

    @ColumnInfo(name = "contenuto")
    private String contenuto;

    @NonNull
    @ColumnInfo(name = "data_creazione")
    private long dataCreazione;

    @NonNull
    @ColumnInfo(name = "data_modifica")
    private long dataModifica;

    /**
     * Constructor per Room
     */
    public NoteEntity(@NonNull String titolo, String contenuto,
                      long dataCreazione, long dataModifica) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.dataCreazione = dataCreazione;
        this.dataModifica = dataModifica;
    }

    // ========== Getters ==========

    public int getId() {
        return id;
    }

    @NonNull
    public String getTitolo() {
        return titolo;
    }

    public String getContenuto() {
        return contenuto;
    }

    public long getDataCreazione() {
        return dataCreazione;
    }

    public long getDataModifica() {
        return dataModifica;
    }

    // ========== Setters ==========

    public void setId(int id) {
        this.id = id;
    }

    public void setTitolo(@NonNull String titolo) {
        this.titolo = titolo;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public void setDataCreazione(long dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public void setDataModifica(long dataModifica) {
        this.dataModifica = dataModifica;
    }
}
