package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBHManager;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicHNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

public class BasicHEventNamedItemFragment extends Fragment {

    private BasicNoteItemA mNoteItem;
    private List<BasicHNoteItemA> mHNoteItemList = new ArrayList<>();

    public static BasicHEventNamedItemFragment newInstance(BasicNoteItemA noteItem) {
        BasicHEventNamedItemFragment instance = new BasicHEventNamedItemFragment();

        Bundle args = new Bundle();
        args.putParcelable(BasicNoteItemA.class.getName(), noteItem);
        instance.setArguments(args);

        return instance;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicHEventNamedItemFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mNoteItem = args.getParcelable(BasicNoteItemA.class.getName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler_view_list, container, false);

        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        refreshList(new DBHManager(context));

        BasicHEventNamedItemRecyclerViewAdapter recyclerViewAdapter = new BasicHEventNamedItemRecyclerViewAdapter(context, mHNoteItemList);

        recyclerView.setAdapter(recyclerViewAdapter);

        // add decoration
        recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        return view;
    }

    public void refreshList(DBHManager hManager) {
        mHNoteItemList.clear();
        mHNoteItemList.addAll(hManager.mBasicHNoteItemDAO.getByNoteItemIdWithEvents(mNoteItem.getId()));
    }

}
