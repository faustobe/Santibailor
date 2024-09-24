package it.faustobe.santibailor;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class NavGraphDirections {
  private NavGraphDirections() {
  }

  @NonNull
  public static NavDirections actionGlobalNavigationHome() {
    return new ActionOnlyNavDirections(R.id.action_global_navigation_home);
  }
}
