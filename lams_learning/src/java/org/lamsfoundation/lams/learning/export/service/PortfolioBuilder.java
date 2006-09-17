/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $Id$ */

package org.lamsfoundation.lams.learning.export.service;

import java.util.ArrayList;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.export.ActivityPortfolio;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.SystemToolActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class PortfolioBuilder extends LearningDesignProcessor {

    private static Logger log = Logger.getLogger(PortfolioBuilder.class);

    ArrayList<ActivityPortfolio> mainPortfolioList;
	ArrayStack activityListStack;
	ArrayList<ActivityPortfolio> currentPortfolioList;
	ToolAccessMode accessMode;
	ILamsCoreToolService lamsCoreToolService;
	User user;
	LearnerProgress progress;
	Lesson lesson;

	/** Create the builder. Supply all the data that will be needed to parse the design and build the portfolio entries. 
	 * 
	 * If accessMode == LEARNER then progress and user must not be null. This will create a list of portfolio objects for 
	 * all activities that the LEARNER has completed or attempted.
	 * 
	 * If accessMode == TEACHER then progress and user will be null and all activities will be included. Note: Because 
	 * the progress is null we can't rely on getting the lesson from the progress.
	 * 
	 * @param design
	 * @param activityDAO
	 * @param lamsCoreToolService
	 * @param accessMode
	 * @param lesson 
	 * @param progress
	 * @param user
	 */
	public PortfolioBuilder(LearningDesign design, IActivityDAO activityDAO, 
			ILamsCoreToolService lamsCoreToolService, ToolAccessMode accessMode, 
			Lesson lesson, LearnerProgress progress, User user) {
		super(design, activityDAO);
		this.mainPortfolioList = new ArrayList<ActivityPortfolio>();
		this.currentPortfolioList = mainPortfolioList;
		this.activityListStack = new ArrayStack(5);
		this.accessMode = accessMode;
		this.lamsCoreToolService = lamsCoreToolService;
		
		this.user = user;
		this.lesson = lesson;
		this.progress = progress;
	}

	/** Prepares to process children */
	public void startComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
		// Create a new current activity list, putting the old current one on the stack. 
		activityListStack.push(currentPortfolioList);
		currentPortfolioList = new ArrayList<ActivityPortfolio>();
		
	}

	/** Creates an ActivityPortfolio and sets up the list of its children. Doesn't create an entry if there are no children. */
	public void endComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
		ActivityPortfolio complexPortfolio = null;
		
		if ( currentPortfolioList.size()>0 ) {
			complexPortfolio = createActivityPortfolio(activity);
			complexPortfolio.setChildPortfolios(currentPortfolioList);
		}
		
		currentPortfolioList = (ArrayList<ActivityPortfolio>) activityListStack.pop();
		if ( complexPortfolio != null )
			currentPortfolioList.add(complexPortfolio);
	}

	public void startSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
		// everything done by endSimpleActivity
	}

	/** Creates an ActivityPortfolio. */
	public void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
		
		// if learner only include the attempted and completed activities
		// if learner don't include gates
		if (accessMode == ToolAccessMode.LEARNER) {
			if ( activity.isGateActivity() || 
					! ( progress.getCompletedActivities().contains(activity) || progress.getAttemptedActivities().contains(activity)) ) {
				return;
			}
		}

		ActivityPortfolio p = createActivityPortfolio(activity);
		String exportUrlForTool = null;
		
		if ( activity.isToolActivity() ) {
			
			/* if the tool does not have an export url, use the url that points to the servlet that generates a page saying that the export is not supported */
			ToolActivity toolActivity = (ToolActivity) activity;
			Tool tool = toolActivity.getTool();	
			if (accessMode == ToolAccessMode.LEARNER)
				exportUrlForTool = tool.getExportPortfolioLearnerUrl();
			else 
				exportUrlForTool = tool.getExportPortfolioClassUrl();
			
			/*
			 * Append parameters to the export url.
			 * If the export is done by teacher: mode, toolContentId is appended
			 * If the export is done by learner: mode, userId, toolSessionId is appended
			 */
			if ( exportUrlForTool != null ) {
				if (accessMode == ToolAccessMode.LEARNER)
				{
					exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_USER_ID, user.getUserId().toString());
					ToolSession toolSession = lamsCoreToolService.getToolSessionByActivity(user, toolActivity);
					if (toolSession != null) {
						exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_TOOL_SESSION_ID, toolSession.getToolSessionId().toString());
					}
				}
				else if (accessMode == ToolAccessMode.TEACHER)
				{
					exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_TOOL_CONTENT_ID, toolActivity.getToolContentId().toString());
				}
			}
			
		} else if ( activity.isSystemToolActivity() ) {
			// only include the gates in the teacher version - learner gate has been avoided at the beginning of this method
			// always include grouping
			// system tools don't use the modes or the tool session id but need to add them or the validation fails in 
			// AbstractExportPortfolio servlet. So why leave the validation there - because system tools are the exception
			// rather than the rule. In the future, they may need these parameters.
			SystemToolActivity sysToolActivity = (SystemToolActivity) activity;
			SystemTool tool = sysToolActivity.getSystemTool();
			if ( tool != null ) {
				if (accessMode == ToolAccessMode.LEARNER) {
					exportUrlForTool = tool.getExportPortfolioLearnerUrl();
				} else { 
					exportUrlForTool = tool.getExportPortfolioClassUrl();
				}
			}
			if ( exportUrlForTool != null ) {
				if (accessMode == ToolAccessMode.LEARNER) {
					exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_USER_ID, user.getUserId().toString());
					exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_TOOL_SESSION_ID, "0");
				} else { 
					exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_TOOL_CONTENT_ID, "0");
				}
				exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_ACTIVITY_ID, activity.getActivityId().toString());
				exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_LESSON_ID, lesson.getLessonId().toString());
			}
		} 
		
		if ( exportUrlForTool == null ) {
			exportUrlForTool = ExportPortfolioConstants.URL_FOR_UNSUPPORTED_EXPORT;
			exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, "activityTitle", activity.getTitle());
			exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, "lessonName", lesson.getLessonName());
		}
		p.setExportUrl(exportUrlForTool);
		
		currentPortfolioList.add(p);
		
	}

	/**
	 * Obtains the Tool from the ToolActivity and creates a portfolio object with properties activityId, activityName, 
	 * activityDescription, exportURL set to the value of the ToolActivity's properties activityId, toolDisplayName 
	 * (retrieved from Tool object), title, exportPortfolioUrl respestively.
	 * 
	 * @param activity The Tool Activity
	 * @return a Portfolio object
	 */
	protected ActivityPortfolio createActivityPortfolio(Activity activity)
	{
		if (activity == null)
		{
			String error="Cannot create portfolio for this activity as the activity is null.";
			log.error(error);
			throw new ExportPortfolioException(error);
		}
		ActivityPortfolio p = new ActivityPortfolio();
		p.setActivityId(activity.getActivityId());
		p.setActivityName(activity.getTitle());
		p.setActivityDescription(activity.getDescription());
		return p;		
	}

	/** Get the list of all the activity portfolios, which in turn may contain other activity portfolios */
	public ArrayList<ActivityPortfolio> getPortfolioList() {
		return mainPortfolioList;
	}
	
}
