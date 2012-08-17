/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.cache;

import org.jboss.cache.notifications.annotation.CacheListener;
import org.jboss.cache.notifications.annotation.CacheStarted;
import org.jboss.cache.notifications.annotation.CacheStopped;
import org.jboss.cache.notifications.annotation.NodeActivated;
import org.jboss.cache.notifications.annotation.NodeCreated;
import org.jboss.cache.notifications.annotation.NodeEvicted;
import org.jboss.cache.notifications.annotation.NodeLoaded;
import org.jboss.cache.notifications.annotation.NodeModified;
import org.jboss.cache.notifications.annotation.NodeMoved;
import org.jboss.cache.notifications.annotation.NodePassivated;
import org.jboss.cache.notifications.annotation.NodeRemoved;
import org.jboss.cache.notifications.annotation.NodeVisited;
import org.jboss.cache.notifications.annotation.ViewChanged;
import org.jboss.cache.notifications.event.Event;
import org.jboss.cache.notifications.event.NodeEvent;
import org.jboss.cache.notifications.event.ViewChangedEvent;

/**
 * This class provides a non-graphical view of <em>JBossCache</em> replication
 * events for a replicated cache.
 * <p/>
 * It can be utilized as a standalone application or as a component in other
 * applications.
 * <p/>
 * <strong>WARNING</strong>: take care when using this class in conjunction with
 * transactionally replicated cache's as it can cause deadlock situations due to
 * the reading of values for nodes in the cache.
 *
 * @author Jimmy Wilson 12-2004
 */
@CacheListener
public class ConsoleListener
{
   private CacheSPI cache;
   private boolean startCache;

   /**
    * Constructor.
    * <p/>
    * When using this constructor, this class with attempt to start and stop
    * the specified cache.
    *
    * @param cache the cache to monitor for replication events.
    */
   public ConsoleListener(CacheSPI cache)
         throws Exception
   {
      this(cache, true, true);
   }

   /**
    * Constructor.
    *
    * @param cache      the cache to monitor for replication events.
    * @param startCache indicates whether or not the cache should be started by
    *                   this class.
    * @param stopCache  indicates whether or not the cache should be stopped by
    *                   this class.
    */
   public ConsoleListener(CacheSPI cache,
                          boolean startCache, boolean stopCache)
         throws Exception
   {
      this.cache = cache;
      this.startCache = startCache;

      if (stopCache)
      {
         new ListenerShutdownHook().register();
      }
   }

   /**
    * Instructs this class to listen for cache replication events.
    * <p/>
    * This method waits indefinately.  Use the notify method of this class
    * (using traditional Java thread notification semantics) to cause this
    * method to return.
    */
   public void listen()
         throws Exception
   {
      listen(true);
   }

   /**
    * Instructs this class to listen for cache replication events.
    *
    * @param wait whether or not this method should wait indefinately.
    *             <p/>
    *             If this parameter is set to <code>true</code>, using the
    *             notify method of this class (using traditional Java thread
    *             notification semantics) will cause this method to return.
    */
   public void listen(boolean wait)
         throws Exception
   {
      cache.getNotifier().addCacheListener(this);

      if (startCache)
      {
         cache.start();
      }

      synchronized (this)
      {
         while (wait)
         {
            wait();
         }
      }
   }


   @CacheStarted
   @CacheStopped
   public void printDetails(Event e)
   {
      printEvent("Cache started.");
   }


   @NodeCreated
   @NodeLoaded
   @NodeModified
   @NodeRemoved
   @NodeVisited
   @NodeMoved
   @NodeEvicted
   @NodeActivated
   @NodePassivated
   public void printDetailsWithFqn(NodeEvent e)
   {
      if (e.isPre())
      {
         printEvent("Event " + e.getType() + " on node [" + e.getFqn() + "] about to be invoked");
      }
      else
      {
         printEvent("Event " + e.getType() + " on node [" + e.getFqn() + "] invoked");
      }
   }

   @ViewChanged
   public void printNewView(ViewChangedEvent e)
   {
      printEvent("View change: " + e.getNewView());
   }

   /**
    * Prints an event message.
    *
    * @param eventSuffix the suffix of the event message.
    */
   private void printEvent(String eventSuffix)
   {
      System.out.print("EVENT");
      System.out.print(' ');

      System.out.println(eventSuffix);
   }

   /**
    * This class provides a shutdown hook for shutting down the cache.
    */
   private class ListenerShutdownHook extends Thread
   {
      /**
       * Registers this hook for invocation during shutdown.
       */
      public void register()
      {
         Runtime.getRuntime().addShutdownHook(this);
      }

      /*
      * Thread overrides.
      */

      @Override
      public void run()
      {
         cache.stop();
      }
   }

   /**
    * The main method.
    *
    * @param args command line arguments dictated by convention.
    *             <p/>
    *             The first command line argument is the name of the
    *             <code>JBossCache</code> configuration file to be utilized
    *             for configuration of the cache.  Only the name of the
    *             configuration file is necessary as it is read off of the
    *             classpath.
    *             <p/>
    *             If a configuration file is not specified on the command line,
    *             <code>jboss-cache.xml</code> will be the assumed file name.
    *             <p/>
    *             All command line arguments after the first are ignored.
    */
   public static void main(String[] args)
   {
      final String DEFAULT_CONFIG_FILE_NAME = "jboss-cache.xml";

      try
      {
         String configFileName = DEFAULT_CONFIG_FILE_NAME;

         if (args.length >= 1)
         {
            configFileName = args[0];
         }
         else
         {
            System.out.print("No xml config file argument is supplied. Will use jboss-cache.xml from classpath");
         }

         CacheSPI cache = (CacheSPI) new DefaultCacheFactory<Object, Object>().createCache(configFileName);
         ConsoleListener listener = new ConsoleListener(cache);
         listener.listen();
      }
      catch (Throwable throwable)
      {
         throwable.printStackTrace();
      }
   }
}
