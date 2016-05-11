
package org.jgroups.blocks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.*;
import org.jgroups.protocols.TP;
import org.jgroups.stack.Protocol;
import org.jgroups.stack.StateTransferInfo;
import org.jgroups.util.Rsp;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;


/**
 * Provides synchronous and asynchronous message sending with request-response
 * correlation; i.e., matching responses with the original request.
 * It also offers push-style message reception (by internally using the PullPushAdapter).
 * <p>
 * Channels are simple patterns to asynchronously send a receive messages.
 * However, a significant number of communication patterns in group communication
 * require synchronous communication. For example, a sender would like to send a
 * message to the group and wait for all responses. Or another application would
 * like to send a message to the group and wait only until the majority of the
 * receivers have sent a response, or until a timeout occurred.  MessageDispatcher
 * offers a combination of the above pattern with other patterns.
 * <p>
 * Used on top of channel to implement group requests. Client's <code>handle()</code>
 * method is called when request is received. Is the equivalent of RpcProtocol on
 * the application instead of protocol level.
 *
 * @author Bela Ban
 *
 */
public class MessageDispatcher implements RequestHandler {
    protected Channel channel=null;
    protected RequestCorrelator corr=null;
    protected MessageListener msg_listener=null;
    protected MembershipListener membership_listener=null;
    protected RequestHandler req_handler=null;
    protected ProtocolAdapter prot_adapter=null;
    protected TransportAdapter transport_adapter=null;
    protected final Collection members=new TreeSet();
    protected Address local_addr=null;
    protected boolean deadlock_detection=false;
    protected PullPushAdapter adapter=null;
    protected PullPushHandler handler=null;
    protected Serializable id=null;
    protected final Log log=LogFactory.getLog(getClass());


    /**
     * Process items on the queue concurrently (RequestCorrelator). The default is to wait until the processing of an
     * item has completed before fetching the next item from the queue. Note that setting this to true may destroy the
     * properties of a protocol stack, e.g total or causal order may not be guaranteed. Set this to true only if you
     * know what you're doing !
     */
    protected boolean concurrent_processing=false;


    public MessageDispatcher() {
    }

    public MessageDispatcher(Channel channel, MessageListener l, MembershipListener l2) {
        this.channel=channel;
        prot_adapter=new ProtocolAdapter();
        if(channel != null) {
            local_addr=channel.getLocalAddress();
        }
        setMessageListener(l);
        setMembershipListener(l2);
        if(channel != null) {
            channel.setUpHandler(prot_adapter);
        }
        start();
    }


    public MessageDispatcher(Channel channel, MessageListener l, MembershipListener l2, boolean deadlock_detection) {
        this.channel=channel;
        this.deadlock_detection=deadlock_detection;
        prot_adapter=new ProtocolAdapter();
        if(channel != null) {
            local_addr=channel.getLocalAddress();
        }
        setMessageListener(l);
        setMembershipListener(l2);
        if(channel != null) {
            channel.setUpHandler(prot_adapter);
        }
        start();
    }

    public MessageDispatcher(Channel channel, MessageListener l, MembershipListener l2,
                             boolean deadlock_detection, boolean concurrent_processing) {
        this.channel=channel;
        this.deadlock_detection=deadlock_detection;
        this.concurrent_processing=concurrent_processing;
        prot_adapter=new ProtocolAdapter();
        if(channel != null) {
            local_addr=channel.getLocalAddress();
        }
        setMessageListener(l);
        setMembershipListener(l2);
        if(channel != null) {
            channel.setUpHandler(prot_adapter);
        }
        start();
    }


    public MessageDispatcher(Channel channel, MessageListener l, MembershipListener l2, RequestHandler req_handler) {
        this(channel, l, l2);
        setRequestHandler(req_handler);
    }


    public MessageDispatcher(Channel channel, MessageListener l, MembershipListener l2, RequestHandler req_handler,
                             boolean deadlock_detection) {
        this(channel, l, l2, deadlock_detection, false);
        setRequestHandler(req_handler);
    }

