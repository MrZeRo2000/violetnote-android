package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.PassDataViewModel;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

public abstract class PassDataBaseFragment extends Fragment {
    private final static String TAG = PassDataBaseFragment.class.getSimpleName();

    public final static int DATA_STATE_PASSWORD_REQUIRED = 0;
    public final static int DATA_STATE_LOADING = 1;
    public final static int DATA_STATE_LOADED = 2;
    public final static int DATA_STATE_LOAD_ERROR = 3;

    private View mIncludePasswordInput;
    private View mIncludeIndeterminateProgress;
    private View mIncludeDataList;
    private EditText mEditTextPassword;

    protected PassDataViewModel model;
    private int mDataState;

    protected int getDataState() {
        return mDataState;
    }

    protected void setDataState(int value) {
        mDataState = value;
        updateStateUI(value);
    }

    protected void updateStateUI(int dataState) {
        mIncludeIndeterminateProgress.setVisibility(dataState == DATA_STATE_LOADING ? View.VISIBLE : View.GONE);
        mIncludePasswordInput.setVisibility(
                dataState == DATA_STATE_PASSWORD_REQUIRED || dataState == DATA_STATE_LOAD_ERROR ? View.VISIBLE : View.GONE
        );
        mIncludeDataList.setVisibility(dataState == DATA_STATE_LOADED ? View.VISIBLE : View.GONE);
        setHasOptionsMenu(dataState == DATA_STATE_LOADED);
        if (dataState == DATA_STATE_PASSWORD_REQUIRED) {
            Log.d(TAG, "Show keyboard on updateStateUI");
            InputManagerHelper.focusAndShowDelayed(mEditTextPassword);
        }
    }

    protected void createViewBindings(View view) {
        mIncludePasswordInput = view.findViewById(R.id.include_password_input);
        mIncludeIndeterminateProgress = view.findViewById(R.id.include_indeterminate_progress);
        mIncludeDataList = view.findViewById(R.id.include_data_list);

        mEditTextPassword = view.findViewById(R.id.edit_text_password);
    }

    protected abstract int getViewLayoutId();

    protected RecyclerView.OnItemTouchListener mRecyclerViewTouchListenerForDataExpiration = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            model.enableDataExpiration();
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(getViewLayoutId(), container, false);
        createViewBindings(view);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(PassDataViewModel.class);

        mEditTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((v.getText().length() > 0) && (actionId == EditorInfo.IME_ACTION_GO)) {
                    //get the data
                    String password = v.getText().toString();

                    // update UI
                    InputManagerHelper.hideInput(v);
                    v.setText(null);

                    setDataState(DATA_STATE_LOADING);

                    // request data from model
                    model.setPassword(password);
                    model.loadPassData();

                    return true;
                }
                return false;
            }
        });

        if ((model.getPassDataResult().getValue() == null) || (model.getPassDataResult().getValue().getPassData() == null)) {
            setDataState(DATA_STATE_PASSWORD_REQUIRED);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        model.checkDataExpired();
    }
}
