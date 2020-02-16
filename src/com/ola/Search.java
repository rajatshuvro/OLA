package com.ola;

import com.ola.luceneIndex.IdAndScore;
import com.ola.parsers.FlatObjectParser;
import com.ola.parsers.ParserUtilities;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Search {
    private static String commandSyntax = "search  [query text]";

    public static void Run(String[] args, DataProvider dataProvider) throws IOException, ParseException {
        if(args.length <= 1) {
            System.out.println(commandSyntax);
            return;
        }
        String queryText = ConstructQuery(args);

        var results = GetUnifiedSearchResults(dataProvider, queryText, 5);

        OutputResults(results, "-----------------------Showing top 5 search results --------------------");
        System.out.println('\n');
        System.out.println("=========================End of search results=====================");
    }

    private static ArrayList<String> GetUnifiedSearchResults(DataProvider dataProvider, String query, int maxCount) throws IOException, ParseException {
        var results = new ArrayList<String>();
        var idAndScores = new ArrayList<IdAndScore>();

        var bookDb = dataProvider.BookDb;
        var bookSearchIndex = bookDb.GetSearchIndex();
        idAndScores.addAll(bookSearchIndex.Search(query, maxCount));

        var userDb = dataProvider.UserDb;
        var userSearchIndex = userDb.GetSearchIndex();
        idAndScores.addAll(userSearchIndex.Search(query, maxCount));

        Collections.sort(idAndScores, Collections.reverseOrder());

        for (var i=0; i < maxCount && i < idAndScores.size(); i++){
            var userId = ParserUtilities.ParseUInt(idAndScores.get(i).Id);
            var isUserId =  userId > 0;
            var result = isUserId ? userDb.GetUser(userId).toString() : bookDb.GetBook(idAndScores.get(i).Id).toString();
            results.add(result);
        }
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
