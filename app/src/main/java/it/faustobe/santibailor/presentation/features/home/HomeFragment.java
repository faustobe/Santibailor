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
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.domain.model.Ricorrenza;
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
    private RicorrenzaAdapter ricorrenzaAdapter;
    private boolean isPersonalInfoExpanded = false;
    private boolean isSaintsListExpanded = false;
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
        ricorrenzaViewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);
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
        setupSaintOfDay();
        setupPersonalInfo();
        setupSaintsList();
        updateComponentsState();
        setupPersonalInfoCard();
        updateInitialVisibility();
        observeDeleteResult();
        setInitialSaintsListState();
        observeRicorrenze();
    }

    private void observeRicorrenze() {
        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), this::updateRicorrenzeList);
        ricorrenzaViewModel.getRicorrenzeReligiose().observe(getViewLifecycleOwner(), this::updateRicorrenzeReligiose);
        ricorrenzaViewModel.getRicorrenzeLaiche().observe(getViewLifecycleOwner(), this::updateRicorrenzeLaiche);
    }

    private void updateRicorrenzeList(List<Ricorrenza> ricorrenze) {
        if (ricorrenze != null && !ricorrenze.isEmpty()) {
            Ricorrenza mainSaint = ricorrenze.get(0);
            binding.tvSaintOfDay.setText(mainSaint.getPrefix() + " " + mainSaint.getNome());
            binding.tvSaintOfDay.setOnClickListener(v -> navigateToRicorrenzaDetail(mainSaint.getId()));

            if (ricorrenze.size() > 1) {
                binding.tvSaintsListHeader.setText("Altri santi del giorno (" + (ricorrenze.size() - 1) + ")");
                binding.saintsHeader.setVisibility(View.VISIBLE);
            } else {
                binding.saintsHeader.setVisibility(View.GONE);
            }

            ricorrenzaAdapter.setRicorrenze(ricorrenze);
            binding.recyclerViewSaints.setVisibility(View.VISIBLE);
        } else {
            binding.tvSaintOfDay.setText("Nessun santo oggi");
            binding.saintsHeader.setVisibility(View.GONE);
            binding.recyclerViewSaints.setVisibility(View.GONE);
            ricorrenzaAdapter.setRicorrenze(Collections.emptyList());
        }

        setInitialSaintsListState();
        updateComponentsState();
    }

    private void updateRicorrenzeReligiose(List<Ricorrenza> ricorrenzeReligiose) {
        // Implementazione da aggiungere quando saranno disponibili le viste per le ricorrenze religiose
        // Per ora, possiamo loggare il numero di ricorrenze religiose per debug
        Log.d(TAG, "Ricorrenze religiose: " + (ricorrenzeReligiose != null ? ricorrenzeReligiose.size() : 0));
    }

    private void updateRicorrenzeLaiche(List<Ricorrenza> ricorrenzeLaiche) {
        // Implementazione da aggiungere quando saranno disponibili le viste per le ricorrenze laiche
        // Per ora, possiamo loggare il numero di ricorrenze laiche per debug
        Log.d(TAG, "Ricorrenze laiche: " + (ricorrenzeLaiche != null ? ricorrenzeLaiche.size() : 0));
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

        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), ricorrenze -> {
            if (ricorrenze != null && !ricorrenze.isEmpty()) {
                Ricorrenza mainSaint = ricorrenze.get(0);
                binding.tvSaintOfDay.setText(mainSaint.getPrefix() + " " + mainSaint.getNome());
                binding.tvSaintOfDay.setOnClickListener(v -> navigateToRicorrenzaDetail(mainSaint.getId()));

                if (ricorrenze.size() > 1) {
                    binding.tvSaintsListHeader.setText("Altri santi del giorno (" + (ricorrenze.size() - 1) + ")");
                    binding.saintsHeader.setVisibility(View.VISIBLE);
                } else {
                    binding.saintsHeader.setVisibility(View.GONE);
                }

                ricorrenzaAdapter.setRicorrenze(ricorrenze);
                // notifyDataSetChanged() non è più necessario qui
            } else {
                binding.tvSaintOfDay.setText("Nessun santo oggi");
                binding.saintsHeader.setVisibility(View.GONE);
                ricorrenzaAdapter.setRicorrenze(Collections.emptyList()); // Aggiorna con una lista vuota
            }

            setInitialSaintsListState();
        });

        binding.saintsHeader.setOnClickListener(v -> toggleSaintsListExpansion());
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

    private void toggleSaintsListExpansion() {
        isSaintsListExpanded = !isSaintsListExpanded;
        if (ricorrenzaAdapter != null) {
            ricorrenzaAdapter.setCollapsedView(!isSaintsListExpanded);
        }
        if (binding.recyclerViewSaints != null) {
            if (isSaintsListExpanded) {
                binding.recyclerViewSaints.setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(binding.recyclerViewSaints, "alpha", 0f, 1f).start();
            } else {
                ObjectAnimator.ofFloat(binding.recyclerViewSaints, "alpha", 1f, 0f).setDuration(200).start();
                binding.recyclerViewSaints.postDelayed(() -> {
                    binding.recyclerViewSaints.setVisibility(View.GONE);
                    updateBottomMenuVisibility();
                }, 200);
            }
        }
        updateSaintsListIcon();
        updateComponentsState();
    }

    private void updateSaintsListIcon() {
        if (binding.expandCollapseIcon != null) {
            binding.expandCollapseIcon.setImageResource(isSaintsListExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand_more);
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

    private void setupSaintOfDay() {
        ricorrenzaViewModel.getRicorrenzeDelGiorno().observe(getViewLifecycleOwner(), ricorrenze -> {
            if (ricorrenze != null && !ricorrenze.isEmpty()) {
                binding.tvSaintOfDay.setText(ricorrenze.get(0).getPrefix() + " " + ricorrenze.get(0).getNome());
            }
        });
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
        expandPersonalInfoIcon = binding.cardPersonalInfo.findViewById(R.id.expand_collapse_icon);
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

    private void updateComponentsState() {
        boolean allCollapsed = !isPersonalInfoExpanded && !isSaintsListExpanded && isScrolledToTop();
        homeViewModel.setAllComponentsCollapsed(allCollapsed);
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