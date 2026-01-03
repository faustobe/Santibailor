package it.faustobe.santibailor.presentation.features.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.domain.model.OverviewItem;

/**
 * Adapter per la lista unificata di impegni, liste spesa e note
 */
public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(OverviewItem item);
    }

    private List<OverviewItem> items = new ArrayList<>();
    private OnItemClickListener listener;

    public OverviewAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_overview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OverviewItem item = items.get(position);
        holder.tvIcon.setText(item.getIcon());
        holder.tvText.setText(item.getText());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<OverviewItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon;
        TextView tvText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcon = itemView.findViewById(R.id.tv_icon);
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }
}
