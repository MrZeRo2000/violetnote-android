package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.AlertOkCancelDialogFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.TextInputDialog;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class BasicNoteActivityFragment extends Fragment {
    private OnBasicNoteFragmentInteractionListener mListener;

    private ArrayList<BasicNoteA> mNoteList;
    private RecyclerView mRecyclerView;
    private BasicNoteRecycleViewAdapter mRecyclerViewAdapter;
    private RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;

    public static BasicNoteActivityFragment newInstance(ArrayList<BasicNoteA> noteList) {
        BasicNoteActivityFragment fragment = new BasicNoteActivityFragment();
        fragment.mNoteList = noteList;
        return fragment;
    }

    public BasicNoteActivityFragment() {
    }

    private void deleteItem(final ActionMode mode, final BasicNoteA item) {
        AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                // delete item
                DBNoteManager mNoteManager = new DBNoteManager(getActivity());
                mNoteManager.deleteNote(item);

                // refresh list
                mNoteManager.refreshNotes(mNoteList);

                //finish action
                mode.finish();
            }
        });
        dialog.show(getFragmentManager(), null);
    }

    private void editItem(final ActionMode mode, final BasicNoteA item) {
        final String oldTitle = item.getTitle();
        TextInputDialog dialog = new TextInputDialog(getActivity(), getResources().getString(R.string.ui_note_title));
        dialog.setText(oldTitle);
        dialog.setNonEmptyErrorMessage(getString(R.string.error_field_not_empty));
        dialog.setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
            @Override
            public void onTextInput(String text) {
                if (text != null) {
                    if (!text.equals(oldTitle)) {
                        // prepare and update item
                        item.setTitle(text);
                        DBNoteManager mNoteManager = new DBNoteManager(getActivity());
                        mNoteManager.updateNote(item);

                        // refresh list
                        mNoteManager.refreshNotes(mNoteList);
                    }
                    // finish anyway
                    mode.finish();
                }
            }
        });
        dialog.show();
    }

    private abstract class MoveActionExecutor {
        protected DBNoteManager mNoteManager;

        public MoveActionExecutor() {
            mNoteManager = new DBNoteManager(getActivity());
        }

        protected abstract boolean execute(BasicNoteA item);

        protected int executeAndReturnNewPos(BasicNoteA item) {
            if (execute(item)) {
                // refresh list
                mNoteManager.refreshNotes(mNoteList);
                // find and return new pos of the node
                return BasicCommonNoteA.getNotePosWithId(mNoteList, item.getId());
            } else
                return -1;
        }
    }

    private class MoveUpActionExecutor extends MoveActionExecutor {
        @Override
        protected boolean execute(BasicNoteA item) {
            return mNoteManager.moveUp(item);
        }
    }

    private class MoveTopActionExecutor extends MoveActionExecutor {
        @Override
        protected boolean execute(BasicNoteA item) {
            return mNoteManager.moveTop(item);
        }
    }

    private class MoveDownActionExecutor extends MoveActionExecutor {
        @Override
        protected boolean execute(BasicNoteA item) {
            return mNoteManager.moveDown(item);
        }
    }

    private class MoveBottomActionExecutor extends MoveActionExecutor {
        @Override
        protected boolean execute(BasicNoteA item) {
            return mNoteManager.moveBottom(item);
        }
    }

    private void performMoveAction(MoveActionExecutor executor, BasicNoteA item) {
        int notePos = executor.executeAndReturnNewPos(item);
        if (notePos != -1) {
            mRecyclerViewSelector.setSelectedView(null, notePos);
            mRecyclerView.scrollToPosition(notePos);
        }
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedItemPos != -1) {
                BasicNoteA selectedItem = mNoteList.get(selectedItemPos);
                switch (item.getItemId()) {
                    case R.id.delete:
                        deleteItem(mode, selectedItem);
                        break;
                    case R.id.edit:
                        editItem(mode, selectedItem);
                        break;
                    case R.id.move_up:
                        performMoveAction(new MoveUpActionExecutor(), selectedItem);
                        break;
                    case R.id.move_top:
                        performMoveAction(new MoveTopActionExecutor(), selectedItem);
                        break;
                    case R.id.move_down:
                        performMoveAction(new MoveDownActionExecutor(), selectedItem);
                        break;
                    case R.id.move_bottom:
                        performMoveAction(new MoveBottomActionExecutor(), selectedItem);
                        break;
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_listitem_generic_actions, menu);
            if (mRecyclerViewSelector.getSelectedItemPos() != -1)
                mode.setTitle(mNoteList.get(mRecyclerViewSelector.getSelectedItemPos()).getTitle());
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.finishActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
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

        if (mNoteList != null) {
            mRecyclerViewAdapter = new BasicNoteRecycleViewAdapter(mNoteList, new ActionBarCallBack(), mListener);
            mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        }

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        return view;
    }

    public void updateList() {
        View view = getView();
        if ((view != null) && (mRecyclerView != null))
            mRecyclerView.getAdapter().notifyDataSetChanged();
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
