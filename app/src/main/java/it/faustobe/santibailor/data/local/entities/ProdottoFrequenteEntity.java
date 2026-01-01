package it.faustobe.santibailor.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prodotti_frequenti")
public class ProdottoFrequenteEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "categoria")
    private String categoria;

    @NonNull
    @ColumnInfo(name = "frequenza_utilizzo")
    private int frequenzaUtilizzo;

    @NonNull
    @ColumnInfo(name = "ultima_data_utilizzo")
    private long ultimaDataUtilizzo;

    public ProdottoFrequenteEntity() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getFrequenzaUtilizzo() {
        return frequenzaUtilizzo;
    }

    public long getUltimaDataUtilizzo() {
        return ultimaDataUtilizzo;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setFrequenzaUtilizzo(int frequenzaUtilizzo) {
        this.frequenzaUtilizzo = frequenzaUtilizzo;
    }

    public void setUltimaDataUtilizzo(long ultimaDataUtilizzo) {
        this.ultimaDataUtilizzo = ultimaDataUtilizzo;
    }
}
