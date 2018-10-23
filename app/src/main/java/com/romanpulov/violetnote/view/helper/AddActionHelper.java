package com.romanpulov.violetnote.view.helper;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.AutoCompleteArrayAdapter;

import java.util.Collection;

/**
 * Helper class for add action
 * Created by romanpulov on 06.09.2016.
 */
public class AddActionHelper implements AutoCompleteArrayAdapter.OnAutoCompleteTextListener {

    private final View mActionView;
    private final ImageButton mListButton;
    private final ImageButton mCancelButton;
    private final AutoCompleteTextView mAddEditText;

    private OnAddInteractionListener mAddListener;
    private View.OnClickListener mListClickListener;

    public void setOnAddInteractionListener(OnAddInteractionListener listener) {
        mAddListener = listener;
    }

    public void setOnListClickListener(View.OnClickListener listener) {
        mListClickListener = listener;
        if (mListClickListener != null)
            mListButton.setVisibility(View.VISIBLE);
        else
            mListButton.setVisibility(View.GONE);
    }

    public AddActionHelper(View actionView) {
        mActionView = actionView;

        mListButton = mActionView.findViewById(R.id.list_button);
        mCancelButton = mActionView.findViewById(R.id.cancel_button);
        mAddEditText = mActionView.findViewById(R.id.add_edit_text);

        setupListButton();
        setupCancelButton();
        setupEditText();
    }

    public void setAutoCompleteList(Collection<String> autoCompleteList) {
        //ArrayAdapter<?> adapter = new ArrayAdapter<>(mAddEditText.getContext(), android.R.layout.simple_dropdown_item_1line, autoCompleteList.toArray(new String[autoCompleteList.size()]));
        ArrayAdapter<?> adapter = new AutoCompleteArrayAdapter(mAddEditText.getContext(), R.layout.dropdown_button_item, autoCompleteList.toArray(new String[0]), this);
        //ArrayAdapter<?> adapter = new AutoCompleteArrayAdapter(mAddEditText.getContext(), android.R.layout.simple_dropdown_item_1line, autoCompleteList.toArray(new String[autoCompleteList.size()]));
        mAddEditText.setAdapter(adapter);
    }

    private void setupListButton() {
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListClickListener != null) {
                    mListClickListener.onClick(v);
                }
            }
        });
    }

    private void setupCancelButton() {
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideLayout();
            }
        });
    }

    private void setupEditText() {
        mAddEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (
                    // editor with action
                        (i == EditorInfo.IME_ACTION_SEARCH) ||
                                // editor with Enter button
                                ((i == EditorInfo.IME_ACTION_UNSPECIFIED) && (keyEvent != null) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN))
                        ) {
                    acceptText(textView.getText().toString());

                    return true;
                }
                return false;
            }
        });
    }

    private void acceptText(String text) {
        if ((mAddListener != null) && (text != null) && (text.trim().length() > 0))
            mAddListener.onAddFragmentInteraction(text);
        //clear search text for future
        mAddEditText.setText(null);
    }


    public void showLayout() {
        mActionView.setVisibility(View.VISIBLE);
        if (mActionView.findViewById(R.id.add_edit_text).requestFocus())
            InputManagerHelper.showInput(mAddEditText);
    }

    public void hideLayout() {
        InputManagerHelper.hideInput(mActionView);
        mActionView.setVisibility(View.GONE);
    }

    @Override
    public void onSelectText(String text) {
        mAddEditText.setText(text);
        mAddEditText.dismissDropDown();
    }

    @Override
    public void onCheckText(String text) {
        acceptText(text);
        mAddEditText.dismissDropDown();
    }

    public interface OnAddInteractionListener {
        void onAddFragmentInteraction(String text);
    }
}
