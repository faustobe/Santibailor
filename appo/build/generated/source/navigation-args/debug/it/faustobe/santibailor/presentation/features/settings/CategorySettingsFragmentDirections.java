package it.faustobe.santibailor.presentation.features.settings;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import it.faustobe.santibailor.NavGraphDirections;
import it.faustobe.santibailor.R;

public class CategorySettingsFragmentDirections {
  private CategorySettingsFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionCategorySettingsFragmentToManageRicorrenzeFragment() {
    return new ActionOnlyNavDirections(R.id.action_categorySettingsFragment_to_manageRicorrenzeFragment);
  }

  @NonNull
  public static NavDirections actionCategorySettingsFragmentToRicorrenzeVisualizationSettingsFragment(
      ) {
    return new ActionOnlyNavDirections(R.id.action_categorySettingsFragment_to_ricorrenzeVisualizationSettingsFragment);
  }

  @NonNull
  public static NavDirections actionCategorySettingsFragmentToNavigationSettings() {
    return new ActionOnlyNavDirections(R.id.action_categorySettingsFragment_to_navigation_settings);
  }

  @NonNull
  public static NavDirections actionGlobalNavigationHome() {
    return NavGraphDirections.actionGlobalNavigationHome();
  }
}
