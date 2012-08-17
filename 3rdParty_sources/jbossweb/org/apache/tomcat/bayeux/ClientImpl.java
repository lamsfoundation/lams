/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tomcat.bayeux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.cometd.bayeux.Bayeux;
import org.apache.cometd.bayeux.Client;
import org.apache.cometd.bayeux.Listener;
import org.apache.cometd.bayeux.Message;
import org.apache.tomcat.util.json.JSONObject;
import org.jboss.logging.Logger;
import org.jboss.servlet.http.HttpEvent;

public class ClientImpl implements Client {
    
    private static Logger log = Logger.getLogger(ClientImpl.class);

    public static final int SUPPORT_CALLBACK_POLL = 0x1;
    public static final int SUPPORT_LONG_POLL = 0x2; 

    public static final String COMET_EVENT_ATTR = "org.apache.cometd.bayeux.client";
    
    protected static LinkedList<Message> EMPTY_LIST = new LinkedList<Message>();
    /**
     * queued message for remote clients.
     */
    protected LinkedList<Message> messages = null;
    
    /**
     * Currently associated event.
     */
    protected HttpEvent event;
    
    /**
     * Unique id representing this client
     */
    protected String id;
    
    /**
     * supported connection types, defaults to long-polling
     */
    protected int supportedConnTypes = SUPPORT_LONG_POLL | SUPPORT_CALLBACK_POLL;
    
    /**
     * The desired connection type
     */
    protected int desirectConnType = SUPPORT_LONG_POLL;
    
    /**
     * Does this client use json-comment-filtered messages
     */
    protected boolean useJsonFiltered = false;
    
    /**
     * Same JVM clients, get local=true
     */
    protected boolean local;
    
    /**
     * The callback object for local clients
     */
    protected Listener listener;
    
    protected AtomicInteger nrofsubscriptions = new AtomicInteger(0);
    
    protected ClientImpl(String id, boolean local) {
        this.id = id;
        this.local = local;
        if (!local) messages = new LinkedList<Message>();
    }
    
/*    protected ClientImpl(String id, HttpEvent event) {
        this(id,false);
        addCometEvent(event);
    }*/

    public void deliver(Message message) {
        deliverInternal(null,new MessageImpl[] {(MessageImpl)message});
    }
    
    public void deliver(Message[] message) {
        deliverInternal(null,message);
    }

    protected void deliverInternal(ChannelImpl channel, MessageImpl message) {
        deliverInternal(channel,new MessageImpl[] {message});
    }

    protected synchronized void deliverInternal(ChannelImpl channel, Message[] msgs) {
        if (isLocal()) {
            //local clients must have a listener
            ArrayList<Message> list = new ArrayList<Message>();
            for (int i=0; msgs!=null && i<msgs.length; i++) {
                //dont deliver to ourselves
                if (this!=msgs[i].getClient()) list.add(msgs[i]);
            }
            if (getListener() != null && list.size()>0) {
                getListener().deliver(list.toArray(new Message[0]));
            }
        } else {
            for (int i=0; msgs!=null && i<msgs.length; i++) {
                MessageImpl message = (MessageImpl)msgs[i];
                if (this==message.getClient()) { 
                    //dont deliver to ourself
                    continue;
                }
                //we are not implementing forever responses, if the client is connected
                //then we will fire off the message
                //first we check to see if we have any existing connections we can piggy back on
                boolean delivered = false;
                if (event!=null) {
                    RequestBase rq = (RequestBase)event.getHttpServletRequest().getAttribute(RequestBase.LAST_REQ_ATTR);
                    if (rq!=null) {
                        Map map = new HashMap();
                        try {
                            map.put(Bayeux.CHANNEL_FIELD,message.getChannel().getId());
                            map.put(Bayeux.DATA_FIELD,message);
                            JSONObject json = new JSONObject(map);
                            if (log.isTraceEnabled()) {
                                log.trace("Message instantly delivered to remote client["+this+"] message:"+json);
                            }
                            rq.addToDeliveryQueue(this, json);
                            //deliver the batch
                            if (i==(msgs.length-1)) {
                                rq.deliver(event, this);
                                event.close(); //todo, figure out a better way, this means only one message gets delivered
                                removeCometEvent(event); //and delivered instantly
                            }
                            delivered = true;
                        } catch (Exception e) {
                            // TODO: fix
                            log.warn("Exception", e);
                        }
                    }
                }
                if (!delivered) {
                    if (log.isTraceEnabled()) {
                        log.trace("Message added to queue for remote client["+this+"] message:"+message);
                    }
                    //queue the message for the next round
                    messages.add(message);
                }
            }
        }
    }

    public String getId() {
        return this.id;
    }

    protected Listener getListener() {
        return listener;
    }

    public boolean hasMessages() {
        if (isLocal()) return false;
        else {
            return messages.size() > 0;
        }
    }

    public boolean isLocal() {
        return local;
    }

    public int getSupportedConnTypes() {
        return supportedConnTypes;
    }

    public int getDesirectConnType() {
        return desirectConnType;
    }

    public boolean useJsonFiltered() {
        return useJsonFiltered;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setSupportedConnTypes(int supportedConnTypes) {
        this.supportedConnTypes = supportedConnTypes;
    }

    public void setUseJsonFiltered(boolean useJsonFiltered) {
        this.useJsonFiltered = useJsonFiltered;
    }

    public void setDesirectConnType(int desirectConnType) {
        this.desirectConnType = desirectConnType;
    }

    public boolean supportsCallbackPoll() {
        return (supportedConnTypes & SUPPORT_CALLBACK_POLL) == SUPPORT_CALLBACK_POLL;
    }

    public boolean supportsLongPoll() {
        return (supportedConnTypes & SUPPORT_LONG_POLL) == SUPPORT_LONG_POLL;
    }

    public synchronized List<Message> takeMessages() {
        if (isLocal()) return null;
        if (messages.size()==0) return EMPTY_LIST;
        List result = new LinkedList(messages);
        messages.clear();
        return result;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer(super.toString());
        buf.append(" id=").append(getId());
        return buf.toString();
    }
    
    public boolean isSubscribed() {
        return nrofsubscriptions.get()>0;
    }
    
    protected void addCometEvent(HttpEvent event) {
        if (this.event != null) {
            try {
                this.event.close();
            } catch (IOException e) {
                // Nothing
            }
        }
        this.event = event;
        event.getHttpServletRequest().setAttribute(COMET_EVENT_ATTR,this);
    }
    
    protected void removeCometEvent(HttpEvent event) {
        if (this.event != null && this.event == event) {
            this.event = null;
        }
        event.getHttpServletRequest().removeAttribute(COMET_EVENT_ATTR);
    }
    
    protected void subscribed(ChannelImpl ch) {
        nrofsubscriptions.addAndGet(1);
    }
    
    protected void unsubscribed(ChannelImpl ch) {
        nrofsubscriptions.addAndGet(-1);
    }
    
    public void startBatch(){
        //noop until improved
    }
    public void endBatch() {
        //noop until improved
    }
        
}
