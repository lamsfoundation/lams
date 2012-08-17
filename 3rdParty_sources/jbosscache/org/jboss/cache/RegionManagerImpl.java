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

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.jboss.cache.Region.Type.ANY;
import static org.jboss.cache.Region.Type.EVICTION;
import static org.jboss.cache.Region.Type.MARSHALLING;
import org.jboss.cache.buddyreplication.BuddyFqnTransformer;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.EvictionConfig;
import org.jboss.cache.config.EvictionRegionConfig;
import org.jboss.cache.eviction.EvictionTimerTask;
import org.jboss.cache.factories.annotations.Destroy;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.factories.annotations.Stop;
import org.jboss.cache.jmx.annotations.MBean;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jboss.cache.jmx.annotations.ManagedOperation;
import org.jboss.cache.lock.LockManager;
import org.jboss.cache.util.concurrent.locks.LockContainer;
import org.jboss.cache.util.concurrent.locks.ReentrantSharedLockContainer;
import org.jgroups.Address;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The default region manager, used with MVCC locking.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani</a>
 * @since 3.0.0
 */
@ThreadSafe
@MBean(objectName = "RegionManager", description = "Manages eviction and marshalling regions")
public class RegionManagerImpl implements RegionManager
{
   /**
    * The default region used in XML configuration files when defining eviction policies.  Any
    * eviction settings bound under this 'default' Fqn is appplied to {@link org.jboss.cache.Fqn#ROOT} internally so
    * any region that is not explicitly defined comes under the settings defined for this default.
    */
   public static final Fqn DEFAULT_REGION = Fqn.fromString("/_default_");

   private RegionRegistry regionsRegistry;
   private boolean defaultInactive;
   protected static final Log log = LogFactory.getLog(RegionManagerImpl.class);
   protected static final boolean trace = log.isTraceEnabled();
   CacheSPI<?, ?> cache;
   private boolean usingEvictions;
   private EvictionConfig evictionConfig;
   private final EvictionTimerTask evictionTimerTask = new EvictionTimerTask();

   private final LockContainer<Fqn> regionLocks = new ReentrantSharedLockContainer<Fqn>(4);
   protected Configuration configuration;
   protected RPCManager rpcManager;
   protected LockManager lockManager;
   protected BuddyFqnTransformer buddyFqnTransformer;
   private boolean isUsingBR;

   // -------- region lock helpers

   protected final boolean isRegionLocked(Fqn fqn)
   {
      return regionLocks.isLocked(fqn);
   }

   protected final void lock(Fqn fqn)
   {
      regionLocks.acquireLock(fqn);
   }

   protected final void unlock(Fqn fqn)
   {
      regionLocks.releaseLock(fqn);
   }

   @Inject
   public void injectDependencies(CacheSPI cache, Configuration configuration, RPCManager rpcManager, LockManager lockManager,
                                  BuddyFqnTransformer transformer, RegionRegistry regionsRegistry)
   {
      this.cache = cache;
      this.rpcManager = rpcManager;
      this.configuration = configuration;
      this.lockManager = lockManager;
      this.buddyFqnTransformer = transformer;
      this.regionsRegistry = regionsRegistry;
   }

   @Start
   public void start()
   {
      if (trace) log.trace("Starting region manager");
      isUsingBR = configuration.getBuddyReplicationConfig() != null && configuration.getBuddyReplicationConfig().isEnabled();
      evictionConfig = configuration.getEvictionConfig();
      if (evictionConfig != null && evictionConfig.isValidConfig())
      {
         this.evictionConfig = configuration.getEvictionConfig();
         // start with the default region
         EvictionRegionConfig defaultRegion = configuration.getEvictionConfig().getDefaultEvictionRegionConfig();
         if (defaultRegion.getEvictionAlgorithmConfig() != null) defaultRegion.getEvictionAlgorithmConfig().validate();

         // validate individual region configs now
         for (EvictionRegionConfig erc : configuration.getEvictionConfig().getEvictionRegionConfigs())
         {
            evictionConfig.applyDefaults(erc);
            erc.validate();
         }

         setEvictionConfig(configuration.getEvictionConfig());
         setUsingEvictions(true);
      }
      else
      {
         setUsingEvictions(false);
         log.debug("Not using an EvictionPolicy");
      }

      setDefaultInactive(configuration.isInactiveOnStartup());

      if (isUsingEvictions())
      {
         evictionTimerTask.init(evictionConfig.getWakeupInterval(), configuration.getRuntimeConfig().getEvictionTimerThreadFactory(), regionsRegistry);
      }
   }

