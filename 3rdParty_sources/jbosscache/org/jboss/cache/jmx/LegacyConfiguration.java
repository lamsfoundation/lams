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
package org.jboss.cache.jmx;

import org.jboss.cache.Region;
import org.jgroups.ChannelFactory;
import org.jgroups.jmx.JChannelFactoryMBean;
import org.w3c.dom.Element;

import javax.transaction.TransactionManager;

/**
 * Legacy configuration attributes from JBC 1.x.
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 * @deprecated use {@link org.jboss.cache.jmx.JmxRegistrationManager}
 */
@Deprecated
public interface LegacyConfiguration
{
   /**
    * Get the name of the replication group
    */
   String getClusterName();

   /**
    * Set the name of the replication group
    */
   void setClusterName(String name);

   /**
    * Sets whether marshalling uses scoped class loaders on a per region basis.
    * <p/>
    * This property must be set to <code>true</code> before any call to
    * {@link Region#registerContextClassLoader(ClassLoader)}.
    *
    * @param isTrue
    */
   void setUseRegionBasedMarshalling(boolean isTrue);

   /**
    * Gets whether marshalling uses scoped class loaders on a per region basis.
    *
    * @return true if region based marshalling is used.
    */
   boolean getUseRegionBasedMarshalling();

   /**
    * Gets whether the cache should create interceptor mbeans
    * that are used to capture and publish interceptor statistics.
    *
    * @return true if mbeans should be created for each interceptor
    */
   boolean getExposeManagementStatistics();

   void setExposeManagementStatistics(boolean expose);

   /**
    * @deprecated use {@link #getExposeManagementStatistics()}
    */
   @Deprecated
   boolean getUseInterceptorMbeans();

   /**
    * @deprecated use {@link #setExposeManagementStatistics(boolean)}
    */
   @Deprecated
   void setUseInterceptorMbeans(boolean expose);

   /**
    * Get the cluster properties (e.g. the protocol stack specification in case of JGroups)
    */
   String getClusterProperties();

   /**
    * Set the cluster properties. If the cache is to use the new properties, it has to be redeployed
    *
    * @param cluster_props The properties for the cluster (JGroups)
    */
   void setClusterProperties(String cluster_props);

   /**
    * Retrieves the cache loader configuration element
    *
    * @return whatever was passed to {@link #setCacheLoaderConfig(Element)}
    *         or <code>null</code> if nothing was
    */
   Element getCacheLoaderConfig();

   void setCacheLoaderConfig(Element cacheLoaderConfig);

   /**
    * @deprecated use {@link #getCacheLoaderConfig()}
    */
   @Deprecated
   Element getCacheLoaderConfiguration();

   /**
    * @deprecated use {@link #setCacheLoaderConfig(org.w3c.dom.Element)}
    */
   @Deprecated
   void setCacheLoaderConfiguration(Element cache_loader_config);

   boolean getSyncCommitPhase();

   void setSyncCommitPhase(boolean sync_commit_phase);

   boolean getSyncRollbackPhase();

   void setSyncRollbackPhase(boolean sync_rollback_phase);

   /**
    * @return whatever was passed to {@link #setEvictionPolicyConfig(Element)}
    *         or <code>null</code> if nothing was
    */
   Element getEvictionPolicyConfig();

   /**
    * Setup eviction policy configuration
    */
   void setEvictionPolicyConfig(Element config);

   /**
    * Gets the JGroups protocol stack config in W3C DOM Element form.
    *
    * @return the protocol stack, or <code>null</code> if it was not
    *         set via {@link #setClusterConfig(Element)}
    */
   Element getClusterConfig();

   /**
    * Convert a list of elements to the JG property string
    */
   void setClusterConfig(Element config);

   /**
    * Get the max time to wait until the initial state is retrieved. This is used in a replicating cache: when a new cache joins the cluster, it needs to acquire the (replicated) state of the other members to initialize itself. If no state has been received within <tt>timeout</tt> milliseconds, the map will be empty.
    *
    * @return long Number of milliseconds to wait for the state. 0 means to wait forever.
    * @deprecated use {@link #getStateRetrievalTimeout()}
    */
   @Deprecated
   long getInitialStateRetrievalTimeout();

   /**
    * Get the max time to wait until the state is retrieved. This is used in a replicating cache: when a new cache joins the cluster, it needs to acquire the (replicated) state of the other members to initialize itself. If no state has been received within <tt>timeout</tt> milliseconds, the map will be empty.
    *
    * @return long Number of milliseconds to wait for the state. 0 means to wait forever.
    */
   long getStateRetrievalTimeout();

   /**
    * Set the initial state transfer timeout (see {@link #getInitialStateRetrievalTimeout()})
    *
    * @deprecated use {@link #setStateRetrievalTimeout(long)}
    */
   @Deprecated
   void setInitialStateRetrievalTimeout(long timeout);

   /**
    * Set the state transfer timeout (see {@link #getStateRetrievalTimeout()})
    */
   void setStateRetrievalTimeout(long timeout);

   /**
    * Returns the current caching mode. Valid values are <ul> <li>LOCAL <li>REPL_ASYNC <li>REPL_SYNC <ul>
    *
    * @return String The caching mode
    */
   String getCacheMode();

