package com.romanpulov.violetnote.db;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.model.BasicNoteItemA;

/**
 * BasicNoteItemA database management
 * Created by romanpulov on 10.08.2017.
 */

public class BasicNoteItemDBManagementProvider implements DBManagementProvider {
    private final BasicNoteItemA mBasicNoteItem;

    public BasicNoteItemDBManagementProvider(BasicNoteItemA basicNoteItem) {
        mBasicNoteItem = basicNoteItem;
    }

    @Override
    public String getTableName() {
        return DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME;
    }

    @Override
    public String getPrevOrderSelection() {
        return DBCommonDef.NOTE_ID_COLUMN_NAME + " = ? AND " + DBCommonDef.PRIORITY_COLUMN_NAME + " = ? AND " + DBCommonDef.ORDER_COLUMN_NAME + " < ?";
    }

    @Override
    public String getNextOrderSelection() {
        return DBCommonDef.NOTE_ID_COLUMN_NAME + " = ? AND " + DBCommonDef.PRIORITY_COLUMN_NAME + " = ? AND " + DBCommonDef.ORDER_COLUMN_NAME + " > ?";
    }

    @Override
    public String[] getOrderSelectionArgs() {
        return new String[] {String.valueOf(mBasicNoteItem.getNoteId()),String.valueOf(mBasicNoteItem.getPriority()), String.valueOf(mBasicNoteItem.getOrderId())};
    }

    @Override
    public String getOrderIdSelectionString() {
        return DBCommonDef.NOTE_ID_COLUMN_NAME + " = " + String.valueOf(mBasicNoteItem.getNoteId()) + " AND " + DBCommonDef.PRIORITY_COLUMN_NAME + " = " + String.valueOf(mBasicNoteItem.getPriority());
    }

    @Override
    public String getOrderIdSelection() {
        return DBCommonDef.NOTE_ID_COLUMN_NAME + " = ? AND " + DBCommonDef.PRIORITY_COLUMN_NAME + " = ?";
    }

    @Override
    public String[] getOrderIdSelectionArgs() {
        return new String[] {String.valueOf(mBasicNoteItem.getNoteId()),String.valueOf(mBasicNoteItem.getPriority())};
    }
}
