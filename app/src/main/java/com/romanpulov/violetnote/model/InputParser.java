package com.romanpulov.violetnote.model;

import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    private static final String FLOAT_PARAMS_REGEXP = "(.+)\\s(([0-9]+[.|,]?[0-9]*)|([0-9]*[.|,]?[0-9]+))";
    private static final Pattern FLOAT_PARAMS_PATTERN = Pattern.compile(FLOAT_PARAMS_REGEXP);
    private static final String FLOAT_PARAMS_FORMAT = "%s %.2f";
    private static final String FLOAT_VALUE_FORMAT = "%.2f";

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
        public String toString() {
            return String.format(Locale.ENGLISH, "{Text=%s, FloatValue=%d}", mText, mLongValue );
        }

        public String compose() {
            String result = null;
            if (hasValue()) {
                result = String.format(Locale.ENGLISH, FLOAT_PARAMS_FORMAT, mText, (double)mLongValue / 100d);
            } else {
                result = mText;
            }

            return result;
        }
    }

    public static FloatParamsResult parseFloatParams(String inputString) {
        FloatParamsResult result = new FloatParamsResult(inputString);

        Matcher matcher = FLOAT_PARAMS_PATTERN.matcher(inputString);
        if (matcher.matches() && (matcher.groupCount() > 2)) {
            result.mText = matcher.group(1);
            String floatString = matcher.group(2).replace(',', '.');
            double floatValue = Double.parseDouble(floatString);
            long longValue = (long)Math.round(floatValue * 100d);
            if (longValue > 0) {
                result.mLongValue = longValue;
            }
        }

        return result;
    }

    public static String getFloatDisplayValue(long value) {
        return String.format(Locale.ENGLISH, FLOAT_VALUE_FORMAT, (double)value / 100d);
    }

    public static String composeFloatParams(String text, long value) {
        FloatParamsResult floatParamsResult = FloatParamsResult.fromTextAndValue(text, (int) value);
        String result = null;
        if (floatParamsResult.hasValue()) {
            result = String.format(Locale.ENGLISH, FLOAT_PARAMS_FORMAT, floatParamsResult.mText, (double)floatParamsResult.mLongValue / 100d);
        } else {
            result = floatParamsResult.mText;
        }

        return result;
    }
}
