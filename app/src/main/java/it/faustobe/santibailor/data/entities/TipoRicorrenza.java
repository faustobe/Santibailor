package it.faustobe.santibailor.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tipo_ricorrenza")
public class TipoRicorrenza {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "nome")
    private String nome = "";

    public static final int RELIGIOSA = 1;
    public static final int LAICA = 2;

    // Getter e Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getNome() { return nome; }
    public void setNome(@NonNull String nome) { this.nome = nome; }
}
