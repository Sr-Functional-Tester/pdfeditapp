package com.automation.framework.tasks;

public class HibernateSystem {
    public static String hibernateSystem() {
        try {
            // Command to hibernate the system
            String command = "shutdown /h"; // /h initiates hibernation
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("System is hibernating...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
