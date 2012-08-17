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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.annotations.Compat;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.EvictionAlgorithmConfig;
import org.jboss.cache.config.EvictionPolicyConfig;
import org.jboss.cache.config.EvictionRegionConfig;
import org.jboss.cache.eviction.*;
import org.jboss.cache.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation of a {@link Region}
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
public class RegionImpl implements Region
{
   private static final Log log = LogFactory.getLog(RegionImpl.class);
   private static final boolean trace = log.isTraceEnabled();
   private final RegionManager regionManager;
   private Fqn fqn;
   private Status status;
   private ClassLoader classLoader;
   private volatile BlockingQueue<EvictionEvent> evictionEventQueue = null;
   private int capacityWarnThreshold = 0;
   private EvictionRegionConfig evictionRegionConfig;
   private EvictionAlgorithm evictionAlgorithm;


   /**
    * Constructs a marshalling region from an fqn and region manager.
    */
   public RegionImpl(Fqn fqn, RegionManager regionManager)
   {
      this.fqn = fqn;
      this.regionManager = regionManager;
      status = !regionManager.isDefaultInactive() ? Status.ACTIVE : Status.INACTIVE;
   }

   /**
    * Constructs an eviction region from a policy and configuration, defined by an fqn and region manager.
    */
   public RegionImpl(EvictionRegionConfig config, Fqn fqn, RegionManager regionManager)
   {
      this(fqn, regionManager);
      this.evictionRegionConfig = config;
      createQueue();
   }

   public Configuration getCacheConfiguration()
   {
      if (regionManager != null && regionManager.getCache() != null)
         return regionManager.getCache().getConfiguration();
      else
         return null;
   }

   public void registerContextClassLoader(ClassLoader classLoader)
   {
      this.classLoader = classLoader;
   }

   public void unregisterContextClassLoader()
   {
      this.classLoader = null;
   }

   public void activate()
   {
      regionManager.activate(fqn);
      status = Status.ACTIVE;
   }

   public void activateIfEmpty()
   {
      regionManager.activateIfEmpty(fqn);
      status = Status.ACTIVE;
   }

   public void deactivate()
   {
      regionManager.deactivate(fqn);
      status = Status.INACTIVE;
   }

   public boolean isActive()
   {
      return status == Status.ACTIVE;
   }

   public ClassLoader getClassLoader()
   {
      return classLoader;
   }

   public void processEvictionQueues()
   {
      if (trace) log.trace("Processing eviction queue for region ["+getFqn()+"].  Queue size is " + evictionEventQueue.size());
      evictionAlgorithm.process(evictionEventQueue);
   }

   public Fqn getFqn()
   {
      return fqn;
   }

   public void setStatus(Status status)
   {
      this.status = status;
   }

   public Status getStatus()
   {
      return status;
   }

   public void setActive(boolean b)
   {
      status = b ? Status.ACTIVE : Status.INACTIVE;
   }

   // -------- eviction stuff -----

   public BlockingQueue<EvictionEvent> getEvictionEventQueue()
   {
      return evictionEventQueue;
   }

   public void markNodeCurrentlyInUse(Fqn fqn, long timeout)
   {
      registerEvictionEvent(fqn, EvictionEvent.Type.MARK_IN_USE_EVENT, 0).setInUseTimeout(timeout);
   }

   public void unmarkNodeCurrentlyInUse(Fqn fqn)
   {
      registerEvictionEvent(fqn, EvictionEvent.Type.UNMARK_USE_EVENT, 0);
   }

   @Override
   public String toString()
   {
      return "RegionImpl{" +
            "fqn=" + fqn +
            "; classloader=" + classLoader +
            "; status=" + status +
            "; eviction=" + (evictionAlgorithm != null) +
            "; evictionQueueSize=" + (evictionAlgorithm == null ? "-1" : evictionEventQueue.size()) +
            '}';
   }

   public int compareTo(Region other)
   {
      return getFqn().compareTo(other.getFqn());
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      RegionImpl region = (RegionImpl) o;

      if (fqn != null ? !fqn.equals(region.fqn) : region.fqn != null) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      return (fqn != null ? fqn.hashCode() : 0);
   }

   public void resetEvictionQueues()
   {
      evictionEventQueue.clear();
      evictionAlgorithm.getEvictionQueue().clear();
   }

