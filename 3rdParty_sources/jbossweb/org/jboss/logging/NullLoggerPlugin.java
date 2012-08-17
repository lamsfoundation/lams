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
package org.jboss.logging;

/**
 * LoggerPlugin implementation producing no output at all. Used for client
 * side logging when no log4j.jar is available on the classpath.
 *
 * @see org.jboss.logging.Logger
 * @see org.jboss.logging.LoggerPlugin
 *
 * @author  <a href="mailto:sacha.labourey@cogito-info.ch">Sacha Labourey</a>.
 * @version $Revision$
 */
public class NullLoggerPlugin implements LoggerPlugin
{
   public void init(String name)
   { 
      /* don't care */
   }

   public boolean isTraceEnabled()
   {
      return false;
   }

   public void trace(Object message)
   {
      // nothing
   }

   public void trace(Object message, Throwable t)
   {
      // nothing
   }

   public boolean isDebugEnabled()
   {
      return false;
   }

   public void debug(Object message)
   {
      // nothing
   }

   public void debug(Object message, Throwable t)
   {
      // nothing
   }

   public boolean isInfoEnabled()
   {
      return false;
   }

   public void info(Object message)
   {
      // nothing
   }

   public void info(Object message, Throwable t)
   {
      // nothing
   }

   public void error(Object message)
   {
      // nothing
   }

   public void error(Object message, Throwable t)
   {
      // nothing
   }

   public void fatal(Object message)
   {
      // nothing
   }

   public void fatal(Object message, Throwable t)
   {
      // nothing
   }

   public void warn(Object message)
   {
      // nothing
   }

   public void warn(Object message, Throwable t)
   {
      // nothing
   }
}
