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
package org.lamsfoundation.lams.authoring.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.authoring.IObjectExtractor;
import org.lamsfoundation.lams.authoring.ObjectExtractorException;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.FloatingActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignAccess;
import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IBranchActivityEntryDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.CompetenceDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.CompetenceMappingDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LicenseDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.TransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.DesignDetailDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceException;
import org.lamsfoundation.lams.planner.dao.PedagogicalPlannerDAO;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolContentIDGenerator;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.dao.hibernate.SystemToolDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.tool.dto.ToolOutputDefinitionDTO;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author Manpreet Minhas
 */
public class AuthoringService implements IAuthoringService, BeanFactoryAware {

    protected Logger log = Logger.getLogger(AuthoringService.class);

    private static final String[] LD_IMAGE_EXTENSIONS = new String[] { "png", "svg" };

    /** Required DAO's */
    protected LearningDesignDAO learningDesignDAO;

    protected LearningLibraryDAO learningLibraryDAO;

    protected ActivityDAO activityDAO;

    protected BaseDAO baseDAO;

    protected TransitionDAO transitionDAO;

    protected ToolDAO toolDAO;

    protected LicenseDAO licenseDAO;

    protected GroupingDAO groupingDAO;

    protected GroupDAO groupDAO;

    protected CompetenceDAO competenceDAO;

    protected CompetenceMappingDAO competenceMappingDAO;

    protected SystemToolDAO systemToolDAO;

    protected PedagogicalPlannerDAO pedagogicalPlannerDAO;

    protected ILamsCoreToolService lamsCoreToolService;

    protected ILearningDesignService learningDesignService;

    protected MessageService messageService;

    protected ILessonService lessonService;

    protected IMonitoringService monitoringService;

    protected IWorkspaceManagementService workspaceManagementService;

    protected ILogEventService logEventService;

    protected ToolContentIDGenerator contentIDGenerator;

    /** The bean factory is used to create ObjectExtractor objects */
    protected BeanFactory beanFactory;

    protected IBranchActivityEntryDAO branchActivityEntryDAO;

    public AuthoringService() {

    }

    public void setPedagogicalPlannerDAO(PedagogicalPlannerDAO pedagogicalPlannerDAO) {
	this.pedagogicalPlannerDAO = pedagogicalPlannerDAO;
    }

    /***************************************************************************
     * Setter Methods
     **************************************************************************/
    /**
     * Set i18n MessageService
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    /**
     * @param groupDAO
     *            The groupDAO to set.
     */
    public void setGroupDAO(GroupDAO groupDAO) {
	this.groupDAO = groupDAO;
    }

    public void setGroupingDAO(GroupingDAO groupingDAO) {
	this.groupingDAO = groupingDAO;
    }

    public void setBranchActivityEntryDAO(IBranchActivityEntryDAO branchActivityEntryDAO) {
	this.branchActivityEntryDAO = branchActivityEntryDAO;
    }

    /**
     *
     * @return
     */
    public CompetenceDAO getCompetenceDAO() {
	return competenceDAO;
    }

    /**
     *
     * @param competenceDAO
     */
    public void setCompetenceDAO(CompetenceDAO competenceDAO) {
	this.competenceDAO = competenceDAO;
    }

    /**
     *
     * @return
     */
    public CompetenceMappingDAO getCompetenceMappingDAO() {
	return competenceMappingDAO;
    }

    /**
     *
     * @param competenceMappingDAO
     */
    public void setCompetenceMappingDAO(CompetenceMappingDAO competenceMappingDAO) {
	this.competenceMappingDAO = competenceMappingDAO;
    }

    /**
     * @param transitionDAO
     *            The transitionDAO to set
     */
    public void setTransitionDAO(TransitionDAO transitionDAO) {
	this.transitionDAO = transitionDAO;
    }

    /**
     * @param learningDesignDAO
     *            The learningDesignDAO to set.
     */
    public void setLearningDesignDAO(LearningDesignDAO learningDesignDAO) {
	this.learningDesignDAO = learningDesignDAO;
    }

    /**
     * @param learningLibraryDAO
     *            The learningLibraryDAO to set.
     */
    public void setLearningLibraryDAO(LearningLibraryDAO learningLibraryDAO) {
	this.learningLibraryDAO = learningLibraryDAO;
    }

