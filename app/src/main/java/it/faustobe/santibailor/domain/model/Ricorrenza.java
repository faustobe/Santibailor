package it.faustobe.santibailor.domain.model;

import java.util.Objects;

public class Ricorrenza {
    private int id;
    private int idMese;
    private int giorno;
    private String nome;
    private String bio;
    private String imageUrl;
    private String prefix;
    private String suffix;
    private int tipoRicorrenzaId;

    public Ricorrenza(int id, int idMese, int giorno, String nome, String bio, String imageUrl, String prefix, String suffix, int tipoRicorrenzaId) {
        this.id = id;
        this.idMese = idMese;
        this.giorno = giorno;
        this.nome = nome;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.prefix = prefix;
        this.suffix = suffix;
        this.tipoRicorrenzaId = tipoRicorrenzaId;
    }

    // Getters and setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdMese() { return idMese; }
    public void setIdMese(int idMese) { this.idMese = idMese; }

    public int getGiorno() { return giorno; }
    public void setGiorno(int giorno) { this.giorno = giorno; }

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

    public int getTipoRicorrenzaId() { return tipoRicorrenzaId; }
    public void setTipoRicorrenzaId(int tipoRicorrenzaId) { this.tipoRicorrenzaId = tipoRicorrenzaId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ricorrenza that = (Ricorrenza) o;
        return id == that.id &&
                idMese == that.idMese &&
                giorno == that.giorno &&
                tipoRicorrenzaId == that.tipoRicorrenzaId &&
                Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idMese, giorno, nome, tipoRicorrenzaId);
    }

    @Override
    public String toString() {
        return "Ricorrenza{" +
                "id=" + id +
                ", idMese=" + idMese +
                ", giorno=" + giorno +
                ", nome='" + nome + '\'' +
                ", tipoRicorrenzaId=" + tipoRicorrenzaId +
                '}';
    }
}