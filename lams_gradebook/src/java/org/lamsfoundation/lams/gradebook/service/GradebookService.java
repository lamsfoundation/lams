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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradebookDAO;
import org.lamsfoundation.lams.gradebook.dto.GBActivityGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBLessonGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.LessonComparator;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
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
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.ExcelCell;
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

    @SuppressWarnings("unchecked")
    public List<GradebookGridRowDTO> getGBActivityRowsForLearner(Lesson lesson, User learner) {

	logger.debug("Getting gradebook user data for lesson: " + lesson.getLessonId() + ". For user: "
		+ learner.getUserId());

	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<GradebookGridRowDTO>();

	Set<ToolActivity> activities = getLessonActivities(lesson);
	for (ToolActivity activity : activities) {

	    String groupName = null;
	    Long groupId = null;
	    if (activity.getGrouping() != null) {
		Group group = activity.getGroupFor(learner);
		if (group != null) {
		    groupName = group.getGroupName();
		    groupId = group.getGroupId();
		}
	    }
	    GBActivityGridRowDTO activityDTO = new GBActivityGridRowDTO(activity, groupName, groupId);

	    // Set the possible marks if applicable
	    activityDTO.setMarksAvailable(this.getTotalMarksAvailable(activity));

	    GradebookUserActivity gradebookActivity = gradebookDAO.getGradebookUserDataForActivity(
		    activity.getActivityId(), learner.getUserId());
	    if (gradebookActivity != null) {
		activityDTO.setMark(gradebookActivity.getMark());
		activityDTO.setFeedback(gradebookActivity.getFeedback());
	    }

	    LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(learner.getUserId(),
		    lesson.getLessonId());
	    // Setting status
	    activityDTO.setStartDate(getActivityStartDate(learnerProgress, activity, learner.getTimeZone()));
	    activityDTO.setTimeTaken(getActivityDuration(learnerProgress, activity));
	    activityDTO.setStatus(getActivityStatusStr(learnerProgress, activity));

	    // Setting averages
	    activityDTO.setAverageMark(gradebookDAO.getAverageMarkForActivity(activity.getActivityId()));
	    activityDTO.setAverageTimeTaken(gradebookDAO.getAverageDurationForActivity(activity.getActivityId()));

	    // Get the tool outputs for this user if there are any
	    ToolSession toolSession = toolService.getToolSessionByLearner(learner, activity);
	    if (toolSession != null && learnerProgress != null) {
		byte activityState = learnerProgress.getProgressState(activity);
		if (activityState == LearnerProgress.ACTIVITY_ATTEMPTED
			|| activityState == LearnerProgress.ACTIVITY_COMPLETED) {
		    // Set the activityLearner URL for this gradebook activity
		    activityDTO.setActivityUrl(Configuration.get(ConfigurationKeys.SERVER_URL)
			    + activity.getTool().getLearnerProgressUrl() + "&userID=" + learner.getUserId()
			    + "&toolSessionID=" + toolSession.getToolSessionId().toString());
		    activityDTO.setOutput(this.getToolOutputsStr(activity, toolSession, learner));
		}
	    }

	    gradebookActivityDTOs.add(activityDTO);
	}

	return gradebookActivityDTOs;
    }

    @SuppressWarnings("unchecked")
    public List<GradebookGridRowDTO> getGBActivityRowsForLesson(Lesson lesson) {

	logger.debug("Getting gradebook data for lesson: " + lesson.getLessonId());

	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<GradebookGridRowDTO>();

	Set<ToolActivity> activities = getLessonActivities(lesson);

	for (ToolActivity activity : activities) {

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
		GBActivityGridRowDTO activityDTO = getGradebookActivityDTO((ToolActivity) activity, lesson, null, null);
		gradebookActivityDTOs.add(activityDTO);
	    }
	}

	return gradebookActivityDTOs;
    }

    @SuppressWarnings("unchecked")
    public List<GBUserGridRowDTO> getGBUserRowsForActivity(Lesson lesson, ToolActivity activity, Long groupId) {

	List<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	Set<User> learners = null;
	if (groupId != null) {
	    Group group = (Group) userService.findById(Group.class, groupId);
	    if (group != null) {
		learners = group.getUsers();
	    } else {
		learners = lesson.getAllLearners();
	    }
	} else {
	    learners = lesson.getAllLearners();
	}

	if (learners != null) {
	    Map<Integer, LearnerProgress> userToLearnerProgressMap = getUserToLearnerProgressMap(lesson);
	    Map<Integer, GradebookUserActivity> userToGradebookUserLessonMap = getUserToGradebookUserActivityMap(activity);
	    Long totalMarksAvailable = getTotalMarksAvailable(activity);

	    for (User learner : learners) {
		GBUserGridRowDTO gUserDTO = new GBUserGridRowDTO(learner);

		gUserDTO.setMarksAvailable(totalMarksAvailable);

		// Set the progress
		LearnerProgress learnerProgress = userToLearnerProgressMap.get(learner.getUserId());
		gUserDTO.setStatus(getActivityStatusStr(learnerProgress, activity));
		gUserDTO.setStartDate(getActivityStartDate(learnerProgress, activity, learner.getTimeZone()));
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
		GradebookUserActivity gradebookUserActivity = userToGradebookUserLessonMap.get(learner.getUserId());
		if (gradebookUserActivity != null) {
		    gUserDTO.setFeedback(gradebookUserActivity.getFeedback());
		    gUserDTO.setMark(gradebookUserActivity.getMark());

		}
		gradebookUserDTOs.add(gUserDTO);
	    }
	}

	return gradebookUserDTOs;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson) {

	ArrayList<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	if (lesson != null) {
	    Set<User> learners = lesson.getAllLearners();

	    if (learners != null) {

		Map<Integer, LearnerProgress> userToLearnerProgressMap = getUserToLearnerProgressMap(lesson);
		Map<Integer, GradebookUserLesson> userToGradebookUserLessonMap = getUserToGradebookUserLessonMap(lesson);

		for (User learner : learners) {
		    LearnerProgress learnerProgress = userToLearnerProgressMap.get(learner.getUserId());
		    GBUserGridRowDTO gradebookUserDTO = new GBUserGridRowDTO(learner);

		    // Setting the status and time taken for the user's lesson
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

		    GradebookUserLesson gradebookUserLesson = userToGradebookUserLessonMap.get(learner.getUserId());
		    if (gradebookUserLesson != null) {
			gradebookUserDTO.setMark(gradebookUserLesson.getMark());
			gradebookUserDTO.setFeedback(gradebookUserLesson.getFeedback());
		    }

		    gradebookUserDTOs.add(gradebookUserDTO);
		}

	    }
	}

	return gradebookUserDTOs;
    }

    public ArrayList<GBUserGridRowDTO> getGBUserRowsForOrganisation(Organisation organisation) {

	ArrayList<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	if (organisation != null) {
	    List<User> learners = userService.getUsersFromOrganisation(organisation.getOrganisationId());

	    if (learners != null) {

		for (User learner : learners) {

		    GBUserGridRowDTO gradebookUserDTO = new GBUserGridRowDTO(learner);
		    gradebookUserDTOs.add(gradebookUserDTO);
		}
	    }
	}

	return gradebookUserDTOs;

    }

    public GradebookUserLesson getGradebookUserLesson(Long lessonID, Integer userID) {
	return gradebookDAO.getGradebookUserDataForLesson(lessonID, userID);
    }

    public GradebookUserActivity getGradebookUserActivity(Long activityID, Integer userID) {
	return gradebookDAO.getGradebookUserDataForActivity(activityID, userID);
    }

    public Double getAverageMarkForActivity(Long activityID, Long groupID) {
	// return AverageMarkForActivity if groupId is null and AverageMarkForGroupedActivity if groupId is specified
	Double averageMark;
	if (groupID == null) {
	    averageMark = gradebookDAO.getAverageMarkForActivity(activityID);
	} else {
	    averageMark = gradebookDAO.getAverageMarkForGroupedActivity(activityID, groupID);
	}
	return averageMark;
    }

    public Double getAverageMarkForLesson(Long lessonID) {
	return gradebookDAO.getAverageMarkForLesson(lessonID);
    }

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

    public void updateUserActivityGradebookMark(Lesson lesson, User learner, Activity activity, Double mark,
	    Boolean markedInGradebook) {
	if (lesson != null && activity != null && learner != null && activity.isToolActivity()) {

	    // First, update the mark for the activity
	    GradebookUserActivity gradebookUserActivity = gradebookDAO.getGradebookUserDataForActivity(
		    activity.getActivityId(), learner.getUserId());

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

    public void updateUserLessonGradebookFeedback(Lesson lesson, User learner, String feedback) {

	GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		learner.getUserId());

	if (gradebookUserLesson == null) {
	    gradebookUserLesson = new GradebookUserLesson(lesson, learner);
	}

	gradebookUserLesson.setFeedback(feedback);
	gradebookDAO.insertOrUpdate(gradebookUserLesson);
    }

    public void updateUserActivityGradebookFeedback(Activity activity, User learner, String feedback) {

	GradebookUserActivity gradebookUserActivity = gradebookDAO.getGradebookUserDataForActivity(
		activity.getActivityId(), learner.getUserId());

	if (gradebookUserActivity == null) {
	    gradebookUserActivity = new GradebookUserActivity((ToolActivity) activity, learner);
	}

	gradebookUserActivity.setFeedback(feedback);
	gradebookDAO.insertOrUpdate(gradebookUserActivity);
    }

    @SuppressWarnings("unchecked")
    public List<GBLessonGridRowDTO> getGBLessonRows(Organisation organisation, User user, User viewer, GBGridView view) {
	List<GBLessonGridRowDTO> lessonRows = new ArrayList<GBLessonGridRowDTO>();

	if (organisation != null) {

	    List<Lesson> lessons = lessonService.getLessonsByGroupAndUser(user.getUserId(),
		    organisation.getOrganisationId());
	    if (lessons != null) {

		for (Lesson lesson : lessons) {

		    // Don't include lesson in list if the user doesn't have permission
		    Integer organisationToCheckPermission = (organisation.getOrganisationType().getOrganisationTypeId()
			    .equals(OrganisationType.COURSE_TYPE)) ? organisation.getOrganisationId() : organisation
			    .getParentOrganisation().getOrganisationId();
		    boolean hasTeacherPermission = lesson.getLessonClass().isStaffMember(viewer)
			    || userService.isUserInRole(viewer.getUserId(), organisationToCheckPermission,
				    Role.GROUP_MANAGER);
		    boolean marksReleased = lesson.getMarksReleased() != null && lesson.getMarksReleased();
		    boolean hasLearnerPermission = lesson.getAllLearners().contains(user);
		    if (!((view == GBGridView.MON_COURSE) && hasTeacherPermission || (view == GBGridView.LRN_COURSE)
			    && hasLearnerPermission && marksReleased || (view == GBGridView.MON_USER)
			    && hasTeacherPermission && hasLearnerPermission)) {
			continue;
		    }

		    GBLessonGridRowDTO lessonRow = new GBLessonGridRowDTO();
		    lessonRow.setLessonName(lesson.getLessonName());
		    lessonRow.setId(lesson.getLessonId().toString());
		    lessonRow.setStartDate(getLocaleDateString(viewer, lesson.getStartDateTime()));

		    if (view == GBGridView.MON_COURSE) {

			// Setting the averages for monitor view
			lessonRow.setAverageTimeTaken(gradebookDAO.getAverageDurationLesson(lesson.getLessonId()));
			lessonRow.setAverageMark(gradebookDAO.getAverageMarkForLesson(lesson.getLessonId()));

			// Set the gradebook monitor url
			String gbMonURL = Configuration.get(ConfigurationKeys.SERVER_URL)
				+ "gradebook/gradebookMonitoring.do?lessonID=" + lesson.getLessonId().toString();
			lessonRow.setGradebookMonitorURL(gbMonURL);
		    } else if ((view == GBGridView.LRN_COURSE) || (view == GBGridView.MON_USER)) {

			GradebookUserLesson gbLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
				user.getUserId());

			lessonRow.setAverageTimeTaken(gradebookDAO.getAverageDurationLesson(lesson.getLessonId()));
			lessonRow.setAverageMark(gradebookDAO.getAverageMarkForLesson(lesson.getLessonId()));

			if (gbLesson != null) {
			    lessonRow.setMark(gbLesson.getMark());
			    lessonRow.setFeedback(gbLesson.getFeedback());
			}

			LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(user.getUserId(),
				lesson.getLessonId());
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

    private HashMap<ToolActivity, List<GBUserGridRowDTO>> getDataForLessonGradebookExport(Lesson lesson) {

	HashMap<ToolActivity, List<GBUserGridRowDTO>> activityToUserDTOMap = new HashMap<ToolActivity, List<GBUserGridRowDTO>>();

	Set<User> learners = lesson.getAllLearners();
	if (learners == null) {
	    learners = new TreeSet<User>();
	}
	Map<Integer, LearnerProgress> userToLearnerProgressMap = getUserToLearnerProgressMap(lesson);
	List<ToolSession> toolSessions = toolService.getToolSessionsByLesson(lesson);
	Set<ToolActivity> activities = getLessonActivities(lesson);
	Map<Long, Long> activityToTotalMarkMap = getActivityToTotalMarkMap(activities);

	for (ToolActivity activity : activities) {

	    Map<Integer, GradebookUserActivity> userToGradebookUserActivityMap = getUserToGradebookUserActivityMap(activity);

	    List<GBUserGridRowDTO> userDTOs = new ArrayList<GBUserGridRowDTO>();

	    for (User learner : learners) {
		GBUserGridRowDTO userDTO = new GBUserGridRowDTO(learner);

		Long activityTotalMarks = activityToTotalMarkMap.get(activity.getActivityId());
		userDTO.setMarksAvailable(activityTotalMarks);

		// Set the progress
		LearnerProgress learnerProgress = userToLearnerProgressMap.get(learner.getUserId());
		userDTO.setTimeTaken(getActivityDuration(learnerProgress, activity));

		// find required toolSession from toolSessions (thus we don't querying DB and hence increasing
		// efficiency).
		ToolSession toolSession = null;
		for (ToolSession dbToolSession : toolSessions) {
		    if (dbToolSession.getToolActivity().getActivityId().equals(activity.getActivityId())
			    && dbToolSession.getLearners().contains(learner)) {
			toolSession = dbToolSession;
		    }
		}

		// Get the tool outputs for this user if there are any.
		if (toolSession != null && learnerProgress != null) {
		    // Set the activityLearner URL for this gradebook activity
		    byte activityState = learnerProgress.getProgressState(activity);
		    if (activityState == LearnerProgress.ACTIVITY_ATTEMPTED
			    || activityState == LearnerProgress.ACTIVITY_COMPLETED) {
			userDTO.setActivityUrl(Configuration.get(ConfigurationKeys.SERVER_URL)
				+ activity.getTool().getLearnerProgressUrl() + "&userID=" + learner.getUserId()
				+ "&toolSessionID=" + toolSession.getToolSessionId().toString());
			userDTO.setOutput(this.getToolOutputsStr(activity, toolSession, learner));
		    }
		}

		// Add marks and feedback
		GradebookUserActivity gradebookUserActivity = userToGradebookUserActivityMap.get(learner.getUserId());
		if (gradebookUserActivity != null) {
		    userDTO.setFeedback(gradebookUserActivity.getFeedback());
		    userDTO.setMark(gradebookUserActivity.getMark());
		}
		userDTOs.add(userDTO);
	    }
	    activityToUserDTOMap.put(activity, userDTOs);
	}

	return activityToUserDTOMap;
    }

    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, ExcelCell[][]> exportLessonGradebook(Lesson lesson) {

	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();

	// -------------------- process summary excel page --------------------------------

	// The entire data list
	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();

	// Adding the lesson average data to the summary
	ExcelCell[] lessonAverageMark = new ExcelCell[2];
	lessonAverageMark[0] = new ExcelCell(getMessage("gradebook.export.average.lesson.mark"), true);
	lessonAverageMark[1] = new ExcelCell(getAverageMarkForLesson(lesson.getLessonId()), false);
	rowList.add(lessonAverageMark);

	ExcelCell[] lessonAverageTimeTaken = new ExcelCell[2];
	lessonAverageTimeTaken[0] = new ExcelCell(getMessage("gradebook.export.average.lesson.time.taken"), true);
	lessonAverageTimeTaken[1] = new ExcelCell(gradebookDAO.getAverageDurationLesson(lesson.getLessonId()) / 1000,
		false);
	rowList.add(lessonAverageTimeTaken);
	rowList.add(EMPTY_ROW);

	// Adding the activity average data to the summary
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

	// Adding the user lesson marks to the summary
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

	ExcelCell[][] summaryData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("gradebook.export.lesson.summary"), summaryData);

	// -------------------- process activity excel page --------------------------------

	HashMap<ToolActivity, List<GBUserGridRowDTO>> activityToUserDTOMap = getDataForLessonGradebookExport(lesson);
	List<ExcelCell[]> rowList1 = new LinkedList<ExcelCell[]>();

	for (Activity activity : activityToUserDTOMap.keySet()) {

	    ExcelCell[] activityTitleRow = new ExcelCell[5];
	    activityTitleRow[0] = new ExcelCell(activity.getTitle(), true);
	    rowList1.add(activityTitleRow);
	    ExcelCell[] titleRow = new ExcelCell[5];
	    titleRow[0] = new ExcelCell(getMessage("gradebook.export.last.name"), true);
	    titleRow[1] = new ExcelCell(getMessage("gradebook.export.first.name"), true);
	    titleRow[2] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
	    titleRow[3] = new ExcelCell(getMessage("gradebook.export.outputs"), true);
	    titleRow[4] = new ExcelCell(getMessage("gradebook.columntitle.mark"), true);
	    rowList1.add(titleRow);

	    // Get the rest of the data
	    List<GBUserGridRowDTO> userRows2 = activityToUserDTOMap.get(activity);
	    for (GBUserGridRowDTO userRow : userRows2) {
		ExcelCell[] userDataRow = new ExcelCell[5];

		userDataRow[0] = new ExcelCell(userRow.getLastName(), false);
		userDataRow[1] = new ExcelCell(userRow.getFirstName(), false);
		userDataRow[2] = new ExcelCell(userRow.getTimeTakenSeconds(), false);
		userDataRow[3] = new ExcelCell(userRow.getOutput(), false);
		userDataRow[4] = new ExcelCell(userRow.getMark(), false);
		rowList1.add(userDataRow);
	    }

	    rowList1.add(EMPTY_ROW);
	}

	ExcelCell[][] activityData = rowList1.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("gradebook.gridtitle.activitygrid"), activityData);

	// -------------------- process user excel page --------------------------------

	Set<User> learners = lesson.getAllLearners();
	if (learners == null) {
	    learners = new TreeSet<User>();
	}

	LinkedList<ExcelCell[]> rowList2 = new LinkedList<ExcelCell[]>();
	for (User learner : learners) {

	    List<GBActivityGridRowDTO> activityDTOs = new ArrayList<GBActivityGridRowDTO>();
	    for (ToolActivity activity : activityToUserDTOMap.keySet()) {

		List<GBUserGridRowDTO> userDTOs = activityToUserDTOMap.get(activity);
		GBUserGridRowDTO userDTO = null;
		for (GBUserGridRowDTO dbUserDTO : userDTOs) {
		    if (dbUserDTO.getId().equals(learner.getUserId().toString())) {
			userDTO = dbUserDTO;
		    }
		}

		String groupName = null;
		Long groupId = null;
		if (activity.getGrouping() != null) {
		    Group group = activity.getGroupFor(learner);
		    if (group != null) {
			groupName = group.getGroupName();
			groupId = group.getGroupId();
		    }
		}
		GBActivityGridRowDTO activityDTO = new GBActivityGridRowDTO(activity, groupName, groupId);
		activityDTO.setMarksAvailable(userDTO.getMarksAvailable());
		activityDTO.setStatus(userDTO.getStatus());
		activityDTO.setStartDate(userDTO.getStartDate());
		activityDTO.setTimeTaken(userDTO.getTimeTaken());
		activityDTO.setActivityUrl(userDTO.getActivityUrl());
		activityDTO.setOutput(userDTO.getOutput());
		activityDTO.setFeedback(userDTO.getFeedback());
		activityDTO.setMark(userDTO.getMark());

		activityDTOs.add(activityDTO);
	    }

	    ExcelCell[] activityTitleRow = new ExcelCell[4];
	    activityTitleRow[0] = new ExcelCell(learner.getFullName(), true);
	    rowList2.add(activityTitleRow);

	    ExcelCell[] titleRow = new ExcelCell[4];
	    titleRow[0] = new ExcelCell(getMessage("gradebook.export.activity"), true);
	    titleRow[1] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
	    titleRow[2] = new ExcelCell(getMessage("gradebook.export.outputs"), true);
	    titleRow[3] = new ExcelCell(getMessage("gradebook.columntitle.mark"), true);
	    rowList2.add(titleRow);

	    for (GBActivityGridRowDTO activityRow : activityDTOs) {
		ExcelCell[] activityDataRow = new ExcelCell[4];
		activityDataRow[0] = new ExcelCell(activityRow.getRowName(), false);
		activityDataRow[1] = new ExcelCell(activityRow.getTimeTakenSeconds(), false);
		activityDataRow[2] = new ExcelCell(activityRow.getOutput(), false);
		activityDataRow[3] = new ExcelCell(activityRow.getMark(), false);
		rowList2.add(activityDataRow);
	    }

	    rowList2.add(EMPTY_ROW);
	}

	ExcelCell[][] userData = rowList2.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("gradebook.export.learner.view"), userData);

	return dataToExport;
    }

    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, ExcelCell[][]> exportCourseGradebook(Integer userId, Integer organisationId) {
	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();

	Organisation organisation = (Organisation) userService.findById(Organisation.class, organisationId);

	// The entire data list
	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();

	User user = (User) userService.findById(User.class, userId);
	Set<Lesson> lessonsFromDB = new TreeSet<Lesson>(new LessonComparator());
	lessonsFromDB.addAll(lessonService.getLessonsByGroupAndUser(userId, organisationId));

	// Dont include lesson in list if the user doesnt have permission
	Integer organisationToCheckPermission = (organisation.getOrganisationType().getOrganisationTypeId()
		.equals(OrganisationType.COURSE_TYPE)) ? organisation.getOrganisationId() : organisation
		.getParentOrganisation().getOrganisationId();
	boolean isGroupManager = userService.isUserInRole(userId, organisationToCheckPermission, Role.GROUP_MANAGER);
	List<Lesson> lessons = new LinkedList<Lesson>();
	for (Lesson lesson : lessonsFromDB) {
	    if (!(lesson.getLessonClass().isStaffMember(user) || isGroupManager)) {
		continue;
	    }

	    lessons.add(lesson);
	}

	if (lessons != null && (lessons.size() > 0)) {

	    // collect users from all lessons
	    Set<User> allLearners = new LinkedHashSet<User>();
	    for (Lesson lesson : lessons) {
		Set dbLessonUsers = lesson.getAllLearners();
		allLearners.addAll(dbLessonUsers);
	    }

	    int numberOfCellsInARow = 2 + lessons.size() * 4;

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
		List<GBUserGridRowDTO> userRows = getGBUserRowsForUser(learner, lessons, organisationId);
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
	}

	ExcelCell[][] summaryData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("gradebook.exportcourse.course.summary"), summaryData);
	return dataToExport;
    }

    @Override
    public LinkedHashMap<String, ExcelCell[][]> exportSelectedLessonsGradebook(Integer userId, Integer organisationId,
	    String[] lessonIds) {
	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();

	Organisation organisation = (Organisation) userService.findById(Organisation.class, organisationId);

	// The entire data list
	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();

	User user = (User) userService.findById(User.class, userId);
	Set<Lesson> selectedLessons = new TreeSet<Lesson>(new LessonComparator());

	// Don't include lesson in list if the user doesnt have permission
	Integer organisationToCheckPermission = (organisation.getOrganisationType().getOrganisationTypeId()
		.equals(OrganisationType.COURSE_TYPE)) ? organisation.getOrganisationId() : organisation
		.getParentOrganisation().getOrganisationId();
	boolean isGroupManager = userService.isUserInRole(userId, organisationToCheckPermission, Role.GROUP_MANAGER);

	// collect users from all lessons
	Set<User> allLearners = new LinkedHashSet<User>();
	Map<Long, Set<ToolActivity>> lessonActivitiesMap = new HashMap<Long, Set<ToolActivity>>();
	List<ToolActivity> allActivities = new ArrayList<ToolActivity>();

	for (String lessonIdStr : lessonIds) {
	    Long lessonId = Long.parseLong(lessonIdStr);
	    Lesson lesson = lessonService.getLesson(lessonId);

	    if (!(lesson.getLessonClass().isStaffMember(user) || isGroupManager)) {
		continue;
	    }

	    selectedLessons.add(lesson);

	    allLearners.addAll(lesson.getAllLearners());

	    Set<ToolActivity> lessonActivities = getLessonActivities(lesson);
	    lessonActivitiesMap.put(lesson.getLessonId(), lessonActivities);
	    allActivities.addAll(lessonActivities);
	}

	if (!selectedLessons.isEmpty()) {

	    Map<Long, Long> activityToTotalMarkMap = getActivityToTotalMarkMap(allActivities);

	    Map<Long, Map<Integer, GradebookUserActivity>> activityTouserToGradebookUserActivityMap = new HashMap<Long, Map<Integer, GradebookUserActivity>>();
	    for (ToolActivity activity : allActivities) {
		Map<Integer, GradebookUserActivity> userToGradebookUserActivityMap = getUserToGradebookUserActivityMap(activity);
		activityTouserToGradebookUserActivityMap.put(activity.getActivityId(), userToGradebookUserActivityMap);
	    }

	    int numberCellsPerRow = selectedLessons.size() * 6 + allActivities.size() * 2 + 5;

	    // Lesson names row----------------------
	    ExcelCell[] lessonsNames = new ExcelCell[numberCellsPerRow];
	    int i = 4;
	    for (Lesson lesson : selectedLessons) {
		Set<ToolActivity> lessonActivities = lessonActivitiesMap.get(lesson.getLessonId());
		int numberActivities = lessonActivities.size();
		lessonsNames[i + numberActivities] = new ExcelCell(lesson.getLessonName(), true);
		i += 6 + numberActivities * 2;
	    }
	    i -= 2;
	    lessonsNames[i++] = new ExcelCell("", ExcelCell.BORDER_STYLE_LEFT_THIN);
	    lessonsNames[i++] = new ExcelCell(getMessage("label.overall.totals"), true);
	    lessonsNames[i++] = new ExcelCell("", ExcelCell.BORDER_STYLE_RIGHT_THICK);

	    rowList.add(lessonsNames);

	    // Headers row----------------------
	    ExcelCell[] headerRow = new ExcelCell[numberCellsPerRow];
	    i = 0;

	    for (Lesson lesson : selectedLessons) {
		headerRow[i++] = new ExcelCell(getMessage("gradebook.export.last.name"), false);
		headerRow[i++] = new ExcelCell(getMessage("gradebook.export.first.name"), false);
		headerRow[i++] = new ExcelCell(getMessage("label.group"), false);

		Set<ToolActivity> activities = lessonActivitiesMap.get(lesson.getLessonId());
		for (Activity activity : activities) {
		    headerRow[i++] = new ExcelCell(activity.getTitle(), true);
		    headerRow[i++] = new ExcelCell(getMessage("label.max.possible"), false);
		}

		headerRow[i++] = new ExcelCell(getMessage("label.total.actuals"), true,
			ExcelCell.BORDER_STYLE_LEFT_THIN);
		headerRow[i++] = new ExcelCell(getMessage("label.max.mark"), false);
		headerRow[i++] = new ExcelCell("%", ExcelCell.BORDER_STYLE_RIGHT_THICK);
	    }
	    i += 2;
	    headerRow[i++] = new ExcelCell(getMessage("label.actuals"), true, ExcelCell.BORDER_STYLE_LEFT_THIN);
	    headerRow[i++] = new ExcelCell(getMessage("label.max"), true);
	    headerRow[i++] = new ExcelCell("%", true, ExcelCell.BORDER_STYLE_RIGHT_THICK);
	    rowList.add(headerRow);

	    // Actual data rows----------------------
	    for (User learner : allLearners) {

		Double overallTotal = 0d;
		Double overallMaxMark = 0d;
		ExcelCell[] userRow = new ExcelCell[numberCellsPerRow];
		i = 0;

		for (Lesson lesson : selectedLessons) {

		    Double lessonTotal = 0d;
		    Double lessonMaxMark = 0d;
		    Set<ToolActivity> activities = lessonActivitiesMap.get(lesson.getLessonId());

		    String lastName = (learner.getLastName() == null) ? learner.getLogin().toUpperCase() : learner
			    .getLastName().toUpperCase();
		    userRow[i++] = new ExcelCell(lastName, false);
		    String firstName = (learner.getFirstName() == null) ? "" : learner.getFirstName().toUpperCase();
		    userRow[i++] = new ExcelCell(firstName, false);

		    // check if learner is participating in this lesson
		    if (!lesson.getAllLearners().contains(learner)) {
			i += 1 + activities.size() * 2;
			userRow[i++] = new ExcelCell("", ExcelCell.BORDER_STYLE_LEFT_THIN);
			userRow[i++] = new ExcelCell("", false);
			userRow[i++] = new ExcelCell("", ExcelCell.BORDER_STYLE_RIGHT_THICK);
			continue;
		    }

		    // getUserGroupName
		    String groupName = "";
		    for (Group group : (Set<Group>) lesson.getLessonClass().getGroups()) {
			if (group.hasLearner(learner)) {
			    groupName = group.getGroupName();
			    break;
			}
		    }
		    userRow[i++] = new ExcelCell(groupName, false);

		    for (ToolActivity activity : activities) {
			Map<Integer, GradebookUserActivity> userToGradebookUserActivityMap = activityTouserToGradebookUserActivityMap
				.get(activity.getActivityId());
			GradebookUserActivity gradebookUserActivity = userToGradebookUserActivityMap.get(learner
				.getUserId());
			Double mark = 0d;
			if (gradebookUserActivity != null) {
			    mark = gradebookUserActivity.getMark();
			    userRow[i++] = new ExcelCell(mark, false);
			} else {
			    userRow[i++] = new ExcelCell("", false);
			}

			Long activityTotalMarks = (activityToTotalMarkMap.get(activity.getActivityId()) != null) ? activityToTotalMarkMap
				.get(activity.getActivityId()) : 0l;
			userRow[i++] = new ExcelCell(activityTotalMarks, false);

			lessonTotal += mark;
			overallTotal += mark;
			lessonMaxMark += activityTotalMarks;
			overallMaxMark += activityTotalMarks;
		    }

		    userRow[i++] = new ExcelCell(lessonTotal, ExcelCell.BORDER_STYLE_LEFT_THIN);
		    userRow[i++] = new ExcelCell(lessonMaxMark, false);
		    Double percentage = (lessonMaxMark != 0) ? lessonTotal / lessonMaxMark : 0d;
		    userRow[i++] = new ExcelCell(percentage, ExcelCell.BORDER_STYLE_RIGHT_THICK);

		}

		i += 2;
		userRow[i++] = new ExcelCell(overallTotal, ExcelCell.BORDER_STYLE_LEFT_THIN);
		userRow[i++] = new ExcelCell(overallMaxMark, false);
		Double percentage = (overallMaxMark != 0) ? overallTotal / overallMaxMark : 0d;
		userRow[i++] = new ExcelCell(percentage, true, ExcelCell.BORDER_STYLE_RIGHT_THICK);

		rowList.add(userRow);
	    }
	}

	ExcelCell[][] summaryData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("gradebook.exportcourse.course.summary"), summaryData);
	return dataToExport;
    }

    public void updateActivityMark(Double mark, String feedback, Integer userID, Long toolSessionID,
	    Boolean markedInGradebook) {
	ToolSession toolSession = toolService.getToolSessionById(toolSessionID);
	User learner = (User) userService.findById(User.class, userID);
	if (learner != null && toolSession != null) {
	    ToolActivity activity = toolSession.getToolActivity();
	    GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(activity.getActivityId(), userID);

	    // If gradebook user activity is null or the mark is set by teacher or was set previously by user - save the
	    // mark and feedback
	    if (gradebookUserActivity == null || markedInGradebook || !gradebookUserActivity.getMarkedInGradebook()) {
		updateUserActivityGradebookMark(toolSession.getLesson(), learner, activity, mark, markedInGradebook);
		updateUserActivityGradebookFeedback(activity, learner, feedback);
	    }
	}
    }

    public Activity getActivityById(Long activityID) {
	return activityDAO.getActivityByActivityId(activityID);
    }

    /**
     * Returns lesson activities. It works almost the same as lesson.getLearningDesign().getActivities() except it
     * solves problem with first activity unable to cast to ToolActivity.
     */
    private Set<ToolActivity> getLessonActivities(Lesson lesson) {
	Set<Activity> activities = new TreeSet<Activity>();
	Set<ToolActivity> toolActivities = new TreeSet<ToolActivity>();

	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 * 
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */
	Activity firstActivity = activityDAO.getActivityByActivityId(lesson.getLearningDesign().getFirstActivity()
		.getActivityId());
	activities.add(firstActivity);
	activities.addAll(lesson.getLearningDesign().getActivities());

	for (Activity activity : activities) {
	    if (activity instanceof ToolActivity) {
		ToolActivity toolActivity = (ToolActivity) activity;
		toolActivities.add(toolActivity);
	    }
	}

	return toolActivities;
    }

    /**
     * Returns list of GBUserGridRowDTOs, all of which will be displayed on 1 line on course export.
     * 
     * @param learner
     * @param lessons
     * @return
     */
    private List<GBUserGridRowDTO> getGBUserRowsForUser(User learner, List<Lesson> lessons, Integer organisationId) {

	List<GBUserGridRowDTO> gradebookUserDTOs = new LinkedList<GBUserGridRowDTO>();
	Map<Long, LearnerProgress> lessonLearnerProgressMap = getLessonToLearnerProgressMap(learner, organisationId);
	Map<Long, GradebookUserLesson> gradebookUserLessonMap = getLessonToGradebookUserLessonMap(learner,
		organisationId);

	for (Lesson lesson : lessons) {
	    GBUserGridRowDTO gradebookUserDTO = new GBUserGridRowDTO(learner);

	    // check if learner is participating in this lesson
	    if (lesson.getAllLearners().contains(learner)) {
		// Setting the status and time taken for the user's lesson
		LearnerProgress learnerProgress = lessonLearnerProgressMap.get(lesson.getLessonId());
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

		GradebookUserLesson gradebookUserLesson = gradebookUserLessonMap.get(lesson.getLessonId());
		if (gradebookUserLesson != null) {
		    gradebookUserDTO.setMark(gradebookUserLesson.getMark());
		    gradebookUserDTO.setFeedback(gradebookUserLesson.getFeedback());
		}
	    } else {
		gradebookUserDTO.setStatus("n/a");
	    }

	    gradebookUserDTOs.add(gradebookUserDTO);
	}

	return gradebookUserDTOs;

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
     * 
     * @param userRow
     * @return
     */
    private String getProgressMessage(GBUserGridRowDTO userRow) {
	String originalStatus = userRow.getStatus();

	String status;
	if (originalStatus.contains("tick.png")) {
	    status = getMessage("gradebook.exportcourse.ok");
	} else if (originalStatus.contains("cog.png")) {
	    status = getMessage("gradebook.exportcourse.current.activity",
		    new String[] { userRow.getCurrentActivity() });
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

	GBActivityGridRowDTO activityDTO = new GBActivityGridRowDTO(activity, groupName, groupId);
	if (groupName != null && groupId != null) {

	    // Setting averages for group
	    activityDTO
		    .setAverageMark(gradebookDAO.getAverageMarkForGroupedActivity(activity.getActivityId(), groupId));
	    activityDTO.setAverageTimeTaken(gradebookDAO.getAverageDurationForGroupedActivity(activity.getActivityId(),
		    groupId));

	} else {
	    // Setting averages for lesson
	    activityDTO.setAverageMark(gradebookDAO.getAverageMarkForActivity(activity.getActivityId()));
	    activityDTO.setAverageTimeTaken(gradebookDAO.getAverageDurationForActivity(activity.getActivityId()));
	}

	// Set the possible marks if applicable
	activityDTO.setMarksAvailable(this.getTotalMarksAvailable(activity));

	String monitorUrl = Configuration.get(ConfigurationKeys.SERVER_URL) + activity.getTool().getMonitorUrl() + "?"
		+ AttributeNames.PARAM_CONTENT_FOLDER_ID + "=" + lesson.getLearningDesign().getContentFolderID() + "&"
		+ AttributeNames.PARAM_TOOL_CONTENT_ID + "=" + activity.getToolContentId();
	activityDTO.setMonitorUrl(monitorUrl);

	return activityDTO;
    }

    /**
     * Gets either attempted or completed activity start time ajusted to monitor time zone.
     * 
     * @param learnerProgress
     * @param activity
     * @param timeZone
     * @return
     */
    private Date getActivityStartDate(LearnerProgress learnerProgress, Activity activity, String timeZone) {
	Date startDate = null;
	if (learnerProgress != null) {
	    startDate = learnerProgress.getAttemptedActivities().get(activity);
	    if (startDate == null) {
		CompletedActivityProgress compProg = learnerProgress.getCompletedActivities().get(activity);
		if (compProg != null) {
		    startDate = compProg.getStartDate();
		}
	    }
	}

	if (startDate != null) {
	    if (StringUtils.isBlank(timeZone)) {
		logger.warn("No user time zone provided, leaving server default");
	    } else {
		if (logger.isTraceEnabled()) {
		    logger.trace("Adjusting time according to zone \"" + timeZone + "\"");
		}
		TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);
		startDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, startDate);
	    }
	}
	return startDate;
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
		status = "<img src='" + IMAGES_DIR + "/cog.png' title='"
			+ learnerProgress.getCurrentActivity().getTitle() + "' />";
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
		return "<img src='" + IMAGES_DIR + "/cog.png' title='"
			+ learnerProgress.getCurrentActivity().getTitle() + "' />";
	    } else if (statusByte == LearnerProgress.ACTIVITY_COMPLETED) {
		return "<img src='" + IMAGES_DIR + "/tick.png' />";
	    }
	}
	return "-";
    }

    /**
     * Gets the outputs for a tool activity and returns the html for the ouputs cell in the grid
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

	    SortedMap<String, ToolOutputDefinition> map = toolService.getOutputDefinitionsFromTool(
		    toolAct.getToolContentId(), ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);

	    Set<ToolOutput> toolOutputs = new HashSet<ToolOutput>();

	    if (map.keySet().size() > 0) {

		for (String outputName : map.keySet()) {

		    try {
			ToolOutput toolOutput = toolService.getOutputFromTool(outputName, toolSession,
				learner.getUserId());

			if (toolOutput != null && toolOutput.getValue().getType() != OutputType.OUTPUT_COMPLEX) {
			    toolOutputs.add(toolOutput);

			    toolOutputsStr += toolOutput.getDescription() + ": " + toolOutput.getValue().getString();
			    toolOutputsStr += "<br />";

			    noOutputs = false;
			}

		    } catch (RuntimeException e) {
			logger.debug(
				"Runtime exception when attempted to get outputs for activity: "
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
	SortedMap<String, ToolOutputDefinition> map = toolService.getOutputDefinitionsFromTool(
		activity.getToolContentId(), ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);

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

    /**
     * Returns map containing (userId -> LearnerProgressMap) pairs. It serves merely for optimizing amount of db
     * queries.
     */
    private Map<Integer, LearnerProgress> getUserToLearnerProgressMap(Lesson lesson) {

	if (lesson != null) {
	    List<LearnerProgress> learnerProgressList = lessonService.getUserProgressForLesson(lesson.getLessonId());

	    if (learnerProgressList != null && learnerProgressList.size() > 0) {
		Map<Integer, LearnerProgress> map = new HashMap<Integer, LearnerProgress>();
		for (LearnerProgress learnerProgress : learnerProgressList) {
		    map.put(learnerProgress.getUser().getUserId(), learnerProgress);
		}
		return map;
	    }
	}

	return new HashMap<Integer, LearnerProgress>();
    }

    /**
     * Returns map containing (userId -> GradebookUserActivity) pairs. It serves merely for optimizing amount of db
     * queries.
     */
    private Map<Integer, GradebookUserActivity> getUserToGradebookUserActivityMap(Activity activity) {

	if (activity != null) {

	    List<GradebookUserActivity> gradebookUserActivities = gradebookDAO
		    .getAllGradebookUserActivitiesForActivity(activity.getActivityId());

	    if (gradebookUserActivities != null && gradebookUserActivities.size() > 0) {
		Map<Integer, GradebookUserActivity> map = new HashMap<Integer, GradebookUserActivity>();
		for (GradebookUserActivity gradebookUserActivity : gradebookUserActivities) {
		    map.put(gradebookUserActivity.getLearner().getUserId(), gradebookUserActivity);
		}
		return map;
	    }
	}

	return new HashMap<Integer, GradebookUserActivity>();
    }

    /**
     * Returns map containing (activityId -> TotalMarksAvailable) pairs. It serves merely for optimizing amount of db
     * queries.
     */
    private Map<Long, Long> getActivityToTotalMarkMap(Collection<ToolActivity> activities) {

	Map<Long, Long> map = new HashMap<Long, Long>();
	for (ToolActivity activity : activities) {
	    map.put(activity.getActivityId(), getTotalMarksAvailable(activity));
	}

	return map;
    }

    /**
     * Returns map containing (lessonId -> LearnerProgress) pairs. It serves merely for optimizing amount of db queries.
     */
    private Map<Long, LearnerProgress> getLessonToLearnerProgressMap(User user, Integer organisationId) {

	if (user != null) {
	    String query = "select lp from LearnerProgress lp where lp.user.userId=? and (lp.lesson.organisation.organisationId=? or lp.lesson.organisation.parentOrganisation.organisationId=?)";
	    List<LearnerProgress> learnerProgressList = baseDAO.find(query, new Object[] { user.getUserId(),
		    organisationId, organisationId });

	    if (learnerProgressList != null && learnerProgressList.size() > 0) {
		Map<Long, LearnerProgress> map = new HashMap<Long, LearnerProgress>();
		for (LearnerProgress learnerProgress : learnerProgressList) {
		    map.put(learnerProgress.getLesson().getLessonId(), learnerProgress);
		}
		return map;
	    }
	}

	return new HashMap<Long, LearnerProgress>();
    }

    /**
     * Returns map containing (userId -> GradebookUserLesson) pairs. It serves merely for optimizing amount of db
     * queries.
     */
    private Map<Integer, GradebookUserLesson> getUserToGradebookUserLessonMap(Lesson lesson) {

	if (lesson != null) {
	    String query = "select ul from GradebookUserLesson ul where ul.lesson.lessonId=?";
	    List<GradebookUserLesson> gradebookUserLessons = baseDAO.find(query, new Object[] { lesson.getLessonId() });

	    if (gradebookUserLessons != null && gradebookUserLessons.size() > 0) {
		Map<Integer, GradebookUserLesson> map = new HashMap<Integer, GradebookUserLesson>();
		for (GradebookUserLesson gradebookUserLesson : gradebookUserLessons) {
		    map.put(gradebookUserLesson.getLearner().getUserId(), gradebookUserLesson);
		}
		return map;
	    }
	}

	return new HashMap<Integer, GradebookUserLesson>();
    }

    /**
     * Returns map containing (lessonId -> GradebookUserLesson) pairs. It serves merely for optimizing amount of db
     * queries.
     */
    private Map<Long, GradebookUserLesson> getLessonToGradebookUserLessonMap(User user, Integer organisationId) {

	if (user != null) {
	    String query = "select ul from GradebookUserLesson ul where ul.learner.userId=? and (ul.lesson.organisation.organisationId=? or ul.lesson.organisation.parentOrganisation.organisationId=?)";
	    List<GradebookUserLesson> gradebookUserLessons = baseDAO.find(query, new Object[] { user.getUserId(),
		    organisationId, organisationId });

	    if (gradebookUserLessons != null && gradebookUserLessons.size() > 0) {
		Map<Long, GradebookUserLesson> map = new HashMap<Long, GradebookUserLesson>();
		for (GradebookUserLesson gradebookUserLesson : gradebookUserLessons) {
		    map.put(gradebookUserLesson.getLesson().getLessonId(), gradebookUserLesson);
		}
		return map;
	    }
	}

	return new HashMap<Long, GradebookUserLesson>();
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
