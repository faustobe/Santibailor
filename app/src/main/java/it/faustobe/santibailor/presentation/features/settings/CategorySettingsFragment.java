package it.faustobe.santibailor.presentation.features.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentCategorySettingsBinding;
import it.faustobe.santibailor.util.LanguageManager;
import it.faustobe.santibailor.util.ThemeManager;

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
            Log.e("CategorySettingsFragment", "Navigation error: " + e.getMessage());
            Toast.makeText(requireContext(), getString(R.string.navigation_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void performAction(SettingItem item) {
        String title = item.getTitle();

        if (getString(R.string.settings_general_theme_title).equals(title)) {
            showThemeDialog();
        } else if (getString(R.string.settings_general_language_title).equals(title)) {
            showLanguageDialog();
        } else {
            Log.d("CategorySettingsFragment", "Action executed for: " + title);
            Toast.makeText(requireContext(), getString(R.string.feature_in_development), Toast.LENGTH_SHORT).show();
        }
    }

    private void showThemeDialog() {
        String currentTheme = ThemeManager.getSavedTheme(requireContext());

        String[] themes = {
                ThemeManager.THEME_LIGHT,
                ThemeManager.THEME_DARK,
                ThemeManager.THEME_SYSTEM
        };

        String[] themeNames = {
                getString(R.string.settings_theme_light),
                getString(R.string.settings_theme_dark),
                getString(R.string.settings_theme_system)
        };

        int selectedIndex = Arrays.asList(themes).indexOf(currentTheme);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.select_theme)
                .setSingleChoiceItems(themeNames, selectedIndex, (dialog, which) -> {
                    String selectedTheme = themes[which];
                    ThemeManager.saveTheme(requireContext(), selectedTheme);
                    ThemeManager.applyTheme(selectedTheme);
                    Toast.makeText(requireContext(), getString(R.string.theme_applied), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showLanguageDialog() {
        String currentLanguage = LanguageManager.getSavedLanguage(requireContext());

        String[] languages = {
                LanguageManager.LANGUAGE_ITALIAN,
                LanguageManager.LANGUAGE_ENGLISH,
                LanguageManager.LANGUAGE_SYSTEM
        };

        String[] languageNames = {
                getString(R.string.settings_language_italian),
                getString(R.string.settings_language_english),
                getString(R.string.settings_language_system)
        };

        int selectedIndex = Arrays.asList(languages).indexOf(currentLanguage);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.select_language)
                .setSingleChoiceItems(languageNames, selectedIndex, (dialog, which) -> {
                    String selectedLanguage = languages[which];
                    LanguageManager.saveLanguage(requireContext(), selectedLanguage);
                    LanguageManager.applyLanguage(requireContext(), selectedLanguage);
                    Toast.makeText(requireContext(), getString(R.string.language_applied_restart), Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                    // Ricrea l'activity per applicare la lingua
                    requireActivity().recreate();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}