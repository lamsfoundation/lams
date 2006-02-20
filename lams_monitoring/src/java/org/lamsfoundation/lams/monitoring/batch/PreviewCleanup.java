package org.lamsfoundation.lams.monitoring.batch;

import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** 
 * Clean up the old preview files. 
 * 
 * Removes all preview lessons and learning designs older than the supplied number of days.
 * 
 * Errors are written to system.error. Results are written to system.out
 * 
 */
public class PreviewCleanup {

	private ApplicationContext appContext;

	public PreviewCleanup() {
		String[] contextPath = new String[] { 
        		"/org/lamsfoundation/lams/applicationContext.xml",
        		"/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
        		"/org/lamsfoundation/lams/tool/toolApplicationContext.xml",
          		"/org/lamsfoundation/lams/learning/learningApplicationContext.xml",        					  
          		"/org/lamsfoundation/lams/authoring/authoringApplicationContext.xml",
          		"/org/lamsfoundation/lams/monitoring/monitoringApplicationContext.xml",
        		"/org/lamsfoundation/lams/tool/survey/applicationContext.xml"};
		
		appContext = new ClassPathXmlApplicationContext(contextPath);
	}

	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		PreviewCleanup pc = new PreviewCleanup();
		pc.cleanup();
		
	}

	private void cleanup() {
		IMonitoringService monitoringService = (IMonitoringService)appContext.getBean("monitoringService");
		
		if ( monitoringService != null ) { 
			int numDeleted = monitoringService.deleteAllOldPreviewLessons();
			System.out.println("PreviewCleanup: "+numDeleted+" preview lessons deleted.");
		} else {
			System.err.println("PreviewCleanup: unable to clean out old sessions as monitoring service cannot be accessed. There is likely to be a problem with the Spring context.");
		}
	}
}
