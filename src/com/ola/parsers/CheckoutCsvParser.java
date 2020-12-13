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

public class CheckoutCsvParser {
    private InputStreamReader _reader;
    public final String TimeTag = "Timestamp";
    public final String UsernameTag = "Username";
    public final String BookIdTag = "Book id";
    public final String UserIdTag = "User id";
    public final String DueDateTag = "Due Date";

    public CheckoutCsvParser(InputStream stream){
        _reader = new InputStreamReader(stream);
    }

    public void Close() throws IOException {
        _reader.close();
    }

    public ArrayList<Checkout> GetCheckouts() throws IOException {
        var checkouts = new ArrayList<Checkout>();
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader(TimeTag, UsernameTag, BookIdTag, UserIdTag, DueDateTag).parse(_reader);
        var isHeaderRecord = true;
        for (CSVRecord record : records) {
            //the first line is also reported as entry. We need to skip it
            if(isHeaderRecord) {
                isHeaderRecord = false;
                continue;
            }

            var timeStamp = record.get(TimeTag).trim();
            var email = record.get(UsernameTag);
            var bookId = record.get(BookIdTag).trim();
            bookId = Book.GetReducedId(bookId);

            var userId = record.get(UserIdTag).trim();
            var dueDateString = record.get(DueDateTag).trim();
            var dueDate = TimeUtilities.parseDate(dueDateString);
            var checkoutDate = TimeUtilities.parseGoogleDateTime(timeStamp);

            checkouts.add(new Checkout(bookId, userId, email, checkoutDate, dueDate));
        }
        return checkouts;
    }

}
