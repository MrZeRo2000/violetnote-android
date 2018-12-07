package com.romanpulov.violetnote.db.tabledef;

import static com.romanpulov.violetnote.db.tabledef.DBCommonDef.*;

public final class HEventTypesTableDef {
    public static final String TABLE_NAME = "h_event_types";
    public static final String EVENT_GROUP_COLUMN_NAME = "event_group";
    public static final String EVENT_TYPE_COLUMN_NAME = "event_type_name";

    public static final String[] TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            EVENT_GROUP_COLUMN_NAME,
            EVENT_TYPE_COLUMN_NAME
    };

    public static final String[] TABLE_COL_TYPES = new String[] {
            "INTEGER PRIMARY KEY",
            "INTEGER NOT NULL",
            "TEXT"
    };

    public static final String TABLE_CREATE = StmtGenerator.createTableStatement(TABLE_NAME, TABLE_COLS, TABLE_COL_TYPES);

    /*
    public static final String INITIAL_LOAD =
            "INSERT INTO " + TABLE_NAME + "(" +
                    EVENT_GROUP_COLUMN_NAME + ""
    */
}
