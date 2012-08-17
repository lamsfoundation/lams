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

import org.jboss.cache.Version;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.config.parsing.JGroupsStackParser;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.lock.IsolationLevel;
import org.jboss.cache.marshall.Marshaller;
import org.w3c.dom.Element;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Encapsulates the configuration of a Cache.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 */
@NonVolatile
public class Configuration extends ConfigurationComponent
{
   private static final long serialVersionUID = 5553791890144997466L;
   private transient JGroupsStackParser jGroupsStackParser = new JGroupsStackParser();

   /**
    * Behavior of the JVM shutdown hook registered by the cache
    */
   public static enum ShutdownHookBehavior
   {
      /**
       * By default a shutdown hook is registered if no MBean server (apart from the JDK default) is detected.
       */
      DEFAULT,
      /**
       * Forces the cache to register a shutdown hook even if an MBean server is detected.
       */
      REGISTER,
      /**
       * Forces the cache NOT to register a shutdown hook, even if no MBean server is detected.
       */
      DONT_REGISTER
   }

   /**
    * Cache replication mode.
    */
   public static enum CacheMode
   {
      /**
       * Data is not replicated.
       */
      LOCAL,

      /**
       * Data replicated synchronously.
       */
      REPL_SYNC,

      /**
       * Data replicated asynchronously.
       */
      REPL_ASYNC,

      /**
       * Data invalidated synchronously.
       */
      INVALIDATION_SYNC,

      /**
       * Data invalidated asynchronously.
       */
      INVALIDATION_ASYNC;

      /**
       * Returns true if the mode is invalidation, either sync or async.
       */
      public boolean isInvalidation()
      {
         return this == INVALIDATION_SYNC || this == INVALIDATION_ASYNC;
      }

      public boolean isSynchronous()
      {
         return this == REPL_SYNC || this == INVALIDATION_SYNC || this == LOCAL;
      }

   }

   public static CacheMode legacyModeToCacheMode(int legacyMode)
   {
      switch (legacyMode)
      {
         case 1:
            return CacheMode.LOCAL;
         case 2:
            return CacheMode.REPL_ASYNC;
         case 3:
            return CacheMode.REPL_SYNC;
         case 4:
            return CacheMode.INVALIDATION_ASYNC;
         case 5:
            return CacheMode.INVALIDATION_SYNC;
         default:
            throw new IllegalArgumentException("Unknown legacy cache mode " +
                  legacyMode);
      }
   }

   /**
    * Cache node locking scheme.
    */
   public static enum NodeLockingScheme
   {
      /**
       * Data is locked using the MVCC locking scheme.  This is the default locking scheme in JBoss Cache 3.0.0.
       *
       * @see <a href="http://wiki.jboss.org/wiki/JBossCacheMVCC">http://wiki.jboss.org/wiki/JBossCacheMVCC</a>
       */
      MVCC,
      /**
       * Data is exclusively locked during modification.
       *
       * @see <a href="http://en.wikipedia.org/wiki/Concurrency_control">http://en.wikipedia.org/wiki/Concurrency_control (pessimistic)</a>
       */
      PESSIMISTIC,
      /**
       * Data is unlocked during modification, modifications merged at commit.
       *
       * @see <a href="http://en.wikipedia.org/wiki/Optimistic_concurrency_control">http://en.wikipedia.org/wiki/Optimistic_concurrency_control</a>
       */
      OPTIMISTIC;

      /**
       * @return true if the node locking scheme uses versioning.
       */
      public boolean isVersionedScheme()
      {
         return this == OPTIMISTIC;
      }
   }

   /**
    * Default replication version, from {@link Version#getVersionShort}.
    */
   public static final short DEFAULT_REPLICATION_VERSION = Version.getVersionShort();

   // ------------------------------------------------------------------------------------------------------------
   //   CONFIGURATION OPTIONS
   // ------------------------------------------------------------------------------------------------------------

