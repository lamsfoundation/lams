
package org.jgroups.protocols;

import org.jgroups.*;
import org.jgroups.stack.AckReceiverWindow;
import org.jgroups.stack.AckSenderWindow;
import org.jgroups.stack.Protocol;
import org.jgroups.stack.StaticInterval;
import org.jgroups.util.BoundedList;
import org.jgroups.util.Streamable;
import org.jgroups.util.TimeScheduler;
import org.jgroups.util.Util;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Reliable unicast layer. Uses acknowledgement scheme similar to TCP to provide lossless transmission
 * of unicast messages (for reliable multicast see NAKACK layer). When a message is sent to a peer for
 * the first time, we add the pair <peer_addr, Entry> to the hashtable (peer address is the key). All
 * messages sent to that peer will be added to hashtable.peer_addr.sent_msgs. When we receive a
 * message from a peer for the first time, another entry will be created and added to the hashtable
 * (unless already existing). Msgs will then be added to hashtable.peer_addr.received_msgs.<p> This
 * layer is used to reliably transmit point-to-point messages, that is, either messages sent to a
 * single receiver (vs. messages multicast to a group) or for example replies to a multicast message. The 
 * sender uses an <code>AckSenderWindow</code> which retransmits messages for which it hasn't received
 * an ACK, the receiver uses <code>AckReceiverWindow</code> which keeps track of the lowest seqno
 * received so far, and keeps messages in order.<p>
 * Messages in both AckSenderWindows and AckReceiverWindows will be removed. A message will be removed from
 * AckSenderWindow when an ACK has been received for it and messages will be removed from AckReceiverWindow
 * whenever a message is received: the new message is added and then we try to remove as many messages as
 * possible (until we stop at a gap, or there are no more messages).
 * @author Bela Ban
 *
 */
public class UNICAST extends Protocol implements AckSenderWindow.RetransmitCommand {
    private final Vector<Address> members=new Vector<Address>(11);
    private final HashMap<Address,Entry> connections=new HashMap<Address,Entry>(11);
    private long[]                timeouts={400,800,1600,3200};  // for AckSenderWindow: max time to wait for missing acks
    private Address               local_addr=null;
    private TimeScheduler         timer=null;                    // used for retransmissions (passed to AckSenderWindow)
    private Map<Thread,ReentrantLock> locks;

    // if UNICAST is used without GMS, don't consult the membership on retransmit() if use_gms=false
    // default is true
    private boolean          use_gms=true;
    private boolean          started=false;

    // ack a message before it is processed by the application to limit unnecessary retransmits
    private boolean          immediate_ack=false;

    /** whether to loop back messages sent to self (will be removed in the future, default=false) */
    private boolean          loopback=false;

    /**
     * By default, we release the lock on the sender in up() after the up() method call passed up the stack returns.
     * However, with eager_lock_release enabled (default), we release the lock as soon as the application calls
     * Channel.down() <em>within</em> the receive() callback. This leads to issues as the one described in
     * http://jira.jboss.com/jira/browse/JGRP-656. Note that ordering is <em>still correct </em>, but messages from self
     * might get delivered concurrently. This can be turned off by setting eager_lock_release to false.
     */
    private boolean eager_lock_release=true;

    /** A list of members who left, used to determine when to prevent sending messages to left mbrs */
    private final BoundedList<Address> previous_members=new BoundedList<Address>(50);
    /** Contains all members that were enabled for unicasts by Event.ENABLE_UNICAST_TO */
    private final BoundedList<Address> enabled_members=new BoundedList<Address>(100);

    private final static String name="UNICAST";
    private static final long DEFAULT_FIRST_SEQNO=1;

    private long num_msgs_sent=0, num_msgs_received=0, num_bytes_sent=0, num_bytes_received=0;
    private long num_acks_sent=0, num_acks_received=0, num_xmit_requests_received=0;


    /** <em>Regular</em> messages which have been added, but not removed */
    private final AtomicInteger undelivered_msgs=new AtomicInteger(0);


    /** All protocol names have to be unique ! */
    public String  getName() {return name;}

