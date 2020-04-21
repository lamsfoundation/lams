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
package org.jboss.security.client;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.jboss.security.PicketBoxMessages;

//$Id$

/**
 *  Factory to return SecurityClient instances
 *  @author Anil.Saldhana@redhat.com
 *  @since  May 1, 2007 
 *  @version $Revision$
 */
public class SecurityClientFactory
{ 
   private static String defaultClient = "org.jboss.security.client.JBossSecurityClient";
   
   /**
    * Return the default Security Client
    * The default Security Client is of type
    * "org.jboss.security.client.JBossSecurityClient"
    * @return
    * @throws Exception
    */
   public static SecurityClient getSecurityClient() throws Exception
   {
      return getSC(defaultClient);
   }
   
   /**
    * Return a security client of type
    * @param client FQN of the security client implementation
    * @return
    * @throws Exception
    */
   public static SecurityClient getSecurityClient(String client) throws Exception
   {
      return getSC(client);
   }
   
   /**
    * Obtain a Security Client
    * @param clazz Class object that is of SecurityClient Type
    * @return
    * @throws Exception
    */
   public static SecurityClient getSecurityClient(Class<?> clazz) 
   throws Exception 
   {
      if(SecurityClient.class.isAssignableFrom(clazz) == false)
         throw PicketBoxMessages.MESSAGES.invalidType(SecurityClient.class.getName());
      //Use reflection to invoke the constructors
      Constructor<?> ctr = clazz.getConstructor(new Class[]{});
      return (SecurityClient) ctr.newInstance(new Object[]{});
   }
   
   @SuppressWarnings("unchecked")
   private static SecurityClient getSC(final String client) throws PrivilegedActionException
   {
      return (SecurityClient)AccessController.doPrivileged(new PrivilegedExceptionAction()
      { 
         public Object run() throws ClassNotFoundException, InstantiationException, IllegalAccessException
         {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(client);
            return clazz.newInstance(); 
         }
      });
   }
}