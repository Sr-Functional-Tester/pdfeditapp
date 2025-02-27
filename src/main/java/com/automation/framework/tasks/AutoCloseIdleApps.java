package com.automation.framework.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AutoCloseIdleApps {

    public static String closeAllIdleApplications() {
        try {
            // Command to get list of running processes (Windows)
            String command = "tasklist"; // Get list of tasks
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                // Skip the first line (column headers)
                if (line.startsWith("Image") || line.startsWith("===")) {
                    continue;
                }

                // Process each line of output
                String[] processInfo = line.trim().split("\\s+");
                if (processInfo.length > 1) {
                    String processName = processInfo[0]; // Process name (e.g., "notepad.exe")
                    String pid = processInfo[1];         // Process ID (PID)

                    // Check if process should be closed (custom logic)
                    if (isIdle(processName)) {
                        // Kill the process
                        System.out.println("Killing process: " + processName + " with PID: " + pid);
                        killProcess(pid);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    // Custom logic to decide if a process is idle
    public static boolean isIdle(String processName) {
        // For demonstration, we will assume that processes like "notepad.exe" are idle.
        // You can extend this logic based on other criteria (e.g., CPU usage, time since last activity).
        return processName.equalsIgnoreCase("notepad.exe");
    }

    // Method to kill a process using its PID (Windows version)
    public static void killProcess(String pid) {
        try {
            // Command to kill the process (Windows)
            String command = "taskkill /F /PID " + pid;
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor(); // Wait for the command to complete
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
