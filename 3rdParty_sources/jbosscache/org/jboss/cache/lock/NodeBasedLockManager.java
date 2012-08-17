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
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.Start;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 * @deprecated since this is specific to legacy locking schemes
 */
@SuppressWarnings("deprecation")
@Deprecated
public class NodeBasedLockManager extends AbstractLockManager
{
   private static final Log log = LogFactory.getLog(NodeBasedLockManager.class);
   private static final boolean trace = log.isTraceEnabled();

   protected DataContainer dataContainer;
   protected NodeSPI rootNode;

   @Inject
   public void inject(DataContainer dataContainer)
   {
      this.dataContainer = dataContainer;
   }

   @Start
   public void setRootNode()
   {
      rootNode = dataContainer.getRoot();
   }

   /**
    * Internal method that acquires a lock and returns the lock object.  Currently uses {@link IdentityLock} objects; may change in
    * future to use standard JDK locks.
    *
    * @param fqn      Fqn to lock
    * @param lockType type of lock to acquire
    * @param owner    owner to acquire lock for
    * @param timeout  timeout
    * @return lock if acquired, null otherwise.
    */
   private NodeLock acquireLock(Fqn fqn, LockType lockType, Object owner, long timeout)
   {
      return acquireLock(dataContainer.peek(fqn), lockType, owner, timeout);
   }

   private NodeLock acquireLock(NodeSPI node, LockType lockType, Object owner, long timeout)
   {
      if (node == null) return null;
      NodeLock lock = node.getLock();
      boolean acquired = false;
      try
      {
         acquired = lock.acquire(owner, timeout, lockType);
      }
      catch (InterruptedException e)
      {
         // interrupted trying to acquire lock!
      }

      if (acquired)
         return lock;
      else
         return null;
   }

   public boolean lock(Fqn fqn, LockType lockType, Object owner)
   {
      return acquireLock(fqn, lockType, owner, lockAcquisitionTimeout) != null;
   }

   public boolean lock(Fqn fqn, LockType lockType, Object owner, long timeout)
   {
      return acquireLock(fqn, lockType, owner, timeout) != null;
   }

   public boolean lock(NodeSPI node, LockType lockType, Object owner)
   {
      return acquireLock(node, lockType, owner, lockAcquisitionTimeout) != null;
   }

   public boolean lock(NodeSPI node, LockType lockType, Object owner, long timeout)
   {
      return acquireLock(node, lockType, owner, timeout) != null;
   }


   public boolean lockAndRecord(Fqn fqn, LockType lockType, InvocationContext ctx)
   {
      return lockAndRecord(dataContainer.peek(fqn), lockType, ctx);
   }

   public boolean lockAndRecord(NodeSPI node, LockType lockType, InvocationContext ctx)
   {
      NodeLock lock = acquireLock(node, lockType, getLockOwner(ctx), ctx.getLockAcquisitionTimeout(lockAcquisitionTimeout));
      if (lock != null)
      {
         ctx.addLock(lock);
         return true;
      }
      else
      {
         return false;
      }
   }

   public void unlock(InvocationContext ctx)
   {
      List<NodeLock> locks = ctx.getLocks();
      if (locks.isEmpty()) return;

      Object owner = getLockOwner(ctx);

      // unlocking needs to be done in reverse order.
      ListIterator<NodeLock> it = locks.listIterator(locks.size());
      while (it.hasPrevious())
      {
         NodeLock l = it.previous();

         if (trace)
            log.trace("releasing lock for " + l.getFqn() + " (" + l + "), owner " + owner);
         l.release(owner);
      }

      ctx.clearLocks();
   }

   private void unlock(NodeLock lock, Object owner)
   {
      if (trace) log.trace("releasing lock for " + lock.getFqn() + " (" + lock + "), owner " + owner);
      lock.release(owner);
   }

   public void unlock(Fqn fqn, Object owner)
   {
      unlock(dataContainer.peek(fqn).getLock(), owner);
   }

   public void unlock(NodeSPI node, Object owner)
   {
      if (node == null) return;
      unlock(node.getLock(), owner);
   }

   public boolean lockAll(NodeSPI node, LockType lockType, Object owner)
   {
      return lockAll(node, lockType, owner, lockAcquisitionTimeout, false);
   }