   private String clusterName = "JBossCache-Cluster";
   private String clusterConfig = null;
   private boolean useReplQueue = false;
   @Dynamic
   private int replQueueMaxElements = 1000;
   @Dynamic
   private long replQueueInterval = 5000;
   private boolean exposeManagementStatistics = true;
   @Dynamic
   private boolean fetchInMemoryState = true;
   private boolean nonBlockingStateTransfer = false;
   private short replicationVersion = DEFAULT_REPLICATION_VERSION;
   @Dynamic
   private long lockAcquisitionTimeout = 10000;
   @Dynamic
   private long syncReplTimeout = 15000;
   private CacheMode cacheMode = CacheMode.LOCAL;
   private boolean inactiveOnStartup = false;
   @Dynamic
   private long stateRetrievalTimeout = 10000;
   private IsolationLevel isolationLevel = IsolationLevel.REPEATABLE_READ;
   @Dynamic
   private boolean lockParentForChildInsertRemove = false;
   @Dynamic
   private EvictionConfig evictionConfig = null;
   private boolean useRegionBasedMarshalling = false;
   private String transactionManagerLookupClass = null;
   private CacheLoaderConfig cacheLoaderConfig = null;
   @Dynamic
   private boolean syncCommitPhase = false;
   @Dynamic
   private boolean syncRollbackPhase = false;
   private BuddyReplicationConfig buddyReplicationConfig;

   @Deprecated
   private NodeLockingScheme nodeLockingScheme = NodeLockingScheme.MVCC;
   private String muxStackName = null;
   private boolean usingMultiplexer = false;
   private transient RuntimeConfig runtimeConfig;
   private String marshallerClass;
   private ShutdownHookBehavior shutdownHookBehavior = ShutdownHookBehavior.DEFAULT;
   private boolean useLazyDeserialization = false;
   private int objectInputStreamPoolSize = 50;
   private int objectOutputStreamPoolSize = 50;
   private List<CustomInterceptorConfig> customInterceptors = Collections.emptyList();
   private boolean writeSkewCheck = false;
   private int concurrencyLevel = 500;
   private int listenerAsyncPoolSize = 1;
   private int listenerAsyncQueueSize = 50000;
   private int serializationExecutorPoolSize = 0;
   private int serializationExecutorQueueSize = 50000;
   private Marshaller marshaller;
   private boolean invocationBatchingEnabled;
   private boolean useLockStriping = true;
   private URL jgroupsConfigFile;

   @Start(priority = 1)
   void correctIsolationLevels()
   {
      // ensure the correct isolation level upgrades and/or downgrades are performed.
      if (nodeLockingScheme == NodeLockingScheme.MVCC)
      {
         switch (isolationLevel)
         {
            case NONE:
            case READ_UNCOMMITTED:
               isolationLevel = IsolationLevel.READ_COMMITTED;
               break;
            case SERIALIZABLE:
               isolationLevel = IsolationLevel.REPEATABLE_READ;
               break;
         }
      }
   }

   // ------------------------------------------------------------------------------------------------------------
   //   SETTERS - MAKE SURE ALL SETTERS PERFORM testImmutability()!!!
   // ------------------------------------------------------------------------------------------------------------

   public void setCacheMarshaller(Marshaller instance)
   {
      marshaller = instance;
   }

   public Marshaller getMarshaller()
   {
      return marshaller;
   }

   public boolean isWriteSkewCheck()
   {
      return writeSkewCheck;
   }

   public boolean isUseLockStriping()
   {
      return useLockStriping;
   }

   public void setWriteSkewCheck(boolean writeSkewCheck)
   {
      testImmutability("writeSkewCheck");
      this.writeSkewCheck = writeSkewCheck;
   }

   public void setUseLockStriping(boolean useLockStriping)
   {
      testImmutability("useLockStriping");
      this.useLockStriping = useLockStriping;
   }

   public int getConcurrencyLevel()
   {
      return concurrencyLevel;
   }

   public void setConcurrencyLevel(int concurrencyLevel)
   {
      testImmutability("concurrencyLevel");
      this.concurrencyLevel = concurrencyLevel;
   }

   /**
    * Converts a list of elements to a Java Groups property string.
    */
   public void setClusterConfig(Element config)
   {
      setClusterConfig(jGroupsStackParser.parseClusterConfigXml(config));
   }

   public void setClusterName(String clusterName)
   {
      testImmutability("clusterName");
      this.clusterName = clusterName;
   }

   public void setClusterConfig(String clusterConfig)
   {
      testImmutability("clusterConfig");
      this.clusterConfig = clusterConfig;
   }

   public void setReplQueueMaxElements(int replQueueMaxElements)
   {
      testImmutability("replQueueMaxElements");
      this.replQueueMaxElements = replQueueMaxElements;
   }

   public void setReplQueueInterval(long replQueueInterval)
   {
      testImmutability("replQueueInterval");
      this.replQueueInterval = replQueueInterval;
   }

   public void setExposeManagementStatistics(boolean useMbean)
   {
      testImmutability("exposeManagementStatistics");
      this.exposeManagementStatistics = useMbean;
   }

