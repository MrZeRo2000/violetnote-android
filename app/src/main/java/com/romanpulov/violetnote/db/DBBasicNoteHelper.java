package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database BasicNote operations helper class
 * Created by romanpulov on 16.08.2016.
 */
public class DBBasicNoteHelper {
    public static String MAX_AGGREGATE_FUNCTION_NAME = "MAX";
    public static String MIN_AGGREGATE_FUNCTION_NAME = "MIN";

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

    public long getAggregateColumn(String tableName, String columnName, String aggregateFunction, String selection, String[] selectionArgs) {
        Cursor c = null;
        try {
            c = mDB.query(tableName, new String[]{aggregateFunction + "(" + columnName + ")"}, selection, selectionArgs, null, null, null);
            c.moveToFirst();
            return c.isNull(0) ? 0 : c.getLong(0);
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }

    /**
     * Selection string with conditions
     * @param noteId 0 for notes, note_id value for note items
     * @return selection string
     */
    private static String getNoteIdSelection(long noteId) {
        if (noteId == 0)
            return null;
        else
            return DBBasicNoteOpenHelper.NOTE_ID_SELECTION_STRING;
    }

    /**
     * Selection args
     * @param noteId 0 for notes, note_id value for note items
     * @return selection args
     */
    private static String[] getNoteIdSelectionArgs(long noteId) {
        if (noteId == 0)
            return null;
        else
            return new String[] {String.valueOf(noteId)};
    }

    public long getMaxOrderId(String tableName, String selection, String[] selectionArgs) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ORDER_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME,
                selection, selectionArgs);
    }

    public long getMaxOrderId(String tableName, long noteId) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ORDER_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME,
                getNoteIdSelection(noteId), getNoteIdSelectionArgs(noteId));
    }

    public long getMinOrderId(String tableName, long noteId) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ORDER_COLUMN_NAME, MIN_AGGREGATE_FUNCTION_NAME,
                getNoteIdSelection(noteId), getNoteIdSelectionArgs(noteId));
    }

    public long getMinOrderId(String tableName, String selection, String[] selectionArgs) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ORDER_COLUMN_NAME, MIN_AGGREGATE_FUNCTION_NAME,
                selection, selectionArgs);
    }

    public long getMaxId(String tableName, long noteId) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ID_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME,
                getNoteIdSelection(noteId), getNoteIdSelectionArgs(noteId));
    }

    public long getMinId(String tableName, long noteId) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ID_COLUMN_NAME, MIN_AGGREGATE_FUNCTION_NAME,
                getNoteIdSelection(noteId), getNoteIdSelectionArgs(noteId));
    }

    public long getPrevOrderId(String tableName, String selection, String[] selectionArgs) {
        return getAggregateColumn(
                tableName,
                DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                selection,
                selectionArgs
        );
    }

    public long getPrevOrderId(String tableName, long noteId, long orderId) {
        if (noteId == 0)
            return getAggregateColumn(
                    tableName,
                    DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                    DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                    DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " < ?",
                    new String[] {String.valueOf(orderId)}
            );
        else
            return getAggregateColumn(
                    tableName,
                    DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                    DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                    DBBasicNoteOpenHelper.NOTE_ID_SELECTION_STRING + " AND "  + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " < ?",
                    new String[] {String.valueOf(noteId), String.valueOf(orderId)}
            );
    }

    public long getNextOrderId(String tableName, String selection, String[] selectionArgs) {
        return getAggregateColumn(
                tableName,
                DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                DBBasicNoteHelper.MIN_AGGREGATE_FUNCTION_NAME,
                selection,
                selectionArgs
        );
    }

    public long getNextOrderId(String tableName, long noteId, long orderId) {
        if (noteId == 0)
            return getAggregateColumn(
                    tableName,
                    DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                    DBBasicNoteHelper.MIN_AGGREGATE_FUNCTION_NAME,
                    DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " > ?",
                    new String[] {String.valueOf(orderId)}
            );
        else
            return getAggregateColumn(
                    tableName,
                    DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                    DBBasicNoteHelper.MIN_AGGREGATE_FUNCTION_NAME,
                    DBBasicNoteOpenHelper.NOTE_ID_SELECTION_STRING + " AND "  + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " > ?",
                    new String[] {String.valueOf(noteId), String.valueOf(orderId)}
            );
    }

    public void exchangeOrderId(String tableName, String selectionString, long orderId1, long orderId2) {
        String sql = "UPDATE " + tableName + " SET " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId1 + " THEN " + orderId2 +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId2 + " THEN " + orderId1 +
                " END " +
                " WHERE " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " IN (" + orderId1 + ", " + orderId2 + ")";
        if (selectionString != null)
            sql = sql + " AND " + selectionString;
        mDB.execSQL(sql);
    }

    public void exchangeOrderId(String tableName, long noteId, long orderId1, long orderId2) {
        String sql = "UPDATE " + tableName + " SET " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId1 + " THEN " + orderId2 +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId2 + " THEN " + orderId1 +
                " END " +
                " WHERE " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " IN (" + orderId1 + ", " + orderId2 + ")";
        if (noteId > 0)
            sql = sql + " AND " + DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = " + String.valueOf(noteId);
        mDB.execSQL(sql);
    }

    public void moveOrderIdTop(String tableName, String selectionString, long orderId, long minOrderId) {
        String sql = "UPDATE " + tableName + " SET " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId + " THEN " + minOrderId +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " < " + orderId + " THEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " + 1" +
                " END " +
                " WHERE " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " <= " + orderId;
        if (selectionString != null)
            sql = sql + " AND " + selectionString;
        mDB.execSQL(sql);
    }


    public void moveOrderIdTop(String tableName, long noteId, long orderId) {
        long minOrderId = getMinOrderId(tableName, noteId);
        String sql = "UPDATE " + tableName + " SET " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId + " THEN " + minOrderId +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " < " + orderId + " THEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " + 1" +
                " END " +
                " WHERE " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " <= " + orderId;
        if (noteId > 0)
            sql = sql + " AND " + DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = " + String.valueOf(noteId);
        mDB.execSQL(sql);
    }

    public void moveOrderIdBottom(String tableName, String selectionString, long orderId, long maxOrderId) {
        String sql = "UPDATE " + tableName + " SET " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId + " THEN " + maxOrderId +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " > " + orderId + " THEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " - 1" +
                " END " +
                " WHERE " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " >= " + orderId;
        if (selectionString != null)
            sql = sql + " AND " + selectionString;
        mDB.execSQL(sql);
    }

    public void moveOrderIdBottom(String tableName, long noteId, long orderId) {
        long maxOrderId = getMaxOrderId(tableName, noteId);
        String sql = "UPDATE " + tableName + " SET " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId + " THEN " + maxOrderId +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " > " + orderId + " THEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " - 1" +
                " END " +
                " WHERE " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " >= " + orderId;
        if (noteId > 0)
            sql = sql + " AND " + DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = " + String.valueOf(noteId);
        mDB.execSQL(sql);
    }

    public int updatePriority(String tableName, long id, long priority) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.PRIORITY_COLUMN_NAME, priority);

        return mDB.update(tableName, cv, DBBasicNoteOpenHelper.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(id)});
    }

    public int updateOrderId(String tableName, long id, long orderId) {
        ContentValues cv = new ContentValues();

        cv.put(DBBasicNoteOpenHelper.ORDER_COLUMN_NAME, orderId);

        return mDB.update(tableName, cv, DBBasicNoteOpenHelper.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(id)});
    }

    /**
     * Get order by id
     * @param tableName Ordered table
     * @param id Row id
     * @return OrderId
     */
    public long getOrderId(String tableName, long id) {
        Cursor c = null;
        try {
            c = mDB.query(tableName, new String[]{DBBasicNoteOpenHelper.ORDER_COLUMN_NAME}, DBBasicNoteOpenHelper.ID_COLUMN_NAME + " = ?", new String[] {String.valueOf(id)}, null, null, null);
            c.moveToFirst();
            return c.isNull(0) ? 0 : c.getLong(0);
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }
}
