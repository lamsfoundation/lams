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
package org.lamsfoundation.lams.web.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * @author mtruong
 *
 * Base servlet for handling the export portfolio calls
 * from the Export Portfolio service.
 * 
 * This servlet takes care of reading the directory name
 * from the request parameter "directoryName", which is set by the export
 * portfolio service for that particular tool. (Concatenated on the end
 * of the export call which calls this servlet).
 * 
 * The tool must implement the method doExport. In this doExport method,
 * the tools would supply the url, the location to store the file and the filename,
 * to the method writeResponseToFile which is responsible for making
 * the HttpURLConnection to that url and write the response to disk.
 * 
 * If needed, the tool should be able to generate as many files
 * as needed (to fit in with the hierarchical structure)
 * although only the name of the main HTML file should be returned. 
 * 
 * This servlet should return the filename to the export service
 * once all operations have been completed. 
 * 
 */
public abstract class AbstractExportPortfolioServlet extends HttpServlet {
	
	private static Logger log = Logger.getLogger(AbstractExportPortfolioServlet.class);
	
	private static final String EXPORT_ERROR_MSG = "This activity does not support portfolio export";
	private static final String EXPORT_ERROR_FILENAME = "portfolioExportNotSupported.html";
	protected Long userID = null;
	protected Long toolSessionID = null;
	protected Long toolContentID = null;
	protected String mode = null;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		String mainFileName = null;
		String directoryName = null;
		
		
		Cookie[] cookies = request.getCookies();		
		
		directoryName = WebUtil.readStrParam(request, AttributeNames.PARAM_DIRECTORY_NAME); 
		
		//put the path together again, since the given directory was a relative one.
		String absoluteDirectoryPath = FileUtil.TEMP_DIR + File.separator + directoryName;
	
		if (log.isDebugEnabled()) {
			log.debug("Directory name to store files is "+directoryName);
		}
		
		//check if the directory name has any trailing slashes, if so, remove it and return the new value
		directoryName = checkDirectoryName(directoryName);
		
	
		//check if the directory exists
		if (!FileUtil.directoryExist(absoluteDirectoryPath))
		{
			//throw new IOException("The directory supplied " + absoluteDirectoryPath + " does not exist.");
		    log.error("The directory supplied " + absoluteDirectoryPath + " does not exist.");
		   
		}
		
		//check whether the necessary parameters are all appended
		if (parametersAreValid(request))
		{
		
		    if (log.isDebugEnabled()) {
				log.debug("Export is conducted in mode: " + mode);
			}
					
			mainFileName = doExport(request, response, absoluteDirectoryPath, cookies);
			
			if (log.isDebugEnabled()) {
				log.debug("The name of main html file is "+mainFileName);
			}
		}
		else
		{
		    log.error("Cannot perform export for tool as some parameters are missing from the export url.");
		    writeErrorMessageToFile(EXPORT_ERROR_MSG, directoryName, EXPORT_ERROR_FILENAME);
		}
		//return the name of the main html file
		PrintWriter out = response.getWriter();
		out.println(mainFileName);
	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
		
