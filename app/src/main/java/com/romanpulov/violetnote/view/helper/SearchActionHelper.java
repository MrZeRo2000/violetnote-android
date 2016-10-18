package com.romanpulov.violetnote.view.helper;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.OnSearchInteractionListener;

import java.util.Collection;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by romanpulov on 08.08.2016.
 */
public class SearchActionHelper implements CompoundButton.OnCheckedChangeListener {
    public final static int DISPLAY_TYPE_NONE = 0;
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

    public void setOnSearchConditionChangedListeber(OnSearchConditionChangedListener listener) {
        mSearchConditionChangedListener = listener;
    }

    public SearchActionHelper(View rootView, int displayType) {
        mSearchView = rootView.findViewById(R.id.search_layout_include);
        mSearchEditText =  (AutoCompleteTextView)mSearchView.findViewById(R.id.search_edit_text);
        mSearchSystemCheckBox = (CheckBox) mSearchView.findViewById(R.id.search_system_check);
        mSearchUserCheckBox = (CheckBox) mSearchView.findViewById(R.id.search_user_check);
        mDisplayType = displayType;

        setupCancelButton();
        setupSearchOptions();
        setupEditText();
    }

    private void setupCancelButton() {
        ImageButton searchCancelButton = (ImageButton)mSearchView.findViewById(R.id.cancel_button);
        searchCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideLayout();
            }
        });
    }

    private void setupSearchOptions() {
        mSearchSystemCheckBox.setOnCheckedChangeListener(this);
        mSearchUserCheckBox.setOnCheckedChangeListener(this);
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
                        ) {
                    hideLayout();

                    if (mSearchListener != null)
                        mSearchListener.onSearchFragmentInteraction(textView.getText().toString(), mSearchSystemCheckBox.isChecked(), mSearchUserCheckBox.isChecked());
                    //clear search text for future
                    mSearchEditText.setText(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void setAutoCompleteList(Collection<String> autoCompleteList) {
        ArrayAdapter<?> adapter = new ArrayAdapter<>(mSearchEditText.getContext(), android.R.layout.simple_dropdown_item_1line, autoCompleteList.toArray(new String[autoCompleteList.size()]));
        mSearchEditText.setAdapter(adapter);
    }

    public void showLayout() {
        //hide unneeded controls
        if (mDisplayType == 0) {
            mSearchView.findViewById(R.id.search_system_check).setVisibility(View.GONE);
            mSearchView.findViewById(R.id.search_user_check).setVisibility(View.GONE);
        }
        mSearchView.setVisibility(View.VISIBLE);
        if (mSearchEditText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) mSearchEditText.getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(mSearchEditText, 0);
        }
    }

    public void hideLayout() {
        InputMethodManager imm = (InputMethodManager) mSearchEditText.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText())
            imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);

        mSearchView.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mSearchConditionChangedListener != null)
            mSearchConditionChangedListener.onSearchConditionChanged(mSearchSystemCheckBox.isChecked(), mSearchUserCheckBox.isChecked());
    }

    public interface OnSearchConditionChangedListener {
        void onSearchConditionChanged(boolean isSearchSystem, boolean isSearchUser);
    }
}
