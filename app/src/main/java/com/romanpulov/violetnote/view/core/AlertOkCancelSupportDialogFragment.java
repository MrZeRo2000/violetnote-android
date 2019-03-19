package com.romanpulov.violetnote.view.core;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.romanpulov.violetnote.R;

/**
 * Dialog fragment with OkCancel support
 * Created by romanpulov on 26.08.2016.
 */
public class AlertOkCancelSupportDialogFragment extends DialogFragment {
    private final static int STYLE_QUESTION_OK_CANCEL = 0;
    private final static int STYLE_INFO_OK = 1;

    private final static String STRING_MESSAGE = "MESSAGE";
    private final static String STRING_STYLE = "STYLE";

    private String mMessage;
    private int mStyle = STYLE_QUESTION_OK_CANCEL;

    public void setStyle(int value) {
        mStyle = value;
    }

    public interface OnClickListener {
        void OnClick(DialogFragment dialog);
    }

    private OnClickListener mOkButtonClickListener;

    public void setOkButtonClickListener(OnClickListener listener) {
        mOkButtonClickListener = listener;
    }

    public static AlertOkCancelSupportDialogFragment newAlertOkCancelDialog(String message) {
        AlertOkCancelSupportDialogFragment newDialog = new AlertOkCancelSupportDialogFragment() ;
        newDialog.setStyle(STYLE_QUESTION_OK_CANCEL);
        newDialog.setMessage(message);
        newDialog.setRetainInstance(true);
        return newDialog;
    }

    public static AlertOkCancelSupportDialogFragment newAlertOkInfoDialog(String message) {
        AlertOkCancelSupportDialogFragment newDialog = new AlertOkCancelSupportDialogFragment() ;
        newDialog.setStyle(STYLE_INFO_OK);
        newDialog.setMessage(message);
        newDialog.setRetainInstance(true);
        return newDialog;
    }


    private void setMessage(String  message) {
        mMessage = message;
    }

    protected FragmentActivity getActivityNonNull() {
        if (super.getActivity() != null) {
            return super.getActivity();
        } else {
            throw new RuntimeException("null returned from getActivity()");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle data) {
        data.putString(AlertOkCancelSupportDialogFragment.STRING_MESSAGE, mMessage);
        data.putInt(AlertOkCancelSupportDialogFragment.STRING_STYLE, mStyle);
        super.onSaveInstanceState(data);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            mStyle = savedInstanceState.getInt(AlertOkCancelSupportDialogFragment.STRING_STYLE);
            mMessage = savedInstanceState.getString(AlertOkCancelSupportDialogFragment.STRING_MESSAGE);
        }

        int titleId;
        int iconId;

        switch (mStyle) {
            case STYLE_QUESTION_OK_CANCEL:
                titleId = R.string.ui_dialog_title_confirmation;
                iconId = android.R.drawable.ic_dialog_alert;
                break;
            case STYLE_INFO_OK:
                titleId = R.string.ui_dialog_title_info;
                iconId = android.R.drawable.ic_dialog_info;
                break;
            default:
                titleId = 0;
                iconId = 0;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivityNonNull(), R.style.AlertDialogTheme);
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
                .setTitle(titleId)
                .setIcon(iconId)
        ;

        if (mStyle == STYLE_QUESTION_OK_CANCEL) {
            dialogBuilder.setNegativeButton(R.string.cancel, null);
        }

        return dialogBuilder.create();
    }
}