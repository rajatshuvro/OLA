package com.ola.luceneIndex;

import com.ola.dataStructures.User;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.ByteBuffersDirectory;

import java.io.IOException;
import java.util.ArrayList;

public class UserSearchIndex {
    private IndexWriter _writer;
    private ByteBuffersDirectory _memDirectory;
    private IndexSearcher _searcher;
    private ArrayList<Integer> _ids;

    public UserSearchIndex(Iterable<User> users) throws IOException {
        //we need to have the index -> book id mapping
        _ids = new ArrayList<Integer>();

        //create a in-memory directory
        _memDirectory = new ByteBuffersDirectory();
        //create the indexer with default configuration
        _writer = new IndexWriter(_memDirectory, new IndexWriterConfig());

        BuildIndex(users);
    }

    private void BuildIndex(Iterable<User> users) throws IOException {
        for (User user : users) {
            _ids.add(user.Id);
            Add(user);
        }
        _writer.close();
        // Build an IndexSearcher using the in-memory index
        _searcher = new IndexSearcher(DirectoryReader.open(_memDirectory));
    }
    private void Add(User user) throws IOException {
        Document document = GetDocument(user);
        _writer.addDocument(document);
    }

    private Document GetDocument(User user) throws IOException {
        Document document = new Document();
        Field contentField = new Field(SearchCommons.UserRecordTag, user.toString(), TextField.TYPE_NOT_STORED);
        document.add(contentField);
        return document;
    }

    public int[] Search(String queryString, int topHitCount) throws IOException, ParseException {
        // Build a Query object
        var parser = new QueryParser(SearchCommons.UserRecordTag, new StandardAnalyzer());
        var query = parser.parse(queryString);
        // Search for the query
        var topDocs = _searcher.search(query, topHitCount);

        var hitCount = topHitCount < topDocs.totalHits.value? topHitCount: (int)(topDocs.totalHits.value);

        var topHitBookIds = new int[hitCount];
        for (var i=0; i< hitCount; i++) {
            topHitBookIds[i]= _ids.get(topDocs.scoreDocs[i].doc);
        }
        return topHitBookIds;
    }
}
