package it.faustobe.santibailor.presentation.features.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.ItemRicorrenzaBinding;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.util.ImageHandler;

public class SearchResultsAdapter extends ListAdapter<Ricorrenza, SearchResultsAdapter.ResultViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Ricorrenza ricorrenza);
    }

    private final OnItemClickListener listener;

    public SearchResultsAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRicorrenzaBinding binding = ItemRicorrenzaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ResultViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        private final ItemRicorrenzaBinding binding;

        ResultViewHolder(ItemRicorrenzaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Ricorrenza ricorrenza) {
            binding.nomeRicorrenza.setText(ricorrenza.getNomeCompleto());

            ImageHandler.getInstance(itemView.getContext())
                    .loadImage(ricorrenza.getImageUrl(), binding.imageRicorrenza, R.drawable.placeholder_image);

            itemView.setOnClickListener(v -> listener.onItemClick(ricorrenza));
        }
    }

    private static final DiffUtil.ItemCallback<Ricorrenza> DIFF_CALLBACK = new DiffUtil.ItemCallback<Ricorrenza>() {
        @Override
        public boolean areItemsTheSame(@NonNull Ricorrenza oldItem, @NonNull Ricorrenza newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ricorrenza oldItem, @NonNull Ricorrenza newItem) {
            return oldItem.getNomeCompleto().equals(newItem.getNomeCompleto())
                    && java.util.Objects.equals(oldItem.getImageUrl(), newItem.getImageUrl());
        }
    };
}
