package it.faustobe.santibailor.util;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

/**
 * Utility class per gestire la tastiera virtuale
 */
public class KeyboardUtils {

    /**
     * Nasconde la tastiera virtuale
     *
     * @param activity L'activity corrente
     */
    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;

        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Nasconde la tastiera virtuale da un Fragment
     *
     * @param fragment Il fragment corrente
     */
    public static void hideKeyboard(Fragment fragment) {
        if (fragment == null || fragment.getActivity() == null) return;
        hideKeyboard(fragment.getActivity());
    }

    /**
     * Nasconde la tastiera virtuale da una View specifica
     *
     * @param view La view da cui nascondere la tastiera
     */
    public static void hideKeyboard(View view) {
        if (view == null) return;

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Mostra la tastiera virtuale per una View specifica
     *
     * @param view La view per cui mostrare la tastiera
     */
    public static void showKeyboard(View view) {
        if (view == null) return;

        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Nasconde la tastiera e rimuove il focus dal campo corrente
     *
     * @param activity L'activity corrente
     */
    public static void hideKeyboardAndClearFocus(Activity activity) {
        if (activity == null) return;

        View view = activity.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            hideKeyboard(activity);
        }
    }

    /**
     * Nasconde la tastiera e rimuove il focus dal campo corrente in un Fragment
     *
     * @param fragment Il fragment corrente
     */
    public static void hideKeyboardAndClearFocus(Fragment fragment) {
        if (fragment == null || fragment.getActivity() == null) return;
        hideKeyboardAndClearFocus(fragment.getActivity());
    }

    /**
     * Configura un layout per nascondere la tastiera quando si tocca fuori dai campi EditText
     *
     * @param view La view root del layout (solitamente il layout principale del fragment/activity)
     */
    public static void setupHideKeyboardOnOutsideTouch(View view) {
        // Se la view non è un EditText, imposta il listener
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyboardAndClearFocus(v);
                }
                return false;
            });
        }

        // Se è un ViewGroup, applica ricorsivamente a tutti i figli
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View innerView = viewGroup.getChildAt(i);
                setupHideKeyboardOnOutsideTouch(innerView);
            }
        }
    }

    /**
     * Nasconde la tastiera e rimuove il focus dalla view
     *
     * @param view La view da cui nascondere la tastiera
     */
    private static void hideKeyboardAndClearFocus(View view) {
        if (view == null) return;

        // Trova la view attualmente in focus
        View focusedView = view.findFocus();
        if (focusedView instanceof EditText) {
            focusedView.clearFocus();
            hideKeyboard(focusedView);
        }
    }
}
