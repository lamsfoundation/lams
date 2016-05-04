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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.lamsfoundation.lams.gradebook.util.GradebookConstants;
import org.lamsfoundation.lams.gradebook.util.LessonComparator;
import org.lamsfoundation.lams.gradebook.util.UserComparator;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputValue;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.session.SessionManager;
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
    private ILearnerProgressDAO learnerProgressDAO;
    private ILessonService lessonService;
    private IUserManagementService userService;
    private IBaseDAO baseDAO;
    private IActivityDAO activityDAO;
    private MessageService messageService;
    private IAuditService auditService;

    @Override
    public List<GradebookGridRowDTO> getGBActivityRowsForLearner(Long lessonId, Integer userId) {
	GradebookService.logger.debug("Getting gradebook user data for lesson: " + lessonId + ". For user: " + userId);

	Lesson lesson = lessonService.getLesson(lessonId);
	User learner = (User) userService.findById(User.class, userId);

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
	    activityDTO.setMarksAvailable(toolService.getActivityMaxPossibleMark(activity));

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
	    if ((toolSession != null) && (learnerProgress != null)) {
		byte activityState = learnerProgress.getProgressState(activity);
		if ((activityState == LearnerProgress.ACTIVITY_ATTEMPTED)
			|| (activityState == LearnerProgress.ACTIVITY_COMPLETED)) {
		    // Set the activityLearner URL for this gradebook activity
		    activityDTO.setActivityUrl(Configuration.get(ConfigurationKeys.SERVER_URL)
			    + activity.getTool().getLearnerProgressUrl() + "&userID=" + learner.getUserId()
			    + "&toolSessionID=" + toolSession.getToolSessionId().toString());
		}
	    }

	    gradebookActivityDTOs.add(activityDTO);
	}

	return gradebookActivityDTOs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GradebookGridRowDTO> getGBActivityRowsForLesson(Long lessonId) {
	GradebookService.logger.debug("Getting gradebook data for lesson: " + lessonId);
	
	Lesson lesson = lessonService.getLesson(lessonId);
	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<GradebookGridRowDTO>();

	Set<ToolActivity> activities = getLessonActivities(lesson);

	for (ToolActivity activity : activities) {

	    Grouping grouping = activity.getGrouping();
	    if (grouping != null) {
		Set<Group> groups = grouping.getGroups();
		if (groups != null) {

		    for (Group group : groups) {
			GBActivityGridRowDTO activityDTO = getGradebookActivityDTO(activity, lesson,
				group.getGroupName(), group.getGroupId());
			gradebookActivityDTOs.add(activityDTO);
		    }

		}
	    } else {
		GBActivityGridRowDTO activityDTO = getGradebookActivityDTO(activity, lesson, null, null);
		gradebookActivityDTOs.add(activityDTO);
	    }
	}

	return gradebookActivityDTOs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GBUserGridRowDTO> getGBUserRowsForActivity(Lesson lesson, ToolActivity activity, Long groupId, int page, int size, String sortBy, String sortOrder,
	    String searchString) {
	Long lessonId = lesson.getLessonId();
	Long activityId = activity.getActivityId();

	List<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	List<User> learners = null;
	if (groupId != null) {
	    Group group = (Group) userService.findById(Group.class, groupId);
	    if (group != null) {
		learners = gradebookDAO.getUsersByGroup(lessonId, activityId, groupId, page, size, sortBy, sortOrder,
			searchString);
	    } else {
		learners = gradebookDAO.getUsersByActivity(lessonId, activityId, page, size, sortBy, sortOrder,
			searchString);
	    }
	} else {
	    learners = gradebookDAO.getUsersByActivity(lessonId, activityId, page, size, sortBy, sortOrder,
		    searchString);
	}

	if (learners != null) {
	    Map<Integer, LearnerProgress> userToLearnerProgressMap = getUserToLearnerProgressMap(lesson, learners);
	    Map<Integer, GradebookUserActivity> userToGradebookUserLessonMap = getUserToGradebookUserActivityMap(
		    activity, learners);
	    Long maxPossibleMark = toolService.getActivityMaxPossibleMark(activity);
	    
//		List<ToolSession> toolSessions = toolService.getToolSessionsByLesson(lesson);
//			// find required toolSession from toolSessions (thus we don't querying DB and hence increasing
//			// efficiency).
//			ToolSession toolSession = null;
//			for (ToolSession dbToolSession : toolSessions) {
//			    if (dbToolSession.getToolActivity().getActivityId().equals(activity.getActivityId())
//				    && dbToolSession.getLearners().contains(learner)) {
//				toolSession = dbToolSession;
//			    }
//			}

	    for (User learner : learners) {
		GBUserGridRowDTO gUserDTO = new GBUserGridRowDTO(learner);

		gUserDTO.setMarksAvailable(maxPossibleMark);

		// Set the progress
		LearnerProgress learnerProgress = userToLearnerProgressMap.get(learner.getUserId());
		gUserDTO.setStatus(getActivityStatusStr(learnerProgress, activity));
		gUserDTO.setTimeTaken(getActivityDuration(learnerProgress, activity));

		// Get the tool outputs for this user if there are any
		ToolSession toolSession = toolService.getToolSessionByLearner(learner, activity);
		if ((toolSession != null) && (learnerProgress != null)) {
		    // Set the activityLearner URL for this gradebook activity
		    byte activityState = learnerProgress.getProgressState(activity);
		    if ((activityState == LearnerProgress.ACTIVITY_ATTEMPTED)
			    || (activityState == LearnerProgress.ACTIVITY_COMPLETED)) {
			gUserDTO.setActivityUrl(Configuration.get(ConfigurationKeys.SERVER_URL)
				+ activity.getTool().getLearnerProgressUrl() + "&userID=" + learner.getUserId()
				+ "&toolSessionID=" + toolSession.getToolSessionId().toString());
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
    
    public ArrayList<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson) {
	return getGBUserRowsForLesson(lesson, 0, 0, null, null, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson, int page, int size, String sortBy, String sortOrder,
	    String searchString) {

	ArrayList<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	if (lesson != null) {
	    List<User> learners;
	    Map<Integer, LearnerProgress> userToLearnerProgressMap;
	    Map<Integer, GradebookUserLesson> userToGradebookUserLessonMap;
	    //size will be 0 in case of excel export 
	    if (size == 0) {
		learners =  new LinkedList<User>(lesson.getAllLearners());
		Collections.sort(learners, new UserComparator());
		
		userToLearnerProgressMap = getUserToLearnerProgressMap(lesson, null);
		userToGradebookUserLessonMap = getUserToGradebookUserLessonMap(lesson, null);
		
	    } else {
		learners = gradebookDAO.getUsersByLesson(lesson.getLessonId(), page, size, sortBy, sortOrder,
			searchString); 
		userToLearnerProgressMap = getUserToLearnerProgressMap(lesson, learners);
		userToGradebookUserLessonMap = getUserToGradebookUserLessonMap(lesson, learners);		
	    }

	    for (User learner : learners) {
		LearnerProgress learnerProgress = userToLearnerProgressMap.get(learner.getUserId());
		GBUserGridRowDTO gradebookUserDTO = new GBUserGridRowDTO(learner);
		gradebookUserDTOs.add(gradebookUserDTO);

		// Setting the status and time taken for the user's lesson
		gradebookUserDTO.setStatus(getLessonStatusStr(learnerProgress));

		// set current activity if available
		if ((learnerProgress != null) && (learnerProgress.getCurrentActivity() != null)) {
		    gradebookUserDTO.setCurrentActivity(learnerProgress.getCurrentActivity().getTitle());
		}

		// calculate time taken
		if (learnerProgress != null) {
		    if ((learnerProgress.getStartDate() != null) && (learnerProgress.getFinishDate() != null)) {
			gradebookUserDTO.setTimeTaken(learnerProgress.getFinishDate().getTime()
				- learnerProgress.getStartDate().getTime());
		    }
		}

		GradebookUserLesson gradebookUserLesson = userToGradebookUserLessonMap.get(learner.getUserId());
		if (gradebookUserLesson != null) {
		    gradebookUserDTO.setMark(gradebookUserLesson.getMark());
		    gradebookUserDTO.setFeedback(gradebookUserLesson.getFeedback());
		}

	    }
	}

	return gradebookUserDTOs;
    }
    
    @Override
    public int getCountUsersByLesson(Long lessonId, String searchString) {
	return gradebookDAO.getCountUsersByLesson(lessonId, searchString);
    }

    @Override
    public ArrayList<GBUserGridRowDTO> getGBUserRowsForOrganisation(Organisation organisation, int page, int size,
	    String sortOrder, String searchString) {

	ArrayList<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	if (organisation != null) {
	    List<User> learners = gradebookDAO.getUsersFromOrganisation(organisation.getOrganisationId(), page, size,
		    sortOrder, searchString);

	    if (learners != null) {

		for (User learner : learners) {

		    GBUserGridRowDTO gradebookUserDTO = new GBUserGridRowDTO(learner);
		    gradebookUserDTOs.add(gradebookUserDTO);
		}
	    }
	}

	return gradebookUserDTOs;

    }
    
    @Override
    public int getCountUsersByOrganisation(Integer orgId, String searchString) {
	return gradebookDAO.getCountUsersByOrganisation(orgId, searchString);
    }

    @Override
    public GradebookUserLesson getGradebookUserLesson(Long lessonID, Integer userID) {
	return gradebookDAO.getGradebookUserDataForLesson(lessonID, userID);
    }

    @Override
    public List<GradebookUserLesson> getGradebookUserLesson(Long lessonID) {
	return gradebookDAO.getGradebookUserDataForLesson(lessonID);
    }

    @Override
    public GradebookUserActivity getGradebookUserActivity(Long activityID, Integer userID) {
	return gradebookDAO.getGradebookUserDataForActivity(activityID, userID);
    }

    @Override
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

    @Override
    public Double getAverageMarkForLesson(Long lessonID) {
	return gradebookDAO.getAverageMarkForLesson(lessonID);
    }

    @Override
    public void updateUserLessonGradebookMark(Lesson lesson, User learner, Double mark) {
	if ((lesson != null) && (learner != null)) {
	    GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		    learner.getUserId());

	    if (gradebookUserLesson == null) {
		gradebookUserLesson = new GradebookUserLesson(lesson, learner);
	    }
	    String oldMark = (gradebookUserLesson.getMark() == null) ? "-" : gradebookUserLesson.getMark().toString();

	    gradebookUserLesson.setMark(mark);
	    gradebookDAO.insertOrUpdate(gradebookUserLesson);

	    // audit log changed gradebook mark
	    UserDTO monitorUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	    String[] args = new String[] { learner.getLogin() + "(" + learner.getUserId() + ")",
		    lesson.getLessonId().toString(), oldMark, mark.toString() };
	    String message = messageService.getMessage("audit.lesson.change.mark", args);
	    auditService.log(monitorUser, GradebookConstants.MODULE_NAME, message);
	}
    }
    
    @Override
    public void updateUserActivityGradebookMark(Lesson lesson, Activity activity, User learner) {
	ToolSession toolSession = toolService.getToolSessionByLearner(learner, activity);

	if ((toolSession == null) || (toolSession == null) || (learner == null) || (lesson == null)
		|| (activity == null) || !(activity instanceof ToolActivity)
		|| (((ToolActivity) activity).getActivityEvaluations() == null)
		|| ((ToolActivity) activity).getActivityEvaluations().isEmpty()) {
	    return;
	}
	ToolActivity toolActivity = (ToolActivity) activity;

	// Getting the first activity evaluation
	ActivityEvaluation eval = toolActivity.getActivityEvaluations().iterator().next();

	try {
	    ToolOutput toolOutput = toolService.getOutputFromTool(eval.getToolOutputDefinition(), toolSession,
		    learner.getUserId());

	    if (toolOutput != null) {
		ToolOutputValue outputVal = toolOutput.getValue();
		if (outputVal != null) {
		    Double outputDouble = outputVal.getDouble();

		    GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(toolActivity.getActivityId(),
			    learner.getUserId());

		    // Only set the mark if it hasnt previously been set by a teacher
		    if ((gradebookUserActivity == null) || !gradebookUserActivity.getMarkedInGradebook()) {
			updateUserActivityGradebookMark(lesson, learner, toolActivity, outputDouble, false, false);
		    }
		}
	    }

	} catch (ToolException e) {
	    GradebookService.logger.debug(
		    "Runtime exception when attempted to get outputs for activity: " + toolActivity.getActivityId(), e);
	}
    }

    @Override
    public void updateUserActivityGradebookMark(Lesson lesson, User learner, Activity activity, Double mark,
	    Boolean markedInGradebook, boolean isAuditLogRequired) {
	if ((lesson != null) && (activity != null) && (learner != null) && activity.isToolActivity()) {

	    // First, update the mark for the activity
	    GradebookUserActivity gradebookUserActivity = gradebookDAO.getGradebookUserDataForActivity(
		    activity.getActivityId(), learner.getUserId());

	    if (gradebookUserActivity == null) {
		gradebookUserActivity = new GradebookUserActivity((ToolActivity) activity, learner);
	    }
	    String oldMark = (gradebookUserActivity.getMark() == null) ? "-" : gradebookUserActivity.getMark()
		    .toString();

	    gradebookUserActivity.setMark(mark);
	    gradebookUserActivity.setUpdateDate(new Date());
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

	    // In order to calculate lesson's mark correctly we need to flush the session beforehand. This is required
	    // as change to gradebookUserActivity isn't synchronized with the DB at this point due to the current transaction isn't
	    // committed yet
	    gradebookDAO.flush();

	    // Calculates a lesson's total mark and saves it
	    Double totalMark = gradebookDAO.getGradebookUserActivityMarkSum(gradebookUserLesson.getLesson()
		    .getLessonId(), gradebookUserLesson.getLearner().getUserId());
	    gradebookUserLesson.setMark(totalMark);
	    gradebookDAO.insertOrUpdate(gradebookUserLesson);

	    // audit log changed gradebook mark
	    if (isAuditLogRequired) {
		UserDTO monitorUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
		String[] args = new String[] { learner.getLogin() + "(" + learner.getUserId() + ")",
			activity.getActivityId().toString(), lesson.getLessonId().toString(), oldMark.toString(),
			mark.toString() };
		String message = messageService.getMessage("audit.activity.change.mark", args);
		auditService.log(monitorUser, GradebookConstants.MODULE_NAME, message);
	    }
	}
    }

    @Override
    public void updateUserLessonGradebookFeedback(Lesson lesson, User learner, String feedback) {

	GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		learner.getUserId());

	if (gradebookUserLesson == null) {
	    gradebookUserLesson = new GradebookUserLesson(lesson, learner);
	}

	gradebookUserLesson.setFeedback(feedback);
	gradebookDAO.insertOrUpdate(gradebookUserLesson);
    }

    @Override
    public void updateUserActivityGradebookFeedback(Activity activity, User learner, String feedback) {

	GradebookUserActivity gradebookUserActivity = gradebookDAO.getGradebookUserDataForActivity(
		activity.getActivityId(), learner.getUserId());

	if (gradebookUserActivity == null) {
	    gradebookUserActivity = new GradebookUserActivity((ToolActivity) activity, learner);
	}

	gradebookUserActivity.setFeedback(feedback);
	gradebookUserActivity.setUpdateDate(new Date());
	gradebookDAO.insertOrUpdate(gradebookUserActivity);
    }

    @Override
    public void toggleMarksReleased(Long lessonId) {

	Lesson lesson = lessonService.getLesson(lessonId);

	boolean isMarksReleased = (lesson.getMarksReleased() != null) && lesson.getMarksReleased();
	lesson.setMarksReleased(!isMarksReleased);
	userService.save(lesson);

	// audit log marks released
	UserDTO monitor = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	String messageKey = (isMarksReleased) ? "audit.marks.released.off" : "audit.marks.released.on";
	String message = messageService.getMessage(messageKey, new String[] { lessonId.toString() });
	auditService.log(monitor, GradebookConstants.MODULE_NAME, message);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GBLessonGridRowDTO> getGBLessonRows(Organisation organisation, User user, User viewer, GBGridView view,
	    int page, int size, String sortBy, String sortOrder, String searchString) {
	List<GBLessonGridRowDTO> lessonRows = new ArrayList<GBLessonGridRowDTO>();
	Integer userId = user.getUserId();
	Integer orgId = organisation.getOrganisationId();

	if (organisation != null) {

	    List<Lesson> lessons = view == GBGridView.MON_COURSE ? gradebookDAO
		    .getLessonsByGroupAndUser(userId, orgId, page, size, sortBy, sortOrder, searchString)
		    : lessonService.getLessonsByGroupAndUser(userId, orgId);
		
	    if (lessons != null) {

		for (Lesson lesson : lessons) {

		    // For My Grades gradebook page: don't include lesson in list if the user doesn't have permission.
		    if (view == GBGridView.LRN_COURSE) {
			boolean marksReleased = (lesson.getMarksReleased() != null) && lesson.getMarksReleased();
			boolean hasLearnerPermission = lesson.getAllLearners().contains(user);
			if (!hasLearnerPermission || !marksReleased) {
			    continue;
			}
		    }
		    
		    GBLessonGridRowDTO lessonRow = new GBLessonGridRowDTO();
		    lessonRows.add(lessonRow);
		    lessonRow.setLessonName(lesson.getLessonName());
		    lessonRow.setId(lesson.getLessonId().toString());

		    if (view == GBGridView.LIST) {
			continue;
			
		    } else if (view == GBGridView.MON_COURSE) {

			// Setting the averages for monitor view
			lessonRow.setAverageTimeTaken(gradebookDAO.getAverageDurationLesson(lesson.getLessonId()));
			lessonRow.setAverageMark(gradebookDAO.getAverageMarkForLesson(lesson.getLessonId()));

			// Set the gradebook monitor url
			String gbMonURL = Configuration.get(ConfigurationKeys.SERVER_URL)
				+ "gradebook/gradebookMonitoring.do?lessonID=" + lesson.getLessonId().toString();
			lessonRow.setGradebookMonitorURL(gbMonURL);
		    } else if ((view == GBGridView.LRN_COURSE) || (view == GBGridView.MON_USER)) {

			GradebookUserLesson gbLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
				userId);

			lessonRow.setAverageTimeTaken(gradebookDAO.getAverageDurationLesson(lesson.getLessonId()));
			lessonRow.setAverageMark(gradebookDAO.getAverageMarkForLesson(lesson.getLessonId()));

			if (gbLesson != null) {
			    lessonRow.setMark(gbLesson.getMark());
			    lessonRow.setFeedback(gbLesson.getFeedback());
			}

			LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(userId,
				lesson.getLessonId());
			lessonRow.setStatus(getLessonStatusStr(learnerProgress));
			if (learnerProgress != null) {
			    if ((learnerProgress.getStartDate() != null) && (learnerProgress.getFinishDate() != null)) {
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

		    lessonRow.setStartDate(getLocaleDateString(viewer, lesson.getStartDateTime()));

		}
	    }

	} else {
	    GradebookService.logger.error("Request for gradebook grid with a null organisation");
	}

	return lessonRows;
    }

    private HashMap<ToolActivity, List<GBUserGridRowDTO>> getDataForLessonGradebookExport(Lesson lesson) {

	HashMap<ToolActivity, List<GBUserGridRowDTO>> activityToUserDTOMap = new HashMap<ToolActivity, List<GBUserGridRowDTO>>();

	Set<User> learners = new TreeSet<User>(new UserComparator());
	if (lesson.getAllLearners() != null) {
	    learners.addAll(lesson.getAllLearners());
	}
	
	Map<Integer, LearnerProgress> userToLearnerProgressMap = getUserToLearnerProgressMap(lesson, null);
	Set<ToolActivity> activities = getLessonActivities(lesson);

	for (ToolActivity activity : activities) {

	    Map<Integer, GradebookUserActivity> userToGradebookUserActivityMap = getUserToGradebookUserActivityMap(activity, null);

	    List<GBUserGridRowDTO> userDTOs = new ArrayList<GBUserGridRowDTO>();

	    for (User learner : learners) {
		GBUserGridRowDTO userDTO = new GBUserGridRowDTO(learner);

		// Set the progress
		LearnerProgress learnerProgress = userToLearnerProgressMap.get(learner.getUserId());
		userDTO.setTimeTaken(getActivityDuration(learnerProgress, activity));
		userDTO.setStartDate(getActivityStartDate(learnerProgress, activity, null));
		userDTO.setFinishDate(getActivityFinishDate(learnerProgress, activity, null));

		// Add marks and feedback
		GradebookUserActivity gradebookUserActivity = userToGradebookUserActivityMap.get(learner.getUserId());
		if (gradebookUserActivity != null) {
		    userDTO.setMark(gradebookUserActivity.getMark());
		}
		userDTOs.add(userDTO);
	    }
	    activityToUserDTOMap.put(activity, userDTOs);
	}

	return activityToUserDTOMap;
    }

    @Override
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
	rowList.add(GradebookService.EMPTY_ROW);

	// Adding the activity average data to the summary
	List<GradebookGridRowDTO> activityRows = getGBActivityRowsForLesson(lesson.getLessonId());
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
	rowList.add(GradebookService.EMPTY_ROW);

	// Adding the user lesson marks to the summary
	ExcelCell[] userMarksTitle = new ExcelCell[1];
	userMarksTitle[0] = new ExcelCell(getMessage("gradebook.export.total.marks.for.lesson"), true);
	rowList.add(userMarksTitle);

	// Fetching the user data
	ArrayList<GBUserGridRowDTO> userRows = getGBUserRowsForLesson(lesson);

	// Setting up the user marks table
	ExcelCell[] userTitleRow = new ExcelCell[6];
	userTitleRow[0] = new ExcelCell(getMessage("gradebook.export.last.name"), true);
	userTitleRow[1] = new ExcelCell(getMessage("gradebook.export.first.name"), true);
	userTitleRow[2] = new ExcelCell(getMessage("gradebook.export.login"), true);
	userTitleRow[3] = new ExcelCell(getMessage("gradebook.exportcourse.progress"), true);
	userTitleRow[4] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
	userTitleRow[5] = new ExcelCell(getMessage("gradebook.export.total.mark"), true);
	rowList.add(userTitleRow);

	for (GBUserGridRowDTO userRow : userRows) {
	    // Adding the user data for the lesson
	    ExcelCell[] userDataRow = new ExcelCell[6];
	    userDataRow[0] = new ExcelCell(userRow.getLastName(), false);
	    userDataRow[1] = new ExcelCell(userRow.getFirstName(), false);
	    userDataRow[2] = new ExcelCell(userRow.getLogin(), false);
	    userDataRow[3] = new ExcelCell(getProgressMessage(userRow), false);
	    userDataRow[4] = new ExcelCell(userRow.getTimeTakenSeconds(), false);
	    userDataRow[5] = new ExcelCell(userRow.getMark(), false);
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
	    titleRow[2] = new ExcelCell(getMessage("gradebook.export.login"), true);
	    titleRow[3] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
	    titleRow[4] = new ExcelCell(getMessage("gradebook.columntitle.mark"), true);
	    rowList1.add(titleRow);

	    // Get the rest of the data
	    List<GBUserGridRowDTO> userDtos = activityToUserDTOMap.get(activity);
	    for (GBUserGridRowDTO userDto : userDtos) {
		ExcelCell[] userDataRow = new ExcelCell[5];

		userDataRow[0] = new ExcelCell(userDto.getLastName(), false);
		userDataRow[1] = new ExcelCell(userDto.getFirstName(), false);
		userDataRow[2] = new ExcelCell(userDto.getLogin(), false);
		userDataRow[3] = new ExcelCell(userDto.getTimeTakenSeconds(), false);
		userDataRow[4] = new ExcelCell(userDto.getMark(), false);
		rowList1.add(userDataRow);
	    }

	    rowList1.add(GradebookService.EMPTY_ROW);
	}

	ExcelCell[][] activityData = rowList1.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("gradebook.gridtitle.activitygrid"), activityData);

	// -------------------- process Learner View page --------------------------------

	Set<User> learners = new TreeSet<User>(new UserComparator());
	if (lesson.getAllLearners() != null) {
	    learners.addAll(lesson.getAllLearners());
	}
	
	SimpleDateFormat cellDateFormat = new SimpleDateFormat(FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT);

	rowList = new LinkedList<ExcelCell[]>();
	for (User learner : learners) {

	    userTitleRow = new ExcelCell[4];
	    userTitleRow[0] = new ExcelCell(learner.getFullName() + " (" + learner.getLogin() + ")", true);
	    rowList.add(userTitleRow);

	    ExcelCell[] titleRow = new ExcelCell[5];
	    titleRow[0] = new ExcelCell(getMessage("gradebook.export.activity"), true);
	    titleRow[1] = new ExcelCell(getMessage("gradebook.columntitle.startDate"), true);
	    titleRow[2] = new ExcelCell(getMessage("gradebook.columntitle.completeDate"), true);
	    titleRow[3] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
	    titleRow[4] = new ExcelCell(getMessage("gradebook.columntitle.mark"), true);
	    rowList.add(titleRow);

	    for (ToolActivity activity : activityToUserDTOMap.keySet()) {

		//find userDto corresponding to the user 
		List<GBUserGridRowDTO> userDtos = activityToUserDTOMap.get(activity);
		GBUserGridRowDTO userDto = null;
		for (GBUserGridRowDTO dbUserDTO : userDtos) {
		    if (dbUserDTO.getId().equals(learner.getUserId().toString())) {
			userDto = dbUserDTO;
		    }
		}
		
		//construct activityRowName
		String groupName = null;
		Long groupId = null;
		if (activity.getGrouping() != null) {
		    Group group = activity.getGroupFor(learner);
		    if (group != null) {
			groupName = group.getGroupName();
			groupId = group.getGroupId();
		    }
		}
		String activityRowName = (groupName != null && groupId != null) ? StringEscapeUtils.escapeHtml(activity
			.getTitle()) + " (" + groupName + ")" : StringEscapeUtils.escapeHtml(activity.getTitle());
		
		String startDate = (userDto.getStartDate() == null) ? "" : cellDateFormat.format(userDto.getStartDate());
		String finishDate = (userDto.getFinishDate() == null) ? "" : cellDateFormat.format(userDto.getFinishDate());
		
		ExcelCell[] activityDataRow = new ExcelCell[5];
		activityDataRow[0] = new ExcelCell(activityRowName, false);
		activityDataRow[1] = new ExcelCell(startDate, false);
		activityDataRow[2] = new ExcelCell(finishDate, false);
		activityDataRow[3] = new ExcelCell(userDto.getTimeTakenSeconds(), false);
		activityDataRow[4] = new ExcelCell(userDto.getMark(), false);
		rowList.add(activityDataRow);
	    }

	    rowList.add(GradebookService.EMPTY_ROW);
	}

	ExcelCell[][] userData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("gradebook.export.learner.view"), userData);

	return dataToExport;
    }

    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, ExcelCell[][]> exportCourseGradebook(Integer userId, Integer organisationId) {
	SimpleDateFormat cellDateFormat = new SimpleDateFormat(FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT);
	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();

	// The entire data list
	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();

	Set<Lesson> lessons = new TreeSet<Lesson>(new LessonComparator());
	lessons.addAll(lessonService.getLessonsByGroupAndUser(userId, organisationId));

	if ((lessons != null) && (lessons.size() > 0)) {

	    int numberOfCellsInARow = 3 + (lessons.size() * 6);

	    // Adding the user lesson marks to the summary----------------------
	    ExcelCell[] lessonsNames = new ExcelCell[numberOfCellsInARow];
	    int i = 0;
	    lessonsNames[i++] = new ExcelCell("", false);
	    lessonsNames[i++] = new ExcelCell("", false);
	    lessonsNames[i++] = new ExcelCell("", false);
	    for (Lesson lesson : lessons) {
		lessonsNames[i++] = new ExcelCell(messageService.getMessage("gradebook.exportcourse.lesson",
			new Object[] { lesson.getLessonName() }), true);
		lessonsNames[i++] = new ExcelCell("", false);
		lessonsNames[i++] = new ExcelCell("", false);
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
	    headerRow[i++] = new ExcelCell(getMessage("gradebook.export.login"), true);
	    for (Lesson lesson : lessons) {
		headerRow[i++] = new ExcelCell(getMessage("gradebook.exportcourse.progress"), true);
		headerRow[i++] = new ExcelCell(getMessage("gradebook.columntitle.startDate"), false);
		headerRow[i++] = new ExcelCell(getMessage("gradebook.columntitle.completeDate"), false);
		headerRow[i++] = new ExcelCell(getMessage("gradebook.export.time.taken.seconds"), true);
		headerRow[i++] = new ExcelCell(getMessage("gradebook.exportcourse.lessonFeedback"), true);
		headerRow[i++] = new ExcelCell(getMessage("gradebook.export.total.mark"), true);
	    }
	    rowList.add(headerRow);

	    // collect users from all lessons
	    LinkedHashSet<User> allLearners = new LinkedHashSet<User>();
	    List<Long> lessonIds = new LinkedList<Long>();
	    for (Lesson lesson : lessons) {
		Set dbLessonUsers = lesson.getAllLearners();
		allLearners.addAll(dbLessonUsers);
		lessonIds.add(lesson.getLessonId());
	    }

	    // Fetching the user data
	    List<LearnerProgress> learnerProgresses = new LinkedList<LearnerProgress>();
	    List<GradebookUserLesson> gradebookUserLessons = new LinkedList<GradebookUserLesson>();
	    if (!allLearners.isEmpty()) {
		learnerProgresses = learnerProgressDAO.getLearnerProgressForLessons(lessonIds);
		gradebookUserLessons = gradebookDAO.getGradebookUserLessons(lessonIds);
	    }
	    
	    //sort users by last name
	    TreeSet<User> sortedLearners = new TreeSet<User>(new UserComparator());
	    sortedLearners.addAll(allLearners);

	    for (User learner : sortedLearners) {
		i = 0;
		ExcelCell[] userDataRow = new ExcelCell[numberOfCellsInARow];
		userDataRow[i++] = new ExcelCell(learner.getLastName(), false);
		userDataRow[i++] = new ExcelCell(learner.getFirstName(), false);
		userDataRow[i++] = new ExcelCell(learner.getLogin(), false);

		for (Lesson lesson : lessons) {
		    GBUserGridRowDTO userDto = new GBUserGridRowDTO(learner);
		    String startDate = "";
		    String finishDate = "";
		    Long timeTakenSeconds = null;
		    Double mark = null;
		    String feedback = "";

		    // check if learner is participating in this lesson
		    if (lesson.getAllLearners().contains(learner)) {
			
			//find according learnerProgress
			LearnerProgress learnerProgress = null;
			for (LearnerProgress learnerProgressIter : learnerProgresses) {
			    if (learnerProgressIter.getUser().getUserId().equals(learner.getUserId())
				    && learnerProgressIter.getLesson().getLessonId().equals(lesson.getLessonId())) {
				learnerProgress = learnerProgressIter;
			    }
			}
			
			// status for the user's lesson
			userDto.setStatus(getLessonStatusStr(learnerProgress));
			if ((learnerProgress != null) && (learnerProgress.getCurrentActivity() != null)) {
			    userDto.setCurrentActivity(learnerProgress.getCurrentActivity().getTitle());
			}
			
			// start date
			if ((learnerProgress != null) && (learnerProgress.getStartDate() != null)) {
			    startDate = cellDateFormat.format(learnerProgress.getStartDate());
			}

			// finish date
			if ((learnerProgress != null) && (learnerProgress.getFinishDate() != null)) {
			    finishDate = cellDateFormat.format(learnerProgress.getFinishDate());
			}

			// calculate time taken
			if (learnerProgress != null) {
			    if ((learnerProgress.getStartDate() != null) && (learnerProgress.getFinishDate() != null)) {
				timeTakenSeconds = learnerProgress.getFinishDate().getTime()
					- learnerProgress.getStartDate().getTime();
			    }
			}
			
			//find according learnerProgress
			GradebookUserLesson gradebookUserLesson = null;
			for (GradebookUserLesson gradebookUserLessonIter : gradebookUserLessons) {
			    if (gradebookUserLessonIter.getLearner().getUserId().equals(learner.getUserId())
				    && gradebookUserLessonIter.getLesson().getLessonId().equals(lesson.getLessonId())) {
				gradebookUserLesson = gradebookUserLessonIter;
			    }
			}

			if (gradebookUserLesson != null) {
			    mark = gradebookUserLesson.getMark();
			    feedback = gradebookUserLesson.getFeedback();
			}
		    } else {
			userDto.setStatus("n/a");
		    }
		    
		    //all of GBUserGridRowDTOs will be displayed on 1 line on course export.
		    userDataRow[i++] = new ExcelCell(getProgressMessage(userDto), false);
		    userDataRow[i++] = new ExcelCell(startDate, false);
		    userDataRow[i++] = new ExcelCell(finishDate, false);
		    userDataRow[i++] = new ExcelCell(timeTakenSeconds, false);
		    userDataRow[i++] = new ExcelCell(feedback, false);
		    userDataRow[i++] = new ExcelCell(mark, false);
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
	SimpleDateFormat cellDateFormat = new SimpleDateFormat(FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT);
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
	    
	    // Fetching the user data
	    List<Long> lessonIdLongs = new LinkedList<Long>();
	    for (String lessonId : lessonIds) {
		lessonIdLongs.add(Long.valueOf(lessonId));
	    }
	    List<LearnerProgress> learnerProgresses = new LinkedList<LearnerProgress>();
	    if (!allLearners.isEmpty()) {
		learnerProgresses = learnerProgressDAO.getLearnerProgressForLessons(lessonIdLongs);
	    }

	    Map<Long, Long> activityToTotalMarkMap = getActivityToTotalMarkMap(allActivities);

	    Map<Long, Map<Integer, GradebookUserActivity>> activityTouserToGradebookUserActivityMap = new HashMap<Long, Map<Integer, GradebookUserActivity>>();
	    for (ToolActivity activity : allActivities) {
		Map<Integer, GradebookUserActivity> userToGradebookUserActivityMap = getUserToGradebookUserActivityMap(activity, null);
		activityTouserToGradebookUserActivityMap.put(activity.getActivityId(), userToGradebookUserActivityMap);
	    }

	    int numberCellsPerRow = (selectedLessons.size() * 9) + (allActivities.size() * 2) + 5;

	    // Lesson names row----------------------
	    ExcelCell[] lessonsNames = new ExcelCell[numberCellsPerRow];
	    int i = 4;
	    for (Lesson lesson : selectedLessons) {
		Set<ToolActivity> lessonActivities = lessonActivitiesMap.get(lesson.getLessonId());
		int numberActivities = lessonActivities.size();
		lessonsNames[i + numberActivities] = new ExcelCell(lesson.getLessonName(), true);
		i += 9 + (numberActivities * 2);
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
		headerRow[i++] = new ExcelCell(getMessage("gradebook.export.login"), false);
		headerRow[i++] = new ExcelCell(getMessage("label.group"), false);
		headerRow[i++] = new ExcelCell(getMessage("gradebook.columntitle.startDate"), false);
		headerRow[i++] = new ExcelCell(getMessage("gradebook.columntitle.completeDate"), false);

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

		    //first, last names and login
		    String lastName = (learner.getLastName() == null) ? "" : learner.getLastName().toUpperCase();
		    userRow[i++] = new ExcelCell(lastName, false);
		    String firstName = (learner.getFirstName() == null) ? "" : learner.getFirstName().toUpperCase();
		    userRow[i++] = new ExcelCell(firstName, false);
		    userRow[i++] = new ExcelCell(learner.getLogin(), false);

		    // check if learner is participating in this lesson
		    if (!lesson.getAllLearners().contains(learner)) {
			i += 1 + (activities.size() * 2);
			userRow[i++] = new ExcelCell("", ExcelCell.BORDER_STYLE_LEFT_THIN);
			userRow[i++] = new ExcelCell("", false);
			userRow[i++] = new ExcelCell("", ExcelCell.BORDER_STYLE_RIGHT_THICK);
			continue;
		    }

		    // group name
		    String groupName = "";
		    for (Group group : (Set<Group>) lesson.getLessonClass().getGroups()) {
			if (group.hasLearner(learner)) {
			    groupName = group.getGroupName();
			    break;
			}
		    }
		    userRow[i++] = new ExcelCell(groupName, false);
		    
		    //start and complete dates
		    LearnerProgress learnerProgress = null;
		    for (LearnerProgress learnerProgressIter : learnerProgresses) {
			if (learnerProgressIter.getUser().getUserId().equals(learner.getUserId())
				&& learnerProgressIter.getLesson().getLessonId().equals(lesson.getLessonId())) {
			    learnerProgress = learnerProgressIter;
			}
		    }
		    String startDate = (learnerProgress == null || learnerProgress.getStartDate() == null) ? ""
			    : cellDateFormat.format(learnerProgress.getStartDate());
		    userRow[i++] = new ExcelCell(startDate, false);
		    String finishDate = (learnerProgress == null || learnerProgress.getFinishDate() == null) ? ""
			    : cellDateFormat.format(learnerProgress.getFinishDate());
		    userRow[i++] = new ExcelCell(finishDate, false);

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

    @Override
    public void updateActivityMark(Double mark, String feedback, Integer userID, Long toolSessionID,
	    Boolean markedInGradebook) {
	ToolSession toolSession = toolService.getToolSessionById(toolSessionID);
	User learner = (User) userService.findById(User.class, userID);
	if ((learner != null) && (toolSession != null)) {
	    ToolActivity activity = toolSession.getToolActivity();
	    GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(activity.getActivityId(), userID);

	    // If gradebook user activity is null or the mark is set by teacher or was set previously by user - save the
	    // mark and feedback
	    if ((gradebookUserActivity == null) || markedInGradebook || !gradebookUserActivity.getMarkedInGradebook()) {
		updateUserActivityGradebookMark(toolSession.getLesson(), learner, activity, mark, markedInGradebook,
			false);
		updateUserActivityGradebookFeedback(activity, learner, feedback);
	    }
	}
    }

    @Override
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
     * Gets the internationalised date
     * 
     * @param user
     * @param date
     * @return
     */
    private String getLocaleDateString(User user, Date date) {
	if ((user == null) || (date == null)) {
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
	if ((groupName != null) && (groupId != null)) {

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
	activityDTO.setMarksAvailable(toolService.getActivityMaxPossibleMark(activity));

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
		GradebookService.logger.warn("No user time zone provided, leaving server default");
	    } else {
		if (GradebookService.logger.isTraceEnabled()) {
		    GradebookService.logger.trace("Adjusting time according to zone \"" + timeZone + "\"");
		}
		TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);
		startDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, startDate);
	    }
	}
	return startDate;
    }
    
    /**
     * Gets completed activity finish time ajusted to monitor time zone.
     * 
     * @param learnerProgress
     * @param activity
     * @param timeZone
     * @return
     */
    private Date getActivityFinishDate(LearnerProgress learnerProgress, Activity activity, String timeZone) {
	Date finishDate = null;
	if (learnerProgress != null) {
	    CompletedActivityProgress compProg = learnerProgress.getCompletedActivities().get(activity);
	    if (compProg != null) {
		finishDate = compProg.getFinishDate();
	    }
	}

	if (finishDate != null) {
	    if (StringUtils.isBlank(timeZone)) {
		GradebookService.logger.warn("No user time zone provided, leaving server default");
	    } else {
		if (GradebookService.logger.isTraceEnabled()) {
		    GradebookService.logger.trace("Adjusting time according to zone \"" + timeZone + "\"");
		}
		TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);
		finishDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, finishDate);
	    }
	}
	return finishDate;
    }

    private Long getActivityDuration(LearnerProgress learnerProgress, Activity activity) {
	if (learnerProgress != null) {
	    if (learnerProgress.getCompletedActivities().get(activity) != null) {
		CompletedActivityProgress compProg = learnerProgress.getCompletedActivities().get(activity);
		if (compProg != null) {
		    Date startTime = compProg.getStartDate();
		    Date endTime = compProg.getFinishDate();
		    if ((startTime != null) && (endTime != null)) {
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
		
	    } else if ((learnerProgress.getAttemptedActivities() != null)
		    && (learnerProgress.getAttemptedActivities().size() > 0)) {
		
		String currentActivityTitle = learnerProgress.getCurrentActivity() == null ? "" : StringEscapeUtils
			.escapeHtml(learnerProgress.getCurrentActivity().getTitle());
		status = "<img src='" + IMAGES_DIR + "/cog.png' title='" + currentActivityTitle + "' />";
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
	    if (statusByte == LearnerProgress.ACTIVITY_ATTEMPTED && learnerProgress.getCurrentActivity() != null) {
		return "<img src='" + IMAGES_DIR + "/cog.png' title='"
			+ StringEscapeUtils.escapeHtml(learnerProgress.getCurrentActivity().getTitle()) + "' />";
	    } else if (statusByte == LearnerProgress.ACTIVITY_COMPLETED) {
		return "<img src='" + IMAGES_DIR + "/tick.png' />";
	    }
	}
	return "-";
    }

    /**
     * Returns map containing (userId -> LearnerProgressMap) pairs. It serves merely for optimizing amount of db
     * queries.
     * 
     * @param learners if null - return all available pairs for the lesson
     */
    private Map<Integer, LearnerProgress> getUserToLearnerProgressMap(Lesson lesson, List<User> learners) {

	Map<Integer, LearnerProgress> map = new HashMap<Integer, LearnerProgress>();
	if (lesson == null || learners != null && learners.isEmpty()) {
	    return map;
	}
	
	//get either all available learnerProgresses or only for specified users
	List<LearnerProgress> learnerProgresses;
	if (learners == null) {
	    learnerProgresses = lessonService.getUserProgressForLesson(lesson.getLessonId());
	} else {
	    
	    List<Integer> userIds = new LinkedList<Integer>();
	    for (User learner : learners) {
		userIds.add(learner.getUserId());
	    }
	    
	    learnerProgresses = learnerProgressDAO.getLearnerProgressForLesson(lesson.getLessonId(), userIds);
	}
	
	if (learnerProgresses != null) {
	    for (LearnerProgress learnerProgress : learnerProgresses) {
		map.put(learnerProgress.getUser().getUserId(), learnerProgress);
	    }
	}
	
	return map;
    }

    /**
     * Returns map containing (userId -> GradebookUserActivity) pairs. It serves merely for optimizing amount of db
     * queries.
     */
    private Map<Integer, GradebookUserActivity> getUserToGradebookUserActivityMap(Activity activity, List<User> learners) {

	Map<Integer, GradebookUserActivity> map = new HashMap<Integer, GradebookUserActivity>();
	if (activity == null || learners != null && learners.isEmpty()) {
	    return map;
	}
	
	//get either all available learnerProgresses or only for specified users
	List<GradebookUserActivity> gradebookUserActivities;
	if (learners == null) {
	    gradebookUserActivities = gradebookDAO
		    .getAllGradebookUserActivitiesForActivity(activity.getActivityId());
	} else {
	    
	    List<Integer> userIds = new LinkedList<Integer>();
	    for (User learner : learners) {
		userIds.add(learner.getUserId());
	    }
	    
	    gradebookUserActivities = gradebookDAO.getGradebookUserActivitiesForActivity(activity.getActivityId(), userIds);
	}
	
	if (gradebookUserActivities != null) {
	    for (GradebookUserActivity gradebookUserActivity : gradebookUserActivities) {
		map.put(gradebookUserActivity.getLearner().getUserId(), gradebookUserActivity);
	    }
	}

	return map;
    }

    /**
     * Returns map containing (activityId -> TotalMarksAvailable) pairs. It serves merely for optimizing amount of db
     * queries.
     */
    private Map<Long, Long> getActivityToTotalMarkMap(Collection<ToolActivity> activities) {

	Map<Long, Long> map = new HashMap<Long, Long>();
	for (ToolActivity activity : activities) {
	    map.put(activity.getActivityId(), toolService.getActivityMaxPossibleMark(activity));
	}

	return map;
    }
    
    /**
     * Returns map containing (userId -> GradebookUserLesson) pairs. It serves merely for optimizing amount of db
     * queries.
     * 
     * @param userIds if provided - return userLessons only for those users
     */
    private Map<Integer, GradebookUserLesson> getUserToGradebookUserLessonMap(Lesson lesson, List<User> learners) {

	Map<Integer, GradebookUserLesson> map = new HashMap<Integer, GradebookUserLesson>();
	if (lesson == null || learners != null && learners.isEmpty()) {
	    return map;
	}
	
	//get either all available gradebookUserLessons or only for specified users
	List<GradebookUserLesson> gradebookUserLessons;
	if (learners == null) {
	    gradebookUserLessons = gradebookDAO.getGradebookUserLessons(lesson);
	} else {
	    
	    List<Integer> userIds = new LinkedList<Integer>();
	    for (User learner : learners) {
		userIds.add(learner.getUserId());
	    }
	    
	    gradebookUserLessons = gradebookDAO.getGradebookUserLessons(lesson, userIds);
	}
	
	if (gradebookUserLessons != null) {
	    for (GradebookUserLesson gradebookUserLesson : gradebookUserLessons) {
		map.put(gradebookUserLesson.getLearner().getUserId(), gradebookUserLesson);
	    }
	}

	return map;
    }

    @Override
    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    private String getMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    // Getter and setter methods -----------------------------------------------

    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

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
    
    public void setLearnerProgressDAO(ILearnerProgressDAO learnerProgressDAO) {
	this.learnerProgressDAO = learnerProgressDAO;
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
