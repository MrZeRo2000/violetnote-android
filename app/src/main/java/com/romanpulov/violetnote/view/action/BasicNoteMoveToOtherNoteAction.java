package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;

import java.util.List;

/**
 * BasicNoteItemA move to other note action
 * Created by romanpulov on 06.10.2017.
 */

public class BasicNoteMoveToOtherNoteAction<I extends BasicNoteItemA> extends BasicItemsAction<BasicNoteDataA, I> {

    private final BasicNoteA mOtherNote;

    public BasicNoteMoveToOtherNoteAction(BasicNoteDataA data, List<I> items, BasicNoteA otherNote) {
        super(data, items);
        mOtherNote = otherNote;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        boolean result = false;
        BasicOrderedEntityNoteA.sortAsc(mItems);

        for (I item : mItems) {
            if (noteManager.mBasicNoteItemDAO.moveToOtherNote(item, mOtherNote) == 1) {
                result = true;
            } else {
                result = false;
                break;
            }
        }

        return result;
    }
}
