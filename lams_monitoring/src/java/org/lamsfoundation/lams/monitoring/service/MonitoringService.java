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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.ProgressActivityDTO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
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
   	private static final long numMilliSecondsInADay = 24 * 60 * 60 * 1000;

    private ILessonDAO lessonDAO;    
    private ILessonClassDAO lessonClassDAO;        
    private ITransitionDAO transitionDAO;
    private IActivityDAO activityDAO;
    private IWorkspaceFolderDAO workspaceFolderDAO;
    private ILearningDesignDAO learningDesignDAO;
    private IOrganisationDAO organisationDAO;
    private IUserDAO userDAO;
    private IGroupingDAO groupingDAO;
    private IAuthoringService authoringService;
    private ILearnerService learnerService;
    private ILamsCoreToolService lamsCoreToolService;
    private IUserManagementService userManagementService;
    private Scheduler scheduler;
    private ApplicationContext applicationContext;
    private MessageService messageService;

	//---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    //---------------------------------------------------------------------
   /**
     * @param messageService the i18n Service bean.
     */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
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
	 * 
	 * @param learnerService
	 */
	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
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
     * @param userDAO
     */
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}
	/**
	 * @param groupingDAO
	 */
	public void setGroupingDAO(IGroupingDAO groupingDAO) {
		this.groupingDAO = groupingDAO;
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
	public void setOrganisationDAO(IOrganisationDAO organisationDAO) {
		this.organisationDAO = organisationDAO;
	}
      /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext=applicationContext;
    }
	
    /**
     * @param scheduler The scheduler to set.
     */
    public void setScheduler(Scheduler scheduler)
    {
        this.scheduler = scheduler;
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
     * <P>Tries to copy the design into the user's default runtime sequence folder. If
     * this is not available, then it is copied into the existing folder.</P>
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#initializeLesson(String, String, long, Integer)
     */
    public Lesson initializeLesson(String lessonName,
                               String lessonDescription,
                               long learningDesignId,
                               Integer userID) 
    {
    	
        LearningDesign originalLearningDesign = authoringService.getLearningDesign(new Long(learningDesignId));
        if ( originalLearningDesign == null) {
        	throw new MonitoringServiceException("Learning design for id="+learningDesignId+" is missing. Unable to initialize lesson.");
        }
        
        // The duplicated sequence should go in the run sequences folder, so we had better 
        // wourk out what the folder is!
    	User user = (userID != null ? userManagementService.getUserById(userID) : null);
        Workspace workspace = (user != null ? user.getWorkspace() : null);
        WorkspaceFolder destinationFolder = ( workspace!=null ? workspace.getDefaultRunSequencesFolder() : null);
        if ( destinationFolder == null ) {
        	log.error("initializeLesson: Copying learning design "+learningDesignId+" for userID "+userID
        			+". Unable to determine runtime sequence folder - copying into folder of the original design");
        	destinationFolder = originalLearningDesign.getWorkspaceFolder(); 
        }
        
        return initializeLesson(lessonName, lessonDescription, originalLearningDesign, user, LearningDesign.COPY_TYPE_LESSON, destinationFolder);
        
    }
    
    /**
     * Create new lesson according to the learning design specified by the 
     * user, but for a preview session rather than a normal learning session.
     * The design is not assigned to any workspace folder.
     */
    public Lesson initializeLessonForPreview(String lessonName,
                               String lessonDescription,
                               long learningDesignId,
                               Integer userID) 
    {
        LearningDesign originalLearningDesign = authoringService.getLearningDesign(new Long(learningDesignId));
        if ( originalLearningDesign == null) {
        	throw new MonitoringServiceException("Learning design for id="+learningDesignId+" is missing. Unable to initialize lesson.");
        }
    	User user = (userID != null ? userManagementService.getUserById(userID) : null);

        return initializeLesson(lessonName, lessonDescription, originalLearningDesign, user, LearningDesign.COPY_TYPE_PREVIEW, null);
    }

    public Lesson initializeLesson(String lessonName,
            String lessonDescription,
            LearningDesign originalLearningDesign,
            User user,
            int copyType,
            WorkspaceFolder folder) { 
    
        //copy the current learning design
        LearningDesign copiedLearningDesign = authoringService.copyLearningDesign(originalLearningDesign,
                                                                                  new Integer(copyType),
                                                                                  user,
                                                                                  folder, true);
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
                            +" in learning design "+originalLearningDesign.getLearningDesignId()
                            +" default content may be missing for the tool. Error was "
                            +e.getMessage();
                    log.error(error,e);
                    throw new MonitoringServiceException(error,e);
                } catch (ToolException e) {
                    String error = "Unable to initialise the lesson. Tool encountered an error copying the data is missing for activity "
                            +currentActivity.getActivityUIID()
                            +" in learning design "+originalLearningDesign.getLearningDesignId()
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
     *  @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#createLessonClassForLessonWDDX(Integer, String)
     */
    public String createLessonClassForLessonWDDX(Integer creatorUserId, String lessonPacket){
    	FlashMessage flashMessage = null;
    	try{
	    	Hashtable table = (Hashtable)WDDXProcessor.deserialize(lessonPacket);
	    	//todo: convert:data type:
	    	Integer orgId = 
	    		WDDXProcessor.convertToInteger(MonitoringConstants.KEY_ORGANISATION_ID, table.get(MonitoringConstants.KEY_ORGANISATION_ID));
	    	long lessonId = 
	    		WDDXProcessor.convertToLong(MonitoringConstants.KEY_LESSON_ID, table.get(MonitoringConstants.KEY_LESSON_ID)).longValue();
	    	//get leaner group info
	    	Hashtable learnerMap = (Hashtable) table.get(MonitoringConstants.KEY_LEARNER);
	    	List learners = (List) learnerMap.get(MonitoringConstants.KEY_USERS);
	    	String learnerGroupName = WDDXProcessor.convertToString(learnerMap, MonitoringConstants.KEY_GROUP_NAME);
	    	//get staff group info
	    	Hashtable staffMap = (Hashtable) table.get(MonitoringConstants.KEY_STAFF);
	    	List staffs = (List) staffMap.get(MonitoringConstants.KEY_USERS);
	    	String staffGroupName = WDDXProcessor.convertToString(staffMap, MonitoringConstants.KEY_GROUP_NAME);
	    	
	    	if(learners == null)
	    		learners = new LinkedList();
	    	if(staffs == null)
	    		staffs = new LinkedList();
	    	
	    	Organisation organisation = organisationDAO.getOrganisationById(orgId);
	    	User creator = userDAO.getUserById(creatorUserId);
	        // create the lesson class - add all the users in this organisation to the lesson class
	        // add user as staff
	    	List learnerList = new LinkedList();
	    	learnerList.add(creator);
	        Iterator iter = learners.iterator();
	        while (iter.hasNext()) {
	        	try {
	        		int id = ((Double) iter.next()).intValue();
	        		learnerList.add(userDAO.getUserById(new Integer(id)));
				} catch (Exception e) {
					log.error("Error parsing learner ID from " + lessonPacket);
					continue;
				}
			}
	        //get staff user info
	    	List staffList = new LinkedList();
	    	staffList.add(creator);
	        iter = staffs.iterator();
	        while (iter.hasNext()) {
	        	try {
	        		int id = ((Double) iter.next()).intValue();
	        		staffList.add(userDAO.getUserById(new Integer(id)));
				} catch (Exception e) {
					log.error("Error parsing staff ID from " + lessonPacket);
					continue;
				}
			}
	        
	        //Create Lesson!
	        createLessonClassForLesson(lessonId,
	        		organisation,
	        		learnerGroupName,
	        		learnerList,
	        		staffGroupName,
	                staffList);
	    	
	   		flashMessage = new FlashMessage("createLesson",Boolean.TRUE);
		} catch (Exception e) {
			flashMessage = new FlashMessage("createLesson",
					e.getMessage(),
					FlashMessage.ERROR);
		}
		
		String message = "Failed on creating flash message:" + flashMessage;
		try {
			message = flashMessage.serializeMessage();
		} catch (IOException e) {
			log.error(message);
		}
		
        return message;
    }
    /**
     * <p>Pre-condition: This method must be called under the condition of the
     * 					 existance of new lesson (without lesson class).</p>
     * <p>A lesson class record should be inserted and organization should be
     * 	  setup after execution of this service.</p> 
     * @param staffGroupName 
     * @param learnerGroupName 
     * 
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#createLessonClassForLesson(long, org.lamsfoundation.lams.usermanagement.Organisation, java.util.List, java.util.List)
     */
    public Lesson createLessonClassForLesson(long lessonId,
                                             Organisation organisation,
                                             String learnerGroupName, List organizationUsers,
                                             String staffGroupName, List staffs)
    {
        Lesson newLesson = lessonDAO.getLesson(new Long(lessonId));
        if ( newLesson == null) {
        	throw new MonitoringServiceException("Lesson for id="+lessonId+" is missing. Unable to create class for lesson.");
        }
       
        LessonClass newLessonClass = this.createLessonClass(organisation,
        													learnerGroupName,
                                                            organizationUsers,
                                                            staffGroupName,
                                                            staffs,
                                                            newLesson);
        newLessonClass.setLesson(newLesson);
        newLesson.setLessonClass(newLessonClass);
        newLesson.setOrganisation(organisation);
        
        lessonDAO.updateLesson(newLesson);
        
        return newLesson;
    }
    /**
     * Start lesson on schedule.
     * @param lessonId
     * @param startDate 
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#startLessonOnSchedule(long , Date)
     */
    public void startLessonOnSchedule(long lessonId, Date startDate){

        JobDetail startLessonJob = getStartScheduleLessonJob();
        //setup the message for scheduling job
        startLessonJob.setName("startLessonOnSchedule");
        startLessonJob.getJobDataMap().put(MonitoringConstants.KEY_LESSON_ID,new Long(lessonId));
        //create customized triggers
        Trigger startLessonTrigger = new SimpleTrigger("startLessonOnScheduleTrigger",
                                                    Scheduler.DEFAULT_GROUP, 
                                                    startDate);
        //start the scheduling job
        try
        {
        	setLessonState(lessonId,Lesson.NOT_STARTED_STATE);
            scheduler.scheduleJob(startLessonJob, startLessonTrigger);
        }
        catch (SchedulerException e)
        {
            throw new MonitoringServiceException("Error occurred at " +
            		"[startLessonOnSchedule]- fail to start scheduling",e);
        }
        
        if(log.isDebugEnabled())
            log.debug("Start lesson  ["+lessonId+"] on schedule is configured");
    }


    /**
     * Start lesson on schedule.
     * @param lessonId
     * @param endDate 
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#finishLessonOnSchedule(long , Date)
     */
	public void finishLessonOnSchedule(long lessonId, Date endDate) {
        JobDetail finishLessonJob = getFinishScheduleLessonJob();
        //setup the message for scheduling job
        finishLessonJob.setName("finishLessonOnSchedule");
        finishLessonJob.getJobDataMap().put(MonitoringConstants.KEY_LESSON_ID,new Long(lessonId));
        //create customized triggers
        Trigger finishLessonTrigger = new SimpleTrigger("finishLessonOnScheduleTrigger",
                                                    Scheduler.DEFAULT_GROUP, 
                                                    endDate);
        //start the scheduling job
        try
        {
            scheduler.scheduleJob(finishLessonJob, finishLessonTrigger);
        }
        catch (SchedulerException e)
        {
            throw new MonitoringServiceException("Error occurred at " +
            		"[finishLessonOnSchedule]- fail to start scheduling",e);
        }
        
        if(log.isDebugEnabled())
            log.debug("Finish lesson  ["+lessonId+"] on schedule is configured");
	}

	/**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#startlesson(long)
     */
    public void startLesson(long lessonId) 
    {
//    	System.out.println(messageService.getMessage("NO.SUCH.LESSON",new Object[]{new Long(lessonId)}));
//    	System.out.println(messageService.getMessage("INVALID.ACTIVITYID.USER", new Object[]{ "activityID","userID" }));
//    	System.out.println(messageService.getMessage("INVALID.ACTIVITYID.TYPE", new Object[]{  "activityID"}));
//    	System.out.println(messageService.getMessage("INVALID.ACTIVITYID.LESSONID",new Object[]{ "activityID","lessonID"}));
//    	System.out.println(messageService.getMessage("INVALID.ACTIVITYID",new Object[]{ "activityID"}));
        if(log.isDebugEnabled())
            log.debug("=============Starting Lesson "+lessonId+"==============");

        //we get the lesson just created
        Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
        if ( requestedLesson == null) {
        	throw new MonitoringServiceException("Lesson for id="+lessonId+" is missing. Unable to start lesson.");
        }

        Date lessonStartTime = new Date();
        //initialize tool sessions if necessary
        Set activities = requestedLesson.getLearningDesign().getActivities();
        for (Iterator i = activities.iterator(); i.hasNext();)
        {
            Activity activity = (Activity) i.next();
            // if it is a non-grouped Tool Activity, create the tool sessions now
            if ( activity.isToolActivity() ) {
            	ToolActivity toolActivity = (ToolActivity) activityDAO.getActivityByActivityId(activity.getActivityId()); 
				initToolSessionIfSuitable(toolActivity, requestedLesson);
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
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#finishLesson(long)
     */
	public void finishLesson(long lessonId) {
		setLessonState(lessonId,Lesson.FINISHED_STATE);
	}
    /**
     * Archive the specified the lesson. When archived, the data is retained
     * but the learners cannot access the details. 
     * @param lessonId the specified the lesson id.
     */
    public void archiveLesson(long lessonId) {
    	setLessonState(lessonId,Lesson.ARCHIVED_STATE);

    }
    /**
     * 
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#suspendLesson(long)
     */
	public void suspendLesson(long lessonId) {
		Lesson lesson = lessonDAO.getLesson(new Long(lessonId));
		Integer state = lesson.getLessonStateId();
		//only suspend started lesson
		if(!Lesson.STARTED_STATE.equals(state)){
			throw new MonitoringServiceException("Lesson does not started yet. It can not be suspended.");
		}
    	if ( lesson == null ) {
    		throw new MonitoringServiceException("Lesson for id="+lessonId+" is missing. Unable to suspend lesson.");
    	}
    	setLessonState(lesson,Lesson.SUSPENDED_STATE);
	}
	/**
	 * 
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#unsuspendLesson(long)
	 */
	public void unsuspendLesson(long lessonId) {
		Lesson lesson = lessonDAO.getLesson(new Long(lessonId));
		Integer state = lesson.getLessonStateId();
		//only suspend started lesson
		if(!Lesson.SUSPENDED_STATE.equals(state)){
			throw new MonitoringServiceException("Lesson is not suspended lesson. It can not be unsuspended.");
		}
		if ( lesson == null ) {
			throw new MonitoringServiceException("Lesson for id="+lessonId+" is missing. Unable to suspend lesson.");
		}
		setLessonState(lesson,Lesson.STARTED_STATE);
	}

    /**
     * Set lesson status to given status value. The stauts value will be one value of class level in   
     * org.lamsfoundation.lams.lesson.Lesson.
     *  
     * @param lessonId
     * @param status
     */
    private void setLessonState(long lessonId,Integer status) {
    	
    	Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
    	if( status == null ){
    		throw new MonitoringServiceException("Lesson status is required");
    	}
    	if ( requestedLesson == null ) {
    		throw new MonitoringServiceException("Lesson for id="+lessonId+" is missing. Unable to set lesson status to "+ status.intValue());
    	}
    	
    	setLessonState(requestedLesson,status);
    	
    }
    /**
     * @see setLessonState(long,Integer)
     * @param requestedLesson
     * @param status
     */
    private void setLessonState(Lesson requestedLesson,Integer status) {
    	
    	requestedLesson.setLessonStateId(status);
    	lessonDAO.updateLesson(requestedLesson);
    	
    }
    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#removeLesson(long)
     */
    public void removeLesson(long lessonId) {
        Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
        if ( requestedLesson == null) {
        	throw new MonitoringServiceException("Lesson for id="+lessonId+" is missing. Unable to remove lesson.");
        }

        requestedLesson.setLessonStateId(Lesson.DISABLED_STATE);
        lessonDAO.updateLesson(requestedLesson);
    	
    }

    /**
     * Delete a lesson and all its contents. Warning: at the moment, this should only be done to preview lessons.
     * Can't guarentee data integrity if it is done to any other type of lesson. See removeLesson() for hiding
     * lessons from a teacher's view without removing them from the database.
     * 
     * This code actually checks that the lesson is a preview lesson - writes out a warning message if it is not
     * a preview lesson.
     * 
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#deleteLesson(org.lamsfoundation.lams.lesson.Lesson)
     * TODO remove the related tool data.
     */
    public void deleteLesson(Lesson lesson) {
    	
		if ( lesson != null && lesson.getLearningDesign()!= null && lesson.getLearningDesign().getCopyTypeID() != null && 
				LearningDesign.COPY_TYPE_PREVIEW == lesson.getLearningDesign().getCopyTypeID().intValue() ) { 
	        lessonDAO.deleteLesson(lesson);
		} else {
			log.warn("Unable to delete lesson as lesson is not a preview lesson. Learning design copy type was "
					+(lesson != null && lesson.getLearningDesign()!= null ? lesson.getLearningDesign().getCopyTypeID() : null)
					+" Lesson is "+lesson);
		}

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
     * @throws LamsToolServiceException 
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#forceCompleteLessonByUser(Integer,long,long)
     */
    public String forceCompleteLessonByUser(Integer learnerId,long lessonId,Long activityId) 
    {

    	List currentLessonLearners = learnerService.getActiveLearnersByLesson(lessonId);
    	User learner = userDAO.getUserById(learnerId);
        Lesson newLesson = lessonDAO.getLesson(new Long(lessonId));
        Set activities = newLesson.getLearningDesign().getActivities();
        /*
         Gate -- LearnerService.knockGate(GateActivity gate, User knocker, List lessonLearners)
         Y - continue
         N - Stop
         Group --  getGroup -> exist? 
         Y - continue 
         N - PermissionGroup - Stop
         	 RandomGroup - create group, then complete it and continue.
         */
        String stopReason = "Success to complete to the end of lesson";
        for (Iterator i = activities.iterator(); i.hasNext();)
        {
            Activity actProxy = (Activity) i.next();
            //reload activity to get real object instead of CGLIB proxy
            Activity activity = activityDAO.getActivityByActivityId(actProxy.getActivityId());
            
            if (activity.isGroupingActivity()){
            	GroupingActivity groupActivity = (GroupingActivity) activity;
            	Grouping grouping = groupActivity.getCreateGrouping();
            	Group myGroup = grouping.getGroupBy(learner);
            	if(myGroup == null && myGroup.isNull()){
            		//group does not exist
            		if(grouping.isRandomGrouping()){
            			//for random grouping, create then complete it. Continue 
            			learnerService.performGrouping(groupActivity,currentLessonLearners);
//            			grouping = groupActivity.getCreateGrouping();
//            			myGroup = grouping.getGroupBy(learner);
            			learnerService.completeActivity(learner,activity,newLesson);
            			log.debug("Grouping activity [" + activity.getActivityId() + "] is completed.");
            		}else{
	            		//except random grouping, stop here
	            		stopReason = "Force complete stop at non-grouped grouping activity [" + groupActivity + "]";
	            		break;
            		}
            	}else{
            		//if group already exist
            		learnerService.completeActivity(learner,activity,newLesson);
            		log.debug("Grouping activity [" + activity.getActivityId() + "] is completed.");
            	}
            }else if ( activity.isGateActivity() ) {
            	GateActivity gate = (GateActivity) activity;
            	if(learnerService.knockGate(gate,learner,currentLessonLearners)){
            		//the gate is opened, continue to next activity to complete
            		learnerService.completeActivity(learner,activity,newLesson);
            		log.debug("Gate activity [" + gate.getActivityId() + "] is completed.");
            	}else{
            		//the gate is closed, stop here
            		stopReason = "Force complete stop at gate activity [" + gate + "]";
            		break;
            	}
            }else{
            	//left: toolActivity and complexActivity
            	if(activity.isToolActivity()){
            		ToolActivity toolActivity = (ToolActivity) activity;
            		try {
						ToolSession toolSession = lamsCoreToolService.getToolSessionByActivity(learner,toolActivity);
						learnerService.completeToolSession(toolSession.getToolSessionId(),new Long(learnerId.intValue()));
						log.debug("Tool activity [" + activity.getActivityId() + "] is completed.");
					} catch (LamsToolServiceException e) {
						throw new MonitoringServiceException(e);
					}
            	}else if(activity.isComplexActivity()){
            		//for complex activities:SEQUENCE ACTIVITY,PARALLEL ACTIVITY,OPTIONS ACTIVITY
            		ComplexActivity complexActivity = (ComplexActivity) activity;
            		Set allActivities = complexActivity.getActivities();
            		Iterator iter = allActivities.iterator();
            		while(iter.hasNext()){
            			Activity act = (Activity) iter.next();
            			forceCompleteLessonByUser(learnerId,lessonId,act.getActivityId());
            		}
            		log.debug("Complex activity [" + activity.getActivityId() + "] is completed.");
            	}
            	
            }
            //complete to the given activity ID, then stop. To be sure, the given activity is forced to complete as well.
            if(activityId != null && activities.equals(activity.getActivityId())){
            	//stop here
            	stopReason = "success complete to the given activity [" + activityId + "]";
            	break;
            }
        }
        
        return stopReason;

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
    	return lessonDAO.getLessonsCreatedByUser(userID);
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
    	FlashMessage flashMessage;
    	if(lesson!=null){
    		flashMessage = new FlashMessage("getLessonDetails",lesson.getLessonDetails());
    	}else
    		flashMessage = new FlashMessage("getLessonDetails",
    										messageService.getMessage("NO.SUCH.LESSON",new Object[]{lessonID}),
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
    	FlashMessage flashMessage;
    	if(lesson!=null){
    		Iterator iterator = lesson.getLessonClass().getLearners().iterator();
    		while(iterator.hasNext()){
    			User user = (User)iterator.next();
    			lessonLearners.add(user.getUserDTO());
    		}    		
    		flashMessage = new FlashMessage("getLessonLearners",lessonLearners);
    	}else
    		flashMessage = new FlashMessage("getLessonLearners",
    										 messageService.getMessage("NO.SUCH.LESSON",new Object[]{lessonID}),
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
    	FlashMessage flashMessage;
    	if(lesson!=null){
    		Iterator iterator = lesson.getLearnerProgresses().iterator();
    		while(iterator.hasNext()){
    			LearnerProgress learnerProgress = (LearnerProgress)iterator.next();
    			progressData.add(learnerProgress.getLearnerProgressData());
    		}
    		flashMessage = new FlashMessage("getAllLearnersProgress",progressData);
    	}else{
    		flashMessage = new FlashMessage("getAllLearnersProgress",
    				 							messageService.getMessage("NO.SUCH.LESSON",new Object[]{lessonID}),
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
    	FlashMessage flashMessage;
    	if(lesson!=null){
    		Vector sortedSet = getOrderedActivityTree(lesson.getLearningDesign());
    		flashMessage = new FlashMessage("getAllContributeActivities",sortedSet);
    	}else{
    		flashMessage = new FlashMessage("getAllContributeActivities",
    										messageService.getMessage("NO.SUCH.LESSON",new Object[]{lessonID}),
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
    	User user = userManagementService.getUserById(userID);
    	FlashMessage flashMessage;
    	if(activity==null || user==null){
    		flashMessage = new FlashMessage("getLearnerActivityURL",
    										messageService.getMessage("INVALID.ACTIVITYID.USER", new Object[]{ activityID,userID }),
											FlashMessage.ERROR);
    	}else{
    		if(activity.isToolActivity()){
        		ToolActivity toolActivity = (ToolActivity)activity;        		
        		String toolURL = lamsCoreToolService.getLearnerToolURLByMode(toolActivity,user,ToolAccessMode.TEACHER);        		
        		flashMessage = new FlashMessage("getLearnerActivityURL",new ProgressActivityDTO(activityID,toolURL));
        	}else{
        		flashMessage = new FlashMessage("getLearnerActivityURL",
        										messageService.getMessage("INVALID.ACTIVITYID.TYPE", new Object[]{ activity.getActivityId()}),
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
    	FlashMessage flashMessage = null;
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
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getActivityDefineLaterURL(java.lang.Long)
	 */
	public String getActivityDefineLaterURL(Long activityID)throws IOException{
		Activity activity = activityDAO.getActivityByActivityId(activityID);
    	FlashMessage flashMessage = null;
		if(activity!=null){
			if(activity.isToolActivity()){
				ToolActivity toolActivity = (ToolActivity)activity;
				String url = toolActivity.getTool().getDefineLaterUrl();
				Long toolContentId = toolActivity.getToolContentId();
				if ( url !=null &&  toolContentId != null ) {
					url = WebUtil.appendParameterToURL(url,
								AttributeNames.PARAM_TOOL_CONTENT_ID,
								toolActivity.getToolContentId().toString());
					flashMessage = new FlashMessage("getActivityDefineLaterURL",new ProgressActivityDTO(activityID, url));
				} else {
					flashMessage = generateDataMissingPacket(activityID, url, "Define Late URL", toolContentId, "Tool Content ID");
				}
			}
		}else
			flashMessage = FlashMessage.getNoSuchActivityExists("getActivityDefineLaterURL",activityID);
		
		return flashMessage.serializeMessage();
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#getActivityMonitorURL(java.lang.Long)
	 */
	public String getActivityMonitorURL(Long activityID)throws IOException{
		Activity activity = activityDAO.getActivityByActivityId(activityID);
    	FlashMessage flashMessage = null;
		if(activity!=null){
			if(activity.isToolActivity()){
				ToolActivity toolActivity = (ToolActivity)activity;
				String url = toolActivity.getTool().getMonitorUrl();
				Long toolContentId = toolActivity.getToolContentId();
				if ( url !=null &&  toolContentId != null ) {
					url = WebUtil.appendParameterToURL(url,
								AttributeNames.PARAM_TOOL_CONTENT_ID,
								toolActivity.getToolContentId().toString());
					flashMessage = new FlashMessage("getActivityMonitorURL",new ProgressActivityDTO(activityID, url));
				} else {
					flashMessage = generateDataMissingPacket(activityID, url, "Monitor URL", toolContentId, "Tool Content ID");
				}
			}
		}else
			flashMessage = FlashMessage.getNoSuchActivityExists("getActivityMonitorURL",activityID);
		
		return flashMessage.serializeMessage();	
	}
	/**
	 * @param activityID
	 * @param url
	 * @param toolContentId
	 */
	private FlashMessage generateDataMissingPacket(Long activityID, String url, 
			String urlDescription, Long toolContentId, String toolContentIdDescription) {
		String[] missing = null;
		if ( url !=null &&  toolContentId != null )
			missing = new String[] {urlDescription, toolContentIdDescription};
		else if ( url !=null )
			missing = new String[] {urlDescription};
		else if ( toolContentId !=null )
			missing = new String[] {toolContentIdDescription};
		return FlashMessage.getDataMissing("getActivityMonitorURL",missing);
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#moveLesson(java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	public String moveLesson(Long lessonID, Integer targetWorkspaceFolderID,Integer userID)throws IOException{
		Lesson lesson = lessonDAO.getLesson(lessonID);
    	FlashMessage flashMessage;
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
											messageService.getMessage("NO.SUCH.LESSON",new Object[]{lessonID}),
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
    	FlashMessage flashMessage;
	 	if(lesson!=null){
	 		if(lesson.getUser().getUserId().equals(userID)){
	 			lesson.setLessonName(newName);
	 			lessonDAO.updateLesson(lesson);
	 			flashMessage = new FlashMessage("renameLesson",newName);
	 		}else
	 			flashMessage = FlashMessage.getUserNotAuthorized("renameLesson",userID);
	 	}else
	 		flashMessage = new FlashMessage("renameLesson",
	 										messageService.getMessage("NO.SUCH.LESSON",new Object[]{lessonID}),
											FlashMessage.ERROR);
	 	return flashMessage.serializeMessage();
	 }

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#checkGateStatus(java.lang.Long, java.lang.Long)
	 */
	public String checkGateStatus(Long activityID, Long lessonID) throws IOException
	{
    	FlashMessage flashMessage;
	    GateActivity gate = (GateActivity)activityDAO.getActivityByActivityId(activityID);
	    Lesson lesson = lessonDAO.getLesson(lessonID); //used to calculate the total learners.
	    
	    if(gate==null || lesson==null){
	    		flashMessage = new FlashMessage("checkGateStatus",
	    										messageService.getMessage("INVALID.ACTIVITYID.LESSONID",new Object[]{activityID,lessonID}),
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
    	FlashMessage flashMessage;
	    if (gate ==null)
	    {
	        flashMessage = new FlashMessage("releaseGate", 
	        								messageService.getMessage("INVALID.ACTIVITYID",new Object[]{activityID}),
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
	 /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#performChosenGrouping(GroupingActivity,java.util.List)
     */
	public void performChosenGrouping(GroupingActivity groupingActivity, List groups) {
        Grouping grouping = groupingActivity.getCreateGrouping();
        
        if(!grouping.isChosenGrouping()){
        	log.error("GroupingActivity ["+groupingActivity.getActivityId() +"] does not have chosen grouping.");
        	throw new MonitoringServiceException("GroupingActivity ["+groupingActivity.getActivityId()
        			+"] is not chosen grouping.");
        }
        try {
        	//try to sorted group list by orderID.
        	Iterator iter = groups.iterator();
        	Map sortedMap = new TreeMap(new Comparator(){
					public int compare(Object arg0, Object arg1) {
						return ((Long)arg0).compareTo((Long)arg1);
					}
        		}
        	); 
	        while(iter.hasNext()){
	        	Hashtable group = (Hashtable) iter.next();
	        	Long orderId = WDDXProcessor.convertToLong(group,MonitoringConstants.KEY_GROUP_ORDER_ID);
	        	sortedMap.put(orderId,group);
	        }
	        iter = sortedMap.values().iterator();
	        //grouping all group in list
	        for(int orderId=0;iter.hasNext();orderId++){
	        	Hashtable group = (Hashtable) iter.next();
	        	List learnerIdList = (List) group.get(MonitoringConstants.KEY_GROUP_LEARNERS);
	        	String groupName = WDDXProcessor.convertToString(group,MonitoringConstants.KEY_GROUP_NAME);
	        	List learners = new ArrayList();
	        	//? Seem too low efficient, is there a easy way?
	        	for(int idx=0;idx<learnerIdList.size();idx++){
	        		User user = userDAO.getUserById(new Integer(((Double)learnerIdList.get(idx)).intValue()));
	        		learners.add(user);
	        	
	        	}
	        	log.debug("Performing grouping for " + groupName + "...");
	        	grouping.doGrouping(groupName,learners);
	        	log.debug("Finish grouping for " + groupName);
	        }
	        
	        log.debug("Persist grouping for [" + grouping.getGroupingId()  + "]...");
        	groupingDAO.update(grouping);
        	log.debug("Persist grouping for [" + grouping.getGroupingId()  + "] success.");
        	
        } catch (WDDXProcessorConversionException e) {
        	throw new MonitoringServiceException("Perform chosen grouping occurs error when parsing WDDX package:"
        			+ e.getMessage());
        }
        
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
    									  String learnerGroupName,
                                          List organizationUsers,
                                          String staffGroupName,
                                          List staffs,
                                          Lesson newLesson)
    {
        //create a new lesson class object
        LessonClass newLessonClass = createNewLessonClass(newLesson.getLearningDesign());
        lessonClassDAO.saveLessonClass(newLessonClass);

        //setup staff group
        newLessonClass.setStaffGroup(Group.createStaffGroup(newLessonClass,staffGroupName,
                                                            new HashSet(staffs)));
        //setup learner group
        //TODO:need confirm group name!
        newLessonClass.getGroups()
                      .add(Group.createLearnerGroup(newLessonClass, learnerGroupName,
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

    //---------------------------------------------------------------------
    // Helper Methods - start lesson
    //---------------------------------------------------------------------
    /**
     * If the activity is not grouped, then it create lams tool session for 
     * all the learners in the lesson. After the creation of lams tool session, 
     * it delegates to the tool instances to create tool's own tool session. 
     * <p>
     * @param activity the tool activity that all tool session reference to.
     * @param lesson the target lesson that these tool sessions belongs to.
     * @throws LamsToolServiceException the exception when lams is talking to tool.
     */
    private void initToolSessionIfSuitable(ToolActivity activity, Lesson lesson) 
    {
    	if ( ! activity.getApplyGrouping().booleanValue() ) {
    		activity.setToolSessions(new HashSet());
    		try {
    		
    		Set newToolSessions = lamsCoreToolService.createToolSessions(lesson.getAllLearners(), activity,lesson);
    		Iterator iter = newToolSessions.iterator();
    		while (iter.hasNext()) {
    			// core has set up a new tool session, we need to ask tool to create their own 
    			// tool sessions using the given id and attach the session to the activity.
    			ToolSession toolSession = (ToolSession) iter.next();
	            lamsCoreToolService.notifyToolsToCreateSession(toolSession, activity);
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
        	if((scheduleGate.getGateStartTimeOffset() == null && scheduleGate.getGateEndTimeOffset() == null) || 
        	   (scheduleGate.getGateStartTimeOffset() != null && scheduleGate.getGateEndTimeOffset() == null))
        		scheduler.scheduleJob(openScheduleGateJob, openGateTrigger);
        	else if(openGateTrigger.getStartTime().before(closeGateTrigger.getStartTime())) {
        		scheduler.scheduleJob(openScheduleGateJob, openGateTrigger);
        		scheduler.scheduleJob(closeScheduleGateJob, closeGateTrigger);
        	}

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
     * 
     * @return the bean that defines start lesson on schedule job.
     */
    private JobDetail getStartScheduleLessonJob() {
    	return (JobDetail)applicationContext.getBean(MonitoringConstants.JOB_START_LESSON);
	}
    /**
     * 
     * @return the bean that defines start lesson on schedule job.
     */
    private JobDetail getFinishScheduleLessonJob() {
    	return (JobDetail)applicationContext.getBean(MonitoringConstants.JOB_FINISH_LESSON);
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
  
    /* ** Temporary methods to support the dummy monitoring pages */
    /** Get a map of organisations. Their users shoulc  and their users. */
    public List getOrganisationsUsers(Integer userId) {
    	
    	User user = userManagementService.getUserById(userId);
    	List orgs = userManagementService.getOrganisationsForUserByRole(user, Role.STAFF);
    	// Make sure the users are loaded
    	Iterator iter = orgs.iterator();
    	while (iter.hasNext()) {
			Organisation element = (Organisation) iter.next();
			element.getUsers();
		}
    	return orgs;
    }
   
    /** Get all the learning designs for this user */
    public List getLearningDesigns(Long userId) {
    	
    	return learningDesignDAO.getLearningDesignByUserId(userId);
    }
    
   //---------------------------------------------------------------------
   // Preview related methods
   //---------------------------------------------------------------------
   /* (non-Javadoc)
   	 * @see org.lamsfoundation.lams.preview.service.IMonitoringService#createPreviewClassForLesson(long, long)
   	 */
       public Lesson createPreviewClassForLesson(int userID, long lessonID) throws UserAccessDeniedException {

           User user = userManagementService.getUserById(new Integer(userID));
           if ( user == null ) {
           	throw new UserAccessDeniedException("User "+userID+" not found");
           }
           Organisation organisation = user.getBaseOrganisation();
           
           // create the lesson class - add the teacher as the learner and as staff
           LinkedList learners = new LinkedList();
           learners.add(user);

           LinkedList staffs = new LinkedList();
           staffs.add(user);
           
           return createLessonClassForLesson(lessonID,
           		organisation,
           		"Learner Group",
   				learners,
   				"Staff Group",
                   staffs);

       }
    
      /* (non-Javadoc)
   	 * @see org.lamsfoundation.lams.preview.service.IMonitoringService#deletePreviewSession(long)
   	 */
       public void deletePreviewLesson(long lessonID) {
       	Lesson lesson = lessonDAO.getLesson(new Long(lessonID));
       	deletePreviewLesson(lesson);
       }
       
       private void deletePreviewLesson(Lesson lesson) {
       	if ( lesson != null ) {
       		if ( lesson.getLearningDesign().getCopyTypeID() != null && 
       				LearningDesign.COPY_TYPE_PREVIEW == lesson.getLearningDesign().getCopyTypeID().intValue() ) {
       			
       	    	// get all the tool sessions for this lesson and remove all the tool session data
       			List toolSessions = lamsCoreToolService.getToolSessionsByLesson(lesson);
       			if ( toolSessions != null && toolSessions.size() > 0 ) {
       				Iterator iter = toolSessions.iterator();
       				while ( iter.hasNext() ) {
       					ToolSession toolSession = (ToolSession) iter.next();
       					lamsCoreToolService.deleteToolSession(toolSession);
       				}
       			} else {
       				log.debug("deletePreviewLesson: Removing tool sessions - none exist");
       			}

       			// lesson has learning design as a foriegn key, so need to remove lesson before learning design
       			LearningDesign ld = lesson.getLearningDesign();
       	    	deleteLesson(lesson);
       			authoringService.deleteLearningDesign(ld);
       		
       		} else {
       			log.warn("Unable to delete lesson as lesson is not a preview lesson. Learning design copy type was "+lesson.getLearningDesign().getCopyTypeID());
       		}
       	}
       }

       /* (non-Javadoc)
   	 * @see org.lamsfoundation.lams.preview.service.IMonitoringService#deleteAllOldPreviewLessons(int)
   	 */
       public int deleteAllOldPreviewLessons() {

    	int numDays = Configuration.getAsInt(ConfigurationKeys.PREVIEW_CLEANUP_NUM_DAYS);

   	    // Contract checking 
   	    if ( numDays <= 0 ) {
   	    	log.error("deleteAllOldPreviewSessions: number of days invalid ("
   	    			+numDays
   	    			+"). See configuration file (option "
   	    			+ConfigurationKeys.PREVIEW_CLEANUP_NUM_DAYS
   	    			+" Unable to delete any preview lessons");
   	    	return 0;
   	    }
   	   
   	    int numDeleted = 0;

     		// calculate comparison date
   	    long newestDateToKeep = System.currentTimeMillis() - ( numDays * numMilliSecondsInADay);
   	    Date date = new Date(newestDateToKeep);
   	    // convert data to UTC
   	    log.info("Deleting all preview lessons before "+date.toString()+" (server time) ("+newestDateToKeep+")");
     	  
     	// get all the preview sessions older than a particular date.
   	    List sessions = lessonDAO.getPreviewLessonsBeforeDate(date);
   	    Iterator iter = sessions.iterator();
   	    while (iter.hasNext()) {
   	    	Lesson lesson = (Lesson) iter.next();
   	    	try {
   	    		deletePreviewLesson(lesson);
   	       	    numDeleted++;
   	    	} catch ( Exception e ) {
   	    		log.error("Unable to delete lesson "+lesson.getLessonId()+" due to exception.",e);
   	    	}
   		}
   	    
   		return numDeleted;
       }
  
}