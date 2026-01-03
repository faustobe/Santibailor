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
import it.faustobe.santibailor.util.KeyboardUtils;

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

        // Nascondi tastiera quando si tocca fuori dai campi di testo
        KeyboardUtils.setupHideKeyboardOnOutsideTouch(binding.getRoot());

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

        // Listener per mostrare/nascondere il checkbox Personale quando si clicca su un item
        binding.etTipo.setOnItemClickListener((parent, view, position, id) -> {
            TipoRicorrenza selectedTipo = (TipoRicorrenza) parent.getItemAtPosition(position);
            updatePersonaleCheckboxVisibility(selectedTipo);
        });

        // Listener alternativo per quando il testo cambia (necessario per alcuni dispositivi)
        binding.etTipo.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tipoNome = s.toString().trim();
                TipoRicorrenza tipo = getTipoRicorrenzaByNome(tipoNome);
                if (tipo != null) {
                    updatePersonaleCheckboxVisibility(tipo);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        ricorrenzaViewModel.getAllTipiRicorrenza().observe(getViewLifecycleOwner(), tipi -> {
            tipiRicorrenza = tipi;
            adapter.clear();
            adapter.addAll(tipiRicorrenza);
            if (ricorrenzaToEdit != null) {
                setSelectedTipoRicorrenza(ricorrenzaToEdit.getTipoRicorrenzaId());
            } else {
                // In modalità creazione, controlla se il campo ha già un valore
                String currentText = binding.etTipo.getText().toString().trim();
                if (!currentText.isEmpty()) {
                    TipoRicorrenza tipo = getTipoRicorrenzaByNome(currentText);
                    if (tipo != null) {
                        updatePersonaleCheckboxVisibility(tipo);
                    }
                }
            }
        });

        // Listener aggiuntivo quando l'utente apre il dropdown o cambia focus
        binding.etTipo.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // Quando perde il focus, verifica il testo e aggiorna il checkbox
                String tipoNome = binding.etTipo.getText().toString().trim();
                TipoRicorrenza tipo = getTipoRicorrenzaByNome(tipoNome);
                if (tipo != null) {
                    updatePersonaleCheckboxVisibility(tipo);
                }
            }
        });
    }

    private void updatePersonaleCheckboxVisibility(TipoRicorrenza selectedTipo) {
        Log.d("EditRicorrenzaFragment", "updatePersonaleCheckboxVisibility called with tipo: " +
              (selectedTipo != null ? selectedTipo.getTipo() + " (id=" + selectedTipo.getId() + ")" : "null"));
        if (selectedTipo != null && selectedTipo.getId() == it.faustobe.santibailor.domain.model.TipoRicorrenza.LAICA) {
            Log.d("EditRicorrenzaFragment", "Showing checkbox Personale");
            binding.cbPersonale.setVisibility(View.VISIBLE);
        } else {
            Log.d("EditRicorrenzaFragment", "Hiding checkbox Personale");
            binding.cbPersonale.setVisibility(View.GONE);
            binding.cbPersonale.setChecked(false);
        }
    }

    private void setSelectedTipoRicorrenza(int tipoId) {
        // Se è PERSONALE (3), mostriamo come LAICA (2) nel dropdown
        int displayTipoId = tipoId;
        boolean isPersonale = (tipoId == it.faustobe.santibailor.domain.model.TipoRicorrenza.PERSONALE);

        if (isPersonale) {
            displayTipoId = it.faustobe.santibailor.domain.model.TipoRicorrenza.LAICA;
        }

        for (TipoRicorrenza tipo : tipiRicorrenza) {
            if (tipo.getId() == displayTipoId) {
                binding.etTipo.setText(tipo.getTipo(), false);

                // Mostra il checkbox Personale se è LAICA o PERSONALE
                if (displayTipoId == it.faustobe.santibailor.domain.model.TipoRicorrenza.LAICA) {
                    binding.cbPersonale.setVisibility(View.VISIBLE);
                    binding.cbPersonale.setChecked(isPersonale);
                }
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
                    // NON impostare selectedImageUri qui - deve essere impostato solo quando l'utente seleziona una nuova immagine
                }
                // Mostra il pulsante Elimina SOLO per ricorrenze NON religiose (laiche e personali)
                if (ricorrenza.getTipoRicorrenzaId() != it.faustobe.santibailor.domain.model.TipoRicorrenza.RELIGIOSA) {
                    binding.btnElimina.setVisibility(View.VISIBLE);
                } else {
                    binding.btnElimina.setVisibility(View.GONE);
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
        binding.btnElimina.setOnClickListener(v -> showDeleteConfirmDialog());
    }

    private void showDeleteConfirmDialog() {
        if (ricorrenzaToEdit == null) return;

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Conferma eliminazione")
                .setMessage("Sei sicuro di voler eliminare questa ricorrenza?")
                .setPositiveButton("Elimina", (dialog, which) -> deleteRicorrenza())
                .setNegativeButton("Annulla", null)
                .show();
    }

    private void deleteRicorrenza() {
        if (ricorrenzaToEdit == null || !isAdded() || getContext() == null) return;

        // Elimina l'immagine se presente e locale
        if (ricorrenzaToEdit.getImageUrl() != null &&
            !ricorrenzaToEdit.getImageUrl().isEmpty() &&
            !ricorrenzaToEdit.getImageUrl().startsWith("https://firebasestorage.googleapis.com")) {
            imageHandler.deleteImage(ricorrenzaToEdit.getImageUrl());
        }

        ricorrenzaViewModel.deleteRicorrenza(ricorrenzaToEdit);

        // Naviga indietro immediatamente senza aspettare il risultato
        // Il delete è asincrono ma il fragment non deve aspettare
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), "Eliminazione in corso...", Toast.LENGTH_SHORT).show();
            navigateBack();
        }
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

        // Determina il tipo finale: se è LAICA e checkbox Personale è selezionato, usa PERSONALE (3)
        int baseTipoId = selectedTipo.getId();
        final int tipoRicorrenzaId;
        if (baseTipoId == it.faustobe.santibailor.domain.model.TipoRicorrenza.LAICA
                && binding.cbPersonale.isChecked()) {
            tipoRicorrenzaId = it.faustobe.santibailor.domain.model.TipoRicorrenza.PERSONALE;
        } else {
            tipoRicorrenzaId = baseTipoId;
        }

        final String initialImageUrl = ricorrenzaToEdit != null ? ricorrenzaToEdit.getImageUrl() : null;
        String updatedImageUrl = initialImageUrl;

        if (selectedImageUri != null) {
            // Determina se è una ricorrenza personale o un Santo da Firebase
            boolean isFirebaseRicorrenza = initialImageUrl != null && initialImageUrl.startsWith("https://firebasestorage.googleapis.com");

            if (isFirebaseRicorrenza) {
                // Santo da Firebase: continua a usare Firebase
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
                // Ricorrenza personale: usa solo storage locale
                // Se c'era una vecchia immagine locale, eliminiamola prima
                if (initialImageUrl != null && !initialImageUrl.isEmpty()) {
                    imageHandler.deleteImage(initialImageUrl);
                }

                imageHandler.saveLocalImageOnly(selectedImageUri, new ImageHandler.OnImageSavedListener() {
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
            }
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
        if (tipiRicorrenza == null) {
            Log.w("EditRicorrenzaFragment", "tipiRicorrenza is null in getTipoRicorrenzaByNome");
            return null;
        }
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
        if (getView() != null) {
            Navigation.findNavController(getView()).navigateUp();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}