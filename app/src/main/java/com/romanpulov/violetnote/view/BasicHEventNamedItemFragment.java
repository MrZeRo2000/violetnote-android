package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBHManager;
import com.romanpulov.violetnote.model.BasicHNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

public class BasicHEventNamedItemFragment extends Fragment {

    private BasicNoteItemA mNoteItem;
    private final List<BasicHNoteItemA> mHNoteItemList = new ArrayList<>();

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
