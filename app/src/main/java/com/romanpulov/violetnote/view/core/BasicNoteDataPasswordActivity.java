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
public abstract class BasicNoteDataPasswordActivity extends PasswordActivity implements BasicNoteDataProgressFragment.OnBasicNoteDataFragmentInteractionListener {
    protected BasicNoteDataA mBasicNoteData;

    @Override
    public void onBasicNoteDataFragmentAttached() {
        onProgressAttached();
    }

    @Override
    public void onBasicNoteDataLoaded(BasicNoteDataA basicNoteData, boolean result) {
        setProgress(false);
        removeProgressFragment();
        if (result) {
            mBasicNoteData = basicNoteData;
            refreshFragment();
            PassDataPasswordActivity.getPasswordValidityChecker().startPeriod();
        }
        else
            setLoadErrorFragment();
    }

    @Override
    protected void updatePassword(final String password) {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getApplicationContext(), mBasicNoteData);
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData, password));

        BasicNoteDataProgressFragment basicNoteDataProgressFragment = BasicNoteDataProgressFragment.newInstance();
        removeFragment().beginTransaction().add(getFragmentContainerId(), basicNoteDataProgressFragment).commit();
        basicNoteDataProgressFragment.execute(executor);
        setProgress(true);
        /*
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                if (result) {
                    mBasicNoteData.setPassword(password);
                    refreshFragment();
                    PassDataPasswordActivity.getPasswordValidityChecker().startPeriod();
                }
                else
                    setLoadErrorFragment();
            }
        });

        executor.execute();
        */
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
        super.onPause();
    }
}
