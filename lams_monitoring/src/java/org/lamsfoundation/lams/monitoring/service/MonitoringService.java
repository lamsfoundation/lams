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
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.service.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

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
public class MonitoringService implements IMonitoringService
{

    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
    private ILessonDAO lessonDAO;
    private ILessonClassDAO lessonClassDAO;
    private ILamsToolService lamsToolService;
    private IAuthoringService authoringService;


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
     * @param lamsToolService The lamsToolService to set.
     */
    public void setLamsToolService(ILamsToolService lamsToolService)
    {
        this.lamsToolService = lamsToolService;
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
    public Lesson createLesson(String lessonName,
                               String lessonDescription,
                               long learningDesignId,
                               User user,
                               Organisation organisation,
                               List organizationUsers,
                               List staffs)
    {
        LearningDesign originalLearningDesign = authoringService.getLearningDesign(new Long(learningDesignId));
        //copy the current learning design
        LearningDesign copiedLearningDesign = authoringService.copyLearningDesign(originalLearningDesign,
                                                                                  new Integer(LearningDesign.COPY_TYPE_LESSON),
                                                                                  user,
                                                                                  originalLearningDesign.getWorkspaceFolder());

        //copy the tool content
        for (Iterator i = copiedLearningDesign.getActivities().iterator(); i.hasNext();)
        {
            Activity currentActivity = (Activity) i.next();
            if (currentActivity.isToolActivity())
            {
                Long newContentId = lamsToolService.copyToolContent((ToolActivity) currentActivity);
                ((ToolActivity) currentActivity).setToolContentId(newContentId);
            }
        }
        authoringService.updateLearningDesign(copiedLearningDesign);
        //create the new lesson
        return createNewLesson(lessonName,
                               lessonDescription,
                               user,
                               organisation,
                               organizationUsers,
                               staffs,
                               copiedLearningDesign);

    }

    /**
     * @throws LamsToolServiceException
     * @see org.lamsfoundation.lams.monitoring.service.IMonitoringService#startlesson(long)
     */
    public void startlesson(long lessonId) throws LamsToolServiceException
    {
        //we get the lesson just created
        Lesson requestedLesson = lessonDAO.getLesson(new Long(lessonId));
        //initialize tool sessions if necessary
        for (Iterator i = requestedLesson.getLearningDesign()
                                         .getActivities()
                                         .iterator(); i.hasNext();)
        {
            Activity activity = (Activity) i.next();
            //TODO this is for testing purpose as survey is the only tool available
            //so far.
            if (shouldInitToolSessionFor(activity)&&this.isSurvey((ToolActivity)activity))
            {
                initToolSessionFor((ToolActivity) activity,
                                   requestedLesson.getAllLearners(),
                                   requestedLesson);
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
    private Lesson createNewLesson(String lessonName,
                                   String lessonDescription,
                                   User user,
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
        Lesson newLesson = Lesson.createNewLesson(lessonName,
                                                  lessonDescription,
                                                  user,
                                                  organisation,
                                                  copiedLearningDesign,
                                                  newLessonClass);
        newLessonClass.setLesson(newLesson);
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

    /**
     * @param activity
     * @throws LamsToolServiceException
     */
    private void initToolSessionFor(ToolActivity activity, Set learners,Lesson lesson) throws LamsToolServiceException
    {
        activity.setToolSessions(new HashSet());
        for (Iterator i = learners.iterator(); i.hasNext();)
        {
            User learner = (User) i.next();

            ToolSession toolSession = lamsToolService.createToolSession(learner,activity,lesson);
            //ask tool to create their own tool sessions using the given id.
            lamsToolService.notifyToolsToCreateSession(toolSession.getToolSessionId(), activity);
            //update the hibernate persistent object
            activity.getToolSessions().add(toolSession);
        }
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