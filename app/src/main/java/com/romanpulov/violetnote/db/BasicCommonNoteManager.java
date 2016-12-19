package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.romanpulov.violetnote.model.BasicCommonNoteA;

/**
 * Created by romanpulov on 29.08.2016.
 */
public class BasicCommonNoteManager {
    protected final Context mContext;
    protected final DBBasicNoteHelper mDBHelper;
    protected final SQLiteDatabase mDB;
    protected final DateTimeFormatter mDTF;

    private long mNoteId = 0;

    public void setNoteId(long value) {
        mNoteId = value;
    }

    public BasicCommonNoteManager(Context context) {
        mContext = context;
        mDBHelper = DBBasicNoteHelper.getInstance(mContext);
        mDB = mDBHelper.getDB();
        mDTF = new DateTimeFormatter(context);
    }

    public boolean moveUp(String tableName, BasicCommonNoteA note) {
        long prevOrderId = mDBHelper.getPrevOrderId(tableName, mNoteId, note.getOrderId());
        if (prevOrderId > 0) {
            mDBHelper.exchangeOrderId(tableName, mNoteId, note.getOrderId(), prevOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveDown(String tableName, BasicCommonNoteA note) {
        long nextOrderId = mDBHelper.getNextOrderId(tableName, mNoteId, note.getOrderId());
        if (nextOrderId > 0) {
            mDBHelper.exchangeOrderId(tableName, mNoteId, note.getOrderId(), nextOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveTop(String tableName, BasicCommonNoteA note) {
        long minOrderId = mDBHelper.getMinOrderId(tableName, mNoteId);
        if (note.getOrderId() > minOrderId) {
            mDBHelper.moveOrderIdTop(tableName, mNoteId, note.getOrderId());
            return true;
        } else
            return false;
    }

    public boolean moveBottom(String tableName, BasicCommonNoteA note) {
        long maxOrderId = mDBHelper.getMaxOrderId(tableName, mNoteId);
        if (note.getOrderId() < maxOrderId) {
            mDBHelper.moveOrderIdBottom(tableName, mNoteId, note.getOrderId());
            return true;
        } else
            return false;
    }
}
