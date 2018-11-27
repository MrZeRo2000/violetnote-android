package com.romanpulov.violetnote.view.helper;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Helper class for managing user input
 * Created by romanpulov on 25.08.2017.
 */

public class InputManagerHelper {

    /**
     * Hide input from view
     * @param v View
     */
    public static void hideInput(View v) {
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
            if ((inputMethodManager != null) && (inputMethodManager.isAcceptingText()))
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * Show input from view
     * @param v View
     */
    public static void showInput(View v) {
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * Toggle force input
     * @param context Context
     */
    public static void toggleInputForced(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
