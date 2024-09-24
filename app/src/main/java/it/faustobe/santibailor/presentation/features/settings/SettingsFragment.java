package it.faustobe.santibailor.presentation.features.settings;

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
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import it.faustobe.santibailor.R;
import it.faustobe.santibailor.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

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
        setupListeners();
        setupMenu();
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
        ricorrenzeSettings.add(new SettingItem("Gestione Ricorrenze", "Aggiungi, modifica o elimina ricorrenze", R.id.action_categorySettingsFragment_to_manageRicorrenzeFragment));
        ricorrenzeSettings.add(new SettingItem("Visualizzazione", "Imposta le preferenze di visualizzazione", R.id.action_categorySettingsFragment_to_ricorrenzeVisualizationSettingsFragment));
        ricorrenzeSettings.add(new SettingItem("Notifiche Ricorrenze", "Attiva o disattiva le notifiche", true ));
        ricorrenzeSettings.add(new SettingItem("Sincronizzazione calendario", "Sincronizza con calendari esterni"));

        SettingItem[] settingItemsArray = ricorrenzeSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings("Impostazioni Ricorrenze", settingItemsArray);
    }

    private void navigateToGeneralSettings() {
        List<SettingItem> generalSettings = new ArrayList<>();
        generalSettings.add(new SettingItem("Tema", "Seleziona il tema dell'app"));
        generalSettings.add(new SettingItem("Lingua", "Scegli la lingua dell'app"));
        generalSettings.add(new SettingItem("Notifiche", "Gestisci le notifiche push"));
        generalSettings.add(new SettingItem("Font e dimensione del testo", "Personalizza il carattere e la dimensione"));
        generalSettings.add(new SettingItem("Sincronizzazione", "Opzioni per sincronizzare i dati" ));
        generalSettings.add(new SettingItem("Backup e ripristino", "Gestisci backup e ripristino dei dati"));

        SettingItem[] settingItemsArray = generalSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings("Impostazioni Generali", settingItemsArray);
    }

    private void navigateToAccountSettings() {
        List<SettingItem> accountSettings = new ArrayList<>();
        accountSettings.add(new SettingItem("Profilo utente", "Modifica le informazioni personali" ));
        accountSettings.add(new SettingItem("Gestione account", "Collega o disconnetti account" ));
        accountSettings.add(new SettingItem("Cambio password", "Modifica la password o configura l'autenticazione" ));
        accountSettings.add(new SettingItem("Esporta dati", "Esporta le informazioni personali" ));
        accountSettings.add(new SettingItem("Elimina account", "Elimina definitivamente l'account" ));

        SettingItem[] settingItemsArray = accountSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings("Impostazioni Account", settingItemsArray);
    }

    private void navigateToImpegniSettings() {
        List<SettingItem> impegniSettings = new ArrayList<>();
        impegniSettings.add(new SettingItem("Visualizzazione impegni", "Scegli il formato di visualizzazione" ));
        impegniSettings.add(new SettingItem("Promemoria per impegni", "Gestisci i promemoria" ));
        impegniSettings.add(new SettingItem("Categorie impegni", "Crea e gestisci categorie personalizzate" ));
        impegniSettings.add(new SettingItem("Condivisione", "Opzioni per condividere gli impegni" ));
        impegniSettings.add(new SettingItem("Durata predefinita", "Imposta una durata standard" ));

        SettingItem[] settingItemsArray = impegniSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings("Impostazioni Impegni", settingItemsArray);
    }

    private void navigateToListeSpesaSettings() {
        List<SettingItem> listeSpesaSettings = new ArrayList<>();
        listeSpesaSettings.add(new SettingItem("Ordine della lista", "Scegli l'ordinamento della lista" ));
        listeSpesaSettings.add(new SettingItem("Suggerimenti intelligenti", "Attiva/disattiva i suggerimenti", true ));
        listeSpesaSettings.add(new SettingItem("Condivisione liste", "Opzioni per condividere le liste" ));
        listeSpesaSettings.add(new SettingItem("Salvataggio liste preferite", "Crea e salva liste ricorrenti" ));
        listeSpesaSettings.add(new SettingItem("Notifiche di promemoria", "Gestisci gli avvisi per le liste", true ));

        SettingItem[] settingItemsArray = listeSpesaSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings("Impostazioni Liste Spesa", settingItemsArray);
    }

    private void navigateToNotificheSettings() {
        List<SettingItem> notificheSettings = new ArrayList<>();
        notificheSettings.add(new SettingItem("Notifiche per Ricorrenze", "Imposta le notifiche per eventi ricorrenti" ));
        notificheSettings.add(new SettingItem("Notifiche per Impegni", "Gestisci i promemoria per gli impegni" ));
        notificheSettings.add(new SettingItem("Notifiche silenziose", "Configura la modalità non disturbare" ));
        notificheSettings.add(new SettingItem("Snooze delle notifiche", "Opzioni per posticipare le notifiche" ));

        SettingItem[] settingItemsArray = notificheSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings("Impostazioni Notifiche", settingItemsArray);
    }

    private void navigateToIntegrazioniSettings() {
        List<SettingItem> integrazioniSettings = new ArrayList<>();
        integrazioniSettings.add(new SettingItem("Integrazione con Calendari", "Collega calendari esterni" ));
        integrazioniSettings.add(new SettingItem("Integrazione con Assistenti Vocali", "Collega assistenti vocali" ));
        integrazioniSettings.add(new SettingItem("Integrazione con App di Terze Parti", "Collega app per la gestione delle attività" ));

        SettingItem[] settingItemsArray = integrazioniSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings("Impostazioni Integrazioni", settingItemsArray);
    }

    private void navigateToPrivacySicurezzaSettings() {
        List<SettingItem> privacySicurezzaSettings = new ArrayList<>();
        privacySicurezzaSettings.add(new SettingItem("Blocco dell'app", "Attiva blocco con PIN o biometrico" ));
        privacySicurezzaSettings.add(new SettingItem("Permessi", "Gestisci i permessi dell'app" ));
        privacySicurezzaSettings.add(new SettingItem("Cronologia dati", "Visualizza e gestisci la cronologia" ));
        privacySicurezzaSettings.add(new SettingItem("Crittografia dati", "Abilita la crittografia per dati sensibili" ));

        SettingItem[] settingItemsArray = privacySicurezzaSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings("Impostazioni Privacy & Sicurezza", settingItemsArray);
    }

    private void navigateToSupportoInfoSettings() {
        List<SettingItem> supportoInfoSettings = new ArrayList<>();
        supportoInfoSettings.add(new SettingItem("Contatti e Supporto", "Accedi all'assistenza" ));
        supportoInfoSettings.add(new SettingItem("Domande frequenti (FAQ)", "Visualizza le domande comuni" ));
        supportoInfoSettings.add(new SettingItem("Versione dell'app", "Informazioni sulla versione" ));
        supportoInfoSettings.add(new SettingItem("Feedback", "Invia feedback o suggerimenti" ));

        SettingItem[] settingItemsArray = supportoInfoSettings.toArray(new SettingItem[0]);

        navigateToCategorySettings("info & SUpporto", settingItemsArray);
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