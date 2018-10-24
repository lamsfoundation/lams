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
package org.jboss.security.vault;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Privileged Blocks
 * @author Anil.Saldhana@redhat.com
 * @since Aug 12, 2011
 */
class SecurityActions
{
   static Class<?> loadClass(final Class<?> clazz, final String fqn)
   {
      return AccessController.doPrivileged(new PrivilegedAction<Class<?>>()
      {
         public Class<?> run()
         {
            ClassLoader cl = clazz.getClassLoader();
            Class<?> loadedClass = null;
            try
            {
               loadedClass = cl.loadClass(fqn);
            }
            catch (ClassNotFoundException e)
            {
            }
            if(loadedClass == null)
            {
               try
               {
                  loadedClass = Thread.currentThread().getContextClassLoader().loadClass(fqn);
               }
               catch (ClassNotFoundException e)
               {
               }
            }
            return loadedClass;
         }
      });

   }

   static Class<?> loadClass(final ClassLoader classLoader, final String fqn)
   {
      return AccessController.doPrivileged(new PrivilegedAction<Class<?>>()
      {
         public Class<?> run()
         {
            try
            {
               return classLoader.loadClass(fqn);
            }
            catch (ClassNotFoundException e)
            {
            }
            return null;
         }
      });
   }
}
