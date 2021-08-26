package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PassDataBaseFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.SearchActionHelper;

public class PassDataNoteFragment extends PassDataBaseFragment {
    private final String TAG = PassDataNoteFragment.class.getSimpleName();

    private SearchActionHelper mSearchActionHelper;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_pass_data_note;
    }

    @Override
    protected void loadModelData() {
        model.loadPassDataSelectedByCategory();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expireModel.setLiveData(model.getPassDataSelectedByCategory());

        model.getPassDataSelectedByCategory().observe(getViewLifecycleOwner(), passDataResult -> {
            if (validatePassDataResult(view, passDataResult)) {
                setDataState(DATA_STATE_LOADED);

                // got the data, setting expiration
                expireModel.initDataExpiration();

                // TODO remove headerTextView from the layout
                TextView headerTextView = view.findViewById(R.id.headerTextView);
                // headerTextView.setText(passDataResult.getPassData().getPassCategoryData().get(0).getCategoryName());
                headerTextView.setVisibility(View.GONE);

                // set title
                setActivityTitle(passDataResult.getPassData().getPassCategoryData().get(0).getCategoryName());

                // setup RecycleView
                RecyclerView recyclerView = view.findViewById(R.id.list);

                // Set the adapter
                Context context = view.getContext();
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new NoteRecyclerViewAdapter(passDataResult.getPassData().getPassNoteData(), item -> {
                    //TODO select note action
                }));

                // add decoration
                recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_orange_black_gradient));

                // add data expiration handler
                recyclerView.addOnItemTouchListener(mRecyclerViewTouchListenerForDataExpiration);

                // setup search action helper
                if (mSearchActionHelper == null) {
                    mSearchActionHelper = new SearchActionHelper(view, SearchActionHelper.DISPLAY_TYPE_SYSTEM_USER);
                    mSearchActionHelper.setOnSearchInteractionListener((searchText, isSearchSystem, isSearchUser) -> {
                        //TODO search result reaction
                        Log.d(TAG, "Search text:" + searchText);
                    });

                    // setup search action helper
                    mSearchActionHelper.setAutoCompleteList(passDataResult.getPassData().getSearchValues(true, true));
                    mSearchActionHelper.setOnSearchConditionChangedListener((isSearchSystem, isSearchUser) -> {
                        mSearchActionHelper.setAutoCompleteList(passDataResult.getPassData().getSearchValues(isSearchSystem, isSearchUser));
                    });
                } else {
                    mSearchActionHelper.hideLayout();
                }
            }
        });
    }

    @Override
    public void onPause() {
        if (mSearchActionHelper != null)
            mSearchActionHelper.hideLayout();
        super.onPause();
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
