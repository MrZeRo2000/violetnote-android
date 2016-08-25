package com.romanpulov.violetnote.helper;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by rpulov on 25.08.2016.
 */
public class DateTimeFormatterHelper {
    private final java.text.DateFormat mDateFormat;
    private final java.text.DateFormat mTimeFormat;

    public DateTimeFormatterHelper(Context context) {
        mDateFormat = DateFormat.getDateFormat(context);
        mTimeFormat = DateFormat.getTimeFormat(context);
    }

    public String formatDate(Date date) {
        return mDateFormat.format(date);
    }

    public String formatTime(Date date) {
        return mTimeFormat.format(date);
    }

    public String formatDateTimeDelimited(Date date, String delimiter) {
        return formatDate(date) + delimiter + formatTime(date);
    }
}
