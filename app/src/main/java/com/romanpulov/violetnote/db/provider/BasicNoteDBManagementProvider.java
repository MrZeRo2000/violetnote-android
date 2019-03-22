package com.romanpulov.violetnote.db.provider;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NotesTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;

public class BasicNoteDBManagementProvider extends AbstractBasicOrderedEntityNoteDBManagementProvider<BasicNoteA> {

    public BasicNoteDBManagementProvider(BasicNoteA basicNoteA) {
        super(basicNoteA);
    }

    @Override
    public String getTableName() {
        return NotesTableDef.TABLE_NAME;
    }

    @Override
    public String getPrevOrderSelection() {
        return DBCommonDef.GROUP_ID_COLUMN_NAME + " = ? AND " + DBCommonDef.ORDER_COLUMN_NAME + " < ?";
    }

    @Override
    public String getNextOrderSelection() {
        return DBCommonDef.GROUP_ID_COLUMN_NAME + " = ? AND " + DBCommonDef.ORDER_COLUMN_NAME + " > ?";
    }

    @Override
    public String[] getOrderSelectionArgs() {
        return new String[] {String.valueOf(mData.getNoteGroupId()), String.valueOf(mData.getOrderId())};
    }

    @Override
    public String getOrderIdSelectionString() {
        return DBCommonDef.GROUP_ID_COLUMN_NAME + " = " + String.valueOf(mData.getNoteGroupId());
    }

    @Override
    public String getOrderIdSelection() {
        return DBCommonDef.GROUP_ID_COLUMN_NAME + " = ?";
    }

    @Override
    public String[] getOrderIdSelectionArgs() {
        return new String[] {String.valueOf(mData.getNoteGroupId())};
    }
}
