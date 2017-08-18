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

public class BasicNoteMovePriorityDownAction<T extends BasicCommonNoteA> extends BasicNoteMoveAction<T> {

    public BasicNoteMovePriorityDownAction(BasicCommonNoteFragment fragment) {
        super(fragment);
    }

    @Override
    public int getDirection() {
        return MovementDirection.DIRECTION_DOWN;
    }

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.priorityDown(item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, List<T> items) {
        BasicOrderedEntityNoteA.sortDesc(items);

        for (T item : items)
            noteManager.priorityDown(item);

        return true;
    }
}
