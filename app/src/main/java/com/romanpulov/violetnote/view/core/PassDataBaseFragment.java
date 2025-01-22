package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.PassDataA;
import com.romanpulov.violetnote.model.core.ExpireViewModel;
import com.romanpulov.violetnote.model.vm.PassDataViewModel;
import com.romanpulov.violetnote.view.OnSearchInteractionListener;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;
import com.romanpulov.violetnote.view.helper.SearchActionHelper;

public abstract class PassDataBaseFragment extends Fragment {
    private final static String TAG = PassDataBaseFragment.class.getSimpleName();

    public final static int DATA_STATE_PASSWORD_REQUIRED = 0;
    public final static int DATA_STATE_LOADING = 1;
    public final static int DATA_STATE_LOADED = 2;
    public final static int DATA_STATE_LOAD_ERROR = 3;

    protected SearchActionHelper mSearchActionHelper;

    private View mIncludePasswordInput;
    private View mIncludeIndeterminateProgress;
    private View mIncludeDataList;
    private EditText mEditTextPassword;

    protected PassDataViewModel model;
    protected ExpireViewModel expireModel;

    private final OnBackPressedCallback mBackPressedFinishCallback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            requireActivity().finish();
        }
    };

    protected void setDataState(int value) {
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
        this.mBackPressedFinishCallback.setEnabled(dataState == DATA_STATE_PASSWORD_REQUIRED);
    }

    protected void createViewBindings(View view) {
        mIncludePasswordInput = view.findViewById(R.id.include_password_input);
        mIncludeIndeterminateProgress = view.findViewById(R.id.include_indeterminate_progress);
        mIncludeDataList = view.findViewById(R.id.include_data_list);

        mEditTextPassword = view.findViewById(R.id.edit_text_password);
    }

    protected abstract int getViewLayoutId();

    protected abstract void loadModelData();

    protected final RecyclerView.OnItemTouchListener mRecyclerViewTouchListenerForDataExpiration = new RecyclerView.OnItemTouchListener() {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, mBackPressedFinishCallback);
    }

    @Override
    public void onDestroy() {
        mBackPressedFinishCallback.remove();
        super.onDestroy();
    }

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
        expireModel = new ViewModelProvider(requireActivity()).get(ExpireViewModel.class);

        mEditTextPassword.setOnEditorActionListener((v, actionId, event) -> {
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
        });

        if ((model.getPassDataResult().getValue() == null) || (model.getPassDataResult().getValue().getPassData() == null)) {
            setDataState(DATA_STATE_PASSWORD_REQUIRED);
        }
    }

    @Override
    public void onPause() {
        if (mSearchActionHelper != null)
            mSearchActionHelper.hideLayout();
        super.onPause();
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

    protected void setupSearchActionHelper(View view, PassDataA passDataA, OnSearchInteractionListener listener) {
        if ((mSearchActionHelper == null) || (mSearchActionHelper.getRootView() != view)) {
            mSearchActionHelper = new SearchActionHelper(view, SearchActionHelper.DISPLAY_TYPE_SYSTEM_USER);
            mSearchActionHelper.setOnSearchInteractionListener(listener);

            // setup search action helper
            mSearchActionHelper.setAutoCompleteList(passDataA.getSearchValues(true, true));
            mSearchActionHelper.setOnSearchConditionChangedListener((isSearchSystem, isSearchUser) ->
                    mSearchActionHelper.setAutoCompleteList(passDataA.getSearchValues(isSearchSystem, isSearchUser)));
        } else {
            mSearchActionHelper.hideLayout();
            mSearchActionHelper.clearSearchText();
        }
    }
}