   /**
    * Enables invocation batching if set to <tt>true</tt>.  You still need to use {@link org.jboss.cache.Cache#startBatch()}
    * and {@link org.jboss.cache.Cache#endBatch(boolean)} to demarcate the start and end of batches.
    *
    * @param enabled if true, batching is enabled.
    * @since 3.0
    */
   public void setInvocationBatchingEnabled(boolean enabled)
   {
      testImmutability("invocationBatchingEnabled");
      this.invocationBatchingEnabled = enabled;
   }

   public void setFetchInMemoryState(boolean fetchInMemoryState)
   {
      testImmutability("fetchInMemoryState");
      this.fetchInMemoryState = fetchInMemoryState;
   }

   public void setReplicationVersion(short replicationVersion)
   {
      testImmutability("replicationVersion");
      this.replicationVersion = replicationVersion;
   }

   public void setReplVersionString(String replVersionString)
   {
      setReplicationVersion(replVersionString == null ? 0 : Version.getVersionShort(replVersionString));
   }

   public void setLockAcquisitionTimeout(long lockAcquisitionTimeout)
   {
      testImmutability("lockAcquisitionTimeout");
      this.lockAcquisitionTimeout = lockAcquisitionTimeout;
   }

   public void setSyncReplTimeout(long syncReplTimeout)
   {
      testImmutability("syncReplTimeout");
      this.syncReplTimeout = syncReplTimeout;
   }

   public void setCacheMode(CacheMode cacheModeInt)
   {
      testImmutability("cacheMode");
      this.cacheMode = cacheModeInt;
   }

   public void setCacheMode(String cacheMode)
   {
      testImmutability("cacheMode");
      if (cacheMode == null) throw new ConfigurationException("Cache mode cannot be null", "CacheMode");
      this.cacheMode = CacheMode.valueOf(uc(cacheMode));
      if (this.cacheMode == null)
      {
         log.warn("Unknown cache mode '" + cacheMode + "', using defaults.");
         this.cacheMode = CacheMode.LOCAL;
      }
   }

   public String getCacheModeString()
   {
      return cacheMode == null ? null : cacheMode.toString();
   }

   public void setCacheModeString(String cacheMode)
   {
      setCacheMode(cacheMode);
   }

   public void setInactiveOnStartup(boolean inactiveOnStartup)
   {
      testImmutability("inactiveOnStartup");
      this.inactiveOnStartup = inactiveOnStartup;
   }

   public EvictionConfig getEvictionConfig()
   {
      return evictionConfig;
   }

   public void setEvictionConfig(EvictionConfig config)
   {
      testImmutability("evictionConfig");
      this.evictionConfig = config;
   }

   /**
    * This is a deprecated configuration option.  While it will be supported for the 2.x series for backward compatibility,
    * expect to see it disappear in 3.x.
    * <p/>
    * With {@link #isUseLazyDeserialization()}, which is enabled by default, custom class loaders are handled implicitly.
    * See the user guide for details on how this is handled.
    * <p/>
    */
   public void setUseRegionBasedMarshalling(boolean useRegionBasedMarshalling)
   {
      testImmutability("useRegionBasedMarshalling");
      this.useRegionBasedMarshalling = useRegionBasedMarshalling;
   }

   public void setTransactionManagerLookupClass(String transactionManagerLookupClass)
   {
      testImmutability("transactionManagerLookupClass");
      this.transactionManagerLookupClass = transactionManagerLookupClass;
   }

   public void setCacheLoaderConfig(CacheLoaderConfig config)
   {
      testImmutability("cacheLoaderConfig");
      replaceChildConfig(this.cacheLoaderConfig, config);
      this.cacheLoaderConfig = config;
   }

   public void setSyncCommitPhase(boolean syncCommitPhase)
   {
      testImmutability("syncCommitPhase");
      this.syncCommitPhase = syncCommitPhase;
   }

   public void setSyncRollbackPhase(boolean syncRollbackPhase)
   {
      testImmutability("syncRollbackPhase");
      this.syncRollbackPhase = syncRollbackPhase;
   }

   /**
    * Sets the size of the asynchronous listener notification thread pool size.  Defaults to 1, and if set to below 1,
    * all async listeners (specified with {@link org.jboss.cache.notifications.annotation.CacheListener#sync()} are notified
    * synchronously.
    *
    * @param listenerAsyncPoolSize number of threads in pool
    * @since 3.0
    */
   public void setListenerAsyncPoolSize(int listenerAsyncPoolSize)
   {
      testImmutability("listenerAsyncPoolSize");
      this.listenerAsyncPoolSize = listenerAsyncPoolSize;
   }

