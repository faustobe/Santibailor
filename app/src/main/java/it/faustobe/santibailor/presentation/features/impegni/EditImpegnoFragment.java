package it.faustobe.santibailor.presentation.features.impegni;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentEditImpegnoBinding;
import it.faustobe.santibailor.domain.model.Impegno;
import it.faustobe.santibailor.domain.model.Priorita;
import it.faustobe.santibailor.util.ImageHandler;

@AndroidEntryPoint
public class EditImpegnoFragment extends Fragment {

    private FragmentEditImpegnoBinding binding;
    private ImpegniViewModel impegniViewModel;
    private Impegno impegnoToEdit;
    private Uri selectedImageUri;
    private ImageHandler imageHandler;
    private ActivityResultLauncher<String> pickImageLauncher;

    private NumberPicker dayPicker, monthPicker, yearPicker, hourPicker, minutePicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        binding.ivImpegno.setVisibility(View.VISIBLE);
                        imageHandler.loadImage(uri.toString(), binding.ivImpegno, R.drawable.placeholder_image);
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditImpegnoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        impegniViewModel = new ViewModelProvider(this).get(ImpegniViewModel.class);
        imageHandler = ImageHandler.getInstance(requireContext());

        setupDateTimePicker();
        setupPrioritaDropdown();
        setupCategoriaDropdown();
        setupReminderFields();
        setupImageSelection();
        setupListeners();

