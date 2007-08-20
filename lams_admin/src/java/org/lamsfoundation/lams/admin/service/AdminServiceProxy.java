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

/* $Id$ */
package org.lamsfoundation.lams.admin.service;

import javax.servlet.ServletContext;

import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 *
 * Common class to make it easier to get the Spring beans.
 */
public class AdminServiceProxy {
	
	private static IUserManagementService manageService;
	private static MessageService messageService;
	private static IIntegrationService integrationService;
	private static IAuditService auditService;
	private static IImportService importService;
	
	public static final IUserManagementService getService(ServletContext servletContext){
		if (manageService == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			return (IUserManagementService) ctx.getBean("userManagementService");
		} else {
			return manageService;
		}
	}
	
	public static final MessageService getMessageService(ServletContext servletContext){
		if (messageService == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			return (MessageService)ctx.getBean("adminMessageService");
		} else {
			return messageService;
		}
	}
	
	public static final IIntegrationService getIntegrationService(ServletContext servletContext){
		if(integrationService == null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			integrationService = (IIntegrationService)ctx.getBean("integrationService");
		}
		return integrationService;
	}
	
	public static final IAuditService getAuditService(ServletContext servletContext){
		if(auditService==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			auditService = (IAuditService)ctx.getBean("auditService");
		}
		return auditService;
	}
	
	public static final IImportService getImportService(ServletContext servletContext){
		if(importService==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			importService = (IImportService)ctx.getBean("importService");
		}
		return importService;
	}
}
