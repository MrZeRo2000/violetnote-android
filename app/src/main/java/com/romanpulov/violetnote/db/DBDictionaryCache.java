package com.romanpulov.violetnote.db;

import android.content.Context;

import com.romanpulov.violetnote.db.dao.BasicHEventTypeDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemParamTypeDAO;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.model.BasicHEventTypeA;

import java.util.Map;

/**
 * Class for caching dictionary values
 */
public class DBDictionaryCache {
    private boolean mIsLoaded = false;

    public boolean isLoaded() {
        return mIsLoaded;
    }

    private void checkLoaded() {
        if (!mIsLoaded)
            throw new RuntimeException("Dictionary Cache is not loaded");
    }

    private Map<String, Long> mParamTypes;
    private Map<String, Long> mHEventTypes;

    public long getPriceNoteParamTypeId() {
        checkLoaded();
        return mParamTypes.get(DBCommonDef.NOTE_ITEM_PARAM_TYPE_NAME_PRICE);
    }

    public long getNoteItemsHEventParamId() {
        checkLoaded();
        return mHEventTypes.get(BasicHEventTypeA.EVENT_TYPE_CODE_NOTE_ITEMS);
    }

    public long getCheckoutHEventParamId() {
        checkLoaded();
        return mHEventTypes.get(BasicHEventTypeA.EVENT_TYPE_CODE_CHECKOUT);
    }

    /**
     * Loads cache from the context
     * @param context Context
     */
    public void load(Context context) {
        BasicNoteItemParamTypeDAO basicNoteItemParamTypeDAO = new BasicNoteItemParamTypeDAO(context);
        BasicHEventTypeDAO basicHEventTypeDAO = new BasicHEventTypeDAO(context);

        mParamTypes = basicNoteItemParamTypeDAO.getAllAsMap();
        mHEventTypes = basicHEventTypeDAO.getAllAsMap();

        mIsLoaded = true;
    }

    public void invalidate() {
        mIsLoaded = false;
        mParamTypes = null;
        mHEventTypes = null;
    }
}
