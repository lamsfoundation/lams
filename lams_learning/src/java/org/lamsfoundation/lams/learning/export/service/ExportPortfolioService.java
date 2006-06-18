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
package org.lamsfoundation.lams.learning.export.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.export.ActivityPortfolio;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;




/**
 * Generates the learner and teacher portfolios. These are designed to be offline versions of a sequence,
 * so that learners and teachers can refer to them after the lesson has finished.
 * 
 * In general, anything that the learner sees should be in their portfolio. The teacher version 
 * should have the details for all students in the lesson.
 * 
 * @author mtruong,fmalikoff
 *
 */
public class ExportPortfolioService implements IExportPortfolioService {
 
    private static Logger log = Logger.getLogger(ExportPortfolioService.class);
    
	private ILamsCoreToolService lamsCoreToolService;
	private IActivityDAO activityDAO;
	private IUserDAO userDAO;
    private ILearnerService learnerService;
    private ILessonDAO lessonDAO;
    protected MessageService messageService;
    
  
	/**
	 * @param learnerService The learnerService to set.
	 */
	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	/**
     * @param activityDAO The activityDAO to set.
     */
    public void setActivityDAO(IActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }
    
    /**
     * @param lessonDAO The lessonDAO to set.
     */
    public void setLessonDAO(ILessonDAO lessonDAO) {
        this.lessonDAO = lessonDAO;
    }
    
