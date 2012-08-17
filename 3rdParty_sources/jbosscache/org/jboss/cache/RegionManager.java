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
import org.jboss.cache.config.EvictionConfig;
import org.jboss.cache.eviction.EvictionTimerTask;

import java.util.List;

/**
 * Manages regions.
 * <p/>
 * Prior to 3.0.0, this was a concrete class.  An interface was introduced in 3.0.0 for enhanced flexibility.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
public interface RegionManager
{
   /**
    * @return true if evictions are being processed.
    */
   boolean isUsingEvictions();

   /**
    * @return true if replication is by default inactive for new {@link org.jboss.cache.Region}s.
    */
   boolean isDefaultInactive();

   /**
    * Sets if replication for new {@link org.jboss.cache.Region}s is by default inactive.
    */
   void setDefaultInactive(boolean defaultInactive);

   /**
    * Helper utility that checks for a {@link ClassLoader} registered for the
    * given {@link org.jboss.cache.Fqn}, and if found sets it as the TCCL. If the given Fqn is
    * under the _BUDDY_BACKUP_ region, the equivalent region in the main
    * cache is used to find the {@link ClassLoader}.
    *
    * @param fqn Fqn pointing to a region for which a special classloader
    *            may have been registered.
    */
   void setContextClassLoaderAsCurrent(Fqn fqn);

   /**
    * Returns a region by {@link org.jboss.cache.Fqn}, creating it optionally if absent.  If the region does not exist and <tt>createIfAbsent</tt>
    * is <tt>false</tt>, a parent region which may apply to the {@link org.jboss.cache.Fqn} is sought.
    * <p/>
    * Note that this takes into account the fact that this may be a Buddy Backup Fqn.  If it is, the actual Fqn is calculated
    * and used instead.
    */
   Region getRegion(Fqn fqn, boolean createIfAbsent);

   /**
    * Retrieves a valid marshalling {@link org.jboss.cache.Region} after taking into account that this may be a Buddy Backup Fqn.
    * If the fqn passed in is null, the region has been deactivated or if a region cannot be found, this method returns a null.
    *
    * @param fqn of the region to locate
    * @return a region
    */
   Region getValidMarshallingRegion(Fqn fqn);

   /**
    * An overloaded form of {@link #getRegion(org.jboss.cache.Fqn,boolean)} that takes an additional {@link org.jboss.cache.Region.Type}
    * parameter to force regions of a specific type.
    * <p/>
    * Note that this takes into account the fact that this may be a Buddy Backup Fqn.  If it is, the actual Fqn is calculated
    * and used instead.
    *
    * @see org.jboss.cache.Region.Type
    */
   Region getRegion(Fqn fqn, Region.Type type, boolean createIfAbsent);

   /**
    * Returns a region using Fqn.fromString(fqn), calling {@link #getRegion(org.jboss.cache.Fqn,boolean)}
    *
    * @param fqn
    * @param createIfAbsent
    * @see #getRegion(org.jboss.cache.Fqn,boolean)
    */
   Region getRegion(String fqn, boolean createIfAbsent);

   /**
    * Removes a {@link Region} identified by the given fqn.
    *
    * @param fqn fqn of the region to remove
    * @return true if such a region existed and was removed.
    */
   boolean removeRegion(Fqn fqn);

   /**
    * Activates unmarshalling of replication messages for the region
    * rooted in the given Fqn.
    * <p/>
    * <strong>NOTE:</strong> This method will cause the creation of a node
    * in the local cache at <code>subtreeFqn</code> whether or not that
    * node exists anywhere else in the cluster.  If the node does not exist
    * elsewhere, the local node will be empty.  The creation of this node will
    * not be replicated.
    * <p/>
    *
    * @param fqn representing the region to be activated.
    * @throws org.jboss.cache.RegionNotEmptyException
    *          if the node <code>fqn</code>
    *          exists and already has either data or children
    */
   void activate(Fqn fqn) throws RegionNotEmptyException;

   /**
    * Attempts to activate a given region rooted at a given Fqn, similar to {@link #activate(org.jboss.cache.Fqn)} except
    * that if the fqn is currently already in use (probably already been activated) this method is a no-op.
    *
    * @param fqn which represents the region to activate
    */
   void activateIfEmpty(Fqn fqn);

   /**
    * Convenienve method.  If the region defined by fqn does not exist, {@link #isDefaultInactive()} is returned, otherwise
    * !{@link org.jboss.cache.Region#isActive()} is returned.
    *
    * @param fqn fqn to test
    * @return true if inactive
    */
   boolean isInactive(Fqn fqn);

   /**
    * Returns true if the region exists
    *
    * @param fqn  FQN of the region
    * @param type type of region to search for
    * @return true if the region exists
    */
   boolean hasRegion(Fqn fqn, Region.Type type);

   /**
    * Disables unmarshalling of replication messages for the region
    * rooted in the given Fqn.
    *
    * @param fqn
    */
   void deactivate(Fqn fqn);

   /**
    * Resets the region manager's regions registry
    */
   void reset();

   /**
    * Returns an ordered list of all regions.
    * Note that the ordered list returned is sorted according to the natural order defined in the {@link Comparable} interface, which {@link Region} extends.
    *
    * @param type Type of region to return
    * @return an ordered list of all regions, based on the type requested.
    */
   List<Region> getAllRegions(Region.Type type);

   /**
    * Sets if evictions are processed.
    */
   void setUsingEvictions(boolean usingEvictions);

   /**
    * Sets the eviction configuration.
    */
   void setEvictionConfig(EvictionConfig evictionConfig);

   /**
    * Returns a string containing debug information on every region.
    *
    * @return Regions as a string
    */
   String dumpRegions();

   CacheSPI getCache();

   /**
    * @return the eviction timer task attached to the region manager
    */
   EvictionTimerTask getEvictionTimerTask();

   /**
    * @return the configuration
    */
   Configuration getConfiguration();


   // ---------- compatibility interface -----------------
   /**
    * Starts the eviction processing thread.
    *
    * @deprecated
    */
   @Deprecated
   @Compat
   void startEvictionThread();

   /**
    * Stops the eviction processing thread
    *
    * @deprecated
    */
   @Deprecated
   @Compat
   void stopEvictionThread();
}
