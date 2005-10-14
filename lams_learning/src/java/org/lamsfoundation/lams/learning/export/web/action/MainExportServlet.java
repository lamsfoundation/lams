/*
 * Created on Oct 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.export.web.action;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.StringBuffer;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.Cookie;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.export.service.IExportPortfolioService;
import org.lamsfoundation.lams.learning.export.service.ExportPortfolioService;
import org.lamsfoundation.lams.learning.export.service.ExportPortfolioServiceProxy;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;




/**
 * @author mtruong
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
 * 
 */
public class MainExportServlet extends HttpServlet {
	
	private static Logger log = Logger.getLogger(MainExportServlet.class);
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException, ExportPortfolioException{
	    
	    /** Get the cookies that were sent along with this request, then pass it onto export service */
		Cookie[] cookies = request.getCookies();	
		
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
		
		 String mainFileName = ExportPortfolioConstants.EXPORT_TMP_DIR + File.separator + ExportPortfolioConstants.MAIN_EXPORT_FILENAME;
	    IExportPortfolioService exportService = ExportPortfolioServiceProxy.getExportPortfolioService(this.getServletContext());
		String htmlOutput=null;
	  
		Portfolio[] portfolios = null;
		String mode = WebUtil.readStrParam(request, WebUtil.PARAM_MODE);
		
			if (mode.equals(ToolAccessMode.LEARNER.toString()))
			{
				//get the learnerprogress id
				User learner = LearningWebUtil.getUserData(request, this.getServletContext());
				LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByUser(request, this.getServletContext());
				Long progressId = learnerProgress.getLearnerProgressId();
				
				portfolios = exportService.exportPortfolioForStudent(progressId, learner, true, cookies);
			}
			else if(mode.equals(ToolAccessMode.TEACHER.toString()))
			{
				//get the lesson data
				//done in the monitoring environment
				Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
				//ILearnerService learnerService = LearnerServiceProxy.getLearnerService(this.getServletContext());
				//Lesson lesson = learnerService.getLesson(lessonID);
				
				//portfolios = exportService.exportPortfolioForTeacher(lesson.getLearningDesign().getLearningDesignId(), cookies);
				portfolios = exportService.exportPortfolioForTeacher(lessonID, cookies);
			}
		htmlOutput = generateMainPage(portfolios);	
		
		BufferedWriter fileout = new BufferedWriter(new FileWriter(mainFileName));
		fileout.write(htmlOutput);
		fileout.close();
		
		PrintWriter out = response.getWriter();
		out.println(htmlOutput);
		
	}
	
	/**
	 * Method taken from AbstractExportPortfolioServlet. Uses HttpUrlConnection to connect to the url,
	 * and will save its contents in <code>directoryToStoreFile</code>.
	 * 
	 * @param urlToConnectTo The url in which the connection is to be made to
	 * @param directoryToStoreFile The directory to store the html file
	 * @param filename The filename of the HTML directory in which contents are to be saved to
	 * @throws ExportPortfolioException
	 */
	//comment out to use common code in HttpUrlConnectionUtil
	/*private void writeResponseToFile(String urlToConnectTo, String directoryToStoreFile, String filename, Cookie[] cookies) throws ExportPortfolioException
	{
		String absoluteFilePath = directoryToStoreFile + File.separator + filename;
		try
		{
			URL url = new URL(urlToConnectTo);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			if ( log.isDebugEnabled() ) {
				log.debug("A connection has been established with "+urlToConnectTo);
			}
			InputStream inputStream = con.getInputStream();
			OutputStream outStream = new FileOutputStream(absoluteFilePath);
			   
			int c = -1;
			while ((c = inputStream.read())!= -1)
			{
				outStream.write(c);
			}
			   
			inputStream.close();
			outStream.close(); 
			if ( log.isDebugEnabled() ) {
				log.debug("A connection to "+urlToConnectTo + " has been closed");
			}
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
		
	} */
	
	/**
	 * Creates the temporary export directory. Checks whether the directory already exists,
	 * if so, it should delete that directory (? not sure if i want this function)
	 * then it creates the directory.
	 * @param directory The name of the directory to create
	 * @return true if the directory has been created, false otherwise
	 */
/*	private boolean createTemporaryDirectory(String directory)
	{
		boolean created=false; 
		if (FileUtil.directoryExist(directory))
		{
			//delete directory and create a new one
			try
			{
				if (FileUtil.deleteDirectory(directory))
				{
					created = FileUtil.createDirectory(directory);
				}
				else
				{
					throw new ExportPortfolioException("Could not delete the temporary directory " + directory + ", please manually delete this directory in order to proceed.");
				}
				
			}
			catch(FileUtilException e)
			{
				throw new ExportPortfolioException("An error has occurred while trying to create the temporary directory for the export. Reason: ", e);
			}
		}
		else
		{
			try
			{		
				created = FileUtil.createDirectory(directory);
			}
			catch(FileUtilException e)
			{
				throw new ExportPortfolioException("An error has occurred while trying to create the temporary directory for the export. Reason: ", e);
			}
					
		}
		return created;
		
		
	} */
	private String generateMainPage(Portfolio[] portfolios)
	{
	    StringBuffer htmlPage = new StringBuffer();
	    htmlPage.append("<html><head><title>Export Portfolio</title></head>");
	    htmlPage.append("<body><h1>Portfolio</h1><h2>Activities</h2>");
	    htmlPage.append("<ol>");
	    for (int i=0; i<portfolios.length; i++)
	    {
	        htmlPage.append("<li>");
	        htmlPage.append(portfolios[i].getActivityName())
	        .append(" <a href='").append(portfolios[i].getToolLink()).append("'> ")
	        .append(portfolios[i].getActivityDescription()).append("</a>");
	        htmlPage.append("</li>");
	    }
	    htmlPage.append("</ol>");
	    htmlPage.append("</body></html");
	    
	    return htmlPage.toString();
	}
	
	
	

}
