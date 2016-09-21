package com.romanpulov.violetnote.view.helper;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.util.Collection;

/**
 * Created by romanpulov on 06.09.2016.
 */
public class AddActionHelper {

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

        mListButton = (ImageButton) mActionView.findViewById(R.id.list_button);
        mCancelButton = (ImageButton) mActionView.findViewById(R.id.cancel_button);
        mAddEditText = (AutoCompleteTextView)mActionView.findViewById(R.id.add_edit_text);

        setupListButton();
        setupCancelButton();
        setupEditText();
    }

    public void setAutoCompleteList(Collection<String> autoCompleteList) {
        ArrayAdapter<?> adapter = new ArrayAdapter<>(mAddEditText.getContext(), android.R.layout.simple_dropdown_item_1line, autoCompleteList.toArray(new String[autoCompleteList.size()]));
        mAddEditText.setAdapter(adapter);
    }

    private void setupListButton() {
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListClickListener != null)
                    mListClickListener.onClick(v);
            }
        });
    }

    private void setupCancelButton() {
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionView.setVisibility(View.GONE);
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

                    if (mAddListener != null)
                        mAddListener.onAddFragmentInteraction(textView.getText().toString());
                    //clear search text for future
                    mAddEditText.setText(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void showLayout() {
        mActionView.setVisibility(View.VISIBLE);
        if (mActionView.findViewById(R.id.add_edit_text).requestFocus())
            ((Activity)mActionView.getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void hideLayout() {
        mActionView.setVisibility(View.GONE);
    }

    public interface OnAddInteractionListener {
        void onAddFragmentInteraction(String text);
    }
}
