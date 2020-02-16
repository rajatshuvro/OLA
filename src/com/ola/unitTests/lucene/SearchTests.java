package com.ola.unitTests.lucene;

import com.ola.dataStructures.Book;
import com.ola.dataStructures.User;
import com.ola.luceneIndex.DocumentSearchIndex;
import com.ola.luceneIndex.ISearchDocument;
import com.ola.luceneIndex.IdAndScore;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SearchTests {
    public ArrayList<ISearchDocument> GetNewBooks() {
        var books = new ArrayList<ISearchDocument>();
        books.add(Book.Create(7890788,"Rajat Shuvro Roy","Amar Baba","Bonosree books and co",
                2015, 5, 5, "General", 4,-1, null, null, "How great is baba"));
        books.add(Book.Create(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,-1, null, null, "Story of a broken heart"));
        books.add(Book.Create(678564,"Saber Nabil","Bhua Bhalobasha","Dakkhin Khan Publishers",
                2017, 10, 10, "Fiction", 5,-1, null, null, "Story of a broken man"));
        books.add(Book.Create(456098,"Nandana Mitra","Robindra Prem","Bakura Publishers",
                2018, 15, 12, "Fiction", 6,-1, null, null, "Bengali gir's fetish with Robi dadu"));

        return books;
    }
    @Test
    public void SearchReadingLevel() throws IOException, ParseException {
        var searcher = new DocumentSearchIndex(GetNewBooks());

        var idAndScores = searcher.Search("reading level 5", 4);
        var ids = IdAndScore.GetIds(idAndScores);
        assertArrayEquals(new String[]{"678564-(-1)", "678564-(-1)", "7890788-(-1)", "456098-(-1)"}, ids);
    }

    @Test
    public void SearchAuthor() throws IOException, ParseException {
        var searcher = new DocumentSearchIndex(GetNewBooks());

        var idAndScores = searcher.Search("Nandana", 4);
        var ids = IdAndScore.GetIds(idAndScores);
        assertArrayEquals(new String[]{"456098-(-1)"}, ids);
    }

    public ArrayList<ISearchDocument> GetUsers(){
        var users = new ArrayList<ISearchDocument>();
        users.add(User.Create(123, "Totini", User.StudentRoleTag, "totini.tonu@onkur.com", "732-666-7242"));
        users.add(User.Create(234, "Nabil", User.TeacherRoleTag, "saber.nabil@onkur.com", "858-345-1234"));
        users.add(User.Create(345, "Rajat", User.VolunteerRoleTag, "rajat.shuvro@onkur.com", "(732) 666-7242"));
        users.add(User.Create(456, "Zohir", User.AdminRoleTag, "zohir.choudhury@onkur.com","987-145-3456"));

        return users;
    }
    @Test
    public void SearchUser() throws IOException, ParseException {
        var searcher = new DocumentSearchIndex(GetUsers());
        var idAndScores = searcher.Search("Zohir", 4);
        var ids = IdAndScore.GetIds(idAndScores);
        assertArrayEquals(new String[]{"456"}, ids);
    }

}
