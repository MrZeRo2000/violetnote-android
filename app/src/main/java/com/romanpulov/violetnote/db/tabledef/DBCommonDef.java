package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Common definitions
 */
public final class DBCommonDef {

    //common column names
    public static final String ID_COLUMN_NAME = "_id";
    public static final String NOTE_ID_COLUMN_NAME = "note_id";
    public static final String NOTE_ITEM_ID_COLUMN_NAME = "note_item_id";
    public static final String LAST_MODIFIED_COLUMN_NAME = "last_modified";
    public static final String ORDER_COLUMN_NAME = "order_id";
    public static final String NAME_COLUMN_NAME = "name";
    public static final String VALUE_COLUMN_NAME = "value";
    public static final String CHECKED_COLUMN_NAME = "checked";
    public static final String PRIORITY_COLUMN_NAME = "priority";
    public static final String ENCRYPTED_STRING_COLUMN_NAME = "encrypted_string";
    public static final String GROUP_ID_COLUMN_NAME = "group_id";
    //events related
    public static final String EVENT_TYPE_ID_COLUMN_NAME = "event_type_id";
    public static final String EVENT_ID_COLUMN_NAME = "event_id";
    public static final String H_NOTE_CO_ITEM_ID_COLUMN_NAME = "h_note_co_item_id";
    public static final String NOTE_ITEM_PARAM_TYPE_ID = "note_item_param_type_id";

    //parameter names
    public static final String NOTE_ITEM_PARAM_TYPE_NAME_PRICE = "Price";

    //note_id selection
    public static final String NOTE_ID_SELECTION_STRING = NOTE_ID_COLUMN_NAME + " = ?";

    //priority selection
    public static final String PRIORITY_SELECTION_STRING = PRIORITY_COLUMN_NAME + " = ?";

    public static final String AND_STRING = " AND ";

    public static String getSelectionFieldDesc(String fieldName) {
        return fieldName + " DESC";
    }

    public interface TableDefSQLProvider {
        List<String> getSQLCreate();
        List<String> getSQLUpgrade(int oldVersion);
    }
}
