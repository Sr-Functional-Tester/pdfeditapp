package com.automation.framework.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KillUserApplicationsWindows {

    public static String closeAllApplications() {
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

                    // Skip system processes and background services
                    if (isUserApplication(processName)) {
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

    // Method to determine if the process is a user-opened application (not a system process)
    public static boolean isUserApplication(String processName) {
        // List of known system processes to exclude
        String[] systemProcesses = {
            "System",              // System process
            "System Idle Process", // Idle process (do not terminate)
            "explorer.exe",        // Windows Explorer
            "taskmgr.exe",         // Task Manager
            "cmd.exe",             // Command Prompt
            "powershell.exe",      // PowerShell (admin)
            "services.exe",        // Windows Services
            "svchost.exe",         // Service Host
            "tasklist",            // This process itself should not be killed
            "winlogon.exe",        // Windows login process
            "lsass.exe",           // Local Security Authority Subsystem Service
            "spoolsv.exe",         // Print spooler service
            "wuauserv.exe",        // Windows Update service
            "msiexec.exe",         // Windows Installer
            "dwm.exe",              // Desktop Window Manager
            "SpringToolSuite4.exe",
            "javaw.exe"
            
        };

        // Check if the process name is in the list of system processes
        for (String systemProcess : systemProcesses) {
            if (processName.equalsIgnoreCase(systemProcess)) {
                return false; // This is a system process, so it's not a user application
            }
        }
        return true; // This is likely a user application
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

