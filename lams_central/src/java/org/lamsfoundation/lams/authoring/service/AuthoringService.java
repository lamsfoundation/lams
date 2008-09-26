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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.IObjectExtractor;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
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
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceException;
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
import org.lamsfoundation.lams.usermanagement.User;
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
	protected ILamsCoreToolService lamsCoreToolService;
	protected ILearningDesignService learningDesignService;
	protected MessageService messageService;
	protected ILessonService lessonService;
	protected IMonitoringService monitoringService;
	protected IWorkspaceManagementService workspaceManagementService;

	protected ToolContentIDGenerator contentIDGenerator;

	/** The bean factory is used to create ObjectExtractor objects */
	protected BeanFactory beanFactory;

	public AuthoringService() {

	}

	/**********************************************
	 * Setter Methods
	 * *******************************************/
	/**
	 * Set i18n MessageService
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * @param groupDAO The groupDAO to set.
	 */
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public void setGroupingDAO(GroupingDAO groupingDAO) {
		this.groupingDAO = groupingDAO;
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
	 * @param transitionDAO The transitionDAO  to set
	 */
	public void setTransitionDAO(TransitionDAO transitionDAO) {
		this.transitionDAO = transitionDAO;
	}

	/**
	 * @param learningDesignDAO The learningDesignDAO to set.
	 */
	public void setLearningDesignDAO(LearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}

	/**
	 * @param learningLibraryDAO The learningLibraryDAO to set.
	 */
	public void setLearningLibraryDAO(LearningLibraryDAO learningLibraryDAO) {
		this.learningLibraryDAO = learningLibraryDAO;
	}

	/**
	 * @param baseDAO The baseDAO to set.
	 */
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	/**
	 * @param activityDAO The activityDAO to set.
	 */
	public void setActivityDAO(ActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}

	/**
	 * @param toolDAO The toolDAO to set 
	 */
	public void setToolDAO(ToolDAO toolDAO) {
		this.toolDAO = toolDAO;
	}

	/**
	 * @param toolDAO The toolDAO to set 
	 */
	public void setSystemToolDAO(SystemToolDAO systemToolDAO) {
		this.systemToolDAO = systemToolDAO;
	}

	/**
	 * @param licenseDAO The licenseDAO to set
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
	 * @param learningDesignService The Learning Design Validator Service
	 */
	public void setLearningDesignService(ILearningDesignService learningDesignService) {
		this.learningDesignService = learningDesignService;
	}

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

	/**
	 * @param contentIDGenerator The contentIDGenerator to set.
	 */
	public void setContentIDGenerator(ToolContentIDGenerator contentIDGenerator) {
		this.contentIDGenerator = contentIDGenerator;
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesign(java.lang.Long)
	 */
	public LearningDesign getLearningDesign(Long learningDesignID) {
		return learningDesignDAO.getLearningDesignById(learningDesignID);
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#saveLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign)
	 */
	public void saveLearningDesign(LearningDesign learningDesign) {
		learningDesignDAO.insertOrUpdate(learningDesign);
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningDesigns()
	 */
	public List getAllLearningDesigns() {
		return learningDesignDAO.getAllLearningDesigns();
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraries()
	 */
	public List getAllLearningLibraries() {
		return learningLibraryDAO.getAllLearningLibraries();
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**********************************************
	 * Utility/Service Methods
	 * *******************************************/

	/**
	 * Helper method to retrieve the user data. Gets the id from the user details
	 * in the shared session
	 * @return the user id
	 */
	public static Integer getUserId() {
		HttpSession ss = SessionManager.getSession();
		UserDTO learner = (UserDTO) ss.getAttribute(AttributeNames.USER);
		return learner != null ? learner.getUserID() : null;
	}

	/**
	* @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getToolOutputDefinitions(java.lang.Long)
	*/
	public String getToolOutputDefinitions(Long toolContentID) throws IOException {

		SortedMap<String, ToolOutputDefinition> defns = lamsCoreToolService.getOutputDefinitionsFromTool(toolContentID);

		ArrayList<ToolOutputDefinitionDTO> defnDTOList = new ArrayList<ToolOutputDefinitionDTO>(defns != null ? defns.size() : 0);
		if (defns != null) {
			for (ToolOutputDefinition defn : defns.values()) {
				defnDTOList.add(new ToolOutputDefinitionDTO(defn));
			}
		}

		FlashMessage flashMessage = new FlashMessage("getToolOutputDefinitions", defnDTOList);
		return flashMessage.serializeMessage();
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesignDetails(java.lang.Long, java.lang.Long)
	 */
	public String getLearningDesignDetails(Long learningDesignID, String languageCode) throws IOException {
		FlashMessage flashMessage = null;

		LearningDesignDTO learningDesignDTO = learningDesignService.getLearningDesignDTO(learningDesignID, languageCode);

		if (learningDesignDTO == null) {
			flashMessage = FlashMessage.getNoSuchLearningDesignExists("getLearningDesignDetails", learningDesignID);
		}
		else {
			flashMessage = new FlashMessage("getLearningDesignDetails", learningDesignDTO);
		}
		return flashMessage.serializeMessage();
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#isLearningDesignAvailable(LearningDesign, java.lang.Integer)
	 */
	public boolean isLearningDesignAvailable(LearningDesign design, Integer userID) throws LearningDesignException, IOException {
		if (design == null) {
			throw new LearningDesignException(FlashMessage.getNoSuchLearningDesignExists("getLearningDesignDetails",
					design.getLearningDesignId()).serializeMessage());
		}

		if (design.getEditOverrideUser() != null && design.getEditOverrideLock() != null) {
			return design.getEditOverrideUser().getUserId().equals(userID) ? true : !design.getEditOverrideLock();
		}
		else {
			return true;
		}
	}

	private void setLessonLock(LearningDesign design, boolean lock) {
		Lesson lesson = null;

		// lock active lesson
		Set lessons = design.getLessons();
		Iterator it = lessons.iterator();

		while (it.hasNext()) {
			lesson = (Lesson) it.next();
			lesson.setLockedForEdit(lock);
		}
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#setupEditOnFlyLock(LearningDesign, java.lang.Integer)
	 */
	public boolean setupEditOnFlyLock(Long learningDesignID, Integer userID) throws LearningDesignException, UserException,
			IOException {
		User user = (User) baseDAO.find(User.class, userID);

		LearningDesign design = learningDesignID != null ? getLearningDesign(learningDesignID) : null;

		if (isLearningDesignAvailable(design, userID)) {

			if (design.getLessons().isEmpty()) {
				throw new LearningDesignException("There are no lessons attached to the design."); // TODO: add error msg
			}
			else if (user == null) {
				throw new UserException(messageService.getMessage("no.such.user.exist", new Object[] { userID }));
			}

			setLessonLock(design, true);

			// lock Learning Design
			design.setEditOverrideLock(true);
			design.setEditOverrideUser(user);

			learningDesignDAO.update(design);

			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#setupEditOnFlyGate(java.lang.Long, java.lang.Integer)
	 */
	public String setupEditOnFlyGate(Long learningDesignID, Integer userID) throws UserException, IOException {
		User user = (User) baseDAO.find(User.class, userID);
		LearningDesign design = learningDesignID != null ? getLearningDesign(learningDesignID) : null;

		if (user == null) {
			throw new UserException(messageService.getMessage("no.such.user.exist", new Object[] { userID }));
		}

		EditOnFlyProcessor processor = new EditOnFlyProcessor(design, activityDAO); /* parse Learning Design to find last read-only Activity */
		processor.parseLearningDesign();

		ArrayList<Activity> activities = processor.getLastReadOnlyActivity();
		addSystemGateAfterActivity(activities, design); /* add new System Gate after last read-only Activity */

		setLessonLock(design, false);

		learningDesignDAO.update(design);

		return new FlashMessage("setupEditOnFlyGate", true).serializeMessage();
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#finishEditOnFly(java.lang.Long, java.lang.Integer)
	 */
	public String finishEditOnFly(Long learningDesignID, Integer userID) throws IOException {
		FlashMessage flashMessage = null;
		Lesson lesson = null;

		LearningDesign design = learningDesignID != null ? learningDesignDAO.getLearningDesignById(learningDesignID) : null;

		User user = (User) baseDAO.find(User.class, userID);
		if (user == null) {
			flashMessage = FlashMessage.getNoSuchUserExists("finishEditOnFly", userID);
		}

		if (design != null) { /* only the user who is editing the design may unlock it */
			if (design.getEditOverrideUser().equals(user)) {
				design.setEditOverrideLock(false);
				design.setEditOverrideUser(null);

				Set lessons = design.getLessons(); /* unlock lesson */

				Iterator it = lessons.iterator();
				while (it.hasNext()) {
					lesson = (Lesson) it.next();
					lesson.setLockedForEdit(false);
				}

				EditOnFlyProcessor processor = new EditOnFlyProcessor(design, activityDAO); /* parse Learning Design to find last read-only Activity (hopefully the system gate in this case) */
				processor.parseLearningDesign();

				ArrayList<Activity> activities = processor.getLastReadOnlyActivity();

				GateActivity gate = null; /* open and release waiting list on system gate */

				if (activities != null) {
					if (!activities.isEmpty() && activities.get(0).isGateActivity()) {
						gate = (GateActivity) activities.get(0);
					}
				}

				if (gate != null) {
					design = removeTempSystemGate(gate, design); /* remove inputted system gate */
				}

				lessonService.performMarkLessonUncompleted(lesson.getLessonId()); /* the lesson may now have additional activities on the end, so clear any completed flags */

				initialiseToolActivityForRuntime(design, lesson);
				learningDesignDAO.insertOrUpdate(design);

				flashMessage = new FlashMessage("finishEditOnFly", lesson.getLessonId());

			}
			else {
				flashMessage = FlashMessage.getNoSuchUserExists("finishEditOnFly", userID);
			}
		}
		else {

			flashMessage = FlashMessage.getNoSuchLearningDesignExists("finishEditOnFly", learningDesignID);
		}

		return flashMessage.serializeMessage();

	}

	/** 
	 * Remove a temp. System Gate from the design. Requires removing the gate from any learner progress entries  - should only
	 * be a current activity but remove it from previous and next, just in case.
	 * 
	 * This will leave a "hole" in the learner progress, but the progress engine can take care of that.
	 * @param gate
	 * @param design
	 * @return Learning Design with removed System Gate
	 */
	public LearningDesign removeTempSystemGate(GateActivity gate, LearningDesign design) {
		Transition toTransition = gate.getTransitionTo(); /* get transitions */
		Transition fromTransition = gate.getTransitionFrom();

		if (toTransition != null && fromTransition != null) { /* rearrange to-transition and/or delete redundant transition */
			toTransition.setToActivity(fromTransition.getToActivity());
			toTransition.setToUIID(toTransition.getToActivity().getActivityUIID());

			design.getTransitions().remove(fromTransition);
			transitionDAO.update(toTransition);

		}
		else if (toTransition != null && fromTransition == null) {
			design.getTransitions().remove(toTransition);
		}
		else if (toTransition == null && fromTransition != null) {
			design.setFirstActivity(fromTransition.getToActivity());
			design.getTransitions().remove(fromTransition);
		}

		design.getActivities().remove(gate); /* remove temp system gate */

		design.setDesignVersion(design.getDesignVersion() + 1); /* increment design version field */

		lessonService.removeProgressReferencesToActivity(gate); /* need to remove it from any learner progress entries */

		return design;
	}

	/**
	 * Add a temp. System Gate. to the design.
	 * 
	 * @param activities
	 * @param design
	 */
	public void addSystemGateAfterActivity(ArrayList<Activity> activities, LearningDesign design) {
		GateActivity gate = null;

		Integer syncType = new Integer(Activity.SYSTEM_GATE_ACTIVITY_TYPE);
		Integer activityType = new Integer(Activity.SYSTEM_GATE_ACTIVITY_TYPE);
		Integer maxId = design.getMaxID();
		String title = "System Gate"; /* messageService.getMessage(MSG_KEY_SYNC_GATE); */

		SystemTool systemTool = systemToolDAO.getSystemToolByID(SystemTool.SYSTEM_GATE);
		Activity activity = activities.isEmpty() ? null : (Activity) activities.get(0);

		try { /* create new System Gate Activity */
			gate = (GateActivity) Activity.getActivityInstance(syncType.intValue());
			gate.setActivityTypeId(activityType.intValue());
			gate.setActivityCategoryID(Activity.CATEGORY_SYSTEM);
			gate.setSystemTool(systemTool);
			gate.setActivityUIID(++maxId);
			gate.setTitle(title != null ? title : "Gate");
			gate.setGateOpen(false);
			gate.setWaitingLearners(null);
			gate.setGateActivityLevelId(GateActivity.LEARNER_GATE_LEVEL);
			gate.setApplyGrouping(false); // not nullable so default to false
			gate.setGroupingSupportType(Activity.GROUPING_SUPPORT_OPTIONAL);
			gate.setOrderId(null);
			gate.setDefineLater(Boolean.FALSE);
			gate.setCreateDateTime(new Date());
			gate.setRunOffline(Boolean.FALSE);
			gate.setReadOnly(Boolean.TRUE);
			gate.setLearningDesign(design);

			design.getActivities().add(gate);
			baseDAO.insert(gate);

			Transition fromTransition;
			Transition newTransition = new Transition();
			Activity toActivity = null;

			if (activity != null) {
				fromTransition = activity.getTransitionFrom(); /* update transitions */

				if (fromTransition != null) {
					toActivity = fromTransition.getToActivity();

					fromTransition.setToActivity(gate);
					fromTransition.setToUIID(gate.getActivityUIID());

					newTransition.setTransitionUIID(++maxId);
					newTransition.setFromActivity(gate);
					newTransition.setFromUIID(gate.getActivityUIID());
					newTransition.setToActivity(toActivity);
					newTransition.setToUIID(toActivity.getActivityUIID());
					newTransition.setLearningDesign(design);

					gate.setTransitionFrom(newTransition);

					toActivity.setTransitionTo(newTransition);

					Integer x1 = activity.getXcoord() != null ? activity.getXcoord() : 0; /* set x/y position for Gate */
					Integer x2 = toActivity.getXcoord() != null ? toActivity.getXcoord() : 0;

					gate.setXcoord(new Integer((x1.intValue() + 123 + x2.intValue()) / 2 - 13));

					Integer y1 = activity.getYcoord() != null ? activity.getYcoord() : 0;
					Integer y2 = toActivity.getYcoord() != null ? toActivity.getYcoord() : 0;

					gate.setYcoord(new Integer((y1.intValue() + 50 + y2.intValue()) / 2));

				}
				else {
					//fromTransition = newTransition;

					newTransition.setTransitionUIID(++maxId);
					newTransition.setFromActivity(activity);
					newTransition.setFromUIID(activity.getActivityUIID());
					newTransition.setToActivity(gate);
					newTransition.setToUIID(gate.getActivityUIID());
					newTransition.setLearningDesign(design);

					activity.setTransitionFrom(fromTransition);
					gate.setTransitionTo(fromTransition);

					Integer x1 = activity.getTransitionTo() != null ? activity.getTransitionTo().getFromActivity().getXcoord()
							: 0; /* set x/y position for Gate */
					Integer x2 = activity.getXcoord() != null ? activity.getXcoord() : 0;

					if (x1 != null && x2 != null) {
						gate.setXcoord(x2 >= x1 ? new Integer(x2.intValue() + 123 + 13 + 20) : new Integer(
								x2.intValue() - 13 - 20));
					}
					else {
						gate.setXcoord(new Integer(x2.intValue() + 123 + 13 + 20));
					}

					gate.setYcoord(activity.getYcoord() + 25);
				}

			}
			else {
				fromTransition = newTransition; /* no read-only activities insert gate at start of sequence */
				toActivity = design.getFirstActivity();

				newTransition.setTransitionUIID(++maxId);
				newTransition.setToActivity(toActivity);
				newTransition.setToUIID(toActivity.getActivityUIID());
				newTransition.setFromActivity(gate);
				newTransition.setFromUIID(gate.getActivityUIID());
				newTransition.setLearningDesign(design);

				gate.setTransitionFrom(fromTransition);
				toActivity.setTransitionTo(fromTransition);

				gate.setGateOpen(false); /* keep gate door closed to stop learner's from going past this point */

				design.setFirstActivity(gate); /* set gate as first activity in sequence */

				Integer x1 = toActivity.getXcoord() != null ? toActivity.getXcoord() : 0; /* set x/y position for Gate */
				Integer x2 = toActivity.getTransitionFrom() != null ? toActivity.getTransitionFrom().getToActivity().getXcoord()
						: null;

				if (x1 != null && x2 != null) {
					gate.setXcoord(x2 >= x1 ? new Integer(x1.intValue() - 13 - 20) : new Integer(x1.intValue() + 123 + 13 + 20));
				}
				else {
					gate.setXcoord(new Integer(x1.intValue() - 13 - 20));
				}

				gate.setYcoord(toActivity.getYcoord() + 25);
			}

			design.getTransitions().add(newTransition);
			design.setMaxID(maxId);

			design.setDesignVersion(design.getDesignVersion() + 1); /* increment design version field */

			if (gate != null) {
				activityDAO.update(gate);
			}
			if (activity != null) {
				activityDAO.update(activity);
			}
			if (toActivity != null) {
				activityDAO.update(toActivity);
			}

			if (fromTransition != null && !fromTransition.equals(newTransition)) {
				baseDAO.update(fromTransition);
			}
			if (newTransition != null) {
				baseDAO.insert(newTransition);
			}
			if (design != null) {
				learningDesignDAO.insertOrUpdate(design);
			}

		}
		catch (NullPointerException npe) {
			log.error(npe.getMessage(), npe);
		}

	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getFirstUnattemptedActivity(org.lamsfoundation.lams.learningdesign.LearningDesign)
	 */
	public Activity getFirstUnattemptedActivity(LearningDesign design) throws LearningDesignException {
		Activity activity = design.getFirstActivity();

		while (activity.getReadOnly() && activity.getTransitionFrom() != null) {
			activity = activity.getTransitionFrom().getToActivity();
		}

		return activity;
	}

	private void initialiseToolActivityForRuntime(LearningDesign design, Lesson lesson) throws MonitoringServiceException {
		Date now = new Date();

		Set activities = design.getActivities();
		for (Iterator i = activities.iterator(); i.hasNext();) {
			Activity activity = (Activity) i.next();

			if (activity.isInitialised()) {
				if (!activity.isActivityReadOnly() && activity.isToolActivity()) {
					// Activity is initialised so it was set up previously. So its tool content will be okay
					// but the run offline flags and define later flags might have been changed, so they need to be updated
					// Content ID shouldn't change, but we update it in case it does change while we update the status flags.
					ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
					Long newContentId = lamsCoreToolService.notifyToolOfStatusFlags(toolActivity);
					toolActivity.setToolContentId(newContentId);
				}

			}
			else {
				// this is a new activity - need to set up the content, do any scheduling, etc
				// always have to copy the tool content, even though it may point to unique content - that way if the 
				// teacher has double clicked on the tool icon (and hence set up a tool content id) but not saved any content
				// the code in copyToolContent will ensure that there is content for this activity. So we end up with a few 
				// unused records - we are trading database space for reliability. If we don't ensure that there is always
				// a content record, then shortcomings in the createToolSession code may throw exceptions.
				if (activity.isToolActivity()) {
					ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
					Long newContentId = lamsCoreToolService.notifyToolToCopyContent(toolActivity, true, null);
					toolActivity.setToolContentId(newContentId);

				}
				else {
					Integer newMaxId = monitoringService.startSystemActivity(activity, design.getMaxID(), now, lesson
							.getLessonName());
					if (newMaxId != null) {
						design.setMaxID(newMaxId);
					}
				}
				activity.setInitialised(Boolean.TRUE);
				activityDAO.update(activity);
			}
		}
	}

	public LearningDesign copyLearningDesign(Long originalDesignID, Integer copyType, Integer userID, Integer workspaceFolderID,
			boolean setOriginalDesign) throws UserException, LearningDesignException, WorkspaceFolderException, IOException {

		LearningDesign originalDesign = learningDesignDAO.getLearningDesignById(originalDesignID);
		if (originalDesign == null) {
			throw new LearningDesignException(messageService.getMessage("no.such.learningdesign.exist",
					new Object[] { originalDesignID }));
		}

		User user = (User) baseDAO.find(User.class, userID);
		if (user == null) {
			throw new UserException(messageService.getMessage("no.such.user.exist", new Object[] { userID }));
		}

		WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
		if (workspaceFolder == null) {
			throw new WorkspaceFolderException(messageService.getMessage("no.such.workspace.exist",
					new Object[] { workspaceFolderID }));
		}

		if (!workspaceManagementService.isUserAuthorizedToModifyFolderContents(workspaceFolder.getWorkspaceFolderId(), user
				.getUserId())) {
			throw new UserAccessDeniedException("User with user_id of " + user.getUserId()
					+ " is not authorized to copy a learning design into the workspace folder "
					+ workspaceFolder.getWorkspaceFolderId());
		}

		return copyLearningDesign(originalDesign, copyType, user, workspaceFolder, setOriginalDesign, null, null);
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign, java.lang.Integer, org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.usermanagement.WorkspaceFolder, java.lang.Boolean, java.lang.String)
	 */
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
		newLearningDesign.setEditOverrideLock(false); // clear the live edit flag
		learningDesignDAO.insert(newLearningDesign);

		updateDesignCompetences(originalLearningDesign, newLearningDesign, false);
		HashMap<Integer, Activity> newActivities = updateDesignActivities(originalLearningDesign, newLearningDesign, 0, customCSV);
		updateDesignTransitions(originalLearningDesign, newLearningDesign, newActivities, 0);

		// set first activity assumes that the transitions are all set up correctly.
		newLearningDesign.setFirstActivity(newLearningDesign.calculateFirstActivity());
		newLearningDesign.setLearningDesignUIID(originalLearningDesign.getLearningDesignUIID());

		updateCompetenceMappings(newLearningDesign.getCompetences(), newActivities);

		return newLearningDesign;
	}

	/**
	 * @throws UserException 
	 * @throws WorkspaceFolderException 
	 * @throws IOException 
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#insertLearningDesign(java.lang.Long, java.lang.Long, java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.Integer)
	 */
	public LearningDesign insertLearningDesign(Long originalDesignID, Long designToImportID, Integer userID,
			boolean createNewLearningDesign, String newDesignName, Integer workspaceFolderID, String customCSV)
			throws UserException, WorkspaceFolderException, IOException {

		User user = (User) baseDAO.find(User.class, userID);
		if (user == null) {
			throw new UserException(messageService.getMessage("no.such.user.exist", new Object[] { userID }));
		}

		LearningDesign mainDesign = learningDesignDAO.getLearningDesignById(originalDesignID);
		if (mainDesign == null) {
			throw new LearningDesignException(messageService.getMessage("no.such.learningdesign.exist",
					new Object[] { originalDesignID }));
		}

		LearningDesign designToImport = learningDesignDAO.getLearningDesignById(designToImportID);
		if (designToImport == null) {
			throw new LearningDesignException(messageService.getMessage("no.such.learningdesign.exist",
					new Object[] { designToImportID }));
		}

		if (createNewLearningDesign) {
			WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
			if (workspaceFolder == null) {
				throw new WorkspaceFolderException(messageService.getMessage("no.such.workspace.exist",
						new Object[] { workspaceFolderID }));
			}
			if (!workspaceManagementService.isUserAuthorizedToModifyFolderContents(workspaceFolder.getWorkspaceFolderId(), user
					.getUserId())) {
				throw new UserAccessDeniedException("User with user_id of " + user.getUserId()
						+ " is not authorized to store a copy a learning design into the workspace folder " + workspaceFolder);
			}

			mainDesign = copyLearningDesign(mainDesign, LearningDesign.COPY_TYPE_NONE, user, workspaceFolder, false,
					newDesignName, customCSV);
		}
		else {
			// updating the existing design so check the rights to the folder containing the design. If this is in live edit mode
			boolean authorised = workspaceManagementService.isUserAuthorizedToModifyFolderContents(mainDesign
					.getWorkspaceFolder().getWorkspaceFolderId(), user.getUserId());
			if (!authorised) {
				authorised = mainDesign.getEditOverrideLock() != null && mainDesign.getEditOverrideLock().booleanValue()
						&& userID.equals(mainDesign.getEditOverrideUser().getUserId());
			}
			if (!authorised) {
				throw new UserAccessDeniedException("User with user_id of " + user.getUserId()
						+ " is not authorized to update a learning design into the workspace folder "
						+ mainDesign.getWorkspaceFolder());
			}
		}

		// now dump the import design into our main sequence. Leave the first activity ui id for the design as it is.
		int uiidOffset = mainDesign.getMaxID().intValue();
		updateDesignCompetences(designToImport, mainDesign, true);
		HashMap<Integer, Activity> newActivities = updateDesignActivities(designToImport, mainDesign, uiidOffset, customCSV);
		updateDesignTransitions(designToImport, mainDesign, newActivities, uiidOffset);
		mainDesign.setMaxID(LearningDesign.addOffset(designToImport.getMaxID(), uiidOffset));
		mainDesign.setValidDesign(Boolean.FALSE);
		learningDesignDAO.update(mainDesign);

		insertCompetenceMappings(mainDesign.getCompetences(), designToImport.getCompetences(), newActivities);

		return mainDesign;

	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesignToolContent(org.lamsfoundation.lams.learningdesign.LearningDesign, org.lamsfoundation.lams.learningdesign.LearningDesign, java.lang.Integer)
	 */
	private LearningDesign copyLearningDesignToolContent(LearningDesign design, LearningDesign originalLearningDesign,
			Integer copyType) throws LearningDesignException {

		for (Iterator i = design.getActivities().iterator(); i.hasNext();) {
			Activity currentActivity = (Activity) i.next();
			if (currentActivity.isToolActivity()) {
				copyActivityToolContent(currentActivity, design.getCopyTypeID(), originalLearningDesign.getLearningDesignId(),
						null);
			}
		}

		return design;
	}

	/**
	 * @param originalLearningDesign
	 * @param copyType
	 * @param currentActivity
	 */
	private void copyActivityToolContent(Activity activity, Integer ldCopyType, Long originalLearningDesignId, String customCSV) {
		try {
			ToolActivity toolActivity = (ToolActivity) activity;
			// copy the content, but don't set the define later flags if it is preview
			Long newContentId = lamsCoreToolService.notifyToolToCopyContent(toolActivity,
					ldCopyType != LearningDesign.COPY_TYPE_PREVIEW, customCSV);
			toolActivity.setToolContentId(newContentId);

			// clear read only field
			toolActivity.setReadOnly(false);

		}
		catch (DataMissingException e) {
			String error = "Unable to copy a design / initialise the lesson. Data is missing for activity "
					+ activity.getActivityUIID() + " in learning design " + originalLearningDesignId
					+ " default content may be missing for the tool. Error was " + e.getMessage();
			log.error(error, e);
			throw new LearningDesignException(error, e);
		}
		catch (ToolException e) {
			String error = "Unable to copy a design / initialise the lesson. Tool encountered an error copying the data is missing for activity "
					+ activity.getActivityUIID()
					+ " in learning design "
					+ originalLearningDesignId
					+ " default content may be missing for the tool. Error was " + e.getMessage();
			log.error(error, e);
			throw new LearningDesignException(error, e);
		}
	}

	/**
	 * Updates the Activity information in the newLearningDesign based 
	 * on the originalLearningDesign. This any grouping details.
	 * 
	 * As new activities are created, the UIID is incremented by the uiidOffset. If we are just copying a sequence this will 
	 * be set to 0. But if we are importing a sequence into another sequence, this will be an offset value so we new ids guaranteed
	 * to be outside of the range of the main sequence (this may mean gaps in the uiids but that doesn't matter).
	 * 
	 * @param originalLearningDesign The LearningDesign to be copied
	 * @param newLearningDesign The copy of the originalLearningDesign
	 * @return Map of all the new activities, where the key is the UIID value. This is used as an input to updateDesignTransitions
	 */
	private HashMap<Integer, Activity> updateDesignActivities(LearningDesign originalLearningDesign,
			LearningDesign newLearningDesign, int uiidOffset, String customCSV) {
		HashMap<Integer, Activity> newActivities = new HashMap<Integer, Activity>(); // key is UIID   
		HashMap<Integer, Grouping> newGroupings = new HashMap<Integer, Grouping>(); // key is UIID

		// as we create the activities, we need to record any "first child" UIID's for the sequence activity to process later
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

		// Go back and find all the grouped activities and assign them the new groupings, based on the UIID. Can't
		// be done as we go as the grouping activity may be processed after the grouped activity.
		for (Activity activity : activities) {
			if (activity.getGroupingUIID() != null) {
				activity.setGrouping(newGroupings.get(activity.getGroupingUIID()));
			}
		}

		// fix up any old "default activity" in the complex activities and the input activities
		// and fix any branch mappings
		for (Activity activity : activities) {
			if (activity.isComplexActivity()) {
				ComplexActivity newComplex = (ComplexActivity) activity;
				Activity oldDefaultActivity = newComplex.getDefaultActivity();
				if (oldDefaultActivity != null) {
					Activity newDefaultActivity = newActivities.get(LearningDesign.addOffset(
							oldDefaultActivity.getActivityUIID(), uiidOffset));
					newComplex.setDefaultActivity(newDefaultActivity);
				}
			}

			if (activity.isSequenceActivity()) {
				SequenceActivity newSequenceActivity = (SequenceActivity) activity;
				// Need to check if the sets are not null as these are new objects and Hibernate may not have backed them with collections yet.
				if (newSequenceActivity.getBranchEntries() != null && newSequenceActivity.getBranchEntries().size() > 0) {

					Activity parentActivity = newSequenceActivity.getParentActivity();
					if (parentActivity.isChosenBranchingActivity() || parentActivity.isGroupBranchingActivity()
							&& parentActivity.getDefineLater() != null && parentActivity.getDefineLater().booleanValue()) {
						// Don't have any preset up entries for a teacher chosen or a define later group based branching.
						// Must be copying a design that was run previously.
						newSequenceActivity.getBranchEntries().clear();

					}
					else {
						Iterator beIter = newSequenceActivity.getBranchEntries().iterator();
						while (beIter.hasNext()) {
							// sequence activity will be correct but the branching activity and the grouping will be wrong
							// the condition was copied by the sequence activity copy
							BranchActivityEntry entry = (BranchActivityEntry) beIter.next();
							BranchingActivity oldBranchingActivity = (BranchingActivity) entry.getBranchingActivity();
							entry.setBranchingActivity(newActivities.get(LearningDesign.addOffset(oldBranchingActivity
									.getActivityUIID(), uiidOffset)));
							Group oldGroup = entry.getGroup();
							if (oldGroup != null) {
								Grouping oldGrouping = oldGroup.getGrouping();
								Grouping newGrouping = newGroupings.get(LearningDesign.addOffset(oldGrouping.getGroupingUIID(),
										uiidOffset));
								if (newGrouping != null) {
									entry.setGroup(newGrouping.getGroup(LearningDesign.addOffset(oldGroup.getGroupUIID(),
											uiidOffset)));
								}
							}
						}

					}
				}
			}

			if (activity.getInputActivities() != null && activity.getInputActivities().size() > 0) {
				Set<Activity> newInputActivities = new HashSet<Activity>();
				Iterator inputIter = activity.getInputActivities().iterator();
				while (inputIter.hasNext()) {
					Activity elem = (Activity) inputIter.next();
					newInputActivities.add(newActivities.get(LearningDesign.addOffset(elem.getActivityUIID(), uiidOffset)));
				}
				activity.getInputActivities().clear();
				activity.getInputActivities().addAll(newInputActivities);
			}
		}

		// The activities collection in the learning design may already exist (as we have already done a save on the design).
		// If so, we can't just override the existing collection as the cascade causes an error.
		// newLearningDesign.getActivities() will create a new TreeSet(new ActivityOrderComparator()) if there isn't an existing set
		// If the uiidOffset is > 0, then we are adding activities, so we don't want to clear first.
		if (uiidOffset == 0) {
			newLearningDesign.getActivities().clear();
		}
		newLearningDesign.getActivities().addAll(activities);

		// On very rare occasions, we've had Hibernate try to save the branching entries before saving the branching activity
		// which throws an exception as the branch_activity_id is null. So force any branching activities to save first.
		// And yes, this IS a hack. (See LDEV-1786)	
		for (Activity activity : activities) {
			if (activity.isBranchingActivity()) {
				activityDAO.insert(activity);
			}
		}

		return newActivities;

	}

	/** As part of updateDesignActivities(), process an activity and, via recursive calls, the activity's child activities. Need to keep track
	 * of any new groupings created so we can go back and update the grouped activities with their new groupings at the end. Also copies the 
	 * tool content.
	 * 
	 * @param activity Activity to process. May not be null.
	 * @param newLearningDesign The new learning design. May not be null.
	 * @param newActivities Temporary set of new activities - as activities are processed they are added to the set. May not be null.
	 * @param newGroupings Temporary set of new groupings. Key is the grouping UUID. May not be null.
	 * @param parentActivity This activity's parent activity (if one exists). May be null.
	 */
	private void processActivity(Activity activity, LearningDesign newLearningDesign, Map<Integer, Activity> newActivities,
			Map<Integer, Grouping> newGroupings, Activity parentActivity, Long originalLearningDesignId, int uiidOffset,
			String customCSV) {
		Activity newActivity = getActivityCopy(activity, newGroupings, uiidOffset);
		newActivity.setActivityUIID(newActivity.getActivityUIID());
		newActivity.setLearningDesign(newLearningDesign);
		newActivity.setReadOnly(false);
		if (parentActivity != null) {
			newActivity.setParentActivity(parentActivity);
			newActivity.setParentUIID(parentActivity.getActivityUIID());
		}
		newActivities.put(newActivity.getActivityUIID(), newActivity);

		if (newActivity.isToolActivity()) {
			copyActivityToolContent(newActivity, newLearningDesign.getCopyTypeID(), originalLearningDesignId, customCSV);
		}

		Set oldChildActivities = getChildActivities(activity);
		if (oldChildActivities != null) {
			Iterator childIterator = oldChildActivities.iterator();
			while (childIterator.hasNext()) {
				processActivity((Activity) childIterator.next(), newLearningDesign, newActivities, newGroupings, newActivity,
						originalLearningDesignId, uiidOffset, customCSV);
			}
		}
	}

	/**
	 * Updates the Transition information in the newLearningDesign based 
	 * on the originalLearningDesign
	 * 
	 * @param originalLearningDesign The LearningDesign to be copied 
	 * @param newLearningDesign The copy of the originalLearningDesign
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
				toActivity.setTransitionTo(newTransition);
			}
			if (newTransition.getFromUIID() != null) {
				fromActivity = newActivities.get(newTransition.getFromUIID());
				fromActivity.setTransitionFrom(newTransition);
			}
			newTransition.setToActivity(toActivity);
			newTransition.setFromActivity(fromActivity);
			newTransition.setLearningDesign(newLearningDesign);
			transitionDAO.insert(newTransition);
			newTransitions.add(newTransition);
		}

		// The transitions collection in the learning design may already exist (as we have already done a save on the design).
		// If so, we can't just override the existing collection as the cascade causes an error.
		// If the uiidOffset is > 0, then we are adding transitions (rather than replacing), so we don't want to clear first.
		if (newLearningDesign.getTransitions() != null) {
			if (uiidOffset == 0) {
				newLearningDesign.getTransitions().clear();
			}
			newLearningDesign.getTransitions().addAll(newTransitions);
		}
		else {
			newLearningDesign.setTransitions(newTransitions);
		}

	}

	/**
	 * Updates the competence information in the newLearningDesign based 
	 * on the originalLearningDesign
	 * 
	 * @param originalLearningDesign The LearningDesign to be copied 
	 * @param newLearningDesign The copy of the originalLearningDesign
	 */
	public void updateDesignCompetences(LearningDesign originalLearningDesign, LearningDesign newLearningDesign, boolean insert) {
		HashSet<Competence> newCompeteces = new HashSet<Competence>();

		Set<Competence> oldCompetences = originalLearningDesign.getCompetences();
		for (Competence competence : oldCompetences) {
			Competence newCompetence = competence.createCopy(competence);
			newCompetence.setLearningDesign(newLearningDesign);

			// check for existing competences to prevent duplicates
			if (competenceDAO.getCompetence(newLearningDesign, newCompetence.getTitle()) == null) {
				competenceDAO.saveOrUpdate(newCompetence);
			}
			newCompeteces.add(newCompetence);
		}

		if (newLearningDesign.getCompetences() != null) {
			if (!insert) {
				newLearningDesign.getCompetences().clear();
				newLearningDesign.getCompetences().addAll(newCompeteces);
			}
			else {
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

		}
		else {
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

						// Check if competence mapping title already exists as a competence in the original sequence
						// If so, simply use the existing competence to map to.
						if (oldCompetences != null && oldCompetences.size() > 0
								&& getCompetenceFromSet(oldCompetences, competenceMapping.getCompetence().getTitle()) != null) {
							newMapping.setCompetence(getCompetenceFromSet(oldCompetences, competenceMapping.getCompetence()
									.getTitle()));
							competenceMappingDAO.insert(newMapping);
							newCompetenceMappings.add(newMapping);
						}
						// If competence was not already existing in the ld, add a new mappping
						else if (newCompetences != null && newCompetences.size() > 0
								&& getCompetenceFromSet(newCompetences, competenceMapping.getCompetence().getTitle()) != null) {
							newMapping.setCompetence(getCompetenceFromSet(newCompetences, competenceMapping.getCompetence()
									.getTitle()));
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
	 * Updates the competence information in the newLearningDesign based 
	 * on the originalLearningDesign
	 * 
	 * @param originalLearningDesign The LearningDesign to be copied 
	 * @param newLearningDesign The copy of the originalLearningDesign
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
								if (competenceMapping.getCompetence().getTitle().equals(newCompetence.getTitle())
										) {
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
				//activityDAO.update(newToolActivity);
			}
		}
	}

	/**
	 * Determines the type of activity and returns a deep-copy of the same
	 * 
	 * @param activity The object to be deep-copied
	 * @param newGroupings Temporary set of new groupings. Key is the grouping UUID. May not be null.
	 * @return Activity The new deep-copied Activity object
	 */
	private Activity getActivityCopy(final Activity activity, Map<Integer, Grouping> newGroupings, int uiidOffset) {
		if (Activity.GROUPING_ACTIVITY_TYPE == activity.getActivityTypeId().intValue()) {
			GroupingActivity newGroupingActivity = (GroupingActivity) activity.createCopy(uiidOffset);
			// now we need to manually add the grouping to the session, as we can't easily
			// set up a cascade
			Grouping grouping = newGroupingActivity.getCreateGrouping();
			grouping.setGroupingUIID(grouping.getGroupingUIID());
			if (grouping != null) {
				groupingDAO.insert(grouping);
				newGroupings.put(grouping.getGroupingUIID(), grouping);
			}
			return newGroupingActivity;
		}
		else {
			return activity.createCopy(uiidOffset);
		}
	}

	/**
	 * Returns a set of child activities for the given parent activity
	 * 
	 * @param parentActivity The parent activity 
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
	 * This method saves a new Learning Design to the database.
	 * It received a WDDX packet from flash, deserializes it
	 * and then finally persists it to the database.
	 * 
	 * A user may update an existing learning design if they have user/owner rights to the 
	 * folder or they are doing live edit. A user may create a new learning design only if they 
	 * have user/owner rights to the folder.
	 * 
	 * Note: it does not validate the design - that must be done
	 * separately.
	 * 
	 * @param wddxPacket The WDDX packet received from Flash
	 * @return Long learning design id 
	 * @throws Exception
	 */
	public Long storeLearningDesignDetails(String wddxPacket) throws Exception {

		Hashtable table = (Hashtable) WDDXProcessor.deserialize(wddxPacket);
		Integer workspaceFolderID = WDDXProcessor.convertToInteger(table, WDDXTAGS.WORKSPACE_FOLDER_ID);

		User user = null;
		Integer userID = getUserId();
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
		LearningDesign existingLearningDesign = learningDesignId != null ? learningDesignDAO
				.getLearningDesignById(learningDesignId) : null;
		if (!authorised && existingLearningDesign != null && Boolean.TRUE.equals(existingLearningDesign.getEditOverrideLock())) {
			authorised = userID.equals(existingLearningDesign.getEditOverrideUser().getUserId());
		}
		if (!authorised) {
			throw new UserException("User with user_id of " + userID
					+ " is not authorized to store a design in this workspace folder " + workspaceFolderID);
		}

		IObjectExtractor extractor = (IObjectExtractor) beanFactory.getBean(IObjectExtractor.OBJECT_EXTRACTOR_SPRING_BEANNAME);
		LearningDesign design = extractor.extractSaveLearningDesign(table, existingLearningDesign, workspaceFolder, user);

		if (extractor.getMode().intValue() == 1) {
			copyLearningDesignToolContent(design, design, design.getCopyTypeID());
		}

		return design.getLearningDesignId();
	}

	/** 
	 * Validate the learning design, updating the valid flag appropriately.
	 * 
	 * This needs to be run in a separate transaction to storeLearningDesignDetails to 
	 * ensure the database is fully updated before the validation occurs (due to some
	 * quirks we are finding using Hibernate)
	 * 
	 * @param learningDesignId
	 * @throws Exception
	 */
	public Vector<ValidationErrorDTO> validateLearningDesign(Long learningDesignId) {
		LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignId);
		Vector<ValidationErrorDTO> listOfValidationErrorDTOs = learningDesignService.validateLearningDesign(learningDesign);
		Boolean valid = listOfValidationErrorDTOs.size() > 0 ? Boolean.FALSE : Boolean.TRUE;
		learningDesign.setValidDesign(valid);
		learningDesignDAO.insertOrUpdate(learningDesign);
		return listOfValidationErrorDTOs;
	}

	public Vector<AuthoringActivityDTO> getToolActivities(Long learningDesignId, String languageCode) {
		LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignId);
		Vector<AuthoringActivityDTO> listOfAuthoringActivityDTOs = new Vector<AuthoringActivityDTO>();

		for (Iterator i = learningDesign.getActivities().iterator(); i.hasNext();) {
			Activity currentActivity = (Activity) i.next();
			if (currentActivity.isToolActivity()) {
				try {
					// Normally we pass in an array for the branch mappings as the second parameter to new  AuthoringActivityDTO()
					// but we don't need to in this case as it is only doing it for tool activities, and the extra parameter is only 
					// used for branching activities
					ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(currentActivity
							.getActivityId());
					AuthoringActivityDTO activityDTO = new AuthoringActivityDTO(toolActivity, null, languageCode);
					listOfAuthoringActivityDTOs.add(activityDTO);
				}
				catch (ToolException e) {
					String error = "" + e.getMessage();
					log.error(error, e);
					throw new LearningDesignException(error, e);
				}
			}
		}

		return listOfAuthoringActivityDTOs;
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningDesignDetails()
	 */
	public String getAllLearningDesignDetails() throws IOException {
		Iterator iterator = getAllLearningDesigns().iterator();
		ArrayList arrayList = createDesignDetailsPacket(iterator);
		FlashMessage flashMessage = new FlashMessage("getAllLearningDesignDetails", arrayList);
		return flashMessage.serializeMessage();
	}

	/**
	 * This is a utility method used by the method 
	 * <code>getAllLearningDesignDetails</code> to pack the 
	 * required information in a data transfer object.
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
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesignsForUser(java.lang.Long)
	 */
	public String getLearningDesignsForUser(Long userID) throws IOException {
		List list = learningDesignDAO.getLearningDesignByUserId(userID);
		ArrayList arrayList = createDesignDetailsPacket(list.iterator());
		FlashMessage flashMessage = new FlashMessage("getLearningDesignsForUser", arrayList);
		return flashMessage.serializeMessage();
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraryDetails()
	 */
	public String getAllLearningLibraryDetails(String languageCode) throws IOException {
		FlashMessage flashMessage = new FlashMessage("getAllLearningLibraryDetails", learningDesignService
				.getAllLearningLibraryDetails(languageCode));
		return flashMessage.serializeMessage();
	}

	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getToolContentID(java.lang.Long) */

	public String getToolContentID(Long toolID) throws IOException {
		Tool tool = toolDAO.getToolByID(toolID);
		if (tool == null) {
			log.error("The toolID " + toolID + " is not valid. A Tool with tool id " + toolID
					+ " does not exist on the database.");
			return FlashMessage.getNoSuchTool("getToolContentID", toolID).serializeMessage();
		}

		Long newContentID = contentIDGenerator.getNextToolContentIDFor(tool);
		FlashMessage flashMessage = new FlashMessage("getToolContentID", newContentID);

		return flashMessage.serializeMessage();
	}

	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyToolContent(java.lang.Long) */
	public String copyToolContent(Long toolContentID, String customCSV) throws IOException {
		Long newContentID = lamsCoreToolService.notifyToolToCopyContent(toolContentID, customCSV);
		FlashMessage flashMessage = new FlashMessage("copyToolContent", newContentID);
		return flashMessage.serializeMessage();
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyMultipleToolContent(java.lang.Integer, java.util.List)
	 */
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

	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAvailableLicenses() */
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

	/** Delete a learning design from the database. Does not remove any content stored in tools - 
	 * that is done by the LamsCoreToolService */
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
					ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
					lamsCoreToolService.notifyToolToDeleteContent(toolActivity);
				}
				catch (ToolException e) {
					log.error("Unable to delete tool content for activity" + activity + " as activity threw an exception", e);
				}
			}
		}

		// remove the learning design 
		learningDesignDAO.delete(design);
	}

	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#generateUniqueContentFolder() */
	public String generateUniqueContentFolder() throws FileUtilException, IOException {

		String newUniqueContentFolderID = FileUtil.generateUniqueContentFolderID();

		FlashMessage flashMessage = new FlashMessage("createUniqueContentFolder", newUniqueContentFolderID);

		return flashMessage.serializeMessage();
	}

	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getHelpURL() */
	public String getHelpURL() throws Exception {

		FlashMessage flashMessage = null;

		String helpURL = Configuration.get(ConfigurationKeys.HELP_URL);
		if (helpURL != null) {
			flashMessage = new FlashMessage("getHelpURL", helpURL);
		}
		else {
			throw new Exception();
		}

		return flashMessage.serializeMessage();
	}

	/**
	 * Get a unique name for a learning design, based on the names of the learning designs in the folder. 
	 * If the learning design has duplicated name in same folder, then the new name will have a timestamp.
	 * The new name format will be oldname_ddMMYYYY_idx. The idx will be auto incremental index number, start from 1.  
	 * Warning - this may be quite intensive as it gets all the learning designs in a folder.
	 * @param originalLearningDesign
	 * @param workspaceFolder
	 * @param copyType
	 * @return
	 */
	public String getUniqueNameForLearningDesign(String originalTitle, Integer workspaceFolderId) {

		String newName = originalTitle;
		if (workspaceFolderId != null) {
			List<String> ldTitleList = learningDesignDAO.getLearningDesignTitlesByWorkspaceFolder(workspaceFolderId);
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

}