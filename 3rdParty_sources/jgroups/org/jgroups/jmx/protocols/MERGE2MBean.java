package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

/**
 * @author Bela Ban
 *
 */
public interface MERGE2MBean extends ProtocolMBean {
    long getMinInterval();
    void setMinInterval(long i);
    long getMaxInterval();
    void setMaxInterval(long l);
}
