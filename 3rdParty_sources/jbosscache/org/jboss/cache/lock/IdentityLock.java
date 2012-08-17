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
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Lock object which grants and releases locks, and associates locks with
 * <em>owners</em>.  The methods to acquire and release a lock require an owner
 * (Object). When a lock is acquired, we store the current owner with the lock.
 * When the same owner (<em>but possibly running in a different thread</em>)
 * wants to acquire the lock, it is immediately granted. When an owner
 * different from the one currently owning the lock wants to release the lock,
 * we do nothing (no-op).
 * <p/>
 * Consider the following example:
 * <pre>
 * FIFOSemaphore lock=new FIFOSemaphore(1);
 * lock.acquire();
 * lock.acquire();
 * lock.release();
 * </pre>
 * This would block on the second <tt>acquire()</tt> (although we currently already hold
 * the lock) because the lock only has 1 permit. So <tt>IdentityLock</tt> will allow the
 * following code to work properly:
 * <pre>
 * IdentityLock lock=new IdentityLock();
 * lock.readLock().acquire(this, timeout);
 * lock.writeLock().acquire(this, timeout);
 * lock.release(this);
 * </pre>
 * If the owner is null, then the current thread is taken by default. This allow the following
 * code to work:
 * <pre>
 * IdentityLock lock=new IdentityLock();
 * lock.readLock().attempt(null, timeout);
 * lock.writeLock().attempt(null, timeout);
 * lock.release(null);
 * </pre>
 * <br/>
 * Note that the Object given as owner is required to implement {@link Object#equals}. For
 * a use case see the examples in the testsuite.
 *
 * @author <a href="mailto:bela@jboss.org">Bela Ban</a> Apr 11, 2003
 * @author Ben Wang July 2003
 * @deprecated will be removed when we drop support for Pessimistic Locking and Optimistic Locking
 */
@Deprecated
@SuppressWarnings("deprecation")
public class IdentityLock implements NodeLock
{

   /**
    * Initialized property for debugging "print_lock_details"
    */
   private boolean PRINT_LOCK_DETAILS = Boolean.getBoolean("print_lock_details");

   private static final Log log = LogFactory.getLog(IdentityLock.class);
   private static final boolean trace = log.isTraceEnabled();
   private final LockStrategy lock;
   private final LockMap map;
   private final boolean mustReacquireRead;
   private NodeSPI<?, ?> node;

   /**
    * Creates a new IdentityLock using the LockFactory passed in.
    *
    * @param factory to create lock strategies
    * @param node    to lock
    */
   public IdentityLock(LockStrategyFactory factory, NodeSPI node)
   {
      lock = factory.getLockStrategy();
      mustReacquireRead = lock instanceof LockStrategyReadCommitted;
      map = new LockMap();
      this.node = node;
   }

   /**
    * Returns the node for this lock, may be <code>null</code>.
    */
   public Node getNode()
   {
      return node;
   }

   /**
    * Returns the FQN this lock, may be <code>null</code>.
    */
   public Fqn getFqn()
   {
      if (node == null)
      {
         return null;
      }
      return node.getFqn();
   }

   /**
    * Return a copy of the reader lock owner in List. Size is zero is not available. Note that this list
    * is synchronized.
    *
    * @return Collection<Object> of readers
    */
   public Collection<Object> getReaderOwners()
   {
      return map.readerOwners();
   }

   /**
    * Return the writer lock owner object. Null if not available.
    *
    * @return Object owner
    */
   public Object getWriterOwner()
   {
      return map.writerOwner();
   }

   public LockMap getLockMap()
   {
      return map;
   }

   /**
    * Acquire a write lock with a timeout of <code>timeout</code> milliseconds.
    * Note that if the current owner owns a read lock, it will be upgraded
    * automatically. However, if upgrade fails, i.e., timeout, the read lock will
    * be released automatically.
    *
    * @param caller  Can't be null.
    * @param timeout
    * @return boolean True if lock was acquired and was not held before, false if lock was held
    * @throws LockingException
    * @throws TimeoutException
    */
   public boolean acquireWriteLock(Object caller, long timeout) throws LockingException, TimeoutException, InterruptedException
   {
      if (trace)
      {
         log.trace(new StringBuilder("acquiring WL: fqn=").append(getFqn()).append(", caller=").append(caller).
               append(", lock=").append(toString(PRINT_LOCK_DETAILS)));
      }
      boolean flag = acquireWriteLock0(caller, timeout);
      if (trace)
      {
         log.trace(new StringBuilder("acquired WL: fqn=").append(getFqn()).append(", caller=").append(caller).
               append(", lock=").append(toString(PRINT_LOCK_DETAILS)));
      }
      return flag;
   }

   private boolean acquireWriteLock0(Object caller, long timeout) throws LockingException, TimeoutException, InterruptedException
   {
      if (caller == null)
      {
         throw new IllegalArgumentException("acquireWriteLock(): null caller");
      }

      if (map.isOwner(caller, LockMap.OWNER_WRITE))
      {
         if (trace)
         {
            log.trace("acquireWriteLock(): caller already owns lock for " + getFqn() + " (caller=" + caller + ')');
         }
         return false;// owner already has the lock
      }

      // Check first if we need to upgrade
      if (map.isOwner(caller, LockMap.OWNER_READ))
      {
         // Currently is a reader owner. Obtain the writer ownership then.
         Lock wLock;
         try
         {
            if (trace)
            {
               log.trace("upgrading RL to WL for " + caller + ", timeout=" + timeout + ", locks: " + map.printInfo());
            }
            wLock = lock.upgradeLockAttempt(timeout);
         }
         catch (UpgradeException ue)
         {
            String errStr = "acquireWriteLock(): lock upgrade failed for " + getFqn() + " (caller=" + caller + ", lock info: " + toString(true) + ')';
            log.trace(errStr, ue);
            throw new UpgradeException(errStr, ue);
         }
         if (wLock == null)
         {
            release(caller);// bug fix: remember to release the read lock before throwing the exception
            map.removeReader(caller);
            String errStr = "upgrade lock for " + getFqn() + " could not be acquired after " + timeout + " ms." +
                  " Lock map ownership " + map.printInfo() + " (caller=" + caller + ", lock info: " + toString(true) + ')';
            log.trace(errStr);
            throw new UpgradeException(errStr);
         }
         try
         {
            if (trace)
            {
               log.trace("upgrading lock for " + getFqn());
            }
            map.upgrade(caller);
         }
         catch (OwnerNotExistedException ex)
         {
            throw new UpgradeException("Can't upgrade lock to WL for " + getFqn() + ", error in LockMap.upgrade()", ex);
         }
      }
      else
      {
         // Not a current reader owner. Obtain the writer ownership then.
         boolean rc = lock.writeLock().tryLock(timeout, TimeUnit.MILLISECONDS);

         // we don't need to synchronize from here on because we own the semaphore
         if (!rc)
         {
            String errStr = "write lock for " + getFqn() + " could not be acquired after " + timeout + " ms. " +
                  "Locks: " + map.printInfo() + " (caller=" + caller + ", lock info: " + toString(true) + ')';
            log.trace(errStr);
            throw new TimeoutException(errStr);
         }
         map.setWriterIfNotNull(caller);
      }
      return true;
   }

   /**
    * Acquire a read lock with a timeout period of <code>timeout</code> milliseconds.
    *
    * @param caller  Can't be null.
    * @param timeout
    * @return boolean True if lock was acquired and was not held before, false if lock was held
    * @throws LockingException
    * @throws TimeoutException
    */
   public boolean acquireReadLock(Object caller, long timeout) throws LockingException, TimeoutException, InterruptedException
   {
      if (trace)
      {
         log.trace(new StringBuilder("acquiring RL: fqn=").append(getFqn()).append(", caller=").append(caller).
               append(", lock=").append(toString(PRINT_LOCK_DETAILS)));
      }
      boolean flag = acquireReadLock0(caller, timeout);
      if (trace)
      {
         log.trace(new StringBuilder("acquired RL: fqn=").append(getFqn()).append(", caller=").append(caller).
               append(", lock=").append(toString(PRINT_LOCK_DETAILS)));
      }
      return flag;
   }

   private boolean acquireReadLock0(Object caller, long timeout)
         throws LockingException, TimeoutException, InterruptedException
   {
      boolean rc;

      if (caller == null)
      {
         throw new IllegalArgumentException("owner is null");
      }

      boolean hasRead = false;
      boolean hasRequired = false;
      if (mustReacquireRead)
      {
         hasRequired = map.isOwner(caller, LockMap.OWNER_WRITE);
         if (!hasRequired)
         {
            hasRead = map.isOwner(caller, LockMap.OWNER_READ);
         }
      }
      else if (map.isOwner(caller, LockMap.OWNER_ANY))
      {
         hasRequired = true;
      }

      if (hasRequired)
      {
         if (trace)
         {
            StringBuilder sb = new StringBuilder(64);
            sb.append("acquireReadLock(): caller ").append(caller).append(" already owns lock for ").append(getFqn());
            log.trace(sb.toString());
         }
         return false;// owner already has the lock
      }

      rc = lock.readLock().tryLock(timeout, TimeUnit.MILLISECONDS);

      // we don't need to synchronize from here on because we own the semaphore
      if (!rc)
      {
         StringBuilder sb = new StringBuilder();
         sb.append("read lock for ").append(getFqn()).append(" could not be acquired by ").append(caller);
         sb.append(" after ").append(timeout).append(" ms. " + "Locks: ").append(map.printInfo());
         sb.append(", lock info: ").append(toString(true));
         String errMsg = sb.toString();
         log.trace(errMsg);
         throw new TimeoutException(errMsg);
      }

      // Only add to the map if we didn't already have the lock
      if (!hasRead)
      {
         map.addReader(caller);// this is synchronized internally, we don't need to synchronize here
      }
      return true;
   }

   /**
    * Release the lock held by the owner.
    *
    * @param caller Can't be null.
    */
   public void release(Object caller)
   {
      if (caller == null)
      {
         throw new IllegalArgumentException("IdentityLock.release(): null owner object.");
      }

      // Check whether to release reader or writer lock.
      if (map.isOwner(caller, LockMap.OWNER_READ))
      {
         map.removeReader(caller);
         lock.readLock().unlock();
      }
      else if (map.isOwner(caller, LockMap.OWNER_WRITE))
      {
         map.removeWriter();
         lock.writeLock().unlock();
      }
   }

   /**
    * Release all locks associated with this instance.
    */
   public void releaseAll()
   {
      try
      {
         if ((map.writerOwner()) != null)
         {
            // lock_.readLock().release();
            lock.writeLock().unlock();
         }

         map.releaseReaderOwners(lock);
      }
      finally
      {
         map.removeAll();
      }
   }

   /**
    * Same as releaseAll now.
    */
   public void releaseForce()
   {
      releaseAll();
   }

   /**
    * Check if there is a read lock.
    */
   public boolean isReadLocked()
   {
      return map.isReadLocked();
   }

   /**
    * Check if there is a write lock.
    */
   public boolean isWriteLocked()
   {
      return map.writerOwner() != null;
   }

   /**
    * Check if there is a read or write lock
    */
   public boolean isLocked()
   {
      return isReadLocked() || isWriteLocked();
   }

   /**
    * Am I a lock owner?
    *
    * @param o
    */
   public boolean isOwner(Object o)
   {
      return map.isOwner(o, LockMap.OWNER_ANY);
   }

   @Override
   public String toString()
   {
      return toString(false);
   }

   public String toString(boolean print_lock_details)
   {
      StringBuilder sb = new StringBuilder();
      toString(sb, print_lock_details);
      return sb.toString();
   }

   public void toString(StringBuilder sb)
   {
      toString(sb, false);
   }

   public void toString(StringBuilder sb, boolean print_lock_details)
   {
      boolean printed_read_owners = false;
      Collection<Object> read_owners = lock != null ? getReaderOwners() : null;
      if (read_owners != null && read_owners.size() > 0)
      {
         // Fix for JBCACHE-310 -- can't just call new ArrayList(read_owners) :(
         // Creating the ArrayList and calling addAll doesn't work either
         // Looking at the details of how this is implemented vs. the 2 prev
         // options, this doesn't look like it should be slower
         Iterator iter = read_owners.iterator();
         read_owners = new ArrayList<Object>(read_owners.size());
         while (iter.hasNext())
         {
            read_owners.add(iter.next());
         }

         sb.append("read owners=").append(read_owners);
         printed_read_owners = true;
      }

      Object write_owner = lock != null ? getWriterOwner() : null;
      if (write_owner != null)
      {
         if (printed_read_owners)
         {
            sb.append(", ");
         }
         sb.append("write owner=").append(write_owner);
      }
      if (read_owners == null && write_owner == null)
      {
         sb.append("<unlocked>");
      }
      if (print_lock_details)
      {
         sb.append(" (").append(lock.toString()).append(')');
      }
   }

   public boolean acquire(Object caller, long timeout, LockType lock_type) throws LockingException, TimeoutException, InterruptedException
   {
      try
      {
         if (lock_type == LockType.NONE)
         {
            return true;
         }
         else if (lock_type == LockType.READ)
         {
            return acquireReadLock(caller, timeout);
         }
         else
         {
            return acquireWriteLock(caller, timeout);
         }
      }
      catch (UpgradeException e)
      {
         StringBuilder buf = new StringBuilder("failure upgrading lock: fqn=").append(getFqn()).append(", caller=").append(caller).
               append(", lock=").append(toString(true));
         if (trace)
         {
            log.trace(buf.toString());
         }
         throw new UpgradeException(buf.toString(), e);
      }
      catch (LockingException e)
      {
         StringBuilder buf = new StringBuilder("failure acquiring lock: fqn=").append(getFqn()).append(", caller=").append(caller).
               append(", lock=").append(toString(true));
         if (trace)
         {
            log.trace(buf.toString());
         }
         throw new LockingException(buf.toString(), e);
      }
      catch (TimeoutException e)
      {
         StringBuilder buf = new StringBuilder("failure acquiring lock: fqn=").append(getFqn()).append(", caller=").append(caller).
               append(", lock=").append(toString(true));
         if (trace)
         {
            log.trace(buf.toString());
         }
         throw new TimeoutException(buf.toString(), e);
      }
   }

   public Set<NodeLock> acquireAll(Object caller, long timeout, LockType lock_type)
         throws LockingException, TimeoutException, InterruptedException
   {
      return acquireAll(caller, timeout, lock_type, false);
   }

   public Set<NodeLock> acquireAll(Object caller, long timeout, LockType lock_type, boolean excludeInternalFqns)
         throws LockingException, TimeoutException, InterruptedException
   {
      boolean acquired;

      if (lock_type == LockType.NONE || (excludeInternalFqns && node.getCache().getInternalFqns().contains(getFqn())))
      {
         return Collections.emptySet();
      }

      Set<NodeLock> retval = new HashSet<NodeLock>();
      acquired = acquire(caller, timeout, lock_type);
      if (acquired)
      {
         retval.add(this);
      }

      for (NodeSPI n : node.getChildrenDirect())
      {
         retval.addAll(n.getLock().acquireAll(caller, timeout, lock_type, excludeInternalFqns));
      }
      return retval;
   }

   public void releaseAll(Object owner)
   {
      for (NodeSPI n : node.getChildrenDirect())
      {
         n.getLock().releaseAll(owner);
      }
      release(owner);
   }

   private void printIndent(StringBuilder sb, int indent)
   {
      if (sb != null)
      {
         for (int i = 0; i < indent; i++)
         {
            sb.append(" ");
         }
      }
   }

   public void printLockInfo(StringBuilder sb, int indent)
   {
      boolean locked = isLocked();

      printIndent(sb, indent);
      sb.append(Fqn.SEPARATOR).append(node.getFqn().getLastElement());
      if (locked)
      {
         sb.append("\t(");
         toString(sb);
         sb.append(")");
      }

      for (NodeSPI n : node.getChildrenDirect())
      {
         sb.append("\n");
         n.getLock().printLockInfo(sb, indent + 4);
      }
   }

}
