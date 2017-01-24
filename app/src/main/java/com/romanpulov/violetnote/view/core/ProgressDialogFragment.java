package com.romanpulov.violetnote.view.core;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.romanpulov.violetnote.R;

/**
 *
 * Created by romanpulov on 24.01.2017.
 */

public class ProgressDialogFragment extends DialogFragment {
    public static String TAG = "ProgressDialogFragment";

    private IFragmentDialogCallbacks mCallbacks;

    public void setCallbacks(IFragmentDialogCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    public ProgressDialogFragment() {
        setCancelable(false);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.DialogTheme);
        progressDialog.setTitle(R.string.caption_loading);

        return progressDialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mCallbacks != null)
            mCallbacks.onCancel(this, dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mCallbacks != null)
            mCallbacks.onDissmiss(this, dialog);

    }
}
