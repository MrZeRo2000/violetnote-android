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
    public static final int ADD_ACTION_TYPE_ADD = 0;
    public static final int ADD_ACTION_TYPE_EDIT = 1;

    private final View mActionView;
    private final ImageButton mListButton;
    private final ImageButton mCancelButton;
    private final AutoCompleteTextView mAddEditText;

    private int mActionType;

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
            mAddListener.onAddFragmentInteraction(mActionType, text);
        //clear search text for future
        mAddEditText.setText(null);
    }

    public void showLayout(String text) {
        mActionType = text == null ? ADD_ACTION_TYPE_ADD : ADD_ACTION_TYPE_EDIT;
        mAddEditText.setText(text);

        String[] hints = mAddEditText.getContext().getResources().getStringArray(R.array.hint_action_entries);
        mAddEditText.setHint(hints[mActionType]);

        if (text != null)
            mAddEditText.setSelection(text.length());

        mActionView.setVisibility(View.VISIBLE);
        if (mAddEditText.requestFocus())
            InputManagerHelper.toggleInputForced(mAddEditText.getContext());
    }

    public void hideLayout() {
        InputManagerHelper.hideInput(mActionView);
        mActionView.setVisibility(View.GONE);
        //clear search text for future
        mAddEditText.setText(null);
    }

    @Override
    public void onSelectText(String text) {
        mAddEditText.setText(text);
        if (text != null)
            mAddEditText.setSelection(text.length());
        mAddEditText.dismissDropDown();
    }

    @Override
    public void onCheckText(String text) {
        acceptText(text);
        mAddEditText.dismissDropDown();
    }

    public interface OnAddInteractionListener {
        void onAddFragmentInteraction(int actionType, String text);
    }
}
