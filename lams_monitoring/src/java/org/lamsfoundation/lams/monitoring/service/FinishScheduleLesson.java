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
package org.lamsfoundation.lams.monitoring.service;

import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class FinishScheduleLesson extends QuartzJobBean{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(FinishScheduleLesson.class);
    private IMonitoringService monitoringService;
    
    //---------------------------------------------------------------------
    // Inverse of control - method injection
    //---------------------------------------------------------------------
    /**
     * @param monitoringService The monitoringService to set.
     */
    public void setMonitoringService(IMonitoringService monitoringService)
    {
        this.monitoringService = monitoringService;
    }
    
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //getting gate id set from scheduler
        Map properties = context.getJobDetail().getJobDataMap();
        long lessonId = ((Long)properties.get(MonitoringConstants.KEY_LESSON_ID)).longValue();
        
        if(log.isDebugEnabled())
            log.debug("Lesson ["+lessonId+"] is stopping...");
        
		
		monitoringService.archiveLesson(lessonId);
        
        if(log.isDebugEnabled())
            log.debug("Lesson ["+lessonId+"] stopped");
	}
}
