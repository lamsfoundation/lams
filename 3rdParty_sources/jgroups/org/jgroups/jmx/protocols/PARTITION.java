package org.jgroups.jmx.protocols;

import org.jgroups.jmx.Protocol;

/**
 * @author Bela Ban
 *
 */
public class PARTITION extends Protocol implements PARTITIONMBean {
    org.jgroups.protocols.PARTITION partiton;

    public PARTITION() {
    }

    public PARTITION(org.jgroups.stack.Protocol p) {
        super(p);
        this.partiton=(org.jgroups.protocols.PARTITION)p;
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
        this.partiton=(org.jgroups.protocols.PARTITION)p;
    }


    public boolean isPartitionOn() {
        return partiton.isPartitionOn();
    }

    public void startPartition() {
        partiton.startPartition();
    }

    public void stopPartition() {
        partiton.stopPartition();
    }
}