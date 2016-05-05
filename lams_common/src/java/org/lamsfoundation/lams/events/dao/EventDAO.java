package org.lamsfoundation.lams.events.dao;

import java.util.List;

import org.lamsfoundation.lams.events.Event;

public interface EventDAO {
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
     * Gets events with messages that need to be resend.
     * Either they failed to be send or they should be repeated.
     * 
     * @return list of events
     */
    List<Event> getEventsToResend();

    void deleteEvent(Event event);

    void saveEvent(Event event);
}