package it.faustobe.santibailor.domain.model;

import java.util.Objects;

/**
 * Domain model per un impegno/evento
 */
public class Impegno {
    private int id;
    private String titolo;
    private String descrizione;
    private long dataOra;
    private String categoria;
    private boolean reminderEnabled;
    private int reminderMinutesBefore;
    private boolean completato;
    private String note;
    private long createdAt;
    private long updatedAt;
    private String priorita;
    private String imageUrl;

    public Impegno(int id, String titolo, String descrizione, long dataOra, String categoria,
                   boolean reminderEnabled, int reminderMinutesBefore, boolean completato,
                   String note, long createdAt, long updatedAt, String priorita, String imageUrl) {
        this.id = id;
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

    // Getters
    public int getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public long getDataOra() {
        return dataOra;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public int getReminderMinutesBefore() {
        return reminderMinutesBefore;
    }

    public boolean isCompletato() {
        return completato;
    }

    public String getNote() {
        return note;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setDataOra(long dataOra) {
        this.dataOra = dataOra;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public void setReminderMinutesBefore(int reminderMinutesBefore) {
        this.reminderMinutesBefore = reminderMinutesBefore;
    }

    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    public void setNote(String note) {
        this.note = note;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Impegno impegno = (Impegno) o;
        return id == impegno.id &&
                dataOra == impegno.dataOra &&
                Objects.equals(titolo, impegno.titolo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titolo, dataOra);
    }

    @Override
    public String toString() {
        return "Impegno{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", dataOra=" + dataOra +
                ", categoria='" + categoria + '\'' +
                ", completato=" + completato +
                '}';
    }
}
