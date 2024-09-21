package it.faustobe.santibailor.presentation.features.settings;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import it.faustobe.santibailor.NavGraphDirections;
import it.faustobe.santibailor.R;
import it.faustobe.santibailor.ui.settings.SettingItem;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class SettingsFragmentDirections {
  private SettingsFragmentDirections() {
  }

  @NonNull
  public static ActionSettingsFragmentToAddItemFragment actionSettingsFragmentToAddItemFragment(
      @NonNull String itemType) {
    return new ActionSettingsFragmentToAddItemFragment(itemType);
  }

  @NonNull
  public static NavDirections actionSettingsFragmentToSearchFragment() {
    return new ActionOnlyNavDirections(R.id.action_settingsFragment_to_searchFragment);
  }

  @NonNull
  public static ActionSettingsFragmentToCategorySettingsFragment actionSettingsFragmentToCategorySettingsFragment(
      @NonNull String categoryTitle, @NonNull SettingItem[] settingItems) {
    return new ActionSettingsFragmentToCategorySettingsFragment(categoryTitle, settingItems);
  }

  @NonNull
  public static NavDirections actionGlobalNavigationHome() {
    return NavGraphDirections.actionGlobalNavigationHome();
  }

  public static class ActionSettingsFragmentToAddItemFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionSettingsFragmentToAddItemFragment(@NonNull String itemType) {
      if (itemType == null) {
        throw new IllegalArgumentException("Argument \"itemType\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("itemType", itemType);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionSettingsFragmentToAddItemFragment setItemType(@NonNull String itemType) {
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
      return R.id.action_settingsFragment_to_addItemFragment;
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
      ActionSettingsFragmentToAddItemFragment that = (ActionSettingsFragmentToAddItemFragment) object;
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
      return "ActionSettingsFragmentToAddItemFragment(actionId=" + getActionId() + "){"
          + "itemType=" + getItemType()
          + "}";
    }
  }

  public static class ActionSettingsFragmentToCategorySettingsFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionSettingsFragmentToCategorySettingsFragment(@NonNull String categoryTitle,
        @NonNull SettingItem[] settingItems) {
      if (categoryTitle == null) {
        throw new IllegalArgumentException("Argument \"categoryTitle\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("categoryTitle", categoryTitle);
      if (settingItems == null) {
        throw new IllegalArgumentException("Argument \"settingItems\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("settingItems", settingItems);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionSettingsFragmentToCategorySettingsFragment setCategoryTitle(
        @NonNull String categoryTitle) {
      if (categoryTitle == null) {
        throw new IllegalArgumentException("Argument \"categoryTitle\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("categoryTitle", categoryTitle);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionSettingsFragmentToCategorySettingsFragment setSettingItems(
        @NonNull SettingItem[] settingItems) {
      if (settingItems == null) {
        throw new IllegalArgumentException("Argument \"settingItems\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("settingItems", settingItems);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("categoryTitle")) {
        String categoryTitle = (String) arguments.get("categoryTitle");
        __result.putString("categoryTitle", categoryTitle);
      }
      if (arguments.containsKey("settingItems")) {
        SettingItem[] settingItems = (SettingItem[]) arguments.get("settingItems");
        __result.putParcelableArray("settingItems", settingItems);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_settingsFragment_to_categorySettingsFragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getCategoryTitle() {
      return (String) arguments.get("categoryTitle");
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public SettingItem[] getSettingItems() {
      return (SettingItem[]) arguments.get("settingItems");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionSettingsFragmentToCategorySettingsFragment that = (ActionSettingsFragmentToCategorySettingsFragment) object;
      if (arguments.containsKey("categoryTitle") != that.arguments.containsKey("categoryTitle")) {
        return false;
      }
      if (getCategoryTitle() != null ? !getCategoryTitle().equals(that.getCategoryTitle()) : that.getCategoryTitle() != null) {
        return false;
      }
      if (arguments.containsKey("settingItems") != that.arguments.containsKey("settingItems")) {
        return false;
      }
      if (getSettingItems() != null ? !getSettingItems().equals(that.getSettingItems()) : that.getSettingItems() != null) {
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
      result = 31 * result + (getCategoryTitle() != null ? getCategoryTitle().hashCode() : 0);
      result = 31 * result + java.util.Arrays.hashCode(getSettingItems());
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionSettingsFragmentToCategorySettingsFragment(actionId=" + getActionId() + "){"
          + "categoryTitle=" + getCategoryTitle()
          + ", settingItems=" + getSettingItems()
          + "}";
    }
  }
}
