package org.jgroups.jmx.protocols.pbcast;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface GMSMBean extends ProtocolMBean {
    String getView();
    String getLocalAddress();
    String getMembers();
    int getNumMembers();
    boolean isCoordinator();
    int getNumberOfViews();
    long getJoinTimeout();
    void setJoinTimeout(long t);
    long getJoinRetryTimeout();
    void setJoinRetryTimeout(long t);
    boolean isShun();
    void setShun(boolean s);
    String printPreviousMembers();
    String printPreviousViews();
    int getViewHandlerQueue();
    boolean isViewHandlerSuspended();
    String dumpViewHandlerQueue();
    String dumpHistory();
    void suspendViewHandler();
    void resumeViewHandler();
}