    /**
     * @param baseDAO
     *            The baseDAO to set.
     */
    public void setBaseDAO(BaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    /**
     * @param activityDAO
     *            The activityDAO to set.
     */
    public void setActivityDAO(ActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    /**
     * @param toolDAO
     *            The toolDAO to set
     */
    public void setToolDAO(ToolDAO toolDAO) {
	this.toolDAO = toolDAO;
    }

    /**
     * @param toolDAO
     *            The toolDAO to set
     */
    public void setSystemToolDAO(SystemToolDAO systemToolDAO) {
	this.systemToolDAO = systemToolDAO;
    }

    /**
     * @param licenseDAO
     *            The licenseDAO to set
     */
    public void setLicenseDAO(LicenseDAO licenseDAO) {
	this.licenseDAO = licenseDAO;
    }

    public ILamsCoreToolService getLamsCoreToolService() {
	return lamsCoreToolService;
    }

    public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
	this.lamsCoreToolService = lamsCoreToolService;
    }

    public ILearningDesignService getLearningDesignService() {
	return learningDesignService;
    }

    /**
     * @param learningDesignService
     *            The Learning Design Validator Service
     */
    public void setLearningDesignService(ILearningDesignService learningDesignService) {
	this.learningDesignService = learningDesignService;
    }

    @Override
    public MessageService getMessageService() {
	return messageService;
    }

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public void setMonitoringService(IMonitoringService monitoringService) {
	this.monitoringService = monitoringService;
    }

    public void setWorkspaceManagementService(IWorkspaceManagementService workspaceManagementService) {
	this.workspaceManagementService = workspaceManagementService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    /**
     * @param contentIDGenerator
     *            The contentIDGenerator to set.
     */
    public void setContentIDGenerator(ToolContentIDGenerator contentIDGenerator) {
	this.contentIDGenerator = contentIDGenerator;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesign(java.lang.Long)
     */
    @Override
    public LearningDesign getLearningDesign(Long learningDesignID) {
	return learningDesignDAO.getLearningDesignById(learningDesignID);
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#saveLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign)
     */
    @Override
    public void saveLearningDesign(LearningDesign learningDesign) {
	learningDesignDAO.insertOrUpdate(learningDesign);
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraries()
     */
    @Override
    public List getAllLearningLibraries() {
	return learningLibraryDAO.getAllLearningLibraries();
    }

    public BeanFactory getBeanFactory() {
	return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
	this.beanFactory = beanFactory;
    }

    /***************************************************************************
     * Utility/Service Methods
     **************************************************************************/

    /**
     * Helper method to retrieve the user data. Gets the id from the user details in the shared session
     *
     * @return the user id
     */
    public static Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO learner = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return learner != null ? learner.getUserID() : null;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getToolOutputDefinitions(java.lang.Long, int)
     */
    @Override
    public List<ToolOutputDefinitionDTO> getToolOutputDefinitions(Long toolContentID, int definitionType) {
	SortedMap<String, ToolOutputDefinition> defns = lamsCoreToolService.getOutputDefinitionsFromTool(toolContentID,
		definitionType);

	ArrayList<ToolOutputDefinitionDTO> defnDTOList = new ArrayList<ToolOutputDefinitionDTO>(
		defns != null ? defns.size() : 0);
	if (defns != null) {
	    for (ToolOutputDefinition defn : defns.values()) {
		defnDTOList.add(new ToolOutputDefinitionDTO(defn));
	    }
	}
	return defnDTOList;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getToolOutputDefinitions(java.lang.Long, int,
     *      java.lang.Long)
     */
    @Override
    public String getToolOutputDefinitions(Long outputToolContentID, int definitionType, Long inputToolContentID)
	    throws IOException {

	SortedMap<String, ToolOutputDefinition> defns = lamsCoreToolService
		.getOutputDefinitionsFromToolFiltered(outputToolContentID, definitionType, inputToolContentID);

	ArrayList<ToolOutputDefinitionDTO> defnDTOList = new ArrayList<ToolOutputDefinitionDTO>(
		defns != null ? defns.size() : 0);
	if (defns != null) {
	    for (ToolOutputDefinition defn : defns.values()) {
		defnDTOList.add(new ToolOutputDefinitionDTO(defn));
	    }
	}

	FlashMessage flashMessage = new FlashMessage("getToolOutputDefinitions", defnDTOList);
	return flashMessage.serializeMessage();
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesignDetails(java.lang.Long,
     *      java.lang.Long)
     */
    @Override
    public String getLearningDesignDetails(Long learningDesignID, String languageCode) throws IOException {
	FlashMessage flashMessage = null;

	LearningDesignDTO learningDesignDTO = learningDesignService.getLearningDesignDTO(learningDesignID,
		languageCode);

	if (learningDesignDTO == null) {
	    flashMessage = FlashMessage.getNoSuchLearningDesignExists("getLearningDesignDetails", learningDesignID);
	} else {
	    flashMessage = new FlashMessage("getLearningDesignDetails", learningDesignDTO);
	}
	return flashMessage.serializeMessage();
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#setupEditOnFlyLock(LearningDesign,
     *      java.lang.Integer)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean setupEditOnFlyLock(Long learningDesignID, Integer userID)
	    throws LearningDesignException, UserException, IOException {
	User user = (User) baseDAO.find(User.class, userID);
	if (user == null) {
	    throw new UserException(messageService.getMessage("no.such.user.exist", new Object[] { userID }));
	}

	LearningDesign design = learningDesignID == null ? null : getLearningDesign(learningDesignID);

	if (design == null) {
	    throw new LearningDesignException(FlashMessage
		    .getNoSuchLearningDesignExists("getLearningDesignDetails", learningDesignID).serializeMessage());
	}

	boolean learningDesignAvailable = (design.getEditOverrideUser() == null)
		|| (design.getEditOverrideLock() == null) || design.getEditOverrideUser().getUserId().equals(userID)
		|| !design.getEditOverrideLock();

	if (learningDesignAvailable) {
	    if (design.getLessons().isEmpty()) {
		// TODO: add error msg
		throw new LearningDesignException("There are no lessons attached to the design.");
	    }

	    for (Lesson lesson : (Set<Lesson>) design.getLessons()) {
		lesson.setLockedForEdit(true);
	    }

	    // lock Learning Design
	    design.setEditOverrideLock(true);
	    design.setEditOverrideUser(user);

	    learningDesignDAO.update(design);

	    return true;
	}
	return false;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#setupEditOnFlyGate(java.lang.Long,
     *      java.lang.Integer)
     */

    @Override
    public String setupEditOnFlyGate(Long learningDesignID, Integer userID) throws UserException, IOException {
	User user = (User) baseDAO.find(User.class, userID);
	if (user == null) {
	    throw new UserException(messageService.getMessage("no.such.user.exist", new Object[] { userID }));
	}

	LearningDesign design = learningDesignID == null ? null : getLearningDesign(learningDesignID);

	// parse Learning Design to find last read-only Activity
	EditOnFlyProcessor processor = new EditOnFlyProcessor(design, activityDAO);

	processor.parseLearningDesign();

	Activity lastReadOnlyActivity = processor.lastReadOnlyActivity;

	// check if system gate already exists
	if ((lastReadOnlyActivity == null) || !lastReadOnlyActivity.isGateActivity()
		|| !((GateActivity) lastReadOnlyActivity).isSystemGate()) {
	    // add new System Gate after last read-only Activity
	    addSystemGateAfterActivity(lastReadOnlyActivity, design);
	    learningDesignDAO.update(design);
	}

	return new FlashMessage("setupEditOnFlyGate", true).serializeMessage();
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#finishEditOnFly(java.lang.Long,
     *      java.lang.Integer)
     */
    @SuppressWarnings("unchecked")
    @Override
    public String finishEditOnFly(Long learningDesignID, Integer userID, boolean cancelled) throws IOException {
	FlashMessage flashMessage = null;

	User user = (User) baseDAO.find(User.class, userID);
	if (user == null) {
	    flashMessage = FlashMessage.getNoSuchUserExists("finishEditOnFly", userID);
	}

	LearningDesign design = learningDesignID == null ? null
		: learningDesignDAO.getLearningDesignById(learningDesignID);

	if (design == null) {
	    flashMessage = FlashMessage.getNoSuchLearningDesignExists("finishEditOnFly", learningDesignID);
	} else {
	    // only the user who is editing the design may unlock it
	    if (design.getEditOverrideUser().equals(user)) {
		design.setEditOverrideLock(false);
		design.setEditOverrideUser(null);

		// parse Learning Design to find last read - only Activity
		// (hopefully the system gate in this case )

		EditOnFlyProcessor processor = new EditOnFlyProcessor(design, activityDAO);

		processor.parseLearningDesign();

		Activity lastReadOnlyActivity = processor.lastReadOnlyActivity;
		Long firstAddedActivityId = processor.firstAddedActivity == null ? null
			: processor.firstAddedActivity.getActivityId();

		// open and release waiting list on system gate
		if ((lastReadOnlyActivity != null) && lastReadOnlyActivity.isGateActivity()
			&& ((GateActivity) lastReadOnlyActivity).isSystemGate()) {
		    // remove previously inserted system gate
		    design = removeTempSystemGate(lastReadOnlyActivity.getActivityId(), design.getLearningDesignId());
		}

		for (Lesson lesson : (Set<Lesson>) design.getLessons()) {
		    lesson.setLockedForEdit(false);

		    // LDEV-1899 only mark learners uncompleted if a change was saved and an activity added
		    if (!cancelled && (firstAddedActivityId != null)) {
			// the lesson may now have additional activities on the end,
			// so clear any completed flags
			lessonService.performMarkLessonUncompleted(lesson.getLessonId(), firstAddedActivityId);
		    }

		    initialiseToolActivityForRuntime(design, lesson);

		    if (flashMessage == null) {
			flashMessage = new FlashMessage("finishEditOnFly", lesson.getLessonId());
		    }
		}

		learningDesignDAO.update(design);
	    } else {
		flashMessage = FlashMessage.getNoSuchUserExists("finishEditOnFly", userID);
	    }
	}

	return flashMessage.serializeMessage();

    }

    /**
     * Remove a temporary System Gate from the design. Requires removing the gate from any learner progress entries -
     * should only be a current activity but remove it from previous and next, just in case.
     *
     * This will leave a "hole" in the learner progress, but the progress engine can take care of that.
     *
     * @return Learning Design with removed System Gate
     */
    private LearningDesign removeTempSystemGate(Long gateId, Long learningDesignId) {
	LearningDesign design = learningDesignDAO.getLearningDesignById(learningDesignId);
	GateActivity gate = (GateActivity) activityDAO.getActivityByActivityId(gateId);

	// get transitions
	Transition toTransition = gate.getTransitionTo();
	Transition fromTransition = gate.getTransitionFrom();

	// rearrange to-transition and/or delete redundant transition
	if ((toTransition != null) && (fromTransition != null)) {
	    toTransition.setToActivity(fromTransition.getToActivity());
	    fromTransition.getToActivity().setTransitionTo(toTransition);
	    toTransition.setToUIID(toTransition.getToActivity().getActivityUIID());

	    design.getTransitions().remove(fromTransition);
	    transitionDAO.update(toTransition);
	} else if ((toTransition != null) && (fromTransition == null)) {
	    toTransition.getFromActivity().setTransitionFrom(null);
	    design.getTransitions().remove(toTransition);
	} else if ((toTransition == null) && (fromTransition != null)) {
	    design.setFirstActivity(fromTransition.getToActivity());
	    fromTransition.getToActivity().setTransitionTo(null);
	    design.getTransitions().remove(fromTransition);
	}

	// need to remove it from any learner progress entries
	lessonService.removeProgressReferencesToActivity(gate);

	// remove temp system gate
	design.getActivities().remove(gate);
	gate.setTransitionFrom(null);
	gate.setTransitionTo(null);

	// increment design version field
	design.setDesignVersion(design.getDesignVersion() + 1);

	return design;
    }

    /**
     * Add a temporary System Gate to the design.
     */
    @SuppressWarnings("unchecked")
    private void addSystemGateAfterActivity(Activity activity, LearningDesign design) {
	GateActivity gate = null;
	Integer maxId = design.getMaxID();
	String title = "System Gate";
	SystemTool systemTool = systemToolDAO.getSystemToolByID(SystemTool.SYSTEM_GATE);

	/* create new System Gate Activity */
	gate = (GateActivity) Activity.getActivityInstance(Activity.SYSTEM_GATE_ACTIVITY_TYPE);
	gate.setActivityTypeId(Activity.SYSTEM_GATE_ACTIVITY_TYPE);
	gate.setActivityCategoryID(Activity.CATEGORY_SYSTEM);
	gate.setSystemTool(systemTool);
	gate.setActivityUIID(++maxId);
	gate.setTitle(title);
	gate.setGateOpen(false);
	gate.setGateActivityLevelId(GateActivity.LEARNER_GATE_LEVEL);
	gate.setApplyGrouping(false); // not nullable so default to false
	gate.setGroupingSupportType(Activity.GROUPING_SUPPORT_OPTIONAL);
	gate.setOrderId(null);
	gate.setCreateDateTime(new Date());
	gate.setReadOnly(Boolean.TRUE);
	gate.setLearningDesign(design);

	design.getActivities().add(gate);
	baseDAO.insert(gate);

	Transition fromTransition = null;
	Activity toActivity = null;

	Transition newTransition = new Transition();
	newTransition.setTransitionUIID(++maxId);
	newTransition.setLearningDesign(design);

	if (activity == null) {
	    // no read-only activities, insert gate at start of sequence
	    toActivity = design.getFirstActivity();

	    newTransition.setToActivity(toActivity);
	    newTransition.setToUIID(toActivity.getActivityUIID());
	    newTransition.setFromActivity(gate);
	    newTransition.setFromUIID(gate.getActivityUIID());

	    gate.setTransitionFrom(newTransition);
	    toActivity.setTransitionTo(newTransition);

	    // set gate as first activity in sequence
	    design.setFirstActivity(gate);

	    // set x / y position for Gate
	    Integer x1 = toActivity.getXcoord() == null ? 0 : toActivity.getXcoord();

	    Integer x2 = toActivity.getTransitionFrom() == null ? null
		    : toActivity.getTransitionFrom().getToActivity().getXcoord();

	    if ((x1 != null) && (x2 != null)) {
		gate.setXcoord(x2 >= x1 ? (x1.intValue() - 13 - 20) : (x1.intValue() + 123 + 13 + 20));
	    } else {
		gate.setXcoord(x1.intValue() - 13 - 20);
	    }

	    gate.setYcoord(toActivity.getYcoord() + 25);

	} else {
	    // update transitions
	    fromTransition = activity.getTransitionFrom();

	    if (fromTransition == null) {
		newTransition.setFromActivity(activity);
		newTransition.setFromUIID(activity.getActivityUIID());
		newTransition.setToActivity(gate);
		newTransition.setToUIID(gate.getActivityUIID());

		activity.setTransitionFrom(newTransition);
		gate.setTransitionTo(newTransition);

		Integer x1 = activity.getTransitionTo() == null ? 0
			: activity.getTransitionTo().getFromActivity().getXcoord(); /* set x/y position for Gate */
		Integer x2 = activity.getXcoord() == null ? null : activity.getXcoord();

		if ((x1 != null) && (x2 != null)) {
		    gate.setXcoord(x2 >= x1 ? (x2.intValue() + 123 + 13 + 20) : (x2.intValue() - 13 - 20));
		} else {
		    gate.setXcoord(x2.intValue() + 123 + 13 + 20);
		}

		gate.setYcoord(activity.getYcoord() + 25);

	    } else {

		toActivity = fromTransition.getToActivity();

		fromTransition.setToActivity(gate);
		fromTransition.setToUIID(gate.getActivityUIID());

		newTransition.setFromActivity(gate);
		newTransition.setFromUIID(gate.getActivityUIID());
		newTransition.setToActivity(toActivity);
		newTransition.setToUIID(toActivity.getActivityUIID());

		gate.setTransitionFrom(newTransition);
		toActivity.setTransitionTo(newTransition);
		gate.setTransitionTo(fromTransition);

		// set x / y position for Gate
		Integer x1 = activity.getXcoord() == null ? 0 : activity.getXcoord();
		Integer x2 = toActivity.getXcoord() == null ? 0 : toActivity.getXcoord();
		gate.setXcoord(((x1.intValue() + 123 + x2.intValue()) / 2) - 13);

		Integer y1 = activity.getYcoord() == null ? 0 : activity.getYcoord();
		Integer y2 = toActivity.getYcoord() == null ? 0 : toActivity.getYcoord();
		gate.setYcoord((y1.intValue() + 50 + y2.intValue()) / 2);
	    }
	}

	design.getTransitions().add(newTransition);
	design.setMaxID(maxId);

	// increment design version field
	design.setDesignVersion(design.getDesignVersion() + 1);

	if (gate != null) {
	    activityDAO.update(gate);
	}
	if (activity != null) {
	    activityDAO.update(activity);
	}
	if (toActivity != null) {
	    activityDAO.update(toActivity);
	}
	if (fromTransition != null) {
	    baseDAO.update(fromTransition);
	}
	baseDAO.insert(newTransition);
	learningDesignDAO.update(design);
    }

    @SuppressWarnings("unchecked")
    private void initialiseToolActivityForRuntime(LearningDesign design, Lesson lesson)
	    throws MonitoringServiceException {
	Date now = new Date();

	for (Activity activity : (Set<Activity>) design.getActivities()) {
	    if (!activity.isInitialised()) {
		// this is a new activity - need to set up the content, do any
		// scheduling, etc
		// always have to copy the tool content, even though it may
		// point to unique content - that way if the
		// teacher has double clicked on the tool icon (and hence set up
		// a tool content id) but not saved any content
		// the code in copyToolContent will ensure that there is content
		// for this activity. So we end up with a few
		// unused records - we are trading database space for
		// reliability. If we don't ensure that there is always
		// a content record, then shortcomings in the createToolSession
		// code may throw exceptions.
		if (activity.isToolActivity()) {
		    ToolActivity toolActivity = (ToolActivity) activityDAO
			    .getActivityByActivityId(activity.getActivityId());
		    Long newContentId = lamsCoreToolService.notifyToolToCopyContent(toolActivity, null);
		    toolActivity.setToolContentId(newContentId);

		    // LDEV-2510 init tool sessions for support activities added during live edit
		    // monitoringService.initToolSessionIfSuitable(toolActivity, lesson);
		} else {
		    Integer newMaxId = monitoringService.startSystemActivity(activity, design.getMaxID(), now,
			    lesson.getLessonName());
		    if (newMaxId != null) {
			design.setMaxID(newMaxId);
		    }
		}
		activity.setInitialised(Boolean.TRUE);
		activityDAO.update(activity);
	    }
	}

	learningDesignDAO.update(design);
    }

    @Override
    public LearningDesign copyLearningDesign(Long originalDesignID, Integer copyType, Integer userID,
	    Integer workspaceFolderID, boolean setOriginalDesign)
	    throws UserException, LearningDesignException, WorkspaceFolderException, IOException {

	LearningDesign originalDesign = learningDesignDAO.getLearningDesignById(originalDesignID);
	if (originalDesign == null) {
	    throw new LearningDesignException(
		    messageService.getMessage("no.such.learningdesign.exist", new Object[] { originalDesignID }));
	}

	User user = (User) baseDAO.find(User.class, userID);
	if (user == null) {
	    throw new UserException(messageService.getMessage("no.such.user.exist", new Object[] { userID }));
	}

	WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
	if (workspaceFolder == null) {
	    throw new WorkspaceFolderException(
		    messageService.getMessage("no.such.workspace.exist", new Object[] { workspaceFolderID }));
	}

	if (!workspaceManagementService.isUserAuthorizedToModifyFolderContents(workspaceFolder.getWorkspaceFolderId(),
		user.getUserId())) {
	    throw new UserAccessDeniedException("User with user_id of " + user.getUserId()
		    + " is not authorized to copy a learning design into the workspace folder "
		    + workspaceFolder.getWorkspaceFolderId());
	}

	return copyLearningDesign(originalDesign, copyType, user, workspaceFolder, setOriginalDesign, null, null);
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign,
     *      java.lang.Integer, org.lamsfoundation.lams.usermanagement.User,
     *      org.lamsfoundation.lams.usermanagement.WorkspaceFolder, java.lang.Boolean, java.lang.String)
     */
    @Override
    public LearningDesign copyLearningDesign(LearningDesign originalLearningDesign, Integer copyType, User user,
	    WorkspaceFolder workspaceFolder, boolean setOriginalDesign, String newDesignName, String customCSV)

    {
	String newTitle = newDesignName;
	if (newTitle == null) {
	    newTitle = getUniqueNameForLearningDesign(originalLearningDesign.getTitle(),
		    workspaceFolder != null ? workspaceFolder.getWorkspaceFolderId() : null);
	}

	LearningDesign newLearningDesign = LearningDesign.createLearningDesignCopy(originalLearningDesign, copyType,
		setOriginalDesign);
	newLearningDesign.setTitle(newTitle);
	newLearningDesign.setUser(user);
	newLearningDesign.setWorkspaceFolder(workspaceFolder);
	newLearningDesign.setEditOverrideLock(false); // clear the live edit
	// flag
	learningDesignDAO.insert(newLearningDesign);

	updateDesignCompetences(originalLearningDesign, newLearningDesign, false);
	HashMap<Integer, Activity> newActivities = updateDesignActivities(originalLearningDesign, newLearningDesign, 0,
		customCSV);
	updateDesignTransitions(originalLearningDesign, newLearningDesign, newActivities, 0);

	// set first activity assumes that the transitions are all set up
	// correctly.
	newLearningDesign.setFirstActivity(newLearningDesign.calculateFirstActivity());
	newLearningDesign.setFloatingActivity(newLearningDesign.calculateFloatingActivity());
	newLearningDesign.setLearningDesignUIID(originalLearningDesign.getLearningDesignUIID());

	updateCompetenceMappings(newLearningDesign.getCompetences(), newActivities);

	updateEvaluations(newActivities);

	try {
	    AuthoringService.copyLearningDesignImages(originalLearningDesign.getLearningDesignId(),
		    newLearningDesign.getLearningDesignId());
	} catch (IOException e) {
	    log.error("Error while copying Learning Design " + originalLearningDesign.getLearningDesignId() + " image",
		    e);
	}
	return newLearningDesign;
    }

    /**
     * @throws UserException
     * @throws WorkspaceFolderException
     * @throws IOException
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#insertLearningDesign(java.lang.Long,
     *      java.lang.Long, java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.Integer)
     */
    @Override
    public LearningDesign insertLearningDesign(Long originalDesignID, Long designToImportID, Integer userID,
	    boolean createNewLearningDesign, String newDesignName, Integer workspaceFolderID, String customCSV)
	    throws UserException, WorkspaceFolderException, IOException {

	User user = (User) baseDAO.find(User.class, userID);
	if (user == null) {
	    throw new UserException(messageService.getMessage("no.such.user.exist", new Object[] { userID }));
	}

	LearningDesign mainDesign = learningDesignDAO.getLearningDesignById(originalDesignID);
	if (mainDesign == null) {
	    throw new LearningDesignException(
		    messageService.getMessage("no.such.learningdesign.exist", new Object[] { originalDesignID }));
	}

	LearningDesign designToImport = learningDesignDAO.getLearningDesignById(designToImportID);
	if (designToImport == null) {
	    throw new LearningDesignException(
		    messageService.getMessage("no.such.learningdesign.exist", new Object[] { designToImportID }));
	}

	if (createNewLearningDesign) {
	    WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
	    if (workspaceFolder == null) {
		throw new WorkspaceFolderException(
			messageService.getMessage("no.such.workspace.exist", new Object[] { workspaceFolderID }));
	    }
	    if (!workspaceManagementService
		    .isUserAuthorizedToModifyFolderContents(workspaceFolder.getWorkspaceFolderId(), user.getUserId())) {
		throw new UserAccessDeniedException("User with user_id of " + user.getUserId()
			+ " is not authorized to store a copy a learning design into the workspace folder "
			+ workspaceFolder);
	    }

	    mainDesign = copyLearningDesign(mainDesign, LearningDesign.COPY_TYPE_NONE, user, workspaceFolder, false,
		    newDesignName, customCSV);
	} else {
	    // updating the existing design so check the rights to the folder
	    // containing the design. If this is in live edit mode
	    boolean authorised = workspaceManagementService.isUserAuthorizedToModifyFolderContents(
		    mainDesign.getWorkspaceFolder().getWorkspaceFolderId(), user.getUserId());
	    if (!authorised) {
		authorised = (mainDesign.getEditOverrideLock() != null)
			&& mainDesign.getEditOverrideLock().booleanValue()
			&& userID.equals(mainDesign.getEditOverrideUser().getUserId());
	    }
	    if (!authorised) {
		throw new UserAccessDeniedException("User with user_id of " + user.getUserId()
			+ " is not authorized to update a learning design into the workspace folder "
			+ mainDesign.getWorkspaceFolder());
	    }
	}

	// now dump the import design into our main sequence. Leave the first
	// activity ui id for the design as it is.
	int uiidOffset = mainDesign.getMaxID().intValue();
	updateDesignCompetences(designToImport, mainDesign, true);
	HashMap<Integer, Activity> newActivities = updateDesignActivities(designToImport, mainDesign, uiidOffset,
		customCSV);
	updateDesignTransitions(designToImport, mainDesign, newActivities, uiidOffset);
	mainDesign.setMaxID(LearningDesign.addOffset(designToImport.getMaxID(), uiidOffset));
	mainDesign.setValidDesign(Boolean.FALSE);
	mainDesign.setLastModifiedDateTime(new Date());
	learningDesignDAO.update(mainDesign);

	insertCompetenceMappings(mainDesign.getCompetences(), designToImport.getCompetences(), newActivities);

	// For some reason, the evaluations will not save on insert when the
	// learning design is saved, so doing it manually here

	this.updateEvaluations(newActivities);

	// for (Integer activityKey : newActivities.keySet()) {
	// Activity activity = newActivities.get(activityKey);
	// if (activity.isToolActivity()) {
	// ToolActivity toolAct = (ToolActivity) activity;
	// baseDAO.insertOrUpdateAll(toolAct.getActivityEvaluations());
	// }
	// }

	return mainDesign;
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesignToolContent(org.lamsfoundation.lams.learningdesign.LearningDesign,
     *      org.lamsfoundation.lams.learningdesign.LearningDesign, java.lang.Integer)
     */
    private LearningDesign copyLearningDesignToolContent(LearningDesign design, LearningDesign originalLearningDesign,
	    Integer copyType, String customCSV) throws LearningDesignException {

	for (Iterator i = design.getActivities().iterator(); i.hasNext();) {
	    Activity currentActivity = (Activity) i.next();
	    if (currentActivity.isToolActivity()) {
		copyActivityToolContent(currentActivity, design.getCopyTypeID(),
			originalLearningDesign.getLearningDesignId(), customCSV);
	    }
	}

	return design;
    }

    /**
     * @param originalLearningDesign
     * @param copyType
     * @param currentActivity
     */
    private void copyActivityToolContent(Activity activity, Integer ldCopyType, Long originalLearningDesignId,
	    String customCSV) {
	try {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    // copy the content
	    Long newContentId = lamsCoreToolService.notifyToolToCopyContent(toolActivity, customCSV);
	    toolActivity.setToolContentId(newContentId);

	    // clear read only field
	    toolActivity.setReadOnly(false);

	} catch (DataMissingException e) {
	    String error = "Unable to copy a design / initialise the lesson. Data is missing for activity "
		    + activity.getActivityUIID() + " in learning design " + originalLearningDesignId
		    + " default content may be missing for the tool. Error was " + e.getMessage();
	    log.error(error, e);
	    throw new LearningDesignException(error, e);
	} catch (ToolException e) {
	    String error = "Unable to copy a design / initialise the lesson. Tool encountered an error copying the data is missing for activity "
		    + activity.getActivityUIID() + " in learning design " + originalLearningDesignId
		    + " default content may be missing for the tool. Error was " + e.getMessage();
	    log.error(error, e);
	    throw new LearningDesignException(error, e);
	}
    }

    /**
     * Updates the Activity information in the newLearningDesign based on the originalLearningDesign. This any grouping
     * details.
     *
     * As new activities are created, the UIID is incremented by the uiidOffset. If we are just copying a sequence this
     * will be set to 0. But if we are importing a sequence into another sequence, this will be an offset value so we
     * new ids guaranteed to be outside of the range of the main sequence (this may mean gaps in the uiids but that
     * doesn't matter).
     *
     * @param originalLearningDesign
     *            The LearningDesign to be copied
     * @param newLearningDesign
     *            The copy of the originalLearningDesign
     * @return Map of all the new activities, where the key is the UIID value. This is used as an input to
     *         updateDesignTransitions
     */
    private HashMap<Integer, Activity> updateDesignActivities(LearningDesign originalLearningDesign,
	    LearningDesign newLearningDesign, int uiidOffset, String customCSV) {
	HashMap<Integer, Activity> newActivities = new HashMap<Integer, Activity>(); // key
	// is
	// UIID
	HashMap<Integer, Grouping> newGroupings = new HashMap<Integer, Grouping>(); // key
	// is
	// UIID

	// as we create the activities, we need to record any "first child"
	// UIID's for the sequence activity to process later
	Map<Integer, SequenceActivity> firstChildUIIDToSequence = new HashMap<Integer, SequenceActivity>();

	Set oldParentActivities = originalLearningDesign.getParentActivities();
	if (oldParentActivities != null) {
	    Iterator iterator = oldParentActivities.iterator();
	    while (iterator.hasNext()) {
		processActivity((Activity) iterator.next(), newLearningDesign, newActivities, newGroupings, null,
			originalLearningDesign.getLearningDesignId(), uiidOffset, customCSV);
	    }
	}

	Collection<Activity> activities = newActivities.values();

	// Go back and find all the grouped activities and assign them the new
	// groupings, based on the UIID. Can't
	// be done as we go as the grouping activity may be processed after the
	// grouped activity.
	for (Activity activity : activities) {
	    if (activity.getGroupingUIID() != null) {
		activity.setGrouping(newGroupings.get(activity.getGroupingUIID()));
	    }
	}

	// fix up any old "default activity" in the complex activities and the
	// input activities
	// and fix any branch mappings
	for (Activity activity : activities) {
	    if (activity.isComplexActivity()) {
		ComplexActivity newComplex = (ComplexActivity) activity;
		Activity oldDefaultActivity = newComplex.getDefaultActivity();
		if (oldDefaultActivity != null) {
		    Activity newDefaultActivity = newActivities
			    .get(LearningDesign.addOffset(oldDefaultActivity.getActivityUIID(), uiidOffset));
		    newComplex.setDefaultActivity(newDefaultActivity);
		}
	    }

	    if (activity.isSequenceActivity()) {
		SequenceActivity newSequenceActivity = (SequenceActivity) activity;
		// Need to check if the sets are not null as these are new
		// objects and Hibernate may not have backed them with
		// collections yet.
		if ((newSequenceActivity.getBranchEntries() != null)
			&& (newSequenceActivity.getBranchEntries().size() > 0)) {

		    Activity parentActivity = newSequenceActivity.getParentActivity();
		    if (parentActivity.isChosenBranchingActivity()) {
			// Don't have any preset up entries for a teacher chosen.
			// Must be copying a design that was run previously.
			newSequenceActivity.getBranchEntries().clear();

		    } else {
			Iterator beIter = newSequenceActivity.getBranchEntries().iterator();
			while (beIter.hasNext()) {
			    // sequence activity will be correct but the
			    // branching activity and the grouping will be wrong
			    // the condition was copied by the sequence activity
			    // copy
			    BranchActivityEntry entry = (BranchActivityEntry) beIter.next();
			    BranchingActivity oldBranchingActivity = (BranchingActivity) entry.getBranchingActivity();
			    entry.setBranchingActivity(newActivities
				    .get(LearningDesign.addOffset(oldBranchingActivity.getActivityUIID(), uiidOffset)));
			    Group oldGroup = entry.getGroup();
			    if (oldGroup != null) {
				Grouping oldGrouping = oldGroup.getGrouping();
				Grouping newGrouping = newGroupings
					.get(LearningDesign.addOffset(oldGrouping.getGroupingUIID(), uiidOffset));
				if (newGrouping != null) {
				    entry.setGroup(newGrouping
					    .getGroup(LearningDesign.addOffset(oldGroup.getGroupUIID(), uiidOffset)));
				}
			    }
			}

		    }
		}
	    }

	    if ((activity.getInputActivities() != null) && (activity.getInputActivities().size() > 0)) {
		Set<Activity> newInputActivities = new HashSet<Activity>();
		Iterator inputIter = activity.getInputActivities().iterator();
		while (inputIter.hasNext()) {
		    Activity elem = (Activity) inputIter.next();
		    newInputActivities
			    .add(newActivities.get(LearningDesign.addOffset(elem.getActivityUIID(), uiidOffset)));
		}
		activity.getInputActivities().clear();
		activity.getInputActivities().addAll(newInputActivities);
	    }
	}

	// The activities collection in the learning design may already exist
	// (as we have already done a save on the design).
	// If so, we can't just override the existing collection as the cascade
	// causes an error.
	// newLearningDesign.getActivities() will create a new TreeSet(new
	// ActivityOrderComparator()) if there isn't an existing set
	// If the uiidOffset is > 0, then we are adding activities, so we don't
	// want to clear first.
	if (uiidOffset == 0) {
	    newLearningDesign.getActivities().clear();
	}

	newLearningDesign.getActivities().addAll(activities);

	// On very rare occasions, we've had Hibernate try to save the branching
	// entries before saving the branching activity
	// which throws an exception as the branch_activity_id is null. So force
	// any branching activities to save first.
	// And yes, this IS a hack. (See LDEV-1786)
	for (Activity activity : activities) {
	    if (activity.isBranchingActivity()) {
		activityDAO.insert(activity);
	    }
	}

	return newActivities;

    }

    /**
     * As part of updateDesignActivities(), process an activity and, via recursive calls, the activity's child
     * activities. Need to keep track of any new groupings created so we can go back and update the grouped activities
     * with their new groupings at the end. Also copies the tool content.
     *
     * @param activity
     *            Activity to process. May not be null.
     * @param newLearningDesign
     *            The new learning design. May not be null.
     * @param newActivities
     *            Temporary set of new activities - as activities are processed they are added to the set. May not be
     *            null.
     * @param newGroupings
     *            Temporary set of new groupings. Key is the grouping UUID. May not be null.
     * @param parentActivity
     *            This activity's parent activity (if one exists). May be null.
     */
    private void processActivity(Activity activity, LearningDesign newLearningDesign,
	    Map<Integer, Activity> newActivities, Map<Integer, Grouping> newGroupings, Activity parentActivity,
	    Long originalLearningDesignId, int uiidOffset, String customCSV) {
	Activity newActivity = getActivityCopy(activity, newGroupings, uiidOffset);
	newActivity.setActivityUIID(newActivity.getActivityUIID());
	newActivity.setLearningDesign(newLearningDesign);
	newActivity.setReadOnly(false);
	if (parentActivity != null) {
	    newActivity.setParentActivity(parentActivity);
	    newActivity.setParentUIID(parentActivity.getActivityUIID());
	    ((ComplexActivity) parentActivity).getActivities().add(newActivity);
	}

	if (!(newActivity.isFloatingActivity() && (newLearningDesign.getFloatingActivity() != null))) {
	    newActivities.put(newActivity.getActivityUIID(), newActivity);
	}

	if (newActivity.isToolActivity()) {
	    copyActivityToolContent(newActivity, newLearningDesign.getCopyTypeID(), originalLearningDesignId,
		    customCSV);
	}

	Set oldChildActivities = getChildActivities(activity);
	if (oldChildActivities != null) {
	    Iterator childIterator = oldChildActivities.iterator();
	    while (childIterator.hasNext()) {
		Activity childActivity = (Activity) childIterator.next();

		// If Floating Activity(s) exist in BOTH designs then we:
		// Transfer the floating activities from the main design to the
		// one that is to be imported.
		// Number of activities may overflow the max limit for the
		// container - to be handled in flash
		// when design is opened.
		FloatingActivity fParentActivity = null;
		Activity refParentActivity = null;

		if (childActivity.isFloating() && (newLearningDesign.getFloatingActivity() != null)) {
		    fParentActivity = newLearningDesign.getFloatingActivity();
		} else {
		    refParentActivity = newActivity;
		}

		if (childActivity.isFloating() && (fParentActivity != null)) {
		    childActivity.setOrderId(fParentActivity.getActivities().size() + childActivity.getOrderId() + 1);
		}

		Activity pActivity = fParentActivity != null ? (Activity) fParentActivity : refParentActivity;
		processActivity(childActivity, newLearningDesign, newActivities, newGroupings, pActivity,
			originalLearningDesignId, uiidOffset, customCSV);

	    }
	}
    }

    /**
     * Updates the Transition information in the newLearningDesign based on the originalLearningDesign
     *
     * @param originalLearningDesign
     *            The LearningDesign to be copied
     * @param newLearningDesign
     *            The copy of the originalLearningDesign
     */
    public void updateDesignTransitions(LearningDesign originalLearningDesign, LearningDesign newLearningDesign,
	    HashMap<Integer, Activity> newActivities, int uiidOffset) {
	HashSet newTransitions = new HashSet();
	Set oldTransitions = originalLearningDesign.getTransitions();
	Iterator iterator = oldTransitions.iterator();
	while (iterator.hasNext()) {
	    Transition transition = (Transition) iterator.next();
	    Transition newTransition = Transition.createCopy(transition, uiidOffset);
	    Activity toActivity = null;
	    Activity fromActivity = null;
	    if (newTransition.getToUIID() != null) {
		toActivity = newActivities.get(newTransition.getToUIID());
		if (transition.isProgressTransition()) {
		    toActivity.setTransitionTo(newTransition);
		}
	    }
	    if (newTransition.getFromUIID() != null) {
		fromActivity = newActivities.get(newTransition.getFromUIID());
		// check if we are dealing with a "real" transition, not data
		// flow
		if (transition.isProgressTransition()) {
		    fromActivity.setTransitionFrom(newTransition);
		}
	    }
	    newTransition.setToActivity(toActivity);
	    newTransition.setFromActivity(fromActivity);
	    newTransition.setLearningDesign(newLearningDesign);
	    transitionDAO.insert(newTransition);
	    newTransitions.add(newTransition);
	}

	// The transitions collection in the learning design may already exist
	// (as we have already done a save on the design).
	// If so, we can't just override the existing collection as the cascade
	// causes an error.
	// If the uiidOffset is > 0, then we are adding transitions (rather than
	// replacing), so we don't want to clear first.
	if (newLearningDesign.getTransitions() != null) {
	    if (uiidOffset == 0) {
		newLearningDesign.getTransitions().clear();
	    }
	    newLearningDesign.getTransitions().addAll(newTransitions);
	} else {
	    newLearningDesign.setTransitions(newTransitions);
	}

    }

    /**
     * Updates the competence information in the newLearningDesign based on the originalLearningDesign
     *
     * @param originalLearningDesign
     *            The LearningDesign to be copied
     * @param newLearningDesign
     *            The copy of the originalLearningDesign
     */
    public void updateDesignCompetences(LearningDesign originalLearningDesign, LearningDesign newLearningDesign,
	    boolean insert) {
	HashSet<Competence> newCompeteces = new HashSet<Competence>();

	Set<Competence> oldCompetences = originalLearningDesign.getCompetences();
	if (oldCompetences != null) {
	    for (Competence competence : oldCompetences) {
		Competence newCompetence = competence.createCopy(competence);
		newCompetence.setLearningDesign(newLearningDesign);

		// check for existing competences to prevent duplicates
		if (competenceDAO.getCompetence(newLearningDesign, newCompetence.getTitle()) == null) {
		    competenceDAO.saveOrUpdate(newCompetence);
		}
		newCompeteces.add(newCompetence);
	    }
	}
	if (newLearningDesign.getCompetences() != null) {
	    if (!insert) {
		newLearningDesign.getCompetences().clear();
		newLearningDesign.getCompetences().addAll(newCompeteces);
	    } else {
		// handle inserting sequences
		for (Competence newCompetence : newCompeteces) {
		    boolean alreadyExistsInLD = false;
		    for (Competence existingCompetence : originalLearningDesign.getCompetences()) {
			if (newCompetence.getTitle().equals(existingCompetence.getTitle())) {
			    alreadyExistsInLD = true;
			    break;
			}
		    }
		    if (!alreadyExistsInLD) {
			newLearningDesign.getCompetences().add(newCompetence);
		    }
		}
	    }

	} else {
	    newLearningDesign.setCompetences(newCompeteces);
	}

    }

    public void insertCompetenceMappings(Set<Competence> oldCompetences, Set<Competence> newCompetences,
	    HashMap<Integer, Activity> newActivities) {

	for (Integer activityKey : newActivities.keySet()) {
	    Activity activity = newActivities.get(activityKey);
	    if (activity.isToolActivity()) {
		Set<CompetenceMapping> newCompetenceMappings = new HashSet<CompetenceMapping>();
		ToolActivity newToolActivity = (ToolActivity) activity;
		if (newToolActivity.getCompetenceMappings() != null) {
		    for (CompetenceMapping competenceMapping : newToolActivity.getCompetenceMappings()) {
			CompetenceMapping newMapping = new CompetenceMapping();
			newMapping.setToolActivity(newToolActivity);

			// Check if competence mapping title already exists as a
			// competence in the original sequence
			// If so, simply use the existing competence to map to.
			if ((oldCompetences != null) && (oldCompetences.size() > 0)
				&& (getCompetenceFromSet(oldCompetences,
					competenceMapping.getCompetence().getTitle()) != null)) {
			    newMapping.setCompetence(
				    getCompetenceFromSet(oldCompetences, competenceMapping.getCompetence().getTitle()));
			    competenceMappingDAO.insert(newMapping);
			    newCompetenceMappings.add(newMapping);
			}
			// If competence was not already existing in the ld, add
			// a new mappping
			else if ((newCompetences != null) && (newCompetences.size() > 0)
				&& (getCompetenceFromSet(newCompetences,
					competenceMapping.getCompetence().getTitle()) != null)) {
			    newMapping.setCompetence(
				    getCompetenceFromSet(newCompetences, competenceMapping.getCompetence().getTitle()));
			    competenceMappingDAO.insert(newMapping);
			    newCompetenceMappings.add(newMapping);
			}
		    }
		}
		newToolActivity.getCompetenceMappings().addAll(newCompetenceMappings);
	    }
	}
    }

    public Competence getCompetenceFromSet(Set<Competence> competences, String title) {
	Competence ret = null;
	for (Competence competence : competences) {
	    if (competence.getTitle().equals(title)) {
		ret = competence;
		break;
	    }
	}
	return ret;
    }

    /**
     * Updates the competence information in the newLearningDesign based on the originalLearningDesign
     *
     * @param originalLearningDesign
     *            The LearningDesign to be copied
     * @param newLearningDesign
     *            The copy of the originalLearningDesign
     */
    public void updateCompetenceMappings(Set<Competence> newCompetences, HashMap<Integer, Activity> newActivities) {
	for (Integer activityKey : newActivities.keySet()) {
	    Activity activity = newActivities.get(activityKey);
	    if (activity.isToolActivity()) {
		Set<CompetenceMapping> newCompetenceMappings = new HashSet<CompetenceMapping>();
		ToolActivity newToolActivity = (ToolActivity) activity;
		if (newToolActivity.getCompetenceMappings() != null) {
		    for (CompetenceMapping competenceMapping : newToolActivity.getCompetenceMappings()) {
			CompetenceMapping newMapping = new CompetenceMapping();
			if (newCompetences != null) {
			    for (Competence newCompetence : newCompetences) {
				if (competenceMapping.getCompetence().getTitle().equals(newCompetence.getTitle())) {
				    newMapping.setToolActivity(newToolActivity);
				    newMapping.setCompetence(newCompetence);
				    competenceMappingDAO.insert(newMapping);
				    newCompetenceMappings.add(newMapping);
				}
			    }
			}
		    }
		}
		newToolActivity.getCompetenceMappings().addAll(newCompetenceMappings);
		// activityDAO.update(newToolActivity);
	    }
	}
    }

    private void updateEvaluations(HashMap<Integer, Activity> newActivities) {

	for (Integer key : newActivities.keySet()) {
	    Activity activity = newActivities.get(key);
	    if (activity.isToolActivity()) {
		Set<ActivityEvaluation> newActivityEvaluations = ((ToolActivity) activity).getActivityEvaluations();

		if (newActivityEvaluations != null) {
		    baseDAO.insertOrUpdateAll(newActivityEvaluations);
		}
	    }
	}
    }

    /**
     * Determines the type of activity and returns a deep-copy of the same
     *
     * @param activity
     *            The object to be deep-copied
     * @param newGroupings
     *            Temporary set of new groupings. Key is the grouping UUID. May not be null.
     * @return Activity The new deep-copied Activity object
     */
    private Activity getActivityCopy(final Activity activity, Map<Integer, Grouping> newGroupings, int uiidOffset) {
	if (Activity.GROUPING_ACTIVITY_TYPE == activity.getActivityTypeId().intValue()) {
	    GroupingActivity newGroupingActivity = (GroupingActivity) activity.createCopy(uiidOffset);
	    // now we need to manually add the grouping to the session, as we
	    // can't easily
	    // set up a cascade
	    Grouping grouping = newGroupingActivity.getCreateGrouping();
	    grouping.setGroupingUIID(grouping.getGroupingUIID());
	    if (grouping != null) {
		groupingDAO.insert(grouping);
		newGroupings.put(grouping.getGroupingUIID(), grouping);
	    }
	    return newGroupingActivity;
	} else {
	    return activity.createCopy(uiidOffset);
	}
    }

    /**
     * Returns a set of child activities for the given parent activity
     *
     * @param parentActivity
     *            The parent activity
     * @return HashSet Set of the activities that belong to the parentActivity
     */
    private HashSet getChildActivities(Activity parentActivity) {
	HashSet childActivities = new HashSet();
	List list = activityDAO.getActivitiesByParentActivityId(parentActivity.getActivityId());
	if (list != null) {
	    childActivities.addAll(list);
	}
	return childActivities;
    }

    /**
     * This method saves a new Learning Design to the database. It received a WDDX packet from flash, deserializes it
     * and then finally persists it to the database.
     *
     * A user may update an existing learning design if they have user/owner rights to the folder or they are doing live
     * edit. A user may create a new learning design only if they have user/owner rights to the folder.
     *
     * Note: it does not validate the design - that must be done separately.
     *
     * @param wddxPacket
     *            The WDDX packet received from Flash
     * @return Long learning design id
     * @throws Exception
     */
    @Override
    public Long storeLearningDesignDetails(String wddxPacket) throws Exception {

	Hashtable table = (Hashtable) WDDXProcessor.deserialize(wddxPacket);
	Integer workspaceFolderID = WDDXProcessor.convertToInteger(table, WDDXTAGS.WORKSPACE_FOLDER_ID);

	User user = null;
	Integer userID = AuthoringService.getUserId();
	if (userID != null) {
	    user = (User) baseDAO.find(User.class, userID);
	}
	if (user == null) {
	    throw new UserException("UserID missing or user not found.");
	}

	WorkspaceFolder workspaceFolder = null;
	boolean authorised = false;
	if (workspaceFolderID != null) {
	    workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
	    authorised = workspaceManagementService.isUserAuthorizedToModifyFolderContents(workspaceFolderID, userID);
	}

	Long learningDesignId = WDDXProcessor.convertToLong(table, "learningDesignID");
	LearningDesign existingLearningDesign = learningDesignId != null
		? learningDesignDAO.getLearningDesignById(learningDesignId) : null;
	if (!authorised && (existingLearningDesign != null)
		&& Boolean.TRUE.equals(existingLearningDesign.getEditOverrideLock())) {
	    authorised = userID.equals(existingLearningDesign.getEditOverrideUser().getUserId());
	}
	if (!authorised) {
	    throw new UserException("User with user_id of " + userID
		    + " is not authorized to store a design in this workspace folder " + workspaceFolderID);
	}

	IObjectExtractor extractor = (IObjectExtractor) beanFactory
		.getBean(IObjectExtractor.OBJECT_EXTRACTOR_SPRING_BEANNAME);
	LearningDesign design = extractor.extractSaveLearningDesign(table, existingLearningDesign, workspaceFolder,
		user);

	if (extractor.getMode().intValue() == 1) {

	    // adding the customCSV to the call if it is present
	    String customCSV = null;
	    if (table.containsKey(WDDXTAGS.CUSTOM_CSV)) {
		customCSV = WDDXProcessor.convertToString(table, WDDXTAGS.CUSTOM_CSV);
	    }

	    copyLearningDesignToolContent(design, design, design.getCopyTypeID(), customCSV);
	}

	if (existingLearningDesign != null) {
	    AuthoringService.deleteLearningDesignImages(existingLearningDesign.getLearningDesignId());
	}

	logEventService.logEvent(LogEvent.TYPE_TEACHER_LEARNING_DESIGN_CREATE, userID, design.getLearningDesignId(),
		null, null);

	return design.getLearningDesignId();
    }

    @Override
    public LearningDesign saveLearningDesignDetails(JSONObject ldJSON)
	    throws UserException, JSONException, WorkspaceFolderException, ObjectExtractorException, ParseException {
	User user = null;
	Integer userID = AuthoringService.getUserId();
	if (userID != null) {
	    user = (User) baseDAO.find(User.class, userID);
	}
	if (user == null) {
	    throw new UserException("UserID missing or user not found.");
	}

	Integer workspaceFolderID = ldJSON.getInt("workspaceFolderID");
	WorkspaceFolder workspaceFolder = null;
	boolean authorised = false;
	if (workspaceFolderID != null) {
	    workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
	    authorised = workspaceManagementService.isUserAuthorizedToModifyFolderContents(workspaceFolderID, userID);
	}

	Long learningDesignId = ldJSON.optLong(AttributeNames.PARAM_LEARNINGDESIGN_ID);
	LearningDesign existingLearningDesign = learningDesignId == null ? null
		: learningDesignDAO.getLearningDesignById(learningDesignId);
	if (!authorised && (existingLearningDesign != null)
		&& Boolean.TRUE.equals(existingLearningDesign.getEditOverrideLock())) {
	    authorised = userID.equals(existingLearningDesign.getEditOverrideUser().getUserId());
	}
	if (!authorised) {
	    throw new UserException("User with ID " + userID
		    + " is not authorized to store a design in this workspace folder " + workspaceFolderID);
	}

	IObjectExtractor extractor = (IObjectExtractor) beanFactory
		.getBean(IObjectExtractor.OBJECT_EXTRACTOR_SPRING_BEANNAME);
	LearningDesign design = extractor.extractSaveLearningDesign(ldJSON, existingLearningDesign, workspaceFolder,
		user);

	if (extractor.getMode().intValue() == 1) {
	    // adding the customCSV to the call if it is present
	    String customCSV = ldJSON.optString("customCSV");

	    copyLearningDesignToolContent(design, design, design.getCopyTypeID(), customCSV);
	}

	logEventService.logEvent(LogEvent.TYPE_TEACHER_LEARNING_DESIGN_CREATE, userID, design.getLearningDesignId(),
		null, null);

	return design;
    }

    /**
     * Validate the learning design, updating the valid flag appropriately.
     *
     * This needs to be run in a separate transaction to storeLearningDesignDetails to ensure the database is fully
     * updated before the validation occurs (due to some quirks we are finding using Hibernate)
     *
     * @param learningDesignId
     * @throws Exception
     */
    @Override
    public Vector<ValidationErrorDTO> validateLearningDesign(Long learningDesignId) {
	LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignId);
	Vector<ValidationErrorDTO> listOfValidationErrorDTOs = learningDesignService
		.validateLearningDesign(learningDesign);
	Boolean valid = listOfValidationErrorDTOs.size() > 0 ? Boolean.FALSE : Boolean.TRUE;
	learningDesign.setValidDesign(valid);
	learningDesignDAO.insertOrUpdate(learningDesign);
	return listOfValidationErrorDTOs;
    }

    @Override
    public Vector<AuthoringActivityDTO> getToolActivities(Long learningDesignId, String languageCode) {
	LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignId);
	Vector<AuthoringActivityDTO> listOfAuthoringActivityDTOs = new Vector<AuthoringActivityDTO>();

	for (Iterator i = learningDesign.getActivities().iterator(); i.hasNext();) {
	    Activity currentActivity = (Activity) i.next();
	    if (currentActivity.isToolActivity()) {
		try {
		    // Normally we pass in an array for the branch mappings as
		    // the second parameter to new AuthoringActivityDTO()
		    // but we don't need to in this case as it is only doing it
		    // for tool activities, and the extra parameter is only
		    // used for branching activities
		    ToolActivity toolActivity = (ToolActivity) activityDAO
			    .getActivityByActivityId(currentActivity.getActivityId());
		    AuthoringActivityDTO activityDTO = new AuthoringActivityDTO(toolActivity, null, languageCode);
		    listOfAuthoringActivityDTOs.add(activityDTO);
		} catch (ToolException e) {
		    String error = "" + e.getMessage();
		    log.error(error, e);
		    throw new LearningDesignException(error, e);
		}
	    }
	}

	return listOfAuthoringActivityDTOs;
    }

    /**
     * This is a utility method used by the method <code>getAllLearningDesignDetails</code> to pack the required
     * information in a data transfer object.
     *
     * @param iterator
     * @return Hashtable The required information in a Hashtable
     */
    private ArrayList createDesignDetailsPacket(Iterator iterator) {
	ArrayList arrayList = new ArrayList();
	while (iterator.hasNext()) {
	    LearningDesign learningDesign = (LearningDesign) iterator.next();
	    DesignDetailDTO designDetailDTO = learningDesign.getDesignDetailDTO();
	    arrayList.add(designDetailDTO);
	}
	return arrayList;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraryDetails()
     */
    @Override
    public String getAllLearningLibraryDetails(String languageCode) throws IOException {
	FlashMessage flashMessage = new FlashMessage("getAllLearningLibraryDetails",
		learningDesignService.getAllLearningLibraryDetails(languageCode));
	return flashMessage.serializeMessage();
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#insertToolContentID(java.lang.Long)
     */

    @Override
    public String getToolContentIDFlash(Long toolID) throws IOException {
	Long toolContentID = insertToolContentID(toolID);
	if (toolContentID == null) {
	    return FlashMessage.getNoSuchTool("getToolContentID", toolID).serializeMessage();
	}

	FlashMessage flashMessage = new FlashMessage("getToolContentID", toolContentID);
	return flashMessage.serializeMessage();
    }

    @Override
    public Long insertToolContentID(Long toolID) {
	Tool tool = toolDAO.getToolByID(toolID);
	if (tool == null) {
	    log.error("The toolID " + toolID + " is not valid. A Tool with tool id " + toolID
		    + " does not exist on the database.");
	    return null;
	}

	return contentIDGenerator.getNextToolContentIDFor(tool);
    }

    @Override
    public Long copyToolContent(Long toolContentID, String customCSV) throws IOException {
	return lamsCoreToolService.notifyToolToCopyContent(toolContentID, customCSV);
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyMultipleToolContent(java.lang.Integer,
     *      java.util.List)
     */
    @Override
    public String copyMultipleToolContent(Integer userId, List<Long> toolContentIds, String customCSV) {
	StringBuffer idMap = new StringBuffer();
	for (Long oldToolContentId : toolContentIds) {
	    if (oldToolContentId != null) {
		Long newToolContentId = lamsCoreToolService.notifyToolToCopyContent(oldToolContentId, customCSV);
		idMap.append(oldToolContentId);
		idMap.append('=');
		idMap.append(newToolContentId);
		idMap.append(',');
	    }
	}
	// return the id list, stripping off the trailing ,
	return idMap.length() > 0 ? idMap.substring(0, idMap.length() - 1) : "";
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAvailableLicenses()
     */
    @Override
    public Vector getAvailableLicenses() {
	List licenses = licenseDAO.findAll(License.class);
	Vector licenseDTOList = new Vector(licenses.size());
	Iterator iter = licenses.iterator();
	while (iter.hasNext()) {
	    License element = (License) iter.next();
	    licenseDTOList.add(element.getLicenseDTO(Configuration.get(ConfigurationKeys.SERVER_URL)));
	}
	return licenseDTOList;
    }

    /**
     * Delete a learning design from the database. Does not remove any content stored in tools - that is done by the
     * LamsCoreToolService
     */
    @Override
    public void deleteLearningDesign(LearningDesign design) {
	if (design == null) {
	    log.error("deleteLearningDesign: unable to delete learning design as design is null.");
	    return;
	}

	// remove all the tool content for the learning design
	Set acts = design.getActivities();
	Iterator iter = acts.iterator();
	while (iter.hasNext()) {
	    Activity activity = (Activity) iter.next();
	    if (activity.isToolActivity()) {
		try {
		    ToolActivity toolActivity = (ToolActivity) activityDAO
			    .getActivityByActivityId(activity.getActivityId());
		    lamsCoreToolService.notifyToolToDeleteContent(toolActivity);
		} catch (ToolException e) {
		    log.error(
			    "Unable to delete tool content for activity" + activity + " as activity threw an exception",
			    e);
		}
	    }
	}

	// remove the learning design
	learningDesignDAO.delete(design);
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#generateUniqueContentFolder()
     */
    @Override
    public String generateUniqueContentFolder() throws FileUtilException, IOException {

	String newUniqueContentFolderID = FileUtil.generateUniqueContentFolderID();

	FlashMessage flashMessage = new FlashMessage("createUniqueContentFolder", newUniqueContentFolderID);

	return flashMessage.serializeMessage();
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getHelpURL()
     */
    @Override
    public String getHelpURL() throws Exception {

	FlashMessage flashMessage = null;

	String helpURL = Configuration.get(ConfigurationKeys.HELP_URL);
	if (helpURL != null) {
	    flashMessage = new FlashMessage("getHelpURL", helpURL);
	} else {
	    throw new Exception();
	}

	return flashMessage.serializeMessage();
    }

    /**
     * Get a unique name for a learning design, based on the names of the learning designs in the folder. If the
     * learning design has duplicated name in same folder, then the new name will have a timestamp. The new name format
     * will be oldname_ddMMYYYY_idx. The idx will be auto incremental index number, start from 1. Warning - this may be
     * quite intensive as it gets all the learning designs in a folder.
     *
     * @param originalLearningDesign
     * @param workspaceFolder
     * @param copyType
     * @return
     */
    @Override
    public String getUniqueNameForLearningDesign(String originalTitle, Integer workspaceFolderId) {

	String newName = originalTitle;
	if (workspaceFolderId != null) {
	    List<String> ldTitleList = learningDesignDAO.getLearningDesignTitlesByWorkspaceFolder(workspaceFolderId,
		    originalTitle);
	    int idx = 1;

	    Calendar calendar = Calendar.getInstance();
	    int mth = calendar.get(Calendar.MONTH) + 1;
	    String mthStr = new Integer(mth).toString();
	    if (mth < 10) {
		mthStr = "0" + mthStr;
	    }
	    int day = calendar.get(Calendar.DAY_OF_MONTH);
	    String dayStr = new Integer(day).toString();
	    if (day < 10) {
		dayStr = "0" + dayStr;
	    }
	    String nameMid = dayStr + mthStr + calendar.get(Calendar.YEAR);
	    while (ldTitleList.contains(newName)) {
		newName = originalTitle + "_" + nameMid + "_" + idx;
		idx++;
	    }
	}
	return newName;
    }

    /**
     * Creates a LD with only one, given activity. If organisation is given, the LD will not be stored in user folder,
     * but will be put straight into run sequences folder of the organisation.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Long insertSingleActivityLearningDesign(String learningDesignTitle, Long toolID, Long toolContentID,
	    Long learningLibraryID, String contentFolderID, Integer organisationID) {
	Integer userID = AuthoringService.getUserId();
	User user = (User) baseDAO.find(User.class, userID);

	LearningDesign learningDesign = new LearningDesign(null, null, null, learningDesignTitle, null, null, 1, false,
		false, null, null, null, new Date(), Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER), user,
		user, null, null, null, null, null, null, null, null, contentFolderID, false, null, 1, null);

	WorkspaceFolder folder = null;

	if (organisationID == null) {
	    folder = user.getWorkspace().getDefaultFolder();
	    learningDesign.setCopyTypeID(LearningDesign.COPY_TYPE_NONE);
	} else {
	    learningDesign.setCopyTypeID(LearningDesign.COPY_TYPE_LESSON);
	    // taken from MonitoringService#initializeLesson()
	    int MAX_DEEP_LEVEL_FOLDER = 50;
	    Organisation organisation = (Organisation) baseDAO.find(Organisation.class, organisationID);
	    for (int idx = 0; idx < MAX_DEEP_LEVEL_FOLDER; idx++) {
		if ((organisation == null) || (folder != null)) {
		    break;
		}
		Workspace workspace = organisation.getWorkspace();
		if (workspace != null) {
		    folder = workspace.getDefaultRunSequencesFolder();
		}
		if (folder == null) {
		    organisation = organisation.getParentOrganisation();
		}
	    }
	}

	learningDesign.setWorkspaceFolder(folder);
	learningDesignDAO.insert(learningDesign);

	ToolActivity templateActivity = (ToolActivity) activityDAO.getTemplateActivityByLibraryID(learningLibraryID);
	ToolActivity activity = (ToolActivity) templateActivity.createCopy(1);
	activity.setLearningDesign(learningDesign);
	activity.setToolContentId(toolContentID);
	activity.setActivityUIID(1);
	// some random coordinates
	activity.setXcoord(300);
	activity.setYcoord(300);
	activityDAO.insert(activity);

	// make Gradebook aware of the activity
	List<ToolOutputDefinitionDTO> defnDTOList = getToolOutputDefinitions(toolContentID,
		ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);
	String gradebookToolOutputDefinitionName = null;
	for (ToolOutputDefinitionDTO definition : defnDTOList) {
	    // find the default output
	    if (definition.getIsDefaultGradebookMark()) {
		gradebookToolOutputDefinitionName = definition.getName();
		break;
	    }
	}
	if (gradebookToolOutputDefinitionName != null) {
	    ActivityEvaluation evaluation = new ActivityEvaluation();
	    evaluation.setActivity(activity);
	    evaluation.setToolOutputDefinition(gradebookToolOutputDefinitionName);
	    baseDAO.insert(evaluation);
	}

	learningDesign.getActivities().add(activity);
	learningDesign.setFirstActivity(activity);
	learningDesign.setValidDesign(true);
	learningDesignDAO.update(learningDesign);

	Long learningDesingID = learningDesign.getLearningDesignId();

	LogEvent logEvent = new LogEvent();
	logEvent.setLogEventTypeId(LogEvent.TYPE_TEACHER_LEARNING_DESIGN_CREATE);
	logEvent.setLearningDesignId(learningDesingID);
	logEvent.setUser(user);
	logEvent.setOccurredDateTime(learningDesign.getCreateDateTime());
	baseDAO.insert(logEvent);

	if (log.isDebugEnabled()) {
	    log.debug("Created a single activity LD with ID: " + learningDesingID);
	}
	return learningDesingID;
    }

    @Override
    public Grouping getGroupingById(Long groupingID) {
	return groupingDAO.getGroupingById(groupingID);
    }

    @Override
    public String getToolAuthorUrl(Long toolID, Long toolContentID, String contentFolderID) {
	Tool tool = toolDAO.getToolByID(toolID);
	if (tool == null) {
	    log.error("The toolID " + toolID + " is not valid. A Tool with tool id " + toolID
		    + " does not exist on the database.");
	    return null;
	}

	String authorUrl = Configuration.get(ConfigurationKeys.SERVER_URL) + tool.getAuthorUrl();
	if (toolContentID != null) {
	    authorUrl = WebUtil.appendParameterToURL(authorUrl, AttributeNames.PARAM_TOOL_CONTENT_ID,
		    toolContentID.toString());
	}
	if (contentFolderID != null) {
	    authorUrl = WebUtil.appendParameterToURL(authorUrl, AttributeNames.PARAM_CONTENT_FOLDER_ID,
		    contentFolderID);
	}
	return authorUrl;
    }

    /**
     * Fetches latest LD access by Author. Optionally removes broken entries. The method is prefixed with update* to
     * make the transaction writable.
     */
    @Override
    public List<LearningDesignAccess> updateLearningDesignAccessByUser(Integer userId) {
	List<LearningDesignAccess> accessList = learningDesignDAO.getAccessByUser(userId);
	List<LearningDesignAccess> result = new LinkedList<LearningDesignAccess>();
	for (LearningDesignAccess access : accessList) {
	    LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(access.getLearningDesignId());
	    if (learningDesign == null) {
		log.warn("When getting recent access list for Author with ID " + userId + " LD with ID "
			+ access.getLearningDesignId() + " was found missing. Deleting access entry.");
		baseDAO.delete(access);
	    } else {
		access.setTitle(learningDesign.getTitle());
		access.setWorkspaceFolderId(learningDesign.getWorkspaceFolder().getWorkspaceFolderId());
		result.add(access);
	    }
	}
	return result;
    }

    @Override
    public void storeLearningDesignAccess(Long learningDesignId, Integer userId) {
	LearningDesignAccess access = new LearningDesignAccess();
	access.setLearningDesignId(learningDesignId);
	access.setUserId(userId);
	access.setAccessDate(new Date());
	learningDesignDAO.insertOrUpdate(access);
    }

    /**
     * Copies LD thumbnails, SVG and PNG.
     */
    private static void copyLearningDesignImages(long originalLearningDesignID, long newLearningDesignID)
	    throws IOException {
	for (String extension : AuthoringService.LD_IMAGE_EXTENSIONS) {
	    String fullExtension = "." + extension;
	    File image = new File(IAuthoringService.LEARNING_DESIGN_IMAGES_FOLDER,
		    originalLearningDesignID + fullExtension);
	    if (image.canRead()) {
		FileUtils.copyFile(image,
			new File(IAuthoringService.LEARNING_DESIGN_IMAGES_FOLDER, newLearningDesignID + fullExtension),
			false);
	    }
	}
    }

    /**
     * Deletes LD thumbnails, SVG and PNG.
     */
    private static void deleteLearningDesignImages(long learningDesignID) throws IOException {
	for (String extension : AuthoringService.LD_IMAGE_EXTENSIONS) {
	    String fullExtension = "." + extension;
	    File image = new File(IAuthoringService.LEARNING_DESIGN_IMAGES_FOLDER, learningDesignID + fullExtension);
	    FileUtils.deleteQuietly(image);
	}
    }
}