   public boolean lockAll(NodeSPI node, LockType lockType, Object owner, long timeout)
   {
      return lockAll(node, lockType, owner, timeout, false);
   }

   /**
    * Locks all nodes, and returns the NodeLocks in a List.  Returns null if the locks could not be acquired.
    *
    * @param node                node to lock
    * @param lockType            type of lock to acquire
    * @param owner               lock owner
    * @param timeout             timeout
    * @param excludeInternalFqns if true, internal Fqns are excluded.
    * @return list of locks acquired, or null.
    */
   private List<NodeLock> lockAllNodes(NodeSPI node, LockType lockType, Object owner, long timeout, boolean excludeInternalFqns)
   {
      if (node == null) return null;
      List<NodeLock> locks = null;
      try
      {
         locks = new ArrayList<NodeLock>(node.getLock().acquireAll(owner, timeout, lockType, excludeInternalFqns));
      }
      catch (InterruptedException e)
      {
         // interrupted
      }
      return locks;
   }

   public boolean lockAll(NodeSPI node, LockType lockType, Object owner, long timeout, boolean excludeInternalFqns)
   {
      return lockAllNodes(node, lockType, owner, timeout, excludeInternalFqns) != null;
   }

   public boolean lockAllAndRecord(Fqn fqn, LockType lockType, InvocationContext ctx)
   {
      return lockAllAndRecord(dataContainer.peek(fqn), lockType, ctx);
   }

   public boolean lockAllAndRecord(NodeSPI node, LockType lockType, InvocationContext ctx)
   {
      List<NodeLock> locks = lockAllNodes(node, lockType, getLockOwner(ctx), ctx.getLockAcquisitionTimeout(lockAcquisitionTimeout), false);
      if (locks == null) return false;

      if (locks.size() > 0)
      {
         ctx.addAllLocks(locks);
      }

      return true;
   }

   public void unlockAll(NodeSPI node, Object owner)
   {
      // recursively visit node and all children, and release all locks held by a given owner.
      node.getLock().releaseAll(owner);
   }

   public void unlockAll(NodeSPI node)
   {
      // recursively visit node and all children, and release all locks held by a given owner.
      node.getLock().releaseAll();
   }

   public boolean ownsLock(Fqn fqn, LockType lockType, Object owner)
   {
      NodeSPI n = dataContainer.peek(fqn, true, true);
      if (n == null) return false;
      NodeLock lock = n.getLock();
      switch (lockType)
      {
         case READ:
            return lock.isReadLocked() && lock.isOwner(owner);
         case WRITE:
            return lock.isWriteLocked() && lock.isOwner(owner);
         case NONE:
         default:
            return false;
      }
   }

   public boolean ownsLock(Fqn fqn, Object owner)
   {
      return ownsLock(dataContainer.peek(fqn, true, true), owner);
   }

   public boolean ownsLock(NodeSPI node, Object owner)
   {
      return node != null && node.getLock().isOwner(owner);
   }

   public boolean isLocked(NodeSPI n)
   {
      return n.getLock().isLocked();
   }

   public boolean isLocked(Fqn fqn)
   {
      return isLocked(dataContainer.peek(fqn));
   }

   public boolean isLocked(NodeSPI n, LockType type)
   {
      switch (type)
      {
         case READ:
            return n.getLock().isReadLocked();
         case WRITE:
            return n.getLock().isWriteLocked();
         case NONE:
         default:
            return false;
      }
   }


   public Object getWriteOwner(Fqn f)
   {
      return getWriteOwner(dataContainer.peek(f));
   }

   public Collection<Object> getReadOwners(Fqn f)
   {
      return getReadOwners(dataContainer.peek(f));
   }

   public Object getWriteOwner(NodeSPI node)
   {
      return node.getLock().getWriterOwner();
   }

   public Collection<Object> getReadOwners(NodeSPI node)
   {
      return node.getLock().getReaderOwners();
   }

   public String printLockInfo(NodeSPI node)
   {
      StringBuilder sb = new StringBuilder("\n");
      int indent = 0;

      for (Object n : node.getChildrenDirect())
      {
         ((NodeSPI) n).getLock().printLockInfo(sb, indent);
         sb.append("\n");
      }

      return sb.toString();
   }

   public String printLockInfo()
   {
      return printLockInfo(dataContainer.getRoot());
   }
}
