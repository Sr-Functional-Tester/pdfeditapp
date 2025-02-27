package com.automation.framework.tasks;

import java.io.*;
import java.util.*;

public class CleanSystem {

	public static String cleanSystem() {
		try {
			// Step 1: Clean Temporary Files
			runCommand("del /q /f /s %temp%\\*");
			runCommand("del /q /f /s C:\\Windows\\Temp\\*");

			// Step 2: Clean Windows Update Cache
//            runCommand("net stop wuauserv");
//            runCommand("del /f /s /q C:\\Windows\\SoftwareDistribution\\Download\\*");
//            runCommand("net start wuauserv");

			// Step 3: Clean Prefetch Files
			runCommand("del /q /f /s C:\\Windows\\Prefetch\\*");

			// Step 4: Clean Event Logs
//            runCommand("wevtutil cl Application");
//            runCommand("wevtutil cl Security");
//            runCommand("wevtutil cl System");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	// Helper method to run a system command
	private static void runCommand(String command) throws IOException, InterruptedException {
		// Prepare the command
		List<String> cmdList = new ArrayList<>();
		cmdList.add("cmd.exe");
		cmdList.add("/c"); // /c runs the command and then terminates
		cmdList.add(command);

		// Start the process and wait for it to complete
		ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
		processBuilder.inheritIO(); // This will allow us to see output from the command in the console

		Process process = processBuilder.start();
		int exitCode = process.waitFor();

	}
}
