/*
 * Created on Sep 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.export.service;

import javax.servlet.ServletContext;

import org.lamsfoundation.lams.learning.export.service.IExportPortfolioService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExportPortfolioServiceProxy {

	public static final IExportPortfolioService getExportPortfolioService(ServletContext servletContext)
	{
		return (IExportPortfolioService)getDomainService(servletContext, "exportService");
	}
	
	 private static Object getDomainService(ServletContext servletContext,String serviceName)
	    {
	        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	        return wac.getBean(serviceName);
	    }
}
