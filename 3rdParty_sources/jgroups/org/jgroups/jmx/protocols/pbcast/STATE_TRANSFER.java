package org.jgroups.jmx.protocols.pbcast;

import org.jgroups.jmx.Protocol;

/**
 * @author Bela Ban
 *
 */
public class STATE_TRANSFER extends Protocol implements STATE_TRANSFERMBean {
    org.jgroups.protocols.pbcast.STATE_TRANSFER p;

    public STATE_TRANSFER() {
    }

    public STATE_TRANSFER(org.jgroups.stack.Protocol p) {
        super(p);
        this.p=(org.jgroups.protocols.pbcast.STATE_TRANSFER)p;
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
        this.p=(org.jgroups.protocols.pbcast.STATE_TRANSFER)p;
    }

    public int getNumberOfStateRequests() {
        return p.getNumberOfStateRequests();
    }

    public long getNumberOfStateBytesSent() {
        return p.getNumberOfStateBytesSent();
    }

    public double getAverageStateSize() {
        return p.getAverageStateSize();
    }
}
