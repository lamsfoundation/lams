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
package org.jboss.cache.lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.invocation.InvocationContextContainer;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import static org.jboss.cache.lock.LockType.READ;
import org.jboss.cache.util.concurrent.locks.LockContainer;
import org.jboss.cache.util.concurrent.locks.OwnableReentrantLock;
import org.jboss.cache.util.concurrent.locks.OwnableReentrantSharedLockContainer;
import org.jboss.cache.util.concurrent.locks.ReentrantSharedLockContainer;
import org.jboss.cache.util.concurrent.locks.PerElementReentrantLockContainer;
import org.jboss.cache.util.concurrent.locks.PerElementOwnableReentrantLockContainer;

import javax.transaction.TransactionManager;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.concurrent.locks.Lock;

/**
 * This lock manager acquires and releases locks based on the Fqn passed in and not on the node itself.  The main benefit
 * here is that locks can be acquired and held even for nonexistent nodes.
 * <p/>
 * Uses lock striping so that a fixed number of locks are maintained for the entire cache, and not a single lock per node.
 * <p/>
 * This implementation only acquires exclusive WRITE locks, and throws an exception if you attempt to use it to acquire a
 * READ lock.  JBoss Cache's MVCC design doesn't use read locks at all.
 * <p/>
 * The concept of lock owners is implicit in this implementation and any owners passed in as parameters (where required)
 * will be ignored.  See {@link org.jboss.cache.util.concurrent.locks.OwnableReentrantLock} for details on how ownership
 * is determined.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see org.jboss.cache.util.concurrent.locks.OwnableReentrantLock
 * @since 3.0
 */
public class MVCCLockManager extends FqnLockManager
{
   LockContainer<Fqn> lockContainer;
   DataContainer dataContainer;
   private Set<Fqn> internalFqns;
   private CacheSPI<?, ?> cache;
   private TransactionManager transactionManager;
   private InvocationContextContainer invocationContextContainer;
   private static final Log log = LogFactory.getLog(MVCCLockManager.class);
   private static final boolean trace = log.isTraceEnabled();

   @Inject
   public void injectDependencies(DataContainer dataContainer, CacheSPI cache, TransactionManager transactionManager, InvocationContextContainer invocationContextContainer)
   {
      this.dataContainer = dataContainer;
      this.cache = cache;
      this.transactionManager = transactionManager;
      this.invocationContextContainer = invocationContextContainer;
   }

   @Start
   public void startLockManager()
   {
      // don't we all love nested ternary operators?  :-)
      lockContainer =
            configuration.isUseLockStriping() ?
               transactionManager == null ? new ReentrantSharedLockContainer<Fqn>(configuration.getConcurrencyLevel()) :
                     new OwnableReentrantSharedLockContainer<Fqn>(configuration.getConcurrencyLevel(), invocationContextContainer) :
               transactionManager == null ? new PerElementReentrantLockContainer<Fqn>(configuration.getConcurrencyLevel()) :
                     new PerElementOwnableReentrantLockContainer<Fqn>(configuration.getConcurrencyLevel(), invocationContextContainer);
   }

   @Start
   public void setInternalFqns()
   {
      internalFqns = cache.getInternalFqns();
   }

   public boolean lock(Fqn fqn, LockType lockType, Object owner) throws InterruptedException
   {
      if (lockType == READ) return true; // we don't support read locks.

      if (trace) log.trace("Attempting to lock " + fqn);
      return lockContainer.acquireLock(fqn, lockAcquisitionTimeout, MILLISECONDS);
   }

   public boolean lock(Fqn fqn, LockType lockType, Object owner, long timeoutMillis) throws InterruptedException
   {
      if (lockType == READ) return true; // we don't support read locks.

      if (trace) log.trace("Attempting to lock " + fqn);
      return lockContainer.acquireLock(fqn, lockAcquisitionTimeout, MILLISECONDS);
   }

   public boolean lockAndRecord(Fqn fqn, LockType lockType, InvocationContext ctx) throws InterruptedException
   {
      if (lockType == READ) return true; // we don't support read locks.

      if (trace) log.trace("Attempting to lock " + fqn);
      if (lockContainer.acquireLock(fqn, lockAcquisitionTimeout, MILLISECONDS))
      {
         ctx.addLock(fqn);
         return true;
      }

      // couldn't acquire lock!
      return false;
   }

   public void unlock(Fqn fqn, Object owner)
   {
      if (trace) log.trace("Attempting to unlock " + fqn);
      try
      {
         lockContainer.releaseLock(fqn);
      }
      catch (IllegalMonitorStateException imse)
      {
         if (trace) log.trace("Caught exception and ignoring.", imse);
      }
   }

   @SuppressWarnings("unchecked")
   public void unlock(InvocationContext ctx)
   {
      List<Fqn> locks = ctx.getLocks();
      if (!locks.isEmpty())
      {
         // unlocking needs to be done in reverse order.
         ListIterator<Fqn> it = locks.listIterator(locks.size());
         while (it.hasPrevious())
         {
            Fqn f = it.previous();
            if (trace) log.trace("Attempting to unlock " + f);
            try
            {
               lockContainer.releaseLock(f);
            }
            catch (IllegalMonitorStateException imse)
            {
               if (trace) log.trace("Caught exception and ignoring.", imse);
            }
         }
      }
   }

