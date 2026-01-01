package it.faustobe.santibailor.presentation.features.riepilogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.domain.model.Impegno;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.presentation.features.impegni.ImpegniViewModel;

@AndroidEntryPoint
public class RiepilogoFragment extends Fragment {
    private ImpegniViewModel impegniViewModel;
    private RicorrenzaViewModel ricorrenzaViewModel;

    private TextView tvPeriodoTitle;
    private TextView tvImpegniTotali;
    private TextView tvImpegniCompletati;
    private TextView tvRicorrenzeTotali;
    private RecyclerView recyclerView;
    private TextView tvNoData;
    private MaterialButtonToggleGroup togglePeriodo;
    private ImageButton btnBack;

    private RiepilogoAdapter adapter;
    private boolean isSettimana = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_riepilogo, container, false);

        tvPeriodoTitle = view.findViewById(R.id.tv_periodo_title);
        tvImpegniTotali = view.findViewById(R.id.tv_impegni_totali);
        tvImpegniCompletati = view.findViewById(R.id.tv_impegni_completati);
        tvRicorrenzeTotali = view.findViewById(R.id.tv_ricorrenze_totali);
        recyclerView = view.findViewById(R.id.recycler_view_riepilogo);
        tvNoData = view.findViewById(R.id.tv_no_data);
        togglePeriodo = view.findViewById(R.id.toggle_periodo);
        btnBack = view.findViewById(R.id.btn_back);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        impegniViewModel = new ViewModelProvider(this).get(ImpegniViewModel.class);
        ricorrenzaViewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);

        adapter = new RiepilogoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        togglePeriodo.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_settimana) {
                    isSettimana = true;
                    loadSettimana();
                } else if (checkedId == R.id.btn_mese) {
                    isSettimana = false;
                    loadMese();
                }
            }
        });

        // Carica dati iniziali (settimana)
        loadSettimana();
    }

    private void loadSettimana() {
        tvPeriodoTitle.setText("Questa settimana");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();

        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = cal.getTime();

        loadData(startOfWeek, endOfWeek);
    }

    private void loadMese() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.ITALIAN);
        tvPeriodoTitle.setText(sdf.format(cal.getTime()));

        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfMonth = cal.getTime();

        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date endOfMonth = cal.getTime();

        loadData(startOfMonth, endOfMonth);
    }

    private void loadData(Date start, Date end) {
        List<RiepilogoItem> items = new ArrayList<>();

        // Carica impegni futuri (semplificato - mostra tutti i futuri)
        int limit = isSettimana ? 20 : 50;
        impegniViewModel.getImpegniFuturi(limit).observe(getViewLifecycleOwner(), impegni -> {
            items.clear();

            if (impegni != null) {
                int totali = 0;
                int completati = 0;

                for (Impegno imp : impegni) {
                    // Filtra per range di date
                    long dataOra = imp.getDataOra();
                    if (dataOra >= start.getTime() && dataOra <= end.getTime()) {
                        totali++;
                        if (imp.isCompletato()) {
                            completati++;
                        }
                        items.add(new RiepilogoItem(imp));
                    }
                }

                tvImpegniTotali.setText(String.valueOf(totali));
                tvImpegniCompletati.setText(String.valueOf(completati));
            }

            // Carica ricorrenze del giorno (semplificato)
            loadRicorrenzeDelGiorno(items);
        });
    }

    private void loadRicorrenzeDelGiorno(List<RiepilogoItem> items) {
        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), ricorrenze -> {
            if (ricorrenze != null) {
                tvRicorrenzeTotali.setText(String.valueOf(ricorrenze.size()));

                for (Ricorrenza ric : ricorrenze) {
                    items.add(new RiepilogoItem(ric));
                }
            }

            // Aggiorna adapter
            if (items.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                adapter.setItems(items);
            }
        });
    }
}
