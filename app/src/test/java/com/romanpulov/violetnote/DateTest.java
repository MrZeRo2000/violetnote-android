package com.romanpulov.violetnote;

import org.junit.Test;

import java.text.DateFormat;
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
}
