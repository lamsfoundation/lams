package org.lamsfoundation.lams.events.dao.hibernate;

import java.security.InvalidParameterException;
import java.util.List;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.events.DeliveryMethodNotification;
import org.lamsfoundation.lams.events.Event;
import org.lamsfoundation.lams.events.Subscription;
import org.lamsfoundation.lams.events.dao.EventDAO;
import org.springframework.stereotype.Repository;

@Repository
class EventDAOHibernate extends LAMSBaseDAO implements EventDAO {

    private static final String GET_EVENT_QUERY = "FROM " + Event.class.getName()
	    + " AS e WHERE e.scope=? AND e.name=? AND e.eventSessionId=? AND e.failTime IS NULL";

    private static final String GET_EVENTS_TO_RESEND_QUERY = "SELECT DISTINCT e FROM " + Event.class.getName()
	    + " AS e WHERE e.failTime IS NOT NULL";

    private static final String GET_LESSON_EVENT_SUBSCRIPTIONS = "FROM " + Subscription.class.getName()
	    + " AS s WHERE s.userId = ? AND s.event.scope LIKE 'LESSON_%'";

    private static final String COUNT_PENDING_NOTIFICATIONS = "SELECT COUNT(*) FROM " + Subscription.class.getName()
	    + " AS s WHERE (s.lastOperationMessage IS NULL OR s.lastOperationMessage != '"
	    + DeliveryMethodNotification.LAST_OPERATION_SEEN + "') AND s.userId = ? AND s.event.scope LIKE 'LESSON_%'";

    @Override
    @SuppressWarnings("unchecked")
    public Event getEvent(String scope, String name, Long sessionId) throws InvalidParameterException {
	List<Event> events = (List<Event>) doFind(EventDAOHibernate.GET_EVENT_QUERY,
		new Object[] { scope, name, sessionId });
	if (events.size() > 1) {
	    throw new InvalidParameterException("Two events with the same parameters exist in the database.");
	}
	if (events.size() == 0) {
	    return null;
	}
	return events.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Event> getEventsToResend() {
	return (List<Event>) doFind(EventDAOHibernate.GET_EVENTS_TO_RESEND_QUERY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Subscription> getLessonEventSubscriptions(Long lessonId, Integer userId, boolean pendingOnly,
	    Integer limit, Integer offset) {
	String query = EventDAOHibernate.GET_LESSON_EVENT_SUBSCRIPTIONS;
	if (lessonId != null) {
	    query += " AND s.event.eventSessionId = ?";
	}
	if (pendingOnly) {
	    query += " AND (s.lastOperationMessage IS NULL OR s.lastOperationMessage != '"
		    + DeliveryMethodNotification.LAST_OPERATION_SEEN + "')";
	}
	query += " ORDER BY ISNULL(s.lastOperationMessage) DESC, uid DESC";
	Query queryObject = getSession().createQuery(query);
	queryObject.setInteger(0, userId);
	if (lessonId != null) {
	    queryObject.setLong(1, lessonId);
	}
	if (limit != null) {
	    queryObject.setMaxResults(limit);
	}
	if (offset != null) {
	    queryObject.setFirstResult(offset);
	}
	return queryObject.list();
    }

    @Override
    public long getPendingNotificationCount(Long lessonId, Integer userId) {
	String query = EventDAOHibernate.COUNT_PENDING_NOTIFICATIONS;
	if (lessonId != null) {
	    query += " AND s.event.eventSessionId = ?";
	}
	Query queryObject = getSession().createQuery(query);
	queryObject.setInteger(0, userId);
	if (lessonId != null) {
	    queryObject.setLong(1, lessonId);
	}
	return (Long) queryObject.uniqueResult();
    }
}