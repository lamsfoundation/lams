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
package org.jboss.security.auth.spi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.jboss.security.plugins.ClassLoaderLocator;
import org.jboss.security.plugins.ClassLoaderLocatorFactory;


/**
 *  Privileged Blocks
 *  @author Anil.Saldhana@redhat.com
 *  @since  Sep 26, 2007 
 *  @version $Revision$
 */
class SecurityActions
{
   static ClassLoader getContextClassLoader()
   {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      { 
         public ClassLoader run()
         { 
            return Thread.currentThread().getContextClassLoader();
         }
       });  
   }
   
   static Void setContextClassLoader(final ClassLoader cl)
   {
      return AccessController.doPrivileged(new PrivilegedAction<Void>()
      {
         public Void run()
         {
            Thread.currentThread().setContextClassLoader(cl);
            return null;
         }
      });
   }
   
   static URL findResource(final URLClassLoader cl, final String name)
   {
      return AccessController.doPrivileged(new PrivilegedAction<URL>()
      { 
         public URL run()
         { 
            return cl.findResource(name);
         }
       });  
   }
   
   static InputStream openStream(final URL url) throws PrivilegedActionException
   {
      return AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>()
      { 
         public InputStream run() throws IOException
         { 
            return url.openStream();
         }
       });
   }

   static Class<?> loadClass(final String name, final String jbossModuleName) throws PrivilegedActionException
   {
      return AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>()
      {
         public Class<?> run() throws ClassNotFoundException
         {
            ClassLoader moduleCL = null;
            if (jbossModuleName != null && jbossModuleName.length() > 0) 
            {
                ClassLoaderLocator locator = ClassLoaderLocatorFactory.get();
                if (locator != null)
                   moduleCL = locator.get(jbossModuleName);
            }
            ClassLoader[] cls = new ClassLoader[] {
                  getContextClassLoader(), // User defined classes
                  moduleCL, // user defined module class loader
                  SecurityActions.class.getClassLoader(), // PB classes (not always on TCCL [modular env])
                  ClassLoader.getSystemClassLoader() }; // System loader, usually has app class path

            ClassNotFoundException e = null;
            for (ClassLoader cl : cls)
            {
               if (cl == null)
                  continue;
               
               try
               {
                  return cl.loadClass(name);
               }
               catch (ClassNotFoundException ce)
               {
                  e = ce;
               }
            }
            throw e != null ? e : new ClassNotFoundException(name);
         }
      });
   }

   static Class<?> loadClass(final String name) throws PrivilegedActionException
   {
      return loadClass(name, null);
   }
}