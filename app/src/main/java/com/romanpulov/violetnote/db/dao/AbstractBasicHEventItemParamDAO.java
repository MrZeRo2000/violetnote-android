package com.romanpulov.violetnote.db.dao;

import android.content.Context;

public abstract class AbstractBasicHEventItemParamDAO<T> extends AbstractBasicHEventItemDAO<T> {

    private BasicHNoteCOItemParamDAO mBasicHNoteCOItemParamDAO;
    BasicHNoteCOItemParamDAO getBasicHNoteCOItemParamDAO() {
        return mBasicHNoteCOItemParamDAO == null ? (mBasicHNoteCOItemParamDAO = new BasicHNoteCOItemParamDAO(mContext)) : mBasicHNoteCOItemParamDAO;
    }

    public AbstractBasicHEventItemParamDAO(Context context) {
        super(context);
    }
}
