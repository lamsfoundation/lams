/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.gradebook.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradeBookUserActivity;
import org.lamsfoundation.lams.gradebook.GradeBookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradeBookDAO;
import org.lamsfoundation.lams.gradebook.dto.GradeBookActivityDTO;
import org.lamsfoundation.lams.gradebook.dto.GradeBookGridRow;
import org.lamsfoundation.lams.gradebook.dto.GradeBookUserDTO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * @author lfoxton
 * 
 * This service handles all gradebook-related service calls
 * 
 */
public class GradeBookService implements IGradeBookService {

    private static Logger logger = Logger.getLogger(GradeBookService.class);

    // Services 
    private IMonitoringService monitoringService;
    private ILamsCoreToolService toolService;
    private IGradeBookDAO gradeBookDAO;

    /**
     * Gets the user gradebook and outputs data for a given activity
     * 
     * @param lesson
     * @param learner
     * @return Collection<GradeBookGridRow>
     */
    @SuppressWarnings("unchecked")
    public Collection<GradeBookGridRow> getUserGradeBookActivityDTOs(Lesson lesson, User learner) {

	logger.debug("Getting gradebook data for lesson: " + lesson.getLessonId() + ". For user: "
		+ learner.getUserId());

	Collection<GradeBookGridRow> gradeBookActivityDTOs = new ArrayList<GradeBookGridRow>();

	LearnerProgress learnerProgress = monitoringService.getLearnerProgress(learner.getUserId(), lesson
		.getLessonId());

	Set<Activity> activities = (Set<Activity>) lesson.getLearningDesign().getActivities();

	/*
	 * Hibernate CGLIB is failing to load the first activity in
	 * the sequence as a ToolActivity for some mysterious reason
	 * Causes a ClassCastException when you try to cast it, even
	 * if it is a ToolActivity.
	 * 
	 * THIS IS A HACK to retrieve the first tool activity 
	 * manually so it can be cast as a ToolActivity - if it is one
	 */
	Activity firstActivity = monitoringService.getActivityById(lesson.getLearningDesign().getFirstActivity()
		.getActivityId());

	if (firstActivity.isToolActivity() && firstActivity instanceof ToolActivity) {
	    GradeBookActivityDTO activityDTO = getGradeBookActivityDTO(firstActivity, learner, learnerProgress);
	    gradeBookActivityDTOs.add(activityDTO);
	}

	for (Activity activity : activities) {
	    if (activity.getActivityId().longValue() != firstActivity.getActivityId().longValue()) {
		GradeBookActivityDTO activityDTO = getGradeBookActivityDTO(activity, learner, learnerProgress);
		gradeBookActivityDTOs.add(activityDTO);
	    }
	}
	return gradeBookActivityDTOs;
    }

