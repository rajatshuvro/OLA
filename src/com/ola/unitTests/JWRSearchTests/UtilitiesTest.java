package com.ola.unitTests.JWRSearchTests;

import com.ola.JWRSearch.JWRUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilitiesTest {
    @Test
    public void GetTokens(){
        String input = "The\rquick!brown  - fox\t\tjumped?over;the,lazy\n,,..  \nsleeping___dog.";
        var tokens = JWRUtilities.GetTokens(input);

        Assertions.assertEquals(10, tokens.size());
    }
}
