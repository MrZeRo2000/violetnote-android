package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

/**
 * Created by romanpulov on 16.09.2016.
 */
public class BasicNoteDataDeleteItemAction extends BasicNoteDataAction  {

    public BasicNoteDataDeleteItemAction(BasicNoteDataA basicNoteData, BasicNoteItemA item) {
        super(basicNoteData);
        mItem = item;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        return noteManager.deleteEntityNote(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, mItem) == 1;
    }
}
