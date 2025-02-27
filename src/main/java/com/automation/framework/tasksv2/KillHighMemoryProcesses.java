package com.automation.framework.tasksv2;

import java.io.*;
import java.util.*;

public class KillHighMemoryProcesses {

    public static void killProcessess() {
        // List of processes to exclude from being killed
       List<String> excludedProcesses = Arrays.asList("explorer.exe");

       // List<String> excludedProcesses = Arrays.asList("SpringToolSuite.exe");

        try {
            // Run 'tasklist' command to get the list of processes with memory usage
            ProcessBuilder pb = new ProcessBuilder("tasklist", "/fo", "csv", "/nh");
        	//ProcessBuilder pb = new ProcessBuilder("tasklist");
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            List<String[]> processes = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
            	System.out.println(line);
                String[] processInfo = line.split(",");
                processes.add(processInfo);
            }
            reader.close();

            // Iterate through the processes
            for (String[] processInfo : processes) {
                String processName = processInfo[0].replace("\"", "");
                String memUsage = processInfo[4].replace("\"", "").replace(",", "");
             ///  if(processName.equalsIgnoreCase("SpringToolSuite4")) {
                // Check if the process is excluded
                if (!excludedProcesses.contains(processName) && !processName.isEmpty()) {
                    try {
                        long memoryUsage = parseMemoryUsage(memUsage);
                        System.out.println("mem usage  "+processName+"--"+memoryUsage);
                      // if (memoryUsage > 2) { // You can set your threshold here, e.g., 100 MB
                            System.out.println("Killing process: " + processName + " with memory usage: " + memoryUsage / 1024 + " MB");

                            // Run the 'taskkill' command to kill the process
                            ProcessBuilder killPb = new ProcessBuilder("taskkill", "/F", "/IM", processName);
                            killPb.start();
                            System.out.println("process terminated");
                      // }
                    } catch (NumberFormatException e) {
                        // Handle invalid memory format
                        System.err.println("Error parsing memory usage for process: " + processName);
                    }
               // }
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long parseMemoryUsage(String memUsage) {
        // Tasklist memory usage format is like "15,000 K" (convert K to bytes)
        long memoryInBytes = 0;
        if (memUsage.endsWith("K")) {
            memUsage = memUsage.replace("K", "").trim();
            memoryInBytes = Long.parseLong(memUsage) * 1024; // Convert KB to Bytes
        } else if (memUsage.endsWith("M")) {
            memUsage = memUsage.replace("M", "").trim();
            memoryInBytes = Long.parseLong(memUsage) * 1024 * 1024; // Convert MB to Bytes
        }
        return memoryInBytes;
    }
}
