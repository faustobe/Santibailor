package it.faustobe.santibailor.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Nullable;

@Entity(tableName = "santi")
public class Ricorrenza {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "id_mesi")
    private int idMese;

    private int giorno;

    @ColumnInfo(name = "santo")
    @NonNull
    private String nome;

    private String bio;
    private String img;
    private String prefix;
    private String suffix;

    @ColumnInfo(name = "tipo_ricorrenza_id", defaultValue = "1")
    private int tipoRicorrenzaId;

    // Getter e Setter
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

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public String getImageUrl() { return img; }
    public void setImageUrl(String imageUrl) { this.img = imageUrl; }

    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }

    public String getSuffix() { return suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }

    public int getTipoRicorrenzaId() { return tipoRicorrenzaId; }
    public void setTipoRicorrenzaId(int tipoRicorrenzaId) { this.tipoRicorrenzaId = tipoRicorrenzaId; }
}