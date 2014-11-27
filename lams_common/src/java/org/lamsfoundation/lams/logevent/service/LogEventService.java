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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
/* $Id$ */  
package org.lamsfoundation.lams.logevent.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.dao.ILogEventDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * Class implements <code>ILogEventService</code>.
 * 
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.timezone.service.ILogEventService
 */
public class LogEventService implements ILogEventService{

    private Logger log = Logger.getLogger(LogEventService.class);

    /**
     * The only instance of this class, since it is a singleton.
     */
    private static LogEventService instance;    

    private ILogEventDAO logEventDAO;   
    private IUserManagementService userManagementService;
    
    /**
     * Default constructor. Should be called only once, since this class in a singleton.
     * 
     */
    public LogEventService() {
	if (LogEventService.instance == null) {
	    LogEventService.instance = this;
	}
    }   

    /**
     * Gets the only existing instance of the class.
     * 
     * @return instance of this class
     */
    public static LogEventService getInstance() {
	return LogEventService.instance;
    }   
    
    public void logEvent(Integer logEventTypeId, Integer userId, Long learningDesignId, Long lessonId, Long activityId) {
	User user = (userId != null) ? (User) userManagementService.findById(User.class, userId) : null;
	if (user == null) {
	    throw new RuntimeException("User can't be null");
	}
	LogEvent logEvent = new LogEvent();
	logEvent.setLogEventTypeId(logEventTypeId);
	logEvent.setUser(user);
	logEvent.setLearningDesignId(learningDesignId);
	logEvent.setLessonId(lessonId);
	logEvent.setActivityId(activityId);
	logEventDAO.save(logEvent);
    }

    public LogEvent getLogEventById(Long logEventId) {
	return logEventDAO.getById(logEventId);
    }  

    public List<LogEvent> getLogEventByUser(Integer userId) {
	return logEventDAO.getByUser(userId);
    }
    
    public List<LogEvent> getEventsOccurredBetween(Date startDate, Date finishDate) {
	return logEventDAO.getEventsOccurredBetween(startDate, finishDate);
    }
    
    /**
     * 
     * @param logEventDAO
     *            The logEventDAO to set.
     */
    public void setLogEventDAO(ILogEventDAO logEventDAO) {
	this.logEventDAO = logEventDAO;
    }
    
    /**
     * 
     * @param logEventDAO
     *            The logEventDAO to set.
     */
    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

}

