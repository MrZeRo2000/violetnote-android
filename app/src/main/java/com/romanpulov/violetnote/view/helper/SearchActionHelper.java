package com.romanpulov.violetnote.view.helper;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.OnSearchInteractionListener;

import java.util.Collection;

/**
 * Search action helper class
 * Created by romanpulov on 08.08.2016.
 */
public class SearchActionHelper {
    public final static int DISPLAY_TYPE_SYSTEM_USER = 1;

    private final View mSearchView;
    private final AutoCompleteTextView mSearchEditText;
    private final CheckBox mSearchSystemCheckBox;
    private final CheckBox mSearchUserCheckBox;
    private final int mDisplayType;
    private OnSearchInteractionListener mSearchListener;
    private OnSearchConditionChangedListener mSearchConditionChangedListener;

    public void setOnSearchInteractionListener(OnSearchInteractionListener listener) {
        mSearchListener = listener;
    }

    public void setOnSearchConditionChangedListener(OnSearchConditionChangedListener listener) {
        mSearchConditionChangedListener = listener;
    }

    public SearchActionHelper(View rootView, int displayType) {
        mSearchView = rootView.findViewById(R.id.search_layout_include);
        mSearchEditText = mSearchView.findViewById(R.id.search_edit_text);
        mSearchSystemCheckBox = mSearchView.findViewById(R.id.search_system_check);
        mSearchUserCheckBox = mSearchView.findViewById(R.id.search_user_check);
        mDisplayType = displayType;

        setupCancelButton();
        setupSearchOptions();
        setupEditText();
    }

    private void setupCancelButton() {
        ImageButton searchCancelButton = mSearchView.findViewById(R.id.cancel_button);
        searchCancelButton.setOnClickListener(view -> hideLayout());
    }

    private void setupSearchOptions() {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
            if (mSearchConditionChangedListener != null) {
                mSearchConditionChangedListener.onSearchConditionChanged(
                        mSearchSystemCheckBox.isChecked(),
                        mSearchUserCheckBox.isChecked()
                );
            }
        };

        mSearchSystemCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
        mSearchUserCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private void setupEditText() {
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (
                    // editor with action
                    (i == EditorInfo.IME_ACTION_SEARCH) ||
                    // editor with Enter button
                    ((i == EditorInfo.IME_ACTION_UNSPECIFIED) && (keyEvent != null) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN))
                )
                {
                    // get search text
                    String searchText = textView.getText().toString();

                    // update UI
                    hideLayout();
                    mSearchEditText.setText(null);

                    // return data
                    if (mSearchListener != null) {
                        mSearchListener.onSearchFragmentInteraction(searchText, mSearchSystemCheckBox.isChecked(), mSearchUserCheckBox.isChecked());
                    }

                    return true;
                }
                return false;
            }
        });
    }

    public void setAutoCompleteList(Collection<String> autoCompleteList) {
        ArrayAdapter<?> adapter = new ArrayAdapter<>(
                mSearchEditText.getContext(),
                android.R.layout.simple_dropdown_item_1line,
                autoCompleteList.toArray(new String[0])
        );
        mSearchEditText.setAdapter(adapter);
    }

    public void showLayout() {
        //hide unneeded controls
        if (mDisplayType == 0) {
            mSearchView.findViewById(R.id.search_system_check).setVisibility(View.GONE);
            mSearchView.findViewById(R.id.search_user_check).setVisibility(View.GONE);
        }
        mSearchView.setVisibility(View.VISIBLE);

        InputManagerHelper.focusAndShowInput(mSearchEditText);
    }

    public void hideLayout() {
        InputManagerHelper.hideInput(mSearchEditText);
        mSearchView.setVisibility(View.GONE);
    }

    public interface OnSearchConditionChangedListener {
        void onSearchConditionChanged(boolean isSearchSystem, boolean isSearchUser);
    }
}
