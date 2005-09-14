/*
 * Created on Aug 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.export.service;

import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.lesson.LearnerProgress;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;

import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.ToolAccessMode;

import org.lamsfoundation.lams.util.WebUtil;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;

import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.service.ILearnerService;

import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExportPortfolioService implements IExportPortfolioService {
 
	
	/** Two different starting points depending on whether the export was
	 * initiated by a teacher or learner
	 * 
	 */
	private ILamsCoreToolService lamsCoreToolService;
	private ITransitionDAO transitionDAO;
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
	 * @param lamsCoreToolService The lamsCoreToolService to set.
	 */
	public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
		this.lamsCoreToolService = lamsCoreToolService;
	}

	/** returns a Portfolio array , change from void to Portfolio[]*/
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
		Portfolio[] portfolios = null;
		
		try
		{
    		portfolios = setupPortfolios(activities, ToolAccessMode.TEACHER, null);	
		}
    	catch (LamsToolServiceException e)
		{
    		//throw new ExportPortfolioException(e);
		}
    	return portfolios;
	}
	
	/** returns a Portfolio array , change from void to Portfolio[]*/
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
    	
		
	}
	
	public Portfolio[] exportPortfolioForStudent(Long learnerProgressId, User user, boolean anonymity)
	{
		if (learnerProgressId == null || user == null)
		{
			String error="learnerProgress or user is null";
			throw new ExportPortfolioException(error);
		}
		//LearnerProgress progress = learnerService.getProgress(learner, lesson);
		Portfolio[] portfolios = null;
		Vector activities = getOrderedActivityList(learnerProgressId);
    	
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
	
	
	
	/** return an array of portfolio objects that can be used int he web layer and display appropriate information **/
	public Portfolio[] setupPortfolios(Vector sortedActivityList, ToolAccessMode accessMode, User user) throws LamsToolServiceException//the iterator will traverse the set in ascending element order
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
		List portfolioList = new ArrayList();
		
		Map mapOfValuesToAppend = new HashMap();
		while(i.hasNext())
		{		
			Activity activity = (Activity)i.next();
			
		//	if (!(activity instanceof ComplexActivity))
			if (activity.isToolActivity())
			{
				ToolActivity toolActivity = (ToolActivity)activity;
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
					mapOfValuesToAppend.put(WebUtil.PARAM_TOOL_CONTENT_ID, toolActivity.getToolContentId().toString());
				}
				
				String url = setupExportUrl(mapOfValuesToAppend, portfolio.getExportUrl());
				portfolio.setExportUrl(url);
									
				portfolioList.add(portfolio);
			}
		}
		
		return (Portfolio[])portfolioList.toArray(new Portfolio[portfolioList.size()]);
		
	}
	/**
	 * all information would be the same, except when setting up the url,
	 * if export was done by a teacher, then the mode and toolContentId is appended
	 * if the export was done by a student , then the mode and toolSessionId and userId is appended
	 * @param activity
	 * @return
	 */
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
		
	public Vector getOrderedActivityList(LearningDesign learningDesign)
	{
		if (learningDesign == null)
		{
			String error="the learningdesign is null. Cannot continue";
			throw new ExportPortfolioException(error);
		}
		HashMap activityTree = learningDesign.getActivityTree();				
		Vector v = new Vector();
		Activity firstActivity = learningDesign.getFirstActivity();
		if(firstActivity.isComplexActivity()){			
			ComplexActivity complexActivity = (ComplexActivity)firstActivity;			
			Iterator childIterator = complexActivity.getActivities().iterator();
			while(childIterator.hasNext()){
				//SimpleActivity simpleActivity= (SimpleActivity)childIterator.next();
				//v.add(simpleActivity);	
				
				Activity simpleActivity= (Activity)childIterator.next();
				v.add(simpleActivity);
			}		
		}else{
			SimpleActivity simpleActivity = (SimpleActivity)firstActivity;
			v.add(simpleActivity);
		}
		
		Activity nextActivity = transitionDAO.getNextActivity(firstActivity.getActivityId());
		while(nextActivity!=null){
			Set childActivities = (Set) activityTree.get(nextActivity.getActivityId());			
			if(childActivities.size()!=0){
				Iterator iterator = childActivities.iterator();
				while(iterator.hasNext()){					
					/*SimpleActivity simpleActivity= (SimpleActivity)iterator.next();
					v.add(simpleActivity);*/
					Activity simpleActivity= (Activity)iterator.next();
					v.add(simpleActivity);
				}
			}else{
				/*SimpleActivity simpleActivity = (SimpleActivity)nextActivity;
				v.add(simpleActivity);*/
			
				v.add(nextActivity);
			}
			
			nextActivity = transitionDAO.getNextActivity(nextActivity.getActivityId());	
		}				
		return v;
	}

	public Vector getOrderedActivityList(LearnerProgress learnerProgress)
	{
		if (learnerProgress == null)
		{
			String error="the learnerProgress is null";
			throw new ExportPortfolioException(error);
		}
		Set activitySet = learnerProgress.getCompletedActivities();
	/*	if (activitySet.size() == 0)
		{
			String error="Cannot export your portfolio. You have not completed any activities yet.";
			throw new ExportPortfolioException(error);
		} */
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
	
	public boolean createTempDirectory(String directoryName)
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
	}
	
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
	
}
