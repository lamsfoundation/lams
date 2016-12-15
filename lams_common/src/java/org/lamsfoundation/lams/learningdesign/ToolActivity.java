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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.learningdesign.strategy.ToolActivityStrategy;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.planner.PedagogicalPlannerActivityMetadata;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.RequiredGroupMissingException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Manpreet Minhas
 *
 */
public class ToolActivity extends SimpleActivity implements Serializable {

    private static Logger log = Logger.getLogger(ToolActivity.class);

    /** Holds value of property toolContentId. */
    private Long toolContentId;

    /** Holds value of property tool. */
    private Tool tool;

    /** List of sessions associated with this ToolActivity */
    private Set<ToolSession> toolSessions;

    private Set<CompetenceMapping> competenceMappings;

    private Set<ActivityEvaluation> activityEvaluations;

    private Set<GradebookUserActivity> gradebookUserActivities;

    private PedagogicalPlannerActivityMetadata plannerMetadata;

    /** full constructor */
    public ToolActivity(Long activityId, Integer id, String description, String title, Integer xcoord, Integer ycoord,
	    Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary, Activity parentActivity,
	    Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign, Grouping grouping,
	    Integer activityTypeId, Transition transitionTo, Transition transitionFrom, String languageFile,
	    Boolean stopAfterActivity, Set inputActivities, Tool tool, Long toolContentId, Set branchActivityEntries,
	    Set<CompetenceMapping> competenceMappings, Set<ActivityEvaluation> activityEvaluations,
	    Set<GradebookUserActivity> gradebookUserActivities) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, branchActivityEntries);
	this.tool = tool;
	this.toolContentId = toolContentId;
	this.competenceMappings = competenceMappings;
	this.activityEvaluations = activityEvaluations;
	super.simpleActivityStrategy = new ToolActivityStrategy(this);
	this.gradebookUserActivities = gradebookUserActivities;
    }

    /** default constructor */
    public ToolActivity() {
	super.simpleActivityStrategy = new ToolActivityStrategy(this);
    }

    /** minimal constructor */
    public ToolActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Tool tool, Long toolContentId) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom);
	this.tool = tool;
	this.toolContentId = toolContentId;
	super.simpleActivityStrategy = new ToolActivityStrategy(this);
    }

    /**
     * Makes a copy of the ToolActivity for authoring, preview and monitoring
     * environment
     *
     * @return ToolActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {
	ToolActivity newToolActivity = new ToolActivity();
	copyToNewActivity(newToolActivity, uiidOffset);
	newToolActivity.setTool(this.getTool());
	newToolActivity.setToolContentId(this.getToolContentId());

	Set<CompetenceMapping> newCompetenceMappings = new HashSet<CompetenceMapping>();
	if (this.competenceMappings != null) {
	    for (CompetenceMapping compMap : this.competenceMappings) {
		CompetenceMapping newComp = new CompetenceMapping();
		newComp.setCompetence(compMap.getCompetence());
		newComp.setToolActivity(compMap.getToolActivity());
		newCompetenceMappings.add(compMap);
	    }
	}

	newToolActivity.setCompetenceMappings(newCompetenceMappings);

	Set<ActivityEvaluation> newEvaluations = new HashSet<ActivityEvaluation>();
	if (this.activityEvaluations != null) {
	    for (ActivityEvaluation evaluation : this.activityEvaluations) {
		ActivityEvaluation newEvaluation = new ActivityEvaluation();
		newEvaluation.setActivity(newToolActivity);
		newEvaluation.setToolOutputDefinition(evaluation.getToolOutputDefinition());
		newEvaluations.add(newEvaluation);
	    }
	}
	newToolActivity.setActivityEvaluations(newEvaluations);

	Set<GradebookUserActivity> newGradebookUserActivities = new HashSet<GradebookUserActivity>();
	if (this.gradebookUserActivities != null) {
	    for (GradebookUserActivity gradebookAct : this.gradebookUserActivities) {
		GradebookUserActivity newGradebookAct = new GradebookUserActivity();
		newGradebookAct.setActivity(newToolActivity);
		newGradebookAct.setLearner(gradebookAct.getLearner());
		newGradebookAct.setMark(gradebookAct.getMark());
		newGradebookAct.setUpdateDate(gradebookAct.getUpdateDate());
	    }
	}
	newToolActivity.setGradebookUserActivities(newGradebookUserActivities);

	if (this.plannerMetadata != null) {
	    PedagogicalPlannerActivityMetadata plannerMetadata = this.plannerMetadata.clone();
	    plannerMetadata.setActivity(newToolActivity);
	    newToolActivity.setPlannerMetadata(plannerMetadata);
	}

	return newToolActivity;
    }

    /**
     * Factory method to create a new tool session for a single user when he is running current activity. Does not check
     * to see if a tool session already exists.
     * <p>
     * If the activity has groupingSupportType = GROUPING_SUPPORT_NONE then a new tool session is created for each
     * learner.
     * <p>
     * If the activity has groupingSupportType = GROUPING_SUPPORT_REQUIRED then a new tool session is created for each
     * group of learners. It will fall back to a class group if no grouping is found - the user interface should not
     * have allowed this!
     * <p>
     * If the activity has groupingSupportType = GROUPING_SUPPORT_OPTIONAL then a new tool session is created for each
     * group of learners. If no grouping is available then a whole of class group is created.
     * <p>
     * If groupingSupportType is not set then defaults to GROUPING_SUPPORT_NONE. If for some reason a grouped session if
     * also does the equivalent of GROUPING_SUPPORT_NONE. This way the system will still function, if not as expected!
     * <p>
     *
     * @param learner
     *            the user who should be using this tool session.
     * @return the new tool session.
     */
    @SuppressWarnings("unchecked")
    public ToolSession createToolSessionForActivity(MessageService messageService, User learner, Lesson lesson)
	    throws RequiredGroupMissingException {
	Date now = new Date(System.currentTimeMillis());
	Integer supportType = getGroupingSupportType();
	ToolSession session = null;

	if (supportType != null
		&& (supportType.equals(GROUPING_SUPPORT_REQUIRED) || supportType.equals(GROUPING_SUPPORT_OPTIONAL))) {

	    // Both cases create a small group if a grouping exists, otherwise creates a class group.
	    Group group = null;
	    if (getApplyGrouping().booleanValue()) {
		group = this.getGroupFor(learner);

		//check if activity requires existing grouping but no group for user exists yet
		if (group == null || group.isNull()) {
		    String errorMessage = messageService.getMessage("error.requires.existing.grouping",
			    new Object[] { getActivityId(), learner.getUserId() });
		    throw new RequiredGroupMissingException(errorMessage);
		}

		for (ToolSession toolSession : (Set<ToolSession>) group.getToolSessions()) {
		    if (this.equals(toolSession.getToolActivity())) {
			session = toolSession;
			break;
		    }
		}

		if (session == null) {
		    session = new GroupedToolSession(this, now, ToolSession.STARTED_STATE, group, lesson);
		}
	    } else {
		LessonClass lessonClassGrouping = lesson.getLessonClass();
		group = this.getGroupFor(learner, lessonClassGrouping);

		if (group != null && !group.isNull()) {
		    session = new GroupedToolSession(this, now, ToolSession.STARTED_STATE, group, lesson);
		} else {
		    log.error("Unable to get a lesson class group for a new tool session for activity "
			    + getActivityId() + " and user " + learner + ". Falling back to one learner per session.");
		}
	    }
	}

	if (session == null) {
	    // Either GROUPING_SUPPORT_NONE was selected, supportType == null or the grouped tool sessions could not be created.
	    // So create one session per user.
	    session = new NonGroupedToolSession(this, now, ToolSession.STARTED_STATE, learner, lesson);
	}

	return session;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    /**
     * Getter for property toolContentId.
     *
     * @return Value of property toolContentId.
     */
    public Long getToolContentId() {

	return this.toolContentId;
    }

    /**
     * Setter for property toolContentId.
     *
     * @param toolContentId
     *            New value of property toolContentId.
     */
    public void setToolContentId(Long toolContentId) {

	this.toolContentId = toolContentId;
    }

    /**
     * Getter for property tool.
     *
     * @return Value of property tool.
     */
    public Tool getTool() {

	return this.tool;
    }

    /**
     * Setter for property tool.
     *
     * @param tool
     *            New value of property tool.
     */
    public void setTool(Tool tool) {

	this.tool = tool;
    }

    /**
     * @return Returns the toolSessions.
     */
    public Set<ToolSession> getToolSessions() {
	return toolSessions;
    }

    /**
     * @param toolSessions
     *            The toolSessions to set.
     */
    public void setToolSessions(Set<ToolSession> toolSessions) {
	this.toolSessions = toolSessions;
    }

    /**
     *
     * @return
     */
    public Set<CompetenceMapping> getCompetenceMappings() {
	return competenceMappings;
    }

    /**
     *
     * @param competenceMappings
     */
    public void setCompetenceMappings(Set<CompetenceMapping> competenceMappings) {
	this.competenceMappings = competenceMappings;
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

    /**
     * Get all the tool activities in this activity. Called by
     * Activity.getAllToolActivities() As we are a tool activity, just add
     * ourself.
     */
    @Override
    protected void getToolActivitiesInActivity(SortedSet toolActivities) {
	toolActivities.add(this);
    }

    public Set<ActivityEvaluation> getActivityEvaluations() {
	return activityEvaluations;
    }

    public void setActivityEvaluations(Set<ActivityEvaluation> activityEvaluations) {
	this.activityEvaluations = activityEvaluations;
    }

    public Set<GradebookUserActivity> getGradebookUserActivities() {
	return gradebookUserActivities;
    }

    public void setGradebookUserActivities(Set<GradebookUserActivity> gradebookUserActivities) {
	this.gradebookUserActivities = gradebookUserActivities;
    }

    public PedagogicalPlannerActivityMetadata getPlannerMetadata() {
	return plannerMetadata;
    }

    public void setPlannerMetadata(PedagogicalPlannerActivityMetadata plannerMetadata) {
	this.plannerMetadata = plannerMetadata;
    }
}
