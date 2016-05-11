
package org.jgroups.protocols;


import org.jgroups.*;
import org.jgroups.annotations.GuardedBy;
import org.jgroups.stack.Protocol;
import org.jgroups.util.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Failure detection based on simple heartbeat protocol. Regularly polls members for
 * liveness. Multicasts SUSPECT messages when a member is not reachable. The simple
 * algorithms works as follows: the membership is known and ordered. Each HB protocol
 * periodically sends an 'are-you-alive' message to its *neighbor*. A neighbor is the next in
 * rank in the membership list, which is recomputed upon a view change. When a response hasn't
 * been received for n milliseconds and m tries, the corresponding member is suspected (and
 * eventually excluded if faulty).<p>
 * FD starts when it detects (in a view change notification) that there are at least
 * 2 members in the group. It stops running when the membership drops below 2.<p>
 * When a message is received from the monitored neighbor member, it causes the pinger thread to
 * 'skip' sending the next are-you-alive message. Thus, traffic is reduced.<p>
 * When we receive a ping from a member that's not in the membership list, we shun it by sending it a
 * NOT_MEMBER message. That member will then leave the group (and possibly rejoin). This is only done if
 * <code>shun</code> is true.
 * @author Bela Ban
 *
 */
public class FD extends Protocol {
    Address               local_addr=null;
    long                  timeout=3000;  // number of millisecs to wait for an are-you-alive msg
    long                  last_ack=System.currentTimeMillis();
    int                   num_tries=0;
    int                   max_tries=2;   // number of times to send a are-you-alive msg (tot time= max_tries*timeout)

    protected final Lock  lock=new ReentrantLock();

    @GuardedBy("lock")
    Address               ping_dest=null;

    @GuardedBy("lock")
    final List<Address>   members=new ArrayList<Address>();

    /** Members from which we select ping_dest. may be subset of {@link #members} */
    @GuardedBy("lock")
    final List<Address>   pingable_mbrs=new ArrayList<Address>();

    // number of pings from suspected mbrs
    @GuardedBy("lock")
    final Map<Address,Integer>  invalid_pingers=new HashMap<Address,Integer>(7);

    boolean               shun=true;
    TimeScheduler         timer=null;

    @GuardedBy("lock")
    private Future        monitor_future=null;  // task that performs the actual monitoring for failure detection

    protected int         num_heartbeats=0;
    protected int         num_suspect_events=0;

    /** Transmits SUSPECT message until view change or UNSUSPECT is received */
    protected final Broadcaster  bcast_task=new Broadcaster();
    final static String   name="FD";

    final BoundedList<Address>   suspect_history=new BoundedList<Address>(20);





    public String getName() {return name;}
    public String getLocalAddress() {return local_addr != null? local_addr.toString() : "null";}
    public String getMembers() {return members != null? members.toString() : "null";}
    public String getPingableMembers() {return pingable_mbrs != null? pingable_mbrs.toString() : "null";}
    public String getPingDest() {return ping_dest != null? ping_dest.toString() : "null";}
    public int getNumberOfHeartbeatsSent() {return num_heartbeats;}
    public int getNumSuspectEventsGenerated() {return num_suspect_events;}
    public long getTimeout() {return timeout;}
    public void setTimeout(long timeout) {this.timeout=timeout;}
    public int getMaxTries() {return max_tries;}
    public void setMaxTries(int max_tries) {this.max_tries=max_tries;}
    public int getCurrentNumTries() {return num_tries;}
    public boolean isShun() {return shun;}
    public void setShun(boolean flag) {this.shun=flag;}
    public String printSuspectHistory() {
        StringBuilder sb=new StringBuilder();
        for(Address addr: suspect_history) {
            sb.append(new Date()).append(": ").append(addr).append("\n");
        }
        return sb.toString();
    }


    public boolean setProperties(Properties props) {
        String str;

        super.setProperties(props);
        str=props.getProperty("timeout");
        if(str != null) {
            timeout=Long.parseLong(str);
            props.remove("timeout");
        }

        str=props.getProperty("max_tries");  // before suspecting a member
        if(str != null) {
            max_tries=Integer.parseInt(str);
            props.remove("max_tries");
        }

        str=props.getProperty("shun");
        if(str != null) {
            shun=Boolean.valueOf(str).booleanValue();
            props.remove("shun");
        }

        if(!props.isEmpty()) {
            log.error("the following properties are not recognized: " + props);
            return false;
        }
        return true;
    }

