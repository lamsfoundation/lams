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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Logger wrapper that tries to dynamically load a log4j class to
 * determine if log4j is available in the VM. If it is the case,
 * a log4j delegate is built and used. In the contrary, a null
 * logger is used. This class cannot directly reference log4j
 * classes otherwise the JVM will try to load it and make it fail.
 * To set
 *
 * <p>Only exposes the relevent factory and logging methods.
 * 
 * <p>For JBoss the logging should be done as follows:
 * <ul>
 * <li>FATAL - JBoss is likely to/will crash
 * <li>ERROR - A definite problem
 * <li>WARN - Likely to be a problem, or it could be JBoss
 *            detected a problem it can recover from
 * <li>INFO - Lifecycle low volume, e.g. "Bound x into jndi",
 *            things that are of interest to a user
 * <li>DEBUG - Lifecycle low volume but necessarily of interest 
 *             to the user, e.g. "Starting listener thread"
 * <li>TRACE - High volume detailed logging
 * </ul>
 *
 * @see #isTraceEnabled
 * @see #trace(Object)
 * @see #trace(Object,Throwable)
 *
 * @version <tt>$Revision$</tt>
 * @author  Scott.Stark@jboss.org
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author  <a href="mailto:sacha.labourey@cogito-info.ch">Sacha Labourey</a>
 */
public class Logger implements Serializable
{
   /** Serialization */
   private static final long serialVersionUID = 4232175575988879434L;

   /** The system property to look for an externalized LoggerPlugin implementation class */
   protected static String PLUGIN_CLASS_PROP = "org.jboss.logging.Logger.pluginClass";

   /** The default LoggerPlugin implementation is log4j */
   protected static final String DEFAULT_PLUGIN_CLASS_NAME = "org.jboss.logging.jdk.JDK14LoggerPlugin";

   /** The LoggerPlugin implementation class to use */
   protected static Class pluginClass = null;

   /** The class name of the LoggerPlugin implementation class to use */
   protected static String pluginClassName = null;

   static
   {
      init();
   }

   /** The logger name. */
   private final String name;

   /** The logger plugin delegate */
   protected transient LoggerPlugin loggerDelegate = null;

   /** The LoggerPlugin implementation class name in use
    * 
    * @return LoggerPlugin implementation class name
    */
   public static String getPluginClassName()
   {
      return Logger.pluginClassName;
   }

   /**
    * Set the LoggerPlugin implementation class name in use
    * 
    * @param pluginClassName the LoggerPlugin implementation class name
    */
   public static void setPluginClassName(String pluginClassName)
   {
      if (pluginClassName.equals(Logger.pluginClassName) == false)
      {
         Logger.pluginClassName = pluginClassName;
         init();
      }
   }

   /**
    * Creates new Logger the given logger name.
    *
    * @param name the logger name.
    */
   protected Logger(final String name)
   {
      this.name = name;
      this.loggerDelegate = getDelegatePlugin(name);
   }

   /**
    * Return the name of this logger.
    *
    * @return The name of this logger.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Get the logger plugin delegate
    * 
    * @return the delegate
    */
   public LoggerPlugin getLoggerPlugin()
   {
      return this.loggerDelegate;
   }

   /**
    * Check to see if the TRACE level is enabled for this logger.
    *
    * @return true if a {@link #trace(Object)} method invocation would pass
    *         the msg to the configured appenders, false otherwise.
    */
   public boolean isTraceEnabled()
   {
      return loggerDelegate.isTraceEnabled();
   }

   /**
    * Issue a log msg with a level of TRACE.
    * 
    * @param message the message
    */
   public void trace(Object message)
   {
      loggerDelegate.trace(message);
   }

   /**
    * Issue a log msg and throwable with a level of TRACE.
    * 
    * @param message the message
    * @param t the throwable
    */
   public void trace(Object message, Throwable t)
   {
      loggerDelegate.trace(message, t);
   }

   /**
    * Check to see if the DEBUG level is enabled for this logger.
    *
    * @deprecated DEBUG is for low volume logging, you don't need this
    * @return true if a {@link #trace(Object)} method invocation would pass
    * the msg to the configured appenders, false otherwise.
    */
   public boolean isDebugEnabled()
   {
      return loggerDelegate.isDebugEnabled();
   }

   /**
    * Issue a log msg with a level of DEBUG.
    * 
    * @param message the message
    */
   public void debug(Object message)
   {
      loggerDelegate.debug(message);
   }

   /**
    * Issue a log msg and throwable with a level of DEBUG.
    * 
    * @param message the message
    * @param t the throwable
    */
   public void debug(Object message, Throwable t)
   {
      loggerDelegate.debug(message, t);
   }

   /**
    * Check to see if the INFO level is enabled for this logger.
    *
    * @deprecated INFO is for low volume information, you don't need this
    * @return true if a {@link #info(Object)} method invocation would pass
    * the msg to the configured appenders, false otherwise.
    */
   public boolean isInfoEnabled()
   {
      return loggerDelegate.isInfoEnabled();
   }

