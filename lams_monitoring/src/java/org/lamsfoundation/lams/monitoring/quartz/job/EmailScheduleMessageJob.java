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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.monitoring.quartz.job;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Email messages at the specified date. List of recipients is being constructed real time based on specified search
 * criterias.
 *
 * @author Andey Balan
 */
public class EmailScheduleMessageJob extends MonitoringJob {
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(EmailScheduleMessageJob.class);

    @SuppressWarnings("rawtypes")
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	IMonitoringFullService monitoringService = getMonitoringService(context);
	IEventNotificationService eventNotificationService = getEventNotificationService(context);

	Map properties = context.getJobDetail().getJobDataMap();
	String emailBody = (String) properties.get("emailBody");

	// get users to whom send emails
	int searchType = (Integer) properties.get("searchType");
	Long lessonId = (Long) properties.get(AttributeNames.PARAM_LESSON_ID);
	Integer orgId = (Integer) properties.get(AttributeNames.PARAM_ORGANISATION_ID);
	Long activityId = (Long) properties.get(AttributeNames.PARAM_ACTIVITY_ID);
	Integer xDaystoFinish = (Integer) properties.get("daysToDeadline");
	String[] lessonIds = (String[]) properties.get("lessonIDs");
	try {
	    HibernateSessionManager.openSession();
	    Collection<User> users = getMonitoringService(context).getUsersByEmailNotificationSearchType(searchType,
		    lessonId, lessonIds, activityId, xDaystoFinish, orgId);
	    Set<Integer> recipients = new HashSet<Integer>();
	    for (User user : users) {
		boolean isHtmlFormat = false;
		int userId = user.getUserId();
		recipients.add(userId);
		log.debug("Sending scheduled email to user [" + userId + "].");
		eventNotificationService
			.sendMessage(null, userId, IEventNotificationService.DELIVERY_METHOD_MAIL,
				monitoringService.getMessageService()
					.getMessage("event.emailnotifications.email.subject", new Object[] {}),
				emailBody, isHtmlFormat);
	    }

	    monitoringService.archiveEmailNotification(orgId, lessonId, searchType, emailBody, recipients);
	} finally {
	    HibernateSessionManager.closeSession();
	}
    }

}