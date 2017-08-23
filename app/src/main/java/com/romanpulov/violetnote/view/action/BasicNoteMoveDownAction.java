package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.model.MovementDirection;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;

import java.util.List;

/**
 * Created by rpulov on 07.09.2016.
 */
public class BasicNoteMoveDownAction<T extends BasicCommonNoteA> extends BasicNoteMoveAction<T>  {

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.moveDown(item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, List<T> items) {
        boolean result = false;
        BasicOrderedEntityNoteA.sortDesc(items);

        for (T item : items) {
            if (execute(noteManager, item)) {
                result = true;
            } else
                break;
        }

        return result;
    }

    @Override
    public int getDirection() {
        return MovementDirection.DIRECTION_DOWN;
    }
}
