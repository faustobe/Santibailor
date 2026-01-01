package it.faustobe.santibailor.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "item_spesa",
    foreignKeys = @ForeignKey(
        entity = ListaSpesaEntity.class,
        parentColumns = "id",
        childColumns = "id_lista",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("id_lista")}
)
public class ItemSpesaEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "id_lista")
    private int idLista;

    @NonNull
    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "quantita")
    private String quantita;

    @NonNull
    @ColumnInfo(name = "completato")
    private boolean completato;

    @ColumnInfo(name = "categoria")
    private String categoria;

    @ColumnInfo(name = "note")
    private String note;

    @NonNull
    @ColumnInfo(name = "ordine")
    private int ordine;

    public ItemSpesaEntity() {
    }

    @Ignore
    public ItemSpesaEntity(int id, int idLista, String nome, String quantita, boolean completato,
                           String categoria, String note, int ordine) {
        this.id = id;
        this.idLista = idLista;
        this.nome = nome;
        this.quantita = quantita;
        this.completato = completato;
        this.categoria = categoria;
        this.note = note;
        this.ordine = ordine;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getIdLista() {
        return idLista;
    }

    public String getNome() {
        return nome;
    }

    public String getQuantita() {
        return quantita;
    }

    public boolean isCompletato() {
        return completato;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNote() {
        return note;
    }

    public int getOrdine() {
        return ordine;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantita(String quantita) {
        this.quantita = quantita;
    }

    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }
}
