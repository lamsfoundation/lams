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
package org.jboss.cache.statetransfer;

import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.RegionEmptyException;
import org.jboss.cache.util.CachePrinter;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.lock.LockManager;
import static org.jboss.cache.lock.LockType.READ;
import org.jboss.cache.lock.TimeoutException;
import org.jboss.cache.marshall.InactiveRegionException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This is to support legacy locking schemes such as Pessimistic and Optimistic locking.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 3.0
 */
@Deprecated
public class LegacyStateTransferManager extends DefaultStateTransferManager
{
   protected LockManager lockManager;
   private boolean usePut;  // for JBCACHE-131

   @Inject
   public void injectLockManager(LockManager lockManager)
   {
      this.lockManager = lockManager;
   }

   @Start(priority = 14)
   public void checkLoaders()
   {
      usePut = configuration.getCacheLoaderConfig() != null && !configuration.getCacheLoaderConfig().isFetchPersistentState() &&
            !configuration.getCacheLoaderConfig().isShared();
   }

   @Override
   public void getState(ObjectOutputStream out, Fqn fqn, long timeout, boolean force, boolean suppressErrors) throws Exception
   {
      // can't give state for regions currently being activated/inactivated
      boolean canProvideState = (!regionManager.isInactive(fqn) && cache.peek(fqn, false) != null);

      if (canProvideState && (fetchPersistentState || fetchTransientState))
      {
         marshaller.objectToObjectStream(true, out);
         long startTime = System.currentTimeMillis();
         NodeSPI rootNode = cache.peek(fqn, false, false);

         try
         {
            if (log.isDebugEnabled())
            {
               log.debug("locking the " + fqn + " subtree to return the in-memory (transient) state");
            }
            acquireLocksForStateTransfer(rootNode, timeout, force);
            generator.generateState(out, rootNode, fetchTransientState, fetchPersistentState, suppressErrors);
            if (log.isDebugEnabled())
            {
               log.debug("Successfully generated state in " + CachePrinter.prettyPrint(System.currentTimeMillis() - startTime));
            }
         }
         finally
         {
            releaseStateTransferLocks(rootNode);
         }
      }
      else
      {
         marshaller.objectToObjectStream(false, out);
         Exception e = null;
         if (!canProvideState)
         {
            String exceptionMessage = "Cache instance at " + cache.getLocalAddress() + " cannot provide state for fqn " + fqn + ".";

            if (regionManager.isInactive(fqn))
            {
               exceptionMessage += " Region for fqn " + fqn + " is inactive.";
               e = new InactiveRegionException(exceptionMessage);
            }
            // this is not really an exception.  Just provide empty state. The exception is just a signal.  Yes, lousy.  - JBCACHE-1349
            if (cache.peek(fqn, false, false) == null)
            {
               e = new RegionEmptyException();
            }
         }
         if (!fetchPersistentState && !fetchTransientState)
         {
            e = new CacheException("Cache instance at " + cache.getLocalAddress() + " is not configured to provide state");
         }
         marshaller.objectToObjectStream(e, out);
         if (e != null) throw e;
      }
   }

   /**
    * Set the portion of the cache rooted in <code>targetRoot</code>
    * to match the given state. Updates the contents of <code>targetRoot</code>
    * to reflect those in <code>new_state</code>.
    * <p/>
    * <strong>NOTE:</strong> This method performs no locking of nodes; it
    * is up to the caller to lock <code>targetRoot</code> before calling
    * this method.
    *
    * @param state      a serialized byte[][] array where element 0 is the
    *                   transient state (or null) , and element 1 is the
    *                   persistent state (or null)
    * @param targetRoot node into which the state should be integrated
    */
   protected void setState(ObjectInputStream state, NodeSPI targetRoot) throws Exception
   {
      long startTime = System.currentTimeMillis();

      acquireLocksForStateTransfer(targetRoot, stateRetrievalTimeout, true);
      /*
       * Vladimir/Manik/Brian (Dec 7,2006)
       *
       * integrator.integrateState(in,targetRoot, cl) will call cache.put for each
       * node read from stream. Having option override below allows nodes read
       * to be directly stored into a tree since we bypass interceptor chain.
       *
       */
      try
      {
         if (log.isDebugEnabled())
         {
            log.debug("starting state integration at node " + targetRoot);
         }
         integrator.integrateState(state, targetRoot, targetRoot.getFqn(), fetchPersistentState);
         if (log.isDebugEnabled())
         {
            log.debug("successfully integrated state in " + CachePrinter.prettyPrint(System.currentTimeMillis() - startTime));
         }
      }
      finally
      {
         releaseStateTransferLocks(targetRoot);
      }
   }


   protected void acquireLocksForStateTransfer(NodeSPI root, long timeout, boolean force) throws InterruptedException
   {
      if (usePut) return;
      try
      {
         lockManager.lockAll(root, READ, getLockOwner(), timeout, true);
      }
      catch (TimeoutException te)
      {
         log.error("Caught TimeoutException acquiring locks on region " +
               root.getFqn(), te);
         if (force)
         {
            throw te;
         }
         else
         {
            throw te;
         }
      }
   }

   protected void releaseStateTransferLocks(NodeSPI root)
   {
      if (usePut) return;
      try
      {
         lockManager.unlockAll(root, getLockOwner());
      }
      catch (Throwable t)
      {
         log.error("failed releasing locks", t);
      }
   }

   private Object getLockOwner()
   {
      Object owner = cache.getCurrentTransaction();
      if (owner == null) owner = Thread.currentThread();
      return owner;
   }
}
