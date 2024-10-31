package it.faustobe.santibailor.presentation.features.ricorrenza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentRicorrenzaDetailBinding;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.domain.model.Ricorrenza;

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
        String ricorrenzaIdString = String.valueOf(ricorrenzaId);

        viewModel.getRicorrenzaById(ricorrenzaId).observe(getViewLifecycleOwner(), ricorrenza -> {
            if (ricorrenza != null) {
                binding.tvNome.setText(ricorrenza.getNome());

                // Carica la biografia da Firebase
                viewModel.getBio(ricorrenzaIdString).observe(getViewLifecycleOwner(), bio -> {
                    if (bio != null && !bio.isEmpty()) {
                        binding.tvBio.setText(bio);
                    } else {
                        binding.tvBio.setText(ricorrenza.getBio());
                    }
                });

                // Carica l'immagine da Firebase
                viewModel.downloadImage(ricorrenzaIdString).observe(getViewLifecycleOwner(), imageData -> {
                    if (imageData != null) {
                        Glide.with(this)
                                .load(imageData)
                                .placeholder(R.drawable.placeholder_image)
                                .into(binding.ivRicorrenza);
                    } else {
                        // Fallback all'URL dell'immagine locale se l'immagine Firebase non è disponibile
                        viewModel.loadImage(ricorrenza.getImageUrl(), binding.ivRicorrenza, R.drawable.placeholder_image);
                    }
                });
            }
        });

        binding.btnModifica.setOnClickListener(v -> {
            RicorrenzaDetailFragmentDirections.ActionRicorrenzaDetailFragmentToEditRicorrenzaFragment action =
                    RicorrenzaDetailFragmentDirections.actionRicorrenzaDetailFragmentToEditRicorrenzaFragment(ricorrenzaId);
            Navigation.findNavController(v).navigate(action);
        });
    }

    private void updateUI(Ricorrenza ricorrenza) {
        if (ricorrenza != null) {
            binding.tvNome.setText(ricorrenza.getNome());
            binding.tvBio.setText(ricorrenza.getBio());
            viewModel.loadImage(ricorrenza.getImageUrl(), binding.ivRicorrenza, R.drawable.placeholder_image);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}