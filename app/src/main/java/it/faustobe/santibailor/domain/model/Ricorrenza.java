package it.faustobe.santibailor.domain.model;

import java.util.Objects;

public class Ricorrenza {
    private int id;
    private int idMese;
    private int giornoDelMese;
    private String nome;
    private String bio;
    private String imageUrl;
    private String prefix;
    private String suffix;
    private int idTipo;

    public Ricorrenza(int id, int idMese, int giornoDelMese, String nome, String bio, String imageUrl, String prefix, String suffix, int idTipo) {
        this.id = id;
        this.idMese = idMese;
        this.giornoDelMese = giornoDelMese;
        this.nome = nome;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.prefix = prefix;
        this.suffix = suffix;
        this.idTipo = idTipo;
    }

    // Getters and setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdMese() { return idMese; }
    public void setIdMese(int idMese) { this.idMese = idMese; }

    public int getGiorno() { return giornoDelMese; }
    public void setGiorno(int giornoDelMese) { this.giornoDelMese = giornoDelMese; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }

    public String getSuffix() { return suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }

    public int getTipoRicorrenzaId() { return idTipo; }
    public void setTipoRicorrenzaId(int idTipo) { this.idTipo = idTipo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ricorrenza that = (Ricorrenza) o;
        return id == that.id &&
                idMese == that.idMese &&
                giornoDelMese == that.giornoDelMese &&
                idTipo == that.idTipo &&
                Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idMese, giornoDelMese, nome, idTipo);
    }

    @Override
    public String toString() {
        return "Ricorrenza{" +
                "id=" + id +
                ", idMese=" + idMese +
                ", giornoDelMese=" + giornoDelMese +
                ", nome='" + nome + '\'' +
                ", idTipo=" + idTipo +
                '}';
    }
}