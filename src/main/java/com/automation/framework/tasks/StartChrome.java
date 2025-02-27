package com.automation.framework.tasks;

public class StartChrome {
	public static String startChrome(String query) {
		try {
			String[] search=null;
			ProcessBuilder processBuilder=null;
			if(!query.contains("search chrome")) {
				search = query.split("start chrome browser", 2);
				if(search[1].isEmpty()) {
					processBuilder = new ProcessBuilder("cmd.exe", "/c", "start chrome.exe");
					
				}else {
					String searchQuery = search[1].trim().replace(" ", "+");
					processBuilder = new ProcessBuilder("cmd.exe", "/c", "start chrome.exe "+searchQuery.trim());
				}
			}
			else {
				search = query.split("search chrome", 2);
				if(search[1].isEmpty()) {
					processBuilder = new ProcessBuilder("cmd.exe", "/c", "start chrome.exe");
					
				}else {
					String searchQuery = search[1].trim().replace(" ", "+");
					processBuilder = new ProcessBuilder("cmd.exe", "/c", "start chrome.exe "+searchQuery.trim());
				}			
			}

			processBuilder.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
}
