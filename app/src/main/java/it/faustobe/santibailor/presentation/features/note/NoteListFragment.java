package it.faustobe.santibailor.presentation.features.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentNoteListBinding;
import it.faustobe.santibailor.domain.model.Nota;

/**
 * Fragment per visualizzare la lista di note
 */
@AndroidEntryPoint
public class NoteListFragment extends Fragment implements NoteAdapter.OnNotaClickListener {

    private FragmentNoteListBinding binding;
    private NoteViewModel viewModel;
    private NoteAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNoteListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new NoteAdapter(this);
        binding.recyclerViewNote.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewNote.setAdapter(adapter);
    }

    private void setupObservers() {
        // Osserva tutte le note
        viewModel.getAllNote().observe(getViewLifecycleOwner(), note -> {
            if (note != null && !note.isEmpty()) {
                binding.recyclerViewNote.setVisibility(View.VISIBLE);
                binding.emptyState.setVisibility(View.GONE);
                adapter.setNote(note);
            } else {
                binding.recyclerViewNote.setVisibility(View.GONE);
                binding.emptyState.setVisibility(View.VISIBLE);
            }
        });

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
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                viewModel.clearMessages();
            }
        });
    }

    private void setupListeners() {
        // FAB per creare nuova nota
        binding.fabAddNota.setOnClickListener(v -> {
            // Naviga a NoteDetailFragment con notaId = 0 (nuova nota)
            Bundle args = new Bundle();
            args.putInt("notaId", 0);
            Navigation.findNavController(v).navigate(
                    R.id.action_noteListFragment_to_noteDetailFragment,
                    args
            );
        });
    }

    @Override
    public void onNotaClick(Nota nota) {
        // Naviga a NoteDetailFragment per modificare la nota
        Bundle args = new Bundle();
        args.putInt("notaId", nota.getId());
        Navigation.findNavController(requireView()).navigate(
                R.id.action_noteListFragment_to_noteDetailFragment,
                args
        );
    }

    @Override
    public void onNotaLongClick(Nota nota) {
        // Per ora non implementato, potrebbe aprire un menu contestuale
        Toast.makeText(getContext(), "Long click su: " + nota.getTitolo(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
