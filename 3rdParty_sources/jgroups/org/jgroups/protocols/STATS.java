package org.jgroups.protocols;

import org.jgroups.stack.Protocol;
import org.jgroups.Event;
import org.jgroups.Message;
import org.jgroups.Address;
import org.jgroups.View;

import java.util.*;

/**
 * Provides various stats
 * @author Bela Ban
 *
 */
public class STATS extends Protocol {
    long sent_msgs, sent_bytes, sent_ucasts, sent_mcasts, received_ucasts, received_mcasts;
    long received_msgs, received_bytes, sent_ucast_bytes, sent_mcast_bytes, received_ucast_bytes, received_mcast_bytes;

    /** HashMap<Address,Entry>, maintains stats per target destination */
    HashMap sent=new HashMap();

    /** HashMap<Address,Entry>, maintains stats per receiver */
    HashMap received=new HashMap();

    static final short UP=1;
    static final short DOWN=2;


    public String getName() {
        return "STATS";
    }

    public boolean setProperties(Properties props) {
        super.setProperties(props);
        if(!props.isEmpty()) {
            log.error("the following properties are not recognized: " + props);
            return false;
        }
        return true;
    }

    public void resetStats() {
        sent_msgs=sent_bytes=sent_ucasts=sent_mcasts=received_ucasts=received_mcasts=0;
        received_msgs=received_bytes=sent_ucast_bytes=sent_mcast_bytes=received_ucast_bytes=received_mcast_bytes=0;
        sent.clear();
        received.clear();
    }


    public long getSentMessages() {return sent_msgs;}
    public long getSentBytes() {return sent_bytes;}
    public long getSentUnicastMessages() {return sent_ucasts;}
    public long getSentUnicastBytes() {return sent_ucast_bytes;}
    public long getSentMcastMessages() {return sent_mcasts;}
    public long getSentMcastBytes() {return sent_mcast_bytes;}

    public long getReceivedMessages() {return received_msgs;}
    public long getReceivedBytes() {return received_bytes;}
    public long getReceivedUnicastMessages() {return received_ucasts;}
    public long getReceivedUnicastBytes() {return received_ucast_bytes;}
    public long getReceivedMcastMessages() {return received_mcasts;}
    public long getReceivedMcastBytes() {return received_mcast_bytes;}


    public Object up(Event evt) {
        if(evt.getType() == Event.MSG) {
            Message msg=(Message)evt.getArg();
            updateStats(msg, UP);
        }
        else if(evt.getType() == Event.VIEW_CHANGE) {
            handleViewChange((View)evt.getArg());
        }
        return up_prot.up(evt);
    }



    public Object down(Event evt) {
        if(evt.getType() == Event.MSG) {
            Message msg=(Message)evt.getArg();
            updateStats(msg, DOWN);
        }
        else if(evt.getType() == Event.VIEW_CHANGE) {
            handleViewChange((View)evt.getArg());
        }
        return down_prot.down(evt);
    }


    public String printStats() {
        Map.Entry entry;
        Object key, val;
        StringBuilder sb=new StringBuilder();
        sb.append("sent:\n");
        for(Iterator it=sent.entrySet().iterator(); it.hasNext();) {
            entry=(Map.Entry)it.next();
            key=entry.getKey();
            if(key == null) key="<mcast dest>";
            val=entry.getValue();
            sb.append(key).append(": ").append(val).append("\n");
        }
        sb.append("\nreceived:\n");
        for(Iterator it=received.entrySet().iterator(); it.hasNext();) {
            entry=(Map.Entry)it.next();
            key=entry.getKey();
            val=entry.getValue();
            sb.append(key).append(": ").append(val).append("\n");
        }

        return sb.toString();
    }

    private void handleViewChange(View view) {
        Vector members=view.getMembers();
        Set tmp=new LinkedHashSet(members);
        tmp.add(null); // for null destination (= mcast)
        sent.keySet().retainAll(tmp);
        received.keySet().retainAll(tmp);
    }

    private void updateStats(Message msg, short direction) {
        int     length;
        HashMap map;
        boolean mcast;
        Address dest, src;

        if(msg == null) return;
        length=msg.getLength();
        dest=msg.getDest();
        src=msg.getSrc();
        mcast=dest == null || dest.isMulticastAddress();

        if(direction == UP) { // received
            received_msgs++;
            received_bytes+=length;
            if(mcast) {
                received_mcasts++;
                received_mcast_bytes+=length;
            }
            else {
                received_ucasts++;
                received_ucast_bytes+=length;
            }
        }
        else {                // sent
            sent_msgs++;
            sent_bytes+=length;
            if(mcast) {
                sent_mcasts++;
                sent_mcast_bytes+=length;
            }
            else {
                sent_ucasts++;
                sent_ucast_bytes+=length;
            }
        }

        Address key=direction == UP? src : dest;
        map=direction == UP? received : sent;
        Entry entry=(Entry)map.get(key);
        if(entry == null) {
            entry=new Entry();
            map.put(key, entry);
        }
        entry.msgs++;
        entry.bytes+=length;
        if(mcast) {
            entry.mcasts++;
            entry.mcast_bytes+=length;
        }
        else {
            entry.ucasts++;
            entry.ucast_bytes+=length;
        }
    }




    static class Entry {
        long msgs, bytes, ucasts, mcasts, ucast_bytes, mcast_bytes;

        public String toString() {
            StringBuilder sb=new StringBuilder();
            sb.append(msgs).append(" (").append(bytes).append(" bytes)");
            sb.append(": ").append(ucasts).append(" ucasts (").append(ucast_bytes).append(" bytes), ");
            sb.append(mcasts).append(" mcasts (").append(mcast_bytes).append(" bytes)");
            return sb.toString();
        }
    }



}
