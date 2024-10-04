package it.faustobe.santibailor.presentation.features.ricorrenza;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentRicorrenzaDetailBinding;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;

public class RicorrenzaDetailFragment extends Fragment {

    private FragmentRicorrenzaDetailBinding binding;
    private RicorrenzaViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRicorrenzaDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);

        int ricorrenzaId = RicorrenzaDetailFragmentArgs.fromBundle(getArguments()).getRicorrenzaId();

        viewModel.getRicorrenzaById(ricorrenzaId).observe(getViewLifecycleOwner(), ricorrenza -> {
            if (ricorrenza != null) {
                binding.tvNome.setText(ricorrenza.getNome());
                binding.tvBio.setText(ricorrenza.getBio());
                viewModel.loadImage(ricorrenza.getImageUrl(), binding.ivRicorrenza, R.drawable.placeholder_image);
                // Aggiungi altri campi se necessario
            }
        });

        binding.btnModifica.setOnClickListener(v -> {
            RicorrenzaDetailFragmentDirections.ActionRicorrenzaDetailFragmentToEditRicorrenzaFragment action =
                    RicorrenzaDetailFragmentDirections.actionRicorrenzaDetailFragmentToEditRicorrenzaFragment(ricorrenzaId);
            Navigation.findNavController(v).navigate(action);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
