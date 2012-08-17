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
package org.jboss.cache.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.RPCManager;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.remote.ReplicateCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.factories.annotations.Stop;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Periodically (or when certain size is exceeded) takes elements and replicates them.
 *
 * @author <a href="mailto:bela@jboss.org">Bela Ban</a> May 24, 2003
 * @version $Revision$
 */
public class ReplicationQueue
{

   private static final Log log = LogFactory.getLog(ReplicationQueue.class);

   /**
    * Max elements before we flush
    */
   private long max_elements = 500;

   /**
    * Holds the replication jobs: LinkedList<MethodCall>
    */
   final List<ReplicableCommand> elements = new LinkedList<ReplicableCommand>();

   /**
    * For periodical replication
    */
   private ScheduledExecutorService scheduledExecutor = null;
   private RPCManager rpcManager;
   private Configuration configuration;
   private boolean enabled;
   private CommandsFactory commandsFactory;
   private static final AtomicInteger counter = new AtomicInteger(0);

   public boolean isEnabled()
   {
      return enabled;
   }

   @Inject
   void injectDependencies(RPCManager rpcManager, Configuration configuration, CommandsFactory commandsFactory)
   {
      this.rpcManager = rpcManager;
      this.configuration = configuration;
      this.commandsFactory = commandsFactory;

      // this is checked again in Start
      enabled = configuration.isUseReplQueue() && (configuration.getBuddyReplicationConfig() == null || !configuration.getBuddyReplicationConfig().isEnabled());
   }

   /**
    * Starts the asynchronous flush queue.
    */
   @Start
   public synchronized void start()
   {
      long interval = configuration.getReplQueueInterval();
      this.max_elements = configuration.getReplQueueMaxElements();
      // check again
      enabled = configuration.isUseReplQueue() && (configuration.getBuddyReplicationConfig() == null || !configuration.getBuddyReplicationConfig().isEnabled());
      if (enabled && interval > 0 && scheduledExecutor == null)
      {
         scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
         {
            public Thread newThread(Runnable r)
            {
               return new Thread(r, "ReplicationQueue-periodicProcessor-" + counter.getAndIncrement());
            }
         });
         scheduledExecutor.scheduleWithFixedDelay(new Runnable()
         {
            public void run()
            {
               flush();
            }
         }, 500l, interval, TimeUnit.MILLISECONDS);
      }
   }

   /**
    * Stops the asynchronous flush queue.
    */
   @Stop
   public synchronized void stop()
   {
      if (scheduledExecutor != null)
      {
         scheduledExecutor.shutdownNow();
      }
      scheduledExecutor = null;
   }


   /**
    * Adds a new method call.
    */
   public void add(ReplicateCommand job)
   {
      if (job == null)
         throw new NullPointerException("job is null");
      synchronized (elements)
      {
         elements.add(job);
         if (elements.size() >= max_elements)
            flush();
      }
   }

   /**
    * Flushes existing method calls.
    */
   public void flush()
   {
      List<ReplicableCommand> toReplicate;
      synchronized (elements)
      {
         if (log.isTraceEnabled())
            log.trace("flush(): flushing repl queue (num elements=" + elements.size() + ")");
         toReplicate = new ArrayList<ReplicableCommand>(elements);
         elements.clear();
      }

      if (toReplicate.size() > 0)
      {
         try
         {

            ReplicateCommand replicateCommand = commandsFactory.buildReplicateCommand(toReplicate);
            // send to all live nodes in the cluster
            rpcManager.callRemoteMethods(null, replicateCommand, false, configuration.getSyncReplTimeout(), false);
         }
         catch (Throwable t)
         {
            log.error("failed replicating " + toReplicate.size() + " elements in replication queue", t);
         }
      }
   }
}