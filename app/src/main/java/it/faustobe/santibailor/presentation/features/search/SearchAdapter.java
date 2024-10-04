package it.faustobe.santibailor.presentation.features.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import it.faustobe.santibailor.databinding.ItemSearchResultBinding;
import it.faustobe.santibailor.domain.model.SearchResult;

public class SearchAdapter extends ListAdapter<SearchResult, SearchAdapter.SearchViewHolder> {

    public SearchAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSearchResultBinding binding = ItemSearchResultBinding.inflate(inflater, parent, false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        private final ItemSearchResultBinding binding;

        SearchViewHolder(ItemSearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SearchResult item) {
            binding.textViewContent.setText(item.getSearchableContent());
            binding.textViewType.setText(item.getType());
        }
    }

    private static final DiffUtil.ItemCallback<SearchResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<SearchResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull SearchResult oldItem, @NonNull SearchResult newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.getType().equals(newItem.getType());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SearchResult oldItem, @NonNull SearchResult newItem) {
            return oldItem.getSearchableContent().equals(newItem.getSearchableContent());
        }
    };
}