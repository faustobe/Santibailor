package it.faustobe.santibailor.presentation.features.settings;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentSettingsBinding;
import it.faustobe.santibailor.data.remote.FirebaseRemoteDataSource;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    @Inject
    FirebaseRemoteDataSource firebaseRemoteDataSource;

    public SettingsFragment() {
        // Costruttore vuoto richiesto da Fragment
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBack();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        setupFirebaseStatusIndicator();
        setupListeners();
        setupMenu();

        // Verifica connessione Firebase all'avvio
        firebaseRemoteDataSource.checkConnection();
    }

    private void setupToolbar() {
        if (getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.title_settings);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void setupFirebaseStatusIndicator() {
        firebaseRemoteDataSource.getConnectionState().observe(getViewLifecycleOwner(), isConnected -> {
            int color = isConnected ?
                    ContextCompat.getColor(requireContext(), R.color.success_green) :
                    ContextCompat.getColor(requireContext(), R.color.error_red);

            binding.firebaseStatusIndicator.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(color));

            // Log can remain in English for debugging
            String status = isConnected ? "Connected to Firebase" : "Offline";
            Log.d(TAG, status);
        });
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                // Se hai un menu specifico per le impostazioni, inflalo qui
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    navigateBack();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }

    private void setupListeners() {
        binding.btnRicorrenze.setOnClickListener(v -> navigateToRicorrenzeSettings());
        binding.btnGenerali.setOnClickListener(v -> navigateToGeneralSettings());
        binding.btnAccount.setOnClickListener(v -> navigateToAccountSettings());
        binding.btnImpegni.setOnClickListener(v -> navigateToImpegniSettings());
        binding.btnListeSpesa.setOnClickListener(v -> navigateToListeSpesaSettings());
        binding.btnNotifiche.setOnClickListener(v -> navigateToNotificheSettings());
        binding.btnIntegrazioni.setOnClickListener(v -> navigateToIntegrazioniSettings());
        binding.btnPrivacySicurezza.setOnClickListener(v -> navigateToPrivacySicurezzaSettings());
        binding.btnSupportoInfo.setOnClickListener(v -> navigateToSupportoInfoSettings());
    }

    private void navigateToRicorrenzeSettings() {
        List<SettingItem> ricorrenzeSettings = new ArrayList<>();
        ricorrenzeSettings.add(new SettingItem(getString(R.string.settings_ricorrenze_manage_title), getString(R.string.settings_ricorrenze_manage_desc), R.id.action_categorySettingsFragment_to_manageRicorrenzeFragment));
        ricorrenzeSettings.add(new SettingItem(getString(R.string.settings_ricorrenze_search_title), getString(R.string.settings_ricorrenze_search_desc), R.id.action_categorySettingsFragment_to_searchFragment));
        ricorrenzeSettings.add(new SettingItem(getString(R.string.settings_ricorrenze_add_title), getString(R.string.settings_ricorrenze_add_desc), R.id.action_categorySettingsFragment_to_addItemFragment));
        ricorrenzeSettings.add(new SettingItem(getString(R.string.settings_ricorrenze_notifications_title), getString(R.string.settings_ricorrenze_notifications_desc), true));
        ricorrenzeSettings.add(new SettingItem(getString(R.string.settings_ricorrenze_sync_title), getString(R.string.settings_ricorrenze_sync_desc)));

        SettingItem[] settingItemsArray = ricorrenzeSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings(getString(R.string.settings_ricorrenze_category), settingItemsArray);
    }

    private void navigateToGeneralSettings() {
        List<SettingItem> generalSettings = new ArrayList<>();
        generalSettings.add(new SettingItem(getString(R.string.settings_general_theme_title), getString(R.string.settings_general_theme_desc)));
        generalSettings.add(new SettingItem(getString(R.string.settings_general_language_title), getString(R.string.settings_general_language_desc)));
        generalSettings.add(new SettingItem(getString(R.string.settings_general_notifications_title), getString(R.string.settings_general_notifications_desc)));
        generalSettings.add(new SettingItem(getString(R.string.settings_general_font_title), getString(R.string.settings_general_font_desc)));
        generalSettings.add(new SettingItem(getString(R.string.settings_general_sync_title), getString(R.string.settings_general_sync_desc)));
        generalSettings.add(new SettingItem(getString(R.string.settings_general_backup_title), getString(R.string.settings_general_backup_desc)));

        SettingItem[] settingItemsArray = generalSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings(getString(R.string.settings_general_category), settingItemsArray);
    }

    private void navigateToAccountSettings() {
        List<SettingItem> accountSettings = new ArrayList<>();
        accountSettings.add(new SettingItem(getString(R.string.settings_account_profile_title), getString(R.string.settings_account_profile_desc)));
        accountSettings.add(new SettingItem(getString(R.string.settings_account_manage_title), getString(R.string.settings_account_manage_desc)));
        accountSettings.add(new SettingItem(getString(R.string.settings_account_password_title), getString(R.string.settings_account_password_desc)));
        accountSettings.add(new SettingItem(getString(R.string.settings_account_export_title), getString(R.string.settings_account_export_desc)));
        accountSettings.add(new SettingItem(getString(R.string.settings_account_delete_title), getString(R.string.settings_account_delete_desc)));

        SettingItem[] settingItemsArray = accountSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings(getString(R.string.settings_account_category), settingItemsArray);
    }

    private void navigateToImpegniSettings() {
        List<SettingItem> impegniSettings = new ArrayList<>();
        impegniSettings.add(new SettingItem(getString(R.string.settings_impegni_view_title), getString(R.string.settings_impegni_view_desc)));
        impegniSettings.add(new SettingItem(getString(R.string.settings_impegni_reminders_title), getString(R.string.settings_impegni_reminders_desc)));
        impegniSettings.add(new SettingItem(getString(R.string.settings_impegni_categories_title), getString(R.string.settings_impegni_categories_desc)));
        impegniSettings.add(new SettingItem(getString(R.string.settings_impegni_sharing_title), getString(R.string.settings_impegni_sharing_desc)));
        impegniSettings.add(new SettingItem(getString(R.string.settings_impegni_duration_title), getString(R.string.settings_impegni_duration_desc)));

        SettingItem[] settingItemsArray = impegniSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings(getString(R.string.settings_impegni_category), settingItemsArray);
    }

    private void navigateToListeSpesaSettings() {
        List<SettingItem> listeSpesaSettings = new ArrayList<>();
        listeSpesaSettings.add(new SettingItem(getString(R.string.settings_liste_categories_title), getString(R.string.settings_liste_categories_desc)));
        listeSpesaSettings.add(new SettingItem(getString(R.string.settings_liste_frequent_title), getString(R.string.settings_liste_frequent_desc)));
        listeSpesaSettings.add(new SettingItem(getString(R.string.settings_liste_units_title), getString(R.string.settings_liste_units_desc)));

        SettingItem[] settingItemsArray = listeSpesaSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings(getString(R.string.settings_liste_category), settingItemsArray);
    }

    private void navigateToNotificheSettings() {
        List<SettingItem> notificheSettings = new ArrayList<>();
        notificheSettings.add(new SettingItem(getString(R.string.settings_notifications_ricorrenze_title), getString(R.string.settings_notifications_ricorrenze_desc)));
        notificheSettings.add(new SettingItem(getString(R.string.settings_notifications_impegni_title), getString(R.string.settings_notifications_impegni_desc)));
        notificheSettings.add(new SettingItem(getString(R.string.settings_notifications_silent_title), getString(R.string.settings_notifications_silent_desc)));
        notificheSettings.add(new SettingItem(getString(R.string.settings_notifications_snooze_title), getString(R.string.settings_notifications_snooze_desc)));

        SettingItem[] settingItemsArray = notificheSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings(getString(R.string.settings_notifications_category), settingItemsArray);
    }

    private void navigateToIntegrazioniSettings() {
        List<SettingItem> integrazioniSettings = new ArrayList<>();
        integrazioniSettings.add(new SettingItem(getString(R.string.settings_integrations_calendar_title), getString(R.string.settings_integrations_calendar_desc)));
        integrazioniSettings.add(new SettingItem(getString(R.string.settings_integrations_voice_title), getString(R.string.settings_integrations_voice_desc)));
        integrazioniSettings.add(new SettingItem(getString(R.string.settings_integrations_third_party_title), getString(R.string.settings_integrations_third_party_desc)));

        SettingItem[] settingItemsArray = integrazioniSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings(getString(R.string.settings_integrations_category), settingItemsArray);
    }

    private void navigateToPrivacySicurezzaSettings() {
        List<SettingItem> privacySicurezzaSettings = new ArrayList<>();
        privacySicurezzaSettings.add(new SettingItem(getString(R.string.settings_privacy_lock_title), getString(R.string.settings_privacy_lock_desc)));
        privacySicurezzaSettings.add(new SettingItem(getString(R.string.settings_privacy_permissions_title), getString(R.string.settings_privacy_permissions_desc)));
        privacySicurezzaSettings.add(new SettingItem(getString(R.string.settings_privacy_history_title), getString(R.string.settings_privacy_history_desc)));
        privacySicurezzaSettings.add(new SettingItem(getString(R.string.settings_privacy_encryption_title), getString(R.string.settings_privacy_encryption_desc)));

        SettingItem[] settingItemsArray = privacySicurezzaSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings(getString(R.string.settings_privacy_category), settingItemsArray);
    }

    private void navigateToSupportoInfoSettings() {
        List<SettingItem> supportoInfoSettings = new ArrayList<>();
        supportoInfoSettings.add(new SettingItem(getString(R.string.settings_support_contact_title), getString(R.string.settings_support_contact_desc)));
        supportoInfoSettings.add(new SettingItem(getString(R.string.settings_support_faq_title), getString(R.string.settings_support_faq_desc)));
        supportoInfoSettings.add(new SettingItem(getString(R.string.settings_support_version_title), getString(R.string.settings_support_version_desc)));
        supportoInfoSettings.add(new SettingItem(getString(R.string.settings_support_feedback_title), getString(R.string.settings_support_feedback_desc)));

        SettingItem[] settingItemsArray = supportoInfoSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings(getString(R.string.settings_support_category), settingItemsArray);
    }

    private void navigateToCategorySettings(String categoryTitle, SettingItem[] settingItems) {
        try {
            SettingsFragmentDirections.ActionSettingsFragmentToCategorySettingsFragment action =
                    SettingsFragmentDirections.actionSettingsFragmentToCategorySettingsFragment(
                            categoryTitle,
                            settingItems
                    );
            Navigation.findNavController(requireView()).navigate(action);
        } catch (Exception e) {
            Log.e("SettingsFragment", "Errore nella navigazione con Safe Args: " + e.getMessage());
            navigateToCategorySettingsFallback(categoryTitle, settingItems);
        }
    }

    private void navigateToCategorySettingsFallback(String categoryTitle, SettingItem[] settingItems) {
        Bundle args = new Bundle();
        args.putString("categoryTitle", categoryTitle);
        args.putParcelableArray("settingItems", settingItems);

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_settingsFragment_to_categorySettingsFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}