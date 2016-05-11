package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface FRAGMBean extends ProtocolMBean {
    int getFragSize();
    void setFragSize(int s);
    long getNumberOfSentMessages();
    long getNumberOfSentFragments();
    long getNumberOfReceivedMessages();
    long getNumberOfReceivedFragments();
}
