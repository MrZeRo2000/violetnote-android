package com.romanpulov.violetnote;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by rpulov on 27.03.2016.
 */
public class PasswordInputDialog {

    interface OnPasswordInputListener {
        void onPasswordInput(String password);
    }

    private final Context mContext;

    private OnPasswordInputListener mOnPasswordInputListener;

    public void setOnPasswordInputListener(OnPasswordInputListener listener) {
        this.mOnPasswordInputListener = listener;
    }

    public PasswordInputDialog(Context context) {
        mContext = context;
    }

    public void show() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
        final EditText input = new EditText(mContext);

        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTextColor(mContext.getResources().getColor(R.color.brightTextColor));

        alert.setView(input);
        alert.setTitle(R.string.password_required);

        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString();
                if (mOnPasswordInputListener != null)
                    mOnPasswordInputListener.onPasswordInput(password);
            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnPasswordInputListener != null)
                    mOnPasswordInputListener.onPasswordInput(null);
            }
        });

        alert.show();
    }
}
