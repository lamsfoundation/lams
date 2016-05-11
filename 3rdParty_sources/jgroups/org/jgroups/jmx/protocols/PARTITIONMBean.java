package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

import java.net.InetAddress;
import java.util.List;

/**
 * @author Bela Ban
 *
 */
public interface PARTITIONMBean extends ProtocolMBean {
    boolean isPartitionOn();
    void startPartition();
    void stopPartition();
}