package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.model.MovementDirection;

import java.util.List;

/**
 * BasicCommonNoteA move bottom action
 * Created by rpulov on 07.09.2016.
 */
public class BasicNoteMoveBottomAction <T extends BasicCommonNoteA> extends BasicNoteMoveAction<T>  {

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.mBasicCommonNoteDAO.moveBottom(item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, List<T> items) {
        boolean result = false;
        BasicOrderedEntityNoteA.sortAsc(items);

        for (T item : items) {
            if (execute(noteManager, item)) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public int getDirection() {
        return MovementDirection.DIRECTION_DOWN;
    }
}
