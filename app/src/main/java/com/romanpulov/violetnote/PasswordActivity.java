package com.romanpulov.violetnote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by rpulov on 30.05.2016.
 */
public abstract class PasswordActivity extends ActionBarCompatActivity {
    public static final String PASS_DATA = "PassData";
    public static final String PASSWORD_REQUIRED = "PasswordRequired";

    protected PassDataA mPassDataA;
    protected boolean mPasswordRequired = true;

    protected String getPassword() {
        if (mPassDataA != null)
            return mPassDataA.getPassword();
        else
            return null;
    }

    protected abstract int getFragmentContainerId();

    protected boolean fragmentExists() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(getFragmentContainerId());
        return (fragment != null);
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

    private class LoadPassDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog;
        String mPassword;

        public LoadPassDataAsyncTask(String password) {
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PasswordActivity.this);
            progressDialog.setTitle(R.string.caption_loading);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mPassDataA = Document.loadPassDataA(Document.DEF_FILE_NAME, mPassword);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            refreshFragment();
        }
    }

    protected void requestPassword() {
        PasswordInputDialog passwordInputDialog = new PasswordInputDialog(this);
        passwordInputDialog.setOnPasswordInputListener(new PasswordInputDialog.OnPasswordInputListener() {
            @Override
            public void onPasswordInput(String password) {
                if (password != null) {
                    String oldPassword = getPassword();
                    if ((oldPassword != null) && (oldPassword.equals(password))) {
                        refreshFragment();
                    } else {
                        new LoadPassDataAsyncTask(password).execute();
                    }
                }
            }
        });
        passwordInputDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPassDataA = getIntent().getParcelableExtra(PASS_DATA);

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
        if (mPasswordRequired) {
            Log.d("PasswordActivity", "OnResume: password required");
            removeFragment();
            requestPassword();
        } else {
            Log.d("PasswordActivity", "OnResume: password not required");
            mPasswordRequired = true;
            if (mPassDataA == null) {
                refreshFragment();
            }
        }
    }
}
