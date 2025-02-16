package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PassDataBaseFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

public class PassDataNoteDetailsFragment extends PassDataBaseFragment {
    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_pass_data_note;
    }

    @Override
    protected void loadModelData() {
        model.loadPassDataNote();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model.getExpireHelper().setLiveData(model.getPassDataNoteResult());

        model.getPassDataNoteResult().observe(getViewLifecycleOwner(), passDataResult -> {
            if (validatePassDataResult(view, passDataResult)) {
                setDataState(DATA_STATE_LOADED);

                // got the data, setting expiration
                model.getExpireHelper().initDataExpiration();

                RecyclerView recyclerView = view.findViewById(R.id.list);

                // Set the adapter
                Context context = view.getContext();
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                recyclerView.setAdapter(new NoteDetailsRecyclerViewAdapter(passDataResult.getPassData().getPassNoteData().get(0).getNoteAttrList()));

                // add decoration
                recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

                // add data expiration handler
                recyclerView.addOnItemTouchListener(mRecyclerViewTouchListenerForDataExpiration);
            }
        });
    }

}
