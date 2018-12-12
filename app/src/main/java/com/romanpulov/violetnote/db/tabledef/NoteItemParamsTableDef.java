package com.romanpulov.violetnote.db.tabledef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * note_item_params table definition
 */
public final class NoteItemParamsTableDef implements DBCommonDef.TableDefSQLProvider {

    //table
    public static final String TABLE_NAME = "note_item_params";

    //columns
    public static final String NOTE_ITEM_ID_COLUMN_NAME = DBCommonDef.NOTE_ITEM_ID_COLUMN_NAME;
    public static final String NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME = "note_item_param_type_id";
    public static final String V_INT_COLUMN_NAME = "v_int";
    public static final String V_TEXT_COLUMN_NAME = "v_text";

    public static final String[] TABLE_COLS = new String[] {
            DBCommonDef.ID_COLUMN_NAME,
            NOTE_ITEM_ID_COLUMN_NAME,
            NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME,
            V_INT_COLUMN_NAME,
            V_TEXT_COLUMN_NAME
    };

    public static final String[] TABLE_COL_TYPES = new String[] {
            "INTEGER PRIMARY KEY",
            "INTEGER NOT NULL",
            "INTEGER NOT NULL",
            "INTEGER",
            "TEXT"
    };

    public static final String TABLE_CREATE = StmtGenerator.createTableStatement(
            TABLE_NAME,
            TABLE_COLS,
            TABLE_COL_TYPES,
            new StmtGenerator.ForeignKeyRec[] {
                    new StmtGenerator.ForeignKeyRec(NOTE_ITEM_ID_COLUMN_NAME, NoteItemsTableDef.TABLE_NAME, DBCommonDef.ID_COLUMN_NAME),
                    new StmtGenerator.ForeignKeyRec(NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME, NoteItemParamTypesTableDef.TABLE_NAME, DBCommonDef.ID_COLUMN_NAME)
            }
    );

    public static final String FK_INDEX_NOTE_ITEM_ID_CREATE = StmtGenerator.createForeignKeyIndexStatement(TABLE_NAME, NOTE_ITEM_ID_COLUMN_NAME);
    public static final String FK_INDEX_NOTE_ITEM_PARAM_TYPE_ID_CREATE = StmtGenerator.createForeignKeyIndexStatement(TABLE_NAME, NOTE_ITEM_PARAM_TYPE_ID_COLUMN_NAME);

    @Override
    public List<String> getSQLCreate() {
        return Arrays.asList(
                TABLE_CREATE,
                FK_INDEX_NOTE_ITEM_ID_CREATE,
                FK_INDEX_NOTE_ITEM_PARAM_TYPE_ID_CREATE
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
                                FK_INDEX_NOTE_ITEM_ID_CREATE,
                                FK_INDEX_NOTE_ITEM_PARAM_TYPE_ID_CREATE
                        )
                );
            case 100:
                return result;
            default:
                return null;
        }

    }
}
