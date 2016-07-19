package com.romanpulov.violetnote.view.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.Document;
import com.romanpulov.violetnote.model.PassDataA;

import java.io.File;
import java.util.List;

/**
 * Created by rpulov on 30.05.2016.
 */
public abstract class PasswordActivity extends ActionBarCompatActivity {
    public static final String PASS_DATA = "PassData";
    protected static final String PASSWORD_REQUIRED = "PasswordRequired";

    protected PassDataA mPassDataA;
    private boolean mPasswordRequired = true;

    private String getPassword() {
        if (mPassDataA != null)
            return mPassDataA.getPassword();
        else
            return null;
    }

    protected int getFragmentContainerId() {
        return android.R.id.content;
    };

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

    private class LoadPassDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog;
        final String mPassword;
        final Context mContext;
        Boolean mFileExists;

        public LoadPassDataAsyncTask(String password) {
            mPassword = password;
            mContext = PasswordActivity.this;
        }

        @Override
        protected void onPreExecute() {
            File file = new File(Document.getInstance(mContext).getFileName());
            mFileExists = file.exists();
            if (mFileExists) {
                progressDialog = new ProgressDialog(mContext, R.style.DialogTheme);
                progressDialog.setTitle(R.string.caption_loading);
                progressDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Document document = Document.getInstance(mContext);
            mPassDataA = document.loadPassDataA(document.getFileName(), mPassword);
            return mPassDataA != null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (progressDialog != null)
                progressDialog.dismiss();

            String errorText = null;
            if (!mFileExists)
                errorText = mContext.getString(R.string.error_file_not_found);

            if (!result) {
                List<String> errorList = Document.getInstance(mContext).getLoadErrorList();
                if (errorList.size() > 0) {
                    errorText = errorList.get(0);
                }
            }

            if (errorText != null)
                Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();

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

        Log.d("PasswordActivity", "Create, passData = " + mPassDataA);
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
