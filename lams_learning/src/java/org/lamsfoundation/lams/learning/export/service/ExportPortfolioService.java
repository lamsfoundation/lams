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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

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
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;


/**
 * @author mtruong
 *
 */
public class ExportPortfolioService implements IExportPortfolioService {
 
    private static Logger log = Logger.getLogger(ExportPortfolioService.class);
    
	/** Two different starting points depending on whether the export was
	 * initiated by a teacher or learner
	 * 
	 */
	private ILamsCoreToolService lamsCoreToolService;
	private ITransitionDAO transitionDAO;
	private IActivityDAO activityDAO;
    private ILearnerService learnerService;
	
	
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
	 * @param lamsCoreToolService The lamsCoreToolService to set.
	 */
	public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
		this.lamsCoreToolService = lamsCoreToolService;
	}

		
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#getOrderedActivityList(org.lamsfoundation.lams.learningdesign.LearningDesign) */
	public Vector getOrderedActivityList(LearningDesign learningDesign)
	{
		if (learningDesign == null)
		{
			String error="the learningdesign is null. Cannot continue";
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
	
	
	
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportPortfolioForTeacher(org.lamsfoundation.lams.lesson.Lesson) */
	public Portfolio[] exportPortfolioForTeacher(Lesson lesson)
	{
		
		//iterate through each Activity and compile an activity id list.
		//call common method to call this set
		if (lesson==null)
		{
			String error="lesson cannot be null";
			throw new ExportPortfolioException(error);
		}
		Vector activities = getOrderedActivityList(lesson.getLearningDesign());
		Vector portfolios = null;
		Portfolio[] exports = null;
		Vector portfolioArray = null;
		try
		{
    		portfolios = setupPortfolios(activities, ToolAccessMode.TEACHER, null);	
    	
    		exports = doExport(portfolios, ExportPortfolioConstants.EXPORT_TMP_DIR);	    	
    		
		}
    	catch (LamsToolServiceException e)
		{
    		//throw new ExportPortfolioException(e);
		}
    	return exports;
	}
	

	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportPortfolioForStudent(java.lang.Long, org.lamsfoundation.lams.usermanagement.User,boolean) */
	public Portfolio[] exportPortfolioForStudent(Long learnerProgressId, User user, boolean anonymity)
	{
		if (learnerProgressId == null || user == null)
		{
			String error="learnerProgress or user is null";
			throw new ExportPortfolioException(error);
		}
		
		Vector activities = getOrderedActivityList(learnerProgressId);
	
		Vector portfolios = null;
		Portfolio[] exports = null;
		Vector portfolioArray = null;
		try
		{
    		portfolios = setupPortfolios(activities, ToolAccessMode.LEARNER, user);	
    	
    		exports = doExport(portfolios, ExportPortfolioConstants.EXPORT_TMP_DIR);	 
		}
    	catch (LamsToolServiceException e)
		{
    		//throw new ExportPortfolioException(e);
		}
    
    	return exports;
	}

	
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#setupPortfolios(java.util.Vector, org.lamsfoundation.lams.tool.ToolAccessMode, org.lamsfoundation.lams.usermanagement.User) */
	public Vector setupPortfolios(Vector sortedActivityList, ToolAccessMode accessMode, User user) throws LamsToolServiceException
	{
		if (sortedActivityList == null)
		{
			String error="the activityList is null";
			throw new ExportPortfolioException(error);
		}
		if (accessMode != ToolAccessMode.TEACHER && user == null )
		{
			String error="the user is null";
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
				
				//setup the export url
				if (accessMode == ToolAccessMode.LEARNER)
				{
					ToolSession toolSession = lamsCoreToolService.getToolSessionByLearner(user, activity);
					mapOfValuesToAppend.put(WebUtil.PARAM_MODE, ToolAccessMode.LEARNER.toString());
					mapOfValuesToAppend.put(WebUtil.PARAM_USER_ID_NEW, user.getUserId().toString());
					mapOfValuesToAppend.put(WebUtil.PARAM_TOOL_SESSION_ID, toolSession.getToolSessionId().toString());
				}
				else if (accessMode == ToolAccessMode.TEACHER)
				{
					mapOfValuesToAppend.put(WebUtil.PARAM_MODE, ToolAccessMode.TEACHER.toString());
					mapOfValuesToAppend.put("toolContentId", toolActivity.getToolContentId().toString());
				}
				
				String url = setupExportUrl(mapOfValuesToAppend, portfolio.getExportUrl());
				portfolio.setExportUrl(url);				
									
				portfolioList.add(portfolio);
			}
		}
		
		return portfolioList;
	}
	
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#createPortfolio(org.lamsfoundation.lams.learningdesign.ToolActivity) */
	public Portfolio createPortfolio(ToolActivity activity)
	{
		if (activity == null)
		{
			String error="activity is null";
			throw new ExportPortfolioException(error);
		}
		Portfolio p = new Portfolio();
		Tool tool = activity.getTool();	
		p.setActivityId(activity.getActivityId());
		p.setActivityName(tool.getToolDisplayName());
		p.setActivityDescription(activity.getTitle());
		p.setExportUrl(tool.getExportPortfolioUrl()); //remember to append parameters
		
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
			String error="the learnerProgressId is null";
			throw new ExportPortfolioException(error);
		}
		
		LearnerProgress learnerProgress = learnerService.getProgressById(learnerProgressId);
		Set activitySet = learnerProgress.getCompletedActivities();
		if (activitySet.size() == 0)
		{
			String error="Cannot export your portfolio. You have not completed any activities yet.";
			throw new ExportPortfolioException(error);
		} 
		
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
	
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#createTempDirectory(String) */
/*	public boolean createTempDirectory(String directoryName)
	{
		boolean created;
		try
		{
			created = FileUtil.createDirectory(directoryName);
	
		}
		catch (FileUtilException e)
		{
			throw new ExportPortfolioException("An error has occurred while creating a directory " + e);
		}
		
		return created;
	} */
	
	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#zipPortfolio(String, String) */
	public String zipPortfolio(String filename, String directoryToZip)
	{
		String zipfileName = null;
		try
		{
			zipfileName = ZipFileUtil.createZipFile(filename, directoryToZip);
		}
		catch(ZipFileUtilException e)
		{
			throw new ExportPortfolioException("An error has occurred while zipping up the directory " + e);
		}
		return zipfileName;
	}
	

	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#doExport(java.util.Vector, String) */
	public Portfolio[] doExport(Vector portfolios, String tempDirectoryName)
	{
		Iterator i = portfolios.iterator();
		String baseSubDirectoryName = "Activity";
		String activitySubDirectory;
		String exportURL;
		String toolLink;
		
		String mainFileName = null;
		//iterate through the list of portfolios, create subdirectory, 
		while(i.hasNext())
		{
			Portfolio portfolio = (Portfolio)i.next();
			//get the activityId so the subdirectory can be created
			String subDirectoryName = baseSubDirectoryName + portfolio.getActivityId().toString();
			if(!createSubDirectory(tempDirectoryName, subDirectoryName))
			{
				throw new ExportPortfolioException("The subdirectory could not be created");
			}
			// The directory in which the activity must place its files 
			activitySubDirectory = tempDirectoryName + File.separator + subDirectoryName; 
			portfolio.setDirectoryName(activitySubDirectory);
			//append directory name to export url call.
			exportURL = WebUtil.appendParameterToURL(portfolio.getExportUrl(), WebUtil.PARAM_DIRECTORY_NAME, activitySubDirectory);
			
			//append the host name to the export url for eg: http://localhost:8080/export_url
			String absoluteExportURL = ExportPortfolioConstants.HOST + exportURL;		
			
			//set the new exportURL
			portfolio.setExportUrl(absoluteExportURL);
				
			//get tool to export its files
			mainFileName = exportToolPortfolio(portfolio);
			
			toolLink = subDirectoryName + File.separator + mainFileName;
			portfolio.setMainFileName(mainFileName);
			portfolio.setToolLink(toolLink);
			
		//	break; //testing purposes only, only go through one iteration to make sure tool exports content
			
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
	

	/** @see org.lamsfoundation.lams.learning.export.service.IExportPortfolioService#exportToolPortfolio(org.lamsfoundation.lams.learning.export.Portfolio) */
	public String exportToolPortfolio(Portfolio tool)
	{
		String mainFileName = null;
		try
		{
			//URL url = new URL("http://localhost:8080/lams/tool/lanb11/portfolioExport?directoryName=D:\\tempExportDir\\Activity20&mode=teacher&toolContentId=355");
			URL url = new URL(tool.getExportUrl());
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			//the main file name is returned, or null
		
			mainFileName = input.readLine();
			
			input.close();
	
			return mainFileName;		
		
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
	
}
