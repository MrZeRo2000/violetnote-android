package com.romanpulov.violetnote.model;

import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    private static final String FLOAT_PARAMS_REGEXP = "(.+)\\s(([0-9]+[.|,]?[0-9]*)|([0-9]*[.|,]?[0-9]+))";
    private static final Pattern FLOAT_PARAMS_PATTERN = Pattern.compile(FLOAT_PARAMS_REGEXP);
    private static final String FLOAT_PARAMS_FORMAT = "%s %.2f";

    public static class FloatParamsResult {
        private String mText;

        public String getText() {
            return mText;
        }

        private Integer mIntValue;

        public Integer getIntValue() {
            return mIntValue;
        }

        public boolean hasValue() {
            return mIntValue != null;
        }

        private FloatParamsResult(String text) {
            mText = text;
        }

        private static FloatParamsResult fromText(String text) {
            return new FloatParamsResult(text);
        }

        public static FloatParamsResult fromTextAndValue(String text, int value) {
            FloatParamsResult instance = fromText(text);
            if (value != 0)
                instance.mIntValue = value;
            return instance;
        }

        @Override
        public String toString() {
            return String.format(Locale.ENGLISH, "{Text=%s, FloatValue=%d}", mText, mIntValue );
        }

        public String compose() {
            String result = null;
            if (hasValue()) {
                result = String.format(Locale.ENGLISH, FLOAT_PARAMS_FORMAT, mText, (double)mIntValue / 100d);
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
            int intValue = (int)Math.round(floatValue * 100d);
            if (intValue > 0) {
                result.mIntValue = intValue;
            }
        }

        return result;
    }

    public static String composeFloatParams(String text, long value) {
        FloatParamsResult floatParamsResult = FloatParamsResult.fromTextAndValue(text, (int) value);
        String result = null;
        if (floatParamsResult.hasValue()) {
            result = String.format(Locale.ENGLISH, FLOAT_PARAMS_FORMAT, floatParamsResult.mText, (double)floatParamsResult.mIntValue / 100d);
        } else {
            result = floatParamsResult.mText;
        }

        return result;
    }
}