   @Stop
   protected void stop()
   {
      if (isUsingEvictions()) evictionTimerTask.stop();
   }

   @Destroy
   protected void destroy()
   {
      regionsRegistry.clear();
      regionLocks.reset();
   }

   public boolean isUsingEvictions()
   {
      return usingEvictions;
   }

   public boolean isDefaultInactive()
   {
      return defaultInactive;
   }

   public void setDefaultInactive(boolean defaultInactive)
   {
      this.defaultInactive = defaultInactive;
      Region defaultRegion = regionsRegistry.get(Fqn.ROOT);
      if (defaultRegion != null) defaultRegion.setActive(!defaultInactive);
   }

   public void setContextClassLoaderAsCurrent(Fqn fqn)
   {
      if (fqn.isChildOf(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN))
      {
         if (fqn.size() <= 2)
         {
            fqn = Fqn.ROOT;
         }
         else
         {
            fqn = fqn.getSubFqn(2, fqn.size());
         }
      }
      Region region = getRegion(fqn, false);
      ClassLoader regionCL = (region == null) ? null : region.getClassLoader();
      if (regionCL != null)
      {
         Thread.currentThread().setContextClassLoader(regionCL);
      }

   }

   public Region getRegion(Fqn fqn, boolean createIfAbsent)
   {
      return getRegion(fqn, ANY, createIfAbsent);
   }

   public Region getValidMarshallingRegion(Fqn fqn)
   {
      if (fqn == null) return null;
      return getRegion(fqn, Region.Type.MARSHALLING, false);
   }

   public Region getRegion(Fqn fqn, Region.Type type, boolean createIfAbsent)
   {
      if (isUsingBR && fqn != null && buddyFqnTransformer.isBackupFqn(fqn))
      {
         fqn = buddyFqnTransformer.getActualFqn(fqn);
      }

      if (trace) log.trace("Contents of RegionsRegistry: " + regionsRegistry);
      Fqn fqnToUse = fqn;
      if (DEFAULT_REGION.equals(fqnToUse)) fqnToUse = Fqn.ROOT;
      // first see if a region for this specific Fqn exists
      if (regionsRegistry.containsKey(fqnToUse))
      {
         Region r = regionsRegistry.get(fqnToUse);

         // this is a very poor way of telling whether a region is a marshalling one or an eviction one.  :-(
         // mandates that class loaders be registered for marshalling regions.
         if (type == ANY
               || (type == MARSHALLING && r.getClassLoader() != null)
               || (type == EVICTION && r.getEvictionRegionConfig() != null))
         {
            return r;
         }
      }

      // if not, attempt to create one ...
      if (createIfAbsent)
      {
         Region r = new RegionImpl(fqnToUse, this);

         // could be created concurrently; so make sure we use appropriate methods on regionsRegistry for this.
         Region previous = regionsRegistry.putIfAbsent(fqnToUse, r);
         if (previous != null) r = previous;
         if (type == MARSHALLING)
         {
            // insert current class loader into region so at least it is recognised as a marshalling region
            r.registerContextClassLoader(getClass().getClassLoader());
         }
         return r;
      }

      // else try and find a parent which has a defined region, may return null if nothing is defined.
      Region nextBestThing = null;
      Fqn nextFqn = fqnToUse;

      while (nextBestThing == null)
      {
         nextFqn = nextFqn.getParent();
         if (regionsRegistry.containsKey(nextFqn))
         {
            Region r = regionsRegistry.get(nextFqn);
            if (trace) log.trace("Trying next region " + nextFqn + " and got " + r);

            // this is a very poor way of telling whether a region is a marshalling one or an eviction one.  :-(
            // mandates that class loaders be registered for marshalling regions.
            if (type == ANY
                  || (type == MARSHALLING && r.getClassLoader() != null)
                  || (type == EVICTION && r.getEvictionRegionConfig() != null))
            {
               nextBestThing = r;
            }
         }
         if (nextFqn.isRoot()) break;
      }

      // test if the default region has been defined.  If not, and if the request
      // is for an eviction region, return null
      if (type == EVICTION && nextBestThing != null && nextBestThing.getFqn().isRoot() && !regionsRegistry.containsKey(Fqn.ROOT))
      {
         log.trace("No default eviction region; returning null");
         nextBestThing = null;
      }

      return nextBestThing;
   }

