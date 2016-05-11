package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

import java.util.Vector;

/**
 * @author Bela Ban
 *
 */
public interface DiscoveryMBean extends ProtocolMBean {
    long getTimeout();
    void setTimeout(long timeout);
    int getInitialMembers();
    void setInitialMembers(int num_initial_members);
    int getPingRequests();
    void setPingRequests(int num_ping_requests);
    int getDiscoveryRequestsSent();
    Vector findInitialMembers();
    String findInitialMembersAsString();
}