   @SuppressWarnings("unchecked")
   private boolean lockRecursively(InternalNode node, long timeoutMillis, boolean excludeInternalFqns, InvocationContext ctx) throws InterruptedException
   {
      if (excludeInternalFqns && internalFqns.contains(node.getFqn()))
         return true; // this will stop the recursion from proceeding down this subtree.

      boolean locked = ctx == null ?
            lock(node.getFqn(), LockType.WRITE, null, timeoutMillis) :
            lockAndRecord(node.getFqn(), LockType.WRITE, ctx);

      if (!locked) return false;
      boolean needToUnlock = false;

      // need to recursively walk through the node's children and acquire locks.  This needs to happen using API methods
      // since any cache loading will need to happen.
      Map<Object, InternalNode> children = node.getChildrenMap();
      try
      {
         for (InternalNode child : children.values())
         {
            locked = lockRecursively(child, timeoutMillis, excludeInternalFqns, ctx);
            if (!locked)
            {
               needToUnlock = true;
               break;
            }
         }
      }
      finally
      {
         if (needToUnlock)
         {
            for (InternalNode child : children.values())
            {
               Fqn childFqn = child.getFqn();
               unlock(childFqn, null);
               if (ctx != null) ctx.removeLock(childFqn);
            }
            unlock(node.getFqn(), null);
            if (ctx != null) ctx.removeLock(node.getFqn());
         }
      }
      return locked;
   }

   public boolean lockAll(NodeSPI node, LockType lockType, Object owner) throws InterruptedException
   {
      if (lockType == READ) return true; // we don't support read locks.
      return lockRecursively(node.getDelegationTarget(), lockAcquisitionTimeout, false, null);
   }

   public boolean lockAll(NodeSPI node, LockType lockType, Object owner, long timeout) throws InterruptedException
   {
      if (lockType == READ) return true; // we don't support read locks.
      return lockRecursively(node.getDelegationTarget(), timeout, false, null);
   }

   public boolean lockAll(NodeSPI node, LockType lockType, Object owner, long timeout, boolean excludeInternalFqns) throws InterruptedException
   {
      if (lockType == READ) return true; // we don't support read locks.
      return lockRecursively(node.getDelegationTarget(), timeout, excludeInternalFqns, null);
   }

   public boolean lockAllAndRecord(NodeSPI node, LockType lockType, InvocationContext ctx) throws InterruptedException
   {
      if (lockType == READ) return true; // we don't support read locks.
      return lockRecursively(node.getDelegationTarget(), ctx.getLockAcquisitionTimeout(lockAcquisitionTimeout), false, ctx);
   }

   public boolean lockAllAndRecord(Fqn fqn, LockType lockType, InvocationContext ctx) throws InterruptedException
   {
      return lockRecursively(dataContainer.peekInternalNode(fqn, false), ctx.getLockAcquisitionTimeout(lockAcquisitionTimeout), false, ctx);
   }

   public void unlockAll(NodeSPI<?, ?> node, Object owner)
   {
      // depth first.
      Set<? extends Node<?, ?>> children = node.getChildren();
      if (children != null)
      {
         for (Node child : children) unlockAll((NodeSPI) child, null);
      }
      unlock(node.getFqn(), null);
   }

   public void unlockAll(NodeSPI node)
   {
      unlockAll(node, null);
   }

   public boolean ownsLock(Fqn fqn, LockType lockType, Object owner)
   {
      if (lockType == READ) return false; // we don't support read locks.
      return lockContainer.ownsLock(fqn, owner);
   }

   public boolean ownsLock(Fqn fqn, Object owner)
   {
      return lockContainer.ownsLock(fqn, owner);
   }

   public boolean isLocked(Fqn fqn)
   {
      return lockContainer.isLocked(fqn);
   }

   public boolean isLocked(NodeSPI n, LockType lockType)
   {
      if (lockType == READ) return false; // we don't support read locks.
      return lockContainer.isLocked(n.getFqn());
   }

   public Object getWriteOwner(Fqn f)
   {
      if (lockContainer.isLocked(f))
      {
         Lock l = lockContainer.getLock(f);

         if (l instanceof OwnableReentrantLock)
         {
            return ((OwnableReentrantLock) l).getOwner();
         }
         else
         {
            // cannot determine owner.
            return null;
         }
      }
      else return null;
   }

   public Collection<Object> getReadOwners(Fqn f)
   {
      return Collections.emptySet();
   }

   public String printLockInfo(NodeSPI node)
   {
      return printLockInfo();
   }

   public String printLockInfo()
   {
      return lockContainer.toString();
   }

   @ManagedAttribute(name = "concurrency level", writable = false, description = "The concurrency level that the MVCC Lock Manager has been configured with.")
   public int getConcurrencyLevel()
   {
      return configuration.getConcurrencyLevel();
   }

   @ManagedAttribute(name = "locks held", writable = false, description = "The number of exclusive locks that are held.")
   public int getNumberOfLocksHeld()
   {
      return lockContainer.getNumLocksHeld();
   }

   @ManagedAttribute(name = "locks held", writable = false, description = "The number of exclusive locks that are available.")
   public int getNumberOfLocksAvailable()
   {
      return lockContainer.size() - lockContainer.getNumLocksHeld();
   }
}
