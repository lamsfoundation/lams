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
 * 
 * @author Jacky Fang 2/02/2005
 */
public interface IMonitoringService
{
    
    public void createLesson(long learningDesignId,User user,Organisation organisation,List organizationUsers,List staffs);;
    
    public void startlesson(long lessonId);
    
    public void forceCompleteLessonByUser(long learnerProgressId);
}
