package it.faustobe.santibailor.data.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity per rappresentare un impegno/evento nel database Room
 */
@Entity(tableName = "impegni")
public class ImpegnoEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "titolo")
    private String titolo;

    @ColumnInfo(name = "descrizione")
    private String descrizione;

    @ColumnInfo(name = "data_ora")
    private long dataOra; // Timestamp in millisecondi

    @ColumnInfo(name = "categoria")
    private String categoria; // Es: "Lavoro", "Personale", "Compleanno", etc.

    @ColumnInfo(name = "reminder_enabled")
    private boolean reminderEnabled;

    @ColumnInfo(name = "reminder_minutes_before")
    private int reminderMinutesBefore; // Minuti prima dell'evento

    @ColumnInfo(name = "completato")
    private boolean completato;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "updated_at")
    private long updatedAt;

    @ColumnInfo(name = "priorita")
    private String priorita; // "BASSA", "MEDIA", "ALTA"

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    // Constructor
    public ImpegnoEntity(String titolo, String descrizione, long dataOra, String categoria,
                         boolean reminderEnabled, int reminderMinutesBefore, boolean completato,
                         String note, long createdAt, long updatedAt, String priorita, String imageUrl) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataOra = dataOra;
        this.categoria = categoria;
        this.reminderEnabled = reminderEnabled;
        this.reminderMinutesBefore = reminderMinutesBefore;
        this.completato = completato;
        this.note = note;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.priorita = priorita;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public long getDataOra() {
        return dataOra;
    }

    public void setDataOra(long dataOra) {
        this.dataOra = dataOra;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public int getReminderMinutesBefore() {
        return reminderMinutesBefore;
    }

    public void setReminderMinutesBefore(int reminderMinutesBefore) {
        this.reminderMinutesBefore = reminderMinutesBefore;
    }

    public boolean isCompletato() {
        return completato;
    }

    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPriorita() {
        return priorita;
    }

    public void setPriorita(String priorita) {
        this.priorita = priorita;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
