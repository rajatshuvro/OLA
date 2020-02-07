package com.ola.databases;

import com.ola.dataStructures.Book;
import com.ola.luceneIndex.BookSearchIndex;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import java.io.IOException;
import java.util.*;

public class BookDb {
    private HashMap<String, Book> _books;
    private HashMap<Long, Integer> _latestCopyNumbers;
    private ArrayList<Book> _newBooks;
    private BookSearchIndex _searchIndex;

    public HashSet<String> GetIds() {
        var ids = new HashSet<String>();
        for (String bookId:_books.keySet()) {
            ids.add(bookId);
        }
        return ids;
    }

    public BookSearchIndex GetSearchIndex() throws IOException {
        if(_searchIndex == null) BuildSearchIndex();
        return _searchIndex;
    }

    public Iterable<Book> GetAllBooks(){
        return _books.values();
    };
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

    public Book GetBook(String id){
        if(_books.containsKey(id)) return _books.get(id);
        else return null;
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

    public String Add(Book book){
        book = StandardizeFields(book);
        if (book == null) return null;
        var id = book.GetId();
        if(_books.containsKey(id)) {
            System.out.println("WARNING!! new book Id exists in database.\nSkipping book:"+book.Title);
            return null;
        }
        _books.put(id, book);
        _newBooks.add(book);
        UpdateLatestCopyNum(book);
        return book.GetUserFriendlyId();
    }

    public String GetTitle(String bookId) {
        if(_books.containsKey(bookId)) return _books.get(bookId).Title;
        return null;
    }

    public List<Book> GetNewRecords(){
        if(_newBooks.size()==0) return null;
        var newBooks = new ArrayList<>(_newBooks);
        _newBooks.clear();
        return newBooks;
    }


    public Book StandardizeFields(Book book) {
        var copyNum = GetCopyCount(book.Isbn) + 1;
        Date date = new Date(System.currentTimeMillis());

        //getting canonical id (id for the first copy of this book)
        var canonicalId = Book.GenerateId(book.Isbn, 1);
        var canon = _books.get(canonicalId);
        if (canon == null) {
            //this is the first copy of this book
            return Book.Create(book.Isbn, book.Author, book.Title,
                    book.Publisher,book.Year, book.PageCount, book.Price,
                    book.Genre, book.ReadingLevel, copyNum, date, null, book.Summary );
        }
        boolean hasGeneratedIsbn = IsGeneratedIsbn(book.Isbn);

        if(!canon.Title.equals(book.Title)){
            if(hasGeneratedIsbn) {
                System.out.println("ERROR!! Title mismatch found for entry without ISBN. Skipping entry.");
                return null;
            }
            System.out.println("WARNING!! Title mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing Title:"+canon.Title);
            System.out.println("new Title:"+ book.Title);
        }
        if(!canon.Author.equals(book.Author)){
            if(hasGeneratedIsbn) {
                System.out.println("ERROR!! Author mismatch found for entry without ISBN. Skipping entry.");
                return null;
            }
            System.out.println("WARNING!! Author mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing Author:"+canon.Author);
            System.out.println("new Author:"+ book.Author);
        }
        if(canon.Year != book.Year){
            if(hasGeneratedIsbn) {
                System.out.println("ERROR!! Year mismatch found for entry without ISBN. Skipping entry.");
                return null;
            }
            System.out.println("WARNING!! Year mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing Year:"+canon.Year);
            System.out.println("new Year:"+ book.Year);

        }
        if(canon.PageCount != book.PageCount){
            if(hasGeneratedIsbn) {
                System.out.println("ERROR!! PageCount mismatch found for entry without ISBN. Skipping entry.");
                return null;
            }
            System.out.println("WARNING!! PageCount mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing PageCount:"+canon.PageCount);
            System.out.println("new PageCount:"+ book.PageCount);

        }

        if(!canon.Genre.equals(book.Genre)){
            if(hasGeneratedIsbn) {
                System.out.println("ERROR!! Genre mismatch found for entry without ISBN. Skipping entry.");
                return null;
            }
            System.out.println("WARNING!! Genre mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing Genre:"+canon.Genre);
            System.out.println("new Genre:"+ book.Genre);

        }
        if(canon.ReadingLevel != book.ReadingLevel){
            if(hasGeneratedIsbn) {
                System.out.println("ERROR!! ReadingLevel mismatch found for entry without ISBN. Skipping entry.");
                return null;
            }
            System.out.println("WARNING!! ReadingLevel mismatch found. New value will be overwritten with existing value.");
            System.out.println("Existing ReadingLevel:"+canon.ReadingLevel);
            System.out.println("new ReadingLevel:"+ book.ReadingLevel);

        }
        return Book.Create(book.Isbn, canon.Author, canon.Title,
                canon.Publisher,canon.Year, canon.PageCount, canon.Price,
                canon.Genre, canon.ReadingLevel, copyNum, date, null , null);
    }

    private boolean IsGeneratedIsbn(Long isbn) {
        var s = isbn.toString();
        return s.length() < 10;
    }

    public void BuildSearchIndex() throws IOException {
        var books = GetAllBooks();
        _searchIndex = new BookSearchIndex(books);
    }

    public ArrayList<Book> Filter(String genre, int level) {
        var isValidGenre = Book.IsValidGenre(genre);
        var isValidLevel = Book.IsValidReadingLevel(level);
        var books = _books.values();
        if(isValidGenre & isValidLevel) return FilterByGenreAndLevel(books, genre, level);
        if(isValidGenre) return FilterByGenre(books, genre);
        if(isValidLevel) return FilterByLevel(books, level);
        return null;
    }

    private ArrayList<Book> FilterByLevel(Collection<Book> allBooks, int level) {
        var books = new ArrayList<Book>();
        for (Book book:allBooks) {
            if(book.ReadingLevel== level)
                books.add(book);
        }
        return books;
    }

    private ArrayList<Book> FilterByGenre(Collection<Book> allBooks, String genre) {
        var books = new ArrayList<Book>();
        for (Book book:allBooks) {
            if(book.Genre.equals(genre) )
                books.add(book);
        }
        return books;
    }

    private ArrayList<Book> FilterByGenreAndLevel(Collection<Book> allBooks, String genre, int level) {
        var booksByGenre = FilterByGenre(allBooks, genre);
        return FilterByLevel(booksByGenre, level);
    }
}
