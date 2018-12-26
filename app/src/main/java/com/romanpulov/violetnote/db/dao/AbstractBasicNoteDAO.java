package com.romanpulov.violetnote.db.dao;

import android.content.Context;

/**
 * 
 * AbstractBasicNote DAO
 * */
public abstract class AbstractBasicNoteDAO<T> extends AbstractDAO<T> {
    
    private BasicNoteValueDAO mBasicNoteValueDAO;
    BasicNoteValueDAO getBasicNoteValueDAO() {
        return mBasicNoteValueDAO == null ? (mBasicNoteValueDAO = new BasicNoteValueDAO(mContext)) : mBasicNoteValueDAO;
    }

    private BasicNoteHistoryDAO mBasicNoteHistoryDAO;
    BasicNoteHistoryDAO getBasicNoteHistoryDAO() {
        return mBasicNoteHistoryDAO == null ? (mBasicNoteHistoryDAO = new BasicNoteHistoryDAO(mContext)) : mBasicNoteHistoryDAO;
    }

    private BasicHNoteCOItemDAO mBasicHNoteCOItemDAO;
    BasicHNoteCOItemDAO getBasicHNoteCOItemDAO() {
        return mBasicHNoteCOItemDAO == null ? (mBasicHNoteCOItemDAO = new BasicHNoteCOItemDAO(mContext)) : mBasicHNoteCOItemDAO;
    }

    private BasicNoteItemDAO mBasicNoteItemDAO;
    BasicNoteItemDAO getBasicNoteItemDAO() {
        return mBasicNoteItemDAO == null ? (mBasicNoteItemDAO = new BasicNoteItemDAO(mContext)) : mBasicNoteItemDAO;
    }

    public AbstractBasicNoteDAO(Context context) {
        super(context);
    }
}
