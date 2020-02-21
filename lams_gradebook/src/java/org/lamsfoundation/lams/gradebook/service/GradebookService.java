/****************************************************************
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

package org.lamsfoundation.lams.gradebook.service;

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
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradebookDAO;
import org.lamsfoundation.lams.gradebook.dto.GBActivityArchiveGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBActivityGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBLessonGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;
import org.lamsfoundation.lams.gradebook.model.GradebookUserActivityArchive;
import org.lamsfoundation.lams.gradebook.model.GradebookUserLessonArchive;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.LessonComparator;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.FloatingActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.CompletedActivityProgressArchive;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.LearnerProgressArchive;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.OutcomeResult;
import org.lamsfoundation.lams.outcome.OutcomeScaleItem;
import org.lamsfoundation.lams.outcome.service.IOutcomeService;
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
import org.lamsfoundation.lams.usermanagement.util.LastNameAlphabeticComparator;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelCell;
import org.lamsfoundation.lams.util.excel.ExcelRow;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * This service handles all gradebook-related service calls
 *
 * @author lfoxton
 */
public class GradebookService implements IGradebookFullService {
    private static Logger logger = Logger.getLogger(GradebookService.class);

    private static final String TOOL_SIGNATURE_ASSESSMENT = "laasse10";
    public static final String TOOL_SIGNATURE_SCRATCHIE = "lascrt11";

    // Services
    private ILamsCoreToolService toolService;
    private IGradebookDAO gradebookDAO;
    private ILearnerProgressDAO learnerProgressDAO;
    private ILessonDAO lessonDAO;
    private ILessonService lessonService;
    private IUserManagementService userService;
    private IActivityDAO activityDAO;
    private MessageService messageService;
    private ILogEventService logEventService;
    private static ILearnerService learnerService;
    private IIntegrationService integrationService;
    private IOutcomeService outcomeService;

    @Override
    public List<GradebookGridRowDTO> getGBActivityRowsForLearner(Long lessonId, Integer userId, TimeZone userTimezone) {
	logger.debug("Getting gradebook user data for lesson: " + lessonId + ". For user: " + userId);

	Lesson lesson = lessonService.getLesson(lessonId);
	User learner = (User) userService.findById(User.class, userId);

	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<>();

	List<ToolActivity> activities = getLessonActivitiesForLearner(lesson, userId);
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

	    GradebookUserActivity gradebookActivity = gradebookDAO
		    .getGradebookUserDataForActivity(activity.getActivityId(), learner.getUserId());
	    if (gradebookActivity != null) {
		activityDTO.setMark(gradebookActivity.getMark());
		activityDTO.setFeedback(gradebookActivity.getFeedback());
	    }

	    LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(learner.getUserId(),
		    lesson.getLessonId());
	    // Setting status
	    activityDTO.setStartDate(getActivityStartDate(learnerProgress, activity, userTimezone));
	    activityDTO.setFinishDate(getActivityFinishDate(learnerProgress, activity, userTimezone));
	    activityDTO.setTimeTaken(getActivityDuration(learnerProgress, activity));
	    activityDTO.setStatus(getActivityStatusStr(learnerProgress, activity));

	    // Setting averages
	    activityDTO.setAverageMark(gradebookDAO.getAverageMarkForActivity(activity.getActivityId()));
	    activityDTO.setMedianTimeTaken(gradebookDAO.getMedianTimeTakenForActivity(activity.getActivityId()));
	    activityDTO.setMaxTimeTaken(gradebookDAO.getMaxTimeTakenForActivity(activity.getActivityId()));
	    activityDTO.setMinTimeTaken(gradebookDAO.getMinTimeTakenForActivity(activity.getActivityId()));

	    // Get the tool outputs for this user if there are any
	    ToolSession toolSession = toolService.getToolSessionByLearner(learner, activity);
	    if ((toolSession != null) && (learnerProgress != null)) {
		byte activityState = learnerProgress.getProgressState(activity);
		if ((activityState == LearnerProgress.ACTIVITY_ATTEMPTED)
			|| (activityState == LearnerProgress.ACTIVITY_COMPLETED)) {
		    // Set the activityLearner URL for this gradebook activity
		    String activityUrl = Configuration.get(ConfigurationKeys.SERVER_URL)
			    + activity.getTool().getLearnerProgressUrl();
		    activityUrl = WebUtil.appendParameterToURL(activityUrl, "userID", learner.getUserId().toString());
		    activityUrl = WebUtil.appendParameterToURL(activityUrl, "toolSessionID",
			    toolSession.getToolSessionId().toString());
		    activityDTO.setActivityUrl(activityUrl);
		}
	    }

	    List<OutcomeMapping> outcomeMappings = outcomeService.getOutcomeMappings(null, activity.getToolContentId(),
		    null, null);
	    if (!outcomeMappings.isEmpty()) {
		ArrayNode outcomeMappingsJSON = JsonNodeFactory.instance.arrayNode();
		for (OutcomeMapping outcomeMapping : outcomeMappings) {
		    ObjectNode outcomeMappingJSON = JsonNodeFactory.instance.objectNode();
		    Outcome outcome = outcomeMapping.getOutcome();
		    outcomeMappingJSON.put("mappingId", outcomeMapping.getMappingId());
		    outcomeMappingJSON.put("name", outcome.getName());
		    outcomeMappingJSON.put("code", outcome.getCode());
		    ArrayNode possibleValues = JsonNodeFactory.instance.arrayNode();
		    for (OutcomeScaleItem possibleValue : outcome.getScale().getItems()) {
			possibleValues.add(possibleValue.getName());
		    }
		    outcomeMappingJSON.set("possibleValues", possibleValues);
		    OutcomeResult result = outcomeService.getOutcomeResult(userId, outcomeMapping.getMappingId());
		    if (result != null) {
			outcomeMappingJSON.put("value", result.getValue());
		    }
		    outcomeMappingsJSON.add(outcomeMappingJSON);
		}
		activityDTO.setOutcomes(outcomeMappingsJSON.toString());
	    }

	    gradebookActivityDTOs.add(activityDTO);
	}

