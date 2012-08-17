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
package org.jboss.cache.loader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.config.CacheLoaderConfig;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig.SingletonStoreConfig;
import org.jboss.cache.notifications.annotation.CacheListener;
import org.jboss.cache.notifications.annotation.CacheStarted;
import org.jboss.cache.notifications.annotation.CacheStopped;
import org.jboss.cache.notifications.annotation.ViewChanged;
import org.jboss.cache.notifications.event.Event;
import org.jboss.cache.notifications.event.ViewChangedEvent;
import org.jgroups.Address;
import org.jgroups.View;

import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * SingletonStoreCacheLoader is a delegating cache loader used for situations when only one node should interact with
 * the underlying store. The coordinator of the cluster will be responsible for the underlying CacheLoader.
 * SingletonStoreCacheLoader is a simply facade to a real CacheLoader implementation. It always delegates reads to the
 * real CacheLoader.
 * <p/>
 * Writes are forwarded only if this SingletonStoreCacheLoader is currently the cordinator. This avoid having all
 * CacheLoaders in a cluster writing the same data to the same underlying store. Although not incorrect (e.g. a DB
 * will just discard additional INSERTs for the same key, and throw an exception), this will avoid a lot of
 * redundant work.<br/>
 * <p/>
 * Whenever the current coordinator dies (or leaves), the second in line will take over. That SingletonStoreCacheLoader
 * will then pass writes through to its underlying CacheLoader. Optionally, when a new coordinator takes over the
 * Singleton, it can push the in-memory state to the cache cacheLoader, within a time constraint.
 *
 * @author Bela Ban
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
public class SingletonStoreCacheLoader extends AbstractDelegatingCacheLoader
{
   /**
    * Log instance.
    */
   private static final Log log = LogFactory.getLog(SingletonStoreCacheLoader.class);
   private static final boolean trace = log.isTraceEnabled();

   /**
    * Name of thread that should pushing in-memory state to cache loader.
    */
   private static final String THREAD_NAME = "InMemoryToCacheLoaderPusher";

   /**
    * Configuration for the SingletonStoreCacheLoader.
    */
   private SingletonStoreDefaultConfig config;

   /**
    * Executor service used to submit tasks to push in-memory state.
    */
   private final ExecutorService executor;

   /**
    * Future result of the in-memory push state task. This allows SingletonStoreCacheLoader to check whether there's any
    * push taks on going.
    */
   private Future<?> pushStateFuture; /* FutureTask guarantess a safe publication of the result */

   /**
    * Address instance that allows SingletonStoreCacheLoader to find out whether it became the coordinator of the
    * cluster, or whether it stopped being it. This dictates whether the SingletonStoreCacheLoader is active or not.
    */
   private Address localAddress;

   /**
    * Whether the the current node is the coordinator and therefore SingletonStoreCacheLoader is active. Being active
    * means delegating calls to the underlying cache loader.
    */
   private boolean active;

   /**
    * Empty constructor so that it can instantiated using reflection.
    */
   public SingletonStoreCacheLoader()
   {
      super(null);

      executor = Executors.newSingleThreadExecutor(new ThreadFactory()
      {
         public Thread newThread(Runnable r)
         {
            return new Thread(r, THREAD_NAME);
         }
      });
   }

   /**
    * Sets the config for SingletonStoreCacheLoader and for the delegating cache loader.
    */
   @Override
   public void setConfig(CacheLoaderConfig.IndividualCacheLoaderConfig config)
   {
      super.setConfig(config);

      SingletonStoreConfig ssc = config.getSingletonStoreConfig();
      if (ssc instanceof SingletonStoreDefaultConfig)
      {
         this.config = (SingletonStoreDefaultConfig) ssc;
      }
      else if (ssc != null)
      {
         this.config = new SingletonStoreDefaultConfig(ssc);
      }
      else
      {
         this.config = new SingletonStoreDefaultConfig();
      }
   }

   @Override
   public void create() throws Exception
   {
      super.create();

      cache.addCacheListener(new SingletonStoreListener());
   }

   /**
    * Protected constructor which should only be used from unit tests. Production code should set
    * pushStateWhenCoordinator using setConfig() method instead.
    *
    * @param config configuration instance for SingletonStoreCacheLoader
    */
   protected SingletonStoreCacheLoader(SingletonStoreDefaultConfig config)
   {
      this();

      this.config = config;
   }

   /**
    * Returns SingletonStoreCacheLoader's configuration instance. This method has been defined for convenience reasons
    * when unit testing SingletonStoreCacheLoader's configuration.
    *
    * @return instance of SingletonStoreDefaultConfig
    */
   protected SingletonStoreDefaultConfig getSingletonStoreDefaultConfig()
   {
      return config;
   }

