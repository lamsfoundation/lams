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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jboss.security.PicketBoxMessages;

/** An implementation of CallbackHandler that obtains the values for
 NameCallback and PasswordCallback from the console.

 @see javax.security.auth.callback.CallbackHandler
 @see #handle(Callback[])

 @author  Scott.Stark@jboss.org
 @version $Revision$
 */
public class ConsoleInputHandler implements CallbackHandler
{
   /** Sets any NameCallback name property to the instance username,
    sets any PasswordCallback password property to the instance, and any
    password.
    @exception UnsupportedCallbackException, thrown if any callback of
    type other than NameCallback or PasswordCallback are seen.
    */
   public void handle(Callback[] callbacks) throws
         UnsupportedCallbackException
   {
      for (int i = 0; i < callbacks.length; i++)
      {
         Callback c = callbacks[i];
         if (c instanceof NameCallback)
         {
            NameCallback nc = (NameCallback) c;
            String prompt = nc.getPrompt();
            if( prompt == null )
               prompt = PicketBoxMessages.MESSAGES.enterUsernameMessage();
            // Prompt the user for the username
            System.out.print(prompt);
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            try
            {
               String username = br.readLine();
               nc.setName(username);
            }
            catch(IOException e)
            {
               throw PicketBoxMessages.MESSAGES.failedToObtainUsername(e);
            }
         }
         else if (c instanceof PasswordCallback)
         {
            PasswordCallback pc = (PasswordCallback) c;
            String prompt = pc.getPrompt();
            if( prompt == null )
               prompt = PicketBoxMessages.MESSAGES.enterPasswordMessage();
            // Prompt the user for the username
            System.out.print(prompt);
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            try
            {
               String password = br.readLine();            
               pc.setPassword(password.toCharArray());
            }
            catch(IOException e)
            {
               throw PicketBoxMessages.MESSAGES.failedToObtainPassword(e);
            }
         }
         else
         {
            throw PicketBoxMessages.MESSAGES.unableToHandleCallback(c, this.getClass().getName(), c.getClass().getCanonicalName());
         }
      }
   }
}