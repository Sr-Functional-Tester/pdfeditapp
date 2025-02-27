package com.automation.framework.googleapi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class GoogleDriveAuthExample {
    // Replace with your client ID and secret.
    private static final String CLIENT_ID = "1028077421449-thl60m4mcpsnadvbqdhvp4c9d3ne971c.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final List<String> SCOPES = Arrays.asList(
    	    DriveScopes.DRIVE  // For Google Drive
    	);


    private static AuthorizationCodeFlow flow;

    public static Credential getCredential(List<String> SCOPES) throws IOException {
        // Use JacksonFactory as the concrete implementation of JsonFactory
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        String query= "C:\\Users\\srinu13587\\Documents\\spring-aI-with-local-LLAMA3-main.zip";
        String str = query.substring(query.lastIndexOf("\\")+1);
        System.out.println("hi== "+str);
        // Create the flow
        flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                jsonFactory,  // Use JacksonFactory instead of JsonFactory
                CLIENT_ID,
                CLIENT_SECRET,
                SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();

        // LocalServerReceiver automatically handles the redirect and captures the authorization code
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();

        // AuthorizationCodeInstalledApp handles the OAuth flow
        AuthorizationCodeInstalledApp authApp = new AuthorizationCodeInstalledApp(flow, receiver);
        Credential credential = authApp.authorize("user");

        return credential;
    }

    public static void uploadFile(Credential credential) throws Exception {
        // Use JacksonFactory for Drive service
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Build the Drive service object
        Drive driveService = new Drive.Builder(new NetHttpTransport(), jsonFactory, credential)
                .setApplicationName("Google Drive API Java Upload Example")
                .build();

        String query= "C:\\Users\\srinu13587\\Documents\\spring-aI-with-local-LLAMA3-main.zip";
        String fileName = query.substring(query.lastIndexOf("\\")+1);
        // Specify the path to the .zip file you want to upload
        java.io.File filePath = new java.io.File(query);

        // Create the media content for uploading
        FileContent mediaContent = new FileContent("application/zip", filePath);

        File fileMetaData = new File();
        fileMetaData.setName(fileName);
        // Create the request to upload the file
        Drive.Files.Create request = driveService.files().create(fileMetaData, mediaContent)
                .setFields("id"); // Specify which fields to return

      
        // Execute the upload request
        com.google.api.services.drive.model.File uploadedFile = request.execute();

        // Output the uploaded file ID
        System.out.println("Uploaded file ID: " + uploadedFile.getId());
    }
    
    public static void main(String[] args) throws Exception {
        Credential credential = getCredential(SCOPES);
        uploadFile(credential);
    }
}
