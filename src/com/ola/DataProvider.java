package com.ola;

import com.ola.NativeSearch.InvertedIndex;
import com.ola.NativeSearch.SmithWaterman;
import com.ola.dataStructures.Book;
import com.ola.dataStructures.Checkout;
import com.ola.dataStructures.Transaction;
import com.ola.dataStructures.User;
import com.ola.databases.*;
import com.ola.luceneIndex.ISearchDocument;
import com.ola.parsers.*;
import com.ola.utilities.FileUtilities;
import com.ola.utilities.PathUtilities;
import com.ola.utilities.PrintUtilities;
import com.ola.utilities.TimeUtilities;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class DataProvider {
    public final String UsersFileName = "Users.fob";
    public final String BooksFileName = "Books.fob";
    public final String TransactionsFileName = "Transactions.fob";
    public final String CheckoutsFileName = "Checkouts.fob";


    private String _dataDir;
    public BookDb BookDb;
    public UserDb UserDb;
    public CheckoutDb CheckoutDb;
    public TransactionDb TransactionDb;

    private BookParser _bookParser;
    private UserParser _userParser;
    private TransactionParser _transactionParser;

    public Appender Appender;

    private InputStream _bookInputStream;
    private InputStream _userInputStream;
    private InputStream _transactionInputStream;

    private OutputStream _bookAppendStream;
    private OutputStream _userAppendStream;
    private OutputStream _transactionAppendStream;

    private InvertedIndex _searchIndex;
    private ArrayList<ISearchDocument> _docs;

    public DataProvider(BookDb bookDb, UserDb userDb, TransactionDb transactionDb, Appender appender){
        BookDb = bookDb;
        UserDb = userDb;
        TransactionDb = transactionDb;
        Appender = appender;
        _searchIndex = new InvertedIndex(new SmithWaterman());
        _docs = new ArrayList<>();

    }
    //to be used for builder pattern
    public DataProvider(String dataDir){
        _dataDir = dataDir;
    }

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


        try {
            Load();
        } catch (IOException e) {
            PrintUtilities.PrintErrorLine("Failed to load data provider");
        }
    }

    private void Load() throws IOException{
        BookDb = new BookDb(_bookParser.GetBooks());
        UserDb = new UserDb(_userParser.GetUsers());
        TransactionDb = new TransactionDb(_transactionParser.GetTransactions(), UserDb, BookDb, Appender);

        BuildSearchIndex();

        _userInputStream.close();
        _bookInputStream.close();
        _transactionInputStream.close();
    }

    public void BuildSearchIndex() {
        _searchIndex = new InvertedIndex(new SmithWaterman());
        _docs = new ArrayList<>();
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

    public int AddNewBooks(ArrayList<Book> books) throws IOException {
        var count=0;
        for (Book book: books) {
            var displayId = BookDb.Add(book);
            if(displayId!=null) {
                PrintUtilities.PrintSuccessLine("New book added: "+displayId);
                _searchIndex.Add(book.GetContent());
                _docs.add(book);
                count++;
            }
            else PrintUtilities.PrintErrorLine("Failed to add: "+ book.Title);
        }

        String fileName = PathUtilities.combine(_dataDir, BooksFileName);//DataDir+ File.separatorChar+BooksFileName;
        if(!FileUtilities.Exists(fileName)){
            PrintUtilities.PrintErrorLine("Failed to find file: "+fileName);
            return 0;
        }
        var appendStream = new FileOutputStream(fileName, true);
        BookDb.AppendBooks(BookDb.GetNewRecords(), appendStream);
        return count;
    }

    public String AddNewUser(String name, String role, String email, String phone) throws IOException {
        var id = UserDb.AddNewUser(name, role, email, phone);
        if(id != null) {
            var newUsers = UserDb.GetNewRecords();
            Appender.AppendUsers(newUsers);
            _searchIndex.Add(newUsers.get(0).GetContent());
            _docs.add(newUsers.get(0));
            return id;
        }
        else return null;
    }

    public int AddUsers(ArrayList<User> users) throws IOException {
        var count =0;
        for(var user: users){
            var id = UserDb.AddNewUser(user.Name, user.Role, user.Email, user.Phone);
            if(id != null) {
                PrintUtilities.PrintSuccessLine(user.Name +" was added to the user database. Assigned Id: "+id);
                _searchIndex.Add(user.GetContent());
                _docs.add(user);
                count++;
            }
            else PrintUtilities.PrintErrorLine("Failed to add new user "+user.Name);
        }

        Appender.AppendUsers(UserDb.GetNewRecords());
        return count;

    }

    public void Close() throws IOException {
        if (Appender != null) Appender.Close();
        if (_userAppendStream != null) _userAppendStream.close();
        if (_bookAppendStream != null) _bookAppendStream.close();
        if ( _transactionAppendStream!= null)_transactionAppendStream.close();

        CheckoutDb.Close();
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

    public ArrayList<Transaction> GetPendingCheckouts(String userId) {
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

    public int AddCheckouts(ArrayList<Checkout> checkouts) throws IOException {
        var count=0;
        for (var checkout:
                checkouts) {
            if(TransactionDb.Checkout(checkout))
            {
                count++;
                PrintUtilities.PrintSuccessLine(checkout.BookId +" has been checked out by "+ checkout.UserId);
            }
            else PrintUtilities.PrintWarningLine("Checkout attempt was unsuccessful!!");
        }
        return count;
    }

    private boolean AddBookDb() {
        String fileName = PathUtilities.combine(_dataDir, BooksFileName);//DataDir+ File.separatorChar+BooksFileName;
        if(!FileUtilities.Exists(fileName)){
            PrintUtilities.PrintErrorLine("Failed to find file: "+fileName);
            return false;
        }
        try {
            var inputStream = new FileInputStream(fileName);
            BookDb = new BookDb(DbUtilities.ReadBooks(inputStream));
        } catch (FileNotFoundException e) {
            PrintUtilities.PrintErrorLine("Failed to open file stream:"+ fileName);
        }
        return true;
    }

    private boolean AddUserDb(){
        String fileName = PathUtilities.combine(_dataDir, UsersFileName);
        if(!FileUtilities.Exists(fileName)){
            PrintUtilities.PrintErrorLine("Failed to find file: "+fileName);
            return false;
        }
        try {
            var inputStream = new FileInputStream(fileName);
            UserDb = new UserDb(DbUtilities.ReadUsers(inputStream));
        } catch (FileNotFoundException e) {
            PrintUtilities.PrintErrorLine("Failed to open file stream:"+ fileName);
        }
        return true;
    }

    private boolean AddTransactionDb() {
        String fileName = PathUtilities.combine(_dataDir, TransactionsFileName);
        if(!FileUtilities.Exists(fileName)){
            PrintUtilities.PrintErrorLine("Failed to find file: "+fileName);
            return false;
        }
        try {
            var inputStream = new FileInputStream(fileName);
            TransactionDb = new TransactionDb(DbUtilities.ReadTransactions(inputStream), UserDb, BookDb, null);
        } catch (FileNotFoundException e) {
            PrintUtilities.PrintErrorLine("Failed to open file stream:"+ fileName);
        }
        return true;
    }

    private boolean AddCheckoutDb() {
        String fileName = PathUtilities.combine(_dataDir, CheckoutsFileName);
        if(!FileUtilities.Exists(fileName)){
            PrintUtilities.PrintErrorLine("Failed to find file: "+fileName);
            return false;
        }
        try {
            var inputStream = new FileInputStream(fileName);
            CheckoutDb = new CheckoutDb(DbUtilities.ReadCheckouts(inputStream),null, UserDb, BookDb);
        } catch (FileNotFoundException e) {
            PrintUtilities.PrintErrorLine("Failed to open file stream:"+ fileName);
        }
        return true;
    }

    public boolean AddDbs(){
        if(!AddBookDb()) return false;
        if(!AddUserDb()) return false;
        if(!AddTransactionDb()) return false;
        if(!AddCheckoutDb()) return false;

        return true;
    }
}
