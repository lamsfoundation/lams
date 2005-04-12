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

import java.util.List;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.service.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * Interface defines all monitoring services needed by presentation tier.
 * @author Jacky Fang 2/02/2005
 */
public interface IMonitoringService
{

    /**
     * Initialize a new lesson so as to start the learning process. It needs to 
     * notify lams which learning design it belongs to. The initialize process
     * doesn't involve the setup of lesson class and organization.
     * 
     * @param lessonName the name of the lesson
     * @param lessonDescription the description of the lesson.
     * @param learningDesignId the selected learning design
     * @param user user the user who want to create this lesson.
     * @return the lesson initialized.
     */
    public Lesson initializeLesson(String lessonName, String lessonDescription,long learningDesignId,User user);;
    
    
    /**
     * Setup the lesson class and organization for a lesson according to the 
     * input from monitoring GUI interface.
     * 
     * @param lessonId the lesson without lesson class and organization
     * @param organisation  the organization this lesson belongs to.
     * @param organizationUsers a list of learner will be in this new lessons.
     * @param staffs a list of staffs who will be in charge of this lesson.
     * @return the lesson with lesson class and organization
     */
    public Lesson createLessonClassForLesson(long lessonId,Organisation organisation,List organizationUsers,List staffs);
    
    /**
     * Start the specified the lesson. It must be created before calling this
     * service.
     * @param lessonId the specified the lesson id.
     * @throws LamsToolServiceException the exception occurred during the
     * 									lams and tool interaction to start a
     * 									lesson.
     */
    public void startlesson(long lessonId)throws LamsToolServiceException;
    
    /**
     * Force the learner to complete all the activities for current lesson.
     * @param learnerProgressId the learner progress belongs to the user who
     * 							will be force complete
     */
    public void forceCompleteLessonByUser(long learnerProgressId);
    
    /**
     * Set the gate to open to let all the learners through. This learning service
     * is triggerred by the system scheduler.
     * @param gate the id of the gate we need to open.
     */
    public void openGate(Long gateId);
    
    /**
     * Set the gate to closed.
     * @param gate the id of the gate we need to close.
     */
    public void closeGate(Long gateId);
}
