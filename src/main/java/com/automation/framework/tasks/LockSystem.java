package com.automation.framework.tasks;

public class LockSystem {
    public static String lockScreen() {
        try {
            // Command to lock the screen
            String command = "rundll32 user32.dll,LockWorkStation";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
           // System.out.println("System is locked.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
