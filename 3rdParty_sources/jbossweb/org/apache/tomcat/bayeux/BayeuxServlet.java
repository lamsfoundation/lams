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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.cometd.bayeux.Bayeux;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.jboss.logging.Logger;
import org.jboss.servlet.http.HttpEvent;
import org.jboss.servlet.http.HttpEventServlet;

/**
 * 
 * @author Filip Hanik
 * @author Guy Molinari
 * @version 1.0
 */
public class BayeuxServlet extends HttpServlet implements HttpEventServlet {


    private static Logger log = Logger.getLogger(BayeuxServlet.class);
    
    
    /**
     * The timeout.
     */
    protected int timeout = 0;


    /**
     * The reconnect interval.
     */
    protected int reconnectInterval = 0;


    /**
     * Attribute to hold the TomcatBayeux object in the servlet context
     */
    public static final String TOMCAT_BAYEUX_ATTR = Bayeux.DOJOX_COMETD_BAYEUX;


    /**
     * Reference to the global TomcatBayeux object
     */
    protected TomcatBayeux tb;
    
    /**
     * Upon servlet destruction, the servlet will clean up the 
     * TomcatBayeux object and terminate any outstanding events.
     */
    public void destroy() {
        // FIXME, close all outstanding comet events
        //tb.destroy();
        tb = null;//FIXME, close everything down
        
    }
    
    /**
     * Returns the preconfigured connection timeout.
     * If no timeout has been configured as a servlet init parameter named <code>timeout</code>
     * then the default of 2min will be used.
     * @return int - the timeout for a connection in milliseconds
     */
    protected int getTimeout() {
        return timeout;
    }
    
    protected int getReconnectInterval() {
        return reconnectInterval;
    }


    public void event(HttpEvent cometEvent) throws IOException, ServletException {
        HttpEvent.EventType type = cometEvent.getType();
        if (log.isTraceEnabled()) {
            log.trace("["+Thread.currentThread().getName()+"] Received Comet Event type="+type);
        }
        switch (type) {
        case BEGIN:
            cometEvent.setTimeout(getTimeout());
            break;
        case READ:
            checkBayeux(cometEvent);
            break;
        case EOF:
        case EVENT:
        case WRITE:
            break;
        case ERROR:
        case END:
        case TIMEOUT:
            tb.remove(cometEvent);
            cometEvent.close();
            break;
        }
    }//event

    /**
     * 
     * @param cometEvent CometEvent
     * @return boolean - true if we comet event stays open
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    protected void checkBayeux(HttpEvent cometEvent) throws IOException,
            UnsupportedOperationException {
        // we actually have data.
        // data can be text/json or
        if (Bayeux.JSON_CONTENT_TYPE.equals(cometEvent.getHttpServletRequest()
                .getContentType())) {
            // read and decode the bytes according to content length
            int contentlength = cometEvent.getHttpServletRequest()
                    .getContentLength();
            throw new UnsupportedOperationException("Decoding "
                    + Bayeux.JSON_CONTENT_TYPE + " not yet implemented.");
        } else { // GET method or application/x-www-form-urlencoded
            String message = cometEvent.getHttpServletRequest().getParameter(
                    Bayeux.MESSAGE_PARAMETER);
            if (log.isTraceEnabled()) {
                log.trace("[" + Thread.currentThread().getName()
                        + "] Received JSON message:" + message);
            }
            try {
                int action = handleBayeux(message, cometEvent);
                if (log.isTraceEnabled()) {
                    log.trace("[" + Thread.currentThread().getName()
                            + "] Bayeux handling complete, action result="
                            + action);
                }
                if (action <= 0) {
                    cometEvent.close();
                }
            } catch (Exception e) {
                tb.remove(cometEvent);
                log.warn("Exception in check", e);
                cometEvent.close();
            }
        }
    }
    
    protected int handleBayeux(String message, HttpEvent event) throws IOException, ServletException {
        int result = 0;
        if (message==null || message.length()==0) return result;
        try {
            BayeuxRequest request = null;
            //a message can be an array of messages
            JSONArray jsArray = new JSONArray(message);
            for (int i = 0; i < jsArray.length(); i++) {
                JSONObject msg = jsArray.getJSONObject(i);
                
                if (log.isTraceEnabled()) {
                    log.trace("["+Thread.currentThread().getName()+"] Processing bayeux message:"+msg);
                }
                request = RequestFactory.getRequest(tb,event,msg);
                if (log.isTraceEnabled()) {
                    log.trace("["+Thread.currentThread().getName()+"] Processing bayeux message using request:"+request);
                }
                result = request.process(result);
                if (log.isTraceEnabled()) {
                    log.trace("["+Thread.currentThread().getName()+"] Processing bayeux message result:"+result);
                }
            }
            if (result>0 && request!=null) {
                event.getHttpServletRequest().setAttribute(BayeuxRequest.LAST_REQ_ATTR, request);
                ClientImpl ci = (ClientImpl)tb.getClient(((RequestBase)request).getClientId());
                ci.addCometEvent(event);
                if (log.isTraceEnabled()) {
                    log.trace("["+Thread.currentThread().getName()+"] Done bayeux message added to request attribute");
                }
            } else if (result == 0 && request!=null) {
                RequestBase.deliver(event,(ClientImpl)tb.getClient(((RequestBase)request).getClientId()));
                if (log.isTraceEnabled()) {
                    log.trace("["+Thread.currentThread().getName()+"] Done bayeux message, delivered to client");
                }
            }
            
        }catch (JSONException e) {
            log.warn("Error", e);// FIXME impl error handling
            result = -1;
        }catch (BayeuxException e) {
            log.warn("Error", e); // FIXME impl error handling
            result = -1;
        }
        return result;
    }

    public String getServletInfo() {
        return "Tomcat/BayeuxServlet/1.0";
    }

    public void init() throws ServletException {
        
        if (getServletConfig().getInitParameter("timeout") != null) {
            timeout = Integer.parseInt(getServletConfig().getInitParameter("timeout"));
        }
        if (getServletConfig().getInitParameter("reconnectInterval") != null) {
            reconnectInterval = Integer.parseInt(getServletConfig().getInitParameter("reconnectInterval"));
        }

        ServletContext ctx = getServletConfig().getServletContext();
        if (ctx.getAttribute(TOMCAT_BAYEUX_ATTR)==null)
            ctx.setAttribute(TOMCAT_BAYEUX_ATTR,new TomcatBayeux());
        this.tb = (TomcatBayeux)ctx.getAttribute(TOMCAT_BAYEUX_ATTR);
        tb.setReconnectInterval(getReconnectInterval());

    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        if (servletResponse instanceof HttpServletResponse) {
            ( (HttpServletResponse) servletResponse).sendError(500, "Misconfigured Tomcat server, must be configured to support Comet operations.");
        } else {
            throw new ServletException("Misconfigured Tomcat server, must be configured to support Comet operations for the Bayeux protocol.");
        }
    }
}