	/**
	 * This method allows the tool to call all necessary export pages
	 * The servlet filter is in charge of writing the files to disk, however, 
	 * the absolute filename must be set in the request parameter fileName, 
	 * so that it is available to the filter.
	 * These pages will be written to disk in the directory specified by
	 * <code>directoryName</code>. The name of the main html file should
	 * be returned.
	 *
	 * @param directoryName
	 * @return
	 */
	abstract protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies);
	
	private String checkDirectoryName(String directoryName)
	{
		String validDirectoryName;
		if (FileUtil.trailingForwardSlashPresent(directoryName))
			validDirectoryName = FileUtil.removeTrailingForwardSlash(directoryName);
		else
			validDirectoryName = directoryName;
		
		return validDirectoryName;
	}
	
	/**
	 * Sets up the HttpURLCOnnection, it will read the response from
	 * the URL given by <code>urlToConnectTo</code> and write it to the
	 * file given by <code>filename</code> in the directory <code>directoryToStoreFile</code>.
	 * 
	 * If the returned status code is not 200, then it will generate an export file
	 * containing a warning message that the export is unsupported. If any exceptions occcur,
	 * a similar export file will be generated in place of the activity's main export file.
	 * 
	 * @param filename
	 */
	protected int writeResponseToFile(String urlToConnectTo, String directoryToStoreFile, String filename, Cookie[] cookies) 
	{
	   // String errorMsg = null;
	    int status = HttpUrlConnectionUtil.STATUS_ERROR; //default to error status
	    try
	    {
	       status = HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, filename, cookies);
	       if (status != HttpUrlConnectionUtil.STATUS_OK)
	       {
	           log.error("Error! The tools export servlet threw an exception");
	           //throw new ExportPortfolioServletException(error);
	           //instead of throwing an exception, write the error to file instead
	           writeErrorMessageToFile(EXPORT_ERROR_MSG, directoryToStoreFile, EXPORT_ERROR_FILENAME);
	       }
	     
	    }
	    catch(MalformedURLException e)
		{
			//throw new ExportPortfolioServletException("The URL given is invalid. ",e);	        
	        log.error("The URL given is invalid. Exception message is: " + e);
	        writeErrorMessageToFile(EXPORT_ERROR_MSG, directoryToStoreFile, EXPORT_ERROR_FILENAME);
		}
		catch(FileNotFoundException e)
		{
			//throw new ExportPortfolioServletException("The directory or file may not exist. ",e);
		    log.error("The directory or file may not exist. Exception message is: " + e);
		    writeErrorMessageToFile(EXPORT_ERROR_MSG, directoryToStoreFile, EXPORT_ERROR_FILENAME);
		}
		catch(IOException e)
		{
		    log.error("A problem has occurred while writing file. Exception message is: " + e);
		    writeErrorMessageToFile(EXPORT_ERROR_MSG, directoryToStoreFile, EXPORT_ERROR_FILENAME);
			//throw new ExportPortfolioServletException("A problem has occurred while writing file. ", e);
		}
		
		return status;
		
	} 
	
	/**
	 * Helper method to generate the export portfolio url in learner mode.
	 * The required parameters are mode, toolSessionId and userId.
	 * The value of these parameters are read from the request.
	 * 
	 * @param request The HttpServletRequest
	 * @param url The url in which the parameters should be appended to
	 * @return	the final url with all the necessary parameters appended
	 */
	protected String appendParametersToLearnerExportURL(HttpServletRequest request, String url)
	{
		String finalURL;
		finalURL = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_MODE, WebUtil.readStrParam(request, AttributeNames.PARAM_MODE));
		finalURL = WebUtil.appendParameterToURL(finalURL, AttributeNames.PARAM_TOOL_SESSION_ID, WebUtil.readStrParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
		finalURL = WebUtil.appendParameterToURL(finalURL, AttributeNames.PARAM_USER_ID, WebUtil.readStrParam(request, AttributeNames.PARAM_USER_ID));
		
		return finalURL;
	}
	
	/**
	 * Helper method to generate the export portfolio url in teacher mode.
	 * The required parameters are mode and toolContentId
	 * The value of these parameters are read from the request.
	 * 
	 * @param request The HttpServletRequest
	 * @param url The url in which the parameters should be appended to
	 * @return	the final url with all the necessary parameters appended
	 */
	protected String appendParametersToTeacherExportURL(HttpServletRequest request, String url)
	{
		String finalURL;
		finalURL = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_MODE, WebUtil.readStrParam(request, AttributeNames.PARAM_MODE));
		finalURL = WebUtil.appendParameterToURL(finalURL, AttributeNames.PARAM_TOOL_CONTENT_ID, WebUtil.readStrParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));		
		return finalURL;
	}
	
	/**
	 * TODO: fix up this method so that it writes the error message
	 * which is obtained from a resource file, and not hardcoded.
	 * 
	 * Temporary helper method that generates the export file containing
	 * the warning message that the export could not be generated for this activity.
	 * Will need to write out html contents instead, and the message should be
	 * moved to a resource file.
	 * 
	 * This method 
	 * @param errorMessage
	 * @param directoryToStoreFile
	 * @param filename
	 */
	private void writeErrorMessageToFile(String errorMessage, String directoryToStoreFile, String filename)
	{
	    try
	    {
		    String filepath = directoryToStoreFile + File.separator + filename;
		    BufferedWriter fileout = new BufferedWriter(new FileWriter(filepath));
			fileout.write(errorMessage);
			fileout.close();
	    }
	    catch(IOException e)
	    {
	       // throw new ExportPortfolioServletException("Could not write error message to a file");
	    }
	}
	
	/**
	 * Helper method that checks that the required request parameters are available.
	 * If the mode=teacher, then it will check to see if toolContentID exists.
	 * If mode = learner, then it will check to see if toolSessionID and userID exists.
	 * Returns true if all needed parameters are there. False otherwise.
	 * 
	 * @param request
	 * @return
	 */
	private boolean parametersAreValid(HttpServletRequest request)
	{
	    boolean valid = true;
	    mode = request.getParameter(AttributeNames.PARAM_MODE);
	    if (mode != null)
	    {
		    if (mode.equals(ToolAccessMode.LEARNER.toString()))
		    {
		        userID = new Long(request.getParameter(AttributeNames.PARAM_USER_ID));
		        toolSessionID = new Long(request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID));
		        if (userID == null || toolSessionID == null)
		            valid = false;
		    }
		    else if(mode.equals(ToolAccessMode.TEACHER.toString()))
		    {
		        toolContentID = new Long(request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID));
		        if (toolContentID == null)
		            valid = false;
		    }
		    else 
		        valid = false;
	    }
	    else
	        valid = false;
	    
	    return valid;
	}
	
	

}
