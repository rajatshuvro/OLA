package com.ola.luceneIndex;

import com.ola.dataStructures.Book;
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

public class SearchIndex {

    private IndexWriter _writer;
    private ByteBuffersDirectory _memDirectory;
    private IndexSearcher _searcher;
    private ArrayList<String> _ids;

    public SearchIndex(Iterable<Book> books) throws IOException {
        //we need to have the index -> book id mapping
        _ids = new ArrayList<String>();

        //create a in-memory directory
        _memDirectory = new ByteBuffersDirectory();
        //create the indexer with default configuration
        _writer = new IndexWriter(_memDirectory, new IndexWriterConfig());

        BuildIndex(books);
    }

    private Document GetDocument(Book book) throws IOException {
        Document document = new Document();

        //book id field
        //Field idField = new Field(SearchCommons.BookIdTag, book.GetId(), StringField.TYPE_NOT_STORED);
        // book contents
        Field contentField = new Field(SearchCommons.BookRecordTag, book.toString(), TextField.TYPE_NOT_STORED);

        document.add(contentField);
        //document.add(idField);

        return document;
    }

    private void Add(Book book) throws IOException {
        //System.out.println("Indexing "+book.GetId());
        Document document = GetDocument(book);
        _writer.addDocument(document);
    }

    private void BuildIndex(Iterable<Book> books) throws IOException {
        for (Book book : books) {
                _ids.add(book.GetId());
               Add(book);
            }
        _writer.close();
        // Build an IndexSearcher using the in-memory index
        _searcher = new IndexSearcher(DirectoryReader.open(_memDirectory));
    }

    public String[] Search(String queryString, int topHitCount) throws IOException, ParseException {
        // Build a Query object
        var parser = new QueryParser(SearchCommons.BookRecordTag, new StandardAnalyzer());
        var query = parser.parse(queryString);
        // Search for the query
        var topDocs = _searcher.search(query, topHitCount);

        var hitCount = topHitCount < topDocs.totalHits.value? topHitCount: (int)(topDocs.totalHits.value);

        var topHitBookIds = new String[hitCount];
        for (var i=0; i< hitCount; i++) {
            topHitBookIds[i]= _ids.get(topDocs.scoreDocs[i].doc);
        }
        return topHitBookIds;
    }

}
