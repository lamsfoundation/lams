/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.monitoring.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.ToolContentIDGenerator;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
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
 * @author Jacky Fang 2/02/2005
 */
public class MonitoringService implements
                              IMonitoringService,
                              ApplicationContextAware
{

    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
    private ILessonDAO lessonDAO;
    private ILessonClassDAO lessonClassDAO;
    private IToolSessionDAO toolSessionDAO;
    private IAuthoringService authoringService;
    private ApplicationContext context;
    private ToolContentIDGenerator contentIDGenerator;

    //---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    //---------------------------------------------------------------------
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
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
    }

    /**
     * @param contentIDGenerator The contentIDGenerator to set.
     */
    public void setContentIDGenerator(ToolContentIDGenerator contentIDGenerator)
    {
        this.contentIDGenerator = contentIDGenerator;
    }

    /**
     * @param toolSessionDAO The toolSessionDAO to set.
     */
    public void setToolSessionDAO(IToolSessionDAO toolSessionDAO)
    {
        this.toolSessionDAO = toolSessionDAO;
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
     * <li>3. Create a new lesson with all staffs and learners involved.</li>
     * 
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#createLesson(long, org.lamsfoundation.lams.usermanagement.User, java.util.List, java.util.List)
     */
    public void createLesson(long learningDesignId,
                             User user,
                             Organisation organisation,
                             List organizationUsers,
                             List staffs)
    {
        LearningDesign originalLearningDesign = authoringService.getLearningDesign(new Long(learningDesignId));
        //copy the current learning design
        LearningDesign copiedLearningDesign = authoringService.copyLearningDesign(originalLearningDesign);

        //copy the tool content
        for (Iterator i = copiedLearningDesign.getActivities().iterator(); i.hasNext();)
        {
            Activity currentActivity = (Activity) i.next();
            if (currentActivity.isToolActivity())
            {
                Long newContentId = copyToolContent((ToolActivity) currentActivity);
                ((ToolActivity) currentActivity).setToolContentId(newContentId);
            }
        }
        authoringService.updateLearningDesign(copiedLearningDesign);
        //create the new lesson
        createNewLesson(user,
                        organisation,
                        organizationUsers,
                        staffs,
                        copiedLearningDesign);

    }

    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#startlesson(long)
     */
    public void startlesson(long lessonId)
    {
        //we get the lesson just created
        Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
        //initialize tool sessions if necessary
        for (Iterator i = requestedLesson.getLearningDesign()
                                         .getActivities()
                                         .iterator(); i.hasNext();)
        {
            Activity activity = (Activity) i.next();
            if (shouldInitToolSessionFor(activity)&&this.isSurvey((ToolActivity)activity))
            {
                initToolSessionFor((ToolActivity) activity,
                                   requestedLesson.getAllLearners());
            }
        }
        //update lesson status
        requestedLesson.setLessonStateId(Lesson.STARTED_STATE);

        lessonDAO.updateLesson(requestedLesson);
    }

    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#forceCompleteLessonByUser(long)
     */
    public void forceCompleteLessonByUser(long learnerProgressId)
    {
        // TODO Auto-generated method stub

    }

    //---------------------------------------------------------------------
    // Helper Methods
    //---------------------------------------------------------------------
    /**
     * Create a new lesson and setup all the staffs and learners who will be
     * participating this less.
     * @param user the user who created this lesson.
     * @param organisation the organization this lesson belongs to.	
     * @param organizationUsers a list of learner will be in this new lessons.
     * @param staffs a list of staffs who will be in charge of this lesson.
     * @param copiedLearningDesign the new run-time learning design copy 
     * 							   for this lesson.
     */
    private void createNewLesson(User user,
                                 Organisation organisation,
                                 List organizationUsers,
                                 List staffs,
                                 LearningDesign copiedLearningDesign)
    {
        //create a new lesson object
        LessonClass newLessonClass = createNewLessonClass(copiedLearningDesign);
        lessonClassDAO.saveLessonClass(newLessonClass);

        //setup staff group
        newLessonClass.setStaffGroup(Group.createStaffGroup(newLessonClass,
                                                            new HashSet(staffs)));
        //setup learner group
        newLessonClass.getGroups()
                      .add(Group.createLearnerGroup(newLessonClass,
                                                    new HashSet(organizationUsers)));
        lessonClassDAO.updateLessonClass(newLessonClass);

        //create new Lesson object
        Lesson newLesson = Lesson.createNewLesson(user,
                                                  organisation,
                                                  copiedLearningDesign,
                                                  newLessonClass);
        newLessonClass.setLesson(newLesson);
        lessonDAO.saveLesson(newLesson);
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
     * Make a copy of all tools content which belongs to this learning design.
     * 
     * @param toolActivity the tool activity defined in the design.
     */
    private Long copyToolContent(ToolActivity toolActivity)
    {
        Long newToolcontentID = contentIDGenerator.getNextToolContentIDFor(toolActivity.getTool());
        //This is just for testing purpose because surveyService is the only 
        //service is available at the moment. 
        //TODO we need to remove this once all done.
        if (isSurvey(toolActivity))
        {
            ToolContentManager contentManager = (ToolContentManager) findToolService(toolActivity);
            contentManager.copyToolContent(toolActivity.getToolContentId(),
                                           newToolcontentID);
        }
        return newToolcontentID;
    }

    /**
     * Find a tool's service registered inside lams. It is implemented using
     * Spring now. We might need to extract this method to a proxy class to
     * find different service such as EJB or Web service. 
     * @param toolActivity the tool activity defined in the design.
     * @return the service object from tool.
     */
    private Object findToolService(ToolActivity toolActivity)
    {
        return context.getBean(toolActivity.getTool().getServiceName());
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

    /**
     * @param activity
     */
    private void initToolSessionFor(ToolActivity activity, Set learners)
    {
        activity.setToolSessions(new HashSet());
        for (Iterator i = learners.iterator(); i.hasNext();)
        {
            User learner = (User) i.next();
            ToolSession toolSession = new NonGroupedToolSession(activity,
                                                                new Date(System.currentTimeMillis()),
                                                                ToolSession.STARTED_STATE,
                                                                learner);
            toolSessionDAO.saveToolSession(toolSession);
            //ask tool to create their own tool sessions using the given id.
            notifyToolsToCreateSession(toolSession.getToolSessionId(), activity);
            //update the hibernate persistent object
            activity.getToolSessions().add(toolSession);
        }
    }

    /**
     * @param toolSessionId
     */
    private void notifyToolsToCreateSession(Long toolSessionId,
                                            ToolActivity activity)
    {
        ToolSessionManager sessionManager = (ToolSessionManager) findToolService(activity);
        //TODO this is for testing purpose as survey is the only tool available
        //so far.
        sessionManager.createToolSession(toolSessionId,
                                         activity.getToolContentId());
    }

    /**
     * @param activity
     * @return
     */
    private boolean shouldInitToolSessionFor(Activity activity)
    {
        return activity.isToolActivity()
                && !((ToolActivity) activity).getTool().getSupportsGrouping();
    }

}