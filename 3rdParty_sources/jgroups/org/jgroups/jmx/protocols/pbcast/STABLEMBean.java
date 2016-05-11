package org.jgroups.jmx.protocols.pbcast;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface STABLEMBean extends ProtocolMBean {
    long getDesiredAverageGossip();
    void setDesiredAverageGossip(long gossip_interval);
    long getMaxBytes();
    void setMaxBytes(long max_bytes);
    long getBytes();
    int getStableSent();
    int getStableReceived();
    int getStabilitySent();
    int getStabilityReceived();
    void runMessageGarbageCollection();
}
