package com.romanpulov.violetnote;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by romanpulov on 09.06.2016.
 */
public class DateTest {

    @Test
    public void simpleTest() {
        System.out.println("simpleTest");
        Calendar c = Calendar.getInstance();
        Date currentDate = new Date(System.currentTimeMillis());

        DateFormat df = DateFormat.getDateTimeInstance();
        System.out.println("From calendar : " + df.format(c.getTime()));
        System.out.println("From currentTimeMillis : " + df.format(currentDate));
        System.out.println("From zero : " + df.format(new Date(0)));
        System.out.println("currentTimeMillis : " + System.currentTimeMillis());

        System.out.println("Full format : " + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));
    }

    @Test
    public void testParseDate() {
        DateTimeFormatter stdFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Assert.assertThrows(DateTimeParseException.class, () -> LocalDate.parse("82.05.2099", stdFormatter));
        Assert.assertThrows(DateTimeParseException.class, () -> LocalDate.parse("22.42.2099", stdFormatter));

        Assert.assertEquals(LocalDate.of(2024, 9, 27), LocalDate.parse("27.09.2024", stdFormatter));
        Assert.assertEquals(LocalDate.of(2024, 9, 27).format(stdFormatter), "27.09.2024");
    }
}
