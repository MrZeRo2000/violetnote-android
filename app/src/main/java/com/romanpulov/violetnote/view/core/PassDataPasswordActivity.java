package com.romanpulov.violetnote.view.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.Document;
import com.romanpulov.violetnote.model.PassDataA;

import java.io.File;
import java.util.List;

/**
 * Created by romanpulov on 01.09.2016.
 */
public abstract class PassDataPasswordActivity extends PasswordActivity {
    protected PassDataA mPassDataA;

    private class LoadPassDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog;
        final String mPassword;
        final Context mContext;
        Boolean mFileExists;

        public LoadPassDataAsyncTask(String password) {
            mPassword = password;
            mContext = PassDataPasswordActivity.this;
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
            try {
                if (progressDialog != null)
                    progressDialog.dismiss();
            } catch (Exception e) {
                progressDialog = null;
                return;
            }

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

    @Override
    protected void updatePassword(String password) {
        new LoadPassDataAsyncTask(password).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPasswordProvider instanceof PassDataA) {
            mPassDataA = (PassDataA)mPasswordProvider;
        }
    }
}
