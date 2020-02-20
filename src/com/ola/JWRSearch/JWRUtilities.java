package com.ola.JWRSearch;

import java.util.ArrayList;

public class JWRUtilities {
    public static String TokenSplitterRegex = "[\\p{P} \\t\\n\\r]";

    public static ArrayList<String> GetTokens(String s){
        var tokens = new ArrayList<String>();
        for(var token: s.split(TokenSplitterRegex)){
            if(token.equals("")) continue;
            tokens.add(token.toLowerCase());
        }
        return tokens;
    }
}
