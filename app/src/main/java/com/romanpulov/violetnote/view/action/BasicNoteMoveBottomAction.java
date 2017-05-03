package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;

import java.util.List;

/**
 * Created by rpulov on 07.09.2016.
 */
public class BasicNoteMoveBottomAction <T extends BasicCommonNoteA> extends BasicNoteAction<T> {

    public BasicNoteMoveBottomAction(BasicCommonNoteFragment fragment) {
        super(fragment);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.moveBottom(mDBDataProvider.getDBTableName(), item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, List<T> items) {
        throw new IllegalArgumentException();
    }

}
