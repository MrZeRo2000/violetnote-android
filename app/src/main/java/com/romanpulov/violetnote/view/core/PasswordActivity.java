package com.romanpulov.violetnote.view.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.romanpulov.violetnote.model.PasswordProvider;

/**
 * Created by rpulov on 30.05.2016.
 */
public abstract class PasswordActivity extends ActionBarCompatActivity {

    public static final String PASS_DATA = "PassData";
    public static final String PASSWORD_REQUIRED = "PasswordRequired";

    protected PasswordProvider mPasswordProvider;

    private boolean mPasswordRequired = true;

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
        return (getFragment() != null);
    }

    protected Fragment getFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentById(getFragmentContainerId());
    }

    protected void updateResult() {
        setResult(fragmentExists() ? RESULT_OK : RESULT_CANCELED);
    }

    protected FragmentManager removeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(getFragmentContainerId());
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
        }
        return fm;
    }

    protected abstract void refreshFragment();

    protected abstract void updatePassword(String password);

    protected void requestPassword() {
        PasswordInputDialog passwordInputDialog = new PasswordInputDialog(this);
        passwordInputDialog.setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
            @Override
            public void onTextInput(String text) {
                if (text != null) {
                    String oldPassword = getPassword();
                    if ((oldPassword != null) && (oldPassword.equals(text))) {
                        refreshFragment();
                    } else {
                        updatePassword(text);
                    }
                }
            }
        });
        passwordInputDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPasswordProvider = getIntent().getParcelableExtra(PASS_DATA);
        mPasswordRequired = getIntent().getBooleanExtra(PASSWORD_REQUIRED, true);
        getIntent().removeExtra(PASSWORD_REQUIRED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("PasswordActivity", "OnActivityResult:" + resultCode);
        mPasswordRequired = resultCode != RESULT_OK;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPasswordProvider.isPasswordProtected()) {
            if (mPasswordRequired) {
                Log.d("PasswordActivity", "OnResume: password required");
                removeFragment();
                requestPassword();
            } else {
                Log.d("PasswordActivity", "OnResume: password not required");
                mPasswordRequired = true;
                if (mPasswordProvider == null) {
                    refreshFragment();
                }
            }
        }
    }
}
