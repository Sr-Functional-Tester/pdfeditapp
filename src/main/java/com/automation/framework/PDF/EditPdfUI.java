package com.automation.framework.PDF;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditPdfUI {
	public static void main(String[] args) {
		// Create frame
		JFrame frame = new JFrame("Edit Pdf Files");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(488, 330); // Set frame size
		// frame.set

		// Create a panel to display the background image
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Load and draw the background image
				ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/background.jpg")); // Path to image
				Image img = backgroundImage.getImage();
				g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // Stretch image to fill the entire panel
			}
		};

		// Set the panel as the content pane and use null layout for absolute
		// positioning of components
		frame.setContentPane(panel);
		panel.setLayout(null); // Use null layout to manually position components

		ImageIcon icon = new ImageIcon(EditPdfUI.class.getResource("/3tasks.png"));
		frame.setIconImage(icon.getImage());
		// Make the panel background transparent
		panel.setOpaque(false);

//        panel.setMaximumSize(new Dimension(400,200));
//        panel.setSize(new Dimension(400,200));
//        panel.setAutoscrolls(false);

		JLabel messageLabel = new JLabel();
		messageLabel.setBounds(90, 230, 340, 25);
		messageLabel.setOpaque(false); // Make the label opaque
		messageLabel.setBackground(Color.WHITE); // Set background color (optional)
		messageLabel.setVisible(false);
		panel.add(messageLabel);

		// messageLabel.setOpaque(false);
		panel.add(messageLabel);
		// Create checkboxes
		JCheckBox checkbox1 = new JCheckBox("convert file to pdf");
		// checkbox1.setToolTipText("<html>Select this option to clear the
		// desktop.<br>Backup files to the specified path.</html>");
		JCheckBox checkbox2 = new JCheckBox("add new page");
		// checkbox2.setToolTipText("<html>Select this option to back up a
		// folder.<br>Choose a folder to back up.</html>");
		JCheckBox checkbox3 = new JCheckBox("delete page");

		JCheckBox checkbox4 = new JCheckBox("merge pdf files");

		JCheckBox checkbox5 = new JCheckBox("replace text");

		JCheckBox checkbox6 = new JCheckBox("add image water mark");

		JCheckBox checkbox7 = new JCheckBox("add annotation links");

		JCheckBox checkbox8 = new JCheckBox("add image at specified");
		// checkbox3.setToolTipText("<html>Select this option to organize your
		// files.<br>Choose folder to backup (default is desktop folder)</html>");
		// Set checkbox positions using setBounds(x, y, width, height)
		checkbox1.setBounds(85, 50, 145, 20);
		checkbox2.setBounds(85, 70, 145, 20);
		checkbox3.setBounds(85, 90, 145, 20);
		checkbox4.setBounds(85, 110, 145, 20);

		checkbox5.setBounds(270, 50, 145, 20);
		checkbox6.setBounds(270, 70, 170, 20);
		checkbox7.setBounds(270, 90, 145, 20);
		checkbox8.setBounds(270, 110, 250, 20);

		// Set transparency for checkboxes and change text color for visibility
		checkbox1.setOpaque(false);
		checkbox2.setOpaque(false);
		checkbox3.setOpaque(false);
		checkbox4.setOpaque(false);
		checkbox5.setOpaque(false);
		checkbox6.setOpaque(false);
		checkbox7.setOpaque(false);
		checkbox8.setOpaque(false);

		checkbox1.setForeground(Color.GREEN);
		checkbox2.setForeground(Color.GREEN);
		checkbox3.setForeground(Color.GREEN);

		checkbox4.setForeground(Color.GREEN);
		checkbox5.setForeground(Color.GREEN);
		checkbox6.setForeground(Color.GREEN);

		checkbox7.setForeground(Color.GREEN);
		checkbox8.setForeground(Color.GREEN);

		// Add checkboxes to the panel
		panel.add(checkbox1);
		panel.add(checkbox2);
		panel.add(checkbox3);

		panel.add(checkbox4);
		panel.add(checkbox5);
		panel.add(checkbox6);

		panel.add(checkbox7);
		panel.add(checkbox8);

		// Create a text field (initially disabled)
		JTextField textField = new JTextField(15);
		textField.setEnabled(false);
		textField.setVisible(false);
		textField.setText("    Enter file path. eg: c:\\sample.docx");
		textField.setForeground(Color.WHITE); // Set text color to white
		textField.setBounds(128, 160, 224, 25); // Position the text field

		// Make the text field background transparent and set border
		textField.setOpaque(false);
		textField.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Add a white border

		// Add the text field to the panel
		panel.add(textField);

		// Create a submit button (initially disabled)
		JButton submitButton = new JButton("Submit");
		submitButton.setVisible(false);
		submitButton.setEnabled(false); // Initially disable the submit button
		submitButton.setBounds(180, 200, 100, 25); // Position the submit button

		// Make the submit button background transparent, add border, and change text
		// color
		submitButton.setOpaque(false);
		submitButton.setContentAreaFilled(false);
		submitButton.setBorderPainted(true);
		submitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Add a white border
		submitButton.setForeground(Color.GREEN); // Set button text color to white

		// Add the submit button to the panel
		panel.add(submitButton);

		// Group checkboxes to allow only one selection
