package it.faustobe.santibailor.presentation.features.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentSearchResultsBinding;
import it.faustobe.santibailor.presentation.common.viewmodels.RicorrenzaViewModel;

public class SearchResultsFragment extends Fragment {

    private FragmentSearchResultsBinding binding;
    private RicorrenzaViewModel viewModel;
    private SearchResultsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(RicorrenzaViewModel.class);

        setupHeader();
        setupRecyclerView();
        observeViewModel();
    }

    private void setupHeader() {
        SearchResultsFragmentArgs args = SearchResultsFragmentArgs.fromBundle(getArguments());
        String searchName = args.getSearchName();
        String dateStart = args.getSearchDateStart();
        String dateEnd = args.getSearchDateEnd();

        if (searchName != null && !searchName.isEmpty()) {
            binding.textViewSearchName.setText(getString(R.string.search_results_for, searchName));
        } else {
            binding.textViewSearchName.setText(getString(R.string.search_results_for, "*"));
        }

        if (dateStart != null && !dateStart.isEmpty() && dateEnd != null && !dateEnd.isEmpty()) {
            binding.textViewSearchPeriod.setText(getString(R.string.search_period, dateStart, dateEnd));
            binding.textViewSearchPeriod.setVisibility(View.VISIBLE);
        } else if (dateStart != null && !dateStart.isEmpty()) {
            binding.textViewSearchPeriod.setText(getString(R.string.search_period, dateStart, "..."));
            binding.textViewSearchPeriod.setVisibility(View.VISIBLE);
        } else if (dateEnd != null && !dateEnd.isEmpty()) {
            binding.textViewSearchPeriod.setText(getString(R.string.search_period, "...", dateEnd));
            binding.textViewSearchPeriod.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        adapter = new SearchResultsAdapter(ricorrenza -> {
            SearchResultsFragmentDirections.ActionSearchResultsFragmentToRicorrenzaDetailFragment action =
                    SearchResultsFragmentDirections.actionSearchResultsFragmentToRicorrenzaDetailFragment(ricorrenza.getId());
            Navigation.findNavController(requireView()).navigate(action);
        });
        binding.recyclerViewResults.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getRisultatiRicercaAvanzata().observe(getViewLifecycleOwner(), ricorrenze -> {
            binding.progressBar.setVisibility(View.GONE);
            if (ricorrenze != null && !ricorrenze.isEmpty()) {
                adapter.submitList(ricorrenze);
                binding.recyclerViewResults.setVisibility(View.VISIBLE);
                binding.textViewNoResults.setVisibility(View.GONE);
            } else {
                binding.recyclerViewResults.setVisibility(View.GONE);
                binding.textViewNoResults.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getTotalSearchResults().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.textViewResultCount.setText(getString(R.string.search_results_count, count));
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
