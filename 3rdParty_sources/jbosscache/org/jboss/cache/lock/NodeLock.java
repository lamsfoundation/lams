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

import org.jboss.cache.Fqn;

import java.util.Collection;
import java.util.Set;


/**
 * Interface for a lock for nodes.
 * <p/>
 *
 * @deprecated will be removed when we drop support for Pessimistic Locking and Optimistic Locking
 */
@Deprecated
public interface NodeLock
{

   Fqn getFqn();

   /**
    * Returns a copy of the reader lock owner in List.
    * Size is zero is not available. Note that this list
    * is synchronized.
    *
    * @return Collection<Object> of readers
    */
   Collection<Object> getReaderOwners();

   /**
    * Returns the writer lock owner object. Null if not available.
    *
    * @return Object owner
    */
   Object getWriterOwner();

   /**
    * Acquires a write lock with a timeout of <code>timeout</code> milliseconds.
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
   boolean acquireWriteLock(Object caller, long timeout) throws LockingException, TimeoutException,
         InterruptedException;

   /**
    * Acquires a read lock with a timeout period of <code>timeout</code> milliseconds.
    *
    * @param caller  Can't be null.
    * @param timeout
    * @return boolean True if lock was acquired and was not held before, false if lock was held
    * @throws LockingException
    * @throws TimeoutException
    */
   boolean acquireReadLock(Object caller, long timeout) throws LockingException, TimeoutException, InterruptedException;

   /**
    * Releases the lock held by the owner.
    *
    * @param caller Can't be null.
    */
   void release(Object caller);

   /**
    * Releases all locks associated with this instance.
    */
   void releaseAll();

   /**
    * Releases all locks with this owner.
    */
   void releaseAll(Object owner);

   /**
    * Check if there is a read lock.
    */
   boolean isReadLocked();

   /**
    * Check if there is a write lock.
    */
   boolean isWriteLocked();

   /**
    * Check if there is a read or write lock
    */
   boolean isLocked();

   /**
    * Returns true if the object is the lock owner.
    */
   boolean isOwner(Object o);

   boolean acquire(Object caller, long timeout, LockType lock_type) throws LockingException, TimeoutException,
         InterruptedException;

   /**
    * Recursively acquire locks for this node and all subnodes, including internal Fqns such as buddy backup subtrees.
    *
    * @param caller    lock owner
    * @param timeout   time to wait
    * @param lock_type type of lock
    * @return locks acquired
    */
   Set<NodeLock> acquireAll(Object caller, long timeout, LockType lock_type) throws LockingException, TimeoutException,
         InterruptedException;

   /**
    * Same as the overloaded {@link #acquire(Object, long, LockType)} except that you can
    * optionally specify that internal Fqns - such as buddy backup subtrees - can be excluded when acquiring locks.
    *
    * @param caller              lock owner
    * @param timeout             time to wait
    * @param lock_type           type of lock
    * @param excludeInternalFqns if true, locks on internal fqns are not acquired.
    * @return locks acquired
    */
   Set<NodeLock> acquireAll(Object caller, long timeout, LockType lock_type, boolean excludeInternalFqns) throws LockingException, TimeoutException,
         InterruptedException;

   void printLockInfo(StringBuilder sb, int indent);

}