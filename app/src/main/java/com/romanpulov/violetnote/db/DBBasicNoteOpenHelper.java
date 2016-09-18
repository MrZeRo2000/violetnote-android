package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by romanpulov on 16.08.2016.
 */
public class DBBasicNoteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "basic_note.db";
    public static final int DATABASE_VERSION = 1;

    //common column names
    public static final String ID_COLUMN_NAME = "_id";
    public static final String NOTE_ID_COLUMN_NAME = "note_id";
    public static final String LAST_MODIFIED_COLUMN_NAME = "last_modified";
    public static final String ORDER_COLUMN_NAME = "order_id";

    //notes
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String[] NOTES_TABLE_COLS = new String[] {
        ID_COLUMN_NAME,
        LAST_MODIFIED_COLUMN_NAME,
        ORDER_COLUMN_NAME,
        "note_type",
        "title",
        "is_encrypted",
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
            "value",
            "checked"
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
                    " FOREIGN KEY (" + NOTE_ITEMS_TABLE_COLS[3] + ") REFERENCES " + NOTES_TABLE_NAME + "(" + NOTES_TABLE_COLS[0] + ")" +
                    ");";

    //note values
    public static final String NOTE_VALUES_TABLE_NAME = "note_values";
    public static final String[] NOTE_VALUES_TABLE_COLS = new String[] {
            ID_COLUMN_NAME,
            NOTE_ID_COLUMN_NAME,
            "value"
    };
    private static final String NOTE_VALUES_TABLE_CREATE =
            "CREATE TABLE " + NOTE_VALUES_TABLE_NAME + " (" +
                    NOTE_VALUES_TABLE_COLS[0] + " INTEGER PRIMARY KEY," +
                    NOTE_VALUES_TABLE_COLS[1] + " INTEGER ," +
                    NOTE_VALUES_TABLE_COLS[2] + " TEXT," +
                    " FOREIGN KEY (" + NOTE_VALUES_TABLE_COLS[1] + ") REFERENCES " + NOTES_TABLE_NAME + "(" + NOTES_TABLE_COLS[0] + ")" +
                    ");";

    //note_id selection
    public static final String NOTE_ID_SELECTION_STRING = NOTE_ID_COLUMN_NAME + " = ?";

    public DBBasicNoteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTES_TABLE_CREATE);
        db.execSQL(NOTE_ITEMS_TABLE_CREATE);
        db.execSQL(NOTE_VALUES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
