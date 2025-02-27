package com.automation.framework.tasks;

public class StartEdge {
	public static String startEdgeBrowser(String query) {
		try {
			String[] search=null;
			ProcessBuilder processBuilder=null;
			if(!query.contains("search edge")) {
				search = query.split("start edge browser", 2);
				if(search[1].isEmpty()) {
					processBuilder = new ProcessBuilder("cmd.exe", "/c", "start msedge.exe");
					
				}else {
					String searchQuery = search[1].trim().replace(" ", "+");
					processBuilder = new ProcessBuilder("cmd.exe", "/c", "start msedge.exe "+searchQuery.trim());
				}
			}
			else {
				search = query.split("search edge", 2);
				if(search[1].isEmpty()) {
					processBuilder = new ProcessBuilder("cmd.exe", "/c", "start msedge.exe");
					
				}else {
					String searchQuery = search[1].trim().replace(" ", "+");
					processBuilder = new ProcessBuilder("cmd.exe", "/c", "start msedge.exe "+searchQuery.trim());
				}			
			}

			processBuilder.start();

		}catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
}
