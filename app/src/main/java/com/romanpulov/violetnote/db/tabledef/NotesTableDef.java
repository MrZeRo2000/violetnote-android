package com.romanpulov.violetnote.db.tabledef;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * notes table definition
 */
public final class NotesTableDef implements DBCommonDef.TableDefSQLProvider {

    //table
    public static final String TABLE_NAME = "notes";

    //columns
    public static final String LAST_MODIFIED_COLUMN_NAME = DBCommonDef.LAST_MODIFIED_COLUMN_NAME;
    public static final String ORDER_COLUMN_NAME = DBCommonDef.ORDER_COLUMN_NAME;
    public static final String GROUP_ID_COLUMN_NAME = DBCommonDef.GROUP_ID_COLUMN_NAME;
    public static final String NOTE_TYPE_COLUMN_NAME = "note_type";
    public static final String TITLE_COLUMN_NAME = "title";
    public static final String IS_ENCRYPTED_COLUMN_NAME = "is_encrypted";
    public static final String ENCRYPTED_STRING_COLUMN_NAME = DBCommonDef.ENCRYPTED_STRING_COLUMN_NAME;

    public static final String[] TABLE_COLS = new String[] {
            DBCommonDef.ID_COLUMN_NAME,
            LAST_MODIFIED_COLUMN_NAME,
            ORDER_COLUMN_NAME,
            GROUP_ID_COLUMN_NAME,
            NOTE_TYPE_COLUMN_NAME,
            TITLE_COLUMN_NAME,
            IS_ENCRYPTED_COLUMN_NAME,
            ENCRYPTED_STRING_COLUMN_NAME
    };

    public static final String[] TABLE_COL_TYPES = new String[] {
            "INTEGER PRIMARY KEY",
            "INTEGER NOT NULL",
            "INTEGER NOT NULL",
            "INTEGER NOT NULL",
            "INTEGER NOT NULL",
            "TEXT NOT NULL",
            "INTEGER",
            "TEXT"
    };

    public static final String TABLE_CREATE = StmtGenerator.createTableStatement(
            TABLE_NAME,
            TABLE_COLS,
            TABLE_COL_TYPES,
            new StmtGenerator.ForeignKeyRec[] {
                    new StmtGenerator.ForeignKeyRec(GROUP_ID_COLUMN_NAME, NoteGroupsTableDef.TABLE_NAME, DBCommonDef.ID_COLUMN_NAME)
            }
            );

    public static final String FK_INDEX_GROUP_ID_CREATE = StmtGenerator.createForeignKeyIndexStatement(TABLE_NAME, GROUP_ID_COLUMN_NAME);

    public static final String TABLE_ADD_GROUP_ID = StmtGenerator.createAlterTableAddStatement(TABLE_NAME, GROUP_ID_COLUMN_NAME, "INTEGER NOT NULL");

    @Override
    public List<String> getSQLCreate() {
        return Arrays.asList(
                TABLE_CREATE,
                FK_INDEX_GROUP_ID_CREATE
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
                                "PRAGMA foreign_keys=off;",
                                "ALTER TABLE notes RENAME TO _notes_old;",
                                TABLE_CREATE,
                                FK_INDEX_GROUP_ID_CREATE,
                                "INSERT INTO notes (_id, last_modified, order_id, group_id, note_type, title, is_encrypted, encrypted_string) " +
                                "SELECT _id, last_modified, order_id, 2, note_type, title, is_encrypted, encrypted_string FROM _notes_old;",
                                "DROP TABLE _notes_old;",
                                "PRAGMA foreign_keys=on;"
                        )
                );
            case 100:
                return result;
            default:
                return null;
        }
    }
}
