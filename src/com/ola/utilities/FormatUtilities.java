package com.ola.utilities;

import com.ola.parsers.ParserUtilities;

public  class FormatUtilities {
    public static boolean IsValidPhoneNumber(String phnString){
        if(phnString== null)
            return false;
        var numCount =0;
        for(var c : phnString.toCharArray()){
            if(Character.isDigit(c)) numCount++;
        }
        return numCount == 10;
    }

    public static boolean IsValidEmail(String email) {
        if(ParserUtilities.IsNullOrEmpty(email)) return false;
        var atIndex = email.indexOf('@');
        if (atIndex < 0) return false;

        var domainIndex = email.indexOf('.',atIndex);
        return domainIndex > 0;
    }
}
