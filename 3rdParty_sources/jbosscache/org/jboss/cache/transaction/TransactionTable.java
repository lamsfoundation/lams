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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.RPCManager;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.factories.context.ContextFactory;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jgroups.Address;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maintains the mapping between a local {@link Transaction} and a {@link GlobalTransaction}.
 * Also stores {@link TransactionContext} instances under a given transaction.
 *
 * @author <a href="mailto:bela@jboss.org">Bela Ban</a> Apr 14, 2003
 * @version $Revision$
 */
@NonVolatile
public class TransactionTable
{
   private static final Log log = LogFactory.getLog(TransactionTable.class);
   private static final boolean trace = log.isTraceEnabled();

   /**
    * Mapping between local (javax.transaction.Transaction)
    * and GlobalTransactions.
    * New: a local TX can have a number of GTXs
    */
   protected final Map<Transaction, GlobalTransaction> tx2gtxMap = new ConcurrentHashMap<Transaction, GlobalTransaction>();

   /**
    * Mappings between GlobalTransactions and modifications.
    */
   protected final Map<GlobalTransaction, TransactionContext> gtx2ContextMap = new ConcurrentHashMap<GlobalTransaction, TransactionContext>();

   protected final Map<GlobalTransaction, Transaction> gtx2TxMap = new ConcurrentHashMap<GlobalTransaction, Transaction>();

   private TransactionManager transactionManager = null;

   private RPCManager rpcManager;

   private ContextFactory contextFactory;

   @Inject
   public void initialize(TransactionManager transactionManager, RPCManager rpcManager, ContextFactory contextFactory)
   {
      this.transactionManager = transactionManager;
      this.rpcManager = rpcManager;
      this.contextFactory = contextFactory;
   }

   /**
    * Returns the number of local transactions.
    */
   public int getNumLocalTransactions()
   {
      return tx2gtxMap.size();
   }

   /**
    * Returns the number of global transactions.
    */
   public int getNumGlobalTransactions()
   {
      return gtx2ContextMap.size();
   }

   /**
    * Returns the global transaction associated with the local transaction.
    * Returns null if tx is null or it was not found.
    */
   public GlobalTransaction get(Transaction tx)
   {
      if (tx == null)
      {
         return null;
      }
      return tx2gtxMap.get(tx);
   }

   /**
    * Returns the local transaction associated with a GlobalTransaction.
    *
    * @param gtx The GlobalTransaction
    * @return Transaction. The local transaction associated with a given
    *         GlobalTransaction). This will be null if no local transaction is
    *         associated with a given GTX
    */
   public Transaction getLocalTransaction(GlobalTransaction gtx)
   {
      if (gtx == null) return null;
      return gtx2TxMap.get(gtx);
   }

   /**
    * If assers exists is true and the coresponding local transaction is null an IllegalStateExcetpion is being thrown.
    */
   public Transaction getLocalTransaction(GlobalTransaction gtx, boolean assertExists)
   {
      Transaction ltx = getLocalTransaction(gtx);
      if (!assertExists)
      {
         return ltx;
      }
      if (ltx != null)
      {
         if (log.isDebugEnabled()) log.debug("Found local TX=" + ltx + ", global TX=" + gtx);
         return ltx;
      }
      else
      {
         throw new IllegalStateException(" found no local TX for global TX " + gtx);
      }
   }

   /**
    * Associates the global transaction with the local transaction.
    */
   public void put(Transaction tx, GlobalTransaction gtx)
   {
      if (tx == null)
      {
         log.error("key (Transaction) is null");
         return;
      }
      tx2gtxMap.put(tx, gtx);
      gtx2TxMap.put(gtx, tx);
   }

   /**
    * Returns the local transaction entry for the global transaction.
    * Returns null if tx is null or it was not found.
    */
   public TransactionContext get(GlobalTransaction gtx)
   {
      return gtx != null ? gtx2ContextMap.get(gtx) : null;
   }

   /**
    * Associates the global transaction with a transaction context.
    */
   public void put(GlobalTransaction tx, TransactionContext transactionContext)
   {
      if (tx == null)
      {
         log.error("key (GlobalTransaction) is null");
         return;
      }
      gtx2ContextMap.put(tx, transactionContext);
   }

   /**
    * Removes a global transation, returns the old transaction entry.
    */
   public TransactionContext remove(GlobalTransaction tx)
   {
      if (tx == null) return null;
      gtx2TxMap.remove(tx);
      return gtx2ContextMap.remove(tx);
   }

   /**
    * Removes a local transation, returns the global transaction entry.
    */
   public GlobalTransaction remove(Transaction tx)
   {
      if (tx == null)
      {
         return null;
      }
      return tx2gtxMap.remove(tx);
   }

   public void remove(GlobalTransaction gtx, Transaction tx)
   {
      gtx2TxMap.remove(gtx);
      gtx2ContextMap.remove(gtx);
      tx2gtxMap.remove(tx);
   }

   /**
    * Returns summary debug information.
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append(tx2gtxMap.size()).append(" mappings, ");
      sb.append(gtx2ContextMap.size()).append(" transactions");
      return sb.toString();
   }

   /**
    * Returns detailed debug information.
    */
   public String toString(boolean printDetails)
   {
      if (!printDetails)
      {
         return toString();
      }
      StringBuilder sb = new StringBuilder();
      sb.append("LocalTransactions: ").append(tx2gtxMap.size()).append("\n");
      sb.append("GlobalTransactions: ").append(gtx2ContextMap.size()).append("\n");
      sb.append("tx2gtxMap:\n");
      for (Map.Entry<Transaction, GlobalTransaction> entry : tx2gtxMap.entrySet())
      {
         sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
      }
      sb.append("gtx2EntryMap:\n");
      for (Map.Entry<GlobalTransaction, TransactionContext> transactionContextEntry : gtx2ContextMap.entrySet())
      {
         sb.append(transactionContextEntry.getKey()).append(": ").append(transactionContextEntry.getValue()).append("\n");
      }
      return sb.toString();
   }

