package it.faustobe.santibailor.presentation.features.listespesa;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentDettaglioListaSpesaBinding;
import it.faustobe.santibailor.domain.model.ItemSpesa;
import it.faustobe.santibailor.domain.model.ListaSpesa;
import it.faustobe.santibailor.util.KeyboardUtils;

@AndroidEntryPoint
public class DettaglioListaSpesaFragment extends Fragment implements ItemSpesaAdapter.OnItemClickListener {
    private FragmentDettaglioListaSpesaBinding binding;
    private ListeSpesaViewModel viewModel;
    private ItemSpesaAdapter adapter;
    private int listaId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDettaglioListaSpesaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ListeSpesaViewModel.class);

        // Ottieni l'ID della lista dagli arguments
        if (getArguments() != null) {
            listaId = getArguments().getInt("listaId", -1);
        }

        if (listaId == -1) {
            Toast.makeText(getContext(), getString(R.string.error_list_not_found), Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigateUp();
            return;
        }

        setupRecyclerView();
        setupObservers();
        setupListeners();
        setupAutocomplete();

        // Nascondi tastiera quando si tocca fuori dai campi di testo
        KeyboardUtils.setupHideKeyboardOnOutsideTouch(binding.getRoot());
    }

    private void setupRecyclerView() {
        adapter = new ItemSpesaAdapter(this);
        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewItems.setAdapter(adapter);
    }

    private void setupObservers() {
        // Osserva la lista
        viewModel.getListaById(listaId).observe(getViewLifecycleOwner(), this::updateListaInfo);

        // Osserva gli item
        viewModel.getItemsByListaId(listaId).observe(getViewLifecycleOwner(), items -> {
            if (items != null && !items.isEmpty()) {
                binding.recyclerViewItems.setVisibility(View.VISIBLE);
                binding.emptyState.setVisibility(View.GONE);
                adapter.setItems(items);
            } else {
                binding.recyclerViewItems.setVisibility(View.GONE);
                binding.emptyState.setVisibility(View.VISIBLE);
            }
        });

        // Osserva messaggi
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                viewModel.clearMessages();
            }
        });

        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                viewModel.clearMessages();
            }
        });
    }

    private void updateListaInfo(ListaSpesa lista) {
        if (lista == null) return;

        // Solo nome della lista
        binding.tvNomeLista.setText(lista.getNome());
    }

    private void setupListeners() {
        // Pulsante indietro/chiudi
        binding.btnBack.setOnClickListener(v -> {
            try {
                NavController navController = Navigation.findNavController(v);
                navController.navigateUp();
            } catch (Exception e) {
                // Fallback: usa requireActivity
                requireActivity().onBackPressed();
            }
        });

        // Aggiunta item
        binding.btnAddItem.setOnClickListener(v -> addNewItem());

        binding.etNuovoItem.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                addNewItem();
                return true;
            }
            return false;
        });

        // Menu lista
        binding.btnMenuLista.setOnClickListener(v -> showListaMenu());
    }

    private void addNewItem() {
        String nome = binding.etNuovoItem.getText().toString().trim();
        if (!nome.isEmpty()) {
            viewModel.addItem(listaId, nome, null, null);
            binding.etNuovoItem.setText("");
            KeyboardUtils.hideKeyboard(this); // Chiudi tastiera dopo aver aggiunto
        } else {
            Toast.makeText(getContext(), getString(R.string.insert_product_name), Toast.LENGTH_SHORT).show();
        }
    }


    private void showListaMenu() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.list_options)
                .setItems(new String[]{getString(R.string.delete_list)}, (dialog, which) -> {
                    if (which == 0) {
                        showDeleteListaDialog();
                    }
                })
                .show();
    }

    private void showDeleteListaDialog() {
        viewModel.getListaById(listaId).observe(getViewLifecycleOwner(), lista -> {
            if (lista != null) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.delete_list)
                        .setMessage(getString(R.string.confirm_delete_list, lista.getNome()))
                        .setPositiveButton(R.string.elimina, (dialog, which) -> {
                            viewModel.deleteLista(lista);
                            Navigation.findNavController(requireView()).navigateUp();
                        })
                        .setNegativeButton(R.string.annulla, null)
                        .show();
            }
        });
    }

    private void showDeleteCompletatiDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_completed_items)
                .setMessage(R.string.confirm_delete_completed)
                .setPositiveButton(R.string.elimina, (dialog, which) -> {
                    viewModel.deleteItemCompletati(listaId);
                })
                .setNegativeButton(R.string.annulla, null)
                .show();
    }

    private void setupAutocomplete() {
        // Adapter per le suggestionsi
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                new java.util.ArrayList<>()
        );
        binding.etNuovoItem.setAdapter(adapter);

        // Listener per il cambio di testo
        binding.etNuovoItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    viewModel.searchProdotti(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Osserva i suggerimenti dal ViewModel
        viewModel.getSuggestionsList().observe(getViewLifecycleOwner(), suggestions -> {
            if (suggestions != null) {
                adapter.clear();
                adapter.addAll(suggestions);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemToggle(ItemSpesa item) {
        viewModel.toggleItemCompletato(item);
    }

    @Override
    public void onItemDelete(ItemSpesa item) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_product)
                .setMessage(getString(R.string.confirm_delete_product, item.getNome()))
                .setPositiveButton(R.string.elimina, (dialog, which) -> {
                    viewModel.deleteItem(item);
                })
                .setNegativeButton(R.string.annulla, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
