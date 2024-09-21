package it.faustobe.santibailor.presentation.features.settings.ricorrenze;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import it.faustobe.santibailor.NavGraphDirections;
import it.faustobe.santibailor.R;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class ManageRicorrenzeFragmentDirections {
  private ManageRicorrenzeFragmentDirections() {
  }

  @NonNull
  public static ActionManageRicorrenzeFragmentToAddItemFragment actionManageRicorrenzeFragmentToAddItemFragment(
      @NonNull String itemType) {
    return new ActionManageRicorrenzeFragmentToAddItemFragment(itemType);
  }

  @NonNull
  public static NavDirections actionManageRicorrenzeFragmentToSearchFragment() {
    return new ActionOnlyNavDirections(R.id.action_manageRicorrenzeFragment_to_searchFragment);
  }

  @NonNull
  public static NavDirections actionGlobalNavigationHome() {
    return NavGraphDirections.actionGlobalNavigationHome();
  }

  public static class ActionManageRicorrenzeFragmentToAddItemFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionManageRicorrenzeFragmentToAddItemFragment(@NonNull String itemType) {
      if (itemType == null) {
        throw new IllegalArgumentException("Argument \"itemType\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("itemType", itemType);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionManageRicorrenzeFragmentToAddItemFragment setItemType(@NonNull String itemType) {
      if (itemType == null) {
        throw new IllegalArgumentException("Argument \"itemType\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("itemType", itemType);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("itemType")) {
        String itemType = (String) arguments.get("itemType");
        __result.putString("itemType", itemType);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_manageRicorrenzeFragment_to_addItemFragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getItemType() {
      return (String) arguments.get("itemType");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionManageRicorrenzeFragmentToAddItemFragment that = (ActionManageRicorrenzeFragmentToAddItemFragment) object;
      if (arguments.containsKey("itemType") != that.arguments.containsKey("itemType")) {
        return false;
      }
      if (getItemType() != null ? !getItemType().equals(that.getItemType()) : that.getItemType() != null) {
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
      result = 31 * result + (getItemType() != null ? getItemType().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionManageRicorrenzeFragmentToAddItemFragment(actionId=" + getActionId() + "){"
          + "itemType=" + getItemType()
          + "}";
    }
  }
}
