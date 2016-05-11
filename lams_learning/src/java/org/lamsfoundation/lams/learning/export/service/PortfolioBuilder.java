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


package org.lamsfoundation.lams.learning.export.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.export.ActivityPortfolio;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
import org.lamsfoundation.lams.learning.export.NotebookPortfolio;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ISystemToolActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
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
    ArrayList<NotebookPortfolio> mainNotebookList;
    ArrayStack activityListStack;
    ArrayList<ActivityPortfolio> currentPortfolioList;
    ArrayList<NotebookPortfolio> currentNotebookList;
    ToolAccessMode accessMode;
    ILamsCoreToolService lamsCoreToolService;
    ICoreNotebookService coreNotebookService;
    User user;
    LearnerProgress progress;
    Lesson lesson;

    /**
     * Create the builder. Supply all the data that will be needed to parse the design and build the portfolio entries.
     *
     * If accessMode == LEARNER then progress and user must not be null. This will create a list of portfolio objects
     * for all activities that the LEARNER has completed or attempted.
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
    public PortfolioBuilder(LearningDesign design, IActivityDAO activityDAO, ILamsCoreToolService lamsCoreToolService,
	    ICoreNotebookService coreNotebookService, ToolAccessMode accessMode, Lesson lesson,
	    LearnerProgress progress, User user) {
	super(design, activityDAO);
	mainPortfolioList = new ArrayList<ActivityPortfolio>();
	mainNotebookList = new ArrayList<NotebookPortfolio>();
	currentPortfolioList = mainPortfolioList;
	currentNotebookList = mainNotebookList;
	activityListStack = new ArrayStack(5);
	this.accessMode = accessMode;
	this.lamsCoreToolService = lamsCoreToolService;
	this.coreNotebookService = coreNotebookService;

	this.user = user;
	this.lesson = lesson;
	this.progress = progress;

    }

    /** Prepares to process children */
    @Override
    public boolean startComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
	// Create a new current activity list, putting the old current one on the stack.
	// An exception to this is when we reach a branching activity in learner mode. The children are added to the
	// current list.
	boolean flattenLearnerBranching = (activity.isBranchingActivity()
		|| activity.isSequenceActivity() && activity.getParentActivity().isBranchingActivity())
		&& accessMode == ToolAccessMode.LEARNER;

	if (!flattenLearnerBranching) {
	    activityListStack.push(currentPortfolioList);
	    currentPortfolioList = new ArrayList<ActivityPortfolio>();
	}

	return true;
    }

    /**
     * Creates an ActivityPortfolio and sets up the list of its children. Doesn't create an entry if there are no
     * children.
     */
    @Override
    public void endComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {

	ActivityPortfolio complexPortfolio = null;

	boolean flattenLearnerBranching = (activity.isBranchingActivity()
		|| activity.isSequenceActivity() && activity.getParentActivity().isBranchingActivity())
		&& accessMode == ToolAccessMode.LEARNER;

	if (currentPortfolioList.size() > 0 || accessMode == ToolAccessMode.TEACHER) {
	    complexPortfolio = createActivityPortfolio(activity);

	    if (currentPortfolioList.size() > 0) {
		complexPortfolio.setChildPortfolios(currentPortfolioList);
	    }

	    if (activity.isSystemToolActivity()) {
		String exportURL = getExportURLForSystemTool(activity);
		complexPortfolio = complexPortfolio == null ? createActivityPortfolio(activity) : complexPortfolio;
		complexPortfolio.setExportUrl(exportURL);
		// if there isn't a url, we assume we just want a heading
		complexPortfolio.setHeadingNoPage(exportURL == null);
	    } else {
		// sequence, parallel, etc
		complexPortfolio.setHeadingNoPage(true);
	    }
	}

	if (!flattenLearnerBranching) {
	    currentPortfolioList = (ArrayList<ActivityPortfolio>) activityListStack.pop();
	}

	if (complexPortfolio != null && !flattenLearnerBranching) {
	    currentPortfolioList.add(complexPortfolio);
	}

    }

    @Override
    public void startSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
	// everything done by endSimpleActivity
    }

    /** Creates an ActivityPortfolio. */
    @Override
    public void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {

	// if learner only include the attempted and completed activities
	if (accessMode == ToolAccessMode.LEARNER && !(progress.getCompletedActivities().containsKey(activity)
		|| progress.getAttemptedActivities().containsKey(activity))) {
	    return;
	}

	String exportUrlForTool = null;

	if (activity.isToolActivity()) {

	    // if the tool does not have an export url, then the processor will include an "not supported" error page

	    ToolActivity toolActivity = (ToolActivity) activity;
	    Tool tool = toolActivity.getTool();
	    if (accessMode == ToolAccessMode.TEACHER) {
		exportUrlForTool = tool.getExportPortfolioClassUrl();
	    } else {
		exportUrlForTool = tool.getExportPortfolioLearnerUrl();
	    }

	    /*
	     * Append parameters to the export url. If the export is done by teacher: mode, toolContentId is appended If
	     * the export is done by learner: mode, userId, toolSessionId is appended
	     */
	    if (exportUrlForTool != null) {
		if (accessMode == ToolAccessMode.LEARNER) {
		    exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_USER_ID,
			    user.getUserId().toString());
		    ToolSession toolSession = lamsCoreToolService.getToolSessionByActivity(user, toolActivity);
		    if (toolSession != null) {
			exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool,
				AttributeNames.PARAM_TOOL_SESSION_ID, toolSession.getToolSessionId().toString());
		    }
		} else if (accessMode == ToolAccessMode.TEACHER) {
		    exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool,
			    AttributeNames.PARAM_TOOL_CONTENT_ID, toolActivity.getToolContentId().toString());
		}
	    }

	    ActivityPortfolio p = createActivityPortfolio(activity);
	    p.setExportUrl(exportUrlForTool);
	    if (toolActivity.getCompetenceMappings() != null) {
		Set<String> competencesCovered = p.getCompetencesCovered();
		for (CompetenceMapping mapping : toolActivity.getCompetenceMappings()) {
		    competencesCovered.add(mapping.getCompetence().getTitle());
		}
	    }

	    currentPortfolioList.add(p);

	} else if (activity.isSystemToolActivity()) {

	    exportUrlForTool = getExportURLForSystemTool(activity);
	    if (exportUrlForTool != null) {
		ActivityPortfolio p = createActivityPortfolio(activity);
		p.setExportUrl(exportUrlForTool);
		currentPortfolioList.add(p);
	    }
	}

    }

    /**
     * A system tool may be a simple or a complex activity, so the logic is in a method called by both
     * endSimpleActivity() and endComplexActivity(). Only include the gates and branching in the teacher version, but
     * always include grouping. This is controlled by the urls in the db - if there isn't a url in the system table then
     * we assume that there shouldn't be a screen.
     * <p>
     * System tools don't use the modes or the tool session id but need to add them or the validation fails in
     * AbstractExportPortfolio servlet. So why leave the validation there - because system tools are the exception
     * rather than the rule. In the future, they may need these parameters.
     *
     * @param activity:
     *            an activity which also meets the ISystemToolActivity interface
     * @param exportUrlForTool
     */
    private String getExportURLForSystemTool(Activity activity) {

	// At present the sequence activity always has a page, which is a bit redundant for when it is a branch.
	// If we just want the sequence activity to be a heading when it is a branch (rather than in an optional
	// activity)
	// then uncomment this code.
	// if ( activity.isSequenceActivity() && activity.getParentActivity() != null &&
	// activity.getParentActivity().isBranchingActivity() ) {
	// return null;
	// }

	String exportUrlForTool = null;
	ISystemToolActivity sysToolActivity = (ISystemToolActivity) activity;
	SystemTool tool = sysToolActivity.getSystemTool();

	if (tool != null) {
	    if (accessMode == ToolAccessMode.LEARNER) {
		exportUrlForTool = tool.getExportPortfolioLearnerUrl();
	    } else {
		exportUrlForTool = tool.getExportPortfolioClassUrl();
	    }
	}
	if (exportUrlForTool != null) {
	    if (accessMode == ToolAccessMode.LEARNER) {
		exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_USER_ID,
			user.getUserId().toString());
		exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_TOOL_SESSION_ID,
			"0");
	    } else {
		exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_TOOL_CONTENT_ID,
			"0");
	    }
	    exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_ACTIVITY_ID,
		    activity.getActivityId().toString());
	    exportUrlForTool = WebUtil.appendParameterToURL(exportUrlForTool, AttributeNames.PARAM_LESSON_ID,
		    lesson.getLessonId().toString());
	}
	return exportUrlForTool;
    }

    /**
     * Process all Notebook (Scratchpad) entries into portfolio objects.
     *
     * @param notebookAccessMode
     *            The access mode that determines what entries are returned.
     */
    public void processNotebook(ToolAccessMode notebookAccessMode) throws ExportPortfolioException {
	List entries = null;

	if (lesson == null || user == null) {
	    throw new ExportPortfolioException();
	}

	if (notebookAccessMode == null) {
	    entries = coreNotebookService.getEntry(lesson.getLessonId(), CoreNotebookConstants.SCRATCH_PAD,
		    user.getUserId());
	} else if (notebookAccessMode.equals(ToolAccessMode.TEACHER)) {
	    entries = coreNotebookService.getEntry(lesson.getLessonId(), CoreNotebookConstants.SCRATCH_PAD,
		    CoreNotebookConstants.JOURNAL_SIG, user.getUserId());
	}

	if (entries == null) {
	    throw new ExportPortfolioException();
	}

	Iterator it = entries.iterator();

	while (it.hasNext()) {
	    NotebookEntry entry = (NotebookEntry) it.next();
	    NotebookPortfolio portfolio = createNotebookPortfolio(entry);
	    currentNotebookList.add(portfolio);
	}
    }

    /**
     * Obtains the Tool from the ToolActivity and creates a portfolio object with properties activityId, activityName,
     * activityDescription, exportURL set to the value of the ToolActivity's properties activityId, toolDisplayName
     * (retrieved from Tool object), title, exportPortfolioUrl respestively.
     *
     * @param activity
     *            The Tool Activity
     * @return a Portfolio object
     */
    protected ActivityPortfolio createActivityPortfolio(Activity activity) {
	if (activity == null) {
	    String error = "Cannot create portfolio for this activity as the activity is null.";
	    PortfolioBuilder.log.error(error);
	    throw new ExportPortfolioException(error);
	}
	ActivityPortfolio p = new ActivityPortfolio();
	p.setActivityId(activity.getActivityId());
	p.setActivityName(activity.getTitle());
	p.setActivityDescription(activity.getDescription());
	p.setFloating(activity.isFloatingActivity());
	return p;
    }

    /**
     * Creates a portfolio object with properties title and entry from the NotebookEntry.
     *
     * @param entry
     *            The Notebook Entry
     * @return a Portfolio object
     */
    protected NotebookPortfolio createNotebookPortfolio(NotebookEntry entry) {
	if (entry == null) {
	    String error = "Cannot create portfolio for this notebook entry as the entry is null.";
	    PortfolioBuilder.log.error(error);
	    throw new ExportPortfolioException(error);
	}

	NotebookPortfolio p = new NotebookPortfolio();
	p.setEntry(entry.getEntry());
	p.setTitle(entry.getTitle());
	p.setCreated(entry.getCreateDate());
	p.setModified(entry.getLastModified());

	if (entry.getExternalSignature().equals(CoreNotebookConstants.JOURNAL_SIG)) {
	    p.setTeacherViewable(true);
	} else {
	    p.setTeacherViewable(false);
	}

	return p;
    }

    /** Get the list of all the activity portfolios, which in turn may contain other activity portfolios */
    public ArrayList<ActivityPortfolio> getPortfolioList() {
	return mainPortfolioList;
    }

    /**
     * Get the list of notebook entries
     *
     * @return
     */
    public ArrayList<NotebookPortfolio> getNotebookList() {
	return mainNotebookList;
    }

}