   /**
    * Sets the queue size of the bounded queue used to store async listener events on.  This defaults to 50,000.
    *
    * @param listenerAsyncQueueSize queue size to use
    */
   public void setListenerAsyncQueueSize(int listenerAsyncQueueSize)
   {
      testImmutability("listenerAsyncQueueSize");
      this.listenerAsyncQueueSize = listenerAsyncQueueSize;
   }

   /**
    * Sets the queue size of the bounded queue used to store async serialization events on.  This defaults to 50,000.
    *
    * @param serializationExecutorQueueSize queue size to use
    */
   public void setSerializationExecutorQueueSize(int serializationExecutorQueueSize)
   {
      testImmutability("serializationExecutorQueueSize");
      this.serializationExecutorQueueSize = serializationExecutorQueueSize;
   }

   public void setBuddyReplicationConfig(BuddyReplicationConfig config)
   {
      testImmutability("buddyReplicationConfig");
      replaceChildConfig(this.buddyReplicationConfig, config);
      this.buddyReplicationConfig = config;
   }

   /**
    * @deprecated will default to MVCC once optimistic and pessimistic schemes are removed.
    * @param nodeLockingScheme
    */
   @Deprecated
   public void setNodeLockingScheme(NodeLockingScheme nodeLockingScheme)
   {
      testImmutability("nodeLockingScheme");
      testImmutability("nodeLockingOptimistic");
      this.nodeLockingScheme = nodeLockingScheme;
   }

   public void setUseReplQueue(boolean useReplQueue)
   {
      testImmutability("useReplQueue");
      this.useReplQueue = useReplQueue;
   }

   public void setIsolationLevel(IsolationLevel isolationLevel)
   {
      testImmutability("isolationLevel");
      this.isolationLevel = isolationLevel;
   }

   /**
    * Starting with 3.x there are 3 locking schemes, so if true is passed in then state is not defined.
    * It is here for backward compatibility reasons only and should not be used by new code.
    */
   @Deprecated
   public void setNodeLockingOptimistic(boolean nodeLockingOptimistic)
   {
      testImmutability("nodeLockingOptimistic");
      if (nodeLockingOptimistic) setNodeLockingScheme(NodeLockingScheme.OPTIMISTIC);
      else setNodeLockingScheme(NodeLockingScheme.PESSIMISTIC);
   }

   public void setStateRetrievalTimeout(long stateRetrievalTimeout)
   {
      testImmutability("stateRetrievalTimeout");
      this.stateRetrievalTimeout = stateRetrievalTimeout;
   }

   public void setNodeLockingScheme(String nodeLockingScheme)
   {
      testImmutability("nodeLockingScheme");
      if (nodeLockingScheme == null)
      {
         throw new ConfigurationException("Node locking scheme cannot be null", "NodeLockingScheme");
      }
      this.nodeLockingScheme = NodeLockingScheme.valueOf(uc(nodeLockingScheme));
      if (this.nodeLockingScheme == null)
      {
         log.warn("Unknown node locking scheme '" + nodeLockingScheme + "', using defaults.");
         this.nodeLockingScheme = NodeLockingScheme.PESSIMISTIC;
      }
   }

   @Deprecated
   public String getNodeLockingSchemeString()
   {
      return nodeLockingScheme == null ? null : nodeLockingScheme.toString();
   }

   public void setNodeLockingSchemeString(String nodeLockingScheme)
   {
      setNodeLockingScheme(nodeLockingScheme);
   }

   private static String uc(String s)
   {
      return s.toUpperCase(Locale.ENGLISH);
   }

   public void setIsolationLevel(String isolationLevel)
   {
      testImmutability("isolationLevel");
      if (isolationLevel == null) throw new ConfigurationException("Isolation level cannot be null", "IsolationLevel");
      this.isolationLevel = IsolationLevel.valueOf(uc(isolationLevel));
      if (this.isolationLevel == null)
      {
         log.warn("Unknown isolation level '" + isolationLevel + "', using defaults.");
         this.isolationLevel = IsolationLevel.REPEATABLE_READ;
      }
   }

   public String getIsolationLevelString()
   {
      return isolationLevel == null ? null : isolationLevel.toString();
   }

   public void setIsolationLevelString(String isolationLevel)
   {
      setIsolationLevel(isolationLevel);
   }

   /**
    * Sets whether inserting or removing a node requires a write lock
    * on the node's parent (when pessimistic locking is used.)
    * <p/>
    * The default value is <code>false</code>
    */
   public void setLockParentForChildInsertRemove(boolean lockParentForChildInsertRemove)
   {
      testImmutability("lockParentForChildInsertRemove");
      this.lockParentForChildInsertRemove = lockParentForChildInsertRemove;
   }

