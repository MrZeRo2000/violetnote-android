package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class DBDefRepository {
    private static List<Class<? extends DBCommonDef.TableDefSQLProvider>> mTableDefs = new ArrayList<>();

    static {
        mTableDefs.add(HEventTypesTableDef.class);
        mTableDefs.add(HEventsTableDef.class);
        mTableDefs.add(HNoteItemsTableDef.class);
        mTableDefs.add(HNoteCOItems.class);
        mTableDefs.add(HNoteCOItemParams.class);
    }

    @NonNull
    public static List<Class<? extends DBCommonDef.TableDefSQLProvider>> getCreateTableDefs() {
        return mTableDefs;
    }
}