    public void resetStats() {
        num_heartbeats=num_suspect_events=0;
        suspect_history.clear();
    }


    public void init() throws Exception {
        timer=getTransport().getTimer();
        if(timer == null)
            throw new Exception("timer cannot be retrieved from protocol stack");
    }


    public void stop() {
        lock.lock();
        try {
            stopMonitor();
        }
        finally {
            lock.unlock();
        }
    }


    private Address getPingDest(List<Address> mbrs) {
        Address tmp, retval=null;

        if(mbrs == null || mbrs.size() < 2 || local_addr == null)
            return null;
        for(int i=0; i < mbrs.size(); i++) {
            tmp=mbrs.get(i);
            if(local_addr.equals(tmp)) {
                if(i + 1 >= mbrs.size())
                    retval=mbrs.get(0);
                else
                    retval=mbrs.get(i + 1);
                break;
            }
        }
        return retval;
    }

    /** Requires lock to held by caller */
    @GuardedBy("lock")
    private void startMonitor() {
        if(monitor_future == null || monitor_future.isDone()) {
            last_ack=System.currentTimeMillis();  // start from scratch
            monitor_future=timer.scheduleWithFixedDelay(new Monitor(), timeout, timeout, TimeUnit.MILLISECONDS);
            num_tries=0;
        }
    }

    /** Requires lock to be held by caller */
    @GuardedBy("lock")
    private void stopMonitor() {
        if(monitor_future != null) {
            monitor_future.cancel(true);
            monitor_future=null;
        }
    }

    /** Restarts the monitor if the ping destination has changed. If not, this is a no-op.
     * Requires lock to be held by the caller */
    @GuardedBy("lock")
    private void restartMonitor() {
        Address tmp_dest=getPingDest(pingable_mbrs);
        boolean restart_monitor=tmp_dest == null ||
                ping_dest == null || // tmp_dest != null && ping_dest == null
                !ping_dest.equals(tmp_dest); // tmp_dest != null && ping_dest != null

        if(restart_monitor) {
            ping_dest=tmp_dest;
            stopMonitor();
            if(ping_dest != null) {
                try {
                    startMonitor();
                }
                catch(Exception ex) {
                    if(log.isWarnEnabled()) log.warn("exception when calling startMonitor(): " + ex);
                }
            }
        }
    }


    public Object up(Event evt) {
        switch(evt.getType()) {

            case Event.SET_LOCAL_ADDRESS:
                local_addr=(Address)evt.getArg();
                break;

            case Event.MSG:
                Message msg=(Message)evt.getArg();
                FdHeader hdr=(FdHeader)msg.getHeader(name);
                if(hdr == null) {
                    updateTimestamp(msg.getSrc());
                    break;  // message did not originate from FD layer, just pass up
                }

                switch(hdr.type) {
                    case FdHeader.HEARTBEAT:                       // heartbeat request; send heartbeat ack
                        Address hb_sender=msg.getSrc();
                        if(log.isTraceEnabled())
                            log.trace("received are-you-alive from " + hb_sender + ", sending response");
                        sendHeartbeatResponse(hb_sender);

                        // 2. Shun the sender of a HEARTBEAT message if that sender is not a member. This will cause
                        //    the sender to leave the group (and possibly rejoin it later)
                        if(shun)
                            shunInvalidHeartbeatSender(hb_sender);
                        break;                                     // don't pass up !

                    case FdHeader.HEARTBEAT_ACK:                   // heartbeat ack
                        updateTimestamp(hdr.from);
                        break;

                    case FdHeader.SUSPECT:
                        if(hdr.mbrs != null) {
                            if(log.isTraceEnabled()) log.trace("[SUSPECT] suspect hdr is " + hdr);
                            for(int i=0; i < hdr.mbrs.size(); i++) {
                                Address m=hdr.mbrs.elementAt(i);
                                if(local_addr != null && m.equals(local_addr)) {
                                    if(log.isWarnEnabled())
                                        log.warn("I was suspected by " + msg.getSrc() + "; ignoring the SUSPECT " +
                                                "message and sending back a HEARTBEAT_ACK");
                                    sendHeartbeatResponse(msg.getSrc());
                                    continue;
                                }
                                else {
                                    lock.lock();
                                    try {
                                        pingable_mbrs.remove(m);
                                        restartMonitor();
                                    }
                                    finally {
                                        lock.unlock();
                                    }
                                }
                                up_prot.up(new Event(Event.SUSPECT, m));
                                down_prot.down(new Event(Event.SUSPECT, m));
                            }
                        }
                        break;

                    case FdHeader.NOT_MEMBER:
                        if(shun) {
                            if(log.isDebugEnabled()) log.debug("[NOT_MEMBER] I'm being shunned; exiting");
                            up_prot.up(new Event(Event.EXIT));
                        }
                        break;
                }
                return null;
        }
        return up_prot.up(evt); // pass up to the layer above us
    }





