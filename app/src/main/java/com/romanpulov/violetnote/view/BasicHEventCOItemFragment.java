package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBHManager;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemA;
import com.romanpulov.violetnote.model.BasicNoteA;

import java.util.ArrayList;
import java.util.List;

public class BasicHEventCOItemFragment extends Fragment {
    //data
    private BasicNoteA mNote;
    private LongSparseArray<BasicHEventA> mHEvents = new LongSparseArray<>();
    private LongSparseArray<List<BasicHNoteCOItemA>> mHEventCOItems = new LongSparseArray<>();

    //controls
    private ExpandableListView mExListView;
    private BasicHEventCOItemExpandableListViewAdapter mExListViewAdapter;

    public static BasicHEventCOItemFragment newInstance(BasicNoteA note) {
        BasicHEventCOItemFragment instance = new BasicHEventCOItemFragment();

        Bundle args = new Bundle();
        args.putParcelable(BasicNoteA.class.getName(), note);
        instance.setArguments(args);

        return instance;
    }

    public void refreshList(DBHManager hManager) {
        List<BasicHEventA> hEventList = hManager.mBasicHEventDAO.getByCOItemsNoteId(mNote.getId());
        List<BasicHNoteCOItemA> hCOItemList = hManager.mBasicHNoteCOItemDAO.getByNoteId(mNote.getId());

        BasicHEventA.fillArrayFromList(mHEvents, hEventList);
        BasicHNoteCOItemA.fillArrayFromList(mHEventCOItems, hCOItemList);

    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicHEventCOItemFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mNote = args.getParcelable(BasicNoteA.class.getName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_basic_h_event_expandable_list, container, false);

        refreshList(new DBHManager(view.getContext()));

        mExListView = view.findViewById(R.id.ex_list);
        mExListViewAdapter = new BasicHEventCOItemExpandableListViewAdapter(getContext(), mHEvents, mHEventCOItems);
        mExListView.setAdapter(mExListViewAdapter);
        mExListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ExpandableListView exListView = (ExpandableListView) parent;
                long pos = exListView.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(pos);
                int groupPos = ExpandableListView.getPackedPositionGroup(pos);
                int childPos = ExpandableListView.getPackedPositionChild(pos);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    BasicHNoteCOItemA item = (BasicHNoteCOItemA)(exListView.getExpandableListAdapter()).getChild(groupPos, childPos);
                    Toast.makeText(getContext(), "long clicked:" + item, Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    //Toast.makeText(getContext(), "long click id=" + id + ", position=" + position, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        return view;
    }
}
