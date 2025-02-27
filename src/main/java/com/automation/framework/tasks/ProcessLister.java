package com.automation.framework.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessLister {

    /**
     * Get the process IDs for a given process name using the tasklist command on Windows.
     * @param processName The name of the process (e.g., "chrome.exe", "java.exe")
     * @return A list of process IDs for the given process name.
     */
    public static List<Integer> getProcessIds(String processName) {
        try {
            // Run the tasklist command and capture the output
            Process process = new ProcessBuilder("tasklist", "/FI", "IMAGENAME eq " + processName)
                    .start();

            // Read and process the output, filtering for the process name and extracting PIDs
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.lines()  // Stream each line
                        .skip(1)  // Skip the header line
                        .filter(line -> line.contains(processName))  // Only include lines with the process name
                        .map(line -> line.split("\\s+")[1])  // Get the PID from the second column
                        .map(Integer::parseInt)  // Convert string to integer (PID)
                        .collect(Collectors.toList());  // Collect all PIDs into a list
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();  // Return an empty list if any error occurs
    }

    public static void main(String[] args) {
        // Example usage to get process IDs for chrome.exe
        String processName = "chrome.exe"; // You can change this to any process name
        List<Integer> pids = getProcessIds(processName);

        // Print the PIDs of the specified process
       // System.out.println("List of Process IDs for " + processName + ":");
        pids.forEach(System.out::println);
    }
    
    public static void terminateAllSubProcesses(List<Integer> subProcesses) throws IOException, InterruptedException {
		// Iterate over the list of child processes and destroy them
		for (Integer processId : subProcesses) {
			killProcess(processId);// Terminate the child process
		}
	}

	public static void killProcess(Integer pid) throws IOException, InterruptedException {
		String os = System.getProperty("os.name").toLowerCase(); // Get the OS name
		String command;

		// Check if the OS is Windows or Unix-like (Linux/macOS)
		if (os.contains("win")) {
			// Windows command to kill process by PID

			command = "taskkill /F /PID " + pid;

		} else {
			// Linux/macOS command to kill process by PID
			command = "kill -9 " + pid;
		}

		// Execute the command using Runtime.exec()

		Process process = Runtime.getRuntime().exec(command);

		// Wait for the command to complete
		int exitCode = process.waitFor();
		if (exitCode == 0) {
			//System.out.println("Process " + pid + " has been successfully terminated.");
		} else {
			//System.out.println("Failed to terminate process " + pid);
		}
	}
	
	 public static List<Integer> unKnownChromeProcessIds(String processName) {
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
	                    .filter(line -> line.contains(processName))  // Only include lines with the process name
	                    .map(line -> line.split("\\s+"))  // Split the line by spaces into columns
	                    .filter(columns -> columns.length > 4)  // Ensure there are enough columns
	                    .filter(columns -> "UnKnown".equalsIgnoreCase(columns[6]))
	                    .filter(columns -> "N/A".equalsIgnoreCase(columns[9]))// Filter by "UNKNOWN" status (5th column)
	                    .map(columns -> Integer.parseInt(columns[1]))  // Get the PID from the second column (index 1)
	                    .collect(Collectors.toList());  // Collect PIDs into a list

	            // Print the PIDs with "UNKNOWN" status
	           // return unknownStatusPIDs;   // Collect all PIDs into a list

	          

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			return unknownStatusPIDs;
	    }
}
