package com.ola.databases;

import com.ola.dataStructures.Checkout;
import com.ola.dataStructures.IdMap;
import com.ola.parsers.CheckoutParser;
import com.ola.parsers.IdMapParser;
import com.ola.utilities.PrintUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DbUtilities {
    public static ArrayList<Checkout> ReadCheckouts(InputStream inputStream)  {
        if(inputStream !=null){
            var parser = new CheckoutParser(inputStream);
            ArrayList<Checkout> checkouts;
            try {
                checkouts = parser.GetCheckouts();
                inputStream.close();
                return checkouts;
            } catch (IOException e) {
                PrintUtilities.PrintWarningLine("Error reading checkout file");
                return null;
            }

        }
        return null;
    }

    public static ArrayList<IdMap> ReadIdMaps(InputStream inputStream)  {
        if(inputStream !=null){
            var parser = new IdMapParser(inputStream);
            ArrayList<IdMap> idMaps;
            try {
                idMaps = parser.GetIdMaps();
                inputStream.close();
                return idMaps;
            } catch (IOException e) {
                PrintUtilities.PrintWarningLine("Error reading Id map file");
                return null;
            }

        }
        return null;
    }
}
