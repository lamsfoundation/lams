/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.monitoring.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.ProgressActivityDTO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>This is the major service facade for all monitoring functionalities. It is 
 * configured as a Spring factory bean so as to utilize the Spring's IOC and
 * declarative transaction management.</p> 
 * <p>It needs to implement <code>ApplicationContextAware<code> interface 
 * because we need to load up tool's service dynamically according to the
 * selected learning design.</p>
 * 
 * @author Jacky Fang 
 * @author Manpreet Minhas
 * @since 2/02/2005
 * @version 1.1
 */
public class MonitoringService implements IMonitoringService,ApplicationContextAware
{

    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(MonitoringService.class);
    
    private ILessonDAO lessonDAO;    
    private ILessonClassDAO lessonClassDAO;        
    private IOrganisationDAO organisationDAO;
    private ITransitionDAO transitionDAO;
    private IActivityDAO activityDAO;
    private IUserDAO userDAO;        
    private IWorkspaceFolderDAO workspaceFolderDAO;
    private ILearningDesignDAO learningDesignDAO;
    private FlashMessage flashMessage;
    private IAuthoringService authoringService;
    private ILamsCoreToolService lamsCoreToolService;
    private IUserManagementService userManagementService;
    private Scheduler scheduler;
    private ApplicationContext applicationContext;
    
    private ILearnerService learnerService;
    //---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    //---------------------------------------------------------------------
	/**
	 * @param userManagementService The userManagementService to set.
	 */
	public void setUserManagementService(
			IUserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}
	/**
	 * @param learningDesignDAO The learningDesignDAO to set.
	 */
	public void setLearningDesignDAO(ILearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}
	/**
	 * @param workspaceFolderDAO The workspaceFolderDAO to set.
	 */
	public void setWorkspaceFolderDAO(IWorkspaceFolderDAO workspaceFolderDAO) {
		this.workspaceFolderDAO = workspaceFolderDAO;
	}

	/**
	 * @param transitionDAO The transitionDAO to set.
	 */
	public void setTransitionDAO(ITransitionDAO transitionDAO) {
		this.transitionDAO = transitionDAO;
	}

