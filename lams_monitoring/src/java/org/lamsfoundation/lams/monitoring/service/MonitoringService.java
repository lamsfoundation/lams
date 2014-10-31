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

/* $$Id$$ */
package org.lamsfoundation.lams.monitoring.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.web.bean.GateActivityDTO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.util.LastNameAlphabeticComparator;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.AuditService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>
 * This is the major service facade for all monitoring functionalities. It is configured as a Spring factory bean so as
 * to utilize the Spring's IOC and declarative transaction management.
 * </p>
 * <p>
 * It needs to implement <code>ApplicationContextAware<code> interface 
 * because we need to load up tool's service dynamically according to the
 * selected learning design.
 * </p>
 * 
 * TODO Analyse the efficiency of the grouping algorithms for adding/removing users. Possible performance issue.
 * 
 * @author Jacky Fang
 * @author Manpreet Minhas
 * @since 2/02/2005
 * @version 1.1
 */
public class MonitoringService implements IMonitoringService, ApplicationContextAware {

    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(MonitoringService.class);

    private static final long numMilliSecondsInADay = 24 * 60 * 60 * 1000;

    private static final String AUDIT_LEARNER_PORTFOLIO_SET = "audit.learner.portfolio.set";

    private static final String AUDIT_LESSON_CREATED_KEY = "audit.lesson.created";

    private ILessonDAO lessonDAO;

    private ILessonClassDAO lessonClassDAO;

    private IActivityDAO activityDAO;

    private IBaseDAO baseDAO;

    private ILearningDesignDAO learningDesignDAO;

    private IGroupingDAO groupingDAO;

    private IGroupDAO groupDAO;

    private IGroupUserDAO groupUserDAO;

    private ILearnerProgressDAO learnerProgressDAO;

    private IAuthoringService authoringService;

    private ICoreLearnerService learnerService;

    private ILessonService lessonService;

    private ILamsCoreToolService lamsCoreToolService;

    private IUserManagementService userManagementService;

    private ISecurityService securityService;

    private Scheduler scheduler;

    private ApplicationContext applicationContext;

    private MessageService messageService;

    private AuditService auditService;

    private ILogEventService logEventService;

    /** Message keys */

    private static final String FORCE_COMPLETE_STOP_MESSAGE_GROUPING_ERROR = "force.complete.stop.message.grouping.error";

    private static final String FORCE_COMPLETE_STOP_MESSAGE_GROUPING = "force.complete.stop.message.grouping";

    private static final String FORCE_COMPLETE_STOP_MESSAGE_GATE = "force.complete.stop.message.gate";

    private static final String FORCE_COMPLETE_STOP_MESSAGE_COMPLETED_TO_ACTIVITY = "force.complete.stop.message.completed.to.activity";

    private static final String FORCE_COMPLETE_STOP_MESSAGE_COMPLETED_TO_END = "force.complete.stop.message.completed.to.end";

    private static final String FORCE_COMPLETE_STOP_MESSAGE_STOPPED_UNEXPECTEDLY = "force.complete.stop.message.stopped.unexpectedly";

    // ---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    // ---------------------------------------------------------------------
    /**
     * @param messageService
     *            the i18n Service bean.
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    @Override
    public MessageService getMessageService() {
	return messageService;
    }

    /**
     * @param userManagementService
     *            The userManagementService to set.
     */
    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setSecurityService(ISecurityService securityService) {
	this.securityService = securityService;
    }

    /**
     * @param learningDesignDAO
     *            The learningDesignDAO to set.
     */
    public void setLearningDesignDAO(ILearningDesignDAO learningDesignDAO) {
	this.learningDesignDAO = learningDesignDAO;
    }

    /**
     * 
     * @param learnerService
     */
    public void setLearnerService(ICoreLearnerService learnerService) {
	this.learnerService = learnerService;
    }

    /**
     * 
     * @param lessonService
     */
    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    /**
     * @param authoringService
     *            The authoringService to set.
     */
    public void setAuthoringService(IAuthoringService authoringService) {
	this.authoringService = authoringService;
    }

    /**
     * @param lessonClassDAO
     *            The lessonClassDAO to set.
     */
    public void setLessonClassDAO(ILessonClassDAO lessonClassDAO) {
	this.lessonClassDAO = lessonClassDAO;
    }

    /**
     * @param lessonDAO
     *            The lessonDAO to set.
     */
    public void setLessonDAO(ILessonDAO lessonDAO) {
	this.lessonDAO = lessonDAO;
    }

    /**
     * @param learnerProgressDAO
     *            The learnerProgressDAO to set.
     */
    public void setLearnerProgressDAO(ILearnerProgressDAO learnerProgressDAO) {
	this.learnerProgressDAO = learnerProgressDAO;
    }

    /**
     * @param userDAO
     */
    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    /**
     * @param groupDAO
     */
    public void setGroupDAO(IGroupDAO groupDAO) {
	this.groupDAO = groupDAO;
    }

    /**
     * @param groupDAO
     */
    public void setGroupUserDAO(IGroupUserDAO groupUserDAO) {
	this.groupUserDAO = groupUserDAO;
    }

    /**
     * @param groupingDAO
     */
    public void setGroupingDAO(IGroupingDAO groupingDAO) {
	this.groupingDAO = groupingDAO;
    }

    /**
     * @param lamsToolService
     *            The lamsToolService to set.
     */
    public void setLamsCoreToolService(ILamsCoreToolService lamsToolService) {
	lamsCoreToolService = lamsToolService;
    }

