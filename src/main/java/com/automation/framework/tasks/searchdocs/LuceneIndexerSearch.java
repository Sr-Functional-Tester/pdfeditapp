package com.automation.framework.tasks.searchdocs;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class LuceneIndexerSearch {

    // Delete the existing index if it exists
    private static void deleteExistingIndex() throws IOException {
        File indexDirectory = new File("index");
        if (indexDirectory.exists()) {
            // Delete all files in the 'index' directory
            for (File file : indexDirectory.listFiles()) {
                file.delete();
            }
            System.out.println("Existing index deleted.");
        }
    }

    // Index all documents under the folderPath (including nested folders)
    public static void indexDocuments(String folderPath) throws IOException {
        // Before starting to index, delete the existing index
        deleteExistingIndex();

        // Initialize a fresh index
        Directory indexDirectory = FSDirectory.open(Paths.get("index"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(indexDirectory, config);

        // Traverse the directory recursively to index all files
        File folder = new File(folderPath);
        indexFilesRecursively(folder, writer);

        writer.close();
    }

    // Recursively index files in the given folder (including subfolders)
    private static void indexFilesRecursively(File folder, IndexWriter writer) throws IOException {
        if (folder.isDirectory()) {
            // Traverse subfolders
            for (File file : folder.listFiles()) {
                indexFilesRecursively(file, writer);
            }
        } else {
            // Process file if it's not a directory
            String content = DocumentExtractor.extractTextFromFile(folder.getAbsolutePath());

            // Debugging: Print the content of the file being indexed
            //System.out.println("Indexing file: " + folder.getAbsolutePath());
           // System.out.println("Content: " + content);  // Print content to check if it's correct

            // Create a document and add it to the index
            Document doc = new Document();
            doc.add(new TextField("content", content, Field.Store.YES)); // Full-text field
            doc.add(new TextField("filename", folder.getAbsolutePath(), Field.Store.YES)); // To retrieve the file path
            writer.addDocument(doc);  // Add the document to the index
        }
    }

    public static Set<String> searchQuery(String queryStr) throws Exception {
        Directory indexDirectory = FSDirectory.open(Paths.get("index"));
        DirectoryReader reader = DirectoryReader.open(indexDirectory);
        IndexSearcher searcher = new IndexSearcher(reader);

        Analyzer analyzer = new StandardAnalyzer();

        // We need to modify the query parser so it treats the query as a phrase (exact match)
        QueryParser parser = new QueryParser("content", analyzer);
        parser.setAllowLeadingWildcard(true);  // Allow wildcards at the start of the query (optional)
        Query query = parser.parse('"' + queryStr + '"');  // Treat the query as a phrase (enclosed in quotes)

        // Debugging: Print the query and the parsed content
        //System.out.println("Searching for query: " + queryStr);
       // System.out.println("Parsed query: " + query.toString());  // Display the parsed query for debugging

        TopDocs results = searcher.search(query, Integer.MAX_VALUE);  // Get all results

        Set<String> uniqueFileNames = new HashSet<>(); // Set to ensure unique filenames
        int foundFiles = 0;  // Counter to track number of files with matches

        // Loop through the results and add filenames to the set
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            int docId = scoreDoc.doc;
            Document doc = searcher.doc(docId);
            String filename = doc.get("filename"); // Retrieve the filename

            // Add the file only once, and don't continue if the file is already added
            if (filename != null && !uniqueFileNames.contains(filename)) {
                uniqueFileNames.add(filename); // Add filename to the set to ensure uniqueness
                foundFiles++;

                // Stop searching if 3 files are matched
                if (foundFiles >= 44) {
                    break;
                }
            }
        }

        reader.close();
        return uniqueFileNames;
    }


    // Inspect all documents in the index (for debugging purposes)
    public static void inspectIndex() throws IOException {
        Directory indexDirectory = FSDirectory.open(Paths.get("index"));
        DirectoryReader reader = DirectoryReader.open(indexDirectory);

        // Loop through all documents in the index
        for (int i = 0; i < reader.maxDoc(); i++) {
            Document doc = reader.document(i);
           // System.out.println("Document " + i + ":");
           // System.out.println("Filename: " + doc.get("filename"));
           // System.out.println("Content: " + doc.get("content"));  // Check the content field
        }

        reader.close();
    }

    // Initialize the index (delete the existing one if it exists)
    public static void initializeIndex(String folderPath) throws IOException {
        // Delete the existing index before starting new indexing
        deleteExistingIndex();

        // Proceed to index all files in the folder
       // System.out.println("Deleting existing index and starting fresh indexing...");
        indexDocuments(folderPath);
    }

    public static String luceneIndexSearch(String input) {
        Set<String> resultFiles = new HashSet<>();
        try {
            // Extract the folder path and search query from the input string
            String folderPath = "";
            String query = "";

            // Step 1: Extract the folder path (everything between "search folder" and "with keyword")
            int folderIndex = input.indexOf("search folder");
            if (folderIndex == -1) {
                throw new IllegalArgumentException("Input doesn't contain 'search folder'.");
            }

            // Extract the part of the string after "search folder"
            String afterSearchFolder = input.substring(folderIndex + "search folder".length()).trim();

            // Step 2: Extract folder path (everything before "with keyword")
            int withKeywordIndex = afterSearchFolder.indexOf("with keyword");
            if (withKeywordIndex == -1) {
                throw new IllegalArgumentException("Input doesn't contain 'with keyword'.");
            }

            // Extract folder path (before "with keyword")
            folderPath = afterSearchFolder.substring(0, withKeywordIndex).trim();

            // Step 3: Extract the search query (everything after "with keyword")
            query = afterSearchFolder.substring(withKeywordIndex + "with keyword".length()).trim();

            // Initialize the index and index the documents in the specified folder
            initializeIndex(folderPath);

            // Perform the search using the extracted query
            resultFiles = searchQuery(query);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultFiles.toString();
    }

}
