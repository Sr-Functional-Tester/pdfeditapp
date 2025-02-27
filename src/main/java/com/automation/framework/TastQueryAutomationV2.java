package com.automation.framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.automation.framework.tasks.ProcessLister;

public class TastQueryAutomationV2 {
    public static List<Integer> subProcesses = new ArrayList<Integer>();

    public static void main(String[] args) throws Exception {
        // Create the main frame for the chat window
        JFrame frame = new JFrame("Submit Query for your task automation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Add a window listener to detect when the application window is closed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Main application is closing...");
                // Clean up all child processes before exiting
                try {
                    ProcessLister.terminateAllSubProcesses(subProcesses);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                // Exit the application
                System.exit(0);
            }
        });

        ImageIcon icon = new ImageIcon(TastQueryAutomationV2.class.getResource("/3tasks.png"));
        frame.setIconImage(icon.getImage());

        // Create the menu bar with a Help menu
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpMenuItem = new JMenuItem("Help Documentation");
        helpMenuItem.addActionListener(e -> showHelpDialog(frame));
        helpMenu.add(helpMenuItem);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        // Create a panel for the chat window layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Create a JTextArea for displaying the chat conversation and typing the query
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(Color.WHITE);
        chatArea.setForeground(Color.BLACK);
        chatArea.setFont(new Font("Calibri", Font.BOLD, 17));

        // Scroll panel for the chatArea
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a JTextArea for user input
        JTextArea inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBackground(Color.WHITE);
        inputArea.setForeground(Color.BLACK);
        inputArea.setPreferredSize(new Dimension(600, 100)); // Increase height and width for better visibility
        inputArea.setText("Submit your query here");
        inputArea.setForeground(Color.BLACK);
        inputArea.setFont(new Font("Calibri", Font.BOLD, 17));

        // Focus and mouse listeners to manage default text clearing
        inputArea.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (inputArea.getText().equals("Submit your query here")) {
                    inputArea.setText(""); 
                    inputArea.setForeground(Color.BLACK); 
                }
            }
            public void focusLost(FocusEvent evt) {
                if (inputArea.getText().isEmpty()) {
                    inputArea.setText("Submit your query here");
                    inputArea.setForeground(Color.BLACK);
                }
            }
        });

        // Panel for the input area and the submit button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setPreferredSize(new Dimension(600, 120));  // Increased size for better visibility
        inputPanel.add(inputArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(100, 40));
        submitButton.setBackground(Color.BLUE);
        submitButton.setForeground(Color.WHITE);
        buttonPanel.add(submitButton);

        inputPanel.add(buttonPanel, BorderLayout.SOUTH); // Place button below inputArea

        // Panel for progress bar and message
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        JLabel progressLabel = new JLabel("Processing... Please wait.");
        progressLabel.setVisible(false); // Hide initially
        progressPanel.add(progressLabel, BorderLayout.NORTH);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Set progress bar to indeterminate mode
        progressBar.setVisible(false); // Hide initially
        progressPanel.add(progressBar, BorderLayout.CENTER);

        // Main panel layout to hold chat area, input area, and progress panel
        panel.add(progressPanel, BorderLayout.NORTH); // Put progress at the top
        panel.add(inputPanel, BorderLayout.SOUTH); // Put input and submit button at the bottom

        frame.add(panel);
        frame.setVisible(true);

        submitButton.addActionListener(e -> {
            String userInput = inputArea.getText().trim();
            if (!userInput.isEmpty() && !userInput.equals("Submit your query here")) {
                // Show progress while the query is being processed
                progressLabel.setVisible(true);
                progressBar.setVisible(true);
                submitButton.setEnabled(false); // Disable the button while processing

                // Use SwingWorker for background task
                SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() {
                        // Simulate a long-running task (e.g., processing query)
                        return performAction(userInput);
                    }

                    @Override
                    protected void done() {
                        try {
                            String result = get(); // Get the result from background task
                            // Append result to chat area
                            chatArea.append("Task: " + userInput + "\n");
                            chatArea.append("Response: " + result + "\n\n");
                            inputArea.setText(""); 
                            // Hide progress components
                            progressLabel.setVisible(false);
                            progressBar.setVisible(false);
                            submitButton.setEnabled(true); // Re-enable the submit button
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                worker.execute(); // Start the background task
            } else {
                JOptionPane.showMessageDialog(inputArea, "Please enter a valid message!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Method to handle the query submission
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
				+ "4. Below are the list of commands to be used to achieve tasks automation.\n" 
				+ "   Clear desktop \n"
				+ "   Desktop backup to path c:\\your_backup_folder_name \n" 
				+ "   Clean system \n"
				+ "   Close all apps \n" 
				+ "   Close idle apps \n"
				+ "   Schedule a reminder at 7:00 am about my breakfast \n"
				+ "   Schedule a reminder at 8:00 am about my video call with client \n"
				+ "   Schedule a reminder at 3:30 pm about my meeting \n"
				+ "   Schedule a reminder at 5:00 pm tomorrow about client meeting \n"
				+ "   Shutdown or restart or logout or sleep or hibernate \n"
		        + "   convert to pdf C:\\Documents\\sample.docx \n"
		        + "   merge pdf files into D:\\Documents\\output.pdf from C:\\Documents\\sample1.pdf, C:\\Documents\\sample2.pdf \n"
		        + "   image to text C:\\Documents\\image.png \n"
		        + "   search folder C:\\Documents\\folder with keyword hello world \n"
				+ "   reduce image to size 50KB C:\\Documents\\image.jpg (or you can give .png file as well) \n"
		        + "   convert image from png to jpg C:\\Documents\\image.png \n"
		        + "   convert image from jpg to png C:\\Documents\\image.jpg \n"
		        + "   play youtube video for relaxing music \n"		        
		        ;
		        
		//helpContent=null;
        JTextArea helpTextArea = new JTextArea(helpContent);
        helpTextArea.setEditable(false);
        helpTextArea.setBackground(Color.WHITE);
        helpTextArea.setFont(new Font("Calibri", Font.BOLD, 17));

        JScrollPane helpScrollPane = new JScrollPane(helpTextArea);
        JOptionPane.showMessageDialog(parentFrame, helpScrollPane, "Help", JOptionPane.INFORMATION_MESSAGE);
    }
}
