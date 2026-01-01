package it.faustobe.santibailor.presentation.features.impegni;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.ItemImpegnoBinding;
import it.faustobe.santibailor.domain.model.Impegno;
import it.faustobe.santibailor.domain.model.Priorita;
import it.faustobe.santibailor.util.ImageHandler;

public class ImpegniAdapter extends RecyclerView.Adapter<ImpegniAdapter.ImpegnoViewHolder> {

    private List<Impegno> impegni = new ArrayList<>();
    private final OnImpegnoClickListener clickListener;
    private final OnImpegnoCompletedListener completedListener;
    private final ImageHandler imageHandler;

    public interface OnImpegnoClickListener {
        void onImpegnoClick(Impegno impegno);
    }

    public interface OnImpegnoCompletedListener {
        void onImpegnoCompleted(Impegno impegno, boolean isCompleted);
    }

    public ImpegniAdapter(OnImpegnoClickListener clickListener,
                          OnImpegnoCompletedListener completedListener,
                          ImageHandler imageHandler) {
        this.clickListener = clickListener;
        this.completedListener = completedListener;
        this.imageHandler = imageHandler;
    }

    @NonNull
    @Override
    public ImpegnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImpegnoBinding binding = ItemImpegnoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ImpegnoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImpegnoViewHolder holder, int position) {
        Impegno impegno = impegni.get(position);
        holder.bind(impegno);
    }

    @Override
    public int getItemCount() {
        return impegni.size();
    }

    public void setImpegni(List<Impegno> impegni) {
        this.impegni = impegni != null ? impegni : new ArrayList<>();
        notifyDataSetChanged();
    }

    public class ImpegnoViewHolder extends RecyclerView.ViewHolder {
        private final ItemImpegnoBinding binding;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN);

        public ImpegnoViewHolder(@NonNull ItemImpegnoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Impegno impegno) {
            // Titolo
            binding.tvTitolo.setText(impegno.getTitolo());

            // Data/Ora
            String dataOraText = dateFormat.format(new Date(impegno.getDataOra()));
            binding.tvDataOra.setText(dataOraText);

            // Priorità - barra colorata laterale
            int colorRes = Priorita.getColorResource(impegno.getPriorita());
            int color = ContextCompat.getColor(binding.getRoot().getContext(), colorRes);
            binding.priorityIndicator.setBackgroundColor(color);

            // Chip Priorità
            String prioritaDisplay = Priorita.getDisplayName(impegno.getPriorita());
            binding.chipPriorita.setText(prioritaDisplay);
            binding.chipPriorita.setChipBackgroundColorResource(colorRes);

            // Chip Categoria
            if (impegno.getCategoria() != null && !impegno.getCategoria().isEmpty()) {
                binding.chipCategoria.setText(impegno.getCategoria());
                binding.chipCategoria.setVisibility(View.VISIBLE);
            } else {
                binding.chipCategoria.setVisibility(View.GONE);
            }

            // Checkbox completato
            binding.cbCompletato.setOnCheckedChangeListener(null); // Rimuovi listener temporaneamente
            binding.cbCompletato.setChecked(impegno.isCompletato());

            // Stile barrato se completato
            if (impegno.isCompletato()) {
                binding.tvTitolo.setPaintFlags(binding.tvTitolo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding.tvTitolo.setAlpha(0.6f);
            } else {
                binding.tvTitolo.setPaintFlags(binding.tvTitolo.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                binding.tvTitolo.setAlpha(1.0f);
            }

            binding.cbCompletato.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (completedListener != null) {
                    completedListener.onImpegnoCompleted(impegno, isChecked);
                }
            });

            // Immagine (se presente)
            if (impegno.getImageUrl() != null && !impegno.getImageUrl().isEmpty()) {
                binding.ivImpegno.setVisibility(View.VISIBLE);
                imageHandler.loadImage(impegno.getImageUrl(), binding.ivImpegno, R.drawable.placeholder_image);
            } else {
                binding.ivImpegno.setVisibility(View.GONE);
            }

            // Click listener
            binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onImpegnoClick(impegno);
                }
            });
        }
    }
}
