package com.automation.framework.tasksv2;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CleanUpSystem {

    public static void main(String[] args) {
        // List of directories to delete
    	String str1="C:\\Users\\%USERNAME%\\AppData\\Local\\Packages\\Microsoft.MicrosoftEdge";
     	str1= expandEnvironmentVariables(str1);
     	str1= findFolderPath(str1);
    	System.out.println(str1);
//    	String[] str = str1.split("Microsoft.MicrosoftEdge");
//    	String str2= str[0]+"Microsoft.MicrosoftEdge"+str[1];
//    	System.out.println(str2);
    	 List<String> directoriesToDelete=null;
    	if(str1 != null) {
        directoriesToDelete = Arrays.asList(
               // "C:\\Users\\%USERNAME%\\AppData\\Local\\Microsoft\\Windows\\INetCache", 
        		"C:\\Windows\\SoftwareDistribution\\Download", // Internet Temp Files
                "C:\\Users\\%USERNAME%\\AppData\\Local\\Microsoft\\Windows\\WebCache",
                "C:\\Users\\%USERNAME%\\AppData\\Local\\Temp",// WebCache (Related to Edge)
                                             // Delivery Optimization Files
               // "C:\\$Recycle.Bin",                                                         // Recycle Bin
                                              // Temporary Files
                "C:\\Users\\%USERNAME%\\AppData\\Local\\Microsoft\\Windows\\Explorer",      // Thumbnail Cache
                 str1, // Edge browser data
                "C:\\Users\\%USERNAME%\\AppData\\Local\\Google\\Chrome\\User Data"          // Chrome browser data
        );
    	}else {
    	  directoriesToDelete = Arrays.asList(
    			        "C:\\Windows\\SoftwareDistribution\\Download",
    	               // "C:\\Users\\%USERNAME%\\AppData\\Local\\Microsoft\\Windows\\INetCache",        // Internet Temp Files
    	                "C:\\Users\\%USERNAME%\\AppData\\Local\\Microsoft\\Windows\\WebCache",        // WebCache (Related to Edge)
    	                                              // Delivery Optimization Files
    	               // "C:\\$Recycle.Bin",                                                         // Recycle Bin
    	                "C:\\Users\\%USERNAME%\\AppData\\Local\\Temp",                              // Temporary Files
    	                "C:\\Users\\%USERNAME%\\AppData\\Local\\Microsoft\\Windows\\Explorer",      // Thumbnail Cache
    	                "C:\\Users\\%USERNAME%\\AppData\\Local\\Google\\Chrome\\User Data"          // Chrome browser data
    	        );
    	}

    	stopWindowsUpdateInstallerSysMain();
    	KillHighMemoryProcesses.killProcessess(); 
        // Delete specified directories and files
        for (String dirPath : directoriesToDelete) {
            try {
                deleteDirectory(Paths.get(expandEnvironmentVariables(dirPath)));
            } catch (IOException e) {
                System.out.println("Error deleting " + dirPath + ": " + e.getMessage());
            }
        }

        // Reset browser settings
        emptyRecycleBin();
        resetBrowserSettings();
       // KillHighMemoryProcesses.killProcessess();
        System.out.println("System cleanup complete.");
    }

    // Method to expand environment variables like %USERNAME% in paths
    private static String expandEnvironmentVariables(String path) {
        String expandedPath = path;
        Map<String, String> env = System.getenv();
        
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String key = "%" + entry.getKey() + "%";
            String value = entry.getValue();
            
            // Replace each occurrence of "%key%" with the corresponding value
            expandedPath = expandedPath.replace(key, value);
        }
        
        return expandedPath;
    }


    // Method to recursively delete a directory
    private static void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            System.out.println("Deleted: " + path);
        } else {
            System.out.println("Directory not found: " + path);
        }
    }

    // Method to reset browser settings (Edge and Chrome)
    private static void resetBrowserSettings() {
        // Edge reset by deleting profiles (for user data)
        try {
            Path edgeUserData = Paths.get(System.getenv("USERPROFILE"), "AppData\\Local\\Microsoft\\Edge\\User Data");
            deleteDirectory(edgeUserData);
            System.out.println("Edge browser data reset.");
        } catch (IOException e) {
            System.err.println("Error resetting Edge: " + e.getMessage());
        }

        // Chrome reset by deleting profiles (for user data)
        try {
            Path chromeUserData = Paths.get(System.getenv("USERPROFILE"), "AppData\\Local\\Google\\Chrome\\User Data");
            deleteDirectory(chromeUserData);
            System.out.println("Chrome browser data reset.");
        } catch (IOException e) {
            System.err.println("Error resetting Chrome: " + e.getMessage());
        }

        // Alternatively, you can provide a command-line approach (for Chrome) to reset settings
        try {
            System.out.println("Resetting Chrome settings...");
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "del /f /q %LOCALAPPDATA%\\Google\\Chrome\\User Data\\*");
            pb.start().waitFor();
            System.out.println("Chrome settings reset.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Error resetting Chrome via command: " + e.getMessage());
        }
        
        // Alternatively, you can provide a command-line approach (for Chrome) to reset settings
        try {
            System.out.println("SoftwareDistribution download settings...");
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "rd /s /q C:\\Windows\\SoftwareDistribution\\Download");
            pb.start().waitFor();
            System.out.println("SoftwareDistribution download reset.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Error resetting Chrome via command: " + e.getMessage());
        }

        // You can also automate Edge reset, but it might be more complex depending on the browser version and method of reset.
    }
    
    private static void emptyRecycleBin() {
        try {
            // This command will empty the Recycle Bin for the current user
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "rd /s /q C:\\$Recycle.Bin");
            Process process = pb.start();
            process.waitFor();
            System.out.println("Recycle Bin emptied.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Error emptying Recycle Bin: " + e.getMessage());
        }
    }
    
    private static void stopWindowsUpdateInstallerSysMain() {
        try {
            // This command will empty the Recycle Bin for the current user
            ProcessBuilder pb1 = new ProcessBuilder("cmd", "/c", "net stop wuauserv");
            ProcessBuilder pb2 = new ProcessBuilder("cmd", "/c", "net stop msiserver");
            ProcessBuilder pb3 = new ProcessBuilder("cmd", "/c", "net stop sysmain");
            Process process1 = pb1.start();
            Process process2 = pb2.start();
            Process process3 = pb3.start();
            process1.waitFor();
            process2.waitFor();
            process3.waitFor();
            System.out.println("Recycle Bin emptied.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Error emptying Recycle Bin: " + e.getMessage());
        }
    }
    
    private static String findFolderPath(String partialPath) {
    	File parentDir = new File(partialPath).getParentFile();
    	if(parentDir == null || !parentDir.exists()) {
    		return null;
    	}
    	
    	File[] files = parentDir.listFiles((dir, name) ->
    	name.startsWith(new File(partialPath).getName()));
    	if(files != null && files.length > 0) {
    		return files[0].getAbsolutePath();
    	}
    	return null;
    }
}

