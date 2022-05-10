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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.learningdesign.strategy.ToolActivityStrategy;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.RequiredGroupMissingException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Manpreet Minhas
 *
 */
@Entity
@DiscriminatorValue("1")
public class ToolActivity extends SimpleActivity implements Serializable {

    private static final long serialVersionUID = -7500867438126908849L;

    private static Logger log = Logger.getLogger(ToolActivity.class);

    @Column(name = "tool_content_id")
    private Long toolContentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tool_id")
    private Tool tool;

    @OneToMany(mappedBy = "toolActivity")
    private Set<ToolSession> toolSessions = new HashSet<>();

    @OneToMany(mappedBy = "toolActivity")
    private Set<CompetenceMapping> competenceMappings = new HashSet<>();

    @OneToMany(mappedBy = "activity")
    private Set<GradebookUserActivity> gradebookUserActivities = new HashSet<>();

    @Transient
    private ActivityEvaluation evaluation;

    /** default constructor */
    public ToolActivity() {
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

	Set<CompetenceMapping> newCompetenceMappings = new HashSet<>();
	if (this.competenceMappings != null) {
	    for (CompetenceMapping compMap : this.competenceMappings) {
		CompetenceMapping newComp = new CompetenceMapping();
		newComp.setCompetence(compMap.getCompetence());
		newComp.setToolActivity(compMap.getToolActivity());
		newCompetenceMappings.add(compMap);
	    }
	}

	newToolActivity.setCompetenceMappings(newCompetenceMappings);

	ActivityEvaluation evaluation = (ActivityEvaluation) UserManagementService.getInstance()
		.findById(ActivityEvaluation.class, this.getActivityId());
	if (evaluation != null) {
	    ActivityEvaluation newEvaluation = new ActivityEvaluation();
	    newEvaluation.setToolOutputDefinition(evaluation.getToolOutputDefinition());
	    newEvaluation.setWeight(evaluation.getWeight());
	    newEvaluation.setActivity(newToolActivity);
	    newToolActivity.evaluation = newEvaluation;
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
		    log.warn("Activity " + getActivityId() + " requires existing grouping but no group for user "
			    + learner.getUserId() + " exists yet.");
		    String errorMessage = messageService.getMessage("error.requires.existing.grouping");
		    throw new RequiredGroupMissingException(errorMessage);
		}

		for (ToolSession toolSession : group.getToolSessions()) {
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

    public Long getToolContentId() {

	return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {

	this.toolContentId = toolContentId;
    }

    public Tool getTool() {

	return this.tool;
    }

    public void setTool(Tool tool) {

	this.tool = tool;
    }

    public Set<ToolSession> getToolSessions() {
	return toolSessions;
    }

    public void setToolSessions(Set<ToolSession> toolSessions) {
	this.toolSessions = toolSessions;
    }

    public Set<CompetenceMapping> getCompetenceMappings() {
	return competenceMappings;
    }

    public void setCompetenceMappings(Set<CompetenceMapping> competenceMappings) {
	this.competenceMappings = competenceMappings;
    }

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
    protected void getToolActivitiesInActivity(SortedSet<ToolActivity> toolActivities) {
	toolActivities.add(this);
    }

    public ActivityEvaluation getEvaluation() {
	return evaluation;
    }

    public Set<GradebookUserActivity> getGradebookUserActivities() {
	return gradebookUserActivities;
    }

    public void setGradebookUserActivities(Set<GradebookUserActivity> gradebookUserActivities) {
	this.gradebookUserActivities = gradebookUserActivities;
    }

}