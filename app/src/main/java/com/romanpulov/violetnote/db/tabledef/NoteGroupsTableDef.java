package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * note_groups table definition
 */
public final class NoteGroupsTableDef implements DBCommonDef.TableDefSQLProvider {

    //table
    public static final String TABLE_NAME = "note_groups";

    //columns
    public static final String NOTE_GROUP_TYPE_COLUMN_NAME = "note_group_type";
    public static final String NOTE_GROUP_NAME_COLUMN_NAME = "note_group_name";
    public static final String NOTE_GROUP_ICON_COLUMN_NAME = "note_group_icon";
    public static final String NOTE_GROUP_DISPLAY_OPTIONS = "note_group_display_options";

    public static final String[] TABLE_COLS = new String[] {
            DBCommonDef.ID_COLUMN_NAME,
            NOTE_GROUP_TYPE_COLUMN_NAME,
            NOTE_GROUP_NAME_COLUMN_NAME,
            NOTE_GROUP_ICON_COLUMN_NAME,
            DBCommonDef.ORDER_COLUMN_NAME,
            NOTE_GROUP_DISPLAY_OPTIONS
    };

    public static final String[] TABLE_COL_TYPES = new String[] {
            "INTEGER PRIMARY KEY",
            "INTEGER NOT NULL",
            "TEXT NOT NULL",
            "INTEGER NOT NULL",
            "INTEGER NOT NULL",
            "TEXT NULL"
    };

    public static final String TABLE_CREATE = StmtGenerator.createTableStatement(TABLE_NAME, TABLE_COLS, TABLE_COL_TYPES);

    public static final String U_INDEX_CREATE = StmtGenerator.createUniqueIndexStatement(TABLE_NAME, NOTE_GROUP_NAME_COLUMN_NAME);

    public static final String INITIAL_LOAD = StmtGenerator.createInsertTableStatement(
            TABLE_NAME,
            Arrays.copyOfRange(TABLE_COLS, 1, TABLE_COLS.length),
            "SELECT 1, 'Password Notes', 0, 1, NULL " +
                    "UNION ALL " +
                    "SELECT 10, 'Basic Notes', 0, 2, NULL"
    );

    @Override
    @NonNull
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
            case 3:
                result.add(
                        "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + NOTE_GROUP_DISPLAY_OPTIONS + " TEXT NULL;"
                );
            case 100:
                return result;
            default:
                return null;
        }
    }
}