   public void setEvictionRegionConfig(EvictionRegionConfig evictionRegionConfig)
   {
      if (trace) log.trace("setEvictionRegionConfig called with " + evictionRegionConfig);
      this.evictionRegionConfig = evictionRegionConfig;
      evictionAlgorithm = createEvictionAlgorithm(evictionRegionConfig.getEvictionAlgorithmConfig(), evictionRegionConfig.getEvictionActionPolicyClassName());
      if (evictionEventQueue == null) createQueue();
      evictionAlgorithm.initialize();
   }

   public EvictionRegionConfig getEvictionRegionConfig()
   {
      return evictionRegionConfig;
   }

   public EvictionEvent registerEvictionEvent(Fqn fqn, EvictionEvent.Type eventType)
   {
      return registerEvictionEvent(fqn, eventType, 0);
   }

   public EvictionEvent registerEvictionEvent(Fqn fqn, EvictionEvent.Type eventType, int elementDifference)
   {
      if (evictionAlgorithm.canIgnoreEvent(eventType)) return null;

      EvictionEvent event = new EvictionEvent(fqn, eventType, elementDifference);
      registerEvictionEvent(event);
      return event;
   }

   private void registerEvictionEvent(EvictionEvent ee)
   {
      try
      {
         if (evictionEventQueue == null) createQueue();// in case the queue does not exist yet.
         if (evictionEventQueue.size() > capacityWarnThreshold && log.isWarnEnabled())
         {
            log.warn("putNodeEvent(): eviction node event queue size is at 98% threshold value of capacity: " + evictionRegionConfig.getEventQueueSize() +
                  " Region: " + fqn +
                  " You will need to reduce the wakeUpIntervalSeconds parameter.");
         }
         evictionEventQueue.put(ee);
      }
      catch (InterruptedException e)
      {
         if (log.isDebugEnabled()) log.debug("Interrupted on adding event", e);
         // reinstate interrupt flag
         Thread.currentThread().interrupt();
      }
   }

   private void createQueue()
   {
      if (evictionEventQueue == null)
      {
         if (evictionRegionConfig == null)
         {
            throw new IllegalArgumentException("null eviction configuration");
         }
         int size = evictionRegionConfig.getEventQueueSize();
         capacityWarnThreshold = (98 * size) / 100 - 100;
         if (capacityWarnThreshold <= 0 && log.isWarnEnabled())
         {
            log.warn("Capacity warn threshold used in eviction is smaller than 1. Defined Event queu size is:" + size);
         }
         synchronized (this)
         {
            if (evictionEventQueue == null) evictionEventQueue = new LinkedBlockingQueue<EvictionEvent>(size);
         }
      }
   }

   private EvictionAlgorithm createEvictionAlgorithm(EvictionAlgorithmConfig algoConfig, String evictionActionPolicyClass)
   {
      if (trace) log.trace("Creating eviction algorithm using config " + algoConfig);
      if (algoConfig == null)
         throw new IllegalArgumentException("Eviction algorithm class must not be null!");

      if (evictionActionPolicyClass == null)
         throw new IllegalArgumentException("Eviction action policy class must not be null!");

      try
      {
         if (trace) log.trace("Instantiating " + evictionActionPolicyClass);
         EvictionActionPolicy actionPolicy = (EvictionActionPolicy) Util.getInstance(evictionActionPolicyClass);
         actionPolicy.setCache(regionManager.getCache());

         if (trace) log.trace("Instantiating " + algoConfig.getEvictionAlgorithmClassName());
         EvictionAlgorithm algorithm = (EvictionAlgorithm) Util.getInstance(algoConfig.getEvictionAlgorithmClassName());
         algorithm.setEvictionActionPolicy(actionPolicy);
         algorithm.assignToRegion(fqn, regionManager.getCache(), algoConfig, regionManager.getConfiguration());
         return algorithm;
      }
      catch (Exception e)
      {
         log.fatal("Unable to instantiate eviction algorithm " + algoConfig.getEvictionAlgorithmClassName(), e);
         throw new IllegalStateException(e);
      }
   }

   public Region copy(Fqn newRoot)
   {
      RegionImpl clone;
      clone = new RegionImpl(evictionRegionConfig, Fqn.fromRelativeFqn(newRoot, fqn), regionManager);
      clone.status = status;
      // we also need to copy all of the eviction event nodes to the clone's queue
      clone.createQueue();
      for (EvictionEvent een : this.evictionEventQueue)
      {
         clone.registerEvictionEvent(een.getFqn(), een.getEventType(), een.getElementDifference());
      }
      return clone;
   }

