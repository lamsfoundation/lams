package org.lamsfoundation.lams.monitoring.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.monitoring.dto.PermissionGateDTO;
import org.lamsfoundation.lams.monitoring.dto.TblGroupDTO;
import org.lamsfoundation.lams.monitoring.dto.TblUserDTO;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Displays TBL monitor.
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/tblmonitor")
public class TblMonitoringController {
    private static Logger log = Logger.getLogger(TblMonitoringController.class);

    @Autowired
    private ILessonService lessonService;
    @Autowired
    private IMonitoringFullService monitoringService;
    @Autowired
    private ILamsToolService lamsToolService;
    @Autowired
    private IActivityDAO activityDAO;
    @Autowired
    private IGradebookService gradebookService;

    /**
     * Displays addStudent page.
     */
    @RequestMapping("/start")
    public String unspecified(HttpServletRequest request) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);
	request.setAttribute("lesson", lesson);
	request.setAttribute("totalLearnersNumber", lesson.getAllLearners().size());

	List<Activity> lessonActivities = getLessonActivities(lesson);
	setupAvailableActivityTypes(request, lessonActivities);
	return "tblmonitor/tblmonitor";
    }

    /**
     * Shows Teams page
     */
    @RequestMapping("/teams")
    public String teams(HttpServletRequest request) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);

	List<Activity> lessonActivities = getLessonActivities(lesson);
	setupAvailableActivityTypes(request, lessonActivities);
	boolean isScratchieAvailable = (request.getAttribute("isScratchieAvailable") != null)
		&& ((Boolean) request.getAttribute("isScratchieAvailable"));
	boolean isIraMcqAvailable = (request.getAttribute("isIraMcqAvailable") != null)
		&& ((Boolean) request.getAttribute("isIraMcqAvailable"));
	boolean isIraAssessmentAvailable = (request.getAttribute("isIraAssessmentAvailable") != null)
		&& ((Boolean) request.getAttribute("isIraAssessmentAvailable"));
	Long iraToolActivityId = request.getAttribute("iraToolActivityId") == null ? null
		: (Long) request.getAttribute("iraToolActivityId");
	Long traToolActivityId = request.getAttribute("traToolActivityId") == null ? null
		: (Long) request.getAttribute("traToolActivityId");
	Long leaderselectionToolActivityId = request.getAttribute("leaderselectionToolActivityId") == null ? null
		: (Long) request.getAttribute("leaderselectionToolActivityId");

	//get all mcq and assessment scores
	List<GradebookUserActivity> iraGradebookUserActivities = new LinkedList<>();
	List<GradebookUserActivity> traGradebookUserActivities = new LinkedList<>();
	if (isIraMcqAvailable || isIraAssessmentAvailable) {
	    iraGradebookUserActivities = gradebookService.getGradebookUserActivities(iraToolActivityId);
	}
	if (isScratchieAvailable) {
	    traGradebookUserActivities = gradebookService.getGradebookUserActivities(traToolActivityId);
	}

	Set<Long> leaderUserIds = leaderselectionToolActivityId == null ? new HashSet<>()
		: lamsToolService.getLeaderUserId(leaderselectionToolActivityId);

	GroupingActivity groupingActivity = getGroupingActivity(lesson);
	Grouping grouping = groupingActivity == null ? null : groupingActivity.getCreateGrouping();
	Set<Group> groups = grouping == null ? null : grouping.getGroups();

	Set<TblGroupDTO> groupDtos = new TreeSet<>();
	if (groups != null) {
	    for (Group group : groups) {
		TblGroupDTO groupDto = new TblGroupDTO(group);
		groupDtos.add(groupDto);

		if (group.getUsers() != null) {
		    for (User user : group.getUsers()) {
			TblUserDTO userDto = new TblUserDTO(user.getUserDTO());
			groupDto.getUserList().add(userDto);

			//set up all user leaders
			if (leaderUserIds.contains(new Long(user.getUserId()))) {
			    userDto.setGroupLeader(true);
			    groupDto.setGroupLeader(userDto);
			}

			if (isIraMcqAvailable || isIraAssessmentAvailable) {
			    //find according iraGradebookUserActivity
			    for (GradebookUserActivity iraGradebookUserActivity : iraGradebookUserActivities) {
				if (iraGradebookUserActivity.getLearner().getUserId().equals(user.getUserId())) {
				    userDto.setIraScore(iraGradebookUserActivity.getMark());
				    break;
				}
			    }
			}

			if (isScratchieAvailable) {
			    //find according traGradebookUserActivity
			    for (GradebookUserActivity traGradebookUserActivity : traGradebookUserActivities) {
				if (traGradebookUserActivity.getLearner().getUserId().equals(user.getUserId())) {
				    //we set traScore multiple times, but it's doesn't matter
				    groupDto.setTraScore(traGradebookUserActivity.getMark());
				    break;
				}
			    }
			}
		    }
		}
	    }
	}
	request.setAttribute("groupDtos", groupDtos);

	return "tblmonitor/teams";
    }

    /**
     * Shows Gates page
     */
    @RequestMapping("/gates")
    public String gates(HttpServletRequest request) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	List<PermissionGateDTO> permissionGates = new ArrayList<>();

	List<ContributeActivityDTO> contributeActivities = monitoringService.getAllContributeActivityDTO(lessonId);
	if (contributeActivities != null) {
	    for (ContributeActivityDTO contributeActivity : contributeActivities) {

		if (contributeActivity.getContributeEntries() != null) {

		    //check if there is any persmission gates entries
		    for (ContributeActivityDTO.ContributeEntry contributeEntry : contributeActivity
			    .getContributeEntries()) {
			if (ContributionTypes.PERMISSION_GATE.equals(contributeEntry.getContributionType())) {

			    Long activityId = contributeActivity.getActivityID();
			    Activity activity = monitoringService.getActivityById(activityId);
			    PermissionGateDTO gateDto = new PermissionGateDTO((PermissionGateActivity) activity);

			    gateDto.setUrl(contributeEntry.getURL());
			    gateDto.setComplete(contributeEntry.getIsComplete());

			    int waitingLearnersCount = lessonService.getCountLearnersHaveAttemptedActivity(activity);
			    gateDto.setWaitingLearnersCount(waitingLearnersCount);

			    permissionGates.add(gateDto);
			    break;
			}
		    }
		}
	    }
	}

	request.setAttribute("permissionGates", permissionGates);
	return "tblmonitor/gates";
    }

    /**
     * Shows forum page
     */
    @RequestMapping("/forum")
    public String forum(HttpServletRequest request) {
	long forumActivityId = WebUtil.readLongParam(request, "activityId");
	ToolActivity forumActivity = (ToolActivity) monitoringService.getActivityById(forumActivityId);

	int attemptedLearnersNumber = lessonService.getCountLearnersHaveAttemptedActivity(forumActivity);
	request.setAttribute("attemptedLearnersNumber", attemptedLearnersNumber);

	Set<ToolSession> toolSessions = forumActivity.getToolSessions();
	request.setAttribute("toolSessions", toolSessions);

	return "tblmonitor/forum";
    }

    /**
     * Shows peerreview page
     */
    @RequestMapping("/peerreview")
    public String peerreview(HttpServletRequest request) {
	long peerreviewActivityId = WebUtil.readLongParam(request, "activityId");
	ToolActivity peerreviewActivity = (ToolActivity) monitoringService.getActivityById(peerreviewActivityId);

	int attemptedLearnersNumber = lessonService.getCountLearnersHaveAttemptedActivity(peerreviewActivity);
	request.setAttribute("attemptedLearnersNumber", attemptedLearnersNumber);

	Set<ToolSession> toolSessions = peerreviewActivity.getToolSessions();
	request.setAttribute("toolSessions", toolSessions);

	return "tblmonitor/peerreview";
    }

    /**
     * Shows sequence diagram page
     */
    @RequestMapping("/sequence")
    public String sequence(HttpServletRequest request) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);
	request.setAttribute("lesson", lesson);
	return "tblmonitor/sequence";
    }

    /**
     * Returns lesson activities sorted by the learning design order.
     */
    private List<Activity> getLessonActivities(Lesson lesson) {
	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 *
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */
	Activity firstActivity = activityDAO
		.getActivityByActivityId(lesson.getLearningDesign().getFirstActivity().getActivityId());
	List<Activity> activities = new ArrayList<>();
	sortActivitiesByLearningDesignOrder(firstActivity, activities);

	return activities;
    }

    /**
     * Sort all activities by the learning design order.
     *
     * @param activity
     * @param sortedActivities
     */
    @SuppressWarnings("unchecked")
    private void sortActivitiesByLearningDesignOrder(Activity activity, List<Activity> sortedActivities) {
	sortedActivities.add(activity);

	//in case of branching activity - add all activities based on their orderId
	if (activity.isBranchingActivity()) {
	    BranchingActivity branchingActivity = (BranchingActivity) activity;
	    Set<SequenceActivity> sequenceActivities = new TreeSet<SequenceActivity>(new ActivityOrderComparator());
	    sequenceActivities.addAll((Set<SequenceActivity>) (Set<?>) branchingActivity.getActivities());
	    for (Activity sequenceActivityNotInitialized : sequenceActivities) {
		SequenceActivity sequenceActivity = (SequenceActivity) monitoringService
			.getActivityById(sequenceActivityNotInitialized.getActivityId());
		Set<Activity> childActivities = new TreeSet<Activity>(new ActivityOrderComparator());
		childActivities.addAll(sequenceActivity.getActivities());

		//add one by one in order to initialize all activities
		for (Activity childActivity : childActivities) {
		    Activity activityInit = monitoringService.getActivityById(childActivity.getActivityId());
		    sortedActivities.add(activityInit);
		}
	    }

	    // In case of complex activity (parallel, help or optional activity) add all its children activities.
	    // They will be sorted by orderId
	} else if (activity.isComplexActivity()) {
	    ComplexActivity complexActivity = (ComplexActivity) activity;
	    Set<Activity> childActivities = new TreeSet<Activity>(new ActivityOrderComparator());
	    childActivities.addAll(complexActivity.getActivities());

	    // add one by one in order to initialize all activities
	    for (Activity childActivity : childActivities) {
		Activity activityInit = monitoringService.getActivityById(childActivity.getActivityId());
		sortedActivities.add(activityInit);
	    }
	}

	Transition transitionFrom = activity.getTransitionFrom();
	if (transitionFrom != null) {
	    // query activity from DB as transition holds only proxied activity object
	    Long nextActivityId = transitionFrom.getToActivity().getActivityId();
	    Activity nextActivity = monitoringService.getActivityById(nextActivityId);

	    sortActivitiesByLearningDesignOrder(nextActivity, sortedActivities);
	}
    }

    private GroupingActivity getGroupingActivity(Lesson lesson) {
	Set<Activity> activities = new TreeSet<>();

	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 *
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */
	Activity firstActivity = monitoringService
		.getActivityById(lesson.getLearningDesign().getFirstActivity().getActivityId());
	activities.add(firstActivity);
	activities.addAll(lesson.getLearningDesign().getActivities());

	for (Activity activity : activities) {
	    if (activity instanceof GroupingActivity) {
		return (GroupingActivity) activity;
	    }
	}

	return null;
    }

    private void setupAvailableActivityTypes(HttpServletRequest request, List<Activity> activities) {
	//check if there is Scratchie activity. It's used only in case of LKC TBL monitoring, when all assessment are treated as AEs
	boolean isScratchieAvailable = false;
	for (Activity activity : activities) {
	    if (activity instanceof ToolActivity) {
		ToolActivity toolActivity = (ToolActivity) activity;
		String toolSignature = toolActivity.getTool().getToolSignature();
		if (CommonConstants.TOOL_SIGNATURE_SCRATCHIE.equals(toolSignature)) {
		    isScratchieAvailable = true;
		    break;
		}
	    }
	}

	boolean scratchiePassed = false;
	boolean iraPassed = false;
	String assessmentToolContentIds = "";
	String assessmentActivityTitles = "";
	for (Activity activity : activities) {
	    if (activity instanceof ToolActivity) {
		ToolActivity toolActivity = (ToolActivity) activity;
		String toolSignature = toolActivity.getTool().getToolSignature();
		Long toolContentId = toolActivity.getToolContentId();
		Long toolActivityId = toolActivity.getActivityId();
		String toolTitle = toolActivity.getTitle();

		//count only the first MCQ or Assessmnet as iRA
		if (!iraPassed && (CommonConstants.TOOL_SIGNATURE_MCQ.equals(toolSignature)
			|| isScratchieAvailable && CommonConstants.TOOL_SIGNATURE_ASSESSMENT.equals(toolSignature))) {
		    iraPassed = true;
		    if (CommonConstants.TOOL_SIGNATURE_MCQ.equals(toolSignature)) {
			request.setAttribute("isIraMcqAvailable", true);

		    } else {
			request.setAttribute("isIraAssessmentAvailable", true);
		    }
		    request.setAttribute("iraToolContentId", toolContentId);
		    request.setAttribute("iraToolActivityId", toolActivityId);

		    continue;
		}

		//aes are counted only after Scratchie activity, or for LKC TBL monitoring
		if ((scratchiePassed || !isScratchieAvailable)
			&& CommonConstants.TOOL_SIGNATURE_ASSESSMENT.equals(toolSignature)) {
		    request.setAttribute("isAeAvailable", true);
		    //prepare assessment details to be passed to Assessment tool
		    assessmentToolContentIds += toolContentId + ",";
		    assessmentActivityTitles += toolTitle + "\\,";

		} else if (CommonConstants.TOOL_SIGNATURE_FORUM.equals(toolSignature)) {
		    request.setAttribute("isForumAvailable", true);
		    request.setAttribute("forumActivityId", toolActivityId);

		} else if (CommonConstants.TOOL_SIGNATURE_PEER_REVIEW.equals(toolSignature)) {
		    request.setAttribute("isPeerreviewAvailable", true);
		    request.setAttribute("peerreviewToolContentId", toolContentId);

		    //tRA is the first scratchie activity
		} else if (!scratchiePassed && CommonConstants.TOOL_SIGNATURE_SCRATCHIE.equals(toolSignature)) {
		    scratchiePassed = true;

		    request.setAttribute("isScratchieAvailable", true);
		    request.setAttribute("traToolContentId", toolContentId);
		    request.setAttribute("traToolActivityId", toolActivityId);
		}

		if (CommonConstants.TOOL_SIGNATURE_LEADERSELECTION.equals(toolSignature)) {
		    request.setAttribute("leaderselectionToolActivityId", toolActivityId);
		    request.setAttribute("leaderselectionToolContentId", toolContentId);
		}

	    } else if (activity instanceof GateActivity) {
		request.setAttribute("isGatesAvailable", true);
	    }
	}

	request.setAttribute("assessmentToolContentIds", assessmentToolContentIds);
	request.setAttribute("assessmentActivityTitles", assessmentActivityTitles);
    }
}