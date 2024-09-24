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
    @ColumnInfo(name = "tipo")
    private String tipo;

    public static final int RELIGIOSA = 1;
    public static final int LAICA = 2;

    // Costruttore normale
    public TipoRicorrenzaEntity(int id, @NonNull String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public String toString() {
        return tipo;
    }

    // Getter e Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getTipo() { return tipo; }
    public void setTipo(@NonNull String tipo) { this.tipo = tipo; }

    // Implementazione Parcelable
    protected TipoRicorrenzaEntity(Parcel in) {
        id = in.readInt();
        tipo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(tipo);
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
