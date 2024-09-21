package it.faustobe.santibailor.presentation.features.settings.ricorrenze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentRicorrenzeVisualizationSettingsBinding;
import it.faustobe.santibailor.presentation.features.settings.SettingsViewModel;

public class RicorrenzeVisualizationSettingsFragment extends Fragment {
    private FragmentRicorrenzeVisualizationSettingsBinding binding;
    private SettingsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRicorrenzeVisualizationSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        setupUI();
        observeViewModel();
    }

    private void setupUI() {
        binding.switchShowReligiose.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.setShowReligiose(isChecked));

        binding.switchShowLaiche.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.setShowLaiche(isChecked));

        binding.radioGroupViewType.setOnCheckedChangeListener((group, checkedId) -> {
            String viewType = checkedId == R.id.radioButtonDaily ? "daily" :
                    checkedId == R.id.radioButtonWeekly ? "weekly" : "monthly";
            viewModel.setRicorrenzeViewType(viewType);
        });
    }

    private void observeViewModel() {
        viewModel.getShowReligiose().observe(getViewLifecycleOwner(), show ->
                binding.switchShowReligiose.setChecked(show));

        viewModel.getShowLaiche().observe(getViewLifecycleOwner(), show ->
                binding.switchShowLaiche.setChecked(show));

        viewModel.getRicorrenzeViewType().observe(getViewLifecycleOwner(), viewType -> {
            int id = viewType.equals("daily") ? R.id.radioButtonDaily :
                    viewType.equals("weekly") ? R.id.radioButtonWeekly : R.id.radioButtonMonthly;
            binding.radioGroupViewType.check(id);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
