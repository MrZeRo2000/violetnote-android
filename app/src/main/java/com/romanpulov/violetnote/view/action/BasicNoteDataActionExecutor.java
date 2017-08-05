package com.romanpulov.violetnote.view.action;

import android.content.Context;
import android.os.AsyncTask;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataActionExecutor {
    private final Context mContext;
    private final BasicNoteDataA mBasicNoteData;
    private List<Map.Entry<String, BasicNoteDataAction>> mActionList = new ArrayList<>();
    private OnExecutionCompletedListener mListener;

    private long mNoteId = 0;

    public void setNoteId(long value) {
        mNoteId = value;
    }

    public BasicNoteDataActionExecutor(Context context, BasicNoteDataA basicNoteData) {
        mContext = context;
        mBasicNoteData = basicNoteData;
    }

    public void addAction(String description, BasicNoteDataAction action) {
        mActionList.add(new AbstractMap.SimpleEntry<>(description, action));
    }

    public OnExecutionCompletedListener getOnExecutionCompletedListener() {
        return mListener;
    }

    public void setOnExecutionCompletedListener(OnExecutionCompletedListener listener) {
        mListener = listener;
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

    public void execute() {
        if (mBasicNoteData.getNote().isEncrypted())
            new ExecuteAsyncTask().execute();
        else {
            boolean executionResult = internalExecute();

            if (mListener != null)
                mListener.onExecutionCompleted(mBasicNoteData, executionResult);
        }
    }

    private class ExecuteAsyncTask extends AsyncTask<Void, String, Boolean> {

        @Override
        protected void onPreExecute() {
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
            //values[0] - value to update
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (mListener != null)
                mListener.onExecutionCompleted(mBasicNoteData, result);
        }
    }

    public interface OnExecutionCompletedListener {
        void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result);
    }

}
