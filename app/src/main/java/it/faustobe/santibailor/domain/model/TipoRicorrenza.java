package it.faustobe.santibailor.domain.model;

import java.util.Objects;

public class TipoRicorrenza {
    public static final int RELIGIOSA = 1;
    public static final int LAICA = 2;

    private int id;
    private String nome;

    public TipoRicorrenza(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public boolean isReligiosa() {
        return this.id == RELIGIOSA;
    }

    public boolean isLaica() {
        return this.id == LAICA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoRicorrenza that = (TipoRicorrenza) o;
        return id == that.id && Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return nome;
    }
}