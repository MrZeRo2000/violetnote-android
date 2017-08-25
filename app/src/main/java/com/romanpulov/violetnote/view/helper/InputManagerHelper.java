package com.romanpulov.violetnote.view.helper;

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
            if (inputMethodManager.isAcceptingText())
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
