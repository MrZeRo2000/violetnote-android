package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.romanpulov.violetnote.db.tabledef.DBDefFactory;

/**
 * BasicNote database open helper
 * Created by romanpulov on 16.08.2016.
 */
public class DBBasicNoteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "basic_note.db";
    public static final int DATABASE_VERSION = 5;

    private final Context mContext;

    public DBBasicNoteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String s : DBDefFactory.buildDBCreate()) {
            db.execSQL(s);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.endTransaction();
        db.setForeignKeyConstraintsEnabled(false);
        for (String s : DBDefFactory.buildDBUpgrade(oldVersion)) {
            db.execSQL(s);
        }
        db.beginTransaction();

        DBDataUpgradeManager.upgradeData(mContext, oldVersion);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
