package com.romanpulov.violetnote.model;

import android.support.annotation.NonNull;

/**
 * HEvent data class
 */
public final class BasicHEventA extends BasicEntityNoteA {
    private final long mEventTypeId;
    public long getEventTypeId() {
        return mEventTypeId;
    }

    private final long mEventTime;
    public long getEventTime() {
        return mEventTime;
    }

    private final String mEventSummary;
    public String getEventSummary() {
        return mEventSummary;
    }

    private BasicHEventA(long id, long eventTypeId, long eventTime, String eventSummary) {
        setId(id);
        mEventTypeId = eventTypeId;
        mEventTime = eventTime;
        mEventSummary = eventSummary;
    }

    @NonNull
    public static BasicHEventA newInstance(long id, long eventTypeId, long eventTime, String eventSummary) {
        return new BasicHEventA(id, eventTypeId, eventTime, eventSummary);
    }

    @NonNull
    public static BasicHEventA newInstance(long eventTypeId, String eventSummary) {
        return new BasicHEventA(0, eventTypeId, System.currentTimeMillis(), eventSummary);
    }

}
