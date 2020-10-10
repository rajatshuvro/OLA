package com.ola.parsers;

import com.ola.dataStructures.Book;
import com.ola.dataStructures.Checkout;
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
    public final String BookIdTag = "Book id";

    public ReturnCsvParser(InputStream stream){
        _reader = new InputStreamReader(stream);
    }

    public void Close() throws IOException {
        _reader.close();
    }

    public ArrayList<String> GetReturnedBookIds() throws IOException {
        var returns = new ArrayList<String>();
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader(TimeTag, BookIdTag).parse(_reader);
        var isHeaderRecord = true;
        for (CSVRecord record : records) {
            //the first line is also reported as entry. We need to skip it
            if(isHeaderRecord) {
                isHeaderRecord = false;
                continue;
            }

            var bookId = record.get(BookIdTag).trim();
            bookId = Book.GetReducedId(bookId);
            returns.add(bookId);
        }
        return returns;
    }

}
