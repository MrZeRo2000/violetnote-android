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
public class PasswordInputDialog extends TextInputDialog {

    public PasswordInputDialog(Context context) {
        super(context, context.getString(R.string.password_required));
        mInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
    }
}
