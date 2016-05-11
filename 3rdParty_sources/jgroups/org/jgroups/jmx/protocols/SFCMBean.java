package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface SFCMBean extends ProtocolMBean {
    long getMaxCredits();
    long getCredits();
    long getBytesSent();
    long getBlockings();
    long getCreditRequestsReceived();
    long getCreditRequestsSent();
    long getReplenishmentsReceived();
    long getReplenishmentsSent();
    long getTotalBlockingTime();
    double getAverageBlockingTime();
    String printBlockingTimes();
    String printReceived();
    String printPendingCreditors();
    String printPendingRequesters();
    void unblock();
}
