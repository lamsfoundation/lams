package org.jgroups.blocks;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.Address;
import org.jgroups.Message;
import org.jgroups.Transport;
import org.jgroups.View;
import org.jgroups.annotations.GuardedBy;
import org.jgroups.util.Command;
import org.jgroups.util.Rsp;
import org.jgroups.util.RspList;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Sends a message to all members of the group and waits for all responses (or timeout). Returns a
 * boolean value (success or failure). Results (if any) can be retrieved when done.<p>
 * The supported transport to send requests is currently either a RequestCorrelator or a generic
 * Transport. One of them has to be given in the constructor. It will then be used to send a
 * request. When a message is received by either one, the receiveResponse() of this class has to
 * be called (this class does not actively receive requests/responses itself). Also, when a view change
 * or suspicion is received, the methods viewChange() or suspect() of this class have to be called.<p>
 * When started, an array of responses, correlating to the membership, is created. Each response
 * is added to the corresponding field in the array. When all fields have been set, the algorithm
 * terminates.
 * This algorithm can optionally use a suspicion service (failure detector) to detect (and
 * exclude from the membership) fauly members. If no suspicion service is available, timeouts
 * can be used instead (see <code>execute()</code>). When done, a list of suspected members
 * can be retrieved.<p>
 * Because a channel might deliver requests, and responses to <em>different</em> requests, the
 * <code>GroupRequest</code> class cannot itself receive and process requests/responses from the
 * channel. A mechanism outside this class has to do this; it has to determine what the responses
 * are for the message sent by the <code>execute()</code> method and call <code>receiveResponse()</code>
 * to do so.<p>
 * <b>Requirements</b>: lossless delivery, e.g. acknowledgment-based message confirmation.
 * @author Bela Ban
 *
 */
public class GroupRequest implements RspCollector, Command {
    /** return only first response */
    public static final int GET_FIRST=1;

    /** return all responses */
    public static final int GET_ALL=2;

    /** return majority (of all non-faulty members) */
    public static final int GET_MAJORITY=3;

    /** return majority (of all members, may block) */
    public static final int GET_ABS_MAJORITY=4;

    /** return n responses (may block) */
    public static final int GET_N=5;

    /** return no response (async call) */
    public static final int GET_NONE=6;

    private Address caller;

    private final Lock lock=new ReentrantLock();

    /** Is set as soon as the request has received all required responses */
    private final Condition completed=lock.newCondition();

    /** Map<Address, Rsp>. Maps requests and responses */
    @GuardedBy("lock")
    private final Map<Address,Rsp> requests=new HashMap<Address,Rsp>();


    /** bounded queue of suspected members */
    @GuardedBy("lock")
    private final List<Address> suspects=new ArrayList<Address>();

    /** list of members, changed by viewChange() */
    @GuardedBy("lock")
    private final Collection<Address> members=new TreeSet<Address>();

    /** keep suspects vector bounded */
    private static final int max_suspects=40;
    final protected Message request_msg;
    final protected RequestCorrelator corr; // either use RequestCorrelator or ...
    protected Transport transport;    // Transport (one of them has to be non-null)

    protected RspFilter rsp_filter=null;

    protected int rsp_mode=GET_ALL;
    protected boolean done=false;
    protected long timeout=0;
    protected int expected_mbrs=0;

    private static final Log log=LogFactory.getLog(GroupRequest.class);

    /** to generate unique request IDs (see getRequestId()) */
    private static long last_req_id=1;

    private long req_id=-1; // request ID for this request




