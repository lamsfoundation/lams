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
     * Create a new lesson so as to start the learning process. It needs to 
     * notify lams which learning design it belongs to and who will be 
     * involved in this lesson.
     * 
     * @param learningDesignId the selected learning design
     * @param user user the user who want to create this lesson.
     * @param organisation  the organization this lesson belongs to.
     * @param organizationUsers a list of learner will be in this new lessons.
     * @param staffs a list of staffs who will be in charge of this lesson.
     */
    public void createLesson(long learningDesignId,User user,Organisation organisation,List organizationUsers,List staffs);;
    
    /**
     * Start the specified the lesson. It must be created before calling this
     * service.
     * @param lessonId the specified the lesson id.
     */
    public void startlesson(long lessonId)throws LamsToolServiceException;
    
    /**
     * Force the learner to complete all the activities for current lesson.
     * @param learnerProgressId the learner progress belongs to the user who
     * 							will be force complete
     */
    public void forceCompleteLessonByUser(long learnerProgressId);
}
