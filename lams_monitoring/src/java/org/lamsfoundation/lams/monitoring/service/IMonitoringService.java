/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.monitoring.service;

import java.util.List;

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
    public void startlesson(long lessonId);
    
    /**
     * Force the learner to complete all the activities for current lesson.
     * @param learnerProgressId the learner progress belongs to the user who
     * 							will be force complete
     */
    public void forceCompleteLessonByUser(long learnerProgressId);
}
