package org.lamsfoundation.lams.events.dao.hibernate;

import java.security.InvalidParameterException;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.events.Event;
import org.lamsfoundation.lams.events.dao.EventDAO;
import org.springframework.stereotype.Repository;

@Repository
class EventDAOHibernate extends LAMSBaseDAO implements EventDAO {

    private static final String GET_EVENT_QUERY = "FROM " + Event.class.getName()
	    + " AS e WHERE e.scope=? AND e.name=? AND e.eventSessionId=? AND e.failTime IS NULL";

    private static final String GET_EVENTS_TO_RESEND_QUERY = "SELECT DISTINCT e FROM " + Event.class.getName()
	    + " AS e WHERE e.failTime IS NOT NULL";

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

    @SuppressWarnings("unchecked")
    public List<Event> getEventsToResend() {
	return (List<Event>) doFind(EventDAOHibernate.GET_EVENTS_TO_RESEND_QUERY);
    }

    public void deleteEvent(Event event) {
	getSession().delete(event);
    }

    public void saveEvent(Event event) {
	getSession().saveOrUpdate(event);
    }
}