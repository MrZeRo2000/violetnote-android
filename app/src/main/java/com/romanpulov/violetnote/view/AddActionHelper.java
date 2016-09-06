package com.romanpulov.violetnote.view;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.romanpulov.violetnote.R;

/**
 * Created by romanpulov on 06.09.2016.
 */
public class AddActionHelper {

    private final View mActionView;

    private OnAddInteractionListener mAddListener;

    public void setOnAddInteractionListener(OnAddInteractionListener listener) {
        mAddListener = listener;
    }

    public AddActionHelper(View actionView) {
        mActionView = actionView;

        setupCancelButton();
        setupEditText();
    }

    private void setupCancelButton() {
        Button searchCancelButton = (Button)mActionView.findViewById(R.id.cancel_button);
        searchCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionView.setVisibility(View.GONE);
            }
        });
    }

    private void setupEditText() {
        final EditText searchEditText = (EditText)mActionView.findViewById(R.id.add_edit_text);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (
                    // editor with action
                        (i == EditorInfo.IME_ACTION_SEARCH) ||
                                // editor with Enter button
                                ((i == EditorInfo.IME_ACTION_UNSPECIFIED) && (keyEvent != null) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN))
                        ) {

                    if (mAddListener != null)
                        mAddListener.onAddFragmentInteraction(textView.getText().toString());
                    //clear search text for future
                    searchEditText.setText(null);
                    return true;
                }
                return false;
            }
        });
    }

    public interface OnAddInteractionListener {
        void onAddFragmentInteraction(String text);
    }
}
