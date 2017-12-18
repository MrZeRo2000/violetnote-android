package com.romanpulov.violetnote.view.core;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import com.romanpulov.violetnote.model.PassDataA;

import java.io.File;
import java.util.List;

/**
 * PassData load progress fragment
 * Created by rpulov on 29.07.2017.
 */

public class PassDataProgressFragment extends ProgressFragment {
    private OnPassDataFragmentInteractionListener mListener;
    private Context mContext;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        setProgressText(mContext.getString(R.string.caption_loading));
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadPassData(String password) {
        LoadPassDataAsyncTask task = new LoadPassDataAsyncTask(this, password);
        task.execute();
    }

    public interface OnPassDataFragmentInteractionListener {
        void onPassDataFragmentAttached();
        void onPassDataLoaded(PassDataA passDataA, String errorText);
    }

    private static class LoadPassDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private final String mPassword;
        private final PassDataProgressFragment mHost;

        private PassDataA mPassDataA;
        private String mErrorText;

        LoadPassDataAsyncTask(PassDataProgressFragment host, String password) {
            mHost = host;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            File file = new File(DocumentPassDataLoader.getDocumentFileName(mHost.mContext));

            if (file.exists()) {
                List<String> loadErrorList;
                DocumentPassDataLoader documentPassDataLoader = DocumentPassDataLoader.newInstance(mHost.mContext);
                mPassDataA = documentPassDataLoader.loadPassDataA(file.getAbsolutePath(), mPassword);
                loadErrorList = documentPassDataLoader.getLoadErrorList();
                if (loadErrorList.size() > 0)
                    mErrorText = loadErrorList.get(0);
            } else {
                mErrorText = mHost.mContext.getString(R.string.error_file_not_found);
            }

            return mPassDataA != null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (mHost.mListener != null)
                mHost.mListener.onPassDataLoaded(mPassDataA, mErrorText);
        }
    }
}
