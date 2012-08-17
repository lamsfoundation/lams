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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.InvalidTransactionException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.jboss.cache.CacheException;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.RPCManager;
import org.jboss.cache.ReplicationException;
import org.jboss.cache.commands.AbstractVisitor;
import org.jboss.cache.commands.CommandsFactory;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.InvalidateCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.config.Option;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.context.ContextFactory;
import org.jboss.cache.invocation.InvocationContextContainer;
import org.jboss.cache.jmx.annotations.ManagedAttribute;
import org.jboss.cache.jmx.annotations.ManagedOperation;
import org.jboss.cache.lock.LockManager;
import org.jboss.cache.notifications.Notifier;
import org.jboss.cache.transaction.GlobalTransaction;
import org.jboss.cache.transaction.TransactionContext;
import org.jboss.cache.transaction.TransactionLog;
import org.jboss.cache.transaction.TransactionTable;
import org.jboss.cache.util.concurrent.ConcurrentHashSet;

/**
 * This interceptor is the new default at the head of all interceptor chains,
 * and makes transactional attributes available to all interceptors in the chain.
 * This interceptor is also responsible for registering for synchronisation on
 * transaction completion.
 *
 * @author <a href="mailto:manik AT jboss DOT org">Manik Surtani (manik AT jboss DOT org)</a>
 * @author <a href="mailto:stevew@jofti.com">Steve Woodcock (stevew@jofti.com)</a>
 * @author <a href="mailto:galder.zamarreno@jboss.com">Galder Zamarreno</a>
 */
public class TxInterceptor extends BaseTransactionalContextInterceptor
{
   protected CommandsFactory commandsFactory;
   protected RPCManager rpcManager;
   private Notifier notifier;
   private InvocationContextContainer invocationContextContainer;
   private ComponentRegistry componentRegistry;
   private ContextFactory contextFactory;
   private TransactionLog transactionLog;

   /**
    * List <Transaction>that we have registered for
    */
   private final Set<Transaction> transactions = new ConcurrentHashSet<Transaction>();
   private final Map<Transaction, GlobalTransaction> rollbackTransactions = new ConcurrentHashMap<Transaction, GlobalTransaction>(16);

   private long prepares = 0;
   private long commits = 0;
   private long rollbacks = 0;
   protected boolean optimistic = false;
   private LockManager lockManager;
   private boolean statsEnabled;

   @Inject
   public void intialize(RPCManager rpcManager, ContextFactory contextFactory,
                         Notifier notifier, InvocationContextContainer icc,
                         TransactionLog transactionLog, CommandsFactory factory,
                         ComponentRegistry componentRegistry, LockManager lockManager)
   {
      this.contextFactory = contextFactory;
      this.transactionLog = transactionLog;
      this.commandsFactory = factory;
      this.rpcManager = rpcManager;
      this.notifier = notifier;
      this.invocationContextContainer = icc;
      this.componentRegistry = componentRegistry;
      this.lockManager = lockManager;
      setStatisticsEnabled(configuration.getExposeManagementStatistics());
   }

   @Override
   public Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      Object result = null;

      // this is a prepare, commit, or rollback.
      GlobalTransaction gtx = ctx.getGlobalTransaction();
      if (trace) log.trace("Got gtx from invocation context " + gtx);

      try
      {
         if (gtx.isRemote())
         {
            result = handleRemotePrepare(ctx, command);
            if (getStatisticsEnabled()) prepares++;
         }
         else
         {
            if (trace) log.trace("received my own message (discarding it)");
            result = null;
         }
      }
      catch (Throwable e)
      {
         ctx.throwIfNeeded(e);
      }

