package com.romanpulov.violetnote.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

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
        /*
        Bundle args = new Bundle();
        args.putParcelableArrayList(BasicNoteActivity.NOTE_LIST, noteList);
        fragment.setArguments(args);
        */
        return fragment;
    }

    public BasicNoteActivityFragment() {
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // TODO Auto-generated method stub
            mode.setTitle("onActionItemClicked:" + item);
            Toast.makeText(getActivity(), mNoteList.get(
                    mRecyclerViewSelector.getSelectedItem()).getTitle(),
            Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            mode.getMenuInflater().inflate(R.menu.menu_listitem_generic_actions, menu);
            if (mRecyclerViewSelector.getSelectedItem() != -1)
                mode.setTitle(mNoteList.get(mRecyclerViewSelector.getSelectedItem()).getTitle());
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.finishActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            mode.setTitle("onPrepareActionMode");
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
