package com.romanpulov.violetnote.model;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    public static final int NUMBER_DISPLAY_STYLE_INT = 1;
    public static final int NUMBER_DISPLAY_STYLE_FLOAT = 2;

    private static final SimpleDateFormat PARSE_DATE_FORMAT;

    static {
        PARSE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        PARSE_DATE_FORMAT.setLenient(true);
    }

    private static final String FLOAT_PARAMS_REGEXP = "(.+)\\s(([0-9]+[.|,]?[0-9]*)|([0-9]*[.|,]?[0-9]+))";
    private static final Pattern FLOAT_PARAMS_PATTERN = Pattern.compile(FLOAT_PARAMS_REGEXP);
    private static final String FLOAT_PARAMS_FORMAT = "%s %.2f";
    private static final String INT_PARAMS_FORMAT = "%s %d";
    private static final String FLOAT_VALUE_FORMAT = "%.2f";
    private static final String INT_VALUE_FORMAT = "%d";

    public static class FloatParamsResult {
        private String mText;

        public String getText() {
            return mText;
        }

        private Long mLongValue;

        public Long getLongValue() {
            return mLongValue;
        }

        public boolean hasValue() {
            return mLongValue != null;
        }

        private FloatParamsResult(String text) {
            mText = text;
        }

        private static FloatParamsResult fromText(String text) {
            return new FloatParamsResult(text);
        }

        public static FloatParamsResult fromTextAndValue(String text, long value) {
            FloatParamsResult instance = fromText(text);
            if (value != 0)
                instance.mLongValue = value;
            return instance;
        }

        @Override
        @NonNull
        public String toString() {
            return String.format(Locale.ENGLISH, "{Text=%s, FloatValue=%d}", mText, mLongValue );
        }
    }

    public static FloatParamsResult parseFloatParams(String inputString) {
        FloatParamsResult result = new FloatParamsResult(inputString);

        Matcher matcher = FLOAT_PARAMS_PATTERN.matcher(inputString);
        if (matcher.matches() && (matcher.groupCount() > 2)) {
            String mg1 = matcher.group(1);
            String mg2 = matcher.group(2);
            if (mg1 != null && mg2 != null) {
                result.mText = mg1;
                String floatString = mg2.replace(',', '.');

                long longValue = getLongValueFromString(floatString);
                if (longValue > 0) {
                    result.mLongValue = longValue;
                }
            }
        }

        return result;
    }

    public static int getNumberDisplayStyle(boolean isInt) {
        return isInt ? NUMBER_DISPLAY_STYLE_INT : NUMBER_DISPLAY_STYLE_FLOAT;
    }

    public static long getLongValueFromString(String floatString) {
        double floatValue = Double.parseDouble(floatString);
        return Math.round(floatValue * 100d);
    }

    public static String getDisplayValue(long value, int numberDisplayStyle) {
        switch (numberDisplayStyle) {
            case NUMBER_DISPLAY_STYLE_INT:
                return String.format(Locale.ENGLISH, INT_VALUE_FORMAT, value > 0 ? value / 100 : 1);
            case NUMBER_DISPLAY_STYLE_FLOAT:
                return String.format(Locale.ENGLISH, FLOAT_VALUE_FORMAT, (double)value / 100d) ;
            default:
                return String.valueOf(value);
        }
    }

    public static String composeFloatParams(String text, long value, int numberDisplayStyle) {
        FloatParamsResult floatParamsResult = FloatParamsResult.fromTextAndValue(text, (int) value);
        String result;
        if (floatParamsResult.hasValue()) {
            switch (numberDisplayStyle) {
                case NUMBER_DISPLAY_STYLE_INT:
                    result = String.format(Locale.ENGLISH, INT_PARAMS_FORMAT, floatParamsResult.mText, floatParamsResult.mLongValue / 100);
                    break;
                case NUMBER_DISPLAY_STYLE_FLOAT:
                    result = String.format(Locale.ENGLISH, FLOAT_PARAMS_FORMAT, floatParamsResult.mText, (double)floatParamsResult.mLongValue / 100d);
                    break;
                default:
                    result = floatParamsResult.mText;
            }
        } else {
            result = floatParamsResult.mText;
        }

        return result;
    }

    public static int getDisplayValueDifference(String value, String previousValue) {
        try {
            int intValue = Integer.parseInt(value);
            int intPreviousValue = Integer.parseInt(previousValue);
            return intValue - intPreviousValue;
        } catch (NumberFormatException ne) {
            try {
                Date dateValue = PARSE_DATE_FORMAT.parse(value);
                Date datePreviousValue = PARSE_DATE_FORMAT.parse(previousValue);
                if (dateValue != null && datePreviousValue != null) {
                    long diff = dateValue.getTime() - datePreviousValue.getTime();
                    return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                } else {
                    return 0;
                }
            } catch (ParseException pe) {
                return 0;
            }
        }
    }

    public static boolean isDate(String value) {
        PARSE_DATE_FORMAT.setLenient(true);
        try {
            return PARSE_DATE_FORMAT.parse(value) != null;
        } catch (ParseException e) {
            return false;
        }
    }

    @NonNull
    public static String getCurrentDateAsString() {
        return PARSE_DATE_FORMAT.format(new Date());
    }
}
