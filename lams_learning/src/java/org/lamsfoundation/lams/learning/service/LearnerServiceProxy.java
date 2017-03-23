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

package org.lamsfoundation.lams.learning.service;

import javax.servlet.ServletContext;

import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * This class act as the proxy between web layer and service layer. It is
 * designed to decouple the presentation logic and business logic completely.
 * In this way, the presentation tier will no longer be aware of the changes in
 * service layer. Therefore we can feel free to switch the business logic
 * implementation.
 * </p>
 *
 */
public class LearnerServiceProxy {

    /**
     * Return the learner domain service object. It will delegate to the Spring
     * helper method to retrieve the proper bean from Spring bean factory.
     *
     * @param servletContext
     *            the servletContext for current application
     * @return learner service object.
     */
    public static final ICoreLearnerService getLearnerService(ServletContext servletContext) {
	return (ICoreLearnerService) LearnerServiceProxy.getDomainService(servletContext, "learnerService");
    }

    /**
     * Return the user management domain service object. It will delegate to
     * the Spring helper method to retrieve the proper bean from Spring bean
     * factory
     *
     * @param servletContext
     *            the servletContext for current application
     * @return user management service object
     */
    public static final IUserManagementService getUserManagementService(ServletContext servletContext) {
	return (IUserManagementService) LearnerServiceProxy.getDomainService(servletContext, "userManagementService");
    }

    /**
     * Return the lams tool domain service object. It will delegate to the
     * Spring helper method to retrieve the proper bean from Spring bean factory.
     *
     * @param serlvetContext
     *            the servletContext for current application
     * @return tool service object
     */
    public static final ILamsToolService getLamsToolService(ServletContext serlvetContext) {
	return (ILamsToolService) LearnerServiceProxy.getDomainService(serlvetContext, "lamsToolService");
    }

    public static final ILessonService getLessonService(ServletContext servletContext) {
	return (ILessonService) LearnerServiceProxy.getDomainService(servletContext, "lessonService");
    }

    public static final IGradebookService getGradebookService(ServletContext servletContext) {
	return (IGradebookService) LearnerServiceProxy.getDomainService(servletContext, "gradebookService");
    }

    public static final IMonitoringService getMonitoringService(ServletContext servletContext) {
	return (IMonitoringService) LearnerServiceProxy.getDomainService(servletContext, "monitoringService");
    }

    public static final MessageService getMessageService(ServletContext servletContext) {
	return (MessageService) LearnerServiceProxy.getDomainService(servletContext, "learningMessageService");
    }

    public static final MessageService getMonitoringMessageService(ServletContext servletContext) {
	return (MessageService) LearnerServiceProxy.getDomainService(servletContext, "monitoringMessageService");
    }

    /**
     * Return the activity mapping service object.
     *
     * @param serlvetContext
     *            the servletContext for current application
     * @return the activity mapping service object.
     */
    public static final ActivityMapping getActivityMapping(ServletContext serlvetContext) {
	return (ActivityMapping) LearnerServiceProxy.getDomainService(serlvetContext, "activityMapping");
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

}