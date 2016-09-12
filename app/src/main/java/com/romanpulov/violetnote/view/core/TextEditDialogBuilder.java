package com.romanpulov.violetnote.view.core;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by romanpulov on 08.09.2016.
 */
public class TextEditDialogBuilder {
    private final Context mContext;
    private final String mTitle;
    private final String mInitialValue;
    private String mNonEmptyErrorMessage;

    private TextInputDialog.OnTextInputListener mListener;

    public TextEditDialogBuilder(Context context, String title, String initialValue) {
        mContext = context;
        mTitle = title;
        mInitialValue = initialValue;
    }

    public TextEditDialogBuilder setNonEmptyErrorMessage(String value) {
        mNonEmptyErrorMessage = value;
        return this;
    }

    public TextEditDialogBuilder setOnTextInputListener(TextInputDialog.OnTextInputListener listener) {
        mListener = listener;
        return this;
    }

    public AlertDialog execute() {
        TextInputDialog dialog = new TextInputDialog(mContext, mTitle);
        dialog.setText(mInitialValue);
        if (mNonEmptyErrorMessage != null)
            dialog.setNonEmptyErrorMessage(mNonEmptyErrorMessage);

        dialog.setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
            @Override
            public void onTextInput(String text) {
                if ((text != null) && (mListener != null)) {
                    mListener.onTextInput(text);
                }
            }
        });
        dialog.show();
        return dialog.getAlertDialog();
    }
}
