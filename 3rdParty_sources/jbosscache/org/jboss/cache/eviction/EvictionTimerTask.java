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
package org.jboss.cache.eviction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Region;
import org.jboss.cache.RegionRegistry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Timer threads to do periodic node clean up by running the eviction policy.
 *
 * @author Ben Wang 2-2004
 * @author Daniel Huang (dhuang@jboss.org)
 * @version $Revision$
 */
public class EvictionTimerTask
{
   private Log log = LogFactory.getLog(EvictionTimerTask.class);

   private RegionRegistry regionsRegistry;
   private static AtomicInteger tcount = new AtomicInteger();
   private long wakeupInterval;
   ScheduledExecutorService scheduledExecutor;
   private Task task;

   public EvictionTimerTask()
   {
      task = new Task();
   }

   public void init(long wakeupInterval, ThreadFactory evictionThreadFactory, RegionRegistry rr)
   {
      if (log.isTraceEnabled())
         log.trace("Creating a new eviction listener with wakeupInterval millis set at " + wakeupInterval);
      this.regionsRegistry = rr;
      this.wakeupInterval = wakeupInterval;
      start(evictionThreadFactory);
   }

   public void stop()
   {
      if (log.isDebugEnabled()) log.debug("Stopping eviction timer");

      if (scheduledExecutor != null)
      {
         scheduledExecutor.shutdownNow();
      }
      scheduledExecutor = null;
   }

   private void start(ThreadFactory tf)
   {
      if (wakeupInterval < 1)
      {
         if (log.isInfoEnabled())
            log.info("Wakeup Interval set to " + wakeupInterval + ".  Not starting an eviction thread!");
         return;
      }

      if (tf == null) tf = new ThreadFactory()
      {
         public Thread newThread(Runnable r)
         {
            Thread t = new Thread(r, "EvictionTimer-" + tcount.getAndIncrement());
            t.setDaemon(true);
            return t;
         }
      };

      scheduledExecutor = Executors.newSingleThreadScheduledExecutor(tf);
      scheduledExecutor.scheduleWithFixedDelay(task, wakeupInterval / 2, wakeupInterval, TimeUnit.MILLISECONDS);
   }

   private void processRegions()
   {
      if (log.isTraceEnabled()) log.trace("Processing eviction regions " + regionsRegistry.keySet());

      for (Region region : regionsRegistry.values())
      {
         if (region.getEvictionRegionConfig() != null)
            handleRegion(region);
      }
   }

   private void handleRegion(Region region)
   {
      try
      {
         region.processEvictionQueues();
      }
      catch (EvictionException e)
      {
         log.error("run(): error processing eviction with exception: " + e.toString()
               + " will reset the eviction queue list.");
         region.resetEvictionQueues();
         log.debug("trace", e);
      }
   }

   public class Task implements Runnable
   {
      public void run()
      {
         // Run the eviction thread.
         // This thread will synchronize the set of regions and iterate through every MarshRegion registered w/ the
         // Eviction thread. It also synchronizes on each individual region as it is being processed.
         processRegions();
      }
   }
}


