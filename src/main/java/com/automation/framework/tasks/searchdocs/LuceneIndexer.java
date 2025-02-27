package com.automation.framework.tasks.searchdocs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexer {

    // Indexes documents from a specified folder
    public static void indexDocuments(String folderPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(Paths.get("index"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(indexDirectory, config);

        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                // Extract text from the document
               String content = DocumentExtractor.extractTextFromFile(file.getAbsolutePath());
            //	String content =null;
                // Index the document
                Document doc = new Document();
                doc.add(new TextField("content", content, Field.Store.YES));
                writer.addDocument(doc);
            }
        }

        writer.close();
    }

    // Searches for the most relevant documents for a given query
    public static String searchQuery(String queryStr) throws Exception {
        Directory indexDirectory = FSDirectory.open(Paths.get("index"));
        DirectoryReader reader = DirectoryReader.open(indexDirectory);
        IndexSearcher searcher = new IndexSearcher(reader);

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse(queryStr);

        TopDocs results = searcher.search(query, 1); // Fetch top 1 document for brevity
        String context = null;
        TotalHits totalHitCount = results.totalHits;
        if (totalHitCount.value > 0) {
            context = searcher.doc(results.scoreDocs[0].doc).get("content");
        }

        reader.close();
        return context; // Return the most relevant context
    }

    public static void main(String[] args) {
        try {
            // Index all documents in the knowledgebase folder
            indexDocuments("C:\\knowledgebase");

            // Example query
            String query = "What is document indexing?";
            String context = searchQuery(query);
            System.out.println("Context from search: " + context);

            // Step 3: Apply Question-Answering model (get answer based on the context)
          // String answer = QuestionAnswering.answerQuestion(context, query);
          //  System.out.println("Final Answer: " + answer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
