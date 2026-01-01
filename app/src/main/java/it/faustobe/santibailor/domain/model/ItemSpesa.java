package it.faustobe.santibailor.domain.model;

public class ItemSpesa {
    private int id;
    private int idLista;
    private String nome;
    private String quantita; // Es: "2kg", "3 pezzi", etc.
    private boolean completato;
    private String categoria; // Es: "Frutta", "Verdura", "Latticini"
    private String note;
    private int ordine; // Per ordinare gli item nella lista

    public ItemSpesa() {
    }

    public ItemSpesa(int id, int idLista, String nome, String quantita, boolean completato,
                     String categoria, String note, int ordine) {
        this.id = id;
        this.idLista = idLista;
        this.nome = nome;
        this.quantita = quantita;
        this.completato = completato;
        this.categoria = categoria;
        this.note = note;
        this.ordine = ordine;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getIdLista() {
        return idLista;
    }

    public String getNome() {
        return nome;
    }

    public String getQuantita() {
        return quantita;
    }

    public boolean isCompletato() {
        return completato;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNote() {
        return note;
    }

    public int getOrdine() {
        return ordine;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantita(String quantita) {
        this.quantita = quantita;
    }

    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }
}
