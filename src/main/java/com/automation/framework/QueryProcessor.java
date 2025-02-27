package com.automation.framework;

import java.util.List;

import com.automation.framework.tasks.AutoCloseIdleApps;
import com.automation.framework.tasks.CleanSystem;
import com.automation.framework.tasks.ClearDesktopBackupTask;
import com.automation.framework.tasks.DynamicReminderApp;
import com.automation.framework.tasks.HibernateSystem;
import com.automation.framework.tasks.ImageConversion;
import com.automation.framework.tasks.ImageToText;
import com.automation.framework.tasks.KillUserApplicationsWindows;
import com.automation.framework.tasks.LockSystem;
import com.automation.framework.tasks.LogoutUser;
import com.automation.framework.tasks.PDFMerger;
import com.automation.framework.tasks.ReduceImageSize;
import com.automation.framework.tasks.RestartSystem;
import com.automation.framework.tasks.ShutdownSystem;
import com.automation.framework.tasks.SleepMachine;
import com.automation.framework.tasks.StartChrome;
import com.automation.framework.tasks.StartEdge;
import com.automation.framework.tasks.WordToPdfConverter;
import com.automation.framework.tasks.YouTubeSearchAndPlay;
import com.automation.framework.tasks.searchdocs.LuceneIndexerSearch;

public class QueryProcessor {
	static String status = null;

	public static String processQuery(String query, List<Integer> subProcesses) {

		try {
			if (query.toLowerCase().contains("clear desktop") || query.toLowerCase().contains("desktop backup to path")) {

				status = ClearDesktopBackupTask.clearDesktopAndBackup(query);

			} else if (query.toLowerCase().contains("clean system")
			        || query.toLowerCase().contains("clean temp files")
			        || query.toLowerCase().contains("clean tmp files")) {
				status = CleanSystem.cleanSystem();
				
			} else if (query.toLowerCase().contains("close idle app")
			        || query.toLowerCase().contains("close unused app")) {
				status = AutoCloseIdleApps.closeAllIdleApplications();
			}
			
			else if (query.toLowerCase().contains("close all apps")
			        || query.toLowerCase().contains("close apps")) {
				status = KillUserApplicationsWindows.closeAllApplications();
			}
			
			else if (query.toLowerCase().contains("shutdown")
			        || query.toLowerCase().contains("turn off")) {
				status = ShutdownSystem.shutDownSystem();
			}
			
			else if (query.toLowerCase().contains("restart")
					|| query.toLowerCase().contains("restart machine")) {
				status = RestartSystem.restartSystem();
			}
			
			else if (query.toLowerCase().contains("logout")
			        || query.toLowerCase().contains("log off")) {
				status = LogoutUser.logoutSystem();
			}
			
			else if (query.toLowerCase().contains("lock")) {
				status = LockSystem.lockScreen();
			}
			
			else if (query.toLowerCase().contains("hibernate")) {
				status = HibernateSystem.hibernateSystem();
			}
			
			else if (query.toLowerCase().contains("sleep")) {
				status = SleepMachine.sleepSystem();
			}
			
			else if (query.toLowerCase().contains("start chrome browser") ||
					(query.toLowerCase().contains("search chrome"))) {
				status = StartChrome.startChrome(query);
			}
			
			else if (query.toLowerCase().contains("start edge browser") ||
					(query.toLowerCase().contains("search edge"))) {
				status = StartEdge.startEdgeBrowser(query);
			}
			
			else if (query.toLowerCase().contains("schedule")
					&& (query.toLowerCase().contains("reminder"))) {
				status = DynamicReminderApp.scheduleReminder(query, subProcesses);
			}
			
			else if (query.toLowerCase().contains("convert to pdf")) {
				status = WordToPdfConverter.convertDocxToPdf(query);
			}
			
			else if (query.toLowerCase().contains("merge pdf files into")) {
				status = PDFMerger.mergePdfs(query);
			}
			
			else if (query.toLowerCase().contains("image to text")) {
				status = ImageToText.imageToText(query);
			}
			
			else if (query.toLowerCase().contains("search folder") &&
					(query.toLowerCase().contains("with keyword"))) {
				status = LuceneIndexerSearch.luceneIndexSearch(query);
				
			}
			
			else if (query.toLowerCase().contains("reduce image")) {
				status = ReduceImageSize.reduceSize(query);
			}
			
			else if (query.toLowerCase().contains("convert image")) {
				status = ImageConversion.convertImage(query);
			}
			
			else if (query.toLowerCase().contains("play youtube video for")) {
				status = YouTubeSearchAndPlay.playYoutubeVideo(query, subProcesses);
			}
			
			else {
				status= "Query not recognized. please refer help document for valid queries";
			}

		} catch (Exception e) {
			status = "Error, please give proper command";
		}
		return status;
	}

}
