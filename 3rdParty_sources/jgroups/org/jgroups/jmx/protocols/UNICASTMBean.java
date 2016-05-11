package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface UNICASTMBean extends ProtocolMBean {
    String getLocalAddress();
    String getMembers();
    String printConnections();
    long getMessagesSent();
    long getMessagesReceived();
    long getBytesSent();
    long getBytesReceived();
    long getAcksSent();
    long getAcksReceived();
    long getXmitRequestsReceived();
    int getNumUnackedMessages();
    String getUnackedMessages();
    int getNumberOfMessagesInReceiveWindows();
}