    public Object down(Event evt) {
        switch(evt.getType()) {
            case Event.VIEW_CHANGE:
                down_prot.down(evt);

                lock.lock();
                try {
                    View v=(View)evt.getArg();
                    members.clear();
                    members.addAll(v.getMembers());
                    bcast_task.adjustSuspectedMembers(members);
                    pingable_mbrs.clear();
                    pingable_mbrs.addAll(members);
                    restartMonitor();
                }
                finally {
                    lock.unlock();
                }
                return null;

            case Event.UNSUSPECT:
                unsuspect((Address)evt.getArg());
                return down_prot.down(evt);
        }
        return down_prot.down(evt);
    }


    private void sendHeartbeatResponse(Address dest) {
        Message hb_ack=new Message(dest, null, null);
        hb_ack.setFlag(Message.OOB);
        FdHeader tmp_hdr=new FdHeader(FdHeader.HEARTBEAT_ACK);
        tmp_hdr.from=local_addr;
        hb_ack.putHeader(name, tmp_hdr);
        down_prot.down(new Event(Event.MSG, hb_ack));
    }

    @GuardedBy("lock")
    private void unsuspect(Address mbr) {
        lock.lock();
        try {
            bcast_task.removeSuspectedMember(mbr);
            pingable_mbrs.clear();
            pingable_mbrs.addAll(members);
            pingable_mbrs.removeAll(bcast_task.getSuspectedMembers());
            restartMonitor();
        }
        finally {
            lock.unlock();
        }
    }

    @GuardedBy("lock")
    private void updateTimestamp(Address sender) {
        lock.lock();
        try {
            if(ping_dest != null && sender != null && ping_dest.equals(sender)) {
                last_ack=System.currentTimeMillis();
                if(log.isTraceEnabled())
                    log.trace("received msg from " + sender + " (counts as ack)");
                num_tries=0;
            }
        }
        finally {
            lock.unlock();
        }

    }


    /**
     * If sender is not a member, send a NOT_MEMBER to sender (after n pings received)
     */
    private void shunInvalidHeartbeatSender(Address hb_sender) {
        int num_pings=0;
        Message shun_msg=null;

        lock.lock();
        try {
            if(hb_sender != null && members != null && !members.contains(hb_sender)) {
                if(invalid_pingers.containsKey(hb_sender)) {
                    num_pings=invalid_pingers.get(hb_sender).intValue();
                    if(num_pings >= max_tries) {
                        if(log.isDebugEnabled())
                            log.debug(hb_sender + " is not in " + members + " ! Shunning it");
                        shun_msg=new Message(hb_sender, null, null);
                        shun_msg.setFlag(Message.OOB);
                        shun_msg.putHeader(name, new FdHeader(FdHeader.NOT_MEMBER));
                        invalid_pingers.remove(hb_sender);
                    }
                    else {
                        num_pings++;
                        invalid_pingers.put(hb_sender, new Integer(num_pings));
                    }
                }
                else {
                    num_pings++;
                    invalid_pingers.put(hb_sender, new Integer(num_pings));
                }
            }
        }
        finally {
            lock.unlock();
        }

        if(shun_msg != null)
            down_prot.down(new Event(Event.MSG, shun_msg));
    }


    public static class FdHeader extends Header implements Streamable {
        public static final byte HEARTBEAT=0;
        public static final byte HEARTBEAT_ACK=1;
        public static final byte SUSPECT=2;
        public static final byte NOT_MEMBER=3;  // received as response by pinged mbr when we are not a member