	return gradebookActivityDTOs;
    }

    @Override
    public List<GradebookGridRowDTO> getGBActivityArchiveRowsForLearner(Long activityId, Integer userId,
	    TimeZone userTimezone) {
	logger.debug("Getting archive gradebook user data for activity: " + activityId + ". For user: " + userId);

	Activity activity = getActivityById(activityId);
	Lesson lesson = activity.getLearningDesign().getLessons().iterator().next();

	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<>();
	List<GradebookUserLessonArchive> lessonArchives = gradebookDAO.getArchivedLessonMarks(lesson.getLessonId(),
		userId);
	int attemptOrder = lessonArchives.size();
	List<GradebookUserActivityArchive> activityArchives = gradebookDAO.getArchivedActivityMarks(activityId, userId);
	for (GradebookUserLessonArchive lessonArchive : lessonArchives) {
	    Date archiveDate = lessonArchive.getArchiveDate();
	    GBActivityArchiveGridRowDTO activityDTO = new GBActivityArchiveGridRowDTO(attemptOrder,
		    lessonArchive.getMark());
	    LearnerProgressArchive learnerProgress = learnerProgressDAO.getLearnerProgressArchive(lesson.getLessonId(),
		    userId, lessonArchive.getArchiveDate());

	    if (learnerProgress != null) {
		// Setting status
		activityDTO.setStartDate(getActivityStartDate(learnerProgress, activity, userTimezone));
		activityDTO.setFinishDate(getActivityFinishDate(learnerProgress, activity, userTimezone));
		activityDTO.setTimeTaken(getActivityDuration(learnerProgress, activity));
		activityDTO.setStatus(getActivityStatusStr(learnerProgress, activity));

		for (GradebookUserActivityArchive activityArchive : activityArchives) {
		    // if it matches, we found an archived mark for this activity and this attempt
		    if (archiveDate.equals(activityArchive.getArchiveDate())) {
			activityDTO.setMark(activityArchive.getMark());
			activityDTO.setFeedback(activityArchive.getFeedback());
			break;
		    }
		}
	    }

	    gradebookActivityDTOs.add(activityDTO);
	    attemptOrder--;
	}

	return gradebookActivityDTOs;
    }

    @Override
    public List<GradebookGridRowDTO> getGBLessonComplete(Long lessonId, Integer userId) {
	logger.debug("Getting lesson complete gradebook user data for lesson: " + lessonId + ". For user: " + userId);

	Lesson lesson = lessonService.getLesson(lessonId);
	User learner = (User) userService.findById(User.class, userId);
	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<>();

	List<ToolActivity> activities = getLessonActivitiesForLearner(lesson, userId);
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

	    GradebookUserActivity gradebookActivity = gradebookDAO
		    .getGradebookUserDataForActivity(activity.getActivityId(), learner.getUserId());
	    if (gradebookActivity != null) {
		activityDTO.setMark(gradebookActivity.getMark());
	    }
	    activityDTO.setAverageMark(gradebookDAO.getAverageMarkForActivity(activity.getActivityId()));

	    LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(learner.getUserId(),
		    lesson.getLessonId());
	    activityDTO.setStatus(getActivityStatusStr(learnerProgress, activity));

	    gradebookActivityDTOs.add(activityDTO);
	}

	return gradebookActivityDTOs;
    }

    @Override
    public List<GradebookGridRowDTO> getGBActivityRowsForLesson(Long lessonId, TimeZone userTimezone,
	    boolean escapeTitles) {
	logger.debug("Getting gradebook data for lesson: " + lessonId);

	Lesson lesson = lessonService.getLesson(lessonId);
	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<>();

	List<ToolActivity> activities = getLessonToolActivitiesForLesson(lesson);

	for (ToolActivity activity : activities) {

	    Grouping grouping = activity.getGrouping();
	    if (grouping != null) {
		Set<Group> groups = grouping.getGroups();
		if (groups != null) {

		    for (Group group : groups) {
			GBActivityGridRowDTO activityDTO = getGradebookActivityDTO(activity, lesson,
				group.getGroupName(), group.getGroupId(), escapeTitles);
			gradebookActivityDTOs.add(activityDTO);
		    }

		}
	    } else {
		GBActivityGridRowDTO activityDTO = getGradebookActivityDTO(activity, lesson, null, null, escapeTitles);
		gradebookActivityDTOs.add(activityDTO);
	    }
	}

	return gradebookActivityDTOs;
    }

    @Override
    public List<GBUserGridRowDTO> getGBUserRowsForActivity(Lesson lesson, ToolActivity activity, Long groupId, int page,
	    int size, String sortBy, String sortOrder, String searchString, TimeZone timezone) {
	Long lessonId = lesson.getLessonId();
	Long activityId = activity.getActivityId();

	List<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<>();

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
		gUserDTO.setStartDate(getActivityStartDate(learnerProgress, activity, timezone));
		gUserDTO.setFinishDate(getActivityFinishDate(learnerProgress, activity, timezone));

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

		boolean hasArchivedMarks = gradebookDAO.hasArchivedMarks(lesson.getLessonId(), learner.getUserId());
		gUserDTO.setHasArchivedMarks(hasArchivedMarks);

		gradebookUserDTOs.add(gUserDTO);
	    }
	}

	return gradebookUserDTOs;
    }

    @Override
    public ArrayList<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson, TimeZone timeZone) {
	return getGBUserRowsForLesson(lesson, 0, 0, null, null, null, timeZone);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson, int page, int size, String sortBy,
	    String sortOrder, String searchString, TimeZone userTimeZone) {

	ArrayList<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<>();

	if (lesson != null) {
	    List<User> learners;
	    Map<Integer, LearnerProgress> userToLearnerProgressMap;
	    Map<Integer, GradebookUserLesson> userToGradebookUserLessonMap;
	    //size will be 0 in case of excel export
	    if (size == 0) {
		learners = new LinkedList<>(lesson.getAllLearners());
		Collections.sort(learners, new LastNameAlphabeticComparator());

		userToLearnerProgressMap = getUserToLearnerProgressMap(lesson, null);
		userToGradebookUserLessonMap = getUserToGradebookUserLessonMap(lesson, null);

	    } else {
		learners = gradebookDAO.getUsersByLesson(lesson.getLessonId(), page, size, sortBy, sortOrder,
			searchString);
		userToLearnerProgressMap = getUserToLearnerProgressMap(lesson, learners);
		userToGradebookUserLessonMap = getUserToGradebookUserLessonMap(lesson, learners);
	    }

	    boolean isWeighted = toolService.isWeightedMarks(lesson.getLearningDesign());

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
		    Date startDate = learnerProgress.getStartDate();
		    Date finishDate = learnerProgress.getFinishDate();

		    if (startDate != null && finishDate != null) {
			gradebookUserDTO.setTimeTaken(finishDate.getTime() - startDate.getTime());
		    }

		    if (startDate != null) {
			if (userTimeZone != null) {
			    startDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, startDate);
			}
			gradebookUserDTO.setStartDate(startDate);
		    }

		    if (learnerProgress.getFinishDate() != null) {
			if (userTimeZone != null) {
			    finishDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, finishDate);
			}
			gradebookUserDTO.setFinishDate(finishDate);
		    }

		}

		GradebookUserLesson gradebookUserLesson = userToGradebookUserLessonMap.get(learner.getUserId());
		if (gradebookUserLesson != null) {
		    gradebookUserDTO.setMark(gradebookUserLesson.getMark());
		    gradebookUserDTO.setFeedback(gradebookUserLesson.getFeedback());
		    gradebookUserDTO.setDisplayMarkAsPercent(isWeighted);
		}

		boolean hasArchivedMarks = gradebookDAO.hasArchivedMarks(lesson.getLessonId(), learner.getUserId());
		gradebookUserDTO.setHasArchivedMarks(hasArchivedMarks);

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

	ArrayList<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<>();

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
    public List<GradebookUserActivity> getGradebookUserActivities(Long activityId) {
	return gradebookDAO.getAllGradebookUserActivitiesForActivity(activityId);
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
	    logEventService.logEvent(LogEvent.TYPE_MARK_UPDATED, monitorUser.getUserID(), learner.getUserId(),
		    lesson.getLessonId(), null, message);
	}
    }

    @Override
    public void recalculateTotalMarksForLesson(Long lessonId) throws Exception {
	Lesson lesson = lessonDAO.getLesson(lessonId);
	if (lesson == null) {
	    return;
	}
	boolean weighted = toolService.isWeightedMarks(lesson.getLearningDesign());

	Map<Integer, GradebookUserLesson> userToGradebookUserLessonMap = getUserToGradebookUserLessonMap(lesson, null);

	//update for all users in activity
	Set<User> users = lesson.getAllLearners();
	for (User user : users) {
	    Integer userId = user.getUserId();
	    GradebookUserLesson gradebookUserLesson = userToGradebookUserLessonMap.get(userId);

	    List<GradebookUserActivity> userActivities = gradebookDAO.getGradebookUserActivitiesForLesson(lessonId,
		    userId);
	    Double totalMark = calculateLessonMark(weighted, userActivities, null);

	    if (totalMark != null) {

		if (totalMark > 0 && gradebookUserLesson == null) {
		    throw new Exception("An error detected: user (userId:" + userId + ") has total mark that equals to "
			    + totalMark + " but no assocciated gradebookUserLesson exist ");
		}

		if (gradebookUserLesson != null) {
		    gradebookUserLesson.setMark(totalMark);
		    gradebookDAO.insertOrUpdate(gradebookUserLesson);
		}
	    }
	}
    }

    @Override
    public void recalculateGradebookMarksForActivity(Activity activity) {
	Long activityId = activity.getActivityId();
	Lesson lesson = lessonDAO.getLessonForActivity(activityId);

	if ((lesson == null) || (activity == null) || !(activity instanceof ToolActivity)
		|| (((ToolActivity) activity).getEvaluation() == null)) {
	    return;
	}
	ToolActivity toolActivity = (ToolActivity) activity;

	// Getting the first activity evaluation
	ActivityEvaluation eval = toolActivity.getEvaluation();
	String toolOutputDefinition = eval.getToolOutputDefinition();

	Map<Integer, GradebookUserActivity> userToGradebookUserActivityMap = getUserToGradebookUserActivityMap(activity,
		null);
	Map<Integer, GradebookUserLesson> userToGradebookUserLessonMap = getUserToGradebookUserLessonMap(lesson, null);
	List<ToolOutput> toolOutputs = toolService.getOutputsFromTool(toolOutputDefinition, toolActivity);

	//update for all users in activity
	List<User> users = learnerProgressDAO.getLearnersCompletedActivity(activity);
	for (User user : users) {
	    //find according toolOutput
	    ToolOutput toolOutput = null;
	    for (ToolOutput toolOutputIter : toolOutputs) {
		if (toolOutputIter.getUserId().equals(user.getUserId())) {
		    toolOutput = toolOutputIter;
		}
	    }

	    //in case of toolOutput == null (which means no results in the tool but user nonetheless has finished activity), assign 0 as a result
	    Double outputDouble = (toolOutput == null || toolOutput.getValue() == null) ? null
		    : toolOutput.getValue().getDouble();

	    GradebookUserActivity gradebookUserActivity = userToGradebookUserActivityMap.get(user.getUserId());
	    GradebookUserLesson gradebookUserLesson = userToGradebookUserLessonMap.get(user.getUserId());

	    // Only set the mark if it hasnt previously been set by a teacher
	    if ((gradebookUserActivity == null) || !gradebookUserActivity.getMarkedInGradebook()) {
		updateUserActivityGradebookMark(lesson, user, toolActivity, outputDouble, false, false,
			gradebookUserActivity, gradebookUserLesson);
	    }
	}
    }

    @Override
    public void updateGradebookUserActivityMark(Lesson lesson, Activity activity, User learner) {
	ToolSession toolSession = toolService.getToolSessionByLearner(learner, activity);

	if ((toolSession == null) || (toolSession == null) || (learner == null) || (lesson == null)
		|| (activity == null) || !(activity instanceof ToolActivity)
		|| (((ToolActivity) activity).getEvaluation() == null)) {
	    return;
	}
	ToolActivity toolActivity = (ToolActivity) activity;

	// Getting the first activity evaluation
	ActivityEvaluation eval = toolActivity.getEvaluation();

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
			updateGradebookUserActivityMark(lesson, learner, toolActivity, outputDouble, false, false);
		    }
		}
	    }
	} catch (ToolException e) {
	    logger.debug(
		    "Runtime exception when attempted to get outputs for activity: " + toolActivity.getActivityId(), e);
	}
    }

    @Override
    public void updateGradebookUserActivityMark(Lesson lesson, User learner, Activity activity, Double mark,
	    Boolean markedInGradebook, boolean isAuditLogRequired) {
	GradebookUserActivity gradebookUserActivity = gradebookDAO
		.getGradebookUserDataForActivity(activity.getActivityId(), learner.getUserId());

	GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		learner.getUserId());

	updateUserActivityGradebookMark(lesson, learner, activity, mark, markedInGradebook, isAuditLogRequired,
		gradebookUserActivity, gradebookUserLesson);
    }

    @Override
    public boolean isWeightedMarks(Long lessonId) {
	Lesson lesson = lessonService.getLesson(lessonId);
	return toolService.isWeightedMarks(lesson.getLearningDesign());
    }

    @Override
    public List<String[]> getWeights(LearningDesign design) {
	List<String[]> evaluations = new ArrayList<>();
	Set<Activity> activities = design.getActivities();
	for (Activity activity : activities) {
	    if (activity.isToolActivity()) {
		// fetch real object, otherwise there is a cast error
		ToolActivity act = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
		ActivityEvaluation eval = act.getEvaluation();
		if (eval != null && eval.getWeight() != null && eval.getWeight() > 0) {
		    String[] evaluation = new String[3];
		    evaluation[0] = act.getTitle();
		    evaluation[1] = eval.getToolOutputDefinition();
		    evaluation[2] = eval.getWeight().toString() + '%';
		    evaluations.add(evaluation);
		}
	    }
	}
	return evaluations;
    }

    /*
     * TODO Method is not in use. Remove it?
     * 
     * private void updateUserActivityGradebookMark(Lesson lesson, Activity activity, User learner) {
     * ToolSession toolSession = toolService.getToolSessionByLearner(learner, activity);
     * 
     * if ((toolSession == null) || (toolSession == null) || (learner == null) || (lesson == null)
     * || (activity == null) || !(activity instanceof ToolActivity)
     * || (((ToolActivity) activity).getEvaluation() == null)) {
     * return;
     * }
     * ToolActivity toolActivity = (ToolActivity) activity;
     * 
     * // Getting the first activity evaluation
     * ActivityEvaluation eval = toolActivity.getEvaluation();
     * 
     * try {
     * ToolOutput toolOutput = toolService.getOutputFromTool(eval.getToolOutputDefinition(), toolSession,
     * learner.getUserId());
     * 
     * if (toolOutput != null) {
     * ToolOutputValue outputVal = toolOutput.getValue();
     * if (outputVal != null) {
     * Double outputDouble = outputVal.getDouble();
     * 
     * GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(toolActivity.getActivityId(),
     * learner.getUserId());
     * 
     * // Only set the mark if it hasnt previously been set by a teacher
     * if ((gradebookUserActivity == null) || !gradebookUserActivity.getMarkedInGradebook()) {
     * updateGradebookUserActivityMark(lesson, learner, toolActivity, outputDouble, false, false);
     * }
     * }
     * }
     * 
     * } catch (ToolException e) {
     * logger.debug(
     * "Runtime exception when attempted to get outputs for activity: " + toolActivity.getActivityId(), e);
     * }
     * }
     */

    /**
     * It's the same method as above, it only also accepts gradebookUserActivity and gradebookUserLesson as parameters.
     */
    private void updateUserActivityGradebookMark(Lesson lesson, User learner, Activity activity, Double mark,
	    Boolean markedInGradebook, boolean isAuditLogRequired, GradebookUserActivity gradebookUserActivity,
	    GradebookUserLesson gradebookUserLesson) {
	if ((lesson != null) && (activity != null) && (learner != null) && activity.isToolActivity()) {

	    // First, update the mark for the activity
	    if (gradebookUserActivity == null) {
		gradebookUserActivity = new GradebookUserActivity((ToolActivity) activity, learner);
	    }
	    String oldMark = (gradebookUserActivity.getMark() == null) ? "-"
		    : gradebookUserActivity.getMark().toString();
	    Double oldActivityMark = gradebookUserActivity.getMark();

	    gradebookUserActivity.setMark(mark);
	    gradebookUserActivity.setUpdateDate(new Date());
	    gradebookUserActivity.setMarkedInGradebook(markedInGradebook);
	    gradebookDAO.insertOrUpdate(gradebookUserActivity);
	    //flush the session in order to make updated mark be available at calculating lesson total mark
	    gradebookDAO.flush();

	    // Now update the lesson mark
	    if (gradebookUserLesson == null) {
		gradebookUserLesson = new GradebookUserLesson(lesson, learner);
		gradebookDAO.insertOrUpdate(gradebookUserLesson);
		//flush the session to delay the same gradebookUserLesson from being inserted by another user (it  primarily targets situation of leader updating marks for all non-leaders in a group)
		gradebookDAO.flush();
	    }

	    boolean isWeightedMarks = toolService.isWeightedMarks(lesson.getLearningDesign());
	    aggregateTotalMarkForLesson(isWeightedMarks, gradebookUserLesson, gradebookUserActivity, oldActivityMark);

	    // audit log changed gradebook mark
	    if (isAuditLogRequired) {
		UserDTO monitorUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
		String markStr = mark == null ? "" : mark.toString();
		String[] args = new String[] { learner.getLogin() + "(" + learner.getUserId() + ")",
			lesson.getLessonId().toString(), activity.getActivityId().toString(), oldMark.toString(),
			markStr };
		String message = messageService.getMessage("audit.activity.change.mark", args);
		logEventService.logEvent(LogEvent.TYPE_MARK_UPDATED, monitorUser.getUserID(), learner.getUserId(),
			lesson.getLessonId(), activity.getActivityId(), message);
	    }

	    //propagade mark to integration server, if the lesson has been finished by the learner
	    LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(learner.getUserId(),
		    lesson.getLessonId());
	    if (learnerProgress != null && learnerProgress.isComplete()) {
		integrationService.pushMarkToLtiConsumer(learner, lesson, gradebookUserLesson.getMark());
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
    public void updateGradebookUserActivityFeedback(Activity activity, User learner, String feedback) {

	GradebookUserActivity gradebookUserActivity = gradebookDAO
		.getGradebookUserDataForActivity(activity.getActivityId(), learner.getUserId());

	if (gradebookUserActivity == null) {
	    gradebookUserActivity = new GradebookUserActivity((ToolActivity) activity, learner);
	}

	gradebookUserActivity.setFeedback(feedback);
	gradebookUserActivity.setUpdateDate(new Date());
	gradebookDAO.insertOrUpdate(gradebookUserActivity);
    }

    private void updateUserActivityGradebookFeedback(Activity activity, User learner, String feedback) {

	GradebookUserActivity gradebookUserActivity = gradebookDAO
		.getGradebookUserDataForActivity(activity.getActivityId(), learner.getUserId());

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

	boolean isMarksReleased = lesson.getMarksReleased();
	lesson.setMarksReleased(!isMarksReleased);
	userService.save(lesson);

	// audit log marks released
	UserDTO monitor = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	String messageKey = (isMarksReleased) ? "audit.marks.released.off" : "audit.marks.released.on";
	String message = messageService.getMessage(messageKey, new String[] { lessonId.toString() });
	logEventService.logEvent(LogEvent.TYPE_MARK_RELEASED, monitor.getUserID(), null, lessonId, null, message);
    }

    @Override
    public List<GBLessonGridRowDTO> getGBLessonRows(Organisation organisation, User user, User viewer,
	    boolean isGroupManager, GBGridView view, int page, int size, String sortBy, String sortOrder,
	    String searchString, TimeZone userTimeZone) {
	List<GBLessonGridRowDTO> lessonRows = new ArrayList<>();
	Integer userId = user.getUserId();
	Integer orgId = organisation.getOrganisationId();

	if (organisation != null) {
	    List<Lesson> lessons = (view == GBGridView.MON_COURSE || view == GBGridView.LIST
		    || view == GBGridView.MON_USER)
			    ? gradebookDAO.getLessonsByGroupAndUser(isGroupManager ? null : viewer.getUserId(), true,
				    orgId, page, size, sortBy, sortOrder, searchString)
			    : lessonService.getLessonsByGroupAndUser(userId, orgId);

	    if (lessons != null) {

		for (Lesson lesson : lessons) {

		    // For My Grades gradebook page: don't include lesson in list if the user doesn't have permission.
		    if (view == GBGridView.LRN_COURSE) {
			boolean marksReleased = lesson.getMarksReleased();
			boolean hasLearnerPermission = lesson.getAllLearners().contains(user);
			if (!hasLearnerPermission || !marksReleased) {
			    continue;
			}
		    }

		    GBLessonGridRowDTO lessonRow = new GBLessonGridRowDTO();
		    lessonRows.add(lessonRow);
		    lessonRow.setLessonName(lesson.getLessonName());
		    lessonRow.setId(lesson.getLessonId().toString());
		    boolean isWeightedMarks = toolService.isWeightedMarks(lesson.getLearningDesign());
		    lessonRow.setDisplayMarkAsPercent(isWeightedMarks);

		    if (view == GBGridView.LIST) {
			continue;

		    } else if (view == GBGridView.MON_COURSE) {

			// Setting the averages for monitor view
			lessonRow.setMedianTimeTaken(gradebookDAO.getMedianTimeTakenLesson(lesson.getLessonId()));
			lessonRow.setAverageMark(gradebookDAO.getAverageMarkForLesson(lesson.getLessonId()));

			// Set the gradebook monitor url
			String gbMonURL = Configuration.get(ConfigurationKeys.SERVER_URL)
				+ "gradebook/gradebookMonitoring.do?lessonID=" + lesson.getLessonId().toString();
			lessonRow.setGradebookMonitorURL(gbMonURL);

			Date startDate = lesson.getStartDateTime();
			if (startDate != null) {
			    if (userTimeZone != null) {
				startDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, startDate);
			    }
			    lessonRow.setStartDate(startDate);
			}

		    } else if ((view == GBGridView.LRN_COURSE) || (view == GBGridView.MON_USER)) {

			GradebookUserLesson gbLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
				userId);

			lessonRow.setMedianTimeTaken(gradebookDAO.getMedianTimeTakenLesson(lesson.getLessonId()));
			lessonRow.setAverageMark(gradebookDAO.getAverageMarkForLesson(lesson.getLessonId()));

			if (gbLesson != null) {
			    lessonRow.setMark(gbLesson.getMark());
			    lessonRow.setFeedback(gbLesson.getFeedback());
			}

			LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(userId,
				lesson.getLessonId());
			lessonRow.setStatus(getLessonStatusStr(learnerProgress));
			if (learnerProgress != null) {
			    Date startDate = learnerProgress.getStartDate();
			    Date finishDate = learnerProgress.getFinishDate();

			    if (startDate != null && finishDate != null) {
				lessonRow.setTimeTaken(finishDate.getTime() - startDate.getTime());
			    }

			    if (startDate != null) {
				if (userTimeZone != null) {
				    startDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, startDate);
				}
				lessonRow.setStartDate(startDate);
			    }

			    if (learnerProgress.getFinishDate() != null) {
				if (userTimeZone != null) {
				    finishDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, finishDate);
				}
				lessonRow.setFinishDate(finishDate);
			    }
			}
		    }

		    if (lesson.getOrganisation().getOrganisationId() != organisation.getOrganisationId()) {
			lessonRow.setSubGroup(lesson.getOrganisation().getName());
		    } else {
			lessonRow.setSubGroup("");
		    }
		}
	    }
	}

	return lessonRows;
    }

    private Map<ToolActivity, List<GBUserGridRowDTO>> getDataForLessonGradebookExport(Lesson lesson) {

	Map<ToolActivity, List<GBUserGridRowDTO>> activityToUserDTOMap = new LinkedHashMap<>();

	Set<User> learners = new TreeSet<User>(new LastNameAlphabeticComparator());
	if (lesson.getAllLearners() != null) {
	    learners.addAll(lesson.getAllLearners());
	}

	Map<Integer, LearnerProgress> userToLearnerProgressMap = getUserToLearnerProgressMap(lesson, null);
	List<Activity> activities = getLessonActivitiesForLesson(lesson);

	for (Activity activity : activities) {
	    getActivityDataForLessonGradebookExport(activityToUserDTOMap, learners, userToLearnerProgressMap, activity);
	}

	return activityToUserDTOMap;
    }

    @SuppressWarnings("unchecked")
    private void getActivityDataForLessonGradebookExport(Map<ToolActivity, List<GBUserGridRowDTO>> activityToUserDTOMap,
	    Set<User> learners, Map<Integer, LearnerProgress> userToLearnerProgressMap, Activity activity) {

	if (activity.isToolActivity()) {
	    List<GBUserGridRowDTO> userDTOs = getToolActivityDataForLessonGradebookExport(learners,
		    userToLearnerProgressMap, (ToolActivity) activity);
	    activityToUserDTOMap.put((ToolActivity) activity, userDTOs);

	} else if (activity instanceof ComplexActivity) {
	    // encountered a sequence, branch, optional or parallel within a branching sequence
	    Set<User> complexLearners = learners;

	    if (activity instanceof SequenceActivity) {
		// use only a subset of learners for this branch of the branching activity based on who has started the branch
		complexLearners = new TreeSet<User>(new LastNameAlphabeticComparator());
		for (User learner : learners) {
		    LearnerProgress learnerProgress = userToLearnerProgressMap.get(learner.getUserId());
		    if (learnerProgress != null && (learnerProgress.getCompletedActivities().get(activity) != null
			    || learnerProgress.getAttemptedActivities().get(activity) != null)) {
			complexLearners.add(learner);
		    }
		}
	    }

	    ComplexActivity sequence = (ComplexActivity) activity;
	    Set<Activity> childActivities = sequence.getActivities();
	    for (Activity childActivity : childActivities) {
		getActivityDataForLessonGradebookExport(activityToUserDTOMap, complexLearners, userToLearnerProgressMap,
			activityDAO.getActivityByActivityId(childActivity.getActivityId()));
	    }
	}
    }

    private List<GBUserGridRowDTO> getToolActivityDataForLessonGradebookExport(Set<User> learners,
	    Map<Integer, LearnerProgress> userToLearnerProgressMap, ToolActivity toolActivity) {

	Map<Integer, GradebookUserActivity> userToGradebookUserActivityMap = getUserToGradebookUserActivityMap(
		toolActivity, null);

	List<GBUserGridRowDTO> userDTOs = new ArrayList<>();

	for (User learner : learners) {
	    GBUserGridRowDTO userDTO = new GBUserGridRowDTO(learner);

	    // Set the progress
	    LearnerProgress learnerProgress = userToLearnerProgressMap.get(learner.getUserId());
	    userDTO.setTimeTaken(getActivityDuration(learnerProgress, toolActivity));
	    userDTO.setStartDate(getActivityStartDate(learnerProgress, toolActivity, null));
	    userDTO.setFinishDate(getActivityFinishDate(learnerProgress, toolActivity, null));

	    // Add marks and feedback
	    GradebookUserActivity gradebookUserActivity = userToGradebookUserActivityMap.get(learner.getUserId());
	    if (gradebookUserActivity != null) {
		userDTO.setMark(gradebookUserActivity.getMark());
	    }
	    userDTOs.add(userDTO);
	}

	return userDTOs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ExcelSheet> exportLessonGradebook(Lesson lesson) {
	boolean isWeighted = toolService.isWeightedMarks(lesson.getLearningDesign());

	List<ExcelSheet> sheets = new LinkedList<>();

	// -------------------- process summary excel page --------------------------------

	ExcelSheet summarySheet = new ExcelSheet(getMessage("gradebook.export.lesson.summary"));
	sheets.add(summarySheet);

	// Adding the lesson average data to the summary
	Double lessonAverageMarkValue = getAverageMarkForLesson(lesson.getLessonId());
	ExcelRow lessonAverageMark = summarySheet.initRow();
	lessonAverageMark.addCell(getMessage("gradebook.export.average.lesson.mark"), true);
	if (isWeighted) {
	    lessonAverageMark.addPercentageCell(lessonAverageMarkValue == null ? null : lessonAverageMarkValue / 100.0);
	} else {
	    lessonAverageMark.addCell(lessonAverageMarkValue);
	}

	ExcelRow lessonMedianTimeTaken = summarySheet.initRow();
	lessonMedianTimeTaken.addCell(getMessage("gradebook.export.average.lesson.time.taken"), true);
	lessonMedianTimeTaken.addCell(gradebookDAO.getMedianTimeTakenLesson(lesson.getLessonId()) / 1000, false);
	summarySheet.addEmptyRow();

	// Adding the activity average data to the summary
	List<GradebookGridRowDTO> activityRows = getGBActivityRowsForLesson(lesson.getLessonId(), null, false);
	ExcelRow activityAverageTitle = summarySheet.initRow();
	activityAverageTitle.addCell(getMessage("gradebook.export.activities"), true);

	// Setting up the activity summary table
	ExcelRow activityAverageRow = summarySheet.initRow();
	activityAverageRow.addCell(getMessage("gradebook.export.activity"), true);
	activityAverageRow.addCell(getMessage("gradebook.columntitle.competences"), true);
	activityAverageRow.addCell(getMessage("gradebook.export.average.time.taken.seconds"), true);
	activityAverageRow.addCell(getMessage("gradebook.columntitle.averageMark"), true);
	activityAverageRow.addCell(getMessage("gradebook.export.min.time.taken.seconds"), true);
	activityAverageRow.addCell(getMessage("gradebook.export.max.time.taken.seconds"), true);

	Iterator it = activityRows.iterator();
	while (it.hasNext()) {
	    GBActivityGridRowDTO activityRow = (GBActivityGridRowDTO) it.next();
	    // Add the activity average data
	    ExcelRow activityDataRow = summarySheet.initRow();
	    activityDataRow.addCell(activityRow.getRowName()); // this is the problem entry
	    activityDataRow.addCell(activityRow.getCompetences());
	    activityDataRow.addCell(activityRow.getMedianTimeTakenSeconds());
	    activityDataRow.addCell(activityRow.getAverageMark());
	    activityDataRow.addCell(activityRow.getMinTimeTakenSeconds());
	    activityDataRow.addCell(activityRow.getMaxTimeTakenSeconds());
	}
	summarySheet.addEmptyRow();

	// Adding the user lesson marks to the summary
	ExcelRow userMarksTitle = summarySheet.initRow();
	userMarksTitle.addCell(getMessage("gradebook.export.total.marks.for.lesson"), true);

	// Fetching the user data
	ArrayList<GBUserGridRowDTO> userRows = getGBUserRowsForLesson(lesson, null);

	// Setting up the user marks table
	ExcelRow userTitleRow = summarySheet.initRow();
	userTitleRow.addCell(getMessage("gradebook.export.last.name"), true);
	userTitleRow.addCell(getMessage("gradebook.export.first.name"), true);
	userTitleRow.addCell(getMessage("gradebook.export.login"), true);
	userTitleRow.addCell(getMessage("gradebook.exportcourse.progress"), true);
	userTitleRow.addCell(getMessage("gradebook.export.time.taken.seconds"), true);
	userTitleRow.addCell(getMessage("gradebook.export.total.mark"), true);

	for (GBUserGridRowDTO userRow : userRows) {
	    // Adding the user data for the lesson
	    ExcelRow userDataRow = summarySheet.initRow();
	    userDataRow.addCell(userRow.getLastName());
	    userDataRow.addCell(userRow.getFirstName());
	    userDataRow.addCell(userRow.getLogin());
	    userDataRow.addCell(getProgressMessage(userRow));
	    userDataRow.addCell(userRow.getTimeTakenSeconds());
	    Double userMark = userRow.getMark();
	    if (isWeighted) {
		userDataRow.addPercentageCell(userMark == null ? null : userMark / 100.0);
	    } else {
		userDataRow.addCell(userMark);
	    }
	}
	summarySheet.addEmptyRow();

	// -- Summary for activity marks (simplified) ---

	Map<ToolActivity, List<GBUserGridRowDTO>> activityToUserDTOMap = getDataForLessonGradebookExport(lesson);
	//filter out all activities that doesn't have numeric outputs
	Map<ToolActivity, List<GBUserGridRowDTO>> filteredActivityToUserDTOMap = new LinkedHashMap<>();
	for (ToolActivity activity : activityToUserDTOMap.keySet()) {
	    String toolSignature = activity.getTool().getToolSignature();
	    //check whether toolActivity has a NumericToolOutput
	    if (activity.getEvaluation() != null && (TOOL_SIGNATURE_ASSESSMENT.equals(toolSignature)
		    || TOOL_SIGNATURE_SCRATCHIE.equals(toolSignature))) {
		filteredActivityToUserDTOMap.put(activity, activityToUserDTOMap.get(activity));
	    }
	}

	//add header
	ExcelRow headerRow = summarySheet.initRow();
	headerRow.addCell(getMessage("gradebook.summary.activity.marks"), true);

	headerRow = summarySheet.initRow();
	headerRow.addEmptyCells(3);
	for (Activity activity : filteredActivityToUserDTOMap.keySet()) {
	    headerRow.addCell(activity.getTitle(), true); // this one works
	}

	headerRow = summarySheet.initRow();
	headerRow.addCell(getMessage("gradebook.export.last.name"), true);
	headerRow.addCell(getMessage("gradebook.export.first.name"), true);
	headerRow.addCell(getMessage("gradebook.export.login"), true);
	for (Activity activity : filteredActivityToUserDTOMap.keySet()) {
	    headerRow.addCell(getMessage("gradebook.columntitle.mark"), true);
	}
	headerRow.addCell(getMessage("gradebook.export.total.mark"), true);

	//iterating through all users in a lesson
	for (GBUserGridRowDTO userRow : userRows) {
	    ExcelRow userDataRow = summarySheet.initRow();
	    userDataRow.addCell(userRow.getLastName());
	    userDataRow.addCell(userRow.getFirstName());
	    userDataRow.addCell(userRow.getLogin());

	    for (Activity activity : filteredActivityToUserDTOMap.keySet()) {

		//find according userActivityMark
		Double userActivityMark = null;
		List<GBUserGridRowDTO> userDtos = filteredActivityToUserDTOMap.get(activity);
		for (GBUserGridRowDTO userDto : userDtos) {
		    if (userDto.getLogin().equals(userRow.getLogin())) {
			userActivityMark = userDto.getMark();
			break;
		    }
		}
		userDataRow.addCell(userActivityMark);
	    }
	    Double userMark = userRow.getMark();
	    if (isWeighted) {
		userDataRow.addPercentageCell(userMark == null ? null : userMark / 100.0);
	    } else {
		userDataRow.addCell(userRow.getMark());
	    }
	}

	// -------------------- process activity excel page --------------------------------
	ExcelSheet activitySheet = new ExcelSheet(getMessage("gradebook.gridtitle.activitygrid"));
	sheets.add(activitySheet);

	for (Activity activity : activityToUserDTOMap.keySet()) {

	    ExcelRow activityTitleRow = activitySheet.initRow();
	    activityTitleRow.addCell(activity.getTitle(), true);

	    ExcelRow titleRow = activitySheet.initRow();
	    titleRow.addCell(getMessage("gradebook.export.last.name"), true);
	    titleRow.addCell(getMessage("gradebook.export.first.name"), true);
	    titleRow.addCell(getMessage("gradebook.export.login"), true);
	    titleRow.addCell(getMessage("gradebook.columntitle.startDate"), true);
	    titleRow.addCell(getMessage("gradebook.columntitle.completeDate"), true);
	    titleRow.addCell(getMessage("gradebook.export.time.taken.seconds"), true);
	    titleRow.addCell(getMessage("gradebook.columntitle.mark"), true);

	    // Get the rest of the data
	    List<GBUserGridRowDTO> userDtos = activityToUserDTOMap.get(activity);
	    for (GBUserGridRowDTO userDto : userDtos) {

		String startDate = (userDto.getStartDate() == null) ? ""
			: FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(userDto.getStartDate());
		String finishDate = (userDto.getFinishDate() == null) ? ""
			: FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(userDto.getFinishDate());

		ExcelRow userDataRow = activitySheet.initRow();
		userDataRow.addCell(userDto.getLastName());
		userDataRow.addCell(userDto.getFirstName());
		userDataRow.addCell(userDto.getLogin());
		userDataRow.addCell(startDate);
		userDataRow.addCell(finishDate);
		userDataRow.addCell(userDto.getTimeTakenSeconds());
		userDataRow.addCell(userDto.getMark());
	    }

	    activitySheet.addEmptyRow();
	}

	// -------------------- process Learner View page --------------------------------

	ExcelSheet learnerViewSheet = new ExcelSheet(getMessage("gradebook.export.learner.view"));
	sheets.add(learnerViewSheet);

	Set<User> learners = new TreeSet<User>(new LastNameAlphabeticComparator());
	if (lesson.getAllLearners() != null) {
	    learners.addAll(lesson.getAllLearners());
	}

	for (User learner : learners) {

	    userTitleRow = learnerViewSheet.initRow();
	    userTitleRow.addCell(learner.getFullName() + " (" + learner.getLogin() + ")", true);

	    ExcelRow titleRow = learnerViewSheet.initRow();
	    titleRow.addCell(getMessage("gradebook.export.activity"), true);
	    titleRow.addCell(getMessage("gradebook.columntitle.startDate"), true);
	    titleRow.addCell(getMessage("gradebook.columntitle.completeDate"), true);
	    titleRow.addCell(getMessage("gradebook.export.time.taken.seconds"), true);
	    titleRow.addCell(getMessage("gradebook.columntitle.mark"), true);

	    Map<Long, String> activityIdToName = new HashMap<>();

	    for (ToolActivity activity : activityToUserDTOMap.keySet()) {

		//find userDto corresponding to the user
		List<GBUserGridRowDTO> userDtos = activityToUserDTOMap.get(activity);
		GBUserGridRowDTO userDto = null;
		for (GBUserGridRowDTO dbUserDTO : userDtos) {
		    if (dbUserDTO.getId().equals(learner.getUserId().toString())) {
			userDto = dbUserDTO;
		    }
		}

		// userDto will be null if this tool activity was within a branch and the user has not attempted that branch
		if (userDto != null) {
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
		    String activityRowName = (groupName != null && groupId != null)
			    ? activity.getTitle() + " (" + groupName + ")"
			    : activity.getTitle();
		    activityIdToName.put(activity.getActivityId(), activityRowName);

		    String startDate = (userDto.getStartDate() == null) ? ""
			    : FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(userDto.getStartDate());
		    String finishDate = (userDto.getFinishDate() == null) ? ""
			    : FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(userDto.getFinishDate());

		    ExcelRow activityDataRow = learnerViewSheet.initRow();
		    activityDataRow.addCell(activityRowName);
		    activityDataRow.addCell(startDate);
		    activityDataRow.addCell(finishDate);
		    activityDataRow.addCell(userDto.getTimeTakenSeconds());
		    activityDataRow.addCell(userDto.getMark());
		}
	    }

	    // check if learner has restarted the lesson and has archived marks
	    boolean hasArchivedMarks = gradebookDAO.hasArchivedMarks(lesson.getLessonId(), learner.getUserId());
	    if (hasArchivedMarks) {
		// "Previous attempts" row
		ExcelRow attemptsRow = learnerViewSheet.initRow();
		attemptsRow.addCell(getMessage("gradebook.columntitle.attempts"), true);

		List<GradebookUserLessonArchive> lessonArchives = gradebookDAO
			.getArchivedLessonMarks(lesson.getLessonId(), learner.getUserId());
		int attemptOrder = lessonArchives.size();
		// go through each lesson attempt
		for (GradebookUserLessonArchive lessonArchive : lessonArchives) {
		    // lesson attempt header
		    ExcelRow attemptRow = learnerViewSheet.initRow();
		    attemptRow.addCell(getMessage("gradebook.columntitle.attempt"), true);
		    ExcelCell cell = attemptRow.addCell(attemptOrder, true);
		    cell.setAlignment(ExcelCell.ALIGN_LEFT);
		    attemptRow.addCell(getMessage("gradebook.columntitle.lesson.mark"), true);
		    attemptRow.addCell(lessonArchive.getMark());

		    Date archiveDate = lessonArchive.getArchiveDate();
		    LearnerProgressArchive learnerProgress = learnerProgressDAO
			    .getLearnerProgressArchive(lesson.getLessonId(), learner.getUserId(), archiveDate);

		    // go throuch each activity and see if there is an archived mark for it
		    for (ToolActivity activity : activityToUserDTOMap.keySet()) {
			GradebookUserActivityArchive activityArchive = null;
			List<GradebookUserActivityArchive> activityArchives = gradebookDAO
				.getArchivedActivityMarks(activity.getActivityId(), learner.getUserId());
			for (GradebookUserActivityArchive possibleActivityArchive : activityArchives) {
			    // if it matches, we found an archived mark for this activity and this attempt
			    if (archiveDate.equals(possibleActivityArchive.getArchiveDate())) {
				activityArchive = possibleActivityArchive;
				break;
			    }
			}

			ExcelRow activityDataRow = learnerViewSheet.initRow();
			activityDataRow.addCell(activityIdToName.get(activity.getActivityId()));
			Date startDate = getActivityStartDate(learnerProgress, activity, null);
			activityDataRow.addCell(startDate == null ? ""
				: FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(startDate));
			Date finishDate = getActivityFinishDate(learnerProgress, activity, null);
			activityDataRow.addCell(finishDate == null ? ""
				: FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(finishDate));
			Long duration = getActivityDuration(learnerProgress, activity);
			activityDataRow.addCell(duration == null ? "" : duration / 1000);
			activityDataRow.addCell(activityArchive == null ? "" : activityArchive.getMark(), false);
		    }
		    attemptOrder--;
		}
	    }
	    learnerViewSheet.addEmptyRow();
	}

	return sheets;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ExcelSheet> exportCourseGradebook(Integer userId, Integer organisationId) {
	List<ExcelSheet> sheets = new LinkedList<>();

	// The entire data list
	ExcelSheet sheet = new ExcelSheet(getMessage("gradebook.exportcourse.course.summary"));
	sheets.add(sheet);

	Set<Lesson> lessons = new TreeSet<>(new LessonComparator());
	lessons.addAll(lessonService.getLessonsByGroupAndUser(userId, organisationId));

	Map<Long, Boolean> isWeightedLessonMap = new HashMap<>();

	if ((lessons != null) && (lessons.size() > 0)) {
	    // Adding the user lesson marks to the summary----------------------
	    ExcelRow lessonsNames = sheet.initRow();
	    lessonsNames.addCell("");
	    lessonsNames.addCell("");
	    lessonsNames.addCell("");
	    for (Lesson lesson : lessons) {
		lessonsNames.addCell(messageService.getMessage("gradebook.exportcourse.lesson",
			new Object[] { lesson.getLessonName() }), true);
		lessonsNames.addCell("");
		lessonsNames.addCell("");
		lessonsNames.addCell("");
		lessonsNames.addCell("");
		lessonsNames.addCell("");
	    }

	    // Setting up the user marks table
	    ExcelRow headerRow = sheet.initRow();
	    headerRow.addCell(getMessage("gradebook.export.last.name"), true);
	    headerRow.addCell(getMessage("gradebook.export.first.name"), true);
	    headerRow.addCell(getMessage("gradebook.export.login"), true);
	    for (Lesson lesson : lessons) {
		headerRow.addCell(getMessage("gradebook.exportcourse.progress"), true);
		headerRow.addCell(getMessage("gradebook.columntitle.startDate"));
		headerRow.addCell(getMessage("gradebook.columntitle.completeDate"));
		headerRow.addCell(getMessage("gradebook.export.time.taken.seconds"), true);
		headerRow.addCell(getMessage("gradebook.exportcourse.lessonFeedback"), true);
		headerRow.addCell(getMessage("gradebook.export.total.mark"), true);
	    }

	    // collect users from all lessons & check if lesson uses weightings
	    LinkedHashSet<User> allLearners = new LinkedHashSet<>();
	    List<Long> lessonIds = new LinkedList<>();
	    for (Lesson lesson : lessons) {
		Set dbLessonUsers = lesson.getAllLearners();
		allLearners.addAll(dbLessonUsers);
		lessonIds.add(lesson.getLessonId());
		boolean isWeightedMarks = toolService.isWeightedMarks(lesson.getLearningDesign());
		isWeightedLessonMap.put(lesson.getLessonId(), isWeightedMarks);
	    }

	    // Fetching the user data
	    List<LearnerProgress> learnerProgresses = new LinkedList<>();
	    List<GradebookUserLesson> gradebookUserLessons = new LinkedList<>();
	    if (!allLearners.isEmpty()) {
		learnerProgresses = learnerProgressDAO.getLearnerProgressForLessons(lessonIds);
		gradebookUserLessons = gradebookDAO.getGradebookUserLessons(lessonIds);
	    }

	    //sort users by last name
	    TreeSet<User> sortedLearners = new TreeSet<User>(new LastNameAlphabeticComparator());
	    sortedLearners.addAll(allLearners);

	    for (User learner : sortedLearners) {
		ExcelRow userDataRow = sheet.initRow();
		userDataRow.addCell(learner.getLastName());
		userDataRow.addCell(learner.getFirstName());
		userDataRow.addCell(learner.getLogin());

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
			    startDate = FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT
				    .format(learnerProgress.getStartDate());
			}

			// finish date
			if ((learnerProgress != null) && (learnerProgress.getFinishDate() != null)) {
			    finishDate = FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT
				    .format(learnerProgress.getFinishDate());
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
		    userDataRow.addCell(getProgressMessage(userDto));
		    userDataRow.addCell(startDate);
		    userDataRow.addCell(finishDate);
		    userDataRow.addCell(timeTakenSeconds);
		    userDataRow.addCell(feedback);
		    if (isWeightedLessonMap.get(lesson.getLessonId())) {
			userDataRow.addPercentageCell(mark == null ? null : mark / 100.0);
		    } else {
			userDataRow.addCell(mark);
		    }
		}
	    }
	}
	return sheets;
    }

    @Override
    public List<ExcelSheet> exportSelectedLessonsGradebook(Integer userId, Integer organisationId, String[] lessonIds,
	    boolean simplified) {
	List<ExcelSheet> sheets = new LinkedList<>();
	ExcelSheet sheet = new ExcelSheet(getMessage("gradebook.exportcourse.course.summary"));
	sheets.add(sheet);

	Organisation organisation = (Organisation) userService.findById(Organisation.class, organisationId);

	User user = (User) userService.findById(User.class, userId);
	Set<Lesson> selectedLessons = new TreeSet<>(new LessonComparator());
	Map<Long, Boolean> isWeightedLessonMap = new HashMap<>();

	// Don't include lesson in list if the user doesn't have permission
	Integer organisationToCheckPermission = (organisation.getOrganisationType().getOrganisationTypeId()
		.equals(OrganisationType.COURSE_TYPE)) ? organisation.getOrganisationId()
			: organisation.getParentOrganisation().getOrganisationId();
	boolean isGroupManager = userService.isUserInRole(userId, organisationToCheckPermission, Role.GROUP_MANAGER);

	// collect users from all lessons
	Set<User> allLearners = new LinkedHashSet<>();
	Map<Long, List<ToolActivity>> lessonActivitiesMap = new HashMap<>();
	List<ToolActivity> allActivities = new ArrayList<>();

	for (String lessonIdStr : lessonIds) {
	    Long lessonId = Long.parseLong(lessonIdStr);
	    Lesson lesson = lessonService.getLesson(lessonId);

	    if (!(lesson.getLessonClass().isStaffMember(user) || isGroupManager)) {
		continue;
	    }

	    selectedLessons.add(lesson);
	    boolean isWeightedMarks = toolService.isWeightedMarks(lesson.getLearningDesign());
	    isWeightedLessonMap.put(lesson.getLessonId(), isWeightedMarks);

	    allLearners.addAll(lesson.getAllLearners());

	    List<ToolActivity> lessonActivities = getLessonToolActivitiesForLesson(lesson);
	    lessonActivitiesMap.put(lesson.getLessonId(), lessonActivities);
	    allActivities.addAll(lessonActivities);
	}

	if (!selectedLessons.isEmpty()) {
	    // Fetching the user data
	    List<Long> lessonIdLongs = new LinkedList<>();
	    for (String lessonId : lessonIds) {
		lessonIdLongs.add(Long.valueOf(lessonId));
	    }
	    List<LearnerProgress> learnerProgresses = new LinkedList<>();
	    if (!allLearners.isEmpty()) {
		learnerProgresses = learnerProgressDAO.getLearnerProgressForLessons(lessonIdLongs);
	    }

	    Map<Long, Long> activityToTotalMarkMap = getActivityToTotalMarkMap(allActivities);

	    Map<Long, Map<Integer, GradebookUserActivity>> activityTouserToGradebookUserActivityMap = new HashMap<>();
	    for (ToolActivity activity : allActivities) {
		Map<Integer, GradebookUserActivity> userToGradebookUserActivityMap = getUserToGradebookUserActivityMap(
			activity, null);
		activityTouserToGradebookUserActivityMap.put(activity.getActivityId(), userToGradebookUserActivityMap);
	    }

	    int numberCellsPerRow = simplified ? 3 + selectedLessons.size() + 3
		    : (selectedLessons.size() * 9) + (allActivities.size() * 2) + 5;

	    String weightedMessage = messageService.getMessage("label.activity.marks.weighted");
	    // Lesson names row----------------------
	    ExcelRow lessonsNames = sheet.initRow();
	    if (simplified) {
		lessonsNames.addEmptyCells(3);
		for (Lesson lesson : selectedLessons) {
		    lessonsNames.addCell(lesson.getLessonName(), true).setAlignment(ExcelCell.ALIGN_CENTER);
		}
		lessonsNames.addCell("", ExcelCell.BORDER_STYLE_LEFT_THICK);
		lessonsNames.addCell(getMessage("label.overall.totals"), true).setAlignment(ExcelCell.ALIGN_CENTER);
		lessonsNames.addCell("", ExcelCell.BORDER_STYLE_RIGHT_THICK);
	    } else {
		lessonsNames.addEmptyCells(4);
		for (Lesson lesson : selectedLessons) {
		    String lessonName = isWeightedLessonMap.get(lesson.getLessonId())
			    ? new StringBuilder(lesson.getLessonName()).append(" ").append(weightedMessage).toString()
			    : lesson.getLessonName();

		    List<ToolActivity> lessonActivities = lessonActivitiesMap.get(lesson.getLessonId());
		    int numberActivities = lessonActivities.size();
		    lessonsNames.addEmptyCells(numberActivities);
		    lessonsNames.addCell(lessonName, true);
		    lessonsNames.addEmptyCells(9 + (numberActivities * 2));
		}
//		i -= 2;
		lessonsNames.addCell("", ExcelCell.BORDER_STYLE_LEFT_THIN);
		lessonsNames.addCell(getMessage("label.overall.totals"), true);
		lessonsNames.addCell("", ExcelCell.BORDER_STYLE_RIGHT_THICK);
	    }

	    // Headers row----------------------
	    ExcelRow headerRow = sheet.initRow();
	    if (simplified) {
		// Simplified shows the learner's name once at the far left of the spreadsheet.
		headerRow.addCell(getMessage("gradebook.export.last.name"));
		headerRow.addCell(getMessage("gradebook.export.first.name"));
		headerRow.addCell(getMessage("gradebook.export.login"));

		for (Lesson lesson : selectedLessons) {
		    headerRow.addCell(getMessage("label.total.actuals"), false, ExcelCell.BORDER_STYLE_LEFT_THIN)
			    .setAlignment(ExcelCell.ALIGN_CENTER);
		}

		headerRow.addCell(getMessage("label.actuals"), true, ExcelCell.BORDER_STYLE_LEFT_THICK)
			.setAlignment(ExcelCell.ALIGN_CENTER);
		headerRow.addCell(getMessage("label.max")).setAlignment(ExcelCell.ALIGN_CENTER);
		headerRow.addCell("%", false, ExcelCell.BORDER_STYLE_RIGHT_THICK).setAlignment(ExcelCell.ALIGN_CENTER);
	    } else {
		//create Selected Lessons Header Full
		for (Lesson lesson : selectedLessons) {
		    headerRow.addCell(getMessage("gradebook.export.last.name"));
		    headerRow.addCell(getMessage("gradebook.export.first.name"));
		    headerRow.addCell(getMessage("gradebook.export.login"));
		    headerRow.addCell(getMessage("label.group"));
		    headerRow.addCell(getMessage("gradebook.columntitle.startDate"));
		    headerRow.addCell(getMessage("gradebook.columntitle.completeDate"));

		    List<ToolActivity> activities = lessonActivitiesMap.get(lesson.getLessonId());
		    for (Activity activity : activities) {
			headerRow.addCell(activity.getTitle(), true);
			headerRow.addCell(getMessage("label.max.possible"));
		    }

		    headerRow.addCell(getMessage("label.total.actuals"), true, ExcelCell.BORDER_STYLE_LEFT_THIN);
		    headerRow.addCell(getMessage("label.max.mark"));
		    headerRow.addCell("%", ExcelCell.BORDER_STYLE_RIGHT_THICK);
		}

		headerRow.addEmptyCells(2);
		headerRow.addCell(getMessage("label.actuals"), true, ExcelCell.BORDER_STYLE_LEFT_THIN);
		headerRow.addCell(getMessage("label.max"), true);
		headerRow.addCell("%", true, ExcelCell.BORDER_STYLE_RIGHT_THICK);
	    }

	    // Actual data rows----------------------
	    for (User learner : allLearners) {

		Double overallTotal = 0d;
		Double overallMaxMark = 0d;
		ExcelRow userRow = sheet.initRow();

		if (simplified) {
		    addUsernameCells(learner, userRow);
		}

		for (Lesson lesson : selectedLessons) {

		    Double lessonTotal = 0d;
		    Double lessonMaxMark = 0d;
		    List<ToolActivity> activities = lessonActivitiesMap.get(lesson.getLessonId());
		    Boolean weighted = isWeightedLessonMap.get(lesson.getLessonId());

		    if (!simplified) {
			addUsernameCells(learner, userRow);

			// check if learner is participating in this lesson
			if (!lesson.getAllLearners().contains(learner)) {
			    userRow.addEmptyCells(3 + (activities.size() * 2));
			    userRow.addCell("", ExcelCell.BORDER_STYLE_LEFT_THIN);
			    userRow.addCell("");
			    userRow.addCell("", ExcelCell.BORDER_STYLE_RIGHT_THICK);
			    continue;
			}

			// group name
			String groupName = "";
			for (Group group : lesson.getLessonClass().getGroups()) {
			    if (group.hasLearner(learner)) {
				groupName = group.getGroupName();
				break;
			    }
			}
			userRow.addCell(groupName);

			//start and complete dates
			LearnerProgress learnerProgress = null;
			for (LearnerProgress learnerProgressIter : learnerProgresses) {
			    if (learnerProgressIter.getUser().getUserId().equals(learner.getUserId())
				    && learnerProgressIter.getLesson().getLessonId().equals(lesson.getLessonId())) {
				learnerProgress = learnerProgressIter;
			    }
			}
			String startDate = (learnerProgress == null || learnerProgress.getStartDate() == null) ? ""
				: FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT
					.format(learnerProgress.getStartDate());
			userRow.addCell(startDate);
			String finishDate = (learnerProgress == null || learnerProgress.getFinishDate() == null) ? ""
				: FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT
					.format(learnerProgress.getFinishDate());
			userRow.addCell(finishDate);
		    }

		    for (ToolActivity activity : activities) {
			Map<Integer, GradebookUserActivity> userToGradebookUserActivityMap = activityTouserToGradebookUserActivityMap
				.get(activity.getActivityId());
			GradebookUserActivity gradebookUserActivity = userToGradebookUserActivityMap
				.get(learner.getUserId());

			Long rawActivityTotalMarks = 0l;
			if (activityToTotalMarkMap.get(activity.getActivityId()) != null) {
			    rawActivityTotalMarks = activityToTotalMarkMap.get(activity.getActivityId());
			}
			Integer weight = weighted && activity.getEvaluation() != null
				&& activity.getEvaluation().getWeight() != null ? activity.getEvaluation().getWeight()
					: null;
			Long weightedActivityTotalMarks = weight != null ? weight : rawActivityTotalMarks;

			Double mark = 0d;
			if (gradebookUserActivity != null) {
			    mark = gradebookUserActivity.getMark();
			    if (weight != null) {
				mark = doWeightedMarkCalc(mark, activity, weight, rawActivityTotalMarks);
			    }
			    if (!simplified) {
				userRow.addCell(mark);
			    }
			} else {
			    if (!simplified) {
				userRow.addCell("");
			    }
			}

			if (!simplified) {
			    if (weightedActivityTotalMarks > 0) {
				userRow.addCell(weightedActivityTotalMarks);
			    } else {
				userRow.addCell("");
			    }
			}

			lessonTotal += mark;
			overallTotal += mark;
			lessonMaxMark += weightedActivityTotalMarks;
			overallMaxMark += weightedActivityTotalMarks;
		    }

		    if (simplified) {
			if (weighted) {
			    userRow.addPercentageCell(lessonTotal / 100.0, false, ExcelCell.BORDER_STYLE_LEFT_THIN);
			} else {
			    userRow.addCell(lessonTotal, ExcelCell.BORDER_STYLE_LEFT_THIN);
			}
		    } else {
			userRow.addCell(lessonTotal, ExcelCell.BORDER_STYLE_LEFT_THIN);
			userRow.addCell(lessonMaxMark);
			Double percentage = (lessonMaxMark != 0) ? lessonTotal / lessonMaxMark : 0d;

			userRow.addPercentageCell(percentage, false, ExcelCell.BORDER_STYLE_RIGHT_THICK);
		    }
		}

		Double percentage = (overallMaxMark != 0) ? overallTotal / overallMaxMark : 0d;
		if (simplified) {
		    userRow.addCell(overallTotal, ExcelCell.BORDER_STYLE_LEFT_THICK);
		    userRow.addCell(overallMaxMark);
		    userRow.addPercentageCell(percentage, false, ExcelCell.BORDER_STYLE_RIGHT_THICK);
		} else {
		    userRow.addEmptyCells(2);
		    userRow.addCell(overallTotal, ExcelCell.BORDER_STYLE_LEFT_THIN);
		    userRow.addCell(overallMaxMark);
		    userRow.addPercentageCell(percentage, true, ExcelCell.BORDER_STYLE_RIGHT_THICK);
		}
	    }
	}
	return sheets;
    }

    private void addUsernameCells(User learner, ExcelRow userRow) {
	//first, last names and login
	String lastName = (learner.getLastName() == null) ? "" : learner.getLastName().toUpperCase();
	userRow.addCell(lastName);
	String firstName = (learner.getFirstName() == null) ? "" : learner.getFirstName().toUpperCase();
	userRow.addCell(firstName);
	userRow.addCell(learner.getLogin());
    }

    @Override
    public void updateGradebookUserActivityMark(Double mark, String feedback, Integer userID, Long toolSessionID,
	    Boolean markedInGradebook) {
	ToolSession toolSession = toolService.getToolSessionById(toolSessionID);
	User learner = (User) userService.findById(User.class, userID);
	if ((learner != null) && (toolSession != null)) {
	    ToolActivity activity = toolSession.getToolActivity();
	    GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(activity.getActivityId(), userID);

	    // If gradebook user activity is null or the mark is set by teacher or was set previously by user - save the
	    // mark and feedback
	    if ((gradebookUserActivity == null) || markedInGradebook || !gradebookUserActivity.getMarkedInGradebook()) {
		updateGradebookUserActivityMark(toolSession.getLesson(), learner, activity, mark, markedInGradebook,
			false);
		updateGradebookUserActivityFeedback(activity, learner, feedback);
	    }
	}
    }

    /*
     * TODO Method is not in use. Remove it?
     * 
     * public void updateActivityMark(Double mark, String feedback, Integer userID, Long toolSessionID,
     * Boolean markedInGradebook) {
     * ToolSession toolSession = toolService.getToolSessionById(toolSessionID);
     * User learner = (User) userService.findById(User.class, userID);
     * if ((learner != null) && (toolSession != null)) {
     * ToolActivity activity = toolSession.getToolActivity();
     * GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(activity.getActivityId(), userID);
     * 
     * // If gradebook user activity is null or the mark is set by teacher or was set previously by user - save the
     * // mark and feedback
     * if ((gradebookUserActivity == null) || markedInGradebook || !gradebookUserActivity.getMarkedInGradebook()) {
     * updateGradebookUserActivityMark(toolSession.getLesson(), learner, activity, mark, markedInGradebook,
     * false);
     * updateUserActivityGradebookFeedback(activity, learner, feedback);
     * }
     * }
     * }
     */

    @Override
    public void removeActivityMark(Integer userID, Long toolSessionID) {
	ToolSession toolSession = toolService.getToolSessionById(toolSessionID);
	User learner = (User) userService.findById(User.class, userID);
	if ((learner != null) && (toolSession != null)) {
	    ToolActivity activity = toolSession.getToolActivity();
	    GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(activity.getActivityId(), userID);
	    if (gradebookUserActivity != null) {
		removeActivityMark(activity, gradebookUserActivity);
	    }
	}
    }

    @Override
    public void removeActivityMark(Long toolContentID) {
	Activity activity = activityDAO.getToolActivityByToolContentId(toolContentID);
	List<GradebookUserActivity> userActivities = getGradebookUserActivities(activity.getActivityId());
	for (GradebookUserActivity gradebookUserActivity : userActivities) {
	    removeActivityMark(activity, gradebookUserActivity);
	}
    }

    private void removeActivityMark(Activity activity, GradebookUserActivity gradebookUserActivity) {
	// set mark to null so lesson mark recalculates correctly
	Double oldActivityMark = gradebookUserActivity.getMark();
	gradebookUserActivity.setMark(null);
	Lesson lesson = activity.getLearningDesign().getLessons().iterator().next();
	boolean isWeightedMarks = toolService.isWeightedMarks(lesson.getLearningDesign());
	GradebookUserLesson gradebookUserLesson = getGradebookUserLesson(lesson.getLessonId(),
		gradebookUserActivity.getLearner().getUserId());
	aggregateTotalMarkForLesson(isWeightedMarks, gradebookUserLesson, gradebookUserActivity, oldActivityMark);

	// finally completely remove the activity mark
	gradebookDAO.delete(gradebookUserActivity);
    }

    @Override
    public Activity getActivityById(Long activityID) {
	return activityDAO.getActivityByActivityId(activityID);
    }

    @Override
    public void removeLearnerFromLesson(Long lessonId, Integer learnerId) {
	if (logger.isDebugEnabled()) {
	    logger.debug(
		    "Removing activity and lesson entries for learner ID " + learnerId + " and lesson ID " + lessonId);
	}
	Lesson lesson = lessonService.getLesson(lessonId);
	List<ToolActivity> activities = getLessonActivitiesForLearner(lesson, learnerId);
	for (ToolActivity activity : activities) {
	    GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(activity.getActivityId(), learnerId);
	    if (gradebookUserActivity != null) {
		gradebookDAO.delete(gradebookUserActivity);
	    }
	}
	GradebookUserLesson gradebookUserLesson = getGradebookUserLesson(lesson.getLessonId(), learnerId);
	if (gradebookUserLesson != null) {
	    gradebookDAO.delete(gradebookUserLesson);
	}
    }

    @Override
    public void archiveLearnerMarks(Long lessonId, Integer learnerId, Date archiveDate) {
	if (logger.isDebugEnabled()) {
	    logger.debug("Archiving activity and lesson entries for learner ID " + learnerId + " and lesson ID "
		    + lessonId + " with archive date " + archiveDate);
	}
	Lesson lesson = lessonService.getLesson(lessonId);
	List<ToolActivity> activities = getLessonActivitiesForLearner(lesson, learnerId);
	for (ToolActivity activity : activities) {
	    GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(activity.getActivityId(), learnerId);
	    if (gradebookUserActivity != null) {
		gradebookDAO.insert(new GradebookUserActivityArchive(gradebookUserActivity, archiveDate));
	    }
	}
	GradebookUserLesson gradebookUserLesson = getGradebookUserLesson(lesson.getLessonId(), learnerId);
	if (gradebookUserLesson != null) {
	    gradebookDAO.insert(new GradebookUserLessonArchive(gradebookUserLesson, archiveDate));
	}
    }

    /**
     * Returns a completely flat list of lesson activities for the whole lesson.
     */
    private List<ToolActivity> getLessonToolActivitiesForLesson(Lesson lesson) {
	List<ToolActivity> toolActivities = new ArrayList<>();
	List<ActivityURL> activityUrls = getLearnerService().getStructuredActivityURLs(lesson.getLessonId());
	for (ActivityURL activityUrl : activityUrls) {
	    processLessonToolActivity(activityUrl, toolActivities);
	}
	return toolActivities;
    }

    private void processLessonToolActivity(ActivityURL activityUrl, List<ToolActivity> toolActivities) {
	Activity activity = activityDAO.getActivityByActivityId(activityUrl.getActivityId());
	if (activity instanceof ToolActivity) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    toolActivities.add(toolActivity);
	} else if (activity instanceof ComplexActivity) {
	    for (ActivityURL childUrl : activityUrl.getChildActivities()) {
		processLessonToolActivity(childUrl, toolActivities);
	    }
	}
    }

    /**
     * Returns a list of lesson activities made up of tool activities and sequence activities for the whole lesson.
     * The sequence activities allow the export to tweak the learner out.
     */
    private List<Activity> getLessonActivitiesForLesson(Lesson lesson) {
	List<Activity> activities = new ArrayList<>();
	List<ActivityURL> activityUrls = getLearnerService().getStructuredActivityURLs(lesson.getLessonId());
	for (ActivityURL activityUrl : activityUrls) {
	    processLessonActivity(activityUrl, activities);
	}
	return activities;
    }

    private void processLessonActivity(ActivityURL activityUrl, List<Activity> activities) {
	Activity activity = activityDAO.getActivityByActivityId(activityUrl.getActivityId());
	if (activity instanceof ToolActivity || activity instanceof SequenceActivity) {
	    activities.add(activity);
	} else if (activity instanceof ComplexActivity) {
	    for (ActivityURL childUrl : activityUrl.getChildActivities()) {
		processLessonActivity(childUrl, activities);
	    }
	}
    }

    /**
     * Returns lesson activities for a particular user
     */
    @SuppressWarnings("unchecked")
    private List<ToolActivity> getLessonActivitiesForLearner(Lesson lesson, Integer learnerId) {
	List<ToolActivity> toolActivities = new ArrayList<>();
	Object[] objs = getLearnerService().getStructuredActivityURLs(learnerId, lesson.getLessonId());
	// will be null if learner has not started the lesson.
	if (objs != null) {
	    List<ActivityURL> activityUrls = (List<ActivityURL>) objs[0];
	    for (ActivityURL activityUrl : activityUrls) {
		processLearnerActivity(activityUrl, toolActivities, false);
	    }
	}

	return toolActivities;
    }

    private void processLearnerActivity(ActivityURL activityUrl, List<ToolActivity> toolActivities,
	    boolean includeOnlyAttemptedCompleted) {
	Activity activity = activityDAO.getActivityByActivityId(activityUrl.getActivityId());
	if (activity instanceof ToolActivity) {
	    if (!includeOnlyAttemptedCompleted || activityUrl.getStatus() != LearnerProgress.ACTIVITY_NOT_ATTEMPTED) {
		ToolActivity toolActivity = (ToolActivity) activity;
		toolActivities.add(toolActivity);
	    }
	} else if (activity instanceof ParallelActivity || activity instanceof FloatingActivity) {
	    for (ActivityURL childUrl : activityUrl.getChildActivities()) {
		processLearnerActivity(childUrl, toolActivities, false);
	    }
	} else if (activity instanceof OptionsActivity || activity instanceof SequenceActivity) {
	    for (ActivityURL childUrl : activityUrl.getChildActivities()) {
		processLearnerActivity(childUrl, toolActivities, true);
	    }
	}
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
	if (originalStatus.contains("check")) {
	    status = getMessage("gradebook.exportcourse.ok");
	} else if (originalStatus.contains("cog")) {
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
    private void aggregateTotalMarkForLesson(boolean useWeightings, GradebookUserLesson gradebookUserLesson,
	    GradebookUserActivity markedActivity, Double oldActivityMark) {
	List<GradebookUserActivity> userActivities = gradebookDAO.getGradebookUserActivitiesForLesson(
		gradebookUserLesson.getLesson().getLessonId(), gradebookUserLesson.getLearner().getUserId());
	// if there is only one marked activity and it is being deleted, remove whole lesson mark
	if (markedActivity.getMark() == null && userActivities.size() == 1
		&& userActivities.get(0).getUid() == markedActivity.getUid()) {
	    gradebookDAO.delete(gradebookUserLesson);
	    return;
	}

	Double totalMark = null;
	if (oldActivityMark == null || gradebookUserLesson.getMark() == null) {
	    totalMark = calculateLessonMark(useWeightings, userActivities, markedActivity);
	} else if (!useWeightings) {
	    totalMark = gradebookUserLesson.getMark() - oldActivityMark;
	    if (markedActivity != null && markedActivity.getMark() != null) {
		totalMark += markedActivity.getMark();
	    }
	} else {
	    Double oldWeightedMark = getWeightedMark(true, markedActivity, oldActivityMark);
	    Double newWeighterMark = markedActivity.getMark() == null ? 0
		    : getWeightedMark(true, markedActivity, markedActivity.getMark());
	    totalMark = gradebookUserLesson.getMark() - oldWeightedMark + newWeighterMark;
	}

	gradebookUserLesson.setMark(totalMark);
	gradebookDAO.insertOrUpdate(gradebookUserLesson);
    }

    private Double calculateLessonMark(boolean useWeightings, List<GradebookUserActivity> userActivities,
	    GradebookUserActivity markedActivity) {

	Double totalMark = markedActivity != null
		? getWeightedMark(useWeightings, markedActivity, markedActivity.getMark())
		: 0.0;

	for (GradebookUserActivity guact : userActivities) {
	    if (markedActivity == null || guact.getUid() != markedActivity.getUid()) {
		if (useWeightings) {
		    totalMark = totalMark + getWeightedMark(useWeightings, guact, guact.getMark());
		} else {
		    totalMark = totalMark + guact.getMark();
		}
	    }
	}
	return totalMark;
    }

    private Double getWeightedMark(boolean useWeightings, GradebookUserActivity guact, Double inputRawMark) {
	Double rawMark = inputRawMark != null ? inputRawMark : 0.0;
	if (useWeightings) {
	    ToolActivity activity = guact.getActivity();
	    if (activity.getEvaluation() == null || activity.getEvaluation().getWeight() == null) {
		return 0.0;
	    } else {
		return doWeightedMarkCalc(rawMark, activity, activity.getEvaluation().getWeight(),
			toolService.getActivityMaxPossibleMark(activity));
	    }
	}
	return rawMark;
    }

    // activity details are user only for the error message.
    private Double doWeightedMarkCalc(Double rawMark, ToolActivity activity, Integer weight, Long maxMark) {
	if (maxMark == null) {
	    logger.error(new StringBuilder(
		    "Unable to correctly calculate weighted mark as no maximum mark is known for activity \"")
			    .append(activity.getTitle()).append("\" (").append(activity.getActivityId())
			    .append("). This activity will be skipped.").toString());
	    return 0.0;
	}
	if (maxMark == 0) {
	    return 0.0;
	}
	return rawMark / maxMark * weight;
    }

    /**
     * Gets the GBActivityGridRowDTO fro a given activity and lesson. Only set escapeTitles to false if the
     * output is *not* going to a webpage, but is instead going to a spreadsheet.
     *
     * @param activity
     * @param lesson
     * @return
     */
    private GBActivityGridRowDTO getGradebookActivityDTO(ToolActivity activity, Lesson lesson, String groupName,
	    Long groupId, boolean escapeTitles) {

	GBActivityGridRowDTO activityDTO = new GBActivityGridRowDTO(activity, groupName, groupId, escapeTitles);
	if ((groupName != null) && (groupId != null)) {

	    // Setting averages for group
	    activityDTO
		    .setAverageMark(gradebookDAO.getAverageMarkForGroupedActivity(activity.getActivityId(), groupId));
	    activityDTO.setMedianTimeTaken(
		    gradebookDAO.getMedianTimeTakenForGroupedActivity(activity.getActivityId(), groupId));
	    activityDTO
		    .setMinTimeTaken(gradebookDAO.getMinTimeTakenForGroupedActivity(activity.getActivityId(), groupId));
	    activityDTO
		    .setMaxTimeTaken(gradebookDAO.getMaxTimeTakenForGroupedActivity(activity.getActivityId(), groupId));

	} else {
	    // Setting averages for lesson
	    activityDTO.setAverageMark(gradebookDAO.getAverageMarkForActivity(activity.getActivityId()));
	    activityDTO.setMedianTimeTaken(gradebookDAO.getMedianTimeTakenForActivity(activity.getActivityId()));
	    activityDTO.setMinTimeTaken(gradebookDAO.getMinTimeTakenForActivity(activity.getActivityId()));
	    activityDTO.setMaxTimeTaken(gradebookDAO.getMaxTimeTakenForActivity(activity.getActivityId()));

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
    private Date getActivityStartDate(Object learnerProgress, Activity activity, TimeZone timeZone) {
	Date startDate = null;
	if (learnerProgress != null) {

	    // this construct looks bad but see LDEV-4609 commit for explanation
	    if (learnerProgress instanceof LearnerProgressArchive) {
		startDate = ((LearnerProgressArchive) learnerProgress).getAttemptedActivities().get(activity);
	    } else {
		startDate = ((LearnerProgress) learnerProgress).getAttemptedActivities().get(activity);
	    }
	    if (startDate == null) {
		if (learnerProgress instanceof LearnerProgressArchive) {
		    CompletedActivityProgressArchive compProg = ((LearnerProgressArchive) learnerProgress)
			    .getCompletedActivities().get(activity);
		    if (compProg != null) {
			startDate = compProg.getStartDate();
		    }
		} else {
		    CompletedActivityProgress compProg = ((LearnerProgress) learnerProgress).getCompletedActivities()
			    .get(activity);
		    if (compProg != null) {
			startDate = compProg.getStartDate();
		    }
		}

	    }
	}

	if (startDate != null) {
	    if (timeZone == null) {
		logger.warn("No user time zone provided, leaving server default");
	    } else {
		if (logger.isTraceEnabled()) {
		    logger.trace("Adjusting time according to zone \"" + timeZone + "\"");
		}
		startDate = DateUtil.convertToTimeZoneFromDefault(timeZone, startDate);
	    }
	}
	return startDate;
    }

    /**
     * Gets completed activity finish time adjusted to monitor time zone.
     *
     * @param learnerProgress
     * @param activity
     * @param timeZone
     * @return
     */
    private Date getActivityFinishDate(Object learnerProgress, Activity activity, TimeZone timeZone) {
	Date finishDate = null;
	if (learnerProgress != null) {
	    // this construct looks bad but see LDEV-4609 commit for explanation
	    if (learnerProgress instanceof LearnerProgressArchive) {
		CompletedActivityProgressArchive compProg = ((LearnerProgressArchive) learnerProgress)
			.getCompletedActivities().get(activity);
		if (compProg != null) {
		    finishDate = compProg.getFinishDate();
		}
	    } else {
		CompletedActivityProgress compProg = ((LearnerProgress) learnerProgress).getCompletedActivities()
			.get(activity);
		if (compProg != null) {
		    finishDate = compProg.getFinishDate();
		}
	    }
	}

	if (finishDate != null) {
	    if (timeZone == null) {
		logger.warn("No user time zone provided, leaving server default");
	    } else {
		if (logger.isTraceEnabled()) {
		    logger.trace("Adjusting time according to zone \"" + timeZone + "\"");
		}
		finishDate = DateUtil.convertToTimeZoneFromDefault(timeZone, finishDate);
	    }
	}
	return finishDate;
    }

    private Long getActivityDuration(Object learnerProgress, Activity activity) {
	if (learnerProgress != null) {
	    // this construct looks bad but see LDEV-4609 commit for explanation
	    if (learnerProgress instanceof LearnerProgressArchive) {
		CompletedActivityProgressArchive compProg = ((LearnerProgressArchive) learnerProgress)
			.getCompletedActivities().get(activity);
		if (compProg != null) {
		    Date startTime = compProg.getStartDate();
		    Date endTime = compProg.getFinishDate();
		    if ((startTime != null) && (endTime != null)) {
			return endTime.getTime() - startTime.getTime();
		    }
		}
	    } else {
		CompletedActivityProgress compProg = ((LearnerProgress) learnerProgress).getCompletedActivities()
			.get(activity);
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
		status = "<i class='fa fa-check text-success'></i>";

	    } else if ((learnerProgress.getAttemptedActivities() != null)
		    && (learnerProgress.getAttemptedActivities().size() > 0)) {

		String currentActivityTitle = learnerProgress.getCurrentActivity() == null ? ""
			: HtmlUtils.htmlEscape(learnerProgress.getCurrentActivity().getTitle());
		status = "<i class='fa fa-cog' title='" + currentActivityTitle + "'></i>";
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
    private String getActivityStatusStr(Object learnerProgress, Activity activity) {

	final String IMAGES_DIR = Configuration.get(ConfigurationKeys.SERVER_URL) + "images";
	if (learnerProgress != null) {
	    // this construct looks bad but see LDEV-4609 commit for explanation
	    byte statusByte = learnerProgress instanceof LearnerProgressArchive
		    ? ((LearnerProgressArchive) learnerProgress).getProgressState(activity)
		    : ((LearnerProgress) learnerProgress).getProgressState(activity);
	    Activity currentActivity = learnerProgress instanceof LearnerProgressArchive
		    ? ((LearnerProgressArchive) learnerProgress).getCurrentActivity()
		    : ((LearnerProgress) learnerProgress).getCurrentActivity();
	    if (statusByte == LearnerProgress.ACTIVITY_ATTEMPTED && currentActivity != null) {
		return "<i class='fa fa-cog' title='" + HtmlUtils.htmlEscape(currentActivity.getTitle()) + "'></i>";
	    } else if (statusByte == LearnerProgress.ACTIVITY_COMPLETED) {
		return "<i class='fa fa-check text-success'></i>";
	    }
	}
	return "-";
    }

    /**
     * Returns map containing (userId -> LearnerProgressMap) pairs. It serves merely for optimizing amount of db
     * queries.
     *
     * @param learners
     *            if null - return all available pairs for the lesson
     */
    private Map<Integer, LearnerProgress> getUserToLearnerProgressMap(Lesson lesson, List<User> learners) {

	Map<Integer, LearnerProgress> map = new HashMap<>();
	if (lesson == null || learners != null && learners.isEmpty()) {
	    return map;
	}

	//get either all available learnerProgresses or only for specified users
	List<LearnerProgress> learnerProgresses;
	if (learners == null) {
	    learnerProgresses = lessonService.getUserProgressForLesson(lesson.getLessonId());
	} else {

	    List<Integer> userIds = new LinkedList<>();
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
    private Map<Integer, GradebookUserActivity> getUserToGradebookUserActivityMap(Activity activity,
	    List<User> learners) {

	Map<Integer, GradebookUserActivity> map = new HashMap<>();
	if (activity == null || learners != null && learners.isEmpty()) {
	    return map;
	}

	//get either all available learnerProgresses or only for specified users
	List<GradebookUserActivity> gradebookUserActivities;
	if (learners == null) {
	    gradebookUserActivities = gradebookDAO.getAllGradebookUserActivitiesForActivity(activity.getActivityId());
	} else {

	    List<Integer> userIds = new LinkedList<>();
	    for (User learner : learners) {
		userIds.add(learner.getUserId());
	    }

	    gradebookUserActivities = gradebookDAO.getGradebookUserActivitiesForActivity(activity.getActivityId(),
		    userIds);
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

	Map<Long, Long> map = new HashMap<>();
	for (ToolActivity activity : activities) {
	    map.put(activity.getActivityId(), toolService.getActivityMaxPossibleMark(activity));
	}

	return map;
    }

    /**
     * Returns map containing (userId -> GradebookUserLesson) pairs. It serves merely for optimizing amount of db
     * queries.
     *
     * @param userIds
     *            if provided - return userLessons only for those users
     */
    private Map<Integer, GradebookUserLesson> getUserToGradebookUserLessonMap(Lesson lesson, List<User> learners) {

	Map<Integer, GradebookUserLesson> map = new HashMap<>();
	if (lesson == null || learners != null && learners.isEmpty()) {
	    return map;
	}

	//get either all available gradebookUserLessons or only for specified users
	List<GradebookUserLesson> gradebookUserLessons;
	if (learners == null) {
	    gradebookUserLessons = gradebookDAO.getGradebookUserLessons(lesson);
	} else {

	    List<Integer> userIds = new LinkedList<>();
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
    public List<Number> getMarksArray(Long lessonId) {
	return gradebookDAO.getAllMarksForLesson(lessonId);
    }

    private ILearnerService getLearnerService() {
	if (learnerService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    learnerService = (ILearnerService) ctx.getBean("learnerService");
	}
	return learnerService;
    }

    @Override
    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    private String getMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    // Getter and setter methods -----------------------------------------------

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setToolService(ILamsCoreToolService toolService) {
	this.toolService = toolService;
    }

    public void setGradebookDAO(IGradebookDAO gradebookDAO) {
	this.gradebookDAO = gradebookDAO;
    }

    public void setLearnerProgressDAO(ILearnerProgressDAO learnerProgressDAO) {
	this.learnerProgressDAO = learnerProgressDAO;
    }

    public void setLessonDAO(ILessonDAO lessonDAO) {
	this.lessonDAO = lessonDAO;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public void setUserService(IUserManagementService userService) {
	this.userService = userService;
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

    public void setOutcomeService(IOutcomeService outcomeService) {
	this.outcomeService = outcomeService;
    }

    public void setIntegrationService(IIntegrationService integrationService) {
	this.integrationService = integrationService;
    }
}