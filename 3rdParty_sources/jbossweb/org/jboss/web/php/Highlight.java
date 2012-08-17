/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
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

package org.jboss.web.php;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Highlight.
 *
 * @author Mladen Turk
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class Highlight extends Handler
{

    /**
     * Provides PHP Highlight Gateway service
     *
     * @param  req   HttpServletRequest passed in by servlet container
     * @param  res   HttpServletResponse passed in by servlet container
     *
     * @exception  ServletException  if a servlet-specific exception occurs
     * @exception  IOException  if a read/write exception occurs
     *
     * @see org.apache.catalina.servlets.php.Handler
     *
     */
    protected void service(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        syntaxHighlight = true;
        super.service(req, res);
    }
}