    public MessageDispatcher(Channel channel, MessageListener l, MembershipListener l2, RequestHandler req_handler,
                             boolean deadlock_detection, boolean concurrent_processing) {
        this(channel, l, l2, deadlock_detection, concurrent_processing);
        setRequestHandler(req_handler);
    }


    /*
     * Uses a user-provided PullPushAdapter rather than a Channel as transport. If id is non-null, it will be
     * used to register under that id. This is typically used when another building block is already using
     * PullPushAdapter, and we want to add this building block in addition. The id is the used to discriminate
     * between messages for the various blocks on top of PullPushAdapter. If null, we will assume we are the
     * first block created on PullPushAdapter.
     * @param adapter The PullPushAdapter which to use as underlying transport
     * @param id A serializable object (e.g. an Integer) used to discriminate (multiplex/demultiplex) between
     *           requests/responses for different building blocks on top of PullPushAdapter.
     */
    public MessageDispatcher(PullPushAdapter adapter, Serializable id,
                             MessageListener l, MembershipListener l2) {
        this.adapter=adapter;
        this.id=id;
        setMembers(((Channel) adapter.getTransport()).getView().getMembers());
        setMessageListener(l);
        setMembershipListener(l2);
        handler=new PullPushHandler();
        transport_adapter=new TransportAdapter();
        adapter.addMembershipListener(handler); // remove in stop()
        if(id == null) { // no other building block around, let's become the main consumer of this PullPushAdapter
            adapter.setListener(handler);
        }
        else {
            adapter.registerListener(id, handler);
        }

        Transport tp;
        if((tp=adapter.getTransport()) instanceof Channel) {
            local_addr=((Channel) tp).getLocalAddress();
        }
        start();
    }


    /*
     * Uses a user-provided PullPushAdapter rather than a Channel as transport. If id is non-null, it will be
     * used to register under that id. This is typically used when another building block is already using
     * PullPushAdapter, and we want to add this building block in addition. The id is the used to discriminate
     * between messages for the various blocks on top of PullPushAdapter. If null, we will assume we are the
     * first block created on PullPushAdapter.
     * @param adapter The PullPushAdapter which to use as underlying transport
     * @param id A serializable object (e.g. an Integer) used to discriminate (multiplex/demultiplex) between
     *           requests/responses for different building blocks on top of PullPushAdapter.
     * @param req_handler The object implementing RequestHandler. It will be called when a request is received
     */
    public MessageDispatcher(PullPushAdapter adapter, Serializable id,
                             MessageListener l, MembershipListener l2,
                             RequestHandler req_handler) {
        this.adapter=adapter;
        this.id=id;
        setMembers(((Channel) adapter.getTransport()).getView().getMembers());
        setRequestHandler(req_handler);
        setMessageListener(l);
        setMembershipListener(l2);
        handler=new PullPushHandler();
        transport_adapter=new TransportAdapter();
        adapter.addMembershipListener(handler);
        if(id == null) { // no other building block around, let's become the main consumer of this PullPushAdapter
            adapter.setListener(handler);
        }
        else {
            adapter.registerListener(id, handler);
        }

        Transport tp;
        if((tp=adapter.getTransport()) instanceof Channel) {
            local_addr=((Channel) tp).getLocalAddress(); // fixed bug #800774
        }

        start();
    }


    public MessageDispatcher(PullPushAdapter adapter, Serializable id,
                             MessageListener l, MembershipListener l2,
                             RequestHandler req_handler, boolean concurrent_processing) {
        this.concurrent_processing=concurrent_processing;
        this.adapter=adapter;
        this.id=id;
        setMembers(((Channel) adapter.getTransport()).getView().getMembers());
        setRequestHandler(req_handler);
        setMessageListener(l);
        setMembershipListener(l2);
        handler=new PullPushHandler();
        transport_adapter=new TransportAdapter();
        adapter.addMembershipListener(handler);
        if(id == null) { // no other building block around, let's become the main consumer of this PullPushAdapter
            adapter.setListener(handler);
        }
        else {
            adapter.registerListener(id, handler);
        }

        Transport tp;
        if((tp=adapter.getTransport()) instanceof Channel) {
            local_addr=((Channel) tp).getLocalAddress(); // fixed bug #800774
        }

        start();
    }

