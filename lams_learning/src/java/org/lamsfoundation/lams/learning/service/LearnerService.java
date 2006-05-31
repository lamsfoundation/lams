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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.learning.service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.progress.ProgressEngine;
import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressDTO;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
/**
 * This class is a facade over the Learning middle tier.
 * @author chris, Jacky Fang
 */
public class LearnerService implements ILearnerService
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(LearnerService.class);
    
    private ILearnerProgressDAO learnerProgressDAO;
    private ILessonDAO lessonDAO;
    private IActivityDAO activityDAO;
    private IGroupingDAO groupingDAO;
    private ProgressEngine progressEngine;
    private IToolSessionDAO toolSessionDAO;
    private ILamsCoreToolService lamsCoreToolService;
    private ActivityMapping activityMapping;
    private IUserManagementService userManagementService;
    private ILessonService lessonService;
    protected MessageService messageService;
    

    //---------------------------------------------------------------------
    // Inversion of Control Methods - Constructor injection
    //---------------------------------------------------------------------
    
    /** Creates a new instance of LearnerService */
    public LearnerService(ProgressEngine progressEngine)
    {
        this.progressEngine = progressEngine;
    }
    //---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    //---------------------------------------------------------------------
	/**
	 * Set i18n MessageService
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

    public MessageService getMessageService() {
		return messageService;
	}
	/**
     * @param toolSessionDAO The toolSessionDAO to set.
     */
    public void setToolSessionDAO(IToolSessionDAO toolSessionDAO)
    {
        this.toolSessionDAO = toolSessionDAO;
    }
    
    /**
     * @param lessonDAO The lessonDAO to set.
     */
	public void setLessonDAO(ILessonDAO lessonDAO) 
	{
		this.lessonDAO = lessonDAO;
	}

    /**
     * @param learnerProgressDAO The learnerProgressDAO to set.
     */
    public void setLearnerProgressDAO(ILearnerProgressDAO learnerProgressDAO)
    {
        this.learnerProgressDAO = learnerProgressDAO;
    }

    /**
     * @param lamsToolService The lamsToolService to set.
     */
    public void setLamsCoreToolService(ILamsCoreToolService lamsToolService)
    {
        this.lamsCoreToolService = lamsToolService;
    }
    
    public void setActivityMapping(ActivityMapping activityMapping) {
		this.activityMapping = activityMapping;
	}
    
    /**
     * @param activityDAO The activityDAO to set.
     */
    public void setActivityDAO(IActivityDAO activityDAO)
    {
        this.activityDAO = activityDAO;
    }
    
    /**
     * @param groupingDAO The groupingDAO to set.
     */
    public void setGroupingDAO(IGroupingDAO groupingDAO)
    {
        this.groupingDAO = groupingDAO;
    }
    /**
     * @return the User Management Service
     */
	public IUserManagementService getUserManagementService() {
		return userManagementService;
	}
	/**
	 * @param userService User Management Service
	 */
	public void setUserManagementService(IUserManagementService userService) {
		this.userManagementService = userService;
	}
	
	public void setLessonService(ILessonService lessonService) {
		this.lessonService = lessonService;
	}

    //---------------------------------------------------------------------
    // Service Methods
    //---------------------------------------------------------------------
    /**
     * Delegate to lesson dao to load up the lessons.
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#getActiveLessonsFor(org.lamsfoundation.lams.usermanagement.User)
     */
    public LessonDTO[] getActiveLessonsFor(Integer learnerId)
    {
    	User learner = userManagementService.getUserById(learnerId);
        List activeLessons = this.lessonDAO.getActiveLessonsForLearner(learner);
        return getLessonDataFor(activeLessons);
    }
    
    public Lesson getLesson(Long lessonId)
    {
        return lessonDAO.getLesson(lessonId);
    }
    
    /**
     * Get the lesson data for a particular lesson. In a DTO format suitable for sending to the client.
     */
    public LessonDTO getLessonData(Long lessonId)
    {
    	Lesson lesson = getLesson(lessonId);
    	return ( lesson != null ? lesson.getLessonData() : null );
    }
 
    /**
     * <p>Joins a User to a lesson as a learner. It could either be a new lesson
     * or a lesson that has been started.</p>
     * 
     * <p>In terms of new lesson, a new learner progress would be initialized.
     * Tool session for the next activity will be initialized if necessary.</p>
     * 
     * <p>In terms of an started lesson, the learner progress will be returned
     * without calculation. Tool session will be initialized if necessary.
     * Note that we won't initialize tool session for current activity because
     * we assume tool session will always initialize before it becomes a 
     * current activity.</p
     * 
     * 
     * @param learnerId the Learner's userID
     * @param lessionID identifies the Lesson to start
     * @throws LamsToolServiceException
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress joinLesson(Integer learnerId, Long lessonID)  
    {
    	User learner = userManagementService.getUserById(learnerId);
    	
    	Lesson lesson = getLesson(lessonID);
        LearnerProgress learnerProgress = learnerProgressDAO.getLearnerProgressByLearner(learner.getUserId(),lesson);
    	
        if(learnerProgress==null)
        {
            //create a new learner progress for new learner
            learnerProgress = new LearnerProgress(learner,lesson);
            
            try
            {
                progressEngine.setUpStartPoint(learner, lesson,learnerProgress);
            }
            catch (ProgressException e)
            {
                log.error("error occurred in 'setUpStartPoint':"+e.getMessage());
        		throw new LearnerServiceException(e.getMessage());
            }
        	//Use TimeStamp rather than Date directly to keep consistent with Hibnerate persiste object.
        	learnerProgress.setStartDate(new Timestamp(new Date().getTime()));
            learnerProgressDAO.saveLearnerProgress(learnerProgress);
        }
        //The restarting flag should be setup when the learner hit the exit
        //button. But it is possible that user exit by closing the browser,
        //In this case, we set the restarting flag again.
        else if(!learnerProgress.isRestarting())
        {
            learnerProgress.setRestarting(true);
            learnerProgressDAO.updateLearnerProgress(learnerProgress);
        }
        
        createToolSessionsIfNecessary(learnerProgress);
    	return learnerProgress;
    }
    


    /**
     * Returns the current progress data of the User. 
     * @param learnerId the Learner's userID
     * @param lesson the Lesson to get progress from.
     * @return LearnerProgess contains the learner's progress for the lesson.
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress getProgress(Integer learnerId, Lesson lesson)
    {
        return learnerProgressDAO.getLearnerProgressByLearner(learnerId, lesson);
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#getProgressById(java.lang.Long)
     */
    public LearnerProgress getProgressById(Long progressId)
    {
        return learnerProgressDAO.getLearnerProgress(progressId);
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#getProgressDTOById(java.lang.Long)
     */
    public LearnerProgressDTO getProgressDTOById(Long progressId)
    {
        return learnerProgressDAO.getLearnerProgress(progressId).getLearnerProgressData();
    }

    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#chooseActivity(org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.lesson.Lesson, org.lamsfoundation.lams.learningdesign.Activity)
     */
    public LearnerProgress chooseActivity(Integer learnerId, Lesson lesson, Activity activity) 
    {
    	LearnerProgress progress = learnerProgressDAO.getLearnerProgressByLearner(learnerId, lesson);
    	progress.setProgressState(activity, LearnerProgress.ACTIVITY_ATTEMPTED);
    	learnerProgressDAO.saveLearnerProgress(progress);
    	return progress;
    }
    
    
    /**
     * Calculates learner progress and returns the data required to be displayed 
     * to the learner.
     * TODO need to initialize tool session for next activity if necessary.
     * @param completedActivityID identifies the activity just completed
     * @param learner the Learner
     * @param lesson the Lesson in progress.
     * @return the bean containing the display data for the Learner
     * @throws LamsToolServiceException
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress calculateProgress(Activity completedActivity, 
                                             Integer learnerId, 
                                             Lesson lesson) 
    {
        LearnerProgress learnerProgress = learnerProgressDAO.getLearnerProgressByLearner(learnerId,lesson);

        try
        {
            learnerProgress = progressEngine.calculateProgress(learnerProgress.getUser(), lesson, completedActivity,learnerProgress);
            learnerProgressDAO.updateLearnerProgress(learnerProgress);
            
            createToolSessionsIfNecessary(learnerProgress);
        }
        catch (ProgressException e)
        {
            throw new LearnerServiceException(e.getMessage());
        }
/*        catch (LamsToolServiceException e)
        {
            throw new LearnerServiceException(e.getMessage());
        }
*/

        return learnerProgress;
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#completeToolSession(java.lang.Long, java.lang.Long)
     */
    public String completeToolSession(Long toolSessionId, Long learnerId) 
    {
        //update tool session state in lams
        ToolSession toolSession = lamsCoreToolService.getToolSessionById(toolSessionId);
    	
        toolSession.setToolSessionStateId(ToolSession.ENDED_STATE);
        
        lamsCoreToolService.updateToolSession(toolSession);
        
        return completeActivity(new Integer(learnerId.intValue()), toolSession.getToolActivity(), toolSession.getLesson());
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#completeActivity(java.lang.Integer, java.lang.Long, java.lang.Long)
     */
    public String completeActivity(Integer learnerId,Long activityId,Long lessonId) {
    	Activity activity = getActivity(activityId);
		Lesson lesson = getLesson(lessonId); 
    	return completeActivity(learnerId, activity,lesson);
    }

    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#completeActivity(java.lang.Integer, org.lamsfoundation.lams.learningdesign.Activity,org.lamsfoundation.lams.lesson.Lesson )
     */
    public String completeActivity(Integer learnerId,Activity activity,Lesson lesson)
    {
        //build up the url for next activity.
    	
    	// need to update the learner progress in the special user's session or the Flash 
    	// side won't be notified of the correct progress (via the display activity screen).
    	// this isn't nice as the service layer is calling the web layer, but as this
    	// is triggered from a tool calling completeToolSession, its a bit hard to avoid.
    	try 
    	{
	    	LearnerProgress nextLearnerProgress = calculateProgress(activity, learnerId, lesson);
	    	LearningWebUtil.setLearnerProgress(nextLearnerProgress);
	    	return activityMapping.getProgressURL(nextLearnerProgress);
    	}
        catch (UnsupportedEncodingException e)
        {
            log.error("error occurred in 'getProgressURL':"+e.getMessage());
    		throw new LearnerServiceException(e.getMessage());
        }
    }
    
    /**
     * Exit a lesson.
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#exitLesson(org.lamsfoundation.lams.lesson.LearnerProgress)
     */
    public void exitLesson(Long progressId)
    {
       LearnerProgress progress = learnerProgressDAO.getLearnerProgress(progressId);
       if ( progress != null ) {
    	   progress.setRestarting(true);
    	   learnerProgressDAO.updateLearnerProgress(progress);
           lessonService.removeLessonUserFromCache(progress.getLesson(),progress.getUser());
       } else { 
    	   String error = "Learner Progress "+progressId+" does not exist. Cannot exit lesson successfully.";
    	   log.error(error);
    	   throw new LearnerServiceException(error);
       }
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#getActivity(java.lang.Long)
     */
    public Activity getActivity(Long activityId)
    {
        return activityDAO.getActivityByActivityId(activityId);
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#performGrouping(java.lang.Long, java.lang.Long)
     */
    public void performGrouping(Long lessonId, Long groupingActivityId)
    {
    	GroupingActivity groupingActivity = (GroupingActivity) activityDAO.getActivityByActivityId(groupingActivityId, GroupingActivity.class);
    	if ( groupingActivity != null ) { 
    		performGrouping(lessonId, groupingActivity);
    		
    	} else {

    		String error = "Grouping activity "+groupingActivityId+" does not exist. Cannot perform grouping.";
            log.error(error);
    		throw new LearnerServiceException(error);

    	}
       
    }

    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#performGrouping(java.lang.Long, org.lamsfoundation.lams.learningdesign.GroupingActivity)
     */
    public void performGrouping(Long lessonId, GroupingActivity groupingActivity) {
    	
        List learners = lessonService.getActiveLessonLearners(lessonId.longValue());
	    Grouping grouping = groupingActivity.getCreateGrouping();
		grouping.doGrouping(learners);
		groupingDAO.update(grouping);
    }
    
	
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#performGrouping(org.lamsfoundation.lams.learningdesign.GroupingActivity, org.lamsfoundation.lams.usermanagement.User)
     */
    public void performGrouping(Long groupingActivityId, User learner)
    {
    	GroupingActivity groupingActivity = (GroupingActivity) activityDAO.getActivityByActivityId(groupingActivityId, GroupingActivity.class);
    	if ( groupingActivity != null ) { 

	        Grouping grouping = groupingActivity.getCreateGrouping();
	        grouping.doGrouping(learner);
	        groupingDAO.update(grouping);
	        
    	} else {

    		String error = "Grouping activity "+groupingActivityId+" does not exist. Cannot perform grouping.";
            log.error(error);
    		throw new LearnerServiceException(error);

    	}
        
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#knockGate(java.lang.Long, java.lang.Long, org.lamsfoundation.lams.usermanagement.User)
     */
    public boolean knockGate(Long lessonId, Long gateActivityId, User knocker) {
    	GateActivity gate = (GateActivity) activityDAO.getActivityByActivityId(gateActivityId, GateActivity.class);
    	if ( gate != null ) {
    		return knockGate(lessonId, gate,knocker);
    	} 
    	
		String error = "Gate activity "+gateActivityId+" does not exist. Cannot knock on gate.";
        log.error(error);
		throw new LearnerServiceException(error);
    }
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#knockGate(java.lang.Long, org.lamsfoundation.lams.learningdesign.GateActivity, org.lamsfoundation.lams.usermanagement.User)
     */
    public boolean knockGate(Long lessonId, GateActivity gate, User knocker)
    {
    		//get all learners who have started the lesson
        	List lessonLearners = getActiveLearnersByLesson(lessonId);

	        boolean gateOpen = false;
	        //knock the gate.
	        if(gate.shouldOpenGateFor(knocker,lessonLearners))
	            gateOpen = true;
	        else
	            gateOpen = false;
	        
	        //update gate including updating the waiting list and gate status in
	        //the database.
	        activityDAO.update(gate);
	        return gateOpen;
	        
    }
    
    //---------------------------------------------------------------------
    // Helper Methods
    //---------------------------------------------------------------------
    /**
     * This method navigate through all the tool activities included inside
     * the next activity. For each tool activity, we look up the database
     * to check up the existance of correspondent tool session. If the tool 
     * session doesn't exist, we create a new tool session instance.
     * 
     * @param learnerProgress the learner progress we are processing.
     * @throws LamsToolServiceException
     */
    private void createToolSessionsIfNecessary(LearnerProgress learnerProgress) 
    {
        if(learnerProgress.getNextActivity()==null)
            throw new LearnerServiceException("Error occurs in [" +
            		"createToolSessionsIfNecessary], Can't initialize tool " +
            		"sessions without knowing the activity.");
        
        Activity nextActivity = learnerProgress.getNextActivity();
        try
        {
            for(Iterator i = nextActivity.getAllToolActivities().iterator();i.hasNext();)
            {
            	ToolActivity toolActivity =  (ToolActivity)i.next();
           		createToolSessionFor(toolActivity, learnerProgress.getUser(),learnerProgress.getLesson());
            }
        }
        catch (LamsToolServiceException e)
        {
            log.error("error occurred in 'createToolSessionFor':"+e.getMessage());
    		throw new LearnerServiceException(e.getMessage());
        }
        catch (ToolException e)
        {
            log.error("error occurred in 'createToolSessionFor':"+e.getMessage());
    		throw new LearnerServiceException(e.getMessage());
        }
    }
    
    /**
     * <p>Create a lams tool session for learner against a tool activity. This will
     * have concurrency issues interms of grouped tool session because it might 
     * be inserting some tool session that has already been inserted by other
     * member in the group. If the unique_check is broken, we need to query
     * the database to get the instance instead of inserting it. It should be
     * done in the Spring rollback strategy. </p>
     *
     * Once lams tool session is inserted, we need to notify the tool to its
     * own session. 
     * 
     * @param toolActivity
     * @param learner
     * @throws LamsToolServiceException
     */
    private void createToolSessionFor(ToolActivity toolActivity,User learner,Lesson lesson) throws LamsToolServiceException, ToolException
    {
        ToolSession toolSession = lamsCoreToolService.createToolSession(learner,toolActivity,lesson);
        
        // if the tool session already exists, will return null
        if ( toolSession !=null ) {
	        toolActivity.getToolSessions().add(toolSession);
	        lamsCoreToolService.notifyToolsToCreateSession(toolSession, toolActivity);
        }
    }
    
    
    /**
     * Create an array of lesson dto based a list of lessons.
     * @param lessons the list of lessons.
     * @return the lesson dto array.
     */
    private LessonDTO[] getLessonDataFor(List lessons)
    {
        List lessonDTOList = new ArrayList();
        for(Iterator i=lessons.iterator();i.hasNext();)
        {
            Lesson currentLesson = (Lesson)i.next();
            lessonDTOList.add(currentLesson.getLessonData());
        }
        return (LessonDTO[])lessonDTOList.toArray(new LessonDTO[lessonDTOList.size()]);   
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#getActiveLearnersByLesson(long)
     */
    public List getActiveLearnersByLesson(long lessonId)
    {
    	return lessonService.getActiveLessonLearners(lessonId);
    }
    
    /**
     * @see org.lamsfoundation.lams.learning.service.ILearnerService#getCountActiveLessonLearners(long)
     */
    public Integer getCountActiveLearnersByLesson(long lessonId)
    {
    	return lessonService.getCountActiveLessonLearners(lessonId);
    }

}
