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

package org.jboss.logging.jdk;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.jboss.logging.LoggerPlugin;
import org.jboss.logging.MDCProvider;
import org.jboss.logging.MDCSupport;
import org.jboss.logging.NDCProvider;
import org.jboss.logging.NDCSupport;

/** An example LoggerPlugin which uses the JDK java.util.logging framework.
 *
 * @author Scott.Stark@jboss.org
 * @version $Revison:$
 */
public class JDK14LoggerPlugin implements LoggerPlugin, MDCSupport, NDCSupport
{
   private Logger log;

   public void init(String name)
   {
      log = Logger.getLogger(name);
   }

   public boolean isTraceEnabled()
   {
      return log.isLoggable(Level.FINER);
   }

   public void trace(Object message)
   {
       log(Level.FINER, String.valueOf(message), null);
   }

   public void trace(Object message, Throwable t)
   {
      log(Level.FINER, String.valueOf(message), t);
   }

   public boolean isDebugEnabled()
   {
      return log.isLoggable(Level.FINE);
   }

   public void debug(Object message)
   {
       log(Level.FINE, String.valueOf(message), null);
   }

   public void debug(Object message, Throwable t)
   {
      log(Level.FINE, String.valueOf(message), t);
   }

   public boolean isInfoEnabled()
   {
      return log.isLoggable(Level.INFO);
   }

   public void info(Object message)
   {
       log(Level.INFO, String.valueOf(message), null);
   }

   public void info(Object message, Throwable t)
   {
      log(Level.INFO, String.valueOf(message), t);
   }

   public void warn(Object message)
   {
       log(Level.WARNING, String.valueOf(message), null);
   }

   public void warn(Object message, Throwable t)
   {
       log(Level.WARNING, String.valueOf(message), t);
   }

   public void error(Object message)
   {
       log(Level.SEVERE, String.valueOf(message), null);
   }

   public void error(Object message, Throwable t)
   {
       log(Level.SEVERE, String.valueOf(message), t);
   }

   public void fatal(Object message)
   {
       log(Level.SEVERE, String.valueOf(message), null);
   }

   public void fatal(Object message, Throwable t)
   {
       log(Level.SEVERE, String.valueOf(message), t);
   }

   // From commons-logging
   private void log(Level level, String msg, Throwable ex) {
       if (log.isLoggable(level)) {
           // Get the stack trace.
           Throwable dummyException = new Throwable();
           StackTraceElement locations[] = dummyException.getStackTrace();
           // Caller will be the third element
           String cname = "unknown";
           String method = "unknown";
           if (locations != null && locations.length > 3) {
               StackTraceElement caller = locations[3];
               cname = caller.getClassName();
               method = caller.getMethodName();
           }
           if (ex == null) {
               log.logp(level, cname, method, msg);
           } else {
               log.logp(level, cname, method, msg, ex);
           }
       }
   }

   public NDCProvider getNDCProvider()
   {
      return new JDKNDCProvider();
   }

   public MDCProvider getMDCProvider()
   {
      return new JDKMDCProvider();
   }
}
