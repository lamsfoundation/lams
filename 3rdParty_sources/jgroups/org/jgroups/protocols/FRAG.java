
package org.jgroups.protocols;

import org.jgroups.Address;
import org.jgroups.Event;
import org.jgroups.Message;
import org.jgroups.View;
import org.jgroups.stack.Protocol;
import org.jgroups.util.ExposedByteArrayOutputStream;
import org.jgroups.util.Util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.*;



/**
 * Fragmentation layer. Fragments messages larger than FRAG_SIZE into smaller packets.
 * Reassembles fragmented packets into bigger ones. The fragmentation number is prepended
 * to the messages as a header (and removed at the receiving side).<p>
 * Each fragment is identified by (a) the sender (part of the message to which the header is appended),
 * (b) the fragmentation ID (which is unique per FRAG layer (monotonically increasing) and (c) the
 * fragement ID which ranges from 0 to number_of_fragments-1.<p>
 * Requirement: lossless delivery (e.g. NAK, ACK). No requirement on ordering. Works for both unicast and
 * multicast messages.
 * @author Bela Ban
 * @author Filip Hanik
 *
 */
public class FRAG extends Protocol {
    private int frag_size=8192;  // conservative value

    private int max_retained_buffer=70000;

    /*the fragmentation list contains a fragmentation table per sender
     *this way it becomes easier to clean up if a sender (member) leaves or crashes
     */
    private final FragmentationList     fragment_list=new FragmentationList();
    private int                         curr_id=1;
    private final ExposedByteArrayOutputStream bos=new ExposedByteArrayOutputStream(1024);
    private final Vector<Address>       members=new Vector<Address>(11);
    private final static String         name="FRAG";

    long num_sent_msgs=0;
    long num_sent_frags=0;
    long num_received_msgs=0;
    long num_received_frags=0;


    public String getName() {
        return name;
    }

    public int getFragSize() {return frag_size;}
    public void setFragSize(int s) {frag_size=s;}
    public long getNumberOfSentMessages() {return num_sent_msgs;}
    public long getNumberOfSentFragments() {return num_sent_frags;}
    public long getNumberOfReceivedMessages() {return num_received_msgs;}
    public long getNumberOfReceivedFragments() {return num_received_frags;}

    /**
     * Setup the Protocol instance acording to the configuration string
     */
    public boolean setProperties(Properties props) {
        String str;
        
        super.setProperties(props);
        str=props.getProperty("frag_size");
        if(str != null) {
            frag_size=Integer.parseInt(str);
            props.remove("frag_size");
        }

        str=props.getProperty("max_retained_buffer");
        if(str != null) {
            max_retained_buffer=Integer.parseInt(str);
            props.remove("max_retained_buffer");
        }

        if(!props.isEmpty()) {
            log.error("the following properties are not recognized: " + props);
            return false;
        }
        return true;
    }

    public void init() throws Exception {
        super.init();
        Map<String,Object> info=new HashMap<String,Object>(1);
        info.put("frag_size", frag_size);
        up_prot.up(new Event(Event.INFO, info));
        down_prot.down(new Event(Event.INFO, info));
    }

    public void resetStats() {
        super.resetStats();
        num_sent_msgs=num_sent_frags=num_received_msgs=num_received_frags=0;
    }


    /**
     * Fragment a packet if larger than frag_size (add a header). Otherwise just pass down. Only
     * add a header if framentation is needed !
     */
    public Object down(Event evt) {
        switch(evt.getType()) {

        case Event.MSG:
            Message msg=(Message)evt.getArg();
            long size=msg.size();
            num_sent_msgs++;
            if(size > frag_size) {
                if(log.isTraceEnabled()) {
                    StringBuilder sb=new StringBuilder("message size is ");
                    sb.append(size).append(", will fragment (frag_size=").append(frag_size).append(')');
                    log.trace(sb.toString());
                }
                fragment(msg);  // Fragment and pass down
                return null;
            }
            break;

        case Event.VIEW_CHANGE:
            handleViewChange((View)evt.getArg());
            break;

        case Event.CONFIG:
            Object ret=down_prot.down(evt);
            if(log.isDebugEnabled()) log.debug("received CONFIG event: " + evt.getArg());
            handleConfigEvent((Map<String,Object>)evt.getArg());
            return ret;
        }

        return down_prot.down(evt);  // Pass on to the layer below us
    }


