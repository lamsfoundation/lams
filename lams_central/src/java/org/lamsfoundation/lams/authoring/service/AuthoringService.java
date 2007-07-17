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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.lamsfoundation.lams.authoring.IObjectExtractor;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupBranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
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
import org.lamsfoundation.lams.tool.dao.hibernate.SystemToolDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
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
	protected SystemToolDAO systemToolDAO;
	protected ILamsCoreToolService lamsCoreToolService;
	protected ILearningDesignService learningDesignService;
	protected MessageService messageService;
	protected ILessonService lessonService;
	protected IMonitoringService monitoringService;
	
	protected ToolContentIDGenerator contentIDGenerator;
	
	/** The bean factory is used to create ObjectExtractor objects */
	protected BeanFactory beanFactory;
	
	public AuthoringService(){
		
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


    /**
     * @param contentIDGenerator The contentIDGenerator to set.
     */
    public void setContentIDGenerator(ToolContentIDGenerator contentIDGenerator)
    {
        this.contentIDGenerator = contentIDGenerator;
    }
    
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesign(java.lang.Long)
	 */
	public LearningDesign getLearningDesign(Long learningDesignID){
		return learningDesignDAO.getLearningDesignById(learningDesignID);
	}
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#saveLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign)
	 */
	public void saveLearningDesign(LearningDesign learningDesign){
		learningDesignDAO.insertOrUpdate(learningDesign);
	}
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningDesigns()
	 */
	public List getAllLearningDesigns(){
		return learningDesignDAO.getAllLearningDesigns();		
	}
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraries()
	 */
	public List getAllLearningLibraries(){
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
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesignDetails(java.lang.Long)
	 */
	public String getLearningDesignDetails(Long learningDesignID)throws IOException{
		FlashMessage flashMessage= null;

		LearningDesignDTO learningDesignDTO = learningDesignService.getLearningDesignDTO(learningDesignID);
		
		if(learningDesignDTO==null)
			flashMessage = FlashMessage.getNoSuchLearningDesignExists("getLearningDesignDetails",learningDesignID);
		else{
			flashMessage = new FlashMessage("getLearningDesignDetails",learningDesignDTO);
		}
		return flashMessage.serializeMessage();
	}

	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#isLearningDesignAvailable(LearningDesign, java.lang.Integer)
	 */
	public boolean isLearningDesignAvailable(LearningDesign design, Integer userID) throws LearningDesignException, IOException {
		if(design == null)
			throw new LearningDesignException(FlashMessage.getNoSuchLearningDesignExists("getLearningDesignDetails",design.getLearningDesignId()).serializeMessage());
		
		if(design.getEditOverrideUser() != null && design.getEditOverrideLock() != null)
			return (design.getEditOverrideUser().getUserId().equals(userID)) ? true : !design.getEditOverrideLock();
		else
			return true;
	}
	
	private void setLessonLock(LearningDesign design, boolean lock) {
		Lesson lesson = null;
		
		// lock active lesson
		Set lessons = design.getLessons();
		Iterator it = lessons.iterator();
		
		while(it.hasNext()) {
			lesson = (Lesson) it.next();
			lesson.setLockedForEdit(lock);
		}
	}
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#setupEditOnFlyLock(LearningDesign, java.lang.Integer)
	 */
	public boolean setupEditOnFlyLock(Long learningDesignID, Integer userID) throws LearningDesignException, UserException, IOException {
		User user = (User)baseDAO.find(User.class,userID);
		
		LearningDesign design = learningDesignID!=null ? getLearningDesign(learningDesignID) : null;
		
		if(isLearningDesignAvailable(design, userID)) {
		
			if(design.getLessons().isEmpty())
				throw new LearningDesignException("There are no lessons attached to the design."); // TODO: add error msg
			else if(user==null)
				throw new UserException(messageService.getMessage("no.such.user.exist",new Object[]{userID}));
			
			setLessonLock(design, true);
				
			// lock Learning Design
			design.setEditOverrideLock(true);
			design.setEditOverrideUser(user);
				
			learningDesignDAO.update(design);
			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#setupEditOnFlyGate(java.lang.Long, java.lang.Integer)
	 */
	public String setupEditOnFlyGate(Long learningDesignID, Integer userID) throws UserException, IOException {
		User user = (User)baseDAO.find(User.class,userID);
		LearningDesign design = learningDesignID!=null ? getLearningDesign(learningDesignID) : null;
		
		if(user==null)
			throw new UserException(messageService.getMessage("no.such.user.exist",new Object[]{userID}));
		
		EditOnFlyProcessor processor = new EditOnFlyProcessor(design, activityDAO);			/* parse Learning Design to find last read-only Activity */
	   	processor.parseLearningDesign();
			
	   	ArrayList<Activity> activities = processor.getLastReadOnlyActivity();
		addSystemGateAfterActivity(activities, design);										/* add new System Gate after last read-only Activity */
		
		setLessonLock(design, false);
		
		learningDesignDAO.update(design);
		
		return new FlashMessage("setupEditOnFlyGate", true).serializeMessage();
	}
	
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#finishEditOnFly(java.lang.Long, java.lang.Integer)
	 */
	public String finishEditOnFly(Long learningDesignID, Integer userID) throws IOException {
		FlashMessage flashMessage= null;
		Lesson lesson = null;
		
		LearningDesign design = learningDesignID!=null ? learningDesignDAO.getLearningDesignById(learningDesignID) : null;
		
		User user = (User)baseDAO.find(User.class,userID);
		if(user==null)
			flashMessage = FlashMessage.getNoSuchUserExists("finishEditOnFly", userID);
		
		if(design != null) {											/* only the user who is editing the design may unlock it */
			if(design.getEditOverrideUser().equals(user)) {
				design.setEditOverrideLock(false);
				design.setEditOverrideUser(null);
				
				Set lessons = design.getLessons();						/* unlock lesson */
				
				Iterator it = lessons.iterator();
				while(it.hasNext()) {
					lesson = (Lesson) it.next();
					lesson.setLockedForEdit(false);
				}
				
				EditOnFlyProcessor processor = new EditOnFlyProcessor(design, activityDAO);		/* parse Learning Design to find last read-only Activity (hopefully the system gate in this case) */
		   		processor.parseLearningDesign();
				
		   		ArrayList<Activity> activities = processor.getLastReadOnlyActivity();
		   		
		   		GateActivity gate = null;														/* open and release waiting list on system gate */
				
		   		if(activities != null)
					if(!activities.isEmpty() && activities.get(0).isGateActivity())
						gate = (GateActivity) activities.get(0);
					
				if(gate != null)
					design = removeTempSystemGate(gate, design);								/* remove inputted system gate */
				
				lessonService.performMarkLessonUncompleted(lesson.getLessonId());				/* the lesson may now have additional activities on the end, so clear any completed flags */

				initialiseToolActivityForRuntime(design, lesson);
				learningDesignDAO.insertOrUpdate(design);
			
				flashMessage = new FlashMessage("finishEditOnFly", lesson.getLessonId());
				
			} else {
				flashMessage = FlashMessage.getNoSuchUserExists("finishEditOnFly", userID);
			}
		} else {
			
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
		Transition toTransition = gate.getTransitionTo();								/* get transitions */
		Transition fromTransition = gate.getTransitionFrom();

		if(toTransition != null && fromTransition != null) {							/* rearrange to-transition and/or delete redundant transition */	
			toTransition.setToActivity(fromTransition.getToActivity());
			toTransition.setToUIID(toTransition.getToActivity().getActivityUIID());
			
			design.getTransitions().remove(fromTransition);
			transitionDAO.update(toTransition);
			
		} else if(toTransition != null && fromTransition == null) {
			design.getTransitions().remove(toTransition);
		} else if(toTransition == null && fromTransition != null) {
			design.setFirstActivity(fromTransition.getToActivity());
			design.getTransitions().remove(fromTransition);
		}
		
		design.getActivities().remove(gate);											/* remove temp system gate */
		
		design.setDesignVersion(design.getDesignVersion() + 1);							/* increment design version field */
		
		lessonService.removeProgressReferencesToActivity(gate);							/* need to remove it from any learner progress entries */

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
		String title = "System Gate"; 														/* messageService.getMessage(MSG_KEY_SYNC_GATE); */

		SystemTool systemTool = systemToolDAO.getSystemToolByID(SystemTool.SYSTEM_GATE);		
		Activity activity = (activities.isEmpty()) ? null : (Activity) activities.get(0);
		
		try {																					/* create new System Gate Activity */
			gate = (GateActivity) Activity.getActivityInstance(syncType.intValue());
			gate.setActivityTypeId(activityType.intValue());
			gate.setActivityCategoryID(Activity.CATEGORY_SYSTEM);
			gate.setSystemTool(systemTool);
			gate.setActivityUIID(++maxId);
			gate.setTitle(title!=null?title:"Gate");
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
			
			if(activity != null) {
				fromTransition = activity.getTransitionFrom();				/* update transitions */
				
				if(fromTransition != null) {
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
	
					Integer x1 = (activity.getXcoord() != null) ? activity.getXcoord() : 0;				/* set x/y position for Gate */
			    	Integer x2 = (toActivity.getXcoord() != null) ? toActivity.getXcoord() : 0;
			    	
			    	gate.setXcoord(new Integer(((x1.intValue() + 123 + x2.intValue()) / 2) - 13));
			    	
			    	Integer y1 = (activity.getYcoord() != null) ? activity.getYcoord() : 0;
			    	Integer y2 = (toActivity.getYcoord() != null) ? toActivity.getYcoord() : 0;
			    	
			    	gate.setYcoord(new Integer((y1.intValue() + 50 + y2.intValue()) / 2));
					
				} else {
					//fromTransition = newTransition;
					
					newTransition.setTransitionUIID(++maxId);
					newTransition.setFromActivity(activity);
					newTransition.setFromUIID(activity.getActivityUIID());
					newTransition.setToActivity(gate);
					newTransition.setToUIID(gate.getActivityUIID());
					newTransition.setLearningDesign(design);
					
					activity.setTransitionFrom(fromTransition);
					gate.setTransitionTo(fromTransition);
					
					Integer	x1 = (activity.getTransitionTo() != null) ? activity.getTransitionTo().getFromActivity().getXcoord() : 0;		/* set x/y position for Gate */
					Integer x2 = (activity.getXcoord() != null) ? activity.getXcoord() : 0;
			    	
					if(x1 != null && x2 != null) gate.setXcoord(x2>=x1 ? new Integer(x2.intValue()+123+13+20) : new Integer(x2.intValue()-13-20));
					else gate.setXcoord(new Integer(x2.intValue()+123+13+20));
					
					gate.setYcoord(activity.getYcoord() + 25);
				}
				
			} else {
				fromTransition = newTransition;								/* no read-only activities insert gate at start of sequence */
				toActivity = design.getFirstActivity();
				
				newTransition.setTransitionUIID(++maxId);
				newTransition.setToActivity(toActivity);
				newTransition.setToUIID(toActivity.getActivityUIID());
				newTransition.setFromActivity(gate);
				newTransition.setFromUIID(gate.getActivityUIID());
				newTransition.setLearningDesign(design);
				
				gate.setTransitionFrom(fromTransition);
				toActivity.setTransitionTo(fromTransition);
				
				gate.setGateOpen(false);									/* keep gate door closed to stop learner's from going past this point */
				
				design.setFirstActivity(gate);								/* set gate as first activity in sequence */
				
				Integer x1 = (toActivity.getXcoord() != null) ? toActivity.getXcoord() : 0;														/* set x/y position for Gate */
				Integer	x2 = (toActivity.getTransitionFrom() != null) ? toActivity.getTransitionFrom().getToActivity().getXcoord() : null;
				
				if(x1 != null && x2 != null) gate.setXcoord(x2>=x1 ? new Integer(x1.intValue()-13-20) : new Integer(x1.intValue()+123+13+20));
				else gate.setXcoord(new Integer(x1.intValue()-13-20));
				
				gate.setYcoord(toActivity.getYcoord() + 25);
			}
			
			design.getTransitions().add(newTransition);
			design.setMaxID(maxId);
			
			design.setDesignVersion(design.getDesignVersion() + 1);					/* increment design version field */
			
			if(gate != null) activityDAO.update(gate);
			if(activity != null) activityDAO.update(activity);
			if(toActivity != null) activityDAO.update(toActivity);
			
			if(fromTransition != null && !fromTransition.equals(newTransition)) baseDAO.update(fromTransition);
			if(newTransition != null) baseDAO.insert(newTransition);
			if(design != null) learningDesignDAO.insertOrUpdate(design);
			
		} catch(NullPointerException npe) {
			log.error(npe.getMessage(), npe);
		}
		
	}
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getFirstUnattemptedActivity(org.lamsfoundation.lams.learningdesign.LearningDesign)
	 */
	public Activity getFirstUnattemptedActivity(LearningDesign design) throws LearningDesignException {
		Activity activity = design.getFirstActivity();
		
		while(activity.getReadOnly() && activity.getTransitionFrom() != null) {
				activity = activity.getTransitionFrom().getToActivity();
		}
		
		return activity;
	}
	
	private void initialiseToolActivityForRuntime(LearningDesign design, Lesson lesson) throws MonitoringServiceException {
		Date now = new Date();

		Set activities = design.getActivities();
        for (Iterator i = activities.iterator(); i.hasNext();) {
            Activity activity = (Activity) i.next();
            
            if ( activity.isInitialised() ) {
            	if ( ! activity.isActivityReadOnly() && activity.isToolActivity() ) {
            		// Activity is initialised so it was set up previously. So its tool content will be okay
            		// but the run offline flags and define later flags might have been changed, so they need to be updated
            		// Content ID shouldn't change, but we update it in case it does change while we update the status flags.
	            	ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
	            	Long newContentId = lamsCoreToolService.notifyToolOfStatusFlags(toolActivity);
                    toolActivity.setToolContentId(newContentId);
            	}
            	
            } else {
            	// this is a new activity - need to set up the content, do any scheduling, etc
            	// always have to copy the tool content, even though it may point to unique content - that way if the 
            	// teacher has double clicked on the tool icon (and hence set up a tool content id) but not saved any content
            	// the code in copyToolContent will ensure that there is content for this activity. So we end up with a few 
            	// unused records - we are trading database space for reliability. If we don't ensure that there is always
            	// a content record, then shortcomings in the createToolSession code may throw exceptions.
            	if ( activity.isToolActivity() ) {
	            	ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
	            	Long newContentId = lamsCoreToolService.notifyToolToCopyContent(toolActivity, true);
                    toolActivity.setToolContentId(newContentId);
 
            	} else if ( activity.isScheduleGate() ) {
		            //if it is schedule gate, we need to initialize the sheduler for it.
	            	ScheduleGateActivity gateActivity = (ScheduleGateActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
	            	monitoringService.runGateScheduler(gateActivity,now,lesson.getLessonName()); 
	            }
	            activity.setInitialised(Boolean.TRUE);
	            activityDAO.update(activity);
            }
        }
	}
	
	public LearningDesign copyLearningDesign(Long originalDesignID,Integer copyType,
									Integer userID, Integer workspaceFolderID, boolean setOriginalDesign) 
																	throws UserException, LearningDesignException, 
											 							      WorkspaceFolderException, IOException{
		
		LearningDesign originalDesign = learningDesignDAO.getLearningDesignById(originalDesignID);
		if(originalDesign==null)
			throw new LearningDesignException(messageService.getMessage("no.such.learningdesign.exist",new Object[]{originalDesignID}));
		
		User user = (User)baseDAO.find(User.class,userID);
		if(user==null)
			throw new UserException(messageService.getMessage("no.such.user.exist",new Object[]{userID}));
		
		WorkspaceFolder workspaceFolder = (WorkspaceFolder)baseDAO.find(WorkspaceFolder.class,workspaceFolderID);
		if(workspaceFolder==null)
			throw new WorkspaceFolderException(messageService.getMessage("no.such.workspace.exist",new Object[]{workspaceFolderID}));
		
		return copyLearningDesign(originalDesign,copyType,user,workspaceFolder, setOriginalDesign);
	}
	
    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign, java.lang.Integer, org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.usermanagement.WorkspaceFolder)
     */
    public LearningDesign copyLearningDesign(LearningDesign originalLearningDesign,Integer copyType,User user, WorkspaceFolder workspaceFolder, boolean setOriginalDesign)
    	throws LearningDesignException
    {
    	LearningDesign newLearningDesign  = LearningDesign.createLearningDesignCopy(originalLearningDesign,copyType, setOriginalDesign);
    	newLearningDesign.setUser(user);    	
    	newLearningDesign.setWorkspaceFolder(workspaceFolder);    	
    	learningDesignDAO.insert(newLearningDesign);
    	updateDesignActivities(originalLearningDesign,newLearningDesign); 
    	updateDesignTransitions(originalLearningDesign,newLearningDesign);
    	// set first activity assumes that the transitions are all set up correctly.
    	newLearningDesign.setFirstActivity(newLearningDesign.calculateFirstActivity());
    	newLearningDesign.setLearningDesignUIID(originalLearningDesign.getLearningDesignUIID());
    	
    	copyLearningDesignToolContent(newLearningDesign, originalLearningDesign, copyType);
    	
        return newLearningDesign;
    }
    
    /**
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyLearningDesignToolContent(org.lamsfoundation.lams.learningdesign.LearningDesign, org.lamsfoundation.lams.learningdesign.LearningDesign, java.lang.Integer)
     */
    public LearningDesign copyLearningDesignToolContent(LearningDesign design, LearningDesign originalLearningDesign, Integer copyType ) throws LearningDesignException {
    	
    	// copy the tool content
        // unfortuanately, we have to reaccess the activities to make sure we get the
        // subclass, not a hibernate proxy.
        for (Iterator i = design.getActivities().iterator(); i.hasNext();)
        {
            Activity currentActivity = (Activity) i.next();
            if (currentActivity.isToolActivity())
            {
                try {
                	ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(currentActivity.getActivityId());
                	// copy the content, but don't set the define later flags if it is preview
                    Long newContentId = lamsCoreToolService.notifyToolToCopyContent(toolActivity, copyType.intValue() != LearningDesign.COPY_TYPE_PREVIEW);
                    toolActivity.setToolContentId(newContentId);
                    
                    // clear read only field
                    toolActivity.setReadOnly(false);
                    
                } catch (DataMissingException e) {
                    String error = "Unable to initialise the lesson. Data is missing for activity "+currentActivity.getActivityUIID()
                            +" in learning design "+originalLearningDesign.getLearningDesignId()
                            +" default content may be missing for the tool. Error was "
                            +e.getMessage();
                    log.error(error,e);
                    throw new LearningDesignException(error,e);
                } catch (ToolException e) {
                    String error = "Unable to initialise the lesson. Tool encountered an error copying the data is missing for activity "
                            +currentActivity.getActivityUIID()
                            +" in learning design "+originalLearningDesign.getLearningDesignId()
                            +" default content may be missing for the tool. Error was "
                            +e.getMessage();
                    log.error(error,e);
                    throw new LearningDesignException(error,e);
                }

            }
        }
        
        return design;
    }
    
    /**
     * Updates the Activity information in the newLearningDesign based 
     * on the originalLearningDesign. This any grouping details.
     * 
     * @param originalLearningDesign The LearningDesign to be copied
     * @param newLearningDesign The copy of the originalLearningDesign
     */
    private void updateDesignActivities(LearningDesign originalLearningDesign, LearningDesign newLearningDesign){
    	HashMap<Integer, Activity> newActivities = new HashMap<Integer, Activity>(); // key is UIID   
    	HashMap<Integer, Grouping> newGroupings = new HashMap<Integer, Grouping>();    // key is UIID
    	
		// as we create the activities, we need to record any "first child" UIID's for the sequence activity to process later
		Map<Integer,SequenceActivity> firstChildUIIDToSequence = new HashMap<Integer,SequenceActivity>();

    	Set oldParentActivities = originalLearningDesign.getParentActivities();
    	if ( oldParentActivities != null ) {
	    	Iterator iterator = oldParentActivities.iterator();    	
	    	while(iterator.hasNext()){
	    		processActivity((Activity)iterator.next(), newLearningDesign, newActivities, newGroupings, null);
	    	}
    	}
    	
    	Collection<Activity> activities = newActivities.values();
    	
    	// Go back and find all the grouped activities and assign them the new groupings, based on the UIID. Can't
    	// be done as we go as the grouping activity may be processed after the grouped activity.
    	for ( Activity activity : activities) {
    		Integer groupingUIID = activity.getGroupingUIID();
    		if ( groupingUIID != null ) {
   				activity.setGrouping(newGroupings.get(groupingUIID));
    		}
    	}
    	
      	// fix up any old "first activity" in the sequence activities
    	for ( Activity activity : activities) {
    		if ( activity.isSequenceActivity() ) {
    			SequenceActivity newSeq = (SequenceActivity) activity;
    			Activity oldFirstActivity = newSeq.getFirstActivity();
    			if ( oldFirstActivity != null ) {
    				Activity newFirstActivity = newActivities.get(oldFirstActivity.getActivityUIID());
    				newSeq.setFirstActivity(newFirstActivity);
    			}
    		}
    	}

    	// Now go back and fix any branch mapping entries - they will still be pointing to old activities.
    	// Need to check if the sets are not null as these are new objects and Hibernate may not have
    	// backed them with collections yet.
    	for ( Grouping grouping: newGroupings.values() ) {
    		if ( grouping.getGroups() !=null && grouping.getGroups().size() > 0 ) {
    			Iterator iter = grouping.getGroups().iterator();
    			while ( iter.hasNext() ) {
    				Group group = (Group) iter.next();
    	    		if ( group.getBranchActivities() != null && group.getBranchActivities().size() > 0 ) {
    	    			Iterator iter2 = group.getBranchActivities().iterator();
    	    			while ( iter2.hasNext() ) {
    	    				GroupBranchActivityEntry entry = (GroupBranchActivityEntry) iter2.next();
    	    				SequenceActivity oldSequenceActivity = entry.getBranchSequenceActivity();
    	    				entry.setBranchSequenceActivity((SequenceActivity) newActivities.get(oldSequenceActivity.getActivityUIID()));
    	    				BranchingActivity oldBranchingActivity = entry.getBranchingActivity();
    	    				entry.setBranchingActivity((BranchingActivity) newActivities.get(oldBranchingActivity.getActivityUIID()));
    	    			}
    	    		}
    			}
    		}
    	}
    	
    	// The activities collection in the learning design may already exist (as we have already done a save on the design).
    	// If so, we can't just override the existing collection as the cascade causes an error.
    	// newLearningDesign.getActivities() will create a new TreeSet(new ActivityOrderComparator()) if there isn't an existing set
   		newLearningDesign.getActivities().clear();
   		newLearningDesign.getActivities().addAll(activities);
    }
    
    /** As part of updateDesignActivities(), process an activity and, via recursive calls, the activity's child activities. Need to keep track
     * of any new groupings created so we can go back and update the grouped activities with their new groupings at the end.
     * 
     * @param activity Activity to process. May not be null.
     * @param newLearningDesign The new learning design. May not be null.
     * @param newActivities Temporary set of new activities - as activities are processed they are added to the set. May not be null.
     * @param newGroupings Temporary set of new groupings. Key is the grouping UUID. May not be null.
     * @param parentActivity This activity's parent activity (if one exists). May be null.
     */
    private void processActivity(Activity activity, LearningDesign newLearningDesign, Map<Integer, Activity> newActivities, Map<Integer, Grouping> newGroupings, Activity parentActivity) {
		Activity newActivity = getActivityCopy(activity, newGroupings);
		newActivity.setLearningDesign(newLearningDesign);
		if ( parentActivity != null ) {
			newActivity.setParentActivity(parentActivity);
			newActivity.setParentUIID(parentActivity.getActivityUIID());
		}
		activityDAO.insert(newActivity);
		newActivities.put(newActivity.getActivityUIID(),newActivity);
		
		Set oldChildActivities = getChildActivities((Activity)activity);
		if ( oldChildActivities != null ) {
			Iterator childIterator = oldChildActivities.iterator();
			while(childIterator.hasNext()){
				processActivity((Activity)childIterator.next(), newLearningDesign, newActivities, newGroupings, newActivity);
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
    public void updateDesignTransitions(LearningDesign originalLearningDesign, LearningDesign newLearningDesign){
    	HashSet newTransitions = new HashSet();
    	Set oldTransitions = originalLearningDesign.getTransitions();
    	Iterator iterator = oldTransitions.iterator();
    	while(iterator.hasNext()){
    		Transition transition = (Transition)iterator.next();
    		Transition newTransition = Transition.createCopy(transition);    		
    		Activity toActivity = null;
        	Activity fromActivity=null;
    		if(newTransition.getToUIID()!=null) {
    			toActivity = activityDAO.getActivityByUIID(newTransition.getToUIID(),newLearningDesign);
    			toActivity.setTransitionTo(newTransition);
    		}
    		if(newTransition.getFromUIID()!=null) {
    			fromActivity = activityDAO.getActivityByUIID(newTransition.getFromUIID(),newLearningDesign);
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
    	if ( newLearningDesign.getTransitions() != null ) {
    		newLearningDesign.getTransitions().clear();
    		newLearningDesign.getTransitions().addAll(newTransitions);
    	} else {
        	newLearningDesign.setTransitions(newTransitions);
    	}

    }
    /**
     * Determines the type of activity and returns a deep-copy of the same
     * 
     * @param activity The object to be deep-copied
     * @param newGroupings Temporary set of new groupings. Key is the grouping UUID. May not be null.
     * @return Activity The new deep-copied Activity object
     */
    private Activity getActivityCopy(final Activity activity, Map<Integer, Grouping> newGroupings){
    	if ( Activity.GROUPING_ACTIVITY_TYPE == activity.getActivityTypeId().intValue() ) {
    		GroupingActivity newGroupingActivity = (GroupingActivity) activity.createCopy();
    		// now we need to manually add the grouping to the session, as we can't easily
    		// set up a cascade
    		Grouping grouping = newGroupingActivity.getCreateGrouping();
    		if ( grouping != null ) {
    			groupingDAO.insert(grouping);
    			newGroupings.put(grouping.getGroupingUIID(), grouping);
    		}
    		return newGroupingActivity;
    	}
    	else 
    		return activity.createCopy();    	
    } 
    /**
     * Returns a set of child activities for the given parent activitity
     * 
     * @param parentActivity The parent activity 
     * @return HashSet Set of the activities that belong to the parentActivity 
     */
    private HashSet getChildActivities(Activity parentActivity){
    	HashSet childActivities = new HashSet();
    	List list = activityDAO.getActivitiesByParentActivityId(parentActivity.getActivityId());
    	if(list!=null)
    		childActivities.addAll(list);
    	return childActivities;
    }		
	/**
	 * This method saves a new Learning Design to the database.
	 * It received a WDDX packet from flash, deserializes it
	 * and then finally persists it to the database.
	 * 
	 * Note: it does not validate the design - that must be done
	 * separately.
	 * 
	 * @param wddxPacket The WDDX packet received from Flash
	 * @return Long learning design id 
	 * @throws Exception
	 */
	public Long storeLearningDesignDetails(String wddxPacket) throws Exception {

		Hashtable table = (Hashtable)WDDXProcessor.deserialize(wddxPacket);
		IObjectExtractor extractor = (IObjectExtractor) beanFactory.getBean(IObjectExtractor.OBJECT_EXTRACTOR_SPRING_BEANNAME);
		LearningDesign design = extractor.extractSaveLearningDesign(table);	
		
		if(extractor.getMode().intValue() == 1)
			copyLearningDesignToolContent(design, design, design.getCopyTypeID());
		
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
	
	public Vector<AuthoringActivityDTO> getToolActivities(Long learningDesignId) {
		LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignId);
		Vector<AuthoringActivityDTO> listOfAuthoringActivityDTOs = new Vector<AuthoringActivityDTO>();
		
		for (Iterator i = learningDesign.getActivities().iterator(); i.hasNext();)
        {
            Activity currentActivity = (Activity) i.next();
            if (currentActivity.isToolActivity())
            {
            	try {
            		ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(currentActivity.getActivityId());
            		AuthoringActivityDTO activityDTO = new AuthoringActivityDTO(toolActivity);
            		listOfAuthoringActivityDTOs.add(activityDTO);
            	} catch (ToolException e) {
                        String error = ""
                                +e.getMessage();
                        log.error(error,e);
                        throw new LearningDesignException(error,e);
                }
            }
        }
		
		return listOfAuthoringActivityDTOs;
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningDesignDetails()
	 */
	public String getAllLearningDesignDetails()throws IOException{
		Iterator iterator= getAllLearningDesigns().iterator();
		ArrayList arrayList = createDesignDetailsPacket(iterator);
		FlashMessage flashMessage = new FlashMessage("getAllLearningDesignDetails",arrayList);		
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
	private ArrayList createDesignDetailsPacket(Iterator iterator){
	    ArrayList arrayList = new ArrayList();
		while(iterator.hasNext()){
			LearningDesign learningDesign = (LearningDesign)iterator.next();
			DesignDetailDTO designDetailDTO = learningDesign.getDesignDetailDTO();
			arrayList.add(designDetailDTO);
		}
		return arrayList;
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getLearningDesignsForUser(java.lang.Long)
	 */
	public String getLearningDesignsForUser(Long userID) throws IOException{
		List list = learningDesignDAO.getLearningDesignByUserId(userID);
		ArrayList arrayList = createDesignDetailsPacket(list.iterator());
		FlashMessage flashMessage = new FlashMessage("getLearningDesignsForUser",arrayList);
		return flashMessage.serializeMessage();
	}	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAllLearningLibraryDetails()
	 */
	public String getAllLearningLibraryDetails()throws IOException{
		FlashMessage flashMessage = new FlashMessage("getAllLearningLibraryDetails",learningDesignService.getAllLearningLibraryDetails());
		return flashMessage.serializeMessage();
	}
	
	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getToolContentID(java.lang.Long) */

	public String getToolContentID(Long toolID) throws IOException
	{
	   Tool tool = toolDAO.getToolByID(toolID);
	   if (tool == null)
	   {
	       log.error("The toolID "+ toolID + " is not valid. A Tool with tool id " + toolID + " does not exist on the database.");
	       return FlashMessage.getNoSuchTool("getToolContentID", toolID).serializeMessage();
	   }
	   
	   Long newContentID = contentIDGenerator.getNextToolContentIDFor(tool);
	   FlashMessage flashMessage = new FlashMessage("getToolContentID", newContentID);
	   
	   return flashMessage.serializeMessage();
	}
	
	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#copyToolContent(java.lang.Long) */
	public String copyToolContent(Long toolContentID) throws IOException
	{ 
		Long newContentID = lamsCoreToolService.notifyToolToCopyContent(toolContentID);
		FlashMessage flashMessage = new FlashMessage("copyToolContent", newContentID);
		return flashMessage.serializeMessage();
	}

	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getAvailableLicenses() */
	public Vector getAvailableLicenses() {
		List licenses = licenseDAO.findAll(License.class);
		Vector licenseDTOList = new Vector(licenses.size());
		Iterator iter = licenses.iterator(); 
		while ( iter.hasNext() ) {
			License element = (License) iter.next();
			licenseDTOList.add(element.getLicenseDTO(Configuration.get(ConfigurationKeys.SERVER_URL)));
		}
		return licenseDTOList;
	}

	/** Delete a learning design from the database. Does not remove any content stored in tools - 
	 * that is done by the LamsCoreToolService */
	public void deleteLearningDesign(LearningDesign design) {
		if ( design == null ) {
			log.error("deleteLearningDesign: unable to delete learning design as design is null.");
			return;
		}
		
		// remove all the tool content for the learning design
		Set acts = design.getActivities();
		Iterator iter = acts.iterator();
		while (iter.hasNext()) {
			Activity activity = (Activity) iter.next();
            if (activity.isToolActivity())
            {
                try {
                	ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
					lamsCoreToolService.notifyToolToDeleteContent(toolActivity);
				} catch (ToolException e) {
					log.error("Unable to delete tool content for activity"+activity
							+" as activity threw an exception",e);
				}
			}
		}
			
		// remove the learning design 
		learningDesignDAO.delete(design);
	}
	
	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#generateUniqueContentFolder() */
	public String generateUniqueContentFolder() throws FileUtilException, IOException {
		
		String newUniqueContentFolderID = null;
		Properties props = new Properties();
		
		IdentifierGenerator uuidGen = new UUIDHexGenerator();
		( (Configurable) uuidGen).configure(Hibernate.STRING, props, null);
		
		// lowercase to resolve OS issues
		newUniqueContentFolderID = ((String) uuidGen.generate(null, null)).toLowerCase();
		
		FlashMessage flashMessage = new FlashMessage("createUniqueContentFolder", newUniqueContentFolderID);
		
		return flashMessage.serializeMessage();
	}
	
	/** @see org.lamsfoundation.lams.authoring.service.IAuthoringService#getHelpURL() */
	public String getHelpURL() throws Exception {
		
		FlashMessage flashMessage =null;
		
		String helpURL = Configuration.get(ConfigurationKeys.HELP_URL);
		if(helpURL != null)
			flashMessage = new FlashMessage("getHelpURL", helpURL);
		else 
			throw new Exception();
		
		return flashMessage.serializeMessage();
	}
	


}