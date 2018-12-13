package com.romanpulov.violetnote.db;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NotesTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;

/**
 * BasicNoteA database management operations
 * Created by rpulov on 09.08.2017.
 */

public class BasicNoteDBManagementProvider implements DBManagementProvider {
    private final BasicNoteA mBasicNote;

    public BasicNoteDBManagementProvider(BasicNoteA basicNote) {
        mBasicNote = basicNote;
    }

    @Override
    public String getTableName() {
        return NotesTableDef.TABLE_NAME;
    }

    @Override
    public String getPrevOrderSelection() {
        return DBCommonDef.ORDER_COLUMN_NAME + " < ?";
    }

    @Override
    public String getNextOrderSelection() {
        return DBCommonDef.ORDER_COLUMN_NAME + " > ?";
    }

    @Override
    public String[] getOrderSelectionArgs() {
        return new String[] {String.valueOf(mBasicNote.getOrderId())};
    }

    @Override
    public String getOrderIdSelectionString() {
        return null;
    }

    @Override
    public String getOrderIdSelection() {
        return null;
    }

    @Override
    public String[] getOrderIdSelectionArgs() {
        return null;
    }
}
