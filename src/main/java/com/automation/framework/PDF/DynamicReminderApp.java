package com.automation.framework.PDF;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.lang.management.ManagementFactory;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DynamicReminderApp {

	// Method to parse user input and get the scheduled time and reminder message
	public static void reminder(String userInput, List<Integer> subProcessess) {
		// Split input into time part and reminder message part
		String[] parts = userInput.toLowerCase().split(" about ");
		if (parts.length < 2) {
			System.out.println("Invalid input format");
			return;
		}

		String reminderMessage = parts[1].trim();
		String timePart = parts[0].trim();

		// Extract time portion from input if present in "at <time>" format
		if (timePart.contains("at")) {
			timePart = timePart.split("at")[1].trim(); // Extract the time part after "at"
		}

		// Parse the time from user input (e.g., "12:30pm tomorrow", "3pm on 1st
		// January")
		LocalDateTime reminderDateTime = parseTime(timePart);

		if (reminderDateTime == null) {
			System.out.println("Could not understand the time format.");
			return;
		}

		// Schedule the reminder popup
		schedulePopup(reminderDateTime, reminderMessage, subProcessess);
	}

	// Method to parse the time string and convert it into LocalDateTime
	private static LocalDateTime parseTime(String timeInput) {
		try {
			// Normalize input (convert to lowercase and remove extra spaces)
			String normalizedInput = timeInput.toLowerCase().trim();

			// Get the current date and time
			LocalDateTime now = LocalDateTime.now();

			// Handle special keywords like "tomorrow", "next week", "next Friday", etc.
			if (normalizedInput.contains("tomorrow")) {
				normalizedInput = normalizedInput.replace("tomorrow", "").trim();
				LocalDateTime tomorrow = now.plusDays(1); //22+17 39-22=17
				return combineDateAndTime(tomorrow, normalizedInput);

			} else if (normalizedInput.contains("next")) {
				String dayOfWeek = normalizedInput.split(" ")[1]; // Extract day like "Friday"
				LocalDateTime nextDate = getNextDayOfWeek(now, dayOfWeek);
				String timePart = normalizedInput.replace("next " + dayOfWeek, "").trim();
				return combineDateAndTime(nextDate, timePart);

			} else {
				return combineDateAndTime(now, normalizedInput);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Combine date and time from LocalDateTime and a time string (e.g., "12:30pm")
	private static LocalDateTime combineDateAndTime(LocalDateTime date, String timeString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
		try {
			timeString = timeString.replaceAll("\\s+", "").toLowerCase();
			System.out.println(LocalDateTime.now()+"---"+date+"----"+timeString);
			LocalTime time = LocalTime.parse(timeString, formatter);
			return date.withHour(time.getHour()).withMinute(time.getMinute()).withSecond(0);
		} catch (Exception e) {
			System.out.println("Time parsing failed for input: " + timeString);
			e.printStackTrace();
		}
		return null;  // 22+
	}

	// Get next occurrence of a specific day of the week (e.g., next Friday)
	private static LocalDateTime getNextDayOfWeek(LocalDateTime date, String dayOfWeek) {
		DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
		return date.with(TemporalAdjusters.next(targetDay));
	}

	// Method to schedule the reminder popup at the specified time
	private static void schedulePopup(LocalDateTime reminderDateTime, String message, List<Integer> subProcessess) {
		long delay = Duration.between(LocalDateTime.now(), reminderDateTime).toMillis();

		if (delay <= 0) {
			System.out.println("The time for the reminder has already passed.");
			return;
		}

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		Runnable reminderTask = () -> {
			showReminderDialog(message, scheduler, reminderDateTime, subProcessess);
		};
		scheduler.schedule(reminderTask, delay, TimeUnit.MILLISECONDS);
	}

	private static void showReminderDialog(String message, ScheduledExecutorService scheduler,
			LocalDateTime originalTime, List<Integer> subProcessess) {
		// Create a JDialog that stays on top of other windows
		JDialog reminderDialog = new JDialog();
		reminderDialog.setTitle("Reminder");
		reminderDialog.setModal(true); // Make it modal
		reminderDialog.setAlwaysOnTop(true); // Keep it on top of all other windows
		reminderDialog.setSize(350, 150);
		reminderDialog.setLayout(new BorderLayout());

		// Set background color of the dialog to yellow (entire window)
		reminderDialog.getContentPane().setBackground(Color.YELLOW);

		// Display the reminder message in black
		JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
		messageLabel.setForeground(Color.BLACK); // Set text color to black
		reminderDialog.add(messageLabel, BorderLayout.CENTER);

		// Create panel for buttons with side-by-side layout
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(Color.YELLOW); // Set background of button panel to yellow

		// Create "OK" button to close the dialog and terminate the program
		JButton okButton = new JButton("OK");
		okButton.setBackground(Color.BLACK); // Set button background color to black
		okButton.setForeground(Color.WHITE); // Set text color to white
		okButton.addActionListener(e -> {
			reminderDialog.dispose(); // Close the dialog
			String processName = ManagementFactory.getRuntimeMXBean().getName();

			// The process name is in the format: "pid@hostname"
			String pid = processName.split("@")[0];

			System.out.println("Process ID reminder: " + pid);
			subProcessess.add(Integer.parseInt(pid));
		});
		buttonPanel.add(okButton);

		// Create "Snooze" button to reschedule the reminder
		JButton snoozeButton = new JButton("Snooze (10 minutes)");
		snoozeButton.setBackground(Color.BLACK); // Set button background color to black
		snoozeButton.setForeground(Color.WHITE); // Set text color to white
		snoozeButton.addActionListener(e -> {
			LocalDateTime snoozedTime = originalTime.plusMinutes(10); // Reschedule 10 minutes later
			schedulePopup(snoozedTime, message, subProcessess); // Reschedule the popup
			reminderDialog.dispose(); // Close the current dialog

		});
		buttonPanel.add(snoozeButton);

		// Add the button panel to the dialog
		reminderDialog.add(buttonPanel, BorderLayout.SOUTH);

		// Center the dialog on the screen
		reminderDialog.setLocationRelativeTo(null);

		// Make the dialog visible
		reminderDialog.setVisible(true);

	}

	public static String scheduleReminder(String query, List<Integer> subProcessess) {

		String userInput = query;

		reminder(userInput, subProcessess);

		return "success";

	}
	
	public static void main(String[] args) {
		List<Integer> subProcessess = new ArrayList<Integer>();
		String query = "Schedule a reminder at 5:00 pm tomorrow about client meeting";
		scheduleReminder(query, subProcessess);
	}
}