        byte    type=HEARTBEAT;
        Vector<Address>  mbrs=null;
        Address from=null;  // member who detected that suspected_mbr has failed
        private static final long serialVersionUID=-6387039473828820899L;


        public FdHeader() {
        } // used for externalization

        public FdHeader(byte type) {
            this.type=type;
        }

        public FdHeader(byte type, Vector<Address> mbrs, Address from) {
            this(type);
            this.mbrs=mbrs;
            this.from=from;
        }


        public String toString() {
            switch(type) {
                case HEARTBEAT:
                    return "heartbeat";
                case HEARTBEAT_ACK:
                    return "heartbeat ack";
                case SUSPECT:
                    return "SUSPECT (suspected_mbrs=" + mbrs + ", from=" + from + ")";
                case NOT_MEMBER:
                    return "NOT_MEMBER";
                default:
                    return "unknown type (" + type + ")";
            }
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(type);
            if(mbrs == null)
                out.writeBoolean(false);
            else {
                out.writeBoolean(true);
                out.writeInt(mbrs.size());
                for(Iterator it=mbrs.iterator(); it.hasNext();) {
                    Address addr=(Address)it.next();
                    Marshaller.write(addr, out);
                }
            }
            Marshaller.write(from, out);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            type=in.readByte();
            boolean mbrs_not_null=in.readBoolean();
            if(mbrs_not_null) {
                int len=in.readInt();
                mbrs=new Vector<Address>(11);
                for(int i=0; i < len; i++) {
                    Address addr=(Address)Marshaller.read(in);
                    mbrs.add(addr);
                }
            }
            from=(Address)Marshaller.read(in);
        }


        public int size() {
            int retval=Global.BYTE_SIZE; // type
            retval+=Util.size(mbrs);
            retval+=Util.size(from);
            return retval;
        }


        public void writeTo(DataOutputStream out) throws IOException {
            out.writeByte(type);
            Util.writeAddresses(mbrs, out);
            Util.writeAddress(from, out);
        }