   /**
    * Returns the Future instance of a running in-memory to cache loader push task. This method has been defined for
    * convenience reasons when unit testing.
    *
    * @return an instance of Future
    */
   protected Future<?> getPushStateFuture()
   {
      return pushStateFuture;
   }

   /**
    * Method called when the node either becomes the coordinator or stops being the coordinator. If it becomes the
    * coordinator, it can optionally start the in-memory state transfer to the underlying cache store.
    *
    * @param newActiveState true if the node just became the coordinator, false if the nodes stopped being the coordinator.
    */
   protected void activeStatusChanged(boolean newActiveState) throws PushStateException
   {
      active = newActiveState;
      log.debug("changed mode: " + this);
      if (active && config.isPushStateWhenCoordinator())
      {
         doPushState();
      }
   }

   /**
    * Factory method for the creation of a Callable task in charge of pushing in-memory state to cache loader.
    *
    * @return new instance of Callable<?> whose call() method either throws an exception or returns null if the task
    *         was successfull.
    */
   protected Callable<?> createPushStateTask()
   {
      return new Callable()
      {
         public Object call() throws Exception
         {
            final boolean debugEnabled = log.isDebugEnabled();

            if (debugEnabled) log.debug("start pushing in-memory state to cache cacheLoader");
            pushState(cache.getRoot());
            if (debugEnabled) log.debug("in-memory state passed to cache cacheLoader successfully");

            return null;
         }
      };
   }

   /**
    * Pushes the state of a specific node by reading the node's data from the cache and putting in the cache store
    * via the cache loader. This method is call recursively so that it iterates through the whole cache.
    *
    * @param node instance of NodeSPI to push to the cache loader
    * @throws Exception if there's any issues reading the data from the cache or pushing the node's data to the cache
    *                   loader.
    */
   protected void pushState(NodeSPI node) throws Exception
   {
      /* Put the node's data first */
      Set keys = node.getKeysDirect();
      Fqn fqn = node.getFqn();

      for (Object aKey : keys)
      {
         Object value = cache.get(fqn, aKey);
         put(fqn, aKey, value);
      }

      /* Navigates to the children */
      Collection<NodeSPI> children = node.getChildrenDirect();
      for (NodeSPI aChildren : children)
      {
         //Map.Entry entry = (Map.Entry) aChildren;
         pushState(aChildren);
      }
   }

   /**
    * Method that waits for the in-memory to cache loader state to finish. This method's called in case a push state
    * is already in progress and we need to wait for it to finish.
    *
    * @param future  instance of Future representing the on going push task
    * @param timeout time to wait for the push task to finish
    * @param unit    instance of TimeUnit representing the unit of timeout
    */
   protected void awaitForPushToFinish(Future future, int timeout, TimeUnit unit)
   {
      final boolean debugEnabled = log.isDebugEnabled();
      try
      {
         if (debugEnabled) log.debug("wait for state push to cache loader to finish");
         future.get(timeout, unit);
      }
      catch (TimeoutException e)
      {
         if (debugEnabled) log.debug("timed out waiting for state push to cache loader to finish");
      }
      catch (ExecutionException e)
      {
         if (debugEnabled) log.debug("exception reported waiting for state push to cache loader to finish");
      }
      catch (InterruptedException ie)
      {
         /* Re-assert the thread's interrupted status */
         Thread.currentThread().interrupt();
         if (trace) log.trace("wait for state push to cache loader to finish was interrupted");
      }
   }

   /**
    * Called when the SingletonStoreCacheLoader discovers that the node has become the coordinator and push in memory
    * state has been enabled. It might not actually push the state if there's an ongoing push task running, in which
    * case will wait for the push task to finish.
    *
    * @throws PushStateException when the push state task reports an issue.
    */
   private void doPushState() throws PushStateException
   {
      if (pushStateFuture == null || pushStateFuture.isDone())
      {
         Callable<?> task = createPushStateTask();
         pushStateFuture = executor.submit(task);
         try
         {
            waitForTaskToFinish(pushStateFuture, config.getPushStateWhenCoordinatorTimeout(), TimeUnit.MILLISECONDS);
         }
         catch (Exception e)
         {
            throw new PushStateException("unable to complete in memory state push to cache loader", e);
         }
      }
      else
      {
         /* at the most, we wait for push state timeout value. if it push task finishes earlier, this call
         * will stop when the push task finishes, otherwise a timeout exception will be reported */
         awaitForPushToFinish(pushStateFuture, config.getPushStateWhenCoordinatorTimeout(), TimeUnit.MILLISECONDS);
      }
   }

