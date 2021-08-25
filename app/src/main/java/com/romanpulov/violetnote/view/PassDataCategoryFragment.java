package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentPassDataCategoryBinding;
import com.romanpulov.violetnote.model.PassDataViewModel;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

public class PassDataCategoryFragment extends Fragment {
    private final static String TAG = PassDataCategoryFragment.class.getSimpleName();

    private final static int DATA_STATE_PASSWORD_REQUIRED = 0;
    private final static int DATA_STATE_LOADING = 1;
    private final static int DATA_STATE_LOADED = 2;
    private final static int DATA_STATE_LOAD_ERROR = 3;

    private FragmentPassDataCategoryBinding binding;
    private PassDataViewModel model;
    private int mDataState;

    protected int getDataState() {
        return mDataState;
    }

    protected void setDataState(int value) {
        mDataState = value;
        updateStateUI(value);
    }

    protected void updateStateUI(int dataState) {
        binding.includeIndeterminateProgress.getRoot().setVisibility(dataState == DATA_STATE_LOADING ? View.VISIBLE : View.GONE);
        binding.includePasswordInput.getRoot().setVisibility(
                dataState == DATA_STATE_PASSWORD_REQUIRED || dataState == DATA_STATE_LOAD_ERROR ? View.VISIBLE : View.GONE
        );
        binding.includeCategoryList.getRoot().setVisibility(dataState == DATA_STATE_LOADED ? View.VISIBLE : View.GONE);
        setHasOptionsMenu(dataState == DATA_STATE_LOADED);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentPassDataCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(PassDataViewModel.class);
        model.getPassDataResult().observe(getViewLifecycleOwner(), passDataResult -> {
            //Toast.makeText(getContext(), "PassData observed:" + (passDataResult.getPassData() == null ? "null" : passDataResult.getPassData().toString()), Toast.LENGTH_SHORT).show();

            //binding.includeIndeterminateProgress.getRoot().setVisibility(View.GONE);
            if (passDataResult.getPassData() == null) {
                setDataState(DATA_STATE_LOAD_ERROR);

                //binding.includePasswordInput.getRoot().setVisibility(View.VISIBLE);

                if (passDataResult.getLoadErrorText() != null) {
                    Snackbar.make(view, passDataResult.getLoadErrorText(), 2000)
                            .setTextColor(getResources().getColor(R.color.colorError))
                            .show();
                }
            } else {
                setDataState(DATA_STATE_LOADED);

                RecyclerView recyclerView = view.findViewById(R.id.list);

                // Set the adapter
                Context context = view.getContext();
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                recyclerView.setAdapter(new CategoryRecyclerViewAdapter(passDataResult.getPassData().getPassCategoryData(), item -> {

                }));

                // add decoration
                recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));
            }
        });

        binding.includePasswordInput.editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        if ((model.getPassDataResult().getValue() == null) || (model.getPassDataResult().getValue().getPassData() == null)) {
            setDataState(DATA_STATE_PASSWORD_REQUIRED);
            InputManagerHelper.focusAndShowDelayed(binding.includePasswordInput.editTextPassword);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pass_category, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Log.d(TAG, "requested search");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        model.checkDataExpired();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}