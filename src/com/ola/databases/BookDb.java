package com.ola.databases;

import com.ola.dataStructures.Book;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

public class BookDb {
    private HashMap<String, Book> _books;
    private HashMap<Long, Integer> _latestCopyNumbers;
    private int _newBookIndex;

    public HashSet<String> GetIds() {
        var ids = new HashSet<String>();
        for (String bookId:_books.keySet()) {
            ids.add(bookId);
        }
        return ids;
    }

    public int Count(){
        return _books.size();
    }
    //Title    Author  ISBN    Page count      Price   Publisher       Genre   Reading level Copy number

    public BookDb(Iterable<Book> books) {
        _books = new HashMap<>();
        _latestCopyNumbers = new HashMap<>();
        for(Book book: books){
            var id = book.GetId();
            if (_books.containsKey(id)) throw new DataValidationException("Duplicate book id:"+ id);
            _books.put(book.GetId(), book);
            UpdateLatestCopyNum(book);
        }
        _newBookIndex = _books.size();
    }

    //return a list of all books having a isbn number
    public ArrayList<Book> GetBooks(long isbn){
        ArrayList<Book> books = new ArrayList<>();
        for (Book book : _books.values()) {
            if(book.Isbn== isbn) books.add(book);
        }
        return books;
    }

    // get the new copy number for a given isbn. e.g. if the latest copy in the db has copy# 4, this will return 4
    //if book doesn't exist return 0.
    public int GetLatestCopyNumber(long isbn){
        return _latestCopyNumbers.containsKey(isbn)? _latestCopyNumbers.get(isbn) : 0;
    }
    private void UpdateLatestCopyNum(Book book) {
        if(_latestCopyNumbers.containsKey(book.Isbn)){
            if(_latestCopyNumbers.get(book.Isbn) < book.CopyNum) _latestCopyNumbers.replace(book.Isbn, book.CopyNum);
        }
        else _latestCopyNumbers.put(book.Isbn, book.CopyNum);
    }

    public boolean Add(Book book){
        var id = book.GetId();
        if(_books.containsKey(id)) return false;
        _books.put(id, book);

        UpdateLatestCopyNum(book);
        return true;
    }

    public String GetTitle(String bookId) {
        if(_books.containsKey(bookId)) return _books.get(bookId).Title;
        return null;
    }
    private final String RecordSeparator = "***************************************************************";
    public void Append(OutputStream stream)throws IOException {
        if(_newBookIndex == _books.size()) return;
        var writer = new BufferedWriter(new OutputStreamWriter(stream));
        for (int i = _newBookIndex; i < _books.size(); i++) {
            var book = _books.get(i);
            writer.write("Title:\t\t\t"+book.Title+'\n');
            writer.write("Author:\t\t\t"+book.Author+'\n');
            writer.write("ISBN:\t\t\t"+ book.Isbn+'\n');
            writer.write("Publisher:\t\t"+book.Publisher+'\n');
            writer.write("Year:\t\t\t"+book.Year+'\n');
            writer.write("Genre:\t\t\t"+book.Genre+'\n');
            writer.write("Copy number:\t"+book.CopyNum+'\n');
            writer.write("Page count:\t\t"+ book.PageCount+'\n');
            writer.write("Price:\t\t\t"+book.Price+'\n');
            writer.write("Reading level:\t"+book.ReadingLevel+'\n');
            writer.write("Entry date:\t\t"+TimeUtilities.GetCurrentTime()+'\n');
            writer.write("Expiry date:\t-\n");
            writer.write(RecordSeparator+'\n');
            _newBookIndex++;
        }
        writer.close();

    }
}
