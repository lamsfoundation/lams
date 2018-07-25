package org.lamsfoundation.lams.events.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.events.Event;
import org.lamsfoundation.lams.events.Subscription;
import org.lamsfoundation.lams.usermanagement.User;

public interface EventDAO extends IBaseDAO {
    /**
     * Gets an instance of the event.
     *
     * @param scope
     * @param name
     * @param eventSessionId
     * @return
     */
    Event getEvent(String scope, String name, Long sessionId);

    /**
     * Gets events with messages that need to be resend. Either they failed to be send or they should be repeated.
     *
     * @return list of events
     */
    List<Event> getEventsToResend();

    List<Subscription> getLessonEventSubscriptions(Long lessonId, Integer userId, boolean pendingOnly, Integer limit,
	    Integer offset);

    long getPendingNotificationCount(Long lessonId, Integer userId);

    /**
     * Gets users to whom the given email notification was sent. Uses paging.
     */
    List<User> getArchivedEmailNotificationRecipients(Long emailNotificationUid, Integer limit, Integer offset);
}