package it.faustobe.santibailor.presentation.features.note;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentNoteDetailBinding;
import it.faustobe.santibailor.domain.model.Nota;
import it.faustobe.santibailor.util.KeyboardUtils;

/**
 * Fragment per creare o modificare una nota
 */
@AndroidEntryPoint
public class NoteDetailFragment extends Fragment {

    private FragmentNoteDetailBinding binding;
    private NoteViewModel viewModel;
    private int notaId = 0; // 0 = nuova nota
    private Nota currentNota;
    private boolean isEditMode = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // Recupera argomento notaId
        if (getArguments() != null) {
            notaId = getArguments().getInt("notaId", 0);
        }

        isEditMode = notaId > 0;

        setupUI();
        setupListeners();
        setupObservers();

        // Nascondi tastiera quando si tocca fuori dai campi di testo
        KeyboardUtils.setupHideKeyboardOnOutsideTouch(binding.getRoot());

        if (isEditMode) {
            loadNota();
        }
    }

    private void setupUI() {
        // Aggiorna titolo toolbar
        String title = isEditMode ? getString(R.string.edit_note) : getString(R.string.new_note);
        binding.tvToolbarTitle.setText(title);

        // Mostra pulsante elimina solo in modalità modifica (ora nella toolbar)
        binding.btnDelete.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    }

    private void setupListeners() {
        // Pulsante annulla (non salva le modifiche)
        binding.btnCancel.setOnClickListener(v -> {
            KeyboardUtils.hideKeyboard(this);
            Navigation.findNavController(v).navigateUp();
        });

        // Pulsante salva
        binding.btnSave.setOnClickListener(v -> {
            KeyboardUtils.hideKeyboard(this);
            saveNota(true);
        });

        // Pulsante elimina (ora nella toolbar)
        binding.btnDelete.setOnClickListener(v -> {
            KeyboardUtils.hideKeyboard(this);
            showDeleteConfirmationDialog();
        });
    }

    private void setupObservers() {
        // Osserva messaggi di errore
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                viewModel.clearMessages();
            }
        });

        // Osserva messaggi di successo
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                // Solo se richiesto esplicitamente
                viewModel.clearMessages();
            }
        });

        // Osserva ID nota creata (per passare da create a edit mode)
        viewModel.getNotaCreatedId().observe(getViewLifecycleOwner(), id -> {
            if (id != null && id > 0 && !isEditMode) {
                notaId = id;
                isEditMode = true;
                setupUI();
                // Carica la nota appena creata per evitare copie multiple
                loadNota();
            }
        });
    }

    private void loadNota() {
        viewModel.getNotaById(notaId).observe(getViewLifecycleOwner(), nota -> {
            if (nota != null) {
                currentNota = nota;
                binding.etTitolo.setText(nota.getTitolo());
                binding.etContenuto.setText(nota.getContenuto());
            }
        });
    }

    private void saveNota(boolean showToast) {
        // Controllo se binding è null (fragment distrutto)
        if (binding == null) {
            return;
        }

        String titolo = binding.etTitolo.getText().toString().trim();
        String contenuto = binding.etContenuto.getText().toString().trim();

        if (titolo.isEmpty()) {
            if (showToast) {
                Toast.makeText(getContext(), getString(R.string.note_title_required), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (isEditMode && currentNota != null) {
            // Aggiorna nota esistente
            currentNota.setTitolo(titolo);
            currentNota.setContenuto(contenuto);
            viewModel.updateNota(currentNota);
            if (showToast) {
                Toast.makeText(getContext(), getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
            }
        } else {
            // Crea nuova nota
            viewModel.createNota(titolo, contenuto);
            if (showToast) {
                Toast.makeText(getContext(), getString(R.string.note_created), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_note))
                .setMessage(getString(R.string.delete_note_confirm))
                .setPositiveButton(getString(R.string.elimina), (dialog, which) -> {
                    if (currentNota != null) {
                        viewModel.deleteNota(currentNota);
                        Toast.makeText(getContext(), getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).navigateUp();
                    }
                })
                .setNegativeButton(getString(R.string.annulla), null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