   public void setMultiplexerStack(String stackName)
   {
      testImmutability("muxStackName");
      this.muxStackName = stackName;
   }

   public boolean isUsingMultiplexer()
   {
      return usingMultiplexer;
   }

   public void setUsingMultiplexer(boolean usingMultiplexer)
   {
      testImmutability("usingMultiplexer");
      this.usingMultiplexer = usingMultiplexer;
   }

   public void setShutdownHookBehavior(ShutdownHookBehavior shutdownHookBehavior)
   {
      testImmutability("shutdownHookBehavior");
      this.shutdownHookBehavior = shutdownHookBehavior;
   }

   public void setShutdownHookBehavior(String shutdownHookBehavior)
   {
      testImmutability("shutdownHookBehavior");
      if (shutdownHookBehavior == null)
         throw new ConfigurationException("Shutdown hook behavior cannot be null", "ShutdownHookBehavior");
      this.shutdownHookBehavior = ShutdownHookBehavior.valueOf(uc(shutdownHookBehavior));
      if (this.shutdownHookBehavior == null)
      {
         log.warn("Unknown shutdown hook behavior '" + shutdownHookBehavior + "', using defaults.");
         this.shutdownHookBehavior = ShutdownHookBehavior.DEFAULT;
      }
   }

   public void setUseLazyDeserialization(boolean useLazyDeserialization)
   {
      testImmutability("useLazyDeserialization");
      this.useLazyDeserialization = useLazyDeserialization;
   }

   /**
    * Initialises the size of the object input stream pool size, which defaults to 50.
    *
    * @param objectInputStreamPoolSize
    * @since 2.1.0
    */
   public void setObjectInputStreamPoolSize(int objectInputStreamPoolSize)
   {
      testImmutability("objectInputStreamPoolSize");
      this.objectInputStreamPoolSize = objectInputStreamPoolSize;
   }

   /**
    * Initialises the size of the object output stream pool size, which defaults to 50.
    *
    * @param objectOutputStreamPoolSize
    * @since 2.1.0
    */
   public void setObjectOutputStreamPoolSize(int objectOutputStreamPoolSize)
   {
      testImmutability("objectOutputStreamPoolSize");
      this.objectOutputStreamPoolSize = objectOutputStreamPoolSize;
   }

   /**
    * Sets the async replication serialization executor pool size for async replication.  Has no effect if the
    * replication queue is used.
    *
    * @param serializationExecutorPoolSize number of threads to use
    */
   public void setSerializationExecutorPoolSize(int serializationExecutorPoolSize)
   {
      testImmutability("serializationExecutorPoolSize");
      this.serializationExecutorPoolSize = serializationExecutorPoolSize;
   }

   // ------------------------------------------------------------------------------------------------------------
   //   GETTERS
   // ------------------------------------------------------------------------------------------------------------


   public ShutdownHookBehavior getShutdownHookBehavior()
   {
      return this.shutdownHookBehavior;
   }

   /**
    * This helper method is deprecated and will be removed when optimistic and pessimistic locking support is dropped.
    *
    * @return true if node locking scheme is optimistic.
    * @deprecated use {@link #getNodeLockingScheme()} to determine node locking scheme used.
    */
   @Deprecated
   public boolean isNodeLockingOptimistic()
   {
      return nodeLockingScheme == NodeLockingScheme.OPTIMISTIC;
   }

   public boolean isUseReplQueue()
   {
      return useReplQueue;
   }

   public String getClusterName()
   {
      return clusterName;
   }

   public String getClusterConfig()
   {
      return clusterConfig;
   }

   public int getReplQueueMaxElements()
   {
      return replQueueMaxElements;
   }

   public long getReplQueueInterval()
   {
      return replQueueInterval;
   }

   /**
    * @deprecated use isExposeManagementStatistics()
    */
   @Deprecated
   public boolean getExposeManagementStatistics()
   {
      return exposeManagementStatistics;
   }

   public boolean isExposeManagementStatistics()
   {
      return exposeManagementStatistics;
   }

   /**
    * @return true if invocation batching is enabled.
    * @since 3.0
    */
   public boolean isInvocationBatchingEnabled()
   {
      return invocationBatchingEnabled;
   }

   public boolean isFetchInMemoryState()
   {
      return fetchInMemoryState;
   }

   public short getReplicationVersion()
   {
      return replicationVersion;
   }

   public String getReplVersionString()
   {
      return Version.getVersionString(replicationVersion);
   }

   public long getLockAcquisitionTimeout()
   {
      return lockAcquisitionTimeout;
   }

