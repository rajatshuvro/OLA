package com.ola;

import com.ola.NativeSearch.InvertedIndex;
import com.ola.NativeSearch.SmithWaterman;
import com.ola.dataStructures.Transaction;
import com.ola.databases.BookDb;
import com.ola.databases.TransactionDb;
import com.ola.databases.UserDb;
import com.ola.luceneIndex.ISearchDocument;
import com.ola.parsers.BookParser;
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

    private InvertedIndex _searchIndex;
    private ArrayList<ISearchDocument> _docs;

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

        _searchIndex = new InvertedIndex(new SmithWaterman());
        _docs = new ArrayList<>();
    }

    public void Load() throws IOException{
        BookDb = new BookDb(_bookParser.GetBooks());
        UserDb = new UserDb(_userParser.GetUsers());
        TransactionDb = new TransactionDb(_transactionParser.GetTransactions(), UserDb, BookDb, Appender);

        BuildSearchIndex();

        _userInputStream.close();
        _bookInputStream.close();
        _transactionInputStream.close();
    }

    private void BuildSearchIndex() {
        for(var book: BookDb.GetAllBooks())
        {
            _searchIndex.Add(book.GetContent());
            _docs.add(book);
        }
        for(var user: UserDb.GetAllUsers())
        {
            _searchIndex.Add(user.GetContent());
            _docs.add(user);
        }
    }

    public void Close() throws IOException {
        Appender.Close();
        _userAppendStream.close();
        _bookAppendStream.close();
        _transactionAppendStream.close();
    }

    private static final int CheckoutDurationInDays = 14;
    public String GetTransactionString(Transaction record) {
        var sb = new StringBuilder();
        var userName = UserDb.GetUserName(record.UserId);
        var bookTitle = BookDb.GetTitle(record.BookId);
        sb.append("Name:      " + userName + " ("+record.UserId+")\n");
        sb.append("Title:     " + bookTitle +" ("+record.BookId+")\n");
        sb.append("Type:      " + record.Type + "\n");
        sb.append("Date:      " + TimeUtilities.ToString(record.Date) + "\n");
        //print due date if this is a checkout
        if(record.Type.equals(Transaction.CheckoutTag)){
            Calendar cal = Calendar.getInstance();
            cal.setTime(record.Date);
            cal.add(Calendar.DATE, CheckoutDurationInDays);
            var dueDate = cal.getTime();
            var now = TimeUtilities.GetCurrentTime();
            sb.append("Due:       " + TimeUtilities.ToString(dueDate) + "\n");
            if(dueDate.before(now))
                sb.append("Return OVERDUE!!\n");
        }
        return sb.toString();
    }

    public ArrayList<Transaction> GetPendingCheckouts(int userId) {
         return TransactionDb.GetPendingCheckouts(userId);
    }

    public ArrayList<String> Search(String query){
        var topDocs = _searchIndex.Search(query);
        return GetResults(topDocs, 5);
    }
    private ArrayList<String> GetResults( int[] topDocs, int maxCount) {
        var results = new ArrayList<String>();

        for(var i=0; i < maxCount && i < topDocs.length; i++)
            results.add(_docs.get(topDocs[i]).toString());

        return results;
    }
}
