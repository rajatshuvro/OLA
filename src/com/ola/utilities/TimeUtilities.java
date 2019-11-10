package com.ola.utilities;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtilities {
    public static String TimeToString(long time){
        if (time < 0) return "-";
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
}
