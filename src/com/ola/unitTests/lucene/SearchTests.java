package com.ola.unitTests.lucene;

import com.ola.dataStructures.Book;
import com.ola.dataStructures.Transaction;
import com.ola.dataStructures.User;
import com.ola.luceneIndex.BookSearchIndex;
import com.ola.luceneIndex.UserSearchIndex;
import com.ola.utilities.TimeUtilities;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchTests {
    public ArrayList<Book> GetNewBooks() {
        var books = new ArrayList<Book>();
        books.add(new Book(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "General", 4,-1, null, null, "How great is baba"));
        books.add(new Book(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,-1, null, null, "Story of a broken heart"));
        books.add(new Book(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,-1, null, null, "Story of a broken man"));
        books.add(new Book(456098,"Nandana Mitra","Robindra Prem","Bakura Publishers",
                2018, 15, 12, "Fiction", 6,-1, null, null, "Bengali gir's fetish with Robi dadu"));

        return books;
    }
    @Test
    public void SearchReadingLevel() throws IOException, ParseException {
        var searcher = new BookSearchIndex(GetNewBooks());

        var topHitIds = searcher.Search("reading level 5", 4);
        assertArrayEquals(new String[]{"678564-(-1)", "678564-(-1)", "7890788-(-1)", "456098-(-1)"}, topHitIds);
    }

    @Test
    public void SearchAuthor() throws IOException, ParseException {
        var searcher = new BookSearchIndex(GetNewBooks());

        var topHitIds = searcher.Search("Nandana", 4);
        assertArrayEquals(new String[]{"456098-(-1)"}, topHitIds);
    }

    public ArrayList<User> GetUsers(){
        var users = new ArrayList<User>();
        users.add(new User(123, "Totini", User.StudentRoleTag, "totini.tonu@onkur.com", "732-666-7242"));
        users.add(new User(234, "Nabil", User.TeacherRoleTag, "saber.nabil@onkur.com", "858-345-1234"));
        users.add(new User(345, "Rajat", User.VolunteerRoleTag, "rajat.shuvro@onkur.com", "(732) 666-7242"));
        users.add(new User(456, "Zohir", User.AdminRoleTag, "zohir.choudhury@onkur.com","987-145-3456"));

        return users;
    }
    @Test
    public void SearchUser() throws IOException, ParseException {
        var searcher = new UserSearchIndex(GetUsers());
        var topHitIds = searcher.Search("Zohir", 4);
        assertArrayEquals(new int[]{456}, topHitIds);
    }

}
