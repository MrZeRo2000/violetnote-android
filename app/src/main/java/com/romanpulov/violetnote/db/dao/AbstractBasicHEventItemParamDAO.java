package com.romanpulov.violetnote.db.dao;

import android.content.Context;

import com.romanpulov.violetnote.model.BasicHNoteCOItemParam;

public abstract class AbstractBasicHEventItemParamDAO<T> extends AbstractBasicHEventItemDAO<T> {

    private BasicHNoteCOItemParamDAO mBasicHNoteCOItemParamDAO;
    BasicHNoteCOItemParamDAO getBasicHNoteCOItemParamDAO() {
        if (mBasicHNoteCOItemParamDAO == null)
            mBasicHNoteCOItemParamDAO = new BasicHNoteCOItemParamDAO(mContext);

        return mBasicHNoteCOItemParamDAO;
    }

    public AbstractBasicHEventItemParamDAO(Context context) {
        super(context);
    }
}
