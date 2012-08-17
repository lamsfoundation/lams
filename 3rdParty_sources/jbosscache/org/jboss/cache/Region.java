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

import org.jboss.cache.annotations.Compat;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.EvictionPolicyConfig;
import org.jboss.cache.config.EvictionRegionConfig;
import org.jboss.cache.eviction.EvictedEventNode;
import org.jboss.cache.eviction.EvictionEvent;
import org.jboss.cache.eviction.EvictionPolicy;

/**
 * Defines characteristics such as class loading and eviction of {@link org.jboss.cache.Node}s belonging to a Region in a {@link Cache}.
 * A Region is described by an {@link #getFqn() Fqn} relative to the root of the cache.
 * All nodes and child nodes of this Fqn belong to this region.
 * <p/>
 * If a region is to be recognised as an eviction region (region of type {@link Type#EVICTION} then
 * it <b>must</b> have an {@link org.jboss.cache.config.EvictionRegionConfig} set using {@link #setEvictionRegionConfig(org.jboss.cache.config.EvictionRegionConfig)}.
 * <p/>
 * Similarly, to be recognised as a marshalling region (region of type {@link Type#MARSHALLING} then it <b>must</b> have a
 * {@link ClassLoader} registered using {@link #registerContextClassLoader(ClassLoader)}.
 * <p/>
 * Note that a single region can be both an eviction and marshalling region at the same time.
 * <p/>
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @see RegionManager
 * @since 2.0.0
 */
@Compat(notes = "Cloneable is extended for backward compat.")
public interface Region extends Comparable<Region>, Cloneable
{
   /**
    * Types of regions.
    */
   static enum Type
   {
      EVICTION, MARSHALLING, ANY
   }

   /**
    * Region status
    */
   static enum Status
   {
      ACTIVATING, ACTIVE, INACTIVATING, INACTIVE
   }

   /**
    * Registers a specific {@link ClassLoader} for this region,
    * overridding the default cache class loader.
    *
    * @param classLoader specific class loader
    */
   void registerContextClassLoader(ClassLoader classLoader);

   /**
    * @return the cache-wide configuration
    * @since 2.1.0
    */
   Configuration getCacheConfiguration();

   /**
    * Unregisters a registered {@link ClassLoader}s for this region.
    */
   void unregisterContextClassLoader();

   /**
    * Activates this region for replication.
    * By default, the entire cache is activated for replication at start-up.
    *
    * @throws RegionNotEmptyException if the {@link Fqn} that represents this region already exists and contains data or children.
    */
   void activate() throws RegionNotEmptyException;

   /**
    * Activates this region for replication, but if the {@link Fqn} that represents this region already exists and
    * either contains data or children, no state transfers take place.  The region is simply marked as active in this
    * case.
    */
   void activateIfEmpty();

   /**
    * Deactivates this region from being replicated.
    */
   void deactivate();

   /**
    * Sets this region as active - this only marks a flag
    * and does not actually activates or
    * deactivates this region.  Use {@link #activate()}
    * and {@link #deactivate()} for the full process.
    *
    * @param b
    */
   void setActive(boolean b);

   /**
    * Returns true if this region has been activated.
    *
    * @return true if this region has been activated.
    */
   boolean isActive();

   /**
    * Returns the configured {@link ClassLoader} for this region.
    *
    * @return a ClassLoader
    */
   ClassLoader getClassLoader();

   /**
    * Processes the eviction queues (primary and recycle queues) associated with this region.  A no-op if this is not an eviction region.
    *
    * @since 3.0
    */
   void processEvictionQueues();

   /**
    * Clears the node event queue used for processing eviction.
    */
   void resetEvictionQueues();

   /**
    * Configures this region for eviction.
    *
    * @param evictionRegionConfig configuration to set
    */
   void setEvictionRegionConfig(EvictionRegionConfig evictionRegionConfig);

   /**
    * @return the eviction region config, if any, set on the current region.
    */
   EvictionRegionConfig getEvictionRegionConfig();

   /**
    * Registers an eviction event on the region's eviction event queue for later processing by
    * {@link #processEvictionQueues()}.
    *
    * @param fqn               passed in to the constructor of {@link org.jboss.cache.eviction.EvictionEvent}
    * @param eventType         passed in to the constructor of {@link org.jboss.cache.eviction.EvictionEvent}
    * @param elementDifference passed in to the constructor of {@link org.jboss.cache.eviction.EvictionEvent}
    * @return an EvictedEventNode that has been created for this queue
    */
   EvictionEvent registerEvictionEvent(Fqn fqn, EvictionEvent.Type eventType, int elementDifference);

   /**
    * An overloaded version of {@link #registerEvictionEvent(Fqn, org.jboss.cache.eviction.EvictionEvent.Type, int)} which
    * uses a default elementDifference value.
    *
    * @param fqn       passed in to the constructor of {@link org.jboss.cache.eviction.EvictionEvent}
    * @param eventType passed in to the constructor of {@link org.jboss.cache.eviction.EvictionEvent}
    * @return an EvictedEventNode that has been created for this queue
    */
   EvictionEvent registerEvictionEvent(Fqn fqn, EvictionEvent.Type eventType);

