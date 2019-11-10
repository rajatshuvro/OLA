package com.ola.dataStructures;

import com.ola.utilities.TimeUtilities;

public class Lending {
    public final long Id; // use the transaction time stamp
    public final String BookId;
    public final int UserId;
    private long ReturnTime;

    public Lending(long id, String bookId, int userId){
        Id = id;
        BookId = bookId;
        UserId = userId;
        ReturnTime = -1;
    }

    public boolean IsPending(){
        return ReturnTime == -1;
    }
    public void Return(long returnTime){
        if(ReturnTime != -1) {
            System.out.println("Error: This lending transaction has already been closed at:" + TimeUtilities.TimeToString(ReturnTime));
        }
        ReturnTime = returnTime;
    }

    public void OverrideReturn(long returnTime){
        ReturnTime = returnTime;
    }
    public String toString(){
        return "BookId: "+BookId + "UserId: "+UserId + "Issue time: "+ TimeUtilities.TimeToString(Id)
                + "Return time: "+ TimeUtilities.TimeToString(ReturnTime);
    }
}