    /**
     @param m The message to be sent
     @param corr The request correlator to be used. A request correlator sends requests tagged with
     a unique ID and notifies the sender when matching responses are received. The
     reason <code>GroupRequest</code> uses it instead of a <code>Transport</code> is
     that multiple requests/responses might be sent/received concurrently.
     @param members The initial membership. This value reflects the membership to which the request
     is sent (and from which potential responses are expected). Is reset by reset().
     @param rsp_mode How many responses are expected. Can be
     <ol>
     <li><code>GET_ALL</code>: wait for all responses from non-suspected members.
     A suspicion service might warn
     us when a member from which a response is outstanding has crashed, so it can
     be excluded from the responses. If no suspision service is available, a
     timeout can be used (a value of 0 means wait forever). <em>If a timeout of
     0 is used, no suspicion service is available and a member from which we
     expect a response has crashed, this methods blocks forever !</em>.
     <li><code>GET_FIRST</code>: wait for the first available response.
     <li><code>GET_MAJORITY</code>: wait for the majority of all responses. The
     majority is re-computed when a member is suspected.
     <li><code>GET_ABS_MAJORITY</code>: wait for the majority of
     <em>all</em> members.
     This includes failed members, so it may block if no timeout is specified.
     <li><code>GET_N</CODE>: wait for N members.
     Return if n is >= membership+suspects.
     <li><code>GET_NONE</code>: don't wait for any response. Essentially send an
     asynchronous message to the group members.
     </ol>
     */
    public GroupRequest(Message m, RequestCorrelator corr, Vector<Address> members, int rsp_mode) {
        request_msg=m;
        this.corr=corr;
        this.rsp_mode=rsp_mode;
        reset(members);
        // suspects.removeAllElements(); // bela Aug 23 2002: made suspects bounded
    }


    /**
     @param timeout Time to wait for responses (ms). A value of <= 0 means wait indefinitely
     (e.g. if a suspicion service is available; timeouts are not needed).
     */
    public GroupRequest(Message m, RequestCorrelator corr, Vector<Address> members, int rsp_mode,
                        long timeout, int expected_mbrs) {
        this(m, corr, members, rsp_mode);
        if(timeout > 0)
            this.timeout=timeout;
        this.expected_mbrs=expected_mbrs;
    }


    public GroupRequest(Message m, Transport transport, Vector<Address> members, int rsp_mode) {
        corr = null;
        request_msg=m;
        this.transport=transport;
        this.rsp_mode=rsp_mode;
        reset(members);
        // suspects.removeAllElements(); // bela Aug 23 2002: make suspects bounded
    }


    /**
     * @param timeout Time to wait for responses (ms). A value of <= 0 means wait indefinitely
     *                       (e.g. if a suspicion service is available; timeouts are not needed).
     */
    public GroupRequest(Message m, Transport transport, Vector<Address> members,
                        int rsp_mode, long timeout, int expected_mbrs) {
       this(m, transport, members, rsp_mode);
       if(timeout > 0)
          this.timeout=timeout;
       this.expected_mbrs=expected_mbrs;
    }

    public Address getCaller() {
        return caller;
    }

    public void setCaller(Address caller) {
        this.caller=caller;
    }

    public void setResponseFilter(RspFilter filter) {
        rsp_filter=filter;
    }

    public boolean execute() throws Exception {
        return execute(false);
    }

    /**
     * Sends the message. Returns when n responses have been received, or a
     * timeout  has occurred. <em>n</em> can be the first response, all
     * responses, or a majority  of the responses.
     */
    public boolean execute(boolean use_anycasting) throws Exception {
        if(corr == null && transport == null) {
            if(log.isErrorEnabled()) log.error("both corr and transport are null, cannot send group request");
            return false;
        }

        Vector<Address> targets=null;
        lock.lock();
        try {
            targets=new Vector<Address>(members);
            req_id=getRequestId();
            // reset(null); // clear 'responses' array

            for(Address suspect: suspects) { // mark all suspects in 'received' array
                Rsp rsp=requests.get(suspect);
                if(rsp != null) {
                    rsp.setSuspected(true);
                    break; // we can break here because we ensure there are no duplicate members
                }
            }
        }
        finally {
            lock.unlock();
        }

        sendRequest(targets, req_id, use_anycasting);

        lock.lock();
        try {
            done=false;
            boolean retval=collectResponses(timeout);
            if(retval == false && log.isTraceEnabled())
                log.trace("call did not execute correctly, request is " + this.toString());
            return retval;
        }
        finally {
            done=true;
            lock.unlock();
        }
    }



