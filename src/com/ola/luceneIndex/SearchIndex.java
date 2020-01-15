package com.ola.luceneIndex;

import java.io.IOException;

import com.ola.dataStructures.Book;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.ByteBuffersDirectory;

public class SearchIndex {

    private IndexWriter _writer;
    private ByteBuffersDirectory _memDirectory;
    private IndexSearcher _searcher;

    public SearchIndex(Iterable<Book> books) throws IOException {
        //create a in-memory directory
        _memDirectory = new ByteBuffersDirectory();
        //create the indexer with default configuration
        _writer = new IndexWriter(_memDirectory, new IndexWriterConfig());

        BuildIndex(books);
    }

    private Document GetDocument(Book book) throws IOException {
        Document document = new Document();

        //book id field
        Field idField = new Field(SearchCommons.BookIdTag, book.GetId(), StringField.TYPE_STORED);
        // book contents
        Field contentField = new Field(SearchCommons.BookRecordTag, book.toString(), TextField.TYPE_STORED);

        document.add(contentField);
        document.add(idField);

        return document;
    }

    private void Add(Book book) throws IOException {
        System.out.println("Indexing "+book.GetId());
        Document document = GetDocument(book);
        _writer.addDocument(document);
    }

    private void BuildIndex(Iterable<Book> books) throws IOException {
        for (Book book : books) {
               Add(book);
            }
        _writer.close();
        // Build an IndexSearcher using the in-memory index
        _searcher = new IndexSearcher(DirectoryReader.open(_memDirectory));
    }

    public void Search(String query){


    }

}
