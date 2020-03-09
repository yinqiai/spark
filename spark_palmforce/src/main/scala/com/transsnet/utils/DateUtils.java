package com.transsnet.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Objects;

/**
 * @author yinqi
 * @date 2019/9/30
 */
public class DateUtils {
    /**
     * 将字符串20180123转换为2018-01-23日期格式 针对一个的操作
     * @param
     * @return
     * @throws ParseException
     */
    public static String StrToDate(String str) throws ParseException
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy-MM-dd");
        String result=dateFormat2.format(dateFormat.parse(str));
//System.err.println(result);
        return result;
    }
    /**
     * 将2018-01-23的字符串转换成20180123的格式 针对一个日期的操作
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static String DateToStr(String dateStr) throws ParseException
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyyMMdd");
        Date parse = dateFormat.parse(dateStr);
        String format = dateFormat2.format(parse);
        return format;

    }

    /** 
     * 将短时间格式字符串转换为时间 yyyy-MM-dd 
     *  
     * @param strDate 
     * @return 
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * java8(经测试别的版本获取2月有bug) 获取某月第一天的00:00:00
     * @return
     */
    public static String getFirstDayOfMonth(Date date){

        if (Objects.isNull(date)) return  null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strdate=sdf.format(date);
        Date date1 = strToDate(strdate);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        Date dates = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return sdf.format(dates);
    }


    public static void main(String[] args) {
        System.out.println(strToDate("2019-09-11 12:12:12"));
    }


}