   public long getSyncReplTimeout()
   {
      return syncReplTimeout;
   }

   public CacheMode getCacheMode()
   {
      return cacheMode;
   }

   public boolean isInactiveOnStartup()
   {
      return inactiveOnStartup;
   }

   public IsolationLevel getIsolationLevel()
   {
      return isolationLevel;
   }

   /**
    * Gets whether inserting or removing a node requires a write lock
    * on the node's parent (when pessimistic locking is used.)
    * <p/>
    * The default value is <code>false</code>
    */
   public boolean isLockParentForChildInsertRemove()
   {
      return lockParentForChildInsertRemove;
   }

   public boolean isUseRegionBasedMarshalling()
   {
      return useRegionBasedMarshalling;
   }

   public String getTransactionManagerLookupClass()
   {
      return transactionManagerLookupClass;
   }

   public CacheLoaderConfig getCacheLoaderConfig()
   {
      return cacheLoaderConfig;
   }

   public boolean isSyncCommitPhase()
   {
      return syncCommitPhase;
   }

   public boolean isSyncRollbackPhase()
   {
      return syncRollbackPhase;
   }

   /**
    * Gets the size of the asynchronous listener notification thread pool size.  Defaults to 1, and if set to below 1,
    * all async listeners (specified with {@link org.jboss.cache.notifications.annotation.CacheListener#sync()} are notified
    * synchronously.
    *
    * @return thread pool size
    * @since 3.0
    */
   public int getListenerAsyncPoolSize()
   {
      return listenerAsyncPoolSize;
   }

   public BuddyReplicationConfig getBuddyReplicationConfig()
   {
      return buddyReplicationConfig;
   }

   /**
    * @deprecated will be removed once optimistic and pessimistic locking is removed.
    * @return node locking scheme in use
    */
   @Deprecated
   public NodeLockingScheme getNodeLockingScheme()
   {
      return nodeLockingScheme;
   }

   public long getStateRetrievalTimeout()
   {
      return stateRetrievalTimeout;
   }

   public String getMultiplexerStack()
   {
      return muxStackName;
   }

   public boolean isUseLazyDeserialization()
   {
      return useLazyDeserialization;
   }

   public synchronized RuntimeConfig getRuntimeConfig()
   {
      if (runtimeConfig == null)
      {
         setRuntimeConfig(new RuntimeConfig(), false);
      }
      return runtimeConfig;
   }

   public void setRuntimeConfig(RuntimeConfig runtimeConfig)
   {
      setRuntimeConfig(runtimeConfig, true);
   }

   private void setRuntimeConfig(RuntimeConfig runtimeConfig, boolean testImmutability)
   {
      if (testImmutability)
      {
         testImmutability("runtimeConfig");
      }
      this.runtimeConfig = runtimeConfig;
   }

   public String getMarshallerClass()
   {
      return marshallerClass;
   }

   public void setMarshallerClass(String marshallerClass)
   {
      this.marshallerClass = marshallerClass;
   }

   /**
    * @return the size of he object input stream pool
    * @since 2.1.0
    */
   public int getObjectInputStreamPoolSize()
   {
      return objectInputStreamPoolSize;
   }

   /**
    * @return the size of he object output stream pool
    * @since 2.1.0
    */
   public int getObjectOutputStreamPoolSize()
   {
      return objectOutputStreamPoolSize;
   }


   /**
    * Returns a {@link java.net.URL} to a default JGroups configuration file.
    *
    * @return a default JGroups config file
    */
   public URL getDefaultClusterConfig()
   {
      URL url = getClass().getClassLoader().getResource("flush-udp.xml");
      if (log.isTraceEnabled()) log.trace("Using default JGroups configuration file " + url);
      return url;
   }

   public URL getJGroupsConfigFile()
   {
      return jgroupsConfigFile;
   }

   public void setJgroupsConfigFile(URL jgroupsConfigFile)
   {
      this.jgroupsConfigFile = jgroupsConfigFile;
   }

   /**
    * @return the serialization executor pool size.
    */
   public int getSerializationExecutorPoolSize()
   {
      return serializationExecutorPoolSize;
   }

   /**
    *
    * @return the bounded queue size for async listeners
    */
   public int getListenerAsyncQueueSize()
   {
      return listenerAsyncQueueSize;
   }

   /**
    *
    * @return the bounded queue size for async serializers
    */
   public int getSerializationExecutorQueueSize()
   {
      return serializationExecutorQueueSize;
   }

   // ------------------------------------------------------------------------------------------------------------
   //   HELPERS
   // ------------------------------------------------------------------------------------------------------------

