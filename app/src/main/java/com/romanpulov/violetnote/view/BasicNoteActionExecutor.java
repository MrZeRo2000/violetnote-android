package com.romanpulov.violetnote.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;

import java.util.List;

/**
 * Created by romanpulov on 07.09.2016.
 */
public abstract class BasicNoteActionExecutor {
    protected final Context mContext;
    protected final Fragment mFragment;
    protected final DBNoteManager mNoteManager;
    protected PersistenceProvider mPersistenceProvider;

    public BasicNoteActionExecutor(Fragment fragment) {
        mContext = fragment.getActivity();
        mFragment = fragment;
        mNoteManager = new DBNoteManager(mContext);
    }

    public void setQueryListProvider(PersistenceProvider value) {
        mPersistenceProvider = value;
    }

    protected abstract boolean execute(final ActionMode mode, final BasicCommonNoteA item);

    protected int executeAndReturnNewPos(List<? extends BasicCommonNoteA> items, final BasicCommonNoteA item) {
        if (execute(null, item)) {
            // refresh list
            if (mPersistenceProvider != null)
                mPersistenceProvider.queryList();

            // find and return new pos of the node
            return BasicCommonNoteA.getNotePosWithId(items, item.getId());
        } else
            return -1;
    }

    public interface PersistenceProvider {
        void queryList();
        String getTableName();
    }
}
