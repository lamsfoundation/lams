package org.jgroups.jmx.protocols;

import org.jgroups.jmx.Protocol;

/**
 * @author Bela Ban
 *
 */
public class FC extends Protocol implements FCMBean {
    org.jgroups.protocols.FC p;

    public FC() {
    }

    public FC(org.jgroups.stack.Protocol p) {
        super(p);
        this.p=(org.jgroups.protocols.FC)p;
    }

    public void attachProtocol(org.jgroups.stack.Protocol p) {
        super.attachProtocol(p);
        this.p=(org.jgroups.protocols.FC)p;
    }

    public long getMaxCredits() {
        return p.getMaxCredits();
    }

    public void setMaxCredits(long max_credits) {
        p.setMaxCredits(max_credits);
    }

    public double getMinThreshold() {
        return p.getMinThreshold();
    }

    public void setMinThreshold(double min_threshold) {
        p.setMinThreshold(min_threshold);
    }

    public long getMinCredits() {
        return p.getMinCredits();
    }

    public void setMinCredits(long min_credits) {
        p.setMinCredits(min_credits);
    }


    public int getBlockings() {
        return p.getNumberOfBlockings();
    }

    public long getTotalTimeBlocked() {
        return p.getTotalTimeBlocked();
    }

    public long getMaxBlockTime() {
        return p.getMaxBlockTime();
    }

    public void setMaxBlockTime(long t) {
        p.setMaxBlockTime(t);
    }

    public double getAverageTimeBlocked() {
        return p.getAverageTimeBlocked();
    }

    public int getCreditRequestsReceived() {
        return p.getNumberOfCreditRequestsReceived();
    }

    public int getCreditRequestsSent() {
        return p.getNumberOfCreditRequestsSent();
    }

    public int getCreditResponsesReceived() {
        return p.getNumberOfCreditResponsesReceived();
    }

    public int getCreditResponsesSent() {
        return p.getNumberOfCreditResponsesSent();
    }


    public String printSenderCredits() {
        return p.printSenderCredits();
    }

    public String printReceiverCredits() {
        return p.printReceiverCredits();
    }

    public String printCredits() {
        return p.printCredits();
    }

    public String showLastBlockingTimes() {
        return p.showLastBlockingTimes();
    }

    public void unblock() {
        p.unblock();
    }
}
