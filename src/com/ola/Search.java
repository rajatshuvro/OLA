package com.ola;

import com.ola.parsers.FlatObjectParser;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Search {
    private static String commandSyntax = "search  [query text]";

    public static void Run(String[] args, DataProvider dataProvider) throws IOException, ParseException {
        if(args.length <= 1) {
            System.out.println(commandSyntax);
            return;
        }
        String queryText = ConstructQuery(args);
        var results = new ArrayList<String>();

        var bookDb = dataProvider.BookDb;
        var bookSearchIndex = bookDb.GetSearchIndex();

        for (var id: bookSearchIndex.Search(queryText, 5)) {
            results.add(bookDb.GetBook(id).toString());
        }
        OutputResults(results, "----------------------Books----------------------");

        var userDb = dataProvider.UserDb;
        var userSearchIndex = userDb.GetSearchIndex();

        for(var id: userSearchIndex.Search(queryText, 5)){
            results.add(userDb.GetUser(id).toString());
        }
        OutputResults(results, "----------------------Users----------------------");

        System.out.println('\n');
        System.out.println("=========================End of search results=====================");
    }

    private static void OutputResults(ArrayList<String> results, String banner) {
        if (results.size() <= 0) return;

        System.out.println(banner);
        for (var result: results) {
            System.out.println(result);
            System.out.println(FlatObjectParser.RecordSeparator);
        }

        results.clear();
    }

    private static String ConstructQuery(String[] args) {
        var  queryText = args[1];
        for (var i=2; i < args.length; i++ ){
            queryText+= ' ' + args[i];
        }
        return queryText;
    }
}
