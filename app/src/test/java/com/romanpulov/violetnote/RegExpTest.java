package com.romanpulov.violetnote;

import com.romanpulov.violetnote.model.InputParser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpTest {

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
        Assert.assertNull(result1.getIntValue());
        Assert.assertEquals(test1, result1.getText());

        String test2 = "Something with space 52";
        InputParser.FloatParamsResult result2 = InputParser.parseFloatParams(test2);
        Assert.assertEquals((Integer)5200, result2.getIntValue());
        Assert.assertEquals("Something with space", result2.getText());

        String test3 = "Something with space 52,33";
        InputParser.FloatParamsResult result3 = InputParser.parseFloatParams(test3);
        Assert.assertEquals("Something with space", result3.getText());
        Assert.assertEquals((Integer)5233, result3.getIntValue());

        String test4 = "Something with space 52,33.777";
        InputParser.FloatParamsResult result4 = InputParser.parseFloatParams(test4);
        Assert.assertEquals(test4, result4.getText());
        Assert.assertNull(result4.getIntValue());

        String test5 = "Something with space 74";
        InputParser.FloatParamsResult result5 = InputParser.parseFloatParams(test5);
        Assert.assertEquals((Integer)7400, result5.getIntValue());

        String test6 = "Something with space .88";
        InputParser.FloatParamsResult result6 = InputParser.parseFloatParams(test6);
        Assert.assertEquals((Integer)88, result6.getIntValue());

        String test7 = "Something with space rounded 6344.735476";
        InputParser.FloatParamsResult result7 = InputParser.parseFloatParams(test7);
        Assert.assertEquals((Integer)634474, result7.getIntValue());

        String test8 = "String with different numbers: 12, 33.44 88.15";
        InputParser.FloatParamsResult result8 = InputParser.parseFloatParams(test8);
        Assert.assertEquals((Integer)8815, result8.getIntValue());
        Assert.assertEquals("String with different numbers: 12, 33.44", result8.getText());
        System.out.println("Result 8:" + result8);

        String test9 = "Simple dot should not go .";
        InputParser.FloatParamsResult result9 = InputParser.parseFloatParams(test9);
        Assert.assertNull(result9.getIntValue());
        Assert.assertEquals(test9, result9.getText());

    }

}
