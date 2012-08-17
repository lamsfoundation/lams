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
import org.jboss.cache.util.Immutables;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * An abstract transaction context
 */
public abstract class AbstractTransactionContext implements TransactionContext
{

   /**
    * Local transaction
    */
   private Transaction ltx = null;
   private Option option;
   private OrderedSynchronizationHandler orderedSynchronizationHandler;

   private boolean forceAsyncReplication = false;
   private boolean forceSyncReplication = false;

   /**
    * List&lt;ReversibleCommand&gt; of modifications ({@link org.jboss.cache.commands.WriteCommand}). They will be replicated on TX commit
    */
   private List<WriteCommand> modificationList;
   /**
    * A list of modifications that have been encountered with a LOCAL mode option.  These will be removed from the modification list during replication.
    */
   private List<WriteCommand> localModifications;

   /**
    * LinkedHashSet of locks acquired by the transaction. We use a LinkedHashSet because we need efficient Set semantics
    * but also need guaranteed ordering for use by lock release code (see JBCCACHE-874).
    * <p/>
    * This needs to be unchecked since we support both MVCC (Fqns held here) or legacy Opt/Pess locking (NodeLocks held here).
    * once we drop support for opt/pess locks we can genericise this to contain Fqns. - Manik Surtani, June 2008
    */
   private LinkedHashSet transactionLocks;

   /**
    * A list of dummy uninitialised nodes created by the cache loader interceptor to load data for a
    * given node in this tx.
    */
   private List<Fqn> dummyNodesCreatedByCacheLoader;

   /**
    * List<Fqn> of nodes that have been removed by the transaction
    */
   private List<Fqn> removedNodes = null;

   public AbstractTransactionContext(Transaction tx) throws SystemException, RollbackException
   {
      ltx = tx;
      orderedSynchronizationHandler = new OrderedSynchronizationHandler(tx);
   }

   public void addModification(WriteCommand command)
   {
      if (command == null) return;
      if (modificationList == null) modificationList = new LinkedList<WriteCommand>();
      modificationList.add(command);
   }

   public List<WriteCommand> getModifications()
   {
      if (modificationList == null) return Collections.emptyList();
      return modificationList;
   }

   public void addLocalModification(WriteCommand command)
   {
      if (command == null) throw new NullPointerException("Command is null!");
      if (localModifications == null) localModifications = new LinkedList<WriteCommand>();
      localModifications.add(command);
   }

   public List<WriteCommand> getLocalModifications()
   {
      if (localModifications == null) return Collections.emptyList();
      return localModifications;
   }


   public void addRemovedNode(Fqn fqn)
   {
      if (fqn == null) throw new NullPointerException("Fqn is null!");
      if (removedNodes == null) removedNodes = new LinkedList<Fqn>();
      removedNodes.add(fqn);
   }

   public List<Fqn> getRemovedNodes()
   {
      if (removedNodes == null) return Collections.emptyList();
      return new ArrayList<Fqn>(removedNodes);
   }

   public void setTransaction(Transaction tx)
   {
      ltx = tx;
   }

   public Transaction getTransaction()
   {
      return ltx;
   }

   @SuppressWarnings("unchecked")
   public void addLock(Object lock)
   {
      // no need to worry about concurrency here - a context is only valid for a single thread.
      if (transactionLocks == null) transactionLocks = new LinkedHashSet(5);
      transactionLocks.add(lock);
   }

   @SuppressWarnings("unchecked")
   public void removeLock(Object lock)
   {
      // no need to worry about concurrency here - a context is only valid for a single thread.
      if (transactionLocks != null) transactionLocks.remove(lock);
   }

   public void clearLocks()
   {
      if (transactionLocks != null) transactionLocks.clear();
   }

   public boolean hasLock(Object lock)
   {
      return transactionLocks != null && transactionLocks.contains(lock);
   }

   @SuppressWarnings("unchecked")
   public void addAllLocks(List newLocks)
   {
      // no need to worry about concurrency here - a context is only valid for a single thread.
      if (transactionLocks == null) transactionLocks = new LinkedHashSet(5);
      transactionLocks.addAll(newLocks);
   }

   @SuppressWarnings("unchecked")
   public List getLocks()
   {
      return transactionLocks == null || transactionLocks.isEmpty() ? Collections.emptyList() : Immutables.immutableListConvert(transactionLocks);
   }


   public boolean isForceAsyncReplication()
   {
      return forceAsyncReplication;
   }

   public void setForceAsyncReplication(boolean forceAsyncReplication)
   {
      this.forceAsyncReplication = forceAsyncReplication;
      if (forceAsyncReplication)
      {
         forceSyncReplication = false;
      }
   }

   public boolean isForceSyncReplication()
   {
      return forceSyncReplication;
   }

   public void setForceSyncReplication(boolean forceSyncReplication)
   {
      this.forceSyncReplication = forceSyncReplication;
      if (forceSyncReplication)
      {
         forceAsyncReplication = false;
      }
   }

   /**
    * Returns debug information about this transaction.
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("TransactionEntry\nmodificationList: ").append(modificationList);
      return sb.toString();
   }

   public void addDummyNodeCreatedByCacheLoader(Fqn fqn)
   {
      if (dummyNodesCreatedByCacheLoader == null)
         dummyNodesCreatedByCacheLoader = new LinkedList<Fqn>();
      dummyNodesCreatedByCacheLoader.add(fqn);
   }

   public List<Fqn> getDummyNodesCreatedByCacheLoader()
   {
      if (dummyNodesCreatedByCacheLoader == null) return Collections.emptyList();
      return dummyNodesCreatedByCacheLoader;
   }

   public void setOption(Option o)
   {
      this.option = o;
   }

   public Option getOption()
   {
      return this.option;
   }

   public OrderedSynchronizationHandler getOrderedSynchronizationHandler()
   {
      return orderedSynchronizationHandler;
   }

   public void setOrderedSynchronizationHandler(OrderedSynchronizationHandler orderedSynchronizationHandler)
   {
      this.orderedSynchronizationHandler = orderedSynchronizationHandler;
   }

   public boolean hasModifications()
   {
      return modificationList != null && !modificationList.isEmpty();
   }

   public boolean hasLocalModifications()
   {
      return localModifications != null && !localModifications.isEmpty();
   }

   public boolean hasAnyModifications()
   {
      return hasModifications() || hasLocalModifications();
   }

   public void reset()
   {
      orderedSynchronizationHandler = null;
      modificationList = null;
      localModifications = null;
      option = null;
      if (transactionLocks != null) transactionLocks.clear();
      if (dummyNodesCreatedByCacheLoader != null) dummyNodesCreatedByCacheLoader.clear();
      if (removedNodes != null) removedNodes.clear();
   }
}
