package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class BasicNoteGroupFragment extends BasicCommonNoteFragment {

    private final List<BasicNoteGroupA> mBasicNoteGroupList = new ArrayList<>();
    private OnBasicNoteGroupFragmentInteractionListener mListener;

    public static BasicNoteGroupFragment newInstance() {
        return new BasicNoteGroupFragment();
    }

    public BasicNoteGroupFragment() {

    }

    @Override
    public void refreshList(DBNoteManager noteManager) {
        noteManager.mBasicNoteGroupDAO.fillByGroupType(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE, mBasicNoteGroupList);
        if (mRecyclerView != null)
            RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteGroupA> selectedNoteItems = BasicEntityNoteSelectionPosA.getItemsByPositions(mBasicNoteGroupList, mRecyclerViewSelector.getSelectedItems());

            //int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedNoteItems.size() > 0) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        //performDeleteAction(mode, selectedNoteItems);
                        break;
                    case R.id.edit:
                        //performEditAction(mode, selectedNoteItems.get(0));
                        break;
                    case R.id.move_up:
                        //performMoveAction(new BasicNoteMoveUpAction<BasicNoteA>(), selectedNoteItems);
                        break;
                    case R.id.move_top:
                        //performMoveAction(new BasicNoteMoveTopAction<BasicNoteA>(), selectedNoteItems);
                        break;
                    case R.id.move_down:
                        //performMoveAction(new BasicNoteMoveDownAction<BasicNoteA>(), selectedNoteItems);
                        break;
                    case R.id.move_bottom:
                        //performMoveAction(new BasicNoteMoveBottomAction<BasicNoteA>(), selectedNoteItems);
                        break;
                }
            }
            return false;
        }

        private void updateTitle(ActionMode mode) {
            if ((mRecyclerViewSelector != null) && (mRecyclerViewSelector.isSelected())) {
                mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getContext(), mBasicNoteGroupList, mRecyclerViewSelector.getSelectedItems()));
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_listitem_generic_actions, menu);
            updateTitle(mode);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            //menu.getItem(R.menu.)
            MenuItem menuItem = menu.findItem(R.id.edit);
            if (menuItem != null)
                menuItem.setVisible(mRecyclerViewSelector.isSelectedSingle());
            updateTitle(mode);
            return false;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler_view_list, container, false);

        Context context = view.getContext();

        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        refreshList(new DBNoteManager(context));

        BasicNoteGroupItemRecyclerViewAdapter recyclerViewAdapter = new BasicNoteGroupItemRecyclerViewAdapter(mBasicNoteGroupList, new ActionBarCallBack());
        mRecyclerView.setAdapter(recyclerViewAdapter);

        mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBasicNoteGroupFragmentInteractionListener) {
            mListener = (OnBasicNoteGroupFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBasicNoteGroupFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBasicNoteGroupFragmentInteractionListener {
        void onBasicNoteGroupFragmentInteraction(BasicNoteGroupA item);
    }
}
