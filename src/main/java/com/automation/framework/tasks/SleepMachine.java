package com.automation.framework.tasks;

public class SleepMachine {
    public static String sleepSystem() {
        try {
            // Command to put the machine to sleep (Windows)
            String command = "rundll32.exe powrprof.dll,SetSuspendState Sleep";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
           // System.out.println("Machine is going to sleep.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

}

