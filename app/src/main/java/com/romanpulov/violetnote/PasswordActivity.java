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

    protected PassDataA mPassDataA;
    protected boolean mResumeFromChild = false;

    protected String getPassword() {
        if (mPassDataA != null)
            return mPassDataA.getPassword();
        else
            return null;
    }

    protected abstract int getFragmentContainerId();

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PASS_DATA, mPassDataA);
        Log.d("PasswordActivity", "OnSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPassDataA = savedInstanceState.getParcelable(PASS_DATA);
        Log.d("PasswordActivity", "OnRestoreInstanceState");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("PasswordActivity", "OnActivityResult:" + resultCode);
        mResumeFromChild = true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!mResumeFromChild) {
            Log.d("PasswordActivity", "OnResume: not resume from child");
            removeFragment();
            requestPassword();
        } else {
            Log.d("PasswordActivity", "OnResume: resume from child");
            mResumeFromChild = false;
            if (mPassDataA == null) {
                refreshFragment();
            }
        }
    }
}
