package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;

/**
 * Progress fragment
 */
public class ProgressFragment extends Fragment {

    private String mProgressText;
    private TextView mProgressTextView;

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain instance
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_progress, container, false);

        //set saved progress text
        mProgressTextView = (TextView)v.findViewById(R.id.progress_text_view);
        mProgressTextView.setText(mProgressText);

        return v;
    }

    protected void setProgressText(String text) {
        mProgressText = text;
        if (mProgressTextView != null)
            mProgressTextView.setText(mProgressText);
    }
}
