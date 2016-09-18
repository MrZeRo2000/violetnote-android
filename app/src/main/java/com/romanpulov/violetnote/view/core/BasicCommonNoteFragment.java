package com.romanpulov.violetnote.view.core;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.romanpulov.violetnote.db.DBNoteManager;

/**
 * Created by rpulov on 07.09.2016.
 */
public abstract class BasicCommonNoteFragment extends Fragment implements DBDataProvider {
    protected DialogFragment mDialogFragment;

    protected RecyclerView mRecyclerView;
    protected RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;

    public abstract void refreshList(DBNoteManager noteManager);
}
