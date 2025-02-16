package com.romanpulov.violetnote.view.helper;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.utils.InputParserUtils;
import com.romanpulov.violetnote.view.AutoCompleteArrayAdapter;

import java.util.Collection;

/**
 * Helper class for add action
 * Created by romanpulov on 06.09.2016.
 */
public class InputActionHelper implements AutoCompleteArrayAdapter.OnAutoCompleteTextListener {
    private final static String TAG = InputActionHelper.class.getSimpleName();

    public static final int INPUT_ACTION_TYPE_ADD = 0;
    public static final int INPUT_ACTION_TYPE_EDIT = 1;
    public static final int INPUT_ACTION_TYPE_NUMBER = 2;

    private final View mActionView;
    private final ImageButton mListButton;
    private final ImageButton mCancelButton;
    private final ImageButton mCalendarButton;
    private final AutoCompleteTextView mInputEditText;

    private int mActionType;
    private Collection<String> mAutoCompleteList;

    private OnInputInteractionListener mInputListener;
    private OnCancelInteractionListener mCancelListener;
    private View.OnClickListener mListClickListener;

    public void setOnInputInteractionListener(OnInputInteractionListener listener) {
        mInputListener = listener;
    }

    /** @noinspection unused*/
    public void setOnCancelListener(OnCancelInteractionListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public void setOnListClickListener(View.OnClickListener listener) {
        mListClickListener = listener;
        if (mListClickListener != null) {
            mListButton.setVisibility(View.VISIBLE);
        } else {
            mListButton.setVisibility(View.GONE);
        }
    }

    public InputActionHelper(View actionView) {
        mActionView = actionView;

        mListButton = mActionView.findViewById(R.id.list_button);
        mCancelButton = mActionView.findViewById(R.id.cancel_button);
        mCalendarButton = mActionView.findViewById(R.id.calendar_button);
        mInputEditText = mActionView.findViewById(R.id.add_edit_text);

        setupListButton();
        setupCancelButton();
        setupCalendarButton();
        setupEditText();
    }

    public void setAutoCompleteList(Collection<String> autoCompleteList) {
        mAutoCompleteList = autoCompleteList;
    }

    private void prepareAutoCompleteList() {
        if (mAutoCompleteList != null) {
            ArrayAdapter<?> adapter = new AutoCompleteArrayAdapter(mInputEditText.getContext(), R.layout.dropdown_button_item, mAutoCompleteList.toArray(new String[0]), this);
            mInputEditText.setAdapter(adapter);
        }
    }

    private void clearAutoCompleteList() {
        mInputEditText.setAdapter(null);
    }

    private void setupListButton() {
        mListButton.setOnClickListener(v -> {
            if (mListClickListener != null) {
                mListClickListener.onClick(v);
            }
        });
    }

    private void setupCancelButton() {
        mCancelButton.setOnClickListener(view -> {
            hideLayout();
            if (mCancelListener != null) {
                mCancelListener.onCancelInteraction();
            }
        });
    }

    private void setupCalendarButton() {
        mCalendarButton.setOnClickListener(view -> {
            Log.d(TAG, "Setting text to" + InputParserUtils.getCurrentDateAsString());
            mInputEditText.setText(InputParserUtils.getCurrentDateAsString());
        });
    }

    private void setupEditText() {
        mInputEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (
                // editor with action
                (i == EditorInfo.IME_ACTION_GO) ||
                // editor with Enter button
                ((keyEvent != null) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN))) {
                    acceptText(textView.getText().toString());
                    return true;
            }
            return false;
        });
    }

    private void acceptText(String text) {
        if ((mInputListener != null) && (text != null) && (!text.trim().isEmpty()))
            mInputListener.onInputInteraction(mActionType, text.trim());
        //clear search text for future
        mInputEditText.setText(null);
    }

    public void showAddLayout() {
        showLayout(null, INPUT_ACTION_TYPE_ADD);
    }

    public void showEditLayout(String text) {
        showLayout(text, INPUT_ACTION_TYPE_EDIT);
    }

    public void showEditNumberLayout(long number, int numberDisplayStyle) {
        showLayout(number == 0 ? null : InputParserUtils.getDisplayValue(number, numberDisplayStyle), INPUT_ACTION_TYPE_NUMBER);
    }

    private void showLayout(String text, int actionType) {
        mActionType = actionType;

        mInputEditText.setText(text);
        mCalendarButton.setVisibility(
                actionType == INPUT_ACTION_TYPE_EDIT &&
                        InputParserUtils.isDate(text) &&
                        !(InputParserUtils.getCurrentDateAsString().equals(text)) ?
                            View.VISIBLE :
                            View.GONE);
        Log.d(TAG, "CalendarButton visibility: " + mCalendarButton.getVisibility() + ", text: " + text);

        switch (actionType) {
            case INPUT_ACTION_TYPE_ADD:
                mInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                prepareAutoCompleteList();
                break;
            case INPUT_ACTION_TYPE_EDIT:
                mInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                // mCalendarButton.setVisibility(View.VISIBLE);
                clearAutoCompleteList();
                break;
            case INPUT_ACTION_TYPE_NUMBER:
                mInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                clearAutoCompleteList();
                break;
        }

        String[] hints = mInputEditText.getContext().getResources().getStringArray(R.array.hint_action_entries);
        mInputEditText.setHint(hints[mActionType]);

        if (text != null) {
            mInputEditText.setSelection(text.length());
        }

        mActionView.setVisibility(View.VISIBLE);
        if (mInputEditText.requestFocus()) {
            //InputManagerHelper.toggleInputForced(mInputEditText.getContext());
            InputManagerHelper.showInput(mInputEditText);
        }
    }

    public void hideLayout() {
        if (mActionView.getVisibility() == View.VISIBLE) {
            InputManagerHelper.hideInput(mActionView);
            mActionView.setVisibility(View.GONE);
            //clear search text for future
            mInputEditText.setText(null);
        }
    }

    @Override
    public void onSelectText(String text) {
        mInputEditText.setText(text);
        if (text != null) {
            mInputEditText.setSelection(text.length());
        }
        mInputEditText.dismissDropDown();
    }

    @Override
    public void onCheckText(String text) {
        acceptText(text);
        mInputEditText.dismissDropDown();
    }

    public interface OnInputInteractionListener {
        void onInputInteraction(int actionType, String text);
    }

    public interface OnCancelInteractionListener {
        void onCancelInteraction();
    }
}
