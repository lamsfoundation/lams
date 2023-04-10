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

package org.lamsfoundation.lams.logevent.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.LogEventType;
import org.lamsfoundation.lams.logevent.dao.ILogEventDAO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Class implements <code>ILogEventService</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.timezone.service.ILogEventService
 */
public class LogEventService implements ILogEventService {

    private Logger log = Logger.getLogger(LogEventService.class);

    // I18N keys used look up the messages in the helper methods
    private final String AUDIT_CHANGE_I18N_KEY = "audit.change.entry";
    private final String AUDIT_MARK_CHANGE_I18N_KEY = "audit.change.mark";
    private final String AUDIT_HIDE_I18N_KEY = "audit.hide.entry";
    private final String AUDIT_SHOW_I18N_KEY = "audit.show.entry";
    private final String AUDIT_STARTED_EDITING_I18N_KEY = "audit.started.editing.activity";
    private final String AUDIT_FINISHED_EDITING_I18N_KEY = "audit.finished.editing.activity";
    private final String AUDIT_CANCELLED_EDITING_I18N_KEY = "audit.cancelled.editing.activity";

    /**
     * The only instance of this class, since it is a singleton.
     */
    private static LogEventService instance;

    private ILogEventDAO logEventDAO;
    private IUserManagementService userManagementService;
    private MessageService messageService;
    private ILessonDAO lessonDAO;

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

    @Override
    public void logEvent(Integer logEventTypeId, Integer userId, Integer targetUserId, Long lessonId, Long activityId,
	    String description) {
	logEvent(logEventTypeId, userId, targetUserId, lessonId, activityId, description, null);
    }

    @Override
    public void logEvent(Integer logEventTypeId, Integer userId, Integer targetUserId, Long lessonId, Long activityId,
	    String description, Date eventDate) {
	LogEvent logEvent = new LogEvent();
	logEvent.setLogEventTypeId(logEventTypeId);
	logEvent.setUserId(userId);
	logEvent.setTargetUserId(targetUserId);
	logEvent.setLessonId(lessonId);
	logEvent.setActivityId(activityId);
	logEvent.setDescription(description);
	if (eventDate != null) {
	    logEvent.setOccurredDateTime(eventDate);
	}
	logEventDAO.save(logEvent);
    }

    @Override
    public LogEvent getLogEventById(Long logEventId) {
	return logEventDAO.getById(logEventId);
    }

    @Override
    public List<LogEvent> getLogEventByUser(Integer userId) {
	return logEventDAO.getByUser(userId);
    }

    @Override
    public List<LogEvent> getEventsOccurredBetween(Date startDate, Date finishDate) {
	return logEventDAO.getEventsOccurredBetween(startDate, finishDate);
    }

    @Override
    public List<LogEventType> getEventTypes() {
	return logEventDAO.getEventTypes();
    }

    @Override
    public Date getOldestEventDate() {
	return logEventDAO.getOldestEventDate();
    }

    @Override
    public List<Object[]> getEventsForTablesorter(int page, int size, int sorting, String searchUser,
	    String searchTarget, String searchRemarks, Date startDate, Date endDate, String area, Integer typeId) {
	return logEventDAO.getEventsForTablesorter(page, size, sorting, searchUser, searchTarget, searchRemarks,
		startDate, endDate, area, typeId);
    }

    @Override
    public int countEventsWithRestrictions(String searchUser, String searchTarget, String searchRemarks, Date startDate,
	    Date endDate, String area, Integer typeId) {
	return logEventDAO.countEventsWithRestrictions(searchUser, searchTarget, searchRemarks, startDate, endDate,
		area, typeId);
    }

