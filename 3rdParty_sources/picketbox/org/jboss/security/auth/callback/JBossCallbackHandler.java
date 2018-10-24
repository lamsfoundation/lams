/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.auth.callback;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.Principal;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jboss.security.PicketBoxMessages;

/**
 * Serializable Callback Handler
 * @author Anil.Saldhana@redhat.com
 * @since 25 November 2008
 */
public class JBossCallbackHandler implements CallbackHandler, Serializable
{
   private static final long serialVersionUID = 1L;

   private Principal principal;
   private Object credential;

   public JBossCallbackHandler()
   {
   }

   /** Initialize the UsernamePasswordHandler with the principal
    and credentials to use.
    */
   public JBossCallbackHandler(Principal principal, Object credential)
   {
      this.principal = principal;
      this.credential = credential;
   }

   public void setSecurityInfo(Principal principal, Object credential)
   {
      this.principal = principal;
      this.credential = credential;
   }

   /** Handles SecurityAssociationCallback, ObjectCallback, NameCallback and
    PasswordCallback types. A NameCallback name property is set to
    the Prinicpal.getName() value. A PasswordCallback password property is
    set to the getPassword() value. The preferred SecurityAssociationCallback
    has its principal and credential properties set to the instance principal
    and credential. An ObjectCallback has its credential set to the credential
    value.

    @see #getPassword()
    @exception UnsupportedCallbackException - thrown if any callback of
    type other than SecurityAssociationCallback, ObjectCallback, NameCallback
    or PasswordCallback are seen.
    */
   public void handle(Callback[] callbacks) throws
      UnsupportedCallbackException, IOException
   {
      for (int i = 0; i < callbacks.length; i++)
      {
         Callback callback = callbacks[i];
         this.handleCallBack( callback ); 
      }
   }
  
   /**
    * Handle a {@code Callback}
    * @param c callback
    * @throws UnsupportedCallbackException If the callback is not supported by this handler
    */
   protected void handleCallBack( Callback c ) throws UnsupportedCallbackException
   {
      if (c instanceof SecurityAssociationCallback)
      {
         SecurityAssociationCallback sac = (SecurityAssociationCallback) c;
         sac.setPrincipal(principal);
         sac.setCredential(credential);
      }
      else if (c instanceof ObjectCallback)
      {
         ObjectCallback oc = (ObjectCallback) c;
         oc.setCredential(credential);
      }
      else if (c instanceof NameCallback)
      {
         NameCallback nc = (NameCallback) c;
         if (principal != null)
            nc.setName(principal.getName());
      }
      else if (c instanceof PasswordCallback)
      {
         PasswordCallback pc = (PasswordCallback) c;
         char[] password = getPassword();
         if (password != null)
            pc.setPassword(password);
      }
      else
      {
         try
         {
            CallbackHandler handler = SecurityActions.getContextCallbackHandler();
            if( handler != null )
            {
               Callback[] unknown = {c};
               handler.handle(unknown);
               return;
            }
         }
         catch (Exception e)
         {
         } 

         throw PicketBoxMessages.MESSAGES.unableToHandleCallback(c, this.getClass().getName(), c.getClass().getCanonicalName());
      }
      
   }

   /** Try to convert the credential value into a char[] using the
    first of the following attempts which succeeds:

    1. Check for instanceof char[]
    2. Check for instanceof String and then use toCharArray()
    3. See if credential has a toCharArray() method and use it
    4. Use toString() followed by toCharArray().
    @return a char[] representation of the credential.
    */
   private char[] getPassword()
   {
      char[] password = null;
      if (credential instanceof char[])
      {
         password = (char[]) credential;
      }
      else if (credential instanceof String)
      {
         String s = (String) credential;
         password = s.toCharArray();
      }
      else
      {
         try
         {
            Class<?>[] types = {};
            Method m = credential.getClass().getMethod("toCharArray", types);
            Object[] args = {};
            password = (char[]) m.invoke(credential, args);
         }
         catch (Exception e)
         {
            if (credential != null)
            {
               String s = credential.toString();
               password = s.toCharArray();
            }
         }
      }
      return password;
   }  
}