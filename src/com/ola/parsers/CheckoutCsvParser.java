package com.ola.parsers;

import com.ola.CheckOut;
import com.ola.dataStructures.Checkout;
import com.ola.dataStructures.User;
import com.ola.databases.UserDb;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CheckoutCsvParser {
    private InputStreamReader _reader;
    public final String TimeTag = "Timestamp";
    public final String BookIdTag = "Book id";
    public final String UserIdTag = "User id";

    public CheckoutCsvParser(InputStream stream){
        _reader = new InputStreamReader(stream);
    }

    public void Close() throws IOException {
        _reader.close();
    }

    public ArrayList<Checkout> GetCheckouts() throws IOException {
        var checkouts = new ArrayList<Checkout>();
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader(TimeTag, BookIdTag, UserIdTag).parse(_reader);
        var isHeaderRecord = true;
        for (CSVRecord record : records) {
            //the first line is also reported as entry. We need to skip it
            if(isHeaderRecord) {
                isHeaderRecord = false;
                continue;
            }
            var bookId = record.get(BookIdTag).trim();
            var userId = record.get(UserIdTag).trim();

            checkouts.add(new Checkout(bookId, ParserUtilities.ParseUInt(userId)));
        }
        return checkouts;
    }

}