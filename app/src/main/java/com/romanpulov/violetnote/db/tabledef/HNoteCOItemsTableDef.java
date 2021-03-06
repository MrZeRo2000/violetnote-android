package com.romanpulov.violetnote.db.tabledef;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * h_note_co_items table definition
 */
public final class HNoteCOItemsTableDef implements DBCommonDef.TableDefSQLProvider {

    //table
    public static final String TABLE_NAME = "h_note_co_items";

    //columns
    public static final String EVENT_ID_COLUMN_NAME = DBCommonDef.EVENT_ID_COLUMN_NAME;
    public static final String NOTE_ID_COLUMN_NAME = DBCommonDef.NOTE_ID_COLUMN_NAME;
    public static final String VALUE_COLUMN_NAME = DBCommonDef.VALUE_COLUMN_NAME;

    public static final String[] TABLE_COLS = new String[] {
            DBCommonDef.ID_COLUMN_NAME,
            EVENT_ID_COLUMN_NAME,
            NOTE_ID_COLUMN_NAME,
            VALUE_COLUMN_NAME
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
                    new StmtGenerator.ForeignKeyRec(EVENT_ID_COLUMN_NAME, HEventsTableDef.TABLE_NAME, DBCommonDef.ID_COLUMN_NAME),
                    new StmtGenerator.ForeignKeyRec(NOTE_ID_COLUMN_NAME, NotesTableDef.TABLE_NAME, DBCommonDef.ID_COLUMN_NAME)
            });

    public static final String FK_INDEX_EVENT_CREATE = StmtGenerator.createForeignKeyIndexStatement(TABLE_NAME, EVENT_ID_COLUMN_NAME);
    public static final String FK_INDEX_NOTE_CREATE = StmtGenerator.createForeignKeyIndexStatement(TABLE_NAME, NOTE_ID_COLUMN_NAME);

    @Override
    public List<String> getSQLCreate() {
        return Arrays.asList(
                TABLE_CREATE,
                FK_INDEX_EVENT_CREATE,
                FK_INDEX_NOTE_CREATE
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
                                FK_INDEX_EVENT_CREATE,
                                FK_INDEX_NOTE_CREATE
                        )
                );
            case 100:
                return result;
            default:
                return null;
        }
    }
}