   /**
    * Issue a log msg with a level of INFO.
    * 
    * @param message the message
    */
   public void info(Object message)
   {
      loggerDelegate.info(message);
   }

   /**
    * Issue a log msg and throwable with a level of INFO.
    * 
    * @param message the message
    * @param t the throwable
    */
   public void info(Object message, Throwable t)
   {
      loggerDelegate.info(message, t);
   }

   /**
    * Issue a log msg with a level of WARN.
    * 
    * @param message the message
    */
   public void warn(Object message)
   {
      loggerDelegate.warn(message);
   }

   /**
    * Issue a log msg and throwable with a level of WARN.
    * 
    * @param message the message
    * @param t the throwable
    */
   public void warn(Object message, Throwable t)
   {
      loggerDelegate.warn(message, t);
   }

   /**
    * Issue a log msg with a level of ERROR.
    * 
    * @param message the message
    */
   public void error(Object message)
   {
      loggerDelegate.error(message);
   }

   /**
    * Issue a log msg and throwable with a level of ERROR.
    * 
    * @param message the message
    * @param t the throwable
    */
   public void error(Object message, Throwable t)
   {
      loggerDelegate.error(message, t);
   }

   /**
    * Issue a log msg with a level of FATAL.
    * 
    * @param message the message
    */
   public void fatal(Object message)
   {
      loggerDelegate.fatal(message);
   }

   /**
    * Issue a log msg and throwable with a level of FATAL.
    * 
    * @param message the message
    * @param t the throwable
    */
   public void fatal(Object message, Throwable t)
   {
      loggerDelegate.fatal(message, t);
   }

   /**
    * Custom serialization to reinitalize the delegate
    * 
    * @param stream the object stream
    * @throws IOException for any error
    * @throws ClassNotFoundException if a class is not found during deserialization
    */
   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
   {
      // restore non-transient fields (aka name)
      stream.defaultReadObject();

      // Restore logging
      if (pluginClass == null)
      {
         init();
      }
      this.loggerDelegate = getDelegatePlugin(name);
   }

   /**
    * Create a Logger instance given the logger name.
    *
    * @param name the logger name
    * @return the logger
    */
   public static Logger getLogger(String name)
   {
      return new Logger(name);
   }

   /**
    * Create a Logger instance given the logger name with the given suffix.
    *
    * <p>This will include a logger seperator between classname and suffix
    *
    * @param name the logger name
    * @param suffix a suffix to append to the classname.
    * @return the logger
    */
   public static Logger getLogger(String name, String suffix)
   {
      return new Logger(name + "." + suffix);
   }

   /**
    * Create a Logger instance given the logger class. This simply
    * calls create(clazz.getName()).
    *
    * @param clazz the Class whose name will be used as the logger name
    * @return the logger
    */
   public static Logger getLogger(Class clazz)
   {
      return new Logger(clazz.getName());
   }

   /**
    * Create a Logger instance given the logger class with the given suffix.
    *
    * <p>This will include a logger seperator between classname and suffix
    *
    * @param clazz the Class whose name will be used as the logger name.
    * @param suffix a suffix to append to the classname.
    * @return the logger
    */
   public static Logger getLogger(Class clazz, String suffix)
   {
      return new Logger(clazz.getName() + "." + suffix);
   }

   /**
    * Get the delegate plugin
    * 
    * @param name the name of the logger
    * @return the plugin
    */
   protected static LoggerPlugin getDelegatePlugin(String name)
   {
      LoggerPlugin plugin = null;
      try
      {
         plugin = (LoggerPlugin) pluginClass.newInstance();
      }
      catch (Throwable e)
      {
         plugin = new NullLoggerPlugin();
      }
      try
      {
         plugin.init(name);
      }
      catch (Throwable e)
      {
         String extraInfo = e.getMessage();
         System.err.println("Failed to initalize plugin: " + plugin
               + (extraInfo != null ? ", cause: " + extraInfo : ""));
         plugin = new NullLoggerPlugin();
      }

      return plugin;
   }

   /**
    * Initialize the LoggerPlugin class to use as the delegate to the
    * logging system. This first checks to see if a pluginClassName has
    * been specified via the {@link #setPluginClassName(String)} method,
    * then the PLUGIN_CLASS_PROP system property and finally the
    * LOG4J_PLUGIN_CLASS_NAME default. If the LoggerPlugin implementation
    * class cannot be loaded the default NullLoggerPlugin will be used.
    */
   protected static void init()
   {
      try
      {
         // See if there is a PLUGIN_CLASS_PROP specified
         if (pluginClassName == null)
         {
            pluginClassName = System.getProperty(PLUGIN_CLASS_PROP, DEFAULT_PLUGIN_CLASS_NAME);
         }

         // Try to load the plugin via the TCL
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         pluginClass = cl.loadClass(pluginClassName);
      }
      catch (Throwable e)
      {
         // The plugin could not be setup, default to a null logger
         pluginClass = org.jboss.logging.NullLoggerPlugin.class;
      }
   }
}