        if (getArguments() != null) {
            int impegnoId = getArguments().getInt("impegnoId", -1);
            if (impegnoId != -1) {
                loadImpegno(impegnoId);
            } else {
                setupForNewImpegno();
            }
        } else {
            setupForNewImpegno();
        }
    }

    private void setupDateTimePicker() {
        // Trova i picker dal layout incluso
        dayPicker = binding.dateTimePicker.dayPicker;
        monthPicker = binding.dateTimePicker.monthPicker;
        yearPicker = binding.dateTimePicker.yearPicker;
        hourPicker = binding.dateTimePicker.hourPicker;
        minutePicker = binding.dateTimePicker.minutePicker;

        // Setup day picker (1-31)
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        // Setup month picker (1-12)
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        // Setup year picker (anno corrente - 10 anni futuri)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearPicker.setMinValue(currentYear);
        yearPicker.setMaxValue(currentYear + 10);

        // Setup hour picker (0-23)
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);

        // Setup minute picker (0-59)
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        // Imposta data/ora corrente di default
        setCurrentDateTime();

        // Button "Ora" per impostare data/ora corrente
        binding.dateTimePicker.nowButton.setOnClickListener(v -> setCurrentDateTime());
    }

    private void setCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        dayPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        monthPicker.setValue(calendar.get(Calendar.MONTH) + 1);
        yearPicker.setValue(calendar.get(Calendar.YEAR));
        hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        minutePicker.setValue(calendar.get(Calendar.MINUTE));
    }

    private void setupPrioritaDropdown() {
        String[] prioritaValues = {
                getString(R.string.priorita_bassa),
                getString(R.string.priorita_media),
                getString(R.string.priorita_alta)
        };

        ArrayAdapter<String> prioritaAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                prioritaValues
        );

        binding.etPriorita.setAdapter(prioritaAdapter);
        binding.etPriorita.setText(getString(R.string.priorita_media), false); // Default: MEDIA

        // Listener per cambiare il colore dell'indicatore
        binding.etPriorita.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPriorita = Priorita.getAll()[position];
            updatePriorityColorIndicator(selectedPriorita);
        });
    }

    private void updatePriorityColorIndicator(String priorita) {
        int colorRes = Priorita.getColorResource(priorita);
        int color = ContextCompat.getColor(requireContext(), colorRes);
        binding.priorityColorIndicator.setBackgroundColor(color);
    }

    private void setupCategoriaDropdown() {
        // Categorie comuni predefinite
        String[] categorieComuni = {
                "Lavoro",
                "Personale",
                "Famiglia",
                "Salute",
                "Studio",
                "Sport",
                "Shopping",
                "Viaggio"
        };

        ArrayAdapter<String> categoriaAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categorieComuni
        );

        binding.etCategoria.setAdapter(categoriaAdapter);
        binding.etCategoria.setThreshold(1); // Mostra suggerimenti dopo 1 carattere
    }

    private void setupReminderFields() {
        binding.switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.tilReminderMinutes.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Default: reminder disabilitato
        binding.tilReminderMinutes.setVisibility(View.GONE);
    }

    private void setupImageSelection() {
        binding.btnSelectImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        binding.ivImpegno.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
    }

    private void setupListeners() {
        binding.btnSalva.setOnClickListener(v -> saveImpegno());
        binding.btnElimina.setOnClickListener(v -> showDeleteConfirmDialog());
    }

    private void showDeleteConfirmDialog() {
        if (impegnoToEdit == null) return;

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Conferma eliminazione")
                .setMessage("Sei sicuro di voler eliminare questo impegno?")
                .setPositiveButton("Elimina", (dialog, which) -> deleteImpegno())
                .setNegativeButton("Annulla", null)
                .show();
    }

    private void setupForNewImpegno() {
        // Imposta valori di default
        binding.etPriorita.setText(getString(R.string.priorita_media), false);
        updatePriorityColorIndicator(Priorita.MEDIA);

        // Nascondi checkbox completato e bottone elimina
        binding.cbCompletato.setVisibility(View.GONE);
        binding.btnElimina.setVisibility(View.GONE);
    }

    private void loadImpegno(int impegnoId) {
        impegniViewModel.getImpegnoById(impegnoId).observe(getViewLifecycleOwner(), impegno -> {
            if (impegno != null) {
                impegnoToEdit = impegno;
                populateFields(impegno);
            }
        });
    }

    private void populateFields(Impegno impegno) {
        // Dati base
        binding.etTitolo.setText(impegno.getTitolo());
        binding.etDescrizione.setText(impegno.getDescrizione());
        binding.etCategoria.setText(impegno.getCategoria());
        binding.etNote.setText(impegno.getNote());

        // Data e ora
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(impegno.getDataOra());
        dayPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        monthPicker.setValue(calendar.get(Calendar.MONTH) + 1);
        yearPicker.setValue(calendar.get(Calendar.YEAR));
        hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        minutePicker.setValue(calendar.get(Calendar.MINUTE));

        // Priorità
        String prioritaDisplay = Priorita.getDisplayName(impegno.getPriorita());
        binding.etPriorita.setText(prioritaDisplay, false);
        updatePriorityColorIndicator(impegno.getPriorita());

        // Reminder
        binding.switchReminder.setChecked(impegno.isReminderEnabled());
        if (impegno.isReminderEnabled()) {
            binding.etReminderMinutes.setText(String.valueOf(impegno.getReminderMinutesBefore()));
        }

        // Completato
        binding.cbCompletato.setVisibility(View.VISIBLE);
        binding.cbCompletato.setChecked(impegno.isCompletato());

        // Immagine
        if (impegno.getImageUrl() != null && !impegno.getImageUrl().isEmpty()) {
            binding.ivImpegno.setVisibility(View.VISIBLE);
            imageHandler.loadImage(impegno.getImageUrl(), binding.ivImpegno, R.drawable.placeholder_image);
        }

        // Mostra bottone elimina
        binding.btnElimina.setVisibility(View.VISIBLE);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Titolo obbligatorio
        if (binding.etTitolo.getText() == null || binding.etTitolo.getText().toString().trim().isEmpty()) {
            binding.tilTitolo.setError(getString(R.string.required_field));
            isValid = false;
        } else {
            binding.tilTitolo.setError(null);
        }

        // Priorità obbligatoria
        if (binding.etPriorita.getText() == null || binding.etPriorita.getText().toString().trim().isEmpty()) {
            binding.tilPriorita.setError(getString(R.string.required_field));
            isValid = false;
        } else {
            binding.tilPriorita.setError(null);
        }

        return isValid;
    }

    private void saveImpegno() {
        if (!validateInputs()) {
            Toast.makeText(requireContext(), R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        android.util.Log.d("EditImpegnoFragment", "saveImpegno called, selectedImageUri: " + selectedImageUri);

        // Gestione immagine se selezionata (salvataggio SOLO locale, non Firebase)
        if (selectedImageUri != null) {
            android.util.Log.d("EditImpegnoFragment", "Saving image locally: " + selectedImageUri);
            // Se stiamo modificando e c'era una vecchia immagine, eliminiamola prima
            if (impegnoToEdit != null && impegnoToEdit.getImageUrl() != null && !impegnoToEdit.getImageUrl().isEmpty()) {
                imageHandler.deleteImage(impegnoToEdit.getImageUrl());
            }

            imageHandler.saveLocalImageOnly(
                    selectedImageUri,
                    new ImageHandler.OnImageSavedListener() {
                        @Override
                        public void onImageSaved(String newImageUrl) {
                            completeSave(newImageUrl);
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(requireContext(), "Errore salvataggio immagine: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            completeSave(null);
                        }
                    }
            );
        } else {
            // Mantieni l'immagine esistente se in edit mode
            String imageUrl = (impegnoToEdit != null) ? impegnoToEdit.getImageUrl() : null;
            completeSave(imageUrl);
        }
    }

    private void completeSave(String imageUrl) {
        long dataOra = getSelectedDateTime();
        long now = System.currentTimeMillis();

        // Estrai priorità dal display name
        String prioritaDisplay = binding.etPriorita.getText().toString();
        String priorita = Priorita.MEDIA; // default
        if (prioritaDisplay.equals(getString(R.string.priorita_alta))) {
            priorita = Priorita.ALTA;
        } else if (prioritaDisplay.equals(getString(R.string.priorita_bassa))) {
            priorita = Priorita.BASSA;
        }

        // Reminder
        boolean reminderEnabled = binding.switchReminder.isChecked();
        int reminderMinutes = 30; // default
        if (reminderEnabled && binding.etReminderMinutes.getText() != null) {
            try {
                reminderMinutes = Integer.parseInt(binding.etReminderMinutes.getText().toString());
            } catch (NumberFormatException e) {
                reminderMinutes = 30;
            }
        }

        Impegno impegno;
        if (impegnoToEdit != null) {
            // Update mode
            impegno = new Impegno(
                    impegnoToEdit.getId(),
                    binding.etTitolo.getText().toString().trim(),
                    binding.etDescrizione.getText() != null ? binding.etDescrizione.getText().toString().trim() : "",
                    dataOra,
                    binding.etCategoria.getText() != null ? binding.etCategoria.getText().toString().trim() : "",
                    reminderEnabled,
                    reminderMinutes,
                    binding.cbCompletato.isChecked(),
                    binding.etNote.getText() != null ? binding.etNote.getText().toString().trim() : "",
                    impegnoToEdit.getCreatedAt(),
                    now,
                    priorita,
                    imageUrl
            );

            impegniViewModel.updateImpegno(impegno);
            Toast.makeText(requireContext(), R.string.impegno_updated, Toast.LENGTH_SHORT).show();
        } else {
            // Insert mode
            impegno = new Impegno(
                    0,
                    binding.etTitolo.getText().toString().trim(),
                    binding.etDescrizione.getText() != null ? binding.etDescrizione.getText().toString().trim() : "",
                    dataOra,
                    binding.etCategoria.getText() != null ? binding.etCategoria.getText().toString().trim() : "",
                    reminderEnabled,
                    reminderMinutes,
                    false,
                    binding.etNote.getText() != null ? binding.etNote.getText().toString().trim() : "",
                    now,
                    now,
                    priorita,
                    imageUrl
            );

            impegniViewModel.insertImpegno(impegno);
            Toast.makeText(requireContext(), R.string.impegno_saved, Toast.LENGTH_SHORT).show();
        }

        navigateBack();
    }

    private long getSelectedDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yearPicker.getValue());
        calendar.set(Calendar.MONTH, monthPicker.getValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayPicker.getValue());
        calendar.set(Calendar.HOUR_OF_DAY, hourPicker.getValue());
        calendar.set(Calendar.MINUTE, minutePicker.getValue());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private void deleteImpegno() {
        if (impegnoToEdit != null) {
            // Elimina l'immagine locale se presente
            if (impegnoToEdit.getImageUrl() != null && !impegnoToEdit.getImageUrl().isEmpty()) {
                imageHandler.deleteImage(impegnoToEdit.getImageUrl());
            }

            impegniViewModel.deleteImpegno(impegnoToEdit);
            Toast.makeText(requireContext(), R.string.impegno_deleted, Toast.LENGTH_SHORT).show();
            navigateBack();
        }
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
