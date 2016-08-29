package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
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

    public long getMaxOrderId(String tableName) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ORDER_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME, null, null);
    }

    public long getMinOrderId(String tableName) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ORDER_COLUMN_NAME, MIN_AGGREGATE_FUNCTION_NAME, null, null);
    }

    public long getMaxId(String tableName) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ID_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME, null, null);
    }

    public long getMinId(String tableName) {
        return getAggregateColumn(tableName, DBBasicNoteOpenHelper.ID_COLUMN_NAME, MIN_AGGREGATE_FUNCTION_NAME, null, null);
    }

    public long getPrevOrderId(String tableName, long orderId) {
        return getAggregateColumn(
                tableName,
                DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " < ?",
                new String[] {String.valueOf(orderId)}
        );
    }

    public long getNextOrderId(String tableName, long orderId) {
        return getAggregateColumn(
                tableName,
                DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                DBBasicNoteHelper.MIN_AGGREGATE_FUNCTION_NAME,
                DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " > ?",
                new String[] {String.valueOf(orderId)}
        );
    }

    public void exchangeOrderId(String tableName, long orderId1, long orderId2) {
        String sql = "UPDATE " + tableName + " SET " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId1 + " THEN " + orderId2 +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId2 + " THEN " + orderId1 +
                " END " +
                " WHERE " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " IN (" + orderId1 + ", " + orderId2 + ")";
        mDB.execSQL(sql);
    }

    public void moveOrderIdTop(String tableName, long orderId) {
        long minOrderId = getMinOrderId(tableName);
        String sql = "UPDATE " + tableName + " SET " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId + " THEN " + minOrderId +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " < " + orderId + " THEN " + orderId + " + 1" +
                " END " +
                " WHERE " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " <= " + orderId;
        mDB.execSQL(sql);
    }

    public void moveOrderIdBottom(String tableName, long orderId) {
        long maxOrderId = getMaxOrderId(tableName);
        String sql = "UPDATE " + tableName + " SET " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " = " + orderId + " THEN " + maxOrderId +
                " WHEN " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " > " + orderId + " THEN " + orderId + " - 1" +
                " END " +
                " WHERE " + DBBasicNoteOpenHelper.ORDER_COLUMN_NAME + " >= " + orderId;
        mDB.execSQL(sql);
    }


}
