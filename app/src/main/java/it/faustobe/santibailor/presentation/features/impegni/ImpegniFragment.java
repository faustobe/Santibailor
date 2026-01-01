package it.faustobe.santibailor.presentation.features.impegni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentImpegniBinding;
import it.faustobe.santibailor.domain.model.Impegno;
import it.faustobe.santibailor.util.ImageHandler;

@AndroidEntryPoint
public class ImpegniFragment extends Fragment {

    private FragmentImpegniBinding binding;
    private ImpegniViewModel impegniViewModel;
    private ImpegniAdapter adapter;
    private ImageHandler imageHandler;

    // Filtro corrente
    private FilterType currentFilter = FilterType.ALL;

    private enum FilterType {
        ALL, TODAY, FUTURE, COMPLETED
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentImpegniBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        impegniViewModel = new ViewModelProvider(this).get(ImpegniViewModel.class);
        imageHandler = ImageHandler.getInstance(requireContext());

        setupRecyclerView();
        setupFilters();
        setupFab();
        setupSwipeRefresh();
        observeImpegni();
    }

    private void setupRecyclerView() {
        adapter = new ImpegniAdapter(
                this::onImpegnoClick,
                this::onImpegnoCompleted,
                imageHandler
        );

        binding.recyclerViewImpegni.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewImpegni.setAdapter(adapter);
    }

    private void setupFilters() {
        binding.chipShowAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentFilter = FilterType.ALL;
                observeImpegni();
            }
        });

        binding.chipToday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentFilter = FilterType.TODAY;
                observeImpegni();
            }
        });

        binding.chipFuture.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentFilter = FilterType.FUTURE;
                observeImpegni();
            }
        });

        binding.chipCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                currentFilter = FilterType.COMPLETED;
                observeImpegni();
            }
        });
    }

    private void setupFab() {
        binding.fabAdd.setOnClickListener(v -> navigateToCreate());
        binding.btnAddFirstImpegno.setOnClickListener(v -> navigateToCreate());
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(() -> {
            observeImpegni();
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    private void observeImpegni() {
        // Rimuovi observer precedenti
        impegniViewModel.getAllImpegni().removeObservers(getViewLifecycleOwner());
        impegniViewModel.getImpegniOggi().removeObservers(getViewLifecycleOwner());
        impegniViewModel.getImpegniFuturi(100).removeObservers(getViewLifecycleOwner());

        switch (currentFilter) {
            case ALL:
                impegniViewModel.getAllImpegni().observe(getViewLifecycleOwner(), this::updateList);
                break;

            case TODAY:
                impegniViewModel.getImpegniOggi().observe(getViewLifecycleOwner(), this::updateList);
                break;

            case FUTURE:
                impegniViewModel.getImpegniFuturi(100).observe(getViewLifecycleOwner(), this::updateList);
                break;

            case COMPLETED:
                impegniViewModel.getAllImpegni().observe(getViewLifecycleOwner(), impegni -> {
                    if (impegni != null) {
                        List<Impegno> completati = new java.util.ArrayList<>();
                        for (Impegno impegno : impegni) {
                            if (impegno.isCompletato()) {
                                completati.add(impegno);
                            }
                        }
                        updateList(completati);
                    }
                });
                break;
        }
    }

    private void updateList(List<Impegno> impegni) {
        if (impegni == null || impegni.isEmpty()) {
            binding.emptyState.setVisibility(View.VISIBLE);
            binding.recyclerViewImpegni.setVisibility(View.GONE);
        } else {
            binding.emptyState.setVisibility(View.GONE);
            binding.recyclerViewImpegni.setVisibility(View.VISIBLE);
            adapter.setImpegni(impegni);
        }
    }

    private void onImpegnoClick(Impegno impegno) {
        navigateToEdit(impegno.getId());
    }

    private void onImpegnoCompleted(Impegno impegno, boolean isCompleted) {
        if (isCompleted) {
            impegniViewModel.markAsCompletato(impegno.getId());
        } else {
            impegniViewModel.markAsNonCompletato(impegno.getId());
        }
    }

    private void navigateToCreate() {
        if (getView() != null) {
            Bundle args = new Bundle();
            args.putInt("impegnoId", -1);
            Navigation.findNavController(getView()).navigate(R.id.action_impegniFragment_to_editImpegnoFragment, args);
        }
    }

    private void navigateToEdit(int impegnoId) {
        if (getView() != null) {
            Bundle args = new Bundle();
            args.putInt("impegnoId", impegnoId);
            Navigation.findNavController(getView()).navigate(R.id.action_impegniFragment_to_editImpegnoFragment, args);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
