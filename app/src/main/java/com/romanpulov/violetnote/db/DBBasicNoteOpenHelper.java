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

    //notes
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String[] NOTES_TABLE_COLS = new String[] {
        "_id",
        "last_modified",
        "order_id",
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
            "_id",
            "last_modified",
            "note_id",
            "order_id",
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
                    " FOREIGN KEY (" + NOTE_ITEMS_TABLE_COLS[2] + ") REFERENCES " + NOTES_TABLE_NAME + "(" + NOTES_TABLE_COLS[0] + ")" +
                    ");";

    public DBBasicNoteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTES_TABLE_CREATE);
        db.execSQL(NOTE_ITEMS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