//        checkbox1.addActionListener(e -> {
//            checkbox2.setSelected(false);
//            checkbox3.setSelected(false);
//            textField.setEnabled(true);
//            textField.setVisible(true);
//            textField.setText("    Enter file path. eg: c:\\sample.docx");
//            textField.setForeground(Color.WHITE);  // Ensure text is white
//            submitButton.setEnabled(false); // Enable the submit button when option 1 is selected
//            submitButton.setVisible(true);
//        });

		checkbox1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// If checkbox1 is selected
				if (e.getStateChange() == ItemEvent.SELECTED) {
					checkbox2.setSelected(false); // Unselect other checkboxes
					checkbox3.setSelected(false);
					checkbox4.setSelected(false);
					checkbox5.setSelected(false);
					checkbox6.setSelected(false);
					checkbox7.setSelected(false);
					checkbox8.setSelected(false);

					textField.setEnabled(true); // Enable text field
					textField.setVisible(true); // Make text field visible
					textField.setText("    Enter file path. eg: c:\\sample.docx");
					textField.setForeground(Color.WHITE); // Set text color to black
					textField.setBounds(128, 160, 224, 25); // Position the text field

					submitButton.setEnabled(false); // Enable submit button
					submitButton.setVisible(true); // Make submit button visible
				} else { // If checkbox1 is unselected
					textField.setVisible(false); // Hide text field
					submitButton.setVisible(false); // Hide submit button
					messageLabel.setVisible(false);
				}
			}
		});

		checkbox2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// If checkbox1 is selected
				if (e.getStateChange() == ItemEvent.SELECTED) {
					checkbox1.setSelected(false); // Unselect other checkboxes
					checkbox3.setSelected(false);
					checkbox4.setSelected(false);
					checkbox5.setSelected(false);
					checkbox6.setSelected(false);
					checkbox7.setSelected(false);
					checkbox8.setSelected(false);

					textField.setEnabled(true); // Enable text field
					textField.setVisible(true); // Make text field visible
					textField.setText("    Enter file path. eg: c:\\sample.pdf");
					textField.setForeground(Color.WHITE); // Set text color to black
					textField.setBounds(128, 160, 224, 25); // Position the text field

					submitButton.setEnabled(false); // Enable submit button
					submitButton.setVisible(true); // Make submit button visible
				} else { // If checkbox1 is unselected
					textField.setVisible(false); // Hide text field
					submitButton.setVisible(false); // Hide submit button
					messageLabel.setVisible(false);
				}
			}
		});

		checkbox3.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// If checkbox1 is selected
				if (e.getStateChange() == ItemEvent.SELECTED) {
					checkbox1.setSelected(false); // Unselect other checkboxes
					checkbox2.setSelected(false);
					checkbox4.setSelected(false);
					checkbox5.setSelected(false);
					checkbox6.setSelected(false);
					checkbox7.setSelected(false);
					checkbox8.setSelected(false);

					textField.setEnabled(true); // Enable text field
					textField.setVisible(true); // Make text field visible
					textField.setText("Enter Page Number(eg.2) or range (eg.2-8), File Path");
					textField.setBounds(90, 160, 310, 25); // Position the text field
					textField.setForeground(Color.WHITE); // Set text color to black

					submitButton.setEnabled(false); // Enable submit button
					submitButton.setVisible(true); // Make submit button visible
				} else { // If checkbox1 is unselected
					textField.setVisible(false); // Hide text field
					submitButton.setVisible(false); // Hide submit button
					messageLabel.setVisible(false);
				}
			}
		});

		checkbox4.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// If checkbox1 is selected
				if (e.getStateChange() == ItemEvent.SELECTED) {
					checkbox1.setSelected(false); // Unselect other checkboxes
					checkbox2.setSelected(false);
					checkbox3.setSelected(false);
					checkbox5.setSelected(false);
					checkbox6.setSelected(false);
					checkbox7.setSelected(false);
					checkbox8.setSelected(false);

					textField.setEnabled(true); // Enable text field
					textField.setVisible(true); // Make text field visible
					textField.setText("Enter File Paths seperated by comma");
					textField.setForeground(Color.WHITE); // Set text color to black
					textField.setBounds(128, 160, 224, 25); // Position the text field

					submitButton.setEnabled(false); // Enable submit button
					submitButton.setVisible(true); // Make submit button visible
				} else { // If checkbox1 is unselected
					textField.setVisible(false); // Hide text field
					submitButton.setVisible(false); // Hide submit button
					messageLabel.setVisible(false);
				}
			}
		});
		
		
		checkbox5.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// If checkbox1 is selected
				if (e.getStateChange() == ItemEvent.SELECTED) {
					checkbox1.setSelected(false); // Unselect other checkboxes
					checkbox2.setSelected(false);
					checkbox3.setSelected(false);
					checkbox4.setSelected(false);
					checkbox6.setSelected(false);
					checkbox7.setSelected(false);
					checkbox8.setSelected(false);

					textField.setEnabled(true); // Enable text field
					textField.setVisible(true); // Make text field visible
					textField.setText("Enter pageNo, fontSize, findText, ReplaceText, FilePath");
					textField.setBounds(90, 160, 310, 25); // Position the text field
					textField.setForeground(Color.WHITE); // Set text color to black

					submitButton.setEnabled(false); // Enable submit button
					submitButton.setVisible(true); // Make submit button visible
				} else { // If checkbox1 is unselected
					textField.setVisible(false); // Hide text field
					submitButton.setVisible(false); // Hide submit button
					messageLabel.setVisible(false);
				}
			}
		});
		
		checkbox6.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// If checkbox1 is selected
				if (e.getStateChange() == ItemEvent.SELECTED) {
					checkbox1.setSelected(false); // Unselect other checkboxes
					checkbox2.setSelected(false);
					checkbox3.setSelected(false);
					checkbox4.setSelected(false);
					checkbox5.setSelected(false);
					checkbox7.setSelected(false);
					checkbox8.setSelected(false);

					textField.setEnabled(true); // Enable text field
					textField.setVisible(true); // Make text field visible
					textField.setText("Enter pdf file path, image file path");
					textField.setBounds(124, 164, 204, 24); // Position the text field
					textField.setForeground(Color.WHITE); // Set text color to black

					submitButton.setEnabled(false); // Enable submit button
					submitButton.setVisible(true); // Make submit button visible
				} else { // If checkbox1 is unselected
					textField.setVisible(false); // Hide text field
					submitButton.setVisible(false); // Hide submit button
					messageLabel.setVisible(false);
				}
			}
		});
		
		checkbox7.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// If checkbox1 is selected
				if (e.getStateChange() == ItemEvent.SELECTED) {
					checkbox1.setSelected(false); // Unselect other checkboxes
					checkbox2.setSelected(false);
					checkbox3.setSelected(false);
					checkbox4.setSelected(false);
					checkbox5.setSelected(false);
					checkbox6.setSelected(false);
					checkbox8.setSelected(false);

					textField.setEnabled(true); // Enable text field
					textField.setVisible(true); // Make text field visible
					textField.setText("    Enter pdf file path, website link");
					textField.setBounds(124, 164, 204, 24); // Position the text field
					textField.setForeground(Color.WHITE); // Set text color to black

					submitButton.setEnabled(false); // Enable submit button
					submitButton.setVisible(true); // Make submit button visible
				} else { // If checkbox1 is unselected
					textField.setVisible(false); // Hide text field
					submitButton.setVisible(false); // Hide submit button
					messageLabel.setVisible(false);
				}
			}
		});
		
		checkbox8.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// If checkbox1 is selected
				if (e.getStateChange() == ItemEvent.SELECTED) {
					checkbox1.setSelected(false); // Unselect other checkboxes
					checkbox2.setSelected(false);
					checkbox3.setSelected(false);
					checkbox4.setSelected(false);
					checkbox5.setSelected(false);
					checkbox6.setSelected(false);
					checkbox7.setSelected(false);

					textField.setEnabled(true); // Enable text field
					textField.setVisible(true); // Make text field visible
					textField.setText("Enter PageNo, x-position, y-position, image file path, pdf file path");
					textField.setBounds(66, 164, 376, 27); // Position the text field
					textField.setForeground(Color.WHITE); // Set text color to black

					submitButton.setEnabled(false); // Enable submit button
					submitButton.setVisible(true); // Make submit button visible
				} else { // If checkbox1 is unselected
					textField.setVisible(false); // Hide text field
					submitButton.setVisible(false); // Hide submit button
					messageLabel.setVisible(false);
				}
			}
		});

		// Add focus listener to remove the default message when user clicks into the
		// text field
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().contains("Enter")) {
					textField.setText("");
					textField.setForeground(Color.WHITE); // Ensure text is white when user starts typing
				}
			}
		});

		// Add mouse listener to the text field
		textField.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// If the text field contains placeholder text, clear it when clicked
				if (textField.getText().contains("Enter")) {
					textField.setText(""); // Clear the text field
					textField.setForeground(Color.WHITE); // Set text color to white
				}
			}
		});

		// Create a MouseListener to detect clicks on the panel
		panel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// Get the position of the mouse click
				Point clickPoint = e.getPoint();

				// Check if the click was not on the checkboxes or the submit button
				if (textField.getText().isEmpty() && !checkbox1.getBounds().contains(clickPoint)
						&& !checkbox2.getBounds().contains(clickPoint) && 
						!checkbox3.getBounds().contains(clickPoint) &&
						!checkbox4.getBounds().contains(clickPoint) && 
						!checkbox5.getBounds().contains(clickPoint) && 
						!checkbox6.getBounds().contains(clickPoint) &&
						!checkbox7.getBounds().contains(clickPoint) &&
						!checkbox8.getBounds().contains(clickPoint) && 
						!textField.getBounds().contains(clickPoint)
						&& !submitButton.getBounds().contains(clickPoint)) {

					// Trigger event (e.g., set text in the text field)
					if (checkbox1.isSelected())
						textField.setText("    Enter file path. eg: c:\\sample.docx");
					else if (checkbox2.isSelected())
						textField.setText("    Enter file path. eg: c:\\sample.pdf");
					else if (checkbox3.isSelected())
						textField.setText("Enter Page Number(eg.2) or range (eg.2-8), File Path");
					else if (checkbox4.isSelected())
						textField.setText("Enter File Paths seperated by comma");
					else if (checkbox5.isSelected())
						textField.setText("Enter pageNo, fontSize, findText, ReplaceText, FilePath");
					else if (checkbox6.isSelected())
						textField.setText("Enter pdf file path, image file path");
					else if (checkbox7.isSelected())
						textField.setText("    Enter pdf file path, website link");
					else if (checkbox8.isSelected())
						textField.setText("Enter PageNo, x-position, y-position, image file path, pdf file path");
					else
						textField.setText("Enter folder path. eg: c:\\documents");
					textField.setForeground(Color.WHITE); // Ensure the text color is white
					submitButton.setEnabled(false);
				}
			}
		});

		// Add key listener to remove the default message when the user starts typing
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (textField.getText().contains("Enter")) {
					textField.setText("");
					textField.setForeground(Color.WHITE); // Ensure text is white when user starts typing
					submitButton.setForeground(Color.GREEN);

				}
			}
		});

		// Enable the submit button when the user enters data
		textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				enableSubmitButton();
			}

			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				enableSubmitButton();
			}

			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				enableSubmitButton();
			}

			private void enableSubmitButton() {
				if (textField.getText().length() > 0) {
					submitButton.setEnabled(true); // Enable submit button when text is entered
					submitButton.setForeground(Color.GREEN);
					messageLabel.setVisible(false);
				} else {
					submitButton.setEnabled(false); // Keep it disabled if no text is entered
				}
			}
		});

		// Add action listener to submit button
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedOption = "";
				if (checkbox1.isSelected()) {
					try {
						if (textField.getText().contains("Enter"))
							textField.setText("");
						String status = FileSToPdfConverter.convertToPdf(textField.getText());
					} catch (Exception e1) {
						messageLabel.setVisible(true);
						messageLabel.setText("some error occured, please check details provided");
						messageLabel.setForeground(Color.WHITE);
					}
				} else if (checkbox2.isSelected()) {
					try {
						if (textField.getText().contains("Enter"))
							textField.setText("");
						PDFAddNewPage.addPdfPage(0, textField.getText());

					} catch (Exception e1) {
						messageLabel.setVisible(true);
						messageLabel.setText("some error occured, please check details provided");
						messageLabel.setForeground(Color.WHITE);
					}
				} else if (checkbox3.isSelected()) {
					try {
						if (textField.getText().contains("Enter"))
							textField.setText("");
						PDFDeletePage.deletePage(textField.getText());

					} catch (Exception e1) {
						messageLabel.setVisible(true);
						messageLabel.setText("some error occured, please check details provided");
						messageLabel.setForeground(Color.WHITE);
					}
				} else if (checkbox4.isSelected()) {
					try {
						if (textField.getText().contains("Enter"))
							textField.setText("");
						PDFMerger.mergePdfs(textField.getText());

					} catch (Exception e1) {
						messageLabel.setVisible(true);
						messageLabel.setText("some error occured, please check details provided");
						messageLabel.setForeground(Color.WHITE);
					}
				} else if (checkbox5.isSelected()) {
					try {
						if (textField.getText().contains("Enter"))
							textField.setText("");
						PDFReplaceText.changePdf(textField.getText());

					} catch (Exception e1) {
						messageLabel.setVisible(true);
						messageLabel.setText("some error occured, please check details provided");
						messageLabel.setForeground(Color.WHITE);
					}
				} 
				else if (checkbox6.isSelected()) {
					try {
						if (textField.getText().contains("Enter"))
							textField.setText("");
						PDFAddImageWaterMark.addImageBackground(textField.getText());

					} catch (Exception e1) {
						messageLabel.setVisible(true);
						messageLabel.setText("some error occured, please check details provided");
						messageLabel.setForeground(Color.WHITE);
					}
				}
				else if (checkbox7.isSelected()) {
					try {
						if (textField.getText().contains("Enter"))
							textField.setText("");
						PDFAddPDFAnnoationLinks.addAnnotationLinks(textField.getText());

					} catch (Exception e1) {
						messageLabel.setVisible(true);
						messageLabel.setText("some error occured, please check details provided");
						messageLabel.setForeground(Color.WHITE);
					}
				} 
				else if (checkbox8.isSelected()) {
					try {
						if (textField.getText().contains("Enter"))
							textField.setText("");
						PDFAddImageAtSpecifiedLocation.addImageBackground(textField.getText());

					} catch (Exception e1) {
						messageLabel.setVisible(true);
						messageLabel.setText("some error occured, please check details provided");
						messageLabel.setForeground(Color.WHITE);
					}
				} 
				// Print the result to the console
				System.out.println("Selected: " + selectedOption);
			}
		});

		// Make the frame visible
		frame.setVisible(true);
	}

	public boolean isCheckAnyOneCheckBoxSelected(JCheckBox checkbox1, JCheckBox checkbox2, JCheckBox checkbox3,
			JCheckBox checkbox4, JCheckBox checkbox5, JCheckBox checkbox6, JCheckBox checkbox7, JCheckBox checkbox8) {
		if (checkbox1.isSelected() || checkbox2.isSelected() || checkbox3.isSelected() || checkbox4.isSelected()
				|| checkbox5.isSelected() || checkbox6.isSelected() || checkbox7.isSelected() || checkbox8.isSelected())
			return true;
		else
			return false;

	}
}
