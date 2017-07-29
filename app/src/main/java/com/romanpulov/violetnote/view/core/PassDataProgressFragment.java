package com.romanpulov.violetnote.view.core;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.Document;
import com.romanpulov.violetnote.model.PassDataA;

import java.io.File;
import java.util.List;

/**
 * Created by rpulov on 29.07.2017.
 */

public class PassDataProgressFragment extends ProgressFragment {
    private OnPassDataFragmentInteractionListener mListener;
    private Context mContext;
    private LoadPassDataAsyncTask mTask;

    public static PassDataProgressFragment newInstance(Context context) {
        PassDataProgressFragment fragment = new PassDataProgressFragment();
        fragment.mContext = context;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnPassDataFragmentInteractionListener) {
            mListener = (OnPassDataFragmentInteractionListener) activity;
            mListener.onPassDataFragmentAttached();
        }
        else
            throw new RuntimeException(activity.toString()
                    + " must implement OnPassDataFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadPassData(String password) {
        mTask = new LoadPassDataAsyncTask(mContext, password);
        mTask.execute();
    }

    public interface OnPassDataFragmentInteractionListener {
        void onPassDataFragmentAttached();
        void onPassDataLoaded(PassDataA passDataA, String errorText);
    }

    private class LoadPassDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private final String mPassword;
        private final Context mContext;

        private PassDataA mPassDataA;
        private String mErrorText;

        public LoadPassDataAsyncTask(Context context, String password) {
            mContext = context;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            File file = new File(Document.newInstance(mContext).getFileName());

            List<String> loadErrorList;
            if (file.exists()) {
                Document document = Document.newInstance(mContext);
                mPassDataA = document.loadPassDataA(document.getFileName(), mPassword);
                loadErrorList = document.getLoadErrorList();
                if (loadErrorList.size() > 0)
                    mErrorText = loadErrorList.get(0);
            } else {
                mErrorText = mContext.getString(R.string.error_file_not_found);
            }

            return mPassDataA != null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (mListener != null)
                mListener.onPassDataLoaded(mPassDataA, mErrorText);
        }
    }
}