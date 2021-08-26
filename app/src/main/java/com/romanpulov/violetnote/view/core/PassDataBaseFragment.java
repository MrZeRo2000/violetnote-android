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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.PassDataExpireViewModel;
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
    protected PassDataExpireViewModel expireModel;

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

    protected abstract void loadModelData();

    protected RecyclerView.OnItemTouchListener mRecyclerViewTouchListenerForDataExpiration = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            expireModel.prolongDataExpiration();
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
        expireModel = new ViewModelProvider(requireActivity()).get(PassDataExpireViewModel.class);

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

                    loadModelData();

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
        if (!expireModel.checkDataExpired()) {
            expireModel.prolongDataExpiration();
        }
    }

    protected boolean validatePassDataResult(View view, PassDataViewModel.PassDataResult passDataResult) {
        if (passDataResult == null) {
            setDataState(DATA_STATE_PASSWORD_REQUIRED);
            return false;
        } else if (passDataResult.getPassData() == null) {
            if (passDataResult.getLoadErrorText() == null) {
                setDataState(DATA_STATE_PASSWORD_REQUIRED);
                return false;
            } else {
                setDataState(DATA_STATE_LOAD_ERROR);

                Snackbar.make(view, passDataResult.getLoadErrorText(), 2000)
                        .setTextColor(getResources().getColor(R.color.colorError))
                        .show();
                return false;
            }
        } else {
            return true;
        }
    }

    protected void setActivityTitle(String title) {
        if ((getActivity() instanceof AppCompatActivity)){
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }
}
