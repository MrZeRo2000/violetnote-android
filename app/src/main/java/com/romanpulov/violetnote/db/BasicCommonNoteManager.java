package com.romanpulov.violetnote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.romanpulov.violetnote.helper.DateTimeFormatterHelper;
import com.romanpulov.violetnote.model.BasicCommonNoteA;

/**
 * Created by romanpulov on 29.08.2016.
 */
public class BasicCommonNoteManager {
    protected final Context mContext;
    protected final DBBasicNoteHelper mDBHelper;
    protected final SQLiteDatabase mDB;
    protected final DateTimeFormatterHelper mDTF;

    public BasicCommonNoteManager(Context context) {
        mContext = context;
        mDBHelper = DBBasicNoteHelper.getInstance(mContext);
        mDB = mDBHelper.getDB();
        mDTF = new DateTimeFormatterHelper(context);
    }

    public boolean moveUp(BasicCommonNoteA note) {
        long prevOrderId = mDBHelper.getPrevOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note.getOrderId());
        if (prevOrderId > 0) {
            mDBHelper.exchangeOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note.getOrderId(), prevOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveDown(BasicCommonNoteA note) {
        long nextOrderId = mDBHelper.getNextOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note.getOrderId());
        if (nextOrderId > 0) {
            mDBHelper.exchangeOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note.getOrderId(), nextOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveTop(BasicCommonNoteA note) {
        long minOrderId = mDBHelper.getMinOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME);
        if (note.getOrderId() > minOrderId) {
            mDBHelper.moveOrderIdTop(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note.getOrderId());
            return true;
        } else
            return false;
    }

    public boolean moveBottom(BasicCommonNoteA note) {
        long maxOrderId = mDBHelper.getMaxOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME);
        if (note.getOrderId() < maxOrderId) {
            mDBHelper.moveOrderIdBottom(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note.getOrderId());
            return true;
        } else
            return false;
    }


}
