package com.ola.parsers;

import com.ola.dataStructures.Book;
import com.ola.dataStructures.Checkout;
import com.ola.utilities.PrintUtilities;
import com.ola.utilities.TimeUtilities;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReturnCsvParser {
    private InputStreamReader _reader;
    public final String TimeTag = "Timestamp";
    public final String UsernameTag = "Username";
    public final String BookIdTag = "Book id";

    public ReturnCsvParser(InputStream stream){
        _reader = new InputStreamReader(stream);
    }

    public void Close() throws IOException {
        _reader.close();
    }

    public ArrayList<String> GetReturnedBookIds() {
        var returns = new ArrayList<String>();
        Iterable<CSVRecord> records = null;
        try {
            records = CSVFormat.RFC4180.withHeader(TimeTag, UsernameTag, BookIdTag).parse(_reader);
        } catch (IOException e) {
            PrintUtilities.PrintErrorLine("Failed to parse return CSV stream");
        }
        var isHeaderRecord = true;
        for (CSVRecord record : records) {
            //the first line is also reported as entry. We need to skip it
            if(isHeaderRecord) {
                isHeaderRecord = false;
                continue;
            }

            var dateTime = TimeUtilities.parseGoogleDateTime(record.get(TimeTag));
            var userName = record.get(UsernameTag);
            var bookId = record.get(BookIdTag).trim();
            bookId = Book.GetReducedId(bookId);
            returns.add(bookId);
        }
        return returns;
    }

}
