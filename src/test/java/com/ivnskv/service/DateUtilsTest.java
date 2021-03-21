package com.ivnskv.service;

import com.ivnskv.utils.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtilsTest {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Test
    public void addMillisecondTest() throws ParseException {
        Date testDate = dateFormat.parse("2021-02-28T23:59:59.999+03:00");
        Date expected = dateFormat.parse("2021-03-01T00:00:00.000+03:00");
        Assert.assertEquals(expected, DateUtils.addMillisecond(testDate));
    }

    @Test
    public void subtractMillisecondTest() throws ParseException {
        Date testDate = dateFormat.parse("2021-03-15T15:00:00.000+03:00");
        Date expected = dateFormat.parse("2021-03-15T14:59:59.999+03:00");
        Assert.assertEquals(expected, DateUtils.subtractMillisecond(testDate));
    }
}
