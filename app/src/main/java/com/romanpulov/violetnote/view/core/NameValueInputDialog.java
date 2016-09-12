package com.romanpulov.violetnote.view.core;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.romanpulov.violetnote.R;

/**
 * Created by romanpulov on 09.09.2016.
 */
public class NameValueInputDialog {

    public interface OnNameValueInputListener {
        void onNameValueInput(String name, String value);
    }

    private final Context mContext;
    private final String mTitle;

    protected String mName;
    protected String mValue;

    private OnNameValueInputListener mOnNameValueInputListener;

    public void setOnNameValueInputListener(OnNameValueInputListener value){
        mOnNameValueInputListener = value;
    }

    public void setNameValue(String name, String value) {
        mName = name;
        mValue = value;
    }

    public NameValueInputDialog(Context context, String title) {
        mContext = context;
        mTitle = title;
    }

    public void show() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);

        //title
        alert.setTitle(mTitle);

        //content view
        View contentView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.dialog_name_value, null);
        final EditText nameEditText = (EditText) contentView.findViewById(R.id.name);
        final EditText valueEditText = (EditText) contentView.findViewById(R.id.value);
        alert.setView(contentView);

        //initial data
        nameEditText.setText(mName);
        valueEditText.setText(mValue);

        //buttons
        alert.setPositiveButton(R.string.ok, null);
        alert.setNegativeButton(R.string.cancel, null);

        //create
        final AlertDialog alertDialog = alert.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = nameEditText.getText().toString();
                        String value = valueEditText.getText().toString();

                        if (name.isEmpty())
                            nameEditText.setError(mContext.getString(R.string.error_field_not_empty));
                        else
                            if (value.isEmpty())
                                valueEditText.setError(mContext.getString(R.string.error_field_not_empty));
                            else {
                                if (mOnNameValueInputListener != null)
                                    mOnNameValueInputListener.onNameValueInput(name, value);
                                alertDialog.dismiss();
                            }
                    }
                });
            }
        });

        alertDialog.show();
    }
}