package com.ola.utilities;

import com.ola.parsers.ParserUtilities;

public  class FormatUtilities {
    public static boolean IsValidPhoneNumber(String phnString){
        if(phnString== null)
            return false;
        var phn = phnString.replace("-", "");
        if(phn.length() != 10) return false;
        return true;
    }

    public static boolean IsValidEmail(String email) {
        if(ParserUtilities.IsNullOrEmpty(email)) return false;
        var atIndex = email.indexOf('@');
        if (atIndex < 0) return false;

        var domainIndex = email.indexOf('.',atIndex);
        return domainIndex > 0;
    }
}
