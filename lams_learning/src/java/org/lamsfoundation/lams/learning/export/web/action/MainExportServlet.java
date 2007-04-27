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
package org.lamsfoundation.lams.learning.export.web.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.export.service.ExportPortfolioServiceProxy;
import org.lamsfoundation.lams.learning.export.service.IExportPortfolioService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
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
	
    private static Logger log = Logger.getLogger(MainExportServlet.class);
 
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
		String role = null;
		ToolAccessMode accessMode = null;
		String exportFilename = "";
		
	    /** Get the cookies that were sent along with this request, then pass it onto export service */
		Cookie[] cookies = request.getCookies();	

		IExportPortfolioService exportService = ExportPortfolioServiceProxy.getExportPortfolioService(this.getServletContext());
		
		String mode = WebUtil.readStrParam(request, AttributeNames.PARAM_MODE);
		if (mode.equals(ToolAccessMode.LEARNER.toString()))
		{
			// TODO check if the user id is coming from the request then the current user should have monitoring privilege
			Integer userId = getLearnerUserID(request);
		    lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
		    
		    if((role = WebUtil.readStrParam(request, AttributeNames.PARAM_ROLE, true)) != null){   
		    	accessMode = (role.equals(ToolAccessMode.TEACHER.toString()))?ToolAccessMode.TEACHER:null;
		    }
		    
		    portfolios = exportService.exportPortfolioForStudent(userId, lessonID, true, accessMode, cookies);
		    exportFilename = ExportPortfolioConstants.EXPORT_LEARNER_PREFIX + " " + portfolios.getLessonName() + ".zip";
		}
		else if(mode.equals(ToolAccessMode.TEACHER.toString()))
		{
			//done in the monitoring environment
			lessonID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID));			
			portfolios = exportService.exportPortfolioForTeacher(lessonID, cookies);
			exportFilename = ExportPortfolioConstants.EXPORT_TEACHER_PREFIX + " " + portfolios.getLessonName() + ".zip";
		}
		
		if (portfolios!= null)
		{
			
			exportTmpDir = portfolios.getExportTmpDir();
				
			exportService.generateMainPage(request, portfolios, cookies);	
			
			if(portfolios.getNotebookPortfolios() != null)
				if(portfolios.getNotebookPortfolios().length > 0)
					exportService.generateNotebookPage(request, portfolios, cookies);
			
			
			//bundle the stylesheet with the package
			CSSBundler bundler = new CSSBundler(request, cookies, exportTmpDir, exportService.getUserThemes());
			bundler.bundleStylesheet();
			
			// zip up the contents of the temp export folder 
			String zipFilename = exportService.zipPortfolio(exportFilename, exportTmpDir);
/*			-- Used for testing timeout  change the export url in exportWaitingPage to  
			-- String exportUrl = learning_root + "portfolioExport?" + request.getQueryString()+"&sleep=1800000";
			-- to pause for 30 mins. 
			Integer sleeptime = WebUtil.checkInteger("sleep", request.getParameter("sleep"), true);
			if ( sleeptime != null ) {
				log.debug("Testing timeouts. Sleeping for "+sleeptime/1000+" seconds");
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e) {
					log.error("Sleep interrupted",e);
				}
			}
*/			
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
	

}