    // ********************** Helper methods used by tools to keep the audit entries consistent  *****************
    private Integer getCurrentUserId() {
	HttpSession ss = SessionManager.getSession();
	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null) {
		return user.getUserID();
	    }
	}
	return null;
    }

    private UserDTO getCurrentUser() {
	HttpSession ss = SessionManager.getSession();
	if (ss != null) {
	    return (UserDTO) ss.getAttribute(AttributeNames.USER);
	}
	return null;
    }

    @Override
    public void logChangeLearnerContent(Long learnerUserId, String learnerUserLogin, Long toolContentId,
	    String originalText, String newText) {
	Object[] args = new Object[5];
	args[0] = learnerUserLogin + " (" + learnerUserId + ")";
	args[1] = originalText;
	args[2] = newText;
	logLearnerChange(LogEvent.TYPE_LEARNER_CONTENT_UPDATED, learnerUserId, toolContentId, AUDIT_CHANGE_I18N_KEY,
		args);
    }

    @Override
    public void logMarkChange(Long learnerUserId, String learnerUserLogin, Long toolContentId, String originalMark,
	    String newMark) {
	Object[] args = new Object[5];
	args[0] = learnerUserLogin + " (" + learnerUserId + ")";
	args[1] = originalMark;
	args[2] = newMark;
	logLearnerChange(LogEvent.TYPE_MARK_UPDATED, learnerUserId, toolContentId, AUDIT_MARK_CHANGE_I18N_KEY, args);
    }

    @Override
    public void logHideLearnerContent(Long learnerUserId, String learnerUserLogin, Long toolContentId,
	    String hiddenItem) {
	Object[] args = new Object[4];
	args[0] = learnerUserLogin + "(" + learnerUserId + ")";
	args[1] = hiddenItem;
	logLearnerChange(LogEvent.TYPE_LEARNER_CONTENT_SHOW_HIDE, learnerUserId, toolContentId, AUDIT_HIDE_I18N_KEY,
		args);
    }

    @Override
    public void logShowLearnerContent(Long learnerUserId, String learnerUserLogin, Long toolContentId,
	    String hiddenItem) {
	Object[] args = new Object[4];
	args[0] = learnerUserLogin + " (" + learnerUserId + ")";
	args[1] = hiddenItem;
	logLearnerChange(LogEvent.TYPE_LEARNER_CONTENT_SHOW_HIDE, learnerUserId, toolContentId, AUDIT_SHOW_I18N_KEY,
		args);
    }

    @Override
    public void logToolEvent(int eventType, Long toolContentId, Long learnerUserId, String message) {
	Long[] ids = lessonDAO.getLessonActivityIdsForToolContentId(toolContentId);
	Long lessonId = ids[0];
	Long activityId = ids[1];
	Integer currentUserId = getCurrentUserId();

	logEvent(eventType, currentUserId, learnerUserId != null ? learnerUserId.intValue() : null, lessonId,
		activityId, message);
    }

    private void logLearnerChange(int eventType, Long learnerUserId, Long toolContentId, String messageKey,
	    Object[] args) {
	Long[] ids = lessonDAO.getLessonActivityIdsForToolContentId(toolContentId);
	Long lessonId = ids[0];
	Long activityId = ids[1];
	UserDTO currentUser = getCurrentUser();

	args[args.length - 1] = currentUser.getUserID();
	args[args.length - 2] = currentUser.getLogin();
	logEvent(eventType, currentUser.getUserID(), learnerUserId != null ? learnerUserId.intValue() : null, lessonId,
		activityId, messageService.getMessage(messageKey, args));
    }

    @Override
    // Use for unusual changes such as adding/removing file
    public void logChangeLearnerArbitraryChange(Long learnerUserId, String learnerUserLogin, Long toolContentId,
	    String message) {
	Long[] ids = lessonDAO.getLessonActivityIdsForToolContentId(toolContentId);
	Long lessonId = ids[0];
	Long activityId = ids[1];

	logEvent(LogEvent.TYPE_LEARNER_CONTENT_UPDATED, getCurrentUserId(),
		learnerUserId != null ? learnerUserId.intValue() : null, lessonId, activityId, message);
    }

    @Override
    public void logStartEditingActivityInMonitor(Long toolContentId) {
	logEditActivityInMonitor(toolContentId, AUDIT_STARTED_EDITING_I18N_KEY);
    }

    @Override
    public void logFinishEditingActivityInMonitor(Long toolContentId) {
	logEditActivityInMonitor(toolContentId, AUDIT_FINISHED_EDITING_I18N_KEY);
    }

    @Override
    public void logCancelEditingActivityInMonitor(Long toolContentId) {
	logEditActivityInMonitor(toolContentId, AUDIT_CANCELLED_EDITING_I18N_KEY);
    }

    private void logEditActivityInMonitor(Long toolContentId, String messageKey) {

	Long[] ids = lessonDAO.getLessonActivityIdsForToolContentId(toolContentId);
	Long lessonId = ids[0];
	Long activityId = ids[1];

	UserDTO user = getCurrentUser();
	String userString = user != null
		? new StringBuilder(user.getLogin()).append(" (").append(user.getUserID()).append(")").toString()
		: "";
	String activityString = new StringBuilder(" (").append(activityId).append(")").toString();
	String message = messageService.getMessage(messageKey, new Object[] { userString, activityString });
	logEvent(LogEvent.TYPE_ACTIVITY_EDIT, getCurrentUserId(), null, lessonId != null ? lessonId.longValue() : null,
		activityId != null ? activityId.longValue() : null, message.toString());
    }

    // ******************  End of tool helper methods ****************************************************************
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

    public void setLessonDAO(ILessonDAO lessonDAO) {
	this.lessonDAO = lessonDAO;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

}