    /**
     * This method sets the <code>membership</code> variable to the value of
     * <code>members</code>. It requires that the caller already hold the
     * <code>rsp_mutex</code> lock.
     * @param mbrs The new list of members
     */
    public final void reset(Vector<Address> mbrs) {
        lock.lock();
        try {
            if(mbrs != null) {
                requests.clear();
                for(Address mbr: mbrs) {
                    requests.put(mbr, new Rsp(mbr));
                }
                // maintain local membership
                this.members.clear();
                this.members.addAll(mbrs);
            }
            else {
                for(Rsp rsp: requests.values()) {
                    rsp.setReceived(false);
                    rsp.setValue(null);
                }
            }
        }
        finally {
            lock.unlock();
        }
    }


    /* ---------------------- Interface RspCollector -------------------------- */
    /**
     * <b>Callback</b> (called by RequestCorrelator or Transport).
     * Adds a response to the response table. When all responses have been received,
     * <code>execute()</code> returns.
     */
    public void receiveResponse(Object response_value, Address sender) {
        lock.lock();
        try {
            if(done) {
                if(log.isTraceEnabled()) log.trace("command is done; cannot add response !");
                return;
            }
            if(suspects.contains(sender)) {
                if(log.isWarnEnabled()) log.warn("received response from suspected member " + sender + "; discarding");
                return;
            }

            if(rsp_filter != null && !rsp_filter.isAcceptable(response_value, sender)) {
                if(!rsp_filter.needMoreResponses()) {
                    done=true;
                    completed.signalAll(); // we're done as we don't need more responses
                }
                return;
            }


            Rsp rsp=requests.get(sender);
            if(rsp != null) {
                if(rsp.wasReceived() == false) {
                    rsp.setValue(response_value);
                    rsp.setReceived(true);
                    if(log.isTraceEnabled())
                        log.trace(new StringBuilder("received response for request ").append(req_id).append(", sender=").
                                append(sender).append(", val=").append(response_value));
                    if(rsp_filter != null && !rsp_filter.needMoreResponses())
                        done=true;
                    completed.signalAll(); // wakes up execute()
                }
            }
        }
        finally {
            lock.unlock();
        }
    }


    /**
     * <b>Callback</b> (called by RequestCorrelator or Transport).
     * Report to <code>GroupRequest</code> that a member is reported as faulty (suspected).
     * This method would probably be called when getting a suspect message from a failure detector
     * (where available). It is used to exclude faulty members from the response list.
     */
    public void suspect(Address suspected_member) {
        if(suspected_member == null)
            return;

        lock.lock();
        try {
            addSuspect(suspected_member);
            Rsp rsp=requests.get(suspected_member);
            if(rsp != null) {
                rsp.setSuspected(true);
                rsp.setValue(null);
                completed.signalAll();
            }
        }
        finally {
            lock.unlock();
        }
    }


    /**
     * Any member of 'membership' that is not in the new view is flagged as
     * SUSPECTED. Any member in the new view that is <em>not</em> in the
     * membership (ie, the set of responses expected for the current RPC) will
     * <em>not</em> be added to it. If we did this we might run into the
     * following problem:
     * <ul>
     * <li>Membership is {A,B}
     * <li>A sends a synchronous group RPC (which sleeps for 60 secs in the
     * invocation handler)
     * <li>C joins while A waits for responses from A and B
     * <li>If this would generate a new view {A,B,C} and if this expanded the
     * response set to {A,B,C}, A would wait forever on C's response because C
     * never received the request in the first place, therefore won't send a
     * response.
     * </ul>
     */
    public void viewChange(View new_view) {
        Address mbr;
        Vector<Address> mbrs=new_view != null? new_view.getMembers() : null;

        lock.lock();
        try {
            if(requests == null || requests.isEmpty() || mbrs == null)
                return;

            this.members.clear();
            this.members.addAll(mbrs);

            Rsp rsp;
            Set<Address> tmp=null;
            for(Map.Entry<Address,Rsp> entry: requests.entrySet()) {
                mbr=entry.getKey();
                if(!mbrs.contains(mbr)) {
                    if(tmp == null)
                        tmp=new HashSet<Address>();
                    tmp.add(mbr);
                    addSuspect(mbr);
                    rsp=entry.getValue();
                    rsp.setValue(null);
                    rsp.setSuspected(true);
                }
            }

            if(tmp != null) {
                for(Address suspect: tmp) {
                    addSuspect(suspect);
                }
                completed.signalAll();
            }
        }
        finally {
            lock.unlock();
        }
    }


