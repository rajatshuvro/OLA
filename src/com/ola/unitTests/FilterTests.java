package com.ola.unitTests;
import com.ola.Filter;
import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterTests {
    public static ArrayList<Book> GetBooks() {
        var books = new ArrayList<Book>();
        books.add(new Book(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "General", 4,1, null, null));
        books.add(new Book(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "General", 4,2, null, null));
        books.add(new Book(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,1, null, null));
        books.add(new Book(456098,"Nandana Mitra","Robindra Prem","Bakura Publishers",
                2018, 15, 12, "Fiction", 6,1, null, null));

        return books;
    }

    @Test
    public void Filter_by_genre(){
        var bookDb = new BookDb(GetBooks());
        var args = new String[]{"find", "-g", "General", "-v"};

        Filter.Run(args, bookDb);
    }

    @Test
    public void Filter_by_level(){
        var bookDb = new BookDb(GetBooks());
        var args = new String[]{"find", "-l", "5", "-v"};

        Filter.Run(args, bookDb);
    }
    @Test
    public void Filter_by_genre_level(){
        var bookDb = new BookDb(GetBooks());
        var args = new String[]{"find", "-g", "Fiction","-l", "6", "-v"};

        Filter.Run(args, bookDb);
    }
}
