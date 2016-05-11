
package org.jgroups.protocols;

import org.jgroups.*;
import org.jgroups.stack.Protocol;
import org.jgroups.util.SeqnoTable;
import org.jgroups.util.Streamable;
import org.jgroups.util.Util;

import java.io.*;
import java.util.*;


/**
 * Implementation of total order protocol using a sequencer. Consult doc/design/SEQUENCER.txt for details
 * @author Bela Ban
 *
 */
public class SEQUENCER extends Protocol {
    private Address           local_addr=null, coord=null;
    static final String       name="SEQUENCER";
    private boolean           is_coord=false;
    private long              seqno=0;

    /** Map<seqno, Message>: maintains messages forwarded to the coord which which no ack has been received yet */
    private final Map<Long,Message>          forward_table=new TreeMap<Long,Message>();

    /** Map<Address, seqno>: maintains the highest seqnos seen for a given member */
    private final SeqnoTable received_table=new SeqnoTable(0);

    private long forwarded_msgs=0;
    private long bcast_msgs=0;
    private long received_forwards=0;
    private long received_bcasts=0;

    public boolean isCoordinator() {return is_coord;}
    public Address getCoordinator() {return coord;}
    public Address getLocalAddress() {return local_addr;}
    public String getName() {return name;}
    public long getForwarded() {return forwarded_msgs;}
    public long getBroadcast() {return bcast_msgs;}
    public long getReceivedForwards() {return received_forwards;}
    public long getReceivedBroadcasts() {return received_bcasts;}

    public void resetStats() {
        forwarded_msgs=bcast_msgs=received_forwards=received_bcasts=0L;
    }

    public Map<String,Object> dumpStats() {
        Map<String,Object> m=super.dumpStats();
        if(m == null)
            m=new HashMap<String,Object>();
        m.put("forwarded", new Long(forwarded_msgs));
        m.put("broadcast", new Long(bcast_msgs));
        m.put("received_forwards", new Long(received_forwards));
        m.put("received_bcasts", new Long(received_bcasts));
        return m;
    }

    public String printStats() {
        return dumpStats().toString();
    }


    public boolean setProperties(Properties props) {
        super.setProperties(props);

        if(!props.isEmpty()) {
            log.error("the following properties are not recognized: " + props);
            return false;
        }
        return true;
    }

    private final long nextSeqno() {
        synchronized(this) {
            return seqno++;
        }
    }

    
    public Object down(Event evt) {
        switch(evt.getType()) {
            case Event.MSG:
                Message msg=(Message)evt.getArg();
                Address dest=msg.getDest();
                if(dest == null || dest.isMulticastAddress()) { // only handle multicasts
                    long next_seqno=nextSeqno();
                    SequencerHeader hdr=new SequencerHeader(SequencerHeader.FORWARD, local_addr, next_seqno);
                    msg.putHeader(name, hdr);
                    if(!is_coord) {
                        forwardToCoord(msg, next_seqno);
                    }
                    else {
                        broadcast(msg);
                    }
                    return null; // don't pass down
                }
                break;

            case Event.VIEW_CHANGE:
                handleViewChange((View)evt.getArg());
                break;
        }
        return down_prot.down(evt);
    }




    public Object up(Event evt) {
        Message msg;
        SequencerHeader hdr;

        switch(evt.getType()) {

            case Event.SET_LOCAL_ADDRESS:
                local_addr=(Address)evt.getArg();
                break;

            case Event.MSG:
                msg=(Message)evt.getArg();
                hdr=(SequencerHeader)msg.getHeader(name);
                if(hdr == null)
                    break; // pass up

                switch(hdr.type) {
                    case SequencerHeader.FORWARD:
                        if(!is_coord) {
                            if(log.isErrorEnabled())
                                log.warn("I (" + local_addr + ") am not the coord and don't handle " +
                                        "FORWARD requests, ignoring request");
                            return null;
                        }
                        broadcast(msg);
                        received_forwards++;
                        return null;
                    case SequencerHeader.BCAST:
                        deliver(msg, hdr);  // deliver a copy and return (discard the original msg)
                        received_bcasts++;
                        return null;
                }
                break;

            case Event.VIEW_CHANGE:
                handleViewChange((View)evt.getArg());
                break;
        }

        return up_prot.up(evt);
    }



    /* --------------------------------- Private Methods ----------------------------------- */

