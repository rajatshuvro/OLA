package com.ola.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtilities {
    public static final String DateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final DateFormat DateFormat = new SimpleDateFormat(DateTimeFormat);

    public static Date GetCurrentTime(){
        return new Date(System.currentTimeMillis());
    }

    public static Date parseDate(String value) {
        if(value == null || value.equals("")) return null;
        try {
            return DateFormat.parse(value);
        } catch (ParseException e) {
            System.out.println("Invalid entry date provided:"+value);
            System.out.println("Please use the following format: "+ DateTimeFormat);
            return null;
        }
    }

    public static String ToString(Date date) {
        return DateFormat.format(date);
    }
}
