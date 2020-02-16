package com.ola.luceneIndex;

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

public class DocumentSearchIndex {
    private IndexWriter _writer;
    private ByteBuffersDirectory _memDirectory;
    private IndexSearcher _searcher;
    private ArrayList<String> _ids;

    public DocumentSearchIndex(Iterable<ISearchDocument> documents) throws IOException {
        //we need to have the index -> book id mapping
        _ids = new ArrayList<>();

        //create a in-memory directory
        _memDirectory = new ByteBuffersDirectory();
        //create the indexer with default configuration
        _writer = new IndexWriter(_memDirectory, new IndexWriterConfig());

        BuildIndex(documents);
    }

    private void BuildIndex(Iterable<ISearchDocument> documents) throws IOException {
        for (var document : documents) {
            _ids.add(document.GetId());
            Add(document);
        }
        _writer.close();
        // Build an IndexSearcher using the in-memory index
        _searcher = new IndexSearcher(DirectoryReader.open(_memDirectory));
    }
    private void Add(ISearchDocument searchDocument) throws IOException {
        Document document = new Document();
        Field contentField = new Field(SearchCommons.SearchableDocumentTag, searchDocument.toString(), TextField.TYPE_NOT_STORED);
        document.add(contentField);
        _writer.addDocument(document);
    }

    public ArrayList<IdAndScore> Search(String queryString, int topHitCount) throws IOException, ParseException {
        // Build a Query object
        var parser = new QueryParser(SearchCommons.SearchableDocumentTag, new StandardAnalyzer());
        var query = parser.parse(queryString);
        // Search for the query
        var topDocs = _searcher.search(query, topHitCount);

        var hitCount = topHitCount < topDocs.totalHits.value? topHitCount: (int)(topDocs.totalHits.value);

        var idAndScores = new ArrayList<IdAndScore>();
        for (var i=0; i< hitCount; i++) {
            idAndScores.add(new IdAndScore(_ids.get(topDocs.scoreDocs[i].doc), topDocs.scoreDocs[i].score));
        }
        return idAndScores;
    }
}
