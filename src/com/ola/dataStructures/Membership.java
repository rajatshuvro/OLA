package com.ola.dataStructures;

import com.ola.utilities.TimeUtilities;

import java.util.Date;

public class Membership {
    public final int UserId;
    public final Date StartDate;
    public final Date EndDate;

    public Membership(int userId, Date startDate, Date endDate){
        UserId = userId;
        StartDate = startDate;
        EndDate = endDate;
    }

    public String toString(){
        return    "UserId:    "+ UserId +
                "\nStart:     "+ TimeUtilities.ToString(StartDate) +
                "\nEnd:       "+ TimeUtilities.ToString(EndDate);
    }
}
