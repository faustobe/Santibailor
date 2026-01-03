package it.faustobe.santibailor.presentation.features.listespesa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentListeSpesaBinding;
import it.faustobe.santibailor.domain.model.ListaSpesa;
import it.faustobe.santibailor.presentation.features.main.MainActivity;

@AndroidEntryPoint
public class ListeSpesaFragment extends Fragment implements ListeSpesaAdapter.OnListaClickListener {
    private FragmentListeSpesaBinding binding;
    private ListeSpesaViewModel viewModel;
    private ListeSpesaAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListeSpesaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ListeSpesaViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();

        // Focus automatico sul textbox nuova lista
        binding.etNuovaLista.requestFocus();
    }

    private void setupRecyclerView() {
        adapter = new ListeSpesaAdapter(this);
        binding.recyclerViewListe.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewListe.setAdapter(adapter);
    }

    private void setupObservers() {
        // Osserva tutte le liste
        viewModel.getAllListe().observe(getViewLifecycleOwner(), liste -> {
            if (liste != null && !liste.isEmpty()) {
                binding.recyclerViewListe.setVisibility(View.VISIBLE);
                binding.emptyState.setVisibility(View.GONE);
                adapter.setListe(liste);
            } else {
                binding.recyclerViewListe.setVisibility(View.GONE);
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

        // Osserva l'ID della lista appena creata per aprirla automaticamente
        viewModel.getListaCreatedId().observe(getViewLifecycleOwner(), listaId -> {
            if (listaId != null && listaId > 0) {
                // Naviga al dettaglio della lista appena creata
                Bundle args = new Bundle();
                args.putInt("listaId", listaId);
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_listeSpesaFragment_to_dettaglioListaSpesaFragment,
                    args
                );
            }
        });
    }

    private void setupListeners() {
        // FAB per aggiungere nuova lista dal textbox
        binding.fabAddLista.setOnClickListener(v -> createListaFromTextbox());

        // Bottone elimina liste completate
        binding.btnDeleteCompletate.setOnClickListener(v -> showDeleteCompletateDialog());

        // Enter sul textbox crea la lista
        binding.etNuovaLista.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                createListaFromTextbox();
                return true;
            }
            return false;
        });
    }

    private void createListaFromTextbox() {
        String nome = binding.etNuovaLista.getText().toString().trim();
        if (!nome.isEmpty()) {
            viewModel.createLista(nome, "#4CAF50");
            binding.etNuovaLista.setText(""); // Pulisci il campo
            // Nascondi tastiera
            android.view.inputmethod.InputMethodManager imm =
                (android.view.inputmethod.InputMethodManager) requireActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.etNuovaLista.getWindowToken(), 0);
        } else {
            Toast.makeText(getContext(), getString(R.string.insert_list_name), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddListaDialog() {
        Log.d("ListeSpesaFragment", "Apertura dialog nuova lista");

        // Usa il layout custom con tema chiaro forzato
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_lista, null);
        final EditText input = dialogView.findViewById(R.id.edit_nome_lista);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), android.R.style.Theme_Material_Light_Dialog);
        builder.setTitle(R.string.new_list);
        builder.setView(dialogView);

        builder.setPositiveButton(R.string.create, (dialog, which) -> {
            String nome = input.getText().toString().trim();
            if (!nome.isEmpty()) {
                viewModel.createLista(nome, "#4CAF50");
            } else {
                Toast.makeText(getContext(), getString(R.string.insert_list_name), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.annulla, null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Colora i pulsanti
        Button btnCrea = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button btnAnnulla = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        if (btnCrea != null) {
            btnCrea.setTextColor(ContextCompat.getColor(requireContext(), R.color.success_green));
            btnCrea.setTextSize(16);
        }
        if (btnAnnulla != null) {
            btnAnnulla.setTextColor(Color.DKGRAY);
            btnAnnulla.setTextSize(16);
        }
    }

    private void showDeleteCompletateDialog() {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_completed_lists)
                .setMessage(R.string.confirm_delete_completed_lists)
                .setPositiveButton(R.string.elimina, (d, which) -> {
                    viewModel.deleteAllListeCompletate();
                })
                .setNegativeButton(R.string.annulla, null)
                .create();

        dialog.show();

        // Colora i pulsanti
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(Color.RED);
        }
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_default));
        }
    }

    @Override
    public void onListaClick(ListaSpesa lista) {
        // Naviga al dettaglio della lista
        Bundle args = new Bundle();
        args.putInt("listaId", lista.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_listeSpesaFragment_to_dettaglioListaSpesaFragment, args);
    }

    @Override
    public void onDeleteClick(ListaSpesa lista) {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_list)
                .setMessage(getString(R.string.confirm_delete_list, lista.getNome()))
                .setPositiveButton(R.string.elimina, (d, which) -> {
                    viewModel.deleteLista(lista);
                })
                .setNegativeButton(R.string.annulla, null)
                .create();

        dialog.show();

        // Colora i pulsanti
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(Color.RED);
        }
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_default));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
