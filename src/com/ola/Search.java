package com.ola;

import com.ola.dataStructures.Book;
import com.ola.databases.BookDb;
import com.ola.luceneIndex.SearchIndex;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.print.DocFlavor;
import java.io.IOException;
import java.util.ArrayList;

public class Search {
    private static String commandSyntax = "find  \"query text\"";

    public static void Run(String[] args, BookDb bookDb) throws IOException, ParseException {
        String queryText = ConstructQuery(args);

        var searcher = bookDb.GetSearchIndex();

        for (var id: searcher.Search(queryText, 10)) {
            System.out.println(BookDb.RecordSeparator);
            System.out.println(bookDb.GetBook(id).toString());
        }
    }

    private static String ConstructQuery(String[] args) {
        var  queryText = args[1];
        for (var i=2; i < args.length; i++ ){
            queryText+= ' ' + args[i];
        }
        return queryText;
    }
}
