package com.romanpulov.violetnote.db;

import com.romanpulov.violetnote.model.BasicNoteA;

/**
 * Created by rpulov on 09.08.2017.
 */

public class BasicNoteDBManagementProvider implements DBManagementProvider {
    private final BasicNoteA mBasicNote;

    public BasicNoteDBManagementProvider(BasicNoteA basicNote) {
        mBasicNote = basicNote;
    }

    @Override
    public String getTableName() {
        return DBBasicNoteOpenHelper.NOTES_TABLE_NAME;
    }

    @Override
    public String getPrevOrderSelection() {
        return DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " < ?";
    }

    @Override
    public String getNextOrderSelection() {
        return DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " > ?";
    }

    @Override
    public String[] getOrderSelectionArgs() {
        return new String[] {String.valueOf(mBasicNote.getOrderId())};
    }

    @Override
    public String getOrderIdSelection() {
        return null;
    }
}
