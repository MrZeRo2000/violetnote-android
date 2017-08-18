package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.model.MovementDirection;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;

import java.util.List;

/**
 * Created by romanpulov on 18.08.2017.
 */

public class BasicNotePriorityUpAction<T extends BasicCommonNoteA> extends BasicNoteMoveAction<T> {

    public BasicNotePriorityUpAction(BasicCommonNoteFragment fragment) {
        super(fragment);
    }

    @Override
    public int getDirection() {
        return MovementDirection.DIRECTION_UP;
    }

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.priorityUp(item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, List<T> items) {
        BasicOrderedEntityNoteA.sortAsc(items);

        for (T item : items)
            noteManager.priorityUp(item);

        return true;
    }
}
