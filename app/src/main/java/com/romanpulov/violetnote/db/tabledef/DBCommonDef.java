package com.romanpulov.violetnote.db.tabledef;

import java.util.Collection;
import java.util.List;

/**
 * Common definitions
 */
public final class DBCommonDef {

    //common column names
    public static final String ID_COLUMN_NAME = "_id";
    public static final String NOTE_ID_COLUMN_NAME = "note_id";
    public static final String LAST_MODIFIED_COLUMN_NAME = "last_modified";
    public static final String ORDER_COLUMN_NAME = "order_id";
    public static final String NAME_COLUMN_NAME = "name";
    public static final String VALUE_COLUMN_NAME = "value";
    public static final String CHECKED_COLUMN_NAME = "checked";
    public static final String PRIORITY_COLUMN_NAME = "priority";
    public static final String ENCRYPTED_STRING_COLUMN_NAME = "encrypted_string";
    //events related
    public static final String EVENT_TYPE_ID_COLUMN_NAME = "event_type_id";
    public static final String EVENT_ID_COLUMN_NAME = "event_id";

    public interface TableDefSQLProvider {
        List<String> getSQLCreate();
        List<String> getSQLUpgrade(int oldVersion);
    }
}
