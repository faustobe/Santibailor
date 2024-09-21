package it.faustobe.santibailor.util;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

import it.faustobe.santibailor.domain.model.RicorrenzaConTipo;

public class RicorrenzaDiffCallback extends DiffUtil.Callback {
    private final List<RicorrenzaConTipo> oldList;
    private final List<RicorrenzaConTipo> newList;

    public RicorrenzaDiffCallback(List<RicorrenzaConTipo> oldList, List<RicorrenzaConTipo> newList) {
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
        RicorrenzaConTipo oldItem = oldList.get(oldItemPosition);
        RicorrenzaConTipo newItem = newList.get(newItemPosition);
        return oldItem != null && newItem != null && oldItem.getRicorrenza().getId() == newItem.getRicorrenza().getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        RicorrenzaConTipo oldItem = oldList.get(oldItemPosition);
        RicorrenzaConTipo newItem = newList.get(newItemPosition);

        if (oldItem == null || newItem == null) {
            return false;
        }

        boolean sameNome = Objects.equals(oldItem.getRicorrenza().getNome(), newItem.getRicorrenza().getNome());
        boolean sameGiorno = oldItem.getRicorrenza().getGiorno() == newItem.getRicorrenza().getGiorno();
        boolean sameMese = oldItem.getRicorrenza().getIdMese() == newItem.getRicorrenza().getIdMese();
        boolean sameTipo = Objects.equals(oldItem.getTipoRicorrenza().getNome(), newItem.getTipoRicorrenza().getNome());

        return sameNome && sameGiorno && sameMese && sameTipo;
    }
}