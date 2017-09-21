package com.romanpulov.violetnote.db;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;

/**
 * Utility date/time formatter class
 * Created by rpulov on 25.08.2016.
 */
public class DateTimeFormatter {
    private final java.text.DateFormat mDateFormat;
    private final java.text.DateFormat mTimeFormat;

    public DateTimeFormatter(Context context) {
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
