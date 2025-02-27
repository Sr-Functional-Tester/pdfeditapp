package com.automation.framework.tasks.searchdocs;

import ai.onnxruntime.*;
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

import java.io.File;
import java.io.IOException;
import java.nio.LongBuffer;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SearchAndAnswer {

    private static final String MODEL_PATH = "C:\\Users\\srinu8963\\Downloads\\model.onnx";  // Update to the correct path
    private static final int MAX_LENGTH = 128;  // Maximum length for tokenized input, typically 512 for BERT-like models

    // Indexes documents from a specified folder
    public static void indexDocuments(String folderPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(Paths.get("index"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(indexDirectory, config);

        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                // Extract text from the document (this could be a custom method)
                String content = DocumentExtractor.extractTextFromFile(file.getAbsolutePath());
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

    // Method to get the answer from the context based on the question using ONNX model
    public static String answerQuestion(String context, String question) throws Exception {
        try (OrtEnvironment env = OrtEnvironment.getEnvironment()) {
            // Load the model
            OrtSession.SessionOptions options = new OrtSession.SessionOptions();
            OrtSession session = env.createSession(MODEL_PATH, options);

            // Prepare inputs for the model
            Map<String, OnnxTensor> inputs = prepareInputs(context, question, env);

            // Run inference
            OrtSession.Result output = session.run(inputs);
            return extractAnswer(output);
        }
    }

    private static Map<String, OnnxTensor> prepareInputs(String context, String question, OrtEnvironment env) throws Exception {
        Map<String, OnnxTensor> inputs = new HashMap<>();

        // Tokenize context and question and get their corresponding token IDs
        long[] contextTokens = tokenizeTextToIds(context);
        long[] questionTokens = tokenizeTextToIds(question);

        // Combine context and question tokens
        long[] inputIds = combineTokens(contextTokens, questionTokens);

        // Truncate the inputIds to the maximum length (e.g., 128 or 512)
        if (inputIds.length > MAX_LENGTH) {
            inputIds = Arrays.copyOfRange(inputIds, 0, MAX_LENGTH);
        }

        // Create attention mask (1 for real tokens, 0 for padding tokens)
        long[] attentionMask = new long[inputIds.length];
        for (int i = 0; i < attentionMask.length; i++) {
            attentionMask[i] = 1; // 1 for real tokens
        }

        // Create token_type_ids (0 for context, 1 for question)
        long[] tokenTypeIds = new long[inputIds.length];
        int questionStartIndex = contextTokens.length;
        for (int i = questionStartIndex; i < inputIds.length; i++) {
            tokenTypeIds[i] = 1;  // Set 1 for question tokens
        }

        // Prepare shape arrays (this will vary based on input length)
        long[] shape = {1, inputIds.length};  // Shape should be [1, N], where N is the length of inputIds

        // Convert long[] to LongBuffer for OnnxTensor creation
        LongBuffer inputIdsBuffer = LongBuffer.wrap(inputIds);
        LongBuffer attentionMaskBuffer = LongBuffer.wrap(attentionMask);
        LongBuffer tokenTypeIdsBuffer = LongBuffer.wrap(tokenTypeIds);

        // Convert to OnnxTensor
        OnnxTensor inputIdsTensor = OnnxTensor.createTensor(env, inputIdsBuffer, shape);
        OnnxTensor attentionMaskTensor = OnnxTensor.createTensor(env, attentionMaskBuffer, shape);
        OnnxTensor tokenTypeIdsTensor = OnnxTensor.createTensor(env, tokenTypeIdsBuffer, shape);

        // Add tensors to the input map
        inputs.put("input_ids", inputIdsTensor);
        inputs.put("attention_mask", attentionMaskTensor);
        inputs.put("token_type_ids", tokenTypeIdsTensor);

        return inputs;
    }




    // Method to extract the answer from the model's output
    private static String extractAnswer(OrtSession.Result output) throws OrtException {
        // Get the output (assuming the model returns start and end positions)
        float[] resultData = (float[]) output.get(0).getValue();  // Get tensor values as a float array

        // Assuming the model returns two numbers: start and end token positions
        int startIdx = (int) resultData[0];  // Start token index
        int endIdx = (int) resultData[1];    // End token index

        return "Answer start: " + startIdx + ", Answer end: " + endIdx;  // Simplified output
    }

    // Tokenization (convert text to token IDs, this should be done using a tokenizer)
    private static long[] tokenizeTextToIds(String text) {
        // Placeholder function to convert text to token IDs using HuggingFace or a similar tokenizer
        // For simplicity, let's assume each word gets a unique token ID (use real tokenization in practice)
        String[] words = text.split("\\s+");
        long[] tokenIds = new long[words.length];
        for (int i = 0; i < words.length; i++) {
            tokenIds[i] = words[i].hashCode();  // This is a simplification. Replace with real tokenization.
        }
        return tokenIds;
    }

    // Method to combine context and question tokens into one array (input_ids)
    private static long[] combineTokens(long[] contextTokens, long[] questionTokens) {
        long[] combined = new long[contextTokens.length + questionTokens.length];
        System.arraycopy(contextTokens, 0, combined, 0, contextTokens.length);
        System.arraycopy(questionTokens, 0, combined, contextTokens.length, questionTokens.length);
        return combined;
    }

    // Main method: integrates everything
    public static void main(String[] args) {
        try {
            // Step 1: Index documents in the knowledgebase folder
            String folderPath = "C:\\knowledgebase";  // Specify the correct path to your documents
            indexDocuments(folderPath);

            // Example query to ask
            String query = "what is 2025?";

            // Step 2: Search for the most relevant document based on the query
            String context = searchQuery(query);
            System.out.println("Context from search: " + context);

            if (context != null && !context.isEmpty()) {
                // Step 3: Use the ONNX model to answer the question based on the context
                String answer = answerQuestion(context, query);
                System.out.println("Final Answer: " + answer);
            } else {
                System.out.println("No relevant document found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
