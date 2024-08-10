package it.faustobe.santibailor.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentHomeBinding;
import it.faustobe.santibailor.viewmodel.RicorrenzaViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RicorrenzaViewModel ricorrenzaViewModel;
    private RicorrenzaAdapter ricorrenzaAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ricorrenzaViewModel = new ViewModelProvider(this).get(RicorrenzaViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //ricorrenzaViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(RicorrenzaViewModel.class);

        setupDateDisplay();
        setupSaintOfDay();
        setupPersonalInfo();
        setupBottomNavigation();
        setupRecyclerView();

        return root;
    }

    private void loadBackgroundImage() {
        Glide.with(this)
                .load(R.drawable.background_saint)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.backgroundImage);
    }

    private void setupDateDisplay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EEEE", Locale.ITALIAN);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ITALIAN);

        binding.tvWeekday.setText(weekdayFormat.format(calendar.getTime()));
        binding.tvDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        binding.tvMonth.setText(monthFormat.format(calendar.getTime()));
    }

    private void setupSaintOfDay() {
        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), ricorrenze -> {
            if (ricorrenze != null && !ricorrenze.isEmpty()) {
                binding.tvSaintOfDay.setText(ricorrenze.get(0).ricorrenza.getNome());
            }
        });
    }

    private void setupPersonalInfo() {
        // Qui dovresti recuperare le informazioni personali dal tuo database o dalle preferenze
        binding.tvPersonalEvent.setText("compleanno di zia Rosina");

        StringBuilder todoList = new StringBuilder("- paga bolletta luce\n");
        todoList.append("- cambia filtro aria della cappa\n");
        todoList.append("- compra regalo per Denise - compleanno fra 5 gg\n");
        todoList.append("- finisci rapporto AZS - consegna fra 3 gg");
        binding.tvTodoList.setText(todoList.toString());

        StringBuilder todayEvents = new StringBuilder("oggi:\n");
        todayEvents.append("* gym\n");
        todayEvents.append("* ape con Chicca alle h 18\n");
        todayEvents.append("+ la ricotta è in frigo da 4 giorni");
        binding.tvTodayEvents.setText(todayEvents.toString());
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = binding.bottomNavigation;
        bottomNav.setOnItemSelectedListener(item -> {
            // Gestisci la navigazione qui
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Già nella home, non fare nulla
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                // Naviga alla dashboard
                // Esempio: Navigation.findNavController(requireView()).navigate(R.id.action_home_to_dashboard);
                return true;
            } else if (itemId == R.id.navigation_notifications) {
                // Naviga alle notifiche
                // Esempio: Navigation.findNavController(requireView()).navigate(R.id.action_home_to_notifications);
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        ricorrenzaAdapter = new RicorrenzaAdapter(null);
        binding.recyclerViewRicorrenze.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRicorrenze.setAdapter(ricorrenzaAdapter);

        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), ricorrenze -> {
            ricorrenzaAdapter.setRicorrenze(ricorrenze);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}