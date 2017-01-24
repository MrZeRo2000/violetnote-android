package com.romanpulov.violetnote.view.core;

import android.app.DialogFragment;
import android.content.DialogInterface;

/**
 * Created by romanpulov on 24.01.2017.
 */

public interface IFragmentDialogCallbacks {

    public void onCancel(DialogFragment df, DialogInterface di);
    public void onDissmiss(DialogFragment df, DialogInterface di);
}
