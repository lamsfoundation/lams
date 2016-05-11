package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface SEQUENCERMBean extends ProtocolMBean {
    boolean isCoord();
    String getCoordinator();
    String getLocalAddress();
    long getForwarded();
    long getBroadcast();
    long getReceivedForwards();
    long getReceivedBroadcasts();

}
