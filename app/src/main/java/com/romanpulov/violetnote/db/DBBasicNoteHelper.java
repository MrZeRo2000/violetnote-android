package com.romanpulov.violetnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemsTableDef;
import com.romanpulov.violetnote.db.tabledef.NotesTableDef;

/**
 * Database BasicNote operations helper class
 * Created by romanpulov on 16.08.2016.
 */
public class DBBasicNoteHelper {
    public static final String MAX_AGGREGATE_FUNCTION_NAME = "MAX";
    public static final String MIN_AGGREGATE_FUNCTION_NAME = "MIN";

    private static DBBasicNoteHelper mInstance;

    public static DBBasicNoteHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DBBasicNoteHelper(context.getApplicationContext());
        return mInstance;
    }

    private final Context mContext;
    private final DBBasicNoteOpenHelper mDBOpenHelper;
    private SQLiteDatabase mDB;

    private DBDictionaryCache mDBDictionaryCache;

    public DBDictionaryCache getDBDictionaryCache() {
        if (mDBDictionaryCache == null)
            mDBDictionaryCache = new DBDictionaryCache();
        if (!mDBDictionaryCache.isLoaded())
            mDBDictionaryCache.load(mContext);

        return mDBDictionaryCache;
    }

    private DBBasicNoteHelper(Context context) {
        mContext = context.getApplicationContext();
        mDBOpenHelper = new DBBasicNoteOpenHelper(mContext);
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
            if (mDBDictionaryCache != null) {
                mDBDictionaryCache.invalidate();
            }
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

    private static String getNoteIdPrioritySelection(long noteId, long priority) {
        return DBCommonDef.NOTE_ID_SELECTION_STRING + DBCommonDef.AND_STRING + DBCommonDef.PRIORITY_SELECTION_STRING;
    }

    private static String[] getNoteIdPrioritySelectionArgs(long noteId, long priority) {
        return new String[] {String.valueOf(noteId), String.valueOf(priority)};
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
            return DBCommonDef.NOTE_ID_SELECTION_STRING;
    }

    /**
     * Selection args
     * @param id 0 for notes, id value for items
     * @return selection args
     */
    private static String[] getIdSelectionArgs(long id) {
        if (id == 0)
            return null;
        else
            return new String[] {String.valueOf(id)};
    }

    private static String getGroupIdSelection(long groupId) {
        if (groupId == 0)
            return null;
        else
            return DBCommonDef.GROUP_ID_SELECTION_STRING;
    }


    public long getMaxOrderId(String tableName, String selection, String[] selectionArgs) {
        return getAggregateColumn(tableName, DBCommonDef.ORDER_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME,
                selection, selectionArgs);
    }

    public long getMaxNoteOrderId(String tableName, long noteId) {
        return getAggregateColumn(tableName, DBCommonDef.ORDER_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME,
                getNoteIdSelection(noteId), getIdSelectionArgs(noteId));
    }

    public long getNoteGroupMaxOrderId(long noteGroupId) {
        return getAggregateColumn(NotesTableDef.TABLE_NAME, DBCommonDef.ORDER_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME,
                getGroupIdSelection(noteGroupId), getIdSelectionArgs(noteGroupId));
    }

    /**
     * Returns max orderId for note with given priority
     * @param noteId note
     * @param priority proproty
     * @return orderId
     */
    public long getNoteMaxOrderId(long noteId, long priority) {
        return getAggregateColumn(NoteItemsTableDef.TABLE_NAME, DBCommonDef.ORDER_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME,
                getNoteIdPrioritySelection(noteId, priority), getNoteIdPrioritySelectionArgs(noteId, priority));
    }

    public long getMinOrderId(String tableName, long noteId) {
        return getAggregateColumn(tableName, DBCommonDef.ORDER_COLUMN_NAME, MIN_AGGREGATE_FUNCTION_NAME,
                getNoteIdSelection(noteId), getIdSelectionArgs(noteId));
    }

    public long getMinOrderId(String tableName, String selection, String[] selectionArgs) {
        return getAggregateColumn(tableName, DBCommonDef.ORDER_COLUMN_NAME, MIN_AGGREGATE_FUNCTION_NAME,
                selection, selectionArgs);
    }

    public long getMaxId(String tableName, long noteId) {
        return getAggregateColumn(tableName, DBCommonDef.ID_COLUMN_NAME, MAX_AGGREGATE_FUNCTION_NAME,
                getNoteIdSelection(noteId), getIdSelectionArgs(noteId));
    }

    public long getMinId(String tableName, long noteId) {
        return getAggregateColumn(tableName, DBCommonDef.ID_COLUMN_NAME, MIN_AGGREGATE_FUNCTION_NAME,
                getNoteIdSelection(noteId), getIdSelectionArgs(noteId));
    }

    public long getPrevOrderId(String tableName, String selection, String[] selectionArgs) {
        return getAggregateColumn(
                tableName,
                DBCommonDef.ORDER_COLUMN_NAME,
                DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                selection,
                selectionArgs
        );
    }

    public long getPrevOrderId(String tableName, long noteId, long orderId) {
        if (noteId == 0)
            return getAggregateColumn(
                    tableName,
                    DBCommonDef.ORDER_COLUMN_NAME,
                    DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                    DBCommonDef.ORDER_COLUMN_NAME + " < ?",
                    new String[] {String.valueOf(orderId)}
            );
        else
            return getAggregateColumn(
                    tableName,
                    DBCommonDef.ORDER_COLUMN_NAME,
                    DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                    DBCommonDef.NOTE_ID_SELECTION_STRING + " AND "  + DBCommonDef.ORDER_COLUMN_NAME + " < ?",
                    new String[] {String.valueOf(noteId), String.valueOf(orderId)}
            );
    }

    public long getNextOrderId(String tableName, String selection, String[] selectionArgs) {
        return getAggregateColumn(
                tableName,
                DBCommonDef.ORDER_COLUMN_NAME,
                DBBasicNoteHelper.MIN_AGGREGATE_FUNCTION_NAME,
                selection,
                selectionArgs
        );
    }

    public long getNextOrderId(String tableName, long noteId, long orderId) {
        if (noteId == 0)
            return getAggregateColumn(
                    tableName,
                    DBCommonDef.ORDER_COLUMN_NAME,
                    DBBasicNoteHelper.MIN_AGGREGATE_FUNCTION_NAME,
                    DBCommonDef.ORDER_COLUMN_NAME + " > ?",
                    new String[] {String.valueOf(orderId)}
            );
        else
            return getAggregateColumn(
                    tableName,
                    DBCommonDef.ORDER_COLUMN_NAME,
                    DBBasicNoteHelper.MIN_AGGREGATE_FUNCTION_NAME,
                    DBCommonDef.NOTE_ID_SELECTION_STRING + " AND "  + DBCommonDef.ORDER_COLUMN_NAME + " > ?",
                    new String[] {String.valueOf(noteId), String.valueOf(orderId)}
            );
    }

    public void exchangeOrderId(String tableName, String selectionString, long orderId1, long orderId2) {
        String sql = "UPDATE " + tableName + " SET " + DBCommonDef.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBCommonDef.ORDER_COLUMN_NAME + " = " + orderId1 + " THEN " + orderId2 +
                " WHEN " + DBCommonDef.ORDER_COLUMN_NAME + " = " + orderId2 + " THEN " + orderId1 +
                " END " +
                " WHERE " + DBCommonDef.ORDER_COLUMN_NAME + " IN (" + orderId1 + ", " + orderId2 + ")";
        if (selectionString != null)
            sql = sql + " AND " + selectionString;
        mDB.execSQL(sql);
    }

    public void exchangeOrderId(String tableName, long noteId, long orderId1, long orderId2) {
        String sql = "UPDATE " + tableName + " SET " + DBCommonDef.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBCommonDef.ORDER_COLUMN_NAME + " = " + orderId1 + " THEN " + orderId2 +
                " WHEN " + DBCommonDef.ORDER_COLUMN_NAME + " = " + orderId2 + " THEN " + orderId1 +
                " END " +
                " WHERE " + DBCommonDef.ORDER_COLUMN_NAME + " IN (" + orderId1 + ", " + orderId2 + ")";
        if (noteId > 0)
            sql = sql + " AND " + DBCommonDef.NOTE_ID_COLUMN_NAME + " = " + String.valueOf(noteId);
        mDB.execSQL(sql);
    }

    public void moveOrderIdTop(String tableName, String selectionString, long orderId, long minOrderId) {
        String sql = "UPDATE " + tableName + " SET " + DBCommonDef.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBCommonDef.ORDER_COLUMN_NAME + " = " + orderId + " THEN " + minOrderId +
                " WHEN " + DBCommonDef.ORDER_COLUMN_NAME + " < " + orderId + " THEN " + DBCommonDef.ORDER_COLUMN_NAME + " + 1" +
                " END " +
                " WHERE " + DBCommonDef.ORDER_COLUMN_NAME + " <= " + orderId;
        if (selectionString != null)
            sql = sql + " AND " + selectionString;
        mDB.execSQL(sql);
    }

    public void moveOrderIdBottom(String tableName, String selectionString, long orderId, long maxOrderId) {
        String sql = "UPDATE " + tableName + " SET " + DBCommonDef.ORDER_COLUMN_NAME + " = " +
                " CASE" +
                " WHEN " + DBCommonDef.ORDER_COLUMN_NAME + " = " + orderId + " THEN " + maxOrderId +
                " WHEN " + DBCommonDef.ORDER_COLUMN_NAME + " > " + orderId + " THEN " + DBCommonDef.ORDER_COLUMN_NAME + " - 1" +
                " END " +
                " WHERE " + DBCommonDef.ORDER_COLUMN_NAME + " >= " + orderId;
        if (selectionString != null)
            sql = sql + " AND " + selectionString;
        mDB.execSQL(sql);
    }

    public int updatePriority(String tableName, long id, long priority) {
        ContentValues cv = new ContentValues();

        cv.put(DBCommonDef.PRIORITY_COLUMN_NAME, priority);

        return mDB.update(tableName, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(id)});
    }

    public int updateOrderId(String tableName, long id, long orderId) {
        ContentValues cv = new ContentValues();

        cv.put(DBCommonDef.ORDER_COLUMN_NAME, orderId);

        return mDB.update(tableName, cv, DBCommonDef.ID_COLUMN_NAME + " = ?" , new String[] {String.valueOf(id)});
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
            c = mDB.query(tableName, new String[]{DBCommonDef.ORDER_COLUMN_NAME}, DBCommonDef.ID_COLUMN_NAME + " = ?", new String[] {String.valueOf(id)}, null, null, null);
            c.moveToFirst();
            return c.isNull(0) ? 0 : c.getLong(0);
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }
    }
}
