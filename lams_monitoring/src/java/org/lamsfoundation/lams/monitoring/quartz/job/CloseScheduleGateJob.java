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

import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * The Quartz sheduling job that closes the gate. It is configured as a Spring
 * bean and will be triggered by the scheduler to perform its work.
 *
 * @author Jacky Fang
 * @since 2005-4-12
 */
public class CloseScheduleGateJob extends MonitoringJob {

    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(CloseScheduleGateJob.class);

    /**
     * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	IMonitoringFullService monitoringService = getMonitoringService(context);

	//getting gate id set from scheduler
	Map properties = context.getJobDetail().getJobDataMap();
	Long gateId = (Long) properties.get("gateId");

	if (log.isDebugEnabled()) {
	    log.debug("Closing gate......[" + gateId.longValue() + "]");
	}

	monitoringService.closeGate(gateId);

	if (log.isDebugEnabled()) {
	    log.debug("Gate......[" + gateId.longValue() + "] Closed");
	}
    }
}
