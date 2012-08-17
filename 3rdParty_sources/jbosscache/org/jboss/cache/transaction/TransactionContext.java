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
package org.jboss.cache.transaction;

import org.jboss.cache.Fqn;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.config.Option;
import org.jboss.cache.interceptors.OrderedSynchronizationHandler;

import javax.transaction.Transaction;
import java.util.List;

/**
 * Captures information pertaining to a specific JTA transaction.
 * <p/>
 * This was a concrete class called TransactionEntry prior to 3.0.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @see org.jboss.cache.InvocationContext
 */
public interface TransactionContext
{
   /**
    * Adds a modification to the modification list.
    *
    * @param command modification
    */
   void addModification(WriteCommand command);

   /**
    * Returns all modifications.  If there are no modifications in this transaction this method will return an empty list.
    *
    * @return list of modifications.
    */
   List<WriteCommand> getModifications();

   /**
    * Adds a modification to the local modification list.
    *
    * @param command command to add to list.  Should not be null.
    * @throws NullPointerException if the command to be added is null.
    */
   void addLocalModification(WriteCommand command);

   /**
    * Returns all modifications that have been invoked with the LOCAL cache mode option.  These will also be in the standard modification list.
    *
    * @return list of LOCAL modifications, or an empty list.
    */
   List<WriteCommand> getLocalModifications();

   /**
    * Adds the node that has been removed in the scope of the current transaction.
    *
    * @param fqn fqn that has been removed.
    * @throws NullPointerException if the Fqn is null.
    */
   void addRemovedNode(Fqn fqn);

   /**
    * Gets the list of removed nodes.
    *
    * @return list of nodes removed in the current transaction scope.  Note that this method will return an empty list if nothing has been removed.  The list returned is defensively copied.
    */
   List<Fqn> getRemovedNodes();

   /**
    * Sets the local transaction to be associated with this transaction context.
    *
    * @param tx JTA transaction to associate with.
    */
   void setTransaction(Transaction tx);

   /**
    * Returns a local transaction associated with this context.
    *
    * @return a JTA transaction
    */
   Transaction getTransaction();

   /**
    * Adds a lock to the currently maintained collection of locks acquired.
    * <p/>
    * Most code could not use this method directly, but use {@link org.jboss.cache.InvocationContext#addLock(Object)} instead,
    * which would delegate to this method if a transaction is in scope or otherwise use invocation-specific locks.
    * <p/>
    * Note that currently (as of 3.0.0) this lock is weakly typed.  This is to allow support for both MVCC (which uses {@link org.jboss.cache.Fqn}s as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to accept {@link org.jboss.cache.Fqn}.
    *
    * @param lock lock to add
    * @see org.jboss.cache.InvocationContext#addLock(Object)
    */
   @SuppressWarnings("unchecked")
   void addLock(Object lock);

   /**
    * Removes a lock from the currently maintained collection of locks acquired.
    * <p/>
    * Most code could not use this method directly, but use {@link org.jboss.cache.InvocationContext#removeLock(Object)}  instead,
    * which would delegate to this method if a transaction is in scope or otherwise use invocation-specific locks.
    * <p/>
    * Note that currently (as of 3.0.0) this lock is weakly typed.  This is to allow support for both MVCC (which uses {@link org.jboss.cache.Fqn}s as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to accept {@link org.jboss.cache.Fqn}.
    *
    * @param lock lock to remove
    * @see org.jboss.cache.InvocationContext#removeLock(Object)
    */
   @SuppressWarnings("unchecked")
   void removeLock(Object lock);

   /**
    * Clears all locks from the currently maintained collection of locks acquired.
    * <p/>
    * Most code could not use this method directly, but use {@link org.jboss.cache.InvocationContext#clearLocks()} instead,
    * which would delegate to this method if a transaction is in scope or otherwise use invocation-specific locks.
    * <p/>
    * Note that currently (as of 3.0.0) this lock is weakly typed.  This is to allow support for both MVCC (which uses {@link org.jboss.cache.Fqn}s as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to accept {@link org.jboss.cache.Fqn}.
    *
    * @see org.jboss.cache.InvocationContext#clearLocks()
    */
   void clearLocks();

   /**
    * Adds a List of locks to the currently maintained collection of locks acquired.
    * <p/>
    * Most code could not use this method directly, but use {@link org.jboss.cache.InvocationContext#addAllLocks(java.util.List)} instead,
    * which would delegate to this method if a transaction is in scope or otherwise use invocation-specific locks.
    * <p/>
    * Note that currently (as of 3.0.0) this list is unchecked.  This is to allow support for both MVCC (which uses Fqns as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to accept <tt>List<Fqn></tt>.
    *
    * @param newLocks locks to add
    * @see org.jboss.cache.InvocationContext#addAllLocks(java.util.List)
    */
   @SuppressWarnings("unchecked")
   void addAllLocks(List newLocks);

