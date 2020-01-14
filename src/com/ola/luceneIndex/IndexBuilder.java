package com.ola.luceneIndex;

import java.io.IOException;
import java.nio.file.Paths;

import com.ola.dataStructures.Book;
import org.apache.lucene.document.*;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class IndexBuilder {

    private IndexWriter _writer;

    // build an index in memory with MemoryIndex.
    // note: limited to one document per index
    // have to present result by descending order of scores per index
    public IndexBuilder(String indexDirectoryPath) throws IOException {
        //this directory will contain the indexes
        Directory indexDirectory =
                FSDirectory.open(Paths.get(indexDirectoryPath));

        //create the indexer with default configuration
        _writer = new IndexWriter(indexDirectory, new IndexWriterConfig());
    }

    public void close() throws CorruptIndexException, IOException {
        _writer.close();
    }

    private Document getDocument(Book book) throws IOException {
        Document document = new Document();

        //book id field
        Field idField = new Field(SearchCommons.BookIdTag, book.GetId(), StringField.TYPE_STORED);
        // book contents
        Field contentField = new Field(SearchCommons.BookRecordTag, book.toString(), TextField.TYPE_STORED);

        document.add(contentField);
        document.add(idField);

        return document;
    }

    private void indexBook(Book book) throws IOException {
        System.out.println("Indexing "+book.GetId());
        Document document = getDocument(book);
        _writer.addDocument(document);
    }

    public void createIndex(Iterable<Book> books) throws IOException {
        for (Book book : books) {
               indexBook(book);
            }
    }


}
