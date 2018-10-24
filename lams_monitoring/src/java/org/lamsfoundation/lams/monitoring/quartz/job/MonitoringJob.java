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

import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * All Quartz Job Bean super classes in monitoring. It provides a simple helper methods to get monitoringService, eventNotificationService.
 * @author Steve.Ni
 */
public abstract class MonitoringJob extends QuartzJobBean {
    private static final String CONTEXT_NAME = "context.central";
    private static final String MONITORING_SERVICE_NAME = "monitoringService";
    private static final String EVENT_SERVICE_NAME = "eventNotificationService";

    protected Object getService(JobExecutionContext context, String serviceName) throws JobExecutionException {
	try {
	    SchedulerContext sc = context.getScheduler().getContext();
	    ApplicationContext cxt = (ApplicationContext) sc.get(MonitoringJob.CONTEXT_NAME);
	    return cxt.getBean(serviceName);
	} catch (SchedulerException e) {
	    throw new JobExecutionException("Failed look up the " + serviceName + " " + e.toString());
	}
    }

    protected IMonitoringFullService getMonitoringService(JobExecutionContext context) throws JobExecutionException {
	return (IMonitoringFullService) getService(context, MonitoringJob.MONITORING_SERVICE_NAME);
    }

    protected IEventNotificationService getEventNotificationService(JobExecutionContext context)
	    throws JobExecutionException {
	return (IEventNotificationService) getService(context, MonitoringJob.EVENT_SERVICE_NAME);
    }

}
