package com.automation.framework.tasks;

import java.io.IOException;

public class ShutdownSystem {
    public static String shutDownSystem() {
        try {
            // Execute the shutdown command
            String command = "shutdown -s -t 0"; // This will shut down the system immediately
            Process process = Runtime.getRuntime().exec(command);

            // Wait for the process to complete (optional)
            process.waitFor();
            
           // System.out.println("System is shutting down...");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
