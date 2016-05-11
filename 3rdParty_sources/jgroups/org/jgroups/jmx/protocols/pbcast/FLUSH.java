package org.jgroups.jmx.protocols.pbcast;

import org.jgroups.jmx.Protocol;

/**
 * @author Vladimir Blagojevic
 *
 */
public class FLUSH extends Protocol implements FLUSHMBean {
    org.jgroups.protocols.pbcast.FLUSH p;

    public FLUSH(){}

    public FLUSH(org.jgroups.stack.Protocol p){
        super(p);
        this.p = (org.jgroups.protocols.pbcast.FLUSH) p;
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
        this.p = (org.jgroups.protocols.pbcast.FLUSH) p;
    }

    public double getAverageFlushDuration() {
        return p.getAverageFlushDuration();
    }

    public long getTotalTimeInFlush() {
        return p.getTotalTimeInFlush();
    }

    public int getNumberOfFlushes() {
        return p.getNumberOfFlushes();
    }

    public boolean startFlush() {
        return p.startFlush();
    }

    public void stopFlush() {
        p.stopFlush();
    }
}