   public Region getRegion(String fqn, boolean createIfAbsent)
   {
      return getRegion(Fqn.fromString(fqn), createIfAbsent);
   }

   public boolean removeRegion(Fqn fqn)
   {
      Region r = regionsRegistry.remove(fqn);
      if (r == null) return false;

      return true;
   }

   public EvictionTimerTask getEvictionTimerTask()
   {
      return evictionTimerTask;
   }

   public Configuration getConfiguration()
   {
      return configuration;
   }

   public void activate(Fqn fqn) throws RegionNotEmptyException
   {
      activate(fqn, false);
   }

   public void activateIfEmpty(Fqn fqn)
   {
      activate(fqn, true);
   }

   private void activate(Fqn fqn, boolean suppressRegionNotEmptyException)
   {
      try
      {
         if (trace) log.trace("Activating region " + fqn);
         Region r = getRegion(fqn, false);
         if (r != null)
         {
            if (!defaultInactive && r.getClassLoader() == null)
            {
               // This region's state will no match that of a non-existent one
               // So, there is no reason to keep this region any more

               // (Brian) We shouldn't do this anymore; now outside code
               // can have a ref to the region!!
               removeRegion(fqn);
            }
            else
            {
               //r.activate();
               r.setStatus(Region.Status.ACTIVATING);
               if (configuration.isFetchInMemoryState())
               {
                  activateRegion(r.getFqn(), suppressRegionNotEmptyException);
               }
               r.setActive(true);
            }
         }
         else if (defaultInactive)
         {
            // "Active" region is not the default, so create a region
            r = getRegion(fqn, true);
            // FIXME - persistent state transfer counts too!
            r.setStatus(Region.Status.ACTIVATING);
            if (configuration.isFetchInMemoryState())
            {
               activateRegion(r.getFqn(), suppressRegionNotEmptyException);
            }
            r.setActive(true);
         }
      }
      catch (RuntimeException re)
      {
         throw re;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   /**
    * Causes the cache to transfer state for the subtree rooted at
    * <code>subtreeFqn</code> and to begin accepting replication messages
    * for that subtree.
    * <p/>
    * <strong>NOTE:</strong> This method will cause the creation of a node
    * in the local cache at <code>subtreeFqn</code> whether or not that
    * node exists anywhere else in the cluster.  If the node does not exist
    * elsewhere, the local node will be empty.  The creation of this node will
    * not be replicated.
    *
    * @param fqn Fqn string indicating the uppermost node in the
    *            portion of the cache that should be activated.
    * @throws RegionNotEmptyException if the node <code>subtreeFqn</code>
    *                                 exists and has either data or children
    */
   private void activateRegion(Fqn fqn, boolean suppressRegionNotEmptyException)
   {
      // Check whether the node already exists and has data
      Node subtreeRoot = cache.getNode(fqn); // NOTE this used to be a peek!

      if (log.isDebugEnabled())
      {
         log.debug("activating " + fqn);
      }

      try
      {

         // Add this fqn to the set of those we are activating
         // so calls to _getState for the fqn can return quickly
         lock(fqn);

         BuddyManager buddyManager = cache.getBuddyManager();
         // Request partial state from the cluster and integrate it
         if (buddyManager == null)
         {
            // Get the state from any node that has it and put it
            // in the main cache
            if (subtreeRoot == null)
            {
               // We'll update this node with the state we receive
               // need to obtain all necessary locks.
               Node root = cache.getRoot();
               cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
               subtreeRoot = root.addChild(fqn);
               cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(false);
            }

            List<Address> members = cache.getMembers();

            // Don't bother trying to fetch state if we are in LOCAL mode
            if (members != null && !members.isEmpty())
            {
               rpcManager.fetchPartialState(members, subtreeRoot.getFqn());
            }
         }
         else if (!buddyFqnTransformer.isBackupFqn(fqn))
         {
            // Get the state from each DataOwner and integrate in their
            // respective buddy backup cache
            List<Address> buddies = buddyManager.getBackupDataOwners();
            for (Address buddy : buddies)
            {
               List<Address> sources = new ArrayList<Address>(1);
               if (!cache.getMembers().contains(buddy))
               {
                  continue;
               }
               sources.add(buddy);
               Fqn buddyRoot = buddyFqnTransformer.getBackupFqn(buddy, fqn);
               subtreeRoot = cache.peek(buddyRoot, false, false);
               if (subtreeRoot == null)
               {
                  // We'll update this node with the state we receive
                  // need to obtain all necessary locks.
                  // needs to be a LOCAL call!
                  NodeSPI root = cache.getRoot();
                  cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(true);
                  subtreeRoot = root.addChild(buddyRoot);
                  cache.getInvocationContext().getOptionOverrides().setCacheModeLocal(false);
               }
               rpcManager.fetchPartialState(sources, fqn, subtreeRoot.getFqn());
            }
         }
         else
         {
            log.info("Attempting to activate a backup region.  Not attempting to retrieve any state as this will be pushed.");
         }
      }
      catch (Throwable t)
      {
         log.error("failed to activate " + fqn, t);

         // "Re-deactivate" the region
         try
         {
            inactivateRegion(fqn);
         }
         catch (Exception e)
         {
            log.error("failed inactivating " + fqn, e);
            // just swallow this one and throw the first one
         }

         // Throw the exception on, wrapping if necessary
         if (t instanceof RegionNotEmptyException)
         {
            if (!suppressRegionNotEmptyException) throw (RegionNotEmptyException) t;
         }
         else if (t instanceof CacheException)
         {
            throw (CacheException) t;
         }
         else
         {
            throw new CacheException(t.getClass().getName() + " " +
                  t.getLocalizedMessage(), t);
         }
      }
      finally
      {
         unlock(fqn);
      }
   }

   public boolean isInactive(Fqn fqn)
   {
      Region region = getRegion(fqn, false);
      return region == null ? defaultInactive : !region.isActive();
   }


   /**
    * Causes the cache to stop accepting replication events for the subtree
    * rooted at <code>subtreeFqn</code> and evict all nodes in that subtree.
    * <p/>
    * This is legacy code and should not be called directly.  This is a private method for now and will be refactored out.
    * You should be using {@link #activate(Fqn)} and {@link #deactivate(Fqn)}
    * <p/>
    *
    * @param fqn Fqn string indicating the uppermost node in the
    *            portion of the cache that should be activated.
    * @throws CacheException        if there is a problem evicting nodes
    * @throws IllegalStateException if {@link org.jboss.cache.config.Configuration#isUseRegionBasedMarshalling()} is <code>false</code>
    */
   protected void inactivateRegion(Fqn fqn) throws CacheException
   {
      NodeSPI subtreeRoot = null;
      InvocationContext ctx = cache.getInvocationContext();
      ctx.getOptionOverrides().setLockAcquisitionTimeout((int) (cache.getConfiguration().getLockAcquisitionTimeout() + 5000));

      try
      {
         // Record that this fqn is in status change, so can't provide state
         lock(fqn);

         if (!isInactive(fqn))
         {
            deactivate(fqn);
         }

         // Create a list with the Fqn in the main cache and any buddy backup trees
         BuddyManager buddyManager = cache.getBuddyManager();
         ArrayList<Fqn> list = new ArrayList<Fqn>();
         list.add(fqn);

         if (buddyManager != null)
         {
            Set buddies = cache.peek(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN, false, false).getChildrenNames();
            if (buddies != null)
            {
               for (Object buddy : buddies)
               {
                  list.add(buddyFqnTransformer.getBackupFqn((String) buddy, fqn));
               }
            }
         }

         // Remove the subtree from the main cache  and any buddy backup trees
         for (Fqn subtree : list)
         {
            subtreeRoot = cache.peek(subtree, false);

            if (subtreeRoot != null)
            {
               // Remove the subtree
               cache.evict(subtree, true);
            }
         }
      }
      finally
      {
         unlock(fqn);
      }
   }

   public boolean hasRegion(Fqn fqn, Region.Type type)
   {
      Region r = regionsRegistry.get(fqn);
      if (r == null) return false;
      switch (type)
      {
         case ANY:
            return true;
         case EVICTION:
            return r.getEvictionRegionConfig() != null;
         case MARSHALLING:
            return r.isActive() && r.getClassLoader() != null;
      }
      // should never reach here?
      return false;
   }

   public void deactivate(Fqn fqn)
   {
      try
      {
         Region region = getRegion(fqn, false);

         if (region != null)
         {
            if (defaultInactive && region.getClassLoader() == null)
            {
               // This region's state will no match that of a non-existent one
               // So, there is no reason to keep this region any more

               // FIXME (Brian) We shouldn't do this anymore; now outside code can have a ref to the region!!
               removeRegion(fqn);
            }
            else
            {
               //region.deactivate();
               region.setActive(false);
               // FIXME - we should always clean up; otherwise stale data is in memory!
               if (cache.getConfiguration().isFetchInMemoryState())
               {
                  inactivateRegion(fqn);
               }
            }
         }
         else if (!defaultInactive)
         {
            region = getRegion(fqn, true);
            region.setActive(false);
            // FIXME - we should always clean up; otherwise stale data is in memory!
            if (cache.getConfiguration().isFetchInMemoryState())
            {
               inactivateRegion(fqn);
            }
         }
      }
      catch (RuntimeException re)
      {
         throw re;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   public void reset()
   {
      regionsRegistry.clear();
   }

   public List<Region> getAllRegions(Region.Type type)
   {
      List<Region> regions;

      if (type != ANY)
      {
         regions = new ArrayList<Region>();
         // we need to loop thru the regions and only select specific regions to rtn.
         for (Region r : regionsRegistry.values())
         {
            if ((type == EVICTION && r.getEvictionRegionConfig() != null) ||
                  (type == MARSHALLING && r.isActive() && r.getClassLoader() != null))
            {
               regions.add(r);
            }
         }
      }
      else
      {
         // put all regions
         regions = new ArrayList<Region>(regionsRegistry.values());
      }

      Collections.sort(regions);

      return regions;
   }

   public void setUsingEvictions(boolean usingEvictions)
   {
      this.usingEvictions = usingEvictions;
   }

   public void setEvictionConfig(EvictionConfig evictionConfig)
   {
      // JBAS-1288
      // Try to establish a default region if there isn't one already
//      boolean needDefault;
      List<EvictionRegionConfig> ercs = evictionConfig.getEvictionRegionConfigs();
      // Only add a default region if there are no regions. This is
      // contrary to the idea that there *must* be a default region, but some
      // unit tests fail w/ APPROACH 1, so for now we go with this approach.
//      needDefault = ercs.size() == 0;
      if (evictionConfig.getDefaultEvictionRegionConfig().getEvictionAlgorithmConfig() != null &&
            !ercs.contains(evictionConfig.getDefaultEvictionRegionConfig())) // then the default is a real region too; not just a template for others
      {
         ercs.add(0, evictionConfig.getDefaultEvictionRegionConfig());
      }

      // create regions for the regions defined in the evictionConfig.
      // scan to be sure the _default_ region isn't added twice
      boolean setDefault = false;
      for (EvictionRegionConfig erc : ercs)
      {
         Fqn fqn = erc.getRegionFqn();
         if (fqn == null) throw new ConfigurationException("Regions cannot be configured with a null region fqn.  If you configured this region programmatically, ensure that you set the region fqn in EvictionRegionConfig");
         if (trace) log.trace("Creating eviction region " + fqn);

         if (fqn.equals(DEFAULT_REGION) || fqn.isRoot())
         {
            if (setDefault)
            {
               throw new ConfigurationException("A default region for evictions has already been set for this cache");
            }
            if (trace) log.trace("Applying settings for default region to Fqn.ROOT");
            fqn = Fqn.ROOT;
            setDefault = true;
         }
         Region r = getRegion(fqn, true);
         evictionConfig.applyDefaults(erc);
         r.setEvictionRegionConfig(erc);
      }
   }

   @ManagedOperation(description = "A String representation of all registered regions")
   public String dumpRegions()
   {
      StringBuilder sb = new StringBuilder();
      if (regionsRegistry != null)
      {
         for (Region r : regionsRegistry.values())
         {
            sb.append("\tRegion ").append(r);
            sb.append("\n");
         }
      }
      return sb.toString();
   }

   /**
    * Returns a string containing debug information on every region.
    */
   @Override
   public String toString()
   {
      return "RegionManager " + dumpRegions();
   }

   public CacheSPI getCache()
   {
      return cache;
   }


   // --------- for backward compat --------------
   /**
    * Starts the eviction processing thread.
    */
   public void startEvictionThread()
   {
      evictionTimerTask.init(evictionConfig.getWakeupInterval(), configuration.getRuntimeConfig().getEvictionTimerThreadFactory(), regionsRegistry);
   }

   /**
    * Stops the eviction processing thread
    */
   public void stopEvictionThread()
   {
      evictionTimerTask.stop();
   }

   @ManagedAttribute(name = "numRegions", description = "A count of all regions")
   public int getNumRegions()
   {
      return regionsRegistry.size();
   }
}