    /**
     * Gets the gradebook data for a user for a given activity
     * 
     * @param activity
     * @param learner
     * @param learnerProgress
     * @return GradeBookActivityDTO
     */
    public GradeBookActivityDTO getGradeBookActivityDTO(Activity activity, User learner, LearnerProgress learnerProgress) {

	logger.debug("Getting gradebook data for activity: " + activity.getActivityId() + ". For user: "
		+ learner.getUserId());

	GradeBookActivityDTO gactivityDTO = new GradeBookActivityDTO();
	gactivityDTO.setActivityId(activity.getActivityId());
	gactivityDTO.setActivityTitle(activity.getTitle());

	GradeBookUserActivity gradeBookActivity = gradeBookDAO.getGradeBookUserDataForActivity(
		activity.getActivityId(), learner.getUserId());
	if (gradeBookActivity != null) {
	    gactivityDTO.setMark(gradeBookActivity.getMark());
	}
	if (learnerProgress != null) {
	    byte progressState = learnerProgress.getProgressState(activity);

	    if (progressState == LearnerProgress.ACTIVITY_COMPLETED) {
		gactivityDTO.setStatus("COMPLETED");
	    } else if (progressState == LearnerProgress.ACTIVITY_ATTEMPTED) {
		gactivityDTO.setStatus("ATTEMPTED");
	    } else {
		gactivityDTO.setStatus("NOT ATTEMPTED");
	    }
	} else {
	    gactivityDTO.setStatus("NOT ATTEMPTED");
	}

	if (activity.isToolActivity() && activity instanceof ToolActivity) {
	    ToolActivity toolAct = (ToolActivity) activity;

	    // Get the competences for this activity
	    Set<CompetenceMapping> competenceMappings = toolAct.getCompetenceMappings();
	    String competenceMappingsStr = "";
	    if (competenceMappings != null) {
		for (CompetenceMapping mapping : competenceMappings) {
		    competenceMappingsStr += mapping.getCompetence().getTitle() + ", ";
		}

		// trim the last comma off
		if (competenceMappingsStr.length() > 0) {
		    competenceMappingsStr = competenceMappingsStr.substring(0, competenceMappingsStr.lastIndexOf(","));
		}
	    }
	    gactivityDTO.setCompetences(competenceMappingsStr);

	    // Get the tool outputs for this user if there are any
	    ToolSession toolSession = toolService.getToolSessionByLearner(learner, toolAct);
	    if (toolSession != null) {
		SortedMap<String, ToolOutputDefinition> map = toolService.getOutputDefinitionsFromTool(toolAct
			.getToolContentId());

		Set<ToolOutput> toolOutputs = new HashSet<ToolOutput>();

		// Set the activityLearner URL for this gradebook activity
		gactivityDTO.setActivityUrl(Configuration.get(ConfigurationKeys.SERVER_URL)
			+ toolAct.getTool().getLearnerProgressUrl() + "&userID=" + learner.getUserId()
			+ "&toolSessionID=" + toolSession.getToolSessionId().toString());

		// Setting the tool outputs
		String toolOutputsStr = "";
		for (String outputName : map.keySet()) {

		    try {
			ToolOutput toolOutput = toolService.getOutputFromTool(outputName, toolSession, learner
				.getUserId());

			if (toolOutput != null) {
			    toolOutputs.add(toolOutput);
			    toolOutputsStr += toolOutput.getDescription() + ": " + toolOutput.getValue().getString()
				    + "<br />";
			}

		    } catch (RuntimeException e) {
			logger.debug("Runtime exception when attempted to get outputs for activity: "
				+ toolAct.getActivityId() + ", continuing for other activities", e);
		    }
		}
		toolOutputsStr = (toolOutputsStr.equals("")) ? "No output available." : toolOutputsStr;
		gactivityDTO.setOutput(toolOutputsStr);
	    }
	}

	return gactivityDTO;
    }

    /**
     * Returns the gradebook data for an entire lesson, which is essentailly a
     * list of GradeBookUserDTOs
     * 
     * @param lesson
     * @return ArrayList<GradeBookUserDTO>
     */
    @SuppressWarnings("unchecked")
    public ArrayList<GradeBookUserDTO> getGradeBookLessonData(Lesson lesson) {

	ArrayList<GradeBookUserDTO> gradeBookUserDTOs = new ArrayList<GradeBookUserDTO>();

	if (lesson != null) {
	    Set<User> learners = (Set<User>) lesson.getAllLearners();

	    if (learners != null) {

		for (User learner : learners) {
		    GradeBookUserDTO gradeBookUserDTO = new GradeBookUserDTO();
		    gradeBookUserDTO.setLogin(learner.getLogin());
		    gradeBookUserDTO.setFirstName(learner.getFirstName());
		    gradeBookUserDTO.setLastName(learner.getLastName());

		    // Setting the status for the user's lesson
		    gradeBookUserDTO.setStatus("NOT STARTED");
		    LearnerProgress learnerProgress = monitoringService.getLearnerProgress(learner.getUserId(), lesson
			    .getLessonId());
		    if (learnerProgress != null) {
			if (learnerProgress.isComplete()) {
			    gradeBookUserDTO.setStatus("FINISHED");
			} else if (learnerProgress.getAttemptedActivities() != null
				&& learnerProgress.getAttemptedActivities().size() > 0) {
			    gradeBookUserDTO.setStatus("STARTED");
			}
		    }

		    GradeBookUserLesson gradeBookUserLesson = gradeBookDAO.getGradeBookUserDataForLesson(lesson
			    .getLessonId(), learner.getUserId());
		    if (gradeBookUserLesson != null && gradeBookUserLesson.getMark() != null) {
			gradeBookUserDTO.setTotalLessonMark(gradeBookUserLesson.getMark());
		    } else {
			gradeBookUserDTO.setTotalLessonMark(0.0);
		    }
		    gradeBookUserDTOs.add(gradeBookUserDTO);
		}
	    }
	}

	return gradeBookUserDTOs;

    }

