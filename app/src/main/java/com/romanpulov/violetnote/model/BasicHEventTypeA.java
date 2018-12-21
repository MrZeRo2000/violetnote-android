package com.romanpulov.violetnote.model;

import android.support.annotation.NonNull;

public final class BasicHEventTypeA extends BasicEntityNoteA {
    public static final String EVENT_TYPE_CODE_NOTE_ITEMS = "NOTE_ITEMS";
    public static final String EVENT_TYPE_NAME_NOTE_ITEMS = "Note items";
    public static final String EVENT_TYPE_CODE_CHECKOUT = "CHECKOUT";
    public static final String EVENT_TYPE_NAME_CHECKOUT = "Checkout";

    private final long mEventGroup;
    private final String mEventTypeCode;

    public String getEventTypeCode() {
        return mEventTypeCode;
    }

    private final String mEventTypeName;

    public String getEventTypeName() {
        return mEventTypeName;
    }


    private BasicHEventTypeA(long id, long eventGroup, String eventTypeCode, String eventTypeName) {
        setId(id);
        mEventGroup = eventGroup;
        mEventTypeCode = eventTypeCode;
        mEventTypeName = eventTypeName;
    }

    @NonNull
    public static BasicHEventTypeA newInstance(long id, long eventGroup, String eventTypeCode, String eventTypeName) {
        return new BasicHEventTypeA(id, eventGroup, eventTypeCode, eventTypeName);
    }
}
