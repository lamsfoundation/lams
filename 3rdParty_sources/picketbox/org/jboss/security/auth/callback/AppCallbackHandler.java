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
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jboss.security.PicketBoxMessages;

//$Id$

/** 
 * JBAS-3109:AppCallbackHandler as the default CallbackHandler in the 
 * security module
 * 
 * An implementation of the JAAS CallbackHandler interface that 
 * handles NameCallbacks, PasswordCallback, TextInputCallback 
 * and the JBoss ByteArrayCallback.
 * All JBoss Callbacks must be handled.
 * - MapCallback
 * 
 * @see javax.security.auth.callback.CallbackHandler
 * @see #handle(Callback[])
 * 
 * @author Scott.Stark@jboss.org
 * @author Anil.Saldhana@jboss.org
 * @version $Revision$
 */
public class AppCallbackHandler implements CallbackHandler
{
   private String username;
   private char[] password;
   private byte[] data;
   private String text; 
   
   private transient String prompt;
   private transient Object credential;
   
   private Map<String,Object> keyValuePair;  
   
   /** Whether this handler gets the username/password from the console */
   private boolean consoleHandler = false; 

   public AppCallbackHandler()
   {   
   }
   
   public AppCallbackHandler(String username, char[] password)
   {
      this.username = username;
      this.password = password;
   }
   public AppCallbackHandler(String username, char[] password, byte[] data)
   {
      this.username = username;
      this.password = password;
      this.data = data;
   }
   public AppCallbackHandler(String username, char[] password, byte[] data, String text)
   {
      this.username = username;
      this.password = password;
      this.data = data;
      this.text = text;
   } 
   
   /**
    * 
    * Create a new AppCallbackHandler.
    * 
    * @param isConsoleHandler Denotes whether the input is from
    *                         the console.
    */
   public AppCallbackHandler(boolean isConsoleHandler)
   {
      this.consoleHandler = isConsoleHandler; 
   } 
   
   /**
    * 
    * Create a new AppCallbackHandler.
    * 
    * @param prompt Prompt meaningful to the LoginModule
    */
   public AppCallbackHandler(String prompt)
   {
       this.prompt = prompt;
   }
   
   /**
    * 
    * Create a new AppCallbackHandler.
    * 
    * @param mapOfValues Key Value Pair
    */
   public AppCallbackHandler(Map<String,Object> mapOfValues)
   { 
      this.keyValuePair = mapOfValues;
   }
   
   public void setSecurityInfo(Principal p, Object cred)
   {
      this.username = p.getName();
      this.credential = cred;
   }

   public String getPrompt()
   {
       return prompt;
   }
   public Object getCredential()
   {
       return credential;
   }
   public void setCredential(Object credential)
   {
       this.credential = credential;
   }
   public void clearCredential()
   {
       this.credential = null;
   } 

   public void handle(Callback[] callbacks) throws
         IOException, UnsupportedCallbackException
   {
      for (int i = 0; i < callbacks.length; i++)
      {
         Callback c = callbacks[i]; 
         if( c instanceof NameCallback )
         {
            NameCallback nc = (NameCallback) c; 
            String prompt = nc.getPrompt();
            if( prompt == null )
               prompt = PicketBoxMessages.MESSAGES.enterUsernameMessage();
            if(this.consoleHandler)
               nc.setName(getUserNameFromConsole(prompt));
            else
               nc.setName(username);
         }
         else if( c instanceof PasswordCallback )
         {
            PasswordCallback pc = (PasswordCallback) c;
            String prompt = pc.getPrompt();
            if( prompt == null )
               prompt = PicketBoxMessages.MESSAGES.enterPasswordMessage();
            if(this.consoleHandler)
               pc.setPassword(getPasswordFromConsole(prompt));
            else
               if(this.credential != null && this.password == null)
                  pc.setPassword(this.getPassword());
            else
               pc.setPassword(password);
         }
         else if( c instanceof TextInputCallback )
         {
            TextInputCallback tc = (TextInputCallback) c;
            tc.setText(text);
         }
         else if( c instanceof ByteArrayCallback )
         {
            ByteArrayCallback bac = (ByteArrayCallback) c;
            bac.setByteArray(data);
         }
         else if (c instanceof ObjectCallback)
         {
            ObjectCallback oc = (ObjectCallback) c;
            oc.setCredential(credential);
         }
         else if( c instanceof MapCallback )
         {
            MapCallback mc = (MapCallback) c;
            if(keyValuePair != null && !keyValuePair.isEmpty())
            {
               for (String key : keyValuePair.keySet())
               {
                  mc.setInfo(key, keyValuePair.get(key));
               }  
            }
         }
         else
         {
            throw PicketBoxMessages.MESSAGES.unableToHandleCallback(c, this.getClass().getName(), c.getClass().getCanonicalName());
         }
      }
   }
   
   private String getUserNameFromConsole(String prompt)
   {
      String uName = "";
      System.out.print(prompt);
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      try
      {
         uName = br.readLine(); 
      }
      catch(IOException e)
      {
         throw PicketBoxMessages.MESSAGES.failedToObtainUsername(e);
      }
      return uName;
   }
   
   private char[] getPasswordFromConsole(String prompt)
   {
      String pwd = "";
      //Prompt the user for the username
      System.out.print(prompt);
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      try
      {
         pwd = br.readLine();   
      }
      catch(IOException e)
      {
         throw PicketBoxMessages.MESSAGES.failedToObtainPassword(e);
      }
      return pwd.toCharArray();
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