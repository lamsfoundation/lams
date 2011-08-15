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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradebookDAO;
import org.lamsfoundation.lams.gradebook.dto.ExcelCell;
import org.lamsfoundation.lams.gradebook.dto.GBActivityGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBLessonGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.LessonComparator;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * This service handles all gradebook-related service calls
 * 
 * @author lfoxton
 * 
 */
public class GradebookService implements IGradebookService {

    private static Logger logger = Logger.getLogger(GradebookService.class);

    private static final ExcelCell[] EMPTY_ROW = new ExcelCell[4];

    // Services
    private ILamsCoreToolService toolService;
    private IGradebookDAO gradebookDAO;
    private ILessonService lessonService;
    private IUserManagementService userService;
    private IBaseDAO baseDAO;
    private IActivityDAO activityDAO;
    private MessageService messageService;

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getGBActivityRowsForLearner(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.usermanagement.User)
     */
    @SuppressWarnings("unchecked")
    public List<GradebookGridRowDTO> getGBActivityRowsForLearner(Lesson lesson, User learner) {

	logger.debug("Getting gradebook user data for lesson: " + lesson.getLessonId() + ". For user: "
		+ learner.getUserId());

	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<GradebookGridRowDTO>();

	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put("lesson", lesson);
	properties.put("user", learner);
	LearnerProgress learnerProgress = getLearnerProgress(lesson, learner);

	Set<Activity> activities = (Set<Activity>) lesson.getLearningDesign().getActivities();

	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 * 
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */

	Activity firstActivity = activityDAO.getActivityByActivityId(lesson.getLearningDesign().getFirstActivity()
		.getActivityId());

	if (firstActivity != null && firstActivity.isToolActivity() && firstActivity instanceof ToolActivity) {

	    GBActivityGridRowDTO activityDTO = getGradebookActivityDTO((ToolActivity) firstActivity, learner,
		    learnerProgress);
	    gradebookActivityDTOs.add(activityDTO);
	}

	SortedSet<Activity> sortedActivities = new TreeSet<Activity>(activities);

	for (Activity activity : sortedActivities) {
	    if (activity.getActivityId().longValue() != firstActivity.getActivityId().longValue()
		    && activity instanceof ToolActivity) {

		GBActivityGridRowDTO activityDTO = getGradebookActivityDTO((ToolActivity) activity, learner,
			learnerProgress);
		gradebookActivityDTOs.add(activityDTO);
	    }
	}
	return gradebookActivityDTOs;
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getGBActivityRowsForLesson(org.lamsfoundation.lams.lesson.Lesson)
     */
    @SuppressWarnings("unchecked")
    public List<GradebookGridRowDTO> getGBActivityRowsForLesson(Lesson lesson) {

	logger.debug("Getting gradebook data for lesson: " + lesson.getLessonId());

	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<GradebookGridRowDTO>();

	Set<Activity> activities = lesson.getLearningDesign().getActivities();

	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 * 
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */
	Activity firstActivity = activityDAO.getActivityByActivityId(lesson.getLearningDesign().getFirstActivity()
		.getActivityId());

	if (firstActivity.isToolActivity() && firstActivity instanceof ToolActivity) {
	    Grouping grouping = firstActivity.getGrouping();
	    if (grouping != null) {
		Set<Group> groups = grouping.getGroups();
		if (groups != null) {

		    for (Group group : groups) {
			GBActivityGridRowDTO activityDTO = getGradebookActivityDTO((ToolActivity) firstActivity,
				lesson, group.getGroupName(), group.getGroupId());
			gradebookActivityDTOs.add(activityDTO);
		    }

		}
	    } else {
		GBActivityGridRowDTO activityDTO = getGradebookActivityDTO((ToolActivity) firstActivity, lesson, null,
			null);
		gradebookActivityDTOs.add(activityDTO);
	    }
	}

	for (Activity activity : activities) {
	    if (activity.getActivityId().longValue() != firstActivity.getActivityId().longValue()
		    && activity instanceof ToolActivity) {

		Grouping grouping = activity.getGrouping();
		if (grouping != null) {
		    Set<Group> groups = grouping.getGroups();
		    if (groups != null) {

			for (Group group : groups) {
			    GBActivityGridRowDTO activityDTO = getGradebookActivityDTO((ToolActivity) activity, lesson,
				    group.getGroupName(), group.getGroupId());
			    gradebookActivityDTOs.add(activityDTO);
			}

		    }
		} else {
		    GBActivityGridRowDTO activityDTO = getGradebookActivityDTO((ToolActivity) activity, lesson, null,
			    null);
		    gradebookActivityDTOs.add(activityDTO);
		}
	    }
	}

	return gradebookActivityDTOs;

    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getGBUserRowsForActivity(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.learningdesign.Activity)
     */
    @SuppressWarnings("unchecked")
    public List<GBUserGridRowDTO> getGBUserRowsForActivity(Lesson lesson, ToolActivity activity, Long groupId) {

	List<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	Set<User> learners = null;
	if (groupId != null) {
	    Group group = (Group) baseDAO.find(Group.class, groupId);
	    if (group != null) {
		learners = group.getUsers();
	    } else {
		learners = lesson.getAllLearners();
	    }
	} else {
	    learners = lesson.getAllLearners();
	}

	if (learners != null) {
	    for (User learner : learners) {
		GBUserGridRowDTO gUserDTO = new GBUserGridRowDTO();
		gUserDTO.setRowName(learner.getLastName() + " " + learner.getFirstName());
		gUserDTO.setFirstName(learner.getFirstName());
		gUserDTO.setLastName(learner.getLastName());
		gUserDTO.setId(learner.getUserId().toString());

		gUserDTO.setMarksAvailable(getTotalMarksAvailable(activity));

		GradebookUserActivity gradebookUserActivity = gradebookDAO.getGradebookUserDataForActivity(activity
			.getActivityId(), learner.getUserId());

		// Set the progress
		LearnerProgress learnerProgress = getLearnerProgress(lesson, learner);
		gUserDTO.setStatus(getActivityStatusStr(learnerProgress, activity));
		gUserDTO.setTimeTaken(getActivityDuration(learnerProgress, activity));

		// Get the tool outputs for this user if there are any
		ToolSession toolSession = toolService.getToolSessionByLearner(learner, activity);
		if (toolSession != null && learnerProgress != null) {
		    // Set the activityLearner URL for this gradebook activity
		    byte activityState = learnerProgress.getProgressState(activity);
		    if (activityState == LearnerProgress.ACTIVITY_ATTEMPTED
			    || activityState == LearnerProgress.ACTIVITY_COMPLETED) {
			gUserDTO.setActivityUrl(Configuration.get(ConfigurationKeys.SERVER_URL)
				+ activity.getTool().getLearnerProgressUrl() + "&userID=" + learner.getUserId()
				+ "&toolSessionID=" + toolSession.getToolSessionId().toString());

			gUserDTO.setOutput(this.getToolOutputsStr(activity, toolSession, learner));
		    }
		}

		// Add marks and feedback
		if (gradebookUserActivity != null) {
		    gUserDTO.setFeedback(gradebookUserActivity.getFeedback());
		    gUserDTO.setMark(gradebookUserActivity.getMark());

		}
		gradebookUserDTOs.add(gUserDTO);
	    }
	}

	return gradebookUserDTOs;

    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getGBUserRowsForLesson(org.lamsfoundation.lams.lesson.Lesson)
     */
    @SuppressWarnings("unchecked")
    public ArrayList<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson) {

	ArrayList<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	if (lesson != null) {
	    Set<User> learners = lesson.getAllLearners();

	    if (learners != null) {

		for (User learner : learners) {
		    GBUserGridRowDTO gradebookUserDTO = populateGradebookUserDTO(learner, lesson);
		    gradebookUserDTOs.add(gradebookUserDTO);
		}
	    }
	}

	return gradebookUserDTOs;

    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getGradebookUserLesson(java.lang.Long,
     *      java.lang.Integer)
     */
    public GradebookUserLesson getGradebookUserLesson(Long lessonID, Integer userID) {
	return gradebookDAO.getGradebookUserDataForLesson(lessonID, userID);
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getGradebookUserActivity(java.lang.Long,
     *      java.lang.Integer)
     */
    public GradebookUserActivity getGradebookUserActivity(Long activityID, Integer userID) {
	return gradebookDAO.getGradebookUserDataForActivity(activityID, userID);
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getAverageMarkForActivity(java.lang.Long)
     */
    public Double getAverageMarkForActivity(Long activityID) {
	return gradebookDAO.getAverageMarkForActivity(activityID);
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getAverageMarkForLesson(java.lang.Long)
     */
    public Double getAverageMarkForLesson(Long lessonID) {
	return gradebookDAO.getAverageMarkForLesson(lessonID);
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#updateUserLessonGradebookMark(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.usermanagement.User, java.lang.Double)
     */
    public void updateUserLessonGradebookMark(Lesson lesson, User learner, Double mark) {
	if (lesson != null && learner != null) {
	    GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		    learner.getUserId());

	    if (gradebookUserLesson == null) {
		gradebookUserLesson = new GradebookUserLesson(lesson, learner);
	    }
	    gradebookUserLesson.setMark(mark);
	    gradebookDAO.insertOrUpdate(gradebookUserLesson);
	}
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#updateUserActivityGradebookMark(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.usermanagement.User,
     *      org.lamsfoundation.lams.learningdesign.Activity, java.lang.Double)
     */
    public void updateUserActivityGradebookMark(Lesson lesson, User learner, Activity activity, Double mark,
	    Boolean markedInGradebook) {
	if (lesson != null && activity != null && learner != null && activity.isToolActivity()) {

	    // First, update the mark for the activity
	    GradebookUserActivity gradebookUserActivity = gradebookDAO.getGradebookUserDataForActivity(activity
		    .getActivityId(), learner.getUserId());

	    if (gradebookUserActivity == null) {
		gradebookUserActivity = new GradebookUserActivity((ToolActivity) activity, learner);
	    }

	    gradebookUserActivity.setMark(mark);
	    gradebookUserActivity.setMarkedInGradebook(markedInGradebook);
	    gradebookDAO.insertOrUpdate(gradebookUserActivity);

	    // Now update the lesson mark
	    GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		    learner.getUserId());

	    if (gradebookUserLesson == null) {
		gradebookUserLesson = new GradebookUserLesson();
		gradebookUserLesson.setLearner(learner);
		gradebookUserLesson.setLesson(lesson);
	    }

	    aggregateTotalMarkForLesson(gradebookUserLesson);
	}
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#updateUserLessonGradebookFeedback(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.usermanagement.User, java.lang.String)
     */
    public void updateUserLessonGradebookFeedback(Lesson lesson, User learner, String feedback) {

	GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		learner.getUserId());

	if (gradebookUserLesson == null) {
	    gradebookUserLesson = new GradebookUserLesson(lesson, learner);
	}

	gradebookUserLesson.setFeedback(feedback);
	gradebookDAO.insertOrUpdate(gradebookUserLesson);
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#updateUserActivityGradebookFeedback(org.lamsfoundation.lams.learningdesign.Activity,
     *      org.lamsfoundation.lams.usermanagement.User, java.lang.String)
     */
    public void updateUserActivityGradebookFeedback(Activity activity, User learner, String feedback) {

	GradebookUserActivity gradebookUserActivity = gradebookDAO.getGradebookUserDataForActivity(activity
		.getActivityId(), learner.getUserId());

	if (gradebookUserActivity == null) {
	    gradebookUserActivity = new GradebookUserActivity((ToolActivity) activity, learner);
	}

	gradebookUserActivity.setFeedback(feedback);
	gradebookDAO.insertOrUpdate(gradebookUserActivity);
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getGBLessonRows(org.lamsfoundation.lams.usermanagement.Organisation)
     */
    @SuppressWarnings("unchecked")
    public List<GBLessonGridRowDTO> getGBLessonRows(Organisation organisation, User user, GBGridView view) {
	List<GBLessonGridRowDTO> lessonRows = new ArrayList<GBLessonGridRowDTO>();

	if (organisation != null) {

	    List<Lesson> lessons = lessonService.getLessonsByGroupAndUser(user.getUserId(), organisation
		    .getOrganisationId());
	    if (lessons != null) {

		for (Lesson lesson : lessons) {

		    boolean marksReleased = lesson.getMarksReleased() != null && lesson.getMarksReleased();

		    // Dont include lesson in list if the user doesnt have permission
		    if (!(view == GBGridView.MON_COURSE
			    && (lesson.getLessonClass().isStaffMember(user) || userService.isUserInRole(user
				    .getUserId(), organisation.getOrganisationId(), Role.GROUP_MANAGER)) || view == GBGridView.LRN_COURSE
			    && lesson.getAllLearners().contains(user) && marksReleased)) {
			continue;
		    }

		    GBLessonGridRowDTO lessonRow = new GBLessonGridRowDTO();
		    lessonRow.setLessonName(lesson.getLessonName());
		    lessonRow.setId(lesson.getLessonId().toString());
		    lessonRow.setStartDate(getLocaleDateString(user, lesson.getStartDateTime()));

		    if (view == GBGridView.MON_COURSE) {

			// Setting the averages for monitor view
			lessonRow.setAverageTimeTaken(gradebookDAO.getAverageDurationLesson(lesson.getLessonId()));
			lessonRow.setAverageMark(gradebookDAO.getAverageMarkForLesson(lesson.getLessonId()));

			// Set the gradebook monitor url
			String gbMonURL = Configuration.get(ConfigurationKeys.SERVER_URL)
				+ "gradebook/gradebookMonitoring.do?lessonID=" + lesson.getLessonId().toString();
			lessonRow.setGradebookMonitorURL(gbMonURL);
		    } else if (view == GBGridView.LRN_COURSE) {

			GradebookUserLesson gbLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
				user.getUserId());

			lessonRow.setAverageTimeTaken(gradebookDAO.getAverageDurationLesson(lesson.getLessonId()));
			lessonRow.setAverageMark(gradebookDAO.getAverageMarkForLesson(lesson.getLessonId()));

			if (gbLesson != null) {
			    lessonRow.setMark(gbLesson.getMark());
			    lessonRow.setFeedback(gbLesson.getFeedback());
			}

			LearnerProgress learnerProgress = getLearnerProgress(lesson, user);
			lessonRow.setStatus(getLessonStatusStr(learnerProgress));
			if (learnerProgress != null) {
			    if (learnerProgress.getStartDate() != null && learnerProgress.getFinishDate() != null) {
				lessonRow.setTimeTaken(learnerProgress.getFinishDate().getTime()
					- learnerProgress.getStartDate().getTime());
			    }

			    lessonRow.setFinishDate(getLocaleDateString(user, learnerProgress.getFinishDate()));
			}
		    }

		    if (lesson.getOrganisation().getOrganisationId() != organisation.getOrganisationId()) {
			lessonRow.setSubGroup(lesson.getOrganisation().getName());
		    } else {
			lessonRow.setSubGroup("");
		    }

		    lessonRows.add(lessonRow);

		}
	    }

	} else {
	    logger.error("Request for gradebook grid with a null organisation");
	}

	return lessonRows;
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getActivityViewDataForExcel(org.lamsfoundation.lams.lesson.Lesson)
     */
    @SuppressWarnings("unchecked")
    public ExcelCell[][] getActivityViewDataForExcel(Lesson lesson) {
	ExcelCell[][] data = null;
	if (lesson != null) {

	    Set<Activity> activities = lesson.getLearningDesign().getActivities();

	    Activity firstActivity = activityDAO.getActivityByActivityId(lesson.getLearningDesign().getFirstActivity()
		    .getActivityId());

	    HashMap<Activity, List<GBUserGridRowDTO>> activityViewMap = new HashMap<Activity, List<GBUserGridRowDTO>>();

	    if (firstActivity != null && firstActivity.isToolActivity() && firstActivity instanceof ToolActivity) {

		List<GBUserGridRowDTO> userRows = getGBUserRowsForActivity(lesson, (ToolActivity) firstActivity, null);
		activityViewMap.put(firstActivity, userRows);
	    }

	    SortedSet<Activity> sortedActivities = new TreeSet<Activity>(activities);

	    for (Activity activity : sortedActivities) {
		if (activity.getActivityId().longValue() != firstActivity.getActivityId().longValue()
			&& activity instanceof ToolActivity) {

		    List<GBUserGridRowDTO> userRows = getGBUserRowsForActivity(lesson, (ToolActivity) activity, null);
		    activityViewMap.put(activity, userRows);
		}
	    }

	    List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();

	    for (Activity act : activityViewMap.keySet()) {

		ExcelCell[] activityTitleRow = new ExcelCell[5];
		activityTitleRow[0] = new ExcelCell(act.getTitle(), true);
		rowList.add(activityTitleRow);
		ExcelCell[] titleRow = new ExcelCell[5];
		titleRow[0] = new ExcelCell(getMessage("gradebook.export.last.name"), true);
		titleRow[1] = new ExcelCell(getMessage("gradebook.export.first.name"), true);
		titleRow[2] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
		titleRow[3] = new ExcelCell(getMessage("gradebook.export.outputs"), true);
		titleRow[4] = new ExcelCell(getMessage("gradebook.columntitle.mark"), true);
		rowList.add(titleRow);

		// Get the rest of the data
		List<GBUserGridRowDTO> userRows = activityViewMap.get(act);
		for (GBUserGridRowDTO userRow : userRows) {
		    ExcelCell[] userDataRow = new ExcelCell[5];

		    userDataRow[0] = new ExcelCell(userRow.getLastName(), false);
		    userDataRow[1] = new ExcelCell(userRow.getFirstName(), false);
		    userDataRow[2] = new ExcelCell(userRow.getTimeTakenSeconds(), false);
		    userDataRow[3] = new ExcelCell(userRow.getOutput(), false);
		    userDataRow[4] = new ExcelCell(userRow.getMark(), false);
		    rowList.add(userDataRow);
		}

		rowList.add(EMPTY_ROW);
	    }

	    data = rowList.toArray(new ExcelCell[][] {});

	}
	return data;
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getUserViewDataForExcel(org.lamsfoundation.lams.lesson.Lesson)
     */
    @SuppressWarnings("unchecked")
    public ExcelCell[][] getUserViewDataForExcel(Lesson lesson) {
	ExcelCell[][] data = null;
	if (lesson != null) {

	    Set<User> learners = lesson.getAllLearners();

	    List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();
	    for (User learner : learners) {
		List<GradebookGridRowDTO> activityRows = getGBActivityRowsForLearner(lesson, learner);

		ExcelCell[] activityTitleRow = new ExcelCell[4];
		activityTitleRow[0] = new ExcelCell(learner.getFullName(), true);
		rowList.add(activityTitleRow);

		ExcelCell[] titleRow = new ExcelCell[4];
		titleRow[0] = new ExcelCell(getMessage("gradebook.export.activity"), true);
		titleRow[1] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
		titleRow[2] = new ExcelCell(getMessage("gradebook.export.outputs"), true);
		titleRow[3] = new ExcelCell(getMessage("gradebook.columntitle.mark"), true);
		rowList.add(titleRow);

		Iterator it = activityRows.iterator();
		while (it.hasNext()) {
		    GBActivityGridRowDTO activityRow = (GBActivityGridRowDTO) it.next();
		    ExcelCell[] activityDataRow = new ExcelCell[4];
		    activityDataRow[0] = new ExcelCell(activityRow.getRowName(), false);
		    activityDataRow[1] = new ExcelCell(activityRow.getTimeTakenSeconds(), false);
		    activityDataRow[2] = new ExcelCell(activityRow.getOutput(), false);
		    activityDataRow[3] = new ExcelCell(activityRow.getMark(), false);
		    rowList.add(activityDataRow);
		}

		rowList.add(EMPTY_ROW);
	    }

	    data = rowList.toArray(new ExcelCell[][] {});

	}
	return data;
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getSummaryDataForExcel(org.lamsfoundation.lams.lesson.Lesson)
     */
    @SuppressWarnings("unchecked")
    public ExcelCell[][] getSummaryDataForExcel(Lesson lesson) {
	ExcelCell[][] data = null;
	if (lesson != null) {

	    // The entire data list
	    List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();

	    // Adding the lesson average data to the summary -------------------
	    ExcelCell[] lessonAverageMark = new ExcelCell[2];
	    lessonAverageMark[0] = new ExcelCell(getMessage("gradebook.export.average.lesson.mark"), true);
	    lessonAverageMark[1] = new ExcelCell(getAverageMarkForLesson(lesson.getLessonId()), false);
	    rowList.add(lessonAverageMark);

	    ExcelCell[] lessonAverageTimeTaken = new ExcelCell[2];
	    lessonAverageTimeTaken[0] = new ExcelCell(getMessage("gradebook.export.average.lesson.time.taken"), true);
	    lessonAverageTimeTaken[1] = new ExcelCell(gradebookDAO.getAverageDurationLesson(lesson.getLessonId())/1000, false);
	    rowList.add(lessonAverageTimeTaken);
	    rowList.add(EMPTY_ROW);
	    // ------------------------------------------------------------------

	    // Adding the activity average data to the summary -----------------
	    List<GradebookGridRowDTO> activityRows = getGBActivityRowsForLesson(lesson);
	    ExcelCell[] activityAverageTitle = new ExcelCell[1];
	    activityAverageTitle[0] = new ExcelCell(getMessage("gradebook.export.activities"), true);
	    rowList.add(activityAverageTitle);

	    // Setting up the activity summary table
	    ExcelCell[] activityAverageRow = new ExcelCell[4];
	    activityAverageRow[0] = new ExcelCell(getMessage("gradebook.export.activity"), true);
	    activityAverageRow[1] = new ExcelCell(getMessage("gradebook.columntitle.competences"), true);
	    activityAverageRow[2] = new ExcelCell(getMessage("gradebook.export.average.time.taken.seconds"), true);
	    activityAverageRow[3] = new ExcelCell(getMessage("gradebook.columntitle.averageMark"), true);
	    rowList.add(activityAverageRow);

	    Iterator it = activityRows.iterator();
	    while (it.hasNext()) {
		GBActivityGridRowDTO activityRow = (GBActivityGridRowDTO) it.next();
		// Add the activity average data
		ExcelCell[] activityDataRow = new ExcelCell[4];
		activityDataRow[0] = new ExcelCell(activityRow.getRowName(), false);
		activityDataRow[1] = new ExcelCell(activityRow.getCompetences(), false);
		activityDataRow[2] = new ExcelCell(activityRow.getAverageTimeTakenSeconds(), false);
		activityDataRow[3] = new ExcelCell(activityRow.getAverageMark(), false);
		rowList.add(activityDataRow);
	    }
	    rowList.add(EMPTY_ROW);
	    // ------------------------------------------------------------------

	    // Adding the user lesson marks to the summary----------------------
	    ExcelCell[] userMarksTitle = new ExcelCell[1];
	    userMarksTitle[0] = new ExcelCell(getMessage("gradebook.export.total.marks.for.lesson"), true);
	    rowList.add(userMarksTitle);

	    // Fetching the user data
	    ArrayList<GBUserGridRowDTO> userRows = getGBUserRowsForLesson(lesson);

	    // Setting up the user marks table
	    ExcelCell[] userTitleRow = new ExcelCell[5];
	    userTitleRow[0] = new ExcelCell(getMessage("gradebook.export.last.name"), true);
	    userTitleRow[1] = new ExcelCell(getMessage("gradebook.export.first.name"), true);
	    userTitleRow[2] = new ExcelCell(getMessage("gradebook.exportcourse.progress"), true);
	    userTitleRow[3] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
	    userTitleRow[4] = new ExcelCell(getMessage("gradebook.export.total.mark"), true);
	    rowList.add(userTitleRow);

	    for (GBUserGridRowDTO userRow : userRows) {
		 // Adding the user data for the lesson
		    ExcelCell[] userDataRow = new ExcelCell[5];
		    userDataRow[0] = new ExcelCell(userRow.getLastName(), false);
		    userDataRow[1] = new ExcelCell(userRow.getFirstName(), false);
		    userDataRow[2] = new ExcelCell(getProgressMessage(userRow), false);
		    userDataRow[3] = new ExcelCell(userRow.getTimeTakenSeconds(), false);
		    userDataRow[4] = new ExcelCell(userRow.getMark(), false);
		    rowList.add(userDataRow);
	    }
	    // ------------------------------------------------------------------

	    data = rowList.toArray(new ExcelCell[][] {});

	}
	return data;
    }
    
    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getSummaryDataForExcel(org.lamsfoundation.lams.lesson.Lesson)
     */
    @SuppressWarnings("unchecked")
    public ExcelCell[][] getCourseDataForExcel(Integer userId, Integer organisationId) {
	// The entire data list
	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();

	User user = (User) getUserService().findById(User.class, userId);
	Set<Lesson> lessonsFromDB = new TreeSet<Lesson>(new LessonComparator());
	lessonsFromDB.addAll(lessonService.getLessonsByGroupAndUser(userId, organisationId));
	
	List<Lesson> lessons = new LinkedList<Lesson>();
	// Dont include lesson in list if the user doesnt have permission
	for (Lesson lesson : lessonsFromDB) {
	    if (!(lesson.getLessonClass().isStaffMember(user) || userService.isUserInRole(userId, organisationId,
		    Role.GROUP_MANAGER))) {
		continue;
	    }

	    lessons.add(lesson);
	}
	
	if (lessons == null || (lessons.size() == 0)) {
	    return rowList.toArray(new ExcelCell[][] {});
	}
	
	//collect users from all lessons
	Set<User> allLearners = new LinkedHashSet<User>();
	Map<Long, Set<User>> lessonUsers = new HashMap<Long, Set<User>>();
	for (Lesson lesson : lessons) {
	    Set dbLessonUsers = lesson.getAllLearners();
	    lessonUsers.put(lesson.getLessonId(), dbLessonUsers);
	    allLearners.addAll(dbLessonUsers);
	}
	
	int numberOfCellsInARow = 2 + lessons.size()*4;
	
	// Adding the user lesson marks to the summary----------------------
	ExcelCell[] lessonsNames = new ExcelCell[numberOfCellsInARow];
	int i = 0;
	lessonsNames[i++] = new ExcelCell("", false);
	lessonsNames[i++] = new ExcelCell("", false);
	for (Lesson lesson : lessons) {
	    lessonsNames[i++] = new ExcelCell(messageService.getMessage("gradebook.exportcourse.lesson",
		    new Object[] { lesson.getLessonName() }), true);
	    lessonsNames[i++] = new ExcelCell("", false);
	    lessonsNames[i++] = new ExcelCell("", false);
	    lessonsNames[i++] = new ExcelCell("", false);
	}
	rowList.add(lessonsNames);
	
	// Setting up the user marks table
	ExcelCell[] headerRow = new ExcelCell[numberOfCellsInARow];
	i = 0;
	headerRow[i++] = new ExcelCell(getMessage("gradebook.export.last.name"), true);
	headerRow[i++] = new ExcelCell(getMessage("gradebook.export.first.name"), true);
	for (Lesson lesson : lessons) {
	    headerRow[i++] = new ExcelCell(getMessage("gradebook.exportcourse.progress"), true);
	    headerRow[i++] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
	    headerRow[i++] = new ExcelCell(getMessage("gradebook.exportcourse.lessonFeedback"), true);
	    headerRow[i++] = new ExcelCell(getMessage("gradebook.export.total.mark"), true);
	}
	rowList.add(headerRow);
	
	for (User learner : allLearners) {
	    // Fetching the user data
	    List<GBUserGridRowDTO> userRows = getGBUserRowsForUser(learner, lessons, lessonUsers);
	    i = 0;
	    ExcelCell[] userDataRow = new ExcelCell[numberOfCellsInARow];
	    userDataRow[i++] = new ExcelCell(learner.getLastName(), false);
	    userDataRow[i++] = new ExcelCell(learner.getFirstName(), false);
	    
	    for (GBUserGridRowDTO userRow : userRows) {
		userDataRow[i++] = new ExcelCell(getProgressMessage(userRow), false);
		userDataRow[i++] = new ExcelCell(userRow.getTimeTakenSeconds(), false);
		userDataRow[i++] = new ExcelCell(userRow.getFeedback(), false);
		userDataRow[i++] = new ExcelCell(userRow.getMark(), false);
	    }

	    rowList.add(userDataRow);
	}
	
	return rowList.toArray(new ExcelCell[][] {});
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#updateActivityMark(java.lang.Double,
     *      java.lang.String, java.lang.Integer, java.lang.Long)
     */
    public void updateActivityMark(Double mark, String feedback, Integer userID, Long toolSessionID,
	    Boolean markedInGradebook) {
	ToolSession toolSession = toolService.getToolSessionById(toolSessionID);
	User learner = (User) userService.findById(User.class, userID);
	if (learner != null && toolSession != null) {
	    ToolActivity activity = toolSession.getToolActivity();
	    GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(activity.getActivityId(), userID);

	    // If gradebook user activity is null, save the mark and feedback
	    if (gradebookUserActivity == null || !gradebookUserActivity.getMarkedInGradebook()) {
		updateUserActivityGradebookMark(toolSession.getLesson(), learner, activity, mark, markedInGradebook);
	    }

	}
    }

    /**
     * @see org.lamsfoundation.lams.gradebook.service.IGradebookService#getActivityById(java.lang.Long)
     */
    public Activity getActivityById(Long activityID) {
	return activityDAO.getActivityByActivityId(activityID);
    }
    
    /**
     * Returns list of GBUserGridRowDTOs, all of which will be displayed on 1 line on course export.
     * 
     * @param learner
     * @param lessons
     * @return
     */
    private List<GBUserGridRowDTO> getGBUserRowsForUser(User learner, List<Lesson> lessons, Map<Long, Set<User>> lessonUsers) {

	List<GBUserGridRowDTO> gradebookUserDTOs = new LinkedList<GBUserGridRowDTO>();
	for (Lesson lesson : lessons) {
	    GBUserGridRowDTO gradebookUserDTO;
	    
	    //check if learner is participating in this lesson
	    if (lessonUsers.get(lesson.getLessonId()).contains(learner)) {
		gradebookUserDTO = populateGradebookUserDTO(learner, lesson);
	    } else {
		gradebookUserDTO = new GBUserGridRowDTO();
		gradebookUserDTO.setId(learner.getUserId().toString());
		gradebookUserDTO.setRowName(learner.getLastName() + " " + learner.getFirstName());
		gradebookUserDTO.setFirstName(learner.getFirstName());
		gradebookUserDTO.setLastName(learner.getLastName());
		gradebookUserDTO.setStatus("n/a");
	    }
	    
	    gradebookUserDTOs.add(gradebookUserDTO);
	}

	return gradebookUserDTOs;

    }
    
    /**
     * Fill up GBUserGridRowDTO with appropriate values.
     * 
     * @param learner
     * @param lessons
     * @return
     */
    private GBUserGridRowDTO populateGradebookUserDTO(User learner, Lesson lesson) {

	GBUserGridRowDTO gradebookUserDTO = new GBUserGridRowDTO();
	gradebookUserDTO.setId(learner.getUserId().toString());
	gradebookUserDTO.setRowName(learner.getLastName() + " " + learner.getFirstName());
	gradebookUserDTO.setFirstName(learner.getFirstName());
	gradebookUserDTO.setLastName(learner.getLastName());

	// Setting the status and time taken for the user's lesson
	LearnerProgress learnerProgress = getLearnerProgress(lesson, learner);
	gradebookUserDTO.setStatus(getLessonStatusStr(learnerProgress));

	// set current activity if available
	if ((learnerProgress != null) && (learnerProgress.getCurrentActivity() != null)) {
	    gradebookUserDTO.setCurrentActivity(learnerProgress.getCurrentActivity().getTitle());
	}

	// calculate time taken
	if (learnerProgress != null) {
	    if (learnerProgress.getStartDate() != null && learnerProgress.getFinishDate() != null) {
		gradebookUserDTO.setTimeTaken(learnerProgress.getFinishDate().getTime()
			- learnerProgress.getStartDate().getTime());
	    }
	}

	GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		learner.getUserId());
	if (gradebookUserLesson != null) {
	    gradebookUserDTO.setMark(gradebookUserLesson.getMark());
	    gradebookUserDTO.setFeedback(gradebookUserLesson.getFeedback());
	}
	return gradebookUserDTO;

    }

    /**
     * Gets the internationalised date
     * 
     * @param user
     * @param date
     * @return
     */
    private String getLocaleDateString(User user, Date date) {
	if (user == null || date == null) {
	    return null;
	}

	Locale locale = new Locale(user.getLocale().getLanguageIsoCode(), user.getLocale().getCountryIsoCode());
	String dateStr = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale).format(date);
	return dateStr;
    }

    /**
     * Returns progress status as text message.
     * @param userRow
     * @return
     */
    private String getProgressMessage(GBUserGridRowDTO userRow) {
	String originalStatus = userRow.getStatus();
	
	String status;
	if (originalStatus.contains("tick.png")) {
	    status = getMessage("gradebook.exportcourse.ok");
	} else if (originalStatus.contains("cog.png")) {
	    status = getMessage("gradebook.exportcourse.current.activity", new String[] { userRow.getCurrentActivity()});
	} else {
	    status = originalStatus;
	}
	return status;
    }

    /**
     * Adds a mark to the aggregated total and saves it
     * 
     * @param gradebookUserLesson
     */
    private void aggregateTotalMarkForLesson(GradebookUserLesson gradebookUserLesson) {
	Double totalMark = gradebookDAO.getGradebookUserActivityMarkSum(gradebookUserLesson.getLesson().getLessonId(),
		gradebookUserLesson.getLearner().getUserId());
	gradebookUserLesson.setMark(totalMark);
	gradebookDAO.insertOrUpdate(gradebookUserLesson);
    }

    /**
     * Gets the GBActivityGridRowDTO fro a given activity and lesson
     * 
     * @param activity
     * @param lesson
     * @return
     */
    private GBActivityGridRowDTO getGradebookActivityDTO(ToolActivity activity, Lesson lesson, String groupName,
	    Long groupId) {

	GBActivityGridRowDTO gactivityDTO = new GBActivityGridRowDTO();
	gactivityDTO.setId(activity.getActivityId().toString());

	// Setting name
	// If grouped acitivty, append group name
	gactivityDTO.setRowName(activity.getTitle());
	if (groupName != null && groupId != null) {
	    gactivityDTO.setGroupId(groupId);
	    gactivityDTO.setRowName(activity.getTitle() + " (" + groupName + ")");

	    // Need to make the id unique, so appending the group id for this row
	    gactivityDTO.setId(activity.getActivityId().toString() + "_" + groupId.toString());

	    // Setting averages for group
	    gactivityDTO.setAverageMark(gradebookDAO
		    .getAverageMarkForGroupedActivity(activity.getActivityId(), groupId));
	    gactivityDTO.setAverageTimeTaken(gradebookDAO.getAverageDurationForGroupedActivity(
		    activity.getActivityId(), groupId));

	} else {
	    // Setting averages for lesson
	    gactivityDTO.setAverageMark(gradebookDAO.getAverageMarkForActivity(activity.getActivityId()));
	    gactivityDTO.setAverageTimeTaken(gradebookDAO.getAverageDurationForActivity(activity.getActivityId()));
	}

	// Set the possible marks if applicable
	gactivityDTO.setMarksAvailable(this.getTotalMarksAvailable(activity));

	String monitorUrl = Configuration.get(ConfigurationKeys.SERVER_URL) + activity.getTool().getMonitorUrl() + "?"
		+ AttributeNames.PARAM_CONTENT_FOLDER_ID + "=" + lesson.getLearningDesign().getContentFolderID() + "&"
		+ AttributeNames.PARAM_TOOL_CONTENT_ID + "=" + activity.getToolContentId();
	gactivityDTO.setMonitorUrl(monitorUrl);

	// Get the competences for this activity
	Set<CompetenceMapping> competenceMappings = activity.getCompetenceMappings();
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

	return gactivityDTO;
    }

    /**
     * Gets the GBActivityGridRowDTO for a given user and activity
     * 
     * @param activity
     * @param learner
     * @param learnerProgress
     * @return
     */
    private GBActivityGridRowDTO getGradebookActivityDTO(ToolActivity activity, User learner,
	    LearnerProgress learnerProgress) {

	logger.debug("Getting gradebook data for activity: " + activity.getActivityId() + ". For user: "
		+ learner.getUserId());

	GBActivityGridRowDTO gactivityDTO = new GBActivityGridRowDTO();
	gactivityDTO.setId(activity.getActivityId().toString());

	// Set the title
	// If it is a grouped activity, and the user has a group get the group title as well
	gactivityDTO.setRowName(activity.getTitle());
	if (activity.getGrouping() != null) {
	    Group group = activity.getGroupFor(learner);
	    if (group != null) {
		gactivityDTO.setRowName(activity.getTitle() + " (" + group.getGroupName() + ")");
	    }
	}

	// Set the possible marks if applicable
	gactivityDTO.setMarksAvailable(this.getTotalMarksAvailable(activity));

	GradebookUserActivity gradebookActivity = gradebookDAO.getGradebookUserDataForActivity(
		activity.getActivityId(), learner.getUserId());
	if (gradebookActivity != null) {
	    gactivityDTO.setMark(gradebookActivity.getMark());
	    gactivityDTO.setFeedback(gradebookActivity.getFeedback());
	}

	// Setting status
	gactivityDTO.setTimeTaken(getActivityDuration(learnerProgress, activity));
	gactivityDTO.setStatus(getActivityStatusStr(learnerProgress, activity));

	// Setting averages
	gactivityDTO.setAverageMark(gradebookDAO.getAverageMarkForActivity(activity.getActivityId()));
	gactivityDTO.setAverageTimeTaken(gradebookDAO.getAverageDurationForActivity(activity.getActivityId()));

	// Get the competences for this activity
	Set<CompetenceMapping> competenceMappings = activity.getCompetenceMappings();
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
	ToolSession toolSession = toolService.getToolSessionByLearner(learner, activity);
	if (toolSession != null && learnerProgress != null) {
	    byte activityState = learnerProgress.getProgressState(activity);
	    if (activityState == LearnerProgress.ACTIVITY_ATTEMPTED
		    || activityState == LearnerProgress.ACTIVITY_COMPLETED) {
		// Set the activityLearner URL for this gradebook activity
		gactivityDTO.setActivityUrl(Configuration.get(ConfigurationKeys.SERVER_URL)
			+ activity.getTool().getLearnerProgressUrl() + "&userID=" + learner.getUserId()
			+ "&toolSessionID=" + toolSession.getToolSessionId().toString());
		gactivityDTO.setOutput(this.getToolOutputsStr(activity, toolSession, learner));

		if (activityState == LearnerProgress.ACTIVITY_ATTEMPTED) {
		    gactivityDTO.setStartDate(learnerProgress.getAttemptedActivities().get(activity));
		} else {
		    if (learnerProgress.getCompletedActivities() != null && learnerProgress.getCompletedActivities().get(activity) != null) {
			gactivityDTO.setStartDate(learnerProgress.getCompletedActivities().get(activity).getStartDate());
		    }
		}
	    }
	}

	return gactivityDTO;
    }

    private Long getActivityDuration(LearnerProgress learnerProgress, Activity activity) {
	if (learnerProgress != null) {
	    if (learnerProgress.getCompletedActivities().get(activity) != null) {
		CompletedActivityProgress compProg = learnerProgress.getCompletedActivities().get(activity);
		if (compProg != null) {
		    Date startTime = compProg.getStartDate();
		    Date endTime = compProg.getFinishDate();
		    if (startTime != null && endTime != null) {
			return endTime.getTime() - startTime.getTime();
		    }
		}
	    }
	}
	return null;
    }

    /**
     * Returns the lesson status string which is a reference to an image
     * 
     * @param learnerProgress
     * @return
     */
    private String getLessonStatusStr(LearnerProgress learnerProgress) {
	String status = "-";

	final String IMAGES_DIR = Configuration.get(ConfigurationKeys.SERVER_URL) + "images";
	if (learnerProgress != null) {
	    if (learnerProgress.isComplete()) {
		status = "<img src='" + IMAGES_DIR + "/tick.png' />";
	    } else if (learnerProgress.getAttemptedActivities() != null
		    && learnerProgress.getAttemptedActivities().size() > 0) {
		status = "<img src='" + IMAGES_DIR + "/cog.png' title='" + learnerProgress.getCurrentActivity().getTitle() + "' />";
	    }
	}
	return status;
    }

    /**
     * Returns the activity status string which is a reference to an image
     * 
     * @param learnerProgress
     * @param activity
     * @return
     */
    private String getActivityStatusStr(LearnerProgress learnerProgress, Activity activity) {

	final String IMAGES_DIR = Configuration.get(ConfigurationKeys.SERVER_URL) + "images";
	if (learnerProgress != null) {
	    byte statusByte = learnerProgress.getProgressState(activity);
	    if (statusByte == LearnerProgress.ACTIVITY_ATTEMPTED) {
		return "<img src='" + IMAGES_DIR + "/cog.png' title='" + learnerProgress.getCurrentActivity().getTitle() + "' />";
	    } else if (statusByte == LearnerProgress.ACTIVITY_COMPLETED) {
		return "<img src='" + IMAGES_DIR + "/tick.png' />";
	    }
	}
	return "-";
    }

    /**
     * Gets the outputs for a tool activity and returns the html for the ouputs
     * cell in the grid
     * 
     * @param toolAct
     * @param toolSession
     * @param learner
     * @return
     */
    private String getToolOutputsStr(ToolActivity toolAct, ToolSession toolSession, User learner) {
	String toolOutputsStr = "";
	boolean noOutputs = true;

	if (toolAct != null && toolSession != null && learner != null) {

	    SortedMap<String, ToolOutputDefinition> map = toolService.getOutputDefinitionsFromTool(toolAct
		    .getToolContentId(), ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);

	    Set<ToolOutput> toolOutputs = new HashSet<ToolOutput>();

	    if (map.keySet().size() > 0) {

		for (String outputName : map.keySet()) {

		    try {
			ToolOutput toolOutput = toolService.getOutputFromTool(outputName, toolSession, learner
				.getUserId());

			if (toolOutput != null && toolOutput.getValue().getType() != OutputType.OUTPUT_COMPLEX) {
			    toolOutputs.add(toolOutput);

			    toolOutputsStr += toolOutput.getDescription() + ": " + toolOutput.getValue().getString();
			    toolOutputsStr += "<br />";

			    noOutputs = false;
			}

		    } catch (RuntimeException e) {
			logger.debug("Runtime exception when attempted to get outputs for activity: "
				+ toolAct.getActivityId() + ", continuing for other activities", e);
		    }
		}
		// toolOutputsStr += "</ul>";
	    }
	}

	// Fix up outputs html if there are not outputs available
	if (noOutputs) {
	    toolOutputsStr = "-";
	}

	return toolOutputsStr;
    }

    /**
     * Gets the
     * 
     * @param activity
     * @return
     */
    private Long getTotalMarksAvailable(ToolActivity activity) {
	SortedMap<String, ToolOutputDefinition> map = toolService.getOutputDefinitionsFromTool(activity
		.getToolContentId(), ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);

	Set<ActivityEvaluation> actEvals = activity.getActivityEvaluations();

	if (map != null) {
	    for (String key : map.keySet()) {
		ToolOutputDefinition definition = map.get(key);
		if (actEvals != null && actEvals.size() > 0) {

		    // get first evaluation
		    ActivityEvaluation actEval = actEvals.iterator().next();

		    if (actEval.getToolOutputDefinition().equals(key)) {

			Object upperLimit = definition.getEndValue();
			if (upperLimit != null && upperLimit instanceof Long) {
			    return (Long) upperLimit;
			}
			break;
		    }
		} else {
		    if (definition.isDefaultGradebookMark() != null && definition.isDefaultGradebookMark()) {
			Object upperLimit = definition.getEndValue();
			if (upperLimit != null && upperLimit instanceof Long) {
			    return (Long) upperLimit;
			}
			break;
		    }
		}
	    }
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    private LearnerProgress getLearnerProgress(Lesson lesson, User user) {
	if (lesson != null && user != null) {
	    Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put("lesson", lesson);
	    properties.put("user", user);
	    List learnerProgressList = baseDAO.findByProperties(LearnerProgress.class, properties);

	    if (learnerProgressList != null && learnerProgressList.size() > 0) {
		return (LearnerProgress) learnerProgressList.get(0);
	    } else {
		return null;
	    }
	} else {
	    return null;
	}
    }

    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    public String getMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    // Getter and setter methods -----------------------------------------------

    public ILamsCoreToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsCoreToolService toolService) {
	this.toolService = toolService;
    }

    public IGradebookDAO getGradebookDAO() {
	return gradebookDAO;
    }

    public void setGradebookDAO(IGradebookDAO gradebookDAO) {
	this.gradebookDAO = gradebookDAO;
    }

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public IUserManagementService getUserService() {
	return userService;
    }

    public void setUserService(IUserManagementService userService) {
	this.userService = userService;
    }

    public IBaseDAO getBaseDAO() {
	return baseDAO;
    }

    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    public IActivityDAO getActivityDAO() {
	return activityDAO;
    }

    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    public MessageService getMessageService() {
	return messageService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    // -------------------------------------------------------------------------
}
