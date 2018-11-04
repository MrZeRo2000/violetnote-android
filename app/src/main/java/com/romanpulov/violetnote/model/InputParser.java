package com.romanpulov.violetnote.model;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    public static class FloatParamsResult {
        private String mText;

        public String getText() {
            return mText;
        }

        private Integer mIntValue;

        public Integer getIntValue() {
            return mIntValue;
        }

        public FloatParamsResult(String text) {
            mText = text;
        }

        @Override
        public String toString() {
            return String.format(Locale.ENGLISH, "{Text=%s, FloatValue=%d}", mText, mIntValue );
        }
    }

    private static final String FLOAT_PARAMS_REGEXP = "(.+)\\s(([0-9]+[.|,]?[0-9]*)|([0-9]*[.|,]?[0-9]+))";
    private static final Pattern FLOAT_PARAMS_PATTERN = Pattern.compile(FLOAT_PARAMS_REGEXP);

    public static FloatParamsResult parseFloatParams(String inputString) {
        FloatParamsResult result = new FloatParamsResult(inputString);

        Matcher matcher = FLOAT_PARAMS_PATTERN.matcher(inputString);
        if (matcher.matches() && (matcher.groupCount() > 2)) {
            result.mText = matcher.group(1);
            String floatString = matcher.group(2).replace(',', '.');
            double floatValue = Double.parseDouble(floatString);
            result.mIntValue = (int)Math.round(floatValue * 100d);
        }

        return result;
    }
}
