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
public class AlertOkCancelDialogFragment extends DialogFragment {
    private final static String STRING_MESSAGE = "MESSAGE";

    private int mMessageId;

    public interface OnClickListener {
        void OnClick(DialogFragment dialog);
    }

    private OnClickListener mOkButtonClickListener;

    public void setOkButtonClickListener(OnClickListener listener) {
        mOkButtonClickListener = listener;
    }

    public static AlertOkCancelDialogFragment newAlertOkCancelDialog(int message) {
        AlertOkCancelDialogFragment newDialog = new AlertOkCancelDialogFragment() ;
        newDialog.setMessage(message);
        newDialog.setRetainInstance(false);
        return newDialog;
    }

    public void setMessage(int messageId) {
        mMessageId = messageId;
    }

    @Override
    public void onSaveInstanceState(Bundle data) {
        data.putInt(AlertOkCancelDialogFragment.STRING_MESSAGE, mMessageId);
        super.onSaveInstanceState(data);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null != savedInstanceState)
            mMessageId = savedInstanceState.getInt(AlertOkCancelDialogFragment.STRING_MESSAGE);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        dialogBuilder
                .setMessage(mMessageId)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (null != mOkButtonClickListener)
                                    mOkButtonClickListener.OnClick(AlertOkCancelDialogFragment.this);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel, null)
        ;
        return dialogBuilder.create();
    }
}