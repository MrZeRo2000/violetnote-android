package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;

/**
 * Created by rpulov on 07.09.2016.
 */
public class BasicNoteMoveUpAction<T extends BasicCommonNoteA> extends BasicNoteAction<T> {

    public BasicNoteMoveUpAction(BasicCommonNoteFragment fragment) {
        super(fragment);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.moveUp(mDBDataProvider.getDBTableName(), item);
    }
}
