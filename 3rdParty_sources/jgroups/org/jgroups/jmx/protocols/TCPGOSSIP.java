package org.jgroups.jmx.protocols;

import org.jgroups.jmx.Protocol;

/**
 * @author Bela Ban
 *
 */
public class TCPGOSSIP extends Discovery implements TCPGOSSIPMBean {

    public TCPGOSSIP() {
    }

    public TCPGOSSIP(org.jgroups.stack.Protocol p) {
        super(p);
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
    }
}
