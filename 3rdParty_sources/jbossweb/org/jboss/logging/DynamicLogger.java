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
 * An extension of the JBoss Logger that adds a log()
 * primitive that maps to a dynamically defined log level.
 * 
 * TODO - Make sure serialization works correctly
 * 
 * @author <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 * @version <tt>$Revision$</tt>
 * 
 * @since 4.0.3
 */
public class DynamicLogger extends Logger
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -5963699806863917370L;
   
   /** No logging */
   public static final int LOG_LEVEL_NONE  = 0;

   /** Fatal level logging */
   public static final int LOG_LEVEL_FATAL = 1;

   /** Error level logging */
   public static final int LOG_LEVEL_ERROR = 2;

   /** Warn level logging */
   public static final int LOG_LEVEL_WARN  = 3;

   /** Info level logging */
   public static final int LOG_LEVEL_INFO  = 4;
   
   /** Debug level logging */
   public static final int LOG_LEVEL_DEBUG = 5;

   /** Trace level logging */
   public static final int LOG_LEVEL_TRACE = 6;
   
   /** The available log level strings */
   public final static String[] LOG_LEVEL_STRINGS =
      { "NONE", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE" }; 
    
   /** The log level to use for the "log" primitive */
   private int logLevel = LOG_LEVEL_DEBUG;

   /**
    * Create a DynamicLogger instance given the logger name.
    *
    * @param name the logger name
    * @return the dynamic logger
    */
   public static DynamicLogger getDynamicLogger(String name)
   {
      return new DynamicLogger(name);
   }

   /**
    * Create a DynamicLogger instance given the logger name with the given suffix.
    *
    * <p>This will include a logger seperator between classname and suffix
    *
    * @param name     The logger name
    * @param suffix   A suffix to append to the classname.
    * @return the dynamic logger
    */
   public static DynamicLogger getDynamicLogger(String name, String suffix)
   {
      return new DynamicLogger(name + "." + suffix);
   }

   /**
    * Create a DynamicLogger instance given the logger class. This simply
    * calls create(clazz.getName()).
    *
    * @param clazz    the Class whose name will be used as the logger name
    * @return the dynamic logger
    */
   public static DynamicLogger getDynamicLogger(Class clazz)
   {
      return new DynamicLogger(clazz.getName());
   }

   /**
    * Create a DynamicLogger instance given the logger class with the given suffix.
    *
    * <p>This will include a logger seperator between classname and suffix
    *
    * @param clazz    The Class whose name will be used as the logger name.
    * @param suffix   A suffix to append to the classname.
    * @return the dynamic logger
    */
   public static DynamicLogger getDynamicLogger(Class clazz, String suffix)
   {
      return new DynamicLogger(clazz.getName() + "." + suffix);
   }

   /**
    * Create a new DynamicLogger.
    * 
    * @param name the log name
    */
   protected DynamicLogger(final String name)
   {
      super(name);
   }
   
   /**
    * Sets the logLevel for the log() primitive
    * 
    * @param logLevel between LOG_LEVEL_NONE and LOG_LEVEL_TRACE
    */
   public void setLogLevel(int logLevel)
   {
      if (logLevel >= LOG_LEVEL_NONE && logLevel <= LOG_LEVEL_TRACE)
      {
         this.logLevel = logLevel;
      }
   }
   
   /**
    * Gets the logLevel of the log() primitive
    * 
    * @return the logLevel of the log() primitive
    */
   public int getLogLevel()
   {
      return logLevel;
   }
   
   /**
    * Sets the logLevel of the log() primitive
    * 
    * @param logLevelString the log level in String form
    */
   public void setLogLevelAsString(String logLevelString)
   {
      if (logLevelString != null)
      {
         logLevelString = logLevelString.toUpperCase().trim();
         
         for (int i = 0; i <= LOG_LEVEL_TRACE; i++)
         {
            if (logLevelString.equals(LOG_LEVEL_STRINGS[i]))
            {
               // match
               this.logLevel = i;
               break;
            }
         }
      }
   }
   
   /**
    * Gets the logLevel of the log() primitive in String form
    *
    * @return the logLevel of the log() primitive in String form
    */
   public String getLogLevelAsString()
   {
      return LOG_LEVEL_STRINGS[logLevel];
   }
   
   /**
    * Logs a message using dynamic log level
    * 
    * @param message the message to log
    */
   public void log(Object message)
   {
      switch (logLevel)
      {
         case LOG_LEVEL_TRACE:
            super.trace(message);
            break;
            
         case LOG_LEVEL_DEBUG:
            super.debug(message);
            break;
            
         case LOG_LEVEL_INFO:
            super.info(message);
            break;
            
         case LOG_LEVEL_WARN:
            super.warn(message);
            break;
            
         case LOG_LEVEL_ERROR:
            super.error(message);
            break;
            
         case LOG_LEVEL_FATAL:
            super.fatal(message);
            break;
            
         case LOG_LEVEL_NONE:
         default:
            // do nothing
            break;
      }
   }
   
   /**
    * Logs a message and a throwable using dynamic log level
    * 
    * @param message the message to log
    * @param t       the throwable to log
    */
   public void log(Object message, Throwable t)
   {
      switch (logLevel)
      {
         case LOG_LEVEL_TRACE:
            super.trace(message, t);
            break;
            
         case LOG_LEVEL_DEBUG:
            super.debug(message, t);
            break;
            
         case LOG_LEVEL_INFO:
            super.info(message, t);
            break;
            
         case LOG_LEVEL_WARN:
            super.warn(message, t);
            break;
            
         case LOG_LEVEL_ERROR:
            super.error(message, t);
            break;
            
         case LOG_LEVEL_FATAL:
            super.fatal(message, t);
            break;
            
         case LOG_LEVEL_NONE:
         default:
            // do nothing
            break;
      }      
   }
}
