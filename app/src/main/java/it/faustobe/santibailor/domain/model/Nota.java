package it.faustobe.santibailor.domain.model;

import java.util.Objects;

/**
 * Domain model per una nota/appunto
 * POJO puro senza dipendenze da Room o altre librerie
 */
public class Nota {
    private int id;
    private String titolo;
    private String contenuto;
    private long dataCreazione;
    private long dataModifica;

    /**
     * Constructor completo
     */
    public Nota(int id, String titolo, String contenuto,
                long dataCreazione, long dataModifica) {
        this.id = id;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.dataCreazione = dataCreazione;
        this.dataModifica = dataModifica;
    }

    // ========== Getters ==========

    public int getId() {
        return id;
    }

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

    public void setTitolo(String titolo) {
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

    // ========== Object Methods ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota = (Nota) o;
        return id == nota.id &&
                dataCreazione == nota.dataCreazione &&
                Objects.equals(titolo, nota.titolo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titolo, dataCreazione);
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", dataModifica=" + dataModifica +
                '}';
    }
}
