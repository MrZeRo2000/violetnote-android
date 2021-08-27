package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PassDataBaseFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.SearchActionHelper;

public class PassDataCategoryFragment extends PassDataBaseFragment {
    private final static String TAG = PassDataCategoryFragment.class.getSimpleName();

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_pass_data_category;
    }

    @Override
    protected void loadModelData() {
        model.loadPassData();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expireModel.setLiveData(model.getPassDataResult());

        model.getPassDataResult().observe(getViewLifecycleOwner(), passDataResult -> {
            if (validatePassDataResult(view, passDataResult)) {
                setDataState(DATA_STATE_LOADED);

                // got the data, setting expiration
                expireModel.initDataExpiration();

                RecyclerView recyclerView = view.findViewById(R.id.list);

                // Set the adapter
                Context context = view.getContext();
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                recyclerView.setAdapter(new CategoryRecyclerViewAdapter(passDataResult.getPassData().getPassCategoryData(), item -> {
                    if (!expireModel.checkDataExpired()) {
                        model.selectPassDataByCategory(item);
                        NavHostFragment.findNavController(PassDataCategoryFragment.this)
                                .navigate(R.id.action_PassDataCategoryFragment_to_PassDataNoteFragment);
                        expireModel.prolongDataExpiration();
                    }
                }));

                // add decoration
                recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

                // add data expiration handler
                recyclerView.addOnItemTouchListener(mRecyclerViewTouchListenerForDataExpiration);

                // setup search action helper
                setupSearchActionHelper(view, passDataResult.getPassData(), (searchText, isSearchSystem, isSearchUser) -> {
                    model.searchPassData(searchText, isSearchSystem, isSearchUser);
                    NavHostFragment.findNavController(PassDataCategoryFragment.this)
                            .navigate(R.id.action_PassDataCategoryFragment_to_PassDataSearchResultFragment);
                    expireModel.prolongDataExpiration();
                });
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
            if (mSearchActionHelper != null) {
                mSearchActionHelper.showLayout();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}