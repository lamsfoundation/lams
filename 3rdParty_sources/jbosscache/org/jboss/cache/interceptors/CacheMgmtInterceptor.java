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
package org.jboss.cache.interceptors;

import org.jboss.cache.DataContainer;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.base.JmxStatsCommandInterceptor;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jboss.cache.jmx.annotations.ManagedOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * Captures cache management statistics
 *
 * @author Jerry Gauthier
 *
 */
public class CacheMgmtInterceptor extends JmxStatsCommandInterceptor
{
   private long hitTimes = 0;
   private long missTimes = 0;
   private long storeTimes = 0;
   private long hits = 0;
   private long misses = 0;
   private long stores = 0;
   private long evictions = 0;
   private long start = System.currentTimeMillis();
   private long reset = start;

   private DataContainer dataContainer;

   @Inject
   public void setDependencies(DataContainer dataContainer)
   {
      this.dataContainer = dataContainer;
   }

   @Override
   public Object visitEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      Object returnValue = invokeNextInterceptor(ctx, command);
      evictions++;
      return returnValue;
   }

   @Override
   public Object visitGetKeyValueCommand(InvocationContext ctx, GetKeyValueCommand command) throws Throwable
   {
      long t1 = System.currentTimeMillis();
      Object retval = invokeNextInterceptor(ctx, command);
      long t2 = System.currentTimeMillis();
      if (retval == null)
      {
         missTimes = missTimes + (t2 - t1);
         misses++;
      }
      else
      {
         hitTimes = hitTimes + (t2 - t1);
         hits++;
      }
      return retval;
   }

   @Override
   public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
   {
      Map data = command.getData();
      long t1 = System.currentTimeMillis();
      Object retval = invokeNextInterceptor(ctx, command);
      long t2 = System.currentTimeMillis();

      if (data != null && data.size() > 0)
      {
         storeTimes = storeTimes + (t2 - t1);
         stores = stores + data.size();
      }
      return retval;
   }


   @Override
   public Object visitPutForExternalReadCommand(InvocationContext ctx, PutForExternalReadCommand command) throws Throwable
   {
      return visitPutKeyValueCommand(ctx, command);
   }

   @Override
   public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
   {
      long t1 = System.currentTimeMillis();
      Object retval = invokeNextInterceptor(ctx, command);
      long t2 = System.currentTimeMillis();
      storeTimes = storeTimes + (t2 - t1);
      stores++;
      return retval;
   }

   @ManagedAttribute(description = "number of cache attribute hits")
   public long getHits()
   {
      return hits;
   }

   @ManagedAttribute(description = "number of cache attribute misses")
   public long getMisses()
   {
      return misses;
   }

   @ManagedAttribute(description = "number of cache attribute put operations")
   public long getStores()
   {
      return stores;
   }

   @ManagedAttribute(description = "number of cache eviction operations")
   public long getEvictions()
   {
      return evictions;
   }

   @ManagedAttribute(description = "hit/miss ratio for the cache")
   public double getHitMissRatio()
   {
      double total = hits + misses;
      if (total == 0)
      {
         return 0;
      }
      return (hits / total);
   }

   @ManagedAttribute(description = "read/writes ratio for the cache")
   public double getReadWriteRatio()
   {
      if (stores == 0)
      {
         return 0;
      }
      return (((double) (hits + misses) / (double) stores));
   }

   @ManagedAttribute(description = "average number of milliseconds for a read operation")
   public long getAverageReadTime()
   {
      long total = hits + misses;
      if (total == 0)
      {
         return 0;
      }
      return (hitTimes + missTimes) / total;
   }

   @ManagedAttribute(description = "average number of milliseconds for a write operation")
   public long getAverageWriteTime()
   {
      if (stores == 0)
      {
         return 0;
      }
      return (storeTimes) / stores;
   }

   @ManagedAttribute(description = "number of cache attributes")
   public int getNumberOfAttributes()
   {
      return dataContainer.getNumberOfAttributes();
   }

   @ManagedAttribute(description = "number of nodes in the cache")
   public int getNumberOfNodes()
   {
      return dataContainer.getNumberOfNodes();
   }

   @ManagedAttribute(description = "seconds since cache started")
   public long getElapsedTime()
   {
      return (System.currentTimeMillis() - start) / 1000;
   }

   @ManagedAttribute(description = "number of seconds since the cache statistics were last reset")
   public long getTimeSinceReset()
   {
      return (System.currentTimeMillis() - reset) / 1000;
   }

   @ManagedOperation
   public Map<String, Object> dumpStatistics()
   {
      Map<String, Object> retval = new HashMap<String, Object>();
      retval.put("Hits", hits);
      retval.put("Misses", misses);
      retval.put("Stores", stores);
      retval.put("Evictions", evictions);
      retval.put("NumberOfAttributes", dataContainer.getNumberOfAttributes());
      retval.put("NumberOfNodes", dataContainer.getNumberOfNodes());
      retval.put("ElapsedTime", getElapsedTime());
      retval.put("TimeSinceReset", getTimeSinceReset());
      retval.put("AverageReadTime", getAverageReadTime());
      retval.put("AverageWriteTime", getAverageWriteTime());
      retval.put("HitMissRatio", getHitMissRatio());
      retval.put("ReadWriteRatio", getReadWriteRatio());
      return retval;
   }

   @ManagedOperation
   public void resetStatistics()
   {
      hits = 0;
      misses = 0;
      stores = 0;
      evictions = 0;
      hitTimes = 0;
      missTimes = 0;
      storeTimes = 0;
      reset = System.currentTimeMillis();
   }
}

