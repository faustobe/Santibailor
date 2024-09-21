

package it.faustobe.santibailor.presentation.features.add;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.bumptech.glide.Glide;

import it.faustobe.santibailor.domain.model.Ricorrenza;
//import it.faustobe.santibailor.domain.model.RicorrenzaConTipo;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.presentation.features.main.MainActivity;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.data.local.entities.RicorrenzaEntity;
import it.faustobe.santibailor.data.repository.GenericRepository;
import it.faustobe.santibailor.databinding.FragmentAddItemBinding;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;


public class AddItemFragment extends Fragment {
    private FragmentAddItemBinding binding;
    private ActivityResultLauncher<String> mGetContent;
    private boolean isViewCreated = false;
    private String itemType;
    private RicorrenzaViewModel ricorrenzaViewModel;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gestione del pulsante indietro
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBack();
            }
        });
        // Registrazione del launcher per la selezione dell'immagine
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        displaySelectedImage();
                    }
                });

        if (getArguments() != null) {
            itemType = getArguments().getString("itemType");
        }
        ricorrenzaViewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        setupUI();
        setupDatePickers();
        hideBottomNavigation();
        setupToolbar();
        setupMenu();
    }

    private void setupToolbar() {
        if (!isViewCreated) return;

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
    }

    private void setupUI() {
        // Rimuovere il titolo dal layout del fragment
        binding.titleTextView.setVisibility(View.GONE);

        if ("ricorrenza".equals(itemType)) {
            setupTypeDropdown();
        }

        binding.saveButton.setOnClickListener(v -> saveItem());
        binding.selectImageButton.setOnClickListener(v -> openImageChooser());
    }

    private void setupDatePickers() {
        Calendar today = Calendar.getInstance();

        binding.datePicker.dayPicker.setMinValue(1);
        binding.datePicker.dayPicker.setMaxValue(31);
        binding.datePicker.dayPicker.setValue(today.get(Calendar.DAY_OF_MONTH));

        binding.datePicker.monthPicker.setMinValue(1);
        binding.datePicker.monthPicker.setMaxValue(12);
        binding.datePicker.monthPicker.setValue(today.get(Calendar.MONTH) + 1);
        binding.datePicker.monthPicker.setDisplayedValues(new String[]{"Gen", "Feb", "Mar", "Apr", "Mag", "Giu",
                "Lug", "Ago", "Set", "Ott", "Nov", "Dic"});

        binding.datePicker.monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            updateDayPicker(newVal);
        });

        binding.datePicker.todayButton.setOnClickListener(v -> setDateToToday());

        updateDayPicker(today.get(Calendar.MONTH) + 1);
    }

    private void setDateToToday() {
        Calendar today = Calendar.getInstance();
        binding.datePicker.dayPicker.setValue(today.get(Calendar.DAY_OF_MONTH));
        binding.datePicker.monthPicker.setValue(today.get(Calendar.MONTH) + 1);
    }


    private void updateDayPicker(int month) {
        int maxDays = switch (month) {
            case 2 -> // Febbraio
                    29; // Consideriamo gli anni bisestili
            case 4, 6, 9, 11 -> // Aprile, Giugno, Settembre, Novembre
                    30;
            default -> 31;
        };
        binding.datePicker.dayPicker.setMaxValue(maxDays);
    }

    private void setupTypeDropdown() {
        String[] types = {"Religioso", "Laico"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, types);
        ((AutoCompleteTextView) binding.typeInputLayout.getEditText()).setAdapter(adapter);
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                // Inflate del menu specifico per questo fragment, se necessario
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    navigateBack();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }

    private void saveItem() {
        if ("ricorrenza".equals(itemType)) {
            saveRicorrenza();
        }
        // Aggiungi altri casi per diversi tipi di elementi se necessario
    }

    private void openImageChooser() {
        imagePickerLauncher.launch("image/*");
    }

    private void displaySelectedImage() {
        if (selectedImageUri != null) {
            Glide.with(this).load(selectedImageUri).into(binding.imageView);
        }
    }

    private Uri processAndSaveImage() {
        if (selectedImageUri == null) return null;

        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
            if (inputStream == null) return null;

            File outputDir = requireContext().getCacheDir();
            File outputFile = File.createTempFile("processed_image", ".jpg", outputDir);
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return Uri.fromFile(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Errore nel processamento dell'immagine", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void saveRicorrenza() {
        String nome = binding.nameInputLayout.getEditText().getText().toString();
        String tipo = binding.typeInputLayout.getEditText().getText().toString();
        String prefisso = binding.prefixInputLayout.getEditText().getText().toString();
        String suffisso = binding.suffixInputLayout.getEditText().getText().toString();
        String bio = binding.bioInputLayout.getEditText().getText().toString();

        int giorno = binding.datePicker.dayPicker.getValue();
        int mese = binding.datePicker.monthPicker.getValue();

        if (nome.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(requireContext(), "Per favore, compila tutti i campi obbligatori", Toast.LENGTH_SHORT).show();
            return;
        }
        Ricorrenza ricorrenza = new Ricorrenza(0, mese, giorno, nome, bio, "", "", "", 0);
        TipoRicorrenza tipoRicorrenza = new TipoRicorrenza(ricorrenza.getId(), ""); // Il nome del tipo può essere vuoto per ora
        RicorrenzaConTipo newRicorrenza = new RicorrenzaConTipo(ricorrenza, tipoRicorrenza);
        if (selectedImageUri != null) {
            Uri processedUri = processAndSaveImage();
            if (processedUri != null) {
                ricorrenza.setImageUrl(processedUri.toString());
            }
        }

        ricorrenzaViewModel.insert(newRicorrenza.getRicorrenza(), new RicorrenzaViewModel.OnInsertCompleteListener() {
            @Override
            public void onInsertSuccess(int newId) {
                Toast.makeText(getContext(), "Ricorrenza inserita con successo", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigateUp();
            }

            @Override
            public void onInsertFailure(String error) {
                Toast.makeText(getContext(), "Errore nell'inserimento: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideBottomNavigation() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    private void showBottomNavigation() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showBottomNavigation();
        isViewCreated = false;
        binding = null;
    }
}