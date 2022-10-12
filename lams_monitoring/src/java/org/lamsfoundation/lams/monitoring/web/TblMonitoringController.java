package org.lamsfoundation.lams.monitoring.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.qb.dto.QbAnswersForOptionDTO;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ICommonAssessmentService;
import org.lamsfoundation.lams.tool.service.ICommonScratchieService;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

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
    private IQbService qbService;
    @Autowired
    private ILamsToolService lamsToolService;
    @Autowired
    private ILamsCoreToolService lamsCoreToolService;
    @Autowired
    private IActivityDAO activityDAO;
    @Autowired
    @Qualifier("laasseAssessmentService")
    private ICommonAssessmentService commonAssessmentService;
    @Autowired
    @Qualifier("scratchieService")
    private ICommonScratchieService commonScratchieService;

    /**
     * Shows Teams page
     */
    @RequestMapping("/teams")
    public String teams(HttpServletRequest request) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);

	List<Activity> lessonActivities = getLessonActivities(lesson);
	TblMonitoringController.setupAvailableActivityTypes(request, lessonActivities);
	boolean isTraAvailable = (request.getAttribute("isScratchieAvailable") != null)
		&& ((Boolean) request.getAttribute("isScratchieAvailable"));
	boolean isIraAvailable = request.getAttribute("isIraAvailable") != null
		&& ((Boolean) request.getAttribute("isIraAvailable"));
	Long iraToolActivityId = request.getAttribute("iraToolActivityId") == null ? null
		: (Long) request.getAttribute("iraToolActivityId");
	Long traToolActivityId = request.getAttribute("traToolActivityId") == null ? null
		: (Long) request.getAttribute("traToolActivityId");
	Long leaderselectionToolActivityId = request.getAttribute("leaderselectionToolActivityId") == null ? null
		: (Long) request.getAttribute("leaderselectionToolActivityId");
	Long iraToolContentId = isIraAvailable
		? activityDAO.find(ToolActivity.class, iraToolActivityId).getToolContentId()
		: null;
	Long traToolContentId = isTraAvailable
		? activityDAO.find(ToolActivity.class, traToolActivityId).getToolContentId()
		: null;

	Set<Long> leaderUserIds = null;
	if (leaderselectionToolActivityId != null) {
	    leaderUserIds = lamsToolService.getLeaderUserId(leaderselectionToolActivityId);
	    ToolActivity leaderSelection = activityDAO.find(ToolActivity.class, leaderselectionToolActivityId);
	    request.setAttribute("leaderSelectionToolContentId", leaderSelection.getToolContentId());
	} else {
	    leaderUserIds = new HashSet<>();
	}

	GroupingActivity groupingActivity = getGroupingActivity(lesson);

	String groupsSetupUrl = lamsCoreToolService.getToolContributionURL(lessonId, groupingActivity);
	request.setAttribute("groupsSetupUrl", groupsSetupUrl);

	Grouping grouping = groupingActivity == null ? null : groupingActivity.getCreateGrouping();
	Set<Group> groups = grouping == null ? null : grouping.getGroups();

	Map<Integer, Integer> iraCorrectAnswerCountByUser = Map.of();
	if (isIraAvailable) {
	    iraCorrectAnswerCountByUser = commonAssessmentService.countCorrectAnswers(iraToolContentId);
	}

	Set<TblGroupDTO> groupDtos = new TreeSet<>();
	for (Group group : groups) {
	    TblGroupDTO groupDto = new TblGroupDTO(group);
	    groupDtos.add(groupDto);

	    if (group.getUsers() == null) {
		continue;
	    }

	    for (User user : group.getUsers()) {
		TblUserDTO userDto = new TblUserDTO(user.getUserDTO());
		groupDto.getUserList().add(userDto);

		//set up all user leaders
		if (leaderUserIds.contains(user.getUserId().longValue())) {
		    userDto.setGroupLeader(true);
		    groupDto.setGroupLeader(userDto);
		}

		Integer correctAnswerCount = iraCorrectAnswerCountByUser.get(userDto.getUserID());
		if (correctAnswerCount != null) {
		    userDto.setIraCorrectAnswerCount(correctAnswerCount);
		}
	    }

	    if (isTraAvailable && groupDto.getGroupLeader() != null) {
		Integer correctAnswerCount = commonScratchieService.countCorrectAnswers(traToolContentId,
			groupDto.getGroupLeader().getUserID());
		if (correctAnswerCount != null) {
		    groupDto.setTraCorrectAnswerCount(correctAnswerCount);

		    for (TblUserDTO userDto : groupDto.getUserList()) {
			userDto.setTraCorrectAnswerCount(correctAnswerCount);
		    }
		}
	    }
	}
	request.setAttribute("groupDtos", groupDtos);

	double highestIraCorrectAnswerCountAverage = 0;
	double lowestIraCorrectAnswerCountAverage = Double.MAX_VALUE;
	int highestTraCorrectAnswerCount = 0;
	int lowestTraCorrectAnswerCount = Integer.MAX_VALUE;
	long highestСorrectAnswerCountDelta = Long.MIN_VALUE;
	long lowestСorrectAnswerCountDelta = Long.MAX_VALUE;

	int iraGroupsCount = 0;
	int traGroupsCount = 0;
	int deltaCount = 0;
	double iraCorrectAnswerCountAverageSum = 0;
	int traCorrectAnswerSum = 0;
	int deltaSum = 0;

	ArrayNode chartIraDataset = JsonNodeFactory.instance.arrayNode();
	ArrayNode chartTraDataset = JsonNodeFactory.instance.arrayNode();
	ArrayNode chartNamesDataset = JsonNodeFactory.instance.arrayNode();

	for (TblGroupDTO group : groupDtos) {
	    Double iraCorrectAnswerCountAverage = group.getIraCorrectAnswerCountAverage();
	    if (iraCorrectAnswerCountAverage != null) {
		iraCorrectAnswerCountAverageSum += iraCorrectAnswerCountAverage;
		iraGroupsCount++;

		if (iraCorrectAnswerCountAverage > highestIraCorrectAnswerCountAverage) {
		    highestIraCorrectAnswerCountAverage = iraCorrectAnswerCountAverage;
		}
		if (iraCorrectAnswerCountAverage < lowestIraCorrectAnswerCountAverage) {
		    lowestIraCorrectAnswerCountAverage = iraCorrectAnswerCountAverage;
		}
	    }

	    Integer traCorrectAnswerCount = group.getTraCorrectAnswerCount();

	    if (traCorrectAnswerCount != null) {
		traCorrectAnswerSum += traCorrectAnswerCount;
		traGroupsCount++;

		if (traCorrectAnswerCount > highestTraCorrectAnswerCount) {
		    highestTraCorrectAnswerCount = traCorrectAnswerCount;
		}
		if (traCorrectAnswerCount < lowestTraCorrectAnswerCount) {
		    lowestTraCorrectAnswerCount = traCorrectAnswerCount;
		}

		if (iraCorrectAnswerCountAverage != null) {
		    chartIraDataset
			    .add(NumberUtil.formatLocalisedNumber(iraCorrectAnswerCountAverage, (Locale) null, 2));
		    chartTraDataset.add(traCorrectAnswerCount);
		    chartNamesDataset.add(group.getGroupName());

		    long correctAnswerCountPercentDelta = iraCorrectAnswerCountAverage.equals(0d)
			    ? traCorrectAnswerCount * 100
			    : Math.round((traCorrectAnswerCount - iraCorrectAnswerCountAverage) * 100
				    / iraCorrectAnswerCountAverage);
		    group.setCorrectAnswerCountPercentDelta(correctAnswerCountPercentDelta);
		    deltaSum += correctAnswerCountPercentDelta;
		    deltaCount++;

		    if (correctAnswerCountPercentDelta > highestСorrectAnswerCountDelta) {
			highestСorrectAnswerCountDelta = correctAnswerCountPercentDelta;
		    }
		    if (correctAnswerCountPercentDelta < lowestСorrectAnswerCountDelta) {
			lowestСorrectAnswerCountDelta = correctAnswerCountPercentDelta;
		    }
		}
	    }

	}

	if (iraGroupsCount > 1) {
	    request.setAttribute("highestIraCorrectAnswerCountAverage", highestIraCorrectAnswerCountAverage);
	    request.setAttribute("lowestIraCorrectAnswerCountAverage", lowestIraCorrectAnswerCountAverage);
	    request.setAttribute("averageIraCorrectAnswerCountAverage",
		    iraCorrectAnswerCountAverageSum / iraGroupsCount);
	}
	if (traGroupsCount > 1) {
	    request.setAttribute("highestTraCorrectAnswerCount", highestTraCorrectAnswerCount);
	    request.setAttribute("lowestTraCorrectAnswerCount", lowestTraCorrectAnswerCount);
	    request.setAttribute("averageTraCorrectAnswerCount", (double) traCorrectAnswerSum / traGroupsCount);
	}

	if (deltaCount > 1) {
	    request.setAttribute("highestCorrectAnswerCountDelta", highestСorrectAnswerCountDelta);
	    request.setAttribute("lowestCorrectAnswerCountDelta", lowestСorrectAnswerCountDelta);
	    request.setAttribute("averageCorrectAnswerCountDelta", (double) deltaSum / deltaCount);
	}

	if (iraGroupsCount > 0 && traGroupsCount > 0) {
	    request.setAttribute("chartIraDataset", chartIraDataset.toString());
	    request.setAttribute("chartTraDataset", chartTraDataset.toString());
	    request.setAttribute("chartNamesDataset", chartNamesDataset.toString());
	}

	return "tblmonitor/teams";
    }

    @GetMapping("/questionResults")
    public String getQuestionResults(@RequestParam(required = false) Long iraToolContentId,
	    @RequestParam(required = false) Long traToolContentId, @RequestParam(required = false) String order,
	    Model model) {
	List<QbAnswersForOptionDTO> iraAnswers = null;
	if (iraToolContentId != null) {
	    // get answer percentage for each iRAT question and its options
	    iraAnswers = qbService.getAnswerCountForOptions(iraToolContentId);
	    model.addAttribute("iraAnswerCountForOptions", iraAnswers);
	}

	List<QbAnswersForOptionDTO> traAnswers = null;
	List<QbAnswersForOptionDTO> averageAnswers = null;
	if (traToolContentId != null) {
	    traAnswers = qbService.getAnswerCountForOptions(traToolContentId);
	    model.addAttribute("traAnswerCountForOptions", traAnswers);

	    // if both iRAT and tRAT are present, count average
	    if (iraAnswers != null) {
		averageAnswers = new LinkedList<>();
		model.addAttribute("averageAnswerCountForOptions", averageAnswers);

		for (int iraDtoIndex = 0; iraDtoIndex < iraAnswers.size(); iraDtoIndex++) {
		    QbAnswersForOptionDTO iraDto = iraAnswers.get(iraDtoIndex);
		    QbAnswersForOptionDTO averageDto = new QbAnswersForOptionDTO(iraDto.getQbQuestionUid(),
			    iraDto.getDisplayOrder());
		    averageAnswers.add(averageDto);

		    // there can be no answers for iRAT yet
		    boolean iraHasAnswers = iraDto.getCorrectAnswerPercent() >= 0;

		    QbAnswersForOptionDTO traDto = traAnswers.get(iraDtoIndex);
		    boolean traHasAnswers = traDto.getCorrectAnswerPercent() >= 0;
		    if (iraHasAnswers) {
			if (traHasAnswers) {
			    // both RATs have answers present, count average
			    averageDto.setCorrectAnswerPercent(
				    (iraDto.getCorrectAnswerPercent() + traDto.getCorrectAnswerPercent()) / 2);
			} else {
			    // just iRAT has answers
			    averageDto.setCorrectAnswerPercent(iraDto.getCorrectAnswerPercent());
			}
		    } else if (traHasAnswers) {
			// just tRAT has answers
			averageDto.setCorrectAnswerPercent(traDto.getCorrectAnswerPercent());
		    }

		    // count average for each option
		    for (int optionIndex = 0; optionIndex < iraDto.getOptionAnswerPercent().size(); optionIndex++) {
			int iraOptionAnswers = iraDto.getOptionAnswerPercent().get(optionIndex);
			int traOptionAnswers = traDto.getOptionAnswerPercent().get(optionIndex);

			if (iraHasAnswers) {
			    if (traHasAnswers) {
				averageDto.getOptionAnswerPercent().add((iraOptionAnswers + traOptionAnswers) / 2);
			    } else {
				averageDto.getOptionAnswerPercent().add(iraOptionAnswers);
			    }
			} else if (traHasAnswers) {
			    averageDto.getOptionAnswerPercent().add(traOptionAnswers);
			} else {
			    averageDto.getOptionAnswerPercent().add(-1);
			}
		    }
		}
	    }
	}

	List<QbAnswersForOptionDTO> sortSource = iraAnswers == null ? iraAnswers : traAnswers;
	List<List<QbAnswersForOptionDTO>> sortTargets = new LinkedList<>();

	if (StringUtils.isBlank(order)) {
	    order = "default";
	} else {
	    order = order.toLowerCase();
	}

	// find out by which column to sort
	if (iraAnswers != null && order.startsWith("ira")) {
	    iraAnswers.sort(Comparator.comparing(QbAnswersForOptionDTO::getCorrectAnswerPercent));
	    sortSource = iraAnswers;
	    // other collections need to have same sorting as well
	    if (traAnswers != null) {
		sortTargets.add(traAnswers);
		sortTargets.add(averageAnswers);
	    }
	} else if (traAnswers != null && order.startsWith("tra")) {
	    traAnswers.sort(Comparator.comparing(QbAnswersForOptionDTO::getCorrectAnswerPercent));

	    sortSource = traAnswers;
	    if (iraAnswers != null) {
		sortTargets.add(iraAnswers);
		sortTargets.add(averageAnswers);
	    }
	} else if (averageAnswers != null && order.startsWith("average")) {
	    averageAnswers.sort(Comparator.comparing(QbAnswersForOptionDTO::getCorrectAnswerPercent));
	    sortSource = averageAnswers;
	    sortTargets.add(traAnswers);
	    sortTargets.add(iraAnswers);
	}

	if (order.endsWith("reversed")) {
	    Collections.reverse(sortSource);
	}

	// fetch questions according to sort order
	Map<Integer, QbQuestion> questions = new LinkedHashMap<>();
	for (QbAnswersForOptionDTO dto : sortSource) {
	    QbQuestion question = qbService.getQuestionByUid(dto.getQbQuestionUid());
	    questions.put(dto.getDisplayOrder(), question);
	}

	// sort remaining collections according to sort order
	for (List<QbAnswersForOptionDTO> dtos : sortTargets) {
	    List<QbAnswersForOptionDTO> sortedTarget = new LinkedList<>();
	    for (QbAnswersForOptionDTO sourceDto : sortSource) {
		for (QbAnswersForOptionDTO targetDto : dtos) {
		    if (sourceDto.getDisplayOrder() == targetDto.getDisplayOrder()) {
			sortedTarget.add(targetDto);
			break;
		    }
		}
	    }
	    dtos.clear();
	    dtos.addAll(sortedTarget);
	}

	model.addAttribute("questions", questions);
	return "tblmonitor/teamsQuestionResults";
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

	int attemptedLearnersNumber = lessonService.getCountLearnersHaveAttemptedOrCompletedActivity(forumActivity);
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

	int attemptedLearnersNumber = lessonService
		.getCountLearnersHaveAttemptedOrCompletedActivity(peerreviewActivity);
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
     * Shows AEs page
     */
    @RequestMapping("/aes")
    public String aes(HttpServletRequest request, Model model) {
	String[] toolContentIds = request.getParameter("aeToolContentIds").split(",");
	String[] toolTypes = request.getParameter("aeToolTypes").split(",");
	String[] activityTitles = request.getParameter("aeActivityTitles").split("\\,");

	model.addAttribute("aeToolContentIds", toolContentIds);
	model.addAttribute("aeToolTypes", toolTypes);
	model.addAttribute("aeActivityTitles", activityTitles);

	return "tblmonitor/aes";
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
	    Set<SequenceActivity> sequenceActivities = new TreeSet<>(new ActivityOrderComparator());
	    sequenceActivities.addAll((Set<SequenceActivity>) (Set<?>) branchingActivity.getActivities());
	    for (Activity sequenceActivityNotInitialized : sequenceActivities) {
		SequenceActivity sequenceActivity = (SequenceActivity) monitoringService
			.getActivityById(sequenceActivityNotInitialized.getActivityId());
		Set<Activity> childActivities = new TreeSet<>(new ActivityOrderComparator());
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
	    Set<Activity> childActivities = new TreeSet<>(new ActivityOrderComparator());
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

    public static void setupAvailableActivityTypes(HttpServletRequest request, List<Activity> activities) {
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
	String aeToolContentIds = "";
	String aeToolTypes = "";
	String aeActivityTitles = "";
	for (Activity activity : activities) {
	    if (activity instanceof ToolActivity) {
		ToolActivity toolActivity = (ToolActivity) activity;
		String toolSignature = toolActivity.getTool().getToolSignature();
		Long toolContentId = toolActivity.getToolContentId();
		Long toolActivityId = toolActivity.getActivityId();
		String toolTitle = toolActivity.getTitle();

		//count only the first Assessmnet as iRA
		if (!iraPassed && isScratchieAvailable
			&& CommonConstants.TOOL_SIGNATURE_ASSESSMENT.equals(toolSignature)) {
		    iraPassed = true;
		    request.setAttribute("isIraAvailable", true);
		    request.setAttribute("iraToolContentId", toolContentId);
		    request.setAttribute("iraToolActivityId", toolActivityId);

		    continue;
		}

		//aes are counted only after Scratchie activity, or for LKC TBL monitoring
		if ((scratchiePassed || !isScratchieAvailable)
			&& (CommonConstants.TOOL_SIGNATURE_ASSESSMENT.equals(toolSignature)
				|| CommonConstants.TOOL_SIGNATURE_DOKU.equals(toolSignature))) {
		    request.setAttribute("isAeAvailable", true);
		    //prepare assessment details to be passed to Assessment tool
		    aeToolContentIds += toolContentId + ",";
		    aeToolTypes += CommonConstants.TOOL_SIGNATURE_DOKU.equals(toolSignature) ? "d," : "a,";
		    aeActivityTitles += toolTitle + "\\,";

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

	request.setAttribute("aeToolContentIds", aeToolContentIds);
	request.setAttribute("aeToolTypes", aeToolTypes);
	request.setAttribute("aeActivityTitles", aeActivityTitles);
    }
}