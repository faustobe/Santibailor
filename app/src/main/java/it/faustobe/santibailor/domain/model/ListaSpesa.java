package it.faustobe.santibailor.domain.model;

import java.util.Date;
import java.util.List;

public class ListaSpesa {
    private int id;
    private String nome;
    private Date dataCreazione;
    private boolean completata;
    private String colore; // Colore associato alla lista (es: "#FF5722")
    private List<ItemSpesa> items; // Lista degli item (opzionale, può essere caricata separatamente)

    public ListaSpesa() {
    }

    public ListaSpesa(int id, String nome, Date dataCreazione, boolean completata, String colore) {
        this.id = id;
        this.nome = nome;
        this.dataCreazione = dataCreazione;
        this.completata = completata;
        this.colore = colore;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public boolean isCompletata() {
        return completata;
    }

    public String getColore() {
        return colore;
    }

    public List<ItemSpesa> getItems() {
        return items;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public void setCompletata(boolean completata) {
        this.completata = completata;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public void setItems(List<ItemSpesa> items) {
        this.items = items;
    }

    // Metodi di utilità
    public int getNumeroItemCompletati() {
        if (items == null) return 0;
        int count = 0;
        for (ItemSpesa item : items) {
            if (item.isCompletato()) count++;
        }
        return count;
    }

    public int getNumeroItemTotali() {
        return items != null ? items.size() : 0;
    }

    public int getProgressoPercentuale() {
        if (items == null || items.isEmpty()) return 0;
        return (int) ((getNumeroItemCompletati() * 100.0) / getNumeroItemTotali());
    }
}
