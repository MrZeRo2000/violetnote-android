package com.romanpulov.violetnote.view.helper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Helper class for clipboard copy
 */
public class ClipboardHelper {
    public static void copyPlainText(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(ClipboardHelper.class.getName(), text));
        }
    }
}
