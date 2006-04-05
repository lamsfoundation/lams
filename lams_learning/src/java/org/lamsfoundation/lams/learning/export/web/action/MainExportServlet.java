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

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.StringBuffer;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CSSThemeUtil;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.export.ToolPortfolio;
import org.lamsfoundation.lams.learning.export.service.IExportPortfolioService;
import org.lamsfoundation.lams.learning.export.service.ExportPortfolioService;
import org.lamsfoundation.lams.learning.export.service.ExportPortfolioServiceProxy;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

import org.apache.struts.util.MessageResources;




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
	private String exportTmpDir;
	private MessageResources resources = MessageResources.getMessageResources(ExportPortfolioConstants.MESSAGE_RESOURCE_CONFIG_PARAM);
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException, ExportPortfolioException{
	    
	    String htmlOutput=null;	
		Portfolio portfolios = null;
		Long lessonID = null;
		String lessonDescription = null;
		
	    /** Get the cookies that were sent along with this request, then pass it onto export service */
		Cookie[] cookies = request.getCookies();	
	
		IExportPortfolioService exportService = ExportPortfolioServiceProxy.getExportPortfolioService(this.getServletContext());
		ILearnerService learnerService = LearnerServiceProxy.getLearnerService(this.getServletContext());
		
		String mode = WebUtil.readStrParam(request, AttributeNames.PARAM_MODE);
		
		if (mode.equals(ToolAccessMode.LEARNER.toString()))
		{
			HttpSession session = SessionManager.getSession();
		    UserDTO userDto = (UserDTO)session.getAttribute(AttributeNames.USER);
		    Integer userId = userDto.getUserID();
		    lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
		    portfolios = exportService.exportPortfolioForStudent(userId, lessonID, true, cookies);
		}
		else if(mode.equals(ToolAccessMode.TEACHER.toString()))
		{
			//done in the monitoring environment
			lessonID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID));			
			portfolios = exportService.exportPortfolioForTeacher(lessonID, cookies);
		}
		
		Lesson lesson = learnerService.getLesson(lessonID);
		if (lesson != null)
			lessonDescription = lesson.getLessonDescription();
		
		if (portfolios!= null)
		{
			
			exportTmpDir = portfolios.getExportTmpDir();
				
			String mainFileName = exportTmpDir+ File.separator + ExportPortfolioConstants.MAIN_EXPORT_FILENAME;				   
				
			htmlOutput = generateMainPage(portfolios, lessonDescription);	
			
			//writing the file to the temp export folder.	
			BufferedWriter fileout = new BufferedWriter(new FileWriter(mainFileName));
			fileout.write(htmlOutput);
			fileout.close();			
			
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
	
	/**
	 * Used StringBuffer for concatenation as it is more efficient than using String
	 * @param portfolios
	 * @return
	 */
	private String generateMainPage(Portfolio portfolio, String lessonDescriptionToDisplay)
	{
	    ToolPortfolio[] portfolios = portfolio.getToolPortfolios();
	    String htmlPage;
	    if (portfolios != null)
	    {
	    	StringBuffer htmlContent = new StringBuffer(300);
	    	htmlContent.append("<ol> ");
	    	for (int i=0; i<portfolios.length; i++)
		    {
		        htmlContent.append("<li>");
		        
		        htmlContent.append("<a href='").append(portfolios[i].getToolLink()).append("'> ");
		        htmlContent.append(portfolios[i].getActivityName());
		        htmlContent.append("</a>: ");

		        htmlContent.append(portfolios[i].getActivityDescription());
		        
		        htmlContent.append("</li>");
		    }
	    	htmlContent.append(" </ol>");
		    htmlPage = resources.getMessage(ExportPortfolioConstants.EXPORT_MAINPAGE_KEY, lessonDescriptionToDisplay, htmlContent.toString());

		   
	    }
	    else
	    	htmlPage = resources.getMessage(ExportPortfolioConstants.EXPORT_FAILED_KEY);
	    
	     return htmlPage;
	    
	}
	
	
	private String returnRelativeExportTmpDir(String absolutePath)
	{
	    String tempSysDirName = ExportPortfolioConstants.TEMP_DIRECTORY;
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
	
	
	
	/*
	 * The method below is commented out, because it uses HttpUrlConnection to connect to export service.
	 * It might be reverted back to this method, if we need to use jsp engine for the look and feel of things.
	 * 
	 * MainExportServlet is responsible for calling the main export url 
	 * which is in charge of calling all tools to output its portfolio.
	 * It is responsible for creating the temporary directory where
	 * all file output should be placed. It will connect to the main
	 * export url via HttpUrlConnection.
	 * At this moment, it is unsure where this page will forward to once 
	 * the files are generated. (Most likely, the online version of the
	 * export page, or forward to the download page, which zips up all 
	 * the exports and allows the user to download the zip file.
	 */
/*	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	String url = basePath + "/exportPortfolio.do";
	String urlWithParameters = null;
	String mode = WebUtil.readStrParam(request, WebUtil.PARAM_MODE);
	
	
	
	
	//create the directory
	if (createTemporaryDirectory(ExportPortfolioConstants.EXPORT_TMP_DIR))
	{			
	
		if (mode.equals(ToolAccessMode.LEARNER.toString()))
		{
			urlWithParameters = WebUtil.appendParameterToURL(url, WebUtil.PARAM_MODE, ToolAccessMode.LEARNER.toString());	
		}
		else if (mode.equals(ToolAccessMode.TEACHER.toString()))
		{
			urlWithParameters = WebUtil.appendParameterToURL(url, WebUtil.PARAM_MODE, ToolAccessMode.TEACHER.toString());
			urlWithParameters = WebUtil.appendParameterToURL(urlWithParameters, "lessonID", WebUtil.readStrParam(request, "lessonID"));
		}
		
		try
		{
		    HttpUrlConnectionUtil.writeResponseToFile(urlWithParameters, ExportPortfolioConstants.EXPORT_TMP_DIR, ExportPortfolioConstants.MAIN_EXPORT_FILENAME, cookies);
		}
		catch(MalformedURLException e)
		{
			throw new ExportPortfolioException("The URL given is invalid. ",e);
		}
		catch(FileNotFoundException e)
		{
			throw new ExportPortfolioException("The directory or file may not exist. ",e);
		}
		catch(IOException e)
		{
			throw new ExportPortfolioException("A problem has occurred while writing file. ", e);
		}
		
		
		//writeResponseToFile(urlWithParameters, ExportPortfolioConstants.EXPORT_TMP_DIR, ExportPortfolioConstants.MAIN_EXPORT_FILENAME, cookies);
	}
	//forward somewhere
	
	*/
	

}
