package com.romanpulov.violetnote.view.helper;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.utils.InputParserUtils;

import java.util.Locale;

/**
 * Checkout progress display helper class
 * Created by rpulov on 07.12.2016.
 */

public class CheckoutProgressHelper {

    private final View mHostView;
    private final TextView mProgressTextView;
    private final ProgressBar mProgressbar;
    private final TextView mPriceTextView;

    public CheckoutProgressHelper(View hostView) {
        mHostView = hostView;
        mProgressTextView = hostView.findViewById(R.id.progress_text_view);
        mProgressbar = hostView.findViewById(R.id.progress_bar);
        mPriceTextView = hostView.findViewById(R.id.price_view);
    }

    public void setProgressData(int checkedItemCount, int itemCount, long checkedPrice, long totalPrice, boolean isInt) {
        mPriceTextView.setVisibility(totalPrice > 0 ? View.VISIBLE : View.GONE);
        mHostView.setVisibility(itemCount > 0 ? View.VISIBLE : View.GONE);

        if (mHostView.getVisibility() == View.VISIBLE) {
            mProgressbar.setMax(itemCount);
            mProgressbar.setProgress(checkedItemCount);

            String progressText;
            String priceText;
            int numberDisplayStyle = InputParserUtils.getNumberDisplayStyle(isInt);
            if (checkedItemCount > 0) {
                progressText = String.format(Locale.getDefault(), "%d/%d", checkedItemCount, itemCount);
                priceText = String.format(
                        Locale.getDefault(),
                        "%s/%s",
                        InputParserUtils.getDisplayValue(checkedPrice, numberDisplayStyle),
                        InputParserUtils.getDisplayValue(totalPrice, numberDisplayStyle));
            } else {
                progressText = String.format(Locale.getDefault(), "%d", itemCount);
                priceText = InputParserUtils.getDisplayValue(totalPrice, numberDisplayStyle);
            }
            mProgressTextView.setText(progressText);

            if (mPriceTextView.getVisibility() == View.VISIBLE)
                mPriceTextView.setText(priceText);
        }
    }
}
