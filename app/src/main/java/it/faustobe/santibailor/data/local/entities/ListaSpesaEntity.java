package it.faustobe.santibailor.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "liste_spesa")
public class ListaSpesaEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "nome")
    private String nome;

    @NonNull
    @ColumnInfo(name = "data_creazione")
    private long dataCreazione; // Timestamp

    @NonNull
    @ColumnInfo(name = "completata")
    private boolean completata;

    @ColumnInfo(name = "colore")
    private String colore;

    public ListaSpesaEntity() {
    }

    @Ignore
    public ListaSpesaEntity(int id, String nome, long dataCreazione, boolean completata, String colore) {
        this.id = id;
        this.nome = nome;
        this.dataCreazione = dataCreazione;
        this.completata = completata;
        this.colore = colore;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public long getDataCreazione() {
        return dataCreazione;
    }

    public boolean isCompletata() {
        return completata;
    }

    public String getColore() {
        return colore;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataCreazione(long dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public void setCompletata(boolean completata) {
        this.completata = completata;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }
}
