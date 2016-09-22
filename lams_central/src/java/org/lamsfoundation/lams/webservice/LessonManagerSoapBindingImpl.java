/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.webservice;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServlet;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.DateUtil;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * <a href="LessonManagerSoapBindingImpl.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class LessonManagerSoapBindingImpl implements LessonManager {

    private static MessageContext context = MessageContext.getCurrentContext();

    private static IntegrationService integrationService = (IntegrationService) WebApplicationContextUtils
	    .getRequiredWebApplicationContext(
		    ((HttpServlet) LessonManagerSoapBindingImpl.context.getProperty(HTTPConstants.MC_HTTP_SERVLET))
			    .getServletContext())
	    .getBean("integrationService");

    private static IMonitoringService monitoringService = (IMonitoringService) WebApplicationContextUtils
	    .getRequiredWebApplicationContext(
		    ((HttpServlet) LessonManagerSoapBindingImpl.context.getProperty(HTTPConstants.MC_HTTP_SERVLET))
			    .getServletContext())
	    .getBean("monitoringService");

    @Override
    public Long startLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, String countryIsoCode, String langIsoCode, String customCSV)
	    throws RemoteException {
	try {
	    ExtServer extServer = LessonManagerSoapBindingImpl.integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerSoapBindingImpl.integrationService.getExtUserUseridMap(extServer,
		    username);
	    ExtCourseClassMap orgMap = LessonManagerSoapBindingImpl.integrationService.getExtCourseClassMap(extServer,
		    userMap, courseId, countryIsoCode, langIsoCode, null, LoginRequestDispatcher.METHOD_MONITOR);
	    // 1. init lesson
	    Lesson lesson = LessonManagerSoapBindingImpl.monitoringService.initializeLesson(title, desc, ldId,
		    orgMap.getOrganisation().getOrganisationId(), userMap.getUser().getUserId(), customCSV, false,
		    false, true, false, false, false, false, false, false, null, null);
	    // 2. create lessonClass for lesson
	    createLessonClass(lesson, orgMap.getOrganisation(), userMap.getUser());
	    // 3. start lesson
	    LessonManagerSoapBindingImpl.monitoringService.startLesson(lesson.getLessonId(),
		    userMap.getUser().getUserId());
	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    @Override
    public Long scheduleLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, String startDate, String countryIsoCode, String langIsoCode,
	    String customCSV) throws RemoteException {
	try {
	    ExtServer extServer = LessonManagerSoapBindingImpl.integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerSoapBindingImpl.integrationService.getExtUserUseridMap(extServer,
		    username);
	    ExtCourseClassMap orgMap = LessonManagerSoapBindingImpl.integrationService.getExtCourseClassMap(extServer,
		    userMap, courseId, countryIsoCode, langIsoCode, null, LoginRequestDispatcher.METHOD_MONITOR);
	    // 1. init lesson
	    Lesson lesson = LessonManagerSoapBindingImpl.monitoringService.initializeLesson(title, desc, ldId,
		    orgMap.getOrganisation().getOrganisationId(), userMap.getUser().getUserId(), customCSV, false,
		    false, true, false, false, false, false, false, false, null, null);
	    // 2. create lessonClass for lesson
	    createLessonClass(lesson, orgMap.getOrganisation(), userMap.getUser());
	    // 3. schedule lesson
	    Date date = DateUtil.convertFromLAMSFlashFormat(startDate);
	    LessonManagerSoapBindingImpl.monitoringService.startLessonOnSchedule(lesson.getLessonId(), date,
		    userMap.getUser().getUserId());
	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    @Override
    public boolean deleteLesson(String serverId, String datetime, String hashValue, String username, long lsId)
	    throws RemoteException {
	try {
	    ExtServer extServer = LessonManagerSoapBindingImpl.integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerSoapBindingImpl.integrationService.getExtUserUseridMap(extServer,
		    username);
	    LessonManagerSoapBindingImpl.monitoringService.removeLesson(lsId, userMap.getUser().getUserId());
	    return true;
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    @SuppressWarnings("unchecked")
    private void createLessonClass(Lesson lesson, Organisation organisation, User creator) {
	List<User> staffList = new LinkedList<User>();
	staffList.add(creator);
	List<User> learnerList = new LinkedList<User>();
	IUserManagementService userManagementService = (IUserManagementService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(
			((HttpServlet) LessonManagerSoapBindingImpl.context.getProperty(HTTPConstants.MC_HTTP_SERVLET))
				.getServletContext())
		.getBean("userManagementService");
	Vector<User> learnerVector = userManagementService
		.getUsersFromOrganisationByRole(organisation.getOrganisationId(), Role.LEARNER, false, true);
	learnerList.addAll(learnerVector);
	LessonManagerSoapBindingImpl.monitoringService.createLessonClassForLesson(lesson.getLessonId(), organisation,
		organisation.getName() + "Learners", learnerList, organisation.getName() + "Staff", staffList,
		creator.getUserId());

    }

}
