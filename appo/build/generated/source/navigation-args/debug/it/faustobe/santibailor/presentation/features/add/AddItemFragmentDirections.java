package it.faustobe.santibailor.presentation.features.add;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import it.faustobe.santibailor.NavGraphDirections;
import it.faustobe.santibailor.R;

public class AddItemFragmentDirections {
  private AddItemFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionAddItemFragmentToNavigationHome() {
    return new ActionOnlyNavDirections(R.id.action_addItemFragment_to_navigation_home);
  }

  @NonNull
  public static NavDirections actionAddItemFragmentToNavigationSettings() {
    return new ActionOnlyNavDirections(R.id.action_addItemFragment_to_navigation_settings);
  }

  @NonNull
  public static NavDirections actionGlobalNavigationHome() {
    return NavGraphDirections.actionGlobalNavigationHome();
  }
}
