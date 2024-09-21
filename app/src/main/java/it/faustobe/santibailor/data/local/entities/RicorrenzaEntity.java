package it.faustobe.santibailor.data.local.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "santi")
public class RicorrenzaEntity implements DatabaseEntity, Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id_mesi")
    private int idMese;

    private int giorno;

    @ColumnInfo(name = "santo")
    @NonNull
    private String nome;

    private String bio;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    private String prefix;
    private String suffix;

    @ColumnInfo(name = "tipo_ricorrenza_id", defaultValue = "1")
    private int tipoRicorrenzaId;

    // Costruttore di default
    public RicorrenzaEntity() {}

    // Costruttore completo
    public RicorrenzaEntity(int id, int idMese, int giorno, @NonNull String nome, String bio,
                            String imageUrl, String prefix, String suffix, int tipoRicorrenzaId) {
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

    // Implementazione Parcelable
    protected RicorrenzaEntity(Parcel in) {
        id = in.readInt();
        idMese = in.readInt();
        giorno = in.readInt();
        nome = in.readString();
        bio = in.readString();
        imageUrl = in.readString();
        prefix = in.readString();
        suffix = in.readString();
        tipoRicorrenzaId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idMese);
        dest.writeInt(giorno);
        dest.writeString(nome);
        dest.writeString(bio);
        dest.writeString(imageUrl);
        dest.writeString(prefix);
        dest.writeString(suffix);
        dest.writeInt(tipoRicorrenzaId);
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
    public void setId(int id) { this.id = id; }
    @Override
    public String getTableName() { return "santi"; }

    public int getIdMese() { return idMese; }
    public void setIdMese(int idMese) { this.idMese = idMese; }

    public int getGiorno() { return giorno; }
    public void setGiorno(int giorno) { this.giorno = giorno; }

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

    public int getTipoRicorrenzaId() { return tipoRicorrenzaId; }
    public void setTipoRicorrenzaId(int tipoRicorrenzaId) { this.tipoRicorrenzaId = tipoRicorrenzaId; }
}