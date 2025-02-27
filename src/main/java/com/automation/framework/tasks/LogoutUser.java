package com.automation.framework.tasks;

public class LogoutUser {
    public static String logoutSystem() {
        try {
            // Command to log off the user
            String command = "shutdown -l"; // -l logs off the current user
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("Logging off user...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}

