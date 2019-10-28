package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;

public class BasicHEventCOItemFragment extends Fragment {
    private BasicNoteA mNote;

    public static BasicHEventCOItemFragment newInstance(BasicNoteA note) {
        BasicHEventCOItemFragment instance = new BasicHEventCOItemFragment();

        Bundle args = new Bundle();
        args.putParcelable(BasicNoteA.class.getName(), note);
        instance.setArguments(args);

        return instance;
    }

    public void refreshList(DBNoteManager noteManager) {

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
}
