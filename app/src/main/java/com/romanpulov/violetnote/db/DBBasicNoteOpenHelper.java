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
                    NOTES_TABLE_COLS[5] + " INTEGER NOT NULL," +
                    NOTES_TABLE_COLS[6] + " TEXT" +
                    ");";


    //note items
    public static final String NOTE_ITEMS_TABLE_NAME = "note_items";
    public static final String[] NOTE_ITEMS_TABLE_COLS = new String[] {
            "_id",
            "note_id",
            "last_modified",
            "order_id",
            "name",
            "value",
            "checked"
    };

    public DBBasicNoteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
