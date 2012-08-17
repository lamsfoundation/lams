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
package org.jboss.cache.config;

import org.jboss.cache.optimistic.DataVersion;

/**
 * Used to override characteristics of specific calls to the cache.  The javadocs of each of the setters below detail functionality and behaviour.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @since 1.3.0
 */
public class Option implements Cloneable
{
   private boolean failSilently;
   private boolean cacheModeLocal;
   private DataVersion dataVersion;
   private boolean suppressLocking;
   private boolean forceDataGravitation;
   private boolean skipDataGravitation;

   private boolean forceWriteLock;
   private boolean skipCacheStatusCheck;

   private boolean forceAsynchronous;
   private boolean forceSynchronous;

   private long syncReplTimeout = -1;
   private int groupRequestMode = -1;

   private int lockAcquisitionTimeout = -1;
   private boolean suppressPersistence;
   private boolean suppressEventNotification;

   /**
    * @since 1.4.0
    */
   public boolean isSuppressLocking()
   {
      return suppressLocking;
   }

   /**
    * Suppresses acquiring locks for the given invocation.  Used with pessimistic locking only.  Use with extreme care, may lead to a breach in data integrity!
    *
    * @since 1.4.0
    */
   public void setSuppressLocking(boolean suppressLocking)
   {
      this.suppressLocking = suppressLocking;
   }


   /**
    * @since 1.3.0
    */
   public boolean isFailSilently()
   {
      return failSilently;
   }

   /**
    * suppress any failures in your cache operation, including version mismatches with optimistic locking, timeouts obtaining locks, transaction rollbacks.  If this is option is set, the method invocation will __never fail or throw an exception__, although it may not succeed.  With this option enabled the call will <b>not</b> participate in any ongoing transactions even if a transaction is running.
    *
    * @since 1.3.0
    */
   public void setFailSilently(boolean failSilently)
   {
      this.failSilently = failSilently;
   }

   /**
    * only applies to put() and remove() methods on the cache.
    *
    * @since 1.3.0
    */
   public boolean isCacheModeLocal()
   {
      return cacheModeLocal;
   }

   /**
    * overriding CacheMode from REPL_SYNC, REPL_ASYNC, INVALIDATION_SYNC, INVALIDATION_ASYNC to LOCAL.  Only applies to put() and remove() methods on the cache.
    *
    * @param cacheModeLocal
    * @since 1.3.0
    */
   public void setCacheModeLocal(boolean cacheModeLocal)
   {
      this.cacheModeLocal = cacheModeLocal;
   }

   /**
    * @since 1.3.0
    * @deprecated this is to support a deprecated locking scheme (Optimistic Locking).  Will be removed when Optimistic Locking support is removed.
    */
   @Deprecated
   @SuppressWarnings("deprecation")
   public DataVersion getDataVersion()
   {
      return dataVersion;
   }

   /**
    * Passing in an {@link org.jboss.cache.optimistic.DataVersion} instance when using optimistic locking will override the default behaviour of internally generated version info and allow the caller to handle data versioning.
    *
    * @since 1.3.0
    * @deprecated this is to support a deprecated locking scheme (Optimistic Locking).  Will be removed when Optimistic Locking support is removed.
    */
   @Deprecated
   @SuppressWarnings("deprecation")
   public void setDataVersion(DataVersion dataVersion)
   {
      this.dataVersion = dataVersion;
   }

   /**
    * @since 1.4.0
    */
   public boolean getForceDataGravitation()
   {
      return forceDataGravitation;
   }

   /**
    * Enables data gravitation calls if a cache miss is detected when using <a href="http://wiki.jboss.org/wiki/Wiki.jsp?page=JBossCacheBuddyReplicationDesign">Buddy Replication</a>.
    * Enabled only for a given invocation, and only useful if <code>autoDataGravitation</code> is set to <code>false</code>.
    * See <a href="http://wiki.jboss.org/wiki/Wiki.jsp?page=JBossCacheBuddyReplicationDesign">Buddy Replication</a> documentation for more details.
    *
    * @since 1.4.0
    */
   public void setForceDataGravitation(boolean enableDataGravitation)
   {
      this.forceDataGravitation = enableDataGravitation;
   }

