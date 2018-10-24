/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
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
package org.picketbox.plugins;

import java.io.IOException;
import java.security.Principal;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.callback.ObjectCallback;
import org.picketbox.handlers.HandlerContract;

/**
 * Default Callbackhandler that primarily uses the {@link}HandlerContract
 * for the Principal/Credential combination
 * <a href="mailto:anil.saldhana@redhat.com">Anil Saldhana</a>
 */
public class PicketBoxCallbackHandler implements CallbackHandler, HandlerContract
{
   private Principal principal = null;
   private Object credential = null;
   
   /**
    * @see CallbackHandler#handle(Callback[])
    */
   public void handle(Callback[] callbacks) 
   throws IOException, UnsupportedCallbackException
   {
      int len = callbacks.length;
      if(len > 0)
      {
         for(Callback cb: callbacks)
         {
           if(cb instanceof NameCallback)
           {
              NameCallback nameCallback = (NameCallback) cb;
              nameCallback.setName(principal.getName());
           }
           else
           if(cb instanceof ObjectCallback)
           {
              ((ObjectCallback)cb).setCredential(credential);
           }
           else
           if(cb instanceof PasswordCallback)
           {
             char[] passwd = null;
             if(credential instanceof String)
             {
                passwd = ((String)credential).toCharArray();
             }
             else if(credential instanceof char[])
             {
                passwd = (char[]) credential;
             }
             ((PasswordCallback)cb).setPassword(passwd);
           }
           else
           throw PicketBoxMessages.MESSAGES.unableToHandleCallback(cb, this.getClass().getName(),
                   cb.getClass().getCanonicalName());
         }
      }
   }

   /**
    * @see HandlerContract#setSecurityInfo(Principal, Object)
    */
   public void setSecurityInfo(Principal principal, Object credential)
   { 
      this.principal = principal;
      this.credential = credential;
   } 
}