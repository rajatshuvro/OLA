package com.ola;

import com.ola.dataStructures.Book;
import com.ola.dataStructures.Transaction;
import com.ola.databases.BookDb;
import com.ola.databases.TransactionDb;
import com.ola.databases.UserDb;
import com.ola.parsers.BookParser;
import com.ola.parsers.FlatObjectParser;
import com.ola.parsers.TransactionParser;
import com.ola.parsers.UserParser;
import com.ola.utilities.TimeUtilities;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class DataProvider {
    public BookDb BookDb;
    public UserDb UserDb;
    public TransactionDb TransactionDb;

    private BookParser _bookParser;
    private UserParser _userParser;
    private TransactionParser _transactionParser;

    public final Appender Appender;

    private InputStream _bookInputStream;
    private InputStream _userInputStream;
    private InputStream _transactionInputStream;

    private OutputStream _bookAppendStream;
    private OutputStream _userAppendStream;
    private OutputStream _transactionAppendStream;

    public DataProvider(InputStream bookInputStream, InputStream userInputStream, InputStream transactionInputStream
                        , OutputStream transactionAppendStream, OutputStream bookAppendStream, OutputStream userAppendStream) {
        _bookInputStream = bookInputStream;
        _userInputStream = userInputStream;
        _transactionInputStream = transactionInputStream;

        _bookAppendStream = bookAppendStream;
        _userAppendStream = userAppendStream;
        _transactionAppendStream = transactionAppendStream;

        Appender = new Appender(bookAppendStream, userAppendStream, transactionAppendStream);

        _bookParser = new BookParser(bookInputStream);
        _userParser = new UserParser(userInputStream);
        _transactionParser = new TransactionParser(transactionInputStream);

    }

    public void Load() throws IOException{
        BookDb = new BookDb(_bookParser.GetBooks());
        UserDb = new UserDb(_userParser.GetUsers());
        TransactionDb = new TransactionDb(_transactionParser.GetTransactions(), UserDb, BookDb, Appender);

        BookDb.GetSearchIndex();
        UserDb.GetSearchIndex();

        _userInputStream.close();
        _bookInputStream.close();
        _transactionInputStream.close();
    }

    public void Close() throws IOException {
        Appender.Close();
        _userAppendStream.close();
        _bookAppendStream.close();
        _transactionAppendStream.close();
    }

    public String GetTransactionString(Transaction record) {
        var sb = new StringBuilder();
        var userName = UserDb.GetUserName(record.UserId);
        var bookTitle = BookDb.GetTitle(record.BookId);
        sb.append("Name:      " + userName + "]\n");
        sb.append("User id:   " + record.UserId + "]\n");
        sb.append("Title:     " + bookTitle + "]\n");
        sb.append("Book id:   " + record.BookId + "]\n");
        sb.append("Type:      " + record.Type + "]\n");
        sb.append("Date:      " + TimeUtilities.ToString(record.Date) + "\n");
        //print due date if this is a checkout
        if(record.Type.equals(Transaction.CheckoutTag)){
            Calendar cal = Calendar.getInstance();
            cal.setTime(record.Date);
            cal.add(Calendar.DATE, 14);
            var dueDate = cal.getTime();
            sb.append("Due:       " + TimeUtilities.ToString(dueDate) + "\n");
        }
        sb.append(FlatObjectParser.RecordSeparator + '\n');
        return sb.toString();
    }

    public ArrayList<Transaction> GetPendingCheckouts(int userId) {
        var checkouts = new ArrayList<Transaction>();
        for (Transaction record: TransactionDb.GetPendingCheckouts()) {
            if(userId != record.UserId) continue;
            checkouts.add(record);
        }
        return checkouts.size()==0? null: checkouts;
    }


}
