package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class DBDefRepository {
    private static List<Class<? extends DBCommonDef.TableDefSQLProvider>> mTableDefs = new ArrayList<>();

    static {
        //dictionary tables
        mTableDefs.add(NoteGroupsTableDef.class);
        mTableDefs.add(NoteItemParamTypesTableDef.class);

        //core data tables
        mTableDefs.add(NotesTableDef.class);
        mTableDefs.add(NoteItemsTableDef.class);
        mTableDefs.add(NoteValuesTableDef.class);

        //H tables
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
