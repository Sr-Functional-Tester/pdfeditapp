package com.automation.framework.tasks;

public class RestartSystem {
    public static String restartSystem() {
        try {
            // Command to restart the system
            String command = "shutdown -r -t 0"; // -r restarts the system immediately
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("System is restarting...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}

