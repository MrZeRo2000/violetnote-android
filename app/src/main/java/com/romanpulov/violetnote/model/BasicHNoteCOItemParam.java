package com.romanpulov.violetnote.model;

import androidx.annotation.NonNull;

import com.romanpulov.violetnote.model.core.BasicEntityNoteA;
import com.romanpulov.violetnote.model.vo.BasicNoteItemParamValueA;
import com.romanpulov.violetnote.model.vo.BasicParamValueA;

public final class BasicHNoteCOItemParam extends BasicEntityNoteA {
    private final long mHNoteCOItemId;
    public long getHNoteCOItemId() {
        return mHNoteCOItemId;
    }

    private final BasicNoteItemParamValueA mNoteItemParamValue;
    public BasicNoteItemParamValueA getNoteItemParamValue() {
        return mNoteItemParamValue;
    }

    private BasicHNoteCOItemParam(long id, long hNoteCOItemId, long noteItemParamTypeId, long vInt, String vText) {
        setId(id);
        mHNoteCOItemId = hNoteCOItemId;
        mNoteItemParamValue = BasicNoteItemParamValueA.newInstance(noteItemParamTypeId,
                BasicParamValueA.newInstance(vInt, vText));
    }

    @NonNull
    public static BasicHNoteCOItemParam newInstance(long id, long hNoteCOItemId, long noteItemParamTypeId, long vInt, String vText) {
        return new BasicHNoteCOItemParam(id, hNoteCOItemId, noteItemParamTypeId, vInt, vText);
    }
}
