package it.faustobe.santibailor.presentation.features.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentSearchBinding;
import it.faustobe.santibailor.domain.model.TipoRicorrenza;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;
import it.faustobe.santibailor.util.DateUtils;
import it.faustobe.santibailor.util.MonthDayPickerHelper;
import it.faustobe.santibailor.util.SearchPreferences;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private RicorrenzaViewModel viewModel;
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

    private ArrayAdapter<TipoRicorrenza> tipoAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            shouldClearSearch = true;
        }
        viewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();
        setupSearchView();
        setupListeners();
        setupTipoSpinner();
        observeViewModel();

        if (shouldClearSearch) {
            clearSearchState();
            shouldClearSearch = false;
        } else {
            ripristinaDatiRicerca();
        }

        viewModel.loadTotalItemCount();
    }

    private void setupViews() {
        binding.buttonDebug.setOnClickListener(v -> showDebugInfo());
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> {
                    if (newText.length() >= 3) {
                        performSearch(newText);
                    }
                };
                searchHandler.postDelayed(searchRunnable, SEARCH_DELAY_MS);
                return true;
            }
        });
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
    }

    private void setupTipoSpinner() {
        tipoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        binding.spinnerTipo.setAdapter(tipoAdapter);

        viewModel.getAllTipiRicorrenza().observe(getViewLifecycleOwner(), tipi -> {
            if (tipi != null && !tipi.isEmpty()) {
                List<TipoRicorrenza> tipiConTutte = new ArrayList<>();
                tipiConTutte.add(new TipoRicorrenza(0, "Tutte"));
                tipiConTutte.addAll(tipi);

                tipoAdapter.clear();
                tipoAdapter.addAll(tipiConTutte);
                tipoAdapter.notifyDataSetChanged();

                String savedTipo = SearchPreferences.getSavedTipo(requireContext());
                if (!savedTipo.isEmpty()) {
                    for (int i = 0; i < tipoAdapter.getCount(); i++) {
                        if (tipoAdapter.getItem(i).getTipo().equals(savedTipo)) {
                            binding.spinnerTipo.setText(savedTipo, false);
                            viewModel.setSelectedTipo(tipoAdapter.getItem(i));
                            break;
                        }
                    }
                } else {
                    binding.spinnerTipo.setText("Tutte", false);
                    viewModel.setSelectedTipo(new TipoRicorrenza(0, "Tutte"));
                }
            }
        });

        binding.spinnerTipo.setOnItemClickListener((parent, view, position, id) -> {
            TipoRicorrenza selectedTipo = (TipoRicorrenza) parent.getItemAtPosition(position);
            viewModel.setSelectedTipo(selectedTipo);
            SearchPreferences.saveTipo(requireContext(), selectedTipo.getTipo());
        });
    }

    private void observeViewModel() {
        viewModel.getTotalItemCount().observe(getViewLifecycleOwner(), this::updateTotalCount);
    }

    private void performSearch(String query) {
        // SearchView quick search: set as nome and navigate to results
        viewModel.setSearchParams(query, null, null, null);
        navigateToResults(query, "", "");
    }

    private void performSearch() {
        String nome = binding.editTextNome.getText().toString().trim();
        String dataInizio = (String) binding.editTextDataInizio.getTag();
        String dataFine = (String) binding.editTextDataFine.getTag();

        TipoRicorrenza selectedTipo = viewModel.getSelectedTipo().getValue();
        Integer tipo = (selectedTipo != null && selectedTipo.getId() != 0) ? selectedTipo.getId() : null;

        viewModel.setSearchParams(nome, tipo, dataInizio, dataFine);

        String displayDateStart = binding.editTextDataInizio.getText().toString();
        String displayDateEnd = binding.editTextDataFine.getText().toString();
        navigateToResults(nome, displayDateStart, displayDateEnd);
    }

    private void navigateToResults(String searchName, String dateStart, String dateEnd) {
        SearchFragmentDirections.ActionSearchFragmentToSearchResultsFragment action =
                SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment();
        action.setSearchName(searchName != null ? searchName : "");
        action.setSearchDateStart(dateStart != null ? dateStart : "");
        action.setSearchDateEnd(dateEnd != null ? dateEnd : "");
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void updateTotalCount(Integer count) {
        binding.textViewTotalItems.setText("Totale item nel database: " + count);
    }

    private void showDebugInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.debug_info)
                .setMessage("Parametri di ricerca:\n" +
                        "Nome (campo): " + binding.editTextNome.getText() + "\n" +
                        "SearchView: " + binding.searchView.getQuery() + "\n" +
                        "Tipo: " + binding.spinnerTipo.getText() + "\n" +
                        "Data Inizio: " + binding.editTextDataInizio.getText() +
                        " (tag: " + binding.editTextDataInizio.getTag() + ")\n" +
                        "Data Fine: " + binding.editTextDataFine.getText() +
                        " (tag: " + binding.editTextDataFine.getTag() + ")")
                .setPositiveButton(android.R.string.ok, null)
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

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadTipiRicorrenza();
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
}