    public String getLocalAddress() {return local_addr != null? local_addr.toString() : "null";}
    public String getMembers() {return members != null? members.toString() : "[]";}
    public String printConnections() {
        StringBuilder sb=new StringBuilder();
        for(Map.Entry<Address,Entry> entry: connections.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }


    public long getNumMessagesSent() {
        return num_msgs_sent;
    }

    public long getNumMessagesReceived() {
        return num_msgs_received;
    }

    public long getNumBytesSent() {
        return num_bytes_sent;
    }

    public long getNumBytesReceived() {
        return num_bytes_received;
    }

    public long getNumAcksSent() {
        return num_acks_sent;
    }

    public long getNumAcksReceived() {
        return num_acks_received;
    }

    public long getNumberOfRetransmitRequestsReceived() {
        return num_xmit_requests_received;
    }

    /** The number of messages in all Entry.sent_msgs tables (haven't received an ACK yet) */
    public int getNumberOfUnackedMessages() {
        int num=0;
        synchronized(connections) {
            for(Entry entry: connections.values()) {
                if(entry.sent_msgs != null)
                    num+=entry.sent_msgs.size();
            }
        }
        return num;
    }


    public String getUnackedMessages() {
        StringBuilder sb=new StringBuilder();
        Entry e;
        Object member;
        synchronized(connections) {
            for(Map.Entry<Address,Entry> entry: connections.entrySet()) {
                member=entry.getKey();
                e=entry.getValue();
                sb.append(member).append(": ");
                if(e.sent_msgs != null)
                    sb.append(e.sent_msgs.toString()).append("\n");
            }
        }
        return sb.toString();
    }


    public int getNumberOfMessagesInReceiveWindows() {
        int num=0;
        synchronized(connections) {
            for(Entry entry: connections.values()) {
                if(entry.received_msgs != null)
                    num+=entry.received_msgs.size();
            }
        }
        return num;
    }

    public void resetStats() {
        num_msgs_sent=num_msgs_received=num_bytes_sent=num_bytes_received=num_acks_sent=num_acks_received=0;
        num_xmit_requests_received=0;
    }


    public Map<String, Object> dumpStats() {
        Map<String,Object> m=new HashMap<String,Object>();
        m.put("num_msgs_sent", new Long(num_msgs_sent));
        m.put("num_msgs_received", new Long(num_msgs_received));
        m.put("num_bytes_sent", new Long(num_bytes_sent));
        m.put("num_bytes_received", new Long(num_bytes_received));
        m.put("num_acks_sent", new Long(num_acks_sent));
        m.put("num_acks_received", new Long(num_acks_received));
        m.put("num_xmit_requests_received", new Long(num_xmit_requests_received));
        m.put("num_unacked_msgs", new Long(getNumberOfUnackedMessages()));
        m.put("unacked_msgs", getUnackedMessages());
        m.put("num_msgs_in_recv_windows", new Long(getNumberOfMessagesInReceiveWindows()));
        return m;
    }

    public boolean setProperties(Properties props) {
        String     str;
        long[]     tmp;

        super.setProperties(props);
        str=props.getProperty("timeout");
        if(str != null) {
        tmp=Util.parseCommaDelimitedLongs(str);
        if(tmp != null && tmp.length > 0)
        timeouts=tmp;
            props.remove("timeout");
        }

        str=props.getProperty("window_size");
        if(str != null) {
            props.remove("window_size");
            log.warn("window_size is deprecated and will be ignored");
        }

        str=props.getProperty("min_threshold");
        if(str != null) {
            props.remove("min_threshold");
            log.warn("min_threshold is deprecated and will be ignored");
        }

        str=props.getProperty("use_gms");
        if(str != null) {
            use_gms=Boolean.valueOf(str).booleanValue();
            props.remove("use_gms");
        }

        str=props.getProperty("immediate_ack");
        if(str != null) {
        	immediate_ack=Boolean.valueOf(str).booleanValue();
            props.remove("immediate_ack");
        }

        str=props.getProperty("loopback");
        if(str != null) {
            loopback=Boolean.valueOf(str).booleanValue();
            props.remove("loopback");
        }

        str=props.getProperty("eager_lock_release");
        if(str != null) {
            eager_lock_release=Boolean.valueOf(str).booleanValue();
            props.remove("eager_lock_release");
        }

        if(!props.isEmpty()) {
            log.error("these properties are not recognized: " + props);
            return false;
        }
        return true;
    }

    public void start() throws Exception {
        timer=getTransport().getTimer();
        if(timer == null)
            throw new Exception("timer is null");
        locks=stack.getLocks();
        started=true;
    }

    public void stop() {
        started=false;
        removeAllConnections();
        undelivered_msgs.set(0);
    }


    public Object up(Event evt) {
        Message        msg;
        Address        dst, src;
        UnicastHeader  hdr;

        switch(evt.getType()) {

        case Event.MSG:
            msg=(Message)evt.getArg();
            dst=msg.getDest();

            if(dst == null || dst.isMulticastAddress())  // only handle unicast messages
                break;  // pass up

            // changed from removeHeader(): we cannot remove the header because if we do loopback=true at the
            // transport level, we will not have the header on retransmit ! (bela Aug 22 2006)
            hdr=(UnicastHeader)msg.getHeader(name);
            if(hdr == null)
                break;
            src=msg.getSrc();
            switch(hdr.type) {
            case UnicastHeader.DATA:      // received regular message
                // only send an ACK if added to the received_msgs table (bela Aug 2006)
                // if in immediate_ack mode, send ack inside handleDataReceived
            	if(handleDataReceived(src, hdr.seqno, msg) && !immediate_ack)
                    sendAck(src, hdr.seqno);
                return null; // we pass the deliverable message up in handleDataReceived()
            case UnicastHeader.ACK:  // received ACK for previously sent message
                handleAckReceived(src, hdr.seqno);
                break;
            default:
                log.error("UnicastHeader type " + hdr.type + " not known !");
                break;
            }
            return null;

        case Event.SET_LOCAL_ADDRESS:
            local_addr=(Address)evt.getArg();
            break;
        }

        return up_prot.up(evt);   // Pass up to the layer above us
    }



    public Object down(Event evt) {
        switch (evt.getType()) {

            case Event.MSG: // Add UnicastHeader, add to AckSenderWindow and pass down
                Message msg=(Message)evt.getArg();
                Address dst=msg.getDest();

                /* only handle unicast messages */
                if (dst == null || dst.isMulticastAddress()) {
                    break;
                }

                if(!started) {
                    if(log.isTraceEnabled())
                        log.trace("discarded message as start() has not yet been called, message: " + msg);
                    return null;
                }

                // if the dest is self --> pass the message back up
                if(loopback && local_addr != null && local_addr.equals(dst)) {
                    msg.setSrc(local_addr);
                    up_prot.up(evt);
                    num_msgs_sent++;
                    num_bytes_sent+=msg.getLength();
                    return null;
                }

                if(!members.contains(dst) && !enabled_members.contains(dst)) {
                    throw new IllegalArgumentException(dst + " is not a member of the group " + members + " (enabled_members=" + enabled_members + ")");
                }

                Entry entry;
                synchronized(connections) {
                    entry=connections.get(dst);
                    if(entry == null) {
                        entry=new Entry();
                        connections.put(dst, entry);
                        if(log.isTraceEnabled())
                            log.trace(local_addr + ": created new connection for dst " + dst);
                    }
                }

                long seqno=-2;
                synchronized(entry) { // threads will only sync if they access the same entry
                    try {
                        seqno=entry.sent_msgs_seqno;
                        UnicastHeader hdr=new UnicastHeader(UnicastHeader.DATA, seqno);
                        if(entry.sent_msgs == null) { // first msg to peer 'dst'
                            entry.sent_msgs=new AckSenderWindow(this, new StaticInterval(timeouts), timer, this.local_addr); // use the global timer
                        }
                        msg.putHeader(name, hdr);
                        if(log.isTraceEnabled())
                            log.trace(new StringBuilder().append(local_addr).append(" --> DATA(").append(dst).append(": #").
                                    append(seqno));
                        if(entry.sent_msgs != null)
                            entry.sent_msgs.add(seqno, msg);  // add *including* UnicastHeader, adds to retransmitter
                        entry.sent_msgs_seqno++;
                    }
                    catch(Throwable t) {
                        if(entry.sent_msgs != null)
                            entry.sent_msgs.ack(seqno); // remove seqno again, so it is not transmitted
                        if(t instanceof Error)
                            throw (Error)t;
                        if(t instanceof RuntimeException)
                            throw (RuntimeException)t;
                        else {
                            throw new RuntimeException("failure adding msg " + msg + " to the retransmit table", t);
                        }
                    }
                }
                // moved passing down of message out of the synchronized block: similar to NAKACK, we do *not* need
                // to send unicast messages in order of sequence numbers because they will be sorted into the correct
                // order at the receiver anyway. Of course, most of the time, the order will be correct (FIFO), so
                // the cost of reordering is minimal. This is part of http://jira.jboss.com/jira/browse/JGRP-303
                try { // we catch the exception in this case because the msg is in the XMIT table and will be retransmitted
                    send(msg, evt);
                }
                catch(Throwable t) {
                    log.warn("failed sending the message", t);
                }
                return null; // we already passed the msg down

            case Event.VIEW_CHANGE:  // remove connections to peers that are not members anymore !
                View view=(View)evt.getArg();
                Vector<Address> new_members=view.getMembers();
                Vector<Address> left_members;
                synchronized(members) {
                    left_members=Util.determineLeftMembers(members, new_members);
                    members.clear();
                    if(new_members != null)
                        members.addAll(new_members);
                }

                // Remove all connections for members that left between the current view and the new view
                // See DESIGN for details
                boolean rc;
                if(use_gms && !left_members.isEmpty()) {
                    Address mbr;
                    for(int i=0; i < left_members.size(); i++) {
                        mbr=left_members.elementAt(i);
                        rc=removeConnection(mbr); // adds to previous_members
                        if(rc && log.isTraceEnabled())
                            log.trace("removed " + mbr + " from connection table, member(s) " + left_members + " left");
                    }
                }
                // code by Matthias Weber May 23 2006
                for(Address mbr: previous_members) {
                    if(members.contains(mbr)) {
                        if(previous_members.remove(mbr)) {
                            if(log.isTraceEnabled())
                                log.trace("removed " + mbr + " from previous_members as result of VIEW_CHANGE event, " +
                                        "previous_members=" + previous_members);
                        }
                    }
                }
                // remove all members from enabled_members
                synchronized(members) {
                    for(Address mbr: members) {
                        enabled_members.remove(mbr);
                    }
                }

                synchronized(previous_members) {
                    for(Address mbr: previous_members) {
                        enabled_members.remove(mbr);
                    }
                }

                // trash connections to/from members who are in the merge view, fix for: http://jira.jboss.com/jira/browse/JGRP-348
                // update (bela, April  25 2008): reverted because of http://jira.jboss.com/jira/browse/JGRP-659, we fix this
                // in 2.7 only as we don't want to change the serialization format. The JIRA issue is
                // http://jira.jboss.com/jira/browse/JGRP-742
//                if(view instanceof MergeView) {
//                    if(log.isTraceEnabled())
//                        log.trace("removing all connections for the current members due to a merge");
//                    removeConnections(members);
//                }

                break;

            case Event.ENABLE_UNICASTS_TO:
                Address member=(Address)evt.getArg();
                if(!enabled_members.contains(member))
                    enabled_members.add(member);
                boolean removed=previous_members.remove(member);
                if(removed && log.isTraceEnabled())
                    log.trace("removing " + member + " from previous_members as result of ENABLE_UNICAST_TO event, " +
                            "previous_members=" + previous_members);
                break;

            case Event.DISABLE_UNICASTS_TO:
                member=(Address)evt.getArg();
                removeConnection(member);
                enabled_members.remove(member);
                previous_members.remove(member);
                break;
        }

        return down_prot.down(evt);          // Pass on to the layer below us
    }


    private void send(Message msg, Event evt) {
        down_prot.down(evt);
        num_msgs_sent++;
        num_bytes_sent+=msg.getLength();
    }

    /** Removes and resets from connection table (which is already locked). Returns true if member was found, otherwise false */
    private boolean removeConnection(Address mbr) {
        Entry entry;

        synchronized(connections) {
            entry=connections.remove(mbr);
            if(!previous_members.contains(mbr))
                previous_members.add(mbr);
        }
        if(entry != null) {
            entry.reset();
            return true;
        }
        else
            return false;
    }


    private void removeAllConnections() {
        synchronized(connections) {
            for(Entry entry: connections.values()) {
                entry.reset();
            }
            connections.clear();
        }
    }



    /** Called by AckSenderWindow to resend messages for which no ACK has been received yet */
    public void retransmit(long seqno, Message msg) {
        Object  dst=msg.getDest();

        // bela Dec 23 2002:
        // this will remove a member on a MERGE request, e.g. A and B merge: when A sends the unicast
        // request to B and there's a retransmit(), B will be removed !

        //          if(use_gms && !members.contains(dst) && !prev_members.contains(dst)) {
        //
        //                  if(log.isWarnEnabled()) log.warn("UNICAST.retransmit()", "seqno=" + seqno + ":  dest " + dst +
        //                             " is not member any longer; removing entry !");

        //              synchronized(connections) {
        //                  removeConnection(dst);
        //              }
        //              return;
        //          }

        if(log.isTraceEnabled())
            log.trace("[" + local_addr + "] --> XMIT(" + dst + ": #" + seqno + ')');

        down_prot.down(new Event(Event.MSG, msg));
        num_xmit_requests_received++;
    }





    /**
     * Check whether the hashtable contains an entry e for <code>sender</code> (create if not). If
     * e.received_msgs is null and <code>first</code> is true: create a new AckReceiverWindow(seqno) and
     * add message. Set e.received_msgs to the new window. Else just add the message.
     * @return boolean True if we can send an ack, false otherwise
     */
    private boolean handleDataReceived(Address sender, long seqno, Message msg) {
        if(log.isTraceEnabled())
            log.trace(new StringBuilder().append(local_addr).append(" <-- DATA(").append(sender).append(": #").append(seqno));

        if(previous_members.contains(sender)) {
            // we don't want to see messages from departed members
            if(seqno > DEFAULT_FIRST_SEQNO) {
                if(log.isTraceEnabled())
                    log.trace("discarding message " + seqno + " from previous member " + sender);
                return false; // don't ack this message so the sender keeps resending it !
            }
            if(log.isTraceEnabled())
                log.trace("removed " + sender + " from previous_members as we received a message from it");
            previous_members.remove(sender);
        }

        Entry    entry;
        AckReceiverWindow win;
        synchronized(connections) {
            entry=connections.get(sender);
            if(entry == null) {
                entry=new Entry();
                connections.put(sender, entry);
                if(log.isTraceEnabled())
                    log.trace(local_addr + ": created new connection for dst " + sender);
            }
            win=entry.received_msgs;
            if(win == null) {
                win=new AckReceiverWindow(DEFAULT_FIRST_SEQNO);
                entry.received_msgs=win;
            }
        }

        boolean added=win.add(seqno, msg); // entry.received_msgs is guaranteed to be non-null if we get here
        boolean regular_msg_added=added && !msg.isFlagSet(Message.OOB);
        num_msgs_received++;
        num_bytes_received+=msg.getLength();

        // http://jira.jboss.com/jira/browse/JGRP-713: // send the ack back *before* we process the message
        // to limit unnecessary retransmits
        if(immediate_ack)
            sendAck(sender, seqno); // send an ack regardless of whether the message was added (stops retransmission)

        // message is passed up if OOB. Later, when remove() is called, we discard it. This affects ordering !
        // http://jira.jboss.com/jira/browse/JGRP-377
        if(msg.isFlagSet(Message.OOB)) {
            if(added)
                up_prot.up(new Event(Event.MSG, msg));
            win.removeOOBMessage(); // if we only have OOB messages, we'd never remove them !
            if(!(win.hasMessagesToRemove() && undelivered_msgs.get() > 0))
                return true;
        }

        if(!added && !win.hasMessagesToRemove()) { // no ack if we didn't add the msg (e.g. duplicate)
            return true; // ack the message, because this will stop retransmissions (which are unreliable) !
        }


        // Try to remove (from the AckReceiverWindow) as many messages as possible as pass them up
        Message  m;
        short removed_regular_msgs=0;

        // Prevents concurrent passing up of messages by different threads (http://jira.jboss.com/jira/browse/JGRP-198);
        // this is all the more important once we have a threadless stack (http://jira.jboss.com/jira/browse/JGRP-181),
        // where lots of threads can come up to this point concurrently, but only 1 is allowed to pass at a time
        // We *can* deliver messages from *different* senders concurrently, e.g. reception of P1, Q1, P2, Q2 can result in
        // delivery of P1, Q1, Q2, P2: FIFO (implemented by UNICAST) says messages need to be delivered only in the
        // order in which they were sent by their senders
        ReentrantLock lock=win.getLock();
        lock.lock(); // we don't block on entry any more (http://jira.jboss.com/jira/browse/JGRP-485)
        try {
            if(eager_lock_release)
                locks.put(Thread.currentThread(), lock);
            while((m=win.remove()) != null) {
                // discard OOB msg as it has already been delivered (http://jira.jboss.com/jira/browse/JGRP-377)
                if(m.isFlagSet(Message.OOB)) {
                    continue;
                }
                removed_regular_msgs++;
                up_prot.up(new Event(Event.MSG, m));
            }
        }
        finally {
            if(eager_lock_release)
                locks.remove(Thread.currentThread());
            if(lock.isHeldByCurrentThread())
                lock.unlock();
            // We keep track of regular messages that we added, but couldn't remove (because of ordering).
            // When we have such messages pending, then even OOB threads will remove and process them.
            // http://jira.jboss.com/jira/browse/JGRP-780
            if(regular_msg_added && removed_regular_msgs == 0) {
                undelivered_msgs.incrementAndGet();
            }

            if(removed_regular_msgs > 0) { // regardless of whether a message was added or not !
                int num_msgs_added=regular_msg_added? 1 : 0;
                undelivered_msgs.addAndGet(-(removed_regular_msgs -num_msgs_added));
            }
        }
        return true; // msg was successfully received - send an ack back to the sender
    }


    /** Add the ACK to hashtable.sender.sent_msgs */
    private void handleAckReceived(Address sender, long seqno) {
        Entry           entry;
        AckSenderWindow win;

        if(log.isTraceEnabled())
            log.trace(new StringBuilder().append(local_addr).append(" <-- ACK(").append(sender).
                      append(": #").append(seqno).append(')'));
        synchronized(connections) {
            entry=connections.get(sender);
        }
        if(entry == null || entry.sent_msgs == null)
            return;
        win=entry.sent_msgs;
        win.ack(seqno); // removes message from retransmission
        num_acks_received++;
    }



    private void sendAck(Address dst, long seqno) {
        Message ack=new Message(dst);
        ack.setFlag(Message.OOB);
        ack.putHeader(name, new UnicastHeader(UnicastHeader.ACK, seqno));
        if(log.isTraceEnabled())
            log.trace(new StringBuilder().append(local_addr).append(" --> ACK(").append(dst).
                      append(": #").append(seqno).append(')'));
        down_prot.down(new Event(Event.MSG, ack));
        num_acks_sent++;
    }






    public static class UnicastHeader extends Header implements Streamable {
        public static final byte DATA=0;
        public static final byte ACK=1;

        byte    type=DATA;
        long    seqno=0;

        static final int serialized_size=Global.BYTE_SIZE + Global.LONG_SIZE;
        private static final long serialVersionUID=-5590873777959784299L;


        public UnicastHeader() {} // used for externalization

        public UnicastHeader(byte type, long seqno) {
            this.type=type;
            this.seqno=seqno;
        }

        public String toString() {
            return "[UNICAST: " + type2Str(type) + ", seqno=" + seqno + ']';
        }

        public static String type2Str(byte t) {
            switch(t) {
                case DATA: return "DATA";
                case ACK: return "ACK";
                default: return "<unknown>";
            }
        }

        public final int size() {
            return serialized_size;
        }


        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(type);
            out.writeLong(seqno);
        }


        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            type=in.readByte();
            seqno=in.readLong();
        }

        public void writeTo(DataOutputStream out) throws IOException {
            out.writeByte(type);
            out.writeLong(seqno);
        }

        public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
            type=in.readByte();
            seqno=in.readLong();
        }
    }

    private static final class Entry {
        AckReceiverWindow  received_msgs=null;  // stores all msgs rcvd by a certain peer in seqno-order
        AckSenderWindow    sent_msgs=null;      // stores (and retransmits) msgs sent by us to a certain peer
        long               sent_msgs_seqno=DEFAULT_FIRST_SEQNO;   // seqno for msgs sent by us

        void reset() {
            if(sent_msgs != null)
                sent_msgs.reset();
            if(received_msgs != null)
                received_msgs.reset();
            sent_msgs_seqno=DEFAULT_FIRST_SEQNO;
        }


        public String toString() {
            StringBuilder sb=new StringBuilder();
            if(sent_msgs != null)
                sb.append("sent_msgs=").append(sent_msgs).append('\n');
            if(received_msgs != null)
                sb.append("received_msgs=").append(received_msgs).append('\n');
            return sb.toString();
        }
    }


}
