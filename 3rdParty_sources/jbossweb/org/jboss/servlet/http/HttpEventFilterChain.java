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

import javax.servlet.ServletException;

/**
 * A HttpEventFilterChain is an object provided by the Servlet container to the developer
 * giving a view into the invocation chain of a filtered event for a resource. Filters
 * use the HttpEventFilterChain to invoke the next filter in the chain, or if the calling filter
 * is the last filter in the chain, to invoke the resource at the end of the chain.
 */
public interface HttpEventFilterChain {

    
    /**
     * Causes the next filter in the chain to be invoked, or if the calling filter is the last filter
     * in the chain, causes the resource at the end of the chain to be invoked.
     *
     * @param event the event to pass along the chain.
     */
    public void doFilterEvent(HttpEvent event) throws IOException, ServletException;
    

}
