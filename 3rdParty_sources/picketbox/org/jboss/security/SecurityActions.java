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
package org.jboss.security;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Priviledged actions for this package
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
class SecurityActions
{
   interface SystemPropertyAction
   {
      SystemPropertyAction PRIVILEGED = new SystemPropertyAction()
      {
         public String getProperty(final String name, final String defaultValue)
         {
            String prop = AccessController.doPrivileged(
               new PrivilegedAction<String>()
               {
                  public String run()
                  {
                     String p = System.getProperty(name, defaultValue);
                     return p;
                  }
               }
            );
            return prop;
         }
      };

      SystemPropertyAction NON_PRIVILEGED = new SystemPropertyAction()
      {
         public String getProperty(final String name, final String defaultValue)
         {
            String prop = System.getProperty(name, defaultValue);
            return prop;
         }
      };

      String getProperty(final String name, final String defaultValue);
   }

   static String getProperty(final String name, final String defaultValue)
   {
      SecurityManager sm = System.getSecurityManager();
      String prop;
      if( sm != null )
      {
         prop = SystemPropertyAction.PRIVILEGED.getProperty(name, defaultValue);
      }
      else
      {
         prop = SystemPropertyAction.NON_PRIVILEGED.getProperty(name, defaultValue);
      }
      return prop;
   }
   
   private static class GetTCLAction implements PrivilegedAction<ClassLoader>
   {
      static PrivilegedAction<ClassLoader> ACTION = new GetTCLAction();
      public ClassLoader run()
      {
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         return loader;
      }
   }
   
   static ClassLoader getContextClassLoader()
   {
      ClassLoader loader = (ClassLoader) AccessController.doPrivileged(GetTCLAction.ACTION);
      return loader;
   }
}
