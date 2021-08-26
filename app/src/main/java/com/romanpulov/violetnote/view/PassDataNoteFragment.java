package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PassDataBaseFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

public class PassDataNoteFragment extends PassDataBaseFragment {
    private final String TAG = PassDataNoteFragment.class.getSimpleName();

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

                TextView headerTextView = view.findViewById(R.id.headerTextView);
                headerTextView.setText(passDataResult.getPassData().getPassCategoryData().get(0).getCategoryName());

                RecyclerView recyclerView = view.findViewById(R.id.list);

                // Set the adapter
                Context context = view.getContext();
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new NoteRecyclerViewAdapter(passDataResult.getPassData().getPassNoteData(), item -> {

                }));

                // add decoration
                recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_orange_black_gradient));

                // add data expiration handler
                recyclerView.addOnItemTouchListener(mRecyclerViewTouchListenerForDataExpiration);
            }
        });
    }
}
