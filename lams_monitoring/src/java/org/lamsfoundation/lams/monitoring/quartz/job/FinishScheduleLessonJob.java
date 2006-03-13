/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.monitoring.quartz.job;

import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class FinishScheduleLessonJob extends MonitoringJob{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(FinishScheduleLessonJob.class);
    
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		IMonitoringService monitoringService = getMonitoringService(context);
		
        //getting gate id set from scheduler
        Map properties = context.getJobDetail().getJobDataMap();
        long lessonId = ((Long)properties.get(MonitoringConstants.KEY_LESSON_ID)).longValue();
        
        if(log.isDebugEnabled())
            log.debug("Lesson ["+lessonId+"] is stopping...");
        
		
		monitoringService.finishLesson(lessonId);
        
        if(log.isDebugEnabled())
            log.debug("Lesson ["+lessonId+"] stopped");
	}
}