    /**
     * If event is a message, if it is fragmented, re-assemble fragments into big message and pass up the stack.
     */
    public Object up(Event evt) {
        switch(evt.getType()) {

        case Event.MSG:
            Message msg=(Message)evt.getArg();
            FragHeader hdr=(FragHeader)msg.getHeader(name);
            if(hdr != null) { // needs to be defragmented
                unfragment(msg, hdr); // Unfragment and possibly pass up
                return null;
            }
            else {
                num_received_msgs++;
            }
            break;

        case Event.VIEW_CHANGE:
            handleViewChange((View)evt.getArg());
            break;

        case Event.CONFIG:
            Object ret=up_prot.up(evt);
            if(log.isDebugEnabled()) log.debug("received CONFIG event: " + evt.getArg());
            handleConfigEvent((Map<String,Object>)evt.getArg());
            return ret;
        }

        return up_prot.up(evt); // Pass up to the layer above us by default
    }

    private void handleViewChange(View view) {
        Vector new_mbrs=view.getMembers(), left_mbrs;
        Address mbr;

        left_mbrs=Util.determineLeftMembers(members, new_mbrs);
        members.clear();
        members.addAll(new_mbrs);

        for(int i=0; i < left_mbrs.size(); i++) {
            mbr=(Address)left_mbrs.elementAt(i);
            //the new view doesn't contain the sender, he must have left,
            //hence we will clear all his fragmentation tables
            fragment_list.remove(mbr);
            if(log.isTraceEnabled())
                log.trace("[VIEW_CHANGE] removed " + mbr + " from fragmentation table");
        }
    }


    /**
     * Send all fragments as separate messages (with same ID !).
     * Example:
     * <pre>
     * Given the generated ID is 2344, number of fragments=3, message {dst,src,buf}
     * would be fragmented into:
     * <p/>
     * [2344,3,0]{dst,src,buf1},
     * [2344,3,1]{dst,src,buf2} and
     * [2344,3,2]{dst,src,buf3}
     * </pre>
     */
    private void fragment(Message msg) {
        DataOutputStream   out=null;
        byte[]             buffer;
        byte[]             fragments[];
        Event              evt;
        FragHeader         hdr;
        Message            frag_msg;
        Address            dest=msg.getDest(), src=msg.getSrc();
        long               id=curr_id++; // used as seqnos
        int                num_frags;

        try {
            // Write message into a byte buffer and fragment it
            // Synchronization around bos is needed for concurrent access (http://jira.jboss.com/jira/browse/JGRP-215)
            synchronized(bos) {
                bos.reset(this.max_retained_buffer);
                out=new DataOutputStream(bos);
                msg.writeTo(out);
                out.flush();
                buffer=bos.getRawBuffer();
                fragments=Util.fragmentBuffer(buffer, frag_size, bos.size());
            }

            num_frags=fragments.length;
            num_sent_frags+=num_frags;

            if(log.isTraceEnabled()) {
                StringBuilder sb=new StringBuilder();
                sb.append("fragmenting packet to ").append(dest != null ? dest.toString() : "<all members>");
                sb.append(" (size=").append(buffer.length).append(") into ").append(num_frags);
                sb.append(" fragment(s) [frag_size=").append(frag_size).append(']');
                log.trace(sb.toString());
            }

            for(int i=0; i < num_frags; i++) {
                frag_msg=new Message(dest, src, fragments[i]);
                hdr=new FragHeader(id, i, num_frags);
                frag_msg.putHeader(name, hdr);
                evt=new Event(Event.MSG, frag_msg);
                down_prot.down(evt);
            }
        }
        catch(Exception e) {
            log.error("exception occurred trying to fragment message", e);
        }
        finally {
            Util.close(out);
        }
    }


    /**
     * 1. Get all the fragment buffers
     * 2. When all are received -> Assemble them into one big buffer
     * 3. Read headers and byte buffer from big buffer
     * 4. Set headers and buffer in msg
     * 5. Pass msg up the stack
     */
    private void unfragment(Message msg, FragHeader hdr) {
        FragmentationTable   frag_table;
        Address              sender=msg.getSrc();
        Message              assembled_msg;
        byte[]               m;
        ByteArrayInputStream bis;
        DataInputStream      in=null;

        frag_table=fragment_list.get(sender);
        if(frag_table == null) {
            frag_table=new FragmentationTable(sender);
            try {
                fragment_list.add(sender, frag_table);
            }
            catch(IllegalArgumentException x) { // the entry has already been added, probably in parallel from another thread
                frag_table=fragment_list.get(sender);
            }
        }
        num_received_frags++;
        m=frag_table.add(hdr.id, hdr.frag_id, hdr.num_frags, msg.getBuffer());
        if(m != null) {
            try {
                bis=new ByteArrayInputStream(m);
                in=new DataInputStream(bis);
                assembled_msg=new Message(false);
                assembled_msg.readFrom(in);
                if(log.isTraceEnabled()) log.trace("assembled_msg is " + assembled_msg);
                assembled_msg.setSrc(sender); // needed ? YES, because fragments have a null src !!
                num_received_msgs++;
                up_prot.up(new Event(Event.MSG, assembled_msg));
            }
            catch(Exception e) {
                log.error("failed unfragmenting a message", e);
            }
            finally {
                Util.close(in);
            }
        }
    }


