package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;

import java.util.List;

public class BasicNoteMoveToOtherNoteGroupAction extends BasicAction<List<BasicNoteA>> {

    private final BasicNoteGroupA mOtherNoteGroup;

    public BasicNoteMoveToOtherNoteGroupAction(List<BasicNoteA> data, BasicNoteGroupA otherNoteGroup) {
        super(data);
        mOtherNoteGroup = otherNoteGroup;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        boolean result = false;
        BasicOrderedEntityNoteA.sortAsc(mData);

        for (BasicNoteA item : mData) {
            if (noteManager.mBasicNoteDAO.moveToOtherNoteGroup(item, mOtherNoteGroup) == 1) {
                result = true;
            } else {
                result = false;
                break;
            }
        }

        return result;
    }
}
