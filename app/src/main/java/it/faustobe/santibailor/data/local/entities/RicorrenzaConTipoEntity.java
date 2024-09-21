package it.faustobe.santibailor.data.local.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

public class RicorrenzaConTipoEntity implements Parcelable {
    @Embedded
    public RicorrenzaEntity ricorrenza;


    @Relation(
            parentColumn = "tipo_ricorrenza_id",
            entityColumn = "id"
    )
    public TipoRicorrenzaEntity tipoRicorrenza;

    // Costruttore vuoto per Room
    public RicorrenzaConTipoEntity() {}

    // Costruttore con parametri, ignorato da Room
    @Ignore
    public RicorrenzaConTipoEntity(RicorrenzaEntity ricorrenzaEntity, TipoRicorrenzaEntity tipoRicorrenzaEntity) {
        this.ricorrenza = ricorrenza;
        this.tipoRicorrenza = tipoRicorrenza;
    }

    // Costruttore per Parcelable, ignorato da Room
    @Ignore
    protected RicorrenzaConTipoEntity(Parcel in) {
        ricorrenza = in.readParcelable(RicorrenzaEntity.class.getClassLoader(), RicorrenzaEntity.class);
        tipoRicorrenza = in.readParcelable(TipoRicorrenzaEntity.class.getClassLoader(), TipoRicorrenzaEntity.class);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(ricorrenza, flags);
        dest.writeParcelable(tipoRicorrenza, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RicorrenzaConTipoEntity> CREATOR = new Creator<RicorrenzaConTipoEntity>() {
        @Override
        public RicorrenzaConTipoEntity createFromParcel(Parcel in) {
            return new RicorrenzaConTipoEntity(in);
        }

        @Override
        public RicorrenzaConTipoEntity[] newArray(int size) {
            return new RicorrenzaConTipoEntity[size];
        }
    };

    public RicorrenzaEntity getRicorrenza() {
        return ricorrenza;
    }

    public void setRicorrenza(RicorrenzaEntity ricorrenza) {
        this.ricorrenza = ricorrenza;
    }

    public TipoRicorrenzaEntity getTipoRicorrenza() {
        return tipoRicorrenza;
    }

    public void setTipoRicorrenza(TipoRicorrenzaEntity tipoRicorrenza) {
        this.tipoRicorrenza = tipoRicorrenza;
    }
}