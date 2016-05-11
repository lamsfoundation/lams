package org.jgroups.jmx.protocols.pbcast;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Vladimir Blagojevic
 *
 */
public interface STREAMING_STATE_TRANSFERMBean extends ProtocolMBean {
    int getNumberOfStateRequests();
    long getNumberOfStateBytesSent();
    double getAverageStateSize();   
}
