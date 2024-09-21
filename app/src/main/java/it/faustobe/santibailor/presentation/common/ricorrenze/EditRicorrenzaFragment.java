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
import it.faustobe.santibailor.databinding.FragmentEditRicorrenzaBinding;
import it.faustobe.santibailor.domain.model.RicorrenzaConTipo;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.util.DateUtils;
public class EditRicorrenzaFragment extends Fragment {

    private FragmentEditRicorrenzaBinding binding;
    private RicorrenzaViewModel ricorrenzaViewModel;
    private Ricorrenza ricorrenzaToEdit;

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
                ricorrenzaToEdit = ricorrenza;
                populateFields(ricorrenza);
            } else {
                Toast.makeText(getContext(), "Ricorrenza non trovata", Toast.LENGTH_SHORT).show();
                navigateBack();
            }
        });
    }

    private void populateFields(Ricorrenza ricorrenza) {
        if (ricorrenza != null) {
            binding.etNome.setText(ricorrenza.getNome());
            binding.etTipo.setText(getTipoRicorrenzaNomeFromId(ricorrenza.getTipoRicorrenzaId()));
            binding.etPrefisso.setText(ricorrenza.getPrefix());
            binding.etSuffisso.setText(ricorrenza.getSuffix());
            binding.etDescrizione.setText(ricorrenza.getBio());

            binding.datePicker.dayPicker.setValue(ricorrenza.getGiorno());
            binding.datePicker.monthPicker.setValue(ricorrenza.getIdMese() + 1);
        }
    }

    private String getTipoRicorrenzaNomeFromId(int tipoId) {
        switch (tipoId) {
            case TipoRicorrenza.RELIGIOSA:
                return "Religiosa";
            case TipoRicorrenza.LAICA:
                return "Laica";
            default:
                return "Sconosciuto";
        }
    }

    private void setupForNewRicorrenza() {
        binding.btnSalva.setText(R.string.create);
    }

    private void setupListeners() {
        binding.btnSalva.setOnClickListener(v -> saveRicorrenza());
    }

    private void saveRicorrenza() {
        String nome = binding.etNome.getText().toString().trim();
        String tipoNome = binding.etTipo.getText().toString().trim();
        String prefisso = binding.etPrefisso.getText().toString().trim();
        String suffisso = binding.etSuffisso.getText().toString().trim();
        String descrizione = binding.etDescrizione.getText().toString().trim();

        if (!validateInputs(nome, tipoNome)) {
            return;
        }

        int id = (ricorrenzaToEdit != null) ? ricorrenzaToEdit.getId() : 0;
        int giorno = binding.datePicker.dayPicker.getValue();
        int idMese = binding.datePicker.monthPicker.getValue() - 1;
        int tipoRicorrenzaId = getTipoRicorrenzaIdFromNome(tipoNome);
        String imageUrl = ricorrenzaToEdit != null ? ricorrenzaToEdit.getImageUrl() : "";

        Ricorrenza ricorrenza = new Ricorrenza(
                id,
                idMese,
                giorno,
                nome,
                descrizione,
                imageUrl,
                prefisso,
                suffisso,
                tipoRicorrenzaId
        );

        if (ricorrenzaToEdit != null) {
            ricorrenzaViewModel.update(ricorrenza);
            ricorrenzaViewModel.getUpdateResult().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    Toast.makeText(requireContext(), "Ricorrenza aggiornata", Toast.LENGTH_SHORT).show();
                    navigateBack();
                } else {
                    Toast.makeText(requireContext(), "Errore nell'aggiornamento della ricorrenza", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            ricorrenzaViewModel.insert(ricorrenza, new RicorrenzaViewModel.OnInsertCompleteListener() {
                @Override
                public void onInsertSuccess(int newId) {
                    Toast.makeText(requireContext(), "Nuova ricorrenza creata", Toast.LENGTH_SHORT).show();
                    navigateBack();
                }

                @Override
                public void onInsertFailure(String error) {
                    Toast.makeText(requireContext(), "Errore nella creazione della ricorrenza: " + error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // Metodo di utilità per ottenere l'ID del tipo di ricorrenza dal nome
    private int getTipoRicorrenzaIdFromNome(String tipoNome) {
        switch (tipoNome.toLowerCase()) {
            case "religiosa":
                return TipoRicorrenza.RELIGIOSA;
            case "laica":
                return TipoRicorrenza.LAICA;
            default:
                // Gestisci il caso di default o lancia un'eccezione
                throw new IllegalArgumentException("Tipo di ricorrenza non valido: " + tipoNome);
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