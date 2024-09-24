package it.faustobe.santibailor.presentation.common.ricorrenze;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.util.DateUtils;

public class RicorrenzaAdapter extends RecyclerView.Adapter<RicorrenzaAdapter.SaintViewHolder> {
    private List<Ricorrenza> ricorrenze = new ArrayList<>();
    private boolean isCollapsedView = false;
    private final OnItemClickListener listener;
    private final OnDeleteClickListener deleteListener;
    private final RicorrenzaViewModel viewModel;

    public interface OnItemClickListener {
        void onItemClick(int ricorrenzaId);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Ricorrenza ricorrenza);
    }

    public RicorrenzaAdapter(OnItemClickListener listener, OnDeleteClickListener deleteListener, RicorrenzaViewModel viewModel) {
        this.listener = listener;
        this.deleteListener = deleteListener;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ricorrenza, parent, false);
        return new SaintViewHolder(view, listener, deleteListener, viewModel);
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

    public void setCollapsedView(boolean isCollapsed) {
        if (this.isCollapsedView != isCollapsed) {
            this.isCollapsedView = isCollapsed;
            notifyDataSetChanged();
        }
    }

    static class SaintViewHolder extends RecyclerView.ViewHolder {
        final TextView nomeTextView;
        final TextView tipoTextView;
        final ImageButton deleteButton;
        final ImageView imageView;
        final OnItemClickListener listener;
        final OnDeleteClickListener deleteListener;
        final RicorrenzaViewModel viewModel;

        SaintViewHolder(@NonNull View itemView, OnItemClickListener listener, OnDeleteClickListener deleteListener, RicorrenzaViewModel viewModel) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.nome_ricorrenza);
            tipoTextView = itemView.findViewById(R.id.tipo_ricorrenza);
            deleteButton = itemView.findViewById(R.id.delete_button);
            imageView = itemView.findViewById(R.id.image_ricorrenza);
            this.listener = listener;
            this.deleteListener = deleteListener;
            this.viewModel = viewModel;
        }

        void bind(Ricorrenza ricorrenza) {
            String displayDate = DateUtils.formatDate(ricorrenza.getGiorno(), ricorrenza.getIdMese());

            nomeTextView.setText(String.format("%s %s (%s)",
                    ricorrenza.getPrefix(),
                    ricorrenza.getNome(),
                    displayDate));

            tipoTextView.setText(ricorrenza.getNome());

            itemView.setOnClickListener(v -> listener.onItemClick(ricorrenza.getId()));

            viewModel.loadImage(ricorrenza.getImageUrl(), imageView, R.drawable.placeholder_image);

            if (deleteButton != null) {
                deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(ricorrenza));
            }
        }
    }
}