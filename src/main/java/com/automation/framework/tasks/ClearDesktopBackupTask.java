package com.automation.framework.tasks;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class ClearDesktopBackupTask {

	public static String clearDesktopAndBackup(String query) throws Exception {
		// Ask the user for a backup path
		// Look for the index of the keyword "path" (case-insensitive)
		int index = StringUtils.indexOfIgnoreCase(query, "path");

		// Variable to store the backup path
		String backupPath = "";

		if (index != -1) {
			// Find the substring that comes after the keyword "path"
			String pathPart = query.substring(index + 4).trim(); // Skip the keyword "path" and trim any spaces

			// If the path part is not empty, assign it to backupPath
			if (!pathPart.isEmpty()) {
				backupPath = pathPart;
			}
		}

		// Get the path to the user's desktop
		String userHome = System.getProperty("user.home");
		Path desktopPath = Paths.get(userHome, "Desktop");

		// Check if the desktop path exists
		if (!Files.exists(desktopPath) || !Files.isDirectory(desktopPath)) {
			System.out.println("Desktop path not found.");
			return null;
		}

		// Determine the backup folder location
		Path backupFolder;
		boolean desktopFlag = false;
		if (!backupPath.isEmpty() && backupPath.contains(":\\")) {
			// If backup path is provided, create folder there
			backupFolder = Paths.get(backupPath, "DesktopBackup_" + System.currentTimeMillis());
		} else {
			// If no backup path is provided, create a folder on the desktop
			backupFolder = Paths.get(desktopPath.toString(), "DesktopBackup_" + System.currentTimeMillis());
			desktopFlag = true;
		}

		// Create the backup folder
		try {
			Files.createDirectories(backupFolder);
			// System.out.println("Backup folder created at: " + backupFolder.toString());
		} catch (IOException e) {
			// System.out.println("Error creating backup folder: " + e.getMessage());
			return null;
		}

		// Get all files and directories on the desktop
		try {
			List<Path> filesAndFolders = Files.list(desktopPath).filter(path -> !path.equals(desktopPath)) // Exclude
																											// the
																											// desktop
																											// folder
																											// itself
					.filter(path -> !path.equals(backupFolder)) // Exclude the backup folder
					.collect(Collectors.toList());

			if (filesAndFolders.isEmpty()) {
				System.out.println("No files or folders found on the desktop.");
				return null;
			}

			// Copy each file and folder to the backup location
			for (Path fileOrFolder : filesAndFolders) {
				Path destination = backupFolder.resolve(fileOrFolder.getFileName());
				if (Files.isDirectory(fileOrFolder)) {
					copyDirectory(fileOrFolder, destination);
				} else {
					copyFile(fileOrFolder, destination);
				}
			}

			System.out.println("Backup completed successfully.");

			// Remove files and folders from Desktop (except .exe, .lnk, and the backup
			// folder)
			removeFilesAndFolders(desktopPath, backupFolder); // Exclude the backup folder and files inside it

			// System.out.println("Desktop has been cleared, excluding .exe, .lnk files, and
			// the backup folder.");
			if (desktopFlag) {
				String command = "cmd.exe /c taskkill /f /im explorer.exe && start explorer.exe";
				Process process;
				try {
					process = Runtime.getRuntime().exec(command);
					process.waitFor();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("Error during backup process: " + e.getMessage());
			throw e;
		}
		return "success";
	}

	// Method to copy a directory and its contents
	private static void copyDirectory(Path source, Path destination) throws IOException {
		// If the destination exists, skip copying
		if (Files.exists(destination)) {
			// System.out.println("Skipping existing folder: " + destination.getFileName());
			return;
		}

		// Copy the entire directory
		Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Path targetDir = destination.resolve(source.relativize(dir));
				if (!Files.exists(targetDir)) {
					Files.createDirectories(targetDir);
					System.out.println("Creating directory: " + targetDir);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				// Exclude .exe and .lnk files
				if (shouldExcludeFile(file)) {
					System.out.println("Skipping excluded file: " + file);
					return FileVisitResult.CONTINUE;
				}

				// Copy the file to the backup location
				Path targetFile = destination.resolve(source.relativize(file));
				Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Copying file: " + file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
	}

	// Method to copy a file to the backup folder
	private static void copyFile(Path source, Path destination) throws IOException {
		// Exclude .exe and .lnk files
		if (shouldExcludeFile(source)) {
			// System.out.println("Skipping excluded file: " + source);
			return;
		}

		// Copy the file to the backup location
		Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
		// System.out.println("Copying file: " + source);
	}

	// Method to determine if a file should be excluded (e.g., .exe, .lnk)
	private static boolean shouldExcludeFile(Path file) {
		String fileName = file.getFileName().toString().toLowerCase();
		return fileName.endsWith(".exe") || fileName.endsWith(".lnk");
	}

	// Method to remove files and folders from Desktop (except .exe, .lnk, and the
	// backup folder)
	private static void removeFilesAndFolders(Path desktopPath, Path backupFolder) throws IOException {
		try (Stream<Path> paths = Files.walk(desktopPath)) {
			paths.filter(Files::exists).filter(path -> !path.equals(desktopPath)) // Exclude the desktop folder itself
					.filter(path -> !path.equals(backupFolder)) // Exclude the backup folder
					.filter(path -> !isInsideBackupFolder(path, backupFolder)) // Exclude files inside the backup folder
					.filter(path -> !shouldExcludeFile(path)) // Exclude .exe and .lnk files
					.forEach(path -> {
						try {
							if (Files.isDirectory(path)) {
								// Recursively delete folder and its contents
								deleteDirectory(path);
								System.out.println("Deleted folder: " + path);
							} else {
								// Delete individual file
								Files.delete(path);
								System.out.println("Deleted file: " + path);
							}
						} catch (IOException e) {
							System.out.println("Error deleting file/folder: " + path + " - " + e.getMessage());
						}
					});
		}
	}

	// Method to check if a file is inside the backup folder
	private static boolean isInsideBackupFolder(Path path, Path backupFolder) {
		return path.startsWith(backupFolder);
	}

	// Method to delete a directory and its contents
	private static void deleteDirectory(Path path) throws IOException {
		try (Stream<Path> paths = Files.walk(path)) {
			paths.sorted(Comparator.reverseOrder()) // Delete files before directories
					.forEach(p -> {
						try {
							Files.delete(p);
						} catch (IOException e) {
							System.out.println("Error deleting: " + p + " - " + e.getMessage());
						}
					});
		}
	}
}
