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


package org.lamsfoundation.lams.monitoring.service;

import javax.servlet.ServletContext;

import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * This class act as the proxy between web layer and service layer. It is designed to decouple the presentation logic
 * and business logic completely. In this way, the presentation tier will no longer be aware of the changes in service
 * layer. Therefore we can feel free to switch the business logic implementation.
 * </p>
 *
 * @author Jacky Fang
 * @since 2005-4-15
 * @version 1.1
 *
 */
public class MonitoringServiceProxy {

    /**
     * Return the monitor domain service object. It will delegate to the Spring helper method to retrieve the proper
     * bean from Spring bean factory.
     *
     * @param servletContext
     *            the servletContext for current application
     * @return monitoring service object.
     */
    public static final IMonitoringService getMonitoringService(ServletContext servletContext) {
	return (IMonitoringService) MonitoringServiceProxy.getDomainService(servletContext, "monitoringService");
    }

    /**
     * Return the learner domain service object. It will delegate to the Spring helper method to retrieve the proper
     * bean from Spring bean factory.
     *
     * @param servletContext
     *            the servletContext for current application
     * @return learner service object.
     */
    public static final ILearnerService getLearnerService(ServletContext servletContext) {
	return (ILearnerService) MonitoringServiceProxy.getDomainService(servletContext, "learnerService");
    }

    public static final IUserManagementService getUserManagementService(ServletContext servletContext) {
	return (IUserManagementService) MonitoringServiceProxy.getDomainService(servletContext,
		"userManagementService");
    }

    public static final IAuthoringService getAuthoringService(ServletContext servletContext) {
	return (IAuthoringService) MonitoringServiceProxy.getDomainService(servletContext, "authoringService");
    }

    /**
     * Retrieve the proper Spring bean from bean factory.
     *
     * @param servletContext
     *            the servletContext for current application
     * @return the Spring service bean.
     */
    private static Object getDomainService(ServletContext servletContext, String serviceName) {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	return wac.getBean(serviceName);
    }

    public static final ILessonService getLessonService(ServletContext servletContext) {
	return (ILessonService) MonitoringServiceProxy.getDomainService(servletContext, "lessonService");
    }
}