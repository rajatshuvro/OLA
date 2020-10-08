package com.ola.databases;

import com.ola.dataStructures.Checkout;
import com.ola.parsers.CheckoutCsvParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DbUtilities {
    public static ArrayList<Checkout> ReadCheckouts(InputStream inputStream)  {
        if(inputStream !=null){
            var csvParser = new CheckoutCsvParser(inputStream);
            ArrayList<Checkout> checkouts = null;
            try {
                checkouts = csvParser.GetCheckouts();
                inputStream.close();
                return checkouts;
            } catch (IOException e) {
                System.out.println("Error reading checkout file");
                return null;
            }

        }
        return null;
    }
}