   /**
    * Returns the transaction associated with the current thread.
    * If a local transaction exists, but doesn't yet have a mapping to a
    * GlobalTransaction, a new GlobalTransaction will be created and mapped to
    * the local transaction.  Note that if a local transaction exists, but is
    * not ACTIVE or PREPARING, null is returned.
    *
    * @return A GlobalTransaction, or null if no (local) transaction was associated with the current thread
    */
   public GlobalTransaction getCurrentTransaction()
   {
      return getCurrentTransaction(true);
   }


   /**
    * Returns the transaction associated with the thread; optionally creating
    * it if is does not exist.
    */
   public GlobalTransaction getCurrentTransaction(boolean createIfNotExists)
   {
      Transaction tx;

      if ((tx = getLocalTransaction()) == null)
      {// no transaction is associated with the current thread
         return null;
      }

      if (!isValid(tx))
      {// we got a non-null transaction, but it is not active anymore
         int status = -1;
         try
         {
            status = tx.getStatus();
         }
         catch (SystemException e)
         {
         }

         // JBCACHE-982 -- don't complain if COMMITTED
         if (status != Status.STATUS_COMMITTED)
         {
            log.warn("status is " + status + " (not ACTIVE or PREPARING); returning null)");
         }
         else
         {
            log.trace("status is COMMITTED; returning null");
         }

         return null;
      }

      return getCurrentTransaction(tx, createIfNotExists);
   }

   /**
    * Returns the transaction associated with the current thread. We get the
    * initial context and a reference to the TransactionManager to get the
    * transaction. This method is used by {@link #getCurrentTransaction()}
    */
   protected Transaction getLocalTransaction()
   {
      if (transactionManager == null)
      {
         return null;
      }
      try
      {
         return transactionManager.getTransaction();
      }
      catch (Throwable t)
      {
         return null;
      }
   }

   /**
    * Returns true if transaction is ACTIVE, false otherwise
    */
   public static boolean isActive(Transaction tx)
   {
      if (tx == null) return false;
      int status;
      try
      {
         status = tx.getStatus();
         return status == Status.STATUS_ACTIVE;
      }
      catch (SystemException e)
      {
         return false;
      }
   }

   /**
    * Returns true if transaction is PREPARING, false otherwise
    */
   public static boolean isPreparing(Transaction tx)
   {
      if (tx == null) return false;
      int status;
      try
      {
         status = tx.getStatus();
         return status == Status.STATUS_PREPARING;
      }
      catch (SystemException e)
      {
         return false;
      }
   }

   /**
    * Returns true if transaction is STATUS_MARKED_ROLLBACK, false otherwise
    */
   public static boolean isMarkedAsRollback(Transaction tx)
   {
      if (tx == null) return false;
      int status;
      try
      {
         status = tx.getStatus();
         return status == Status.STATUS_MARKED_ROLLBACK;
      }
      catch (SystemException e)
      {
         return false;
      }
   }


   /**
    * Returns true of tx's status is ACTIVE or PREPARING or MARKED_ROLLBACK
    *
    * @param tx
    * @return true if the tx is active or preparing
    */
   public static boolean isValid(Transaction tx)
   {
      return isActive(tx) || isPreparing(tx) || isMarkedAsRollback(tx);
   }

   /**
    * Tests whether the caller is in a valid transaction.  If not, will throw a CacheException.
    */
   public static void assertTransactionValid(InvocationContext ctx)
   {
      Transaction tx = ctx.getTransaction();
      if (!isValid(tx))
      {
         try
         {
            throw new CacheException("Invalid transaction " + tx + ", status = " + (tx == null ? null : tx.getStatus()));
         }
         catch (SystemException e)
         {
            throw new CacheException("Exception trying to analyse status of transaction " + tx, e);
         }
      }
   }


   /**
    * Returns the global transaction for this local transaction.
    */
   public GlobalTransaction getCurrentTransaction(Transaction tx)
   {
      return getCurrentTransaction(tx, true);
   }

   /**
    * Returns the global transaction for this local transaction.
    *
    * @param createIfNotExists if true, if a global transaction is not found; one is created
    */
   public GlobalTransaction getCurrentTransaction(Transaction tx, boolean createIfNotExists)
   {
      // removed synchronization on txTable because underlying implementation is thread safe
      // and JTA spec (section 3.4.3 Thread of Control, par 2) says that only one thread may
      // operate on the transaction at one time so no concern about 2 threads trying to call
      // this method for the same Transaction instance at the same time
      //
      GlobalTransaction gtx = get(tx);
      if (gtx == null && createIfNotExists)
      {
         Address addr = rpcManager.getLocalAddress();
         gtx = GlobalTransaction.create(addr);
         put(tx, gtx);
         TransactionContext transactionContext;
         try
         {
            transactionContext = contextFactory.createTransactionContext(tx);
         }
         catch (Exception e)
         {
            throw new CacheException("Unable to create a transaction entry!", e);
         }

         put(gtx, transactionContext);
         if (trace)
         {
            log.trace("created new GTX: " + gtx + ", local TX=" + tx);
         }
      }
      return gtx;
   }

   @ManagedAttribute(name = "numberOfRegisteredTransactions", description = "Number of registered transactions")
   public int getNumberOfRegisteredTransactions()
   {
      return tx2gtxMap.size();
   }

   @ManagedAttribute(name = "transactionMap", description = "A String representation of the transaction map")
   public String getTransactionMap()
   {
      return tx2gtxMap.toString();
   }
}
