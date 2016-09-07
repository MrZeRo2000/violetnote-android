package com.romanpulov.violetnote.model;

import java.util.List;

/**
 * Created by rpulov on 07.09.2016.
 */
public class BasicEntityNoteA {
    protected long mId;

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public static int getNotePosWithId(List<? extends BasicEntityNoteA> noteList, long id) {
        int pos = 0;
        for (BasicEntityNoteA note : noteList) {
            if (note.getId() == id)
                return pos;
            pos++;
        }
        return -1;
    }
}
