package com.romanpulov.violetnote.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DateTimeFormatter;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;

import java.util.List;

/**
 * Base abstract class for DAO classes
 */
public abstract class AbstractDAO<T> {
    final Context mContext;
    final DBBasicNoteHelper mDBHelper;
    final SQLiteDatabase mDB;
    final DateTimeFormatter mDTF;

    @NonNull
    public List<T> getAll() {
        throw new RuntimeException("getAll method not implemented");
    }

    public long delete(@NonNull T object) {
        throw new RuntimeException("delete method not implemented");
    }

    public long insert(@NonNull T object) {
        throw new RuntimeException("insert method not implemented");
    }

    public long update(@NonNull T object) {
        throw new RuntimeException("update method not implemented");
    }

    AbstractDAO(Context context) {
        mContext = context;
        mDBHelper = DBBasicNoteHelper.getInstance(mContext);
        mDB = mDBHelper.getDB();
        mDTF = new DateTimeFormatter(context);
    }

    interface CursorReaderHandler {
        Cursor createCursor();
        void readFromCursor(Cursor c);
    }

    final void readCursor(@NonNull AbstractDAO.CursorReaderHandler cursorReaderHandler) {
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

    final long internalDeleteById(String tableName, long id) {
        return internalDeleteById(tableName, DBCommonDef.ID_COLUMN_NAME, id);
    }

    final long internalDeleteById(String tableName, String columnName, long id) {
        return mDB.delete(tableName, columnName + " = ?", new String[] {String.valueOf(id)});
    }
}
