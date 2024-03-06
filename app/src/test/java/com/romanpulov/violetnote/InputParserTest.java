package com.romanpulov.violetnote;

import com.romanpulov.violetnote.model.InputParser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParserTest {

    @Test
    public void test1() {
        String regExp = "(.+)\\s(([0-9]+\\.?[0-9]*)|([0-9]*\\.?[0-9]+))";
        String testString = "this is something 74.66";
        String[] splitString = testString.split(regExp);
        System.out.println("Length:" + splitString.length);

        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(testString);
        if (matcher.find()) {
            System.out.println("match found");
            System.out.println("Group 1: " + matcher.group(1));
            System.out.println("Group 2: " + matcher.group(2));
        }
    }

    @Test
    public void testInputParser() {
        String test1 = "Something with space";
        InputParser.FloatParamsResult result1 = InputParser.parseFloatParams(test1);
        Assert.assertNull(result1.getLongValue());
        Assert.assertEquals(test1, result1.getText());

        String test2 = "Something with space 52";
        InputParser.FloatParamsResult result2 = InputParser.parseFloatParams(test2);
        Assert.assertEquals((Long)5200L, result2.getLongValue());
        Assert.assertEquals("Something with space", result2.getText());

        String test3 = "Something with space 52,33";
        InputParser.FloatParamsResult result3 = InputParser.parseFloatParams(test3);
        Assert.assertEquals("Something with space", result3.getText());
        Assert.assertEquals((Long)5233L, result3.getLongValue());

        String test4 = "Something with space 52,33.777";
        InputParser.FloatParamsResult result4 = InputParser.parseFloatParams(test4);
        Assert.assertEquals(test4, result4.getText());
        Assert.assertNull(result4.getLongValue());

        String test5 = "Something with space 74";
        InputParser.FloatParamsResult result5 = InputParser.parseFloatParams(test5);
        Assert.assertEquals((Long)7400L, result5.getLongValue());

        String test6 = "Something with space .88";
        InputParser.FloatParamsResult result6 = InputParser.parseFloatParams(test6);
        Assert.assertEquals((Long)88L, result6.getLongValue());

        String test7 = "Something with space rounded 6344.735476";
        InputParser.FloatParamsResult result7 = InputParser.parseFloatParams(test7);
        Assert.assertEquals((Long)634474L, result7.getLongValue());

        String test8 = "String with different numbers: 12, 33.44 88.15";
        InputParser.FloatParamsResult result8 = InputParser.parseFloatParams(test8);
        Assert.assertEquals((Long)8815L, result8.getLongValue());
        Assert.assertEquals("String with different numbers: 12, 33.44", result8.getText());
        System.out.println("Result 8:" + result8);

        String test9 = "Simple dot should not go .";
        InputParser.FloatParamsResult result9 = InputParser.parseFloatParams(test9);
        Assert.assertNull(result9.getLongValue());
        Assert.assertEquals(test9, result9.getText());

    }

    @Test
    public void testInputComposer() {
        String test1 = InputParser.composeFloatParams("Some text", 1255, InputParser.NUMBER_DISPLAY_STYLE_FLOAT);
        Assert.assertEquals("Some text 12.55", test1);

        String test2 = InputParser.composeFloatParams("Some text", 1500, InputParser.NUMBER_DISPLAY_STYLE_FLOAT);
        Assert.assertEquals("Some text 15.00", test2);

        String test3 = InputParser.composeFloatParams("Some text", 0, InputParser.NUMBER_DISPLAY_STYLE_FLOAT);
        Assert.assertEquals("Some text", test3);

    }
}
