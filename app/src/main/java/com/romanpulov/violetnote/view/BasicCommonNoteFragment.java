package com.romanpulov.violetnote.view;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

/**
 * Created by rpulov on 07.09.2016.
 */
public abstract class BasicCommonNoteFragment extends Fragment{

    protected RecyclerView mRecyclerView;
    protected RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;

    public abstract String getDBTableName();

    public abstract void refreshList(DBNoteManager noteManager);
}
