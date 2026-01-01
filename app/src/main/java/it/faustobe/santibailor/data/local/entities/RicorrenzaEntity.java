package it.faustobe.santibailor.data.local.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import it.faustobe.santibailor.domain.model.Searchable;

@Entity(tableName = "santi",
        indices = {
                @Index(value = {"giorno_del_mese", "id_mese"}, name = "idx_santi_giorno_mese"),
                @Index(value = {"id_tipo"}, name = "idx_santi_tipo"),
                @Index(value = {"giorno_del_mese", "id_mese", "id_tipo"}, name = "idx_santi_giorno_mese_tipo")
        })
public class RicorrenzaEntity implements DatabaseEntity, Parcelable, Searchable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id_mese")
    private int idMese;

    @ColumnInfo(name = "giorno_del_mese")
    private int giornoDelMese;

    @ColumnInfo(name = "santo")
    @NonNull
    private String nome;

    private String bio;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    private String prefix;
    private String suffix;

    @ColumnInfo(name = "id_tipo", defaultValue = "1")
    private int idTipo;

    // Costruttore di default
    public RicorrenzaEntity() {}

    // Costruttore completo
    @Ignore
    public RicorrenzaEntity(int id, int idMese, int giornoDelMese, @NonNull String nome, String bio,
                            String imageUrl, String prefix, String suffix, int idTipo) {
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

    // Implementazione Parcelable
    protected RicorrenzaEntity(Parcel in) {
        id = in.readInt();
        idMese = in.readInt();
        giornoDelMese = in.readInt();
        nome = in.readString();
        bio = in.readString();
        imageUrl = in.readString();
        prefix = in.readString();
        suffix = in.readString();
        idTipo = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idMese);
        dest.writeInt(giornoDelMese);
        dest.writeString(nome);
        dest.writeString(bio);
        dest.writeString(imageUrl);
        dest.writeString(prefix);
        dest.writeString(suffix);
        dest.writeInt(idTipo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RicorrenzaEntity> CREATOR = new Creator<RicorrenzaEntity>() {
        @Override
        public RicorrenzaEntity createFromParcel(Parcel in) {
            return new RicorrenzaEntity(in);
        }

        @Override
        public RicorrenzaEntity[] newArray(int size) {
            return new RicorrenzaEntity[size];
        }
    };

    // Getter e Setter
    @Override
    public int getId() { return id; }

    @Override
    public String getSearchableContent() {
        return nome + " " + bio; // Combiniamo nome e bio per la ricerca
    }

    @Override
    public String getType() {//get tipo di contenuto per la ricerca, NON il tipo di Ricorrenza
        return "Ricorrenza";
    }

    @Override
    public void setId(int id) { this.id = id; }

    @Override
    public String getTableName() { return "santi"; }

    public int getIdMese() { return idMese; }

    public void setIdMese(int idMese) { this.idMese = idMese; }

    public int getGiornoDelMese() { return giornoDelMese; }

    public void setGiornoDelMese(int giornoDelMese) { this.giornoDelMese = giornoDelMese; }

    @NonNull
    public String getNome() { return nome; }

    public void setNome(@NonNull String nome) { this.nome = nome; }

    public String getBio() { return bio; }

    public void setBio(String bio) { this.bio = bio; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getPrefix() { return prefix; }

    public void setPrefix(String prefix) { this.prefix = prefix; }

    public String getSuffix() { return suffix; }

    public void setSuffix(String suffix) { this.suffix = suffix; }

    public int getIdTipo() { return idTipo; }

    public void setIdTipo(int idTipo) { this.idTipo = idTipo; }

}