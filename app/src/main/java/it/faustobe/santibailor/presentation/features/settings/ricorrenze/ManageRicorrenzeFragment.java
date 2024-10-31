package it.faustobe.santibailor.presentation.features.settings.ricorrenze;
//fragment di gestione delle ricorrenze
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentManageRicorrenzeBinding;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.util.NavigationUtils;

public class ManageRicorrenzeFragment extends Fragment {

    private FragmentManageRicorrenzeBinding binding;
    private RicorrenzaViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageRicorrenzeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);

        setupUI();
    }

    private void setupUI() {
        binding.btnAddRicorrenza.setOnClickListener(v -> navigateToAddRicorrenza());
        binding.btnViewAllRicorrenze.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_manageRicorrenzeFragment_to_searchFragment));
    }

    private void navigateToAddRicorrenza() {
        NavController navController = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putString("itemType", "ricorrenza");
        Log.d("ManageRicorrenzeFragment", "Navigating to AddItemFragment with itemType: ricorrenza");
        navController.navigate(R.id.action_manageRicorrenzeFragment_to_addItemFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}