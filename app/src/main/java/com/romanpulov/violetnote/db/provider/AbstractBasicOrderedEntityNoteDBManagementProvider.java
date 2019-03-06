package com.romanpulov.violetnote.db.provider;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;

/**
 * AbstractBasicOrderedEntityNoteDBManagementProvider database management operations
 * Created by rpulov on 09.08.2017.
 */

public abstract class AbstractBasicOrderedEntityNoteDBManagementProvider implements DBManagementProvider {
    private final BasicOrderedEntityNoteA mBasicOrderedEntityNote;

    public AbstractBasicOrderedEntityNoteDBManagementProvider(BasicOrderedEntityNoteA basicOrderedEntityNoteA) {
        mBasicOrderedEntityNote = basicOrderedEntityNoteA;
    }

    @Override
    public abstract String getTableName();

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
        return new String[] {String.valueOf(mBasicOrderedEntityNote.getOrderId())};
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
