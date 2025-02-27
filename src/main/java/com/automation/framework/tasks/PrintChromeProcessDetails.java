package com.automation.framework.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class PrintChromeProcessDetails {
    public static void main(String args[]) {
    	List<Integer> unknownStatusPIDs=null;
        try {
            // Execute the tasklist command to get all chrome.exe processes with their details
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "tasklist /FI \"IMAGENAME eq chrome.exe\" /V");
            Process process = processBuilder.start();
            
            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
         // Use Java 8 Streams to process the lines and filter PIDs based on "UNKNOWN" status
         // Use Java 8 Streams to process the lines and filter PIDs based on "UNKNOWN" status
            unknownStatusPIDs = reader.lines()  // Stream each line
                    .skip(1)  // Skip the header line
                    .filter(line -> line.contains("chrome.exe"))  // Only include lines with the process name
                    .map(line -> line.split("\\s+"))  // Split the line by spaces into columns
                    .filter(columns -> columns.length > 4)  // Ensure there are enough columns
                    .filter(columns -> "UnKnown".equalsIgnoreCase(columns[6])) 
                    .filter(columns -> "N/A".equalsIgnoreCase(columns[9]))// Filter by "UNKNOWN" status (5th column)
                    .map(columns -> Integer.parseInt(columns[1]))  // Get the PID from the second column (index 1)
                    .collect(Collectors.toList());  // Collect PIDs into a list


            // Print the PIDs with "UNKNOWN" status
            unknownStatusPIDs.forEach(n -> System.out.println(n)); // Collect all PIDs into a list

          

        } catch (Exception e) {
            e.printStackTrace();
        }
		//return unknownStatusPIDs;
    }
}
