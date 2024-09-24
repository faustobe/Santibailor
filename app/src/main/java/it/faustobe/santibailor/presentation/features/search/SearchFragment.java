package it.faustobe.santibailor.presentation.features.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.faustobe.santibailor.data.repository.RicorrenzaRepository;
import it.faustobe.santibailor.databinding.FragmentSearchBinding;
import it.faustobe.santibailor.domain.model.Ricorrenza;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.presentation.common.ricorrenze.RicorrenzaAdapter;
import it.faustobe.santibailor.util.DateUtils;
import it.faustobe.santibailor.util.MonthDayPickerHelper;
import it.faustobe.santibailor.util.SearchPreferences;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.R;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private RicorrenzaViewModel viewModel;
    private RicorrenzaAdapter adapter;
    private RicorrenzaRepository repository;
    private boolean shouldClearSearch = false;

    private static final long SEARCH_DELAY_MS = 300;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private static final String PREFS_NAME = "SearchPreferences";
    private static final String PREF_NOME = "nome";
    private static final String PREF_TIPO = "tipo";
    private static final String PREF_DATA_INIZIO = "dataInizio";
    private static final String PREF_DATA_FINE = "dataFine";

    private String nome = "";
    private Integer tipo = null;
    private String dataInizio = "";
    private String dataFine = "";

    private final boolean isLoading = false;
    private final boolean isLastPage = false;
    private boolean isLoadingMore = false;

    private ArrayAdapter<TipoRicorrenza> tipoAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // L'app è stata appena avviata, ma non possiamo ancora chiamare clearSearchState()
            shouldClearSearch = true;
        }
        viewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Inizializzazione SearchFragment");
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getRisultatiRicerca().observe(getViewLifecycleOwner(), this::updateUIWithResults);


        SavedStateHandle handle = new SavedStateHandle();
        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                if (savedInstanceState.containsKey(key)) {
                    Object value = savedInstanceState.get(key);
                    if (value instanceof String) {
                        handle.set(key, (String) value);
                    } else if (value instanceof Integer) {
                        handle.set(key, (Integer) value);
                    } // Aggiungere altri tipi se necessario
                }
            }
        }

        viewModel = new ViewModelProvider(this).get(RicorrenzaViewModel.class);

        repository = new RicorrenzaRepository(requireActivity().getApplication());

        setupViews();
        setupRecyclerView();
        setupListeners();
        setupInfiniteScroll();
        setupTipoSpinner();

        // Osservazione dei risultati di ricerca
        viewModel.getRisultatiRicerca().observe(getViewLifecycleOwner(), this::updateUIWithResults);

        if (shouldClearSearch) {
            clearSearchState();
            shouldClearSearch = false;
        } else {
            ripristinaDatiRicerca();
        }

        viewModel.loadTotalItemCount();
        viewModel.getTotalItemCount().observe(getViewLifecycleOwner(), this::updateTotalCount);

        // Inizializza la UI con una lista vuota
        updateUIWithResults(new ArrayList<>());
    }

    private void setupListeners() {
        binding.buttonCerca.setOnClickListener(v -> {
            performSearch();
            hideKeyboard();
        });

        binding.editTextDataInizio.setOnClickListener(v -> {
            showDatePickerDialog(true);
            hideKeyboard();
        });

        binding.editTextDataFine.setOnClickListener(v -> {
            showDatePickerDialog(false);
            hideKeyboard();
        });

        binding.spinnerTipo.setOnItemClickListener((parent, view, position, id) -> {
            hideKeyboard();
        });

        // Rimuovi qualsiasi TextWatcher esistente su binding.editTextNome
    }

    private void updateTotalCount(Integer count) {
        binding.textViewTotalItems.setText("Totale item nel database: " + count);
    }

    private void setupViews() {
        Log.d(TAG, "setupViews: Configurazione delle viste");
        setupTipoSpinner();
        setupDatePickers();
        setupRecyclerView();
        setupSearchButton();
        setupSearchInput();
        binding.buttonDebug.setOnClickListener(v -> showDebugInfo());
    }

    private void setupTipoSpinner() {
        Log.d(TAG, "setupTipoSpinner called");
        ArrayAdapter<TipoRicorrenza> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        binding.spinnerTipo.setAdapter(adapter);

        viewModel.getAllTipiRicorrenza().observe(getViewLifecycleOwner(), tipi -> {
            Log.d(TAG, "Received tipi ricorrenza update. Size: " + (tipi != null ? tipi.size() : "null"));
            if (tipi != null && !tipi.isEmpty()) {
                adapter.clear();
                adapter.addAll(tipi);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Updated adapter with " + tipi.size() + " items");

                // Ripristina la selezione precedente se disponibile
                String savedTipo = SearchPreferences.getSavedTipo(requireContext());
                Log.d(TAG, "Saved tipo: " + savedTipo);
                if (!savedTipo.isEmpty()) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (adapter.getItem(i).getTipo().equals(savedTipo)) {
                            binding.spinnerTipo.setText(savedTipo, false);
                            viewModel.setSelectedTipo(adapter.getItem(i));
                            Log.d(TAG, "Restored saved tipo: " + savedTipo);
                            break;
                        }
                    }
                } else {
                    binding.spinnerTipo.setText(tipi.get(0).toString(), false);
                    viewModel.setSelectedTipo(tipi.get(0));
                    Log.d(TAG, "Set default tipo: " + tipi.get(0).getTipo());
                }
            } else {
                Log.w(TAG, "Received null or empty tipi ricorrenza list");
            }
        });

        binding.spinnerTipo.setOnItemClickListener((parent, view, position, id) -> {
            TipoRicorrenza selectedTipo = (TipoRicorrenza) parent.getItemAtPosition(position);
            viewModel.setSelectedTipo(selectedTipo);
            SearchPreferences.saveTipo(requireContext(), selectedTipo.getTipo());
        });
    }

    private void setupDatePickers() {
        binding.editTextDataInizio.setOnClickListener(v -> showDatePickerDialog(true));
        binding.editTextDataFine.setOnClickListener(v -> showDatePickerDialog(false));
    }

    private void setupRecyclerView() {
        adapter = new RicorrenzaAdapter(this::navigateToEditFragment, this::showDeleteConfirmationDialog, viewModel);
        binding.recyclerViewRisultati.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRisultati.setAdapter(adapter);

        viewModel.getRisultatiRicercaAvanzata().observe(getViewLifecycleOwner(), this::updateUIWithResults);
    }

    private void showDeleteConfirmationDialog(Ricorrenza ricorrenza) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Conferma cancellazione")
                .setMessage("Sei sicuro di voler cancellare questa ricorrenza?")
                .setPositiveButton("Sì", (dialog, which) -> deleteRicorrenza(ricorrenza))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteRicorrenza(Ricorrenza ricorrenza) {
        viewModel.deleteRicorrenza(ricorrenza);
    }


    private void navigateToEditFragment(int ricorrenzaId) {
        Log.d("SearchFragment", "Navigating to EditRicorrenzaFragment with ID: " + ricorrenzaId);
        if (ricorrenzaId > 0) {
            Bundle bundle = new Bundle();
            bundle.putInt("ricorrenzaId", ricorrenzaId);
            Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_editRicorrenzaFragment, bundle);
        } else {
            Toast.makeText(requireContext(), "ID ricorrenza non valido: " + ricorrenzaId, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSearchButton() {
        binding.buttonCerca.setOnClickListener(v -> {
            performSearch();
            salvaDatiRicerca();
        });
    }

    private void setupSearchInput() {
        binding.editTextNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> {
                    if (s.length() >= 3) {
                        performSearch();
                    } else if (s.length() == 0) {
                        clearResults();
                    }
                };
                searchHandler.postDelayed(searchRunnable, SEARCH_DELAY_MS);
            }
        });
    }

    private void updateSearchParamsSummary() {
        StringBuilder summary = new StringBuilder("Ricerca: ");
        if (!TextUtils.isEmpty(nome)) {
            summary.append("Nome: ").append(nome).append(", ");
        }
        TipoRicorrenza selectedTipo = viewModel.getSelectedTipo().getValue();
        if (selectedTipo != null) {
            summary.append("Tipo: ").append(selectedTipo.getTipo()).append(", ");
        }
        if (!TextUtils.isEmpty(dataInizio) || !TextUtils.isEmpty(dataFine)) {
            summary.append("Periodo: ").append(dataInizio).append(" - ").append(dataFine);
        }
        binding.searchParamsSummary.setText(summary.toString());
        binding.searchParamsSummary.setVisibility(View.VISIBLE);
    }

    private void setupInfiniteScroll() {
        binding.recyclerViewRisultati.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    Boolean isLoading = viewModel.getIsLoading().getValue();
                    Boolean isLastPage = viewModel.getIsLastPage().getValue();

                    if (isLoading != null && !isLoading && isLastPage != null && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= viewModel.getPageSize()) {
                            viewModel.loadNextPage();
                        }
                    }
                }
            }
        });
    }

    private void initializeSearch() {
        // Imposta valori di default se necessario
        if (binding.editTextDataInizio.getText().toString().isEmpty()) {
            binding.editTextDataInizio.setText("01/01");
        }
        if (binding.editTextDataFine.getText().toString().isEmpty()) {
            binding.editTextDataFine.setText("31/12");
        }
        performSearch();
    }

    private void performSearch() {
        viewModel.clearSearch();
        String nome = binding.editTextNome.getText().toString().trim();
        String dataInizio = (String) binding.editTextDataInizio.getTag();
        String dataFine = (String) binding.editTextDataFine.getTag();

        TipoRicorrenza selectedTipo = viewModel.getSelectedTipo().getValue();
        Integer tipo = (selectedTipo != null) ? selectedTipo.getId() : null;

        Log.d(TAG, "Performing search with params: nome=" + nome + ", tipo=" + tipo +
                ", dataInizio=" + dataInizio + ", dataFine=" + dataFine);

        updateSearchParamsSummary();

        // Utilizziamo il nuovo metodo setSearchParams invece di eseguiRicercaAvanzata
        viewModel.setSearchParams(nome, tipo, dataInizio, dataFine);

        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void observeViewModel() {
        viewModel.getRisultatiRicercaAvanzata().observe(getViewLifecycleOwner(), this::updateUIWithResults);
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), this::updateLoadingState);
        viewModel.getIsLastPage().observe(getViewLifecycleOwner(), isLast -> {
            // Aggiorna UI se necessario quando si raggiunge l'ultima pagina
        });
        viewModel.getTotalSearchResults().observe(getViewLifecycleOwner(), this::updateTotalResultsCount);
    }

    private void loadMoreItems() {
        isLoadingMore = true;
        viewModel.loadNextPage();
    }

    private void updateTotalResultsCount(int count) {
        binding.textViewRisultati.setText("Risultati trovati: " + count);
    }

    private void updateUIWithResults(List<Ricorrenza> risultati) {
        if (risultati == null) {
            risultati = new ArrayList<>();
        }
        for (Ricorrenza ricorrenza : risultati) {
            if (ricorrenza != null) {
                Log.d("SearchFragment", "Ricorrenza ID: " + ricorrenza.getId() + ", Nome: " + ricorrenza.getNome());
            }
        }
        adapter.setRicorrenze(risultati);
        binding.recyclerViewRisultati.setVisibility(risultati.isEmpty() ? View.GONE : View.VISIBLE);
        binding.textViewNoResults.setVisibility(risultati.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateLoadingState(boolean isLoading) {
        binding.loadingMoreProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        isLoadingMore = isLoading;
    }

    private void clearResults() {
        viewModel.clearRisultatiRicerca();
        updateUIWithResults(new ArrayList<>());
    }

    private void showDebugInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Debug Info")
                .setMessage("Parametri di ricerca:\n" +
                        "Nome: " + binding.editTextNome.getText() + "\n" +
                        "Tipo: " + binding.spinnerTipo.getText() + "\n" +
                        "Data Inizio: " + binding.editTextDataInizio.getText() + "\n" +
                        "Data Fine: " + binding.editTextDataFine.getText() + "\n\n" +
                        "Risultati: " + (adapter.getItemCount() > 0 ?
                        adapter.getItemCount() : "Nessun risultato"))
                .setPositiveButton("OK", null)
                .show();
    }

    private void showDatePickerDialog(boolean isStartDate) {
        MonthDayPickerHelper.showPicker(requireContext(), (month, day) -> {
            String displayDate = DateUtils.formatDate(day, month);
            String queryDate = String.format(Locale.getDefault(), "%02d/%02d", day, month);
            if (isStartDate) {
                binding.editTextDataInizio.setText(displayDate);
                binding.editTextDataInizio.setTag(queryDate);
            } else {
                binding.editTextDataFine.setText(displayDate);
                binding.editTextDataFine.setTag(queryDate);
            }
        });
    }

    private void salvaDatiRicerca() {
        SearchPreferences.saveSearchParams(
                requireContext(),
                binding.editTextNome.getText().toString(),
                binding.spinnerTipo.getText().toString(),
                binding.editTextDataInizio.getText().toString(),
                binding.editTextDataFine.getText().toString()
        );
    }

    private void ripristinaDatiRicerca() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        nome = prefs.getString(PREF_NOME, "");
        String tipoString = prefs.getString(PREF_TIPO, "Tutti");
        dataInizio = prefs.getString(PREF_DATA_INIZIO, "");
        dataFine = prefs.getString(PREF_DATA_FINE, "");

        binding.editTextNome.setText(nome);
        binding.editTextDataInizio.setText(dataInizio);
        binding.editTextDataFine.setText(dataFine);

        // Ripristina il tipo selezionato
        viewModel.getAllTipiRicorrenza().observe(getViewLifecycleOwner(), tipi -> {
            for (TipoRicorrenza tipo : tipi) {
                if (tipo.getTipo().equals(tipoString)) {
                    binding.spinnerTipo.setText(tipoString);
                    viewModel.setSelectedTipo(tipo);
                    break;
                }
            }
        });

        updateSearchParamsSummary();
    }

    private void clearSearchState() {
        SearchPreferences.clearSearchParams(requireContext());
        viewModel.clearSearchState();
        ripristinaDatiRicerca();
        updateUIWithResults(new ArrayList<>());
    }

    private Integer getTipoFromString(String tipo) {
        return switch (tipo) {
            case "Religiosa" -> 1;
            case "Laica" -> 2;
            case "Tutte" -> null;  // Usiamo null per indicare "Tutte"
            default -> null;
        };
    }

    private void onRicorrenzaClick(int ricorrenzaId) {
        // Implementa la navigazione ai dettagli della ricorrenza
        Toast.makeText(getContext(), "Clicked on ricorrenza with id: " + ricorrenzaId, Toast.LENGTH_SHORT).show();
    }

    private void onRicorrenzaDeleteClick(Ricorrenza ricorrenza) {
        // Implementa la logica per eliminare una ricorrenza
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Conferma eliminazione")
                .setMessage("Sei sicuro di voler eliminare questa ricorrenza?")
                .setPositiveButton("Sì", (dialog, which) -> {
                    viewModel.deleteRicorrenza(ricorrenza);
                    Toast.makeText(getContext(), "Ricorrenza eliminata", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nome", binding.editTextNome.getText().toString());
        outState.putString("tipo", binding.spinnerTipo.getText().toString());
        outState.putString("dataInizio", binding.editTextDataInizio.getText().toString());
        outState.putString("dataFine", binding.editTextDataFine.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            binding.editTextNome.setText(savedInstanceState.getString("nome", ""));
            binding.spinnerTipo.setText(savedInstanceState.getString("tipo", "Tutti"));
            binding.editTextDataInizio.setText(savedInstanceState.getString("dataInizio", ""));
            binding.editTextDataFine.setText(savedInstanceState.getString("dataFine", ""));
        }
    }

    private void clearSearchFields() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Azzera tutti i campi nelle SharedPreferences
        editor.putString(PREF_NOME, "");
        editor.putString(PREF_TIPO, "Tutti");
        editor.putString(PREF_DATA_INIZIO, "");
        editor.putString(PREF_DATA_FINE, "");

        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        viewModel.loadTipiRicorrenza();
        //ripristinaDatiRicerca();
    }

    @Override
    public void onPause() {
        super.onPause();
        salvaDatiRicerca();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchHandler.removeCallbacksAndMessages(null);
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearSearchFields();
    }
}