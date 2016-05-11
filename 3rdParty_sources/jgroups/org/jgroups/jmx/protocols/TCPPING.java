package org.jgroups.jmx.protocols;

/**
 * @author Bela Ban
 *
 */
public class TCPPING extends Discovery implements TCPPINGMBean {

    public TCPPING() {
    }

    public TCPPING(org.jgroups.stack.Protocol p) {
        super(p);
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
    }
}
