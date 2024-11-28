package com.romanpulov.violetnote.view.core;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

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
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        mProgressBar = (ProgressBar) holder.findViewById(R.id.progress_bar);
        setProgressVisibility(mIsVisible);
    }

    public void setProgressVisibility(boolean value) {
        mIsVisible = value;
        if (mProgressBar != null) {
            int v = value ? View.VISIBLE : View.GONE;
            mProgressBar.setVisibility(v);
        }
    }
}
