package com.romanpulov.violetnote;

import com.romanpulov.violetnote.model.utils.InputParserUtils;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParserUtilsTest {

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
        InputParserUtils.FloatParamsResult result1 = InputParserUtils.parseFloatParams(test1);
        Assert.assertNull(result1.getLongValue());
        Assert.assertEquals(test1, result1.getText());

        String test2 = "Something with space 52";
        InputParserUtils.FloatParamsResult result2 = InputParserUtils.parseFloatParams(test2);
        Assert.assertEquals((Long)5200L, result2.getLongValue());
        Assert.assertEquals("Something with space", result2.getText());

        String test3 = "Something with space 52,33";
        InputParserUtils.FloatParamsResult result3 = InputParserUtils.parseFloatParams(test3);
        Assert.assertEquals("Something with space", result3.getText());
        Assert.assertEquals((Long)5233L, result3.getLongValue());

        String test4 = "Something with space 52,33.777";
        InputParserUtils.FloatParamsResult result4 = InputParserUtils.parseFloatParams(test4);
        Assert.assertEquals(test4, result4.getText());
        Assert.assertNull(result4.getLongValue());

        String test5 = "Something with space 74";
        InputParserUtils.FloatParamsResult result5 = InputParserUtils.parseFloatParams(test5);
        Assert.assertEquals((Long)7400L, result5.getLongValue());

        String test6 = "Something with space .88";
        InputParserUtils.FloatParamsResult result6 = InputParserUtils.parseFloatParams(test6);
        Assert.assertEquals((Long)88L, result6.getLongValue());

        String test7 = "Something with space rounded 6344.735476";
        InputParserUtils.FloatParamsResult result7 = InputParserUtils.parseFloatParams(test7);
        Assert.assertEquals((Long)634474L, result7.getLongValue());

        String test8 = "String with different numbers: 12, 33.44 88.15";
        InputParserUtils.FloatParamsResult result8 = InputParserUtils.parseFloatParams(test8);
        Assert.assertEquals((Long)8815L, result8.getLongValue());
        Assert.assertEquals("String with different numbers: 12, 33.44", result8.getText());
        System.out.println("Result 8:" + result8);

        String test9 = "Simple dot should not go .";
        InputParserUtils.FloatParamsResult result9 = InputParserUtils.parseFloatParams(test9);
        Assert.assertNull(result9.getLongValue());
        Assert.assertEquals(test9, result9.getText());

    }

    @Test
    public void testInputComposer() {
        String test1 = InputParserUtils.composeFloatParams("Some text", 1255, InputParserUtils.NUMBER_DISPLAY_STYLE_FLOAT);
        Assert.assertEquals("Some text 12.55", test1);

        String test2 = InputParserUtils.composeFloatParams("Some text", 1500, InputParserUtils.NUMBER_DISPLAY_STYLE_FLOAT);
        Assert.assertEquals("Some text 15.00", test2);

        String test3 = InputParserUtils.composeFloatParams("Some text", 0, InputParserUtils.NUMBER_DISPLAY_STYLE_FLOAT);
        Assert.assertEquals("Some text", test3);

    }

    @Test
    public void testIsDate() {
        Assert.assertTrue(InputParserUtils.isDate("27.09.2024"));
        Assert.assertFalse(InputParserUtils.isDate("e2.2024"));
    }

    @Test
    public void testGetCurrentDateAsString() {
        String currentDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        Assert.assertEquals(currentDateAsString, InputParserUtils.getCurrentDateAsString());
    }
}
