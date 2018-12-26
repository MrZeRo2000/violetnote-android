package com.romanpulov.violetnote.db.dao;

import android.content.Context;

/**
 * AbstractBasicNoteItem DAO
 **/
public abstract class AbstractBasicNoteItemDAO<T> extends AbstractDAO<T> {

    private BasicNoteItemParamsDAO mBasicNoteItemParamsDAO;
    BasicNoteItemParamsDAO getBasicNoteItemParamsDAO() {
        return mBasicNoteItemParamsDAO == null ? (mBasicNoteItemParamsDAO = new BasicNoteItemParamsDAO(mContext)) : mBasicNoteItemParamsDAO;
    }

    private BasicHNoteItemDAO basicHNoteItemDAO;
    BasicHNoteItemDAO getBasicHNoteItemDAO() {
        return basicHNoteItemDAO == null ? (basicHNoteItemDAO = new BasicHNoteItemDAO(mContext)) : basicHNoteItemDAO;
    }

    public AbstractBasicNoteItemDAO(Context context) {
        super(context);
    }
}
