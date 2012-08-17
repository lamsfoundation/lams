/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.servlet.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.ServletException;

/**
 * An event filter, similar to regular filters, performs filtering tasks on either 
 * the request to a resource (an event driven Servlet), or on the response from a resource, or both.
 */
public interface HttpEventFilter extends Filter {

    
    /**
     * The <code>doFilterEvent</code> method of the HttpEventFilter is called by the container
     * each time a request/response pair is passed through the chain due
     * to a client event for a resource at the end of the chain. The HttpEventFilterChain passed in to this
     * method allows the Filter to pass on the event to the next entity in the
     * chain.<p>
     * A typical implementation of this method would follow the following pattern:- <br>
     * 1. Examine the request<br>
     * 2. Optionally wrap the request object contained in the event with a custom implementation to
     * filter content or headers for input filtering and pass a HttpEvent instance containing
     * the wrapped request to the next filter<br>
     * 3. Optionally wrap the response object contained in the event with a custom implementation to
     * filter content or headers for output filtering and pass a HttpEvent instance containing
     * the wrapped request to the next filter<br>
     * 4. a) <strong>Either</strong> invoke the next entity in the chain using the HttpEventFilterChain 
     *       object (<code>chain.doFilterEvent()</code>), <br>   
     * 4. b) <strong>or</strong> not pass on the request/response pair to the next entity in the 
     *       filter chain to block the event processing<br>
     * 5. Directly set fields on the response after invocation of the next entity in the filter chain.
     * 
     * @param event the event that is being processed. Another event may be passed along the chain.
     * @param chain 
     * @throws IOException
     * @throws ServletException
     */
    public void doFilterEvent(HttpEvent event, HttpEventFilterChain chain)
        throws IOException, ServletException;
    

}
