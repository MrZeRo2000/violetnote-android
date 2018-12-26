package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.model.MovementDirection;

import java.util.List;

/**
 * BasicCommonNoteA move priority down action
 * Created by romanpulov on 18.08.2017.
 */

public class BasicNoteMovePriorityDownAction<T extends BasicCommonNoteA> extends BasicNoteMoveAction<T> {

    @Override
    public int getDirection() {
        return MovementDirection.DIRECTION_DOWN;
    }

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.mBasicCommonNoteDAO.priorityDown(item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, List<T> items) {
        BasicOrderedEntityNoteA.sortDesc(items);

        for (T item : items)
            execute(noteManager, item);

        return true;
    }
}
