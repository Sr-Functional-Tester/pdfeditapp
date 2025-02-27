package com.automation.framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.automation.framework.tasks.ProcessLister;

public class TaskAutomationWithQueryUIBackup {
	public static List<Integer> subProcesses = new ArrayList<Integer>();

	public static void main(String[] args) throws Exception {
		// Create the main frame for the chat window

		JFrame frame = new JFrame("Submit Query for your task automation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 400);
		frame.setLocationRelativeTo(null); // Center the window on the screen

//		// Add a window listener to detect when the application window is closed
//		frame.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				System.out.println("Main application is closing...");
//				// Clean up all child processes before exiting
//				try {
//					ProcessLister.terminateAllSubProcesses(subProcesses);
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				// Exit the application
//				System.exit(0);
//			}
//		});
//
//		// Register a JVM shutdown hook to clean up child processes when the parent
//		// exits
//		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//			System.out.println("JVM is shutting down...");
//			try {
//				ProcessLister.terminateAllSubProcesses(subProcesses);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}));

		ImageIcon icon = new ImageIcon(TaskAutomationWithQueryUIBackup.class.getResource("/3tasks.png"));

		// Set the icon for the JFrame
		frame.setIconImage(icon.getImage());

		// Create the menu bar with a Help menu
		JMenuBar menuBar = new JMenuBar();
		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpMenuItem = new JMenuItem("Help Documentation");
		helpMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHelpDialog(frame);
			}
		});
		helpMenu.add(helpMenuItem);
		menuBar.add(helpMenu);
		frame.setJMenuBar(menuBar);

		// Create a panel for the chat window layout
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.WHITE); // Set background color to yellow
		panel.setForeground(Color.BLACK);

		// Create a JTextArea for displaying the chat conversation and typing the query
		JTextArea chatArea = new JTextArea();
		chatArea.setEditable(false); // Make chatArea non-editable for displaying conversation
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatArea.setBackground(Color.WHITE); // Set background color of chat area to yellow
		chatArea.setForeground(Color.BLACK); // Set text color to black for all text
		chatArea.setFont(new Font("Calibri", Font.BOLD, 17));

		// Scroll panel for the chatArea
		JScrollPane scrollPane = new JScrollPane(chatArea);
		panel.add(scrollPane, BorderLayout.CENTER);

		// Create a JTextArea for user input (instead of a JTextField)
		JTextArea inputArea = new JTextArea();
		inputArea.setLineWrap(true);
		inputArea.setWrapStyleWord(true);
		inputArea.setBackground(Color.WHITE); // Set background color of input area to white
		inputArea.setForeground(Color.BLACK); // Set text color to black for input
		inputArea.setPreferredSize(new Dimension(300, 50)); // Set width to 300, height to 50

		inputArea.setText("Submit your query here"); // Set default text
		inputArea.setForeground(Color.BLACK); // Set the default text color to black
		inputArea.setFont(new Font("Calibri", Font.BOLD, 17));

		// Make sure the cursor is focused and blinking when the app starts
		inputArea.requestFocusInWindow();

		// Add focus listener to clear the default text when the user starts typing
		inputArea.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent evt) {
				if (inputArea.getText().equals("Submit your query here")) {
					inputArea.setText(""); // Clear default text on focus
					inputArea.setForeground(Color.BLACK); // Set text color to black
				}
			}

			public void focusLost(FocusEvent evt) {
				if (inputArea.getText().isEmpty()) {
					inputArea.setText("Submit your query here"); // Restore default text
					inputArea.setForeground(Color.BLACK); // Set the color back to black
				}
			}
		});

		// Add a MouseListener to clear the default text when the user clicks
		inputArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (inputArea.getText().equals("Submit your query here")) {
					inputArea.setText(""); // Clear default text when clicked
					inputArea.setForeground(Color.BLACK); // Set text color to black
				}
			}
		});

		// Add a KeyListener to clear the default text when any key is pressed
		inputArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (inputArea.getText().equals("Submit your query here")) {
					inputArea.setText(""); // Clear default text when any key is pressed
					inputArea.setForeground(Color.BLACK); // Set text color to black
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// Prevents any further key type action when there is no default text
				if (inputArea.getText().equals("Submit your query here")) {
					inputArea.setText(""); // Clear default text when any key is pressed
					inputArea.setForeground(Color.BLACK); // Set text color to black
				}
			}
		});

		// Create a JPanel to give a black background around the input area
		JPanel inputPanel = new JPanel();
		inputPanel.setBackground(Color.RED); // Set red background to match the main window
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setPreferredSize(new Dimension(300, 50)); // Set the size for the panel
		inputPanel.add(inputArea, BorderLayout.CENTER);

		// Create a JPanel for Submit button and place it in the center
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10)); // This will center the button
		JButton submitButton = new JButton("Submit");
		submitButton.setPreferredSize(new Dimension(100, 40)); // Set a specific size for the button
		submitButton.setBackground(Color.BLUE); // Set button background to yellow
		submitButton.setForeground(Color.WHITE); // Set button text color to black
		buttonPanel.add(submitButton);

		// Create a JPanel to hold both the inputPanel and the buttonPanel and center
		// them
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout()); // Use BorderLayout for proper alignment
		centerPanel.setBackground(Color.RED); // Match background color of the main panel

		// Add the inputPanel and button to the centerPanel
		centerPanel.add(inputPanel, BorderLayout.NORTH); // Add input panel to the top
		centerPanel.add(buttonPanel, BorderLayout.CENTER); // Add button panel to the center

		// Add the centerPanel to the main panel
		panel.add(centerPanel, BorderLayout.SOUTH); // Use SOUTH so it stays at the bottom of the window

		// Add the panel to the frame
		frame.add(panel);

		// Set the frame visible
		frame.setVisible(true);

		int i = 0;

		// Action listener for the submit button
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				submitQuery(inputArea, chatArea, i);

			}

		}

		);

		// KeyListener to submit the query when the Enter key is pressed
		inputArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// Submit query when Enter key is pressed
					boolean flag = true;
					submitQuery(inputArea, chatArea, i);
					inputArea.setText(""); // Clear the input area after submission
					flag = false;
				}
			}
		});
		
		String processName = ManagementFactory.getRuntimeMXBean().getName();
        String pid = processName.split("@")[0];               
        subProcesses.add(Integer.parseInt(pid));
	}

	// Method to handle the query submission
	private static void submitQuery(JTextArea inputArea, JTextArea chatArea, int i) {
		String userInput = inputArea.getText().trim();

		Random rand = new Random();

		// Generate random integers in range 0 to 999
		int rand_int1 = rand.nextInt(2);

		if (!userInput.isEmpty() && !userInput.equals("Submit your query here")) {
			// Append the user input to the chat area
			String responseMessage = performAction(userInput);
			chatArea.append("Task: " + userInput + "\n");

			// Append the response with black color (text color for the response is now
			// black)
			chatArea.append("Response: ");
			chatArea.setForeground(Color.BLACK); // Set response text to black
			chatArea.append(responseMessage + "\n\n");

			chatArea.setForeground(Color.BLACK);

			// Clear the input area after submission
			inputArea.setText("");
			inputArea.setForeground(Color.BLACK); // Reset text color to black after submit
			inputArea.setText("Submit your query here"); // Restore default text
		} else {
			// If input area is empty or default text, notify the user
			JOptionPane.showMessageDialog(inputArea, "Please enter a valid message!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private static String performAction(String userInput) {
		// You can replace this with your actual task logic
		return QueryProcessor.processQuery(userInput, subProcesses);
	}

	// Method to show the help dialog
	private static void showHelpDialog(JFrame parentFrame) {
		String helpContent = "Help Documentation:\n\n"
				+ "1. To use this application, enter your query in the input box.\n"
				+ "2. After typing your query, click the Submit button or press Enter.\n"
				+ "3. The application will process your task and display the result.\n"
				+ "4. Below are the list of commands to be used to achieve tasks automation.\n" + "   Clear desktop \n"
				+ "   Desktop backup to path c:\\your_backup_folder_name \n" + "   Clean system \n"
				+ "   Close all apps \n" + "   Close idle apps \n"
				+ "   Schedule a reminder at 7:00 am about my breakfast \n"
				+ " ==  Schedule a reminder at 8:00 am about my video call with client \n"
				+ " ==  Schedule a reminder at 3:30 pm about my meeting \n"
				+ "   Schedule a reminder at 5:00 pm tomorrow about client meeting \n"
				+ "   Shutdown or restart or logout or sleep or hibernate \n";

		JTextArea helpTextArea = new JTextArea(helpContent);
		helpTextArea.setEditable(false); // Make the text area non-editable
		helpTextArea.setBackground(Color.WHITE); // Set the background color of help area
		helpTextArea.setFont(new Font("Calibri", Font.BOLD, 17));

		JScrollPane helpScrollPane = new JScrollPane(helpTextArea);
		JOptionPane.showMessageDialog(parentFrame, helpScrollPane, "Help", JOptionPane.INFORMATION_MESSAGE);
	}

	

}
