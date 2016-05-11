package org.jgroups.jmx.protocols;

import org.jgroups.stack.Protocol;

/**
 * @author Bela Ban
 *
 */
public class UDP extends org.jgroups.jmx.protocols.TP implements UDPMBean {
    org.jgroups.protocols.UDP udp;

    public UDP() {
    }

    public UDP(Protocol p) {
        super(p);
        udp=(org.jgroups.protocols.UDP)p;
    }

    public void attachProtocol(Protocol p) {
        super.attachProtocol(p);
        udp=(org.jgroups.protocols.UDP)p;
    }
}
