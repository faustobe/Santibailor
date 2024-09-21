package it.faustobe.santibailor.presentation.common.ricorrenze;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavArgs;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class EditRicorrenzaFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private EditRicorrenzaFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private EditRicorrenzaFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static EditRicorrenzaFragmentArgs fromBundle(@NonNull Bundle bundle) {
    EditRicorrenzaFragmentArgs __result = new EditRicorrenzaFragmentArgs();
    bundle.setClassLoader(EditRicorrenzaFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("ricorrenzaId")) {
      int ricorrenzaId;
      ricorrenzaId = bundle.getInt("ricorrenzaId");
      __result.arguments.put("ricorrenzaId", ricorrenzaId);
    } else {
      __result.arguments.put("ricorrenzaId", -1);
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static EditRicorrenzaFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    EditRicorrenzaFragmentArgs __result = new EditRicorrenzaFragmentArgs();
    if (savedStateHandle.contains("ricorrenzaId")) {
      int ricorrenzaId;
      ricorrenzaId = savedStateHandle.get("ricorrenzaId");
      __result.arguments.put("ricorrenzaId", ricorrenzaId);
    } else {
      __result.arguments.put("ricorrenzaId", -1);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  public int getRicorrenzaId() {
    return (int) arguments.get("ricorrenzaId");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("ricorrenzaId")) {
      int ricorrenzaId = (int) arguments.get("ricorrenzaId");
      __result.putInt("ricorrenzaId", ricorrenzaId);
    } else {
      __result.putInt("ricorrenzaId", -1);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("ricorrenzaId")) {
      int ricorrenzaId = (int) arguments.get("ricorrenzaId");
      __result.set("ricorrenzaId", ricorrenzaId);
    } else {
      __result.set("ricorrenzaId", -1);
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
    EditRicorrenzaFragmentArgs that = (EditRicorrenzaFragmentArgs) object;
    if (arguments.containsKey("ricorrenzaId") != that.arguments.containsKey("ricorrenzaId")) {
      return false;
    }
    if (getRicorrenzaId() != that.getRicorrenzaId()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + getRicorrenzaId();
    return result;
  }

  @Override
  public String toString() {
    return "EditRicorrenzaFragmentArgs{"
        + "ricorrenzaId=" + getRicorrenzaId()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull EditRicorrenzaFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    public Builder() {
    }

    @NonNull
    public EditRicorrenzaFragmentArgs build() {
      EditRicorrenzaFragmentArgs result = new EditRicorrenzaFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setRicorrenzaId(int ricorrenzaId) {
      this.arguments.put("ricorrenzaId", ricorrenzaId);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    public int getRicorrenzaId() {
      return (int) arguments.get("ricorrenzaId");
    }
  }
}
