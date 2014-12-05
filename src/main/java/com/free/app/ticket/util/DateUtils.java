/*
 * 文 件 名:  DateUtils.java
 * 修 改 人:  tangqian
 * 修改时间:  2013-10-23
 */
package com.free.app.ticket.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <日期时间处理工具类>
 */
public class DateUtils {
    
    /**
     * Date format pattern  this is often used.
     */
    public static final String PATTERN_YMD = "yyyy-MM-dd";
    
    /**
     * Date format pattern  this is often used.
     */
    public static final String PATTERN_YMDHMS="yyyy-MM-dd HH:mm:ss";
    
    /**
     * Formats the given date according to the YMD pattern.
     * 
     * @param date The date to format.
     * @return An YMD formatted date string.
     * 
     * @see #PATTERN_YMD
     */
    public static String formatDate(Date date) {
        return formatDate(date, PATTERN_YMD);
    }
    
    /**
     * Formats the given date according to the specified pattern.  The pattern
     * must conform to that used by the {@link SimpleDateFormat simple date
     * format} class.
     * 
     * @param date The date to format.
     * @param pattern The pattern to use for formatting the date.  
     * @return A formatted date string.
     * 
     * @throws IllegalArgumentException If the given date pattern is invalid.
     * 
     * @see SimpleDateFormat
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null)
            throw new IllegalArgumentException("date is null");
        if (pattern == null)
            throw new IllegalArgumentException("pattern is null");
        
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }
    
    /**
     * Parses a date value.  The format used for parsing the date value are retrieved from
     * the default PATTERN_YMD.
     *
     * @param dateValue the date value to parse
     * 
     * @return the parsed date
     * 
     * @throws IllegalArgumentException If the given dateValue is invalid.
     */
    public static Date parseDate(String dateValue) {
        return parseDate(dateValue, null);
    }
    
    /**
     * Parses the date value using the given date format.
     * 
     * @param dateValue the date value to parse
     * @param dateFormat the date format to use
     * 
     * @return the parsed date. if parse is failed , return null
     * 
     * @throws IllegalArgumentException If the given dateValue is invalid.
     */
    public static Date parseDate(String dateValue, String dateFormat) {
        if (dateValue == null) {
            throw new IllegalArgumentException("dateValue is null");
        }
        if (dateFormat == null) {
            dateFormat = PATTERN_YMD;
        }
        
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        Date result = null;
        try {
            result = df.parse(dateValue);
        }
        catch (ParseException pe) {
            pe.printStackTrace();// 日期型字符串格式错误
        }
        return result;
    }
    
    /**
     * Adds a number of years to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addYears(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }
    
    /**
     * Adds a number of years to a timestamp returning a new object.
     * The original timestamp object is unchanged.
     *
     * @param timestamp  the timestamp, not null
     * @param amount  the amount to add, may be negative
     * @return the new timestamp object with the amount added
     * @throws IllegalArgumentException if the timestamp is null
     */
    public static Timestamp addYears(Timestamp timestamp, int amount) {
        return add(timestamp, Calendar.YEAR, amount);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Adds a number of months to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMonths(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }
    
    /**
     * Adds a number of months to a timestamp returning a new object.
     * The original timestamp object is unchanged.
     *
     * @param timestamp  the timestamp, not null
     * @param amount  the amount to add, may be negative
     * @return the new timestamp object with the amount added
     * @throws IllegalArgumentException if the timestamp is null
     */
    public static Timestamp addMonths(Timestamp timestamp, int amount) {
        return add(timestamp, Calendar.MONTH, amount);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Adds a number of days to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DATE, amount);
    }
    
    /**
     * Adds a number of days to a timestamp returning a new object.
     * The original timestamp object is unchanged.
     *
     * @param timestamp  the timestamp, not null
     * @param amount  the amount to add, may be negative
     * @return the new timestamp object with the amount added
     * @throws IllegalArgumentException if the timestamp is null
     */
    public static Timestamp addDays(Timestamp timestamp, int amount) {
        return add(timestamp, Calendar.DATE, amount);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Adds a number of minutes to a timestamp returning a new object.
     * The original timestamp object is unchanged.
     *
     * @param timestamp  the timestamp, not null
     * @param amount  the amount to add, may be negative
     * @return the new timestamp object with the amount added
     * @throws IllegalArgumentException if the timestamp is null
     */
    public static Timestamp addMinutes(Timestamp timestamp, int amount) {
        return add(timestamp, Calendar.MINUTE, amount);
    }
    
    /**
     * Adds a number of days to current time returning a new object.
     *
     * @param amount  the amount to add, may be negative
     * @return the new timestamp object with the amount added
     */
    public static Timestamp addDays(int amount) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, amount);
        return new Timestamp(c.getTimeInMillis());
    }
    
    //-----------------------------------------------------------------------
    /**
     * Adds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param calendarField  the calendar field to add to
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    private static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }
    
    /**
     * Adds to a timestamp returning a new object.
     * The original timestamp object is unchanged.
     *
     * @param timestamp  the timestamp, not null
     * @param calendarField  the calendar field to add to
     * @param amount  the amount to add, may be negative
     * @return the new timestamp object with the amount added
     * @throws IllegalArgumentException if the timestamp is null
     */
    private static Timestamp add(Timestamp timestamp, int calendarField, int amount) {
        if (timestamp == null) {
            throw new IllegalArgumentException("The timestamp must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.add(calendarField, amount);
        return new Timestamp(c.getTimeInMillis());
    }
    
    /**
     * <生成最小的当天日期值>
     * @return 最小的当天日期值
     */
    public static Timestamp now() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }
    
    
    
    /** This class should not be instantiated. */
    private DateUtils() {
    }
}