    /* -------------------- End of Interface RspCollector ----------------------------------- */



    /** Returns the results as a RspList */
    public RspList getResults() {
        lock.lock();
        try {
            Collection<Rsp> rsps=requests.values();
            return new RspList(rsps);
        }
        finally {
            lock.unlock();
        }
    }


    public String toString() {
        StringBuilder ret=new StringBuilder(128);
        ret.append("[req_id=").append(req_id).append('\n');
        if(caller != null)
            ret.append("caller=").append(caller).append("\n");

        Address mbr;
        Rsp rsp;
        lock.lock();
        try {
            if(!requests.isEmpty()) {
                ret.append("entries:\n");
                for(Map.Entry<Address,Rsp> entry: requests.entrySet()) {
                    mbr=entry.getKey();
                    rsp=entry.getValue();
                    ret.append(mbr).append(": ").append(rsp).append("\n");
                }
            }
        }
        finally {
            lock.unlock();
        }
        return ret.toString();
    }


    public int getNumSuspects() {
        return suspects.size();
    }

    /** Returns the list of suspected members. An attempt to modify the return value will throw an excxeption */
    public Vector<Address> getSuspects() {
        return new Vector<Address>(suspects);
    }


    public boolean isDone() {
        return done;
    }



    /* --------------------------------- Private Methods -------------------------------------*/

    private static int determineMajority(int i) {
        return i < 2? i : (i / 2) + 1;
    }

    /** Generates a new unique request ID */
    private static synchronized long getRequestId() {
        long result=System.currentTimeMillis();
        if(result <= last_req_id) {
            result=last_req_id + 1;
        }
        last_req_id=result;
        return result;
    }

    /** This method runs with lock locked (called by <code>execute()</code>). */
    @GuardedBy("lock")
    private boolean collectResponses(long timeout) throws Exception {
        if(timeout <= 0) {
            while(true) { /* Wait for responses: */
                adjustMembership(); // may not be necessary, just to make sure...
                if(responsesComplete()) {
                    if(corr != null) {
                        corr.done(req_id);
                    }
                    if(log.isTraceEnabled() && rsp_mode != GET_NONE) {
                        log.trace("received all responses: " + toString());
                    }
                    return true;
                }
                try {
                    completed.await();
                }
                catch(Exception e) {
                }
            }
        }
        else {
            long start_time=System.currentTimeMillis();
            long timeout_time=start_time + timeout;
            while(timeout > 0) { /* Wait for responses: */
                if(responsesComplete()) {
                    if(corr != null)
                        corr.done(req_id);
                    if(log.isTraceEnabled() && rsp_mode != GET_NONE) {
                        log.trace("received all responses: " + toString());
                    }
                    return true;
                }
                timeout=timeout_time - System.currentTimeMillis();
                if(timeout > 0) {
                    try {
                        completed.await(timeout, TimeUnit.MILLISECONDS);
                    }
                    catch(Exception e) {
                    }
                }
            }
            if(corr != null) {
                corr.done(req_id);
            }
            if(log.isTraceEnabled())
                log.trace("timed out waiting for responses");

            return false;
        }
    }


