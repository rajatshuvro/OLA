package com.ola;

import com.ola.parsers.FlatObjectParser;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class Search {
    private static String commandSyntax = "find  \"query text\"";

    public static void Run(String[] args, DataProvider dataProvider) throws IOException, ParseException {
        if(args.length <= 1) {
            System.out.println(commandSyntax);
            return;
        }
        String queryText = ConstructQuery(args);

        var bookDb = dataProvider.BookDb;
        var bookSearchIndex = bookDb.GetSearchIndex();
        for (var id: bookSearchIndex.Search(queryText, 10)) {
            System.out.println(FlatObjectParser.RecordSeparator);
            System.out.println(bookDb.GetBook(id).toString());
        }

        var userDb = dataProvider.UserDb;
        var userSearchIndex = userDb.GetSearchIndex();
        for(var id: userSearchIndex.Search(queryText, 10)){
            System.out.println(FlatObjectParser.RecordSeparator);
            System.out.println(userDb.GetUser(id).toString());
        }

        var transactionDb = dataProvider.TransactionDb;
        var transactionsSearchIndex = transactionDb.GetSearchIndex();
        for(var index: transactionsSearchIndex.Search(queryText, 10)){
            System.out.println(FlatObjectParser.RecordSeparator);
            System.out.println(transactionDb.Get(index).toString());
        }
        System.out.println(FlatObjectParser.RecordSeparator);
        System.out.println("-------------------End of search results----------------");
    }

    private static String ConstructQuery(String[] args) {
        var  queryText = args[1];
        for (var i=2; i < args.length; i++ ){
            queryText+= ' ' + args[i];
        }
        return queryText;
    }
}
