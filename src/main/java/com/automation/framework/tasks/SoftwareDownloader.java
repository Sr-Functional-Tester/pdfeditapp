package com.automation.framework.tasks;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class SoftwareDownloader {

    public static void main(String[] args) {
        // Check if the user has provided a website URL
       

        String url = "https://www.sublimetext.com/download"; // The URL of the website to scrape
        String chromePath = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"; // Adjust as needed

        
        		Playwright playwright = Playwright.create();
            // Launch the browser
        	Browser   browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                       .setExecutablePath(Path.of(chromePath))  // Set the path to Google Chrome
                       .setHeadless(false) // Make the browser visible
               );  
            
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // Navigate to the provided website
            page.navigate(url);

            // Wait for the page to load
            page.waitForSelector("a"); 

            // Extract all <a> tags' text content
            List<String> downloadLinks = page.locator("a")
                    .allTextContents() // Get the text content of each <a> tag
                    .stream()
                    .filter(text -> text != null && (
                        text.toLowerCase().contains("windows") || 
                        text.toLowerCase().contains("download")
                    ))
                    .collect(Collectors.toList());

            // Extract all <a> tags' href and title attributes
            List<String> hrefLinks = ((Collection<String>) page.locator("a")
                    .evaluateAll("elements => elements.map(e => e.href)") // Get the href attribute of all <a> tags
)
                    .stream()
                    .filter(href -> href != null && (
                        href.toLowerCase().contains("windows") || 
                        href.toLowerCase().contains("download")
                    ))
                    .collect(Collectors.toList());

            List<String> titleLinks = ((Collection<String>) page.locator("a")
                    .evaluateAll("elements => elements.map(e => e.title)") // Get the title attribute of all <a> tags
)
                    .stream()
                    .filter(title -> title != null && (
                        title.toLowerCase().contains("windows") || 
                        title.toLowerCase().contains("download")
                    ))
                    .collect(Collectors.toList());

            // Extract all <label> tags' text content and filter
            List<String> labelLinks = page.locator("label")
                    .allTextContents() // Get the text content of each <label> tag
                    .stream()
                    .filter(label -> label != null && (
                        label.toLowerCase().contains("windows") || 
                        label.toLowerCase().contains("download")
                    ))
                    .collect(Collectors.toList());
            // Combine all links from text, href, title attributes, and labels
            downloadLinks.addAll(hrefLinks);
            downloadLinks.addAll(titleLinks);
            downloadLinks.addAll(labelLinks);

            // Remove duplicates (if any)
            downloadLinks = downloadLinks.stream().distinct().collect(Collectors.toList());

            // Print out the found links
            System.out.println("Found download links and labels:");
            downloadLinks.forEach(System.out::println);

            // If there are valid download links, navigate to the first one and start the download
            if (!downloadLinks.isEmpty()) {
                String downloadLink = downloadLinks.get(0); // Choose the first matching link
                System.out.println("Navigating to download link: " + downloadLink);
                
                // Navigate to the download link and wait for the download event
                page.navigate(downloadLink);

                // For demonstration purposes, assuming the download is immediate
                System.out.println("Download started...");

                // Optional: If the download starts automatically and you want to manage it, 
                // set up download path and intercept the download.
                page.onDownload(download -> {
                    Path downloadPath = download.path();
                    if (downloadPath != null) {
                        System.out.println("Downloaded file saved at: " + downloadPath.toString());
                    } else {
                        System.out.println("Download started, but no file path was provided.");
                    }
                });

            } else {
                System.out.println("No download links found containing the keywords 'windows' or 'download'.");
            }

            // Close the browser after the operation
           // browser.close();
        
    }
}