    private void sendRequest(Vector<Address> targetMembers, long requestId,boolean use_anycasting) throws Exception {
        try {
            if(log.isTraceEnabled()) log.trace(new StringBuilder("sending request (id=").append(req_id).append(')'));
            if(corr != null) {
                corr.sendRequest(requestId, targetMembers, request_msg, rsp_mode == GET_NONE? null : this, use_anycasting);
            }
            else {
                if(use_anycasting) {
                    for(Address mbr: targetMembers) {
                        Message copy=request_msg.copy(true);
                        copy.setDest(mbr);
                        transport.send(copy);
                    }
                }
                else {
                    transport.send(request_msg);
                }
            }
        }
        catch(Exception ex) {
            if(corr != null)
                corr.done(requestId);
            throw ex;
        }
    }


    @GuardedBy("lock")
    private boolean responsesComplete() {
        int num_received=0, num_not_received=0, num_suspected=0;
        final int num_total=requests.size();

        if(done)
            return true;

        for(Rsp rsp: requests.values()) {
            if(rsp.wasReceived()) {
                num_received++;
            }
            else {
                if(rsp.wasSuspected()) {
                    num_suspected++;
                }
                else {
                    num_not_received++;
                }
            }
        }

        switch(rsp_mode) {
            case GET_FIRST:
                if(num_received > 0)
                    return true;
                if(num_suspected >= num_total)
                // e.g. 2 members, and both suspected
                    return true;
                break;
            case GET_ALL:
                return num_received + num_suspected >= num_total;
            case GET_MAJORITY:
                int majority=determineMajority(num_total);
                if(num_received + num_suspected >= majority)
                    return true;
                break;
            case GET_ABS_MAJORITY:
                majority=determineMajority(num_total);
                if(num_received >= majority)
                    return true;
                break;
            case GET_N:
                if(expected_mbrs >= num_total) {
                    rsp_mode=GET_ALL;
                    return responsesComplete();
                }
                return num_received >= expected_mbrs || num_received + num_not_received < expected_mbrs && num_received + num_suspected >= expected_mbrs;
            case GET_NONE:
                return true;
            default :
                if(log.isErrorEnabled()) log.error("rsp_mode " + rsp_mode + " unknown !");
                break;
        }
        return false;
    }





    /**
     * Adjusts the 'received' array in the following way:
     * <ul>
     * <li>if a member P in 'membership' is not in 'members', P's entry in the 'received' array
     *     will be marked as SUSPECTED
     * <li>if P is 'suspected_mbr', then P's entry in the 'received' array will be marked
     *     as SUSPECTED
     * </ul>
     * This call requires exclusive access to rsp_mutex (called by getResponses() which has
     * a the rsp_mutex locked, so this should not be a problem). This method needs to have lock held.
     */
    @GuardedBy("lock")
    private void adjustMembership() {
        if(requests.isEmpty())
            return;

        Address mbr;
        Rsp rsp;
        for(Map.Entry<Address,Rsp> entry: requests.entrySet()) {
            mbr=entry.getKey();
            if((!this.members.contains(mbr)) || suspects.contains(mbr)) {
                addSuspect(mbr);
                rsp=entry.getValue();
                rsp.setValue(null);
                rsp.setSuspected(true);
            }
        }
    }

    /**
     * Adds a member to the 'suspects' list. Removes oldest elements from 'suspects' list
     * to keep the list bounded ('max_suspects' number of elements), Requires lock to be held
     */
    @GuardedBy("lock")
    private void addSuspect(Address suspected_mbr) {
        if(!suspects.contains(suspected_mbr)) {
            suspects.add(suspected_mbr);
            while(suspects.size() >= max_suspects && !suspects.isEmpty())
                suspects.remove(0); // keeps queue bounded
        }
    }

    private static String modeToString(int m) {
        switch(m) {
            case GET_FIRST: return "GET_FIRST";
            case GET_ALL: return "GET_ALL";
            case GET_MAJORITY: return "GET_MAJORITY";
            case GET_ABS_MAJORITY: return "GET_ABS_MAJORITY";
            case GET_N: return "GET_N";
            case GET_NONE: return "GET_NONE";
            default: return "<unknown> (" + m + ")";
        }
    }


}