	/**
	 * @param lamsCoreToolService The lamsCoreToolService to set.
	 */
	public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
		this.lamsCoreToolService = lamsCoreToolService;
	}

	/**
     * @param userDAO The userDAO to set.
     */
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
		
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportPortfolioForTeacher(org.lamsfoundation.lams.lesson.Lesson) */
	public Portfolio exportPortfolioForTeacher(Long lessonId, Cookie[] cookies)
	{
	    Lesson lesson = lessonDAO.getLesson(lessonId);

	    ArrayList<ActivityPortfolio> portfolios = null; //each portfolio contains information about its activity, ordered in the same sequence as the ordered activity list.
	    Portfolio exports = null;
				
		if (lesson != null)
		{
			try
			{
		   		PortfolioBuilder builder = new PortfolioBuilder(lesson.getLearningDesign(), activityDAO, lamsCoreToolService, ToolAccessMode.TEACHER, null, null);
		   		builder.parseLearningDesign();
	    		portfolios = builder.getPortfolioList();

	    		exports = doExport(portfolios, cookies,lesson);	    	
	    		
			}
	    	catch (LamsToolServiceException e)
			{
	    	    /** TODO avoid throwing exceptions if possible */
	    		throw new ExportPortfolioException("An exception has occurred while generating portfolios. The error is: " + e);
			}
		}
		else
		{
		    String error="The Lesson with lessonID " + lessonId + "is null.";
			log.error(error);
			/*
			 * Instead of throwing an exception, create the Portfolio object with 
			 * the ToolPortfolio[] set to null. Set the value of exportTmpDir using
			 * createDirectory() method, so that the main file can be generated in 
			 * that directory.
			 */
		    exports = createPortfolioIndicatingErrorHasOccurred(lesson); 
		}
		
    	return exports;
	    
	   
	}
	

	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportPortfolioForStudent(java.lang.Long, org.lamsfoundation.lams.usermanagement.User,boolean) */
	public Portfolio exportPortfolioForStudent(Integer userId, Long lessonID, boolean anonymity, Cookie[] cookies)
	{
		ArrayList<ActivityPortfolio> portfolios = null;
	    Portfolio exports = null;
		
	    User learner = userDAO.getUserById(userId);
	    Lesson lesson = lessonDAO.getLesson(lessonID);
	
	    if (learner != null && lesson != null)
	    {
	        LearnerProgress learnerProgress = learnerService.getProgress(learner.getUserId(), lesson.getLessonId());
	        
		    if (learnerProgress != null)
		    {

				try
				{
			   		PortfolioBuilder builder = new PortfolioBuilder(lesson.getLearningDesign(), activityDAO, lamsCoreToolService, ToolAccessMode.LEARNER, learnerProgress, learner);
			   		builder.parseLearningDesign();
		    		portfolios = builder.getPortfolioList();
		    		
		    		if ( portfolios.size() >= 0 ) {
			    		exports = doExport(portfolios, cookies,lesson);	 
			    		
		    		} else {
				        log.error("The learner has not completed or attempted any activities");
		    		}

				}
		    	catch (LamsToolServiceException e)
				{
		    	    log.error("An exception has occurred while generating portfolios.",e);
		        }
		    }
		    else
		    {
		        log.error("The LearnerProgress cannot be found for userId " + userId + " participating in lessonId " + lessonID);
		    }
	    }
	    else
	    {
			log.error("The User object with userId" + userId + "or Lesson object with lessonId" + lessonID + " is null. Cannot Continue");
	    }
	    
	    if ( exports == null ) {
			exports = createPortfolioIndicatingErrorHasOccurred(lesson);
	    }
	    
	    return exports;
	       
	    
	}
	
	 private Portfolio createPortfolioIndicatingErrorHasOccurred(Lesson lesson)
	 {
	     String tempDir = createDirectory(ExportPortfolioConstants.DIR_SUFFIX_EXPORT);
	     File dir = new File(tempDir);
		 String exportID = dir.getName();

	     Portfolio dummyPortfolio = new Portfolio(exportID);
	     dummyPortfolio.setExportTmpDir(tempDir);
	     dummyPortfolio.setLessonDescription(lesson.getLessonDescription());
	     dummyPortfolio.setLessonName(lesson.getLessonName());
	     return dummyPortfolio;
	 }
	 
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#zipPortfolio(String, String) */
	public String zipPortfolio(String filename, String directoryToZip)
	{
		String zipfileName, dirToPutZip;
		//create tmp dir to put zip file
		
		try
		{
		    dirToPutZip = FileUtil.createTempDirectory(ExportPortfolioConstants.DIR_SUFFIX_ZIP);
			zipfileName = ZipFileUtil.createZipFile(filename, directoryToZip, dirToPutZip);
		}
		catch(FileUtilException e)
		{
		    throw new ExportPortfolioException("Cannot create the temporary directory for this export", e);
		}
		catch(ZipFileUtilException e)
		{
		    throw new ExportPortfolioException("An error has occurred while zipping up the directory ", e);
		}
		return zipfileName;
	}
	
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#doExport(Vector, Cookie[]) */
	public Portfolio doExport(ArrayList<ActivityPortfolio> portfolios, Cookie[] cookies, Lesson lesson)
	{		
		String activitySubDirectory;
		String exportURL;
		String toolLink;		
		String mainFileName = null;
		String tempDirectoryName;
		
		//create the root directory for the export
		tempDirectoryName = createDirectory(ExportPortfolioConstants.DIR_SUFFIX_EXPORT);
		File dir = new File(tempDirectoryName);
		String exportID = dir.getName();
		
		Portfolio portfolio = new Portfolio(exportID);
		portfolio.setExportTmpDir(tempDirectoryName);
		portfolio.setLessonName(lesson.getLessonName());
		portfolio.setLessonDescription(lesson.getLessonDescription());
		
		Iterator i = portfolios.iterator();
		//iterate through the list of portfolios, create subdirectory, 
		while(i.hasNext())
		{
			ActivityPortfolio activityPortfolio = (ActivityPortfolio)i.next();
			
			//create a subdirectory with the name ActivityXX where XX is the activityId
			String subDirectoryName = ExportPortfolioConstants.SUBDIRECTORY_BASENAME + activityPortfolio.getActivityId().toString();
			if(!createSubDirectory(tempDirectoryName, subDirectoryName))
			{
			    throw new ExportPortfolioException("The subdirectory " + subDirectoryName + " could not be created.");
			}
			
			// The directory in which the activity must place its files 
			activitySubDirectory = tempDirectoryName + File.separator + subDirectoryName; 
		
			//for security reasons, append the relative directory name to the end of the export url instead of the whole path
			String relativePath = activitySubDirectory.substring(FileUtil.TEMP_DIR.length()+1, activitySubDirectory.length());
			exportURL = WebUtil.appendParameterToURL(activityPortfolio.getExportUrl(), AttributeNames.PARAM_DIRECTORY_NAME, relativePath);
			
			String absoluteExportURL = ExportPortfolioConstants.HOST + exportURL;		
					
			activityPortfolio.setExportUrl(absoluteExportURL);
				
			//get tool to export its files, mainFileName is the name of the main HTML page that the tool exported.
			mainFileName = connectToToolViaExportURL(absoluteExportURL, cookies, activitySubDirectory);
						
			//toolLink is used in main page, so that it can link with the tools export pages.
			toolLink = subDirectoryName + "/" + mainFileName;	
			activityPortfolio.setToolLink(toolLink);
			
			
		}
		
		portfolio.setActivityPortfolios((ActivityPortfolio[])portfolios.toArray(new ActivityPortfolio[portfolios.size()]));
		return portfolio;
		
		
	}
	
	/**
	 * Helper method which calls the FileUtil to create a subdirectory. This method might not be needed,
	 * can be used why calling the FileUtil directly.
	 * @param parentDir The name of the parent directory
	 * @param subDir The name of the child directory to create.
	 * @return true is the subdirectory was created, false otherwise
	 */
	private boolean createSubDirectory(String parentDir, String subDir)
	{
		boolean created;
		try
		{
			created = FileUtil.createDirectory(parentDir, subDir);
	
		}
		catch (FileUtilException e)
		{
		    /** TODO avoid throwing exceptions if possible */
			throw new ExportPortfolioException("An error has occurred while creating the sub directory " + subDir);
		}
		
		return created;
	}
	
	/**
	 * Helper method which calls the FileUtil to create directory. 
	 * It will check whether the directory exists, if so, then it will 
	 * overwrite the existing directory. (It is assumed that the cron jobs 
	 * should delete the directory + files anyway, after the export is done)
	 * @param parentDir The name of the parent directory
	 * @param subDir The name of the child directory to create.
	 * @return true is the subdirectory was created, false otherwise
	 */
	private String createDirectory(String name)
	{
	    String tmpDir=null;
	    try
	    {
	        tmpDir = FileUtil.createTempDirectory(name);
	    }
	    catch(FileUtilException e)
	    {
	       throw new ExportPortfolioException("Unable to create temporary directory " + name + " for export.", e);
	    }
	    return tmpDir;
	}
	

	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportToolPortfolio(org.lamsfoundation.lams.learning.export.Portfolio) */
	public String connectToToolViaExportURL(String exportURL, Cookie[] cookies, String directoryToStoreErrorFile)
	{
	    String filename = null;
	    try
	    {
	        filename =  HttpUrlConnectionUtil.connectToToolExportURL(exportURL, cookies);
	        if (filename == null)
	        {
	           filename = ExportPortfolioConstants.EXPORT_ERROR_FILENAME;
	           log.error("A problem has occurred while connecting to the tool's export url. The export url may be invalid or may not exist");
	           writeErrorMessageToFile(directoryToStoreErrorFile);	           
	        }      
	          
	    }	    
		catch(MalformedURLException e)
		{
		    log.error("The URL "+exportURL+" given is invalid.",e);
	        writeErrorMessageToFile(directoryToStoreErrorFile);	
		}
		catch(FileNotFoundException e)
		{
		    log.error("The directory "+directoryToStoreErrorFile+" may not exist.",e);
	        writeErrorMessageToFile(directoryToStoreErrorFile);	
		}
		catch(IOException e)
		{
		    log.error("A problem has occurred while writing the contents of " + exportURL + " to file.", e);
	        writeErrorMessageToFile(directoryToStoreErrorFile);	
			
		}
		
		return filename;
	}
	
	/** Generate the main page, given this portfolio */
	public void generateMainPage(HttpServletRequest request, Portfolio portfolio, Cookie[] cookies) {

		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		String url = basePath + "/exportPortfolio/main.jsp";
		
		String filename = ExportPortfolioConstants.MAIN_EXPORT_FILENAME;				   
	    try
	    {
	    	request.getSession().setAttribute("portfolio", portfolio);
		    HttpUrlConnectionUtil.writeResponseToFile(url, portfolio.getExportTmpDir(), filename, cookies);
	    	request.getSession().removeAttribute("portfolio");
	    }	    
		catch(MalformedURLException e)
		{
		    log.error("The URL given is invalid. Exception Message was: " +e);
	        writeErrorMessageToFile(portfolio.getExportTmpDir());	
		}
		catch(FileNotFoundException e)
		{
		    log.error("The directory or file may not exist. Exception Message was: " +e);
	        writeErrorMessageToFile(portfolio.getExportTmpDir());	
		}
		catch(IOException e)
		{
		    log.error("A problem has occurred while writing the contents of " + url + " to file. Exception Message was: " +e);
	        writeErrorMessageToFile(portfolio.getExportTmpDir());	
			
		}
	}
	
	 private void writeErrorMessageToFile(String directoryToStoreFile)
	 {
	     try
		 {	
	    	 String errorMessage = messageService.getMessage(ExportPortfolioConstants.EXPORT_ACTIVITY_ERROR_KEY);
	         String filepath = directoryToStoreFile + File.separator +  ExportPortfolioConstants.EXPORT_ERROR_FILENAME;
			 BufferedWriter fileout = new BufferedWriter(new FileWriter(filepath));
			 fileout.write(errorMessage);
			 fileout.close();
		 }
		 catch(IOException e)
		 {
			 log.error("Exception occured trying to write out error message to file.",e);
		 }
	}
	 
}
