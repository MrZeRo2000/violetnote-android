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

    public boolean moveUp(BasicCommonNoteA note) {
        DBManagementProvider dbManagementProvider = note.getDBManagementProvider();

        long prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getPrevOrderSelection(), dbManagementProvider.getOrderSelectionArgs());

        if (prevOrderId > 0) {
            mDBHelper.exchangeOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelectionString(), note.getOrderId(), prevOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveDown(BasicCommonNoteA note) {
        DBManagementProvider dbManagementProvider = note.getDBManagementProvider();

        long nextOrderId = mDBHelper.getNextOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getNextOrderSelection(), dbManagementProvider.getOrderSelectionArgs());

        if (nextOrderId > 0) {
            mDBHelper.exchangeOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelectionString(), note.getOrderId(), nextOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveTop(BasicCommonNoteA note) {
        DBManagementProvider dbManagementProvider = note.getDBManagementProvider();

        long minOrderId = mDBHelper.getMinOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelection(), dbManagementProvider.getOrderIdSelectionArgs());
        long orderId = mDBHelper.getOrderId(dbManagementProvider.getTableName(), note.getId());

        if (orderId > minOrderId) {
            mDBHelper.moveOrderIdTop(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelectionString(), orderId, minOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveBottom(BasicCommonNoteA note) {
        DBManagementProvider dbManagementProvider = note.getDBManagementProvider();

        long maxOrderId = mDBHelper.getMaxOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelection(), dbManagementProvider.getOrderIdSelectionArgs());
        long orderId = mDBHelper.getOrderId(dbManagementProvider.getTableName(), note.getId());

        if (orderId < maxOrderId) {
            mDBHelper.moveOrderIdBottom(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelectionString(), orderId, maxOrderId);
            return true;
        } else
            return false;
    }
}
