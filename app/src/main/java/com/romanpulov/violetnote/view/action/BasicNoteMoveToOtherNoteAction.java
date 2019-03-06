package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;

import java.util.List;

/**
 * BasicNoteItemA move to other note action
 * Created by romanpulov on 06.10.2017.
 */

public class BasicNoteMoveToOtherNoteAction<T extends BasicNoteItemA> extends BasicNoteAction<T> {

    private final BasicNoteA mOtherNote;

    public BasicNoteMoveToOtherNoteAction(BasicNoteA otherNote) {
        mOtherNote = otherNote;
    }

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.mBasicNoteItemDAO.moveToOtherNote(item, mOtherNote) == 1;
    }

    @Override
    public boolean execute(DBNoteManager noteManager, List<T> items) {
        boolean result = false;
        BasicOrderedEntityNoteA.sortAsc(items);

        for (T item : items) {
            if (execute(noteManager, item)) {
                result = true;
            } else {
                result = false;
                break;
            }
        }

        return result;
    }
}
