package com.romanpulov.violetnote.model;

import com.romanpulov.violetnote.db.DBManagementProvider;

/**
 * Base class for BasicCommonNote
 * Created by rpulov on 28.08.2016.
 */
public abstract class BasicCommonNoteA extends BasicOrderedEntityNoteA implements DisplayTitleProvider {
    private DBManagementProvider mDBManagementProvider;

    public DBManagementProvider getDBManagementProvider() {
        return mDBManagementProvider;
    }

    public void setDBManagementProvider(DBManagementProvider value) {
        mDBManagementProvider = value;
    }
}
