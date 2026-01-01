package it.faustobe.santibailor.presentation.features.riepilogo;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.faustobe.santibailor.R;

public class RiepilogoAdapter extends RecyclerView.Adapter<RiepilogoAdapter.ViewHolder> {
    private List<RiepilogoItem> items = new ArrayList<>();

    public void setItems(List<RiepilogoItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RiepilogoItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text1;
        private final TextView text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }

        public void bind(RiepilogoItem item) {
            text1.setText(item.getTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM, HH:mm", Locale.ITALIAN);
            long timestamp = item.getDateTimestamp();
            String dateStr = timestamp > 0 ? sdf.format(new java.util.Date(timestamp)) : "N/A";

            String typeStr = item.getType() == RiepilogoItem.Type.IMPEGNO ? "Impegno" : "Ricorrenza";
            text2.setText(typeStr + " - " + dateStr);

            // Stile barrato se completato
            if (item.isCompleted()) {
                text1.setPaintFlags(text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                text1.setTextColor(0xFF999999);
            } else {
                text1.setPaintFlags(text1.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                text1.setTextColor(0xFF000000);
            }
        }
    }
}
