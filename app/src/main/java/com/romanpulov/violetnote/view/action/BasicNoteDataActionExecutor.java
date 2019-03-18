package com.romanpulov.violetnote.view.action;

import android.content.Context;
import android.os.AsyncTask;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BasicNoteDataA action executor
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataActionExecutor extends BasicActionExecutor<BasicNoteDataA> {

    public BasicNoteDataActionExecutor(Context context, BasicNoteDataA basicNoteData) {
        super(context, basicNoteData);
    }

    public void execute() {
        if (mData.getNote().isEncrypted())
            new ExecuteAsyncTask(this).execute();
        else {
            super.execute();
        }
    }

    private static class ExecuteAsyncTask extends AsyncTask<Void, String, Boolean> {

        private final BasicNoteDataActionExecutor mHost;

        ExecuteAsyncTask(BasicNoteDataActionExecutor host) {
            mHost = host;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DBNoteManager noteManager = mHost.createNoteManager();

            for (Map.Entry<String, BasicNoteDataAction> entry : mHost.mActionList) {
                //get caption, default if no caption
                String caption = entry.getKey();
                if ((caption == null) || caption.isEmpty())
                    caption = mHost.mContext.getString(R.string.caption_processing);

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
            if (mHost.mProgressListener != null)
                mHost.mProgressListener.onExecutionProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (mHost.mListener != null)
                mHost.mListener.onExecutionCompleted(mHost.mData, result);
        }
    }

    public interface OnExecutionCompletedListener extends BasicActionExecutor.OnExecutionCompletedListener<BasicNoteDataA> {};

}
