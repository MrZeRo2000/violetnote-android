package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PassDataBaseFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

public class PassDataCategoryFragment extends PassDataBaseFragment {
    private final static String TAG = PassDataCategoryFragment.class.getSimpleName();

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_pass_data_category;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model.getPassDataResult().observe(getViewLifecycleOwner(), passDataResult -> {
            if (passDataResult.getPassData() == null) {
                if (passDataResult.getLoadErrorText() == null) {
                    setDataState(DATA_STATE_PASSWORD_REQUIRED);
                } else {
                    setDataState(DATA_STATE_LOAD_ERROR);

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

                // add data expiration handler
                recyclerView.addOnItemTouchListener(mRecyclerViewTouchListenerForDataExpiration);
            }
        });

        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
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
}