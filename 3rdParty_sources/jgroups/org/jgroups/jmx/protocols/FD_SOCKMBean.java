package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

import java.util.Enumeration;
import java.util.Date;

/**
 * @author Bela Ban
 *
 */
public interface FD_SOCKMBean extends ProtocolMBean {
    String getLocalAddress();
    String getMembers();
    String getPingableMembers();
    String getPingDest();
    int getNumSuspectEventsGenerated();
    String printSuspectHistory();
    String printCache();
}
