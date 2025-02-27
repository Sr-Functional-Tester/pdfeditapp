package com.automation.framework.tasks;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

public class ReduceImageSize {

	public static String reduceSize(String query) throws IOException {
		// String query = "reduce image to size 50KB C:\\Users\\srinu8963\\Downloads\\png\\imagepng2.png";
		double scaleValue = 0.16;
		String inputFilePath = "";
		String outputFilePath = "";
		int width = 0;
		int height = 0;
		String targetSize = null;

		// Remove the "resize image" part from the query, if it exists
		query = query.replace("reduce image", "").trim();

		// Check for keyword "to pixels" in the query to handle pixel resizing
		if (query.contains("to pixels")) {
			// Split using "to pixels"
			String[] str = query.split("to pixels");
			String strAfterPixels = str[1].trim();
			String parts[] = strAfterPixels.split(" ");
			inputFilePath = parts[1].trim(); // The part before "to pixels" is the input file path
			String dimensions = parts[0].trim(); // The part after "to pixels" is the dimension (e.g., 1080x1080)

			// Split dimensions by "x" to get width and height
			String[] dims = dimensions.split("x");
			if (dims.length == 2) {
				try {
					width = Integer.parseInt(dims[0]);
					height = Integer.parseInt(dims[1]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		// Check for keyword "to size" in the query to handle size-based resizing
		else if (query.contains("to size")) {
			// Split using "to size"
			String[] str = query.split("to size");
			String strAfterPixels = str[1].trim();
			String parts[] = strAfterPixels.split(" ");
			inputFilePath = parts[1].trim(); // The part before "to size" is the input file path
			targetSize = parts[0].trim(); // The part after "to size" is the target size (e.g., 40KB)
		}

		// Generate the output file path based on the input file name
		String fileExtension = null;
		// String outputFilePathPngTemp=null;
		boolean flag = false;
		if (!inputFilePath.isEmpty()) {
			int targetKBSize = convertSizeToKB(targetSize);

			if (targetKBSize >= 50 && targetKBSize <= 99)
				scaleValue = 0.16;
			else if (targetKBSize >= 100 && targetKBSize <= 199)
				scaleValue = 0.2;
			else if (targetKBSize >= 200)
				scaleValue = 0.3;

			File inputFile = new File(inputFilePath);
			String fileName = inputFile.getName();
			String fileBaseName = fileName.substring(0, fileName.lastIndexOf('.'));
			fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
			outputFilePath = inputFile.getParent() + "\\" + fileBaseName + "_output." + fileExtension;
			if (inputFilePath.contains(".png")) {
				inputFilePath = ImageConversion.convertPngToJpg(inputFilePath,
						inputFile.getParent() + "\\" + fileBaseName + ".jpg");
				outputFilePath = inputFile.getParent() + "\\" + fileBaseName + "_output.jpg";
				fileExtension = "jpg";
				flag = true;
			}

		}

		try {
			if (targetSize != null) {
				resizeToTargetFileSize(inputFilePath, outputFilePath, targetSize, fileExtension);
			} else {
				resizeToPixelDimensions(inputFilePath, outputFilePath, width, height, fileExtension);
			}
			if (flag) {
				File inputFile = new File(inputFilePath);
				String fileName = inputFile.getName();
				String fileBaseName = fileName.substring(0, fileName.lastIndexOf('.'));
				ImageConversion.convertJpgToPngSpecificSize(outputFilePath,
						inputFile.getParent() + "\\" + fileBaseName + "_output.png", scaleValue);
				inputFile.delete();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";

	}

	// Method to resize image to a given pixel size (width x height)
	public static void resizeToPixelDimensions(String inputFilePath, String outputFilePath, int width, int height,
			String fileExtension) throws IOException {
		Thumbnails.of(new File(inputFilePath)).size(width, height) // Resize to user-provided dimensions
				.outputQuality(0.8) // Maintain a good quality while resizing
				.toFile(new File(outputFilePath));
		// System.out.println("Image resized to " + width + "x" + height + " pixels
		// successfully!");
	}

	// Method to resize image to a target file size (KB or MB)
	public static void resizeToTargetFileSize(String inputFilePath, String outputFilePath, String targetSize,
			String fileExtension) throws IOException {
		File inputFile = new File(inputFilePath);
		// System.out.println(inputFile.length());
		long inputFileSizeKB = inputFile.length() / 1024; // File size in KB

		// Convert target size input into KB
		int targetFileSizeKB = convertSizeToKB(targetSize);

		// If the file is already smaller than the target, no need to resize
		if (inputFileSizeKB <= targetFileSizeKB) {
//            System.out.println("Input file is already smaller than the target size.");
//            Files.copy(inputFile.toPath(), Paths.get(outputFilePath));
//            return;
			inputFileSizeKB = targetFileSizeKB;
		}

		// Start with an initial quality value
		float quality = 1.0f; // 100% quality initially
		float qualityStep = 0.05f; // Decrease quality in steps of 5%

		// Try resizing and compressing until the file size is close to the target size
		int currentFileSizeKB = (int) inputFileSizeKB;
		int maxIterations = 50; // Set a maximum number of iterations to avoid infinite loop
		int iteration = 0;

		// Convert to JPEG for better compression if the input is PNG or other formats
		File tempJpegFile = new File(outputFilePath);

		while (currentFileSizeKB >= targetFileSizeKB && quality > 0.025f && iteration < maxIterations) {
			// Convert image to JPEG and apply current quality and resolution settings
			Thumbnails.of(inputFile).size(1080, 1080) // Resize to 1080x1080 to keep reasonable resolution
					.outputQuality(quality) // Set the current quality
					.outputFormat(fileExtension) // Convert to JPEG
					.toFile(tempJpegFile); // Save to output file path

			File resizedFile = tempJpegFile;
			currentFileSizeKB = (int) (resizedFile.length() / 1024); // Get the current file size in KB
			quality -= qualityStep; // Reduce quality to further compress the image

			if (currentFileSizeKB <= targetFileSizeKB)
				break;
			System.out.println("Trying with quality " + quality + ": Current file size " + currentFileSizeKB + " KB");

			iteration++;
		}

		if (currentFileSizeKB <= targetFileSizeKB) {
			// System.out.println("Successfully resized to target file size " +
			// targetFileSizeKB + " KB.");
		} else {
			// System.out.println("Could not achieve target file size within acceptable
			// quality.");
		}
	}

	// Helper method to convert size in KB or MB to KB
	public static int convertSizeToKB(String size) {
		int targetFileSizeKB = 0;
		if (size.toUpperCase().endsWith("KB")) {
			targetFileSizeKB = Integer.parseInt(size.replace("KB", "").trim());
		} else if (size.toUpperCase().endsWith("MB")) {
			targetFileSizeKB = Integer.parseInt(size.replace("MB", "").trim()) * 1024;
		} else {
			// System.out.println("Invalid size format. Please specify either KB or MB.");
		}
		return targetFileSizeKB;
	}

}
