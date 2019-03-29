package com.romanpulov.violetnote.view.core;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

    @Override
    public void show() {
        super.show();

        // make input focused with shown keyboard
        mInputView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Window window = mAlertDialog.getWindow();
                    if (window != null) {
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }
            }
        });

        mInputView.requestFocus();
    }
}
