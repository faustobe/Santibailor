package it.faustobe.santibailor.data.local.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tipo_ricorrenza")
public class TipoRicorrenzaEntity implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "nome")
    private String nome;

    public static final int RELIGIOSA = 1;
    public static final int LAICA = 2;

    // Costruttore normale
    public TipoRicorrenzaEntity(int id, @NonNull String nome) {
        this.id = id;
        this.nome = nome;
    }

    @NonNull
    @Override
    public String toString() {
        return nome;
    }

    // Getter e Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getNome() { return nome; }
    public void setNome(@NonNull String nome) { this.nome = nome; }

    // Implementazione Parcelable
    protected TipoRicorrenzaEntity(Parcel in) {
        id = in.readInt();
        nome = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TipoRicorrenzaEntity> CREATOR = new Creator<TipoRicorrenzaEntity>() {
        @Override
        public TipoRicorrenzaEntity createFromParcel(Parcel in) {
            return new TipoRicorrenzaEntity(in);
        }

        @Override
        public TipoRicorrenzaEntity[] newArray(int size) {
            return new TipoRicorrenzaEntity[size];
        }
    };
}