    /**
     * @param activityDAO
     *            The activityDAO to set.
     */
    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	this.applicationContext = applicationContext;
    }

    /**
     * @param scheduler
     *            The scheduler to set.
     */
    public void setScheduler(Scheduler scheduler) {
	this.scheduler = scheduler;
    }

    public void setAuditService(AuditService auditService) {
	this.auditService = auditService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    // ---------------------------------------------------------------------
    // Service Methods
    // ---------------------------------------------------------------------

    @Override
    public Lesson initializeLesson(String lessonName, String lessonDescription, long learningDesignId,
	    Integer organisationId, Integer userID, String customCSV, Boolean enableLessonIntro,
	    Boolean displayDesignImage, Boolean learnerExportAvailable, Boolean learnerPresenceAvailable,
	    Boolean learnerImAvailable, Boolean liveEditEnabled, Boolean enableLessonNotifications,
	    Boolean learnerRestart, Integer scheduledNumberDaysToLessonFinish, Long precedingLessonId) {

	securityService.isGroupMonitor(organisationId, userID, "intializeLesson", true);

	LearningDesign originalLearningDesign = authoringService.getLearningDesign(new Long(learningDesignId));
	if (originalLearningDesign == null) {
	    throw new MonitoringServiceException("Learning design for id=" + learningDesignId
		    + " is missing. Unable to initialize lesson.");
	}

	Lesson precedingLesson = (precedingLessonId == null) ? null : lessonDAO.getLesson(precedingLessonId);

	// The duplicated sequence should go in the run sequences folder under
	// the given organisation
	WorkspaceFolder runSeqFolder = null;
	int MAX_DEEP_LEVEL_FOLDER = 50;
	if (organisationId != null) {
	    Organisation org = (Organisation) baseDAO.find(Organisation.class, organisationId);
	    // Don't use unlimited loop to avoid dead lock. For instance, orgA
	    // is orgB parent, but orgB parent is orgA as well.
	    for (int idx = 0; idx < MAX_DEEP_LEVEL_FOLDER; idx++) {
		if ((org == null) || (runSeqFolder != null)) {
		    break;
		}
		Workspace workspace = org.getWorkspace();
		if (workspace != null) {
		    runSeqFolder = workspace.getDefaultRunSequencesFolder();
		}
		if (runSeqFolder == null) {
		    org = org.getParentOrganisation();
		}
	    }
	}

	User user = userID != null ? (User) baseDAO.find(User.class, userID) : null;
	Lesson initializedLesson = initializeLesson(lessonName, lessonDescription, originalLearningDesign, user,
		runSeqFolder, LearningDesign.COPY_TYPE_LESSON, customCSV, enableLessonIntro, displayDesignImage,
		learnerExportAvailable, learnerPresenceAvailable, learnerImAvailable, liveEditEnabled,
		enableLessonNotifications, learnerRestart, scheduledNumberDaysToLessonFinish, precedingLesson);

	Long initializedLearningDesignId = initializedLesson.getLearningDesign().getLearningDesignId();
	logEventService.logEvent(LogEvent.TYPE_TEACHER_LESSON_CREATE, userID, initializedLearningDesignId,
		initializedLesson.getLessonId(), null);

	return initializedLesson;
    }

    @Override
    public Lesson initializeLessonForPreview(String lessonName, String lessonDescription, long learningDesignId,
	    Integer userID, String customCSV, Boolean learnerPresenceAvailable, Boolean learnerImAvailable,
	    Boolean liveEditEnabled) {
	LearningDesign originalLearningDesign = authoringService.getLearningDesign(new Long(learningDesignId));
	if (originalLearningDesign == null) {
	    throw new MonitoringServiceException("Learning design for id=" + learningDesignId
		    + " is missing. Unable to initialize lesson.");
	}
	User user = userID != null ? (User) baseDAO.find(User.class, userID) : null;

	return initializeLesson(lessonName, lessonDescription, originalLearningDesign, user, null,
		LearningDesign.COPY_TYPE_PREVIEW, customCSV, false, false, false, learnerPresenceAvailable,
		learnerImAvailable, liveEditEnabled, true, false, null, null);
    }

    @Override
    public Lesson initializeLessonWithoutLDcopy(String lessonName, String lessonDescription, long learningDesignID,
	    User user, String customCSV, Boolean enableLessonIntro, Boolean displayDesignImage,
	    Boolean learnerExportAvailable, Boolean learnerPresenceAvailable, Boolean learnerImAvailable,
	    Boolean liveEditEnabled, Boolean enableLessonNotifications, Boolean learnerRestart,
	    Integer scheduledNumberDaysToLessonFinish, Lesson precedingLesson) {
	LearningDesign learningDesign = authoringService.getLearningDesign(learningDesignID);
	if (learningDesign == null) {
	    throw new MonitoringServiceException("Learning design for id=" + learningDesignID
		    + " is missing. Unable to initialize lesson.");
	}
	Lesson lesson = createNewLesson(lessonName, lessonDescription, user, learningDesign, enableLessonIntro,
		displayDesignImage, learnerExportAvailable, learnerPresenceAvailable, learnerImAvailable,
		liveEditEnabled, enableLessonNotifications, learnerRestart, scheduledNumberDaysToLessonFinish,
		precedingLesson);
	writeAuditLog(MonitoringService.AUDIT_LESSON_CREATED_KEY, new Object[] { lessonName, learningDesign.getTitle(),
		learnerExportAvailable });
	return lesson;
    }

    private Lesson initializeLesson(String lessonName, String lessonDescription, LearningDesign originalLearningDesign,
	    User user, WorkspaceFolder workspaceFolder, int copyType, String customCSV, Boolean enableLessonIntro,
	    Boolean displayDesignImage, Boolean learnerExportAvailable, Boolean learnerPresenceAvailable,
	    Boolean learnerImAvailable, Boolean liveEditEnabled, Boolean enableLessonNotifications,
	    Boolean learnerRestart, Integer scheduledNumberDaysToLessonFinish, Lesson precedingLesson) {
	// copy the current learning design
	LearningDesign copiedLearningDesign = authoringService.copyLearningDesign(originalLearningDesign, new Integer(
		copyType), user, workspaceFolder, true, null, customCSV);
	authoringService.saveLearningDesign(copiedLearningDesign);

	// Make all efforts to make sure it has a title
	String title = lessonName != null ? lessonName : copiedLearningDesign.getTitle();
	title = title != null ? title : "Unknown Lesson";
	// truncate title
	if (title.length() > 254) {
	    title = title.substring(0, 254);
	}

	Lesson lesson = createNewLesson(title, lessonDescription, user, copiedLearningDesign, enableLessonIntro,
		displayDesignImage, learnerExportAvailable, learnerPresenceAvailable, learnerImAvailable,
		liveEditEnabled, enableLessonNotifications, learnerRestart, scheduledNumberDaysToLessonFinish,
		precedingLesson);
	writeAuditLog(MonitoringService.AUDIT_LESSON_CREATED_KEY,
		new Object[] { lessonName, copiedLearningDesign.getTitle(), learnerExportAvailable });
	return lesson;
    }

    @Override
    public String initializeLesson(Integer creatorUserId, String lessonPacket) throws Exception {
	FlashMessage flashMessage = null;

	try {
	    Hashtable table = (Hashtable) WDDXProcessor.deserialize(lessonPacket);

	    // parse WDDX values

	    String title = WDDXProcessor.convertToString("lessonName", table.get("lessonName"));
	    String desc = WDDXProcessor.convertToString("lessonDescription", table.get("lessonDescription"));
	    int copyType = WDDXProcessor.convertToInt("copyType", table.get("copyType"));
	    Integer organisationId = WDDXProcessor.convertToInteger("organisationID", table.get("organisationID"));
	    long ldId = WDDXProcessor.convertToLong(AttributeNames.PARAM_LEARNINGDESIGN_ID,
		    table.get(AttributeNames.PARAM_LEARNINGDESIGN_ID));
	    String customCSV = WDDXProcessor.convertToString(WDDXTAGS.CUSTOM_CSV, table.get(WDDXTAGS.CUSTOM_CSV));
	    Boolean enableLessonIntro = WDDXProcessor.convertToBoolean("enableLessonIntro",
		    table.get("enableLessonIntro"));
	    Boolean displayDesignImage = WDDXProcessor.convertToBoolean("displayDesignImage",
		    table.get("displayDesignImage"));
	    boolean learnerExportAvailable = WDDXProcessor.convertToBoolean("learnerExportPortfolio",
		    table.get("learnerExportPortfolio"));
	    boolean learnerPresenceAvailable = WDDXProcessor.convertToBoolean("enablePresence",
		    table.get("enablePresence"));
	    boolean learnerImAvailable = WDDXProcessor.convertToBoolean("enableIm", table.get("enableIm"));
	    boolean liveEditEnabled = WDDXProcessor.convertToBoolean("enableLiveEdit", table.get("enableLiveEdit"));
	    Boolean enableLessonNotifications = WDDXProcessor.convertToBoolean("enableLessonNotifications",
		    table.get("enableLessonNotifications"));
	    Integer scheduledNumberDaysToLessonFinish = WDDXProcessor.convertToInteger(
		    "scheduledNumberDaysToLessonFinish", table.get("scheduledNumberDaysToLessonFinish"));
	    Long precedingLessonId = WDDXProcessor.convertToLong("organisationID", table.get("precedingLessonID"));

	    // initialize lesson

	    Lesson newLesson = null;

	    if (copyType == LearningDesign.COPY_TYPE_PREVIEW) {
		newLesson = initializeLessonForPreview(title, desc, ldId, creatorUserId, customCSV,
			learnerPresenceAvailable, learnerImAvailable, liveEditEnabled);
	    } else {
		newLesson = initializeLesson(title, desc, ldId, organisationId, creatorUserId, customCSV,
			enableLessonIntro, displayDesignImage, learnerExportAvailable, learnerPresenceAvailable,
			learnerImAvailable, liveEditEnabled, enableLessonNotifications, false,
			scheduledNumberDaysToLessonFinish, precedingLessonId);
	    }

	    if (newLesson != null) {
		flashMessage = new FlashMessage("initializeLesson", newLesson.getLessonId());
	    }

	    return flashMessage.serializeMessage();

	} catch (Exception e) {
	    MonitoringService.log.error("Exception occured trying to create a lesson class ", e);
	    throw new Exception(e);
	}
    }

    @Override
    public Lesson createLessonClassForLesson(long lessonId, Organisation organisation, String learnerGroupName,
	    List<User> organizationUsers, String staffGroupName, List<User> staffs, Integer userId) {
	Lesson newLesson = lessonDAO.getLesson(new Long(lessonId));
	// if lesson isn't started recreate the lesson class
	if (newLesson.isLessonStarted()) {
	    securityService.isLessonMonitor(lessonId, userId, "create class for lesson", true);
	    lessonService.setLearners(newLesson, organizationUsers);
	    lessonService.setStaffMembers(newLesson, staffs);
	} else {
	    if (organisation != null) {
		// security check needs organisation to be set
		// it is not set for lesson preview, so it still needs improvement
		newLesson.setOrganisation(organisation);
		securityService.isLessonMonitor(lessonId, userId, "create class for lesson", true);
	    }

	    LessonClass oldLessonClass = newLesson.getLessonClass();

	    LessonClass newLessonClass = this.createLessonClass(organisation, learnerGroupName, organizationUsers,
		    staffGroupName, staffs, newLesson);
	    newLessonClass.setLesson(newLesson);
	    newLesson.setLessonClass(newLessonClass);

	    lessonDAO.updateLesson(newLesson);

	    if (oldLessonClass != null) {
		lessonClassDAO.deleteLessonClass(oldLessonClass);
	    }
	}

	return newLesson;
    }

    @Override
    public void startLessonOnSchedule(long lessonId, Date startDate, Integer userId) {
	securityService.isLessonMonitor(lessonId, userId, "start lesson on schedule", true);

	// we get the lesson just created
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	if (requestedLesson.isLessonStarted()) {
	    // can't schedule it as it is already started. If the UI is correct,
	    // this should never happen.
	    MonitoringService.log.error("Lesson for id=" + lessonId
		    + " has been started. Unable to schedule lesson start.");
	    return;
	}

	if (requestedLesson.getScheduleStartDate() != null) {
	    // can't reschedule!
	    MonitoringService.log.error("Lesson for id=" + lessonId
		    + " is already scheduled and cannot be rescheduled.");
	    return;
	}

	// Change client/users schedule date to server's timezone.
	User user = (User) baseDAO.find(User.class, userId);
	TimeZone userTimeZone = TimeZone.getTimeZone(user.getTimeZone());
	Date tzStartLessonDate = DateUtil.convertFromTimeZoneToDefault(userTimeZone, startDate);

	JobDetail startLessonJob = getStartScheduleLessonJob();
	// setup the message for scheduling job
	startLessonJob.setName("startLessonOnSchedule:" + lessonId);

	startLessonJob.setDescription(requestedLesson.getLessonName() + ":"
		+ (requestedLesson.getUser() == null ? "" : requestedLesson.getUser().getFullName()));
	startLessonJob.getJobDataMap().put(MonitoringConstants.KEY_LESSON_ID, new Long(lessonId));
	startLessonJob.getJobDataMap().put(MonitoringConstants.KEY_USER_ID, new Integer(userId));

	// create customized triggers
	Trigger startLessonTrigger = new SimpleTrigger("startLessonOnScheduleTrigger:" + lessonId,
		Scheduler.DEFAULT_GROUP, tzStartLessonDate);
	// start the scheduling job
	try {
	    requestedLesson.setScheduleStartDate(tzStartLessonDate);
	    scheduler.scheduleJob(startLessonJob, startLessonTrigger);
	    setLessonState(requestedLesson, Lesson.NOT_STARTED_STATE);
	} catch (SchedulerException e) {
	    throw new MonitoringServiceException("Error occurred at "
		    + "[startLessonOnSchedule]- fail to start scheduling", e);
	}

	if (MonitoringService.log.isDebugEnabled()) {
	    MonitoringService.log.debug("Start lesson  [" + lessonId + "] on schedule is configured");
	}
    }

    @Override
    public void finishLessonOnSchedule(long lessonId, int scheduledNumberDaysToLessonFinish, Integer userId) {
	securityService.isLessonMonitor(lessonId, userId, "finish lesson on schedule", true);

	// we get the lesson want to finish
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	String triggerName = "finishLessonOnScheduleTrigger:" + lessonId;
	boolean alreadyScheduled = false;
	try {
	    // if trigger exists, the job was already scheduled and we need to (re)move the trigger
	    alreadyScheduled = scheduler.getTrigger(triggerName, Scheduler.DEFAULT_GROUP) != null;
	} catch (SchedulerException e) {
	    MonitoringService.log.error(e);
	}

	Trigger finishLessonTrigger = null;
	String finishLessonJobName = "finishLessonOnSchedule:" + lessonId;
	JobDetail finishLessonJob = null;
	Date endDate = null;

	if (scheduledNumberDaysToLessonFinish > 0) {
	    // calculate finish date
	    Date startDate = (requestedLesson.getStartDateTime() != null) ? requestedLesson.getStartDateTime()
		    : requestedLesson.getScheduleStartDate();
	    if (startDate == null) {
		throw new MonitoringServiceException("Lesson with id=" + lessonId
			+ " neither has been started nor scheduled to start. Unable to schedule lesson's finish.");
	    }
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(startDate);
	    calendar.add(Calendar.DATE, scheduledNumberDaysToLessonFinish);
	    endDate = calendar.getTime();

	    if (endDate.before(new Date())) {
		throw new IllegalArgumentException("Lesson scheduled finish date is already in the past");
	    }

	    if (!alreadyScheduled) {
		finishLessonJob = getFinishScheduleLessonJob();
		// setup the message for scheduling job
		finishLessonJob.setName(finishLessonJobName);
		finishLessonJob.setDescription(requestedLesson.getLessonName() + ":"
			+ (requestedLesson.getUser() == null ? "" : requestedLesson.getUser().getFullName()));
		finishLessonJob.getJobDataMap().put(MonitoringConstants.KEY_LESSON_ID, new Long(lessonId));
		finishLessonJob.getJobDataMap().put(MonitoringConstants.KEY_USER_ID, new Integer(userId));
	    }

	    // create customized triggers
	    finishLessonTrigger = new SimpleTrigger(triggerName, Scheduler.DEFAULT_GROUP, endDate);
	    finishLessonTrigger.setJobName(finishLessonJobName);
	}

	// start the scheduling job
	try {
	    requestedLesson.setScheduleEndDate(endDate);
	    lessonDAO.updateLesson(requestedLesson);
	    if (alreadyScheduled) {
		if (scheduledNumberDaysToLessonFinish > 0) {
		    scheduler.rescheduleJob(triggerName, Scheduler.DEFAULT_GROUP, finishLessonTrigger);
		    if (MonitoringService.log.isDebugEnabled()) {
			MonitoringService.log.debug("Finish lesson  [" + lessonId + "] job has been rescheduled to "
				+ endDate);
		    }
		} else {
		    scheduler.deleteJob(finishLessonJobName, Scheduler.DEFAULT_GROUP);
		    if (MonitoringService.log.isDebugEnabled()) {
			MonitoringService.log.debug("Finish lesson  [" + lessonId + "] job has been removed");
		    }
		}
	    } else if (scheduledNumberDaysToLessonFinish > 0) {
		scheduler.scheduleJob(finishLessonJob, finishLessonTrigger);
		if (MonitoringService.log.isDebugEnabled()) {
		    MonitoringService.log.debug("Finish lesson  [" + lessonId + "] job has been scheduled to "
			    + endDate);
		}
	    }
	} catch (SchedulerException e) {
	    throw new MonitoringServiceException("Error occurred at "
		    + "[finishLessonOnSchedule]- fail to start scheduling", e);
	}
    }

    @Override
    public void startLesson(long lessonId, Integer userId) {
	if (MonitoringService.log.isDebugEnabled()) {
	    MonitoringService.log.debug("=============Starting Lesson " + lessonId + "==============");
	}
	// we get the lesson just created
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	if (requestedLesson.getOrganisation() != null) {
	    // preview does not have organisation set, so this security check still needs improvement
	    securityService.isLessonMonitor(lessonId, userId, "start lesson", true);
	}
	if (requestedLesson.isLessonStarted()) {
	    MonitoringService.log
		    .warn("Lesson "
			    + lessonId
			    + " has been started. No need to start the lesson. The lesson was probably scheduled, and then the staff used \"Start now\". This message would have then been created by the schedule start");
	    return;
	}

	Date lessonStartTime = new Date();
	// initialize tool sessions if necessary
	LearningDesign design = requestedLesson.getLearningDesign();
	boolean designModified = false;

	for (Activity activity : (Set<Activity>) design.getActivities()) {
	    Integer newMaxId = startSystemActivity(activity, design.getMaxID(), lessonStartTime,
		    requestedLesson.getLessonName());
	    if (newMaxId != null) {
		design.setMaxID(newMaxId);
		designModified = true;
	    }

	    activity.setInitialised(Boolean.TRUE);
	    activityDAO.update(activity);
	}

	if (designModified) {
	    learningDesignDAO.update(design);
	}

	// update lesson status
	requestedLesson.setLessonStateId(Lesson.STARTED_STATE);
	requestedLesson.setStartDateTime(lessonStartTime);
	lessonDAO.updateLesson(requestedLesson);

	if (MonitoringService.log.isDebugEnabled()) {
	    MonitoringService.log.debug("=============Lesson " + lessonId + " started===============");
	}
	logEventService.logEvent(LogEvent.TYPE_TEACHER_LESSON_START, userId, null, lessonId, null);
    }

    @Override
    public Integer startSystemActivity(Activity activity, Integer currentMaxId, Date lessonStartTime, String lessonName) {
	Integer newMaxId = null;

	// if it is schedule gate, we need to initialize the sheduler for it.
	if (activity.getActivityTypeId().intValue() == Activity.SCHEDULE_GATE_ACTIVITY_TYPE) {
	    ScheduleGateActivity gateActivity = (ScheduleGateActivity) activityDAO.getActivityByActivityId(activity
		    .getActivityId());
	    // do not run the scheduler if the gate is basen on user completing previous activity
	    if (!Boolean.TRUE.equals(gateActivity.getGateActivityCompletionBased())) {
		activity = runGateScheduler(gateActivity, lessonStartTime, lessonName);
	    }
	    activity.setInitialised(true);
	}
	if (activity.isBranchingActivity() && (activity.getGrouping() == null)) {
	    // all branching activities must have a grouping, as the learner
	    // will be allocated to a group linked to a sequence (branch)
	    Grouping grouping = new ChosenGrouping(null, null, null);
	    grouping.setGroupingUIID(currentMaxId);
	    grouping.getActivities().add(activity);
	    activity.setGrouping(grouping);
	    activity.setGroupingUIID(currentMaxId);
	    activity.setApplyGrouping(Boolean.TRUE);
	    groupingDAO.insert(grouping);

	    activity.setGrouping(grouping);
	    if (MonitoringService.log.isDebugEnabled()) {
		MonitoringService.log.debug("startLesson: Created chosen grouping " + grouping
			+ " for branching activity " + activity);
	    }
	    newMaxId = new Integer(currentMaxId.intValue() + 1);
	    activity.setInitialised(true);
	}
	return newMaxId;
    }

    @Override
    public ScheduleGateActivity runGateScheduler(ScheduleGateActivity scheduleGate, Date schedulingStartTime,
	    String lessonName) {

	if (MonitoringService.log.isDebugEnabled()) {
	    MonitoringService.log.debug("Running scheduler for gate " + scheduleGate.getActivityId() + "...");
	}
	JobDetail openScheduleGateJob = getOpenScheduleGateJob();
	JobDetail closeScheduleGateJob = getCloseScheduleGateJob();
	// setup the message for scheduling job
	openScheduleGateJob.setName("openGate:" + scheduleGate.getActivityId());
	openScheduleGateJob.setDescription(scheduleGate.getTitle() + ":" + lessonName);
	openScheduleGateJob.getJobDataMap().put("gateId", scheduleGate.getActivityId());
	closeScheduleGateJob.setName("closeGate:" + scheduleGate.getActivityId());
	closeScheduleGateJob.getJobDataMap().put("gateId", scheduleGate.getActivityId());
	closeScheduleGateJob.setDescription(scheduleGate.getTitle() + ":" + lessonName);

	// create customized triggers
	Trigger openGateTrigger = new SimpleTrigger("openGateTrigger:" + scheduleGate.getActivityId(),
		Scheduler.DEFAULT_GROUP, scheduleGate.getGateOpenTime(schedulingStartTime));

	Trigger closeGateTrigger = new SimpleTrigger("closeGateTrigger:" + scheduleGate.getActivityId(),
		Scheduler.DEFAULT_GROUP, scheduleGate.getGateCloseTime(schedulingStartTime));

	// start the scheduling job
	try {
	    if (((scheduleGate.getGateStartTimeOffset() == null) && (scheduleGate.getGateEndTimeOffset() == null))
		    || ((scheduleGate.getGateStartTimeOffset() != null) && (scheduleGate.getGateEndTimeOffset() == null))) {
		scheduler.scheduleJob(openScheduleGateJob, openGateTrigger);
	    } else if (openGateTrigger.getStartTime().before(closeGateTrigger.getStartTime())) {
		scheduler.scheduleJob(openScheduleGateJob, openGateTrigger);
		scheduler.scheduleJob(closeScheduleGateJob, closeGateTrigger);
	    }

	} catch (SchedulerException e) {
	    throw new MonitoringServiceException("Error occurred at " + "[runGateScheduler]- fail to start scheduling",
		    e);
	}

	if (MonitoringService.log.isDebugEnabled()) {
	    MonitoringService.log.debug("Scheduler for Gate " + scheduleGate.getActivityId() + " started...");
	}

	return scheduleGate;
    }

    @Override
    public void finishLesson(long lessonId, Integer userId) {
	securityService.isLessonMonitor(lessonId, userId, "finish lesson", true);
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	setLessonState(requestedLesson, Lesson.FINISHED_STATE);
    }

    @Override
    public void archiveLesson(long lessonId, Integer userId) {
	securityService.isLessonMonitor(lessonId, userId, "archive lesson", true);
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	if (!Lesson.ARCHIVED_STATE.equals(requestedLesson.getLessonStateId())
		&& !Lesson.REMOVED_STATE.equals(requestedLesson.getLessonStateId())) {
	    setLessonState(requestedLesson, Lesson.ARCHIVED_STATE);
	}
    }

    @Override
    public void unarchiveLesson(long lessonId, Integer userId) {
	securityService.isLessonMonitor(lessonId, userId, "unarchive lesson", true);
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	revertLessonState(requestedLesson);
    }

    @Override
    public void suspendLesson(long lessonId, Integer userId) {
	securityService.isLessonMonitor(lessonId, userId, "suspend lesson", true);
	Lesson lesson = lessonDAO.getLesson(new Long(lessonId));
	if (!Lesson.SUSPENDED_STATE.equals(lesson.getLessonStateId())
		&& !Lesson.REMOVED_STATE.equals(lesson.getLessonStateId())) {
	    setLessonState(lesson, Lesson.SUSPENDED_STATE);
	}
    }

    @Override
    public void unsuspendLesson(long lessonId, Integer userId) {
	securityService.isLessonMonitor(lessonId, userId, "unsuspend lesson", true);
	Lesson lesson = lessonDAO.getLesson(new Long(lessonId));
	Integer state = lesson.getLessonStateId();
	// only suspend started lesson
	if (!Lesson.SUSPENDED_STATE.equals(state)) {
	    throw new MonitoringServiceException("Lesson is not suspended lesson. It can not be unsuspended.");
	}
	revertLessonState(lesson);
    }

    /**
     * Set a lesson to a particular state. Copies the current state to the previous lesson state.
     * 
     * @param requestedLesson
     * @param status
     */
    private void setLessonState(Lesson requestedLesson, Integer status) {
	requestedLesson.setPreviousLessonStateId(requestedLesson.getLessonStateId());
	requestedLesson.setLessonStateId(status);
	lessonDAO.updateLesson(requestedLesson);
	logEventService.logEvent(LogEvent.TYPE_TEACHER_LESSON_CHANGE_STATE, requestedLesson.getUser().getUserId(),
		null, requestedLesson.getLessonId(), null);
    }

    /**
     * Sets a lesson back to its previous state. Used when we "unsuspend" or "unarchive"
     * 
     * @param requestedLesson
     * @param status
     */
    private void revertLessonState(Lesson requestedLesson) {
	Integer currentStatus = requestedLesson.getLessonStateId();
	if (requestedLesson.getPreviousLessonStateId() != null) {
	    if (requestedLesson.getPreviousLessonStateId().equals(Lesson.NOT_STARTED_STATE)
		    && requestedLesson.getScheduleStartDate().before(new Date())) {
		requestedLesson.setLessonStateId(Lesson.STARTED_STATE);
	    } else {
		requestedLesson.setLessonStateId(requestedLesson.getPreviousLessonStateId());
	    }
	    requestedLesson.setPreviousLessonStateId(null);
	} else {
	    if ((requestedLesson.getStartDateTime() != null) && (requestedLesson.getScheduleStartDate() != null)) {
		requestedLesson.setLessonStateId(Lesson.STARTED_STATE);
	    } else if (requestedLesson.getScheduleStartDate() != null) {
		if (requestedLesson.getScheduleStartDate().after(new Date())) {
		    requestedLesson.setLessonStateId(Lesson.NOT_STARTED_STATE);
		} else {
		    requestedLesson.setLessonStateId(Lesson.STARTED_STATE);
		}
	    } else if (requestedLesson.getStartDateTime() != null) {
		requestedLesson.setLessonStateId(Lesson.STARTED_STATE);
	    } else {
		requestedLesson.setLessonStateId(Lesson.CREATED);
	    }

	    requestedLesson.setPreviousLessonStateId(currentStatus);
	}
	lessonDAO.updateLesson(requestedLesson);

	logEventService.logEvent(LogEvent.TYPE_TEACHER_LESSON_CHANGE_STATE, requestedLesson.getUser().getUserId(),
		null, requestedLesson.getLessonId(), null);
    }

    @Override
    public void removeLesson(long lessonId, Integer userId) {
	securityService.isLessonMonitor(lessonId, userId, "remove lesson", true);
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	setLessonState(requestedLesson, Lesson.REMOVED_STATE);
    }

    @Override
    public Boolean setLearnerPortfolioAvailable(long lessonId, Integer userId, Boolean isLearnerExportAvailable) {
	securityService.isLessonMonitor(lessonId, userId, "set learner portfolio available", true);

	isLearnerExportAvailable = (isLearnerExportAvailable != null) ? isLearnerExportAvailable : Boolean.FALSE;
	Lesson lesson = lessonDAO.getLesson(new Long(lessonId));
	lesson.setLearnerExportAvailable(isLearnerExportAvailable);
	lessonDAO.updateLesson(lesson);

	// audit log enabling/disabling export portfolio
	writeAuditLog(MonitoringService.AUDIT_LEARNER_PORTFOLIO_SET,
		new Object[] { lesson.getLessonName(), lesson.getLearnerExportAvailable() });

	return lesson.getLearnerExportAvailable();
    }

    @Override
    public Boolean setPresenceAvailable(long lessonId, Integer userId, Boolean presenceAvailable) {
	securityService.isLessonMonitor(lessonId, userId, "set presence available", true);
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	requestedLesson.setLearnerPresenceAvailable(presenceAvailable != null ? presenceAvailable : Boolean.FALSE);
	lessonDAO.updateLesson(requestedLesson);
	return requestedLesson.getLearnerPresenceAvailable();
    }

    @Override
    public Boolean setPresenceImAvailable(long lessonId, Integer userId, Boolean presenceImAvailable) {
	securityService.isLessonMonitor(lessonId, userId, "set presence instant messaging available", true);
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	requestedLesson.setLearnerImAvailable(presenceImAvailable != null ? presenceImAvailable : Boolean.FALSE);
	lessonDAO.updateLesson(requestedLesson);
	return requestedLesson.getLearnerImAvailable();
    }

    @Override
    public Boolean setLiveEditEnabled(long lessonId, Integer userId, Boolean liveEditEnabled) {
	securityService.isLessonMonitor(lessonId, userId, "set live edit available", true);
	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
	requestedLesson.setLiveEditEnabled(liveEditEnabled != null ? liveEditEnabled : Boolean.FALSE);
	lessonDAO.updateLesson(requestedLesson);
	return requestedLesson.getLiveEditEnabled();
    }

    @Override
    public GateActivity openGate(Long gateId) {
	GateActivity gate = (GateActivity) activityDAO.getActivityByActivityId(gateId);
	if (gate != null) {
	    gate.setGateOpen(new Boolean(true));

	    // we un-schedule the gate from the scheduler if it's of a scheduled
	    // gate (LDEV-1271)
	    if (gate.isScheduleGate()) {

		try {
		    scheduler.unscheduleJob("openGateTrigger:" + gate.getActivityId(), Scheduler.DEFAULT_GROUP);
		} catch (SchedulerException e) {
		    MonitoringService.log.error(
			    "Error unscheduling trigger for gate activity id:" + gate.getActivityId(), e);
		    throw new MonitoringServiceException("Error unscheduling trigger for gate activity id:"
			    + gate.getActivityId(), e);

		}

	    }

	    activityDAO.update(gate);
	}
	return gate;
    }

    @Override
    public GateActivity openGateForSingleUser(Long gateId, Integer userId) {
	GateActivity gate = (GateActivity) activityDAO.getActivityByActivityId(gateId);
	if ((gate != null) && (userId != null) && (userId >= 0)) {
	    User user = (User) baseDAO.find(User.class, userId);
	    gate.addLeaner(user, true);
	    activityDAO.update(gate);
	}
	return gate;
    }

    @Override
    public GateActivity closeGate(Long gateId) {
	GateActivity gate = (GateActivity) activityDAO.getActivityByActivityId(gateId);
	gate.setGateOpen(new Boolean(false));
	activityDAO.update(gate);
	return gate;
    }

    @Override
    public String forceCompleteActivitiesByUser(Integer learnerId, Integer requesterId, long lessonId, Long activityId,
	    boolean removeLearnerContent) {
	securityService.isLessonMonitor(lessonId, requesterId, "force complete", true);
	Lesson lesson = lessonDAO.getLesson(new Long(lessonId));
	User learner = (User) baseDAO.find(User.class, learnerId);

	LearnerProgress learnerProgress = learnerService.getProgress(learnerId, lessonId);
	Activity stopActivity = null;

	if (activityId != null) {
	    stopActivity = getActivityById(activityId);
	    if (stopActivity == null) {
		throw new MonitoringServiceException("Activity missing. Activity id" + activityId);
	    }

	    // check if activity is already complete
	    Activity parentActivity = stopActivity.getParentActivity();

	    // if user is moved into branch, see if he is allowed to do that
	    if ((parentActivity != null) && parentActivity.isSequenceActivity()) {
		SequenceActivity sequenceActivity = (SequenceActivity) parentActivity;
		Group group = sequenceActivity.getSoleGroupForBranch();
		if ((group == null) || !group.hasLearner(learner)) {
		    return "User is not assigned to the chosen branch";
		}
	    }

	    // check if the target activity or its parents were completed
	    // if yes, we move user backward, otherwise forward
	    if ((learnerProgress != null)
		    && (learnerProgress.getCompletedActivities().containsKey(stopActivity) || ((parentActivity != null) && (learnerProgress
			    .getCompletedActivities().containsKey(parentActivity) || ((parentActivity
			    .getParentActivity() != null) && learnerProgress.getCompletedActivities().containsKey(
			    parentActivity.getParentActivity())))))) {

		return forceUncompleteActivity(learnerProgress, stopActivity, removeLearnerContent);
	    }
	}

	if (learnerProgress == null) {
	    learnerProgress = new LearnerProgress(learner, lesson);
	    learnerProgress.setStartDate(new Timestamp(new Date().getTime()));
	    learnerProgress.setNextActivity(lesson.getLearningDesign().getFirstActivity());
	    baseDAO.insert(learnerProgress);
	}

	Activity currentActivity = learnerProgress.getCurrentActivity();
	Activity stopPreviousActivity = null;
	if (stopActivity != null) {
	    Activity firstActivity = lesson.getLearningDesign().getFirstActivity();
	    if (stopActivity.equals(firstActivity)) {
		// special case with forcing user to the first activity,
		// which can not be processed by forceCompleteActivity() method
		learnerProgress.setCurrentActivity(firstActivity);
		if (firstActivity.getTransitionFrom() != null) {
		    learnerProgress.setNextActivity(firstActivity.getTransitionFrom().getToActivity());
		} else {
		    learnerProgress.setNextActivity(null);
		}
		learnerProgress.getAttemptedActivities().put(firstActivity, new Date());
		baseDAO.update(learnerProgress);

		return messageService.getMessage(MonitoringService.FORCE_COMPLETE_STOP_MESSAGE_COMPLETED_TO_ACTIVITY,
			new Object[] { firstActivity.getTitle() });
	    }
	    // force complete operates on previous activity, not target
	    stopPreviousActivity = stopActivity.getTransitionTo().getFromActivity();
	    // de-proxy the activity
	    stopPreviousActivity = getActivityById(stopPreviousActivity.getActivityId());
	}
	String stopReason = forceCompleteActivity(learner, lessonId, learnerProgress, currentActivity,
		stopPreviousActivity, new ArrayList<Long>());

	return stopReason != null ? stopReason : messageService
		.getMessage(MonitoringService.FORCE_COMPLETE_STOP_MESSAGE_STOPPED_UNEXPECTEDLY);
    }

    /**
     * Recursive method to step through a design and do the force complete.
     * 
     * Special Cases: Gate -- LearnerService.knockGate(GateActivity gate, User knocker, List lessonLearners) Y -
     * continue N - Stop Group -- getGroup -> exist? Y - continue N - PermissionGroup - Stop RandomGroup - create group,
     * then complete it and continue.
     * 
     * As we process an activity, we stick it in touchedActivityIds. Then we check this list before forwarding to an
     * activity - this will stop us going into loops on the parallel activities and other complex activities that return
     * to the parent activity after the child.
     */
    private String forceCompleteActivity(User learner, Long lessonId, LearnerProgress progress, Activity activity,
	    Activity stopActivity, ArrayList<Long> touchedActivityIds) {

	// TODO check performance - does it load the learner progress every time
	// or do it load it from the cache.
	String stopReason = null;

	if (activity != null) {
	    // activity likely to be a cglib so get the real activity
	    activity = activityDAO.getActivityByActivityId(activity.getActivityId());
	    touchedActivityIds.add(activity.getActivityId());

	    if (activity.isGroupingActivity()) {
		GroupingActivity groupActivity = (GroupingActivity) activity;
		Grouping grouping = groupActivity.getCreateGrouping();
		Group myGroup = grouping.getGroupBy(learner);
		if ((myGroup == null) || myGroup.isNull()) {
		    // group does not exist
		    if (grouping.isRandomGrouping()) {
			// for random grouping, create then complete it. Continue
			try {
			    lessonService.performGrouping(lessonId, groupActivity, learner);
			} catch (LessonServiceException e) {
			    MonitoringService.log.error("Force complete failed. Learner " + learner + " lessonId "
				    + lessonId + " processing activity " + activity, e);
			    stopReason = messageService.getMessage(
				    MonitoringService.FORCE_COMPLETE_STOP_MESSAGE_GROUPING_ERROR,
				    new Object[] { activity.getTitle() });
			}
			learnerService.completeActivity(learner.getUserId(), activity, lessonId);
			if (MonitoringService.log.isDebugEnabled()) {
			    MonitoringService.log.debug("Grouping activity [" + activity.getActivityId()
				    + "] is completed.");
			}
		    } else {
			// except random grouping, stop here
			stopReason = messageService.getMessage(MonitoringService.FORCE_COMPLETE_STOP_MESSAGE_GROUPING,
				new Object[] { activity.getTitle() });
		    }
		} else {
		    // if group already exist
		    learnerService.completeActivity(learner.getUserId(), activity, lessonId);
		    if (MonitoringService.log.isDebugEnabled()) {
			MonitoringService.log.debug("Grouping activity [" + activity.getActivityId()
				+ "] is completed.");
		    }
		}

	    } else if (activity.isGateActivity()) {
		GateActivity gate = (GateActivity) activity;
		GateActivityDTO dto = learnerService.knockGate(gate, learner, false);
		if (dto.getAllowToPass()) {
		    // the gate is opened, continue to next activity to complete
		    learnerService.completeActivity(learner.getUserId(), activity, lessonId);
		    if (MonitoringService.log.isDebugEnabled()) {
			MonitoringService.log.debug("Gate activity [" + gate.getActivityId() + "] is completed.");
		    }
		} else {
		    // the gate is closed, stop here
		    stopReason = messageService.getMessage(MonitoringService.FORCE_COMPLETE_STOP_MESSAGE_GATE,
			    new Object[] { activity.getTitle() });
		}

	    } else if (activity.isToolActivity()) {
		ToolActivity toolActivity = (ToolActivity) activity;
		try {
		    ToolSession toolSession = lamsCoreToolService.getToolSessionByActivity(learner, toolActivity);
		    if (toolSession == null) {
			// grouped tool's tool session isn't created until the first
			// user in the group reaches that
			// point the tool session creation is normally triggered by
			// LoadTooLActivityAction, so we need
			// to do it here. Won't happen very often - normally another
			// member of the group will have
			// triggered the creation of the tool session.
			learnerService.createToolSessionsIfNecessary(toolActivity, progress);
			toolSession = lamsCoreToolService.getToolSessionByActivity(learner, toolActivity);
		    }
		    learnerService.completeToolSession(toolSession.getToolSessionId(), new Long(learner.getUserId()
			    .longValue()));
		    learnerService.completeActivity(learner.getUserId(), activity, lessonId);
		    if (MonitoringService.log.isDebugEnabled()) {
			MonitoringService.log.debug("Tool activity [" + activity.getActivityId() + "] is completed.");
		    }
		} catch (LamsToolServiceException e) {
		    throw new MonitoringServiceException(e);
		}

	    } else if (activity.isBranchingActivity() || activity.isOptionsActivity()) {
		// Can force complete over a branching activity, but none of the
		// branches are marked as done.
		// Ditto the two types of optional activities.
		// Then if the user goes back to them, they will operate normally.
		learnerService.completeActivity(learner.getUserId(), activity, lessonId);

	    } else if (activity.isComplexActivity()) {
		// expect it to be a parallel activity
		ComplexActivity complexActivity = (ComplexActivity) activity;
		Set allActivities = complexActivity.getActivities();
		Iterator iter = allActivities.iterator();
		while ((stopReason == null) && iter.hasNext()) {
		    Activity act = (Activity) iter.next();
		    stopReason = forceCompleteActivity(learner, lessonId, progress, act, stopActivity,
			    touchedActivityIds);
		}
		MonitoringService.log.debug("Complex activity [" + activity.getActivityId() + "] is completed.");
	    }
	}
	// complete to the given activity ID, then stop. To be sure, the given
	// activity is forced to complete as well.
	// if we had stopped due to a subactivity, the stop reason will already
	// be set.
	if (stopReason == null) {
	    LearnerProgress learnerProgress = learnerService.getProgress(learner.getUserId(), lessonId);
	    if ((stopActivity != null) && learnerProgress.getCompletedActivities().containsKey(stopActivity)) {
		// we have reached the stop activity. It may have been the
		// activity we just processed
		// or it may have been a parent activity (e.g. an optional
		// activity) and completing its last
		// child has triggered the parent activity to be completed.
		// Hence we have to check the actual
		// completed activities list rather than just checking the id of
		// the activity.
		stopReason = messageService.getMessage(
			MonitoringService.FORCE_COMPLETE_STOP_MESSAGE_COMPLETED_TO_ACTIVITY,
			new Object[] { activity.getTitle() });

		// without this, there are errors when target is in branching
		learnerService.createToolSessionsIfNecessary(stopActivity, learnerProgress);
	    } else {
		Activity nextActivity = learnerProgress.getNextActivity();

		// now where?
		if ((nextActivity == null)
			|| ((activity != null) && nextActivity.getActivityId().equals(activity.getActivityId()))) {
		    // looks like we have reached the end of the sequence?
		    stopReason = messageService
			    .getMessage(MonitoringService.FORCE_COMPLETE_STOP_MESSAGE_COMPLETED_TO_END);
		} else if (touchedActivityIds.contains(nextActivity.getActivityId())) {
		    // processed this one before. Better cut at this point or we
		    // will end up in a loop.
		    // it's probably the parent activity
		    stopReason = null; // i.e. do nothing
		} else {
		    // no where else to go - keep on pressing on down the
		    // sequence then.
		    stopReason = forceCompleteActivity(learner, lessonId, learnerProgress, nextActivity, stopActivity,
			    touchedActivityIds);
		}
	    }
	}

	return stopReason;
    }

    /**
     * Moves user to the given activity which he already completed. Removes and resets entries in LearnerProgress to
     * achieve it.
     */
    @SuppressWarnings("unchecked")
    private String forceUncompleteActivity(LearnerProgress learnerProgress, Activity targetActivity,
	    boolean removeLearnerContent) {
	User learner = learnerProgress.getUser();
	Activity currentActivity = learnerProgress.getCurrentActivity();
	// set of activities for which "attempted" and "completed" status will be removed
	Set<Activity> uncompleteActivities = new HashSet<Activity>();
	uncompleteActivities.add(targetActivity);

	if (currentActivity == null) {
	    // Learner has finished the whole lesson. Find the last activity by traversing the transition.
	    currentActivity = learnerProgress.getLesson().getLearningDesign().getFirstActivity();
	    while (currentActivity.getTransitionFrom() != null) {
		currentActivity = currentActivity.getTransitionFrom().getToActivity();
	    }
	    uncompleteActivities.add(currentActivity);
	}

	// check if the target is a part of complex activity
	CompletedActivityProgress completedActivityProgress = learnerProgress.getCompletedActivities().get(
		targetActivity);
	Activity previousActivity = null;
	Activity targetParentActivity = targetActivity.getParentActivity();
	if (targetParentActivity != null) {
	    uncompleteActivities.add(targetParentActivity);
	    if (targetParentActivity.getParentActivity() != null) {
		targetParentActivity = targetParentActivity.getParentActivity();
		uncompleteActivities.add(targetParentActivity);
	    }
	    if (completedActivityProgress == null) {
		completedActivityProgress = learnerProgress.getCompletedActivities().get(targetParentActivity);
	    }
	}

	// find the activity just before the target activity
	if (targetActivity.getTransitionTo() == null) {
	    // if user is moved to first activity in branch
	    // previous activity is the one before whole branching activity
	    if ((targetParentActivity != null) && (targetParentActivity.getTransitionTo() != null)) {
		previousActivity = targetParentActivity.getTransitionTo().getFromActivity();
	    }
	} else {
	    previousActivity = targetActivity.getTransitionTo().getFromActivity();
	}

	learnerProgress.setLessonComplete(LearnerProgress.LESSON_NOT_COMPLETE);
	learnerProgress.setFinishDate(null);
	learnerProgress.setPreviousActivity(previousActivity);
	learnerProgress.setCurrentActivity(targetActivity);
	learnerProgress.setNextActivity(targetActivity);

	// grouping and branch activities which need to be reset
	Set<Activity> groupings = new HashSet<Activity>();

	// remove completed activities step by step, all the way from current to target activity
	while (!currentActivity.equals(targetActivity)) {
	    uncompleteActivities.add(currentActivity);

	    if (currentActivity.isComplexActivity()) {
		if (currentActivity.equals(targetParentActivity)) {
		    // we came to the complex activity which contains our target
		    currentActivity = targetActivity;
		    while (currentActivity.getTransitionFrom() != null) {
			// find the last activity in the branch and carry on with backwards traversal
			currentActivity = currentActivity.getTransitionFrom().getToActivity();
		    }
		    continue;
		} else {
		    ComplexActivity complexActivity = (ComplexActivity) getActivityById(currentActivity.getActivityId());
		    // forget all records within complex activity
		    for (Activity childActivity : (Set<Activity>) complexActivity.getActivities()) {
			uncompleteActivities.add(childActivity);
			if (childActivity.isComplexActivity()) {
			    ComplexActivity complexChildActivity = (ComplexActivity) getActivityById(childActivity
				    .getActivityId());
			    uncompleteActivities.addAll(complexChildActivity.getActivities());
			}

			// mark the activity to be "unbranched"
			if (childActivity.isSequenceActivity()) {
			    groupings.add(childActivity);
			}
		    }
		}
	    }

	    Transition transitionTo = currentActivity.getTransitionTo();
	    if (transitionTo == null) {
		// reached beginning of either sequence or complex activity
		if (currentActivity.getParentActivity() == null) {
		    // special case when learning design has only one activity
		    if (!((previousActivity == null) && currentActivity.equals(targetActivity))) {
			// reached beginning of sequence and target activity was not found, something is wrong
			throw new MonitoringServiceException("Target activity was not found sequence. Activity id: "
				+ targetActivity.getActivityId());
		    }
		} else {
		    currentActivity = currentActivity.getParentActivity();
		    if (currentActivity.getParentActivity() != null) {
			// for optional sequences, the real complex activity is 2 tiers up, not just one
			currentActivity = currentActivity.getParentActivity();
		    }
		    // now the current activity is the complex one in main sequence
		}
	    } else {
		// move backwards
		currentActivity = transitionTo.getFromActivity();
		if (currentActivity.isGroupingActivity()) {
		    groupings.add(currentActivity);
		}
	    }
	}

	// forget that user completed and attempted activiites
	for (Activity activity : uncompleteActivities) {
	    learnerProgress.getAttemptedActivities().remove(activity);
	    learnerProgress.getCompletedActivities().remove(activity);
	    if (removeLearnerContent && activity.isToolActivity()) {
		ToolActivity toolActivity = (ToolActivity) getActivityById(activity.getActivityId());
		// remove learner content from this activity
		lamsCoreToolService.notifyToolToDeleteLearnerContent(toolActivity, learner.getUserId());
	    }
	}

	// set target activity as attempted
	learnerProgress.getAttemptedActivities().put(targetActivity, completedActivityProgress.getStartDate());
	if (targetParentActivity != null) {
	    // set parent as attempted
	    learnerProgress.getAttemptedActivities()
		    .put(targetParentActivity, completedActivityProgress.getStartDate());
	    targetParentActivity = targetActivity.getParentActivity();
	    if (targetParentActivity != null) {
		// if target was part of branch, then immediate parent was Sequence
		// and parent's parent is Branching
		learnerProgress.getAttemptedActivities().put(targetParentActivity,
			completedActivityProgress.getStartDate());
	    }
	}

	learnerProgressDAO.updateLearnerProgress(learnerProgress);

	// do ungrouping and unbranching
	for (Activity activity : groupings) {
	    if (activity.isGroupingActivity()) {
		// fetch real object, otherwise there is a cast error
		GroupingActivity groupingActivity = (GroupingActivity) getActivityById(activity.getActivityId());
		Grouping grouping = groupingActivity.getCreateGrouping();
		if (grouping.doesLearnerExist(learner)) {
		    // cancel existing grouping, so the learner has a chance to be grouped again
		    Group group = grouping.getGroupBy(learner);
		    group.getUsers().remove(learner);
		    groupDAO.saveGroup(group);
		}
	    } else if (activity.isSequenceActivity()) {
		SequenceActivity sequenceActivity = (SequenceActivity) getActivityById(activity.getActivityId());
		Group group = sequenceActivity.getSoleGroupForBranch();
		if ((group != null) && group.hasLearner(learner)) {
		    // remove learner from the branch
		    removeUsersFromBranch(sequenceActivity.getActivityId(), new String[] { learner.getUserId()
			    .toString() });
		}
	    } else {
		MonitoringService.log.warn("Unknow activity type marked for ungrouping: " + activity.getActivityId());
	    }
	}

	if (targetParentActivity != null) {
	    // needed by Monitor to display user's progress in the given activity
	    learnerService.createToolSessionsIfNecessary(targetActivity, learnerProgress);

	    // if user was moved to a branch, he needs to be force completed from beginning of the branch
	    // all the way to target activity
	    Activity precedingUncompleteActivity = null;
	    Activity precedingActivity = targetActivity;
	    while (precedingActivity.getTransitionTo() != null) {
		precedingActivity = precedingActivity.getTransitionTo().getFromActivity();
		if (!learnerProgress.getCompletedActivities().containsKey(precedingActivity)) {
		    precedingUncompleteActivity = precedingActivity;
		}
	    }

	    if (precedingUncompleteActivity != null) {
		return forceCompleteActivity(learner, learnerProgress.getLesson().getLessonId(), learnerProgress,
			precedingUncompleteActivity, previousActivity, new ArrayList<Long>());
	    }
	}

	return messageService.getMessage(MonitoringService.FORCE_COMPLETE_STOP_MESSAGE_COMPLETED_TO_ACTIVITY,
		new Object[] { targetActivity.getTitle() });
    }

    @Override
    public String getLessonLearners(Long lessonID, Integer userID) throws IOException {
	securityService.isLessonMonitor(lessonID, userID, "get lesson learners", true);
	Lesson lesson = lessonDAO.getLesson(lessonID);

	Vector lessonLearners = new Vector();
	FlashMessage flashMessage;
	if (lesson != null) {
	    Iterator iterator = lesson.getLessonClass().getLearners().iterator();
	    while (iterator.hasNext()) {
		User user = (User) iterator.next();
		lessonLearners.add(user.getUserFlashDTO());
	    }
	    flashMessage = new FlashMessage("getLessonLearners", lessonLearners);
	} else {
	    flashMessage = new FlashMessage("getLessonLearners", messageService.getMessage("NO.SUCH.LESSON",
		    new Object[] { lessonID }), FlashMessage.ERROR);
	}
	return flashMessage.serializeMessage();
    }

    @Override
    public String getLessonStaff(Long lessonID, Integer userID) throws IOException {
	securityService.isLessonMonitor(lessonID, userID, "get lesson staff", true);
	Lesson lesson = lessonDAO.getLesson(lessonID);

	Vector lessonStaff = new Vector();
	FlashMessage flashMessage;
	if (lesson != null) {
	    Iterator iterator = lesson.getLessonClass().getStaffGroup().getUsers().iterator();
	    while (iterator.hasNext()) {
		User user = (User) iterator.next();
		lessonStaff.add(user.getUserFlashDTO());
	    }
	    flashMessage = new FlashMessage("getLessonStaff", lessonStaff);
	} else {
	    flashMessage = new FlashMessage("getLessonStaff", messageService.getMessage("NO.SUCH.LESSON",
		    new Object[] { lessonID }), FlashMessage.ERROR);
	}
	return flashMessage.serializeMessage();
    }

    @Override
    public Collection<User> getUsersByEmailNotificationSearchType(int searchType, Long lessonId, String[] lessonIds,
	    Long activityId, Integer xDaystoFinish, Integer orgId) {

	Lesson lesson = null;
	if (lessonId != null) {
	    lesson = learnerService.getLesson(lessonId);
	}

	Collection<User> users = new LinkedList<User>();
	switch (searchType) {
	case MonitoringConstants.LESSON_TYPE_ASSIGNED_TO_LESSON:
	    users = lesson.getAllLearners();
	    break;

	case MonitoringConstants.LESSON_TYPE_HAVENT_FINISHED_LESSON:
	    Set<User> allUsers = lesson.getAllLearners();
	    List<User> usersCompletedLesson = getUsersCompletedLesson(lessonId);
	    users = CollectionUtils.subtract(allUsers, usersCompletedLesson);
	    break;

	case MonitoringConstants.LESSON_TYPE_HAVE_FINISHED_LESSON:
	case MonitoringConstants.COURSE_TYPE_HAVE_FINISHED_PARTICULAR_LESSON:
	    users = getUsersCompletedLesson(lessonId);
	    break;

	case MonitoringConstants.LESSON_TYPE_HAVENT_STARTED_LESSON:
	case MonitoringConstants.COURSE_TYPE_HAVENT_STARTED_PARTICULAR_LESSON:
	    allUsers = lesson.getAllLearners();
	    List<User> usersStartedLesson = lessonService.getActiveLessonLearners(lessonId);
	    users = CollectionUtils.subtract(allUsers, usersStartedLesson);
	    break;

	case MonitoringConstants.LESSON_TYPE_HAVE_STARTED_LESSON:
	    users = lessonService.getActiveLessonLearners(lessonId);
	    break;

	case MonitoringConstants.LESSON_TYPE_HAVENT_REACHED_PARTICULAR_ACTIVITY:
	    Activity activity = learnerService.getActivity(activityId);
	    allUsers = lesson.getAllLearners();
	    List<User> usersAttemptedActivity = lessonService.getLearnersHaveAttemptedActivity(activity);
	    users = CollectionUtils.subtract(allUsers, usersAttemptedActivity);
	    break;

	case MonitoringConstants.LESSON_TYPE_LESS_THAN_X_DAYS_TO_DEADLINE:
	    Date now = new Date();
	    Calendar currentTimePlusXDays = Calendar.getInstance();
	    currentTimePlusXDays.setTime(now);
	    currentTimePlusXDays.add(Calendar.DATE, xDaystoFinish);

	    Date scheduleEndDate = lesson.getScheduleEndDate();
	    if (scheduleEndDate != null) {
		if (now.before(scheduleEndDate) && currentTimePlusXDays.getTime().after(scheduleEndDate)) {
		    users = lesson.getAllLearners();
		}

	    } else if (lesson.isScheduledToCloseForIndividuals()) {
		users = groupUserDAO.getUsersWithLessonEndingSoonerThan(lesson, currentTimePlusXDays.getTime());
	    }
	    break;

	case MonitoringConstants.COURSE_TYPE_HAVENT_STARTED_ANY_LESSONS:
	    List<User> allUSers = learnerService.getUserManagementService().getUsersFromOrganisation(orgId);
	    Set<User> usersStartedAtLest1Lesson = new TreeSet<User>();

	    Organisation org = (Organisation) learnerService.getUserManagementService().findById(Organisation.class,
		    orgId);
	    Set<Lesson> lessons = org.getLessons();
	    for (Lesson les : lessons) {
		Activity firstActivity = les.getLearningDesign().getFirstActivity();
		List<User> usersStartedFirstActivity = learnerProgressDAO
			.getLearnersHaveAttemptedActivity(firstActivity);
		usersStartedAtLest1Lesson.addAll(usersStartedFirstActivity);
	    }

	    users = CollectionUtils.subtract(allUSers, usersStartedAtLest1Lesson);

	    break;

	case MonitoringConstants.COURSE_TYPE_HAVE_FINISHED_THESE_LESSONS:
	    int i = 0;
	    for (String lessonIdStr : lessonIds) {
		lessonId = Long.parseLong(lessonIdStr);
		List<User> completedLesson = getUsersCompletedLesson(lessonId);
		if (i++ == 0) {
		    users = completedLesson;
		} else {
		    users = CollectionUtils.intersection(users, completedLesson);
		}
	    }
	    break;

	case MonitoringConstants.COURSE_TYPE_HAVENT_FINISHED_THESE_LESSONS:
	    users = new TreeSet<User>();

	    // add all available users from selected lessons
	    for (String lessonIdStr : lessonIds) {
		lessonId = Long.parseLong(lessonIdStr);
		lesson = learnerService.getLesson(lessonId);
		users.addAll(lesson.getAllLearners());
	    }

	    // subtract the ones which have completed any of the selected lessons
	    for (String lessonIdStr : lessonIds) {
		lessonId = Long.parseLong(lessonIdStr);
		List<User> completedLesson = getUsersCompletedLesson(lessonId);
		users = CollectionUtils.subtract(users, completedLesson);
	    }
	    break;
	}

	Set<User> sortedUsers = new TreeSet<User>(new Comparator<User>() {
	    @Override
	    public int compare(User usr0, User usr1) {
		return ((usr0.getFirstName() + usr0.getLastName() + usr0.getLogin()).compareTo(usr1.getFirstName()
			+ usr1.getLastName() + usr1.getLogin()));
	    }
	});
	sortedUsers.addAll(users);

	return sortedUsers;
    }

    /**
     * Returns list of users who has already finished specified lesson.
     * 
     * @param lessonId
     *            specified lesson
     * @return
     */
    private List<User> getUsersCompletedLesson(Long lessonId) {
	List<User> usersCompletedLesson = new LinkedList<User>();

	List<LearnerProgress> completedLearnerProgresses = learnerProgressDAO
		.getCompletedLearnerProgressForLesson(lessonId);
	for (LearnerProgress learnerProgress : completedLearnerProgresses) {
	    usersCompletedLesson.add(learnerProgress.getUser());
	}
	return usersCompletedLesson;
    }

    @Override
    public String getLearningDesignDetails(Long lessonID) throws IOException {
	Lesson lesson = lessonDAO.getLesson(lessonID);
	return authoringService.getLearningDesignDetails(lesson.getLearningDesign().getLearningDesignId(), "");
    }

    @Override
    public Activity getActivityById(Long activityId) {
	return activityDAO.getActivityByActivityId(activityId);
    }

    @Override
    public Activity getActivityById(Long activityId, Class clasz) {
	return activityDAO.getActivityByActivityId(activityId, clasz);
    }

    @Override
    public GroupingActivity getGroupingActivityById(Long activityID) {
	Activity activity = getActivityById(activityID);
	if (activity == null) {
	    String error = "Activity missing. ActivityID was " + activityID;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	} else if (!activity.isGroupingActivity()) {
	    String error = "Activity should have been GroupingActivity but was a different kind of activity. "
		    + activity;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	return (GroupingActivity) activity;
    }

    @Override
    public List<ContributeActivityDTO> getAllContributeActivityDTO(Long lessonID) {
	List<ContributeActivityDTO> result = null;
	Lesson lesson = lessonDAO.getLesson(lessonID);
	if (lesson != null) {
	    ContributeActivitiesProcessor processor = new ContributeActivitiesProcessor(lesson.getLearningDesign(),
		    lessonID, activityDAO, lamsCoreToolService);
	    processor.parseLearningDesign();
	    result = processor.getMainActivityList();
	}
	return result;
    }

    @Override
    public String getLearnerActivityURL(Long lessonID, Long activityID, Integer learnerUserID, Integer requestingUserId)
	    throws IOException, LamsToolServiceException {
	securityService.isLessonMonitor(lessonID, requestingUserId, "get learner activity URL", true);
	Lesson lesson = lessonDAO.getLesson(lessonID);

	Activity activity = activityDAO.getActivityByActivityId(activityID);
	User learner = (User) baseDAO.find(User.class, learnerUserID);

	String url = null;
	if ((activity == null) || (learner == null)) {
	    MonitoringService.log.error("getLearnerActivityURL activity or user missing. Activity ID " + activityID
		    + " activity " + activity + " userID " + learnerUserID + " user " + learner);
	} else if (activity.isToolActivity()) {
	    url = lamsCoreToolService.getToolLearnerProgressURL(lessonID, activity, learner);
	} else if (activity.isOptionsActivity() || activity.isParallelActivity()) {
	    url = "monitoring/complexProgress.do?" + AttributeNames.PARAM_ACTIVITY_ID + "=" + activityID + "&"
		    + AttributeNames.PARAM_LESSON_ID + "=" + lessonID + "&" + AttributeNames.PARAM_USER_ID + "="
		    + learnerUserID;
	} else if (activity.isSystemToolActivity()) {
	    url = lamsCoreToolService.getToolLearnerProgressURL(lessonID, activity, learner);
	}
	MonitoringService.log.debug("url: " + url);
	return url;
    }

    @Override
    public String getActivityMonitorURL(Long lessonID, Long activityID, String contentFolderID, Integer userID)
	    throws IOException, LamsToolServiceException {
	securityService.isLessonMonitor(lessonID, userID, "get activity monitor URL", true);
	Lesson lesson = lessonDAO.getLesson(lessonID);

	Activity activity = activityDAO.getActivityByActivityId(activityID);

	if (activity == null) {
	    MonitoringService.log.error("getActivityMonitorURL activity missing. Activity ID " + activityID
		    + " activity " + activity);

	} else if (activity.isToolActivity() || activity.isSystemToolActivity()) {
	    return lamsCoreToolService.getToolMonitoringURL(lessonID, activity) + "&contentFolderID=" + contentFolderID;
	}
	return null;
    }

    @Override
    public String moveLesson(Long lessonID, Integer targetWorkspaceFolderID, Integer userID) throws IOException {
	Lesson lesson = lessonDAO.getLesson(lessonID);
	FlashMessage flashMessage;
	if (lesson != null) {
	    if (lesson.getUser().getUserId().equals(userID)) {
		WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class,
			targetWorkspaceFolderID);
		if (workspaceFolder != null) {
		    LearningDesign learningDesign = lesson.getLearningDesign();
		    learningDesign.setWorkspaceFolder(workspaceFolder);
		    learningDesignDAO.update(learningDesign);
		    flashMessage = new FlashMessage("moveLesson", targetWorkspaceFolderID);
		} else {
		    flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists("moveLesson", targetWorkspaceFolderID);
		}
	    } else {
		flashMessage = FlashMessage.getUserNotAuthorized("moveLesson", userID);
	    }
	} else {
	    flashMessage = new FlashMessage("moveLesson", messageService.getMessage("NO.SUCH.LESSON",
		    new Object[] { lessonID }), FlashMessage.ERROR);

	}
	return flashMessage.serializeMessage();

    }

    @Override
    public String releaseGate(Long activityID) throws IOException {
	GateActivity gate = (GateActivity) activityDAO.getActivityByActivityId(activityID);
	FlashMessage flashMessage;
	if (gate == null) {
	    flashMessage = new FlashMessage("releaseGate", messageService.getMessage("INVALID.ACTIVITYID",
		    new Object[] { activityID }), FlashMessage.ERROR);
	} else {
	    // release gate
	    gate = openGate(activityID);

	    flashMessage = new FlashMessage("releaseGate", gate.getGateOpen());

	}
	return flashMessage.serializeMessage();

    }

    @Override
    public void performChosenGrouping(GroupingActivity groupingActivity, List groups) throws LessonServiceException {
	Grouping grouping = groupingActivity.getCreateGrouping();

	if (!grouping.isChosenGrouping()) {
	    MonitoringService.log.error("GroupingActivity [" + groupingActivity.getActivityId()
		    + "] does not have chosen grouping.");
	    throw new MonitoringServiceException("GroupingActivity [" + groupingActivity.getActivityId()
		    + "] is not chosen grouping.");
	}
	try {
	    // try to sorted group list by orderID.
	    Iterator iter = groups.iterator();
	    Map sortedMap = new TreeMap(new Comparator() {
		@Override
		public int compare(Object arg0, Object arg1) {
		    return ((Long) arg0).compareTo((Long) arg1);
		}
	    });
	    while (iter.hasNext()) {
		Hashtable group = (Hashtable) iter.next();
		Long orderId = WDDXProcessor.convertToLong(group, MonitoringConstants.KEY_GROUP_ORDER_ID);
		sortedMap.put(orderId, group);
	    }
	    iter = sortedMap.values().iterator();
	    // grouping all group in list
	    for (int orderId = 0; iter.hasNext(); orderId++) {
		Hashtable group = (Hashtable) iter.next();
		List learnerIdList = (List) group.get(MonitoringConstants.KEY_GROUP_LEARNERS);
		String groupName = WDDXProcessor.convertToString(group, MonitoringConstants.KEY_GROUP_NAME);
		List learners = new ArrayList();
		// ? Seem too low efficient, is there a easy way?
		for (int idx = 0; idx < learnerIdList.size(); idx++) {
		    User user = (User) baseDAO.find(User.class, ((Double) learnerIdList.get(idx)).intValue());
		    learners.add(user);

		}
		MonitoringService.log.debug("Performing grouping for " + groupName + "...");
		lessonService.performGrouping(groupingActivity, groupName, learners);
		MonitoringService.log.debug("Finish grouping for " + groupName);
	    }

	    MonitoringService.log.debug("Persist grouping for [" + grouping.getGroupingId() + "]...");
	    groupingDAO.update(grouping);
	    MonitoringService.log.debug("Persist grouping for [" + grouping.getGroupingId() + "] success.");

	} catch (WDDXProcessorConversionException e) {
	    throw new MonitoringServiceException("Perform chosen grouping occurs error when parsing WDDX package:"
		    + e.getMessage());
	}

    }

    // ---------------------------------------------------------------------
    // Helper Methods - create lesson
    // ---------------------------------------------------------------------
    /**
     * Create a new lesson and setup all the staffs and learners who will be participating this less.
     * 
     * @param organisation
     *            the organization this lesson belongs to.
     * @param organizationUsers
     *            a list of learner will be in this new lessons.
     * @param staffs
     *            a list of staffs who will be in charge of this lesson.
     * @param newLesson
     */
    private LessonClass createLessonClass(Organisation organisation, String learnerGroupName,
	    List<User> organizationUsers, String staffGroupName, List<User> staffs, Lesson newLesson) {
	// create a new lesson class object
	LessonClass newLessonClass = createNewLessonClass(newLesson.getLearningDesign());
	lessonClassDAO.saveLessonClass(newLessonClass);

	// setup staff group
	newLessonClass.setStaffGroup(Group.createStaffGroup(newLessonClass, staffGroupName, new HashSet(staffs)));
	// setup learner group
	// TODO:need confirm group name!
	newLessonClass.getGroups().add(
		Group.createLearnerGroup(newLessonClass, learnerGroupName, new HashSet(organizationUsers)));

	lessonClassDAO.updateLessonClass(newLessonClass);

	return newLessonClass;
    }

    /**
     * Setup a new lesson object without class and insert it into the database.
     * 
     * @param lessonName
     *            the name of the lesson
     * @param lessonDescription
     *            the description of the lesson.
     * @param user
     *            user the user who want to create this lesson.
     * @param learnerExportAvailable
     *            should the export portfolio option be made available to the learner?
     * @param copiedLearningDesign
     *            the copied learning design
     * @param enableLessonNotifications
     *            enable "email notifications" link for the current lesson
     * 
     * @return the lesson object without class.
     * 
     */
    private Lesson createNewLesson(String lessonName, String lessonDescription, User user,
	    LearningDesign copiedLearningDesign, Boolean enableLessonIntro, Boolean displayDesignImage,
	    Boolean learnerExportAvailable, Boolean learnerPresenceAvailable, Boolean learnerImAvailable,
	    Boolean liveEditEnabled, Boolean enableLessonNotifications, Boolean learnerRestart,
	    Integer scheduledNumberDaysToLessonFinish, Lesson precedingLesson) {
	Lesson newLesson = Lesson.createNewLessonWithoutClass(lessonName, lessonDescription, user,
		copiedLearningDesign, enableLessonIntro, displayDesignImage, learnerExportAvailable,
		learnerPresenceAvailable, learnerImAvailable, liveEditEnabled, enableLessonNotifications,
		learnerRestart, scheduledNumberDaysToLessonFinish);
	if (precedingLesson != null) {
	    HashSet precedingLessons = new HashSet();
	    precedingLessons.add(precedingLesson);
	    newLesson.setPrecedingLessons(precedingLessons);
	}

	lessonDAO.saveLesson(newLesson);
	return newLesson;
    }

    /**
     * Setup the empty lesson class according to the run-time learning design copy.
     * 
     * @param copiedLearningDesign
     *            the run-time learning design instance.
     * @return the new empty lesson class.
     */
    private LessonClass createNewLessonClass(LearningDesign copiedLearningDesign) {
	// make a copy of lazily initialized activities
	Set activities = new HashSet(copiedLearningDesign.getActivities());
	LessonClass newLessonClass = new LessonClass(null, // grouping id
		new HashSet(),// groups
		activities, null, // staff group
		null);// lesson
	return newLessonClass;
    }

    // ---------------------------------------------------------------------
    // Helper Methods - scheduling
    // ---------------------------------------------------------------------

    /**
     * Returns the bean that defines the open schedule gate job.
     */
    private JobDetail getOpenScheduleGateJob() {
	return (JobDetail) applicationContext.getBean("openScheduleGateJob");
    }

    /**
     * 
     * @return the bean that defines start lesson on schedule job.
     */
    private JobDetail getStartScheduleLessonJob() {
	return (JobDetail) applicationContext.getBean(MonitoringConstants.JOB_START_LESSON);
    }

    /**
     * 
     * @return the bean that defines start lesson on schedule job.
     */
    private JobDetail getFinishScheduleLessonJob() {
	return (JobDetail) applicationContext.getBean(MonitoringConstants.JOB_FINISH_LESSON);
    }

    /**
     * Returns the bean that defines the close schdule gate job.
     */
    private JobDetail getCloseScheduleGateJob() {
	return (JobDetail) applicationContext.getBean("closeScheduleGateJob");
    }

    // ---------------------------------------------------------------------
    // Preview related methods
    // ---------------------------------------------------------------------

    @Override
    public Lesson createPreviewClassForLesson(int userID, long lessonID) throws UserAccessDeniedException {

	User user = (User) baseDAO.find(User.class, userID);
	if (user == null) {
	    throw new UserAccessDeniedException("User " + userID + " not found");
	}

	// create the lesson class - add the teacher as the learner and as staff
	LinkedList<User> learners = new LinkedList<User>();
	learners.add(user);

	LinkedList<User> staffs = new LinkedList<User>();
	staffs.add(user);

	return createLessonClassForLesson(lessonID, null, "Learner Group", learners, "Staff Group", staffs, userID);
    }

    /* Grouping and branching related calls */

    @Override
    // TODO Optimise the database query. Do a single query rather then large collection access
    public SortedSet<User> getClassMembersNotGrouped(Long lessonID, Long activityID, boolean useCreateGrouping) {
	Activity activity = getActivityById(activityID);
	Grouping grouping = getGroupingForActivity(activity, useCreateGrouping, "getClassMembersNotGrouped");

	// get all the learners in the class, irrespective of whether they have
	// joined the lesson or not.
	// then go through each group and remove the grouped users from the
	// activeLearners set.
	Lesson lesson = lessonDAO.getLesson(lessonID);
	if (lesson == null) {
	    String error = "Lesson missing. LessonID was " + lessonID + " Activity id was " + activityID;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	Set learners = lesson.getAllLearners();
	if (MonitoringService.log.isDebugEnabled()) {
	    MonitoringService.log.debug("getClassMembersNotGrouped: Lesson " + lessonID + " has " + learners.size()
		    + " learners.");
	}

	Iterator iter = grouping.getGroups().iterator();
	while (iter.hasNext()) {
	    Group group = (Group) iter.next();
	    learners.removeAll(group.getUsers());
	    if (MonitoringService.log.isDebugEnabled()) {
		MonitoringService.log.debug("getClassMembersNotGrouped: Group " + group.getGroupId() + " has "
			+ group.getUsers().size() + " members.");
	    }
	}

	if (MonitoringService.log.isDebugEnabled()) {
	    MonitoringService.log.debug("getClassMembersNotGrouped: Lesson " + lessonID + " has " + learners.size()
		    + " learners.");
	}

	SortedSet sortedUsers = new TreeSet(new LastNameAlphabeticComparator());
	sortedUsers.addAll(learners);
	return sortedUsers;
    }

    /**
     * Get the grouping appropriate for this activity.
     * 
     * If the activity is a grouping activity, then set useCreatingGrouping = true to base the list on the create
     * grouping. Otherwise leave it false and it will use the grouping applied to the activity - this is used for
     * branching activities.
     * 
     * If it is a teacher chosen branching activity and the grouping doesn't exist, it creates one.
     */
    private Grouping getGroupingForActivity(Activity activity, boolean useCreateGrouping, String methodName) {
	if (useCreateGrouping && ((activity == null) || !activity.isGroupingActivity())) {
	    String error = methodName
		    + ": Trying to use the create grouping option but the activity isn't a grouping activity. Activity was "
		    + activity;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	Grouping grouping = null;

	if (useCreateGrouping) {
	    GroupingActivity groupingActivity = (GroupingActivity) activity;
	    grouping = groupingActivity.getCreateGrouping();
	} else {
	    grouping = activity.getGrouping();
	}

	if (grouping == null) {
	    String error = methodName + ": Grouping activity missing grouping. Activity was " + activity;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}
	return grouping;
    }

    @Override
    public Group addGroup(Long activityID, String name, boolean overrideMaxNumberOfGroups)
	    throws LessonServiceException, MonitoringServiceException {
	Activity activity = getActivityById(activityID);
	Grouping grouping = getGroupingForActivity(activity, true, "addGroup");
	if (overrideMaxNumberOfGroups) {
	    // Is this grouping used for branching. If it is, must honour the
	    // groups
	    // set in authoring or some groups won't have a branch.
	    if ((grouping.getMaxNumberOfGroups() != null) && (grouping.getMaxNumberOfGroups() > 0)
		    && (grouping.getGroups() != null)
		    && (grouping.getGroups().size() >= grouping.getMaxNumberOfGroups())) {
		boolean usedForBranching = grouping.isUsedForBranching();
		if (!usedForBranching) {
		    MonitoringService.log
			    .info("Setting max number of groups to null for grouping "
				    + grouping
				    + " we have been asked to add a group in excess of the max number of groups (probably via the Chosen Grouping screen).");
		    grouping.setMaxNumberOfGroups(null); // must be null and not
		    // 0 or the groups will
		    // be lost via Live
		    // Edit.
		} else {
		    MonitoringService.log
			    .error("Request made to add a group which would be more than the max number of groups for the grouping "
				    + grouping
				    + ". This grouping is used for branching so we can't increase the max group number.");
		    throw new MonitoringServiceException("Cannot increase the number of groups for the grouping "
			    + grouping + " as this grouping is used for a branching activity.");
		}
	    }
	}
	return lessonService.createGroup(grouping, name);
    }

    @Override
    public void removeGroup(Long activityID, Long groupId) throws LessonServiceException {
	Activity activity = getActivityById(activityID);
	Grouping grouping = getGroupingForActivity(activity, true, "removeGroup");
	lessonService.removeGroup(grouping, groupId);
    }

    @Override
    public void addUsersToGroup(Long activityID, Long groupId, String learnerIDs[]) throws LessonServiceException {
	Activity activity = getActivityById(activityID);
	Grouping grouping = getGroupingForActivity(activity, true, "addUsersToGroup");
	ArrayList<User> learners = createUserList(activityID, learnerIDs, "add");
	lessonService.performGrouping(grouping, groupId, learners);
    }

    private ArrayList<User> createUserList(Long activityIDForErrorMessage, String[] learnerIDs,
	    String addRemoveTextForErrorMessage) {
	ArrayList<User> learners = new ArrayList<User>();
	for (String strlearnerID : learnerIDs) {
	    boolean added = false;
	    try {
		Integer learnerID = new Integer(Integer.parseInt(strlearnerID));
		User learner = (User) baseDAO.find(User.class, learnerID);
		if (learner != null) {
		    learners.add(learner);
		    added = true;
		}
	    } catch (NumberFormatException e) {
	    }
	    if (!added) {
		MonitoringService.log.warn("Unable to " + addRemoveTextForErrorMessage + " learner " + strlearnerID
			+ " for group in related to activity " + activityIDForErrorMessage
			+ " as learner cannot be found.");
	    }
	}
	return learners;
    }

    @Override
    public void addUsersToBranch(Long sequenceActivityID, String learnerIDs[]) throws LessonServiceException {

	SequenceActivity branch = (SequenceActivity) getActivityById(sequenceActivityID);
	if (branch == null) {
	    String error = "addUsersToBranch: Branch missing. ActivityID was " + sequenceActivityID;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	Group group = branch.getSoleGroupForBranch();
	Grouping grouping = null;
	if (group == null) {
	    // create a new group and a matching mapping entry
	    Activity parentActivity = branch.getParentActivity();
	    if ((parentActivity == null) || !parentActivity.isBranchingActivity()) {
		String error = "addUsersToBranch: Branching activity missing or not a branching activity. Branch was "
			+ branch + " parent activity was " + parentActivity;
		MonitoringService.log.error(error);
		throw new MonitoringServiceException(error);
	    }
	    BranchingActivity branchingActivity = (BranchingActivity) getActivityById(parentActivity.getActivityId());
	    grouping = branchingActivity.getGrouping();

	    // Need the learning design to get the next uiid - which is needed
	    // if
	    // Live Edit is done, or Flash can't match the branch to the groups
	    // properly.
	    LearningDesign design = branchingActivity.getLearningDesign();

	    group = lessonService.createGroup(grouping, branch.getTitle());
	    groupingDAO.insert(group);
	    Integer nextUIID = new Integer(design.getMaxID().intValue() + 1);
	    group.setGroupUIID(nextUIID);
	    nextUIID = new Integer(nextUIID.intValue() + 1);
	    group.allocateBranchToGroup(nextUIID, branch, branchingActivity);
	    groupingDAO.update(group);

	    design.setMaxID(new Integer(nextUIID.intValue() + 1));
	    learningDesignDAO.update(design);

	} else {
	    grouping = group.getGrouping();
	}

	ArrayList<User> learners = createUserList(sequenceActivityID, learnerIDs, "add");
	lessonService.performGrouping(grouping, group.getGroupId(), learners);
    }

    @Override
    public void removeUsersFromGroup(Long activityID, Long groupId, String learnerIDs[]) throws LessonServiceException {
	Activity activity = getActivityById(activityID);
	Grouping grouping = getGroupingForActivity(activity, true, "removeUsersFromGroup");
	ArrayList<User> learners = createUserList(activityID, learnerIDs, "remove");
	lessonService.removeLearnersFromGroup(grouping, groupId, learners);
    }

    @Override
    public void removeUsersFromBranch(Long sequenceActivityID, String learnerIDs[]) throws LessonServiceException {

	SequenceActivity branch = (SequenceActivity) getActivityById(sequenceActivityID);
	if (branch == null) {
	    String error = "addUsersToBranch: Branch missing. ActivityID was " + sequenceActivityID;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	Group group = branch.getSoleGroupForBranch();
	Grouping grouping = null;
	if (group != null) {
	    grouping = group.getGrouping();
	    ArrayList<User> learners = createUserList(sequenceActivityID, learnerIDs, "remove");
	    lessonService.removeLearnersFromGroup(grouping, group.getGroupId(), learners);
	} else {
	    MonitoringService.log.warn("Trying to remove users " + learnerIDs + " from branch " + branch
		    + " but no group exists for this branch, so the users can't be in the group!");
	}

    }

    @Override
    public void addGroupToBranch(Long sequenceActivityID, String groupIDs[]) throws LessonServiceException {

	SequenceActivity branch = (SequenceActivity) getActivityById(sequenceActivityID);
	if (branch == null) {
	    String error = "addGroupToBranch: Branch missing. ActivityID was " + sequenceActivityID;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	Activity parentActivity = branch.getParentActivity();
	if ((parentActivity == null) || !parentActivity.isBranchingActivity()) {
	    String error = "addUsersToBranch: Branching activity missing or not a branching activity. Branch was "
		    + branch + " parent activity was " + parentActivity;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}
	BranchingActivity branchingActivity = (BranchingActivity) getActivityById(parentActivity.getActivityId());
	Grouping grouping = branchingActivity.getGrouping();

	LearningDesign design = branchingActivity.getLearningDesign();
	Integer nextUIID = new Integer(design.getMaxID().intValue() + 1);

	for (String groupIDString : groupIDs) {
	    Long groupID = Long.parseLong(groupIDString);

	    Group group = null;
	    Iterator groupIterator = grouping.getGroups().iterator();
	    while (groupIterator.hasNext() && (group == null)) {
		Group obj = (Group) groupIterator.next();
		if (obj.getGroupId().equals(groupID)) {
		    group = obj;
		}
	    }
	    if (group == null) {
		String error = "addGroupToBranch: Group missing. Group ID was " + groupIDString;
		MonitoringService.log.error(error);
		throw new MonitoringServiceException(error);
	    }

	    group.allocateBranchToGroup(nextUIID, branch, branchingActivity);
	    groupingDAO.update(group);
	}

	design.setMaxID(new Integer(nextUIID.intValue() + 1));
	learningDesignDAO.update(design);

    }

    @Override
    public void removeGroupFromBranch(Long sequenceActivityID, String groupIDs[]) throws LessonServiceException {

	SequenceActivity branch = (SequenceActivity) getActivityById(sequenceActivityID);
	if (branch == null) {
	    String error = "addUsersToBranch: Branch missing. ActivityID was " + sequenceActivityID;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	Activity parentActivity = branch.getParentActivity();
	if ((parentActivity == null) || !parentActivity.isBranchingActivity()) {
	    String error = "addUsersToBranch: Branching activity missing or not a branching activity. Branch was "
		    + branch + " parent activity was " + parentActivity;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}
	BranchingActivity branchingActivity = (BranchingActivity) getActivityById(parentActivity.getActivityId());
	Grouping grouping = branchingActivity.getGrouping();

	for (String groupIDString : groupIDs) {
	    Long groupID = Long.parseLong(groupIDString);

	    Group group = null;
	    Iterator groupIterator = grouping.getGroups().iterator();
	    while (groupIterator.hasNext() && (group == null)) {
		Group obj = (Group) groupIterator.next();
		if (obj.getGroupId().equals(groupID)) {
		    group = obj;
		}
	    }
	    if (group == null) {
		String error = "removeGroupFromBranch: Group missing. Group ID was " + groupIDString;
		MonitoringService.log.error(error);
		throw new MonitoringServiceException(error);
	    }

	    // can't remove the group if someone has already started working on
	    // the branch.
	    if (isActivityAttempted(branch)) {
		MonitoringService.log
			.warn("removeGroupFromBranch: A group member has already started the branch. Unable to remove the group from the branch. Group ID was "
				+ groupIDString);
	    } else {
		branch.removeGroupFromBranch(group);
		activityDAO.update(branch);
	    }
	}

    }

    @Override
    public boolean isActivityAttempted(Activity activity) {
	Integer numAttempted = lessonService.getCountLearnersHaveAttemptedActivity(activity);
	if (MonitoringService.log.isDebugEnabled()) {
	    MonitoringService.log.debug("isActivityAttempted: num attempts for activity " + activity.getActivityId()
		    + " is " + numAttempted);
	}
	return (numAttempted != null) && (numAttempted.intValue() > 0);
    }

    @Override
    public SortedSet<Group> getGroupsNotAssignedToBranch(Long branchingActivityID) throws LessonServiceException {

	BranchingActivity branchingActivity = (BranchingActivity) getActivityById(branchingActivityID);
	if (branchingActivity == null) {
	    String error = "getGroupsNotAssignedToBranch: Branching Activity missing missing. ActivityID was "
		    + branchingActivityID;
	    MonitoringService.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	TreeSet<Group> unassignedGroups = new TreeSet<Group>();

	Grouping grouping = branchingActivity.getGrouping();
	Iterator groupIterator = grouping.getGroups().iterator();
	while (groupIterator.hasNext()) {
	    Group group = (Group) groupIterator.next();
	    if ((group.getBranchActivities() == null) || (group.getBranchActivities().size() == 0)) {
		unassignedGroups.add(group);
	    }
	}

	return unassignedGroups;

    }

    @Override
    public List<User> getLearnersHaveAttemptedActivity(Activity activity) throws LessonServiceException {
	return lessonService.getLearnersHaveAttemptedActivity(activity);
    }

    @Override
    public LearnerProgress getLearnerProgress(Integer learnerId, Long lessonId) {
	return learnerService.getProgress(learnerId, lessonId);
    }

    @Override
    public void setGroupName(Long groupID, String name) {
	Group group = groupDAO.getGroupById(groupID);
	group.setGroupName(name);
	groupDAO.saveGroup(group);
    }

    @Override
    public Organisation getOrganisation(Integer organisationId) {
	return (Organisation) baseDAO.find(Organisation.class, organisationId);
    }

    @Override
    public int cloneLessons(String[] lessonIds, Boolean addAllStaff, Boolean addAllLearners, String[] staffIds,
	    String[] learnerIds, Organisation group) throws MonitoringServiceException {
	int result = 0;
	for (String l : lessonIds) {
	    Lesson lesson = lessonService.getLesson(Long.valueOf(l));
	    if (lesson != null) {
		HttpSession ss = SessionManager.getSession();
		if (ss != null) {
		    UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
		    if (userDto != null) {
			if ((!addAllStaff && (staffIds.length > 0)) || addAllStaff) {
			    // create staff LessonClass
			    String staffGroupName = group.getName() + " Staff";
			    List<User> staffUsers = createStaffGroup(group.getOrganisationId(), addAllStaff, staffIds);

			    if ((!addAllLearners && (learnerIds.length > 0)) || addAllLearners) {
				// create learner LessonClass for lesson
				String learnerGroupName = group.getName() + " Learners";
				List<User> learnerUsers = createLearnerGroup(group.getOrganisationId(), addAllLearners,
					learnerIds);

				// init Lesson with user as creator
				Lesson newLesson = this.initializeLesson(lesson.getLessonName(),
					lesson.getLessonDescription(),
					lesson.getLearningDesign().getLearningDesignId(), group.getOrganisationId(),
					userDto.getUserID(), null, lesson.isEnableLessonIntro(),
					lesson.isDisplayDesignImage(), lesson.getLearnerExportAvailable(),
					lesson.getLearnerPresenceAvailable(), lesson.getLearnerImAvailable(),
					lesson.getLiveEditEnabled(), lesson.getEnableLessonNotifications(),
					lesson.getLearnerRestart(), null, null);

				// save LessonClasses
				newLesson = this
					.createLessonClassForLesson(newLesson.getLessonId(), group, learnerGroupName,
						learnerUsers, staffGroupName, staffUsers, userDto.getUserID());

				// start Lessons
				// TODO user-specified creator; must be someone in staff group
				this.startLesson(newLesson.getLessonId(), staffUsers.get(0).getUserId());

				result++;
			    } else {
				throw new MonitoringServiceException("No learners specified, can't create any Lessons.");
			    }
			} else {
			    throw new MonitoringServiceException("No staff specified, can't create any Lessons.");
			}
		    } else {
			throw new MonitoringServiceException("No UserDTO in session, can't create any Lessons.");
		    }
		}
	    } else {
		throw new MonitoringServiceException("Couldn't find Lesson based on id=" + l);
	    }
	}
	return result;
    }

    /*
     * Used in cloneLessons.
     */
    private List<User> createLearnerGroup(Integer groupId, Boolean addAllLearners, String[] learnerIds) {
	List<User> learnerUsers = new ArrayList<User>();
	if (addAllLearners) {
	    Vector learnerVector = userManagementService.getUsersFromOrganisationByRole(groupId, Role.LEARNER, false,
		    true);
	    learnerUsers.addAll(learnerVector);
	} else {
	    User user = null;
	    for (String l : learnerIds) {
		user = (User) userManagementService.findById(User.class, Integer.parseInt(l));
		if (user != null) {
		    learnerUsers.add(user);
		} else {
		    MonitoringService.log.error("Couldn't find User based on id=" + l);
		}
	    }
	}
	return learnerUsers;
    }

    /*
     * Used in cloneLessons.
     */
    private List<User> createStaffGroup(Integer groupId, Boolean addAllStaff, String[] staffIds) {
	List<User> staffUsers = new ArrayList<User>();
	if (addAllStaff) {
	    Vector staffVector = userManagementService.getUsersFromOrganisationByRole(groupId, Role.MONITOR, false,
		    true);
	    staffUsers.addAll(staffVector);
	} else {
	    User user = null;
	    for (String s : staffIds) {
		user = (User) userManagementService.findById(User.class, Integer.parseInt(s));
		if (user != null) {
		    staffUsers.add(user);
		} else {
		    MonitoringService.log.error("Couldn't find User based on id=" + s);
		}
	    }
	}
	return staffUsers;
    }

    /**
     * Write out audit log entry
     * 
     * @param messageKey
     * @param args
     */
    private void writeAuditLog(String messageKey, Object[] args) {
	String message = messageService.getMessage(messageKey, args);
	auditService.log(MonitoringConstants.MONITORING_MODULE_NAME, message);
    }
}