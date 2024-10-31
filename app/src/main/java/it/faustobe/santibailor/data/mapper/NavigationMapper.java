package it.faustobe.santibailor.data.mapper;

import java.util.HashMap;
import java.util.Map;

import it.faustobe.santibailor.R;

public class NavigationMapper {
    private static final Map<Integer, Integer> destinationToActionMap = new HashMap<>();

    static {
        initializeMap();
    }

    private static void initializeMap() {
        // Mappatura per HomeFragment
        destinationToActionMap.put(R.id.navigation_home, R.id.action_global_navigation_home);
        destinationToActionMap.put(R.id.ricorrenzaDetailFragment, R.id.action_homeFragment_to_ricorrenzaDetailFragment);

        // Mappatura per RicorrenzaDetailFragment
        destinationToActionMap.put(R.id.editRicorrenzaFragment, R.id.action_ricorrenzaDetailFragment_to_editRicorrenzaFragment);

        // Mappatura per EditRicorrenzaFragment
        destinationToActionMap.put(R.id.navigation_home, R.id.action_editRicorrenzaFragment_to_navigation_home);

        // Mappatura per SettingsFragment
        //destinationToActionMap.put(R.id.addItemFragment, R.id.action_settingsFragment_to_addItemFragment);
        destinationToActionMap.put(R.id.searchFragment, R.id.action_settingsFragment_to_searchFragment);
        destinationToActionMap.put(R.id.categorySettingsFragment, R.id.action_settingsFragment_to_categorySettingsFragment);

        // Mappatura per CategorySettingsFragment
        destinationToActionMap.put(R.id.manageRicorrenzeFragment, R.id.action_categorySettingsFragment_to_manageRicorrenzeFragment);
        destinationToActionMap.put(R.id.ricorrenzeVisualizationSettingsFragment, R.id.action_categorySettingsFragment_to_ricorrenzeVisualizationSettingsFragment);
        destinationToActionMap.put(R.id.navigation_settings, R.id.action_categorySettingsFragment_to_navigation_settings);

        // Mappatura per ManageRicorrenzeFragment
        destinationToActionMap.put(R.id.addItemFragment, R.id.action_manageRicorrenzeFragment_to_addItemFragment);
        destinationToActionMap.put(R.id.searchFragment, R.id.action_manageRicorrenzeFragment_to_searchFragment);

        // Mappatura per SearchFragment
        destinationToActionMap.put(R.id.editRicorrenzaFragment, R.id.action_searchFragment_to_editRicorrenzaFragment);

        // Mappatura per AddItemFragment
        destinationToActionMap.put(R.id.navigation_home, R.id.action_addItemFragment_to_navigation_home);
        destinationToActionMap.put(R.id.navigation_settings, R.id.action_addItemFragment_to_navigation_settings);

        // Azione globale
        destinationToActionMap.put(R.id.action_global_navigation_home, R.id.action_global_navigation_home);
    }

    public static Integer getActionForDestination(int destinationId) {
        return destinationToActionMap.get(destinationId);
    }

    public static boolean hasActionForDestination(int destinationId) {
        return destinationToActionMap.containsKey(destinationId);
    }
}
