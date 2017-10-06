package com.romanpulov.violetnote.provider;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * BasicNote provider metadata class
 * Created by romanpulov on 16.08.2016.
 */
public class BasicNoteProviderMetaData {
    public static final String AUTHORITY = "com.romanpulov.violetnote.provider.BasicNoteProvider";
    public static final String DATABASE_NAME = "basicnote.db";
    public static final int DATABASE_VERSION = 1;

    public static final String BASIC_NOTE_TABLE_NAME = "note";
    public static final String BASIC_NOTE_ITEM_TABLE_NAME = "note_item";
    public static final String BASIC_NOTE_VALUE_TABLE_NAME = "note_value";

    public static final class BasicNoteTableMetaData implements BaseColumns {
        private BasicNoteTableMetaData() {

        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/note");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.romanpulov.violetnote.provider.note";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.romanpulov.violetnote.provider.note";
        public static final String DEFAULT_SORT_ORDER = "order_id ASC";

        public static final String LAST_MODIFIED = "last_modified";
        public static final String TYPE = "type";
        public static final String TITLE = "title";
        public static final String IS_ENCRYPTED = "is_encrypted";
        public static final String ENCRYPTED_STRING = "encrypted_string";
    }

}
