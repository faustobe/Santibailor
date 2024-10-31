package it.faustobe.santibailor.presentation.common.ricorrenze;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;

public class RicorrenzaAdapter extends RecyclerView.Adapter<RicorrenzaAdapter.SaintViewHolder> {
    private List<Ricorrenza> ricorrenze = new ArrayList<>();
    private final OnItemClickListener listener;
    private final RicorrenzaViewModel viewModel;
    private boolean isCollapsedView = false;

    public interface OnItemClickListener {
        void onItemClick(int ricorrenzaId);
    }

    public RicorrenzaAdapter(OnItemClickListener listener, RicorrenzaViewModel viewModel) {
        this.listener = listener;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ricorrenza, parent, false);
        return new SaintViewHolder(view, listener, viewModel);
    }

    public void setCollapsedView(boolean isCollapsed) {
        if (this.isCollapsedView != isCollapsed) {
            this.isCollapsedView = isCollapsed;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SaintViewHolder holder, int position) {
        Ricorrenza ricorrenza = ricorrenze.get(position);
        holder.bind(ricorrenza);
    }

    @Override
    public int getItemCount() {
        return ricorrenze.size();
    }

    public void setRicorrenze(List<Ricorrenza> ricorrenze) {
        this.ricorrenze = ricorrenze;
        notifyDataSetChanged();
    }

    public List<Ricorrenza> getRicorrenze() {
        return new ArrayList<>(ricorrenze);
    }

    public void addRicorrenze(List<Ricorrenza> nuoveRicorrenze) {
        int startPos = ricorrenze.size();
        ricorrenze.addAll(nuoveRicorrenze);
        notifyItemRangeInserted(startPos, nuoveRicorrenze.size());
    }

    static class SaintViewHolder extends RecyclerView.ViewHolder {
        final TextView nomeTextView;
        final ImageView imageView;
        final OnItemClickListener listener;
        final RicorrenzaViewModel viewModel;

        SaintViewHolder(@NonNull View itemView, OnItemClickListener listener, RicorrenzaViewModel viewModel) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.nome_ricorrenza);
            imageView = itemView.findViewById(R.id.image_ricorrenza);
            this.listener = listener;
            this.viewModel = viewModel;
        }

        void bind(Ricorrenza ricorrenza) {
            nomeTextView.setText(ricorrenza.getNome());

            itemView.setOnClickListener(v -> listener.onItemClick(ricorrenza.getId()));

            viewModel.loadImage(ricorrenza.getImageUrl(), imageView, R.drawable.placeholder_image);
        }
    }
}