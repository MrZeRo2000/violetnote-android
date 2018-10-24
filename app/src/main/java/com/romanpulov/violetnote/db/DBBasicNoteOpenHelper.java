package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * BasicNote database open helper
 * Created by romanpulov on 16.08.2016.
 */
public class DBBasicNoteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "basic_note.db";
    public static final int DATABASE_VERSION = 2;

    //common column names
    static final String ID_COLUMN_NAME = "_id";
    static final String NOTE_ID_COLUMN_NAME = "note_id";
    static final String LAST_MODIFIED_COLUMN_NAME = "last_modified";
    static final String ORDER_COLUMN_NAME = "order_id";
    static final String VALUE_COLUMN_NAME = "value";
    static final String CHECKED_COLUMN_NAME = "checked";
    static final String PRIORITY_COLUMN_NAME = "priority";

    //notes
    static final String NOTES_TABLE_NAME = "notes";
    static final String NOTE_TYPE_COLUMN_NAME = "note_type";
    static final String IS_ENCRYPTED_COLUMN_NAME = "is_encrypted";
    static final String TITLE_COLUMN_NAME = "title";
    static final String[] NOTES_TABLE_COLS = new String[] {
        ID_COLUMN_NAME,
        LAST_MODIFIED_COLUMN_NAME,
        ORDER_COLUMN_NAME,
        NOTE_TYPE_COLUMN_NAME,
        TITLE_COLUMN_NAME,
        IS_ENCRYPTED_COLUMN_NAME,
        "encrypted_string"
    };
    private static final String NOTES_TABLE_CREATE =
            "CREATE TABLE " + NOTES_TABLE_NAME + " (" +
                    NOTES_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTES_TABLE_COLS[1] + " INTEGER NOT NULL," +
                    NOTES_TABLE_COLS[2] + " INTEGER NOT NULL," +
                    NOTES_TABLE_COLS[3] + " INTEGER NOT NULL," +
                    NOTES_TABLE_COLS[4] + " TEXT NOT NULL," +
                    NOTES_TABLE_COLS[5] + " INTEGER," +
                    NOTES_TABLE_COLS[6] + " TEXT" +
                    ");";


    //note items
    public static final String NOTE_ITEMS_TABLE_NAME = "note_items";
    public static final String[] NOTE_ITEMS_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            LAST_MODIFIED_COLUMN_NAME,
            ORDER_COLUMN_NAME,
            NOTE_ID_COLUMN_NAME,
            "name",
            VALUE_COLUMN_NAME,
            CHECKED_COLUMN_NAME,
            PRIORITY_COLUMN_NAME
    };
    private static final String NOTE_ITEMS_TABLE_CREATE =
            "CREATE TABLE " + NOTE_ITEMS_TABLE_NAME + " (" +
                    NOTE_ITEMS_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_ITEMS_TABLE_COLS[1] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_TABLE_COLS[2] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_TABLE_COLS[3] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_TABLE_COLS[4] + " TEXT," +
                    NOTE_ITEMS_TABLE_COLS[5] + " TEXT," +
                    NOTE_ITEMS_TABLE_COLS[6] + " INTEGER," +
                    NOTE_ITEMS_TABLE_COLS[7] + " INTEGER NOT NULL DEFAULT 0," +
                    " FOREIGN KEY (" + NOTE_ITEMS_TABLE_COLS[3] + ") REFERENCES " + NOTES_TABLE_NAME + "(" + NOTES_TABLE_COLS[0] + ")" +
                    ");";
    private static final String NOTE_ITEMS_FK_INDEX_CREATE =
            "CREATE INDEX fk_" + NOTE_ITEMS_TABLE_NAME +
                    " ON " + NOTE_ITEMS_TABLE_NAME + "(" +
                    NOTE_ID_COLUMN_NAME + ")";

    //note values
    public static final String NOTE_VALUES_TABLE_NAME = "note_values";
    public static final String[] NOTE_VALUES_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            NOTE_ID_COLUMN_NAME,
            VALUE_COLUMN_NAME
    };
    private static final String NOTE_VALUES_TABLE_CREATE =
            "CREATE TABLE " + NOTE_VALUES_TABLE_NAME + " (" +
                    NOTE_VALUES_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_VALUES_TABLE_COLS[1] + " INTEGER ," +
                    NOTE_VALUES_TABLE_COLS[2] + " TEXT," +
                    " FOREIGN KEY (" + NOTE_VALUES_TABLE_COLS[1] + ") REFERENCES " + NOTES_TABLE_NAME + "(" + NOTES_TABLE_COLS[0] + ")" +
                    ");";
    private static final String NOTE_VALUES_FK_INDEX_CREATE =
            "CREATE INDEX fk_" + NOTE_VALUES_TABLE_NAME +
                    " ON " + NOTE_VALUES_TABLE_NAME + "(" +
                    NOTE_ID_COLUMN_NAME + ")";
    private static final String NOTE_VALUES_U_INDEX_CREATE =
            "CREATE UNIQUE INDEX u_" + NOTE_VALUES_TABLE_NAME +
                    " ON " + NOTE_VALUES_TABLE_NAME + "(" +
                    NOTE_ID_COLUMN_NAME + ", " +
                    VALUE_COLUMN_NAME +
                    ")";


    //note items history
    public static final String NOTE_ITEMS_HISTORY_TABLE_NAME = "note_items_history";
    public static final String[] NOTE_ITEMS_HISTORY_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            LAST_MODIFIED_COLUMN_NAME,
            NOTE_ID_COLUMN_NAME,
            VALUE_COLUMN_NAME
    };
    private static final String NOTE_ITEMS_HISTORY_TABLE_CREATE =
            "CREATE TABLE " + NOTE_ITEMS_HISTORY_TABLE_NAME + " (" +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[1] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[2] + " INTEGER NOT NULL," +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[3] + " TEXT NOT NULL," +
                    " FOREIGN KEY (" + NOTE_ITEMS_HISTORY_TABLE_COLS[2] + ") REFERENCES " + NOTES_TABLE_NAME + "(" + NOTES_TABLE_COLS[0] + ")" +
                    ");";
    private static final String NOTE_ITEMS_HISTORY_FK_INDEX_CREATE =
            "CREATE INDEX fk_" + NOTE_ITEMS_HISTORY_TABLE_NAME +
                    " ON " + NOTE_ITEMS_HISTORY_TABLE_NAME + "(" +
                    NOTE_ID_COLUMN_NAME + ")";
    private static final String NOTE_ITEMS_HISTORY_U_INDEX_CREATE =
            "CREATE UNIQUE INDEX u_node_items_history ON " +
                    NOTE_ITEMS_HISTORY_TABLE_NAME + " (" +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[2] + ", " +
                    NOTE_ITEMS_HISTORY_TABLE_COLS[3] +
                    ");";

    //note_id selection
    public static final String NOTE_ID_SELECTION_STRING = NOTE_ID_COLUMN_NAME + " = ?";

    //priority selection
    public static final String PRIORITY_SELECTION_STRING = PRIORITY_COLUMN_NAME + " = ?";

    public static final String AND_STRING = " AND ";

    public DBBasicNoteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTES_TABLE_CREATE);
        db.execSQL(NOTE_ITEMS_TABLE_CREATE);
        db.execSQL(NOTE_ITEMS_FK_INDEX_CREATE);
        db.execSQL(NOTE_VALUES_TABLE_CREATE);
        db.execSQL(NOTE_VALUES_FK_INDEX_CREATE);
        db.execSQL(NOTE_VALUES_U_INDEX_CREATE);
        db.execSQL(NOTE_ITEMS_HISTORY_TABLE_CREATE);
        db.execSQL(NOTE_ITEMS_HISTORY_FK_INDEX_CREATE);
        db.execSQL(NOTE_ITEMS_HISTORY_U_INDEX_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE " + NOTE_ITEMS_TABLE_NAME + " ADD priority INTEGER NOT NULL DEFAULT 0;");
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
