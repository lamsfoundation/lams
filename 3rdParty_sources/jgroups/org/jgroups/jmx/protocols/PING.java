package org.jgroups.jmx.protocols;

import org.jgroups.jmx.Protocol;

/**
 * @author Bela Ban
 *
 */
public class PING extends Discovery implements PINGMBean {

    public PING() {
    }

    public PING(org.jgroups.stack.Protocol p) {
        super(p);
        this.p=(org.jgroups.protocols.PING)p;
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
        this.p=(org.jgroups.protocols.PING)p;
    }
}
