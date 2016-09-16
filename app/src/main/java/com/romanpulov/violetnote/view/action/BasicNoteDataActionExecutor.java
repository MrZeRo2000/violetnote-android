package com.romanpulov.violetnote.view.action;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataActionExecutor {
    private final Context mContext;
    private List<Map.Entry<String, BasicNoteDataAction>> mActionList = new ArrayList<>();
    private OnExecutionCompletedListener mListener;

    public BasicNoteDataActionExecutor(Context context) {
        mContext = context;
    }

    public void addAction(String description, BasicNoteDataAction action) {
        mActionList.add(new AbstractMap.SimpleEntry<>(description, action));
    }

    public void setOnExecutionCompletedListener(OnExecutionCompletedListener listener) {
        mListener = listener;
    }

    private boolean internalExecute() {
        DBNoteManager noteManager = new DBNoteManager(mContext);
        for (Map.Entry<String, BasicNoteDataAction> entry : mActionList) {
            if (!entry.getValue().execute(noteManager)) {
                return false;
            }
        }
        return true;
    }

    public void execute() {
        boolean executionResult = internalExecute();

        if (mListener != null)
            mListener.onExecutionCompleted(executionResult);
    }

    public void executeAsync() {
        new ExecuteAsyncTask().execute();
    }

    private class ExecuteAsyncTask extends AsyncTask<Void, String, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext, R.style.DialogTheme);
            progressDialog.setTitle(R.string.caption_processing);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DBNoteManager noteManager = new DBNoteManager(mContext);
            for (Map.Entry<String, BasicNoteDataAction> entry : mActionList) {
                publishProgress(entry.getKey());
                if (!entry.getValue().execute(noteManager)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            try {
                if (progressDialog != null)
                    progressDialog.setTitle(values[0]);
            } catch (Exception e) {
                progressDialog = null;
                return;
            }
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

            if (mListener != null)
                mListener.onExecutionCompleted(result);
        }
    }

    public interface OnExecutionCompletedListener {
        void onExecutionCompleted(boolean result);
    }
}
