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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.monitoring.quartz.job;

import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 * All Quartz Job Bean super classes in monitoring. It provides a simple helper method to 
 * get monitoringService.
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public abstract class MonitoringJob extends QuartzJobBean{
	private static final String CONTEXT_NAME = "monitoringApplicationContext";
	private static final String SERVICE_NAME = "monitoringService";
	
	protected IMonitoringService getMonitoringService(JobExecutionContext context) throws JobExecutionException{
		try {
			SchedulerContext  sc = context.getScheduler().getContext();
			ApplicationContext cxt = (ApplicationContext) sc.get(CONTEXT_NAME);
			return (IMonitoringService) cxt.getBean(SERVICE_NAME);
		} catch (SchedulerException e) {
			throw new JobExecutionException("Failed look up the Scheduler" + e.toString());
		}
	}
}
