package com.romanpulov.violetnote.view.core;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.romanpulov.violetnote.R;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by romanpulov on 26.08.2016.
 */

public class TextInputDialog {

    public interface OnTextInputListener {
        void onTextInput(String text);
    }

    private final Context mContext;
    private final String mTitle;
    protected String mText;
    protected int mInputType;
    private String mNonEmptyErrorMessage;

    public void setText(String value) {
        mText = value;
    }

    private OnTextInputListener mOnTextInputListener;

    public void setOnTextInputListener(OnTextInputListener listener) {
        this.mOnTextInputListener = listener;
    }

    public TextInputDialog(Context context, String title) {
        mContext = context;
        mTitle = title;
        mInputType = InputType.TYPE_CLASS_TEXT;
    }

    public void setNonEmptyErrorMessage(String message) {
        mNonEmptyErrorMessage = message;
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

        input.setInputType(mInputType);
        input.setTextColor(mContext.getResources().getColor(R.color.brightTextColor));
        input.setText(mText);

        alert.setView(input);
        alert.setTitle(mTitle);

        alert.setPositiveButton(R.string.ok, null);
        alert.setNegativeButton(R.string.cancel, null);

        if (mNonEmptyErrorMessage == null) {
            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String password = input.getText().toString();
                    if (password.isEmpty())
                        input.setError("Should not be empty");
                    else if (mOnTextInputListener != null)
                        mOnTextInputListener.onTextInput(password);
                }
            });
        }

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnTextInputListener != null)
                    mOnTextInputListener.onTextInput(null);
            }
        });

        final AlertDialog alertDialog = alert.create();

        if (mNonEmptyErrorMessage != null) {
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String password = input.getText().toString();
                            if (password.isEmpty())
                                input.setError(mNonEmptyErrorMessage);
                            else {
                                if (mOnTextInputListener != null)
                                    mOnTextInputListener.onTextInput(password);
                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });
        }

        alertDialog.show();
    }
}
