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

package org.lamsfoundation.lams.tool.sbmt.service;

import javax.servlet.ServletContext;

import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesServiceProxy {
	
	public static final ISubmitFilesService getSubmitFilesService(ServletContext servletContext){
		return (ISubmitFilesService) getService(servletContext);
	}
	
	public static final ToolSessionManager getToolSessionManager(ServletContext servletContext){
		return (ToolSessionManager) getService(servletContext);
	}
	
	/**
	 * @param servletContext
	 * @return
	 */
	private static Object getService(ServletContext servletContext) {
		WebApplicationContext context = WebApplicationContextUtils
										.getRequiredWebApplicationContext(servletContext);
		return context.getBean("submitFilesService");
	}
	

}
