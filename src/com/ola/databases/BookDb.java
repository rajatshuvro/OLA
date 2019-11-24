package com.ola.databases;

import com.ola.dataStructures.Book;
import com.ola.utilities.TimeUtilities;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.*;

public class BookDb {
    private HashMap<String, Book> _books;
    private HashMap<Long, Integer> _latestCopyNumbers;
    private ArrayList<Book> _newBooks;

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

    public BookDb(Iterable<Book> books) {
        _books = new HashMap<>();
        _newBooks = new ArrayList<>();
        _latestCopyNumbers = new HashMap<>();
        for(Book book: books){
            var id = book.GetId();
            if (_books.containsKey(id)) throw new DataValidationException("Duplicate book id:"+ id);
            _books.put(book.GetId(), book);
            UpdateLatestCopyNum(book);
        }

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

    public boolean AddNew(Book book){
        var id = book.GetId();
        if(_books.containsKey(id)) {
            System.out.println("WARNING!! new book Id exists in database.\nSkipping book:"+book.Title);
            return false;
        }
        _books.put(id, book);
        _newBooks.add(book);
        UpdateLatestCopyNum(book);
        return true;
    }

    public String GetTitle(String bookId) {
        if(_books.containsKey(bookId)) return _books.get(bookId).Title;
        return null;
    }
    private final String RecordSeparator = "***************************************************************";
    public void Append(OutputStream stream)throws IOException {
        if(_newBooks.size()==0) return;
        var writer = new BufferedWriter(new OutputStreamWriter(stream));
        for (Book book: _newBooks) {
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
            writer.write("Entry date:\t\t"+TimeUtilities.ToString(TimeUtilities.GetCurrentTime())+'\n');
            writer.write("Expiry date:\t\n");
            writer.write(RecordSeparator+'\n');
        }
        writer.close();

    }

    public boolean CrossCheck(Book newBook) {
        for (Book book: _books.values()) {
            if(book.Isbn != newBook.Isbn) continue;
            if(!book.Title.equals(newBook.Title)){
                System.out.println("WARNING!! Title mismatch found");
                System.out.println("Existing Title:"+book.Title);
                System.out.println("new Title:"+ newBook.Title);
                return false;
            }
            if(!book.Author.equals(newBook.Author)){
                System.out.println("WARNING!! Author mismatch found");
                System.out.println("Existing Author:"+book.Author);
                System.out.println("new Author:"+ newBook.Author);
                return false;
            }
            if(book.Year != newBook.Year){
                System.out.println("WARNING!! Year mismatch found");
                System.out.println("Existing Year:"+book.Year);
                System.out.println("new Year:"+ newBook.Year);
                return false;
            }
            if(book.PageCount != newBook.PageCount){
                System.out.println("WARNING!! PageCount mismatch found");
                System.out.println("Existing PageCount:"+book.PageCount);
                System.out.println("new PageCount:"+ newBook.PageCount);
                return false;
            }

            if(!book.Genre.equals(newBook.Genre)){
                System.out.println("WARNING!! Genre mismatch found");
                System.out.println("Existing Genre:"+book.Genre);
                System.out.println("new Genre:"+ newBook.Genre);
                return false;
            }
            if(book.ReadingLevel != newBook.ReadingLevel){
                System.out.println("WARNING!! ReadingLevel mismatch found");
                System.out.println("Existing ReadingLevel:"+book.ReadingLevel);
                System.out.println("new ReadingLevel:"+ newBook.ReadingLevel);
                return false;
            }

            return true;

        }
        return true;
    }
}