   /**
    * Waits, within a time constraint, for a task to finish.
    *
    * @param future  represents the task waiting to finish.
    * @param timeout maximum time to wait for the time to finish.
    * @param unit    instance of TimeUnit representing the unit of timeout
    * @throws Exception if any issues are reported while waiting for the task to finish
    */
   private void waitForTaskToFinish(Future future, int timeout, TimeUnit unit) throws Exception
   {
      try
      {
         future.get(timeout, unit);
      }
      catch (TimeoutException e)
      {
         throw new Exception("task timed out", e);
      }
      catch (InterruptedException e)
      {
         /* Re-assert the thread's interrupted status */
         Thread.currentThread().interrupt();
         if (trace) log.trace("task was interrupted");
      }
      finally
      {
         /* no-op if task is completed */
         future.cancel(true); /* interrupt if running */
      }
   }

   /**
    * Indicates whether the current nodes is the coordinator of the cluster.
    *
    * @param newView View instance containing the new view of the cluster
    * @return whether the current node is the coordinator or not.
    */
   private boolean isCoordinator(View newView)
   {
      if (newView != null && localAddress != null)
      {
         Vector mbrs = newView.getMembers();
         return mbrs != null && mbrs.size() > 0 && localAddress.equals(mbrs.firstElement());
      }

      /* Invalid new view, so previous value returned */
      return active;
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      if (active)
      {
         return super.put(name, key, value);
      }

      return null;
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public void put(Fqn name, Map attributes) throws Exception
   {
      if (active)
      {
         super.put(name, attributes);
      }
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public void put(List<Modification> modifications) throws Exception
   {
      if (active)
      {
         super.put(modifications);
      }
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public Object remove(Fqn fqn, Object key) throws Exception
   {
      if (active)
      {
         return super.remove(fqn, key);
      }

      return null;
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public void remove(Fqn fqn) throws Exception
   {
      if (active)
      {
         super.remove(fqn);
      }
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public void removeData(Fqn fqn) throws Exception
   {
      if (active)
      {
         super.removeData(fqn);
      }
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public void prepare(Object tx, List<Modification> modifications, boolean one_phase) throws Exception
   {
      if (active)
      {
         super.prepare(tx, modifications, one_phase);
      }
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public void commit(Object tx) throws Exception
   {
      if (active)
      {
         super.commit(tx);
      }
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public void rollback(Object tx)
   {
      if (active)
      {
         super.rollback(tx);
      }
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public void storeEntireState(ObjectInputStream is) throws Exception
   {
      if (active)
      {
         super.storeEntireState(is);
      }
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public void storeState(Fqn subtree, ObjectInputStream is) throws Exception
   {
      if (active)
      {
         super.storeState(subtree, is);
      }
   }

   /**
    * Calls the underlying cache loader's operation if the current node is the coordinator.
    */
   @Override
   public String toString()
   {
      return "loc_addr=" + localAddress + ", active=" + active;
   }

   /**
    * Cache listener that reacts to cluster topology changes to find out whether a new coordinator is elected.
    * SingletonStoreCacheLoader reacts to these changes in order to decide which node should interact with the
    * underlying cache store.
    */
   @CacheListener
   public class SingletonStoreListener
   {
      /**
       * Cache started, check whether the node is the coordinator and set the singleton store cache loader's active
       * status.
       */
      @CacheStarted
      public void cacheStarted(Event e)
      {
         localAddress = cache.getLocalAddress();
         active = cache.getRPCManager().isCoordinator();
         if (log.isDebugEnabled()) log.debug("cache started: " + this);
      }

      @CacheStopped
      public void cacheStopped(Event e)
      {
         if (log.isDebugEnabled()) log.debug("cache stopped: " + this);
      }

      /**
       * The cluster formation changed, so determine whether the current node stopped being the coordinator or became
       * the coordinator. This method can lead to an optional in memory to cache loader state push, if the current node
       * became the coordinator. This method will report any issues that could potentially arise from this push.
       */
      @ViewChanged
      public void viewChange(ViewChangedEvent event)
      {
         boolean tmp = isCoordinator(event.getNewView());

         if (active != tmp)
         {
            try
            {
               activeStatusChanged(tmp);
            }
            catch (PushStateException e)
            {
               log.error("exception reported changing nodes active status", e);
            }

         }
      }
   }

   /**
    * Exception representing any issues that arise from pushing the in-memory state to the cache loader.
    */
   public static class PushStateException extends Exception
   {
      private static final long serialVersionUID = 5542893943730200886L;

      public PushStateException(String message, Throwable cause)
      {
         super(message, cause);
      }

      public PushStateException(Throwable cause)
      {
         super(cause);
      }
   }
}