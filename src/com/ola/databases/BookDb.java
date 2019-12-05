package com.ola.databases;

import com.ola.dataStructures.Book;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    public int GetCopyCount(long isbn){
        return _latestCopyNumbers.containsKey(isbn)? _latestCopyNumbers.get(isbn) : 0;
    }
    private void UpdateLatestCopyNum(Book book) {
        if(_latestCopyNumbers.containsKey(book.Isbn)){
            if(_latestCopyNumbers.get(book.Isbn) < book.CopyNum) _latestCopyNumbers.replace(book.Isbn, book.CopyNum);
        }
        else _latestCopyNumbers.put(book.Isbn, book.CopyNum);
    }

    public boolean AddNew(Book book){
        book = StandardizeFields(book);
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
            writer.write(book.toString()+'\n');
            writer.write(RecordSeparator+'\n');
        }
        writer.close();

    }


    public Book StandardizeFields(Book book) {
        var copyNum = GetCopyCount(book.Isbn) + 1;
        Date date = new Date(System.currentTimeMillis());

        //getting canonical id
        var canonicalId = Book.GenerateId(book.Isbn, 1);
        var canon = _books.get(canonicalId);
        if (canon == null) {
            //this is the first copy of this book
            return new Book(book.Isbn, book.Author, book.Title,
                    book.Publisher,book.Year, book.PageCount, book.Price,
                    book.Genre, book.ReadingLevel, copyNum, date, null );
        }

        if(!canon.Title.equals(book.Title)){
            System.out.println("WARNING!! Title mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing Title:"+canon.Title);
            System.out.println("new Title:"+ book.Title);
        }
        if(!canon.Author.equals(book.Author)){
            System.out.println("WARNING!! Author mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing Author:"+canon.Author);
            System.out.println("new Author:"+ book.Author);
        }
        if(canon.Year != book.Year){
            System.out.println("WARNING!! Year mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing Year:"+canon.Year);
            System.out.println("new Year:"+ book.Year);

        }
        if(canon.PageCount != book.PageCount){
            System.out.println("WARNING!! PageCount mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing PageCount:"+canon.PageCount);
            System.out.println("new PageCount:"+ book.PageCount);

        }

        if(!canon.Genre.equals(book.Genre)){
            System.out.println("WARNING!! Genre mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing Genre:"+canon.Genre);
            System.out.println("new Genre:"+ book.Genre);

        }
        if(canon.ReadingLevel != book.ReadingLevel){
            System.out.println("WARNING!! ReadingLevel mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing ReadingLevel:"+canon.ReadingLevel);
            System.out.println("new ReadingLevel:"+ book.ReadingLevel);

        }
        return new Book(book.Isbn, canon.Author, canon.Title,
                canon.Publisher,canon.Year, canon.PageCount, canon.Price,
                canon.Genre, canon.ReadingLevel, copyNum, date, null );
    }

    public ArrayList<Book> Search(String genre, int level, String author, String title) {
        //todo: build search indexes
        var isValidGenre = Book.IsValidGenre(genre);
        var isValidLevel = Book.IsValidReadingLevel(level);
        var books = _books.values();
        if(isValidGenre & isValidLevel) return SearchByGenreAndLevel(books, genre, level);
        if(isValidGenre) return SearchByGenre(books, genre);
        if(isValidLevel) return SearchByLevel(books, level);
        return null;
    }

    private ArrayList<Book> SearchByLevel(Collection<Book> allBooks, int level) {
        var books = new ArrayList<Book>();
        for (Book book:allBooks) {
            if(book.ReadingLevel== level)
                books.add(book);
        }
        return books;
    }

    private ArrayList<Book> SearchByGenre(Collection<Book> allBooks, String genre) {
        var books = new ArrayList<Book>();
        for (Book book:allBooks) {
            if(book.Genre.equals(genre) )
                books.add(book);
        }
        return books;
    }

    private ArrayList<Book> SearchByGenreAndLevel(Collection<Book> allBooks, String genre, int level) {
        var booksByGenre = SearchByGenre(allBooks, genre);
        return SearchByLevel(booksByGenre, level);
    }
}
