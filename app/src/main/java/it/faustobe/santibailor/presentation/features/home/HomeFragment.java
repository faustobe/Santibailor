package it.faustobe.santibailor.presentation.features.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.animation.ObjectAnimator;
import android.widget.Toast;

import android.view.ViewTreeObserver;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.presentation.common.ricorrenze.RicorrenzaAdapter;
import it.faustobe.santibailor.presentation.features.main.MainActivity;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentHomeBinding;
import it.faustobe.santibailor.util.DateUtils;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RicorrenzaViewModel ricorrenzaViewModel;
    private boolean isInitialized = false;
    private RicorrenzaAdapter ricorrenzaAdapter;
    private boolean isPersonalInfoExpanded = false;
    private boolean isSaintsListExpanded = false;
    private boolean isCalendarExpanded = false;
    private HomeViewModel homeViewModel;
    private View expandPersonalInfoIcon;
    private static final String TAG = "HomeFragment";
    private NestedScrollView nestedScrollView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("HomeFragment", "onViewCreated called");
        ricorrenzaViewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);
        if (ricorrenzaAdapter == null) {
            ricorrenzaAdapter = new RicorrenzaAdapter(this::navigateToRicorrenzaDetail, this::showDeleteConfirmationDialog, ricorrenzaViewModel);
            binding.recyclerViewSaints.setAdapter(ricorrenzaAdapter);
        }
        // Osserva le ricorrenze del giorno
        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), this::updateRicorrenzeList);
        // Osserva il santo del giorno
        ricorrenzaViewModel.getCurrentSaint().observe(getViewLifecycleOwner(), this::updateSaintOfDay);
        if (!isInitialized) {
            ricorrenzaViewModel.loadRicorrenzeForCurrentDate();
            isInitialized = true;
        }
        // Imposta il listener per il pulsante di ricarica del santo
        binding.reloadSaintButton.setOnClickListener(v -> {
            Log.d("HomeFragment", "Refresh button clicked");
            reloadSaintOfDay();
        });
        // Carica le ricorrenze per la data corrente
        ricorrenzaViewModel.loadRicorrenzeForCurrentDate();

        ricorrenzaViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                // Mostra un indicatore di caricamento
            } else {
                // Nascondi l'indicatore di caricamento
            }
        });
        nestedScrollView = binding.nestedScrollView;
        setupScrollListener();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updateComponentsState();
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        setupDateDisplay();
        loadBackgroundImage();
        setupPersonalInfo();
        setupSaintsList();
        updateComponentsState();
        setupPersonalInfoCard();
        updateInitialVisibility();
        observeDeleteResult();
        setInitialSaintsListState();
        observeRicorrenze();
        setupCalendar();
        setupBottomNavigation();
    }

    private void observeRicorrenze() {
        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), this::updateRicorrenzeList);
        //ricorrenzaViewModel.getRicorrenzeReligiose().observe(getViewLifecycleOwner(), this::updateRicorrenzeReligiose);
        //ricorrenzaViewModel.getRicorrenzeLaiche().observe(getViewLifecycleOwner(), this::updateRicorrenzeLaiche);
    }

    private void updateRicorrenze(List<Ricorrenza> ricorrenze) {
        if (ricorrenze == null) return;
        Log.d("HomeFragment", "updateRicorrenze chiamato con " + ricorrenze.size() + " ricorrenze");
        updateRicorrenzeList(ricorrenze);
    }

    private void updateRicorrenzeList(List<Ricorrenza> ricorrenze) {
        if (ricorrenze != null && !ricorrenze.isEmpty()) {
            if (ricorrenze.size() > 1) {
                binding.expandCollapseSaintsIcon.setVisibility(View.VISIBLE);
                ricorrenzaAdapter.setRicorrenze(ricorrenze.subList(1, ricorrenze.size()));
            } else {
                binding.expandCollapseSaintsIcon.setVisibility(View.GONE);
                binding.recyclerViewSaints.setVisibility(View.GONE);
            }
        } else {
            binding.expandCollapseSaintsIcon.setVisibility(View.GONE);
            binding.recyclerViewSaints.setVisibility(View.GONE);
        }
        updateComponentsState();
    }

    private void updateSaintOfDay(Ricorrenza saint) {
        if (saint == null) return;
        Log.d("HomeFragment", "Updated saint of day: " + saint.getPrefix() + " " + saint.getNome());
        String saintText = saint.getPrefix() + " " + saint.getNome();
        binding.tvSaintOfDay.setText(saintText);
        binding.tvSaintOfDay.setOnClickListener(v -> navigateToRicorrenzaDetail(saint.getId()));
    }

    private void updateRicorrenzeReligiose(List<Ricorrenza> ricorrenze) {
        Log.d("HomeFragment", "Ricorrenze religiose: " + ricorrenze.size());
        // Aggiorna l'UI per le ricorrenze religiose
    }

    private void updateRicorrenzeLaiche(List<Ricorrenza> ricorrenze) {
        Log.d("HomeFragment", "Ricorrenze laiche: " + ricorrenze.size());
        // Aggiorna l'UI per le ricorrenze laiche
    }

    private void updateInitialVisibility() {
        View personalInfoContent = binding.cardPersonalInfo.findViewById(R.id.personal_info_content);
        if (personalInfoContent != null) {
            personalInfoContent.setVisibility(View.GONE);
        }

        if (binding.recyclerViewSaints != null) {
            binding.recyclerViewSaints.setVisibility(View.GONE);
        }

        updatePersonalInfoIcon();
        updateSaintsListIcon();
    }

    private void loadBackgroundImage() {
        Glide.with(this)
                .load(R.drawable.background_saint)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.backgroundImage);
    }

    private void navigateToRicorrenzaDetail(int ricorrenzaId) {
        if (ricorrenzaId > 0) {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToRicorrenzaDetailFragment(ricorrenzaId);
            Navigation.findNavController(requireView()).navigate(action);
        } else {
            Toast.makeText(requireContext(), "ID ricorrenza non valido", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSaintsList() {
        ricorrenzaAdapter = new RicorrenzaAdapter(this::navigateToRicorrenzaDetail, this::showDeleteConfirmationDialog, ricorrenzaViewModel);
        binding.recyclerViewSaints.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSaints.setAdapter(ricorrenzaAdapter);

        binding.expandCollapseSaintsIcon.setOnClickListener(v -> toggleSaintsListExpansion());

        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), this::updateRicorrenzeList);
    }

    private void setupCalendar() {
        Calendar calendar = Calendar.getInstance();
        binding.tvWeekday.setText(new SimpleDateFormat("EEEE", Locale.ITALIAN).format(calendar.getTime()));
        binding.tvDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        binding.tvMonth.setText(DateUtils.getCurrentMonthNameFull());

        // Imposta il calendario come espanso di default
        binding.calendarContent.setVisibility(View.VISIBLE);
        isCalendarExpanded = true;
    }

    private void reloadSaintOfDay() {
        Log.d("HomeFragment", "reloadSaintOfDay chiamata");
        ricorrenzaViewModel.refreshRandomSaint();
    }
/*
    private void setupSaintOfDay() {
        binding.reloadSaintButton.setOnClickListener(v -> {
            Log.d("HomeFragment", "Refresh button clicked");
            ricorrenzaViewModel.refreshRandomSaint();
        });

        ricorrenzaViewModel.getCurrentSaint().observe(getViewLifecycleOwner(), this::updateSaintOfDay);
    }

 */

    private void toggleSaintsListExpansion() {
        isSaintsListExpanded = !isSaintsListExpanded;
        binding.recyclerViewSaints.setVisibility(isSaintsListExpanded ? View.VISIBLE : View.GONE);
        binding.expandCollapseSaintsIcon.setImageResource(isSaintsListExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand_more);
        updateComponentsState();
    }

    private void updateSaintsListIcon() {
        if (binding.expandCollapseSaintsIcon != null) {
            binding.expandCollapseSaintsIcon.setImageResource(isSaintsListExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand_more);
        }
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_overview || itemId == R.id.navigation_help || itemId == R.id.navigation_add) {
                navigateToAddItemFragment();
                return true;
            }
            return false;
        });
    }

    private void navigateToAddItemFragment() {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToAddItemFragment("default");
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void setInitialSaintsListState() {
        isSaintsListExpanded = false;
        if (binding.recyclerViewSaints != null) {
            binding.recyclerViewSaints.setVisibility(View.GONE);
        }
        if (ricorrenzaAdapter != null) {
            ricorrenzaAdapter.setCollapsedView(true);
        }
        updateSaintsListIcon();
    }

    private void navigateToEditFragment(int ricorrenzaId) {
        if (ricorrenzaId > 0) {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToRicorrenzaDetailFragment(ricorrenzaId);
            Navigation.findNavController(requireView()).navigate(action);
        } else {
            Toast.makeText(requireContext(), "ID ricorrenza non valido", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupDateDisplay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EEEE", Locale.ITALIAN);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ITALIAN);

        binding.tvWeekday.setText(weekdayFormat.format(calendar.getTime()));
        binding.tvDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        binding.tvMonth.setText(DateUtils.getCurrentMonthNameFull());
    }

    private void showDeleteConfirmationDialog(Ricorrenza ricorrenza) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Conferma cancellazione")
                .setMessage("Sei sicuro di voler cancellare questa ricorrenza?")
                .setPositiveButton("Sì", (dialog, which) -> deleteRicorrenza(ricorrenza))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteRicorrenza(Ricorrenza ricorrenza) {
        ricorrenzaViewModel.deleteRicorrenza(ricorrenza);
    }

    private void observeDeleteResult() {
        ricorrenzaViewModel.getDeleteResult().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                Toast.makeText(requireContext(), "Ricorrenza cancellata con successo", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Errore durante la cancellazione", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPersonalInfo() {
        binding.tvPersonalEvent.setText("compleanno di zia Rosina");

        String todoList = """
                - paga bolletta luce
                - cambia filtro aria della cappa
                - compra regalo per Denise - compleanno fra 5 gg
                - finisci rapporto AZS - consegna fra 3 gg""";
        binding.tvTodoList.setText(todoList);

        String todayEvents = """
                oggi:
                * gym
                * ape con Chicca alle h 18
                + la ricotta è in frigo da 4 giorni""";
        binding.tvTodayEvents.setText(todayEvents);
    }

    private void setupPersonalInfoCard() {
        View header = binding.cardPersonalInfo.findViewById(R.id.tv_personal_info_header);
        if (header != null) {
            header.setOnClickListener(v -> togglePersonalInfoExpansion());
        }
        expandPersonalInfoIcon = binding.cardPersonalInfo.findViewById(R.id.expand_collapse_saints_icon);
    }

    private void togglePersonalInfoExpansion() {
        isPersonalInfoExpanded = !isPersonalInfoExpanded;
        View contentView = binding.cardPersonalInfo.findViewById(R.id.personal_info_content);
        if (contentView != null) {
            if (isPersonalInfoExpanded) {
                contentView.setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(contentView, "alpha", 0f, 1f).start();
            } else {
                ObjectAnimator.ofFloat(contentView, "alpha", 1f, 0f).setDuration(200).start();
                contentView.postDelayed(() -> {
                    contentView.setVisibility(View.GONE);
                    updateBottomMenuVisibility();
                }, 200);
            }
        }
        updatePersonalInfoIcon();
        updateComponentsState();
    }

    private void updatePersonalInfoIcon() {
        if (expandPersonalInfoIcon != null) {
            expandPersonalInfoIcon.setRotation(isPersonalInfoExpanded ? 180f : 0f);
        }
    }

    private void setupScrollListener() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).handleScroll();
                }
            }
        });
    }

    private boolean isScrolledToTop() {
        return nestedScrollView.getScrollY() == 0;
    }

    private void updateBottomMenuVisibility() {
        boolean allCollapsed = !isPersonalInfoExpanded && !isSaintsListExpanded;
        if (allCollapsed && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateComponentsVisibility(true);
        }
    }

    private void updateComponentsState() {
        boolean allCollapsed = !isCalendarExpanded && !isPersonalInfoExpanded && !isSaintsListExpanded && isScrolledToTop();
        homeViewModel.setAllComponentsCollapsed(allCollapsed);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNav();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}