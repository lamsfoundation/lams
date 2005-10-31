/***************************************************************************
* Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
* =============================================================
* 
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
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
* ***********************************************************************/


/*
 * Created on Aug 30, 2005
 *
 */
package org.lamsfoundation.lams.learning.export.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.lamsfoundation.lams.web.util.AttributeNames;




/**
 * @author mtruong
 *
 */
public class ExportPortfolioService implements IExportPortfolioService {
 
    private static Logger log = Logger.getLogger(ExportPortfolioService.class);
    
	private ILamsCoreToolService lamsCoreToolService;
	private ITransitionDAO transitionDAO;
	private IActivityDAO activityDAO;
	private IUserDAO userDAO;
    private ILearnerService learnerService;
    private ILessonDAO lessonDAO;
    private String exportTmpDir; //value assigned when the doExport method is executed
    
    
    /**
     * @return Returns the exportTmpDir.
     */
    public String getExportTmpDir() {
        return exportTmpDir;
    }
    /**
     * @param exportTmpDir The exportTmpDir to set.
     */
    public void setExportTmpDir(String exportTmpDir) {
        this.exportTmpDir = exportTmpDir;
    }
	/**
	 * @param learnerService The learnerService to set.
	 */
	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	/**
	 * @param transitionDAO The transitionDAO to set.
	 */
	public void setTransitionDAO(ITransitionDAO transitionDAO) {
		this.transitionDAO = transitionDAO;
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
		
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportPortfolioForTeacher(org.lamsfoundation.lams.lesson.Lesson) */
	public Portfolio[] exportPortfolioForTeacher(Long lessonId, Cookie[] cookies)
	{
	    Lesson lesson = lessonDAO.getLesson(lessonId);

	    Vector portfolios = null; //each portfolio contains information about its activity, ordered in the same sequence as the ordered activity list.
		Portfolio[] exports = null;
				
		if (lesson==null)
		{
			String error="The Lesson with lessonID " + lessonId + "is null.";
			log.error(error);
			throw new ExportPortfolioException(error);
		}
		Vector activities = getOrderedActivityList(lesson.getLearningDesign());
		
		try
		{
    		portfolios = setupPortfolios(activities, ToolAccessMode.TEACHER, null);	
    	
    		exports = doExport(portfolios, cookies);	    	
    		
		}
    	catch (LamsToolServiceException e)
		{
    		throw new ExportPortfolioException("An exception has occurred while generating portfolios. The error is: " + e);
		}
    	return exports;
	    
	   
	}
	

	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportPortfolioForStudent(java.lang.Long, org.lamsfoundation.lams.usermanagement.User,boolean) */
	public Portfolio[] exportPortfolioForStudent(Integer userId, Long lessonID, boolean anonymity, Cookie[] cookies)
	{
	    Vector portfolios = null;
		Portfolio[] exports = null;
		
	    User learner = userDAO.getUserById(userId);
	    Lesson lesson = lessonDAO.getLesson(lessonID);
	    
	    if (learner == null || lesson == null)
		{
			String error="The User object with userId" + userId + "or Lesson object with lessonId" + lessonID + " is null. Cannot Continue";
			log.error(error);
			throw new ExportPortfolioException(error);
		}
	    
	    LearnerProgress learnerProgress = learnerService.getProgress(learner, lesson);
	    
	    Vector activities = getOrderedActivityList(learnerProgress.getLearnerProgressId());
		
			
			try
			{
	    		portfolios = setupPortfolios(activities, ToolAccessMode.LEARNER, learner);	
	    	
	    		exports = doExport(portfolios, cookies);	 
			}
	    	catch (LamsToolServiceException e)
			{
	    		throw new ExportPortfolioException("An exception has occurred while generating portfolios. The error is: " + e);
			}
	    
	    	return exports;
	    
	}
	
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#getOrderedActivityList(org.lamsfoundation.lams.learningdesign.LearningDesign) */
	public Vector getOrderedActivityList(LearningDesign learningDesign)
	{
		if (learningDesign == null)
		{
			String error="the learningdesign is null. Cannot continue";
			log.error(error);
			throw new ExportPortfolioException(error);
		}
		HashMap activityTree = learningDesign.getActivityTree();				
		Vector v = new Vector();
		Activity nextActivity = learningDesign.getFirstActivity();
		
		while(nextActivity!=null){
			addActivityToVector(activityTree, v, nextActivity);
			nextActivity = transitionDAO.getNextActivity(nextActivity.getActivityId());	
		}				
		return v;
	}
	
    
	/**
	 * Used by getOrderedActivityList(LearningDesign)
	 * 
	 * @param activityTree
	 * @param v
	 * @param nextActivity
	 */
	private void addActivityToVector(HashMap activityTree, Vector v, Activity activity) {
		Set childActivities = (Set) activityTree.get(activity.getActivityId());			
		if(childActivities.size()!=0){
			// must have been a complex activity
			v.addAll(childActivities);
		}else{
			// must be a simple activity
			v.add(activity);
		}
	}
		
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#setupPortfolios(java.util.Vector, org.lamsfoundation.lams.tool.ToolAccessMode, org.lamsfoundation.lams.usermanagement.User) */
	public Vector setupPortfolios(Vector sortedActivityList, ToolAccessMode accessMode, User user) throws LamsToolServiceException
	{
		if (sortedActivityList == null)
		{
			String error="The ordered activity list is null. Cannot continue";
			log.error(error);
			throw new ExportPortfolioException(error);
		}
		if (accessMode != ToolAccessMode.TEACHER && user == null )
		{
			String error="Invalid User. User object is null. Cannot continue";
			log.error(error);
			throw new ExportPortfolioException(error);
		}
		
		
		Iterator i = sortedActivityList.iterator();
		Vector portfolioList = new Vector();
		
		Map mapOfValuesToAppend = new HashMap();
		while(i.hasNext())
		{		
			Activity activity = (Activity)i.next();
			
			if (activity.isToolActivity())
			{
				ToolActivity toolActivity = (ToolActivity)activityDAO.getActivityByActivityId(activity.getActivityId());
				Portfolio portfolio = createPortfolio(toolActivity);
				
				/*
				 * Append parameters to the export url.
				 * If the export is done by teacher: mode, toolContentId is appended
				 * If the export is done by learner: mode, userId, toolSessionId is appended
				 */
				if (accessMode == ToolAccessMode.LEARNER)
				{
					ToolSession toolSession = lamsCoreToolService.getToolSessionByActivity(user, toolActivity);
					if (toolSession == null)
					{
					    String error = "Cannot obtain the toolSessionId for this tool activity. The Tool Session for this user and activity is null. Cannot Continue";
					    log.error(error);
					    throw new ExportPortfolioException(error);
					}
					mapOfValuesToAppend.put(AttributeNames.PARAM_MODE, ToolAccessMode.LEARNER.toString());
					mapOfValuesToAppend.put(AttributeNames.PARAM_USER_ID, user.getUserId().toString());
					mapOfValuesToAppend.put("toolSessionId", toolSession.getToolSessionId().toString());
				}
				else if (accessMode == ToolAccessMode.TEACHER)
				{
					mapOfValuesToAppend.put(AttributeNames.PARAM_MODE, ToolAccessMode.TEACHER.toString());
					mapOfValuesToAppend.put("toolContentId", toolActivity.getToolContentId().toString());
				}
				
				String url = setupExportUrl(mapOfValuesToAppend, portfolio.getExportUrl());
				portfolio.setExportUrl(url);				
									
				portfolioList.add(portfolio); //add the portfolio at the end of the list.
			}
		}
		
		return portfolioList;
	}
	
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#createPortfolio(org.lamsfoundation.lams.learningdesign.ToolActivity) */
	public Portfolio createPortfolio(ToolActivity activity)
	{
		if (activity == null)
		{
			String error="Cannot create portfolio for this tool activity. Tool Activity is null";
			log.error(error);
			throw new ExportPortfolioException(error);
		}
		Portfolio p = new Portfolio();
		Tool tool = activity.getTool();	
		p.setActivityId(activity.getActivityId());
		p.setActivityName(tool.getToolDisplayName());
		p.setActivityDescription(activity.getTitle());
		p.setExportUrl(tool.getExportPortfolioUrl()); 
		
		return p;		
	}
	
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#setupExportUrl(java.util.Map, java.lang.String) */
	public String setupExportUrl(Map parametersToAppend, String url)
	{
		int mapSize = parametersToAppend.size();
		Iterator mapIterator = parametersToAppend.entrySet().iterator();
		String urlToReturn = url;
		for (int i=0; i< mapSize; i++)
		{
			Map.Entry entry = (Map.Entry)mapIterator.next();
			String parameterName = (String)entry.getKey();
			String parameterValue = (String)entry.getValue();
			urlToReturn = WebUtil.appendParameterToURL(urlToReturn, parameterName, parameterValue);
		}
		return urlToReturn;
	}
		
	

	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#getOrderedActivityList(java.lang.Long) */
	public Vector getOrderedActivityList(Long learnerProgressId)
	{
		if (learnerProgressId == null)
		{
			String error="Cannot obtain the ordered activity list for learner. The learner progress id is null,";
			log.error(error);
			throw new ExportPortfolioException(error);
		}
		
		LearnerProgress learnerProgress = learnerService.getProgressById(learnerProgressId);
		Set activitySet = learnerProgress.getCompletedActivities();
		if (activitySet.size() == 0)
		{
			String error="Cannot export the portfolio for learner with learner progress id " + learnerProgressId + ". Learner has not completed any activities yet.";
			log.error(error);
			throw new ExportPortfolioException(error);
		} 
		
		TreeSet sortedActivities = new TreeSet(new ActivityOrderComparator());
		sortedActivities.addAll(activitySet);
		
		Vector v = new Vector();
				 
		Iterator i = sortedActivities.iterator();
		
		while (i.hasNext())
		{
			Activity activity = (Activity)i.next();
			if (!activity.isComplexActivity())
			{
				v.add(activity);
			}
		}

	return v;
		
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
	

	public Portfolio[] doExport(Vector portfolios, Cookie[] cookies)
	{
		Iterator i = portfolios.iterator();
		String activitySubDirectory;
		String exportURL;
		String toolLink;		
		String mainFileName = null;
		String tempDirectoryName;
		
		//create the root directory for the export
		tempDirectoryName = createDirectory(ExportPortfolioConstants.DIR_SUFFIX_EXPORT);
		setExportTmpDir(tempDirectoryName);
		//iterate through the list of portfolios, create subdirectory, 
		while(i.hasNext())
		{
			Portfolio portfolio = (Portfolio)i.next();
			
			//create a subdirectory with the name ActivityXX where XX is the activityId
			String subDirectoryName = ExportPortfolioConstants.SUBDIRECTORY_BASENAME + portfolio.getActivityId().toString();
			if(!createSubDirectory(tempDirectoryName, subDirectoryName))
			{
				throw new ExportPortfolioException("The subdirectory " + subDirectoryName + " could not be created.");
			}
			
			// The directory in which the activity must place its files 
			activitySubDirectory = tempDirectoryName + File.separator + subDirectoryName; 
		
			//for security reasons, append the relative directory name to the end of the export url instead of the whole path
			String relativePath = activitySubDirectory.substring(ExportPortfolioConstants.TEMP_DIRECTORY.length()+1, activitySubDirectory.length());
			exportURL = WebUtil.appendParameterToURL(portfolio.getExportUrl(), AttributeNames.PARAM_DIRECTORY_NAME, relativePath);
			
			String absoluteExportURL = ExportPortfolioConstants.HOST + exportURL;		
					
			portfolio.setExportUrl(absoluteExportURL);
				
			//get tool to export its files, mainFileName is the name of the main HTML page that the tool exported.
			mainFileName = connectToToolViaExportURL(absoluteExportURL, cookies);
			
			if (mainFileName == null)
			{
			    log.error("The main export file returned by the tool activity was null. Tool activity id " + portfolio.getActivityId());
			    throw new ExportPortfolioException("The main file name the tool returned is null.Cannot continue with export.");
			}
			
			//toolLink is used in main page, so that it can link with the tools export pages.
			toolLink = subDirectoryName + "/" + mainFileName;	
			portfolio.setToolLink(toolLink);
			
			
		}
		
		return (Portfolio[])portfolios.toArray(new Portfolio[portfolios.size()]);
		
		
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
	public String connectToToolViaExportURL(String exportURL, Cookie[] cookies)
	{
	    try
	    {
	        return HttpUrlConnectionUtil.connectToToolExportURL(exportURL, cookies);
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
			throw new ExportPortfolioException("A problem has occurred while writing the contents of " + exportURL + " to file. ", e);
		}
		
	}
	
	//Methods that are not used
	
	/*	public Vector getOrderedActivityList(LearnerProgress learnerProgress)
	{
		if (learnerProgress == null)
		{
			String error="the learnerProgress is null";
			throw new ExportPortfolioException(error);
		}
		Set activitySet = learnerProgress.getCompletedActivities();
	//	if (activitySet.size() == 0)
		//{
			//String error="Cannot export your portfolio. You have not completed any activities yet.";
			//throw new ExportPortfolioException(error);
	//	} 
		TreeSet sortedActivities = new TreeSet(new ActivityOrderComparator());
    	sortedActivities.addAll(activitySet);
    	
		Vector v = new Vector();
		//sort the activities according to order_id or if not activityId
		
		 
    	Iterator i = sortedActivities.iterator();
		
		while (i.hasNext())
		{
			Activity activity = (Activity)i.next();
			if (!activity.isComplexActivity())
			{
				v.add(activity);
			}
		}
	
		return v;
	}
	*/
	
	/*
	public Portfolio[] exportPortfolioForStudent(LearnerProgress learnerProgress, User user, boolean anonymity)
	{
		if (learnerProgress == null || user == null)
		{
			String error="learnerProgress or user is null";
			throw new ExportPortfolioException(error);
		}
		//LearnerProgress progress = learnerService.getProgress(learner, lesson);
		Portfolio[] portfolios = null;
		Vector activities = getOrderedActivityList(learnerProgress);
    	
     	try
		{
    		portfolios = setupPortfolios(activities, ToolAccessMode.LEARNER, user);	
    		
		}
    	catch (LamsToolServiceException e)
		{
    		//throw new ExportPortfolioException(e);
		}
    	return portfolios;  	
		
	}
	
	public Portfolio[] exportPortfolioForStudent(Lesson lesson, User user, boolean anonymity)
	{
		if (lesson == null || user == null)
		{
			String error="learnerProgress or user is null";
			throw new ExportPortfolioException(error);
		}
		LearnerProgress learnerProgress = learnerService.getProgress(user, lesson);
		
		Portfolio[] portfolios = null;
		Vector activities = getOrderedActivityList(learnerProgress);
    	
     	try
		{
    		portfolios = setupPortfolios(activities, ToolAccessMode.LEARNER, user);	
		}
    	catch (LamsToolServiceException e)
		{
    		//throw new ExportPortfolioException(e);
		}
    	return portfolios;
    	
		
	} */
	public String getExportDir()
	{
	    return getExportTmpDir();
	}
	
   
}
