package com.romanpulov.violetnote.view.helper;

import android.content.Context;

import android.os.Build;
import android.view.WindowInsetsController;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import androidx.core.view.WindowInsetsCompat;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Helper class for managing user input
 * Created by romanpulov on 25.08.2017.
 */

public class InputManagerHelper {
    private static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
    }

    /**
     * Hide input from view
     * @param v View
     */
    public static void hideInput(View v) {
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsetsController windowInsetsController = v.getWindowInsetsController();
                if (windowInsetsController != null) {
                    windowInsetsController.hide(WindowInsetsCompat.Type.ime());
                }
            } else {
                InputMethodManager inputMethodManager = getInputMethodManager(v.getContext());
                if ((inputMethodManager != null) && (inputMethodManager.isAcceptingText()))
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    /**
     * Show input from view
     * @param v View
     */
    public static void showInput(View v) {
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsetsController windowInsetsController = v.getWindowInsetsController();
                if (windowInsetsController != null) {
                    windowInsetsController.show(WindowInsetsCompat.Type.ime());
                }
            } else {
                InputMethodManager inputMethodManager = getInputMethodManager(v.getContext());
                if (inputMethodManager != null)
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    /**
     * Request focus and show input from view
     * @param v View
     */
    public static void focusAndShowInput(View v) {
        if ((v != null) && (v.requestFocus())) {
            showInput(v);
        }
    }

    /**
     * Show input from view with delay
     * @param v View
     */
    public static void showInputDelayed(View v) {
        if (v != null) {
            v.postDelayed(() -> {
                InputMethodManager inputMethodManager = getInputMethodManager(v.getContext());
                if (inputMethodManager != null)
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            }, 500);
        }
    }

    /**
     * Focus input and show with delay
     * @param v View
     */
    public static void focusAndShowDelayed(View v) {
        if ((v != null) && v.requestFocus()) {
            showInputDelayed(v);
        }
    }

    /**
     * Shows input from window
     * @param window Window
     */
    public static void showWindowInput(@Nullable Window window) {
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