      return result;
   }

   @Override
   public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
   {
      GlobalTransaction gtx = ctx.getGlobalTransaction();

      if (!ctx.getGlobalTransaction().isRemote())
      {
         if (trace) log.trace("received my own message (discarding it)");
         return null;
      }
      try
      {
         if (trace) log.trace("(" + rpcManager.getLocalAddress() + ") call on command [" + command + "]");

         Transaction ltx = txTable.getLocalTransaction(gtx, true);
         // disconnect if we have a current tx associated
         Transaction currentTx = txManager.getTransaction();
         boolean resumeCurrentTxOnCompletion = false;
         try
         {
            if (!ltx.equals(currentTx))
            {
               currentTx = txManager.suspend();
               resumeCurrentTxOnCompletion = true;
               txManager.resume(ltx);
               // make sure we set this in the ctx
               ctx.setTransaction(ltx);
            }
            if (trace) log.trace(" executing commit() with local TX " + ltx + " under global tx " + gtx);
            txManager.commit();
            if (getStatisticsEnabled()) commits++;
         }
         finally
         {
            //resume the old transaction if we suspended it
            if (resumeCurrentTxOnCompletion)
            {
               resumeTransactionOnCompletion(ctx, currentTx);
            }
            // remove from local lists.
            transactions.remove(ltx);
            // this tx has completed.  Clean up in the tx table.
            txTable.remove(gtx, ltx);
         }
         if (trace) log.trace("Finished remote rollback method for " + gtx);
      }
      catch (Throwable throwable)
      {
         ctx.throwIfNeeded(throwable);
      }

      return null;
   }

   @Override
   public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
   {
      GlobalTransaction gtx = ctx.getGlobalTransaction();

      if (!gtx.isRemote())
      {
         if (trace) log.trace("received my own message (discarding it)");
         return null;
      }
      try
      {
         if (trace) log.trace("(" + rpcManager.getLocalAddress() + ") call on command [" + command + "]");
         Transaction ltx = txTable.getLocalTransaction(gtx);
         if (ltx == null)
         {
            log.warn("No local transaction for this remotely originating rollback.  Possibly rolling back before a prepare call was broadcast?");
            txTable.remove(gtx);
            return null;
         }
         // disconnect if we have a current tx associated
         Transaction currentTx = txManager.getTransaction();
         boolean resumeCurrentTxOnCompletion = false;
         try
         {
            if (!ltx.equals(currentTx))
            {
               currentTx = txManager.suspend();
               resumeCurrentTxOnCompletion = true;
               txManager.resume(ltx);
               // make sure we set this in the ctx
               ctx.setTransaction(ltx);
            }
            if (trace) log.trace("executing with local TX " + ltx + " under global tx " + gtx);
            txManager.rollback();
            if (getStatisticsEnabled()) rollbacks++;
         }
         finally
         {
            //resume the old transaction if we suspended it
            if (resumeCurrentTxOnCompletion)
            {
               resumeTransactionOnCompletion(ctx, currentTx);
            }

            // remove from local lists.
            transactions.remove(ltx);

            // this tx has completed.  Clean up in the tx table.
            txTable.remove(gtx, ltx);
         }
         if (trace) log.trace("Finished remote commit/rollback method for " + gtx);
      }
      catch (Throwable throwable)
      {
         ctx.throwIfNeeded(throwable);
      }

      return null;
   }

   @Override
   public Object visitInvalidateCommand(InvocationContext ctx, InvalidateCommand command) throws Throwable
   {
      return invokeNextInterceptor(ctx, command);
   }

   /**
    * Tests if we already have a tx running.  If so, register a sync handler for this method invocation.
    * if not, create a local tx if we're using opt locking.
    *
    * @throws Throwable
    */
   @Override
   public Object handleDefault(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      try
      {
         Object ret = attachGtxAndPassUpChain(ctx, command);

         if (command instanceof WriteCommand && ctx.getTransaction() == null)
            transactionLog.logNoTxWrite((WriteCommand)command);

         return ret;
      }
      catch (Throwable throwable)
      {
         ctx.throwIfNeeded(throwable);
         return null;
      }
   }

   protected Object attachGtxAndPassUpChain(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      Transaction tx = ctx.getTransaction();
      if (tx != null) attachGlobalTransaction(ctx, tx, command);
      return invokeNextInterceptor(ctx, command);
   }

   // ------------------------------------------------------------------------
   // JMX statistics
   // ------------------------------------------------------------------------

   // --------------------------------------------------------------

   /**
    * Handles a remotely originating prepare call, by creating a local transaction for the remote global transaction
    * and replaying modifications in this new local transaction.
    *
    * @param ctx     invocation context
    * @param command prepare command
    * @return result of the prepare, typically a null.
    * @throws Throwable in the event of problems.
    */
   private Object handleRemotePrepare(InvocationContext ctx, PrepareCommand command) throws Throwable
   {
      // the InvocationContextInterceptor would have set this for us
      GlobalTransaction gtx = ctx.getGlobalTransaction();

      // Is there a local transaction associated with GTX?  (not the current tx associated with the thread, which may be
      // in the invocation context
      Transaction ltx = txTable.getLocalTransaction(gtx);
      Transaction currentTx = txManager.getTransaction();

      Object retval = null;
      boolean success = false;
      try
      {
         if (ltx == null)
         {
            if (currentTx != null) txManager.suspend();
            // create a new local transaction
            ltx = createLocalTx();
            // associate this with a global tx
            txTable.put(ltx, gtx);
            if (trace) log.trace("Created new tx for gtx " + gtx);

            if (trace)
            {
               log.trace("Started new local tx as result of remote prepare: local tx=" + ltx + " (status=" + ltx.getStatus() + "), gtx=" + gtx);
            }
         }
         else
         {
            //this should be valid
            if (!TransactionTable.isValid(ltx))
            {
               throw new CacheException("Transaction " + ltx + " not in correct state to be prepared");
            }

            //associate this thread with the local transaction associated with the global transaction, IF the localTx is NOT the current tx.
            if (currentTx == null || !ltx.equals(currentTx))
            {
               if (trace) log.trace("Suspending current tx " + currentTx);
               txManager.suspend();
               txManager.resume(ltx);
            }
         }
         if (trace) log.trace("Resuming existing tx " + ltx + ", global tx=" + gtx);

         // at this point we have a non-null ltx

         // Asssociate the local TX with the global TX. Create new
         // transactionContext for TX in txTable, the modifications
         // below will need this transactionContext to add their modifications
         // under the GlobalTx key
         TransactionContext transactionContext = txTable.get(gtx);
         if (transactionContext == null)
         {
            // create a new transaction transactionContext
            if (trace) log.trace("creating new tx transactionContext");
            transactionContext = contextFactory.createTransactionContext(ltx);
            txTable.put(gtx, transactionContext);
         }

         setTransactionalContext(ltx, gtx, transactionContext, ctx);

         // register a sync handler for this tx.
         registerHandler(ltx, new RemoteSynchronizationHandler(gtx, ltx, transactionContext), ctx);

         success = false;

         // replay modifications
         replayModifications(ctx, ltx, command);

         success = true; // no exceptions were thrown above!!

         // now pass the prepare command up the chain as well.
         if (command.isOnePhaseCommit())
         {
            if (trace)
            {
               log.trace("Using one-phase prepare.  Not propagating the prepare call up the stack until called to do so by the sync handler.");
            }
         }
         else
         {
            transactionLog.logPrepare(command);

            // now pass up the prepare method itself.
            invokeNextInterceptor(ctx, command);
         }
         // JBCACHE-361 Confirm that the transaction is ACTIVE
         assertTxIsStillValid(ltx);
      }
      finally
      {
         // if we are running a one-phase commit, perform a commit or rollback now.
         if (trace) log.trace("Are we running a 1-phase commit? " + command.isOnePhaseCommit());

         if (command.isOnePhaseCommit())
         {
            try
            {
               if (success)
               {
                  ltx.commit();
               }
               else
               {
                  ltx.rollback();
               }
            }
            catch (Throwable t)
            {
               log.error("Commit/rollback failed.", t);
               if (success)
               {
                  // try another rollback...
                  try
                  {
                     log.info("Attempting anotehr rollback");
                     //invokeOnePhaseCommitMethod(globalTransaction, modifications.size() > 0, false);
                     ltx.rollback();
                  }
                  catch (Throwable t2)
                  {
                     log.error("Unable to rollback", t2);
                  }
               }
            }
            finally
            {
               transactions.remove(ltx);// JBAS-298
            }
         }

         txManager.suspend();// suspends ltx - could be null
         // resume whatever else we had going.
         if (currentTx != null) txManager.resume(currentTx);
         if (trace) log.trace("Finished remote prepare " + gtx);
      }

      return retval;
   }

   private ReplicableCommand attachGlobalTransaction(InvocationContext ctx, Transaction tx, VisitableCommand command) throws Throwable
   {
      if (trace)
      {
         log.trace(" local transaction exists - registering global tx if not present for " + Thread.currentThread());
      }
      if (trace)
      {
         GlobalTransaction tempGtx = txTable.get(tx);
         log.trace("Associated gtx in txTable is " + tempGtx);
      }

      // register a sync handler for this tx - only if the globalTransaction is not remotely initiated.
      GlobalTransaction gtx = registerTransaction(tx, ctx);
      if (gtx != null)
      {
         command = replaceGtx(command, gtx);
      }
      else
      {
         // get the current globalTransaction from the txTable.
         gtx = txTable.get(tx);
      }

      // make sure we attach this globalTransaction to the invocation context.
      ctx.setGlobalTransaction(gtx);

      return command;
   }

   /**
    * Replays modifications
    */
   protected void replayModifications(InvocationContext ctx, Transaction ltx, PrepareCommand command) throws Throwable
   {
      try
      {
         // replay modifications
         for (WriteCommand modification : command.getModifications())
         {
            invokeNextInterceptor(ctx, modification);
            assertTxIsStillValid(ltx);
         }
      }
      catch (Throwable th)
      {
         log.error("prepare failed!", th);
         throw th;
      }
   }

   private void resumeTransactionOnCompletion(InvocationContext ctx, Transaction currentTx)
         throws SystemException, InvalidTransactionException
   {
      if (trace) log.trace("Resuming suspended transaction " + currentTx);
      txManager.suspend();
      if (currentTx != null)
      {
         txManager.resume(currentTx);
         ctx.setTransaction(currentTx);
      }
   }

   /**
    * Handles a commit or a rollback.  Called by the synch handler.  Simply tests that we are in the correct tx and
    * passes the meth call up the interceptor chain.
    *
    * @throws Throwable
    */
   @SuppressWarnings("deprecation")
   private Object handleCommitRollback(InvocationContext ctx, VisitableCommand command) throws Throwable
   {
      GlobalTransaction gtx = ctx.getGlobalTransaction();
      Object result;
      VisitableCommand originalCommand = ctx.getCommand();
      ctx.setCommand(command);
      try
      {
         result = invokeNextInterceptor(ctx, command);
      }
      finally
      {
         ctx.setCommand(originalCommand);
         ctx.setMethodCall(null);
      }
      if (trace) log.trace("Finished local commit/rollback method for " + gtx);
      return result;
   }

   // --------------------------------------------------------------
   //   Transaction phase runners
   // --------------------------------------------------------------

   protected PrepareCommand buildPrepareCommand(GlobalTransaction gtx, List modifications, boolean onePhaseCommit)
   {
      return commandsFactory.buildPrepareCommand(gtx, modifications, rpcManager.getLocalAddress(), onePhaseCommit);
   }

   /**
    * creates a commit()
    */
   protected void runCommitPhase(InvocationContext ctx, GlobalTransaction gtx, List modifications, boolean onePhaseCommit)
   {
      try
      {
         if (trace) log.trace("Running commit for " + gtx);

         VisitableCommand commitCommand = onePhaseCommit ? buildPrepareCommand(gtx, modifications, true)
                                                         : commandsFactory.buildCommitCommand(gtx);


         handleCommitRollback(ctx, commitCommand);

         if (onePhaseCommit)
         {
            transactionLog.logOnePhaseCommit(gtx, modifications);
         }
         else
         {
            transactionLog.logCommit(gtx);
         }
      }
      catch (Throwable e)
      {
         log.warn("Commit failed.  Clearing stale locks.");
         try
         {
            cleanupStaleLocks(ctx);
         }
         catch (RuntimeException re)
         {
            log.error("Unable to clear stale locks", re);
            throw re;
         }
         catch (Throwable e2)
         {
            log.error("Unable to clear stale locks", e2);
            throw new RuntimeException(e2);
         }
         if (e instanceof RuntimeException)
         {
            throw (RuntimeException) e;
         }
         else
         {
            throw new RuntimeException("Commit failed.", e);
         }
      }
   }

   protected void cleanupStaleLocks(InvocationContext ctx) throws Throwable
   {
      TransactionContext transactionContext = ctx.getTransactionContext();
      if (transactionContext != null) lockManager.unlock(ctx);
   }

   /**
    * creates a rollback()
    */
   protected void runRollbackPhase(InvocationContext ctx, GlobalTransaction gtx, Transaction tx)
   {
      try
      {
         // JBCACHE-457
         VisitableCommand rollbackCommand = commandsFactory.buildRollbackCommand(gtx);
         if (trace) log.trace(" running rollback for " + gtx);

         transactionLog.rollback(gtx);

         //JBCACHE-359 Store a lookup for the globalTransaction so a listener
         // callback can find it
         rollbackTransactions.put(tx, gtx);

         handleCommitRollback(ctx, rollbackCommand);
      }
      catch (Throwable e)
      {
         log.warn("Rollback had a problem", e);
      }
      finally
      {
         rollbackTransactions.remove(tx);
      }
   }

   private boolean isOnePhaseCommit()
   {
      if (!configuration.getCacheMode().isSynchronous() && !optimistic)
      {
         // this is a REPL_ASYNC call - do 1-phase commit.  break!
         if (trace) log.trace("This is a REPL_ASYNC call (1 phase commit) - do nothing for beforeCompletion()");
         return true;
      }
      return false;
   }

   /**
    * Handles a local prepare - invoked by the sync handler.  Tests if the current tx matches the gtx passed in to the
    * method call and passes the prepare() call up the chain.
    */
   @SuppressWarnings("deprecation")
   public Object runPreparePhase(InvocationContext ctx, GlobalTransaction gtx, List<WriteCommand> modifications) throws Throwable
   {
      // running a 2-phase commit.
      PrepareCommand prepareCommand = buildPrepareCommand(gtx, modifications, false);

      transactionLog.logPrepare(prepareCommand);

      Object result;

      // Is there a local transaction associated with GTX ?
      Transaction ltx = ctx.getTransaction();

      //if ltx is not null and it is already running
      Transaction currentTransaction = txManager.getTransaction();
      if (currentTransaction != null && ltx != null && currentTransaction.equals(ltx))
      {
         VisitableCommand originalCommand = ctx.getCommand();
         ctx.setCommand(prepareCommand);
         try
         {
            result = invokeNextInterceptor(ctx, prepareCommand);
         }
         finally
         {
            ctx.setCommand(originalCommand);
            ctx.setMethodCall(null);
         }
      }
      else
      {
         log.warn("Local transaction does not exist or does not match expected transaction " + gtx);
         throw new CacheException(" local transaction " + ltx + " does not exist or does not match expected transaction " + gtx);
      }
      return result;
   }

   // --------------------------------------------------------------
   //   Private helper methods
   // --------------------------------------------------------------

   protected void assertTxIsStillValid(Transaction tx)
   {
      if (!TransactionTable.isActive(tx))
      {
         try
         {
            throw new ReplicationException("prepare() failed -- local transaction status is not STATUS_ACTIVE; is " + tx.getStatus());
         }
         catch (SystemException e)
         {
            throw new ReplicationException("prepare() failed -- local transaction status is not STATUS_ACTIVE; Unable to retrieve transaction status.");
         }
      }
   }

   /**
    * Creates a gtx (if one doesnt exist), a sync handler, and registers the tx.
    */
   private GlobalTransaction registerTransaction(Transaction tx, InvocationContext ctx) throws Exception
   {
      // we have ascertained that the current thread *is* associated with a transaction.  We need to make sure the
      // transaction is in a valid state before moving on, and throwing an exception if not.
      boolean txValid = TransactionTable.isValid(tx);
      if (!txValid)
      {
         throw new IllegalStateException("Transaction " + tx + " is not in a valid state to be invoking cache operations on.");
      }

      GlobalTransaction gtx;

      if (transactions.add(tx))
      {
         gtx = txTable.getCurrentTransaction(tx, true);
         TransactionContext transactionContext;
         if (ctx.getGlobalTransaction() == null)
         {
            ctx.setGlobalTransaction(gtx);
            transactionContext = txTable.get(gtx);
            ctx.setTransactionContext(transactionContext);
         }
         else
         {
            transactionContext = ctx.getTransactionContext();
         }
         if (gtx.isRemote())
         {
            // should be no need to register a handler since this a remotely initiated globalTransaction
            if (trace) log.trace("is a remotely initiated gtx so no need to register a tx for it");
         }
         else
         {
            if (trace) log.trace("Registering sync handler for tx " + tx + ", gtx " + gtx);

            // see the comment in the LocalSyncHandler for the last isOriginLocal param.
            LocalSynchronizationHandler myHandler = new LocalSynchronizationHandler(gtx, tx, transactionContext, !ctx.isOriginLocal());
            registerHandler(tx, myHandler, ctx);
         }
      }
      else if ((gtx = rollbackTransactions.get(tx)) != null)
      {
         if (trace) log.trace("Transaction " + tx + " is already registered and is rolling back.");
      }
      else
      {
         if (trace) log.trace("Transaction " + tx + " is already registered.");
      }
      return gtx;
   }

   /**
    * Registers a sync hander against a tx.
    */
   private void registerHandler(Transaction tx, Synchronization handler, InvocationContext ctx) throws Exception
   {
      OrderedSynchronizationHandler orderedHandler = ctx.getTransactionContext().getOrderedSynchronizationHandler(); //OrderedSynchronizationHandler.getInstance(tx);

      if (trace) log.trace("registering for TX completion: SynchronizationHandler(" + handler + ")");

      orderedHandler.registerAtHead(handler);// needs to be invoked first on TX commit

      notifier.notifyTransactionRegistered(tx, ctx);
   }

   /**
    * Replaces the global transaction in a VisitableCommand with a new global transaction passed in.
    */
   private VisitableCommand replaceGtx(VisitableCommand command, final GlobalTransaction gtx) throws Throwable
   {
      command.acceptVisitor(null, new AbstractVisitor()
      {
         @Override
         public Object visitPutDataMapCommand(InvocationContext ctx, PutDataMapCommand command) throws Throwable
         {
            command.setGlobalTransaction(gtx);
            return null;
         }

         @Override
         public Object visitPutKeyValueCommand(InvocationContext ctx, PutKeyValueCommand command) throws Throwable
         {
            command.setGlobalTransaction(gtx);
            return null;
         }

         @Override
         public Object visitClearDataCommand(InvocationContext ctx, ClearDataCommand command) throws Throwable
         {
            command.setGlobalTransaction(gtx);
            return null;
         }

         @Override
         public Object visitRemoveNodeCommand(InvocationContext ctx, RemoveNodeCommand command) throws Throwable
         {
            command.setGlobalTransaction(gtx);
            return null;
         }

         @Override
         public Object visitRemoveKeyCommand(InvocationContext ctx, RemoveKeyCommand command) throws Throwable
         {
            command.setGlobalTransaction(gtx);
            return null;
         }

         @Override
         public Object visitCommitCommand(InvocationContext ctx, CommitCommand command) throws Throwable
         {
            command.setGlobalTransaction(gtx);
            return null;
         }

         @Override
         public Object visitPrepareCommand(InvocationContext ctx, PrepareCommand command) throws Throwable
         {
            command.setGlobalTransaction(gtx);
            return null;
         }

         @Override
         public Object visitRollbackCommand(InvocationContext ctx, RollbackCommand command) throws Throwable
         {
            command.setGlobalTransaction(gtx);
            return null;
         }

         @Override
         public Object visitOptimisticPrepareCommand(InvocationContext ctx, OptimisticPrepareCommand command) throws Throwable
         {
            command.setGlobalTransaction(gtx);
            return null;
         }
      });
      return command;
   }

   /**
    * Creates and starts a local tx
    *
    * @throws Exception
    */
   protected Transaction createLocalTx() throws Exception
   {
      if (trace)
      {
         log.trace("Creating transaction for thread " + Thread.currentThread());
      }
      Transaction localTx;
      if (txManager == null) throw new Exception("Failed to create local transaction; TransactionManager is null");
      txManager.begin();
      localTx = txManager.getTransaction();
      return localTx;
   }

   // ------------------------------------------------------------------------
   // Synchronization classes
   // ------------------------------------------------------------------------

   // this controls the whole transaction

   private class RemoteSynchronizationHandler implements Synchronization
   {
      Transaction tx = null;
      GlobalTransaction gtx = null;
      List<WriteCommand> modifications = null;
      TransactionContext transactionContext = null;
      protected InvocationContext ctx; // the context for this call.

      RemoteSynchronizationHandler(GlobalTransaction gtx, Transaction tx, TransactionContext entry)
      {
         this.gtx = gtx;
         this.tx = tx;
         this.transactionContext = entry;
      }

      public void beforeCompletion()
      {
         if (trace) log.trace("Running beforeCompletion on gtx " + gtx);

         if (transactionContext == null)
         {
            log.error("Transaction has a null transaction entry - beforeCompletion() will fail.");
            throw new IllegalStateException("cannot find transaction entry for " + gtx);
         }

         modifications = transactionContext.getModifications();
         ctx = invocationContextContainer.get();
         setTransactionalContext(tx, gtx, transactionContext, ctx);

         if (ctx.isOptionsUninitialised() && transactionContext.getOption() != null)
         {
            ctx.setOptionOverrides(transactionContext.getOption());
         }

         assertCanContinue();

         ctx.setOriginLocal(false);
      }

      // this should really not be done here -
      // it is supposed to be post commit not actually run the commit
      public void afterCompletion(int status)
      {
         // could happen if a rollback is called and beforeCompletion() doesn't get called.
         if (ctx == null)
         {
            ctx = invocationContextContainer.get();
            setTransactionalContext(tx, gtx, transactionContext, ctx);

            if (ctx.isOptionsUninitialised() && transactionContext != null && transactionContext.getOption() != null)
            {
               // use the options from the transaction entry instead
               ctx.setOptionOverrides(transactionContext.getOption());
            }
         }

         try
         {
            assertCanContinue();

            try
            {
               if (txManager.getTransaction() != null && !txManager.getTransaction().equals(tx)) txManager.resume(tx);
            }
            catch (Exception e)
            {
               log.error("afterCompletion error: " + status, e);
            }

            if (trace) log.trace("calling aftercompletion for " + gtx);

            // set any transaction wide options as current for this thread.
            if (transactionContext != null)
            {
               // this should ideally be set in beforeCompletion(), after compacting the list.
               if (modifications == null) modifications = transactionContext.getModifications();
               boolean isSuppressEventNotification = ctx.getOptionOverrides().isSuppressEventNotification();
               ctx.setOptionOverrides(transactionContext.getOption());
               ctx.getOptionOverrides().setSuppressEventNotification(isSuppressEventNotification);
            }
            if (tx != null) transactions.remove(tx);

            switch (status)
            {
               case Status.STATUS_COMMITTED:
                  boolean onePhaseCommit = isOnePhaseCommit();
                  if (trace) log.trace("Running commit phase.  One phase? " + onePhaseCommit);
                  runCommitPhase(ctx, gtx, modifications, onePhaseCommit);
                  log.trace("Finished commit phase");
                  break;
               case Status.STATUS_UNKNOWN:
                  log.warn("Received JTA STATUS_UNKNOWN in afterCompletion()!  XA resources may not be in sync.  The app should manually clean up resources at this point.");
               case Status.STATUS_MARKED_ROLLBACK:
               case Status.STATUS_ROLLEDBACK:
                  log.trace("Running rollback phase");
                  runRollbackPhase(ctx, gtx, tx);
                  log.trace("Finished rollback phase");
                  break;

               default:
                  throw new IllegalStateException("illegal status: " + status);
            }
         }
         catch (Exception th)
         {
            log.trace("Caught exception ", th);

         }
         finally
         {
            // clean up the tx table
            txTable.remove(gtx);
            txTable.remove(tx);
            setTransactionalContext(null, null, null, ctx);
            cleanupInternalState();
         }
      }

      private void assertCanContinue()
      {
         if (!componentRegistry.invocationsAllowed(true) && (ctx.getOptionOverrides() == null || !ctx.getOptionOverrides().isSkipCacheStatusCheck()))
         {
            throw new IllegalStateException("Cache not in STARTED state!");
         }
      }

      /**
       * Cleans out (nullifies) member variables held by the sync object for easier gc.  Could be (falsely) seen as a mem
       * leak if the TM implementation hangs on to the synchronizations for an unnecessarily long time even after the tx
       * completes.  See JBCACHE-1007.
       */
      private void cleanupInternalState()
      {
         tx = null;
         gtx = null;
         modifications = null;
         if (transactionContext != null) transactionContext.reset();
         transactionContext = null;
      }

      @Override
      public String toString()
      {
         return "TxInterceptor.RemoteSynchronizationHandler(gtx=" + gtx + ", tx=" + getTxAsString() + ")";
      }

      protected String getTxAsString()
      {
         // JBCACHE-1114 -- don't call toString() on tx or it can lead to stack overflow
         if (tx == null)
         {
            return null;
         }

         return tx.getClass().getName() + "@" + System.identityHashCode(tx);
      }
   }

   private class LocalSynchronizationHandler extends RemoteSynchronizationHandler
   {
      private boolean localRollbackOnly = true;
      // a VERY strange situation where a tx has remote origins, but since certain buddy group org methods perform local
      // cleanups even when remotely triggered, and optimistic locking is used, you end up with an implicit local tx.
      // This is STILL remotely originating though and this needs to be made explicit here.
      // this can be checked by inspecting the InvocationContext.isOriginLocal() at the time of registering the sync.
      private boolean remoteLocal = false;
      private Option originalOptions, transactionalOptions;

      /**
       * A Synchronization for locally originating txs.
       * <p/>
       * a VERY strange situation where a tx has remote origins, but since certain buddy group org methods perform local
       * cleanups even when remotely triggered, and optimistic locking is used, you end up with an implicit local tx.
       * This is STILL remotely originating though and this needs to be made explicit here.
       * this can be checked by inspecting the InvocationContext.isOriginLocal() at the time of registering the sync.
       *
       * @param gtx
       * @param tx
       * @param remoteLocal
       */
      LocalSynchronizationHandler(GlobalTransaction gtx, Transaction tx, TransactionContext transactionContext, boolean remoteLocal)
      {
         super(gtx, tx, transactionContext);
         this.remoteLocal = remoteLocal;
      }

      @Override
      public void beforeCompletion()
      {
         super.beforeCompletion();
         ctx.setOriginLocal(!remoteLocal); // this is the LOCAL sync handler after all!
         // fetch the modifications before the transaction is committed
         // (and thus removed from the txTable)
         setTransactionalContext(tx, gtx, transactionContext, ctx);
         if (!transactionContext.hasModifications())
         {
            if (trace) log.trace("No modifications in this tx.  Skipping beforeCompletion()");
            modifications = Collections.emptyList();
            return;
         }
         else
         {
            modifications = transactionContext.getModifications();
         }

         // set any transaction wide options as current for this thread, caching original options that would then be reset
         originalOptions = ctx.getOptionOverrides();
         transactionalOptions = transactionContext.getOption();
         transactionalOptions.setSuppressEventNotification(originalOptions.isSuppressEventNotification());
         ctx.setOptionOverrides(transactionalOptions);

         try
         {
            switch (tx.getStatus())
            {
               // if we are active or preparing then we can go ahead
               case Status.STATUS_ACTIVE:
               case Status.STATUS_PREPARING:
                  // run a prepare call.

                  Object result = isOnePhaseCommit() ? null : runPreparePhase(ctx, gtx, modifications);

                  if (result instanceof Throwable)
                  {
                     if (log.isDebugEnabled())
                     {
                        log.debug("Transaction needs to be rolled back - the cache returned an instance of Throwable for this prepare call (tx=" + tx + " and gtx=" + gtx + ")", (Throwable) result);
                     }
                     tx.setRollbackOnly();
                     throw (Throwable) result;
                  }
                  break;
               default:
                  throw new CacheException("transaction " + tx + " in status " + tx.getStatus() + " unable to start transaction");
            }
         }
         catch (Throwable t)
         {
            if (log.isWarnEnabled()) log.warn("Caught exception, will now set transaction to roll back", t);
            try
            {
               tx.setRollbackOnly();
            }
            catch (SystemException se)
            {
               throw new RuntimeException("setting tx rollback failed ", se);
            }
            if (t instanceof RuntimeException)
            {
               throw (RuntimeException) t;
            }
            else
            {
               throw new RuntimeException("", t);
            }
         }
         finally
         {
            localRollbackOnly = false;
            setTransactionalContext(null, null, null, ctx);
            ctx.setOptionOverrides(originalOptions);
         }
      }

      @Override
      public void afterCompletion(int status)
      {
         // could happen if a rollback is called and beforeCompletion() doesn't get called.
         if (ctx == null) ctx = invocationContextContainer.get();
         ctx.setLocalRollbackOnly(localRollbackOnly);
         setTransactionalContext(tx, gtx, transactionContext, ctx);
         if (transactionalOptions != null) ctx.setOptionOverrides(transactionalOptions);
         try
         {
            super.afterCompletion(status);
         }
         finally
         {
            ctx.setOptionOverrides(originalOptions);
         }
      }

      @Override
      public String toString()
      {
         return "TxInterceptor.LocalSynchronizationHandler(gtx=" + gtx + ", tx=" + getTxAsString() + ")";
      }
   }

   @ManagedOperation
   public void resetStatistics()
   {
      prepares = 0;
      commits = 0;
      rollbacks = 0;
   }

   @ManagedOperation
   public Map<String, Object> dumpStatistics()
   {
      Map<String, Object> retval = new HashMap<String, Object>(3);
      retval.put("Prepares", prepares);
      retval.put("Commits", commits);
      retval.put("Rollbacks", rollbacks);
      return retval;
   }

   @ManagedAttribute
   public boolean getStatisticsEnabled()
   {
      return this.statsEnabled;
   }

   @ManagedAttribute
   public void setStatisticsEnabled(boolean enabled)
   {
      this.statsEnabled = enabled;
   }

   @ManagedAttribute(description = "number of transaction prepares")
   public long getPrepares()
   {
      return prepares;
   }

   @ManagedAttribute(description = "number of transaction commits")
   public long getCommits()
   {
      return commits;
   }

   @ManagedAttribute(description = "number of transaction rollbacks")
   public long getRollbacks()
   {
      return rollbacks;
   }

   @ManagedAttribute(name = "numberOfSyncsRegistered", writable = false, description = "number of transaction synchronizations currently registered")
   public int getNumberOfSyncsRegistered()
   {
      return transactions.size();
   }
}