    void handleConfigEvent(Map<String,Object> map) {
        if(map == null) return;
        if(map.containsKey("frag_size")) {
            frag_size=((Integer)map.get("frag_size")).intValue();
            if(log.isDebugEnabled()) log.debug("setting frag_size=" + frag_size);
        }
    }




    /**
     * A fragmentation list keeps a list of fragmentation tables
     * sorted by an Address ( the sender ).
     * This way, if the sender disappears or leaves the group half way
     * sending the content, we can simply remove this members fragmentation
     * table and clean up the memory of the receiver.
     * We do not have to do the same for the sender, since the sender doesn't keep a fragmentation table
     */
    static class FragmentationList {
        /* initialize the hashtable to hold all the fragmentation tables
         * 11 is the best growth capacity to start with<br/>
         * HashMap<Address,FragmentationTable>
         */
        private final HashMap frag_tables=new HashMap(11);


        /**
         * Adds a fragmentation table for this particular sender
         * If this sender already has a fragmentation table, an IllegalArgumentException
         * will be thrown.
         * @param sender - the address of the sender, cannot be null
         * @param table  - the fragmentation table of this sender, cannot be null
         * @throws IllegalArgumentException if an entry for this sender already exist
         */
        public void add(Address sender, FragmentationTable table) throws IllegalArgumentException {
            FragmentationTable healthCheck;

            synchronized(frag_tables) {
                healthCheck=(FragmentationTable)frag_tables.get(sender);
                if(healthCheck == null) {
                    frag_tables.put(sender, table);
                }
                else {
                    throw new IllegalArgumentException("Sender <" + sender + "> already exists in the fragementation list");
                }
            }
        }

        /**
         * returns a fragmentation table for this sender
         * returns null if the sender doesn't have a fragmentation table
         * @return the fragmentation table for this sender, or null if no table exist
         */
        public FragmentationTable get(Address sender) {
            synchronized(frag_tables) {
                return (FragmentationTable)frag_tables.get(sender);
            }
        }


        /**
         * returns true if this sender already holds a
         * fragmentation for this sender, false otherwise
         * @param sender - the sender, cannot be null
         * @return true if this sender already has a fragmentation table
         */
        public boolean containsSender(Address sender) {
            synchronized(frag_tables) {
                return frag_tables.containsKey(sender);
            }
        }

        /**
         * removes the fragmentation table from the list.
         * after this operation, the fragementation list will no longer
         * hold a reference to this sender's fragmentation table
         * @param sender - the sender who's fragmentation table you wish to remove, cannot be null
         * @return true if the table was removed, false if the sender doesn't have an entry
         */
        public boolean remove(Address sender) {
            synchronized(frag_tables) {
                boolean result=containsSender(sender);
                frag_tables.remove(sender);
                return result;
            }
        }

        /**
         * returns a list of all the senders that have fragmentation tables opened.
         * @return an array of all the senders in the fragmentation list
         */
        public Address[] getSenders() {
            Address[] result;
            int index=0;

            synchronized(frag_tables) {
                result=new Address[frag_tables.size()];
                for(Iterator it=frag_tables.keySet().iterator(); it.hasNext();) {
                    result[index++]=(Address)it.next();
                }
            }
            return result;
        }

        public String toString() {
            Map.Entry entry;
            StringBuilder buf=new StringBuilder("Fragmentation list contains ");
            synchronized(frag_tables) {
                buf.append(frag_tables.size()).append(" tables\n");
                for(Iterator it=frag_tables.entrySet().iterator(); it.hasNext();) {
                    entry=(Map.Entry)it.next();
                    buf.append(entry.getKey()).append(": " ).append(entry.getValue()).append("\n");
                }
            }
            return buf.toString();
        }

    }

