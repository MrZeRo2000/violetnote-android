package com.romanpulov.violetnote.view.action;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataActionExecutor {
    private final Context mContext;
    private List<BasicNoteDataAction> mActionList = new ArrayList<>();
    Map.
    private OnExecutionCompletedListener mListener;

    public BasicNoteDataActionExecutor(Context context) {
        mContext = context;
    }

    public void addAction(BasicNoteDataAction action) {
        mActionList.add(action);
    }

    public void setOnExecutionCompletedListener(OnExecutionCompletedListener listener) {
        mListener = listener;
    }

    private boolean internalExecute() {
        DBNoteManager noteManager = new DBNoteManager(mContext);
        for (BasicNoteDataAction action : mActionList) {
            if (!action.execute(noteManager)) {
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
            return internalExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {

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
