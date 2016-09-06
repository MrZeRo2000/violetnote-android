package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.romanpulov.violetnote.R;

/**
 * Created by romanpulov on 08.08.2016.
 */
public class SearchActionHelper {
    public final static int DISPLAY_TYPE_NONE = 0;
    public final static int DISPLAY_TYPE_SYSTEM_USER = 1;

    private final View mSearchView;
    private final int mDisplayType;
    private OnSearchInteractionListener mSearchListener;

    public void setOnSearchInteractionListener(OnSearchInteractionListener searchInteractionListener) {
        mSearchListener = searchInteractionListener;
    }

    public SearchActionHelper(View rootView, int displayType) {
        mSearchView = rootView.findViewById(R.id.search_layout_include);
        mDisplayType = displayType;

        setupCancelButton();
        setupEditText();
    }

    private void setupCancelButton() {
        ImageButton searchCancelButton = (ImageButton)mSearchView.findViewById(R.id.cancel_button);
        searchCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setVisibility(View.GONE);
            }
        });
    }

    private void setupEditText() {
        final EditText searchEditText = (EditText)mSearchView.findViewById(R.id.search_edit_text);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (
                    // editor with action
                        (i == EditorInfo.IME_ACTION_SEARCH) ||
                                // editor with Enter button
                                ((i == EditorInfo.IME_ACTION_UNSPECIFIED) && (keyEvent != null) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN))
                        ) {
                    mSearchView.setVisibility(View.GONE);

                    final CheckBox searchSystemCheckBox = (CheckBox) mSearchView.findViewById(R.id.search_system_check);
                    final CheckBox searchUserCheckBox = (CheckBox) mSearchView.findViewById(R.id.search_user_check);

                    if (mSearchListener != null)
                        mSearchListener.onSearchFragmentInteraction(textView.getText().toString(), searchSystemCheckBox.isChecked(), searchUserCheckBox.isChecked());
                    //clear search text for future
                    searchEditText.setText(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void showLayout() {
        //hide unneeded controls
        if (mDisplayType == 0) {
            mSearchView.findViewById(R.id.search_system_check).setVisibility(View.GONE);
            mSearchView.findViewById(R.id.search_user_check).setVisibility(View.GONE);
        }
        mSearchView.setVisibility(View.VISIBLE);
        if (mSearchView.findViewById(R.id.search_edit_text).requestFocus())
            ((Activity)mSearchView.getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
