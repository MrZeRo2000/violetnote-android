package com.romanpulov.violetnote.db.tabledef;

import androidx.annotation.Nullable;

import com.romanpulov.violetnote.model.BasicHEventTypeA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * h_event_types table definition
 */
public final class HEventTypesTableDef implements DBCommonDef.TableDefSQLProvider {

    //table
    public static final String TABLE_NAME = "h_event_types";

    //columns
    public static final String EVENT_GROUP_COLUMN_NAME = "event_group";
    public static final String EVENT_TYPE_CODE_COLUMN_NAME = "event_type_code";
    public static final String EVENT_TYPE_NAME_COLUMN_NAME = "event_type_name";
    public static final String[] TABLE_COLS = new String[] {
            DBCommonDef.ID_COLUMN_NAME,
            EVENT_GROUP_COLUMN_NAME,
            EVENT_TYPE_CODE_COLUMN_NAME,
            EVENT_TYPE_NAME_COLUMN_NAME
    };

    public static final String[] TABLE_COL_TYPES = new String[] {
            "INTEGER PRIMARY KEY",
            "INTEGER NOT NULL",
            "TEXT NOT NULL",
            "TEXT NOT NULL"
    };

    public static final String TABLE_CREATE = StmtGenerator.createTableStatement(TABLE_NAME, TABLE_COLS, TABLE_COL_TYPES);

    public static final String U_INDEX_CREATE = StmtGenerator.createUniqueIndexStatement(TABLE_NAME, EVENT_TYPE_CODE_COLUMN_NAME);

    public static final String INITIAL_LOAD = StmtGenerator.createInsertTableStatement(
            TABLE_NAME,
            Arrays.copyOfRange(TABLE_COLS, 1, TABLE_COLS.length),
            "SELECT 1, '" +
                    BasicHEventTypeA.EVENT_TYPE_CODE_NOTE_ITEMS +   "', '" +
                    BasicHEventTypeA.EVENT_TYPE_NAME_NOTE_ITEMS + "' UNION ALL SELECT 1, '" +
                    BasicHEventTypeA.EVENT_TYPE_CODE_CHECKOUT + "', '" +
                    BasicHEventTypeA.EVENT_TYPE_NAME_CHECKOUT + "'"
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
    @Nullable
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
