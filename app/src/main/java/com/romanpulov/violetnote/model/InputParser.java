package com.romanpulov.violetnote.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    public static class FloatParamsResult {
        private String mText;

        public String getText() {
            return mText;
        }

        private Double mFloatValue;

        public Double getFloatValue() {
            return mFloatValue;
        }

        public FloatParamsResult(String text) {
            mText = text;
        }
    }

    private static final String FLOAT_PARAMS_REGEXP = "(.+)\\s(([0-9]+[\\.|\\,]?[0-9]*)|([0-9]*[\\.|\\,]?[0-9]+))";
    private static final Pattern FLOAT_PARAMS_PATTERN = Pattern.compile(FLOAT_PARAMS_REGEXP);

    public static FloatParamsResult parseFloatParams(String inputString) {
        FloatParamsResult result = new FloatParamsResult(inputString);

        Matcher matcher = FLOAT_PARAMS_PATTERN.matcher(inputString);
        if (matcher.find() && (matcher.groupCount() > 2)) {
            result.mText = matcher.group(1);
            result.mFloatValue = Double.valueOf(matcher.group(2).replace(',', '.'));
        }

        return result;
    }
}
