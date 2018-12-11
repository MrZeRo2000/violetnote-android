package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * note_items table definition
 */
public final class NoteItemsTableDef implements DBCommonDef.TableDefSQLProvider {

    //table
    public static final String TABLE_NAME = "note_items";

    //columns
    public static final String LAST_MODIFIED_COLUMN_NAME = DBCommonDef.LAST_MODIFIED_COLUMN_NAME;
    public static final String ORDER_COLUMN_NAME = DBCommonDef.ORDER_COLUMN_NAME;
    public static final String NOTE_ID_COLUMN_NAME = DBCommonDef.NOTE_ID_COLUMN_NAME;
    public static final String NAME_COLUMN_NAME = DBCommonDef.NAME_COLUMN_NAME;
    public static final String VALUE_COLUMN_NAME = DBCommonDef.VALUE_COLUMN_NAME;
    public static final String CHECKED_COLUMN_NAME = DBCommonDef.CHECKED_COLUMN_NAME;
    public static final String PRIORITY_COLUMN_NAME = DBCommonDef.PRIORITY_COLUMN_NAME;

    public static final String[] TABLE_COLS = new String[] {
            DBCommonDef.ID_COLUMN_NAME,
            LAST_MODIFIED_COLUMN_NAME,
            ORDER_COLUMN_NAME,
            NOTE_ID_COLUMN_NAME,
            NAME_COLUMN_NAME,
            VALUE_COLUMN_NAME,
            CHECKED_COLUMN_NAME,
            PRIORITY_COLUMN_NAME
    };

    public static final String[] TABLE_COL_TYPES = new String[] {
            "INTEGER PRIMARY KEY",
            "INTEGER NOT NULL",
            "INTEGER NOT NULL",
            "INTEGER NOT NULL",
            "TEXT",
            "TEXT",
            "INTEGER",
            "INTEGER NOT NULL DEFAULT 0"
    };

    public static final String TABLE_CREATE = StmtGenerator.createTableStatement(
            TABLE_NAME,
            TABLE_COLS,
            TABLE_COL_TYPES,
            new StmtGenerator.ForeignKeyRec[] {
                    new StmtGenerator.ForeignKeyRec(NOTE_ID_COLUMN_NAME, NotesTableDef.TABLE_NAME, DBCommonDef.ID_COLUMN_NAME)
            }
    );

    public static final String FK_INDEX_NOTE_ID_CREATE = StmtGenerator.createForeignKeyIndexStatement(TABLE_NAME, NOTE_ID_COLUMN_NAME);

    @Override
    public List<String> getSQLCreate() {
        return Arrays.asList(
                TABLE_CREATE,
                FK_INDEX_NOTE_ID_CREATE
        );
    }

    @Override
    @Nullable
    public List<String> getSQLUpgrade(int oldVersion) {
        return null;
    }
}
