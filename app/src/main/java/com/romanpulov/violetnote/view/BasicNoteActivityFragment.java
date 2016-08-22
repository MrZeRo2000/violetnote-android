package com.romanpulov.violetnote.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_list, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.list);

        // Set the adapter
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        if (mNoteList != null)
            recyclerView.setAdapter(new BasicNoteRecycleViewAdapter(mNoteList, this));

        /*
        ArrayList<BasicNoteA> noteList = this.getArguments().getParcelableArrayList(BasicNoteActivity.NOTE_LIST);
        if (noteList != null)
            recyclerView.setAdapter(new BasicNoteRecycleViewAdapter(noteList));
            */

        // add decoration
        recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_listitem_generic_actions, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Toast.makeText(this.getActivity(), "Something is selected:" + item, Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }

    public void updateList() {
        View view = getView();
        if (view != null) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
