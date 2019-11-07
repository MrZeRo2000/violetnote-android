package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;
import com.romanpulov.violetnote.view.helper.ActionHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BasicHEventCOItemFragment extends Fragment {
    //data
    private BasicNoteA mNote;
    private LongSparseArray<BasicHEventA> mHEvents = new LongSparseArray<>();
    private LongSparseArray<List<BasicHNoteCOItemA>> mHEventCOItems = new LongSparseArray<>();

    //controls
    private ExpandableListView mExListView;
    private BasicHEventCOItemExpandableListViewAdapter mExListViewAdapter;
    private ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> mViewSelector;

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
        mExListViewAdapter = new BasicHEventCOItemExpandableListViewAdapter(getContext(), mHEvents, mHEventCOItems, new ActionBarCallBack());
        mExListView.setAdapter(mExListViewAdapter);
        mViewSelector = mExListViewAdapter.getViewSelector();
        /*
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
         */

        return view;
    }

    private void updateTitle(@NonNull ActionMode mode) {
        mode.setTitle(DisplayTitleBuilder.buildItemsTitle(getContext(), mViewSelector.getSelectedItems()));
    }


    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //hideAddLayout();
            mode.getMenuInflater().inflate(R.menu.menu_listitem_minimal_actions, menu);
            if (mViewSelector.isSelected()) {
                updateTitle(mode);
            }

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            //hideAddLayout();
            //ActionHelper.updateActionMenu(menu, mViewSelector.getSelectedItems().size(), mBasicNoteValueData.getValues().size());
            updateTitle(mode);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Collection<BasicHNoteCOItemA> selectedNoteItems = mViewSelector.getSelectedItems();

            if (selectedNoteItems.size() > 0) {
                switch (item.getItemId()) {
                    case R.id.select_all:
                        //performSelectAll();
                        break;
                    case R.id.delete:
                        //performDeleteAction(mode, selectedNoteItems);
                        //hideAddLayout();
                        break;
                    case R.id.edit:
                        //performEditAction(mode, selectedNoteItems.get(0));
                        //mInputActionHelper.showLayout(selectedNoteItems.get(0).getValue(), InputActionHelper.INPUT_ACTION_TYPE_EDIT);
                        break;
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //hideAddLayout();
            if (mViewSelector != null)
                mViewSelector.destroyActionMode();
        }
    }

}
