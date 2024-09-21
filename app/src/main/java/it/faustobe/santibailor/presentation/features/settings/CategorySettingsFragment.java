package it.faustobe.santibailor.presentation.features.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentCategorySettingsBinding;

public class CategorySettingsFragment extends Fragment implements CategorySettingsAdapter.OnSettingItemClickListener {
    private FragmentCategorySettingsBinding binding;
    private CategorySettingsAdapter adapter;

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
            List<SettingItem> settingItems = Arrays.asList(settingItemsArray);
            setupRecyclerView(settingItems);
        } else {
            Log.e("CategorySettingsFragment", "Nessun SettingItem fornito");
        }

        setupBackButton();
    }

    private void setupBackButton() {
        binding.btnBackToSettings.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_categorySettingsFragment_to_navigation_settings)
        );
    }

    private void setupRecyclerView(List<SettingItem> settingItems) {
        adapter = new CategorySettingsAdapter(settingItems, this);
        binding.settingsRecyclerView.setAdapter(adapter);
        binding.settingsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onSettingItemClick(SettingItem item) {
        switch (item.getType()) {
            case NAVIGATION:
                Integer navigationAction = item.getNavigationAction();
                if (navigationAction != null) {
                    Navigation.findNavController(requireView()).navigate(navigationAction);
                } else {
                    Log.e("CategorySettingsFragment", "Azione di navigazione non impostata per " + item.getTitle());
                }
                break;
            case TOGGLE:
                item.setToggleState(!item.isToggleState());
                // Qui potresti voler salvare lo stato del toggle
                break;
            case ACTION:
                performAction(item);
                break;
        }
    }

    private void performAction(SettingItem item) {
        // Implementa azioni specifiche qui
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
