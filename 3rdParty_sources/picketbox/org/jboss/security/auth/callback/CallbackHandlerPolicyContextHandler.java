/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.auth.callback;

import javax.security.auth.callback.CallbackHandler;
import javax.security.jacc.PolicyContextException;
import javax.security.jacc.PolicyContextHandler;

import org.jboss.security.SecurityConstants;

/**
 A PolicyContextHandler implementation that allows a dynamic CallbackHandler to
 be associated with the current context for use with authentication.

 @author Scott.Stark@jboss.org
 @version $Revision$
 */
public class CallbackHandlerPolicyContextHandler implements PolicyContextHandler
{
   private static ThreadLocal<CallbackHandler> requestContext = new ThreadLocal<CallbackHandler>();

   public static void setCallbackHandler(CallbackHandler bean)
   {
      requestContext.set(bean);
   }

   /** Access the CallbackHandler policy context data.
    * @param key - "org.jboss.security.auth.spi.CallbackHandler"
    * @param data currently unused
    * @return The active CallbackHandler
    * @throws javax.security.jacc.PolicyContextException
    */
   public Object getContext(String key, Object data)
      throws PolicyContextException
   {
      Object context = null;
      if (key.equalsIgnoreCase(SecurityConstants.CALLBACK_HANDLER_KEY))
         context = requestContext.get();
      return context;
   }

   public String[] getKeys()
      throws PolicyContextException
   {
      String[] keys = {SecurityConstants.CALLBACK_HANDLER_KEY};
      return keys;
   }

   public boolean supports(String key)
      throws PolicyContextException
   {
      return key.equalsIgnoreCase(SecurityConstants.CALLBACK_HANDLER_KEY);
   }

}
