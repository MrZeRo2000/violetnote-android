package com.romanpulov.violetnote.model;

import android.support.annotation.NonNull;

/**
 * HNoteCOItem data class
 */
public final class BasicHNoteCOItemA extends BasicEntityNoteA {
    private final long mEventId;
    public long getEventId() {
        return mEventId;
    }

    private final long mNoteId;
    public long getNoteId() {
        return mNoteId;
    }

    private final String mValue;
    public String getValue() {
        return mValue;
    }

    private BasicNoteItemParams mNoteItemParams = BasicNoteItemParams.createEmpty();
    public BasicNoteItemParams getNoteItemParams() {
        return mNoteItemParams;
    }

    private BasicHNoteCOItemA(long id, long eventId, long noteId, String value) {
        setId(id);
        mEventId = eventId;
        mNoteId = noteId;
        mValue = value;
    }

    @NonNull
    public static BasicHNoteCOItemA newInstance(long id, long eventId, long noteId, String value) {
        return new BasicHNoteCOItemA(id, eventId, noteId, value);
    }

    @NonNull
    public static BasicHNoteCOItemA fromEventData(long eventId, @NonNull BasicNoteA note, @NonNull BasicNoteItemA noteItem) {
        return newInstance(
                0,
                eventId,
                note.getId(),
                noteItem.getValue()
        );
    }
}
