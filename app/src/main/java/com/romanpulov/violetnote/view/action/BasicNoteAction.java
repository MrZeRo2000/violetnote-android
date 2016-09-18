package com.romanpulov.violetnote.view.action;

import android.content.Context;
import android.support.v7.view.ActionMode;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;

import java.util.List;

/**
 * Created by romanpulov on 07.09.2016.
 */
public abstract class BasicNoteAction <T extends BasicEntityNoteA>  {
    protected final BasicCommonNoteFragment mFragment;
    protected final DBNoteManager mNoteManager;

    public BasicNoteAction(BasicCommonNoteFragment fragment) {
        mFragment = fragment;
        mNoteManager = new DBNoteManager(fragment.getActivity());
    }

    public abstract boolean execute(final T item);

    public int executeAndReturnNewPos(List<? extends T> items, final T item) {
        if (execute(item)) {
            // refresh list
            mFragment.refreshList(mNoteManager);

            // find and return new pos of the node
            return BasicEntityNoteA.getNotePosWithId(items, item.getId());
        } else
            return -1;
    }
}
