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

/* $$Id$$ */
package org.lamsfoundation.lams.monitoring.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet for flash to call in order to initialise and create lesson classes
 * for multiple lessons.
 * 
 * @web:servlet name="initializeAndCreateLessons"
 * @web:servlet-mapping url-pattern="/initializeAndCreateLessons"
 */
public class InitializeAndCreateLessons extends AbstractStoreWDDXPacketServlet {
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(InitializeAndCreateLessons.class);

    private static final long serialVersionUID = 2349582345234543680L;

    private static IAuditService auditService;

    private static String messageKey = "initializeAndCreateLessons";

    protected String process(String lessonPackage, HttpServletRequest request) throws Exception {
	FlashMessage flashMessage;
	auditService = getAuditService();

	// get User infomation from shared session.
	HttpSession ss = SessionManager.getSession();

	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userID = (user != null) ? user.getUserID() : null;

	if (userID == null) {
	    log.error("Can not find valid login user information");
	    flashMessage = new FlashMessage(messageKey, "Can not find valid login user information", FlashMessage.ERROR);
	}

	if (log.isDebugEnabled()) {
	    log.debug("StartLessonsServlet process received packet " + lessonPackage);
	}

	try {
	    IMonitoringService monitoringService = getMonitoringService();
	    List<Long> lessonIds = monitoringService.initializeAndCreateLessons(userID, lessonPackage);
	    if (lessonIds != null && lessonIds.size() > 0) {
		String lessonIdsCSV = "";
		for (Long lessonId : lessonIds) {
		    lessonIdsCSV += lessonId + ",";
		}
		flashMessage = new FlashMessage(messageKey, lessonIdsCSV);
	    } else {
		flashMessage = new FlashMessage(messageKey, "");
	    }
	} catch (Exception e) {
	    log.error("Exception thrown while starting lessons.", e);
	    flashMessage = FlashMessage.getExceptionOccured(messageKey, e.getMessage());
	    auditService.log(InitializeAndCreateLessons.class.getName(), e.getMessage());
	}

	return flashMessage.serializeMessage();
    }

    protected String getMessageKey(String packet, HttpServletRequest request) {
	return messageKey;
    }

    public IMonitoringService getMonitoringService() {
	WebApplicationContext webContext = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext());
	return (IMonitoringService) webContext.getBean(MonitoringConstants.MONITORING_SERVICE_BEAN_NAME);
    }

    private IAuditService getAuditService() {
	if (auditService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this
		    .getServletContext());
	    auditService = (IAuditService) ctx.getBean("auditService");
	}
	return auditService;
    }

}
