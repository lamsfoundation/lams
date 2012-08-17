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
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.Modification;
import org.jboss.cache.config.CacheLoaderConfig.IndividualCacheLoaderConfig;
import org.jboss.cache.util.Immutables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The AsyncCacheLoader is a delegating cache loader that extends
 * AbstractDelegatingCacheLoader overriding methods to that should not
 * just delegate the operation to the underlying cache loader.
 * <p/>
 * Read operations are done synchronously, while write (CRUD - Create, Remove,
 * Update, Delete) operations are done asynchronously.  There is no provision
 * for exception handling at the moment for problems encountered with the
 * underlying CacheLoader during a CRUD operation, and the exception is just
 * logged.
 * <p/>
 * When configuring the CacheLoader, use the following attribute:
 * <p/>
 * <code>
 * &lt;attribute name="CacheLoaderAsynchronous"&gt;true&lt;/attribute&gt;
 * </code>
 * <p/>
 * to define whether cache loader operations are to be asynchronous.  If not
 * specified, a cache loader operation is assumed synchronous.
 * <p/>
 * <p/>
 * The following additional parameters are available:
 * <dl>
 * <dt>cache.async.batchSize</dt>
 * <dd>Number of modifications to commit in one transaction, default is
 * 100. The minimum batch size is 1.</dd>
 * <dt>cache.async.pollWait</dt>
 * <dd>How long to wait before processing an incomplete batch, in
 * milliseconds.  Default is 100.  Set this to 0 to not wait before processing
 * available records.</dd>
 * <dt>cache.async.returnOld</dt>
 * <dd>If <code>true</code>, this loader returns the old values from {@link
 * #put} and {@link #remove} methods.  Otherwise, these methods always return
 * null.  Default is true.  <code>false</code> improves the performance of these
 * operations.</dd>
 * <dt>cache.async.queueSize</dt>
 * <dd>Maximum number of entries to enqueue for asynchronous processing.
 * Lowering this size may help prevent out-of-memory conditions.  It also may
 * help to prevent less records lost in the case of JVM failure.  Default is
 * 10,000 operations.</dd>
 * <dt>cache.async.put</dt>
 * <dd>If set to false, all {@link #put} operations will be processed
 * synchronously, and then only the {@link #remove} operations will be
 * processed asynchronously. This mode may be useful for processing
 * expiration of messages within a separate thread and keeping other
 * operations synchronous for reliability.
 * </dd>
 * <dt>cache.async.threadPoolSize</dt>
 * <dd>The size of the async processor thread pool.  Defaults to <tt>1</tt>.  This
 * property is new in JBoss Cache 3.0.</dd>
 * </dl>
 * For increased performance for many smaller transactions, use higher values
 * for <code>cache.async.batchSize</code> and
 * <code>cache.async.pollWait</code>.  For larger sized records, use a smaller
 * value for <code>cache.async.queueSize</code>.
 *
 * @author Manik Surtani (manik.surtani@jboss.com)
 */
public class AsyncCacheLoader extends AbstractDelegatingCacheLoader
{

   private static final Log log = LogFactory.getLog(AsyncCacheLoader.class);
   private static final boolean trace = log.isTraceEnabled();

   private static AtomicInteger threadId = new AtomicInteger(0);

   /**
    * Default limit on entries to process asynchronously.
    */
   private static final int DEFAULT_QUEUE_SIZE = 10000;

   private AsyncCacheLoaderConfig config;
   private ExecutorService executor;
   private AtomicBoolean stopped = new AtomicBoolean(true);
   private BlockingQueue<Modification> queue = new ArrayBlockingQueue<Modification>(DEFAULT_QUEUE_SIZE);
   private List<Future> processorFutures;

   public AsyncCacheLoader()
   {
      super(null);
   }

   public AsyncCacheLoader(CacheLoader cacheLoader)
   {
      super(cacheLoader);
   }

   @Override
   public void setConfig(IndividualCacheLoaderConfig base)
   {
      if (base instanceof AsyncCacheLoaderConfig)
      {
         config = (AsyncCacheLoaderConfig) base;
      }
      else
      {
         config = new AsyncCacheLoaderConfig(base);
      }

      if (config.getQueueSize() > 0)
      {
         queue = new ArrayBlockingQueue<Modification>(config.getQueueSize());
      }

      super.setConfig(base);
   }

   @Override
   public Map get(Fqn name) throws Exception
   {
      try
      {
         return super.get(name);
      }
      catch (IOException e)
      {
         // FileCacheLoader sometimes does this apparently
         log.trace(e);
         return new HashMap(); // ?
      }
   }

   Object get(Fqn name, Object key) throws Exception
   {
      if (config.getReturnOld())
      {
         try
         {
            Map map = super.get(name);
            if (map != null)
            {
               return map.get(key);
            }
         }
         catch (IOException e)
         {
            // FileCacheLoader sometimes does this apparently
            log.trace(e);
         }
      }
      return null;
   }

   /**
    * TODO this is the same as the AbstractCacheLoader.
    */
   @Override
   public void prepare(Object tx, List<Modification> modifications, boolean one_phase) throws Exception
   {
      if (one_phase)
      {
         put(modifications);
      }
      else
      {
         transactions.put(tx, modifications);
      }
   }

   /**
    * TODO this is the same as the AbstractCacheLoader.
    */
   @Override
   public void commit(Object tx) throws Exception
   {
      List<Modification> modifications = transactions.remove(tx);
      if (modifications == null)
      {
         throw new Exception("transaction " + tx + " not found in transaction table");
      }
      put(modifications);
   }

   /**
    * TODO this is the same as the AbstractCacheLoader.
    */
   @Override
   public void rollback(Object tx)
   {
      transactions.remove(tx);
   }
   
   @Override
   public Object put(Fqn name, Object key, Object value) throws Exception
   {
      if (config.getUseAsyncPut())
      {
         Object oldValue = get(name, key);
         Modification mod = new Modification(Modification.ModificationType.PUT_KEY_VALUE, name, key, value);
         enqueue(mod);
         return oldValue;
      }
      else
      {
         return super.put(name, key, value);
      }
   }

   @Override
   public void put(Fqn name, Map attributes) throws Exception
   {
      if (config.getUseAsyncPut())
      {
         // JBCACHE-769 -- make a defensive copy
         Map attrs = (attributes == null ? null : Immutables.immutableMapCopy(attributes));
         Modification mod = new Modification(Modification.ModificationType.PUT_DATA, name, attrs);
         enqueue(mod);
      }
      else
      {
         super.put(name, attributes); // Let delegate make its own defensive copy
      }
   }

   @Override
   public void put(List<Modification> modifications) throws Exception
   {
      if (config.getUseAsyncPut())
      {
         for (Modification modification : modifications)
         {
            enqueue(modification);
         }
      }
      else
      {
         super.put(modifications);
      }
   }

   @Override
   public Object remove(Fqn name, Object key) throws Exception
   {
      Object oldValue = get(name, key);
      Modification mod = new Modification(Modification.ModificationType.REMOVE_KEY_VALUE, name, key);
      enqueue(mod);
      return oldValue;
   }

   @Override
   public void remove(Fqn name) throws Exception
   {
      Modification mod = new Modification(Modification.ModificationType.REMOVE_NODE, name);
      enqueue(mod);
   }

   @Override
   public void removeData(Fqn name) throws Exception
   {
      Modification mod = new Modification(Modification.ModificationType.REMOVE_DATA, name);
      enqueue(mod);
   }

   @Override
   public void start() throws Exception
   {
      if (log.isInfoEnabled()) log.info("Async cache loader starting: " + this);
      stopped.set(false);
      super.start();
      executor = Executors.newFixedThreadPool(config.getThreadPoolSize(), new ThreadFactory()
      {
         public Thread newThread(Runnable r)
         {
            Thread t = new Thread(r, "AsyncCacheLoader-" + threadId.getAndIncrement());
            t.setDaemon(true);
            return t;
         }
      });
      processorFutures = new ArrayList<Future>(config.getThreadPoolSize());
      for (int i = 0; i < config.getThreadPoolSize(); i++) processorFutures.add(executor.submit(new AsyncProcessor()));
   }

   @Override
   public void stop()
   {
      stopped.set(true);
      if (executor != null)
      {
         for (Future f : processorFutures) f.cancel(true);
         executor.shutdown();
         try
         {
            boolean terminated = executor.isTerminated();
            while (!terminated)
            {
               terminated = executor.awaitTermination(60, TimeUnit.SECONDS);
            }
         }
         catch (InterruptedException e)
         {
            Thread.currentThread().interrupt();
         }
      }
      executor = null;
      super.stop();
   }

   private void enqueue(final Modification mod)
         throws CacheException, InterruptedException
   {
      if (stopped.get())
      {
         throw new CacheException("AsyncCacheLoader stopped; no longer accepting more entries.");
      }
      if (trace) log.trace("Enqueuing modification " + mod);
      queue.put(mod);
   }

   /**
    * Processes (by batch if possible) a queue of {@link Modification}s.
    *
    * @author manik surtani
    */
   private class AsyncProcessor implements Runnable
   {
      // Modifications to invoke as a single put
      private final List<Modification> mods = new ArrayList<Modification>(config.getBatchSize());

      public void run()
      {
         while (!Thread.interrupted())
         {
            try
            {
               run0();
            }
            catch (InterruptedException e)
            {
               break;
            }
         }

         try
         {
            if (trace) log.trace("process remaining batch " + mods.size());
            put(mods);
            if (trace) log.trace("process remaining queued " + queue.size());
            while (!queue.isEmpty())
            {
               run0();
            }
         }
         catch (InterruptedException e)
         {
            log.trace("remaining interrupted");
         }
      }

      private void run0() throws InterruptedException
      {
         log.trace("Checking for modifications");
         int i = queue.drainTo(mods, config.getBatchSize());
         if (i == 0)
         {
            Modification m = queue.take();
            mods.add(m);
         }

         if (trace)
         {
            log.trace("Calling put(List) with " + mods.size() + " modifications");
         }
         put(mods);
         mods.clear();
      }

      private void put(List<Modification> mods)
      {
         try
         {
            AsyncCacheLoader.super.put(mods);
         }
         catch (Exception e)
         {
            if (log.isWarnEnabled()) log.warn("Failed to process async modifications: " + e);
            if (log.isDebugEnabled()) log.debug("Exception: ", e);
         }
      }
   }

   @Override
   public String toString()
   {
      return super.toString() +
            " delegate=[" + super.getCacheLoader() + "]" +
            " stopped=" + stopped +
            " batchSize=" + config.getBatchSize() +
            " returnOld=" + config.getReturnOld() +
            " asyncPut=" + config.getUseAsyncPut() +
            " threadPoolSize=" + config.getThreadPoolSize() +
            " queue.remainingCapacity()=" + queue.remainingCapacity() +
            " queue.peek()=" + queue.peek();
   }

}
