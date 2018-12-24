package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;

/**
 * Base class for DB manager
 */
public abstract class BasicDBManager {
    protected final Context mContext;
    protected final DBBasicNoteHelper mDBHelper;
    protected final SQLiteDatabase mDB;
    protected final DateTimeFormatter mDTF;

    BasicDBManager(Context context) {
        mContext = context;
        mDBHelper = DBBasicNoteHelper.getInstance(mContext);
        mDB = mDBHelper.getDB();
        mDTF = new DateTimeFormatter(context);
    }

    interface CursorReaderHandler {
        Cursor createCursor();
        void readFromCursor(Cursor c);
    }

    void readCursor(CursorReaderHandler cursorReaderHandler) {
        Cursor c = null;
        try {
            c = cursorReaderHandler.createCursor();
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                cursorReaderHandler.readFromCursor(c);
            }
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }

    long deleteById(String tableName, long id) {
        return deleteById(tableName, DBCommonDef.ID_COLUMN_NAME, id);
    }

    long deleteById(String tableName, String columnName, long id) {
        return mDB.delete(tableName, columnName + " = ?", new String[] {String.valueOf(id)});
    }

}
