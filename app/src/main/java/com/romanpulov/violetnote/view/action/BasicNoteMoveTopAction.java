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
public class BasicNoteMoveTopAction<T extends BasicCommonNoteA> extends BasicNoteMoveAction<T> {

    public BasicNoteMoveTopAction(BasicCommonNoteFragment fragment) {
        super(fragment);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.moveTop(item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, List<T> items) {
        boolean result = false;
        BasicOrderedEntityNoteA.sortDesc(items);

        for (T item : items) {
            if (noteManager.moveTop(item)) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public int getDirection() {
        return MovementDirection.DIRECTION_UP;
    }
}
