package org.jgroups.jmx.protocols;

import org.jgroups.jmx.Protocol;

/**
 * @author Bela Ban
 *
 */
public class UNICAST extends Protocol implements UNICASTMBean {
    org.jgroups.protocols.UNICAST p;

    public UNICAST() {
    }

    public UNICAST(org.jgroups.stack.Protocol p) {
        super(p);
        this.p=(org.jgroups.protocols.UNICAST)p;
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
        this.p=(org.jgroups.protocols.UNICAST)p;
    }

    public String getLocalAddress() {
        return p.getLocalAddress();
    }

    public String getMembers() {
        return p.getMembers();
    }


    public String printConnections() {
        return p.printConnections();
    }

    public long getMessagesSent() {
        return p.getNumMessagesSent();
    }

    public long getMessagesReceived() {
        return p.getNumMessagesReceived();
    }

    public long getBytesSent() {
        return p.getNumBytesSent();
    }

    public long getBytesReceived() {
        return p.getNumBytesReceived();
    }

    public long getAcksSent() {
        return p.getNumAcksSent();
    }

    public long getAcksReceived() {
        return p.getNumAcksReceived();
    }

    public long getXmitRequestsReceived() {
        return p.getNumberOfRetransmitRequestsReceived();
    }

    public int getNumUnackedMessages() {
        return p.getNumberOfUnackedMessages();
    }

    public String getUnackedMessages() {
        return p.getUnackedMessages();
}

    public int getNumberOfMessagesInReceiveWindows() {
        return p.getNumberOfMessagesInReceiveWindows();
    }

}
