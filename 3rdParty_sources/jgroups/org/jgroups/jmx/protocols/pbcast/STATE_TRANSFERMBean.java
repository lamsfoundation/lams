package org.jgroups.jmx.protocols.pbcast;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 * @version $Id$
 */
public interface STATE_TRANSFERMBean extends ProtocolMBean {
    int getNumberOfStateRequests();
    long getNumberOfStateBytesSent();
    double getAverageStateSize();
}
