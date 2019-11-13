package com.ola.utilities;

import java.util.Date;

public class TimeUtilities {
    public static Date GetCurrentTime(){
        return new Date(System.currentTimeMillis());
    }
}