   @Deprecated
   @Compat
   @SuppressWarnings("deprecation")
   public void setEvictionPolicy(EvictionPolicyConfig evictionPolicyConfig)
   {
      try
      {
         // need to create an EvictionRegionConfig from this.
         EvictionRegionConfig erc = new EvictionRegionConfig(getFqn());
         String epClassName = evictionPolicyConfig.getEvictionPolicyClass();
         EvictionAlgorithmConfig eac = getEvictionAlgorithmConfig(epClassName);
         erc.setEvictionAlgorithmConfig(eac);
         if (!erc.isDefaultRegion())
         {
            erc.setDefaults(regionManager.getConfiguration().getEvictionConfig().getDefaultEvictionRegionConfig());
         }
         setEvictionRegionConfig(erc);
      }
      catch (Exception e)
      {
         throw new CacheException(e);
      }
   }

   /**
    * This is to provide backward compatibility and is not guaranteed to work for every type of eviction policy out
    * there, particularly custom ones.
    *
    * @param evictionPolicyClass
    * @return An eviction algorithm config.
    */
   @Deprecated
   private EvictionAlgorithmConfig getEvictionAlgorithmConfig(String evictionPolicyClass) throws Exception
   {
      Map<String, Class<? extends EvictionAlgorithmConfig>> legacyConfigMap = new HashMap<String, Class<? extends EvictionAlgorithmConfig>>();
      legacyConfigMap.put(FIFOPolicy.class.getName(), FIFOAlgorithmConfig.class);
      legacyConfigMap.put(LRUPolicy.class.getName(), LRUAlgorithmConfig.class);
      legacyConfigMap.put(LFUPolicy.class.getName(), LFUAlgorithmConfig.class);
      legacyConfigMap.put(MRUPolicy.class.getName(), MRUAlgorithmConfig.class);
      legacyConfigMap.put(ExpirationPolicy.class.getName(), ExpirationAlgorithmConfig.class);
      legacyConfigMap.put(ElementSizePolicy.class.getName(), ElementSizeAlgorithmConfig.class);
      legacyConfigMap.put(NullEvictionPolicy.class.getName(), NullEvictionAlgorithmConfig.class);

      Class<? extends EvictionAlgorithmConfig> c = legacyConfigMap.get(evictionPolicyClass);
      if (c != null)
      {
         // this is one of our "shipped" policies.  Easy mapping!
         return Util.getInstance(c);
      }
      else
      {
         // tougher.  This is a custom policy.
         // lets default to LRUPolicy first...
         final EvictionPolicy ep = (EvictionPolicy) Util.getInstance(evictionPolicyClass);
         ep.getEvictionAlgorithm();
         return new LRUAlgorithmConfig()
         {
            @Override
            public String getEvictionAlgorithmClassName()
            {
               return ep.getEvictionAlgorithm().getClass().getName();
            }
         };
      }
   }

   @Deprecated
   @Compat
   @SuppressWarnings("deprecation")
   public EvictionPolicyConfig getEvictionPolicyConfig()
   {
      return null;  //TODO: Autogenerated.  Implement me properly
   }

   @Deprecated
   @Compat
   @SuppressWarnings("deprecation")
   public EvictionPolicy getEvictionPolicy()
   {
      return null;  //TODO: Autogenerated.  Implement me properly
   }

   @Deprecated
   @Compat
   public int nodeEventQueueSize()
   {
      BlockingQueue<?> q = getEvictionEventQueue();
      return q == null ? 0 : q.size();
   }

   @Compat
   @Deprecated
   @SuppressWarnings("deprecation")
   public EvictedEventNode takeLastEventNode()
   {
      try
      {
         EvictionEvent ee = getEvictionEventQueue().poll(0, TimeUnit.SECONDS);
         if (ee instanceof EvictedEventNode) return (EvictedEventNode) ee;
         else return new EvictedEventNode(ee);
      }
      catch (InterruptedException e)
      {
         log.debug("trace", e);
      }
      return null;

   }

   @Deprecated
   @Compat
   @SuppressWarnings("deprecation")
   public void putNodeEvent(EvictedEventNode event)
   {
      this.registerEvictionEvent(event);
   }

   @Deprecated
   @Compat
   public Region clone() throws CloneNotSupportedException
   {
      return (Region) super.clone();
   }
}
