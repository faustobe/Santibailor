package it.faustobe.santibailor.presentation.common.ricorrenze;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.IOException;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentEditRicorrenzaBinding;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.util.DateUtils;
import it.faustobe.santibailor.util.ImageHandler;

public class EditRicorrenzaFragment extends Fragment {

    private FragmentEditRicorrenzaBinding binding;
    private RicorrenzaViewModel ricorrenzaViewModel;
    private Ricorrenza ricorrenzaToEdit;
    private List<TipoRicorrenza> tipiRicorrenza;
    private ArrayAdapter<TipoRicorrenza> tipiAdapter;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private ImageHandler imageHandler;
    private ActivityResultLauncher<String> pickImageLauncher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        imageHandler.loadImage(uri.toString(), binding.ivRicorrenza, R.drawable.placeholder_image);
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditRicorrenzaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ricorrenzaViewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);
        imageHandler = ImageHandler.getInstance(requireContext());

        setupImageSelection();
        setupDatePickers();
        setupTipiRicorrenza();
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

    private void setupImageSelection() {
        binding.btnSelectImage.setOnClickListener(v -> openImageChooser());
        binding.ivRicorrenza.setOnClickListener(v -> openImageChooser());
    }

    private void openImageChooser() {
        pickImageLauncher.launch("image/*");
    }

    private void setupTipiRicorrenza() {
        ArrayAdapter<TipoRicorrenza> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        binding.etTipo.setAdapter(adapter);

        ricorrenzaViewModel.getAllTipiRicorrenza().observe(getViewLifecycleOwner(), tipi -> {
            tipiRicorrenza = tipi;
            adapter.clear();
            adapter.addAll(tipiRicorrenza);
            if (ricorrenzaToEdit != null) {
                setSelectedTipoRicorrenza(ricorrenzaToEdit.getTipoRicorrenzaId());
            }
        });
    }

    private void setSelectedTipoRicorrenza(int tipoId) {
        for (TipoRicorrenza tipo : tipiRicorrenza) {
            if (tipo.getId() == tipoId) {
                binding.etTipo.setText(tipo.getTipo(), false);
                break;
            }
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
                if (ricorrenza.getImageUrl() != null && !ricorrenza.getImageUrl().isEmpty()) {
                    imageHandler.loadImage(ricorrenza.getImageUrl(), binding.ivRicorrenza, R.drawable.placeholder_image);
                    selectedImageUri = Uri.parse(ricorrenza.getImageUrl());
                }
            } else {
                Toast.makeText(getContext(), "Ricorrenza non trovata", Toast.LENGTH_SHORT).show();
                navigateBack();
            }
        });
    }

    private void populateFields(Ricorrenza ricorrenza) {
        if (ricorrenza != null) {
            binding.etNome.setText(ricorrenza.getNome());
            setSelectedTipoRicorrenza(ricorrenza.getTipoRicorrenzaId());
            binding.etPrefisso.setText(ricorrenza.getPrefix());
            binding.etSuffisso.setText(ricorrenza.getSuffix());
            binding.etDescrizione.setText(ricorrenza.getBio());
            binding.datePicker.dayPicker.setValue(ricorrenza.getGiorno());
            binding.datePicker.monthPicker.setValue(ricorrenza.getIdMese()+1);
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

        TipoRicorrenza selectedTipo = getTipoRicorrenzaByNome(tipoNome);
        if (!validateInputs(nome, selectedTipo)) {
            return;
        }

        int id = (ricorrenzaToEdit != null) ? ricorrenzaToEdit.getId() : 0;
        int giorno = binding.datePicker.dayPicker.getValue();
        int idMese = binding.datePicker.monthPicker.getValue() - 1;
        int tipoRicorrenzaId = selectedTipo.getId();
        final String initialImageUrl = ricorrenzaToEdit != null ? ricorrenzaToEdit.getImageUrl() : null;
        String updatedImageUrl = initialImageUrl;

        if (selectedImageUri != null) {
            imageHandler.saveOrUpdateImageSafely(selectedImageUri, initialImageUrl, new ImageHandler.OnImageSavedListener() {
                @Override
                public void onImageSaved(String updatedImageUrl) {
                    if (isAdded() && getContext() != null) {
                        completeRicorrenzaSave(id, idMese, giorno, nome, descrizione, updatedImageUrl, prefisso, suffisso, tipoRicorrenzaId);
                    } else {
                        Log.w("EditRicorrenzaFragment", "Fragment not attached or context is null when image save completed");
                    }
                }

                @Override
                public void onError(Exception e) {
                    if (isAdded() && getContext() != null) {
                        Toast.makeText(getContext(), "Errore nel salvataggio dell'immagine: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("EditRicorrenzaFragment", "Error saving image, and fragment not attached", e);
                    }
                }
            });
        } else {
            completeRicorrenzaSave(id, idMese, giorno, nome, descrizione, initialImageUrl, prefisso, suffisso, tipoRicorrenzaId);
        }
    }

    private void completeRicorrenzaSave(int id, int idMese, int giorno, String nome, String descrizione,
                                        String imageUrl, String prefisso, String suffisso, int tipoRicorrenzaId) {
        if (!isAdded()) {
            Log.w("EditRicorrenzaFragment", "Fragment not attached, cannot complete save");
            return;
        }

        Context context = getContext();
        if (context == null) {
            Log.w("EditRicorrenzaFragment", "Context is null, cannot complete save");
            return;
        }

        Ricorrenza ricorrenza = new Ricorrenza(
                id, idMese, giorno, nome, descrizione, imageUrl, prefisso, suffisso, tipoRicorrenzaId
        );

        if (ricorrenzaToEdit != null) {
            ricorrenzaViewModel.update(ricorrenza);
            showToast(context, "Ricorrenza aggiornata");
            safeNavigateBack();
        } else {
            ricorrenzaViewModel.insert(ricorrenza, new RicorrenzaViewModel.OnInsertCompleteListener() {
                @Override
                public void onInsertSuccess(int newId) {
                    if (isAdded() && getContext() != null) {
                        showToast(getContext(), "Nuova ricorrenza creata");
                        safeNavigateBack();
                    }
                }

                @Override
                public void onInsertFailure(String error) {
                    if (isAdded() && getContext() != null) {
                        showToast(getContext(), "Errore nella creazione della ricorrenza: " + error);
                    }
                }
            });
        }
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void safeNavigateBack() {
        if (isAdded() && getView() != null) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigateUp();
        }
    }

    private TipoRicorrenza getTipoRicorrenzaByNome(String nome) {
        for (TipoRicorrenza tipo : tipiRicorrenza) {
            if (tipo.getTipo().equals(nome)) {
                return tipo;
            }
        }
        return null;
    }

    private boolean validateInputs(String nome, TipoRicorrenza tipo) {
        if (nome.isEmpty() || tipo == null) {
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