package it.faustobe.santibailor.presentation.features.add;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class AddItemFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private AddItemFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private AddItemFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static AddItemFragmentArgs fromBundle(@NonNull Bundle bundle) {
    AddItemFragmentArgs __result = new AddItemFragmentArgs();
    bundle.setClassLoader(AddItemFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("itemType")) {
      String itemType;
      itemType = bundle.getString("itemType");
      if (itemType == null) {
        throw new IllegalArgumentException("Argument \"itemType\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("itemType", itemType);
    } else {
      throw new IllegalArgumentException("Required argument \"itemType\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static AddItemFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    AddItemFragmentArgs __result = new AddItemFragmentArgs();
    if (savedStateHandle.contains("itemType")) {
      String itemType;
      itemType = savedStateHandle.get("itemType");
      if (itemType == null) {
        throw new IllegalArgumentException("Argument \"itemType\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("itemType", itemType);
    } else {
      throw new IllegalArgumentException("Required argument \"itemType\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getItemType() {
    return (String) arguments.get("itemType");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("itemType")) {
      String itemType = (String) arguments.get("itemType");
      __result.putString("itemType", itemType);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("itemType")) {
      String itemType = (String) arguments.get("itemType");
      __result.set("itemType", itemType);
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
    AddItemFragmentArgs that = (AddItemFragmentArgs) object;
    if (arguments.containsKey("itemType") != that.arguments.containsKey("itemType")) {
      return false;
    }
    if (getItemType() != null ? !getItemType().equals(that.getItemType()) : that.getItemType() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getItemType() != null ? getItemType().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AddItemFragmentArgs{"
        + "itemType=" + getItemType()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull AddItemFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@NonNull String itemType) {
      if (itemType == null) {
        throw new IllegalArgumentException("Argument \"itemType\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("itemType", itemType);
    }

    @NonNull
    public AddItemFragmentArgs build() {
      AddItemFragmentArgs result = new AddItemFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setItemType(@NonNull String itemType) {
      if (itemType == null) {
        throw new IllegalArgumentException("Argument \"itemType\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("itemType", itemType);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getItemType() {
      return (String) arguments.get("itemType");
    }
  }
}
