package com.ola.unitTests;

import com.ola.dataStructures.Book;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookTests {
    @Test
    public void GetBookId(){
        Book book = new Book(9_789_849_195_290L,"Binoy Bormon", "Panite Jhopat Jhopat", "Sisimpur",
                2016,16, 5, "FIC", 3, 1, null, null);

        assertEquals("9789849195290-FIC-3-(1)", book.GetUserFriendlyId());
        assertEquals("9789849195290-(1)", book.GetId());
    }
}
