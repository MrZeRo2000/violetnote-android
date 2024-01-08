package com.romanpulov.violetnote.model;

import androidx.annotation.NonNull;

/**
 * Note Item Param data
 */
public final class BasicNoteItemParamTypeA extends BasicEntityNoteA {

    private BasicNoteItemParamTypeA(long id) {
        this.setId(id);
    }

    @NonNull
    public static BasicNoteItemParamTypeA newInstance(long id) {
        return new BasicNoteItemParamTypeA(id);
    }
}
