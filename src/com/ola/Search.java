package com.ola;

import com.ola.databases.BookDb;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class Search {
    private static String commandSyntax = "find  \"query text\"";

    public static void Run(String[] args, BookDb bookDb) throws IOException, ParseException {
        if(args.length <= 1) {
            System.out.println(commandSyntax);
            return;
        }
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
