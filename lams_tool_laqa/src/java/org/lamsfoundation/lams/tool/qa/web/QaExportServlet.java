/*
 * Created on Sep 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.web;

import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.servlet.ExportPortfolioServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import javax.servlet.ServletException;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.util.WebUtil;
import java.io.File;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QaExportServlet extends AbstractExportPortfolioServlet{

	private final String FILENAME="qa_main.html";
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName)
	{
		String mode = WebUtil.readStrParam(request, WebUtil.PARAM_MODE);
		Long toolContentId, toolSessionId, userId;
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

		String url = basePath + "/exportPortfolioStarter.do";
		String urlWithParameters = null;
		
		if (mode.equals(ToolAccessMode.LEARNER.toString()))
		{
			//read in userId and toolSessionId
			urlWithParameters = appendParametersToLearnerExportURL(request, url);		
			
		}
		else if (mode.equals(ToolAccessMode.TEACHER.toString()))
		{
			//read in toolContentId
			urlWithParameters = appendParametersToTeacherExportURL(request, url);
			urlWithParameters = WebUtil.appendParameterToURL(urlWithParameters, USER_ID, WebUtil.readStrParam(request, USER_ID));
		}
	
		try
		{
			/* Can generate other files here by calling writeResponseToFile and supplying different urls */
			writeResponseToFile(urlWithParameters, directoryName, FILENAME);
		
		}
		catch(ExportPortfolioServletException e)
		{
			throw new QaApplicationException("An error has occurred while trying to generate files ",e);
		}
		return FILENAME;
	}

}
