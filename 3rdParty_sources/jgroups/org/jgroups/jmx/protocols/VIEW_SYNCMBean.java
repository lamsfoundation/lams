package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface VIEW_SYNCMBean extends ProtocolMBean {
    long getAverageSendInterval();
    void setAverageSendInterval(long send_interval);
    int getNumViewsSent();
    int getNumViewRequestsSent();
    int getNumViewResponsesSeen();
    int getNumViewsNonLocal();
    int getNumViewsEqual();
    int getNumViewsLess();
    long getLastViewRequestSent();
    int getNumViewsAdjusted();
    void sendViewRequest();
    // void sendFakeViewForTestingOnly();
}
