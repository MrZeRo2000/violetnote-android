package com.romanpulov.violetnote.model;

import androidx.annotation.NonNull;

/**
 * Note Item Param data
 */
public final class BasicNoteItemParamTypeA extends BasicEntityNoteA {
    private final String mParamTypeName;

    public String getParamTypeName() {
        return mParamTypeName;
    }

    private BasicNoteItemParamTypeA(long id, String paramTypeName) {
        this.setId(id);
        this.mParamTypeName = paramTypeName;
    }

    @NonNull
    public static BasicNoteItemParamTypeA newInstance(long id, String paramTypeName) {
        return new BasicNoteItemParamTypeA(id, paramTypeName);
    }
}
