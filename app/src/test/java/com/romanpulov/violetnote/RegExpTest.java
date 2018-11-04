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
        Assert.assertEquals(result1.getText(), test1);

        String test2 = "Something with space 52";
        InputParser.FloatParamsResult result2 = InputParser.parseFloatParams(test2);
        Assert.assertEquals(result2.getFloatValue(), (Double)52d);
        Assert.assertEquals(result2.getText(), "Something with space");

        String test3 = "Something with space 52,33";
        InputParser.FloatParamsResult result3 = InputParser.parseFloatParams(test3);
        Assert.assertEquals(result3.getText(), "Something with space");
        Assert.assertEquals(result3.getFloatValue(), (Double)52.33);


    }

}
