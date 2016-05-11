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
package org.jboss.cache.interceptors;

import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.interceptors.base.JmxStatsCommandInterceptor;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jboss.cache.jmx.annotations.ManagedOperation;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.loader.CacheLoaderManager;
import org.jboss.cache.notifications.Notifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Writes evicted nodes back to the store on the way in through the
 * CacheLoader, either before each method call (no TXs), or at TX commit.
 *
 * @author <a href="mailto:{hmesha@novell.com}">{Hany Mesha}</a>
 *
 */
public class PassivationInterceptor extends JmxStatsCommandInterceptor
{

   private final AtomicLong passivations = new AtomicLong(0);

   protected CacheLoader loader;
   private Notifier notifier;

   @Inject
   public void setDependencies(Notifier notifier, CacheLoaderManager loaderManager)
   {
      this.notifier = notifier;
      this.loader = loaderManager.getCacheLoader();
   }

   /**
    * Notifies the cache instance listeners that the evicted node is about to
    * be passivated and stores the evicted node and its attributes back to the
    * store using the CacheLoader.
    */
   @Override
   public Object visitEvictFqnCommand(InvocationContext ctx, EvictCommand command) throws Throwable
   {
      if (command.isRecursive())
      {
         for (Fqn f : command.getNodesToEvict()) passivate(ctx, f);
      }
      else
      {
         passivate(ctx, command.getFqn());
      }

      return invokeNextInterceptor(ctx, command);
   }

   protected void passivate(InvocationContext ctx, Fqn fqn) throws Throwable
   {
      try
      {
         // evict method local doesn't hold attributes therefore we have
         // to get them manually
         Map attributes = getNodeAttributes(ctx, fqn);
         // notify listeners that this node is about to be passivated
         notifier.notifyNodePassivated(fqn, true, attributes, ctx);
         if (trace) log.trace("Passivating " + fqn);
         loader.put(fqn, attributes);
         notifier.notifyNodePassivated(fqn, false, Collections.emptyMap(), ctx);
         if (getStatisticsEnabled()) passivations.getAndIncrement();
      }
      catch (NodeNotLoadedException e)
      {
         if (trace)
         {
            log.trace("Node " + fqn + " not loaded in memory; passivation skipped");
         }
      }
   }

   /**
    * Returns attributes for a node.
    */
   private Map getNodeAttributes(InvocationContext ctx, Fqn fqn) throws NodeNotLoadedException
   {
      if (fqn == null)
      {
         throw new NodeNotLoadedException();
      }
      NodeSPI n = ctx.lookUpNode(fqn);

      if (n != null)
      {
         return n.getDataDirect();
      }
      else
      {
         throw new NodeNotLoadedException();
      }
   }

   private static class NodeNotLoadedException extends Exception
   {

      /**
       * The serialVersionUID
       */
      private static final long serialVersionUID = -4078972305344328905L;

   }

   @ManagedOperation
   public void resetStatistics()
   {
      passivations.set(0);
   }

   @ManagedOperation
   public Map<String, Object> dumpStatistics()
   {
      Map<String, Object> retval = new HashMap<String, Object>();
      retval.put("Passivations", passivations.get());
      return retval;
   }

   @ManagedAttribute(description = "number of cache node passivations")
   public long getPassivations()
   {
      return passivations.get();
   }
}
