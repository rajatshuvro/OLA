package com.ola.NativeSearch;

import java.util.HashMap;

public class CommonEdits {
    public final HashMap<String, String> Substitutions = new HashMap<>(){
        {
            put("a", "aa");// alu-> aalu
            put("aa", "a");
            put("u","o");// alu -> alo
            put("o", "u");
            put("ph", "p");// phul -> pul
            put("p", "ph");//
            put("j", "jh");// jopath - jhopath
            put("jh", "j");
            put("t", "th"); //pat -> path
            put("th", "t");
            put("bh", "b");//Bhulomona-> bulomona
            put("b", "bh");
            put("kh", "k");//dekhbo -> dekbo
        }

    };
}
