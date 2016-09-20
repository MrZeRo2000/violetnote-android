package com.romanpulov.violetnote.view.action;

import android.app.Dialog;
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
    private OnDialogCreatedListener mDialogCreatedListener;

    private long mNoteId = 0;

    public void setNoteId(long value) {
        mNoteId = value;
    }

    public BasicNoteDataActionExecutor(Context context) {
        mContext = context;
        if (context instanceof OnDialogCreatedListener)
            mDialogCreatedListener = (OnDialogCreatedListener)context;
    }

    public void addAction(String description, BasicNoteDataAction action) {
        mActionList.add(new AbstractMap.SimpleEntry<>(description, action));
    }

    public void setOnExecutionCompletedListener(OnExecutionCompletedListener listener) {
        mListener = listener;
    }

    public void setOnDialogCreatedListener(OnDialogCreatedListener listener) {
        mDialogCreatedListener = listener;
    }

    private DBNoteManager createNoteManager() {
        DBNoteManager noteManager = new DBNoteManager(mContext);
        noteManager.setNoteId(mNoteId);
        return noteManager;
    }

    private boolean internalExecute() {
        DBNoteManager noteManager = createNoteManager();

        for (Map.Entry<String, BasicNoteDataAction> entry : mActionList) {
            if (!entry.getValue().execute(noteManager)) {
                return false;
            }
        }
        return true;
    }

    public void execute(boolean isAsync) {
        if (isAsync)
            new ExecuteAsyncTask().execute();
        else {
            boolean executionResult = internalExecute();

            if (mListener != null)
                mListener.onExecutionCompleted(executionResult);
        }
    }

    private class ExecuteAsyncTask extends AsyncTask<Void, String, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext, R.style.DialogTheme);
            progressDialog.setTitle(R.string.caption_processing);
            progressDialog.show();
            if (mDialogCreatedListener != null)
                mDialogCreatedListener.onDialogCreated(progressDialog);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DBNoteManager noteManager = createNoteManager();

            for (Map.Entry<String, BasicNoteDataAction> entry : mActionList) {
                //get caption, default if no caption
                String caption = entry.getKey();
                if ((caption == null) || caption.isEmpty())
                    caption = mContext.getString(R.string.caption_processing);
                //update caption
                publishProgress(caption);

                //execute
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
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                if (progressDialog != null)
                    progressDialog.dismiss();

                if (mListener != null)
                    mListener.onExecutionCompleted(result);
            } catch (Exception e) {
                progressDialog = null;
                return;
            }
        }
    }

    public interface OnExecutionCompletedListener {
        void onExecutionCompleted(boolean result);
    }

    public interface OnDialogCreatedListener {
        void onDialogCreated(Dialog dialog);
    }
}