    private void handleViewChange(View v) {
        Vector<Address> members=v.getMembers();
        if(members.isEmpty()) return;

        Address prev_coord=coord;
        coord=(Address)members.firstElement();
        is_coord=local_addr != null && local_addr.equals(coord);

        boolean coord_changed=prev_coord != null && !prev_coord.equals(coord);
        if(coord_changed) {
            resendMessagesInForwardTable(); // maybe optimize in the future: broadcast directly if coord
        }
        // remove left members from received_table
        received_table.retainAll(members);
    }

    /**
     * Sends all messages currently in forward_table to the new coordinator (changing the dest field).
     * This needs to be done, so the underlying reliable unicast protocol (e.g. UNICAST) adds these messages
     * to its retransmission mechanism<br/>
     * Note that we need to resend the messages in order of their seqnos ! We also need to prevent other message
     * from being inserted until we're done, that's why there's synchronization.
     */
    private void resendMessagesInForwardTable() {
        Map<Long,Message> copy;
        synchronized(forward_table) {
            copy=new TreeMap<Long,Message>(forward_table);
        }
        for(Message msg: copy.values()) {
            msg.setDest(coord);
            down_prot.down(new Event(Event.MSG, msg));
        }
    }


    private void forwardToCoord(Message msg, long seqno) {
        msg.setDest(coord);  // we change the message dest from multicast to unicast (to coord)
        synchronized(forward_table) {
            forward_table.put(new Long(seqno), msg);
        }
        down_prot.down(new Event(Event.MSG, msg));
        forwarded_msgs++;
    }

    private void broadcast(Message msg) {
        SequencerHeader hdr=(SequencerHeader)msg.getHeader(name);
        hdr.type=SequencerHeader.BCAST; // we change the type of header, but leave the tag intact
        msg.setDest(null); // mcast
        msg.setSrc(local_addr); // the coord is sending it - this will be replaced with sender in deliver()
        down_prot.down(new Event(Event.MSG, msg));
        bcast_msgs++;
    }

    /**
     * We copy the message in order to change the sender's address. If we did this on the original message,
     * retransmission would likely run into problems, and possibly also stability (STABLE) of messages
     * @param msg
     * @param hdr
     */
    private void deliver(Message msg, SequencerHeader hdr) {
        Address original_sender=hdr.getOriginalSender();
        if(original_sender == null) {
            if(log.isErrorEnabled())
                log.error("original sender is null, cannot swap sender address back to original sender");
            return;
        }
        long msg_seqno=hdr.getSeqno();

        // this is the ack for the message sent by myself
        if(original_sender.equals(local_addr)) {
            synchronized(forward_table) {
                forward_table.remove(new Long(msg_seqno));
            }
        }

        // if msg was already delivered, discard it
        boolean added=received_table.add(original_sender, msg_seqno);
        if(!added) {
            if(log.isWarnEnabled())
                log.warn("seqno (" + original_sender + "::" + msg_seqno + " has already been received " +
                        "(highest received=" + received_table.getHighestReceived(original_sender) +
                        "); discarding duplicate message");
            return;
        }

        // pass a copy of the message up the stack
        Message tmp=msg.copy(true);
        tmp.setSrc(original_sender);
        up_prot.up(new Event(Event.MSG, tmp));
    }

    /* ----------------------------- End of Private Methods -------------------------------- */





    public static class SequencerHeader extends Header implements Streamable {
        static final byte FORWARD = 1;
        static final byte BCAST   = 2;

        byte    type=-1;
        /** the original sender's address and a seqno */
        ViewId  tag=null;


        public SequencerHeader() {
        }

        public SequencerHeader(byte type, Address original_sender, long seqno) {
            this.type=type;
            this.tag=new ViewId(original_sender, seqno);
        }

        public Address getOriginalSender() {
            return tag != null? tag.getCoordAddress() : null;
        }

        public long getSeqno() {
            return tag != null? tag.getId() : -1;
        }

        public String toString() {
            StringBuilder sb=new StringBuilder(64);
            sb.append(printType());
            if(tag != null)
                sb.append(" (tag=").append(tag).append(")");
            return sb.toString();
        }

        private final String printType() {
            switch(type) {
                case FORWARD: return "FORWARD";
                case BCAST:   return "BCAST";
                default:      return "n/a";
            }
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(type);
            out.writeObject(tag);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            type=in.readByte();
            tag=(ViewId)in.readObject();
        }

        public void writeTo(DataOutputStream out) throws IOException {
            out.writeByte(type);
            Util.writeStreamable(tag, out);
        }

        public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
            type=in.readByte();
            tag=(ViewId)Util.readStreamable(ViewId.class, in);
        }

        public int size() {
            int size=Global.BYTE_SIZE *2; // type + presence byte
            if(tag != null)
                size+=tag.serializedSize();
            return size;
        }

    }


}