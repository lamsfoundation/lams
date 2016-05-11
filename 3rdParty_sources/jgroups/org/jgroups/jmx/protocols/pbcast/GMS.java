package org.jgroups.jmx.protocols.pbcast;

import org.jgroups.jmx.Protocol;

/**
 * @author Bela Ban
 *
 */
public class GMS extends Protocol implements GMSMBean {
    org.jgroups.protocols.pbcast.GMS p;

    public GMS() {
    }

    public GMS(org.jgroups.stack.Protocol p) {
        super(p);
        this.p=(org.jgroups.protocols.pbcast.GMS)p;
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
        this.p=(org.jgroups.protocols.pbcast.GMS)p;
    }

    public String getView() {
        return p.getView();
    }

    public String getLocalAddress() {
        return p.getLocalAddress();
    }

    public String getMembers() {
        return p.getMembers();
    }

    public int getNumMembers() {
        return p.getNumMembers();
    }

    public boolean isCoordinator() {
        return p.isCoordinator();
    }

    public int getNumberOfViews() {
        return p.getNumberOfViews();
    }

    public long getJoinTimeout() {
        return p.getJoinTimeout();
    }

    public void setJoinTimeout(long t) {
        p.setJoinTimeout(t);
    }

    public long getJoinRetryTimeout() {
        return p.getJoinRetryTimeout();
    }

    public void setJoinRetryTimeout(long t) {
        p.setJoinRetryTimeout(t);
    }

    public boolean isShun() {
        return p.isShun();
    }

    public void setShun(boolean s) {
        p.setShun(s);
    }

    public String printPreviousMembers() {
        return p.printPreviousMembers();
    }

    public String printPreviousViews() {
        return p.printPreviousViews();
    }

    public int getViewHandlerQueue() {
        return p.viewHandlerSize();
    }

    public boolean isViewHandlerSuspended() {
        return p.isViewHandlerSuspended();
    }

    public String dumpViewHandlerQueue() {
        return p.dumpViewHandlerQueue();
    }

    public String dumpHistory() {
        return p.dumpViewHandlerHistory();
    }

    public void suspendViewHandler() {
        p.suspendViewHandler();
    }

    public void resumeViewHandler() {
        p.resumeViewHandler();
    }

}
