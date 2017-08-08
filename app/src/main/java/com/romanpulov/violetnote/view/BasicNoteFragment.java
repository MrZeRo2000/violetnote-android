package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.action.BasicNoteMoveAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveUpAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.TextInputDialog;
import com.romanpulov.violetnote.view.core.TextEditDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class BasicNoteFragment extends BasicCommonNoteFragment {
    private static void log(String message) {
        Log.d("BasicNoteFragment", message);
    }

    private OnBasicNoteFragmentInteractionListener mListener;

    private ArrayList<BasicNoteA> mNoteList = new ArrayList<>();

    public static BasicNoteFragment newInstance(DBNoteManager noteManager) {
        BasicNoteFragment fragment = new BasicNoteFragment();
        fragment.refreshList(noteManager);
        return fragment;
    }

    public BasicNoteFragment() {
    }

    @Override
    public String getDBTableName() {
        return DBBasicNoteOpenHelper.NOTES_TABLE_NAME;
    }

    @Override
    public void refreshList(DBNoteManager noteManager) {
        noteManager.refreshNotes(mNoteList);
        if (mRecyclerView != null)
            mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void performAddAction(final BasicNoteA item) {
        DBNoteManager mNoteManager = new DBNoteManager(getActivity());
        if (mNoteManager.insertNote(item) != -1) {
            // refresh list
            refreshList(mNoteManager);

            //scroll to bottom
            mRecyclerView.scrollToPosition(mNoteList.size() - 1);
        }
    }

    private void performDeleteAction(final ActionMode mode, final List<? extends BasicCommonNoteA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelSupportDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                // delete item
                DBNoteManager mNoteManager = new DBNoteManager(getActivity());

                for (BasicCommonNoteA item : items)
                    mNoteManager.deleteNote(item);

                // refresh list
                refreshList(mNoteManager);

                //finish action
                mode.finish();
            }
        });

        dialog.show(getFragmentManager(), null);
    }

    private void performEditAction(final ActionMode mode, final BasicNoteA item) {
        (new TextEditDialogBuilder(getActivity(), getString(R.string.ui_note_title), item.getTitle()))
                .setNonEmptyErrorMessage(getString(R.string.error_field_not_empty))
                .setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
                    @Override
                    public void onTextInput(String text) {
                        if (!text.equals(item.getTitle())) {
                            //change
                            item.setTitle(text);

                            //update database
                            DBNoteManager noteManager = new DBNoteManager(getActivity());
                            noteManager.updateNote(item);

                            //refresh list
                            BasicNoteFragment.this.refreshList(noteManager);
                        }
                        // finish anyway
                        mode.finish();
                    }
                })
                .execute();
    }

    private void performMoveAction(BasicNoteMoveAction<BasicNoteA> action, List<BasicNoteA> items) {
        DBNoteManager noteManager = new DBNoteManager(getActivity());

        if (action.execute(noteManager, items)) {
            refreshList(noteManager);

            BasicEntityNoteSelectionPosA selectionPos = new BasicEntityNoteSelectionPosA(mNoteList, items);
            int selectionScrollPos = selectionPos.getDirectionPos(action.getDirection());

            if (selectionScrollPos != -1) {
                mRecyclerViewSelector.setSelectedItems(selectionPos.getSelectedItemsPositions());
                mRecyclerView.scrollToPosition(selectionScrollPos);
            }
        }
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteA> selectedNoteItems = BasicEntityNoteSelectionPosA.getItemsByPositions(mNoteList, mRecyclerViewSelector.getSelectedItems());

            //int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedNoteItems.size() > 0) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        performDeleteAction(mode, selectedNoteItems);
                        break;
                    case R.id.edit:
                        performEditAction(mode, selectedNoteItems.get(0));
                        break;
                    case R.id.move_up:
                        performMoveAction(new BasicNoteMoveUpAction<BasicNoteA>(BasicNoteFragment.this), selectedNoteItems);
                        break;
                    case R.id.move_top:
                        performMoveAction(new BasicNoteMoveTopAction<BasicNoteA>(BasicNoteFragment.this), selectedNoteItems);
                        break;
                    case R.id.move_down:
                        performMoveAction(new BasicNoteMoveDownAction<BasicNoteA>(BasicNoteFragment.this), selectedNoteItems);
                        break;
                    case R.id.move_bottom:
                        performMoveAction(new BasicNoteMoveBottomAction<BasicNoteA>(BasicNoteFragment.this), selectedNoteItems);
                        break;
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            log("onCreateActionMode");
            mode.getMenuInflater().inflate(R.menu.menu_listitem_generic_actions, menu);
            if (mRecyclerViewSelector.isSelected())
                mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), mNoteList, mRecyclerViewSelector.getSelectedItems()));
            return true;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            log("onPrepareActionMode");
            //menu.getItem(R.menu.)
            MenuItem menuItem = menu.findItem(R.id.edit);
            if (menuItem != null) {
                menuItem.setVisible(mRecyclerViewSelector.isSelectedSingle());
            }
            return false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_list, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.list);

        // Set the adapter
        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        BasicNoteRecycleViewAdapter recyclerViewAdapter = new BasicNoteRecycleViewAdapter(mNoteList, new ActionBarCallBack(), mListener);
        mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();
        mRecyclerView.setAdapter(recyclerViewAdapter);

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnBasicNoteFragmentInteractionListener) {
            mListener = (OnBasicNoteFragmentInteractionListener) activity;
        }
        else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnBasicNoteFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBasicNoteFragmentInteractionListener {
        void onBasicNoteFragmentInteraction(BasicNoteA item);
    }
}
