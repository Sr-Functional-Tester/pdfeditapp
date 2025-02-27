package com.automation.framework.tasks;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import net.coobird.thumbnailator.Thumbnails;

public class ImageConversion {

	public static String convertImage(String query) {
		try {
			// Convert PNG to JPG
			// String query = "convert image from png to jpg
			// C:\\Users\\srinu8963\\Downloads\\png\\imagepng3.png";
			query = StringUtils.trim(query);

			// Check if query contains "png to jpg" or "jpg to png"
			String inputCheck = null;
			if (StringUtils.containsIgnoreCase(query, "png to jpg")) {
				inputCheck = "png to jpg";
			} else if (StringUtils.containsIgnoreCase(query, "jpg to png")) {
				inputCheck = "jpg to png";
			}
			// String inputCheck = "png to jpg"; // ( it can be "jpg to png")
			// String inputFilePath =
			// "C:\\\\Users\\\\srinu8963\\\\Downloads\\\\png\\\\image.png";
			String inputFilePath = StringUtils.substringAfter(query, inputCheck);
			inputFilePath = StringUtils.trim(inputFilePath);
			String outputFilePath = null;
			int lastBackslashIndex = inputFilePath.lastIndexOf("\\");
			String directory = inputFilePath.substring(0, lastBackslashIndex);
			String fileName = inputFilePath.substring(lastBackslashIndex + 1);
			String str[] = fileName.split("\\.");
			fileName = str[0];
			if (inputCheck.equalsIgnoreCase("png to jpg")) {

				outputFilePath = directory + "\\" + fileName + ".jpg";
				convertPngToJpg(inputFilePath, outputFilePath);
			} else {
				outputFilePath = directory + "\\" + fileName + ".png";
				convertJpgToPng(inputFilePath, outputFilePath);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}

	// Method to convert PNG to JPG
	static String convertPngToJpg(String inputPngPath, String outputJpgPath) throws IOException {
		File inputFile = new File(inputPngPath);
		BufferedImage bufferedImage = ImageIO.read(inputFile);

		// Create a new BufferedImage without transparency (using TYPE_INT_RGB)
		BufferedImage outputImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		// Create a graphics object to paint the image onto the new buffered image
		Graphics2D g2d = outputImage.createGraphics();

		// Set the background color to white and fill the image with it (removes
		// transparency)
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

		// Draw the original image over the white background
		g2d.drawImage(bufferedImage, 0, 0, null);
		g2d.dispose();

		// Create an output file for JPG
		File outputFile = new File(outputJpgPath);

		// Write the image as JPG (using JPEG format)
		ImageIO.write(outputImage, "jpg", outputFile);
		return outputFile.toString();
	}

	// Method to convert JPG to PNG
	static void convertJpgToPng(String inputJpgPath, String outputPngPath) throws IOException {
		File inputFile = new File(inputJpgPath);
		BufferedImage bufferedImage = ImageIO.read(inputFile);

		// Create a new BufferedImage with transparency support for PNG (RGBA)
		BufferedImage outputImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		// Create a graphics object to draw the JPG image onto the new PNG image
		Graphics2D g2d = outputImage.createGraphics();

		// Set the background to transparent (if you want the PNG to have transparency)
		g2d.setComposite(AlphaComposite.SrcOver);
		g2d.drawImage(bufferedImage, 0, 0, null);
		g2d.dispose();

		// Create an output file for PNG
		File outputFile = new File(outputPngPath);

		// Write the image as PNG (using PNG format)
		ImageIO.write(outputImage, "png", outputFile);
		// System.out.println("Converted JPG to PNG successfully.");
	}

	public static void convertJpgToPngSpecificSize(String inputFilePath, String outputFilePath, double scaleValue)
			throws IOException {
		// Convert JPG to PNG using Thumbnailator with resizing (scaling)
		Thumbnails.of(inputFilePath).scale(scaleValue) // No scaling (1.0 means 100% of the original size)
				.outputFormat("png") // Convert to PNG format
				.toFile(new File(outputFilePath)); // Save the result to a file
		// System.out.println("Image successfully converted to PNG.");
	}

}
