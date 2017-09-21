package com.romanpulov.violetnote.db;

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
        return DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = ? AND " + DBBasicNoteOpenHelper.PRIORITY_COLUMN_NAME + " = ? AND " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " < ?";
    }

    @Override
    public String getNextOrderSelection() {
        return DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = ? AND " + DBBasicNoteOpenHelper.PRIORITY_COLUMN_NAME + " = ? AND " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " > ?";
    }

    @Override
    public String[] getOrderSelectionArgs() {
        return new String[] {String.valueOf(mBasicNoteItem.getNoteId()),String.valueOf(mBasicNoteItem.getPriority()), String.valueOf(mBasicNoteItem.getOrderId())};
    }

    @Override
    public String getOrderIdSelectionString() {
        return DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = " + String.valueOf(mBasicNoteItem.getNoteId()) + " AND " + DBBasicNoteOpenHelper.PRIORITY_COLUMN_NAME + " = " + String.valueOf(mBasicNoteItem.getPriority());
    }

    @Override
    public String getOrderIdSelection() {
        return DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = ? AND " + DBBasicNoteOpenHelper.PRIORITY_COLUMN_NAME + " = ?";
    }

    @Override
    public String[] getOrderIdSelectionArgs() {
        return new String[] {String.valueOf(mBasicNoteItem.getNoteId()),String.valueOf(mBasicNoteItem.getPriority())};
    }
}
