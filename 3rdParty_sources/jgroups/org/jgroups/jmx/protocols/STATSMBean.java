package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface STATSMBean extends ProtocolMBean {
    long getSentMessages();
    long getSentBytes();
    long getSentUnicastMessages();
    long getSentUnicastBytes();
    long getSentMcastMessages();
    long getSentMcastBytes();
    long getReceivedMessages();
    long getReceivedBytes();
    long getReceivedUnicastMessages();
    long getReceivedUnicastBytes();
    long getReceivedMcastMessages();
    long getReceivedMcastBytes();
    String printStats();
}
