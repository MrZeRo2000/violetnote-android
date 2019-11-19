package com.romanpulov.violetnote.view.core;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.PasswordProvider;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

/**
 * Base password protected activity class
 * Created by rpulov on 30.05.2016.
 */
public abstract class PasswordActivity extends ActionBarCompatActivity {

    public static final String PASS_DATA = "PassData";

    private static final int PASSWORD_VALIDITY_PERIOD = 10000;

    /**
     * Checks validity period for user actions     *
     */
    public static class ValidityPeriodChecker {
        private final long mValidityPeriod;
        private long mValidTimeStamp;

        public ValidityPeriodChecker(long validityPeriod) {
            mValidityPeriod = validityPeriod;
        }

        public void startPeriod() {
            mValidTimeStamp = System.currentTimeMillis();
        }

        public void resetPeriod() {
            mValidTimeStamp = 0;
        }

        public boolean isValid() {
            return (System.currentTimeMillis() - mValidTimeStamp) < mValidityPeriod;
        }
    }

    private static final ValidityPeriodChecker mPasswordValidityChecker = new ValidityPeriodChecker(PASSWORD_VALIDITY_PERIOD);

    public static ValidityPeriodChecker getPasswordValidityChecker() {
        return mPasswordValidityChecker;
    }

    private DialogInterface mDialog;

    private boolean mIsPasswordProtected = true;

    protected void setPasswordProtected(boolean value) {
        mIsPasswordProtected = value;
    }

    private boolean mIsProgress = false;

    protected void setProgress(boolean value) {
        mIsProgress = value;
    }

    private boolean mForceUpdatePassword = false;

    public void setForceUpdatePassword(boolean value) {
        mForceUpdatePassword = value;
    }

    protected boolean getProgress() {
        return mIsProgress;
    }

    protected PasswordProvider mPasswordProvider;

    private boolean mPasswordRequired = true;
    private boolean mCanceled = false;

    abstract protected boolean isDataLoaded();

    private String getPassword() {
        if (mPasswordProvider != null)
            return mPasswordProvider.getPassword();
        else
            return null;
    }

    protected int getFragmentContainerId() {
        return android.R.id.content;
    }

    private boolean fragmentExists() {
        return ((getFragment() != null) && (!(getFragment() instanceof LoadErrorFragment)));
    }

    protected Fragment getFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentById(getFragmentContainerId());
    }

    protected void updateResult() {
        setResult(fragmentExists() ? RESULT_OK : RESULT_CANCELED);
    }

    /**
     * Removes any fragment except Progress
     * @return FragmentManager
     */
    protected FragmentManager removeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(getFragmentContainerId());
        //remove fragments but leave progress
        if ((fragment != null) && !(fragment instanceof ProgressFragment)) {
            fm.beginTransaction().remove(fragment).commitNow();
        }
        return fm;
    }

    /**
     * Removes Progress fragment
     * @return FragmentManager
     */
    protected FragmentManager removeProgressFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(getFragmentContainerId());
        if (fragment instanceof ProgressFragment) {
            fm.beginTransaction().remove(fragment).commit();
        }
        return fm;
    }

    protected abstract void refreshFragment();

    protected void setLoadErrorFragment(String errorText) {
        LoadErrorFragment fragment = LoadErrorFragment.createWithText(errorText);
        int result = removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
        mPasswordRequired = false;
    }

    public void reload() {
        removeFragment();
        requestPassword();
    }

    protected abstract void updatePassword(String password);

    private void internalRefreshFragment() {
        if (mForceUpdatePassword) {
            updatePassword(mPasswordProvider.getPassword());
        } else {
            refreshFragment();
        }
    }

    protected void processRequestPasswordInput(String text) {
        String oldPassword = getPassword();
        if ((oldPassword != null) && (oldPassword.equals(text))) {
            PassDataPasswordActivity.getPasswordValidityChecker().startPeriod();
            internalRefreshFragment();
        } else {
            updatePassword(text);
        }
    }

    protected void requestPassword() {
        final PasswordInputDialog passwordInputDialog = new PasswordInputDialog(this);
        passwordInputDialog.setNonEmptyErrorMessage(getString(R.string.ui_error_empty_password));
        passwordInputDialog.setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
            @Override
            public void onTextInput(String text) {
                // hide input
                View focusedView = passwordInputDialog.getAlertDialog().getCurrentFocus();
                InputManagerHelper.hideInput(focusedView);

                //dismiss dialog
                if (mDialog != null) {
                    mDialog.dismiss();
                    mDialog = null;
                }

                //process input
                if (text != null) {
                    processRequestPasswordInput(text);
                } else {
                    setResult(RESULT_CANCELED);
                    setLoadErrorFragment(getString(R.string.error_load));
                }
            }
        });
        passwordInputDialog.show();
        mDialog = passwordInputDialog.getAlertDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPasswordProvider = getIntent().getParcelableExtra(PASS_DATA);

        if (mPasswordRequired)
            mPasswordRequired = !mPasswordValidityChecker.isValid();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPasswordRequired = resultCode != RESULT_OK;
        mCanceled = resultCode == RESULT_CANCELED;
    }

    @Override
    protected void onPause() {
        if (mIsPasswordProtected) {
            removeFragment();

            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsPasswordProtected) {
            if (mCanceled) {
                setLoadErrorFragment(getString(R.string.error_load));
            }
            else {
                if (mPasswordRequired) {
                    removeFragment();
                    requestPassword();
                } else {
                    if (!isDataLoaded()) {
                        setLoadErrorFragment(getString(R.string.error_load));
                    } else {
                        mPasswordRequired = true;
                        internalRefreshFragment();
                    }
                }
            }
        }
    }

    protected void onProgressAttached() {
        mIsProgress = true;
        mPasswordRequired = false;
    }

    protected void requirePassword() {
        mPasswordRequired = true;
    }
}
