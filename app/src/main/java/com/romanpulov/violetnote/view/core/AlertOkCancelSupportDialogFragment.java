package com.romanpulov.violetnote.view.core;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.romanpulov.violetnote.R;

/**
 * Created by romanpulov on 26.08.2016.
 */
public class AlertOkCancelSupportDialogFragment extends DialogFragment {
    private final static String STRING_MESSAGE = "MESSAGE";

    private String  mMessage;

    public interface OnClickListener {
        void OnClick(DialogFragment dialog);
    }

    private OnClickListener mOkButtonClickListener;

    public void setOkButtonClickListener(OnClickListener listener) {
        mOkButtonClickListener = listener;
    }

    public static AlertOkCancelSupportDialogFragment newAlertOkCancelDialog(String message) {
        AlertOkCancelSupportDialogFragment newDialog = new AlertOkCancelSupportDialogFragment() ;
        newDialog.setMessage(message);
        newDialog.setRetainInstance(true);
        return newDialog;
    }

    public void setMessage(String  message) {
        mMessage = message;
    }

    @Override
    public void onSaveInstanceState(Bundle data) {
        data.putString(AlertOkCancelSupportDialogFragment.STRING_MESSAGE, mMessage);
        super.onSaveInstanceState(data);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null != savedInstanceState)
            mMessage = savedInstanceState.getString(AlertOkCancelSupportDialogFragment.STRING_MESSAGE);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        dialogBuilder
                .setMessage(mMessage)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (null != mOkButtonClickListener)
                                    mOkButtonClickListener.OnClick(AlertOkCancelSupportDialogFragment.this);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel, null)
        ;
        return dialogBuilder.create();
    }
}