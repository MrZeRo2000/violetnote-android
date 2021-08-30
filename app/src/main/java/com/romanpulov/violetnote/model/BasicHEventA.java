package com.romanpulov.violetnote.model;

import androidx.annotation.NonNull;
import android.util.LongSparseArray;

import java.util.List;

/**
 * HEvent data class
 */
public final class BasicHEventA extends BasicEntityNoteA {
    private static final long UNKNOWN_ITEM_COUNT = -1;

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

    //calculated
    private final long mItemCount;

    public long getItemCount() {
        return mItemCount;
    }

    private BasicHEventA(long id, long eventTypeId, long eventTime, String eventSummary, long itemCount) {
        setId(id);
        mEventTypeId = eventTypeId;
        mEventTime = eventTime;
        mEventSummary = eventSummary;
        mItemCount = itemCount;
    }

    @NonNull
    public static BasicHEventA newInstance(long id, long eventTypeId, long eventTime, String eventSummary) {
        return new BasicHEventA(id, eventTypeId, eventTime, eventSummary, UNKNOWN_ITEM_COUNT);
    }

    @NonNull
    public static BasicHEventA newInstanceWithCount(long id, long eventTypeId, long eventTime, String eventSummary, long itemCount) {
        return new BasicHEventA(id, eventTypeId, eventTime, eventSummary, itemCount);
    }

    @NonNull
    public static BasicHEventA fromEventTypeWithSummary(long eventTypeId, String eventSummary) {
        return new BasicHEventA(0, eventTypeId, System.currentTimeMillis(), eventSummary, UNKNOWN_ITEM_COUNT);
    }

    @NonNull
    public static BasicHEventA fromEventType(long eventTypeId) {
        return new BasicHEventA(0, eventTypeId, System.currentTimeMillis(), null, UNKNOWN_ITEM_COUNT);
    }

    public static void fillArrayFromList(@NonNull LongSparseArray<BasicHEventA> hEventArray, @NonNull List<BasicHEventA> hEventList) {
        hEventArray.clear();
        for (BasicHEventA hEvent: hEventList) {
            hEventArray.append(hEvent.getId(), hEvent);
        }
    }
}
