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

        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString();
                if (mOnTextInputListener != null)
                    mOnTextInputListener.onTextInput(password);
            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnTextInputListener != null)
                    mOnTextInputListener.onTextInput(null);
            }
        });

        alert.show();
    }
}
