package com.romanpulov.violetnote.view.core;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.romanpulov.violetnote.R;

/**
 * Implementation of Preference with progress bar
 */
public class ProgressPreference extends Preference {
    private ProgressBar mProgressBar;
    private boolean mIsVisible = false;

    public ProgressPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWidgetLayoutResource(R.layout.indeterminate_progress);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mProgressBar = view.findViewById(R.id.progress_bar);
        setProgressVisibility(mIsVisible);
    }

    public void setProgressVisibility(boolean value) {
        mIsVisible = value;
        int v = value ? View.VISIBLE : View.GONE;
        mProgressBar.setVisibility(v);
    }
}
