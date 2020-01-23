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
        System.out.println("----------------------Books----------------------");
        for (var id: bookSearchIndex.Search(queryText, 10)) {
            System.out.println(bookDb.GetBook(id).toString());
            System.out.println(FlatObjectParser.RecordSeparator);
        }

        var userDb = dataProvider.UserDb;
        var userSearchIndex = userDb.GetSearchIndex();
        System.out.println('\n');
        System.out.println("----------------------Users----------------------");
        for(var id: userSearchIndex.Search(queryText, 10)){
            System.out.println(userDb.GetUser(id).toString());
            System.out.println(FlatObjectParser.RecordSeparator);
        }

        var transactionDb = dataProvider.TransactionDb;
        var transactionsSearchIndex = transactionDb.GetSearchIndex();
        System.out.println('\n');
        System.out.println("----------------------Transactions--------------------");
        for(var index: transactionsSearchIndex.Search(queryText, 10)){
            System.out.println(transactionDb.Get(index).toString());
            System.out.println(FlatObjectParser.RecordSeparator);
        }
        System.out.println('\n');
        System.out.println("=========================End of search results=====================");
    }

    private static String ConstructQuery(String[] args) {
        var  queryText = args[1];
        for (var i=2; i < args.length; i++ ){
            queryText+= ' ' + args[i];
        }
        return queryText;
    }
}
