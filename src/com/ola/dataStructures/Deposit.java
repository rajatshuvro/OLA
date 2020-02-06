package com.ola.dataStructures;

import com.ola.utilities.TimeUtilities;

import java.util.Date;

public class Deposit {
    public final int UserId;
    public final float Amount;
    public final Date Date;
    public final String Type;

    public static final String DepositTag = "Deposit";
    public static final String WithdrawlTag = "Withdrawal";

    public Deposit(int userId, float amount, Date date, String type)  {
        UserId = userId;
        Amount = amount;
        Date = date;
        Type = type;
    }

    public static boolean IsValid(int userId, float amount, Date date, String type){
        return userId > 0
                && amount > 0.0
                && date!=null
                && (type.equals(DepositTag) || type.equals(WithdrawlTag));
    }

    public String toString(){
        return    "UserId:    "+ UserId +
                "\nAmount:    "+ Amount +
                "\nDate:      "+ TimeUtilities.ToString(Date)+
                "\nType:      "+Type;
    }

}


