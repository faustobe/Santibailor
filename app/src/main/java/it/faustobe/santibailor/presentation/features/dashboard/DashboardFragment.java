package it.faustobe.santibailor.presentation.features.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.databinding.FragmentDashboardBinding;
import it.faustobe.santibailor.util.NotificationHelper;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inizializza ViewModel con Hilt
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Osserva i dati e aggiorna la UI
        observeData();

        // Setup click listeners
        setupClickListeners();

        return root;
    }

    private void setupClickListeners() {
        // Pulsante "Vedi tutti impegni"
        binding.btnVediImpegni.setOnClickListener(v -> {
            // Navigate to ImpegniFragment
            androidx.navigation.Navigation.findNavController(v).navigate(
                it.faustobe.santibailor.R.id.action_global_to_impegni
            );
        });
    }

    private void observeData() {
        // Totale ricorrenze
        dashboardViewModel.getTotalRicorrenze().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                binding.tvTotalRicorrenze.setText(String.valueOf(total));
            }
        });

        // Ricorrenze di oggi
        dashboardViewModel.getTodayRicorrenze().observe(getViewLifecycleOwner(), today -> {
            if (today != null) {
                binding.tvTodayRicorrenze.setText(String.valueOf(today));
            }
        });

        // Ricorrenze religiose
        dashboardViewModel.getReligioseCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.tvReligioseCount.setText(String.valueOf(count));
            }
        });

        // Ricorrenze laiche
        dashboardViewModel.getLaicheCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.tvLaicheCount.setText(String.valueOf(count));
            }
        });

        // Info database
        dashboardViewModel.getDatabaseInfo().observe(getViewLifecycleOwner(), info -> {
            if (info != null) {
                binding.tvDatabaseInfo.setText(info);
            }
        });

        // Stato notifiche
        checkNotificationStatus();

        // === STATISTICHE IMPEGNI ===
        // Totale impegni
        dashboardViewModel.getTotalImpegni().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                binding.tvTotalImpegni.setText(String.valueOf(total));
            }
        });

        // Impegni oggi
        dashboardViewModel.getImpegniOggi().observe(getViewLifecycleOwner(), oggi -> {
            if (oggi != null) {
                binding.tvImpegniOggi.setText(String.valueOf(oggi));
            }
        });

        // Impegni futuri
        dashboardViewModel.getImpegniFuturi().observe(getViewLifecycleOwner(), futuri -> {
            if (futuri != null) {
                binding.tvImpegniFuturi.setText(String.valueOf(futuri));
            }
        });

        // Impegni completati
        dashboardViewModel.getImpegniCompletati().observe(getViewLifecycleOwner(), completati -> {
            if (completati != null) {
                binding.tvImpegniCompletati.setText(String.valueOf(completati));
            }
        });
    }

    private void checkNotificationStatus() {
        if (getContext() != null) {
            NotificationHelper notificationHelper = new NotificationHelper(requireContext());
            boolean notificationsEnabled = notificationHelper.areNotificationsEnabled();

            String status = notificationsEnabled
                    ? "Notifiche: ✓ Abilitate (Santo del giorno ore 7:00)"
                    : "Notifiche: ✗ Disabilitate (Abilita nelle impostazioni)";

            binding.tvNotificationStatus.setText(status);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
