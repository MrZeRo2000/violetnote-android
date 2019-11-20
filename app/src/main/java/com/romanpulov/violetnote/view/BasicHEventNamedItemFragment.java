package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.romanpulov.violetnote.db.manager.DBHManager;
import com.romanpulov.violetnote.model.BasicNoteItemA;

public class BasicHEventNamedItemFragment extends Fragment {

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

    public void refreshList(DBHManager hManager) {

    }

}
