/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.monitoring.quartz.job;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;

public class StartScheduleLessonJob extends MonitoringJob {
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(StartScheduleLessonJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	IMonitoringService monitoringService = getMonitoringService(context);

	//getting gate id set from scheduler
	Map properties = context.getJobDetail().getJobDataMap();
	long lessonId = ((Long) properties.get(MonitoringConstants.KEY_LESSON_ID)).longValue();
	Integer userId = (Integer) properties.get(MonitoringConstants.KEY_USER_ID);

	if (log.isDebugEnabled()) {
	    log.debug("Lesson [" + lessonId + "] is starting...");
	}

	monitoringService.startLesson(lessonId, userId, true);

	if (log.isDebugEnabled()) {
	    log.debug("Lesson [" + lessonId + "] started");
	}
    }
}