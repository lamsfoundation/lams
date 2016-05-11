package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface FRAG2MBean extends ProtocolMBean {
    int getFragSize();
    void setFragSize(int s);
    int getOverhead();
    void setOverhead(int o);
    long getNumberOfSentMessages();
    long getNumberOfSentFragments();
    long getNumberOfReceivedMessages();
    long getNumberOfReceivedFragments();
}
