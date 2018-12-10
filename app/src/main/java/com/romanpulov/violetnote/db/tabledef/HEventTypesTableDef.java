package com.romanpulov.violetnote.db.tabledef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.romanpulov.violetnote.db.tabledef.DBCommonDef.*;

public final class HEventTypesTableDef implements DBCommonDef.TableDefSQLProvider {
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

    public static final String U_INDEX_CREATE = StmtGenerator.createUniqueIndexStatement(TABLE_NAME, EVENT_TYPE_COLUMN_NAME);

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public static final String INITIAL_LOAD = StmtGenerator.createInsertTableStatement(
            TABLE_NAME,
            Arrays.copyOfRange(TABLE_COLS, 1, TABLE_COLS.length),
            "SELECT 1, 'Note items' UNION ALL SELECT 1, 'Checkout'"
            );

    @Override
    public List<String> getSQLCreate() {
        return Arrays.asList(
                TABLE_CREATE,
                U_INDEX_CREATE,
                INITIAL_LOAD
        );
    }

    @Override
    public List<String> getSQLUpgrade(int oldVersion) {
        List<String> result = new ArrayList<>();
        switch(oldVersion) {
            case 1:
            case 2:
                result.addAll(
                        Arrays.asList(
                                TABLE_CREATE,
                                U_INDEX_CREATE,
                                INITIAL_LOAD
                        )
                );
            case 100:
                return result;
            default:
                return null;
        }
    }
}
