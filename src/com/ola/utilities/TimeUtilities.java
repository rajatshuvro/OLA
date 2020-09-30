package com.ola.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtilities {
    public static final String GoogleFormatString = "yyyy/MM/dd HH:mm:ss";
    public static final DateFormat GoogleDateFormat = new SimpleDateFormat(GoogleFormatString);
    public static final String DateFormatString = "yyyy-MM-dd";
    public static final DateFormat DateFormat = new SimpleDateFormat(DateFormatString);

    public static final String DateTimeFormatString = "yyyy-MM-dd HH:mm:ss";
    public static final DateFormat DateTimeFormat = new SimpleDateFormat(DateTimeFormatString);

    public static Date GetCurrentTime(){
        return new Date(System.currentTimeMillis());
    }

    public static Date parseGoogleDateTime(String value) {
        if(value == null || value.equals("")) return null;
        try {
            return GoogleDateFormat.parse(value);
        } catch (ParseException e) {
            System.out.println("Invalid entry date-time provided:"+value);
            System.out.println("Please use the following format: "+ GoogleFormatString);
            return null;
        }
    }
    public static Date parseDateTime(String value) {
        if(value == null || value.equals("")) return null;
        try {
            return DateTimeFormat.parse(value);
        } catch (ParseException e) {
            System.out.println("Invalid entry date-time provided:"+value);
            System.out.println("Please use the following format: "+ DateTimeFormatString);
            return null;
        }
    }

    public static Date parseDate(String value) {
        if(value == null || value.equals("")) return null;
        try {
            return DateFormat.parse(value);
        } catch (ParseException e) {
            System.out.println("Invalid entry date provided:"+value);
            System.out.println("Please use the following format: "+ DateFormatString);
            return null;
        }
    }

    public static String ToString(Date date) {
        return date==null? "": DateTimeFormat.format(date);
    }
}
