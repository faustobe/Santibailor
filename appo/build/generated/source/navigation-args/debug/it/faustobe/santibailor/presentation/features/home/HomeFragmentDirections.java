package it.faustobe.santibailor.presentation.features.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import it.faustobe.santibailor.R;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class HomeFragmentDirections {
  private HomeFragmentDirections() {
  }

  @NonNull
  public static ActionHomeFragmentToEditRicorrenzaFragment actionHomeFragmentToEditRicorrenzaFragment(
      ) {
    return new ActionHomeFragmentToEditRicorrenzaFragment();
  }

  public static class ActionHomeFragmentToEditRicorrenzaFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionHomeFragmentToEditRicorrenzaFragment() {
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionHomeFragmentToEditRicorrenzaFragment setRicorrenzaId(int ricorrenzaId) {
      this.arguments.put("ricorrenzaId", ricorrenzaId);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("ricorrenzaId")) {
        int ricorrenzaId = (int) arguments.get("ricorrenzaId");
        __result.putInt("ricorrenzaId", ricorrenzaId);
      } else {
        __result.putInt("ricorrenzaId", -1);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_homeFragment_to_editRicorrenzaFragment;
    }

    @SuppressWarnings("unchecked")
    public int getRicorrenzaId() {
      return (int) arguments.get("ricorrenzaId");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionHomeFragmentToEditRicorrenzaFragment that = (ActionHomeFragmentToEditRicorrenzaFragment) object;
      if (arguments.containsKey("ricorrenzaId") != that.arguments.containsKey("ricorrenzaId")) {
        return false;
      }
      if (getRicorrenzaId() != that.getRicorrenzaId()) {
        return false;
      }
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + getRicorrenzaId();
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionHomeFragmentToEditRicorrenzaFragment(actionId=" + getActionId() + "){"
          + "ricorrenzaId=" + getRicorrenzaId()
          + "}";
    }
  }
}