   /**
    * Sets the default caching mode)
    */
   void setCacheMode(String mode) throws Exception;

   /**
    * Returns the default max timeout after which synchronous replication calls return.
    *
    * @return long Number of milliseconds after which a sync repl call must return. 0 means to wait forever
    */
   long getSyncReplTimeout();

   /**
    * Sets the default maximum wait time for synchronous replication to receive all results
    */
   void setSyncReplTimeout(long timeout);

   boolean getUseReplQueue();

   void setUseReplQueue(boolean flag);

   long getReplQueueInterval();

   void setReplQueueInterval(long interval);

   int getReplQueueMaxElements();

   void setReplQueueMaxElements(int max_elements);

   /**
    * Returns the transaction isolation level.
    */
   String getIsolationLevel();

   /**
    * Set the transaction isolation level. This determines the locking strategy to be used
    */
   void setIsolationLevel(String level);

   /**
    * Returns whether or not any initial state transfer or subsequent partial
    * state transfer following an <code>activateRegion</code> call should
    * include in-memory state. Allows for warm/hot caches (true/false). The
    * characteristics of a state transfer can be further defined by a cache
    * loader's FetchPersistentState property.
    */
   boolean getFetchInMemoryState();

   /**
    * Sets whether or not any initial or subsequent partial state transfer
    * should include in-memory state.
    */
   void setFetchInMemoryState(boolean flag);

   /**
    * Gets the format version of the data transferred during an initial state
    * transfer or a call to {@link Region#activate()}.  Different
    * releases of JBossCache may format this data differently; this property
    * identifies the format version being used by this cache instance.
    * <p/>
    * The default value for this property is
    * {@link org.jboss.cache.config.Configuration#DEFAULT_REPLICATION_VERSION}.
    * </p>
    *
    * @return a short identifying JBossCache release; e.g. <code>124</code>
    *         for JBossCache 1.2.4
    */
   String getReplicationVersion();

   /**
    * Sets the format version of the data transferred during an initial state
    * transfer or a call to {@link Region#activate()}.  Different
    * releases of JBossCache may format this data differently; this property
    * identifies the format version being used by this cache instance. Setting
    * this property to a value other than the default allows a cache instance
    * from a later release to interoperate with a cache instance from an
    * earlier release.
    *
    * @param version a short identifying JBossCache release;
    *                e.g. <code>124</code> for JBossCache 1.2.4
    */
   void setReplicationVersion(String version);

   /**
    * Default max time to wait for a lock. If the lock cannot be acquired within this time, a LockingException will be thrown.
    *
    * @return long Max number of milliseconds to wait for a lock to be acquired
    */
   long getLockAcquisitionTimeout();

   /**
    * Set the max time for lock acquisition. A value of 0 means to wait forever (not recomended). Note that lock acquisition timeouts may be removed in the future when we have deadlock detection.
    *
    * @param timeout
    */
   void setLockAcquisitionTimeout(long timeout);

   String getTransactionManagerLookupClass();

   /**
    * Sets the class of the TransactionManagerLookup impl. This will attempt to create an instance, and will throw an exception if this fails.
    *
    * @param cl
    * @throws Exception
    */
   void setTransactionManagerLookupClass(String cl) throws Exception;

   TransactionManager getTransactionManager();

   void setTransactionManager(TransactionManager manager);

   void setNodeLockingScheme(String nodeLockingScheme);

   String getNodeLockingScheme();

   /**
    * Gets whether the entire tree is inactive upon startup, only responding
    * to replication messages after activateRegion is
    * called to activate one or more parts of the tree.
    * <p/>
    * This property is only relevant if {@link org.jboss.cache.config.Configuration#isUseRegionBasedMarshalling()} is
    * <code>true</code>.
    */
   boolean isInactiveOnStartup();

   /**
    * Sets whether the entire tree is inactive upon startup, only responding
    * to replication messages after {@link Region#activate()} is
    * called to activate one or more parts of the tree.
    * <p/>
    * This property is only relevant if {@link org.jboss.cache.config.Configuration#isUseRegionBasedMarshalling()} is
    * <code>true</code>.
    */
   void setInactiveOnStartup(boolean inactiveOnStartup);

   /**
    * Sets the buddy replication configuration element
    *
    * @param config
    */
   void setBuddyReplicationConfig(Element config);

   /**
    * Retrieves the buddy replication configuration element
    *
    * @return whatever was passed to {@link #setBuddyReplicationConfig(Element)}
    *         or <code>null</code> if nothing was
    */
   Element getBuddyReplicationConfig();

   /**
    * Retrieves the JGroups multiplexer stack name if defined.
    *
    * @return the multiplexer stack name
    */
   String getMultiplexerStack();

   /**
    * Used with JGroups multiplexer, specifies stack to be used (e.g., fc-fast-minimalthreads)
    * This attribute is optional; if not provided, a default multiplexer stack will be used.
    *
    * @param stackName the name of the multiplexer stack
    */
   void setMultiplexerStack(String stackName);

   ChannelFactory getMuxChannelFactory();

   void setMuxChannelFactory(ChannelFactory factory);

   JChannelFactoryMBean getMultiplexerService();

   void setMultiplexerService(JChannelFactoryMBean muxService);

}