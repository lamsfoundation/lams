package org.jgroups.jmx.protocols;

import org.jgroups.jmx.Protocol;

/**
 * @author Bela Ban
 *
 */
public class MERGE2 extends Protocol implements MERGE2MBean {
    org.jgroups.protocols.MERGE2 p;

    public MERGE2() {
    }

    public MERGE2(org.jgroups.stack.Protocol p) {
        super(p);
        this.p=(org.jgroups.protocols.MERGE2)p;
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
        this.p=(org.jgroups.protocols.MERGE2)p;
    }

    public long getMinInterval() {
        return p.getMinInterval();
    }

    public void setMinInterval(long i) {
        p.setMinInterval(i);
    }

    public long getMaxInterval() {
        return p.getMaxInterval();
    }

    public void setMaxInterval(long l) {
        p.setMaxInterval(l);
    }
}
