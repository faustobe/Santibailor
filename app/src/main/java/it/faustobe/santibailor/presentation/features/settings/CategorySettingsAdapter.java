package it.faustobe.santibailor.presentation.features.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.faustobe.santibailor.R;

public class CategorySettingsAdapter extends RecyclerView.Adapter<CategorySettingsAdapter.ViewHolder> {
    private final List<SettingItem> items;
    private final OnSettingItemClickListener listener;

    public interface OnSettingItemClickListener {
        void onSettingItemClick(SettingItem item);
    }

    public CategorySettingsAdapter(List<SettingItem> items, OnSettingItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView settingTitle;
        private final TextView settingDescription;
        private final Switch settingToggle;

        ViewHolder(View itemView) {
            super(itemView);
            settingTitle = itemView.findViewById(R.id.settingTitle);
            settingDescription = itemView.findViewById(R.id.settingDescription);
            settingToggle = itemView.findViewById(R.id.settingToggle);
        }

        void bind(final SettingItem item) {
            settingTitle.setText(item.getTitle());
            settingDescription.setText(item.getDescription());

            switch (item.getType()) {
                case TOGGLE:
                    settingToggle.setVisibility(View.VISIBLE);
                    settingToggle.setChecked(item.isToggleState());
                    settingToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        item.setToggleState(isChecked);
                        listener.onSettingItemClick(item);
                    });
                    break;
                case NAVIGATION:
                case ACTION:
                    settingToggle.setVisibility(View.GONE);
                    settingToggle.setOnCheckedChangeListener(null);
                    break;
            }

            itemView.setOnClickListener(v -> {
                if (item.getType() != SettingItem.SettingType.TOGGLE) {
                    listener.onSettingItemClick(item);
                }
            });
        }
    }
}