   /**
    * Marks a {@link org.jboss.cache.Node} as currently in use, by adding an event to the eviction queue.
    * If there is an {@link org.jboss.cache.config.EvictionRegionConfig} associated with this region, and
    * it respects this event (e.g., {@link org.jboss.cache.eviction.LRUAlgorithm} does), then the {@link org.jboss.cache.Node} will not
    * be evicted until {@link #unmarkNodeCurrentlyInUse(Fqn)} is invoked.
    * <p/>
    * This mechanism can be used to prevent eviction of data that the application
    * is currently using, in the absence of any locks on the {@link org.jboss.cache.Node} where the
    * data is stored.
    *
    * @param fqn Fqn of the node.
    * @see #unmarkNodeCurrentlyInUse(Fqn)
    */
   void markNodeCurrentlyInUse(Fqn fqn, long timeout);

   /**
    * Adds an event to the eviction queue indicating that a node is no longer in use.
    *
    * @param fqn Fqn of the node.
    * @see #markNodeCurrentlyInUse(Fqn,long)
    */
   void unmarkNodeCurrentlyInUse(Fqn fqn);

   /**
    * Returns the {@link org.jboss.cache.Fqn} of this region.
    *
    * @return the Fqn
    */
   Fqn getFqn();

   /**
    * A mechanism to set status of a region, more fine grained control than just setActive();
    *
    * @param status status of the region
    * @since 2.1.0
    */
   void setStatus(Status status);

   /**
    * @return the region's status
    */
   Status getStatus();

   /**
    * copies the region - including eviction queue events - to a new Region instance, attached to a new Fqn root.
    * Typically used with Buddy Replication where region roots need to be adjusted.
    *
    * @param newRoot new root for the region - e.g., a buddy backup root.
    * @return a new Region instance.
    */
   Region copy(Fqn newRoot);

   // -------- deprecated interfaces retained for compatibility with 2.x. -----------


   /**
    * Configures an eviction policy for this region.
    * <p/>
    * <b>Note:</b> This is deprecated since this is an internal method and never was
    * meant to be a part of the public API.  Please do not treat this as public API, it may be removed in a future release
    * and its functionality is not guaranteed.
    * <p/>
    *
    * @param evictionPolicyConfig configuration to set
    * @deprecated
    */
   @Deprecated
   @Compat
   void setEvictionPolicy(EvictionPolicyConfig evictionPolicyConfig);

   /**
    * Returns an eviction policy configuration.
    * <p/>
    * <b>Note:</b> This is deprecated since this is an internal method and never was
    * meant to be a part of the public API.  Please do not treat this as public API, it may be removed in a future release
    * and its functionality is not guaranteed.
    * <p/>
    *
    * @return an eviction policy configuration
    * @deprecated
    */
   @Deprecated
   @Compat
   EvictionPolicyConfig getEvictionPolicyConfig();

   /**
    * Returns an eviction policy.
    * <p/>
    * <b>Note:</b> This is deprecated since this is an internal method and never was
    * meant to be a part of the public API.  Please do not treat this as public API, it may be removed in a future release
    * and its functionality is not guaranteed.
    * <p/>
    *
    * @return an eviction policy
    * @deprecated
    */
   @Deprecated
   @Compat
   EvictionPolicy getEvictionPolicy();

   /**
    * Returns the size of the node event queue, used by the eviction thread.
    * <p/>
    * <b>Note:</b> This is deprecated since this is an internal method and never was
    * meant to be a part of the public API.  Please do not treat this as public API, it may be removed in a future release
    * and its functionality is not guaranteed.
    * <p/>
    *
    * @return number of events
    * @deprecated
    */
   @Deprecated
   @Compat
   int nodeEventQueueSize();

   /**
    * Returns the most recent {@link org.jboss.cache.eviction.EvictedEventNode} added to the event queue by
    * {@link #putNodeEvent(org.jboss.cache.eviction.EvictedEventNode)}.
    * <p/>
    * <b>Note:</b> This is deprecated since this is an internal method and never was
    * meant to be a part of the public API.  Please do not treat this as public API, it may be removed in a future release
    * and its functionality is not guaranteed.
    * <p/>
    *
    * @return the last {@link org.jboss.cache.eviction.EvictedEventNode}, or null if no more events exist
    * @deprecated
    */
   @Compat
   @Deprecated
   EvictedEventNode takeLastEventNode();

   /**
    * Adds an {@link org.jboss.cache.eviction.EvictedEventNode} to the internal queue for processing
    * by the eviction thread.
    * <p/>
    * <b>Note:</b> This is deprecated since this is an internal method and never was
    * meant to be a part of the public API.  Please do not treat this as public API, it may be removed in a future release
    * and its functionality is not guaranteed.
    * <p/>
    *
    * @param event event to add
    * @deprecated
    */
   @Deprecated
   @Compat
   void putNodeEvent(EvictedEventNode event);

   /**
    * @return a clone
    * @deprecated
    */
   @Deprecated
   @Compat
   Region clone() throws CloneNotSupportedException;
}
