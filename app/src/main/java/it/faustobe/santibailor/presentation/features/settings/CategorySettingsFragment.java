package it.faustobe.santibailor.presentation.features.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentCategorySettingsBinding;

public class CategorySettingsFragment extends Fragment implements CategorySettingsAdapter.OnSettingItemClickListener {
    private FragmentCategorySettingsBinding binding;
    private CategorySettingsAdapter adapter;
    private List<SettingItem> settingItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategorySettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CategorySettingsFragmentArgs args = CategorySettingsFragmentArgs.fromBundle(getArguments());
        String categoryTitle = args.getCategoryTitle();
        SettingItem[] settingItemsArray = args.getSettingItems();

        binding.categoryTitle.setText(categoryTitle);

        if (settingItemsArray != null && settingItemsArray.length > 0) {
            settingItems = Arrays.asList(settingItemsArray);
            setupRecyclerView(settingItems);
        } else {
            Log.e("CategorySettingsFragment", "Nessun SettingItem fornito");
        }

        setupBackButton();
    }

    private void setupRecyclerView(List<SettingItem> settingItems) {
        adapter = new CategorySettingsAdapter(settingItems, this);
        binding.settingsRecyclerView.setAdapter(adapter);
        binding.settingsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupBackButton() {
        binding.btnBackToSettings.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_categorySettingsFragment_to_navigation_settings)
        );
    }

    @Override
    public void onSettingItemClick(SettingItem item) {
        switch (item.getType()) {
            case NAVIGATION:
                Integer navigationAction = item.getNavigationAction();
                if (navigationAction != null && navigationAction != 0) {
                    navigateToDestination(navigationAction);
                }
                break;
            case TOGGLE:
                boolean newState = !item.isToggleState();
                item.setToggleState(newState);
                // Qui potresti voler aggiornare lo stato nel ViewModel o nel repository
                break;
            case ACTION:
                performAction(item);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void navigateToDestination(int destinationId) {
        NavController navController = Navigation.findNavController(requireView());
        try {
            navController.navigate(destinationId);
        } catch (Exception e) {
            Log.e("CategorySettingsFragment", "Errore di navigazione: " + e.getMessage());
            Toast.makeText(requireContext(), "Errore nella navigazione", Toast.LENGTH_SHORT).show();
        }
    }

    private void performAction(SettingItem item) {
        // Implementa azioni specifiche qui
        Log.d("CategorySettingsFragment", "Azione eseguita per: " + item.getTitle());
        // Esempio: Toast.makeText(requireContext(), "Azione eseguita: " + item.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}