package com.romanpulov.violetnote.view.core;

import android.os.Bundle;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutorHost;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;

/**
 * Base class containing BasicNoteDataA with password protection support
 * Created by romanpulov on 02.09.2016.
 */
public abstract class BasicNoteDataPasswordActivity extends PasswordActivity implements BasicNoteDataProgressFragment.OnBasicNoteDataFragmentInteractionListener, BasicNoteDataActionExecutorHost {
    protected BasicNoteDataA mBasicNoteData;

    @Override
    public void onBasicNoteDataFragmentAttached(boolean isProgress) {
        if (isProgress)
            onProgressAttached();
    }

    @Override
    public void onBasicNoteDataLoaded(BasicNoteDataA basicNoteData, boolean result) {
        setProgress(false);
        mBasicNoteData = basicNoteData;

        // fragment related can fail
        try {
            removeProgressFragment();
            if (result) {
                refreshFragment();
                PassDataPasswordActivity.getPasswordValidityChecker().startPeriod();
            } else
                setLoadErrorFragment(getString(R.string.error_load));
        } catch (Exception e) {
            requirePassword();
        }
    }

    @Override
    protected void updatePassword(final String password) {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getApplicationContext(), mBasicNoteData);
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData, password));
        execute(executor);

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
    public void execute(BasicNoteDataActionExecutor executor) {
        BasicNoteDataProgressFragment basicNoteDataProgressFragment = BasicNoteDataProgressFragment.newInstance();
        removeFragment().beginTransaction().add(getFragmentContainerId(), basicNoteDataProgressFragment).commit();
        basicNoteDataProgressFragment.execute(executor);
        setProgress(true);
    }

    @Override
    public void onExecutionStarted() {
        setProgress(true);
    }

    @Override
    public void onExecutionCompleted() {
        setProgress(false);
    }

    @Override
    protected boolean isDataLoaded() {
        return mBasicNoteData != null;
    }
}
