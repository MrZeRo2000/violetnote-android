package com.romanpulov.violetnote.view.core;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Xml;
import android.widget.EditText;

import com.romanpulov.violetnote.R;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by rpulov on 27.03.2016.
 */
class PasswordInputDialog {

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
        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);

        //trick to achieve white caret color
        AttributeSet editTextCursorAttributeSet = null;
        int res = mContext.getResources().getIdentifier("cursor_edit_text", "layout", mContext.getPackageName());
        XmlPullParser parser = mContext.getResources().getXml(res);
        int state=0;
        do {
            try {
                state = parser.next();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (state == XmlPullParser.START_TAG) {
                if (parser.getName().equals("EditText")) {
                    editTextCursorAttributeSet = Xml.asAttributeSet(parser);
                    break;
                }
            }
        } while(state != XmlPullParser.END_DOCUMENT);

        final EditText input = new EditText(mContext, editTextCursorAttributeSet);
        //final EditText input = new EditText(mContext);

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
