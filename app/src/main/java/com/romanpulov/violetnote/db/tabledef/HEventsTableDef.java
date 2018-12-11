package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * h_events table definition
 */
public final class HEventsTableDef implements DBCommonDef.TableDefSQLProvider {

    //table
    public static final String TABLE_NAME = "h_events";

    //columns
    public static final String EVENT_TYPE_ID_COLUMN_NAME = DBCommonDef.EVENT_TYPE_ID_COLUMN_NAME;
    public static final String EVENT_TIME_COLUMN_NAME = "event_time";
    public static final String EVENT_SUMMARY_COLUMN_NAME = "event_summary";

    public static final String[] TABLE_COLS = new String[] {
            DBCommonDef.ID_COLUMN_NAME,
            EVENT_TYPE_ID_COLUMN_NAME,
            EVENT_TIME_COLUMN_NAME,
            EVENT_SUMMARY_COLUMN_NAME
    };

    public static final String[] TABLE_COL_TYPES = new String[] {
            "INTEGER PRIMARY KEY",
            "INTEGER NOT NULL",
            "INTEGER NOT NULL",
            "TEXT"
    };

    public static final String TABLE_CREATE = StmtGenerator.createTableStatement(
            TABLE_NAME,
            TABLE_COLS,
            TABLE_COL_TYPES,
            new StmtGenerator.ForeignKeyRec[]{
                    new StmtGenerator.ForeignKeyRec(EVENT_TYPE_ID_COLUMN_NAME, HEventTypesTableDef.TABLE_NAME, DBCommonDef.EVENT_TYPE_ID_COLUMN_NAME)
            });

    public static final String U_INDEX_CREATE = StmtGenerator.createUniqueIndexStatement(TABLE_NAME, EVENT_TIME_COLUMN_NAME);

    public static final String FK_INDEX_CREATE = StmtGenerator.createForeignKeyIndexStatement(TABLE_NAME, EVENT_TYPE_ID_COLUMN_NAME);

    @Override
    public List<String> getSQLCreate() {
        return Arrays.asList(
                TABLE_CREATE,
                U_INDEX_CREATE,
                FK_INDEX_CREATE
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
                                FK_INDEX_CREATE
                        )
                );
            case 100:
                return result;
            default:
                return null;
        }
    }
}
