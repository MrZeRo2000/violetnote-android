package com.romanpulov.violetnote.view.core;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.romanpulov.violetnote.R;

/**
 * Created by romanpulov on 09.09.2016.
 */
public class NameValueInputDialog {
    private final Context mContext;
    private final String mTitle;

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

        //buttons
        alert.setPositiveButton(R.string.ok, null);
        alert.setNegativeButton(R.string.cancel, null);

        //create
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
}