	/**
	 * @param userDAO The userDAO to set.
	 */
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}
    /**
     * @param authoringService The authoringService to set.
     */
    public void setAuthoringService(IAuthoringService authoringService)
    {
        this.authoringService = authoringService;
    }

    /**
     * @param lessonClassDAO The lessonClassDAO to set.
     */
    public void setLessonClassDAO(ILessonClassDAO lessonClassDAO)
    {
        this.lessonClassDAO = lessonClassDAO;
    }

    /**
     * @param lessonDAO The lessonDAO to set.
     */
    public void setLessonDAO(ILessonDAO lessonDAO)
    {
        this.lessonDAO = lessonDAO;
    }

    /**
     * @param lamsToolService The lamsToolService to set.
     */
    public void setLamsCoreToolService(ILamsCoreToolService lamsToolService)
    {
        this.lamsCoreToolService = lamsToolService;
    }

    /**
     * @param activityDAO The activityDAO to set.
     */
    public void setActivityDAO(IActivityDAO activityDAO)
    {
        this.activityDAO = activityDAO;
    }
    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext=applicationContext;
    }
	public void setOrganisationDAO(IOrganisationDAO organisationDAO) {
		this.organisationDAO = organisationDAO;
	}
	
    /**
     * @param scheduler The scheduler to set.
     */
    public void setScheduler(Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }
    
    /**
     * @param learnerService The learnerService to set
     */
    public void setLearnerService(ILearnerService learnerService)
    {
        this.learnerService = learnerService;
    }
    
    //---------------------------------------------------------------------
    // Service Methods
    //---------------------------------------------------------------------
    /**
     * <p>Create new lesson according to the learning design specified by the 
     * user. This involves following major steps:</P>
     * 
     * <li>1. Make a runtime copy of static learning design defined in authoring</li>
     * <li>2. Go through all the tool activities defined in the learning design,
     * 		  create a runtime copy of all tool's content.</li>
     * 
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#createLesson(long, org.lamsfoundation.lams.usermanagement.User, java.util.List, java.util.List)
     */
    public Lesson initializeLesson(String lessonName,
                               String lessonDescription,
                               long learningDesignId,
                               User user)
    {
        LearningDesign originalLearningDesign = authoringService.getLearningDesign(new Long(learningDesignId));
        //copy the current learning design
        LearningDesign copiedLearningDesign = authoringService.copyLearningDesign(originalLearningDesign,
                                                                                  new Integer(LearningDesign.COPY_TYPE_LESSON),
                                                                                  user,
                                                                                  originalLearningDesign.getWorkspaceFolder());
        // copy the tool content
        // unfortuanately, we have to reaccess the activities to make sure we get the
        // subclass, not a hibernate proxy.
        for (Iterator i = copiedLearningDesign.getActivities().iterator(); i.hasNext();)
        {
            Activity currentActivity = (Activity) i.next();
            if (currentActivity.isToolActivity())
            {
                try {
                	ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(currentActivity.getActivityId());
                    Long newContentId = lamsCoreToolService.notifyToolToCopyContent(toolActivity);
                    toolActivity.setToolContentId(newContentId);
                } catch (DataMissingException e) {
                    String error = "Unable to initialise the lesson. Data is missing for activity "+currentActivity.getActivityUIID()
                            +" in learning design "+learningDesignId
                            +" default content may be missing for the tool. Error was "
                            +e.getMessage();
                    log.error(error,e);
                    throw new MonitoringServiceException(error,e);
                } catch (ToolException e) {
                    String error = "Unable to initialise the lesson. Tool encountered an error copying the data is missing for activity "
                            +currentActivity.getActivityUIID()
                            +" in learning design "+learningDesignId
                            +" default content may be missing for the tool. Error was "
                            +e.getMessage();
                    log.error(error,e);
                    throw new MonitoringServiceException(error,e);
                }

            }
        }
        authoringService.updateLearningDesign(copiedLearningDesign);
        
        return createNewLesson(lessonName,lessonDescription,user,copiedLearningDesign);

    }

    /**
     * <p>Pre-condition: This method must be called under the condition of the
     * 					 existance of new lesson (without lesson class).</p>
     * <p>A lesson class record should be inserted and organization should be
     * 	  setup after execution of this service.</p> 
     * 
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#createLessonClassForLesson(long, org.lamsfoundation.lams.usermanagement.Organisation, java.util.List, java.util.List)
     */
    public Lesson createLessonClassForLesson(long lessonId,
                                             Organisation organisation,
                                             List organizationUsers,
                                             List staffs)
    {
        Lesson newLesson = lessonDAO.getLesson(new Long(lessonId));
        
        LessonClass newLessonClass = this.createLessonClass(organisation,
                                                            organizationUsers,
                                                            staffs,
                                                            newLesson);
        newLessonClass.setLesson(newLesson);
        newLesson.setLessonClass(newLessonClass);
        newLesson.setOrganisation(organisation);
        
        lessonDAO.updateLesson(newLesson);
        
        return newLesson;
    }

    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#startlesson(long)
     */
    public void startlesson(long lessonId) 
    {
        if(log.isDebugEnabled())
            log.debug("=============Starting Lesson "+lessonId+"==============");
        //we get the lesson just created
        Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
        Date lessonStartTime = new Date();
        //initialize tool sessions if necessary
        Set activities = requestedLesson.getLearningDesign().getActivities();
        for (Iterator i = activities.iterator(); i.hasNext();)
        {
            Activity activity = (Activity) i.next();
            System.out.println(activity);
            //TODO this is for testing purpose as survey is the only tool available
            //so far.
            if ( activity.getActivityTypeId().intValue() == Activity.TOOL_ACTIVITY_TYPE ) {
            	ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId()); 
				if (shouldInitToolSessionFor(toolActivity)&&this.isSurvey(toolActivity))
					initToolSessionFor((ToolActivity) activity,
                                   requestedLesson.getAllLearners(),
                                   requestedLesson);
            }
            //if it is schedule gate, we need to initialize the sheduler for it.
            if(activity.getActivityTypeId().intValue() == Activity.SCHEDULE_GATE_ACTIVITY_TYPE) {
            	ScheduleGateActivity gateActivity = (ScheduleGateActivity) activityDAO.getActivityByActivityId(activity.getActivityId());
                runGateScheduler(gateActivity,lessonStartTime); 
            }
        //update lesson status
        }
        requestedLesson.setLessonStateId(Lesson.STARTED_STATE);
        requestedLesson.setStartDateTime(lessonStartTime);
        lessonDAO.updateLesson(requestedLesson);
        
        if(log.isDebugEnabled())
            log.debug("=============Lesson "+lessonId+" started===============");
    }

    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#openGate(org.lamsfoundation.lams.learningdesign.GateActivity)
     */
    public GateActivity openGate(Long gateId)
    {
        GateActivity gate = (GateActivity)activityDAO.getActivityByActivityId(gateId);
        if ( gate != null ) {
        	gate.setGateOpen(new Boolean(true));
        	activityDAO.update(gate);
        }
        return gate;
    }

    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#closeGate(org.lamsfoundation.lams.learningdesign.GateActivity)
     */
    public GateActivity closeGate(Long gateId)
    {
        GateActivity gate = (GateActivity)activityDAO.getActivityByActivityId(gateId);
        gate.setGateOpen(new Boolean(false));
        activityDAO.update(gate);
        return gate;
    }
    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#forceCompleteLessonByUser(long)
     */
    public void forceCompleteLessonByUser(long learnerProgressId)
    {
        // TODO Auto-generated method stub

    }
    
    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getAllLessons()
     */
    public List getAllLessons() throws IOException{
    	return lessonDAO.getAllLessons(); 
    }
    
    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getAllLessonsWDDX()
     */
    public String getAllLessonsWDDX() throws IOException{
    	return requestLessonList(getAllLessons());    	
    }
    
    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getAllLessons(java.lang.Integer)
     */
    public List getAllLessons(Integer userID)throws IOException{
    	return lessonDAO.getLessonsForUser(userID);
    }

    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getAllLessonsWDDX(java.lang.Integer)
     */
    public String getAllLessonsWDDX(Integer userID)throws IOException{
    	return requestLessonList(getAllLessons(userID)); 
    }
    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getLessonDetails(java.lang.Long)
     */
    public String getLessonDetails(Long lessonID)throws IOException{
    	Lesson lesson = lessonDAO.getLesson(lessonID);
    	if(lesson!=null){
    		flashMessage = new FlashMessage("getLessonDetails",lesson.getLessonDetails());
    	}else
    		flashMessage = new FlashMessage("getLessonDetails",
    										"No such Lesson with a lessonID of :" + lessonID + " exists.",
											FlashMessage.ERROR);
    	return flashMessage.serializeMessage();    	
    }
    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getLessonLearners(java.lang.Long)
     */
    public String getLessonLearners(Long lessonID)throws IOException{
    	Vector lessonLearners = new Vector();
    	Lesson lesson = lessonDAO.getLesson(lessonID);
    	if(lesson!=null){
    		Iterator iterator = lesson.getLessonClass().getLearners().iterator();
    		while(iterator.hasNext()){
    			User user = (User)iterator.next();
    			lessonLearners.add(user.getUserDTO());
    		}    		
    		flashMessage = new FlashMessage("getLessonLearners",lessonLearners);
    	}else
    		flashMessage = new FlashMessage("getLessonLearners",
    										 "No such lesson with a lesson_id of :"+ lessonID + " exists",
											 FlashMessage.ERROR);
    	return flashMessage.serializeMessage();
    }    
    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getLearningDesignDetails(java.lang.Long)
     */
    public String getLearningDesignDetails(Long lessonID)throws IOException{
    	Lesson lesson = lessonDAO.getLesson(lessonID);    	
    	return authoringService.getLearningDesignDetails(lesson.getLearningDesign().getLearningDesignId());
    }
    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getAllLearnersProgress(java.lang.Long)
     */
    public String getAllLearnersProgress(Long lessonID)throws IOException {
    	Vector progressData = new Vector();
    	Lesson lesson = lessonDAO.getLesson(lessonID);
    	if(lesson!=null){
    		Iterator iterator = lesson.getLearnerProgresses().iterator();
    		while(iterator.hasNext()){
    			LearnerProgress learnerProgress = (LearnerProgress)iterator.next();
    			progressData.add(learnerProgress.getLearnerProgressData());
    		}
    		flashMessage = new FlashMessage("getAllLearnersProgress",progressData);
    	}else{
    			flashMessage = new FlashMessage("getAllLearnersProgress",
    											"No such lesson with a lesson_id of :"+ lessonID + " exists",
												FlashMessage.ERROR);
    	}
    	return flashMessage.serializeMessage();
    } 
    
    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getActivityById(long)
     */
    public Activity getActivityById(long activityId)
    {
        return activityDAO.getActivityByActivityId(new Long(activityId));
    }
    
    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getAllContributeActivities(java.lang.Long)
     */
    public String getAllContributeActivities(Long lessonID)throws IOException{
    	Lesson lesson = lessonDAO.getLesson(lessonID);
    	if(lesson!=null){
    		Vector sortedSet = getOrderedActivityTree(lesson.getLearningDesign());
    		flashMessage = new FlashMessage("getAllContributeActivities",sortedSet);
    	}else{
    		flashMessage = new FlashMessage("getAllContributeActivities",
    										"No such lesson with a lesson_id of " + lessonID + "exists",
											FlashMessage.ERROR);
    	}
    	return flashMessage.serializeMessage();    	
    }
    /**
     * (non-Javadoc)
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getLearnerActivityURL(java.lang.Long, java.lang.Integer)
     */
    public String getLearnerActivityURL(Long activityID,Integer userID)throws IOException, LamsToolServiceException{    	
    	Activity activity = activityDAO.getActivityByActivityId(activityID);
    	User user = userDAO.getUserById(userID);
    	if(activity==null || user==null){
    		flashMessage = new FlashMessage("getLearnerActivityURL",
    										"Invalid activityID/User :" + activityID + " : " + userID,
											FlashMessage.ERROR);
    	}else{
    		if(activity.isToolActivity()){
        		ToolActivity toolActivity = (ToolActivity)activity;        		
        		String toolURL = lamsCoreToolService.getLearnerToolURLByMode(toolActivity,user,ToolAccessMode.TEACHER);        		
        		flashMessage = new FlashMessage("getLearnerActivityURL",new ProgressActivityDTO(activityID,toolURL));
        	}else{
        		flashMessage = new FlashMessage("getLearnerActivityURL",
        										"Invalid Activity type: " + activity.getActivityId()+ "\n Only ToolActivity allowed.",
												FlashMessage.ERROR);
        	}    		
    	}   	
    	
    	return flashMessage.serializeMessage();
    }	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getActivityContributionURL(java.lang.Long)
	 */
	public String getActivityContributionURL(Long activityID)throws IOException{
		Activity activity = activityDAO.getActivityByActivityId(activityID);
		if(activity!=null){
			if(activity.isToolActivity()){
				ToolActivity toolActivity = (ToolActivity)activity;
				String contributionURL = toolActivity.getTool().getContributeUrl();
				flashMessage = new FlashMessage("getActivityContributionURL",contributionURL);
			}
		}else
			flashMessage = FlashMessage.getNoSuchActivityExists("getActivityContributionURL",activityID);
		
		return flashMessage.serializeMessage();
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#moveLesson(java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	public String moveLesson(Long lessonID, Integer targetWorkspaceFolderID,Integer userID)throws IOException{
		Lesson lesson = lessonDAO.getLesson(lessonID);
		if(lesson!=null){
			if(lesson.getUser().getUserId().equals(userID)){
				WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(targetWorkspaceFolderID);
				if(workspaceFolder!=null){
					LearningDesign learningDesign = lesson.getLearningDesign();
					learningDesign.setWorkspaceFolder(workspaceFolder);
					learningDesignDAO.update(learningDesign);
					flashMessage = new FlashMessage("moveLesson",targetWorkspaceFolderID);
				}
				else
					flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists("moveLesson",targetWorkspaceFolderID);
			}else
				flashMessage = FlashMessage.getUserNotAuthorized("moveLesson",userID);
		}else{
			flashMessage = new FlashMessage("moveLesson",
											"No such lesson with a lesson_id of :" + lessonID +" exists.",
											FlashMessage.ERROR);
											
		}
		return flashMessage.serializeMessage();
		
	}
	 /**
	  * (non-Javadoc)
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#renameLesson(java.lang.Long, java.lang.String, java.lang.Integer)
	 */
	public String renameLesson(Long lessonID, String newName, Integer userID)throws IOException{
	 	Lesson lesson = lessonDAO.getLesson(lessonID);
	 	if(lesson!=null){
	 		if(lesson.getUser().getUserId().equals(userID)){
	 			lesson.setLessonName(newName);
	 			lessonDAO.updateLesson(lesson);
	 			flashMessage = new FlashMessage("renameLesson",newName);
	 		}else
	 			flashMessage = FlashMessage.getUserNotAuthorized("renameLesson",userID);
	 	}else
	 		flashMessage = new FlashMessage("renameLesson",
	 										"No such lesson with a lesson_id of :" + lessonID +" exists.",
											FlashMessage.ERROR);
	 	return flashMessage.serializeMessage();
	 }

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#checkGateStatus(java.lang.Long, java.lang.Long)
	 */
	public String checkGateStatus(Long activityID, Long lessonID) throws IOException
	{
	    GateActivity gate = (GateActivity)activityDAO.getActivityByActivityId(activityID);
	    Lesson lesson = lessonDAO.getLesson(lessonID); //used to calculate the total learners.
	    
	    if(gate==null || lesson==null){
	    		flashMessage = new FlashMessage("checkGateStatus",
	    										"Invalid activityID/lessonID :" + activityID + " : " + lessonID,
												FlashMessage.ERROR);
	    }
	    else
	    { 
	        Hashtable table = new Hashtable();
	        table = createGateStatusInfo(activityID, gate);
	        flashMessage = new FlashMessage("checkGateStatus", table);
	    }
	    return flashMessage.serializeMessage();
	}
	
	/**
	 * (non-javadoc)
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#releaseGate(java.lang.Long)
	 */
	public String releaseGate(Long activityID)throws IOException
	{
	    GateActivity gate = (GateActivity)activityDAO.getActivityByActivityId(activityID);
	    if (gate ==null)
	    {
	        flashMessage = new FlashMessage("releaseGate", 
	                						"Invalid activityID :" + activityID, 
	                						FlashMessage.ERROR);
	    }
	    else
	    {
	        //release gate
	        gate = openGate(activityID);
	     
	        flashMessage = new FlashMessage("releaseGate", gate.getGateOpen());
 
	    }
	    return flashMessage.serializeMessage();
	    
	}
	
	
	
    //---------------------------------------------------------------------
    // Helper Methods - create lesson
    //---------------------------------------------------------------------
    /**
     * Create a new lesson and setup all the staffs and learners who will be
     * participating this less.
     * @param organisation the organization this lesson belongs to.	
     * @param organizationUsers a list of learner will be in this new lessons.
     * @param staffs a list of staffs who will be in charge of this lesson.
     * @param newLesson 
     */
    private LessonClass createLessonClass(Organisation organisation,
                                          List organizationUsers,
                                          List staffs,
                                          Lesson newLesson)
    {
        //create a new lesson class object
        LessonClass newLessonClass = createNewLessonClass(newLesson.getLearningDesign());
        lessonClassDAO.saveLessonClass(newLessonClass);

        //setup staff group
        newLessonClass.setStaffGroup(Group.createStaffGroup(newLessonClass,
                                                            new HashSet(staffs)));
        //setup learner group
        newLessonClass.getGroups()
                      .add(Group.createLearnerGroup(newLessonClass,
                                                    new HashSet(organizationUsers)));
        
        lessonClassDAO.updateLessonClass(newLessonClass);
        
        return newLessonClass;
    }
    
    /**
     * Setup a new lesson object without class and insert it into the database.
     * 
     * @param lessonName the name of the lesson
     * @param lessonDescription the description of the lesson.
     * @param user user the user who want to create this lesson.
     * @param copiedLearningDesign the copied learning design
     * @return the lesson object without class.
     * 
     */
    private Lesson createNewLesson(String lessonName, String lessonDescription, User user, LearningDesign copiedLearningDesign)
    {
        Lesson newLesson = Lesson.createNewLessonWithoutClass(lessonName,
                                                              lessonDescription,
                                                              user,
                                                              copiedLearningDesign);
        lessonDAO.saveLesson(newLesson);
        return newLesson;
    }
    
    /**
     * Setup the empty lesson class according to the run-time learning design
     * copy.
     * @param copiedLearningDesign the run-time learning design instance.
     * @return the new empty lesson class.
     */
    private LessonClass createNewLessonClass(LearningDesign copiedLearningDesign)
    {
        //make a copy of lazily initialized activities
        Set activities = new HashSet(copiedLearningDesign.getActivities());
        LessonClass newLessonClass = new LessonClass(null, //grouping id
                                                     new HashSet(),//groups
                                                     activities, null, //staff group 
                                                     null);//lesson
        return newLessonClass;
    }

    /**
     * This is more for testing purpose. 
     * @param toolActivity the tool activity defined in the design.
     * @return
     */
    private boolean isSurvey(ToolActivity toolActivity)
    {
        return toolActivity.getTool().getServiceName().equals("surveyService");
    }
    
    //---------------------------------------------------------------------
    // Helper Methods - start lesson
    //---------------------------------------------------------------------
    /**
     * Create lams tool session for requested learner in the lesson. After the
     * creation of lams tool session, it delegates to the tool instances to 
     * create tool's own tool session.
     * 
     * @param activity the tool activity that all tool session reference to.
     * @param learners the set of learners that needs to have tool session
     * 				   initialized.
     * @param lesson the target lesson that these tool sessions belongs to.
     * @throws LamsToolServiceException the exception when lams is talking to tool.
     */
    private void initToolSessionFor(ToolActivity activity, Set learners,Lesson lesson) 
    {
        activity.setToolSessions(new HashSet());
        try
        {
            for (Iterator i = learners.iterator(); i.hasNext();)
            {
                User learner = (User) i.next();

                ToolSession toolSession;

                toolSession = lamsCoreToolService.createToolSession(learner,activity,lesson);

                //ask tool to create their own tool sessions using the given id.
                lamsCoreToolService.notifyToolsToCreateSession(toolSession.getToolSessionId(), activity);
                //update the hibernate persistent object
                activity.getToolSessions().add(toolSession);
            }
        }
        catch (LamsToolServiceException e)
        {
	        String error = "Unable to initialise tool session. Fail to call tool services. Error was "
	             +e.getMessage();
	         log.error(error,e);
	         throw new MonitoringServiceException(error,e);
		 }
		 catch (ToolException e)
		 {
		     String error = "Unable to initialise tool session. Tool encountered an error. Error was "
		         +e.getMessage();
		     log.error(error,e);
		     throw new MonitoringServiceException(error,e);
		 }   
     }
    

    /**
     * Returns whether we should initialize tool session or not. Tool sessions
     * can be initialized if the activity is tool activity and it doesn't 
     * involve any grouping.
     * 
     * @param tool activity the activity that needs to be inspected.
     * @return the result.
     */
    private boolean shouldInitToolSessionFor(ToolActivity activity)
    {
        return activity.getApplyGrouping().booleanValue();
    }

    /**
     * This method returns the list of lessons in WDDX format
     * 
     * @param lessons The list of lessons to be converted into WDDX format
     * @return String The required information in WDDX format
     * @throws IOException
     */
    private String requestLessonList(List lessons)throws IOException{
    	Vector lessonObjects = new Vector();
		Iterator lessonIterator = lessons.iterator();
		while(lessonIterator.hasNext()){
			Lesson lesson = (Lesson)lessonIterator.next();
			lessonObjects.add(lesson.getLessonData());
		}	
    	FlashMessage flashMessage = new FlashMessage("getAllLessons",lessonObjects);
    	return flashMessage.serializeMessage();    	
    }
	/**
     * This method assigns an orderID to all the activties in the LearningDesign
     * based on the transitions. Once this is done it just packages it in a container 
     * to be serialized and sent to flash
     * 
     * @param learningDesign The learningdesign whose activities have to be ordered
     * @return Vector The activities with orderID assigned.
     */
	private Vector getOrderedActivityTree(LearningDesign learningDesign){
		int order = 0;		
		HashMap activityTree = learningDesign.getActivityTree();		
		Vector activityVector = new Vector();		
		
		Activity nextActivity = learningDesign.getFirstActivity();
		while(nextActivity!=null){
			order = addActivityToVector(order, activityTree, activityVector, nextActivity);
			nextActivity = transitionDAO.getNextActivity(nextActivity.getActivityId());	
		}				
		return activityVector;
	}	

    /**
     * Used by getOrderedActivityTree(LearningDesign learningDesign)
     * 
	 * @param order
	 * @param activityTree
	 * @param activityVector
	 * @param nextActivity
	 * @return
	 */
	private int addActivityToVector(int order, HashMap activityTree, Vector activityVector, Activity nextActivity) {
		nextActivity.setOrderId(new Integer(order));
		Set childActivities = (Set) activityTree.get(nextActivity.getActivityId());			
		if(childActivities.size()!=0){
			Iterator iterator = childActivities.iterator();
			while(iterator.hasNext()){					
				Activity simpleActivity= (Activity)iterator.next();
				activityVector.add(simpleActivity.getMonitoringActivityDTO());
			}
		}else{
			// we are assuming that this is a simple activity.
			// the original code for this branch only added it if it had a valid contribution 
			// type, but the code for the if branch above didn't. Can add it in 
			// again later if it causes Flash problems.
			activityVector.add(nextActivity.getMonitoringActivityDTO());
		}
		return order + 1;
	}
	//---------------------------------------------------------------------
    // Helper Methods - scheduling
    //---------------------------------------------------------------------
    /**
     * <p>Runs the system scheduler to start the scheduling for opening gate and
     * closing gate. It invlovs a couple of steps to start the scheduler:</p>
     * <li>1. Initialize the resource needed by scheduling job by setting 
     * 		  them into the job data map.
     * </li>
     * <li>2. Create customized triggers for the scheduling.</li>
     * <li>3. start the scheduling job</li> 
     * 
     * @param scheduleGate the gate that needs to be scheduled.
     */
    private void runGateScheduler(ScheduleGateActivity scheduleGate,
                                  Date lessonStartTime)
    {
        if(log.isDebugEnabled())
            log.debug("Running scheduler for gate "+scheduleGate.getActivityId()+"...");
        JobDetail openScheduleGateJob = getOpenScheduleGateJob();
        JobDetail closeScheduleGateJob = getCloseScheduleGateJob();
        //setup the message for scheduling job
        openScheduleGateJob.setName("openGate");
        openScheduleGateJob.getJobDataMap().put("gateId",scheduleGate.getActivityId());
        closeScheduleGateJob.setName("closeGate");
        closeScheduleGateJob.getJobDataMap().put("gateId",scheduleGate.getActivityId());
        //create customized triggers
        Trigger openGateTrigger = new SimpleTrigger("openGateTrigger",
                                                    Scheduler.DEFAULT_GROUP, 
                                                    scheduleGate.getLessonGateOpenTime(lessonStartTime));
        Trigger closeGateTrigger = new SimpleTrigger("closeGateTrigger",
                                                    Scheduler.DEFAULT_GROUP,
                                                    scheduleGate.getLessonGateCloseTime(lessonStartTime));
        //start the scheduling job
        try
        {
            scheduler.scheduleJob(openScheduleGateJob, openGateTrigger);
            scheduler.scheduleJob(closeScheduleGateJob, closeGateTrigger);
        }
        catch (SchedulerException e)
        {
            throw new MonitoringServiceException("Error occurred at " +
            		"[runGateScheduler]- fail to start scheduling",e);
        }
        //update the gate because the start time might be setup 
        activityDAO.update(scheduleGate);
        
        if(log.isDebugEnabled())
            log.debug("Scheduler for Gate "+scheduleGate.getActivityId()+" started...");
    }

    /**
     * Returns the bean that defines the open schedule gate job.
     */
    private JobDetail getOpenScheduleGateJob()
    {
        return (JobDetail)applicationContext.getBean("openScheduleGateJob");
    }
    
    /**
     * Returns the bean that defines the close schdule gate job.
     */
    private JobDetail getCloseScheduleGateJob()
    {
        return (JobDetail)applicationContext.getBean("closeScheduleGateJob");
    }
    
    private Hashtable createGateStatusInfo(Long activityID, GateActivity gate)
    {
        Hashtable table = new Hashtable();
        table.put("activityID", activityID);
        table.put("activityTypeID", gate.getActivityTypeId());
	    table.put("gateOpen", gate.getGateOpen());
	    table.put("activityLevelID", gate.getGateActivityLevelId()); 
	    table.put("learnersWaiting", new Integer(gate.getWaitingLearners().size()));
	    
	    /* if the gate is a schedule gate, include the information about gate opening 
	     * and gate closing times */
	    if (gate.isScheduleGate())
	    {
	        ScheduleGateActivity scheduleGate = (ScheduleGateActivity)gate;
	        table.put("gateStartTime", scheduleGate.getGateStartDateTime());
	        table.put("gateEndTime", scheduleGate.getGateEndDateTime());
	    }
	    return table;
    }
    
   
  

}