    /** Returns a copy of members */
    protected Collection getMembers() {
        synchronized(members) {
            return new ArrayList(members);
        }
    }


    /**
     * If this dispatcher is using a user-provided PullPushAdapter, then need to set the members from the adapter
     * initially since viewChange has most likely already been called in PullPushAdapter.
     */
    private void setMembers(Vector new_mbrs) {
        if(new_mbrs != null) {
            synchronized(members) {
                members.clear();
                members.addAll(new_mbrs);
            }
        }
    }

    public boolean getDeadlockDetection() {return deadlock_detection;}

    public void setDeadlockDetection(boolean flag) {
        deadlock_detection=flag;
        if(corr != null)
            corr.setDeadlockDetection(flag);
    }


    public boolean getConcurrentProcessing() {return concurrent_processing;}

    public void setConcurrentProcessing(boolean flag) {
        this.concurrent_processing=flag;
    }


    public final void start() {
        if(corr == null) {
            if(transport_adapter != null) {
                corr=new RequestCorrelator("MsgDisp", transport_adapter,
                                           this, deadlock_detection, local_addr, concurrent_processing);
            }
            else {
                corr=new RequestCorrelator("MsgDisp", prot_adapter,
                                           this, deadlock_detection, local_addr, concurrent_processing);
            }
        }
        correlatorStarted();
        corr.start();

        if(channel != null) {
            Vector tmp_mbrs=channel.getView() != null ? channel.getView().getMembers() : null;
            setMembers(tmp_mbrs);
            if(channel instanceof JChannel) {
                TP transport=((JChannel)channel).getProtocolStack().getTransport();
                corr.registerProbeHandler(transport);
            }
        }
    }

    protected void correlatorStarted() {
        ;
    }


    public void stop() {
        if(corr != null) {
            corr.stop();
        }

        if(channel instanceof JChannel) {
            TP transport=((JChannel)channel).getProtocolStack().getTransport();
            corr.unregisterProbeHandler(transport);
        }

        // fixes leaks of MembershipListeners (http://jira.jboss.com/jira/browse/JGRP-160)
        if(adapter != null && handler != null) {
            adapter.removeMembershipListener(handler);
        }
    }


    public final void setMessageListener(MessageListener l) {
        msg_listener=l;
    }

    /**
     * Gives access to the currently configured MessageListener. Returns null if there is no
     * configured MessageListener.
     */
    public MessageListener getMessageListener() {
        return msg_listener;
    }

    public final void setMembershipListener(MembershipListener l) {
        membership_listener=l;
    }

    public final void setRequestHandler(RequestHandler rh) {
        req_handler=rh;
    }