    /**
     * Keeps track of the fragments that are received.
     * Reassembles fragements into entire messages when all fragments have been received.
     * The fragmentation holds a an array of byte arrays for a unique sender
     * The first dimension of the array is the order of the fragmentation, in case the arrive out of order
     */
    static class FragmentationTable {
        private final Address sender;
        /* the hashtable that holds the fragmentation entries for this sender*/
        private final Hashtable h=new Hashtable(11);  // keys: frag_ids, vals: Entrys


        FragmentationTable(Address sender) {
            this.sender=sender;
        }


        /**
         * inner class represents an entry for a message
         * each entry holds an array of byte arrays sorted
         * once all the byte buffer entries have been filled
         * the fragmentation is considered complete.
         */
        static class Entry {
            //the total number of fragment in this message
            int tot_frags=0;
            // each fragment is a byte buffer
            byte[] fragments[]=null;
            //the number of fragments we have received
            int number_of_frags_recvd=0;
            // the message ID
            long msg_id=-1;

            /**
             * Creates a new entry
             *
             * @param tot_frags the number of fragments to expect for this message
             */
            Entry(long msg_id, int tot_frags) {
                this.msg_id=msg_id;
                this.tot_frags=tot_frags;
                fragments=new byte[tot_frags][];
                for(int i=0; i < tot_frags; i++) {
                    fragments[i]=null;
                }
            }

            /**
             * adds on fragmentation buffer to the message
             *
             * @param frag_id the number of the fragment being added 0..(tot_num_of_frags - 1)
             * @param frag    the byte buffer containing the data for this fragmentation, should not be null
             */
            public void set(int frag_id, byte[] frag) {
                fragments[frag_id]=frag;
                number_of_frags_recvd++;
            }

            /**
             * returns true if this fragmentation is complete
             * ie, all fragmentations have been received for this buffer
             */
            public boolean isComplete() {
                /*first make the simple check*/
                if(number_of_frags_recvd < tot_frags) {
                    return false;
                }
                /*then double check just in case*/
                for(int i=0; i < fragments.length; i++) {
                    if(fragments[i] == null)
                        return false;
                }
                /*all fragmentations have been received*/
                return true;
            }

            /**
             * Assembles all the fragmentations into one buffer
             * this method does not check if the fragmentation is complete
             *
             * @return the complete message in one buffer
             */
            public byte[] assembleBuffer() {
                return Util.defragmentBuffer(fragments);
            }

            /**
             * debug only
             */
            public String toString() {
                StringBuilder ret=new StringBuilder();
                ret.append("[tot_frags=").append(tot_frags).append(", number_of_frags_recvd=").append(number_of_frags_recvd).append(']');
                return ret.toString();
            }

            public int hashCode() {
                return super.hashCode();
            }
        }


        /**
         * Creates a new entry if not yet present. Adds the fragment.
         * If all fragements for a given message have been received,
         * an entire message is reassembled and returned.
         * Otherwise null is returned.
         *
         * @param id        - the message ID, unique for a sender
         * @param frag_id   the index of this fragmentation (0..tot_frags-1)
         * @param tot_frags the total number of fragmentations expected
         * @param fragment  - the byte buffer for this fragment
         */
        public synchronized byte[] add(long id, int frag_id, int tot_frags, byte[] fragment) {

            /*initialize the return value to default not complete */
            byte[] retval=null;

            Entry e=(Entry)h.get(new Long(id));

            if(e == null) {   // Create new entry if not yet present
                e=new Entry(id, tot_frags);
                h.put(new Long(id), e);
            }

            e.set(frag_id, fragment);
            if(e.isComplete()) {
                retval=e.assembleBuffer();
                h.remove(new Long(id));
            }

            return retval;
        }

        public void reset() {
        }

        public String toString() {
            StringBuilder buf=new StringBuilder("Fragmentation Table Sender:").append(sender).append("\n\t");
            java.util.Enumeration e=this.h.elements();
            while(e.hasMoreElements()) {
                Entry entry=(Entry)e.nextElement();
                int count=0;
                for(int i=0; i < entry.fragments.length; i++) {
                    if(entry.fragments[i] != null) {
                        count++;
                    }
                }
                buf.append("Message ID:").append(entry.msg_id).append("\n\t");
                buf.append("Total Frags:").append(entry.tot_frags).append("\n\t");
                buf.append("Frags Received:").append(count).append("\n\n");
            }
            return buf.toString();
        }
    }

}


