package it.faustobe.santibailor.domain.model;

import java.util.Objects;

public class RicorrenzaConTipo {
    private Ricorrenza ricorrenza;
    private TipoRicorrenza tipoRicorrenza;

    public RicorrenzaConTipo(Ricorrenza ricorrenza, TipoRicorrenza tipoRicorrenza) {
        setRicorrenza(ricorrenza);
        setTipoRicorrenza(tipoRicorrenza);
    }

    public Ricorrenza getRicorrenza() { return ricorrenza; }
    public void setRicorrenza(Ricorrenza ricorrenza) {
        if (ricorrenza == null) throw new IllegalArgumentException("Ricorrenza non può essere null");
        this.ricorrenza = ricorrenza;
    }

    public TipoRicorrenza getTipoRicorrenza() { return tipoRicorrenza; }
    public void setTipoRicorrenza(TipoRicorrenza tipoRicorrenza) {
        if (tipoRicorrenza == null) throw new IllegalArgumentException("TipoRicorrenza non può essere null");
        this.tipoRicorrenza = tipoRicorrenza;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RicorrenzaConTipo that = (RicorrenzaConTipo) o;
        return Objects.equals(ricorrenza, that.ricorrenza) &&
                Objects.equals(tipoRicorrenza, that.tipoRicorrenza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ricorrenza, tipoRicorrenza);
    }

    @Override
    public String toString() {
        return "RicorrenzaConTipo{" +
                "ricorrenza=" + ricorrenza +
                ", tipoRicorrenza=" + tipoRicorrenza +
                '}';
    }
}