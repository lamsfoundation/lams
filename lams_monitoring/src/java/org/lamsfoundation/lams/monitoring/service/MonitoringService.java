/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.monitoring.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 
 * @author Jacky Fang 2/02/2005
 */
public class MonitoringService implements IMonitoringService,ApplicationContextAware
{
    private ILessonDAO lessonDAO;
    private ILessonClassDAO lessonClassDAO;
    private IAuthoringService authoringService;
    private ApplicationContext context;
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
        this.context=context;
    }

    /**
     * 
     */
    public MonitoringService()
    {
        super();
    }

    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#createLesson(long, org.lamsfoundation.lams.usermanagement.User, java.util.List, java.util.List)
     */
    public void createLesson(long learningDesignId, 
                             User user, 
                             Organisation organisation,
                             List organizationUsers, 
                             List staffs)
    {
        
        //copy the current learning design
        LearningDesign copiedLearningDesign = authoringService.copyLearningDesign(new Long(learningDesignId));
        
        //copy the tool content
        for(Iterator i = copiedLearningDesign.getActivities().iterator();i.hasNext();)
        {
            Activity currentActivity = (Activity)i.next();
            if(currentActivity.getActivityTypeId().intValue()==Activity.TOOL_ACTIVITY_TYPE)
            {
                ToolContentManager contentManager = (ToolContentManager)context.getBean(((ToolActivity)currentActivity).getTool().getToolSignature());
                contentManager.copyToolContent(((ToolActivity)currentActivity).getToolContentId(),new Long(1));
            }
        }
        
        //create a new lesson object
        LessonClass newLessonClass = createNewLessonClass(copiedLearningDesign);
        lessonClassDAO.saveLessonClass(newLessonClass);
        
        //setup staff group
        newLessonClass.setStaffGroup(Group.createStaffGroup(newLessonClass,new HashSet(staffs)));
        //setup learner group
        newLessonClass.getGroups().add(Group.createLearnerGroup(newLessonClass,new HashSet(organizationUsers)));
        lessonClassDAO.updateLessonClass(newLessonClass);
        
        //create new Lesson object
        Lesson newLesson = Lesson.createNewLesson(user, organisation, copiedLearningDesign, newLessonClass);
        newLessonClass.setLesson(newLesson);
        lessonDAO.saveLesson(newLesson);

    }

    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#startlesson(long)
     */
    public void startlesson(long lessonId)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#forceCompleteLessonByUser(long)
     */
    public void forceCompleteLessonByUser(long learnerProgressId)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param copiedLearningDesign
     * @return
     */
    private LessonClass createNewLessonClass(LearningDesign copiedLearningDesign)
    {
        //make a copy of lazily initialized activities
        Set activities = new HashSet(copiedLearningDesign.getActivities());
        LessonClass newLessonClass = new LessonClass(null, //grouping id
                                                     Grouping.CLASS_GROUPING_TYPE,
                                                     new HashSet(),//groups
                                                     activities,
                                                     null, //staff group 
                                                     null);//lesson
        return newLessonClass;
    }

    
}
