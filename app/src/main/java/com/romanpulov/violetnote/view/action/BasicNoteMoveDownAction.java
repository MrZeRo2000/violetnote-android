package com.romanpulov.violetnote.view.action;

import android.support.v7.view.ActionMode;

import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;

/**
 * Created by rpulov on 07.09.2016.
 */
public class BasicNoteMoveDownAction<T extends BasicCommonNoteA> extends BasicNoteAction<T> {

    public BasicNoteMoveDownAction(BasicCommonNoteFragment fragment) {
        super(fragment);
    }

    @Override
    public boolean execute(ActionMode mode, T item) {
        return mNoteManager.moveDown(mFragment.getDBTableName(), item);
    }
}