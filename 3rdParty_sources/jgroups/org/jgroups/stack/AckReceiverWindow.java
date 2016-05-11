
package org.jgroups.stack;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Counterpart of AckSenderWindow. Simple FIFO buffer.
 * Every message received is ACK'ed (even duplicates) and added to a hashmap
 * keyed by seqno. The next seqno to be received is stored in <code>next_to_remove</code>. When a message with
 * a seqno less than next_to_remove is received, it will be discarded. The <code>remove()</code> method removes
 * and returns a message whose seqno is equal to next_to_remove, or null if not found.<br>
 * Change May 28 2002 (bela): replaced TreeSet with HashMap. Keys do not need to be sorted, and adding a key to
 * a sorted set incurs overhead.
 *
 * @author Bela Ban
 *
 */
public class AckReceiverWindow {
    long                    next_to_remove=0;
    final Map<Long,Message> msgs=new HashMap<Long,Message>();  // keys: seqnos (Long), values: Messages
    static final Log        log=LogFactory.getLog(AckReceiverWindow.class);
    final ReentrantLock     lock=new ReentrantLock();

    public AckReceiverWindow(long initial_seqno) {
        this.next_to_remove=initial_seqno;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    /** Adds a new message. Message cannot be null
     * @return True if the message was added, false if not (e.g. duplicate, message was already present)
     */
    public boolean add(long seqno, Message msg) {
        if(msg == null)
            throw new IllegalArgumentException("msg must be non-null");
        synchronized(msgs) {
            if(seqno < next_to_remove) {
                if(log.isTraceEnabled())
                    log.trace("discarded msg with seqno=" + seqno + " (next msg to receive is " + next_to_remove + ')');
                return false;
            }
            if(!msgs.containsKey(seqno)) {
                msgs.put(seqno, msg);
                return true;
            }
            else {
                if(log.isTraceEnabled())
                    log.trace("seqno " + seqno + " already received - dropping it");
                return false;
            }
        }
    }


    /**
     * Removes a message whose seqno is equal to <code>next_to_remove</code>, increments the latter.
     * Returns message that was removed, or null, if no message can be removed. Messages are thus
     * removed in order.
     */
    public Message remove() {
        Message retval;

        synchronized(msgs) {
            retval=msgs.remove(next_to_remove);
            if(retval != null) {
                if(log.isTraceEnabled())
                    log.trace("removed seqno=" + next_to_remove);
                next_to_remove++;
            }
        }
        return retval;
    }

    public Message removeOOBMessage() {
        Message retval;

        synchronized(msgs) {
            retval=msgs.get(next_to_remove);
            if(retval != null) {
                if(!retval.isFlagSet(Message.OOB)) {
                    return null;
                }
                retval=msgs.remove(next_to_remove);
                if(log.isTraceEnabled())
                    log.trace("removed OOB message with seqno=" + next_to_remove);
                next_to_remove++;
            }
        }
        return retval;
    }


    public boolean hasMessagesToRemove() {
        synchronized(msgs) {
            return msgs.containsKey(next_to_remove);
        }
    }


    public void reset() {
        synchronized(msgs) {
            msgs.clear();
        }
    }

    public int size() {
        return msgs.size();
    }

    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(msgs.size()).append(" msgs (").append("next=").append(next_to_remove).append(")");
        TreeSet<Long> s=new TreeSet<Long>(msgs.keySet());
        if(!s.isEmpty()) {
            sb.append(" [").append(s.first()).append(" - ").append(s.last()).append("]");
            sb.append(": ").append(s);
        }
        return sb.toString();
    }


    public String printDetails() {
        StringBuilder sb=new StringBuilder();
        sb.append(msgs.size()).append(" msgs (").append("next=").append(next_to_remove).append(")").
                append(", msgs=" ).append(new TreeSet<Long>(msgs.keySet()));
        return sb.toString();
    }


}
