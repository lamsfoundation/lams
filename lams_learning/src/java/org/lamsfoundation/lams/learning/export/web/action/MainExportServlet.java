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
package org.lamsfoundation.lams.learning.export.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.export.service.ExportPortfolioServiceProxy;
import org.lamsfoundation.lams.learning.export.service.IExportPortfolioService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CSSThemeUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;




/**
 * @author mtruong
 *
 * MainExportServlet is responsible for calling the export service,
 * which is responsible for calling all tools to do its export.
 * The main page will be generated after all tools have completed its 
 * export. At the time of writing, the html for the main page is
 * done manually. 
 * All of the outputs of the export will be zipped up and placed
 * in the temporary export directory. The relative path of the 
 * file location of this zip file is returned. 
 * 
 */
public class MainExportServlet extends HttpServlet {
	
	private static final long serialVersionUID = 7788509831929373666L;
	private String exportTmpDir;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}

	/** If it is running from the learner interface then can use the userID from the user object
	 * If running from the monitoring interface, the learner's userID is supplied by the request parameters.
	 * 
	 * @return userID
	 */
	protected Integer getLearnerUserID(HttpServletRequest request) {
		Integer userId = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, true);
		if ( userId == null ) {
			HttpSession session = SessionManager.getSession();
			UserDTO userDto = (UserDTO)session.getAttribute(AttributeNames.USER);
			userId = userDto.getUserID();
		}
		return userId;
	}
		
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException, ExportPortfolioException{
	    
		Portfolio portfolios = null;
		Long lessonID = null;
		
	    /** Get the cookies that were sent along with this request, then pass it onto export service */
		Cookie[] cookies = request.getCookies();	
	
		IExportPortfolioService exportService = ExportPortfolioServiceProxy.getExportPortfolioService(this.getServletContext());
		// ILearnerService learnerService = LearnerServiceProxy.getLearnerService(this.getServletContext());
		
		String mode = WebUtil.readStrParam(request, AttributeNames.PARAM_MODE);
		
		if (mode.equals(ToolAccessMode.LEARNER.toString()))
		{
			// TODO check if the user id is coming from the request then the current user should have monitoring privilege
		    Integer userId = getLearnerUserID(request);
		    lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
		    portfolios = exportService.exportPortfolioForStudent(userId, lessonID, true, cookies);
		}
		else if(mode.equals(ToolAccessMode.TEACHER.toString()))
		{
			//done in the monitoring environment
			lessonID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID));			
			portfolios = exportService.exportPortfolioForTeacher(lessonID, cookies);
		}
		
		if (portfolios!= null)
		{
			
			exportTmpDir = portfolios.getExportTmpDir();
				
			exportService.generateMainPage(request, portfolios, cookies);	
			
			//bundle the stylesheet with the package
			bundleStylesheetWithExportPackage(exportTmpDir, request, cookies);
			
			//zip up the contents of the temp export folder
			String zipFilename = exportService.zipPortfolio(ExportPortfolioConstants.ZIP_FILENAME, exportTmpDir);
			
			//return the relative filelocation of the zip file so that it can be picked up in exportWaitingPage.jsp
			PrintWriter out = response.getWriter();
			out.println(returnRelativeExportTmpDir(zipFilename)); 
		}
		else
		{
		    //redirect the request to another page.
		}
		
	}
	

	private String returnRelativeExportTmpDir(String absolutePath)
	{
	    String tempSysDirName = FileUtil.TEMP_DIR;
	    return absolutePath.substring(tempSysDirName.length()+1, absolutePath.length());
	}
	
	private void bundleStylesheetWithExportPackage(String directory, HttpServletRequest request, Cookie[] cookies) throws IOException
	{
		List themeList = CSSThemeUtil.getAllUserThemes();
		
		Iterator i = themeList.iterator();
		
		while (i.hasNext())
		{
			String theme = (String)i.next();
			
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
			String url = basePath + "/lams/css/" + theme + ".css";
			HttpUrlConnectionUtil.writeResponseToFile(url, directory, theme + ".css", cookies); //cookies aren't really needed here.
		}
	}

}
