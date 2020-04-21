/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.callbacks;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jboss.security.SecurityContext;

//$Id$

/**
 *  CallbackHandler for SecurityContext
 *  @author Anil.Saldhana@redhat.com
 *  @since  Jan 7, 2008 
 *  @version $Revision$
 */
public class SecurityContextCallbackHandler implements CallbackHandler
{ 
   private SecurityContext securityContext = null;
  
   public SecurityContextCallbackHandler(SecurityContext securityContext)
   { 
      this.securityContext = securityContext;
   }
   
   public SecurityContext getSecurityContext()
   {
      return securityContext;
   }
 
   public void handle(Callback[] callbacks) 
   throws IOException, UnsupportedCallbackException
   {
      for(Callback cb: callbacks)
      {
         if(cb instanceof SecurityContextCallback)
         {
            SecurityContextCallback scb = (SecurityContextCallback)cb;
            scb.setSecurityContext(securityContext);
         }
         else
            throw new UnsupportedCallbackException(cb);
      } 
   } 
}