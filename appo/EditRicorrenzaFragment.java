package it.faustobe.santibailor.presentation.common.ricorrenze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.repository.GenericRepository;
import it.faustobe.santibailor.databinding.FragmentEditRicorrenzaBinding;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.RicorrenzaConTipo;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.util.DateUtils;

public class EditRicorrenzaFragment extends Fragment {

    private FragmentEditRicorrenzaBinding binding;
    private RicorrenzaViewModel ricorrenzaViewModel;
    private RicorrenzaEntity ricorrenzaEntityToEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditRicorrenzaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ricorrenzaViewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);

        setupDatePickers();
        setupListeners();

        if (getArguments() != null) {
            int ricorrenzaId = getArguments().getInt("ricorrenzaId", -1);
            if (ricorrenzaId != -1) {
                loadRicorrenza(ricorrenzaId);
            } else {
                setupForNewRicorrenza();
            }
        } else {
            setupForNewRicorrenza();
        }
    }

    private void setupDatePickers() {
        binding.datePicker.dayPicker.setMinValue(1);
        binding.datePicker.dayPicker.setMaxValue(31);

        binding.datePicker.monthPicker.setMinValue(1);
        binding.datePicker.monthPicker.setMaxValue(12);
        binding.datePicker.monthPicker.setDisplayedValues(DateUtils.getMonthNamesShort());

        binding.datePicker.monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            updateDayPicker(newVal);
        });
    }

    private void updateDayPicker(int month) {
        int maxDays = DateUtils.getMaxDaysInMonth(month);
        binding.datePicker.dayPicker.setMaxValue(maxDays);
    }

    private void loadRicorrenza(int ricorrenzaId) {
        ricorrenzaViewModel.getRicorrenzaById(ricorrenzaId).observe(getViewLifecycleOwner(), ricorrenza -> {
            if (ricorrenza != null) {
                ricorrenzaEntityToEdit = ricorrenza;
                populateFields(ricorrenza);
            } else {
                Toast.makeText(getContext(), "Ricorrenza non trovata", Toast.LENGTH_SHORT).show();
                navigateBack();
            }
        });
    }

    private void populateFields(RicorrenzaConTipo ricorrenza) {
        binding.etNome.setText(ricorrenza.getRicorrenza().getNome());
        binding.etTipo.setText(ricorrenza.getRicorrenza().getTipoRicorrenzaId() == 1 ? "Religioso" : "Laico", false);
        binding.etPrefisso.setText(ricorrenza.getRicorrenza().getPrefix());
        binding.etSuffisso.setText(ricorrenza.getRicorrenza().getSuffix());
        binding.etDescrizione.setText(ricorrenza.getRicorrenza().getBio());

        // Correzione: Aggiungiamo 1 al mese per la visualizzazione
        binding.datePicker.dayPicker.setValue(ricorrenza.getRicorrenza().getGiorno());
        binding.datePicker.monthPicker.setValue(ricorrenza.getRicorrenza().getIdMese() + 1);
    }

    private void setupForNewRicorrenza() {
        binding.btnSalva.setText(R.string.create);
    }

    private void setupListeners() {
        binding.btnSalva.setOnClickListener(v -> saveRicorrenza());
    }

    private void saveRicorrenza() {
        String nome = binding.etNome.getText().toString().trim();
        String tipo = binding.etTipo.getText().toString().trim();
        String prefisso = binding.etPrefisso.getText().toString().trim();
        String suffisso = binding.etSuffisso.getText().toString().trim();
        String descrizione = binding.etDescrizione.getText().toString().trim();

        if (!validateInputs(nome, tipo)) {
            return;
        }

        RicorrenzaEntity ricorrenzaEntity = (ricorrenzaEntityToEdit != null) ? ricorrenzaEntityToEdit : new RicorrenzaEntity();
        ricorrenzaEntity.setNome(nome);
        ricorrenzaEntity.setGiorno(binding.datePicker.dayPicker.getValue());
        // Correzione: Sottraiamo 1 al mese per il salvataggio nel database
        ricorrenzaEntity.setIdMese(binding.datePicker.monthPicker.getValue() - 1);
        ricorrenzaEntity.setTipoRicorrenzaId("Religioso".equals(tipo) ? 1 : 2);
        ricorrenzaEntity.setPrefix(prefisso);
        ricorrenzaEntity.setSuffix(suffisso);
        ricorrenzaEntity.setBio(descrizione);

        GenericRepository.OnOperationCompleteListener listener = new GenericRepository.OnOperationCompleteListener() {
            @Override
            public void onSuccess(int id) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(),
                            ricorrenzaEntityToEdit != null ? "Ricorrenza aggiornata" : "Nuova ricorrenza creata",
                            Toast.LENGTH_SHORT).show();
                    navigateBack();
                });
            }

            @Override
            public void onError(String errorMessage) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(),
                            "Errore nel salvataggio: " + errorMessage,
                            Toast.LENGTH_LONG).show();
                });
            }
        };

        if (ricorrenzaEntityToEdit != null) {
            ricorrenzaViewModel.update(ricorrenzaEntity, listener);
        } else {
            ricorrenzaViewModel.insert(ricorrenzaEntity, listener);
        }
    }

    private boolean validateInputs(String nome, String tipo) {
        if (nome.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(getContext(), "Compila tutti i campi obbligatori", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}