   // ------------------------------------------------------------------------------------------------------------
   //   OVERRIDDEN METHODS
   // ------------------------------------------------------------------------------------------------------------

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Configuration that = (Configuration) o;

      if (exposeManagementStatistics != that.exposeManagementStatistics) return false;
      if (fetchInMemoryState != that.fetchInMemoryState) return false;
      if (inactiveOnStartup != that.inactiveOnStartup) return false;
      if (lockAcquisitionTimeout != that.lockAcquisitionTimeout) return false;
      if (lockParentForChildInsertRemove != that.lockParentForChildInsertRemove) return false;
      if (objectInputStreamPoolSize != that.objectInputStreamPoolSize) return false;
      if (objectOutputStreamPoolSize != that.objectOutputStreamPoolSize) return false;
      if (replQueueInterval != that.replQueueInterval) return false;
      if (replQueueMaxElements != that.replQueueMaxElements) return false;
      if (replicationVersion != that.replicationVersion) return false;
      if (stateRetrievalTimeout != that.stateRetrievalTimeout) return false;
      if (syncCommitPhase != that.syncCommitPhase) return false;
      if (syncReplTimeout != that.syncReplTimeout) return false;
      if (syncRollbackPhase != that.syncRollbackPhase) return false;
      if (useLazyDeserialization != that.useLazyDeserialization) return false;
      if (useRegionBasedMarshalling != that.useRegionBasedMarshalling) return false;
      if (useReplQueue != that.useReplQueue) return false;
      if (usingMultiplexer != that.usingMultiplexer) return false;
      if (buddyReplicationConfig != null ? !buddyReplicationConfig.equals(that.buddyReplicationConfig) : that.buddyReplicationConfig != null)
         return false;
      if (cacheLoaderConfig != null ? !cacheLoaderConfig.equals(that.cacheLoaderConfig) : that.cacheLoaderConfig != null)
         return false;
      if (cacheMode != that.cacheMode) return false;
      if (clusterConfig != null ? !clusterConfig.equals(that.clusterConfig) : that.clusterConfig != null) return false;
      if (clusterName != null ? !clusterName.equals(that.clusterName) : that.clusterName != null) return false;
      if (evictionConfig != null ? !evictionConfig.equals(that.evictionConfig) : that.evictionConfig != null)
         return false;
      if (isolationLevel != that.isolationLevel) return false;
      if (marshaller != null ? !marshaller.equals(that.marshaller) : that.marshaller != null) return false;
      if (marshallerClass != null ? !marshallerClass.equals(that.marshallerClass) : that.marshallerClass != null)
         return false;
      if (muxStackName != null ? !muxStackName.equals(that.muxStackName) : that.muxStackName != null) return false;
      if (nodeLockingScheme != that.nodeLockingScheme) return false;
      if (runtimeConfig != null ? !runtimeConfig.equals(that.runtimeConfig) : that.runtimeConfig != null) return false;
      if (shutdownHookBehavior != that.shutdownHookBehavior) return false;
      if (transactionManagerLookupClass != null ? !transactionManagerLookupClass.equals(that.transactionManagerLookupClass) : that.transactionManagerLookupClass != null)
         return false;
      if (listenerAsyncPoolSize != that.listenerAsyncPoolSize) return false;
      if (serializationExecutorPoolSize != that.serializationExecutorPoolSize) return false;
      if (jgroupsConfigFile != that.jgroupsConfigFile) return false;
      if (listenerAsyncQueueSize != that.listenerAsyncQueueSize) return false;
      if (serializationExecutorQueueSize != that.serializationExecutorQueueSize) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (marshaller != null ? marshaller.hashCode() : 0);
      result = 31 * result + (clusterName != null ? clusterName.hashCode() : 0);
      result = 31 * result + (clusterConfig != null ? clusterConfig.hashCode() : 0);
      result = 31 * result + (useReplQueue ? 1 : 0);
      result = 31 * result + replQueueMaxElements;
      result = 31 * result + (int) (replQueueInterval ^ (replQueueInterval >>> 32));
      result = 31 * result + (exposeManagementStatistics ? 1 : 0);
      result = 31 * result + (fetchInMemoryState ? 1 : 0);
      result = 31 * result + (int) replicationVersion;
      result = 31 * result + (int) (lockAcquisitionTimeout ^ (lockAcquisitionTimeout >>> 32));
      result = 31 * result + (int) (syncReplTimeout ^ (syncReplTimeout >>> 32));
      result = 31 * result + (cacheMode != null ? cacheMode.hashCode() : 0);
      result = 31 * result + (inactiveOnStartup ? 1 : 0);
      result = 31 * result + (int) (stateRetrievalTimeout ^ (stateRetrievalTimeout >>> 32));
      result = 31 * result + (isolationLevel != null ? isolationLevel.hashCode() : 0);
      result = 31 * result + (lockParentForChildInsertRemove ? 1 : 0);
      result = 31 * result + (evictionConfig != null ? evictionConfig.hashCode() : 0);
      result = 31 * result + (useRegionBasedMarshalling ? 1 : 0);
      result = 31 * result + (transactionManagerLookupClass != null ? transactionManagerLookupClass.hashCode() : 0);
      result = 31 * result + (cacheLoaderConfig != null ? cacheLoaderConfig.hashCode() : 0);
      result = 31 * result + (syncCommitPhase ? 1 : 0);
      result = 31 * result + (syncRollbackPhase ? 1 : 0);
      result = 31 * result + (buddyReplicationConfig != null ? buddyReplicationConfig.hashCode() : 0);
      result = 31 * result + (nodeLockingScheme != null ? nodeLockingScheme.hashCode() : 0);
      result = 31 * result + (muxStackName != null ? muxStackName.hashCode() : 0);
      result = 31 * result + (usingMultiplexer ? 1 : 0);
      result = 31 * result + (runtimeConfig != null ? runtimeConfig.hashCode() : 0);
      result = 31 * result + (marshallerClass != null ? marshallerClass.hashCode() : 0);
      result = 31 * result + (shutdownHookBehavior != null ? shutdownHookBehavior.hashCode() : 0);
      result = 31 * result + (useLazyDeserialization ? 1 : 0);
      result = 31 * result + objectInputStreamPoolSize;
      result = 31 * result + objectOutputStreamPoolSize;
      result = 31 * result + serializationExecutorPoolSize;
      result = 31 * result + listenerAsyncPoolSize;
      result = 31 * result + serializationExecutorQueueSize;
      result = 31 * result + listenerAsyncQueueSize;
      result = 31 * result + (jgroupsConfigFile != null ? jgroupsConfigFile.hashCode() : 0);
      return result;
   }

   @Override
   public Configuration clone() throws CloneNotSupportedException
   {
      Configuration c = (Configuration) super.clone();
      if (buddyReplicationConfig != null)
      {
         c.setBuddyReplicationConfig(buddyReplicationConfig.clone());
      }
      if (evictionConfig != null)
      {
         c.setEvictionConfig(evictionConfig.clone());
      }
      if (cacheLoaderConfig != null)
      {
         c.setCacheLoaderConfig(cacheLoaderConfig.clone());
      }
      if (runtimeConfig != null)
      {
         c.setRuntimeConfig(runtimeConfig.clone());
         // always make sure we reset the runtime when cloning.
         c.getRuntimeConfig().reset();
      }
      return c;
   }

   public boolean isUsingCacheLoaders()
   {
      return getCacheLoaderConfig() != null && !getCacheLoaderConfig().getIndividualCacheLoaderConfigs().isEmpty();
   }

   public boolean isUsingBuddyReplication()
   {
      return getBuddyReplicationConfig() != null && getBuddyReplicationConfig().isEnabled() &&
            getCacheMode() != Configuration.CacheMode.LOCAL;
   }

   public String getMuxStackName()
   {
      return muxStackName;
   }

   public void setMuxStackName(String muxStackName)
   {
      this.muxStackName = muxStackName;
   }

   /**
    * Returns the {@link org.jboss.cache.config.CustomInterceptorConfig}, if any, associated with this configuration
    * object. The custom interceptors will be added to the cache at startup in the sequence defined by this list.
    *
    * @return List of cutom interceptors, never null
    */
   @SuppressWarnings("unchecked")
   public List<CustomInterceptorConfig> getCustomInterceptors()
   {
      return customInterceptors == null ? Collections.EMPTY_LIST : customInterceptors;
   }

   /**
    * @see #getCustomInterceptors()
    */
   public void setCustomInterceptors(List<CustomInterceptorConfig> customInterceptors)
   {
      testImmutability("customInterceptors");
      this.customInterceptors = customInterceptors;
   }

   public BuddyManager getConsistentHashing()
   {
      return null;
   }

   public boolean isNonBlockingStateTransfer()
   {
      return nonBlockingStateTransfer;
   }

   public void setNonBlockingStateTransfer(boolean nonBlockingStateTransfer)
   {
      testImmutability("nonBlockingStateTransfer");
      this.nonBlockingStateTransfer = nonBlockingStateTransfer;
   }

}