    /**
     * Offers access to the underlying Channel.
     * @return a reference to the underlying Channel.
     */
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel ch) {
        if(ch == null)
            return;
        this.channel=ch;
        local_addr=channel.getLocalAddress();
        if(prot_adapter == null)
            prot_adapter=new ProtocolAdapter();
        channel.setUpHandler(prot_adapter);
    }


    public void send(Message msg) throws ChannelNotConnectedException, ChannelClosedException {
        if(channel != null) {
            channel.send(msg);
        }
        else
            if(adapter != null) {
                try {
                    if(id != null) {
                        adapter.send(id, msg);
                    }
                    else {
                        adapter.send(msg);
                    }
                }
                catch(Throwable ex) {
                    if(log.isErrorEnabled()) {
                        log.error("exception=" + Util.print(ex));
                    }
                }
            }
            else {
                if(log.isErrorEnabled()) {
                    log.error("channel == null");
                }
            }
    }


    public RspList castMessage(final Vector dests, Message msg, int mode, long timeout) {
        return castMessage(dests, msg, mode, timeout, false);
    }


    /**
     * Cast a message to all members, and wait for <code>mode</code> responses. The responses are returned in a response
     * list, where each response is associated with its sender.<p> Uses <code>GroupRequest</code>.
     *
     * @param dests   The members to which the message is to be sent. If it is null, then the message is sent to all
     *                members
     * @param msg     The message to be sent to n members
     * @param mode    Defined in <code>GroupRequest</code>. The number of responses to wait for: <ol> <li>GET_FIRST:
     *                return the first response received. <li>GET_ALL: wait for all responses (minus the ones from
     *                suspected members) <li>GET_MAJORITY: wait for a majority of all responses (relative to the grp
     *                size) <li>GET_ABS_MAJORITY: wait for majority (absolute, computed once) <li>GET_N: wait for n
     *                responses (may block if n > group size) <li>GET_NONE: wait for no responses, return immediately
     *                (non-blocking) </ol>
     * @param timeout If 0: wait forever. Otherwise, wait for <code>mode</code> responses <em>or</em> timeout time.
     * @return RspList A list of responses. Each response is an <code>Object</code> and associated to its sender.
     */
    public RspList castMessage(final Vector dests, Message msg, int mode, long timeout, boolean use_anycasting) {
        return castMessage(dests, msg, mode, timeout, use_anycasting, null);
    }


    public RspList castMessage(final Vector dests, Message msg, int mode, long timeout, boolean use_anycasting,
                               RspFilter filter) {
        GroupRequest _req=null;
        Vector real_dests;
        Channel tmp;

        // we need to clone because we don't want to modify the original
        // (we remove ourselves if LOCAL is false, see below) !
        // real_dests=dests != null ? (Vector) dests.clone() : (members != null ? new Vector(members) : null);
        if(dests != null) {
            real_dests=(Vector)dests.clone();
            real_dests.retainAll(this.members);
        }
        else {
            synchronized(members) {
                real_dests=new Vector(members);
            }
        }

        // if local delivery is off, then we should not wait for the message from the local member.
        // therefore remove it from the membership
        tmp=channel;
        if(tmp == null) {
            if(adapter != null && adapter.getTransport() instanceof Channel) {
                tmp=(Channel) adapter.getTransport();
            }
        }

        if(tmp != null && tmp.getOpt(Channel.LOCAL).equals(Boolean.FALSE)) {
            if(local_addr == null) {
                local_addr=tmp.getLocalAddress();
            }
            if(local_addr != null) {
                real_dests.removeElement(local_addr);
            }
        }

        // don't even send the message if the destination list is empty
        if(log.isTraceEnabled())
            log.trace("real_dests=" + real_dests);

        if(real_dests.isEmpty()) {
            if(log.isTraceEnabled())
                log.trace("destination list is empty, won't send message");
            return new RspList(); // return empty response list
        }

        _req=new GroupRequest(msg, corr, real_dests, mode, timeout, 0);
        _req.setCaller(this.local_addr);
        _req.setResponseFilter(filter);
        try {
            _req.execute(use_anycasting);
        }
        catch(Exception ex) {
            throw new RuntimeException("failed executing request " + _req, ex);
        }

        return _req.getResults();
    }


    /**
     * Multicast a message request to all members in <code>dests</code> and receive responses via the RspCollector
     * interface. When done receiving the required number of responses, the caller has to call done(req_id) on the
     * underlyinh RequestCorrelator, so that the resources allocated to that request can be freed.
     *
     * @param dests  The list of members from which to receive responses. Null means all members
     * @param req_id The ID of the request. Used by the underlying RequestCorrelator to correlate responses with
     *               requests
     * @param msg    The request to be sent
     * @param coll   The sender needs to provide this interface to collect responses. Call will return immediately if
     *               this is null
     */
    public void castMessage(final Vector dests, long req_id, Message msg, RspCollector coll) {
        Vector real_dests;
        Channel tmp;

        if(msg == null) {
            if(log.isErrorEnabled())
                log.error("request is null");
            return;
        }

        if(coll == null) {
            if(log.isErrorEnabled())
                log.error("response collector is null (must be non-null)");
            return;
        }

        // we need to clone because we don't want to modify the original
        // (we remove ourselves if LOCAL is false, see below) !
        //real_dests=dests != null ? (Vector) dests.clone() : (Vector) members.clone();
        if(dests != null) {
            real_dests=(Vector)dests.clone();
            real_dests.retainAll(this.members);
        }
        else {
            synchronized(members) {
                real_dests=new Vector(members);
            }
        }

        // if local delivery is off, then we should not wait for the message from the local member.
        // therefore remove it from the membership
        tmp=channel;
        if(tmp == null) {
            if(adapter != null && adapter.getTransport() instanceof Channel) {
                tmp=(Channel) adapter.getTransport();
            }
        }

        if(tmp != null && tmp.getOpt(Channel.LOCAL).equals(Boolean.FALSE)) {
            if(local_addr == null) {
                local_addr=tmp.getLocalAddress();
            }
            if(local_addr != null) {
                real_dests.removeElement(local_addr);
            }
        }

        // don't even send the message if the destination list is empty
        if(real_dests.isEmpty()) {
            if(log.isDebugEnabled())
                log.debug("destination list is empty, won't send message");
            return;
        }

        try {
            corr.sendRequest(req_id, real_dests, msg, coll);
        }
        catch(Exception e) {
            throw new RuntimeException("failure sending request " + req_id + " to " + real_dests, e);
        }
    }


    public void done(long req_id) {
        corr.done(req_id);
    }


    /**
     * Sends a message to a single member (destination = msg.dest) and returns the response. The message's destination
     * must be non-zero !
     */
    public Object sendMessage(Message msg, int mode, long timeout) throws TimeoutException, SuspectedException {
        Vector mbrs=new Vector();
        RspList rsp_list=null;
        Object dest=msg.getDest();
        Rsp rsp;
        GroupRequest _req=null;

        if(dest == null) {
            if(log.isErrorEnabled())
                log.error("the message's destination is null, cannot send message");
            return null;
        }

        mbrs.addElement(dest);   // dummy membership (of destination address)

        _req=new GroupRequest(msg, corr, mbrs, mode, timeout, 0);
        _req.setCaller(local_addr);
        try {
            _req.execute();
        }
        catch(Exception t) {
            throw new RuntimeException("failed executing request " + _req, t);
        }

        if(mode == GroupRequest.GET_NONE) {
            return null;
        }

        rsp_list=_req.getResults();

        if(rsp_list.isEmpty()) {
            if(log.isWarnEnabled())
                log.warn(" response list is empty");
            return null;
        }
        if(rsp_list.size() > 1) {
            if(log.isWarnEnabled())
                log.warn("response list contains more that 1 response; returning first response !");
        }
        rsp=(Rsp)rsp_list.elementAt(0);
        if(rsp.wasSuspected()) {
            throw new SuspectedException(dest);
        }
        if(!rsp.wasReceived()) {
            throw new TimeoutException("timeout sending message to " + dest);
        }
        return rsp.getValue();
    }



    /* ------------------------ RequestHandler Interface ---------------------- */
    public Object handle(Message msg) {
        if(req_handler != null) {
            return req_handler.handle(msg);
        }
        else {
            return null;
        }
    }
    /* -------------------- End of RequestHandler Interface ------------------- */






    class ProtocolAdapter extends Protocol implements UpHandler {


        /* ------------------------- Protocol Interface --------------------------- */

        public String getName() {
            return "MessageDispatcher";
        }



        private Object handleUpEvent(Event evt) {
            switch(evt.getType()) {
                case Event.MSG:
                    if(msg_listener != null) {
                        msg_listener.receive((Message) evt.getArg());
                    }
                    break;

                case Event.GET_APPLSTATE: // reply with GET_APPLSTATE_OK
                    StateTransferInfo info=(StateTransferInfo)evt.getArg();
                    String state_id=info.state_id;
                    byte[] tmp_state=null;
                    if(msg_listener != null) {
                        try {
                            if(msg_listener instanceof ExtendedMessageListener && state_id!=null) {
                                tmp_state=((ExtendedMessageListener)msg_listener).getState(state_id);
                            }
                            else {
                                tmp_state=msg_listener.getState();
                            }
                        }
                        catch(Throwable t) {
                            this.log.error("failed getting state from message listener (" + msg_listener + ')', t);
                        }
                    }
                    return new StateTransferInfo(null, state_id, 0L, tmp_state);

                case Event.GET_STATE_OK:
                    if(msg_listener != null) {
                        try {
                            info=(StateTransferInfo)evt.getArg();
                            String id=info.state_id;
                            if(msg_listener instanceof ExtendedMessageListener && id!=null) {
                                ((ExtendedMessageListener)msg_listener).setState(id, info.state);
                            }
                            else {
                                msg_listener.setState(info.state);
                            }
                        }
                        catch(ClassCastException cast_ex) {
                            if(this.log.isErrorEnabled())
                                this.log.error("received SetStateEvent, but argument " +
                                        evt.getArg() + " is not serializable. Discarding message.");
                        }
                    }
                    break;

                case Event.STATE_TRANSFER_OUTPUTSTREAM:
                    StateTransferInfo sti=(StateTransferInfo)evt.getArg();
                    OutputStream os=sti.outputStream;
                    if(msg_listener instanceof ExtendedMessageListener) {                        
                        if(os != null && msg_listener instanceof ExtendedMessageListener) {
                            if(sti.state_id == null)
                                ((ExtendedMessageListener)msg_listener).getState(os);
                            else
                                ((ExtendedMessageListener)msg_listener).getState(sti.state_id, os);
                        }
                        return new StateTransferInfo(null, os, sti.state_id);
                    }
                    else if(msg_listener instanceof MessageListener){
                	if(log.isWarnEnabled()){
                	    log.warn("Channel has STREAMING_STATE_TRANSFER, however,"
                	             + " application does not implement ExtendedMessageListener. State is not transfered");
                	    Util.close(os);
                	}
		    }
                    break;

                case Event.STATE_TRANSFER_INPUTSTREAM:
                    sti=(StateTransferInfo)evt.getArg();
                    InputStream is=sti.inputStream;
                    if(msg_listener instanceof ExtendedMessageListener) {                    	
                        if(is!=null && msg_listener instanceof ExtendedMessageListener) {
                            if(sti.state_id == null)
                                ((ExtendedMessageListener)msg_listener).setState(is);
                            else
                                ((ExtendedMessageListener)msg_listener).setState(sti.state_id, is);
                        }
                    }
                    else if(msg_listener instanceof MessageListener){
                	if(log.isWarnEnabled()){
                	    log.warn("Channel has STREAMING_STATE_TRANSFER, however,"
                	             + " application does not implement ExtendedMessageListener. State is not transfered");
                	    Util.close(is);
                	}
		    }                    
                    break;

                case Event.VIEW_CHANGE:
                    View v=(View) evt.getArg();
                    Vector new_mbrs=v.getMembers();
                    setMembers(new_mbrs);
                    if(membership_listener != null) {
                        membership_listener.viewAccepted(v);
                    }
                    break;

                case Event.SET_LOCAL_ADDRESS:
                    if(log.isTraceEnabled())
                        log.trace("setting local_addr (" + local_addr + ") to " + evt.getArg());
                    local_addr=(Address)evt.getArg();
                    break;

                case Event.SUSPECT:
                    if(membership_listener != null) {
                        membership_listener.suspect((Address) evt.getArg());
                    }
                    break;

                case Event.BLOCK:
                    if(membership_listener != null) {
                        membership_listener.block();
                    }
                    channel.blockOk();
                    break;
                case Event.UNBLOCK:
                   if(membership_listener instanceof ExtendedMembershipListener) {
                      ((ExtendedMembershipListener)membership_listener).unblock();
                   }
                   break;
            }

            return null;
        }






        /**
         * Called by channel (we registered before) when event is received. This is the UpHandler interface.
         */
        public Object up(Event evt) {
            if(corr != null) {
                if(!corr.receive(evt)) {
                    return handleUpEvent(evt);
                }
            }
            else {
                if(log.isErrorEnabled()) { //Something is seriously wrong, correlator should not be null since latch is not locked!
                    log.error("correlator is null, event will be ignored (evt=" + evt + ")");
                }
            }
            return null;
        }



        public Object down(Event evt) {
            if(channel != null) {
                return channel.downcall(evt);
            }
            else
                if(this.log.isWarnEnabled()) {
                    this.log.warn("channel is null, discarding event " + evt);
                }
            return null;
        }


        /* ----------------------- End of Protocol Interface ------------------------ */

    }


    class TransportAdapter implements Transport {

        public void send(Message msg) throws Exception {
            if(channel != null) {
                channel.send(msg);
            }
            else
                if(adapter != null) {
                    try {
                        if(id != null) {
                            adapter.send(id, msg);
                        }
                        else {
                            adapter.send(msg);
                        }
                    }
                    catch(Throwable ex) {
                        if(log.isErrorEnabled()) {
                            log.error("exception=" + Util.print(ex));
                        }
                    }
                }
                else {
                    if(log.isErrorEnabled()) {
                        log.error("channel == null");
                    }
                }
        }

        public Object receive(long timeout) throws Exception {
            return null; // not supported and not needed
        }
    }


    class PullPushHandler implements ExtendedMessageListener, MembershipListener {


        /* ------------------------- MessageListener interface ---------------------- */
        public void receive(Message msg) {
            boolean consumed=false;
            if(corr != null) {
                consumed=corr.receiveMessage(msg);
            }

            if(!consumed) {   // pass on to MessageListener
                if(msg_listener != null) {
                    msg_listener.receive(msg);
                }
            }
        }

        public byte[] getState() {
            return msg_listener != null ? msg_listener.getState() : null;
        }

        public byte[] getState(String state_id) {
            if(msg_listener == null) return null;
            if(msg_listener instanceof ExtendedMessageListener && state_id!=null) {
                return ((ExtendedMessageListener)msg_listener).getState(state_id);
            }
            else {
                return msg_listener.getState();
            }
        }

        public void setState(byte[] state) {
            if(msg_listener != null) {
                msg_listener.setState(state);
            }
        }

        public void setState(String state_id, byte[] state) {
            if(msg_listener != null) {
                if(msg_listener instanceof ExtendedMessageListener && state_id!=null) {
                    ((ExtendedMessageListener)msg_listener).setState(state_id, state);
                }
                else {
                    msg_listener.setState(state);
                }
            }
        }

        public void getState(OutputStream ostream) {
            if (msg_listener instanceof ExtendedMessageListener) {
                ((ExtendedMessageListener) msg_listener).getState(ostream);
            }
        }

        public void getState(String state_id, OutputStream ostream) {
            if (msg_listener instanceof ExtendedMessageListener && state_id!=null) {
                ((ExtendedMessageListener) msg_listener).getState(state_id,ostream);
            }

        }

        public void setState(InputStream istream) {
            if (msg_listener instanceof ExtendedMessageListener) {
                ((ExtendedMessageListener) msg_listener).setState(istream);
            }
        }

        public void setState(String state_id, InputStream istream) {
            if (msg_listener instanceof ExtendedMessageListener && state_id != null) {
                ((ExtendedMessageListener) msg_listener).setState(state_id,istream);
            }
        }
        /*
		 * --------------------- End of MessageListener interface
		 * -------------------
		 */


        /* ------------------------ MembershipListener interface -------------------- */
        public void viewAccepted(View v) {
            if(corr != null) {
                corr.receiveView(v);
            }

            Vector new_mbrs=v.getMembers();
            setMembers(new_mbrs);
            if(membership_listener != null) {
                membership_listener.viewAccepted(v);
            }
        }

        public void suspect(Address suspected_mbr) {
            if(corr != null) {
                corr.receiveSuspect(suspected_mbr);
            }
            if(membership_listener != null) {
                membership_listener.suspect(suspected_mbr);
            }
        }

        public void block() {
            if(membership_listener != null) {
                membership_listener.block();
            }
        }

        /* --------------------- End of MembershipListener interface ---------------- */



        // @todo: receive SET_LOCAL_ADDR event and call corr.setLocalAddress(addr)

    }


}
