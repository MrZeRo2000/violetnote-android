package com.romanpulov.violetnote.db.tabledef;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class DBDefRepository {
    private static final List<Class<? extends DBCommonDef.TableDefSQLProvider>> mTableDefs = new ArrayList<>();

    static {
        //dictionary tables
        mTableDefs.add(NoteGroupsTableDef.class);
        mTableDefs.add(NoteItemParamTypesTableDef.class);

        //core data tables
        mTableDefs.add(NotesTableDef.class);
        mTableDefs.add(NoteItemsTableDef.class);
        mTableDefs.add(NoteValuesTableDef.class);
        mTableDefs.add(NoteItemParamsTableDef.class);

        //history tables
        mTableDefs.add(NoteItemsHistoryTableDef.class);

        //H tables
        mTableDefs.add(HEventTypesTableDef.class);
        mTableDefs.add(HEventsTableDef.class);
        mTableDefs.add(HNoteItemsTableDef.class);
        mTableDefs.add(HNoteCOItemsTableDef.class);
        mTableDefs.add(HNoteCOItemParamsTableDef.class);
    }

    @NonNull
    public static List<Class<? extends DBCommonDef.TableDefSQLProvider>> getCreateTableDefs() {
        return mTableDefs;
    }
}
