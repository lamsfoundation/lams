/*
 * Created on Sep 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.web.servlet;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.web.servlet.ExportPortfolioServletException;


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
	
	protected final String TOOL_SESSION_ID = "toolSessionId";
	protected final String TOOL_CONTENT_ID = "toolContentId";
	protected final String USER_ID = "userId";

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		String mainFileName = null;
		String directoryName = null;
		String mode = null;
		
		Cookie[] cookies = request.getCookies();		
		
		directoryName = WebUtil.readStrParam(request, WebUtil.PARAM_DIRECTORY_NAME); 
		
		//put the path together again, since the given directory was a relative one.
		String absoluteDirectoryPath = FileUtil.TEMP_DIR + File.separator + directoryName;
	
		if (log.isDebugEnabled()) {
			log.debug("Directory name to store files is "+directoryName);
		}
		
		//check if the directory name has any trailing slashes, if so, remove it and return the new value
		directoryName = checkDirectoryName(directoryName);
		
	
		//check if the directory exists, if not throw exception
		if (!FileUtil.directoryExist(absoluteDirectoryPath))
			throw new IOException("The directory supplied " + absoluteDirectoryPath + " does not exist.");
		
		mode = WebUtil.readStrParam(request, WebUtil.PARAM_MODE);			
		
		if (log.isDebugEnabled()) {
			log.debug("Export is conducted in mode: " + mode);
		}
				
		mainFileName = doExport(request, response, absoluteDirectoryPath, cookies);
		
		if (log.isDebugEnabled()) {
			log.debug("The name of main html file is "+mainFileName);
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
	 * @param filename
	 */
	protected int writeResponseToFile(String urlToConnectTo, String directoryToStoreFile, String filename, Cookie[] cookies) throws ExportPortfolioServletException
	{
	  
	    try
	    {
	       int status = HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, filename, cookies);
	       if (status != HttpUrlConnectionUtil.STATUS_OK)
	       {
	           throw new ExportPortfolioServletException("error");
	       }
	      return status;
	    }
	    catch(MalformedURLException e)
		{
			throw new ExportPortfolioServletException("The URL given is invalid. ",e);
		}
		catch(FileNotFoundException e)
		{
			throw new ExportPortfolioServletException("The directory or file may not exist. ",e);
		}
		catch(IOException e)
		{
			throw new ExportPortfolioServletException("A problem has occurred while writing file. ", e);
		}
		
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
		finalURL = WebUtil.appendParameterToURL(url, WebUtil.PARAM_MODE, WebUtil.readStrParam(request, WebUtil.PARAM_MODE));
		finalURL = WebUtil.appendParameterToURL(finalURL, TOOL_SESSION_ID, WebUtil.readStrParam(request, TOOL_SESSION_ID));
		finalURL = WebUtil.appendParameterToURL(finalURL, USER_ID, WebUtil.readStrParam(request, USER_ID));
		
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
		finalURL = WebUtil.appendParameterToURL(url, WebUtil.PARAM_MODE, WebUtil.readStrParam(request, WebUtil.PARAM_MODE));
		finalURL = WebUtil.appendParameterToURL(finalURL, TOOL_CONTENT_ID, WebUtil.readStrParam(request, TOOL_CONTENT_ID));		
		return finalURL;
	}

}
