package it.faustobe.santibailor.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.data.entities.RicorrenzaConTipo;
import it.faustobe.santibailor.util.ImageLoadingUtil;

public class RicorrenzaAdapter extends RecyclerView.Adapter<RicorrenzaAdapter.RicorrenzaViewHolder> {
    private List<RicorrenzaConTipo> ricorrenze;

    public RicorrenzaAdapter(List<RicorrenzaConTipo> ricorrenze) {
        this.ricorrenze = ricorrenze;
    }

    @NonNull
    @Override
    public RicorrenzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ricorrenza, parent, false);
        return new RicorrenzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RicorrenzaViewHolder holder, int position) {
        RicorrenzaConTipo ricorrenzaConTipo = ricorrenze.get(position);
        holder.nomeTextView.setText(ricorrenzaConTipo.ricorrenza.getNome());
        holder.tipoTextView.setText(ricorrenzaConTipo.tipoRicorrenza.getNome());

        String imageUrl = ricorrenzaConTipo.ricorrenza.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            ImageLoadingUtil.loadImage(holder.imageView, imageUrl);
        } else {
            // Carica un'immagine di default se non c'è un'immagine specifica
            ImageLoadingUtil.loadImage(holder.imageView, R.drawable.default_ricorrenza_image);
        }
    }

    @Override
    public int getItemCount() {
        return ricorrenze != null ? ricorrenze.size() : 0;
    }

    public void setRicorrenze(List<RicorrenzaConTipo> ricorrenze) {
        this.ricorrenze = ricorrenze;
        notifyDataSetChanged();
    }

    static class RicorrenzaViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView;
        TextView tipoTextView;
        ImageView imageView;

        RicorrenzaViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.nome_ricorrenza);
            tipoTextView = itemView.findViewById(R.id.tipo_ricorrenza);
            imageView = itemView.findViewById(R.id.immagine_ricorrenza);
        }
    }
}