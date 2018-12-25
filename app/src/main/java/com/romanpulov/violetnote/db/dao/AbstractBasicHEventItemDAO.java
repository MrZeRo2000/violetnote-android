package com.romanpulov.violetnote.db.dao;

import android.content.Context;

public abstract class AbstractBasicHEventItemDAO<T> extends AbstractDAO<T> {

    private BasicHEventDAO mBasicHEventDAO;
    BasicHEventDAO getBasicHEventDAO() {
        if (mBasicHEventDAO == null)
            mBasicHEventDAO = new BasicHEventDAO(mContext);

        return mBasicHEventDAO;
    }

    public AbstractBasicHEventItemDAO(Context context) {
        super(context);
    }
}
