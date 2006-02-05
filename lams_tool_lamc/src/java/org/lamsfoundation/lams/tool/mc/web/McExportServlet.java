
package org.lamsfoundation.lams.tool.mc.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Ozgur Demirtas
 *
 */
public class McExportServlet extends AbstractExportPortfolioServlet {
	
	private final String FILENAME = "mc_main.html";
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
	
		String mode = WebUtil.readStrParam(request, AttributeNames.PARAM_MODE);
		//Long toolContentId, toolSessionId, userId;
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

		String url = basePath + "/exportPortfolio.do";
		String urlWithParameters = null;
		
		if (mode.equals(ToolAccessMode.LEARNER.toString()))
		{
			urlWithParameters = appendParametersToLearnerExportURL(request, url);		
			
		}
		else if (mode.equals(ToolAccessMode.TEACHER.toString()))
		{
			urlWithParameters = appendParametersToTeacherExportURL(request, url);
		}
 
		writeResponseToFile(urlWithParameters, directoryName, FILENAME, cookies);

		return FILENAME;
	}
}
