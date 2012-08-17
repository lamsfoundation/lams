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
import org.jboss.cache.InvocationContext;
import org.jboss.cache.NodeSPI;

import java.util.Collection;

/**
 * An interface to deal with all aspects of acquiring and releasing locks for nodes in the cache.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
public interface LockManager
{
   /**
    * Determines the owner to be used when obtaining locks, given an invocation context.  This is typically a {@link org.jboss.cache.transaction.GlobalTransaction} if one
    * is present in the context, or {@link Thread#currentThread()} if one is not present.
    *
    * @param ctx invocation context
    * @return owner to be used for acquiring locks.
    */
   Object getLockOwner(InvocationContext ctx);

   /**
    * Acquires a lock of type lockType, for a given owner, on a specific Node in the cache, denoted by fqn.  This
    * method will try for {@link org.jboss.cache.config.Configuration#getLockAcquisitionTimeout()} milliseconds and give up if it is unable to acquire the required lock.
    *
    * @param fqn      Fqn to lock
    * @param lockType type of lock to acquire
    * @param owner    owner to acquire the lock for
    * @return true if the lock was acquired, false otherwise.
    */
   boolean lock(Fqn fqn, LockType lockType, Object owner) throws InterruptedException;

   /**
    * Acquires a lock of type lockType, for a given owner, on a specific Node in the cache, denoted by fqn.  This
    * method will try for timeout milliseconds and give up if it is unable to acquire the required lock.
    *
    * @param fqn      Fqn to lock
    * @param lockType type of lock to acquire
    * @param owner    owner to acquire the lock for
    * @param timeout  maximum length of time to wait for (in millis)
    * @return true if the lock was acquired, false otherwise.
    */
   boolean lock(Fqn fqn, LockType lockType, Object owner, long timeout) throws InterruptedException;

   /**
    * As {@link #lock(org.jboss.cache.Fqn, LockType, Object)} except that a NodeSPI is passed in instead of an Fqn.
    *
    * @param node     node to lock
    * @param lockType type of lock to acquire
    * @param owner    owner to acquire the lock for
    * @return true if the lock was acquired, false otherwise.
    */
   boolean lock(NodeSPI<?, ?> node, LockType lockType, Object owner) throws InterruptedException;

   /**
    * As {@link #lock(org.jboss.cache.Fqn, LockType, Object, long)} except that a NodeSPI is passed in instead of an Fqn.
    *
    * @param node     node to lock
    * @param lockType type of lock to acquire
    * @param owner    owner to acquire the lock for
    * @param timeout  maximum length of time to wait for (in millis)
    * @return true if the lock was acquired, false otherwise.
    */
   boolean lock(NodeSPI<?, ?> node, LockType lockType, Object owner, long timeout) throws InterruptedException;

   /**
    * Acquires a lock of type lockType, on a specific Node in the cache, denoted by fqn.  This
    * method will try for a period of time and give up if it is unable to acquire the required lock.  The period of time
    * is specified in {@link org.jboss.cache.config.Option#getLockAcquisitionTimeout()} and, if this is unset, the default timeout
    * set in {@link org.jboss.cache.config.Configuration#getLockAcquisitionTimeout()} is used.
    * <p/>
    * In addition, any locks acquired are added to the context OR transaction entry using {@link org.jboss.cache.InvocationContext#addLock(Object)}.
    * <p/>
    * The owner for the lock is determined by passing the invocation context to {@link #getLockOwner(org.jboss.cache.InvocationContext)}.
    * <p/>
    *
    * @param fqn      Fqn to lock
    * @param lockType type of lock to acquire
    * @param ctx      invocation context associated with this invocation
    * @return true if the lock was acquired, false otherwise.
    */
   boolean lockAndRecord(Fqn fqn, LockType lockType, InvocationContext ctx) throws InterruptedException;

   /**
    * Acquires a lock of type lockType, on a specific Node in the cache, denoted by fqn.  This
    * method will try for a period of time and give up if it is unable to acquire the required lock.  The period of time
    * is specified in {@link org.jboss.cache.config.Option#getLockAcquisitionTimeout()} and, if this is unset, the default timeout
    * set in {@link org.jboss.cache.config.Configuration#getLockAcquisitionTimeout()} is used.
    * <p/>
    * In addition, any locks acquired are added to the context OR transaction entry using {@link org.jboss.cache.InvocationContext#addLock(Object)}.
    * <p/>
    * The owner for the lock is determined by passing the invocation context to {@link #getLockOwner(org.jboss.cache.InvocationContext)}.
    * <p/>
    *
    * @param node     Fqn to lock
    * @param lockType type of lock to acquire
    * @param ctx      invocation context associated with this invocation
    * @return true if the lock was acquired, false otherwise.
    */
   boolean lockAndRecord(NodeSPI<?, ?> node, LockType lockType, InvocationContext ctx) throws InterruptedException;


   /**
    * Releases the lock passed in, held by the specified owner
    *
    * @param fqn   Fqn of the node to unlock
    * @param owner lock owner
    */
   void unlock(Fqn fqn, Object owner);

   /**
    * Releases the lock passed in, held by the specified owner
    *
    * @param node  Node to unlock
    * @param owner lock owner
    */
   void unlock(NodeSPI<?, ?> node, Object owner);

   /**
    * Releases locks present in an invocation context and transaction entry, if one is available.
    * <p/>
    * Locks are released in reverse order of which they are acquired and registered.
    * <p/>
    * Lock owner is determined by passing the invocation context to {@link #getLockOwner(org.jboss.cache.InvocationContext)}
    * <p/>
    *
    * @param ctx invocation context to inspect
    */
   void unlock(InvocationContext ctx);


   /**
    * Locks the node and all child nodes, acquiring lock of type specified for the owner specified.  If only some locks are
    * acquired, all locks are released and the method returns false.
    * <p/>
    * This method will try for {@link org.jboss.cache.config.Configuration#getLockAcquisitionTimeout()} milliseconds and give up if it is unable to acquire the required lock.
    * <p/>
    *
    * @param node     Node to lock
    * @param lockType type of lock to acquire
    * @param owner    owner to acquire the lock for
    * @return true if the lock was acquired, false otherwise.
    */
   boolean lockAll(NodeSPI<?, ?> node, LockType lockType, Object owner) throws InterruptedException;

   /**
    * Locks the node and all child nodes, acquiring lock of type specified for the owner specified.  If only some locks are
    * acquired, all locks are released and the method returns false.  Internal Fqns are included as well.
    *
    * @param node     Node to lock
    * @param lockType type of lock to acquire
    * @param owner    owner to acquire the lock for
    * @param timeout  maximum length of time to wait for (in millis)
    * @return true if all locks were acquired, false otherwise.
    */
   boolean lockAll(NodeSPI<?, ?> node, LockType lockType, Object owner, long timeout) throws InterruptedException;

   /**
    * Locks the node and all child nodes, acquiring lock of type specified for the owner specified.  If only some locks are
    * acquired, all locks are released and the method returns false.
    *
    * @param node                Node to lock
    * @param lockType            type of lock to acquire
    * @param owner               owner to acquire the lock for
    * @param timeout             maximum length of time to wait for (in millis)
    * @param excludeInternalFqns if true, any Fqns that are internal are excluded.
    * @return true if all locks were acquired, false otherwise.
    */
   boolean lockAll(NodeSPI<?, ?> node, LockType lockType, Object owner, long timeout, boolean excludeInternalFqns) throws InterruptedException;

   /**
    * Locks the node and all child nodes, acquiring lock of type specified for the owner specified.  If only some locks are
    * acquired, all locks are released and the method returns false.
    * <p/>
    * In addition, any locks acquired are added to the context OR transaction entry using {@link org.jboss.cache.InvocationContext#addLock(Object)}.
    * <p/>
    * The owner for the lock is determined by passing the invocation context to {@link #getLockOwner(org.jboss.cache.InvocationContext)}.
    * <p/>
    *
    * @param node     Node to lock
    * @param lockType type of lock to acquire
    * @param ctx      invocation context associated with this invocation
    * @return true if all locks were acquired, false otherwise.
    */
   boolean lockAllAndRecord(NodeSPI<?, ?> node, LockType lockType, InvocationContext ctx) throws InterruptedException;

   /**
    * Locks the node and all child nodes, acquiring lock of type specified for the owner specified.  If only some locks are
    * acquired, all locks are released and the method returns false.
    * <p/>
    * In addition, any locks acquired are added to the context OR transaction entry using {@link org.jboss.cache.InvocationContext#addLock(Object)}.
    * <p/>
    * The owner for the lock is determined by passing the invocation context to {@link #getLockOwner(org.jboss.cache.InvocationContext)}.
    * <p/>
    *
    * @param fqn      Node to lock
    * @param lockType type of lock to acquire
    * @param ctx      invocation context associated with this invocation
    * @return true if all locks were acquired, false otherwise.
    */
   boolean lockAllAndRecord(Fqn fqn, LockType lockType, InvocationContext ctx) throws InterruptedException;

   /**
    * Releases locks on a given node and all children for a given owner.
    *
    * @param node  node to unlock
    * @param owner lock owner
    */
   void unlockAll(NodeSPI<?, ?> node, Object owner);

   /**
    * Releases locks on a given node and all children for all owners.  Use with care.
    *
    * @param node node to unlock
    */
   void unlockAll(NodeSPI<?, ?> node);

   /**
    * Tests whether a given owner owns a lock of lockType on a particular Fqn.
    *
    * @param fqn      fqn to test
    * @param lockType type of lock to test for
    * @param owner    owner
    * @return true if the owner does own the specified lock type on the specified node, false otherwise.
    */
   boolean ownsLock(Fqn fqn, LockType lockType, Object owner);

   /**
    * Tests whether a given owner owns any sort of lock on a particular Fqn.
    *
    * @param fqn   fqn to test
    * @param owner owner
    * @return true if the owner does own the specified lock type on the specified node, false otherwise.
    */
   boolean ownsLock(Fqn fqn, Object owner);

   /**
    * Tests whether a given owner owns any sort of lock on a particular Fqn.
    *
    * @param node  to test
    * @param owner owner
    * @return true if the owner does own the specified lock type on the specified node, false otherwise.
    */
   boolean ownsLock(NodeSPI<?, ?> node, Object owner);

   /**
    * Returns true if the node is locked (either for reading or writing) by anyone, and false otherwise.
    *
    * @param n node to inspect
    * @return true of locked; false if not.
    */
   boolean isLocked(NodeSPI<?, ?> n);

   /**
    * Returns true if the node is locked (either for reading or writing) by anyone, and false otherwise.
    *
    * @param fqn node to inspect
    * @return true of locked; false if not.
    */
   boolean isLocked(Fqn fqn);

   /**
    * Returns true if the node is locked (either for reading or writing) by anyone, and false otherwise.
    *
    * @param n        node to inspect
    * @param lockType lockType to test for
    * @return true of locked; false if not.
    */
   boolean isLocked(NodeSPI<?, ?> n, LockType lockType);

   /**
    * Retrieves the write lock owner, if any, for the current Fqn.
    *
    * @param f Fqn to inspect
    * @return the owner of the lock, or null if not locked.
    */
   Object getWriteOwner(Fqn f);

   /**
    * Retrieves the read lock owners, if any, for the current Fqn.
    *
    * @param f Fqn to inspect
    * @return a collection of read lock owners, or an empty collection if not locked.
    */
   Collection<Object> getReadOwners(Fqn f);

   /**
    * Retrieves the write lock owner, if any, for the current Fqn.
    *
    * @param node the node to inspect
    * @return the owner of the lock, or null if not locked.
    */
   Object getWriteOwner(NodeSPI<?, ?> node);

   /**
    * Retrieves the read lock owners, if any, for the current Fqn.
    *
    * @param node the node to inspect
    * @return a collection of read lock owners, or an empty collection if not locked.
    */
   Collection<Object> getReadOwners(NodeSPI<?, ?> node);

   /**
    * Prints lock information about a node (and its children) to a String.
    *
    * @param node node to inspect
    */
   String printLockInfo(NodeSPI<?, ?> node);

   /**
    * Prints lock information for all locks.
    *
    * @return lock information
    */
   String printLockInfo();
}
