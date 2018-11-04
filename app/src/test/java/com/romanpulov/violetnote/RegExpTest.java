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
        Assert.assertNull(result1.getFloatValue());
        Assert.assertEquals(test1, result1.getText());

        String test2 = "Something with space 52";
        InputParser.FloatParamsResult result2 = InputParser.parseFloatParams(test2);
        Assert.assertEquals((Double)52d, result2.getFloatValue());
        Assert.assertEquals("Something with space", result2.getText());

        String test3 = "Something with space 52,33";
        InputParser.FloatParamsResult result3 = InputParser.parseFloatParams(test3);
        Assert.assertEquals("Something with space", result3.getText());
        Assert.assertEquals((Double)52.33, result3.getFloatValue());

        String test4 = "Something with space 52,33.777";
        InputParser.FloatParamsResult result4 = InputParser.parseFloatParams(test4);
        Assert.assertEquals(test4, result4.getText());
        Assert.assertNull(result4.getFloatValue());

        String test5 = "Something with space 74";
        InputParser.FloatParamsResult result5 = InputParser.parseFloatParams(test5);
        Assert.assertEquals((Double)74d, result5.getFloatValue());

        String test6 = "Something with space .88";
        InputParser.FloatParamsResult result6 = InputParser.parseFloatParams(test6);
        Assert.assertEquals((Double).88d, result6.getFloatValue());

        String test7 = "Something with space rounded 6344.735476";
        InputParser.FloatParamsResult result7 = InputParser.parseFloatParams(test7);
        Assert.assertEquals((Double)6344.74d, result7.getFloatValue());


    }

}