    /**
     * Updates a user's mark for an entire lesson
     * 
     * @param lesson
     * @param learner
     * @param mark
     */
    public void updateUserLessonGradeBookData(Lesson lesson, User learner, Double mark) {
	if (lesson != null && learner != null) {
	    GradeBookUserLesson gradeBookUserLesson = gradeBookDAO.getGradeBookUserDataForLesson(lesson.getLessonId(),
		    learner.getUserId());

	    if (gradeBookUserLesson == null) {
		gradeBookUserLesson = new GradeBookUserLesson();
		gradeBookUserLesson.setLearner(learner);
		gradeBookUserLesson.setLesson(lesson);
	    }
	    gradeBookUserLesson.setMark(mark);
	    gradeBookDAO.insertOrUpdate(gradeBookUserLesson);
	}
    }

    /**
     * Updates a user mark for an activity, when then aggregates their total
     * lesson mark
     * 
     * @param lesson
     * @param learner
     * @param activity
     * @param mark
     */
    public void updateUserActivityGradeBookData(Lesson lesson, User learner, Activity activity, Double mark) {
	if (lesson != null && activity != null && learner != null && activity.isToolActivity()) {
	    
	    // First, update the mark for the activity
	    GradeBookUserActivity gradeBookUserActivity = gradeBookDAO.getGradeBookUserDataForActivity(activity
		    .getActivityId(), learner.getUserId());

	    if (gradeBookUserActivity == null) {
		gradeBookUserActivity = new GradeBookUserActivity();
		gradeBookUserActivity.setLearner(learner);
		gradeBookUserActivity.setActivity((ToolActivity) activity);
	    }

	    gradeBookUserActivity.setMark(mark);
	    gradeBookDAO.insertOrUpdate(gradeBookUserActivity);

	    // Now update the lesson mark
	    GradeBookUserLesson gradeBookUserLesson = gradeBookDAO.getGradeBookUserDataForLesson(lesson.getLessonId(),
		    learner.getUserId());

	    if (gradeBookUserLesson == null) {
		gradeBookUserLesson = new GradeBookUserLesson();
		gradeBookUserLesson.setLearner(learner);
		gradeBookUserLesson.setLesson(lesson);
	    } 
	    
	    aggregateTotalMarkForLesson(gradeBookUserLesson);
	}
    }

    private void aggregateTotalMarkForLesson(GradeBookUserLesson gradeBookUserLesson) {
	Double totalMark = gradeBookDAO.getGradeBookUserActivityMarkSum(gradeBookUserLesson.getLesson().getLessonId(),
		gradeBookUserLesson.getLearner().getUserId());
	gradeBookUserLesson.setMark(totalMark);
	gradeBookDAO.insertOrUpdate(gradeBookUserLesson);
    }

    public IMonitoringService getMonitoringService() {
	return monitoringService;
    }

    public void setMonitoringService(IMonitoringService monitoringService) {
	this.monitoringService = monitoringService;
    }

    public ILamsCoreToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsCoreToolService toolService) {
	this.toolService = toolService;
    }

    public IGradeBookDAO getGradeBookDAO() {
	return gradeBookDAO;
    }

    public void setGradeBookDAO(IGradeBookDAO gradeBookDAO) {
	this.gradeBookDAO = gradeBookDAO;
    }
}
