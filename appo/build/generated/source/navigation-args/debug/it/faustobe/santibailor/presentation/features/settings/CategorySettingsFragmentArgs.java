package it.faustobe.santibailor.presentation.features.settings;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavArgs;
import it.faustobe.santibailor.ui.settings.SettingItem;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.System;
import java.util.HashMap;

public class CategorySettingsFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private CategorySettingsFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private CategorySettingsFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings({
      "unchecked",
      "deprecation"
  })
  public static CategorySettingsFragmentArgs fromBundle(@NonNull Bundle bundle) {
    CategorySettingsFragmentArgs __result = new CategorySettingsFragmentArgs();
    bundle.setClassLoader(CategorySettingsFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("categoryTitle")) {
      String categoryTitle;
      categoryTitle = bundle.getString("categoryTitle");
      if (categoryTitle == null) {
        throw new IllegalArgumentException("Argument \"categoryTitle\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("categoryTitle", categoryTitle);
    } else {
      throw new IllegalArgumentException("Required argument \"categoryTitle\" is missing and does not have an android:defaultValue");
    }
    if (bundle.containsKey("settingItems")) {
      SettingItem[] settingItems;
      Parcelable[] __array = bundle.getParcelableArray("settingItems");
      if (__array != null) {
        settingItems = new SettingItem[__array.length];
        System.arraycopy(__array, 0, settingItems, 0, __array.length);
      } else {
        settingItems = null;
      }
      if (settingItems == null) {
        throw new IllegalArgumentException("Argument \"settingItems\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("settingItems", settingItems);
    } else {
      throw new IllegalArgumentException("Required argument \"settingItems\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static CategorySettingsFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    CategorySettingsFragmentArgs __result = new CategorySettingsFragmentArgs();
    if (savedStateHandle.contains("categoryTitle")) {
      String categoryTitle;
      categoryTitle = savedStateHandle.get("categoryTitle");
      if (categoryTitle == null) {
        throw new IllegalArgumentException("Argument \"categoryTitle\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("categoryTitle", categoryTitle);
    } else {
      throw new IllegalArgumentException("Required argument \"categoryTitle\" is missing and does not have an android:defaultValue");
    }
    if (savedStateHandle.contains("settingItems")) {
      SettingItem[] settingItems;
      settingItems = savedStateHandle.get("settingItems");
      if (settingItems == null) {
        throw new IllegalArgumentException("Argument \"settingItems\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("settingItems", settingItems);
    } else {
      throw new IllegalArgumentException("Required argument \"settingItems\" is missing and does not have an android:defaultValue");
    }
    return __result;
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

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
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

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("categoryTitle")) {
      String categoryTitle = (String) arguments.get("categoryTitle");
      __result.set("categoryTitle", categoryTitle);
    }
    if (arguments.containsKey("settingItems")) {
      SettingItem[] settingItems = (SettingItem[]) arguments.get("settingItems");
      __result.set("settingItems", settingItems);
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    CategorySettingsFragmentArgs that = (CategorySettingsFragmentArgs) object;
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
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getCategoryTitle() != null ? getCategoryTitle().hashCode() : 0);
    result = 31 * result + java.util.Arrays.hashCode(getSettingItems());
    return result;
  }

  @Override
  public String toString() {
    return "CategorySettingsFragmentArgs{"
        + "categoryTitle=" + getCategoryTitle()
        + ", settingItems=" + getSettingItems()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull CategorySettingsFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@NonNull String categoryTitle, @NonNull SettingItem[] settingItems) {
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
    public CategorySettingsFragmentArgs build() {
      CategorySettingsFragmentArgs result = new CategorySettingsFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setCategoryTitle(@NonNull String categoryTitle) {
      if (categoryTitle == null) {
        throw new IllegalArgumentException("Argument \"categoryTitle\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("categoryTitle", categoryTitle);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setSettingItems(@NonNull SettingItem[] settingItems) {
      if (settingItems == null) {
        throw new IllegalArgumentException("Argument \"settingItems\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("settingItems", settingItems);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getCategoryTitle() {
      return (String) arguments.get("categoryTitle");
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public SettingItem[] getSettingItems() {
      return (SettingItem[]) arguments.get("settingItems");
    }
  }
}
