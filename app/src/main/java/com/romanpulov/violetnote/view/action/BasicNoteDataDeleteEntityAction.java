package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;

/**
 * Created by romanpulov on 16.09.2016.
 */
public class BasicNoteDataDeleteEntityAction extends BasicNoteDataAction  {
    private final BasicEntityNoteA mItem;
    private final String mTableName;

    public BasicNoteDataDeleteEntityAction(BasicNoteDataA basicNoteData, String tableName, BasicEntityNoteA item) {
        super(basicNoteData);
        mTableName = tableName;
        mItem = item;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        return noteManager.deleteEntityNote(mTableName, mItem) == 1;
    }
}
