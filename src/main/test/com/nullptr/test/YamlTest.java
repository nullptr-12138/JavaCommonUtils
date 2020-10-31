package com.nullptr.test;

import com.nullptr.utils.system.DateUtils;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author snowy
 * @date 2020/10/31 16:05
 */
public class YamlTest {


    @Test
    public void oneTest(){
        String nowTimeStr = DateUtils.getNowTimeStr();
        System.out.println(nowTimeStr);
    }

    @Test
    public void twoTest(){
        String nowTimeStr = DateUtils.getNowDateTimeStr();
        System.out.println(nowTimeStr);
    }

    @Test
    public void threeTest(){
        String nowTimeStr = DateUtils.formatDate(new Date(),"yyyy/MM/dd");
        String nowTimeStr2 = DateUtils.formatDate(new Date(),"yyyy*MM*dd HH:mm:ss");
        String nowTimeStr3 = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
        System.out.println(nowTimeStr);
        System.out.println(nowTimeStr2);
        System.out.println(nowTimeStr3);
    }

    @Test
    public void fourTest(){
        Date date = DateUtils.getInitialDateTheDay(new Date());
        long a = DateUtils.toMilliseconds(new Date());
        long b = DateUtils.toMilliseconds(date);
        long c = DateUtils.toMilliseconds(date);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }

    @Test
    public void fiveTest(){
        Date l = DateUtils.fromMillisecond(160410399L);
        System.out.println(l);
    }

    @Test
    public void sixTest() throws ParseException {
        Date date1 = DateUtils.parseDate("2020-10-30", "yyyy-MM-dd");
        long a = DateUtils.subTime(new Date(),date1);
        long b = DateUtils.subTime(new Date(), date1, DateUtils.Base.MINUTE);
        long c = DateUtils.subTime(new Date(), date1, DateUtils.Base.DAY);
        long d = DateUtils.subTime(new Date(), date1, DateUtils.Base.HOUR);
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
    }
}
