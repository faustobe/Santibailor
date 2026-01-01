package it.faustobe.santibailor.presentation.features.listespesa;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.domain.model.ItemSpesa;

public class ItemSpesaAdapter extends RecyclerView.Adapter<ItemSpesaAdapter.ViewHolder> {
    private List<ItemSpesa> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemToggle(ItemSpesa item);
        void onItemDelete(ItemSpesa item);
    }

    public ItemSpesaAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<ItemSpesa> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prodotto_spesa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemSpesa item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkboxCompletato;
        private final TextView tvNomeItem;
        private final TextView tvQuantita;
        private final TextView tvCategoria;
        private final TextView tvNote;
        private final ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxCompletato = itemView.findViewById(R.id.checkbox_completato);
            tvNomeItem = itemView.findViewById(R.id.tv_nome_item);
            tvQuantita = itemView.findViewById(R.id.tv_quantita);
            tvCategoria = itemView.findViewById(R.id.tv_categoria);
            tvNote = itemView.findViewById(R.id.tv_note);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(ItemSpesa item, OnItemClickListener listener) {
            // Nome
            tvNomeItem.setText(item.getNome());

            // Checkbox
            checkboxCompletato.setChecked(item.isCompletato());

            // Stile barrato se completato
            if (item.isCompletato()) {
                tvNomeItem.setPaintFlags(tvNomeItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvNomeItem.setAlpha(0.6f);
            } else {
                tvNomeItem.setPaintFlags(tvNomeItem.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                tvNomeItem.setAlpha(1.0f);
            }

            // Quantità
            if (item.getQuantita() != null && !item.getQuantita().isEmpty()) {
                tvQuantita.setText(item.getQuantita());
                tvQuantita.setVisibility(View.VISIBLE);
            } else {
                tvQuantita.setVisibility(View.GONE);
            }

            // Categoria
            if (item.getCategoria() != null && !item.getCategoria().isEmpty()) {
                tvCategoria.setText(item.getCategoria());
                tvCategoria.setVisibility(View.VISIBLE);
            } else {
                tvCategoria.setVisibility(View.GONE);
            }

            // Note
            if (item.getNote() != null && !item.getNote().isEmpty()) {
                tvNote.setText(item.getNote());
                tvNote.setVisibility(View.VISIBLE);
            } else {
                tvNote.setVisibility(View.GONE);
            }

            // Checkbox listener
            checkboxCompletato.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null && buttonView.isPressed()) {
                    listener.onItemToggle(item);
                }
            });

            // Delete listener
            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemDelete(item);
                }
            });
        }
    }
}
