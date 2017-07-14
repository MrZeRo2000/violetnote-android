package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;

import java.util.List;

/**
 * Created by romanpulov on 16.09.2016.
 */
public class BasicNoteDataDeleteEntityAction extends BasicNoteDataAction  {
    private final List<? extends BasicEntityNoteA> mItems;
    private final String mTableName;

    public BasicNoteDataDeleteEntityAction(BasicNoteDataA basicNoteData, String tableName, List<? extends BasicEntityNoteA> items) {
        super(basicNoteData);
        mTableName = tableName;
        mItems = items;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        long result = 1;
        for (BasicEntityNoteA item : mItems) {
            if (result == 1)
                result = noteManager.deleteEntityNote(mTableName, item);
            else
                break;
        }
        return result == 1;
    }
}
