package com.automation.framework.tasks;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.util.List;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class YouTubeSearchAndPlay {
    public static String playYoutubeVideo(String query, List<Integer> subProcesses) throws IOException, InterruptedException {
        // Define the search query (can be modified based on user input)
    	
    	//ProcessLister.terminateAllSubProcesses(ProcessLister.unKnownChromeProcessIds("chrome.exe"));
        String[] search = query.toLowerCase().split("play youtube video for",2);
        
        String searchQuery = search[1];

        Browser browser = null;  // Declare browser variable here to access later for closing
        Playwright playwright = null;

        Process browserProcess = null;  // Declare Process variable to manage the browser process

        
            // Initialize Playwright and launch the browser
            playwright = Playwright.create();

            // Specify the path to your Google Chrome executable (adjust the path if necessary)
            String chromePath = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"; // Adjust as needed

            // Use ProcessBuilder to launch Chrome and capture the process
           // ProcessBuilder processBuilder = new ProcessBuilder(chromePath, "--remote-debugging-port=9222"); // Add arguments if needed
           // browserProcess = processBuilder.start();  // Start Chrome process and capture the Process

            // Launch the browser using Playwright with the specified Chrome executable
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setExecutablePath(Path.of(chromePath))  // Set the path to Google Chrome
                    .setHeadless(false) // Make the browser visible
            );

            // Create a new page (tab) in the browser
            Page page = browser.newPage();
            

            // Build the YouTube search URL with the query
            String youtubeSearchUrl = "https://www.youtube.com/results?search_query=" + searchQuery;

            // Navigate to the YouTube search results URL
            page.navigate(youtubeSearchUrl);

            // Wait for the first video link to be visible
            page.waitForSelector("ytd-video-renderer a#video-title");

            // Click on the first video in the search results (inside ytd-video-renderer)
            page.locator("ytd-video-renderer a#video-title").first().click();

            String processName = ManagementFactory.getRuntimeMXBean().getName();
            
            // The process name is in the format: "pid@hostname"
            String pid = processName.split("@")[0];
            
            System.out.println("Process ID: youtube " + pid);
            
            subProcesses.add(Integer.parseInt(pid));
            
            subProcesses.addAll(ProcessLister.unKnownChromeProcessIds("chrome.exe"));
                              
            return "success";

       
    }
    
   
}
