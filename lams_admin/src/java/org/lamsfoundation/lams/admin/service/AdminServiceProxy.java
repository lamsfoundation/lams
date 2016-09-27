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


package org.lamsfoundation.lams.admin.service;

import javax.servlet.ServletContext;

import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.statistics.service.IStatisticsService;
import org.lamsfoundation.lams.themes.service.IThemeService;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.LdapService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Common class to make it easier to get the Spring beans.
 *         
 * @author jliew
 */
public class AdminServiceProxy {

    private static IUserManagementService manageService;
    private static MessageService messageService;
    private static IIntegrationService integrationService;
    private static IAuditService auditService;
    private static IImportService importService;
    private static LdapService ldapService;
    private static IStatisticsService statisticsService;
    private static IThemeService themeService;
    private static Configuration configurationService;
    private static ILessonService lessonService;
    private static IMonitoringService monitoringService;
    private static IEventNotificationService eventNotificationService;
    private static ITimezoneService timezoneService;
    private static ISecurityService securityService;

    public static final IUserManagementService getService(ServletContext servletContext) {
	if (AdminServiceProxy.manageService == null) {
	    AdminServiceProxy.manageService = (IUserManagementService) AdminServiceProxy
		    .getDomainService(servletContext, "userManagementService");
	}
	return AdminServiceProxy.manageService;
    }

    public static final MessageService getMessageService(ServletContext servletContext) {
	if (AdminServiceProxy.messageService == null) {
	    AdminServiceProxy.messageService = (MessageService) AdminServiceProxy.getDomainService(servletContext,
		    "adminMessageService");
	}
	return AdminServiceProxy.messageService;

    }

    public static final IIntegrationService getIntegrationService(ServletContext servletContext) {
	if (AdminServiceProxy.integrationService == null) {
	    AdminServiceProxy.integrationService = (IIntegrationService) AdminServiceProxy
		    .getDomainService(servletContext, "integrationService");
	}
	return AdminServiceProxy.integrationService;
    }

    public static final IAuditService getAuditService(ServletContext servletContext) {
	if (AdminServiceProxy.auditService == null) {
	    AdminServiceProxy.auditService = (IAuditService) AdminServiceProxy.getDomainService(servletContext,
		    "auditService");
	}
	return AdminServiceProxy.auditService;
    }

    public static final IImportService getImportService(ServletContext servletContext) {
	if (AdminServiceProxy.importService == null) {
	    AdminServiceProxy.importService = (IImportService) AdminServiceProxy.getDomainService(servletContext,
		    "importService");
	}
	return AdminServiceProxy.importService;
    }

    public static final LdapService getLdapService(ServletContext servletContext) {
	if (AdminServiceProxy.ldapService == null) {
	    AdminServiceProxy.ldapService = (LdapService) AdminServiceProxy.getDomainService(servletContext,
		    "ldapService");
	}
	return AdminServiceProxy.ldapService;
    }

    public static final IStatisticsService getStatisticsService(ServletContext servletContext) {
	if (AdminServiceProxy.statisticsService == null) {
	    AdminServiceProxy.statisticsService = (IStatisticsService) AdminServiceProxy
		    .getDomainService(servletContext, "statisticsService");
	}
	return AdminServiceProxy.statisticsService;
    }

    public static final IThemeService getThemeService(ServletContext servletContext) {
	if (AdminServiceProxy.themeService == null) {
	    AdminServiceProxy.themeService = (IThemeService) AdminServiceProxy.getDomainService(servletContext,
		    "themeService");
	}
	return AdminServiceProxy.themeService;
    }

    public static final Configuration getConfiguration(ServletContext servletContext) {
	if (AdminServiceProxy.configurationService == null) {
	    AdminServiceProxy.configurationService = (Configuration) AdminServiceProxy.getDomainService(servletContext,
		    "configurationService");
	}
	return AdminServiceProxy.configurationService;
    }

    public static final ILessonService getLessonService(ServletContext servletContext) {
	if (AdminServiceProxy.lessonService == null) {
	    AdminServiceProxy.lessonService = (ILessonService) AdminServiceProxy.getDomainService(servletContext,
		    "lessonService");
	}
	return AdminServiceProxy.lessonService;
    }

    public static final IMonitoringService getMonitoringService(ServletContext servletContext) {
	if (AdminServiceProxy.monitoringService == null) {
	    AdminServiceProxy.monitoringService = (IMonitoringService) AdminServiceProxy
		    .getDomainService(servletContext, "monitoringService");
	}
	return AdminServiceProxy.monitoringService;
    }

    public static final IEventNotificationService getEventNotificationService(ServletContext servletContext) {
	if (AdminServiceProxy.eventNotificationService == null) {
	    AdminServiceProxy.eventNotificationService = (IEventNotificationService) AdminServiceProxy
		    .getDomainService(servletContext, "eventNotificationService");
	}
	return AdminServiceProxy.eventNotificationService;
    }

    private static Object getDomainService(ServletContext servletContext, String serviceName) {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	return wac.getBean(serviceName);
    }

    public static final ITimezoneService getTimezoneService(ServletContext servletContext) {
	if (AdminServiceProxy.timezoneService == null) {
	    AdminServiceProxy.timezoneService = (ITimezoneService) AdminServiceProxy.getDomainService(servletContext,
		    "timezoneService");
	}
	return AdminServiceProxy.timezoneService;
    }

    public static final ISecurityService getSecurityService(ServletContext servletContext) {
	if (AdminServiceProxy.securityService == null) {
	    AdminServiceProxy.securityService = (ISecurityService) AdminServiceProxy.getDomainService(servletContext,
		    "securityService");
	}
	return AdminServiceProxy.securityService;
    }
}