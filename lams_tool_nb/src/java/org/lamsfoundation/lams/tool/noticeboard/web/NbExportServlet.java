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
package org.lamsfoundation.lams.tool.noticeboard.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.servlet.ExportPortfolioServletException;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author mtruong
 *
 */
public class NbExportServlet extends AbstractExportPortfolioServlet {
	
	private final String FILENAME = "nb_main.html";
	private static Logger logger = Logger.getLogger(NbExportServlet.class);

	protected String doOfflineExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies) {
        if (toolContentID == null && toolSessionID == null)
        {
            String error = "Tool content Id or and session Id are null. Unable to set activity title";
            logger.error(error);
        } else {
        	INoticeboardService service = NoticeboardServiceProxy.getNbService(getServletContext());
        	NoticeboardContent content = null;
            if ( toolContentID != null ) {
            	content = service.retrieveNoticeboard(toolContentID);
            } else {
            	NoticeboardSession session=service.retrieveNoticeboardSession(toolSessionID);
            	if ( session != null )
            		content = session.getNbContent();
            }
            if ( content != null ) {
            	activityTitle = content.getTitle();
            }
        }
        return super.doOfflineExport(request, response, directoryName, cookies);
	}
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
	
		String mode = WebUtil.readStrParam(request, AttributeNames.PARAM_MODE);
		//Long toolContentId, toolSessionId, userId;
		String basePath =WebUtil.getBaseServerURL()+request.getContextPath();

		String url = basePath + "/exportPortfolio.do";
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
		}
	
		//try
		//{
			// Can generate other files here by calling writeResponseToFile and supplying different urls 
			writeResponseToFile(urlWithParameters, directoryName, FILENAME, cookies);
		
	/*	}
		catch(ExportPortfolioServletException e)
		{
			throw new NbApplicationException("An error has occurred while trying to generate files ",e);
		} */
		return FILENAME;
	}
	
	/**
	 * TODO: create a checkParameters function, to check if any parameters are missing or not.
	 * @author mtruong
	 */
	
	
	
}
