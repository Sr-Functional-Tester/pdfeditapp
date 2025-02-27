package com.automation.framework.tasksv2;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import javax.imageio.ImageIO;

public class AutomatedScreenshots {
	public static void takeSnip(int howManySnips, String howMuchTime) {
		try {
			// Create a Robot object
			Robot robot = new Robot();

			// Define the area to capture (full screen)
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

			int screenshotCount = 1;
			int defaultSnips = 900;
			long maxRunTime = 4500;

			if (howMuchTime != null) {
				maxRunTime = convertToMilliseconds(howMuchTime);
				defaultSnips = calculateNumberOfSnips(howMuchTime);
			}

			if (howManySnips != 0) {
				defaultSnips = howManySnips;
				maxRunTime = calculateMaxTimeInMilliseconds(defaultSnips);
			}
            String folderPath = createScreensFolder();
			while (screenshotCount <= defaultSnips) { // Capture 5 screenshots
				// Capture the screen
				BufferedImage screenshot = robot.createScreenCapture(screenRect);

				// Save the screenshot as a PNG file
				ImageIO.write(screenshot, "PNG",
						new File( folderPath + "\\screenshot_" + screenshotCount + ".png"));

				System.out.println("Screenshot " + screenshotCount + " saved!");

				// Wait for 3 seconds before taking the next screenshot
				Thread.sleep(maxRunTime); // 3000 milliseconds = 3 seconds

				screenshotCount++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static long convertToMilliseconds(String timeInput) {
		// Extract the number and the unit from the input string
		int timeValue = Integer.parseInt(timeInput.substring(0, timeInput.length() - 1)); // Extract the number
		char unit = timeInput.charAt(timeInput.length() - 1); // Extract the unit (last character)

		long timeInMilliseconds = 0;

		// Convert based on the unit
		if (unit == 'm') {
			// Convert minutes to milliseconds
			timeInMilliseconds = timeValue * 60 * 1000;
		} else if (unit == 's') {
			// Convert seconds to milliseconds
			timeInMilliseconds = timeValue * 1000;
		} else {
			throw new IllegalArgumentException(
					"Invalid time unit. Only 'm' (minutes) and 's' (seconds) are supported.");
		}

		return timeInMilliseconds;
	}

	// Method to calculate max time in milliseconds
	public static long calculateMaxTimeInMilliseconds(int snips) {
		return snips * 3 * 1000; // 3 seconds per snip, convert to milliseconds
	}

	// Method to calculate the number of snips based on time input
	public static int calculateNumberOfSnips(String timeInput) {
		// Extract the numeric value and the unit (last character)
		int timeValue = Integer.parseInt(timeInput.substring(0, timeInput.length() - 1)); // Get the number
		char unit = timeInput.charAt(timeInput.length() - 1); // Get the unit (last character)

		// Convert time to total seconds
		long totalSeconds = 0;

		if (unit == 'm') {
			// Convert minutes to seconds
			totalSeconds = timeValue * 60;
		} else if (unit == 's') {
			// Time is already in seconds
			totalSeconds = timeValue;
		} else {
			throw new IllegalArgumentException(
					"Invalid time unit. Only 'm' (minutes) and 's' (seconds) are supported.");
		}

		// Each snip takes 3 seconds, so calculate the number of snips
		return (int) (totalSeconds / 3); // Return the number of snips
	}

	public static String createScreensFolder() {
		// Get the user's home directory (this works on Windows, macOS, and Linux)
		String userHome = System.getProperty("user.home");

		// Build the path to the Pictures directory
		String picturesPath = userHome + "\\Pictures"; // This is typically where the Pictures folder is located on
														// Windows

		// Create a File object for the Pictures directory
		File picturesDir = new File(picturesPath);

		// Check if the Pictures directory exists
		if (!picturesDir.exists()) {
			System.out.println("Pictures directory not found.");
			return null;
		}

		// Start with "screenshots"
		String folderName = "screenshots";
		File screensFolder = new File(picturesDir, folderName);

		// If "screenshots" folder exists, increment the number until an available
		// folder is found
		int counter = 0;

		// If screenshots folder exists, try screenshots0, screenshots1, etc.
		while (screensFolder.exists()) {
			// Construct the folder name with the counter (e.g., screenshots0, screenshots1,
			// etc.)
			folderName = "screenshots" + counter;
			screensFolder = new File(picturesDir, folderName);
			counter++;

			if (!screensFolder.exists())
				 {
				if (screensFolder.mkdir()) {
					System.out.println("Folder '" + folderName + "' created successfully in " + picturesPath);
					// Return the absolute path of the created folder as a string
					break;
				} else {
					System.out.println("Failed to create folder.");
					return null;
				}
			}
		}
		return screensFolder.getAbsolutePath();

	}

	public static void main(String[] args) {
		//System.out.println(createScreensFolder());
		takeSnip(0, "15s");
	}
}