        public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
            type=in.readByte();
            mbrs=(Vector<Address>)Util.readAddresses(in, Vector.class);
            from=Util.readAddress(in);
        }

    }


    protected class Monitor implements Runnable {

        public void run() {
            Message hb_req;
            long not_heard_from; // time in msecs we haven't heard from ping_dest
            Address dest=null;

            lock.lock();
            try {
                if(ping_dest == null) {
                    if(log.isWarnEnabled())
                        log.warn("ping_dest is null: members=" + members + ", pingable_mbrs=" +
                                pingable_mbrs + ", local_addr=" + local_addr);
                    return;
                }
                else
                    dest=ping_dest;
            }
            finally {
                lock.unlock();
            }


            // 1. send heartbeat request
            hb_req=new Message(dest, null, null);
            hb_req.setFlag(Message.OOB);
            hb_req.putHeader(name, new FdHeader(FdHeader.HEARTBEAT));  // send heartbeat request
            if(log.isDebugEnabled())
                log.debug("sending are-you-alive msg to " + dest + " (own address=" + local_addr + ')');
            down_prot.down(new Event(Event.MSG, hb_req));
            num_heartbeats++;

            // 2. If the time of the last heartbeat is > timeout and max_tries heartbeat messages have not been
            //    received, then broadcast a SUSPECT message. Will be handled by coordinator, which may install
            //    a new view
            not_heard_from=System.currentTimeMillis() - last_ack;
            // quick & dirty fix: increase timeout by 500msecs to allow for latency (bela June 27 2003)
            if(not_heard_from > timeout + 500) { // no heartbeat ack for more than timeout msecs
                if(num_tries >= max_tries) {
                    if(log.isDebugEnabled())
                        log.debug("[" + local_addr + "]: received no heartbeat ack from " + dest +
                                " for " + (num_tries +1) + " times (" + ((num_tries+1) * timeout) +
                                " milliseconds), suspecting it");
                    // broadcast a SUSPECT message to all members - loop until
                    // unsuspect or view change is received
                    bcast_task.addSuspectedMember(dest);
                    num_tries=0;
                    if(stats) {
                        num_suspect_events++;
                        suspect_history.add(dest);
                    }
                }
                else {
                    if(log.isDebugEnabled())
                        log.debug("heartbeat missing from " + dest + " (number=" + num_tries + ')');
                    num_tries++;
                }
            }
        }
    }


    /**
     * Task that periodically broadcasts a list of suspected members to the group. Goal is not to lose
     * a SUSPECT message: since these are bcast unreliably, they might get dropped. The BroadcastTask makes
     * sure they are retransmitted until a view has been received which doesn't contain the suspected members
     * any longer. Then the task terminates.
     */
    protected final class Broadcaster {
        final Vector<Address> suspected_mbrs=new Vector<Address>(7);
        final Lock bcast_lock=new ReentrantLock();
        @GuardedBy("bcast_lock")
        Future bcast_future=null;
        @GuardedBy("bcast_lock")
        BroadcastTask task;


        Vector getSuspectedMembers() {
            return suspected_mbrs;
        }

        /**
         * Starts a new task, or - if already running - adds the argument to the running task.
         * @param suspect
         */
        private void startBroadcastTask(Address suspect) {
            bcast_lock.lock();
            try {
                if(bcast_future == null || bcast_future.isDone()) {
                    task=new BroadcastTask(suspected_mbrs);
                    task.addSuspectedMember(suspect);
                    bcast_future=timer.scheduleWithFixedDelay(task,
                                                              0, // run immediately the first time
                                                              timeout, // then every timeout milliseconds, until cancelled
                                                              TimeUnit.MILLISECONDS);
                    if(log.isTraceEnabled())
                        log.trace("BroadcastTask started");
                }
                else {
                    task.addSuspectedMember(suspect);
                }
            }
            finally {
                bcast_lock.unlock();
            }
        }

        private void stopBroadcastTask() {
            bcast_lock.lock();
            try {
                if(bcast_future != null) {
                    bcast_future.cancel(true);
                    bcast_future=null;
                    task=null;
                }
            }
            finally {
                bcast_lock.unlock();
            }
        }

        /** Adds a suspected member. Starts the task if not yet running */
        protected void addSuspectedMember(Address mbr) {
            if(mbr == null) return;
            if(!members.contains(mbr)) return;
            synchronized(suspected_mbrs) {
                if(!suspected_mbrs.contains(mbr)) {
                    suspected_mbrs.addElement(mbr);
                    startBroadcastTask(mbr);
                }
            }
        }

        void removeSuspectedMember(Address suspected_mbr) {
            if(suspected_mbr == null) return;
            if(log.isDebugEnabled()) log.debug("member is " + suspected_mbr);
            synchronized(suspected_mbrs) {
                suspected_mbrs.removeElement(suspected_mbr);
                if(suspected_mbrs.isEmpty())
                    stopBroadcastTask();
            }
        }


        /** Removes all elements from suspected_mbrs that are <em>not</em> in the new membership */
        void adjustSuspectedMembers(List new_mbrship) {
            if(new_mbrship == null || new_mbrship.isEmpty()) return;
            synchronized(suspected_mbrs) {
                suspected_mbrs.retainAll(new_mbrship);
                if(suspected_mbrs.isEmpty())
                    stopBroadcastTask();
            }
        }
    }


    protected final class BroadcastTask implements Runnable {
        private final Vector<Address> suspected_members=new Vector<Address>();


        BroadcastTask(Vector<Address> suspected_members) {
            this.suspected_members.addAll(suspected_members);
        }

        public void stop() {
            suspected_members.clear();
            if(log.isTraceEnabled())
                log.trace("BroadcastTask stopped");
        }


        public void run() {
            Message suspect_msg;
            FD.FdHeader hdr;

            synchronized(suspected_members) {
                if(suspected_members.isEmpty()) {
                    stop();
                    return;
                }

                hdr=new FdHeader(FdHeader.SUSPECT);
                hdr.mbrs=new Vector<Address>(suspected_members);
                hdr.from=local_addr;
            }
            suspect_msg=new Message();       // mcast SUSPECT to all members
            suspect_msg.setFlag(Message.OOB);
            suspect_msg.putHeader(name, hdr);
            if(log.isDebugEnabled())
                log.debug("broadcasting SUSPECT message [suspected_mbrs=" + suspected_members + "] to group");
            down_prot.down(new Event(Event.MSG, suspect_msg));
        }

        public void addSuspectedMember(Address suspect) {
            if(suspect != null && !suspected_members.contains(suspect)) {
                suspected_members.add(suspect);
            }
        }
    }

}
