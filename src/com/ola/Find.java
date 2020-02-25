package com.ola;

import com.ola.NativeSearch.InvertedIndex;
import com.ola.NativeSearch.JaroWinkler;
import com.ola.luceneIndex.ISearchDocument;
import com.ola.parsers.FlatObjectParser;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Find {
    private static String commandSyntax = "find  [query text]";

    public static void Run(String[] args, DataProvider dataProvider) throws IOException, ParseException {
        if(args.length <= 1) {
            System.out.println(commandSyntax);
            return;
        }
        String queryText = ConstructQuery(args);

        var jwrIndex = new InvertedIndex(new JaroWinkler());
        var docs = new ArrayList<ISearchDocument>();
        for(var book: dataProvider.BookDb.GetAllBooks())
        {
            jwrIndex.Add(book.GetContent());
            docs.add(book);
        }
        for(var user: dataProvider.UserDb.GetAllUsers())
        {
            jwrIndex.Add(user.GetContent());
            docs.add(user);
        }

        var topDocs = jwrIndex.Search(queryText);
        var results = GetResults(docs, topDocs, 5);

        OutputResults(results, "-----------------------Showing top 5 search results --------------------");
        System.out.println('\n');
        System.out.println("=========================End of search results=====================");
    }

    private static ArrayList<String> GetResults(ArrayList<ISearchDocument> docs, int[] topDocs, int maxCount) {
        var results = new ArrayList<String>();

        for(var i=0; i < maxCount && i < topDocs.length; i++)
            results.add(docs.get(topDocs[i]).toString());

        return results;
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
