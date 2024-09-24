package it.faustobe.santibailor.presentation.features.search;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import it.faustobe.santibailor.NavGraphDirections;
import it.faustobe.santibailor.R;

public class SearchFragmentDirections {
  private SearchFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionSearchFragmentToEditRicorrenzaFragment() {
    return new ActionOnlyNavDirections(R.id.action_searchFragment_to_editRicorrenzaFragment);
  }

  @NonNull
  public static NavDirections actionGlobalNavigationHome() {
    return NavGraphDirections.actionGlobalNavigationHome();
  }
}
