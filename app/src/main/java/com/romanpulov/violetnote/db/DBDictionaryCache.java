package com.romanpulov.violetnote.db;

import android.content.Context;

import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.model.BasicNoteItemParamTypeA;

import java.util.List;

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

    private long mPriceNoteParamTypeId;

    public long getPriceNoteParamTypeId() {
        checkLoaded();
        return mPriceNoteParamTypeId;
    }

    public void loadNoteParamTypes(DBNoteManager noteManager) {
        List<BasicNoteItemParamTypeA> noteParamTypes = noteManager.getNoteParamTypes();
        for (BasicNoteItemParamTypeA item : noteParamTypes) {
            if (item.getParamTypeName().equals(DBCommonDef.NOTE_ITEM_PARAM_TYPE_NAME_PRICE)) {
                mPriceNoteParamTypeId = item.getId();
            }
        }
    }

    /**
     * Loads cache from the context
     * @param context Context
     */
    public void load(Context context) {
        DBNoteManager noteManager = new DBNoteManager(context);

        loadNoteParamTypes(noteManager);

        mIsLoaded = true;
    }

    public void invalidate() {
        mIsLoaded = false;
    }
}
