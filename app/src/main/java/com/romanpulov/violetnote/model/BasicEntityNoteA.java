package com.romanpulov.violetnote.model;

import java.util.Collection;

/**
 * Base class for BasicEntityNote
 * Created by rpulov on 07.09.2016.
 */
public abstract class BasicEntityNoteA {
    private long mId;

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public static int getNotePosWithId(Collection<? extends BasicEntityNoteA> noteList, long id) {
        int pos = 0;
        for (BasicEntityNoteA note : noteList) {
            if (note.getId() == id)
                return pos;
            pos++;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicEntityNoteA that = (BasicEntityNoteA) o;

        return mId == that.mId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(mId);
    }
}
