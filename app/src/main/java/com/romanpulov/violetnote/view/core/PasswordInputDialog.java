package com.romanpulov.violetnote.view.core;

import android.content.Context;
import android.text.InputType;

import com.romanpulov.violetnote.R;

/**
 * Password input dialog
 * Created by rpulov on 27.03.2016.
 */
public class PasswordInputDialog extends TextInputDialog {

    public PasswordInputDialog(Context context) {
        super(context, context.getString(R.string.password_required));
        mInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
    }
}
