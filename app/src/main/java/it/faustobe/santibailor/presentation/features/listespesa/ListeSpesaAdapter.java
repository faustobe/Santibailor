package it.faustobe.santibailor.presentation.features.listespesa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.domain.model.ListaSpesa;

public class ListeSpesaAdapter extends RecyclerView.Adapter<ListeSpesaAdapter.ViewHolder> {
    private List<ListaSpesa> liste = new ArrayList<>();
    private final OnListaClickListener listener;

    public interface OnListaClickListener {
        void onListaClick(ListaSpesa lista);
        void onDeleteClick(ListaSpesa lista);
    }

    public ListeSpesaAdapter(OnListaClickListener listener) {
        this.listener = listener;
    }

    public void setListe(List<ListaSpesa> liste) {
        this.liste = liste != null ? liste : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_spesa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListaSpesa lista = liste.get(position);
        holder.bind(lista, listener);
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomeLista;
        private final ImageButton btnMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeLista = itemView.findViewById(R.id.tv_nome_lista);
            btnMenu = itemView.findViewById(R.id.btn_menu);
        }

        public void bind(ListaSpesa lista, OnListaClickListener listener) {
            // Solo nome
            tvNomeLista.setText(lista.getNome());

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onListaClick(lista);
                }
            });

            btnMenu.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(lista);
                }
            });
        }
    }
}
