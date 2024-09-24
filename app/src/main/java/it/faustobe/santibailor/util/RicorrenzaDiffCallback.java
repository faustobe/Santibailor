package it.faustobe.santibailor.util;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

import it.faustobe.santibailor.domain.model.Ricorrenza;

public class RicorrenzaDiffCallback extends DiffUtil.Callback {
    private final List<Ricorrenza> oldList;
    private final List<Ricorrenza> newList;

    public RicorrenzaDiffCallback(List<Ricorrenza> oldList, List<Ricorrenza> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Ricorrenza oldItem = oldList.get(oldItemPosition);
        Ricorrenza newItem = newList.get(newItemPosition);
        return oldItem != null && newItem != null && oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Ricorrenza oldItem = oldList.get(oldItemPosition);
        Ricorrenza newItem = newList.get(newItemPosition);

        if (oldItem == null || newItem == null) {
            return false;
        }

        boolean sameNome = Objects.equals(oldItem.getNome(), newItem.getNome());
        boolean sameGiorno = oldItem.getGiorno() == newItem.getGiorno();
        boolean sameMese = oldItem.getIdMese() == newItem.getIdMese();
        boolean sameTipo = Objects.equals(oldItem.getTipoRicorrenzaId(), newItem.getTipoRicorrenzaId());

        return sameNome && sameGiorno && sameMese && sameTipo;
    }
}