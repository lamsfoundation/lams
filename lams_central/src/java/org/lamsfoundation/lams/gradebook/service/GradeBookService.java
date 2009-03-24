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
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradeBookUserActivity;
import org.lamsfoundation.lams.gradebook.GradeBookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradeBookDAO;
import org.lamsfoundation.lams.gradebook.dto.GBActivityGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradeBookGridRowDTO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.tool.OutputType;
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
     * Gets a list of GradeBookGridRowDTO for a given lesson and learner
     * 
     * The result should be used for the "user view" of gradebook, where users
     * are listed, then expanding a sub-grid will bring up the activites
     * 
     * the mark applies to the ebture kessib
     * 
     * @param lesson
     * @param learner
     * @return Collection<GradeBookGridRow>
     */
    @SuppressWarnings("unchecked")
    public List<GradeBookGridRowDTO> getUserGradeBookActivityDTOs(Lesson lesson, User learner) {

	logger.debug("Getting gradebook user data for lesson: " + lesson.getLessonId() + ". For user: "
		+ learner.getUserId());

	List<GradeBookGridRowDTO> gradeBookActivityDTOs = new ArrayList<GradeBookGridRowDTO>();

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
	    GBActivityGridRowDTO activityDTO = getGradeBookActivityDTO(firstActivity, learner, learnerProgress);
	    gradeBookActivityDTOs.add(activityDTO);
	}

	for (Activity activity : activities) {
	    if (activity.getActivityId().longValue() != firstActivity.getActivityId().longValue()) {
		GBActivityGridRowDTO activityDTO = getGradeBookActivityDTO(activity, learner, learnerProgress);
		gradeBookActivityDTOs.add(activityDTO);
	    }
	}
	return gradeBookActivityDTOs;
    }

    /**
     * Gets a list of GradeBookGridRowDTO for a given lesson and learner
     * 
     * The result should be used for the "user view" of gradebook, where users
     * are listed, then expanding a sub-grid will bring up the activites
     * 
     * the mark applies to the activity
     * 
     * @param lesson
     * @param learner
     * @return Collection<GradeBookGridRow>
     */
    @SuppressWarnings("unchecked")
    public List<GBUserGridRowDTO> getUserGradeBookActivityDTOs(Lesson lesson, Activity activity) {

	List<GBUserGridRowDTO> gradeBookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	Set<User> learners = (Set<User>) lesson.getAllLearners();

	if (learners != null) {
	    for (User learner : learners) {
		GBUserGridRowDTO gUserDTO = new GBUserGridRowDTO();
		gUserDTO.setFirstName(learner.getFirstName());
		gUserDTO.setLastName(learner.getLastName());
		gUserDTO.setLogin(learner.getLogin());

		GradeBookUserActivity gradeBookUserActivity = gradeBookDAO.getGradeBookUserDataForActivity(activity
			.getActivityId(), learner.getUserId());

		// Set the progress
		LearnerProgress learnerProgress = monitoringService.getLearnerProgress(learner.getUserId(), lesson
			.getLessonId());
		gUserDTO.setStatus(getActivityStatusStr(learnerProgress, activity));


		// Set the outputs and activity url, if there is one
		if (activity.isToolActivity() && activity instanceof ToolActivity) {
		    ToolActivity toolAct = (ToolActivity) activity;
		    // Get the tool outputs for this user if there are any
		    ToolSession toolSession = toolService.getToolSessionByLearner(learner, toolAct);
		    if (toolSession != null) {
			// Set the activityLearner URL for this gradebook activity
			gUserDTO.setActivityUrl(Configuration.get(ConfigurationKeys.SERVER_URL)
				+ toolAct.getTool().getLearnerProgressUrl() + "&userID=" + learner.getUserId()
				+ "&toolSessionID=" + toolSession.getToolSessionId().toString());

			gUserDTO.setOutput(this.getToolOutputsStr(toolAct, toolSession, learner));

		    }
		}

		// Add marks and feedback
		if (gradeBookUserActivity != null) {
		    gUserDTO.setFeedback(gradeBookUserActivity.getFeedback());
		    gUserDTO.setMark(gradeBookUserActivity.getMark());

		}
		gradeBookUserDTOs.add(gUserDTO);
	    }
	}

	return gradeBookUserDTOs;

    }

    /**
     * Gets the user gradebook and outputs data for a given lesson, which is a
     * list of GBActivityGridRowDTO, this one is specifically for the user view
     * 
     * @param lesson
     * @param learner
     * @return Collection<GradeBookGridRow>
     */
    @SuppressWarnings("unchecked")
    public List<GradeBookGridRowDTO> getUserGradeBookActivityDTOsActivityView(Lesson lesson, User learner) {

	logger.debug("Getting gradebook user data for lesson: " + lesson.getLessonId() + ". For user: "
		+ learner.getUserId());

	List<GradeBookGridRowDTO> gradeBookActivityDTOs = new ArrayList<GradeBookGridRowDTO>();

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
	    GBActivityGridRowDTO activityDTO = getGradeBookActivityDTO(firstActivity, learner, learnerProgress);
	    gradeBookActivityDTOs.add(activityDTO);
	}

	for (Activity activity : activities) {
	    if (activity.getActivityId().longValue() != firstActivity.getActivityId().longValue()) {
		GBActivityGridRowDTO activityDTO = getGradeBookActivityDTO(activity, learner, learnerProgress);
		gradeBookActivityDTOs.add(activityDTO);
	    }
	}
	return gradeBookActivityDTOs;
    }

    /**
     * Given a lesson, this method returns the GBUserGridRowDTO for act
     * 
     * @param lesson
     * @return
     */
    public List<GradeBookGridRowDTO> getActivityGradeBookUserDTOs(Lesson lesson) {

	logger.debug("Getting gradebook data for lesson: " + lesson.getLessonId());

	List<GradeBookGridRowDTO> gradeBookActivityDTOs = new ArrayList<GradeBookGridRowDTO>();

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
	    GBActivityGridRowDTO activityDTO = getGradeBookActivityDTO(firstActivity, lesson);
	    gradeBookActivityDTOs.add(activityDTO);
	}

	for (Activity activity : activities) {
	    if (activity.getActivityId().longValue() != firstActivity.getActivityId().longValue()) {
		GBActivityGridRowDTO activityDTO = getGradeBookActivityDTO(activity, lesson);
		gradeBookActivityDTOs.add(activityDTO);
	    }
	}

	return gradeBookActivityDTOs;

    }

    /**
     * Gets the gradebook data for a user for a given activity and lesson
     * 
     * @param activity
     * @return GradeBookActivityDTO
     */
    public GBActivityGridRowDTO getGradeBookActivityDTO(Activity activity, Lesson lesson) {
	GBActivityGridRowDTO gactivityDTO = new GBActivityGridRowDTO();
	gactivityDTO.setActivityId(activity.getActivityId());
	gactivityDTO.setActivityTitle(activity.getTitle());

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

	    List<GradeBookUserActivity> gradeBookUserActivities = gradeBookDAO
		    .getAllGradeBookUserActivitiesForActivity(activity.getActivityId());

	    if (gradeBookUserActivities != null) {

		double sum = 0;
		double count = 0;
		for (GradeBookUserActivity gact : gradeBookUserActivities) {
		    if (gact.getMark() != null) {
			count++;
			sum += gact.getMark();
		    }
		}

		if (count != 0) {
		    gactivityDTO.setAverage(sum / count);
		}
	    }

	}

	return gactivityDTO;
    }

    /**
     * Gets the gradebook data for a user for a given activity and user
     * 
     * @param activity
     * @param learner
     * @param learnerProgress
     * @return GradeBookActivityDTO
     */
    public GBActivityGridRowDTO getGradeBookActivityDTO(Activity activity, User learner, LearnerProgress learnerProgress) {

	logger.debug("Getting gradebook data for activity: " + activity.getActivityId() + ". For user: "
		+ learner.getUserId());

	GBActivityGridRowDTO gactivityDTO = new GBActivityGridRowDTO();
	gactivityDTO.setActivityId(activity.getActivityId());
	gactivityDTO.setActivityTitle(activity.getTitle());

	GradeBookUserActivity gradeBookActivity = gradeBookDAO.getGradeBookUserDataForActivity(
		activity.getActivityId(), learner.getUserId());
	if (gradeBookActivity != null) {
	    gactivityDTO.setMark(gradeBookActivity.getMark());
	    gactivityDTO.setFeedback(gradeBookActivity.getFeedback());
	}

	gactivityDTO.setStatus(getActivityStatusStr(learnerProgress, activity));

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
		// Set the activityLearner URL for this gradebook activity
		gactivityDTO.setActivityUrl(Configuration.get(ConfigurationKeys.SERVER_URL)
			+ toolAct.getTool().getLearnerProgressUrl() + "&userID=" + learner.getUserId()
			+ "&toolSessionID=" + toolSession.getToolSessionId().toString());

		gactivityDTO.setOutput(this.getToolOutputsStr(toolAct, toolSession, learner));

		//		SortedMap<String, ToolOutputDefinition> map = toolService.getOutputDefinitionsFromTool(toolAct
		//			.getToolContentId());
		//
		//		Set<ToolOutput> toolOutputs = new HashSet<ToolOutput>();
		//
		//		
		//
		//		// Setting the tool outputs
		//		String toolOutputsStr = "";
		//		boolean noOutputs = true;
		//		if (map.keySet().size() > 0) {
		//
		//		    for (String outputName : map.keySet()) {
		//
		//			try {
		//			    ToolOutput toolOutput = toolService.getOutputFromTool(outputName, toolSession, learner
		//				    .getUserId());
		//
		//			    if (toolOutput != null && toolOutput.getValue().getType() != OutputType.OUTPUT_COMPLEX) {
		//				toolOutputs.add(toolOutput);
		//
		//				toolOutputsStr += "<option style='width:100%;'>";
		//				toolOutputsStr += toolOutput.getDescription() + ": "
		//					+ toolOutput.getValue().getString();
		//				toolOutputsStr += "</option>";
		//
		//				noOutputs = false;
		//			    }
		//
		//			} catch (RuntimeException e) {
		//			    logger.debug("Runtime exception when attempted to get outputs for activity: "
		//				    + toolAct.getActivityId() + ", continuing for other activities", e);
		//			}
		//		    }
		//		    toolOutputsStr += "</select>";
		//		}
		//
		//		// Fix up outputs html if there are not outputs available
		//		if (noOutputs) {
		//		    toolOutputsStr = "No output available.";
		//		} else {
		//		    toolOutputsStr = "<select style='width:150px;'><option style='width:100%;'>View outputs</option>"
		//			    + toolOutputsStr;
		//		}
		//
		//		gactivityDTO.setOutput(toolOutputsStr);
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
    public ArrayList<GBUserGridRowDTO> getGradeBookLessonData(Lesson lesson) {

	ArrayList<GBUserGridRowDTO> gradeBookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	if (lesson != null) {
	    Set<User> learners = (Set<User>) lesson.getAllLearners();

	    if (learners != null) {

		for (User learner : learners) {
		    GBUserGridRowDTO gradeBookUserDTO = new GBUserGridRowDTO();
		    gradeBookUserDTO.setLogin(learner.getLogin());
		    gradeBookUserDTO.setFirstName(learner.getFirstName());
		    gradeBookUserDTO.setLastName(learner.getLastName());

		    // Setting the status for the user's lesson
		    LearnerProgress learnerProgress = monitoringService.getLearnerProgress(learner.getUserId(), lesson
			    .getLessonId());
		    gradeBookUserDTO.setStatus(getLessonStatusStr(learnerProgress));

		    GradeBookUserLesson gradeBookUserLesson = gradeBookDAO.getGradeBookUserDataForLesson(lesson
			    .getLessonId(), learner.getUserId());
		    if (gradeBookUserLesson != null) {
			gradeBookUserDTO.setMark(gradeBookUserLesson.getMark());
			gradeBookUserDTO.setFeedback(gradeBookUserLesson.getFeedback());
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
    public void updateUserLessonGradeBookMark(Lesson lesson, User learner, Double mark) {
	if (lesson != null && learner != null) {
	    GradeBookUserLesson gradeBookUserLesson = gradeBookDAO.getGradeBookUserDataForLesson(lesson.getLessonId(),
		    learner.getUserId());

	    if (gradeBookUserLesson == null) {
		gradeBookUserLesson = new GradeBookUserLesson(lesson, learner);
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
    public void updateUserActivityGradeBookMark(Lesson lesson, User learner, Activity activity, Double mark) {
	if (lesson != null && activity != null && learner != null && activity.isToolActivity()) {

	    // First, update the mark for the activity
	    GradeBookUserActivity gradeBookUserActivity = gradeBookDAO.getGradeBookUserDataForActivity(activity
		    .getActivityId(), learner.getUserId());

	    if (gradeBookUserActivity == null) {
		gradeBookUserActivity = new GradeBookUserActivity((ToolActivity) activity, learner);
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

    /**
     * Adds up the total mark for a lesson based on the activity marks
     * 
     * @param gradeBookUserLesson
     */
    private void aggregateTotalMarkForLesson(GradeBookUserLesson gradeBookUserLesson) {
	Double totalMark = gradeBookDAO.getGradeBookUserActivityMarkSum(gradeBookUserLesson.getLesson().getLessonId(),
		gradeBookUserLesson.getLearner().getUserId());
	gradeBookUserLesson.setMark(totalMark);
	gradeBookDAO.insertOrUpdate(gradeBookUserLesson);
    }

    /**
     * Updates the feedback for a learners performance in a lesson
     * 
     * @param lesson
     * @param learner
     * @param feedback
     */
    public void updateUserLessonGradeBookFeedback(Lesson lesson, User learner, String feedback) {

	GradeBookUserLesson gradeBookUserLesson = gradeBookDAO.getGradeBookUserDataForLesson(lesson.getLessonId(),
		learner.getUserId());

	if (gradeBookUserLesson == null) {
	    gradeBookUserLesson = new GradeBookUserLesson(lesson, learner);
	}

	gradeBookUserLesson.setFeedback(feedback);
	gradeBookDAO.insertOrUpdate(gradeBookUserLesson);
    }

    /**
     * Updates the feedback for a learner's performance in an Activity
     * 
     * @param activity
     * @param learner
     * @param feedback
     */
    public void updateUserActivityGradeBookFeedback(Activity activity, User learner, String feedback) {

	GradeBookUserActivity gradeBookUserActivity = gradeBookDAO.getGradeBookUserDataForActivity(activity
		.getActivityId(), learner.getUserId());

	if (gradeBookUserActivity == null) {
	    gradeBookUserActivity = new GradeBookUserActivity((ToolActivity) activity, learner);
	}

	gradeBookUserActivity.setFeedback(feedback);
	gradeBookDAO.insertOrUpdate(gradeBookUserActivity);
    }

    public String getLessonStatusStr(LearnerProgress learnerProgress) {
//	String status = "NOT STARTED";
//
//	if (learnerProgress != null) {
//	    if (learnerProgress.isComplete()) {
//		status = "FINISHED";
//	    } else if (learnerProgress.getAttemptedActivities() != null
//		    && learnerProgress.getAttemptedActivities().size() > 0) {
//		status = "STARTED";
//	    }
//	}
	
	String status = "-";

	if (learnerProgress != null) {
	    if (learnerProgress.isComplete()) {
		status = "<img src='images/tick.png' />";
	    } else if (learnerProgress.getAttemptedActivities() != null
		    && learnerProgress.getAttemptedActivities().size() > 0) {
		status = "<img src='images/pencil.png' />";
	    }
	}
	
	

	return status;
    }

    public String getActivityStatusStr(LearnerProgress learnerProgress, Activity activity) {

	if (learnerProgress != null) {
	    byte statusByte = learnerProgress.getProgressState(activity);
	    if (statusByte == LearnerProgress.ACTIVITY_ATTEMPTED) {
		return "<img src='images/pencil.png' />";
	    } else if (statusByte == LearnerProgress.ACTIVITY_COMPLETED) {
		return "<img src='images/tick.png' />";
	    } 
	} 
	
	return "-";

    }

    public String getToolOutputsStr(ToolActivity toolAct, ToolSession toolSession, User learner) {
	String toolOutputsStr = "";
	boolean noOutputs = true;

	if (toolAct != null && toolSession != null && learner != null) {

	    SortedMap<String, ToolOutputDefinition> map = toolService.getOutputDefinitionsFromTool(toolAct
		    .getToolContentId());

	    Set<ToolOutput> toolOutputs = new HashSet<ToolOutput>();

	    if (map.keySet().size() > 0) {

		for (String outputName : map.keySet()) {

		    try {
			ToolOutput toolOutput = toolService.getOutputFromTool(outputName, toolSession, learner
				.getUserId());

			if (toolOutput != null && toolOutput.getValue().getType() != OutputType.OUTPUT_COMPLEX) {
			    toolOutputs.add(toolOutput);

			    toolOutputsStr += "<option style='width:100%;'>";
			    toolOutputsStr += toolOutput.getDescription() + ": " + toolOutput.getValue().getString();
			    toolOutputsStr += "</option>";

			    noOutputs = false;
			}

		    } catch (RuntimeException e) {
			logger.debug("Runtime exception when attempted to get outputs for activity: "
				+ toolAct.getActivityId() + ", continuing for other activities", e);
		    }
		}
		toolOutputsStr += "</select>";

	    }
	}

	// Fix up outputs html if there are not outputs available
	if (noOutputs) {
	    toolOutputsStr = "No output available.";
	} else {
	    toolOutputsStr = "<select style='width:150px;'><option style='width:100%;'>View outputs</option>"
		    + toolOutputsStr;
	}

	return toolOutputsStr;
    }

    // Getter and setter methods below -----------------------------------------

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

    // Getter and setter methods above -----------------------------------------
}
