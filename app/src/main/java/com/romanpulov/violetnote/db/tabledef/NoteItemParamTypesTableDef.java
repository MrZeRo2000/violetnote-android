package com.romanpulov.violetnote.db.tabledef;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * note_item_param_types table definition
 */
public final class NoteItemParamTypesTableDef implements DBCommonDef.TableDefSQLProvider {

    //table
    public static final String TABLE_NAME = "note_item_param_types";

    //columns
    public static final String PARAM_TYPE_NAME_COLUMN_NAME = "param_type_name";

    public static final String[] TABLE_COLS = new String[] {
            DBCommonDef.ID_COLUMN_NAME,
            PARAM_TYPE_NAME_COLUMN_NAME,
            DBCommonDef.ORDER_COLUMN_NAME
    };

    public static final String[] TABLE_COL_TYPES = new String[] {
            "INTEGER PRIMARY KEY",
            "TEXT NOT NULL",
            "INTEGER NOT NULL"
    };

    public static final String TABLE_CREATE = StmtGenerator.createTableStatement(TABLE_NAME, TABLE_COLS, TABLE_COL_TYPES);

    public static final String U_INDEX_CREATE = StmtGenerator.createUniqueIndexStatement(TABLE_NAME, PARAM_TYPE_NAME_COLUMN_NAME);

    public static final String INITIAL_LOAD = StmtGenerator.createInsertTableStatement(
            TABLE_NAME,
            Arrays.copyOfRange(TABLE_COLS, 1, TABLE_COLS.length),
            "SELECT '" + DBCommonDef.NOTE_ITEM_PARAM_TYPE_NAME_PRICE + "', 1"
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
