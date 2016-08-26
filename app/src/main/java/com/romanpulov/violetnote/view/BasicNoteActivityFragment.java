package com.romanpulov.violetnote.view;

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
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.AlertOkCancelDialogFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.TextInputDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class BasicNoteActivityFragment extends Fragment {
    private List<BasicNoteA> mNoteList;
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
        AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(R.string.ui_question_are_you_sure);
        dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                // delete item
                DBNoteManager mNoteManager = new DBNoteManager(getActivity());
                mNoteManager.deleteNote(item);
                // refresh list
                List<BasicNoteA> newNoteList = mNoteManager.queryNotes();
                mNoteList.clear();
                mNoteList.addAll(newNoteList);

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
                        List<BasicNoteA> newNoteList = mNoteManager.queryNotes();
                        mNoteList.clear();
                        mNoteList.addAll(newNoteList);
                    }
                    // finish anyway
                    mode.finish();
                }
            }
        });
        dialog.show();
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int selectedItem = mRecyclerViewSelector.getSelectedItem();
            if (selectedItem != -1) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        deleteItem(mode, mNoteList.get(selectedItem));
                        break;
                    case R.id.edit:
                        editItem(mode, mNoteList.get(selectedItem));
                        break;
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_listitem_generic_actions, menu);
            if (mRecyclerViewSelector.getSelectedItem() != -1)
                mode.setTitle(mNoteList.get(mRecyclerViewSelector.getSelectedItem()).getTitle());
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
            mRecyclerViewAdapter = new BasicNoteRecycleViewAdapter(mNoteList, new ActionBarCallBack());
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
}
