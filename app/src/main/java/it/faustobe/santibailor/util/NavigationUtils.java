package it.faustobe.santibailor.util;

import android.os.Bundle;

import androidx.navigation.NavController;

import it.faustobe.santibailor.R;

public class NavigationUtils {
    public static void openAddItemFragment(NavController navController, String itemType) {
        Bundle args = new Bundle();
        args.putString("itemType", itemType);
        navController.navigate(R.id.action_global_to_add_item, args);
    }
}
