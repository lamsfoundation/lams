package org.jgroups.jmx.protocols;

import org.jgroups.jmx.Protocol;

/**
 * @author Bela Ban
 *
 */
public class FD_SOCK extends Protocol implements FD_SOCKMBean {
    org.jgroups.protocols.FD_SOCK p;

    public FD_SOCK() {
    }

    public FD_SOCK(org.jgroups.stack.Protocol p) {
        super(p);
        this.p=(org.jgroups.protocols.FD_SOCK)p;
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
        this.p=(org.jgroups.protocols.FD_SOCK)p;
    }


    public String getLocalAddress() {
        return p.getLocalAddress();
    }

    public String getMembers() {
        return p.getMembers();
    }

    public String getPingableMembers() {
        return p.getPingableMembers();
    }

    public String getPingDest() {
        return p.getPingDest();
    }

    public int getNumSuspectEventsGenerated() {
        return p.getNumSuspectEventsGenerated();
    }

    public String printSuspectHistory() {
        return p.printSuspectHistory();
    }

    public String printCache() {
        return p.printCache();
    }
}
