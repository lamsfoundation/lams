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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Cache;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheFactory;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.CacheStatus;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.config.BuddyReplicationConfig;
import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.EvictionConfig;
import org.jboss.cache.config.LegacyConfigurationException;
import org.jboss.cache.config.RuntimeConfig;
import org.jboss.cache.config.parsing.JGroupsStackParser;
import org.jboss.cache.config.parsing.XmlConfigurationParser2x;
import org.jboss.cache.config.parsing.element.BuddyElementParser;
import org.jboss.cache.config.parsing.element.EvictionElementParser;
import org.jboss.cache.config.parsing.element.LoadersElementParser;
import org.jboss.cache.util.CachePrinter;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.ChannelFactory;
import org.jgroups.jmx.JChannelFactoryMBean;
import org.w3c.dom.Element;

import javax.management.*;
import javax.transaction.TransactionManager;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Wrapper class that exposes a {@link CacheJmxWrapperMBean JMX management interface}
 *
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @version $Revision$
 * @deprecated use {@link org.jboss.cache.jmx.JmxRegistrationManager}. This class will not be supported from 3.0 on.
 */
@Deprecated
public class CacheJmxWrapper<K, V>
      extends NotificationBroadcasterSupport
      implements CacheJmxWrapperMBean<K, V>, MBeanRegistration, CacheNotificationBroadcaster
{

   private Log log = LogFactory.getLog(getClass().getName());

   private MBeanServer server;
   private String cacheObjectName;
   private boolean jmxResourceRegistered;
   private CacheSPI<K, V> cache;
   private Configuration config;
   private boolean registerJmxResource = true;
   private final AtomicInteger listenerCount = new AtomicInteger(0);
   private final AtomicLong sequence = new AtomicLong(0);
   private final CacheNotificationListener cacheNotificationListener;
   private CacheStatus cacheStatus;
   private String notificationServiceName;
   private boolean registered;
   private boolean disableStateChangeNotifications;

   // Legacy config support

   private Element buddyReplConfig;
   private Element evictionConfig;
   private Element cacheLoaderConfig;
   private Element clusterConfig;
   private JChannelFactoryMBean multiplexerService;

   private BuddyElementParser buddyElementParser = new BuddyElementParser();
   private LoadersElementParser loadersElementParser = new LoadersElementParser();
   private EvictionElementParser evictionElementParser = new EvictionElementParser();
   private JGroupsStackParser stackParser = new JGroupsStackParser();

   // ----------------------------------------------------------- Constructors

   public CacheJmxWrapper()
   {
      cacheNotificationListener = new CacheNotificationListener(this);
      cacheStatus = CacheStatus.INSTANTIATED;
   }

   public CacheJmxWrapper(Cache<K, V> cache)
   {
      this();
      setCache(cache);
   }

   // --------------------------------------------------- CacheJmxWrapperMBean

   public Cache<K, V> getCache()
   {
      return cache;
   }

   public Configuration getConfiguration()
   {
      Configuration cfg = (cache == null ? config : cache.getConfiguration());
      if (cfg == null)
      {
         cfg = config = new Configuration();
      }
      return cfg;
   }

   public String printConfigurationAsString()
   {
      Configuration cfg = getConfiguration();
      return cfg == null ? "Configuration is null" : cfg.toString();
   }

   public String printConfigurationAsHtmlString()
   {
      Configuration cfg = getConfiguration();
      return cfg == null ? "Configuration is null" : CachePrinter.formatHtml(cfg.toString());
   }

   public String printCacheDetails()
   {
      return cache == null ? "Cache is null" : CachePrinter.printCacheDetails(cache);
   }

   public String printCacheDetailsAsHtml()
   {
      return cache == null ? "Cache is null" : CachePrinter.formatHtml(CachePrinter.printCacheDetails(cache));
   }

   public CacheStatus getCacheStatus()
   {
      return cacheStatus;
   }

   public int getState()
   {
      switch (cacheStatus)
      {
         case INSTANTIATED:
         case CREATING:
            return registered ? REGISTERED : UNREGISTERED;
         case CREATED:
            return CREATED;
         case STARTING:
            return STARTING;
         case STARTED:
            return STARTED;
         case STOPPING:
            return STOPPING;
         case STOPPED:
         case DESTROYING:
            return STOPPED;
         case DESTROYED:
            return registered ? DESTROYED : UNREGISTERED;
         case FAILED:
         default:
            return FAILED;
      }
   }

   public Address getLocalAddress()
   {
      return cache == null ? null : cache.getLocalAddress();
   }

   public List<Address> getMembers()
   {
      return cache == null ? null : cache.getMembers();
   }

   public int getNumberOfNodes()
   {
      return cache == null ? -1 : cache.getNumberOfNodes();
   }

   public int getNumberOfAttributes()
   {
      return cache == null ? -1 : cache.getNumberOfAttributes();
   }

   public String printLockInfo()
   {
      return cache == null ? "Cache is null" : CachePrinter.printCacheLockingInfo(cache);
   }

   public String printLockInfoAsHtml()
   {
      return cache == null ? "Cache is null" : CachePrinter.formatHtml(CachePrinter.printCacheLockingInfo(cache));
   }

   public boolean getRegisterJmxResource()
   {
      return registerJmxResource;
   }

   public void setRegisterJmxResource(boolean register)
   {
      this.registerJmxResource = register;
   }

   // ----------------------------------------------------  LegacyConfiguration

   public Element getBuddyReplicationConfig()
   {
      return buddyReplConfig;
   }

   public Element getCacheLoaderConfig()
   {
      return cacheLoaderConfig;
   }

   public Element getCacheLoaderConfiguration()
   {
      return getCacheLoaderConfig();
   }

   public String getCacheMode()
   {
      return getConfiguration().getCacheModeString();
   }

   public String getClusterName()
   {
      return getConfiguration().getClusterName();
   }

   public String getClusterProperties()
   {
      return getConfiguration().getClusterConfig();
   }

   public Element getClusterConfig()
   {
      return clusterConfig;
   }

   public Element getEvictionPolicyConfig()
   {
      return evictionConfig;
   }

   public boolean getExposeManagementStatistics()
   {
      return getConfiguration().getExposeManagementStatistics();
   }

   public boolean getUseInterceptorMbeans()
   {
      return getExposeManagementStatistics();
   }

   public boolean getFetchInMemoryState()
   {
      return getConfiguration().isFetchInMemoryState();
   }

   public long getStateRetrievalTimeout()
   {
      return getConfiguration().getStateRetrievalTimeout();
   }

   @Deprecated
   public void setInitialStateRetrievalTimeout(long timeout)
   {
      setStateRetrievalTimeout(timeout);
   }

   public String getIsolationLevel()
   {
      return getConfiguration().getIsolationLevelString();
   }

   public long getLockAcquisitionTimeout()
   {
      return getConfiguration().getLockAcquisitionTimeout();
   }

   public JChannelFactoryMBean getMultiplexerService()
   {
      return multiplexerService;
   }

   public String getMultiplexerStack()
   {
      return getConfiguration().getMultiplexerStack();
   }

   public ChannelFactory getMuxChannelFactory()
   {
      return getConfiguration().getRuntimeConfig().getMuxChannelFactory();
   }

   public String getNodeLockingScheme()
   {
      return getConfiguration().getNodeLockingSchemeString();
   }

   public long getReplQueueInterval()
   {
      return getConfiguration().getReplQueueInterval();
   }

   public int getReplQueueMaxElements()
   {
      return getConfiguration().getReplQueueMaxElements();
   }

   public String getReplicationVersion()
   {
      return getConfiguration().getReplVersionString();
   }

   public boolean getSyncCommitPhase()
   {
      return getConfiguration().isSyncCommitPhase();
   }

   public long getSyncReplTimeout()
   {
      return getConfiguration().getSyncReplTimeout();
   }

   public boolean getSyncRollbackPhase()
   {
      return getConfiguration().isSyncRollbackPhase();
   }

   public TransactionManager getTransactionManager()
   {
      return getConfiguration().getRuntimeConfig().getTransactionManager();
   }

   public String getTransactionManagerLookupClass()
   {
      return getConfiguration().getTransactionManagerLookupClass();
   }

   @Deprecated
   @SuppressWarnings("deprecation")
   public boolean getUseRegionBasedMarshalling()
   {
      return getConfiguration().isUseRegionBasedMarshalling();
   }

   public boolean isUseLazyDeserialization()
   {
      return getConfiguration().isUseLazyDeserialization();
   }

   public boolean getUseReplQueue()
   {
      return getConfiguration().isUseReplQueue();
   }

   public boolean isInactiveOnStartup()
   {
      return getConfiguration().isInactiveOnStartup();
   }

   public void setBuddyReplicationConfig(Element config)
   {
      BuddyReplicationConfig brc = null;
      if (config != null)
      {
         try
         {
            brc = buddyElementParser.parseBuddyElement(config);
         }
         catch (LegacyConfigurationException lce)
         {
            brc = XmlConfigurationParser2x.parseBuddyReplicationConfig(config);
         }
      }
      getConfiguration().setBuddyReplicationConfig(brc);
      this.buddyReplConfig = config;
   }

   public void setCacheLoaderConfig(Element cacheLoaderConfig)
   {
      CacheLoaderConfig clc = null;
      if (cacheLoaderConfig != null)
      {
         try
         {
            clc = loadersElementParser.parseLoadersElement(cacheLoaderConfig);
         }
         catch (LegacyConfigurationException lce)
         {
            clc = XmlConfigurationParser2x.parseCacheLoaderConfig(cacheLoaderConfig);
         }
      }
      getConfiguration().setCacheLoaderConfig(clc);
      this.cacheLoaderConfig = cacheLoaderConfig;
   }

   public void setCacheLoaderConfiguration(Element config)
   {
      log.warn("MBean attribute 'CacheLoaderConfiguration' is deprecated; " +
            "use 'CacheLoaderConfig'");
      setCacheLoaderConfig(config);
   }

   public void setCacheMode(String mode) throws Exception
   {
      getConfiguration().setCacheModeString(mode);
   }

   public void setClusterConfig(Element config)
   {
      String props = null;
      if (config != null)
      {
         stackParser.parseClusterConfigXml(config);
      }
      getConfiguration().setClusterConfig(props);
      this.clusterConfig = config;
   }

   @Deprecated
   public long getInitialStateRetrievalTimeout()
   {
      return getStateRetrievalTimeout();
   }

   public void setClusterName(String name)
   {
      getConfiguration().setClusterName(name);
   }

   public void setClusterProperties(String cluster_props)
   {
      getConfiguration().setClusterConfig(cluster_props);
   }

   public void setEvictionPolicyConfig(Element config)
   {
      EvictionConfig ec = null;
      if (config != null)
      {
         try
         {
            ec = evictionElementParser.parseEvictionElement(config);
         }
         catch (LegacyConfigurationException ce)
         {
            ec = XmlConfigurationParser2x.parseEvictionConfig(config);
         }
      }
      getConfiguration().setEvictionConfig(ec);
      this.evictionConfig = config;
   }

   public void setExposeManagementStatistics(boolean expose)
   {
      getConfiguration().setExposeManagementStatistics(expose);
   }

   public void setUseInterceptorMbeans(boolean use)
   {
      log.warn("MBean attribute 'UseInterceptorMbeans' is deprecated; " +
            "use 'ExposeManagementStatistics'");
      setExposeManagementStatistics(use);
   }

   public void setFetchInMemoryState(boolean flag)
   {
      getConfiguration().setFetchInMemoryState(flag);
   }

   public void setInactiveOnStartup(boolean inactiveOnStartup)
   {
      getConfiguration().setInactiveOnStartup(inactiveOnStartup);
   }

   public void setStateRetrievalTimeout(long timeout)
   {
      getConfiguration().setStateRetrievalTimeout(timeout);
   }

   public void setIsolationLevel(String level)
   {
      getConfiguration().setIsolationLevelString(level);
   }

   public void setLockAcquisitionTimeout(long timeout)
   {
      getConfiguration().setLockAcquisitionTimeout(timeout);
   }

   public void setMultiplexerService(JChannelFactoryMBean muxService)
   {
      this.multiplexerService = muxService;
   }

   public void setMultiplexerStack(String stackName)
   {
      getConfiguration().setMultiplexerStack(stackName);
   }

   public void setMuxChannelFactory(ChannelFactory factory)
   {
      getConfiguration().getRuntimeConfig().setMuxChannelFactory(factory);
   }

   public void setNodeLockingScheme(String nodeLockingScheme)
   {
      getConfiguration().setNodeLockingSchemeString(nodeLockingScheme);
   }

   public void setReplQueueInterval(long interval)
   {
      getConfiguration().setReplQueueInterval(interval);
   }

   public void setReplQueueMaxElements(int max_elements)
   {
      getConfiguration().setReplQueueMaxElements(max_elements);
   }

   public void setReplicationVersion(String version)
   {
      getConfiguration().setReplVersionString(version);
   }

   public void setSyncCommitPhase(boolean sync_commit_phase)
   {
      getConfiguration().setSyncCommitPhase(sync_commit_phase);
   }

   public void setSyncReplTimeout(long timeout)
   {
      getConfiguration().setSyncReplTimeout(timeout);
   }

   public void setSyncRollbackPhase(boolean sync_rollback_phase)
   {
      getConfiguration().setSyncRollbackPhase(sync_rollback_phase);
   }

   public void setTransactionManager(TransactionManager manager)
   {
      getConfiguration().getRuntimeConfig().setTransactionManager(manager);
   }

   public void setTransactionManagerLookupClass(String cl) throws Exception
   {
      getConfiguration().setTransactionManagerLookupClass(cl);
   }

   @Deprecated
   @SuppressWarnings("deprecation")
   public void setUseRegionBasedMarshalling(boolean isTrue)
   {
      getConfiguration().setUseRegionBasedMarshalling(isTrue);
   }

   public void setUseReplQueue(boolean flag)
   {
      getConfiguration().setUseReplQueue(flag);
   }

   // --------------------------------------------------------------  Lifecycle

   public void create() throws CacheException
   {
      if (!cacheStatus.createAllowed())
      {
         if (cacheStatus.needToDestroyFailedCache())
         {
            destroy();
         }
         else
         {
            return;
         }
      }

      try
      {
         cacheStatus = CacheStatus.CREATING;

         if (cache == null)
         {
            if (config == null)
            {
               throw new ConfigurationException("Must call setConfiguration() or setCache() before call to create()");
            }

            constructCache();
         }

         cache.create();

         cacheStatus = CacheStatus.CREATED;
      }
      catch (Throwable t)
      {
         handleLifecycleTransitionFailure(t);
      }
   }

   public void start() throws CacheException
   {
      if (!cacheStatus.startAllowed())
      {
         if (cacheStatus.needToDestroyFailedCache())
         {
            destroy(); // this will take us back to DESTROYED
         }

         if (cacheStatus.needCreateBeforeStart())
         {
            create();
         }
         else
         {
            return;
         }
      }

      try
      {
         int oldState = getState();
         cacheStatus = CacheStatus.STARTING;
         int startingState = getState();
         sendStateChangeNotification(oldState, startingState, getClass().getSimpleName() + " starting", null);

         cache.start();

         registerJmxResources();

         cacheStatus = CacheStatus.STARTED;
         sendStateChangeNotification(startingState, getState(), getClass().getSimpleName() + " started", null);
      }
      catch (Throwable t)
      {
         handleLifecycleTransitionFailure(t);
      }
   }

   public void stop()
   {
      if (!cacheStatus.stopAllowed())
      {
         return;
      }

      // Trying to stop() from FAILED is valid, but may not work
      boolean failed = cacheStatus == CacheStatus.FAILED;

      try
      {
         int oldState = getState();
         cacheStatus = CacheStatus.STOPPING;
         int stoppingState = getState();
         sendStateChangeNotification(oldState, stoppingState, getClass().getSimpleName() + " stopping", null);

         cache.stop();

         if (cache.getCacheStatus() == CacheStatus.DESTROYED)
         {
            // Cache was already destroyed externally; 
            // so get rid of the jmx resources
            unregisterJmxResources();
         }

         cacheStatus = CacheStatus.STOPPED;
         sendStateChangeNotification(stoppingState, getState(), getClass().getSimpleName() + " stopped", null);
      }
      catch (Throwable t)
      {
         if (failed)
         {
            log.warn("Attempted to stop() from FAILED state, " +
                  "but caught exception; try calling destroy()", t);
         }
         handleLifecycleTransitionFailure(t);
      }
   }

   public void destroy()
   {
      if (!cacheStatus.destroyAllowed())
      {
         if (cacheStatus.needStopBeforeDestroy())
         {
            try
            {
               stop();
            }
            catch (CacheException e)
            {
               log.warn("Needed to call stop() before destroying but stop() " +
                     "threw exception. Proceeding to destroy", e);
            }
         }
         else
         {
            return;
         }
      }

      try
      {
         cacheStatus = CacheStatus.DESTROYING;

         // The cache is destroyed, so we shouldn't leave the ResourcesDMBean
         // in JMX, even if we didn't register them in create
         unregisterJmxResources();

         if (cache != null)
         {
            cache.destroy();
         }
      }
      finally
      {
         // We always proceed to DESTROYED
         cacheStatus = CacheStatus.DESTROYED;
      }
   }

   // ------------------------------------------------------  MBeanRegistration

   /**
    * Caches the provided <code>server</code> and <code>objName</code>.
    */
   public ObjectName preRegister(MBeanServer server, ObjectName objName)
         throws Exception
   {
      this.server = server;
      if (cacheObjectName == null)
      {
         if (objName != null)
         {
            cacheObjectName = objName.getCanonicalName();
         }
         else
         {
            getCacheObjectName();
         }
      }
      // Inform our CacheNotificationListener of the ObjectName it should transmit
      if (notificationServiceName == null) notificationServiceName = cacheObjectName;
      cacheNotificationListener.setServiceName(notificationServiceName);
      return new ObjectName(cacheObjectName);
   }

   /**
    * Registers the cache's MBean resources, if {@link #getRegisterJmxResource()}
    * is <code>true</code>.
    */
   public void postRegister(Boolean registrationDone)
   {
      if (Boolean.TRUE.equals(registrationDone))
      {
         log.debug("Registered in JMX under " + cacheObjectName);
         if (cache != null && CacheStatus.STARTED.equals(cache.getCacheStatus()))
         {
            try
            {
               registerJmxResources();
            }
            catch (Exception e)
            {
               log.error("Caught exception registering cache ResourcesDMBean with JMX", e);
            }
         }

         registered = true;
      }
   }

   /**
    * No-op.
    */
   public void preDeregister() throws Exception
   {
   }

   /**
    * Unregisters the ResourcesDMBean, if {@link #getRegisterJmxResource()} is
    * <code>true</code>.
    */
   public void postDeregister()
   {
      unregisterJmxResources();

      server = null;
      registered = false;
   }

   // ---------------------------------------------------------  Public methods

   /**
    * Sets the configuration that the underlying cache should use.
    *
    * @param config the configuration
    * @throws IllegalArgumentException if <code>config</code> is <code>null</code>.
    */
   public void setConfiguration(Configuration config)
   {
      this.config = config;
   }

   /**
    * Allows direct injection of the underlying cache.
    *
    * @param cache
    */
   public void setCache(Cache<K, V> cache)
   {
      if (cacheStatus != CacheStatus.INSTANTIATED
            && cacheStatus != CacheStatus.CREATING
            && cacheStatus != CacheStatus.DESTROYED)
      {
         throw new IllegalStateException("Cannot set underlying cache after call to create()");
      }

      this.cache = (CacheSPI<K, V>) cache;
      this.config = (cache == null ? null : cache.getConfiguration());
      synchronized (listenerCount)
      {
         if (listenerCount.get() > 0 && cache != null)
         {
            cache.addCacheListener(cacheNotificationListener);
         }
      }
   }

   public String getCacheObjectName()
   {
      if (cacheObjectName == null)
      {
         if (config.getClusterName() == null)
         {
            cacheObjectName = JmxRegistrationManager.LOCAL_CACHE_PREFIX + "Cache" + System.currentTimeMillis();
         }
         else
         {
            cacheObjectName = JmxRegistrationManager.REPLICATED_CACHE_PREFIX + config.getClusterName();
         }
      }
      return cacheObjectName;
   }

   public void setCacheObjectName(String name) throws MalformedObjectNameException
   {
      if (name != null)
      {
         // test the name
         new ObjectName(name);
      }
      this.cacheObjectName = name;
   }

   /**
    * Gets whether sending of JMX notifications for this mbean's
    * start/stop lifecycle changes is disabled.
    *
    * @see #setDisableStateChangeNotifications(boolean)
    */
   public boolean isDisableStateChangeNotifications()
   {
      return disableStateChangeNotifications;
   }

   /**
    * Hook to allow PojoCacheJmxWrapper to suppress state change
    * notifications from this mbean in lieu of its own.
    *
    * @param disableStateChangeNotifications
    *
    */
   public void setDisableStateChangeNotifications(boolean disableStateChangeNotifications)
   {
      this.disableStateChangeNotifications = disableStateChangeNotifications;
   }

   public MBeanServer getMBeanServer()
   {
      return server;
   }

   public String getNotificationServiceName()
   {
      return notificationServiceName;
   }

   public void setNotificationServiceName(String notificationServiceName)
   {
      this.notificationServiceName = notificationServiceName;
      this.cacheNotificationListener.setServiceName(notificationServiceName);
   }

   public long getNextNotificationSequenceNumber()
   {
      return sequence.getAndIncrement();
   }

   // ----------------------------------------------------  Superclass Overrides

   @Override
   public void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object object) throws IllegalArgumentException
   {
      super.addNotificationListener(notificationListener, notificationFilter, object);
      notificationRegistration(true);
   }

   @Override
   public void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException
   {
      super.removeNotificationListener(notificationListener);
      notificationRegistration(false);
   }

   @Override
   public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws ListenerNotFoundException
   {
      super.removeNotificationListener(listener, filter, handback);
      notificationRegistration(false);
   }

   @Override
   public MBeanNotificationInfo[] getNotificationInfo()
   {
      return CacheNotificationListener.getNotificationInfo();
   }

   // ------------------------------------------------------  Protected Methods

   protected void constructCache() throws ConfigurationException
   {
      log.debug("Constructing Cache");
      CacheFactory<K, V> cf = new DefaultCacheFactory<K, V>();
      setCache(cf.createCache(config, false));
      if (multiplexerService != null)
      {
         injectMuxChannel();
      }
   }

   protected boolean registerJmxResources() throws CacheException
   {
      if (registerJmxResource && config.getExposeManagementStatistics() && !jmxResourceRegistered && server != null)
      {
         log.debug("Registering jmx resources");
         JmxRegistrationManager registrationManager = new JmxRegistrationManager(server, cache, this.cacheObjectName);
         registrationManager.registerAllMBeans();
         jmxResourceRegistered = true;
         return true;
      }
      return false;
   }

   protected void unregisterJmxResources()
   {
      if (registerJmxResource && jmxResourceRegistered && server != null)
      {
         log.debug("Unregistering interceptors");
         JmxRegistrationManager registrationManager = new JmxRegistrationManager(server, cache, this.cacheObjectName);
         registrationManager.unregisterAllMBeans();
         jmxResourceRegistered = false;
      }
   }

   // --------------------------------------------------------  Private methods

   private void injectMuxChannel() throws CacheException
   {
      Configuration cfg = getConfiguration();
      RuntimeConfig rtcfg = cfg.getRuntimeConfig();

      // Only inject if there isn't already a channel or factory
      if (rtcfg.getMuxChannelFactory() == null && rtcfg.getChannel() == null)
      {
         Channel ch;
         try
         {
            ch = multiplexerService.createMultiplexerChannel(cfg.getMultiplexerStack(), cfg.getClusterName());
         }
         catch (Exception e)
         {
            throw new CacheException("Exception creating multiplexed channel", e);
         }
         rtcfg.setChannel(ch);
      }
   }


   /**
    * Adds and removes the CacheListener.
    * A counter is used to determine whether we have any clients who are
    * registered for notifications from this mbean.  When the count is zero,
    * we don't need to listen to cache events, so we remove the CacheListener.
    * Note that a client who terminates without unregistering for notifications
    * will leave the count greater than zero so we'll still listen in that case.
    *
    * @param add <code>true</code> if the event was a listerner addition,
    *            <code>false</code> if it was a removal
    */
   private void notificationRegistration(boolean add)
   {
      // This method adds and removes the CacheImpl listener.
      // The m_listeners counter is used to determine whether
      // we have any clients who are registered for notifications
      // from this mbean.  When the count is zero, we don't need to 
      // listen to cache events.  Note that a client who terminates
      // without unregistering for notifications will leave the count
      // greater than zero so we'll still listen in that case.
      synchronized (listenerCount)
      {
         if (add)
         {
            listenerCount.incrementAndGet();
            if (cache != null)
            {
               cache.addCacheListener(cacheNotificationListener);
            }
         }
         else
         {
            if (listenerCount.decrementAndGet() <= 0)
            {
               if (cache != null)
               {
                  cache.removeCacheListener(cacheNotificationListener);
               }

               listenerCount.set(0);
            }
         }
      }
   }

   /**
    * Sets the cacheStatus to FAILED and rethrows the problem as one
    * of the declared types. Converts any non-RuntimeException Exception
    * to CacheException.
    *
    * @param t
    * @throws CacheException
    * @throws RuntimeException
    * @throws Error
    */
   private void handleLifecycleTransitionFailure(Throwable t)
         throws RuntimeException, Error
   {
      int oldState = getState();
      cacheStatus = CacheStatus.FAILED;
      sendStateChangeNotification(oldState, getState(), getClass().getSimpleName() + " failed", t);

      if (t instanceof CacheException)
      {
         throw (CacheException) t;
      }
      else if (t instanceof RuntimeException)
      {
         throw (RuntimeException) t;
      }
      else if (t instanceof Error)
      {
         throw (Error) t;
      }
      else
      {
         throw new CacheException(t);
      }
   }

   /**
    * Helper for sending out state change notifications
    */
   private void sendStateChangeNotification(int oldState, int newState, String msg, Throwable t)
   {
      if (isDisableStateChangeNotifications())
      {
         return;
      }

      long now = System.currentTimeMillis();

      AttributeChangeNotification stateChangeNotification = new AttributeChangeNotification(
            this,
            getNextNotificationSequenceNumber(), now, msg,
            "State", "java.lang.Integer",
            oldState, newState);

      stateChangeNotification.setUserData(t);

      sendNotification(stateChangeNotification);
   }
}
