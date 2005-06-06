/*
 * Created on May 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.service;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesServiceProxy {
	
	private ISubmitFilesService submitFilesService;
	
	public static final ISubmitFilesService getSubmitFilesService(ServletContext servletContext){
		WebApplicationContext context = WebApplicationContextUtils
										.getRequiredWebApplicationContext(servletContext);
		return (ISubmitFilesService)context.getBean("submitFilesService");
	}
	

}