   /**
    * Returns an immutable,  defensive copy of the List of locks currently maintained for the transaction.
    * <p/>
    * Most code could not use this method directly, but use {@link org.jboss.cache.InvocationContext#getLocks()} instead,
    * which would delegate to this method if a transaction is in scope or otherwise use invocation-specific locks.
    * <p/>
    * Note that currently (as of 3.0.0) this list is unchecked.  This is to allow support for both MVCC (which uses Fqns as locks)
    * as well as legacy Optimistic and Pessimistic Locking schemes (which use {@link org.jboss.cache.lock.NodeLock} as locks).  Once support for
    * legacy node locking schemes are dropped, this method will be more strongly typed to return <tt>List<Fqn></tt>.
    *
    * @return locks held in current scope.
    * @see org.jboss.cache.InvocationContext#getLocks()
    */
   @SuppressWarnings("unchecked")
   List getLocks();

   /**
    * Most code could not use this method directly, but use {@link org.jboss.cache.InvocationContext#hasLock(Object)} ()} instead,
    * which would delegate to this method if a transaction is in scope or otherwise use invocation-specific locks.
    *
    * @param lock lock to test
    * @return true if the lock being tested is already held in the current scope, false otherwise.
    */
   boolean hasLock(Object lock);

   /**
    * Gets the value of the forceAsyncReplication flag.  Used by ReplicationInterceptor and OptimisticReplicationInterceptor
    * when dealing with {@link org.jboss.cache.Cache#putForExternalRead(org.jboss.cache.Fqn,Object,Object)} within
    * a transactional context.
    *
    * @return true if the forceAsyncReplication flag is set to true.
    */
   boolean isForceAsyncReplication();

   /**
    * Sets the value of the forceAsyncReplication flag.  Used by ReplicationInterceptor and OptimisticReplicationInterceptor
    * when dealing with {@link org.jboss.cache.Cache#putForExternalRead(org.jboss.cache.Fqn,Object,Object)} within
    * a transactional context. Also used by OptimisticReplicationInterceptor when dealing
    * with {@link org.jboss.cache.config.Option#setForceAsynchronous(boolean)} in a
    * non-transactional context (i.e. with an implicit transaction).
    *
    * @param forceAsyncReplication value of forceAsyncReplication
    */
   void setForceAsyncReplication(boolean forceAsyncReplication);

   /**
    * Gets the value of the forceSyncReplication flag.  Used by ReplicationInterceptor and OptimisticReplicationInterceptor
    * when dealing with {@link org.jboss.cache.Cache#putForExternalRead(org.jboss.cache.Fqn,Object,Object)} within
    * a transactional context.
    *
    * @return true if the forceAsyncReplication flag is set to true.
    */
   boolean isForceSyncReplication();

   /**
    * Sets the value of the forceSyncReplication flag.  Used by ReplicationInterceptor and OptimisticReplicationInterceptor
    * when dealing with {@link org.jboss.cache.Cache#putForExternalRead(org.jboss.cache.Fqn,Object,Object)} within
    * a transactional context.
    *
    * @param forceSyncReplication value of forceSyncReplication
    */
   void setForceSyncReplication(boolean forceSyncReplication);

   /**
    * Adds an Fqn to the list of uninitialized nodes created by the cache loader.
    *
    * @param fqn fqn to add.  Must not be null.
    */
   void addDummyNodeCreatedByCacheLoader(Fqn fqn);

   /**
    * @return a list of uninitialized nodes created by the cache loader, or an empty list.
    */
   List<Fqn> getDummyNodesCreatedByCacheLoader();

   /**
    * Sets a transaction-scope option override
    *
    * @param o option to set
    */
   void setOption(Option o);

   /**
    * Retrieves a transaction scope option override
    *
    * @return option
    */
   Option getOption();

   /**
    * @return the ordered sync handler associated with this transaction
    */
   OrderedSynchronizationHandler getOrderedSynchronizationHandler();

   /**
    * Associates an ordered sync handler with this transaction.
    *
    * @param orderedSynchronizationHandler to set
    */
   void setOrderedSynchronizationHandler(OrderedSynchronizationHandler orderedSynchronizationHandler);

   /**
    * @return true if modifications were registered.
    */
   boolean hasModifications();

   /**
    * @return true if any modifications have been invoked with cache mode being LOCAL.
    */
   boolean hasLocalModifications();

   /**
    * @return true if either there are modifications or local modifications that are not for replicating.
    */
   boolean hasAnyModifications();

   /**
    * Cleans up internal state, freeing up references.
    */
   void reset();
}
