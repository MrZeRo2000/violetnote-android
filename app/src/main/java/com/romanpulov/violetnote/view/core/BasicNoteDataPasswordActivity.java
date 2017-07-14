package com.romanpulov.violetnote.view.core;

import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;

/**
 * Created by romanpulov on 02.09.2016.
 */
public abstract class BasicNoteDataPasswordActivity extends PasswordActivity implements BasicNoteDataActionExecutor.OnDialogCreatedListener {
    protected BasicNoteDataA mBasicNoteData;
    protected Dialog mProgressDialog;

    @Override
    public void onDialogCreated(Dialog dialog) {
        mProgressDialog = dialog;
    }

    @Override
    protected void updatePassword(final String password) {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(this);
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData, password));
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(boolean result) {
                mProgressDialog = null;
                if (result) {
                    mBasicNoteData.setPassword(password);
                    refreshFragment();
                    PassDataPasswordActivity.getPasswordValidityChecker().startPeriod();
                }
                else
                    setLoadErrorFragment();
            }

        });

        //executor.execute(mBasicNoteData.getNote().getItems().size() > 0);
        executor.execute(mBasicNoteData.getNote().isEncrypted());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPasswordProvider instanceof BasicNoteDataA) {
            mBasicNoteData = (BasicNoteDataA)mPasswordProvider;
            setPasswordProtected(mBasicNoteData.getNote().isEncrypted());
        }
    }

    @Override
    protected void onPause() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        super.onPause();
    }
}
