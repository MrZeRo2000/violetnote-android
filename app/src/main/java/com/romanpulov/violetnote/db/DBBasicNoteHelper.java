package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by romanpulov on 16.08.2016.
 */
public class DBBasicNoteHelper {
    private static DBBasicNoteHelper mInstance;

    public static DBBasicNoteHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DBBasicNoteHelper(context);
        return mInstance;
    }

    //private final Context mContext;
    private DBBasicNoteOpenHelper mDBOpenHelper;
    private SQLiteDatabase mDB;

    private DBBasicNoteHelper(Context context) {
        //mContext = context;
        mDBOpenHelper = new DBBasicNoteOpenHelper(context);
        openDB();
    }

    public void openDB() {
        if (mDB == null)
            mDB = mDBOpenHelper.getWritableDatabase();
    }

    public void closeDB() {
        if (mDB != null) {
            mDB.close();
            mDB = null;
        }
    }

    public SQLiteDatabase getDB() {
        return mDB;
    }

    public long getMaxOrderId(String tableName) {
        Cursor c = null;
        try {
            c = mDB.query(tableName, new String[]{"MAX(" + DBBasicNoteOpenHelper.DEFAULT_ORDER_COLUMN + ")"}, null, null, null, null, null);
            c.moveToFirst();
            return c.isNull(0) ? 0 : c.getLong(0);
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }

}