   /**
    * @return true if skipDataGravitation is set to true.
    * @since 1.4.1.SP6
    */
   public boolean isSkipDataGravitation()
   {
      return skipDataGravitation;
   }

   /**
    * Suppresses data gravitation when buddy replication is used.  If true, overrides {@link #setForceDataGravitation(boolean)}
    * being set to true.  Typically used to suppress gravitation calls when {@link org.jboss.cache.config.BuddyReplicationConfig#setAutoDataGravitation(boolean)}
    * is set to true.
    *
    * @param skipDataGravitation
    * @since 1.4.1.SP6
    */
   public void setSkipDataGravitation(boolean skipDataGravitation)
   {
      this.skipDataGravitation = skipDataGravitation;
   }

   /**
    * Gets whether replication or invalidation should be done asynchronously,
    * even if the cache is configured in a synchronous mode.  Has no
    * effect if the call is occuring within a transactional context.
    *
    * @return <code>true</code> if replication/invalidation should be done
    *         asynchronously; <code>false</code> if the default mode
    *         configured for the cache should be used.
    */
   public boolean isForceAsynchronous()
   {
      return forceAsynchronous;
   }

   /**
    * Sets whether replication or invalidation should be done asynchronously,
    * even if the cache is configured in a synchronous mode.  Has no
    * effect if the call is occuring within a transactional context.
    *
    * @param forceAsynchronous <code>true</code> if replication/invalidation
    *                          should be done asynchronously; <code>false</code>
    *                          if the default mode configured for the cache
    *                          should be used.
    */
   public void setForceAsynchronous(boolean forceAsynchronous)
   {
      this.forceAsynchronous = forceAsynchronous;
   }

   /**
    * Gets whether replication or invalidation should be done synchronously,
    * even if the cache is configured in an asynchronous mode.  Has no
    * effect if the call is occuring within a transactional context.
    *
    * @return <code>true</code> if replication/invalidation should be done
    *         synchronously; <code>false</code> if the default mode
    *         configured for the cache should be used.
    */
   public boolean isForceSynchronous()
   {
      return forceSynchronous;
   }

   /**
    * Sets whether replication or invalidation should be done synchronously,
    * even if the cache is configured in an asynchronous mode.  Has no
    * effect if the call is occuring within a transactional context.
    *
    * @param forceSynchronous <code>true</code> if replication/invalidation
    *                         should be done synchronously; <code>false</code>
    *                         if the default mode configured for the cache
    *                         should be used.
    */
   public void setForceSynchronous(boolean forceSynchronous)
   {
      this.forceSynchronous = forceSynchronous;
   }

   /**
    * Gets any lock acquisition timeout configured for the call.
    *
    * @return the time in ms that lock acquisition attempts should block
    *         before failing with a TimeoutException.  A value < 0 indicates
    *         that the cache's default timeout should be used.
    */
   public int getLockAcquisitionTimeout()
   {
      return lockAcquisitionTimeout;
   }

   /**
    * Sets any lock acquisition timeout configured for the call.
    *
    * @param lockAcquisitionTimeout the time in ms that lock acquisition
    *                               attempts should block before failing with a
    *                               TimeoutException.  A value < 0 indicates
    *                               that the cache's default timeout should be used.
    */
   public void setLockAcquisitionTimeout(int lockAcquisitionTimeout)
   {
      this.lockAcquisitionTimeout = lockAcquisitionTimeout;
   }

   @Override
   public String toString()
   {
      return "Option{" +
            "failSilently=" + failSilently +
            ", cacheModeLocal=" + cacheModeLocal +
            ", dataVersion=" + dataVersion +
            ", suppressLocking=" + suppressLocking +
            ", lockAcquisitionTimeout=" + lockAcquisitionTimeout +
            ", forceDataGravitation=" + forceDataGravitation +
            ", skipDataGravitation=" + skipDataGravitation +
            ", forceAsynchronous=" + forceAsynchronous +
            ", forceSynchronous=" + forceSynchronous +
            ", suppressPersistence=" + suppressPersistence +
            ", suppressEventNotification=" + suppressEventNotification +
            '}';
   }

