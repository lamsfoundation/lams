package org.jgroups.jmx.protocols;

import org.jgroups.jmx.ProtocolMBean;

import java.net.InetAddress;
import java.util.List;

/**
 * @author Bela Ban
 *
 */
public interface MPINGMBean extends PINGMBean {
    InetAddress getBindAddr();
    void setBindAddr(InetAddress bind_addr);
    boolean isReceiveOnAllInterfaces();
    List getReceiveInterfaces();
    boolean isSendOnAllInterfaces();
    List getSendInterfaces();
    int getTTL();
    void setTTL(int ip_ttl);
    InetAddress getMcastAddr();
    void setMcastAddr(InetAddress mcast_addr);
    int getMcastPort();
    void setMcastPort(int mcast_port);
}
