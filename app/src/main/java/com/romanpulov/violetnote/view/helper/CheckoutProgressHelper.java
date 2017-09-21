package com.romanpulov.violetnote.view.helper;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.romanpulov.violetnote.R;

import java.util.Locale;

/**
 * Checkout progress display helper class
 * Created by rpulov on 07.12.2016.
 */

public class CheckoutProgressHelper {

    private final View mHostView;
    private final TextView mProgressTextView;
    private final ProgressBar mProgressbar;

    public CheckoutProgressHelper(View hostView) {
        mHostView = hostView;
        mProgressTextView = (TextView) mHostView.findViewById(R.id.progress_text_view);
        mProgressbar = (ProgressBar) hostView.findViewById(R.id.progress_bar);
    }

    public void setProgressData(int checkedItemCount, int itemCount) {
        if ((checkedItemCount == 0) && (mHostView.getVisibility() == View.VISIBLE))
            mHostView.setVisibility(View.GONE);
        else if (checkedItemCount > 0) {
            mProgressbar.setMax(itemCount);
            mProgressbar.setProgress(checkedItemCount);
            mProgressTextView.setText(String.format(Locale.getDefault(), "%d/%d", checkedItemCount, itemCount));
            mHostView.setVisibility(View.VISIBLE);
        }
    }
}
