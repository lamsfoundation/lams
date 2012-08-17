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


package org.apache.catalina.connector;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.util.StringManager;
import org.jboss.servlet.http.HttpEvent;

public class HttpEventImpl implements HttpEvent {

    /**
     * The string manager for this package.
     */
    protected static StringManager sm =
        StringManager.getManager(Constants.Package);


    public HttpEventImpl(Request request, Response response) {
        this.request = request;
        this.response = response;
    }


    // ----------------------------------------------------- Instance Variables

    
    /**
     * Associated request.
     */
    protected Request request = null;


    /**
     * Associated response.
     */
    protected Response response = null;

    
    /**
     * Event type.
     */
    protected EventType eventType = EventType.BEGIN;
    

    // --------------------------------------------------------- Public Methods

    /**
     * Clear the event.
     */
    public void clear() {
        request = null;
        response = null;
    }

    public void setType(EventType eventType) {
        this.eventType = eventType;
    }
    
    public void close() throws IOException {
        request.setComet(false);
        request.resume();
    }

    public EventType getType() {
        return eventType;
    }

    public HttpServletRequest getHttpServletRequest() {
        return request.getRequest();
    }

    public HttpServletResponse getHttpServletResponse() {
        return response.getResponse();
    }

    public void setTimeout(int timeout) {
        request.setTimeout(timeout);
    }
    
    public boolean isReadReady() {
        return request.isReadable();
    }
    
    public boolean isWriteReady() {
        return response.isWriteable();
    }
    
    public void resume() {
        request.resume();
    }

    public void suspend() {
        request.suspend();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("CometEventImpl[");
        buf.append(super.toString());
        buf.append("] Event:");
        buf.append(getType());
        return buf.toString();
    }

}