   /**
    * @see #copy()
    * @deprecated this method may disappear in future, please use copy() instead.
    */
   @Override
   @Deprecated
   public Option clone() throws CloneNotSupportedException
   {
      return (Option) super.clone();
   }

   /**
    * @return a new Option instance with all fields shallow-copied.
    */
   public Option copy()
   {
      try
      {
         return (Option) super.clone();
      }
      catch (CloneNotSupportedException e)
      {
         // should never happen
         return null;
      }
   }


   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final Option option = (Option) o;

      if (skipDataGravitation != option.skipDataGravitation) return false;
      if (cacheModeLocal != option.cacheModeLocal) return false;
      if (failSilently != option.failSilently) return false;
      if (forceDataGravitation != option.forceDataGravitation) return false;
      if (suppressLocking != option.suppressLocking) return false;
      if (dataVersion != null ? !dataVersion.equals(option.dataVersion) : option.dataVersion != null) return false;
      if (forceWriteLock != option.forceWriteLock) return false;
      if (forceAsynchronous != option.forceAsynchronous) return false;
      if (forceSynchronous != option.forceSynchronous) return false;
      if (lockAcquisitionTimeout != option.lockAcquisitionTimeout) return false;
      if (suppressPersistence != option.suppressPersistence) return false;
      if (suppressEventNotification != option.suppressEventNotification) return false;
      return true;
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (failSilently ? 1 : 0);
      result = 29 * result + (cacheModeLocal ? 1 : 0);
      result = 29 * result + (dataVersion != null ? dataVersion.hashCode() : 0);
      result = 29 * result + (suppressLocking ? 1 : 0);
      result = 29 * result + (forceDataGravitation ? 1 : 0);
      result = 29 * result + (skipDataGravitation ? 1 : 0);
      result = 29 * result + (forceWriteLock ? 0 : 1);
      result = 29 * result + (forceAsynchronous ? 0 : 1);
      result = 29 * result + (forceSynchronous ? 0 : 1);
      result = 29 * result + (lockAcquisitionTimeout);
      result = 29 * result + (suppressPersistence ? 0 : 1);
      result = 29 * result + (suppressEventNotification ? 0 : 1);
      return result;
   }

   /**
    * Resets this option to defaults.
    */
   public void reset()
   {
      this.skipDataGravitation = false;
      this.cacheModeLocal = false;
      this.failSilently = false;
      this.forceDataGravitation = false;
      this.suppressLocking = false;
      this.dataVersion = null;
      this.forceWriteLock = false;
      this.forceAsynchronous = false;
      this.forceSynchronous = false;
      this.lockAcquisitionTimeout = -1;
      this.suppressPersistence = false;
      this.suppressEventNotification = false;
   }

   /**
    * Forces a write lock to be acquired on the call, regardless of whether it is a read or write.
    * <p />
    * Note that this only applies to {@link org.jboss.cache.config.Configuration.NodeLockingScheme#MVCC} and {@link org.jboss.cache.config.Configuration.NodeLockingScheme#PESSIMISTIC}
    * node locking schemes, and is ignored if {@link org.jboss.cache.config.Configuration.NodeLockingScheme#OPTIMISTIC} is used.
    * <p />
    *
    * @param forceWriteLock
    * @since 2.0.0
    */
   public void setForceWriteLock(boolean forceWriteLock)
   {
      this.forceWriteLock = forceWriteLock;
   }


   /**
    * Tests whether a write lock has been forced on the call, regardless of whether it is a read or write.
    * <p />
    * Note that this only applies to {@link org.jboss.cache.config.Configuration.NodeLockingScheme#MVCC} and {@link org.jboss.cache.config.Configuration.NodeLockingScheme#PESSIMISTIC}
    * node locking schemes, and is ignored if {@link org.jboss.cache.config.Configuration.NodeLockingScheme#OPTIMISTIC} is used.
    * <p />
    *
    * @since 2.0.0
    */
   public boolean isForceWriteLock()
   {
      return forceWriteLock;
   }

   /**
    * If set to true, cache lifecycle checks will be skipped.  DO NOT USE unless you really know what you're doing.
    *
    * @since 2.0.0
    */
   public void setSkipCacheStatusCheck(boolean skipCacheStatusCheck)
   {
      this.skipCacheStatusCheck = skipCacheStatusCheck;
   }

   /**
    * @return true if skipCacheStatusCheck is true
    * @since 2.0.0
    */
   public boolean isSkipCacheStatusCheck()
   {
      return skipCacheStatusCheck;
   }

   /**
    * @return the value of the sync replication timeout (used when cache mode is either {@link org.jboss.cache.config.Configuration.CacheMode#REPL_SYNC}
    *         or {@link org.jboss.cache.config.Configuration.CacheMode#INVALIDATION_SYNC}) to be used for this specific call, or -1 (default) if the
    *         default value in {@link Configuration#getSyncReplTimeout()} is to be used instead.
    * @since 2.1.0
    */
   public long getSyncReplTimeout()
   {
      return syncReplTimeout;
   }

   /**
    * Used to override the value in {@link Configuration#getSyncReplTimeout()} (used when cache mode is either {@link org.jboss.cache.config.Configuration.CacheMode#REPL_SYNC}
    * or {@link org.jboss.cache.config.Configuration.CacheMode#INVALIDATION_SYNC}) for this specific invocation.  Defaults to -1,
    * which means use the default in the configuration.
    *
    * @param syncReplTimeout new timeout value for this invocation.
    * @since 2.1.0
    */
   public void setSyncReplTimeout(long syncReplTimeout)
   {
      this.syncReplTimeout = syncReplTimeout;
   }

   /**
    * @return overridden JGroups {@link org.jgroups.blocks.GroupRequest} mode to use, or -1 if the {@link org.jboss.cache.RPCManager}'s
    *         own logic is to be used to select a group request mode (this is the default).
    * @since 2.1.0
    */
   public int getGroupRequestMode()
   {
      return groupRequestMode;
   }

   /**
    * By default, the {@link org.jboss.cache.RPCManager} has inbuilt logic when it comes to selecting a group request mode.
    * This can be overridden by setting the group request mode here, using this method, for a specific invocation.
    *
    * @param groupRequestMode a group request mode, found in the {@link org.jgroups.blocks.GroupRequest} class.
    * @since 2.1.0
    */
   public void setGroupRequestMode(int groupRequestMode)
   {
      this.groupRequestMode = groupRequestMode;
   }

   /**
    * If set to true, any persistence to a cache loader will be suppressed for the current invocation only.  Does not apply to transactional calls.
    *
    * @return true if persistence is suppressed.
    * @since 3.0
    */
   public boolean isSuppressPersistence()
   {
      return suppressPersistence;
   }

   /**
    * If set to true, any persistence to a cache loader will be suppressed for the current invocation only.  Does not apply to transactional calls.
    *
    * @param suppressPersistence if true, will suppress persistence.
    * @since 3.0
    */
   public void setSuppressPersistence(boolean suppressPersistence)
   {
      this.suppressPersistence = suppressPersistence;
   }
   
   /**
    * Get whether event notifications for this invocation will be suppresed. By 
    * default is false which means that corresponding events are sent depending 
    * on the type of invocation.
    * 
    * @return true, if event notification will be suppressed for this invocation.
    */
   public boolean isSuppressEventNotification()
   {
      return suppressEventNotification;
   }

   /**
    * Set whether event notifications should be suppressed for this particular
    * cache or transaction invocation. 
    * 
    * @param suppressEventNotification <code>true</code> if event notification 
    *                          should be skipped; <code>false</code> if events
    *                          should be notified if there're any listeners.
    */
   public void setSuppressEventNotification(boolean suppressEventNotification)
   {
      this.suppressEventNotification = suppressEventNotification;
   }
}
