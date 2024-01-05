package com.romanpulov.violetnote.view.core;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

import org.xmlpull.v1.XmlPullParser;

/**
 * Text input dialog - interface to AlertInputDialog
 * Created by romanpulov on 26.08.2016.
 */

public class TextInputDialog extends AlertInputDialog {

    public interface OnTextInputListener {
        void onTextInput(String text);
    }

    private final Context mContext;
    private final String mTitle;
    protected String mText;
    protected int mInputType;
    protected boolean mSelectEnd;
    protected boolean mShowInput;
    private String mNonEmptyErrorMessage;
    protected View mInputView;

    public void setText(String value) {
        mText = value;
    }

    public void setSelectEnd(boolean value) {
        mSelectEnd = value;
    }

    public void setShowInput(boolean value) {
        mShowInput = value;
    }

    private OnTextInputListener mOnTextInputListener;

    public void setOnTextInputListener(OnTextInputListener listener) {
        this.mOnTextInputListener = listener;
    }

    public TextInputDialog(Context context, String title) {
        mContext = context;
        mTitle = title;
        mInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
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
        mInputView = input;
        //final EditText input = new EditText(mContext);

        input.setInputType(mInputType);
        input.setTextColor(mContext.getResources().getColor(R.color.brightTextColor));
        input.setText(mText);
        if (mSelectEnd && (mText != null))
            input.setSelection(mText.length());

        input.requestFocus();

        alert.setView(input);
        alert.setTitle(mTitle);

        alert.setPositiveButton(R.string.ok, null);
        alert.setNegativeButton(R.string.cancel, null);

        if (mNonEmptyErrorMessage == null) {
            alert.setPositiveButton(R.string.ok, (dialog, which) -> {
                String password = input.getText().toString();
                if (password.isEmpty())
                    input.setError("Should not be empty");
                else if (mOnTextInputListener != null)
                    mOnTextInputListener.onTextInput(password);
            });
        }

        alert.setNegativeButton(R.string.cancel, (dialog, which) -> {
            if (mOnTextInputListener != null)
                mOnTextInputListener.onTextInput(null);
        });

        mAlertDialog = alert.create();

        if (mNonEmptyErrorMessage != null) {
            mAlertDialog.setOnShowListener(dialogInterface -> {
                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(view -> {
                    String password = input.getText().toString();
                    if (password.isEmpty())
                        input.setError(mNonEmptyErrorMessage);
                    else {
                        if (mOnTextInputListener != null)
                            mOnTextInputListener.onTextInput(password);
                        mAlertDialog.dismiss();
                    }
                });
            });
        }

        if (mShowInput) {
            InputManagerHelper.showWindowInput(mAlertDialog.getWindow());
        }

        mAlertDialog.show();
    }
}
